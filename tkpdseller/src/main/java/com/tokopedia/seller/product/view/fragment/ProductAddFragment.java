package com.tokopedia.seller.product.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.adapter.ImageSelectorAdapter;
import com.tokopedia.seller.product.view.dialog.ImageDescriptionDialog;
import com.tokopedia.seller.product.view.dialog.ImageEditDialogFragment;
import com.tokopedia.seller.product.view.model.ImageSelectModel;
import com.tokopedia.seller.product.view.widget.ImagesSelectView;

import java.util.ArrayList;
import java.util.List;

import static com.tkpd.library.utils.CommonUtils.checkCollectionNotNull;
import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * Created by nathan on 4/3/17.
 */

public class ProductAddFragment extends BaseDaggerFragment {

    public static final String TAG = ProductAddFragment.class.getSimpleName();

    ImagesSelectView imagesSelectView;

    public static ProductAddFragment createInstance() {
        ProductAddFragment fragment = new ProductAddFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_add, container, false);
        imagesSelectView = (ImagesSelectView) view.findViewById(R.id.image_select_view);

        imagesSelectView.setOnImageSelectionListener(new ImageSelectorAdapter.OnImageSelectionListener() {
            @Override
            public void onAddClick(int position) {
                int remainingEmptySlot = imagesSelectView.getRemainingEmptySlot();
                GalleryActivity.moveToImageGallery(
                        getActivity(),ProductAddFragment.this, position, remainingEmptySlot);
            }

            @Override
            public void onItemClick(int position, final ImageSelectModel imageSelectModel) {
                showEditImageDialog(getActivity().getSupportFragmentManager(), position,
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
        return view;
    }


    public void showEditImageDialog(FragmentManager fm, int position, boolean isPrimary){
        DialogFragment dialogFragment  = ImageEditDialogFragment.newInstance(position, isPrimary);
        dialogFragment.show(fm, ImageEditDialogFragment.FRAGMENT_TAG);
        ((ImageEditDialogFragment)dialogFragment).setOnImageEditListener(new ImageEditDialogFragment.OnImageEditListener() {
            @Override
            public void clickEditImagePath(int position) {
                GalleryActivity.moveToImageGallery(
                        (AppCompatActivity) getActivity(), position, 1 );
            }

            @Override
            public void clickEditImageDesc(int position) {
                String currentDescription = imagesSelectView.getImageAt(position).getDescription();
                ImageDescriptionDialog fragment = ImageDescriptionDialog.newInstance(currentDescription);
                fragment.show(getActivity().getSupportFragmentManager(), ImageDescriptionDialog.TAG);
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY && data != null) {
            int position = data.getIntExtra(GalleryActivity.ADD_PRODUCT_IMAGE_LOCATION,
                    GalleryActivity.ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);
            String imageUrl = data.getStringExtra(GalleryActivity.IMAGE_URL);
            if (checkNotNull(imageUrl)) {
                if (position == imagesSelectView.getSelectedImageIndex()) {
                    imagesSelectView.changeImagePath(imageUrl);
                }
                else {
                    imagesSelectView.addImageString(imageUrl);
                }
            }

            ArrayList<String> imageUrls = data.getStringArrayListExtra(GalleryActivity.IMAGE_URLS);
            if(checkCollectionNotNull(imageUrls)){
                imagesSelectView.addImagesString(imageUrls);
            }
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }


}
