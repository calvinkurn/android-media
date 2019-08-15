package com.tokopedia.seller.product.variant.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.tokopedia.imagepicker.common.util.FileUtils;
import com.tokopedia.imagepicker.common.util.ImageUtils;
import com.tokopedia.product.manage.item.imagepicker.imagepickerbuilder.AddProductImagePickerBuilder;
import com.tokopedia.product.manage.item.variant.dialog.ProductAddImageEditVariantDialogFragment;

import java.util.ArrayList;

import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;

/**
 * Created by hendry on 4/3/17.
 */

public abstract class BaseImageFragment extends Fragment {

    public static final int REQUEST_CODE_ADD_VARIANT_IMAGE = 3901;
    public static final int REQUEST_CODE_EDIT_VARIANT_IMAGE = 3902;

    public abstract boolean needRetainImage();
    public abstract void changeModelBasedImageUrlOrPath(String imageUrl);
    public abstract void refreshImageView();

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultImage(requestCode, resultCode, data);
    }

    public void onActivityResultImage(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_VARIANT_IMAGE || requestCode == REQUEST_CODE_EDIT_VARIANT_IMAGE) {
            if (resultCode == Activity.RESULT_OK &&
                    data != null) {
                ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
                if (imageUrlOrPathList != null && imageUrlOrPathList.size() > 0) {
                    String imagePath = imageUrlOrPathList.get(0);
                    if (!TextUtils.isEmpty(imagePath)) {
                        addOrChangeImage(imagePath);
                    }
                }
            }
        }
    }

    private void addOrChangeImage(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            changeModelBasedImageUrlOrPath(imageUrl);
            refreshImageView();
        }
    }

    protected void showAddImageDialog(){
        Intent intent = AddProductImagePickerBuilder.createPickerIntentVariant(getContext());
        startActivityForResult(intent, REQUEST_CODE_ADD_VARIANT_IMAGE);
    }

    protected void showEditImageDialog(final String uriOrPath){
        if (TextUtils.isEmpty(uriOrPath)) {
            showAddImageDialog();
            return;
        }
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DialogFragment dialogFragment = ProductAddImageEditVariantDialogFragment.newInstance();
        dialogFragment.show(fm, ProductAddImageEditVariantDialogFragment.FRAGMENT_TAG);
        ((ProductAddImageEditVariantDialogFragment) dialogFragment).setOnImageEditListener(new ProductAddImageEditVariantDialogFragment.OnImageEditListener() {

            @Override
            public void clickChangeImagePath() {
                Intent intent = AddProductImagePickerBuilder.createPickerIntentVariant(getContext());
                startActivityForResult(intent, REQUEST_CODE_ADD_VARIANT_IMAGE);
            }

            @Override
            public void clickImageEditor() {
                if (!TextUtils.isEmpty(uriOrPath)) {
                    Intent editorIntent = AddProductImagePickerBuilder.createEditorIntent(getContext(), uriOrPath);
                    startActivityForResult(editorIntent, REQUEST_CODE_EDIT_VARIANT_IMAGE);
                }
            }

            @Override
            public void clickRemoveImage() {
                changeModelBasedImageUrlOrPath(null);
                refreshImageView();

                if (!TextUtils.isEmpty(uriOrPath) && !needRetainImage()) {
                    FileUtils.deleteFileInTokopediaFolder(uriOrPath);
                }
            }
        });
    }

}