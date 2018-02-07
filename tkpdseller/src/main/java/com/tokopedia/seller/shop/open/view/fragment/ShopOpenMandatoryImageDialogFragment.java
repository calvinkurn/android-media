package com.tokopedia.seller.shop.open.view.fragment;

import android.os.Bundle;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.view.dialog.ImageEditDialogFragment;

/**
 * Created by zulfikarrahman on 1/9/18.
 */

public class ShopOpenMandatoryImageDialogFragment extends ImageEditDialogFragment {

    public static ShopOpenMandatoryImageDialogFragment newInstance(int position) {
        ShopOpenMandatoryImageDialogFragment f = new ShopOpenMandatoryImageDialogFragment();
        Bundle args = new Bundle();
        args.putInt(IMAGE_PRODUCT_POSITION, position);
        f.setArguments(args);
        return f;
    }

    @Override
    protected String getWordingPickImageCamera() {
        return getString(R.string.shop_open_label_picker_image_camera);
    }

    @Override
    protected String getWordingPickImageGallery() {
        return getString(R.string.shop_open_label_picker_gallery);
    }
}
