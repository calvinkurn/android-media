package com.tokopedia.seller.product.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.tokopedia.core.R;

/**
 * Created by Toped18 on 5/30/2016.
 */
public class ImageEditDialogFragment extends DialogFragment {

    public static final String FRAGMENT_TAG = ImageEditDialogFragment.class.getSimpleName();
    public static final String IMAGE_PRODUCT_POSITION = "IMAGE_PRODUCT_POSITION";

    public static final String IMAGE_IS_PRIMARY = "IMAGE_IS_PRIMARY";

    private int imageMenuRes[];
    private CharSequence imageMenu[];
    private OnImageEditListener mListener;

    public static final int DELETE_IMAGE = R.string.title_img_delete;
    public static final int CHANGE_IMAGE = R.string.action_edit;
    public static final int ADD_DESCRIPTION = R.string.title_img_desc;
    public static final int CHANGE_TO_PRIMARY = R.string.title_img_default;

    public interface OnImageEditListener {
        void clickEditImagePath(int position);

        void clickEditImageDesc(int position);

        void clickEditImagePrimary(int position);

        void clickRemoveImage(int positions);
    }

    public int position;
    public boolean isPrimary;

    public static DialogFragment newInstance(int position, boolean isPrimary) {
        ImageEditDialogFragment f = new ImageEditDialogFragment();
        Bundle args = new Bundle();
        args.putInt(IMAGE_PRODUCT_POSITION, position);
        args.putBoolean(IMAGE_IS_PRIMARY, isPrimary);
        f.setArguments(args);
        return f;
    }

    public void setOnImageEditListener(OnImageEditListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(IMAGE_PRODUCT_POSITION);
        isPrimary = getArguments().getBoolean(IMAGE_IS_PRIMARY);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (isPrimary) {
            imageMenuRes = new int[]{
                    DELETE_IMAGE,
                    CHANGE_IMAGE,
                    ADD_DESCRIPTION};
        } else {
            imageMenuRes = new int[]{
                    DELETE_IMAGE,
                    CHANGE_IMAGE,
                    ADD_DESCRIPTION,
                    CHANGE_TO_PRIMARY};
        }
        imageMenu = new CharSequence[imageMenuRes.length];
        for (int i = 0, sizei = imageMenuRes.length; i < sizei; i++) {
            imageMenu[i] = getActivity().getString(imageMenuRes[i]);
        }
        builder.setItems(imageMenu, getImageAddProductListener());
        return builder.create();
    }

    private DialogInterface.OnClickListener getImageAddProductListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    int resClicked = imageMenuRes[which];
                    if (resClicked == DELETE_IMAGE) {
                        mListener.clickRemoveImage(position);
                    } else if (resClicked == CHANGE_IMAGE) {
                        mListener.clickEditImagePath(position);
                    } else if (resClicked == ADD_DESCRIPTION) {
                        mListener.clickEditImageDesc(position);
                    } else if (resClicked == CHANGE_TO_PRIMARY) {
                        mListener.clickEditImagePrimary(position);
                    }
                }
            }
        };
    }
}
