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

public class SpinnerTextView extends FrameLayout {

    public interface OnItemChangeListener {

        void onItemChanged(int position, String entry, String value);

    }

    private static final int DEFAULT_INDEX_NOT_SELECTED = -1;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView textAutoComplete;

    private String hintText;
    private CharSequence[] entries;
    private CharSequence[] values;
    private int selectionIndex;
    private boolean enabled;
    private AdapterView.OnItemClickListener onItemClickListener;

    private OnItemChangeListener onItemChangeListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemChangeListener(OnItemChangeListener onItemChangeListener) {
        this.onItemChangeListener = onItemChangeListener;
    }

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
            selectionIndex = styledAttributes.getInt(R.styleable.SpinnerTextView_spinner_selection_index, 0);
            entries = styledAttributes.getTextArray(R.styleable.SpinnerTextView_spinner_entries);
            values = styledAttributes.getTextArray(R.styleable.SpinnerTextView_spinner_values);
            enabled = styledAttributes.getBoolean(R.styleable.SpinnerTextView_spinner_enabled, true);
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!TextUtils.isEmpty(hintText)) {
            textInputLayout.setHint(hintText);
        }
        if (entries != null) {
            updateEntries(ConverterUtils.convertCharSequenceToString(entries));
        }
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_spinner_text_view, this);
        textInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout);
        textAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.edit_text_spinner);
        textAutoComplete.setOnKeyListener(null);
        textAutoComplete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AutoCompleteTextView) view).showDropDown();
            }
        });
        textAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(adapterView, view, position, id);
                }
                selectionIndex = position;
                updateOnItemChanged(position);
            }
        });
    }

    private void resetPosition() {
        selectionIndex = DEFAULT_INDEX_NOT_SELECTED;
        textAutoComplete.setText("");
    }

    @Override
    public void setEnabled(boolean enabled) {
        textInputLayout.setEnabled(enabled);
        textAutoComplete.setEnabled(enabled);
    }

    public void setHint(String hintText) {
        textInputLayout.setHint(hintText);
        invalidate();
        requestLayout();
    }

    public void setEntries(String[] entries) {
        updateEntries(entries);
        invalidate();
        requestLayout();
    }

    public void setValues(String[] values) {
        this.values = values;
        invalidate();
        requestLayout();
    }

    public String getSpinnerValue() {
        if (values == null) {
            return null;
        }
        return values[selectionIndex].toString();
    }

    public String getSpinnerValue(int position) {
        return values[position].toString();
    }

    public void setSpinnerPosition(int position) {
        if (position >= 0 && position < values.length) {
            selectionIndex = position;
            textAutoComplete.setText(entries[position]);
            updateOnItemChanged(position);
        }
    }

    public int getSpinnerPosition(){
        return selectionIndex;
    }

    private void updateOnItemChanged(final int position) {
        if (onItemChangeListener != null) {
            onItemChangeListener.onItemChanged(position, entries[position].toString(), values[position].toString());
        }
    }

    public void setSpinnerValue(String value) {
        if (TextUtils.isEmpty(value)) {
            resetPosition();
            return;
        }
        for (int i = 0; i < values.length; i++) {
            String tempValue = values[i].toString();
            if (tempValue.equalsIgnoreCase(value)) {
                setSpinnerPosition(i);
                return;
            }
        }
        resetPosition();
    }

    public void setError(String error) {
        textInputLayout.setError(error);
    }

    private void updateEntries(String[] entries) {
        if (entries != null) {
            this.entries = entries;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_top_ads_autocomplete_text, entries);
            textAutoComplete.setAdapter(adapter);
            if (selectionIndex >= 0) {
                textAutoComplete.setText(entries[selectionIndex]);
                updateOnItemChanged(selectionIndex);
            }
        }
    }

    public EditText getEditText() {
        return textAutoComplete;
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.initChildrenStates();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).saveHierarchyState(ss.getChildrenStates());
        }
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).restoreHierarchyState(ss.getChildrenStates());
        }
    }
}