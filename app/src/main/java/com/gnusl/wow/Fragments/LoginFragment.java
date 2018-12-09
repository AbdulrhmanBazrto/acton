package com.gnusl.wow.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Activities.MainActivity;
import com.gnusl.wow.Activities.RegisterActivity;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Enums.FragmentTags;
import com.gnusl.wow.Models.CountryCode;
import com.gnusl.wow.Models.RegisterParams;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.APIUtils;
import com.gnusl.wow.Utils.CountryUtils;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class LoginFragment extends Fragment implements ConnectionDelegate {

    private View inflatedView;

    private EditText edEmailOrPhone, edPassword;
    private View lineEmailOrPhone,linePassword;
    private TextView loginBtn;
    private ProgressDialog progressDialog;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {

        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_login, container, false);

        findViews(inflatedView);

        return inflatedView;
    }

    private void findViews(View rootView) {

        edEmailOrPhone = (EditText) rootView.findViewById(R.id.ed_email_or_phone);
        edPassword = (EditText) rootView.findViewById(R.id.ed_password);
        lineEmailOrPhone = rootView.findViewById(R.id.email_underline);
        linePassword = rootView.findViewById(R.id.password_underline);
        loginBtn = rootView.findViewById(R.id.login_btn);

        changeTextListeners();

        loginBtn.setOnClickListener(v -> handleLoginValidation());
    }

    private void changeTextListeners() {

        edEmailOrPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                lineEmailOrPhone.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.charcoal_grey_20));
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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void handleLoginValidation() {

        if (edEmailOrPhone.getText().toString().isEmpty()) {

            lineEmailOrPhone.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
            edEmailOrPhone.setError("email or phone is required");

        }else if (edPassword.getText().toString().isEmpty()) {

            linePassword.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
            edPassword.setError("password is required");

        } else
            sendLoginRequest();
    }

    private void sendLoginRequest(){

        // send request
        APIConnectionNetwork.Login(edEmailOrPhone.getText().toString(), "", edPassword.getText().toString(),FirebaseInstanceId.getInstance().getToken(), this);

        // make progress dialog
        this.progressDialog = ProgressDialog.show(getContext(), "", "Please Wait for register..");

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

        if (progressDialog != null)
            progressDialog.dismiss();

        // APIUtils.parseNewUserType(response);

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        editor.putBoolean("login", true);
        editor.apply();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);

        getActivity().finishAffinity();

    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

    }

}
