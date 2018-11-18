package com.gnusl.wow.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.gnusl.wow.Enums.SocialType;
import com.gnusl.wow.Models.RegisterParams;
import com.gnusl.wow.R;
import com.gnusl.wow.Views.FontedButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static android.widget.Toast.LENGTH_LONG;

public class LoginActivity extends AppCompatActivity {

    AppCompatImageView facebookBtn;

    private ProgressDialog progressDialog;

    // facebook
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.wow_login_icon).setOnClickListener(v->{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }
            },0);
        });


        facebookBtn=findViewById(R.id.facebook_button);


        facebookBtn.setOnClickListener(v->{

           // startLoginWithFacebook();
        });


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        Log.d("FACEBOOK ","onSuccess");

                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());

                                        // Application code
                                        try {
                                            JSONObject pictureJson=new JSONObject(object.getString("picture"));
                                            JSONObject dataJson=new JSONObject(pictureJson.getString("data"));
                                            String url=dataJson.getString("url");

                                            String name = object.getString("name");
                                            String id = object.getString("id");
                                            String email = object.getString("email");

                                            if (progressDialog != null)
                                                progressDialog.dismiss();

                                            // prepare user credential
                                            RegisterParams params=new RegisterParams();
                                            params.setName(name);
                                            params.setEmail(email);
                                            params.setImage(url);
                                            params.setSocial_id(id);
                                            params.setFcm_token(FirebaseInstanceId.getInstance().getToken());
                                            params.setSocial_type(SocialType.Facebook.name().toLowerCase());

                                            // send register


                                            // progress
                                            progressDialog = ProgressDialog.show(LoginActivity.this, "", "please wait send your credential to server..");

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

                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code

                        Toast.makeText(LoginActivity.this, "Facebook Error try again", LENGTH_LONG).show();

                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });

    }

    // Facebook
    private void startLoginWithFacebook(){

        // login with facebook
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","user_friends","email"));

        // progress
        // make progress dialog
        this.progressDialog = ProgressDialog.show(this, "", "Login with facebook account..");

    }
}
