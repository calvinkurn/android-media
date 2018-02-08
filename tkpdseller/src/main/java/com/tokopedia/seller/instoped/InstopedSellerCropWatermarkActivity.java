package com.tokopedia.seller.instoped;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.common.imageeditor.ImageEditorActivity;
import com.tokopedia.seller.common.imageeditor.ImageEditorWatermarkActivity;

import java.util.ArrayList;

/**
 * Created by User on 10/13/2017.
 */

public class InstopedSellerCropWatermarkActivity extends InstopedSellerCropperActivity {

    public static void startInstopedActivity(Context context){
        Intent moveToProductActivity = new Intent(context, InstopedActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_TO_SHOW, InstagramAuth.TAG);
        moveToProductActivity.putExtras(bundle);
        context.startActivity(moveToProductActivity);
    }

    public static void startInstopedActivityForResult(Activity activity, int resultCode, int maxResult){
        Intent moveToProductActivity = createIntent(activity, maxResult);
        activity.startActivityForResult(moveToProductActivity, resultCode);
    }

    public static void startInstopedActivityForResult(Context context, Fragment fragment, int resultCode, int maxResult){
        Intent moveToProductActivity = createIntent(context, maxResult);
        fragment.startActivityForResult(moveToProductActivity, resultCode);
    }

    private static Intent createIntent (Context context, int maxResult){
        Intent moveToProductActivity = new Intent(context, InstopedSellerCropWatermarkActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_TO_SHOW, InstagramAuth.TAG);
        bundle.putInt(MAX_RESULT, maxResult);
        moveToProductActivity.putExtras(bundle);
        return moveToProductActivity;
    }

    @Override
    protected void finishWithSingleImage(String imageUrl){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(imageUrl);
        ImageEditorWatermarkActivity.start(this,arrayList, true);
    }

}
