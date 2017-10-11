package com.tokopedia.seller.common.imageeditor;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImageView;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.seller.R;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by User on 9/25/2017.
 */

public class ImageEditorFragment extends Fragment implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener {
    public static final String TAG = ImageEditorFragment.class.getSimpleName();
    protected static final String ARG_LOCAL_PATH = "loc_pth";

    protected CropImageView mCropImageView;
    private String localPath;

    OnImageEditorFragmentListener onImageEditorFragmentListener;

    public interface OnImageEditorFragmentListener {
        void onSuccessCrop(CropImageView.CropResult cropResult);
        void onSuccessCrop(String localPath);
    }

    public static ImageEditorFragment newInstance(String localPath) {
        Bundle args = new Bundle();
        args.putString(ARG_LOCAL_PATH, localPath);
        ImageEditorFragment fragment = new ImageEditorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localPath = getArguments().getString(ARG_LOCAL_PATH);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_editor, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCropImageView = (CropImageView) view.findViewById(R.id.cropImageView);
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnCropImageCompleteListener(this);

        File imgFile = new File(localPath);
        if (imgFile.exists()) {
            mCropImageView.setImageUriAsync(Uri.fromFile(imgFile));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_image_editor, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_action_crop) {
            // no need to crop if the rect is same and in local tkpd already
            if (mCropImageView.getRotation() == 0 &&
                   mCropImageView.getCropRect().equals(mCropImageView.getWholeImageRect()) &&
                    FileUtils.isInTkpdCache(new File(localPath))) {
                onImageEditorFragmentListener.onSuccessCrop(localPath);
            } else {
                mCropImageView.getCroppedImageAsync();
            }
            return true;
        } else if (item.getItemId() == R.id.main_action_rotate) {
            mCropImageView.rotateImage(90);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mCropImageView != null) {
            mCropImageView.setOnSetImageUriCompleteListener(null);
            mCropImageView.setOnCropImageCompleteListener(null);
        }
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        // no op
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        handleCropResult(result);
    }

    private void handleCropResult(CropImageView.CropResult result) {
        if (result.getError() == null) {
            onImageEditorFragmentListener.onSuccessCrop(result);
        } else {
            Log.e("AIC", "Failed to crop image", result.getError());
            Toast.makeText(getActivity(), "Image crop failed: " + result.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachListener(activity);
        }
    }

    @TargetApi(23)
    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        onAttachListener(context);
    }

    protected void onAttachListener(Context context) {
        onImageEditorFragmentListener = (OnImageEditorFragmentListener) context;
    }

}
