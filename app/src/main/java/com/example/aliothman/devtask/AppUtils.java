package com.example.aliothman.devtask;

import android.content.Context;
import android.widget.Toast;

public class AppUtils {

    public static void showInfoToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
