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

import com.theartofdev.edmodo.cropper.CropImageView;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.seller.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Hendry on 9/25/2017.
 */

public class ImageEditorActivity extends AppCompatActivity implements ImageEditorFragment.OnImageEditorFragmentListener{

    public static final int REQUEST_CODE = 520;
    public static final String EXTRA_IMAGE_URLS = "IMG_URLS";

    public static final String SAVED_IMAGE_INDEX = "IMG_IDX";
    public static final String RESULT_IMAGE_PATH = "RES_PATH";

    private ArrayList<String> imageUrls;
    private ArrayList<String> resultImageUrls;
    private int imageIndex;

    public static void start(Context context, Fragment fragment, ArrayList<String> imageUrls){
        Intent intent = createInstance(context, imageUrls);
        fragment.startActivityForResult(intent, REQUEST_CODE);
    }

    public static void start(Activity activity, ArrayList<String> imageUrls){
        Intent intent = createInstance(activity, imageUrls);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public static Intent createInstance(Context context, ArrayList<String> imageUrls){
        Intent intent = new Intent(context, ImageEditorActivity.class);
        intent.putExtra(EXTRA_IMAGE_URLS, imageUrls);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_editor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        imageUrls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
        if (imageUrls == null || imageUrls.size() == 0) {
            finish();
            return;
        }
        if (savedInstanceState == null) {
            imageIndex = 0;
            copyOriginalUrlsToResult();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ImageEditorFragment.newInstance(
                            imageUrls.get(imageIndex)), ImageEditorFragment.TAG)
                    .commit();
        } else {
            imageIndex = savedInstanceState.getInt(SAVED_IMAGE_INDEX, 0);
            resultImageUrls = savedInstanceState.getStringArrayList(RESULT_IMAGE_PATH);
        }
        setUpToolbarTitle();
    }

    private void copyOriginalUrlsToResult(){
        resultImageUrls = new ArrayList<>();
        for (int i =0, sizei = imageUrls.size(); i<sizei; i++) {
            resultImageUrls.add(imageUrls.get(i));
        }
    }

    @Override
    public void onSuccessCrop(CropImageView.CropResult cropResult) {
        Bitmap bitmap = cropResult.getBitmap();
        if (bitmap != null) {
            File file = FileUtils.writeTempStateStoreBitmap(this, bitmap);
            if (file!= null && file.exists()) {
                String path = file.getAbsolutePath();
                resultImageUrls.set(imageIndex, path);
                imageIndex++;
                if (imageIndex == imageUrls.size()) {
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_IMAGE_PATH, resultImageUrls);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
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

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            imageIndex--;
            setUpToolbarTitle();
            getSupportFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }

    private void setUpToolbarTitle(){
        String title = getString(R.string.title_activity_image_edit);
        if (imageUrls.size() > 1) {
            title += "("+(imageIndex+1) + "/" + imageUrls.size()+")";
        }
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_IMAGE_INDEX, imageIndex);
        outState.putStringArrayList(RESULT_IMAGE_PATH, resultImageUrls);
    }
}
