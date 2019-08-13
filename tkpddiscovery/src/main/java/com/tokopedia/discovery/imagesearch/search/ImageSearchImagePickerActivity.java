package com.tokopedia.discovery.imagesearch.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.imagepicker.picker.gallery.model.MediaItem;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;

import static com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity.RESULT_IS_EDITTED;
import static com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity.RESULT_PREVIOUS_IMAGE;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP;

public class ImageSearchImagePickerActivity extends ImagePickerActivity {

    private SearchTracking searchTracking;
    private boolean isFromCamera;

    public static final String SAVED_IS_FROM_CAMERA = "saved_is_from_camera";
    public static final String RESULT_IS_FROM_CAMERA = "result_is_from_camera";

    public static Intent getIntent(Context context) {
        return new Intent(context, ImageSearchImagePickerActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initOnCreate(savedInstanceState);
        setIntentWithImagePickerBuilder();

        super.onCreate(savedInstanceState);
    }

    private void initOnCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            isFromCamera = savedInstanceState.getBoolean(SAVED_IS_FROM_CAMERA, false);
        }

        searchTracking = new SearchTracking(this, new UserSession(getApplicationContext()));
    }

    private void setIntentWithImagePickerBuilder() {
        Intent intent = getIntent();

        ImagePickerBuilder imageSearchImagePickerBuilder = createImageSearchImagePickerBuilder();

        // https://stackoverflow.com/questions/28589509/android-e-parcel-class-not-found-when-unmarshalling-only-on-samsung-tab3
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_IMAGE_PICKER_BUILDER, imageSearchImagePickerBuilder);
        intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, bundle);

        setIntent(intent);
    }

    private ImagePickerBuilder createImageSearchImagePickerBuilder() {
        String imagePickerTitle = getString(R.string.choose_image);
        @ImagePickerTabTypeDef int[] imagePickerTabs = createImagePickerTabs();
        ImagePickerEditorBuilder imagePickerEditorBuilder = createImagePickerEditorBuilder();

        return new ImagePickerBuilder (
                imagePickerTitle,
                imagePickerTabs,
                GalleryType.IMAGE_ONLY,
                ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                ImagePickerBuilder.IMAGE_SEARCH_MIN_RESOLUTION,
                null,
                true,
                imagePickerEditorBuilder,
                null
        );
    }

    @ImagePickerTabTypeDef
    private int[] createImagePickerTabs() {
        return new int[] {
                ImagePickerTabTypeDef.TYPE_GALLERY,
                ImagePickerTabTypeDef.TYPE_CAMERA
        };
    }

    private ImagePickerEditorBuilder createImagePickerEditorBuilder() {
        @ImageEditActionTypeDef int[] imageEditActionTypeList = createImageEditActionTypeList();
        ArrayList<ImageRatioTypeDef> allowedImageRatioList = createAllowedImageRatioList();

        return new ImagePickerEditorBuilder(imageEditActionTypeList, false, allowedImageRatioList);
    }

    @ImageEditActionTypeDef
    private int[] createImageEditActionTypeList() {
        return new int[] {
                ACTION_CROP,
                ACTION_BRIGHTNESS,
                ACTION_CONTRAST
        };
    }

    private ArrayList<ImageRatioTypeDef> createAllowedImageRatioList() {
        ArrayList<ImageRatioTypeDef> imageRatioTypeDefArrayList = new ArrayList<>();

        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.ORIGINAL);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_1_1);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_3_4);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_4_3);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_16_9);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_9_16);

        return imageRatioTypeDefArrayList;
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
