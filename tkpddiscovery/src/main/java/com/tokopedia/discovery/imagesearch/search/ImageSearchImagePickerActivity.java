package com.tokopedia.discovery.imagesearch.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.imagepicker.picker.gallery.model.MediaItem;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;

import static com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity.RESULT_IS_EDITTED;
import static com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity.RESULT_PREVIOUS_IMAGE;

public class ImageSearchImagePickerActivity extends ImagePickerActivity {

    private SearchTracking searchTracking;
    private boolean isFromCamera;

    public static final String SAVED_IS_FROM_CAMERA = "saved_is_from_camera";
    public static final String RESULT_IS_FROM_CAMERA = "result_is_from_camera";

    public static Intent getIntent(Context context, ImagePickerBuilder imagePickerBuilder) {
        Intent intent = new Intent(context, ImageSearchImagePickerActivity.class);
        // https://stackoverflow.com/questions/28589509/android-e-parcel-class-not-found-when-unmarshalling-only-on-samsung-tab3
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_IMAGE_PICKER_BUILDER, imagePickerBuilder);
        intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, bundle);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            isFromCamera = savedInstanceState.getBoolean(SAVED_IS_FROM_CAMERA, false);
        }

        searchTracking = new SearchTracking(this, new UserSession(getApplicationContext()));

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_IS_FROM_CAMERA, isFromCamera);
    }

    @Override
    public void onAlbumItemClicked(MediaItem item, boolean isChecked) {
        isFromCamera = false;
        super.onAlbumItemClicked(item, isChecked);
    }

    @Override
    public void onImageTaken(String filePath) {
        isFromCamera = true;
        super.onImageTaken(filePath);
    }

    protected void imagePickerViewPagerOnPageSelected(int position) {
        switch (imagePickerBuilder.getTabTypeDef(position)) {
            case ImagePickerTabTypeDef.TYPE_GALLERY:
                searchTracking.eventSearchImagePickerClickGallery();
                break;
            case ImagePickerTabTypeDef.TYPE_CAMERA:
                searchTracking.eventSearchImagePickerClickCamera();
                break;
            default:
                break;
        }

        super.imagePickerViewPagerOnPageSelected(position);
    }

    @Override
    protected void onFinishWithMultipleFinalImage(ArrayList<String> imageUrlOrPathList,
                                                  ArrayList<String> originalImageList,
                                                  ArrayList<String> imageDescriptionList,
                                                  ArrayList<Boolean> isEdittedList) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(PICKER_RESULT_PATHS, imageUrlOrPathList);
        intent.putStringArrayListExtra(RESULT_PREVIOUS_IMAGE, originalImageList);
        intent.putStringArrayListExtra(RESULT_IMAGE_DESCRIPTION_LIST, imageDescriptionList);
        intent.putExtra(RESULT_IS_EDITTED, isEdittedList);
        intent.putExtra(RESULT_IS_FROM_CAMERA, isFromCamera);

        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
