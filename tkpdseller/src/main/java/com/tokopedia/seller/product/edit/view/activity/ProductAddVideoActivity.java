package com.tokopedia.seller.product.edit.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.edit.di.component.DaggerYoutubeVideoComponent;
import com.tokopedia.seller.product.edit.di.component.YoutubeVideoComponent;
import com.tokopedia.seller.product.edit.di.module.YoutubeVideoModule;
import com.tokopedia.seller.base.view.dialog.BaseTextPickerDialogFragment;
import com.tokopedia.seller.product.edit.view.dialog.ProductAddVideoDialogFragment;
import com.tokopedia.seller.product.edit.view.fragment.ProductAddVideoFragment;
import com.tokopedia.seller.product.edit.view.listener.YoutubeAddVideoActView;
import com.tokopedia.seller.product.edit.view.listener.YoutubeAddVideoView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author normansyahputa on 4/17/17.
 */
public class ProductAddVideoActivity extends BaseSimpleActivity
        implements HasComponent<YoutubeVideoComponent>, YoutubeAddVideoActView, BaseTextPickerDialogFragment.Listener {

    /**
     * due to limitation of existing dataset videoIds throw back is from here.
     */
    private ArrayList<String> videoIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null) {
                videoIDs = getIntent().getStringArrayListExtra(YoutubeAddVideoView.KEY_VIDEOS_LINK);
                if (videoIDs == null) {
                    videoIDs = new ArrayList<>();
                }
            }
        } else {
            videoIDs = savedInstanceState.getStringArrayList(YoutubeAddVideoView.KEY_VIDEOS_LINK);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return ProductAddVideoFragment.createInstance();
    }

    @Override
    public ProductAddVideoFragment youtubeAddVideoFragment() {
        Fragment fragment = getFragment();
        return (fragment != null && fragment instanceof ProductAddVideoFragment) ?
                (ProductAddVideoFragment) fragment : null;
    }

    @Override
    public void openAddYoutubeDialog() {
        ProductAddVideoDialogFragment dialog = new ProductAddVideoDialogFragment();
        dialog.show(getSupportFragmentManager(), ProductAddVideoDialogFragment.TAG);
    }

    @Override
    public List<String> videoIds() {
        return videoIDs;
    }

    @Override
    public void addVideoIds(String videoId) {
        videoIDs.add(videoId);
    }

    @Override
    public void removeVideoIds(int index) {
        videoIDs.remove(index);
    }

    @Override
    public void removeVideoId(String videoId) {
        videoIDs.remove(videoId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(YoutubeAddVideoView.KEY_VIDEOS_LINK, videoIDs);
    }

    @Override
    public void onTextPickerSubmitted(String text) {
        if (youtubeAddVideoFragment() != null) {
            youtubeAddVideoFragment().addYoutubeUrl(text);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = getResultIntent();
        if (intent != null) {
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }

    private Intent getResultIntent() {
        List<String> videoIds = new ArrayList<>(videoIDs);
        if (CommonUtils.checkCollectionNotNull(videoIds)) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(YoutubeAddVideoView.KEY_VIDEOS_LINK, new ArrayList<>(videoIds));
            return intent;
        } else {
            return null;
        }
    }

    @Override
    public YoutubeVideoComponent getComponent() {
        return DaggerYoutubeVideoComponent
                .builder()
                .productComponent(((SellerModuleRouter) getApplication()).getProductComponent())
                .youtubeVideoModule(new YoutubeVideoModule())
                .build();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}