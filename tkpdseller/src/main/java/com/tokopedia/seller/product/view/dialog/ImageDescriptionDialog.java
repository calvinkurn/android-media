package com.tokopedia.seller.product.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.tokopedia.seller.R;

/**
 * Created by m.normansyah on 18/01/2016.
 */
public class ImageDescriptionDialog extends DialogFragment {
    EditText editTextProdDesc;
    View viewCancel;
    View viewOK;

    public static final String TAG = ImageDescriptionDialog.class.getSimpleName();
    private static final String DESC = "desc";
    private String descriptionString;

    OnImageDescDialogListener mListener;
    public interface OnImageDescDialogListener {
        void onImageDescDialogOK(String newDescription);
    }

    public void setListener(OnImageDescDialogListener listener) {
        this.mListener = listener;
    }

    public static ImageDescriptionDialog newInstance(String description){
        ImageDescriptionDialog imageDescriptionDialog = new ImageDescriptionDialog();
        Bundle bundle = new Bundle();
        bundle.putString(DESC, description);
        imageDescriptionDialog.setArguments(bundle);
        return imageDescriptionDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle( DialogFragment.STYLE_NO_TITLE, com.tokopedia.seller.R.style.AppCompatAlertDialogStyle);

        if (getArguments()!=null) {
            descriptionString = getArguments().getString(DESC, "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.dialog_change_image_desc, container, false);
        editTextProdDesc = (EditText) parentView.findViewById(R.id.edit_description);
        viewCancel = parentView.findViewById(R.id.text_cancel);
        viewOK = parentView.findViewById(R.id.text_ok);

        if (savedInstanceState == null) { // happen only on first time. On saved, will the latest desc.
            editTextProdDesc.setText(descriptionString);
            editTextProdDesc.setSelection(descriptionString.length());
        }

        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDescriptionDialog.this.dismiss();
            }
        });
        viewOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onImageDescDialogOK(editTextProdDesc.getText().toString());
                ImageDescriptionDialog.this.dismiss();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnImageDescDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("must implement OnImageDescDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
