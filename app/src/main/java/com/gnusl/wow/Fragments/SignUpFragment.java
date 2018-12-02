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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Activities.LoginActivity;
import com.gnusl.wow.Activities.MainActivity;
import com.gnusl.wow.Activities.RegisterActivity;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.CountryCode;
import com.gnusl.wow.Models.RegisterParams;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.APIUtils;
import com.gnusl.wow.Utils.CountryUtils;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static android.widget.Toast.LENGTH_LONG;

public class SignUpFragment extends Fragment implements View.OnClickListener, ConnectionDelegate {

    //region Variables
    EditText edFullName, edEmail, edMobile, edPassword;
    View lineName, lineEmail, lineMobile, linePassword;
    TextView tvErrorName, tvErrorEmail, tvErrorMobile, tvErrorPassword, SignUpBtn,mTvCodePicker;
    Button btn;
    RegisterActivity registerActivity;
    View containerView;
    long birthdate = -1;
    String code;
    ArrayList<CountryCode> countries;
    String[] countriesNamesWithCodes;
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

        // fill countries
        new Thread(this::fillCountries).start();

        return inflatedView;
    }

    private void findViews(View rootView) {
        edFullName = (EditText) rootView.findViewById(R.id.ed_full_name);
        edEmail = (EditText) rootView.findViewById(R.id.ed_email);
        edMobile = (EditText) rootView.findViewById(R.id.ed_mobile);
        edPassword = (EditText) rootView.findViewById(R.id.ed_birthday);
        tvErrorName = (TextView) rootView.findViewById(R.id.full_name_error);
        tvErrorEmail = (TextView) rootView.findViewById(R.id.email_error);
        tvErrorMobile = (TextView) rootView.findViewById(R.id.mobile_error);
        tvErrorPassword = (TextView) rootView.findViewById(R.id.birthday_error);
        lineName = rootView.findViewById(R.id.full_name_underline);
        lineEmail = rootView.findViewById(R.id.email_underline);
        lineMobile = rootView.findViewById(R.id.mobile_underline);
        linePassword = rootView.findViewById(R.id.birthday_underline);
        containerView = rootView.findViewById(R.id.container);
        SignUpBtn = rootView.findViewById(R.id.sign_up_btn);
        mTvCodePicker = rootView.findViewById(R.id.tv_code_picker);

        changeTextListeners();

        SignUpBtn.setOnClickListener(v->handleRegisterValidation());
    }

    private void fillCountries() {
        countries = CountryUtils.getAllCountries();
        if (!countries.isEmpty()) {
            code = "+" + countries.get(0).getDialCode();
            if (getActivity() != null)
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvCodePicker.setText(code);
                    }
                });
            countriesNamesWithCodes = new String[countries.size()];
            for (int i = 0; i < countries.size(); i++) {
                countriesNamesWithCodes[i] = countries.get(i).getDialCode() + "+ " + countries.get(i).getName();
            }
        } else
            code = "";
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
        params.setPhone(edMobile.getText().toString());
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

