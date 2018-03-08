package com.tokopedia.inbox.attachproduct.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;

import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.attachproduct.analytics.AttachProductAnalytics;
import com.tokopedia.inbox.attachproduct.di.AttachProductComponent;
import com.tokopedia.inbox.attachproduct.di.DaggerAttachProductComponent;
import com.tokopedia.inbox.attachproduct.view.AttachProductPresenter;
import com.tokopedia.inbox.attachproduct.view.AttachProductPresenterImpl;
import com.tokopedia.inbox.attachproduct.view.adapter.AttachProductListAdapter;
import com.tokopedia.inbox.attachproduct.view.adapter.AttachProductListAdapterTypeFactory;
import com.tokopedia.inbox.attachproduct.view.viewholder.CheckableInteractionListenerWithPreCheckedAction;
import com.tokopedia.inbox.attachproduct.view.viewmodel.AttachProductItemViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Hendri on 13/02/18.
 */

public class AttachProductFragment extends BaseSearchListFragment<AttachProductItemViewModel,AttachProductListAdapterTypeFactory>
        implements CheckableInteractionListenerWithPreCheckedAction,
                   AttachProductPresenter.View {
    final static int MAX_CHECKED = 3;
    Button sendButton;
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    AttachProductPresenter.Presenter presenter;

    private AttachProductPresenter.Activity activityContract;
    protected AttachProductListAdapter adapter;

    public void setActivityContract(AttachProductPresenter.Activity activityContract) {
        this.activityContract = activityContract;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerAttachProductComponent.builder().build().inject(this);
        presenter.attachView(this);
        presenter.attachActivityContract(activityContract);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attach_product, container, false);
        sendButton = view.findViewById(R.id.send_button_attach_product);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendButtonClicked();
            }
        });
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadInitialData();
            }
        });
        updateButtonBasedOnChecked(0);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public static AttachProductFragment newInstance(AttachProductPresenter.Activity checkedUIView) {
        Bundle args = new Bundle();
        AttachProductFragment fragment = new AttachProductFragment();
        fragment.setActivityContract(checkedUIView);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSearchSubmitted(String text) {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        loadInitialData();
    }

    @Override
    public void onSearchTextChanged(String text) {
        if(text!=null && text.length()==0){
            loadInitialData();
        }
    }

    @Override
    public void onItemClicked(AttachProductItemViewModel attachProductItemViewModel) {

    }

    @Override
    protected void loadInitialData() {
        adapter.clearAllElements();
        getRecyclerViewLayoutManager().scrollToPosition(0);
        super.loadInitialData();
    }

    @Override
    public void loadData(int page) {
        presenter.loadProductData(searchInputView.getSearchText(),activityContract.getShopId(),page);
    }

    @NonNull
    @Override
    protected BaseListAdapter<AttachProductItemViewModel, AttachProductListAdapterTypeFactory> createAdapterInstance() {
        adapter = new AttachProductListAdapter(getAdapterTypeFactory());
        return adapter;
    }

    @Override
    public BaseListAdapter<AttachProductItemViewModel, AttachProductListAdapterTypeFactory> getAdapter() {
        return adapter;
    }

    @Override
    protected AttachProductListAdapterTypeFactory getAdapterTypeFactory() {
        return new AttachProductListAdapterTypeFactory(this);
    }


    @Override
    public boolean shouldAllowCheckChange(int position, boolean checked) {
        boolean isCurrentlyChecked = isChecked(position);
        boolean willCheckedStatusChanged = (isCurrentlyChecked ^ checked);
        if(adapter.getCheckedCount() >= MAX_CHECKED && (willCheckedStatusChanged && !isCurrentlyChecked)) {
            String message = getString(R.string.string_attach_product_warning_max_product_format,String.valueOf(MAX_CHECKED));
            NetworkErrorHelper.showSnackbar(getActivity(), message);
            return false;
        }
        else
            return true;
    }

    @Override
    public boolean isChecked(int position) {
            return adapter.isChecked(position);
    }

    @Override
    public void updateListByCheck(boolean isChecked, int position) {
        adapter.itemChecked(isChecked,position);
        presenter.updateCheckedList(adapter.getCheckedDataList());
        trackAction();
    }

    @Override
    public void addProductToList(List<AttachProductItemViewModel> products, boolean hasNextPage) {
        renderList(products,hasNextPage);
    }

    @Override
    public void hideAllLoadingIndicator() {
        hideLoading();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showErrorMessage(Throwable throwable) {
        showGetListError(throwable);
    }

    @Override
    public void updateButtonBasedOnChecked(int checkedCount) {
        sendButton.setText(getString(R.string.string_attach_product_send_button_text,String.valueOf(checkedCount),String.valueOf(MAX_CHECKED)));
        if (checkedCount > 0 && checkedCount <=MAX_CHECKED)
            sendButton.setEnabled(true);
        else
            sendButton.setEnabled(false);
    }

    private void sendButtonClicked(){
        presenter.completeSelection();
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        EmptyResultViewModel emptyResultViewModel = new EmptyResultViewModel();
        emptyResultViewModel.setContent(getString(R.string.string_attach_product_empty_product));
        emptyResultViewModel.setIconRes(R.drawable.bg_attach_product_empty_result);
        if(activityContract.isSeller()) {
            emptyResultViewModel.setButtonTitleRes(R.string.string_attach_product_add_product_now);
            emptyResultViewModel.setCallback(new EmptyResultViewHolder.Callback() {
                @Override
                public void onEmptyContentItemTextClicked() {}

                @Override
                public void onEmptyButtonClicked() {
                    addProductClicked();
                }
            });
        }
        return emptyResultViewModel;
    }

    public void addProductClicked(){
        activityContract.goToAddProduct(activityContract.getShopId());
    }

    private void trackAction(){
        ((AbstractionRouter)getActivity().getApplicationContext()).getAnalyticTracker().sendEventTracking(
                AttachProductAnalytics.getEventCheckProduct().getEvent()
        );
    }
}
