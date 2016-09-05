package com.wm.lock.core.utils;

import android.text.TextUtils;
import android.widget.EditText;

public final class StringUtils {

    public static boolean isEmpty(EditText et) {
        return TextUtils.isEmpty(et.getText().toString().trim());
    }

    public static boolean equals(EditText et1, EditText et2) {
        return TextUtils.equals(et1.getText().toString().trim(), et2.getText().toString().trim());
    }

}
