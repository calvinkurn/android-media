package com.tokopedia.seller.common.imageeditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.myproduct.fragment.ImageGalleryAlbumFragment;
import com.tokopedia.core.newgallery.GalleryActivity;

import java.util.ArrayList;

/**
 * Created by Hendry on 10/10/2017.
 */

public class GalleryCropWatermarkActivity extends GalleryCropActivity {

    public static void moveToImageGalleryCamera(Activity context, int position, boolean forceOpenCamera,
                                                int maxImageSelection) {
        moveToImageGalleryCamera(context, position, forceOpenCamera, maxImageSelection, false);
    }
    public static void moveToImageGallery(Context context, Fragment fragment, int position, int maxSelection, boolean compressToTkpd) {
        moveToImageGalleryCamera(context, fragment, position, false, maxSelection, compressToTkpd);
    }
    public static void moveToImageGallery(Activity activity, int position, int maxSelection, boolean compressToTkpd) {
        moveToImageGalleryCamera(activity, position, false, maxSelection, compressToTkpd);
    }
    public static void moveToImageGalleryCamera(Context context, Fragment fragment,
                                                int position,
                                                boolean forceOpenCamera,
                                                int maxImageSelection,
                                                boolean compressToTkpd) {
        Intent imageGallery = createIntent(context, position, forceOpenCamera, maxImageSelection, compressToTkpd);
        fragment.startActivityForResult(imageGallery, com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY);
    }

    public static void moveToImageGalleryCamera(Activity context, int position, boolean forceOpenCamera,
                                                int maxImageSelection,
                                                boolean compressToTkpd) {
        Intent imageGallery = createIntent(context, position, forceOpenCamera, maxImageSelection, compressToTkpd);
        context.startActivityForResult(imageGallery, com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY);
    }

    public static void moveToImageGalleryCamera(Context context, Fragment fragment,
                                                int position,
                                                boolean forceOpenCamera,
                                                int maxImageSelection) {
        moveToImageGalleryCamera(context, fragment, position, forceOpenCamera, maxImageSelection, false);
    }

    public static Intent createIntent(Context context, int position,
                                       boolean forceOpenCamera,
                                       int maxImageSelection,
                                       boolean compressToTkpd) {
        Intent imageGallery = new Intent(context, GalleryCropWatermarkActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ADD_PRODUCT_IMAGE_LOCATION, position);
        bundle.putString(FRAGMENT_TO_SHOW, ImageGalleryAlbumFragment.FRAGMENT_TAG);
        bundle.putBoolean(FORCE_OPEN_CAMERA, forceOpenCamera);
        bundle.putInt(MAX_IMAGE_SELECTION, maxImageSelection);
        bundle.putBoolean(COMPRESS_TO_TKPD, compressToTkpd);
        imageGallery.putExtras(bundle);
        return imageGallery;
    }

    @Override
    public void finishWithSingleImage(String imageUrl){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(imageUrl);
        ImageEditorWatermarkActivity.start(this,arrayList, true);
    }

}
