package com.tokopedia.seller.common.imageeditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
    public static final String EXTRA_WATERMARK_TEXT = "WTRMK_TEXT";

    public static final String SAVED_IMAGE_INDEX = "IMG_IDX";
    public static final String SAVED_IMAGE_URLS = "SAVED_IMG_URLS";
    public static final String SAVED_ALL_CROPPED_PATHS = "SAVED_CROPPED_PATHS";
    public static final String RESULT_IMAGE_PATH = "RES_PATH";

    private ArrayList<String> imageUrls;
    private ArrayList<String> resultImageUrls;

    // store the cropped paths (and the original), so the unused files can be deleted later
    private ArrayList<String> savedCroppedPaths;

    private int imageIndex;

    private TkpdProgressDialog progressDialog;
    private String watermarkText;

    public static void start(Context context, Fragment fragment, ArrayList<String> imageUrls, String watermarkText) {
        Intent intent = createInstance(context, imageUrls, watermarkText);
        fragment.startActivityForResult(intent, REQUEST_CODE);
    }

    public static void start(Activity activity, ArrayList<String> imageUrls, String watermarkText) {
        Intent intent = createInstance(activity, imageUrls, watermarkText);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public static Intent createInstance(Context context, ArrayList<String> imageUrls, String watermarkText) {
        Intent intent = new Intent(context, ImageEditorActivity.class);
        intent.putExtra(EXTRA_IMAGE_URLS, imageUrls);
        intent.putExtra(EXTRA_WATERMARK_TEXT, watermarkText);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_editor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        watermarkText = getIntent().getStringExtra(EXTRA_WATERMARK_TEXT);
        if (savedInstanceState == null) {
            if (getIntent().hasExtra(EXTRA_IMAGE_URLS)) {
                imageUrls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
            } else {
                finish();
                return;
            }
            imageIndex = 0;
            resultImageUrls = new ArrayList<>();
            savedCroppedPaths = new ArrayList<>();
        } else {
            imageIndex = savedInstanceState.getInt(SAVED_IMAGE_INDEX, 0);
            imageUrls = savedInstanceState.getStringArrayList(SAVED_IMAGE_URLS);
            resultImageUrls = savedInstanceState.getStringArrayList(RESULT_IMAGE_PATH);
            savedCroppedPaths = savedInstanceState.getStringArrayList(SAVED_ALL_CROPPED_PATHS);
        }

        if (resultImageUrls == null || resultImageUrls.size() == 0) {
            boolean isNetworkImage = false;
            for (int i = 0, sizei = imageUrls.size(); i < sizei; i++) {
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


    private void startEditLocalImages() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(ImageEditorFragment.TAG) == null) {
            replaceEditorFragment(fragmentManager);
        }
        setUpToolbarTitle();
    }

    private void replaceEditorFragment(FragmentManager fragmentManager){
        fragmentManager.beginTransaction()
                .replace(R.id.container, ImageEditorFragment.newInstance(
                        imageUrls.get(imageIndex)), ImageEditorFragment.TAG)
                .commit();
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
            progressDialog.setCancelable(false);
        }
        if (!progressDialog.isProgress()) {
            progressDialog.showDialog();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isProgress()) {
            progressDialog.dismiss();
        }
    }

    private void copyOriginalUrlsToResult() {
        resultImageUrls = new ArrayList<>();
        savedCroppedPaths = new ArrayList<>();
        for (int i = 0, sizei = imageUrls.size(); i < sizei; i++) {
            resultImageUrls.add(imageUrls.get(i));
            savedCroppedPaths.add(imageUrls.get(i));
        }
    }

    @Override
    public void onSuccessCrop(CropImageView.CropResult cropResult) {
        Bitmap bitmap = cropResult.getBitmap();
        if (bitmap != null) {
            File file = FileUtils.writeImageToTkpdPath(bitmap, FileUtils.generateUniqueFileName());
            if (file != null && file.exists()) {
                String path = file.getAbsolutePath();

                // save the new path
                resultImageUrls.set(imageIndex, path);
                savedCroppedPaths.add(path);
                imageIndex++;
                if (imageIndex == imageUrls.size()) {
                    finishEditing(true);
                } else {
                    // continue to next image index
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    replaceEditorFragment(fragmentManager);
                    setUpToolbarTitle();
                }
            }
        }
    }

    private void finishEditing(boolean isResultOK) {
        Intent intent = new Intent();
        if (isResultOK) {
            intent.putExtra(RESULT_IMAGE_PATH, resultImageUrls);
            deleteAllTkpdFilesNotInResult(savedCroppedPaths, resultImageUrls);
        } else {
            intent.putExtra(RESULT_IMAGE_PATH, getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS));
            deleteAllTkpdFilesNotInResult(savedCroppedPaths, getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS));
        }
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void deleteAllTkpdFilesNotInResult(ArrayList<String> savedCroppedPaths, ArrayList<String> resultImageUrls){
        ArrayList<String> toBeDeletedFiles = new ArrayList<>();
        for (int i=0, sizei = savedCroppedPaths.size(); i<sizei; i++) {
            String savedCroppedPath = savedCroppedPaths.get(i);
            boolean croppedFilesIsInResult = false;
            for (int j = 0, sizej = resultImageUrls.size(); j<sizej; j++) {
                if (savedCroppedPath.equals(resultImageUrls.get(j))) {
                    croppedFilesIsInResult = true;
                    break;
                }
            }
            if (!croppedFilesIsInResult) {
                toBeDeletedFiles.add(savedCroppedPath);
            }
        }
        FileUtils.deleteAllCacheTkpdFiles(toBeDeletedFiles);
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
        outState.putStringArrayList(SAVED_ALL_CROPPED_PATHS, savedCroppedPaths);
    }
}
