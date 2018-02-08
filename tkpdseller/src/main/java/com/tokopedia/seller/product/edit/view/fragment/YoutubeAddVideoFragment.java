package com.tokopedia.seller.product.edit.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.base.view.adapter.BaseRetryDataBinder;
import com.tokopedia.seller.product.draft.view.adapter.ProductEmptyDataBinder;
import com.tokopedia.seller.product.edit.di.component.YoutubeVideoComponent;
import com.tokopedia.seller.product.edit.domain.interactor.YoutubeVideoUseCase;
import com.tokopedia.seller.product.edit.utils.YoutubeVideoLinkUtils;
import com.tokopedia.seller.product.edit.view.adapter.addurlvideo.AddUrlVideoAdapter;
import com.tokopedia.seller.product.edit.view.adapter.addurlvideo.EmptyAddUrlVideoDataBinder;
import com.tokopedia.seller.product.edit.view.listener.YoutubeAddVideoActView;
import com.tokopedia.seller.product.edit.view.listener.YoutubeAddVideoView;
import com.tokopedia.seller.product.edit.view.model.AddUrlVideoModel;
import com.tokopedia.seller.product.edit.view.presenter.YoutubeAddVideoPresenter;
import com.tokopedia.seller.product.edit.view.presenter.YoutubeAddVideoPresenterImpl;

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

    /**
     * this is for hide and show menu
     */
    private Menu menu;

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
        View view = inflater.inflate(R.layout.fragment_product_video, container, false);
        recyclerViewAddUrlVideo = (RecyclerView) view.findViewById(R.id.recycler_view_add_url_video);
        addUrlVideoAdapter = new AddUrlVideoAdapter(new ImageHandler(view.getContext()));
        EmptyAddUrlVideoDataBinder emptyAddUrlVideoDataBinder = new EmptyAddUrlVideoDataBinder(addUrlVideoAdapter);
        emptyAddUrlVideoDataBinder.setCallback(new ProductEmptyDataBinder.Callback() {
            @Override
            public void onEmptyContentItemTextClicked() {
                youtubeAddVideoActView.openAddYoutubeDialog();
            }

            @Override
            public void onEmptyButtonClicked() {

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
                showMenuButton();
            }

            @Override
            public void remove(int index) {
                youtubeAddVideoActView.removeVideoIds(index);
                setVideoSubtitle();
            }

            @Override
            public void inflateDeleteConfirmation(int index) {
                showRemoveVideoConfirmation(index);
            }
        });
        addUrlVideoAdapter.setVideoSameWarn(getString(R.string.product_video_same_warn));
        addUrlVideoAdapter.setRetryView(getRetryDataBinder(addUrlVideoAdapter));
        recyclerViewAddUrlVideo.setAdapter(addUrlVideoAdapter);
        recyclerViewAddUrlVideo.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        addUrlVideoAdapter.showEmptyFull(true);
        return view;
    }

    private void showRemoveVideoConfirmation(
            final int index
    ) {
        if (index < 0)
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.product_title_confirmation_delete_video);
        builder.setMessage(R.string.product_confirmation_delete_video);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                addUrlVideoAdapter.remove(index);
                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
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
            isFirstTime = false;
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
            youtubeAddVideoActView.removeVideoIds(youtubeAddVideoActView.videoIds().size() - 1);
            setVideoSubtitle();
            NetworkErrorHelper.showSnackbar(getActivity(), iae.getMessage());
        }
    }

    private void setVideoSubtitle(int from, int max) {
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setSubtitle(String.format(getString(R.string.product_from_to_video), from, max));
    }

    private void setVideoSubtitle() {
        if (youtubeAddVideoActView != null) {
            int size = youtubeAddVideoActView.videoIds().size();
            setVideoSubtitle(size, MAX_ROWS);
        }
    }

    public void addYoutubeUrl(String youtubeUrl) {
        if (addUrlVideoAdapter.getVideoIds().size() + 1 <= MAX_ROWS) {
            showMenuButton();
            presenter.fetchYoutubeDescription(youtubeUrl);
        } else {
            showMaxRows();
        }
    }

    public void showMaxRows() {
        showMessageErrorRaw(getString(R.string.product_could_not_add_videos));
    }

    public void showMenuButton() {
        menu.findItem(R.id.action_add_video).setEnabled(true);
        menu.findItem(R.id.action_add_video).setVisible(true);
    }

    public void addVideoId(String videoId) {
        presenter.fetchYoutube(videoId);
    }

    @Override
    public void showMessageError(final String videoId) {
        if (addUrlVideoAdapter.getVideoIds() == null || addUrlVideoAdapter.getVideoIds().size() <= 0) {
            addUrlVideoAdapter.showRetryFull(true);
        } else {
            showMessageErrorRaw(getString(com.tokopedia.core.R.string.msg_network_error));
        }
    }

    private RetryDataBinder getRetryDataBinder(DataBindAdapter dataBindAdapter) {
        RetryDataBinder retryDataBinder = new BaseRetryDataBinder(dataBindAdapter);
        retryDataBinder.setOnRetryListenerRV(new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                hideLoading();
                addUrlVideoAdapter.showLoadingFull(true);


                List<String> strings = youtubeAddVideoActView.videoIds();
                if (CommonUtils.checkCollectionNotNull(strings)) {
                    presenter.fetchYoutube(strings);
                }
            }
        });
        return retryDataBinder;
    }

    @Override
    public void showMessageErrorRaw(String message) {
        NetworkErrorHelper
                .showSnackbar(getActivity(), message);
    }

    @Override
    public void addAddUrlVideModels(List<AddUrlVideoModel> convert) {
        for (AddUrlVideoModel addUrlVideoModel : convert) {
            addAddUrlVideModel(addUrlVideoModel);
        }
    }

    @Override
    public void showLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addUrlVideoAdapter.showLoadingFull(true);
            }
        });
    }

    @Override
    public void hideLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addUrlVideoAdapter.showLoadingFull(false);
            }
        });
    }

    @Override
    public void hideRetryFull() {
        addUrlVideoAdapter.showRetryFull(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_product_youtube, menu);
        this.menu = menu;
        if (getActivity() instanceof BaseSimpleActivity) {
            ((BaseSimpleActivity) getActivity()).updateOptionMenuColor(menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_add_video) {
            if (addUrlVideoAdapter.getVideoIds().size() + 1 > MAX_ROWS) {
                showMaxRows();
                return true;
            }
            youtubeAddVideoActView.openAddYoutubeDialog();
            return true;
        }
        return false;
    }

}
