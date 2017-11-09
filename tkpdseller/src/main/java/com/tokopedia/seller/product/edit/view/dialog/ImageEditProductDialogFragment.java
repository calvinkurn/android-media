package com.tokopedia.seller.product.edit.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.tokopedia.seller.R;
import com.tokopedia.seller.instoped.InstopedSellerActivity;
import com.tokopedia.seller.instoped.InstopedSellerCropperActivity;

/**
 * Created by Toped18 on 5/30/2016.
 */
public class ImageEditProductDialogFragment extends DialogFragment {

    public static final String FRAGMENT_TAG = ImageEditProductDialogFragment.class.getSimpleName();
    public static final String IMAGE_PRODUCT_POSITION = "IMAGE_PRODUCT_POSITION";

    public static final String IMAGE_IS_PRIMARY = "IMAGE_IS_PRIMARY";
    public static final String ALLOW_DELETE = "ALLOW_DELETE";

    private CharSequence imageMenu[];
    private OnImageEditListener mListener;
    boolean allowDelete;

    public interface OnImageEditListener {
        void clickEditImagePathFromCamera(int position);
        void clickEditImagePathFromGallery(int position);
        void clickEditImagePathFromInstagram(int position);

        void clickImageEditor(int position);

        void clickEditImageDesc(int position);

        void clickEditImagePrimary(int position);

        void clickRemoveImage(int positions);
    }

    public int position;
    public boolean isPrimary;

    public static DialogFragment newInstance(int position, boolean isPrimary, boolean allowDelete) {
        ImageEditProductDialogFragment f = new ImageEditProductDialogFragment();
        Bundle args = new Bundle();
        args.putInt(IMAGE_PRODUCT_POSITION, position);
        args.putBoolean(IMAGE_IS_PRIMARY, isPrimary);
        args.putBoolean(ALLOW_DELETE, allowDelete);
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
        allowDelete = getArguments().getBoolean(ALLOW_DELETE);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (isPrimary) {
            if (allowDelete) { // primary image and allow delete
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    imageMenu = new CharSequence[]{
//                            getString(R.string.title_img_delete),
//                            getString(R.string.edit_from_camera_text_description),
//                            getString(R.string.edit_from_gallery_text_description),
//                            getString(R.string.edit_from_instagram_text_description),
//                            getString(R.string.title_img_desc)};
//                } else {
                    imageMenu = new CharSequence[]{
                            getString(R.string.title_img_delete),
                            getString(R.string.edit_from_camera_text_description),
                            getString(R.string.edit_from_gallery_text_description),
                            getString(R.string.edit_from_instagram_text_description),
                            getString(R.string.action_editor),
                            getString(R.string.title_img_desc)};
//                }
            } else { // primary image and not allow delete
                imageMenu = new CharSequence[]{
                        getString(R.string.title_img_desc)};
            }

        } else {
            if (allowDelete) { // not primary image and allow delete
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    imageMenu = new CharSequence[]{
//                            getString(R.string.title_img_delete),
//                            getString(R.string.edit_from_camera_text_description),
//                            getString(R.string.edit_from_gallery_text_description),
//                            getString(R.string.edit_from_instagram_text_description),
//                            getString(R.string.title_img_desc),
//                            getString(R.string.title_img_default)};
//                } else {
                    imageMenu = new CharSequence[]{
                            getString(R.string.title_img_delete),
                            getString(R.string.edit_from_camera_text_description),
                            getString(R.string.edit_from_gallery_text_description),
                            getString(R.string.edit_from_instagram_text_description),
                            getString(R.string.action_editor),
                            getString(R.string.title_img_desc),
                            getString(R.string.title_img_default)};
//                }

            } else { // not primary image and not allow delete
                imageMenu = new CharSequence[]{
                        getString(R.string.title_img_desc),
                        getString(R.string.title_img_default)};
            }
        }
        builder.setItems(imageMenu, getImageAddProductListener());
        return builder.create();
    }

    private DialogInterface.OnClickListener getImageAddProductListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    CharSequence stringClicked = imageMenu[which];
                    if (stringClicked.equals(getString(R.string.title_img_delete))) {
                        mListener.clickRemoveImage(position);
                    } else if (stringClicked.equals(getString(R.string.edit_from_camera_text_description))) {
                        mListener.clickEditImagePathFromCamera(position);
                    } else if (stringClicked.equals(getString(R.string.edit_from_gallery_text_description))) {
                        mListener.clickEditImagePathFromGallery(position);
                    } else if (stringClicked.equals(getString(R.string.edit_from_instagram_text_description))) {
                        mListener.clickEditImagePathFromInstagram(position);
                    } else if (stringClicked.equals(getString(R.string.action_editor))) {
                        mListener.clickImageEditor(position);
                    } else if (stringClicked.equals(getString(R.string.title_img_desc))) {
                        mListener.clickEditImageDesc(position);
                    } else if (stringClicked.equals(getString(R.string.title_img_default))) {
                        mListener.clickEditImagePrimary(position);
                    }
                }
            }
        };
    }
}
