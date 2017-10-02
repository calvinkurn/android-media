package com.tokopedia.seller.common.imageeditor;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.theartofdev.edmodo.cropper.CropImageView;
import com.tokopedia.seller.R;

/**
 * Created by User on 9/25/2017.
 */

public class ImageEditorWatermarkFragment extends ImageEditorFragment{

    public static ImageEditorWatermarkFragment newInstance(String localPath) {
        Bundle args = new Bundle();
        args.putString(ARG_LOCAL_PATH, localPath);
        ImageEditorWatermarkFragment fragment = new ImageEditorWatermarkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_editor_watermark, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCropImageView.setOnSetCropOverlayReleasedListener(new CropImageView.OnSetCropOverlayReleasedListener() {
            @Override
            public void onCropOverlayReleased(Rect rect) {
                
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_image_editor_watermark,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_action_watermark) {
            // TODO watermark click
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
