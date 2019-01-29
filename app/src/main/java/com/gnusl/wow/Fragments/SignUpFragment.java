package com.gnusl.wow.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Activities.MainActivity;
import com.gnusl.wow.Activities.RegisterActivity;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.RegisterParams;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.APIUtils;
import com.gnusl.wow.Utils.SharedPreferencesUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rilixtech.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class SignUpFragment extends Fragment implements View.OnClickListener, ConnectionDelegate {

    //region Variables
    EditText edFullName, edEmail, edPassword;
    AppCompatEditText edMobile;
    CountryCodePicker countryCodePicker;
    View lineName, lineEmail, lineMobile, linePassword;
    TextView tvErrorName, tvErrorEmail, tvErrorMobile, tvErrorPassword, SignUpBtn;
    Button btn;
    RegisterActivity registerActivity;
    View containerView;
    long birthdate = -1;
    String code;
    private ProgressDialog progressDialog;
    //endregion

    private View inflatedView;

    public SignUpFragment() {
    }

    public static SignUpFragment newInstance() {

        return new SignUpFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        findViews(inflatedView);

        return inflatedView;
    }

    private void findViews(View rootView) {
        edFullName = rootView.findViewById(R.id.ed_full_name);
        edEmail = rootView.findViewById(R.id.ed_email);
        edMobile = rootView.findViewById(R.id.ed_mobile);
        countryCodePicker = rootView.findViewById(R.id.country_code_picker);
        edPassword = rootView.findViewById(R.id.ed_birthday);
        tvErrorName = rootView.findViewById(R.id.full_name_error);
        tvErrorEmail = rootView.findViewById(R.id.email_error);
        tvErrorMobile = rootView.findViewById(R.id.mobile_error);
        tvErrorPassword = rootView.findViewById(R.id.birthday_error);
        lineName = rootView.findViewById(R.id.full_name_underline);
        lineEmail = rootView.findViewById(R.id.email_underline);
        lineMobile = rootView.findViewById(R.id.mobile_underline);
        linePassword = rootView.findViewById(R.id.birthday_underline);
        containerView = rootView.findViewById(R.id.container);
        SignUpBtn = rootView.findViewById(R.id.sign_up_btn);

        countryCodePicker.registerPhoneNumberTextView(edMobile);

        changeTextListeners();

        SignUpBtn.setOnClickListener(v->handleRegisterValidation());
    }

    private void changeTextListeners() {

        edFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                lineName.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.charcoal_grey_20));
                tvErrorName.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lineEmail.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.charcoal_grey_20));
                tvErrorEmail.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                lineMobile.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.charcoal_grey_20));
                tvErrorMobile.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                linePassword.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.charcoal_grey_20));
                tvErrorPassword.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void handleRegisterValidation() {

        if (edFullName.getText().toString().isEmpty()) {

            lineName.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
            edFullName.setError("username is required");

        }else if (edEmail.getText().toString().isEmpty()) {

            lineEmail.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
            edEmail.setError("email is required");

        } else if (!APIUtils.isValidEmail(edEmail.getText().toString())) {

            lineEmail.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
            edEmail.setError("email isn't correct");

        }else if (edMobile.getText().toString().isEmpty()) {

            lineMobile.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
            edMobile.setError("mobile is required");

        }else if (edPassword.getText().toString().isEmpty()) {

            linePassword.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
            edPassword.setError("password is required");

        } else
            InitRegisterRequest();
    }

    private void InitRegisterRequest(){

        // init params
        RegisterParams params=new RegisterParams();
        params.setName(edFullName.getText().toString());
        params.setEmail(edEmail.getText().toString());
        params.setPassword(edPassword.getText().toString());
        params.setPhone(countryCodePicker.getSelectedCountryCode()+edMobile.getText().toString());
        params.setFcm_token(FirebaseInstanceId.getInstance().getToken());
       // params.setLat(String.valueOf(locationUtils.getLatitude()));
       // params.setLon(String.valueOf(locationUtils.getLongitude()));

        // send request
        APIConnectionNetwork.RegisterNewUser(params,this);

        // make progress dialog
        this.progressDialog = ProgressDialog.show(getContext(), "", "Please Wait for register..");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof RegisterActivity)
            registerActivity= (RegisterActivity) context;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailure() {

        Toast.makeText(getContext(), "your credential incorrect", LENGTH_LONG).show();

        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(getContext(), "Error Connection try again", LENGTH_LONG).show();

        if (progressDialog != null)
            progressDialog.dismiss();
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
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

                getActivity().finishAffinity();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (progressDialog != null)
            progressDialog.dismiss();

    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

    }
}

