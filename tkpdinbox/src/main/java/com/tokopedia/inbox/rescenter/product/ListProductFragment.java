package com.tokopedia.inbox.rescenter.product;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.detailv2.di.component.ResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.product.di.component.DaggerResolutionProductListComponent;
import com.tokopedia.inbox.rescenter.product.di.component.ResolutionProductListComponent;
import com.tokopedia.inbox.rescenter.product.di.module.ResolutionProductListModule;
import com.tokopedia.inbox.rescenter.product.view.customadapter.ListProductAdapter;
import com.tokopedia.inbox.rescenter.product.view.model.ListProductViewItem;
import com.tokopedia.inbox.rescenter.product.view.presenter.ListProductFragmentImpl;
import com.tokopedia.inbox.rescenter.product.view.presenter.ListProductFragmentView;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by hangnadi on 3/23/17.
 */

public class ListProductFragment extends BaseDaggerFragment
    implements ListProductFragmentView {

    private static final String EXTRA_PARAM_RESOLUTION_ID = "resolution_id";
    private static final String EXTRA_PARAM_VIEW_DATA = "extra_view_data";

    private RecyclerView recyclerview;
    private ListProductAdapter adapter;

    private String resolutionID;

    private ArrayList<ListProductViewItem> viewData;

    private TkpdProgressDialog normalLoading;

    @Inject
    ListProductFragmentImpl presenter;

    public static Fragment createInstance(String resolutionID) {
        ListProductFragment fragment = new ListProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ListProductFragment() {
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.onFirstTimeLaunch();
    }

    @Override
    public String getResolutionID() {
        return resolutionID;
    }

    @Override
    public void setResolutionID(String resolutionID) {
        this.resolutionID = resolutionID;
    }

    @Override
    public void setLoadingView(boolean param) {
        if (param) {
            if (getViewData() == null || getViewData().isEmpty()) {
                adapter.showLoadingFull(true);
            } else {
                adapter.showLoading(true);
            }
        } else {
            adapter.showLoadingFull(false);
            adapter.showLoading(false);
        }
    }

    @Override
    public void onGetHistoryAwbTimeOut() {
        setLoadingView(false);
        NetworkErrorHelper.showEmptyState(
                getActivity(),
                getView(),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.onFirstTimeLaunch();
                    }
                });
    }

    @Override
    public void onGetHistoryAwbFailed(String messageError) {
        setLoadingView(false);
        setErrorMessage(messageError);
    }

    @Override
    public void setErrorMessage(String messageError) {
        if (messageError != null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), messageError, null);
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), null);
        }
    }

    @Override
    public void setViewData(ArrayList<ListProductViewItem> viewData) {
        this.viewData = viewData;
    }

    @Override
    public ArrayList<ListProductViewItem> getViewData() {
        return viewData;
    }

    @Override
    public void renderData() {
        adapter.setArraylist(getViewData());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putString(EXTRA_PARAM_RESOLUTION_ID, getResolutionID());
        state.putParcelableArrayList(EXTRA_PARAM_VIEW_DATA, getViewData());
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        setResolutionID(savedState.getString(EXTRA_PARAM_RESOLUTION_ID));
        setViewData(savedState.<ListProductViewItem>getParcelableArrayList(EXTRA_PARAM_VIEW_DATA));
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_RESOLUTION_CENTER_PRODUCT_LIST;
    }

    @Override
    protected void initInjector() {
        ResolutionDetailComponent resolutionDetailComponent = getComponent(ResolutionDetailComponent.class);
        ResolutionProductListComponent resolutionProductListComponent =
                DaggerResolutionProductListComponent.builder()
                        .resolutionDetailComponent(resolutionDetailComponent)
                        .resolutionProductListModule(new ResolutionProductListModule(this))
                        .build();
        resolutionProductListComponent.inject(this);
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        setResolutionID(arguments.getString(EXTRA_PARAM_RESOLUTION_ID));
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_history_address;
    }

    @Override
    protected void initView(View view) {
        recyclerview = (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        normalLoading = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        viewData = new ArrayList<>();
        adapter = new ListProductAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void setOnProductItemClick(String productID, String productName) {
        startActivity(ProductDetailActivity.newInstance(getActivity(), getResolutionID(), productID, productName));
    }

    @Override
    public void showLoadingDialog(boolean show) {
        if (show) {
            normalLoading.showDialog();
        } else {
            normalLoading.dismiss();
        }
    }

    @Override
    public void showSnackBar(String messageError) {
        if (messageError == null) {
            showTimeOutMessage();
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), messageError);
        }
    }

    @Override
    public void showTimeOutMessage() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.setOnDestroyView();
    }
}
