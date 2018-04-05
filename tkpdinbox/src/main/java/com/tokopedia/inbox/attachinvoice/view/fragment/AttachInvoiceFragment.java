package com.tokopedia.inbox.attachinvoice.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.attachinvoice.di.DaggerAttachInvoiceComponent;
import com.tokopedia.inbox.attachinvoice.view.AttachInvoiceContract;
import com.tokopedia.inbox.attachinvoice.view.activity.AttachInvoiceActivity;
import com.tokopedia.inbox.attachinvoice.view.adapter.AttachInvoiceListAdapter;
import com.tokopedia.inbox.attachinvoice.view.adapter.AttachInvoiceListAdapterTypeFactory;
import com.tokopedia.inbox.attachinvoice.view.model.InvoiceViewModel;
import com.tokopedia.inbox.attachinvoice.view.presenter.AttachInvoicePresenter;
import com.tokopedia.inbox.attachinvoice.view.resultmodel.SelectedInvoice;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Hendri on 22/03/18.
 */

public class AttachInvoiceFragment extends BaseListFragment<InvoiceViewModel,AttachInvoiceListAdapterTypeFactory> implements AttachInvoiceContract.View {

    @Inject
    AttachInvoicePresenter presenter;

    AttachInvoiceContract.Activity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        RecyclerView recyclerView = super.getRecyclerView(view);
        if(recyclerView instanceof VerticalRecyclerView){
            VerticalRecyclerView verticalRecyclerView = (VerticalRecyclerView)recyclerView;
            verticalRecyclerView.clearItemDecoration();
        }
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
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerAttachInvoiceComponent daggerInboxChatComponent =
                (DaggerAttachInvoiceComponent) DaggerAttachInvoiceComponent.builder()
                        .appComponent(appComponent).build();
        daggerInboxChatComponent.inject(this);
        presenter.attachView(this);
        presenter.attachActivityContract(activity);
    }

    @Override
    public void onItemClicked(InvoiceViewModel invoiceViewModel) {
        Intent data = new Intent();
        data.putExtra(AttachInvoiceActivity.TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY,new SelectedInvoice(invoiceViewModel));
        getActivity().setResult(AttachInvoiceActivity.TOKOPEDIA_ATTACH_INVOICE_RESULT_CODE_OK,data);
        getActivity().finish();
    }

    @Override
    public void loadData(int page) {
        //Query search are disabled for this release therefore sending empty string
        presenter.loadInvoiceData("",activity.getUserId(),page,activity.getMessageId(),getContext());
    }

    @Override
    protected AttachInvoiceListAdapterTypeFactory getAdapterTypeFactory() {
        return new AttachInvoiceListAdapterTypeFactory();
    }

    @NonNull
    @Override
    protected BaseListAdapter<InvoiceViewModel, AttachInvoiceListAdapterTypeFactory> createAdapterInstance() {
        return new AttachInvoiceListAdapter(getAdapterTypeFactory(),this);
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

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return swipeRefreshLayout;
    }
}
