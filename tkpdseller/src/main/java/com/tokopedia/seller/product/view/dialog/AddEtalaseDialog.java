package com.tokopedia.seller.product.view.dialog;

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
 * @author sebastianuskh on 4/5/17.
 */

public class AddEtalaseDialog extends DialogFragment{

    public static final String TAG = "AddEtalaseDialog";
    protected EditText etalaseName;
    protected TextInputLayout etalaseNameInputLayout;
    protected AddEtalaseDialogListener listener;
    protected TextView etalaseTitle;

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
        if (context instanceof AddEtalaseDialogListener){
            listener = (AddEtalaseDialogListener) context;
        } else {
            throw new RuntimeException("Activity must implement AddEtalaseDialogListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.etalase_picker_add_new_etalase_dialog, container, false);
        etalaseTitle = (TextView) view.findViewById(R.id.etalase_picker_add_etalase_name_title);
        etalaseName = (EditText) view.findViewById(R.id.etalase_picker_add_etalase_name_edit_text);
        etalaseNameInputLayout = (TextInputLayout) view.findViewById(R.id.etalase_picker_add_etalase_name_input_layout);
        view.findViewById(R.id.etalase_picker_add_etalase_confirm)
                .setOnClickListener(getSaveOnClickListener());
        view.findViewById(R.id.etalase_picker_add_etalase_cancel)
                .setOnClickListener(new CancelEtalaseButtonOnClick());
        return view;
    }

    @NonNull
    protected AddEtalaseButtonOnClick getSaveOnClickListener() {
        return new AddEtalaseButtonOnClick();
    }

    protected class AddEtalaseButtonOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String newEtalaseName = etalaseName.getText().toString();
            if(newEtalaseName.isEmpty()){
                etalaseNameInputLayout.setError(getString(R.string.etalase_picker_add_etalase_name_empty));
            } else {
                listener.addEtalase(newEtalaseName);
                dismiss();
            }
        }
    }

    private class CancelEtalaseButtonOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    }
}
