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

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.utils.ConverterUtils;

/**
 * Created by nathan on 04/05/17.
 */

public abstract class AbsSpinnerTextView extends FrameLayout {

    public interface OnItemChangeListener {
        void onItemChanged(int position, String entry, String value);
    }

    public interface OnDropDownListener {
        void onDropDownShown();
    }

    protected TextInputLayout textInputLayout;
    protected AutoCompleteTextView textAutoComplete;

    protected String hintText;
    protected CharSequence[] entries;
    private CharSequence[] values;
    protected int selectionIndex;
    private boolean enabled;
    private AdapterView.OnItemClickListener onItemClickListener;

    private OnItemChangeListener onItemChangeListener;
    private OnDropDownListener onDropDownListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemChangeListener(OnItemChangeListener onItemChangeListener) {
        this.onItemChangeListener = onItemChangeListener;
    }

    public void setOnDropDownListener(OnDropDownListener onDropDownListener) {
        this.onDropDownListener = onDropDownListener;
    }

    public AbsSpinnerTextView(Context context) {
        super(context);
        init();
    }

    public AbsSpinnerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AbsSpinnerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        onFinishInflateSetHint();
        if (entries != null) {
            rearrangeEntries();
            updateEntries(ConverterUtils.convertCharSequenceToString(entries));
        }
        setEnabled(enabled);
        invalidate();
        requestLayout();
    }

    abstract protected void onFinishInflateSetHint();

    abstract protected void rearrangeEntries();

    private void init() {
        View view = inflate(getContext(), R.layout.widget_spinner_text_view, this);
        textInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout);
        textAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.edit_text_spinner);
        textAutoComplete.setOnKeyListener(null);
        textAutoComplete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AutoCompleteTextView) view).showDropDown();
                if (onDropDownListener != null) {
                    onDropDownListener.onDropDownShown();
                }
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
        if (values == null || selectionIndex < 0 || selectionIndex >= values.length) {
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
            setHintOnClicked();
        }
    }

    abstract public int getSpinnerPosition();

    protected void updateOnItemChanged(final int position) {
        if (onItemChangeListener != null) {
            onItemChangeListener.onItemChanged(position, entries[position].toString(), values[position].toString());
        }
    }

    abstract protected void setHintOnClicked();

    public void setSpinnerValue(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        for (int i = 0; i < values.length; i++) {
            String tempValue = values[i].toString();
            if (tempValue.equalsIgnoreCase(value)) {
                setSpinnerPosition(i);
                break;
            }
        }
    }

    public void setError(String error) {
        textInputLayout.setError(error);
    }

    private void updateEntries(String[] entries) {
        if (entries != null) {
            this.entries = entries;
            ArrayAdapter<CharSequence> adapter = newAdapter();
            textAutoComplete.setAdapter(adapter);
            validateSelectionIndex();
            textAutoComplete.setText(entries[selectionIndex]);
            updateOnItemChanged(selectionIndex);
        }
    }

    abstract protected void validateSelectionIndex();

    abstract protected ArrayAdapter<CharSequence> newAdapter();

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