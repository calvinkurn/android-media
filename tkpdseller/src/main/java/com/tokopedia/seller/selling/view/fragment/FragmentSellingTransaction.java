package com.tokopedia.seller.selling.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.DatePickerV2;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.tracking.activity.TrackingActivity;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.facade.FacadeActionShopTransaction;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.selling.presenter.adapter.BaseSellingAdapter;
import com.tokopedia.seller.selling.view.viewHolder.BaseSellingViewHolder;
import com.tokopedia.seller.selling.view.activity.SellingDetailActivity;
import com.tokopedia.seller.selling.model.SellingStatusTxModel;
import com.tokopedia.seller.selling.presenter.SellingStatusTransaction;
import com.tokopedia.seller.selling.presenter.SellingStatusTransactionImpl;
import com.tokopedia.seller.selling.presenter.SellingStatusTransactionView;
import com.tokopedia.core.session.baseFragment.BaseFragment;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.ValidationTextUtil;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.selling.view.viewHolder.TransactionViewHolder;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Erry on 7/19/2016.
 */
public class FragmentSellingTransaction extends BaseFragment<SellingStatusTransaction> implements SellingStatusTransactionView, View.OnClickListener {

    @BindView(R2.id.order_list)
    RecyclerView recyclerView;
    @BindView(R2.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;
    @BindView(R2.id.fab)
    FloatingActionButton fab;
    @BindView(R2.id.root)
    CoordinatorLayout rootView;

    private static final String ORDER_ID = "OrderID";

    private LinearLayoutManager linearLayoutManager;
    private BaseSellingAdapter adapter;
    private Dialog editRefDialog;
    private TkpdProgressDialog progressDialog;
    private DatePickerV2 datePicker;
    private EditText startDate;
    private EditText endDate;
    private SearchView searchTxt;
    private Spinner spinnerFilter;
    private TextView searchbtn;
    private View filterView;
    private BottomSheetDialog bottomSheetDialog;
    protected boolean inhibit_spinner = true;

    private PagingHandler mPaging;
    private RefreshHandler refresh;

    public static FragmentSellingTransaction newInstance() {

        Bundle args = new Bundle();
        FragmentSellingTransaction fragment = new FragmentSellingTransaction();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        initPresenter();

        presenter.getStatusTransactionList(isVisibleToUser, SellingStatusTransactionImpl.Type.TRANSACTION);
        ScreenTracking.screenLoca(AppScreen.SCREEN_LOCA_TXSTATUS);
        ScreenTracking.screen(AppScreen.SCREEN_TX_SHOP_TRANSACTION_SELLING_LIST);
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R2.id.start_date:
                datePicker.getDatePicker(onStartPicked(), new DatePickerV2.Date(startDate.getText().toString()));
                break;
            case R2.id.end_date:
                datePicker.getDatePicker(onEndPicked(), new DatePickerV2.Date(endDate.getText().toString()));
                break;
            case R2.id.search_button:
                String search = searchTxt.getQuery().toString();
                if (!TextUtils.isEmpty(search)) {
                    if (ValidationTextUtil.isValidSalesQuery(search)) {
                        presenter.refreshOnFilter();
                    } else {
                        Snackbar.make(filterView, getActivity().getString(R.string.keyword_min_3_char), Snackbar.LENGTH_LONG).show();
                    }
                } else if (TextUtils.isEmpty(search)) {
                    presenter.refreshOnFilter();
                }
                break;
        }
    }

    private void setDate() {
        try {
            startDate.setText(DateFormatUtils.getStringDateAfter(-30));
            endDate.setText(DateFormatUtils.getStringDateAfter(0));
        } catch (NullPointerException e) {
        }
    }

    private DatePickerV2.OnDatePickerV2Listener onStartPicked() {
        return new DatePickerV2.OnDatePickerV2Listener() {
            @Override
            public void onDatePicked(DatePickerV2.Date date) {
                startDate.setText(date.getDate());
            }

            @Override
            public void onCancel() {

            }
        };
    }

    private DatePickerV2.OnDatePickerV2Listener onEndPicked() {
        return new DatePickerV2.OnDatePickerV2Listener() {
            @Override
            public void onDatePicked(DatePickerV2.Date date) {
                endDate.setText(date.getDate());
            }

            @Override
            public void onCancel() {

            }
        };
    }

