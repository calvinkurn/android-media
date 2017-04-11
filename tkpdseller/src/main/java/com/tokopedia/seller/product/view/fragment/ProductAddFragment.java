package com.tokopedia.seller.product.view.fragment;

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

import java.util.List;

/**
 * Created by nathan on 4/3/17.
 */

public class ProductAddFragment extends BaseDaggerFragment
        implements ImageEditDialogFragment.OnImageEditListener {

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
                        (AppCompatActivity) getActivity(), position, remainingEmptySlot);
            }

            @Override
            public void onItemClick(int position, final ImageSelectModel imageSelectModel) {
                showEditImageDialog(getActivity().getSupportFragmentManager(), position,
                        imageSelectModel.isPrimary(), ProductAddFragment.this);
            }
        });
        imagesSelectView.setOnCheckResolutionListener(new ImagesSelectView.OnCheckResolutionListener() {
            @Override
            public boolean isResolutionCorrect(String uri) {
                return false;
            }

            @Override
            public void resolutionCheckFailed(List<String> imagesStringList) {

            }
        });
        return view;
    }


    public static void showEditImageDialog(FragmentManager fm, int position, boolean isPrimary,
                                           ImageEditDialogFragment.OnImageEditListener listener){
        DialogFragment dialogFragment  = ImageEditDialogFragment.newInstance(position, isPrimary);
        dialogFragment.show(fm, ImageEditDialogFragment.FRAGMENT_TAG);
        ((ImageEditDialogFragment)dialogFragment).setOnImageEditListener(listener);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    // triggered after onActivityR Listener
    public void changeImageDescription(String newDescription) {
        imagesSelectView.changeImageDesc(newDescription);
    }

    /**
     * ADD OR EDIT Image
     */
    // triggered after onActivityResult from activity
    public void imageResultFromGallery(String imageUrl, int position) {
        // if add image on the selected, it means "change", else "add"
        if (position == imagesSelectView.getSelectedImageIndex()) {
            imagesSelectView.changeImagePath(imageUrl);
        }
        else {
            imagesSelectView.addImageString(imageUrl);
        }
    }

    // triggered after onActivityResult from activity
    public void imagesResultFromGallery(List<String> imageUrls, int position) {
        imagesSelectView.addImagesString(imageUrls);
    }

    /**
     * START EDIT IMAGE
     */
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
    }

    @Override
    public void clickEditImagePrimary(int position) {
        imagesSelectView.changeImagePrimary(true, position);
    }

    /**
     * START REMOVE IMAGE
     */
    @Override
    public void clickRemoveImage(int position) {
        imagesSelectView.removeImage(position);
    }
}
