package com.tokopedia.seller.selling.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import android.widget.Toast;

import com.tkpd.library.ui.utilities.DatePickerV2;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.transactionmodule.TransactionRouter;
import com.tokopedia.core.session.baseFragment.BaseFragment;
import com.tokopedia.core.tracking.activity.TrackingActivity;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.ValidationTextUtil;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.facade.FacadeActionShopTransaction;
import com.tokopedia.seller.selling.model.SellingStatusTxModel;
import com.tokopedia.seller.selling.presenter.SellingStatusTransaction;
import com.tokopedia.seller.selling.presenter.SellingStatusTransactionImpl;
import com.tokopedia.seller.selling.presenter.SellingStatusTransactionView;
import com.tokopedia.seller.selling.presenter.adapter.BaseSellingAdapter;
import com.tokopedia.seller.selling.view.activity.SellingDetailActivity;
import com.tokopedia.seller.selling.view.viewHolder.BaseSellingViewHolder;
import com.tokopedia.seller.selling.view.viewHolder.TransactionViewHolder;

import org.parceler.Parcels;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Erry on 7/19/2016.
 */
public class FragmentSellingTransaction extends BaseFragment<SellingStatusTransaction> implements SellingStatusTransactionView, View.OnClickListener {

    RecyclerView recyclerView;
    SwipeToRefresh swipeToRefresh;
    FloatingActionButton fab;
    CoordinatorLayout rootView;

    private static final String ORDER_ID = "OrderID";