    private AdapterView.OnItemSelectedListener onFilterSelected() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (inhibit_spinner) {
                    inhibit_spinner = false;
                }else {
                    presenter.refreshOnFilter();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        };
    }

    @Override
    protected void initPresenter() {
        if(presenter==null) {
            presenter = new SellingStatusTransactionImpl(this, SellingStatusTransactionImpl.Type.TRANSACTION);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shipping_status;
    }

    private String getFilter(int pos) {
        String[] filterValue = getResources().getStringArray(R.array.transaction_filter_type_ppl_value);
        return filterValue[pos];
    }

    @Override
    public int getFragmentId() {
        return 0;
    }

    @Override
    public void ariseRetry(int type, Object... data) {
        Log.d(TAG, "ariseRetry type " + type);
    }

    @Override
    public void setData(int type, Bundle data) {

    }

    @Override
    public void onNetworkError(int type, Object... data) {
        swipeToRefresh.setRefreshing(false);
        if(adapter.getListData().size() == 0) {
            adapter.setIsLoading(false);
            adapter.setIsRetry(true);
        }else{
            NetworkErrorHelper.showSnackbar(getActivity());
        }
    }

    @Override
    public void initHandlerAndAdapter() {
        setRetainInstance(true);
        mPaging = new PagingHandler();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new BaseSellingAdapter<SellingStatusTxModel, TransactionViewHolder>(SellingStatusTxModel.class, getActivity(), R.layout.selling_transaction_list_item, TransactionViewHolder.class) {
            @Override
            protected void populateViewHolder(TransactionViewHolder viewHolder, final SellingStatusTxModel model, int position) {
                viewHolder.bindDataModel(getActivity(), model);
                viewHolder.setOnItemClickListener(new BaseSellingViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClicked(int position) {
                        if(adapter.isLoading()) {
                            getPaging().setPage(getPaging().getPage() - 1);
                            presenter.finishConnection();
                        }
                        Intent intent = new Intent(getActivity(), SellingDetailActivity.class);
                        intent.putExtra(SellingDetailActivity.DATA_EXTRA, Parcels.wrap(model));
                        intent.putExtra(SellingDetailActivity.TYPE_EXTRA, SellingDetailActivity.Type.TRANSACTION);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClicked(int position) {

                    }
                });
                viewHolder.overflow_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createOverflowMenu(v, model);
                    }
                });
            }

            @Override
            protected TransactionViewHolder getViewHolder(int mModelLayout, ViewGroup parent) {
                ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);
                return new TransactionViewHolder(view);
            }
        };
    }

    @Override
    public boolean getUserVisible() {
        return getUserVisibleHint();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    public void initView() {
        refresh = new RefreshHandler(getActivity(), rootView, onRefreshListener());
        setRefreshPullEnable(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        filterView = getActivity().getLayoutInflater().inflate(R.layout.filter_layout_transaction, null);
        searchTxt = ButterKnife.findById(filterView, R.id.search);
        startDate = ButterKnife.findById(filterView, R.id.start_date);

        endDate = ButterKnife.findById(filterView, R.id.end_date);

        spinnerFilter = ButterKnife.findById(filterView, R.id.transaction_filter);
        searchbtn = ButterKnife.findById(filterView, R.id.search_button);

        datePicker = DatePickerV2.createInstance(getActivity());
        spinnerFilter.setOnItemSelectedListener(onFilterSelected());
        int searchPlateId = searchTxt.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchTxt.findViewById(searchPlateId);
        searchPlate.setBackgroundColor(Color.TRANSPARENT);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(filterView);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        setDate();
    }

    private RefreshHandler.OnRefreshHandlerListener onRefreshListener() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                presenter.onRefreshView();
            }
        };
    }

    @Override
    public void setRefreshPullEnable(boolean b) {
        refresh.setPullEnabled(b);
    }

    @Override
    public String getQuery() {
        return searchTxt.getQuery().toString();
    }

    @Override
    public void notifyDataSetChanged(List<SellingStatusTxModel> listDatas) {
        adapter.clearData();
        adapter.setListModel(listDatas);
    }

    @Override
    public void finishRefresh() {
        refresh.finishRefresh();
    }

    @Override
    public void removeRetry() {
        adapter.setIsRetry(false);
    }

    @Override
    public void removeLoading() {
        adapter.setIsLoading(false);
    }

    @Override
    public View getRootView() {
        return getView();
    }

    @Override
    public void setRefreshing(boolean b) {
        refresh.setRefreshing(b);
    }

    @Override
    public void addLoadingFooter() {
        adapter.setIsLoading(true);
    }

    @Override
    public void initListener() {
        adapter.setOnRetryListener(new BaseSellingAdapter.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                presenter.getStatusTransactionList(getUserVisibleHint(), SellingStatusTransactionImpl.Type.TRANSACTION);
            }
        });
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        searchbtn.setOnClickListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                presenter.onScrollList(linearLayoutManager.findLastVisibleItemPosition() == linearLayoutManager.getItemCount() - 1);
            }
        });
    }

    @Override
    public void hideFilterView() {
        bottomSheetDialog.hide();
    }

    @Override
    public boolean getRefreshing() {
        return refresh.isRefreshing();
    }

    @Override
    public void resetPage() {
        mPaging.resetPage();
    }

    @Override
    public PagingHandler getPaging() {
        return mPaging;
    }

    @Override
    public void addRetry() {
        adapter.setIsRetry(true);
    }

    @Override
    public void addEmptyView() {
        adapter.setIsDataEmpty(true);
    }

    @Override
    public void removeEmpty() {
        adapter.setIsDataEmpty(false);
    }

    @Override
    public String getFilter() {
        return getFilter(spinnerFilter.getSelectedItemPosition());
    }

    @Override
    public String getStartDate() {
        return startDate.getText().toString();
    }

    @Override
    public String getEndDate() {
        return endDate.getText().toString();
    }

    @Override
    public void onMessageError(int type, Object... data) {

    }

    @Override
    public void onPause() {
        presenter.finishConnection();
        super.onPause();
    }

    private void createEditRefDialog(final SellingStatusTxModel model) {
        editRefDialog = new Dialog(getActivity());
        editRefDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editRefDialog.setContentView(R.layout.dialog_edit_ref);
        final EditText Ref = (EditText) editRefDialog.findViewById(R.id.ref_number);
        Ref.setText(model.RefNum);
        TextView ConfirmButton = (TextView) editRefDialog.findViewById(R.id.confirm_button);
        View vScan = editRefDialog.findViewById(R.id.scan);
        vScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(CommonUtils.requestBarcodeScanner(), 0);
            }
        });
        ConfirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkEditRef(Ref, model)) {
                    actionEditRefNum(Ref.getText().toString(), model);
                    editRefDialog.dismiss();
                }
            }
        });

        editRefDialog.show();
    }

    private void actionEditRefNum(String refNum, SellingStatusTxModel model) {
        model.RefNum = refNum;
        FacadeActionShopTransaction facadeAction = FacadeActionShopTransaction.createInstance(getActivity(), model.OrderId);
        progressDialog.showDialog();
        facadeAction.setCompositeSubscription(new CompositeSubscription());
        facadeAction.editRefNum(refNum, onEditRefNum());
    }

    private FacadeActionShopTransaction.OnEditRefNumListener onEditRefNum() {
        return new FacadeActionShopTransaction.OnEditRefNumListener() {
            @Override
            public void onSuccess(String refNum) {
                progressDialog.dismiss();
                adapter.clearData();
                presenter.getStatusTransactionList(getUserVisibleHint(), SellingStatusTransactionImpl.Type.TRANSACTION);
            }

            @Override
            public void onFailed(String errorMsg) {
                CommonUtils.UniversalToast(getActivity(), errorMsg);
                adapter.clearData();
                progressDialog.dismiss();
                presenter.getStatusTransactionList(getUserVisibleHint(), SellingStatusTransactionImpl.Type.TRANSACTION);
            }
        };
    }

    private boolean checkEditRef(EditText ref, SellingStatusTxModel model) {
        if (ref.getText().toString().equals(model.RefNum)) {
            ref.setError(getActivity().getString(R.string.edit_ref_error));
            return false;
        }

        if (ref.length() > 7 && ref.length() < 18) {
            return true;
        } else {
            if (ref.length() == 0)
                ref.setError(getString(R.string.error_field_required));
            else
                ref.setError(getString(R.string.error_receipt_number));
            return false;
        }
    }

    private void createOverflowMenu(View v, SellingStatusTxModel model) {
        PopupMenu menu = new PopupMenu(getActivity(), v);
        if (model.ShippingID.equals(TkpdState.SHIPPING_ID.GOJEK)) {
            menu.getMenuInflater().inflate(R.menu.shipping_status_menu_track_only, menu.getMenu());
        } else {
            menu.getMenuInflater().inflate(R.menu.shipping_status_menu, menu.getMenu());
        }
        menu.setOnMenuItemClickListener(onMenuItemClick(model, new LVShopStatusInterface() {
            @Override
            public void onEditRef(SellingStatusTxModel model) {
                createEditRefDialog(model);
            }

            @Override
            public void onTrack(SellingStatusTxModel model) {
                Bundle bundle = new Bundle();
                bundle.putString(ORDER_ID, model.OrderId);
                getActivity().startActivity(TrackingActivity.createInstance(getActivity(),bundle));
            }
        }));
        menu.show();
    }

    private PopupMenu.OnMenuItemClickListener onMenuItemClick(final SellingStatusTxModel model, final LVShopStatusInterface listener) {
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R2.id.action_edit:
                        listener.onEditRef(model);
                        return true;

                    case R2.id.action_track:
                        listener.onTrack(model);
                        return true;

                    default:
                        return false;
                }
            }

            ;
        };
    }

    @OnClick(R2.id.fab)
    public void onClick() {
        bottomSheetDialog.show();
    }

    public interface LVShopStatusInterface {
        void onEditRef(SellingStatusTxModel model);

        void onTrack(SellingStatusTxModel model);
    }


}
