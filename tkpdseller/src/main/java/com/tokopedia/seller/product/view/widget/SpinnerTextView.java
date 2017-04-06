package com.tokopedia.seller.product.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;

import com.tokopedia.seller.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 04/05/17.
 */

public class SpinnerTextView extends FrameLayout {

    private TextInputLayout textInputLayout;
    private AutoCompleteTextView textAutoComplete;

    private String hintText;
    private CharSequence[] entries;
    private CharSequence[] values;
    private int selection;

    private ArrayAdapter<String> adapter;

    public SpinnerTextView(Context context) {
        super(context);
        init();
    }

    public SpinnerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SpinnerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.SpinnerTextView);
        try {
            hintText = styledAttributes.getString(R.styleable.SpinnerTextView_spinner_hint);
            selection = styledAttributes.getInt(R.styleable.SpinnerTextView_spinner_selection, 0);
            entries = styledAttributes.getTextArray(R.styleable.SpinnerTextView_spinner_entries);
            values = styledAttributes.getTextArray(R.styleable.SpinnerTextView_spinner_values);
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textInputLayout.setHint(hintText);
        if (entries != null) {
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, convert(entries));
            textAutoComplete.setAdapter(adapter);
            textAutoComplete.setListSelection(selection);
        }
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_spinner_text_view, this);
        textInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout);
        textAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.edit_text_spinner);
        textAutoComplete.setOnKeyListener(null);
        textAutoComplete.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView) view).showDropDown();
                return false;
            }
        });
    }

    public void setHint(String hintText) {
        textInputLayout.setHint(hintText);
        invalidate();
        requestLayout();
    }

    private int getSelectionIndex() {
        String text = textAutoComplete.getText().toString();
        for (int i = 0; i < entries.length; i++) {
            String entry = entries[i].toString();
            if (entry.equalsIgnoreCase(text)) {
                return i;
            }
        }
        return -1;
    }

    private List<String> convert(CharSequence[] charSequenceArray) {
        List<String> list = new ArrayList<>();
        for (CharSequence charSequence: charSequenceArray) {
            list.add(charSequence.toString());
        }
        return list;
    }
}