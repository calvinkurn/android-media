package com.tokopedia.inbox.attachinvoice.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.attachinvoice.data.mapper.TkpdResponseToInvoicesDataModelMapper;
import com.tokopedia.inbox.attachinvoice.data.repository.AttachInvoicesRepositoryImpl;
import com.tokopedia.inbox.attachinvoice.data.source.service.GetTxInvoicesService;
import com.tokopedia.inbox.attachinvoice.domain.usecase.AttachInvoicesUseCase;
import com.tokopedia.inbox.attachinvoice.view.AttachInvoiceContract;
import com.tokopedia.inbox.attachinvoice.view.adapter.AttachInvoiceListAdapter;
import com.tokopedia.inbox.attachinvoice.view.adapter.AttachInvoiceListAdapterTypeFactory;
import com.tokopedia.inbox.attachinvoice.view.model.InvoiceViewModel;
import com.tokopedia.inbox.attachinvoice.view.presenter.AttachInvoicePresenter;
import com.tokopedia.inbox.attachproduct.view.fragment.AttachProductFragment;
import com.tokopedia.inbox.attachproduct.view.presenter.AttachProductContract;

import java.util.List;

/**
 * Created by Hendri on 22/03/18.
 */

public class AttachInvoiceFragment extends BaseSearchListFragment<InvoiceViewModel,AttachInvoiceListAdapterTypeFactory> implements AttachInvoiceContract.View {
    AttachInvoiceContract.Presenter presenter;
    AttachInvoiceContract.Activity activity;
    String userId;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new AttachInvoicePresenter(new AttachInvoicesUseCase(new AttachInvoicesRepositoryImpl(new GetTxInvoicesService(), new TkpdResponseToInvoicesDataModelMapper())));
        presenter.attachView(this);
        presenter.attachActivityContract(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        return view;
    }

    public static AttachInvoiceFragment newInstance(AttachInvoiceContract.Activity checkedUIView) {
        Bundle args = new Bundle();
        AttachInvoiceFragment fragment = new AttachInvoiceFragment();
        fragment.setActivityContract(checkedUIView);
        fragment.setArguments(args);
        return fragment;
    }

    public void setActivityContract(AttachInvoiceContract.Activity activityContract){
        this.activity = activityContract;
    }

    @Override
    protected String getScreenName() {
        return "Attach Invoice";
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onSearchSubmitted(String text) {
        presenter.loadInvoiceData(text,activity.getUserId(),0,getContext());
    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onItemClicked(InvoiceViewModel invoiceViewModel) {

    }

    @Override
    public void loadData(int page) {
        presenter.loadInvoiceData(searchInputView.getSearchText(),activity.getUserId(),page,getContext());
    }

    @Override
    protected AttachInvoiceListAdapterTypeFactory getAdapterTypeFactory() {
        return new AttachInvoiceListAdapterTypeFactory();
    }

    @NonNull
    @Override
    protected BaseListAdapter<InvoiceViewModel, AttachInvoiceListAdapterTypeFactory> createAdapterInstance() {
        return new AttachInvoiceListAdapter(getAdapterTypeFactory());
    }

    @Override
    public void addInvoicesToList(List<InvoiceViewModel> invoices, boolean hasNextPage) {
        renderList(invoices,hasNextPage);
    }

    @Override
    public void hideAllLoadingIndicator() {
        swipeRefreshLayout.setRefreshing(false);
        super.hideLoading();
    }

    @Override
    public void showErrorMessage(Throwable throwable) {
        throwable.printStackTrace();
        hideAllLoadingIndicator();
    }

    @Override
    public void updateButtonBasedOnChecked(int checkedCount) {

    }

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return swipeRefreshLayout; //super.getSwipeRefreshLayout(view);
    }
}
