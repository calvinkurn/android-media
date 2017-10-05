package com.tokopedia.seller.base.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;

/**
 * @author sebastianuskh on 4/17/17.
 */

public abstract class BaseTextPickerDialogFragment extends DialogFragment {

    protected EditText textInput;
    protected TextInputLayout textInputLayout;
    protected TextView stringPickerTitle;

    protected Listener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else {
            throw new RuntimeException("Activity must implement Listener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_base_text_picker, container, false);
        stringPickerTitle = (TextView) view.findViewById(R.id.string_picker_dialog_title);
        textInput = (EditText) view.findViewById(R.id.string_picker_dialog_edit_text);
        textInputLayout = (TextInputLayout) view.findViewById(R.id.string_picker_dialog_input_layout);
        view.findViewById(R.id.string_picker_dialog_confirm)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onTextSubmitted(textInput.getText().toString());
                    }
                });
        view.findViewById(R.id.string_picker_dialog_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
        return view;
    }

    protected void onTextSubmitted(String text) {
        listener.onTextPickerSubmitted(text);
        dismiss();
    }

    /**
     * @author sebastianuskh on 4/5/17.
     */

    public interface Listener {

        void onTextPickerSubmitted(String text);

    }
}
