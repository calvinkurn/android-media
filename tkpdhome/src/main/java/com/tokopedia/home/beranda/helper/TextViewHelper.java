package com.tokopedia.home.beranda.helper;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * Created by henrypriyono on 01/02/18.
 */

public class TextViewHelper {
    public static void displayText(TextView textView, CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(charSequence);
        } else {
            textView.setVisibility(View.GONE);
        }
    }
}
