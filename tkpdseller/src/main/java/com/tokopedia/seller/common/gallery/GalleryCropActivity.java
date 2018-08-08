package com.tokopedia.seller.common.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.gallery.GalleryActivity;
import com.tokopedia.core.gallery.MediaItem;
import com.tokopedia.seller.common.imageeditor.ImageEditorActivity;

import java.util.ArrayList;

/**
 * Created by zulfikarrahman on 1/4/18.
 */

public class GalleryCropActivity extends GalleryActivity {

    public static final String RESULT_IMAGE_CROPPED = "RESULT_IMAGE_CROPPED";

    public static Intent createIntent(Context context) {
        return createIntent(context, DEFAULT_GALLERY_TYPE, false);
    }

    public static Intent createIntent(Context context, int galleryType, boolean compressToTkpd) {
        return createIntent(context, galleryType, DEFAULT_MAX_SELECTION, compressToTkpd);
    }

    public static Intent createIntent(Context context, int galleryType, int maxSelection, boolean compressToTkpd) {
        Intent intent = new Intent(context, GalleryCropActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_GALLERY_TYPE, galleryType);
        bundle.putInt(BUNDLE_MAX_SELECTION, maxSelection);
        bundle.putBoolean(COMPRESS_TO_TKPD, compressToTkpd);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void finishWithPathFile(String absolutePath) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(absolutePath);
        ImageEditorActivity.start(this,arrayList, true);
    }

    @Override
    protected void finishWithMediaItem(MediaItem item) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(item.getRealPath());
        ImageEditorActivity.start(this,arrayList, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ImageEditorActivity.REQUEST_CODE: {
                if (data != null && resultCode== Activity.RESULT_OK && data.hasExtra(ImageEditorActivity.RESULT_IMAGE_PATH)) {
                    ArrayList<String> dataListResultImage = data.getStringArrayListExtra(ImageEditorActivity.RESULT_IMAGE_PATH);
                    if(dataListResultImage != null && dataListResultImage.size() >0) {
                        Intent intent = new Intent();
                        intent.putExtra(RESULT_IMAGE_CROPPED, dataListResultImage.get(0));
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }
            }
            break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
