package com.tokopedia.seller.product.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.adapter.ImageSelectorAdapter;
import com.tokopedia.seller.product.view.dialog.ImageEditDialogFragment;
import com.tokopedia.seller.product.view.model.ImageSelectModel;
import com.tokopedia.seller.product.view.widget.ImagesSelectView;

import java.util.ArrayList;
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
            public void onAddClick() {
                // TODO show imagepicker to add images
                // This is just to test
                List<ImageSelectModel> imageSelectModelListToAdd = new ArrayList<>();
                imageSelectModelListToAdd.add(
                        new ImageSelectModel("https://upload.wikimedia.org/wikipedia/commons/4/4d/Nuvola_apps_kview.png",
                                null, false));
                imageSelectModelListToAdd.add(
                        new ImageSelectModel("https://maxcdn.icons8.com/Share/icon/Logos//google_logo1600.png",
                                null, false));
                imagesSelectView.addImages(imageSelectModelListToAdd);
            }

            @Override
            public void onItemClick(int position, final ImageSelectModel imageSelectModel) {
                // TODO on item clicked
                // show dialog to edit/change desc, make primary, remove
                showEditImageDialog(getActivity().getSupportFragmentManager(), position,
                        imageSelectModel.isPrimary(), ProductAddFragment.this);
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

    @Override
    public void clickEditImagePath(int position) {
        // TODO open image picker to get image to change
        // TODO test, remov below
        imagesSelectView.changeImagePath(
                    "https://maxcdn.icons8.com/Share/icon/Logos//google_logo1600.png");
    }

    @Override
    public void clickEditImageDesc(int position) {
        // TODO open dialog to show new description
        imagesSelectView.changeImageDesc(
                "Llllalala");
    }

    @Override
    public void clickEditImagePrimary(int position) {
        imagesSelectView.changeImagePrimary(true, position);
    }

    @Override
    public void clickRemoveImage(int position) {
        imagesSelectView.removeImage(position);
    }
}
