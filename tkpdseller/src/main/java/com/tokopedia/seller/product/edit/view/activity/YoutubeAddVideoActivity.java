package com.tokopedia.seller.product.edit.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.di.component.DaggerYoutubeVideoComponent;
import com.tokopedia.seller.product.edit.di.component.YoutubeVideoComponent;
import com.tokopedia.seller.product.edit.di.module.YoutubeVideoModule;
import com.tokopedia.seller.product.edit.view.dialog.TextPickerDialogListener;
import com.tokopedia.seller.product.edit.view.dialog.YoutubeAddUrlDialog;
import com.tokopedia.seller.product.edit.view.fragment.YoutubeAddVideoFragment;
import com.tokopedia.seller.product.edit.view.listener.YoutubeAddVideoActView;
import com.tokopedia.seller.product.edit.view.listener.YoutubeAddVideoView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author normansyahputa on 4/17/17.
 */
public class YoutubeAddVideoActivity extends BaseActivity
        implements HasComponent<YoutubeVideoComponent>, YoutubeAddVideoActView, TextPickerDialogListener {

    private YoutubeVideoComponent youtubeVideoComponent;

    /**
     * due to limitation of existing dataset videoIds throw back is from here.
     */
    private ArrayList<String> videoIDs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjection();

        setContentView(R.layout.activity_product_add);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null) {
                videoIDs = intent.getStringArrayListExtra(YoutubeAddVideoView.KEY_VIDEOS_LINK);
                if (videoIDs == null)
                    videoIDs = new ArrayList<>();
            }
        } else {
            videoIDs = savedInstanceState.getStringArrayList(YoutubeAddVideoView.KEY_VIDEOS_LINK);
        }

        if (!CommonUtils.checkNotNull(youtubeAddVideoFragment())) {
            getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                    .add(R.id.container, YoutubeAddVideoFragment.createInstance(), YoutubeAddVideoFragment.TAG)
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initInjection() {
        youtubeVideoComponent = DaggerYoutubeVideoComponent
                .builder()
                .appComponent(getApplicationComponent())
                .youtubeVideoModule(new YoutubeVideoModule())
                .build();
    }

    @Override
    public YoutubeVideoComponent getComponent() {
        return youtubeVideoComponent;
    }


    @Override
    public YoutubeAddVideoFragment youtubeAddVideoFragment() {
        Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(YoutubeAddVideoFragment.TAG);
        return (fragmentByTag != null && fragmentByTag instanceof YoutubeAddVideoFragment) ?
                (YoutubeAddVideoFragment) fragmentByTag : null;
    }

    @Override
    public void openAddYoutubeDialog() {
        YoutubeAddUrlDialog dialog = new YoutubeAddUrlDialog();
        dialog.show(getSupportFragmentManager(), YoutubeAddUrlDialog.TAG);
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
    public void onTextPickerSubmitted(String youtubeUrl) {
        if (youtubeAddVideoFragment() != null) {
            youtubeAddVideoFragment().addYoutubeUrl(youtubeUrl);
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
}
