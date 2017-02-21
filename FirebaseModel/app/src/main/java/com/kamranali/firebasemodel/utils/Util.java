package com.kamranali.firebasemodel.utils;

import android.content.Context;
import android.widget.Toast;

public class Util {
    public static void successToast(Context context,String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void failureToast(Context context,String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
