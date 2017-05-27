package com.tokopedia.seller.product.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import com.tokopedia.seller.R;

import java.util.List;

/**
 * Created by Hendry on 5/27/2017.
 */

public class DefValueSpinnerTextView extends AbsSpinnerTextView {

    private String defaultValue;

    public DefValueSpinnerTextView(Context context) {
        super(context);
    }

    public DefValueSpinnerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DefValueSpinnerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.DefaultValueSpinnerTextView);
        try {
            defaultValue = styledAttributes.getString(R.styleable.DefaultValueSpinnerTextView_default_value);
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflateSetHint() {
        if (getSpinnerPosition() == -1) {
            textInputLayout.setHint("");
        } else {
            textInputLayout.setHint(hintText);
        }
    }

    @Override
    protected void rearrangeEntries() {
        if (entries != null && !TextUtils.isEmpty(defaultValue)) {
            if (entries[entries.length - 1] != defaultValue) {
                int newSize = entries.length + 1;
                CharSequence[] newEntries = new CharSequence[newSize];
                for (int i = 0, sizei = entries.length; i < sizei; i++) {
                    newEntries[i] = entries[i];
                }
                newEntries[entries.length] = defaultValue;
                entries = newEntries;
            }
        }
    }

    @Override
    protected void validateSelectionIndex() {
        if (selectionIndex < 0 || selectionIndex >= entries.length) {
            selectionIndex = entries.length - 1;
        }
    }

    @Override
    public int getSpinnerPosition() {
        if (selectionIndex >= entries.length - 1) {
            return -1;
        }
        return selectionIndex;
    }

    @Override
    protected void setHintOnClicked() {
        textInputLayout.setHint(hintText);
    }

    protected ArrayAdapter<CharSequence> newAdapter() {
        return new DefaultValueAdapter(getContext(),
                R.layout.item_top_ads_autocomplete_text, entries);
    }

    private class DefaultValueAdapter extends ArrayAdapter<CharSequence> {

        public DefaultValueAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull CharSequence[] objects) {
            super(context, resource, objects);
        }

        @Override
        public int getCount() {
            // don't display last item. It is used as hint.
            int count = super.getCount();
            return count > 0 ? count - 1 : count;
        }
    }
}
