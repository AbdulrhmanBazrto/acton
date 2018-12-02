package com.gnusl.wow.Utils;

public class APIUtils {

    public static boolean isValidEmail(CharSequence target) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
