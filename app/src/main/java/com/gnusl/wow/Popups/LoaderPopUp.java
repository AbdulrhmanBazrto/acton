package com.gnusl.wow.Popups;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;

import com.gnusl.wow.R;

public class LoaderPopUp extends DialogFragment {

    private static LoaderPopUp loaderPopUp;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getActivity().getLayoutInflater().inflate(R.layout.loader_popup_layout, null);
        dialog.setContentView(view);
        setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static void show(FragmentActivity fragmentActivity) {


        if (loaderPopUp == null)
            loaderPopUp = new LoaderPopUp();
        if (fragmentActivity == null)
            return;

        if (loaderPopUp.isAdded()) {
            return; //or return false/true, based on where you are calling from
        }

        loaderPopUp.show(fragmentActivity.getSupportFragmentManager(), "");
    }

    public static void dismissLoader() {

        if (loaderPopUp == null)
            return;

        loaderPopUp.dismiss();
    }
}

