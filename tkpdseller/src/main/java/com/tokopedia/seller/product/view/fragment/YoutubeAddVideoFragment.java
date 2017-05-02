package com.tokopedia.seller.product.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.YoutubeVideoComponent;
import com.tokopedia.seller.product.domain.interactor.YoutubeVideoUseCase;
import com.tokopedia.seller.product.utils.YoutubeVideoLinkUtils;
import com.tokopedia.seller.product.view.adapter.addurlvideo.AddUrlVideoAdapter;
import com.tokopedia.seller.product.view.adapter.addurlvideo.EmptyAddUrlVideoDataBinder;
import com.tokopedia.seller.product.view.listener.YoutubeAddVideoActView;
import com.tokopedia.seller.product.view.listener.YoutubeAddVideoView;
import com.tokopedia.seller.product.view.model.AddUrlVideoModel;
import com.tokopedia.seller.product.view.presenter.YoutubeAddVideoPresenter;
import com.tokopedia.seller.product.view.presenter.YoutubeAddVideoPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyAdDataBinder;

import java.util.List;

import javax.inject.Inject;

/**
 * @author normansyahputa on 4/17/17.
 */

public class YoutubeAddVideoFragment extends BaseDaggerFragment implements YoutubeAddVideoView {
    public static final String TAG = "YoutubeAddVideoFragment";
    public static final int MAX_ROWS = 3;
    @Inject
    YoutubeVideoUseCase youtubeVideoUseCase;
    @Inject
    YoutubeVideoLinkUtils youtubeVideoLinkUtils;
    private boolean isFirstTime = true;
    private HasComponent<YoutubeVideoComponent> parentComponent;
    private YoutubeVideoComponent component;
    private YoutubeAddVideoPresenter presenter;
    private YoutubeAddVideoActView youtubeAddVideoActView;
    private RecyclerView recyclerViewAddUrlVideo;
    private AddUrlVideoAdapter addUrlVideoAdapter;

    public static Fragment createInstance() {
        return new YoutubeAddVideoFragment();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setYoutubeVideoComponent(context);

        setYoutubeAddVideoActView(context);

        setHasOptionsMenu(true);
    }

    protected void setYoutubeAddVideoActView(Context context) {
        if ((context != null) && (context instanceof YoutubeAddVideoActView)) {
            youtubeAddVideoActView
                    = (YoutubeAddVideoActView) context;
        }
    }

    @SuppressWarnings("unchecked")
    protected void setYoutubeVideoComponent(Context context) {
        if ((context != null) && (context instanceof HasComponent<?>)) {
            HasComponent<?> hasComponent = (HasComponent<?>) context;
            Object component = hasComponent.getComponent();
            if (component != null && component instanceof YoutubeVideoComponent) {
                parentComponent = (HasComponent<YoutubeVideoComponent>) context;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youtube_addvideo, container, false);
        recyclerViewAddUrlVideo = (RecyclerView) view.findViewById(R.id.recycler_view_add_url_video);
        addUrlVideoAdapter = new AddUrlVideoAdapter(new ImageHandler(view.getContext()));
        EmptyAddUrlVideoDataBinder emptyAddUrlVideoDataBinder = new EmptyAddUrlVideoDataBinder(addUrlVideoAdapter);
        emptyAddUrlVideoDataBinder.setCallback(new TopAdsEmptyAdDataBinder.Callback() {
            @Override
            public void onEmptyContentItemTextClicked() {
                youtubeAddVideoActView.openAddYoutubeDialog();
            }
        });
        addUrlVideoAdapter.setEmptyView(emptyAddUrlVideoDataBinder);
        addUrlVideoAdapter.setMaxRows(MAX_ROWS);
        addUrlVideoAdapter.setListener(new AddUrlVideoAdapter.Listener() {
            @Override
            public void notifyEmpty() {
                addUrlVideoAdapter.showEmptyFull(true);
            }

            @Override
            public void notifyNonEmpty() {
                addUrlVideoAdapter.showEmptyFull(false);
            }

            @Override
            public void remove(int index) {
                youtubeAddVideoActView.removeVideoIds(index);
            }
        });
        addUrlVideoAdapter.setVideoSameWarn(getString(R.string.video_same_warn));
        recyclerViewAddUrlVideo.setAdapter(addUrlVideoAdapter);
        recyclerViewAddUrlVideo.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        addUrlVideoAdapter.showEmptyFull(true);
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
            setYoutubeAddVideoActView(getActivity());
            setVideoSubtitle();
            presenter.setYoutubeActView(youtubeAddVideoActView);
            List<String> strings = youtubeAddVideoActView.videoIds();
            if (CommonUtils.checkCollectionNotNull(strings)) {
                presenter.fetchYoutube(strings);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void inject() {
        if (parentComponent == null) {
            setYoutubeVideoComponent(getActivity());
        }
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
        try {
            addUrlVideoAdapter.add(addUrlVideoModel);
            setVideoSubtitle();
        } catch (IllegalArgumentException iae) {
            NetworkErrorHelper.showSnackbar(getActivity(), iae.getMessage());
        }
    }

    private void setVideoSubtitle(int from, int max) {
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setSubtitle(String.format(getString(R.string.from_to_video), from, max));
    }

    private void setVideoSubtitle() {
        if (youtubeAddVideoActView != null) {
            setVideoSubtitle(youtubeAddVideoActView.videoIds().size(), MAX_ROWS);
        }
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

    @Override
    public void addAddUrlVideModels(List<AddUrlVideoModel> convert) {
        for (AddUrlVideoModel addUrlVideoModel : convert) {
            addAddUrlVideModel(addUrlVideoModel);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(com.tokopedia.core.R.menu.talk_product, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        } else if (item.getItemId() == com.tokopedia.core.R.id.action_talk_add) {
            youtubeAddVideoActView.openAddYoutubeDialog();
            return true;
        }
        return false;
    }

}
