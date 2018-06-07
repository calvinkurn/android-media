package com.tokopedia.seller.product.edit.view.imagepickerbuilder;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerMultipleSelectionBuilder;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.seller.R;

import java.util.ArrayList;

import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_ROTATE;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_INSTAGRAM;

/**
 * Created by hendry on 07/06/18.
 */

public class AddProductImagePickerBuilder {
    public static final int MAX_IMAGE_LIMIT = 5;

    public static ImagePickerBuilder createPrimaryNewBuilder(Context context, ArrayList<String> imageList) {
        return new ImagePickerBuilder(context.getString(R.string.choose_image),
                new int[]{TYPE_GALLERY, TYPE_CAMERA, TYPE_INSTAGRAM}, GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION, new int[]{1, 1}, true,
                new ImagePickerEditorBuilder(new int[]{ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE},
                        false)
                , new ImagePickerMultipleSelectionBuilder(
                imageList,
                null,
                0,
                MAX_IMAGE_LIMIT));
    }

    public static ImagePickerBuilder createVariantNewBuilder(Context context) {
        return new ImagePickerBuilder(context.getString(R.string.choose_image),
                new int[]{TYPE_GALLERY, TYPE_CAMERA, TYPE_INSTAGRAM}, GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION, new int[]{1, 1}, true,
                new ImagePickerEditorBuilder(new int[]{ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE},
                        false)
                , null);
    }

    public static Intent createPickerIntentPrimary(Context context, ArrayList<String> imageList) {
        ImagePickerBuilder builder = AddProductImagePickerBuilder.createPrimaryNewBuilder(context, imageList);
        return ImagePickerActivity.getIntent(context, builder);
    }

    public static Intent createPickerIntentVariant(Context context) {
        ImagePickerBuilder builder = AddProductImagePickerBuilder.createVariantNewBuilder(context);
        return ImagePickerActivity.getIntent(context, builder);
    }

    public static Intent createEditorIntent(Context context, String uriOrPath) {
        return ImageEditorActivity.getIntent(context, uriOrPath, ImagePickerBuilder.DEFAULT_MIN_RESOLUTION,
                new int[]{ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE},
                1, 1, false,
                ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB);
    }
}
