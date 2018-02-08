package com.tokopedia.seller.product.edit.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.tokopedia.seller.R;

/**
 * Created by Toped18 on 5/30/2016.
 */
public class ImageAddDialogFragment extends DialogFragment {

    public static final String FRAGMENT_TAG = ImageAddDialogFragment.class.getSimpleName();
    public static final String IMAGE_PRODUCT_POSITION = "IMAGE_PRODUCT_POSITION";

    private CharSequence imageMenu[];
    private OnImageAddListener mListener;

    public interface OnImageAddListener {
        void clickAddProductFromCamera(int position);

        void clickAddProductFromGallery(int position);

        void clickAddProductFromInstagram(int position);
    }

    public int position;

    public static ImageAddDialogFragment newInstance(int position) {
        ImageAddDialogFragment f = new ImageAddDialogFragment();
        Bundle args = new Bundle();
        args.putInt(IMAGE_PRODUCT_POSITION, position);
        f.setArguments(args);
        return f;
    }

    public void setOnImageAddListener(OnImageAddListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(IMAGE_PRODUCT_POSITION);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        imageMenu = new CharSequence[]{
                getString(R.string.added_from_camera_text_description),
                getString(R.string.added_from_gallery_text_description),
                getString(R.string.import_from_instagram_text_description)};
        builder.setItems(imageMenu, getImageAddProductListener());
        return builder.create();
    }

    private DialogInterface.OnClickListener getImageAddProductListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    CharSequence stringClicked = imageMenu[which];
                    if (stringClicked.equals(getString(R.string.added_from_camera_text_description))) {
                        mListener.clickAddProductFromCamera(position);
                    } else if (stringClicked.equals(getString(R.string.added_from_gallery_text_description))) {
                        mListener.clickAddProductFromGallery(position);
                    } else if (stringClicked.equals(getString(R.string.import_from_instagram_text_description))) {
                        mListener.clickAddProductFromInstagram(position);
                    }
                }
            }
        };
    }
}
