package com.gnusl.wow.Activities;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Enums.SocialType;
import com.gnusl.wow.Models.RegisterParams;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.APIUtils;
import com.gnusl.wow.Utils.LocaleManager;
import com.gnusl.wow.Utils.SharedPreferencesUtils;
import com.gnusl.wow.Views.FontedButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.Callable;

import retrofit2.Call;

import static android.widget.Toast.LENGTH_LONG;

public class LoginActivity extends AppCompatActivity implements ConnectionDelegate {

    // facebook
    private CallbackManager callbackManager;

    // Google
    private final int GOOGLE_LOGIN_REQUEST = 900;

    // twitter
    private TwitterAuthClient mTwitterAuthClient;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // go to main activity
        findViewById(R.id.wow_login_icon).setOnClickListener(v -> {

            /*startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();*/

        });

        findViewById(R.id.facebook_button).setOnClickListener(v -> {

            startLoginWithFacebook();
        });

        findViewById(R.id.google_button).setOnClickListener(v -> {

            startLoginWithGoogle();
        });


        findViewById(R.id.twitter_button).setOnClickListener(v -> {

            startLoginWithTwitter();
        });

        findViewById(R.id.instagram_button).setOnClickListener(v -> {

            signInWithInstagram();
        });

        findViewById(R.id.accout_button).setOnClickListener(v -> {

            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        // initialize facebook
        initializeFacebookSDK();


    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check Instagram callback
        checkForInstagramData();
    }

    // Facebook Methods
    private void initializeFacebookSDK() {

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        Log.d("FACEBOOK ", "onSuccess");

                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());

                                        // Application code
                                        try {
                                            JSONObject pictureJson = new JSONObject(object.getString("picture"));
                                            JSONObject dataJson = new JSONObject(pictureJson.getString("data"));
                                            String url = dataJson.getString("url");

                                            String name = object.getString("name");
                                            String id = object.getString("id");
                                            String email = object.getString("email");

                                            LoaderPopUp.dismissLoader();

                                            // prepare user credential
                                            RegisterParams params = new RegisterParams();
                                            params.setName(name);
                                            params.setEmail(email);
                                            params.setImage(url);
                                            params.setSocial_id(id);
                                            params.setSocial_type(SocialType.Facebook.name().toLowerCase());
                                            params.setFcm_token(FirebaseInstanceId.getInstance().getToken());

                                            Log.d("FACEBOOK name", name);
                                            Log.d("FACEBOOK email", email);
                                            Log.d("FACEBOOK url", url);
                                            Log.d("FACEBOOK id", id);
                                            Log.d("FACEBOOK firebase", FirebaseInstanceId.getInstance().getToken());

                                            // send register
                                            APIConnectionNetwork.LoginBySocial(params, LoginActivity.this);

                                            // progress
                                            LoaderPopUp.show(LoginActivity.this);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "name,picture,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code

                        Toast.makeText(LoginActivity.this, "end proccess by facebook try again", LENGTH_LONG).show();

                        LoaderPopUp.dismissLoader();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code

                        Toast.makeText(LoginActivity.this, "Facebook Error try again", LENGTH_LONG).show();

                        LoaderPopUp.dismissLoader();
                    }
                });
    }

    private void startLoginWithFacebook() {

        // login with facebook
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));

        // progress
        LoaderPopUp.show(this);

    }

    // Google Methods
    @SuppressLint("RestrictedApi")
    private void startLoginWithGoogle() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_LOGIN_REQUEST);

    }

    void handleGoogleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            try {
                GoogleSignInAccount account = result.getSignInAccount();
                if (account != null) {

                    Uri photoUrl = account.getPhotoUrl();
                    // send request
                    // prepare user credintial
                    RegisterParams params = new RegisterParams();
                    params.setName(account.getDisplayName());
                    params.setEmail(account.getEmail());

                    if (photoUrl != null)
                        params.setImage(photoUrl.toString());

                    params.setSocial_id(String.valueOf(account.getId()));
                    params.setSocial_type(SocialType.Google.name().toLowerCase());
                    params.setFcm_token(FirebaseInstanceId.getInstance().getToken());

                    // send register
                    APIConnectionNetwork.LoginBySocial(params, LoginActivity.this);

                    // progress
                    LoaderPopUp.show(this);


                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {
            Log.w("GOOGLE ", CommonStatusCodes.getStatusCodeString(result.getStatus().getStatusCode()) + "");
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();

        }
    }


    //Twitter Methods
    private void startLoginWithTwitter() {

        mTwitterAuthClient = new TwitterAuthClient();
        mTwitterAuthClient.authorize(this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                Log.d("TWITTER ", "SUCCESS");
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken twitterAuthToken = session.getAuthToken();
                Log.d("TWITTER ", "SUCCESS1");
                String token = twitterAuthToken.token;
                String secret = twitterAuthToken.secret;
                loginTwitter(session);
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
                Log.d("TWITTER ", e.toString());
            }
        });


    }

    private void loginTwitter(TwitterSession session) {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        AccountService accountService = twitterApiClient.getAccountService();
        Call<User> call = accountService.verifyCredentials(true, true, true);

        call.enqueue(new Callback<User>() {
            @Override
            public void success(Result<com.twitter.sdk.android.core.models.User> result) {
                User user = result.data;

                Log.d("TWITTER ", "SUCCESS2");

                // handle image size
                String comingImageUrl = user.profileImageUrl;
                String image_url = comingImageUrl.replaceAll("_normal", "");

                // send to api

                // prepare user credintal
                RegisterParams params = new RegisterParams();
                params.setName(user.name);
                params.setEmail(user.email);
                params.setImage(image_url);
                params.setSocial_id(String.valueOf(user.getId()));
                params.setSocial_type(SocialType.Twitter.name().toLowerCase());
                params.setFcm_token(FirebaseInstanceId.getInstance().getToken());

                // send register
                APIConnectionNetwork.LoginBySocial(params, LoginActivity.this);

                // progress
                LoaderPopUp.show(LoginActivity.this);


            }

            @Override
            public void failure(TwitterException exception) {

                Log.d("TWITTER ", exception.toString());
            }
        });
    }

    // Instagram
    private void signInWithInstagram() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https")
                .authority("api.instagram.com")
                .appendPath("oauth")
                .appendPath("authorize")
                .appendQueryParameter("client_id", "3709c486cf504bc5ab94a404d4568914")
                .appendQueryParameter("redirect_uri", "sociallogin://redirect")
                .appendQueryParameter("response_type", "token");
        final Intent browser = new Intent(Intent.ACTION_VIEW, uriBuilder.build());
        startActivity(browser);
    }

    private void checkForInstagramData() {
        final Uri data = this.getIntent().getData();
        if (data != null && data.getScheme().equals("sociallogin") && data.getFragment() != null) {
            final String accessToken = data.getFragment().replaceFirst("access_token=", "");
            if (accessToken != null) {
                handleInstagramSignInResult(new Callable<Void>() {
                    @Override
                    public Void call() {
                        // Do nothing, just throw the access token away.
                        return null;
                    }
                });
            } else {
                handleInstagramSignInResult(null);
            }
        }
    }

    private void handleInstagramSignInResult(Callable<Void> logout) {
        if (logout == null) {
            /* Login error */
            Toast.makeText(getApplicationContext(), "Login Instagram Error", Toast.LENGTH_SHORT).show();
        } else {
            /* Login success */
            Toast.makeText(getApplicationContext(), "Login Instagram Success", Toast.LENGTH_SHORT).show();
          /*  Application.getInstance().setLogoutCallable(logout);
            startActivity(new Intent(this, LoggedInActivity.class));*/
        }
    }

    @Override
    public void onConnectionFailure() {

        Toast.makeText(this, "your credential incorrect", LENGTH_LONG).show();

        LoaderPopUp.dismissLoader();
    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(this, "Error Connection try again", LENGTH_LONG).show();

        LoaderPopUp.dismissLoader();
    }

    @Override
    public void onConnectionSuccess(String response) {


    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

        // parse token
        if (jsonObject.has("token")) {

            try {
                SharedPreferencesUtils.saveToken(jsonObject.getString("token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        // parse user
        if (jsonObject.has("user")) {

            try {
                SharedPreferencesUtils.saveUser(com.gnusl.wow.Models.User.newInstance(jsonObject.getJSONObject("user")));

                // go to main
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                finishAffinity();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        LoaderPopUp.dismissLoader();

    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 140) {
            if (resultCode == -1) {
                mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == GOOGLE_LOGIN_REQUEST) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleResult(result);
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

}
