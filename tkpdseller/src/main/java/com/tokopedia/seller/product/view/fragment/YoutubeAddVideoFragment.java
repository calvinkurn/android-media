package com.tokopedia.seller.product.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.YoutubeVideoComponent;
import com.tokopedia.seller.product.domain.interactor.YoutubeVideoUseCase;
import com.tokopedia.seller.product.utils.YoutubeVideoLinkUtils;
import com.tokopedia.seller.product.view.holder.AddUrlContainerViewHolder;
import com.tokopedia.seller.product.view.listener.YoutubeAddVideoActView;
import com.tokopedia.seller.product.view.listener.YoutubeAddVideoView;
import com.tokopedia.seller.product.view.model.AddUrlVideoModel;
import com.tokopedia.seller.product.view.presenter.YoutubeAddVideoPresenter;
import com.tokopedia.seller.product.view.presenter.YoutubeAddVideoPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author normansyahputa on 4/17/17.
 */

public class YoutubeAddVideoFragment extends BaseDaggerFragment implements YoutubeAddVideoView {
    public static final String TAG = "YoutubeAddVideoFragment";
    @Inject
    YoutubeVideoUseCase youtubeVideoUseCase;
    @Inject
    YoutubeVideoLinkUtils youtubeVideoLinkUtils;
    private AddUrlContainerViewHolder addUrlContainerViewHolder;
    private boolean isFirstTime = true;
    private HasComponent<YoutubeVideoComponent> parentComponent;
    private YoutubeVideoComponent component;
    private YoutubeAddVideoPresenter presenter;
    private YoutubeAddVideoActView youtubeAddVideoActView;

    public static Fragment createInstance() {
        return new YoutubeAddVideoFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if ((context != null) && (context instanceof HasComponent<?>)) {
            parentComponent
                    = (HasComponent<YoutubeVideoComponent>) context;
        }

        if ((context != null) && (context instanceof YoutubeAddVideoActView)) {
            youtubeAddVideoActView
                    = (YoutubeAddVideoActView) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youtube_addvideo, container, false);
        addUrlContainerViewHolder = new AddUrlContainerViewHolder(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isFirstTime) {
            inject();
            presenter = new YoutubeAddVideoPresenterImpl(
                    youtubeVideoUseCase,
                    youtubeVideoLinkUtils);

            presenter.attachView(this);
            List<String> strings = youtubeAddVideoActView.videoIds();
            if (CommonUtils.checkCollectionNotNull(strings)) {
                for (String videoId : strings) {
                    presenter.fetchYoutube(videoId);
                }
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void inject() {
        component = parentComponent.getComponent();
        component.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
    }

    @Override
    public void addAddUrlVideModel(AddUrlVideoModel addUrlVideoModel) {
        addUrlContainerViewHolder.addAddUrlVideModel(addUrlVideoModel);
    }

    public void addYoutubeUrl(String youtubeUrl) {
        presenter.fetchYoutubeDescription(youtubeUrl);
    }

    public void addVideoId(String videoId) {
        presenter.fetchYoutube(videoId);
    }

    @Override
    public void showMessageError(final String videoId) {
        NetworkErrorHelper
                .createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        if (videoId != null && !videoId.isEmpty())
                            presenter.fetchYoutube(videoId);
                    }
                })
                .showRetrySnackbar();
    }

    public Intent getResultIntent() {
        List<String> videoIds = addUrlContainerViewHolder.getVideoIds();
        if (CommonUtils.checkCollectionNotNull(videoIds)) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(KEY_VIDEOS_LINK, new ArrayList<String>(videoIds));
            return intent;
        } else {
            return null;
        }
    }
}
