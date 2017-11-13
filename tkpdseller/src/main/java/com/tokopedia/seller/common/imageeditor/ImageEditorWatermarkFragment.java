package com.tokopedia.seller.common.imageeditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.theartofdev.edmodo.cropper.CropImageView;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.imageeditor.component.DaggerGetShopInfoComponent;
import com.tokopedia.seller.common.imageeditor.component.GetShopInfoComponent;
import com.tokopedia.seller.shop.presenter.GetShopInfoPresenter;
import com.tokopedia.seller.shop.presenter.GetShopInfoView;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by hendry on 9/25/2017.
 */

public class ImageEditorWatermarkFragment extends ImageEditorFragment implements GetShopInfoView, CropImageView.OnSetCropOverlayReleasedListener, CropImageView.OnSetCropOverlayMovedListener, CropImageView.OnSetCropWindowChangeListener {

    private WatermarkView watermarkView;

    private String watermarkText;
    private boolean isUseWatermark;
    private View vWatermarkWarning;

    @Inject
    public GetShopInfoPresenter getShopInfoPresenter;
    private MenuItem watermarkMenuItem;

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
        GetShopInfoComponent getShopInfoComponent = DaggerGetShopInfoComponent.builder().appComponent(
                MainApplication.getInstance().getApplicationComponent()).build();
        getShopInfoComponent.inject(this);
        getShopInfoPresenter.attachView(this);
        getShopInfoPresenter.getShopInfo();

        isUseWatermark = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_editor_watermark, container, false);
        watermarkView = (WatermarkView) view.findViewById(R.id.watermark_view);
        vWatermarkWarning = view.findViewById(R.id.tv_watermark_warning);
        vWatermarkWarning.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCropImageView.setOnSetCropOverlayReleasedListener(this);
        mCropImageView.setOnSetCropOverlayMovedListener(this);
    }

    public void setWatermarkWindowCropRect(RectF cropWindowRect){
        if (cropWindowRect!= null) {
            watermarkView.setWindowRect(cropWindowRect);
        }
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        super.onSetImageUriComplete(view, uri, error);
        RectF rect = mCropImageView.getCropWindowRect();
        setWatermarkWindowCropRect(rect);
    }

    protected boolean checkIfSameWithPrevImage(){
        return super.checkIfSameWithPrevImage() && !isUseWatermark;
    }


    @Override
    protected Bitmap processBitmap(Bitmap bitmap) {
        if (isUseWatermark) {
            return watermarkView.drawTo(bitmap);
        } else {
            return bitmap;
        }
    }

    @Override
    protected String processCroppedPath(String croppedPath) {
        if (isUseWatermark) {
            Bitmap bitmap = BitmapFactory.decodeFile(croppedPath);
            bitmap = processBitmap(bitmap);
            File file = FileUtils.writeImageToTkpdPath(bitmap, FileUtils.generateUniqueFileName());
            if (file != null && file.exists()) {
                onImageEditorFragmentListener.addCroppedPath(croppedPath);
                return file.getAbsolutePath();
            } else {
                return croppedPath;
            }
        } else {
            return croppedPath;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_image_editor_watermark,menu);
        watermarkMenuItem = menu.findItem(R.id.main_action_watermark);
        setUIByWatermark(isUseWatermark);
    }

    private void setUIByWatermark(boolean isUseWatermark){
        if (isUseWatermark) {
            watermarkMenuItem.setIcon(R.drawable.ic_branding_watermark_checked);
            watermarkMenuItem.getIcon().invalidateSelf();
            watermarkView.setVisibility(View.VISIBLE);
            vWatermarkWarning.setVisibility(View.VISIBLE);
        } else {
            watermarkMenuItem.setIcon(R.drawable.ic_branding_watermark_unchecked);
            watermarkMenuItem.getIcon().invalidateSelf();
            watermarkView.setVisibility(View.GONE);
            vWatermarkWarning.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCropOverlayReleased(Rect rect) {
        RectF windowRect = mCropImageView.getCropWindowRect();
        setWatermarkWindowCropRect(windowRect);
    }

    @Override
    public void onCropOverlayMoved(Rect rect) {
        RectF windowRect = mCropImageView.getCropWindowRect();
        setWatermarkWindowCropRect(windowRect);
    }

    @Override
    public void onCropWindowChanged() {
        RectF windowRect = mCropImageView.getCropWindowRect();
        setWatermarkWindowCropRect(windowRect);
    }

    @Override
    public void onResume() {
        super.onResume();
        getShopInfoPresenter.attachView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getShopInfoPresenter.detachView();
    }

    @Override
    public void onSuccessGetShopInfo(ShopModel shopModel) {
        watermarkText = shopModel.getInfo().getShopName();
        watermarkView.setText(watermarkText);
    }

    @Override
    public void onErrorGetShopInfo(Throwable t) {
        watermarkText = "";
        watermarkView.setText(watermarkText);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_action_watermark) {
            isUseWatermark = !isUseWatermark;
            setUIByWatermark(isUseWatermark);
            return true;
        } else if (item.getItemId() == R.id.main_action_rotate) {
            mCropImageView.rotateImage(90);
            onCropWindowChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
