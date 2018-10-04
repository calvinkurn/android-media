package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author alvarisi on 9/19/16.
 */
public class DescriptionTextView extends AppCompatTextView {

    public DescriptionTextView(Context context) {
        super(context);
    }

    public DescriptionTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        if (selStart == -1 && selEnd == -1) {
            CharSequence text = getText();
            if (text instanceof Spannable) {
                clearFocus();
            }
        } else {
            super.onSelectionChanged(selStart, selEnd);
        }
    }
}
