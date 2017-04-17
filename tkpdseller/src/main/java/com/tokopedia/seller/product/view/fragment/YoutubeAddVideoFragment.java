package com.tokopedia.seller.product.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.v4.BaseNetworkErrorHandlerImpl;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.YoutubeVideoComponent;
import com.tokopedia.seller.product.domain.interactor.YoutubeVideoUseCase;
import com.tokopedia.seller.product.utils.YoutubeVideoLinkUtils;
import com.tokopedia.seller.product.view.holder.AddUrlContainerViewHolder;
import com.tokopedia.seller.product.view.model.AddUrlVideoModel;
import com.tokopedia.seller.product.view.presenter.YoutubeAddVideoPresenter;
import com.tokopedia.seller.product.view.presenter.YoutubeAddVideoPresenterImpl;

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
    private BaseNetworkErrorHandlerImpl baseNetworkErrorHandler;
    private OnRetryClicked onRetryClicked;

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
            onRetryClicked = new OnRetryClicked();
            baseNetworkErrorHandler = new BaseNetworkErrorHandlerImpl(onRetryClicked);
        }

        presenter.attachView(this);
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

    @Override
    public void showMessageError(final String videoId) {
        onRetryClicked.setVideoid(videoId);
        baseNetworkErrorHandler.createSnackbarWithAction(getActivity()).showRetrySnackbar();
    }

    private class OnRetryClicked implements NetworkErrorHelper.RetryClickedListener {
        private String videoid;

        OnRetryClicked() {
        }

        public String getVideoid() {
            return videoid;
        }

        public void setVideoid(String videoid) {
            this.videoid = videoid;
        }

        @Override
        public void onRetryClicked() {
            if (videoid != null && !videoid.isEmpty())
                presenter.fetchYoutube(videoid);
        }
    }
}
