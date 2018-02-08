package com.tokopedia.seller.instoped;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.myproduct.utils.ImageDownloadHelper;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.manage.view.fragment.ProductManageSellerFragment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by User on 10/13/2017.
 */

public class InstopedSellerActivity extends InstopedActivity {

    public static final int MIN_IMG_RESOLUTION = 300;

    public static final String EXTRA_IMAGE_DESC_LIST = "x_instoped_image_desc";

    private TkpdProgressDialog progressDialog;

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
        Intent moveToProductActivity = new Intent(context, InstopedSellerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_TO_SHOW, InstagramAuth.TAG);
        bundle.putInt(MAX_RESULT, maxResult);
        moveToProductActivity.putExtras(bundle);
        return moveToProductActivity;
    }

    private void showProgressDialog(){
        if (progressDialog == null) {
            progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
            progressDialog.setCancelable(false);
        }
        if (! progressDialog.isProgress()) {
            progressDialog.showDialog();
        }
    }

    private void hideProgressDialog(){
        if (progressDialog != null && progressDialog.isProgress()) {
            progressDialog.dismiss();
        }
    }

    private class ImageResolutionException extends RuntimeException {

    }

    @Override
    public void setResultToCaller(ArrayList<InstagramMediaModel> images){
        showProgressDialog();
        final ArrayList<String> standardResoImageUrlList = new ArrayList<>();
        final ArrayList<String> imageDescriptionList = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            InstagramMediaModel instagramMediaModel = images.get(i);
            standardResoImageUrlList.add(instagramMediaModel.standardResolution);
            imageDescriptionList.add(instagramMediaModel.captionText == null? "":instagramMediaModel.captionText);
        }
        ImageDownloadHelper imageDownloadHelper = new ImageDownloadHelper(this);
        imageDownloadHelper.convertHttpPathToLocalPath(standardResoImageUrlList,
                ProductManageSellerFragment.DEFAULT_NEED_COMPRESS_TKPD,
                new ImageDownloadHelper.OnImageDownloadListener() {
                    @Override
                    public void onError(Throwable e) {
                        hideProgressDialog();
                        if (e instanceof ImageResolutionException) {
                            NetworkErrorHelper.showCloseSnackbar(
                                    InstopedSellerActivity.this, getString(R.string.product_instagram_draft_error_save_resolution));
                        } else {
                            NetworkErrorHelper.showCloseSnackbar(
                                    InstopedSellerActivity.this, ErrorHandler.getErrorMessage(e));
                        }
                    }

                    @Override
                    public void onSuccess(ArrayList<String> localPaths) {
                        hideProgressDialog();
                        // if the path is different with the original,
                        // means no all draft is saved to local for some reasons
                        if (localPaths == null || localPaths.size() == 0 ||
                                localPaths.size() != standardResoImageUrlList.size()) {
                            throw new NullPointerException();
                        }

                        // check if there is any incorrect image resolution
                        for (int i=0, sizei = localPaths.size(); i < sizei ; i++) {
                            String localPath = localPaths.get(i);
                            if (!isResolutionCorrect(localPath)) {
                                throw new ImageResolutionException();
                            }
                        }
                        finishWithMultipleImage(localPaths, imageDescriptionList);
                    }
                });
    }

    public void finishWithMultipleImage(ArrayList<String> imageUrls, ArrayList<String> imageDescriptionList){
        Intent intent = new Intent();
        if (imageUrls!= null && imageUrls.size() == 1) {
            intent.putExtra(GalleryActivity.IMAGE_URL, imageUrls.get(0));
        }
        intent.putStringArrayListExtra(GalleryActivity.IMAGE_URLS, imageUrls);
        intent.putStringArrayListExtra(EXTRA_IMAGE_DESC_LIST, imageDescriptionList);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private boolean isResolutionCorrect(String localPath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(localPath).getAbsolutePath(), options);
        if (Math.min(options.outWidth, options.outHeight) >= MIN_IMG_RESOLUTION){
            return true;
        }
        return false;
    }

}
