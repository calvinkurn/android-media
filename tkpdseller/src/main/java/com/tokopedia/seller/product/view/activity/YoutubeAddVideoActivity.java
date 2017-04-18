package com.tokopedia.seller.product.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.DaggerYoutubeVideoComponent;
import com.tokopedia.seller.product.di.component.YoutubeVideoComponent;
import com.tokopedia.seller.product.di.module.YoutubeVideoModule;
import com.tokopedia.seller.product.view.dialog.TextPickerDialogListener;
import com.tokopedia.seller.product.view.dialog.YoutubeAddUrlDialog;
import com.tokopedia.seller.product.view.fragment.YoutubeAddVideoActView;
import com.tokopedia.seller.product.view.fragment.YoutubeAddVideoFragment;

/**
 * @author normansyahputa on 4/17/17.
 */
public class YoutubeAddVideoActivity extends TActivity
        implements HasComponent<YoutubeVideoComponent>, YoutubeAddVideoActView, TextPickerDialogListener {

    private YoutubeVideoComponent youtubeVideoComponent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjection();

        inflateView(R.layout.activity_product_add);

        if (!CommonUtils.checkNotNull(youtubeAddVideoFragment())) {
            getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                    .add(R.id.container, YoutubeAddVideoFragment.createInstance(), YoutubeAddVideoFragment.TAG)
                    .commit();
        }
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
    public void onTextPickerSubmitted(String youtubeUrl) {
        if (youtubeAddVideoFragment() != null) {
            youtubeAddVideoFragment().addYoutubeUrl(youtubeUrl);
        }
    }

}
