package com.tokopedia.seller.common.imageeditor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.theartofdev.edmodo.cropper.CropImageView;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.myproduct.utils.ImageDownloadHelper;
import com.tokopedia.seller.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Hendry on 9/25/2017.
 */

public class ImageEditorActivity extends AppCompatActivity implements ImageEditorFragment.OnImageEditorFragmentListener {

    public static final int REQUEST_CODE = 520;
    public static final String EXTRA_IMAGE_URLS = "IMG_URLS";
    public static final String EXTRA_IMAGE_URL = "IMG_URL";

    public static final String SAVED_IMAGE_INDEX = "IMG_IDX";
    public static final String SAVED_IMAGE_URLS = "SAVED_IMG_URLS";
    public static final String RESULT_IMAGE_PATH = "RES_PATH";

    private ArrayList<String> imageUrls;
    private ArrayList<String> resultImageUrls;
    private int imageIndex;

    private TkpdProgressDialog progressDialog;

    public static void start(Context context, Fragment fragment, String imageUrl) {
        Intent intent = createInstance(context, imageUrl);
        fragment.startActivityForResult(intent, REQUEST_CODE);
    }

    public static void start(Activity activity,String imageUrl) {
        Intent intent = createInstance(activity, imageUrl);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    //    public static void start(Context context, Fragment fragment, ArrayList<String> imageUrls) {
//        Intent intent = createInstance(context, imageUrls);
//        fragment.startActivityForResult(intent, REQUEST_CODE);
//    }
//

//    public static Intent createInstance(Context context, ArrayList<String> imageUrls) {
//        Intent intent = new Intent(context, ImageEditorActivity.class);
//        intent.putExtra(EXTRA_IMAGE_URLS, imageUrls);
//        return intent;
//    }

    public static Intent createInstance(Context context, String imageUrl) {
        Intent intent = new Intent(context, ImageEditorActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, imageUrl);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_editor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            if (getIntent().hasExtra(EXTRA_IMAGE_URL)) {
                String imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
                imageUrls = new ArrayList<>();
                imageUrls.add(imageUrl);
            } else if (getIntent().hasExtra(EXTRA_IMAGE_URLS)) {
                imageUrls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
            } else {
                finish();
                return;
            }
            imageIndex = 0;
            resultImageUrls = new ArrayList<>();
        } else {
            imageIndex = savedInstanceState.getInt(SAVED_IMAGE_INDEX, 0);
            imageUrls = savedInstanceState.getStringArrayList(SAVED_IMAGE_URLS);
            resultImageUrls = savedInstanceState.getStringArrayList(RESULT_IMAGE_PATH);
        }

        if (resultImageUrls== null || resultImageUrls.size() == 0) {
            boolean isNetworkImage = false;
            for (int i=0, sizei = imageUrls.size(); i<sizei; i++) {
                if (imageUrls.get(i).startsWith("http")) {
                    isNetworkImage = true;
                    break;
                }
            }
            if (isNetworkImage) {
                showProgressDialog();
                ImageDownloadHelper imageDownloadHelper = new ImageDownloadHelper(this);
                imageDownloadHelper.convertHttpPathToLocalPath(imageUrls, true,
                        new ImageDownloadHelper.OnImageDownloadListener() {
                            @Override
                            public void onError(Throwable e) {
                                hideProgressDialog();
                            }

                            @Override
                            public void onSuccess(ArrayList<String> resultLocalPaths) {
                                hideProgressDialog();
                                imageUrls = resultLocalPaths;
                                copyOriginalUrlsToResult();
                                startEditLocalImages();
                            }
                        });
            } else {
                copyOriginalUrlsToResult();
                startEditLocalImages();
            }
        } else {
            copyOriginalUrlsToResult();
            startEditLocalImages();
        }

    }


    private void startEditLocalImages(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(ImageEditorFragment.TAG) == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ImageEditorFragment.newInstance(
                            imageUrls.get(imageIndex)), ImageEditorFragment.TAG)
                    .commit();
        }
        setUpToolbarTitle();
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

    private void copyOriginalUrlsToResult() {
        resultImageUrls = new ArrayList<>();
        for (int i = 0, sizei = imageUrls.size(); i < sizei; i++) {
            resultImageUrls.add(imageUrls.get(i));
        }
    }

    @Override
    public void onSuccessCrop(CropImageView.CropResult cropResult) {
        Bitmap bitmap = cropResult.getBitmap();
        if (bitmap != null) {
            File file = FileUtils.writeImageToTkpdPath(bitmap, FileUtils.generateUniqueFileName());
            if (file != null && file.exists()) {
                String path = file.getAbsolutePath();
                resultImageUrls.set(imageIndex, path);
                imageIndex++;
                if (imageIndex == imageUrls.size()) {
                    finishEditing(true);
                } else {
                    // continue to next image index
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, ImageEditorFragment.newInstance(
                                    imageUrls.get(imageIndex)), ImageEditorFragment.TAG)
                            .addToBackStack(String.valueOf(imageIndex))
                            .commit();
                    setUpToolbarTitle();
                }
            }
        }
    }

    private void finishEditing(boolean isResultOK){
        Intent intent = new Intent();
        intent.putExtra(RESULT_IMAGE_PATH, resultImageUrls);
        setResult(isResultOK? Activity.RESULT_OK: Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            imageIndex--;
            setUpToolbarTitle();
            getSupportFragmentManager().popBackStack();
        } else {
            finishEditing(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void setUpToolbarTitle() {
        String title = getString(R.string.title_activity_image_edit);
        if (imageUrls.size() > 1) {
            title += "(" + (imageIndex + 1) + "/" + imageUrls.size() + ")";
        }
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_IMAGE_INDEX, imageIndex);
        outState.putStringArrayList(RESULT_IMAGE_PATH, resultImageUrls);
        outState.putStringArrayList(SAVED_IMAGE_URLS, imageUrls);
    }
}
