package com.tokopedia.inbox.rescenter.detail.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core2.R;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.rescenter.utils.LocalCacheManager;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created by hangnadi on 2/29/16.
 */
public class UploadImageDialog {

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int RESULT_CODE = 323;
    private final Context context;
    private final String resolutionID;
    private Activity activity;
    private Fragment fragment;
    private String cameraFileLoc;
    private LocalCacheManager.ImageAttachment cache;

    public interface UploadImageDialogListener {
        void onSuccess(List<AttachmentResCenterVersion2DB> data);
        void onFailed();
    }

    public UploadImageDialog(Fragment fragment, String resolutionID) {
        this.fragment = fragment;
        this.resolutionID = resolutionID;
        this.context = fragment.getActivity();
        this.cache = LocalCacheManager.ImageAttachment.Builder(resolutionID);
    }

    public UploadImageDialog(Activity activity, String resolutionID) {
        this.activity = activity;
        this.resolutionID = resolutionID;
        this.context = activity;
        this.cache = LocalCacheManager.ImageAttachment.Builder(resolutionID);
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.dialog_upload_option));
        builder.setPositiveButton(context.getString(R.string.title_gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openImagePicker();
            }
        }).setNegativeButton(context.getString(R.string.title_camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openCamera();
            }
        });

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    public void openImagePicker() {
        Intent imageGallery = new Intent(context, GalleryBrowser.class);
        startActivity(imageGallery, REQUEST_GALLERY);
    }

    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri());
        startActivity(intent, REQUEST_CAMERA);
    }

    private Uri getOutputMediaFileUri() {
        return MethodChecker.getUri(context, getOutputMediaFile());
    }

    private void startActivity(Intent intent, int requestCode) {
        if (activity != null) {
            activity.startActivityForResult(intent, requestCode);
        } else {
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    public File getOutputMediaFile() {
        String imageCode = uniqueCode();
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory() + File.separator
                        + "Tokopedia" + File.separator);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + imageCode + ".jpg");

        cameraFileLoc = Environment.getExternalStorageDirectory() + File.separator
                + "Tokopedia" + File.separator + "IMG_" + imageCode + ".jpg";

        return mediaFile;
    }

    private String uniqueCode() {
        String IDunique = UUID.randomUUID().toString();
        String id = IDunique.replaceAll("-", "");
        String code = id.substring(0, 16);
        return code;
    }

    public void onResult(int requestCode, int resultCode, Intent intent, UploadImageDialogListener listener) {
        if(requestCode == REQUEST_CAMERA || requestCode == REQUEST_GALLERY) {
            switch (resultCode) {
                case RESULT_CODE:
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    BitmapFactory.Options checksize = new BitmapFactory.Options();
                    checksize.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    checksize.inJustDecodeBounds = true;

                    BitmapFactory
                            .decodeFile(intent.getStringExtra(ImageGallery.EXTRA_URL), checksize);
                    options.inSampleSize = ImageHandler.calculateInSampleSize(checksize);
                    Bitmap tempPic = BitmapFactory
                            .decodeFile(intent.getStringExtra(ImageGallery.EXTRA_URL), options);
                    if (tempPic != null) {
                        cache.setImageLocalPath(intent.getStringExtra(ImageGallery.EXTRA_URL)).save();
                        listener.onSuccess(LocalCacheManager.ImageAttachment.Builder(resolutionID).getCache());
                    } else {
                        listener.onFailed();
                    }
                    break;
                case Activity.RESULT_OK:
                    if (cameraFileLoc != null) {
                        cache.setImageLocalPath(cameraFileLoc).save();
                        listener.onSuccess(LocalCacheManager.ImageAttachment.Builder(resolutionID).getCache());
                    } else {
                        listener.onFailed();
                    }
                    break;
                default:
                    listener.onFailed();
                    break;
            }
        }
    }

}
