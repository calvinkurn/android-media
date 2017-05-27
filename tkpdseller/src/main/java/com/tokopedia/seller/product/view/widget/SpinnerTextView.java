package com.tokopedia.seller.product.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.utils.ConverterUtils;

/**
 * Created by nathan on 04/05/17.
 */

public class SpinnerTextView extends AbsSpinnerTextView {

    public SpinnerTextView(Context context) {
        super(context);
    }

    public SpinnerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpinnerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflateSetHint() {
        if (!TextUtils.isEmpty(hintText)) {
            textInputLayout.setHint(hintText);
        }
    }

    @Override
    protected void rearrangeEntries() {
        // no op
    }

    protected void validateSelectionIndex(){
        if (selectionIndex < 0 || selectionIndex >= entries.length) {
            selectionIndex = 0;
        }
    }

    public int getSpinnerPosition(){
        return selectionIndex;
    }

    protected void setHintOnClicked (){
        // no op
    }

    protected ArrayAdapter<CharSequence> newAdapter(){
        return new ArrayAdapter<>(getContext(),
                R.layout.item_top_ads_autocomplete_text, entries);
    }

}