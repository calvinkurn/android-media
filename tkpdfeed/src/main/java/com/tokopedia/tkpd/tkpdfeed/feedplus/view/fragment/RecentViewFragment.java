package com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.RecentView;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.WishlistListener;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.RecentViewDetailAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.recentview.RecentViewTypeFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.recentview.RecentViewTypeFactoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter.RecentViewPresenter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.feeddetail.FeedDetailViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewFragment extends BaseDaggerFragment
        implements RecentView.View, WishlistListener {

    private RecyclerView recyclerView;
    private RecentViewDetailAdapter adapter;
    private TkpdProgressDialog progressDialog;
    private LinearLayoutManager layoutManager;

    @Inject
    RecentViewPresenter presenter;

    public static RecentViewFragment createInstance(Bundle bundle) {
        RecentViewFragment fragment = new RecentViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected String getScreenName() {
        return "";
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);

        DaggerFeedPlusComponent daggerFeedPlusComponent =
                (DaggerFeedPlusComponent) DaggerFeedPlusComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerFeedPlusComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar(savedInstanceState);
    }

    private void initVar(Bundle savedInstanceState) {
        layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        RecentViewTypeFactory typeFactory = new RecentViewTypeFactoryImpl(this);
        adapter = new RecentViewDetailAdapter(typeFactory);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_recent_view_detail, container, false);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.list);
        prepareView();
        presenter.attachView(this, this);
        return parentView;

    }

    private void prepareView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_line_separator));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getRecentViewProduct();

    }

    @Override
    public void onWishlistClicked(int adapterPosition, Integer productId, boolean isWishlist) {
        if (!isWishlist) {
            presenter.addToWishlist(adapterPosition, String.valueOf(productId));
        } else {
            presenter.removeFromWishlist(adapterPosition, String.valueOf(productId));
        }
    }

    @Override
    public void onGoToProductDetail(String productId) {
        if (getActivity().getApplication() instanceof PdpRouter) {
            ((PdpRouter) getActivity().getApplication()).goToProductDetail(
                    getActivity(),
                    ProductPass.Builder.aProductPass()
                            .setProductId(productId)
                            .build()
            );
        }
    }

    @Override
    public void showLoading() {
        adapter.showLoading();
    }

    @Override
    public void showLoadingProgress() {
        if (progressDialog == null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        if (getActivity() != null)
            progressDialog.showDialog();
    }

    @Override
    public void onErrorGetRecentView(String errorMessage) {
        adapter.dismissLoading();
        if (getActivity() != null && getView() != null && presenter != null)
            NetworkErrorHelper.showEmptyState(getActivity(), getView(),
                    errorMessage, new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.getRecentViewProduct();
                        }
                    });
    }

    @Override
    public void onSuccessGetRecentView(ArrayList<Visitable> recentViewProductViewModels) {
        adapter.dismissLoading();
        adapter.addList(recentViewProductViewModels);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEmptyGetRecentView() {
        adapter.dismissLoading();
        getActivity().finish();
    }

    @Override
    public void onErrorAddWishList(String errorMessage, int adapterPosition) {
        dismissLoadingProgress();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessAddWishlist(int adapterPosition) {
        dismissLoadingProgress();
        ((FeedDetailViewModel) adapter.getList().get(adapterPosition)).setWishlist(true);
        adapter.notifyItemChanged(adapterPosition);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_add_wishlist));
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, int productId) {
        dismissLoadingProgress();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRemoveWishlist(int adapterPosition) {
        dismissLoadingProgress();
        ((FeedDetailViewModel) adapter.getList().get(adapterPosition)).setWishlist(false);
        adapter.notifyItemChanged(adapterPosition);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_remove_wishlist));
    }

    @Override
    public void dismissLoadingProgress() {
        if (progressDialog != null && progressDialog.isProgress())
            progressDialog.dismiss();
    }
}
