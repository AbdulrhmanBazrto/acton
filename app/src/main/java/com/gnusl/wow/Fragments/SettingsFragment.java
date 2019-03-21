package com.gnusl.wow.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.Activities.SettingsActivity;
import com.gnusl.wow.Activities.SplashActivity;
import com.gnusl.wow.Enums.FragmentTags;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.LocaleManager;
import com.gnusl.wow.Utils.SharedPreferencesUtils;

public class SettingsFragment extends Fragment {

    private View inflatedView;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {

        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_settings, container, false);

        inflatedView.findViewById(R.id.accout_button).setOnClickListener(v -> ((SettingsActivity) getActivity()).makeFragment(FragmentTags.AccountFragment));
        inflatedView.findViewById(R.id.block_list).setOnClickListener(v -> ((SettingsActivity) getActivity()).makeFragment(FragmentTags.BlockListFragment));
        inflatedView.findViewById(R.id.language_button).setOnClickListener(v -> {
            openLanguageDialog();
        });
        inflatedView.findViewById(R.id.logout_button).setOnClickListener(v -> {

            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setMessage(getString(R.string.do_you_want_to_logout));

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                    (dialog, which) -> {
                        dialog.dismiss();

                        // clear shared
                        SharedPreferencesUtils.clear();
                        getActivity().finishAffinity();
                        startActivity(new Intent(getActivity(), SplashActivity.class));
                    });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel", (dialog, which) -> dialog.dismiss());

            alertDialog.show();
        });

        return inflatedView;
    }

    private void openLanguageDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        CharSequence[] languages = new CharSequence[2];
        languages[0] = "العربية";
        languages[1] = "english";

        builder.setItems(languages, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        SharedPreferencesUtils.saveLanguage(getActivity(), "ar");
                        LocaleManager.setNewLocale(getActivity(), "ar");

                        doRestart1(getActivity());
                        break;
                    }
                    case 1: {
                        SharedPreferencesUtils.saveLanguage(getActivity(), "en");
                        LocaleManager.setNewLocale(getActivity(), "en");

                        doRestart1(getActivity());
                        break;
                    }
                    default:
                        break;
                }
            }
        });
        AlertDialog changeLangsDialog = builder.create();
        changeLangsDialog.setCancelable(true);
        changeLangsDialog.setCanceledOnTouchOutside(true);
        changeLangsDialog.show();
    }


    public void doRestart1(Context c) {
        try {
            if (c != null) {
                PackageManager pm = c.getPackageManager();
                if (pm != null) {
                    Intent mStartActivity = pm.getLaunchIntentForPackage(c.getPackageName());
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent.getActivity(c, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);

                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                    }
                }
            }
        } catch (Exception ex) {
        }
    }
}