    private BaseSellingAdapter adapter;
    private Dialog editRefDialog;
    private TkpdProgressDialog progressDialog;
    private DatePickerV2 datePicker;
    protected EditText startDate;
    protected EditText endDate;
    protected SearchView searchTxt;
    protected Spinner spinnerFilter;
    protected TextView searchbtn;
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
        ScreenTracking.screen(MainApplication.getAppContext(),AppScreen.SCREEN_TX_SHOP_TRANSACTION_SELLING_LIST);
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.start_date) {
            datePicker.getDatePicker(onStartPicked(), new DatePickerV2.Date(startDate.getText().toString()));

        } else if (i == R.id.end_date) {
            datePicker.getDatePicker(onEndPicked(), new DatePickerV2.Date(endDate.getText().toString()));

        } else if (i == R.id.search_button) {
            String search = searchTxt.getQuery().toString();
            if (!TextUtils.isEmpty(search)) {
                if (ValidationTextUtil.isValidSalesQuery(search)) {
                    presenter.refreshOnFilter();
                } else {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.keyword_min_3_char), Toast.LENGTH_SHORT).show();
                }
            } else if (TextUtils.isEmpty(search)) {
                presenter.refreshOnFilter();
            }

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
                        Intent intent = ((TransactionRouter) MainApplication.getAppContext())
                                .goToOrderDetail(
                                        getActivity(),
                                        model.OrderId);
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
        recyclerView = (RecyclerView) view.findViewById(R.id.order_list);
        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        rootView = (CoordinatorLayout) view.findViewById(R.id.root);
        initView();
        return view;
    }

    public void initView() {
        refresh = new RefreshHandler(getActivity(), rootView, onRefreshListener());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        filterView = getActivity().getLayoutInflater().inflate(R.layout.filter_layout_transaction, null);
        searchTxt = (SearchView) filterView.findViewById(R.id.search);
        startDate = (EditText) filterView.findViewById(R.id.start_date);

        endDate = (EditText)filterView.findViewById(R.id.end_date);

        spinnerFilter = (Spinner) filterView.findViewById(R.id.transaction_filter);
        searchbtn =  (TextView) filterView.findViewById(R.id.search_button);

        datePicker = DatePickerV2.createInstance(getActivity());
        spinnerFilter.setOnItemSelectedListener(onFilterSelected());
        int searchPlateId = searchTxt.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchTxt.findViewById(searchPlateId);
        searchPlate.setBackgroundColor(Color.TRANSPARENT);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(filterView);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        setDate();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });
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
                presenter.onScrollList(((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition() == recyclerView.getLayoutManager().getItemCount() - 1);
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
    public void showFab() {
        fab.show();
    }

    @Override
    public void hideFab() {
        fab.hide();
    }

    @Override
    public void onMessageError(int type, Object... data) {

    }

    @Override
    public void onPause() {
        presenter.finishConnection();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
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
                CommonUtils.requestBarcodeScanner(FragmentSellingTransaction.this,
                        CustomScannerBarcodeActivity.class);
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
                presenter.onRefreshView();;
            }

            @Override
            public void onFailed(String errorMsg) {
                CommonUtils.UniversalToast(getActivity(), errorMsg);
                progressDialog.dismiss();
                presenter.onRefreshView();
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

    private void createOverflowMenu(View v, final SellingStatusTxModel model) {
        final PopupMenu menu = new PopupMenu(getActivity(), v);
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
                String routingAppLink;
                routingAppLink = ApplinkConst.ORDER_TRACKING;
                Uri.Builder uriBuilder = new Uri.Builder();
                uriBuilder.appendQueryParameter(
                        ApplinkConst.Query.ORDER_TRACKING_ORDER_ID,
                        model.OrderId);
                if (!TextUtils.isEmpty(model.liveTracking)) {
                    uriBuilder.appendQueryParameter(
                            ApplinkConst.Query.ORDER_TRACKING_URL_LIVE_TRACKING,
                            model.liveTracking);
                }
                routingAppLink += uriBuilder.toString();
                RouteManager.route(getActivity(), routingAppLink);
            }

            @Override
            public void onCourierRetry() {
                createRetryPickupDialog(model.OrderId, menu.getMenu().findItem(R.id.action_retry));
            }
        }));
        menu.show();
    }

    private PopupMenu.OnMenuItemClickListener onMenuItemClick(final SellingStatusTxModel model, final LVShopStatusInterface listener) {
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_edit) {
                    listener.onEditRef(model);
                    return true;
                } else if (item.getItemId() == R.id.action_track) {
                    listener.onTrack(model);
                    return true;
                }
                else if (item.getItemId() == R.id.action_retry) {
                    listener.onCourierRetry();
                    return true;
                } else {
                    return false;
                }
            }

            ;
        };
    }

    public interface LVShopStatusInterface {
        void onEditRef(SellingStatusTxModel model);

        void onTrack(SellingStatusTxModel model);

        void onCourierRetry();
    }

    private void createRetryPickupDialog(final String orderId, MenuItem retryMenu) {
        AlertDialog.Builder retryPickupDialog = new AlertDialog.Builder(getActivity());
        retryPickupDialog.setView(R.layout.retry_pickup_dialog);
        retryPickupDialog.setPositiveButton(getString(R.string.title_yes),
                onConfirmRetryPickup(orderId, retryMenu));
        retryPickupDialog.setNegativeButton(getString(R.string.title_cancel), null);
        retryPickupDialog.show();
    }

    private DialogInterface.OnClickListener onConfirmRetryPickup(final String orderId,
                                                                 final MenuItem retryMenu) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.showDialog();
                FacadeActionShopTransaction facadeAction = FacadeActionShopTransaction
                        .createInstance(getActivity(), orderId);
                facadeAction.retryCourierPickup(new FacadeActionShopTransaction
                        .OnRetryPickupListener() {
                    @Override
                    public void onSuccess(String successMessage) {
                        progressDialog.dismiss();
                        Snackbar.make(rootView, successMessage, Snackbar.LENGTH_LONG).show();
                        retryMenu.setVisible(false);
                    }

                    @Override
                    public void onFailed(String errorMessage) {
                        progressDialog.dismiss();
                        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
                    }
                });
            }
        };
    }

}
