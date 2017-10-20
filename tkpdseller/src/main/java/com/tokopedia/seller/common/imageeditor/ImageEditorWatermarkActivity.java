package com.tokopedia.seller.common.imageeditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.myproduct.utils.ImageDownloadHelper;
import com.tokopedia.seller.R;

import java.util.ArrayList;

/**
 * Created by Hendry on 9/25/2017.
 */

public class ImageEditorWatermarkActivity extends ImageEditorActivity {

    public static void start(Context context, Fragment fragment, ArrayList<String> imageUrls, boolean delCacheWhenExit) {
        Intent intent = createInstance(context, imageUrls, delCacheWhenExit);
        fragment.startActivityForResult(intent, ImageEditorActivity.REQUEST_CODE);
    }

    public static void start(Activity activity, ArrayList<String> imageUrls, boolean delCacheWhenExit) {
        Intent intent = createInstance(activity, imageUrls, delCacheWhenExit);
        activity.startActivityForResult(intent, ImageEditorActivity.REQUEST_CODE);
    }

    public static Intent createInstance(Context context, ArrayList<String> imageUrls, boolean delCacheWhenExit) {
        Intent intent = new Intent(context, ImageEditorWatermarkActivity.class);
        intent.putExtra(ImageEditorActivity.EXTRA_IMAGE_URLS, imageUrls);
        intent.putExtra(ImageEditorActivity.EXTRA_DELETE_CACHE_WHEN_EXIT, delCacheWhenExit);
        return intent;
    }

    @Override
    protected ImageEditorFragment getNewEditorFragment() {
        return ImageEditorWatermarkFragment.newInstance(getImageUrl());
    }
}
