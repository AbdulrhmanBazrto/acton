package com.gnusl.wow.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardUtils {

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    public static void hideSoftKeyboardFromDialog(DialogFragment dialogFragment) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) dialogFragment.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(dialogFragment.getDialog().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    public static void displaySoftKeyboardFromDialog(DialogFragment dialogFragment) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) dialogFragment.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(dialogFragment.getDialog().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }


    public static void showSoftKeyboardForEditText(FragmentActivity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

}
