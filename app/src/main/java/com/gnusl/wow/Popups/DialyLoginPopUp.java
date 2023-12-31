package com.gnusl.wow.Popups;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.HideDialyButton;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DialyLoginPopUp extends DialogFragment {

    private static DialyLoginPopUp dailyDialog;

    private View iv_first_check, iv_second_check, iv_third_check, iv_fourth_check, iv_fifth_check, iv_six_check, iv_seven_check, clBalance, clAttenedance;
    private Button btn_login;
    private TextView tv_login_count, tvBalance;

    int daysCount = 0;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = getActivity().getLayoutInflater().inflate(R.layout.daily_login_popup_layout, null);
        dialog.setContentView(view);
        setCancelable(false);
        dialog.show();

        iv_first_check = view.findViewById(R.id.iv_first_check);
        iv_second_check = view.findViewById(R.id.iv_second_check);
        iv_third_check = view.findViewById(R.id.iv_third_check);
        iv_fourth_check = view.findViewById(R.id.iv_fourth_check);
        iv_fifth_check = view.findViewById(R.id.iv_fifth_check);
        iv_six_check = view.findViewById(R.id.iv_six_check);
        iv_seven_check = view.findViewById(R.id.iv_seven_check);

        tvBalance = view.findViewById(R.id.tv_balance);

        clBalance = view.findViewById(R.id.cl_balance);

        clAttenedance = view.findViewById(R.id.cl_attenedance);

        btn_login = view.findViewById(R.id.btn_login);

        tv_login_count = view.findViewById(R.id.tv_login_count);
        initializeDailyLoginDialog();
        return dialog;
    }


    static JSONArray loginJsonArray = null;
    static boolean isNewLoginSequance = false;

    public static DialyLoginPopUp show(FragmentActivity fragmentActivity) {

        if (dailyDialog == null)
            dailyDialog = new DialyLoginPopUp();

//        dailyDialog.show(fragmentActivity.getSupportFragmentManager(), "");

        try {
            String dailyLoginArrayStr = SharedPreferencesUtils.getDailyLoginArray();

            if (dailyLoginArrayStr.equalsIgnoreCase(""))
                loginJsonArray = new JSONArray();
            else
                loginJsonArray = new JSONArray(dailyLoginArrayStr);

            if (loginJsonArray.length() == 7 || loginJsonArray.length() == 0) {
                dailyDialog.show(fragmentActivity.getSupportFragmentManager(), "");
                isNewLoginSequance = true;
            } else {
                JSONObject lastLogin = loginJsonArray.getJSONObject(loginJsonArray.length() - 1);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_YEAR, -1);

                if (lastLogin != null && lastLogin.optLong("timestamp") < calendar.getTimeInMillis()) {
                    dailyDialog.show(fragmentActivity.getSupportFragmentManager(), "");
                }
            }
        } catch (JSONException e) {

        }

        return dailyDialog;
    }

    public static void dismissPopUp() {

        if (dailyDialog == null)
            return;

        dailyDialog.dismiss();
    }

    public View getIv_first_check() {
        return iv_first_check;
    }

    public View getIv_second_check() {
        return iv_second_check;
    }

    public View getIv_third_check() {
        return iv_third_check;
    }

    public View getIv_fourth_check() {
        return iv_fourth_check;
    }

    public View getIv_fifth_check() {
        return iv_fifth_check;
    }

    public View getIv_six_check() {
        return iv_six_check;
    }

    public View getIv_seven_check() {
        return iv_seven_check;
    }

    public Button getBtn_login() {
        return btn_login;
    }

    public TextView getTv_login_count() {
        return tv_login_count;
    }


    private void initializeDailyLoginDialog() {

        APIConnectionNetwork.GetDailyLogin(new ConnectionDelegate() {
            @Override
            public void onConnectionFailure() {

            }

            @Override
            public void onConnectionError(ANError anError) {

            }

            @Override
            public void onConnectionSuccess(String response) {

            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {
                setCancelable(true);
                daysCount = jsonObject.optInt("daily");
                dailyDialog.getTv_login_count().setText(String.format(Locale.getDefault(), getString(R.string.daily_record), daysCount));
                switch (jsonObject.optInt("daily")) {
                    case 1: {
                        dailyDialog.getIv_first_check().setVisibility(View.VISIBLE);
                        break;
                    }
                    case 2: {
                        dailyDialog.getIv_first_check().setVisibility(View.VISIBLE);
                        dailyDialog.getIv_second_check().setVisibility(View.VISIBLE);
                        break;
                    }
                    case 3: {
                        dailyDialog.getIv_first_check().setVisibility(View.VISIBLE);
                        dailyDialog.getIv_second_check().setVisibility(View.VISIBLE);
                        dailyDialog.getIv_third_check().setVisibility(View.VISIBLE);
                        break;
                    }
                    case 4: {
                        dailyDialog.getIv_first_check().setVisibility(View.VISIBLE);
                        dailyDialog.getIv_second_check().setVisibility(View.VISIBLE);
                        dailyDialog.getIv_third_check().setVisibility(View.VISIBLE);
                        dailyDialog.getIv_fourth_check().setVisibility(View.VISIBLE);
                        break;
                    }
                    case 5: {
                        dailyDialog.getIv_first_check().setVisibility(View.VISIBLE);
                        dailyDialog.getIv_second_check().setVisibility(View.VISIBLE);
                        dailyDialog.getIv_third_check().setVisibility(View.VISIBLE);
                        dailyDialog.getIv_fourth_check().setVisibility(View.VISIBLE);
                        dailyDialog.getIv_fifth_check().setVisibility(View.VISIBLE);
                        break;
                    }
                    case 6: {
                        dailyDialog.getIv_first_check().setVisibility(View.VISIBLE);
                        dailyDialog.getIv_second_check().setVisibility(View.VISIBLE);
                        dailyDialog.getIv_third_check().setVisibility(View.VISIBLE);
                        dailyDialog.getIv_fourth_check().setVisibility(View.VISIBLE);
                        dailyDialog.getIv_fifth_check().setVisibility(View.VISIBLE);
                        dailyDialog.getIv_six_check().setVisibility(View.VISIBLE);
                        break;
                    }
                }

            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {

            }
        });


        if (dailyDialog != null) {
            dailyDialog.getBtn_login().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new HideDialyButton());
                    APIConnectionNetwork.StoreDailyLogin(new ConnectionDelegate() {
                        @Override
                        public void onConnectionFailure() {

                        }

                        @Override
                        public void onConnectionError(ANError anError) {

                        }

                        @Override
                        public void onConnectionSuccess(String response) {

                        }

                        @Override
                        public void onConnectionSuccess(JSONObject jsonObject) {
                            APIConnectionNetwork.GetUserDetails(SharedPreferencesUtils.getUser().getId(), new ConnectionDelegate() {
                                        @Override
                                        public void onConnectionFailure() {

                                        }

                                        @Override
                                        public void onConnectionError(ANError anError) {

                                        }

                                        @Override
                                        public void onConnectionSuccess(String response) {

                                        }

                                        @Override
                                        public void onConnectionSuccess(JSONObject jsonObject) {
                                            clAttenedance.setVisibility(View.GONE);
                                            clBalance.setVisibility(View.VISIBLE);
                                            tvBalance.setText(String.format(getString(R.string.your_balance), jsonObject.optJSONObject("user").optString("balance")));
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    DialyLoginPopUp.dismissPopUp();
                                                }
                                            }, 1500);
                                        }

                                        @Override
                                        public void onConnectionSuccess(JSONArray jsonArray) {

                                        }
                                    }
                            );
                        }

                        @Override
                        public void onConnectionSuccess(JSONArray jsonArray) {

                        }
                    });
                    int i = loginJsonArray.length();
                    switch (i) {
                        case 0: {
                            dailyDialog.getIv_first_check().setVisibility(View.VISIBLE);
                            break;
                        }
                        case 1: {
                            dailyDialog.getIv_first_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_second_check().setVisibility(View.VISIBLE);
                            break;
                        }
                        case 2: {
                            dailyDialog.getIv_first_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_second_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_third_check().setVisibility(View.VISIBLE);
                            break;
                        }
                        case 3: {
                            dailyDialog.getIv_first_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_second_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_third_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_fourth_check().setVisibility(View.VISIBLE);
                            break;
                        }
                        case 4: {
                            dailyDialog.getIv_first_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_second_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_third_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_fourth_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_fifth_check().setVisibility(View.VISIBLE);
                            break;
                        }
                        case 5: {
                            dailyDialog.getIv_first_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_second_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_third_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_fourth_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_fifth_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_six_check().setVisibility(View.VISIBLE);
                            break;
                        }
                        case 6: {
                            dailyDialog.getIv_first_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_second_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_third_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_fourth_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_fifth_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_six_check().setVisibility(View.VISIBLE);
                            dailyDialog.getIv_seven_check().setVisibility(View.VISIBLE);
                        }
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("timestamp", calendar.getTimeInMillis());
                        loginJsonArray.put(jsonObject);
                        SharedPreferencesUtils.setDailyLoginArray(loginJsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }


}

