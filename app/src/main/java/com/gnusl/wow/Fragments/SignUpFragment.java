package com.gnusl.wow.Fragments;

import android.content.Context;
import android.os.Bundle;
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

import com.gnusl.wow.Activities.RegisterActivity;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class SignUpFragment extends Fragment implements View.OnClickListener {

    //region Variables
    EditText edFullName, edEmail, edMobile, edBirthday;
    View lineName, lineEmail, lineMobile, lineBirthday;
    TextView tvErrorName, tvErrorEmail, tvErrorMobile, tvErrorBirthday, SignUpBtn;
    Button btn;
    RegisterActivity registerActivity;
    View containerView;
    long birthdate = -1;
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
        edFullName = (EditText) rootView.findViewById(R.id.ed_full_name);
        edEmail = (EditText) rootView.findViewById(R.id.ed_email);
        edMobile = (EditText) rootView.findViewById(R.id.ed_mobile);
        edBirthday = (EditText) rootView.findViewById(R.id.ed_birthday);
        tvErrorName = (TextView) rootView.findViewById(R.id.full_name_error);
        tvErrorEmail = (TextView) rootView.findViewById(R.id.email_error);
        tvErrorMobile = (TextView) rootView.findViewById(R.id.mobile_error);
        tvErrorBirthday = (TextView) rootView.findViewById(R.id.birthday_error);
        lineName = rootView.findViewById(R.id.full_name_underline);
        lineEmail = rootView.findViewById(R.id.email_underline);
        lineMobile = rootView.findViewById(R.id.mobile_underline);
        lineBirthday = rootView.findViewById(R.id.birthday_underline);
        containerView = rootView.findViewById(R.id.container);
        SignUpBtn = rootView.findViewById(R.id.sign_up_btn);

        changeTextListeners();

        edBirthday.setFocusable(false);
        edBirthday.setEnabled(true);

        SignUpBtn.setOnClickListener(this);
        edBirthday.setOnClickListener(this);

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
}

