package com.tokopedia.seller.instoped;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.tokopedia.seller.common.imageeditor.ImageEditorActivity;
import com.tokopedia.seller.common.imageeditor.ImageEditorWatermarkActivity;

import java.util.ArrayList;

/**
 * Created by User on 10/13/2017.
 */

public class InstopedSellerCropperActivity extends InstopedSellerActivity {

    public static final String SAVED_DESC_LIST = "saved_sec_list";

    private ArrayList<String> imageDescriptionList;

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
        Intent moveToProductActivity = new Intent(context, InstopedSellerCropperActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_TO_SHOW, InstagramAuth.TAG);
        bundle.putInt(MAX_RESULT, maxResult);
        moveToProductActivity.putExtras(bundle);
        return moveToProductActivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!= null) {
            imageDescriptionList = savedInstanceState.getStringArrayList(SAVED_DESC_LIST);
        }
    }

    protected void finishWithSingleImage(String imageUrl){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(imageUrl);
        ImageEditorActivity.start(this,arrayList, true);
    }

    @Override
    public void finishWithMultipleImage(ArrayList<String> imageUrls, ArrayList<String> imageDescriptionList) {
        this.imageDescriptionList = imageDescriptionList;
        if (imageUrls!= null && imageUrls.size() == 1) {
            finishWithSingleImage(imageUrls.get(0));
        } else {
            super.finishWithMultipleImage(imageUrls, imageDescriptionList);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ImageEditorActivity.REQUEST_CODE: {
                if (data != null && resultCode==Activity.RESULT_OK && data.hasExtra(ImageEditorActivity.RESULT_IMAGE_PATH)) {
                    super.finishWithMultipleImage(
                            data.getStringArrayListExtra(ImageEditorActivity.RESULT_IMAGE_PATH), imageDescriptionList);
                }
            }
            break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(SAVED_DESC_LIST, imageDescriptionList);
    }
}
