package com.tokopedia.seller.product.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.fragment.ProductAddFragment;

import java.util.ArrayList;

import static com.tkpd.library.utils.CommonUtils.checkCollectionNotNull;
import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * Created by nathan on 4/3/17.
 */

public class ProductAddActivity extends TActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_product_add);
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, ProductAddFragment.createInstance(), ProductAddFragment.class.getSimpleName())
                .commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY && data != null) {
            int position = data.getIntExtra(GalleryActivity.ADD_PRODUCT_IMAGE_LOCATION,
                    GalleryActivity.ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);
            String imageUrl = data.getStringExtra(GalleryActivity.IMAGE_URL);
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(ProductAddFragment.TAG);
            if (checkNotNull(imageUrl) && fragment != null && fragment instanceof ProductAddFragment) {
                ((ProductAddFragment) fragment).imageResultFromGallery(imageUrl, position);
            }

            ArrayList<String> imageUrls = data.getStringArrayListExtra(GalleryActivity.IMAGE_URLS);
            if(checkCollectionNotNull(imageUrls) && fragment != null && fragment instanceof ProductAddFragment){
                ((ProductAddFragment) fragment).imagesResultFromGallery(imageUrls, position);
            }
        }

    }

}
