package com.tokopedia.seller.product.view.holder;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.product.view.adapter.ImageSelectorAdapter;
import com.tokopedia.seller.product.view.dialog.ImageDescriptionDialog;
import com.tokopedia.seller.product.view.dialog.ImageEditDialogFragment;
import com.tokopedia.seller.product.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.view.model.ImageSelectModel;
import com.tokopedia.seller.product.view.widget.ImagesSelectView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductImageViewHolder {

    public static final int REQUEST_CODE_IMAGE = 201;

    private ImagesSelectView imagesSelectView;
    private Fragment fragment;

    public ProductImageViewHolder(final Fragment fragment, View view) {
        this.fragment = fragment;
        imagesSelectView = (ImagesSelectView) view.findViewById(R.id.image_select_view);

        imagesSelectView.setOnImageSelectionListener(new ImageSelectorAdapter.OnImageSelectionListener() {
            @Override
            public void onAddClick(int position) {
                int remainingEmptySlot = imagesSelectView.getRemainingEmptySlot();
                GalleryActivity.moveToImageGallery(fragment.getActivity(), fragment, position, remainingEmptySlot);
            }

            @Override
            public void onItemClick(int position, final ImageSelectModel imageSelectModel) {
                showEditImageDialog(fragment.getActivity().getSupportFragmentManager(), position,
                        imageSelectModel.isPrimary());
            }
        });
        imagesSelectView.setOnCheckResolutionListener(new ImagesSelectView.OnCheckResolutionListener() {
            @Override
            public boolean isResolutionCorrect(String uri) {
                return true;
            }

            @Override
            public void resolutionCheckFailed(List<String> imagesStringList) {

            }
        });
    }

    public void showEditImageDialog(FragmentManager fm, int position, boolean isPrimary) {
        DialogFragment dialogFragment = ImageEditDialogFragment.newInstance(position, isPrimary);
        dialogFragment.show(fm, ImageEditDialogFragment.FRAGMENT_TAG);
        ((ImageEditDialogFragment) dialogFragment).setOnImageEditListener(new ImageEditDialogFragment.OnImageEditListener() {
            @Override
            public void clickEditImagePath(int position) {
                GalleryActivity.moveToImageGallery((AppCompatActivity) fragment.getActivity(), position, 1);
            }

            @Override
            public void clickEditImageDesc(int position) {
                String currentDescription = imagesSelectView.getImageAt(position).getDescription();
                ImageDescriptionDialog fragment = ImageDescriptionDialog.newInstance(currentDescription);
                fragment.show(fragment.getActivity().getSupportFragmentManager(), ImageDescriptionDialog.TAG);
                fragment.setListener(new ImageDescriptionDialog.OnImageDescDialogListener() {
                    @Override
                    public void onImageDescDialogOK(String newDescription) {
                        imagesSelectView.changeImageDesc(newDescription);
                    }
                });
            }

            @Override
            public void clickEditImagePrimary(int position) {
                imagesSelectView.changeImagePrimary(true, position);
            }

            @Override
            public void clickRemoveImage(int positions) {
                imagesSelectView.removeImage();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY && data != null) {
            int position = data.getIntExtra(GalleryActivity.ADD_PRODUCT_IMAGE_LOCATION, GalleryActivity.ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);
            String imageUrl = data.getStringExtra(GalleryActivity.IMAGE_URL);
            if (!TextUtils.isEmpty(imageUrl)) {
                if (position == imagesSelectView.getSelectedImageIndex()) {
                    imagesSelectView.changeImagePath(imageUrl);
                } else {
                    imagesSelectView.addImageString(imageUrl);
                }
            }

            ArrayList<String> imageUrls = data.getStringArrayListExtra(GalleryActivity.IMAGE_URLS);
            if (imageUrls != null) {
                imagesSelectView.addImagesString(imageUrls);
            }
        }
    }
}