package com.tokopedia.seller.selling.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.session.baseFragment.BaseFragment;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.facade.FacadeActionShopTransaction;
import com.tokopedia.seller.selling.model.SellingStatusTxModel;
import com.tokopedia.seller.selling.presenter.SellingStatusTransaction;
import com.tokopedia.seller.selling.presenter.SellingStatusTransactionImpl;
import com.tokopedia.seller.selling.presenter.SellingStatusTransactionView;
import com.tokopedia.seller.selling.presenter.adapter.BaseSellingAdapter;
import com.tokopedia.seller.selling.view.viewHolder.BaseSellingViewHolder;
import com.tokopedia.seller.selling.view.viewHolder.StatusViewHolder;
import com.tokopedia.transaction.common.TransactionRouter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.permissionchecker.PermissionCheckerHelper.Companion.PERMISSION_CAMERA;
import static com.tokopedia.permissionchecker.PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE;

/**
 * Created by Erry on 7/19/2016.
 */
public class FragmentSellingStatus extends BaseFragment<SellingStatusTransaction> implements SellingStatusTransactionView, SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    SwipeToRefresh swipeToRefresh;
    CoordinatorLayout rootView;
    SearchView searchTxt;
    FloatingActionButton fab;

    private PagingHandler mPaging;
    private PermissionCheckerHelper permissionCheckerHelper;


    private static final String ORDER_ID = "OrderID";
    public static final int REQUEST_CODE_BARCODE = 1;

    private BaseSellingAdapter adapter;
    private Dialog editRefDialog;
    private BottomSheetDialog bottomSheetDialog;
    private View filterView;
    private TkpdProgressDialog progressDialog;
    private boolean isVisibleToUser;
    private RefreshHandler refresh;
    private EditText ref;

    public static FragmentSellingStatus newInstance() {

        Bundle args = new Bundle();
        FragmentSellingStatus fragment = new FragmentSellingStatus();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        presenter.finishConnection();
        super.onPause();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        initPresenter();

        presenter.getStatusTransactionList(isVisibleToUser, SellingStatusTransactionImpl.Type.STATUS);
        super.setUserVisibleHint(isVisibleToUser);
        ScreenTracking.screen(MainApplication.getAppContext(),AppScreen.SCREEN_TX_SHOP_SHIPPING_STATUS);
    }


    @Override
    protected void initPresenter() {
        if (presenter == null) {
            presenter = new SellingStatusTransactionImpl(this, SellingStatusTransactionImpl.Type.STATUS);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shipping_status;
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
    public void removeEmpty() {
        adapter.setIsDataEmpty(false);
    }

    @Override
    public String getFilter() {
        return "";
    }

    @Override
    public String getStartDate() {
        return "";
    }

    @Override
    public String getEndDate() {
        return "";
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
    public void onNetworkError(int type, Object... data) {
        swipeToRefresh.setRefreshing(false);
        if (adapter.getListData().size() == 0) {
            adapter.setIsLoading(false);
            adapter.setIsRetry(true);
        } else {
            NetworkErrorHelper.showSnackbar(getActivity());
        }
    }

    @Override
    public void onMessageError(int type, Object... data) {

    }

    @Override
    public void initHandlerAndAdapter() {
        setRetainInstance(true);
        mPaging = new PagingHandler();
        adapter = new BaseSellingAdapter<SellingStatusTxModel, StatusViewHolder>(SellingStatusTxModel.class, getActivity(), R.layout.selling_transaction_list_item, StatusViewHolder.class) {
            @Override
            protected void populateViewHolder(StatusViewHolder viewHolder, final SellingStatusTxModel model, int position) {
                viewHolder.bindDataModel(getActivity(), model);
                viewHolder.setOnItemClickListener(new BaseSellingViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClicked(int position) {
                        if (adapter.isLoading()) {
                            getPaging().setPage(getPaging().getPage() - 1);
                            presenter.finishConnection();
                        }
                        ;
                        Intent intent = (
                                (TransactionRouter) MainApplication
                                        .getAppContext())
                                .goToOrderDetail(getActivity(), model.OrderId
                                );
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
            protected StatusViewHolder getViewHolder(int mModelLayout, ViewGroup parent) {
                ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);
                return new StatusViewHolder(view);
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
        rootView = (CoordinatorLayout) view.findViewById(R.id.root);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        initView();
        return view;
    }

    public void initView() {
        refresh = new RefreshHandler(getActivity(), rootView, onRefreshListener());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        filterView = getActivity().getLayoutInflater().inflate(R.layout.filter_layout_selling_status, null);
        searchTxt = (SearchView) filterView.findViewById(R.id.search);
        int searchPlateId = searchTxt.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchTxt.findViewById(searchPlateId);
        searchPlate.setBackgroundColor(Color.TRANSPARENT);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(filterView);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
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
                presenter.getStatusTransactionList(getUserVisibleHint(), SellingStatusTransactionImpl.Type.STATUS);
            }
        });
        searchTxt.setOnQueryTextListener(this);
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
    public void addRetryMessage(String message) {
        adapter.addRetryMessage(message);
    }

    @Override
    public void removeRetryMessage() {
        adapter.removeRetryMessage();
    }

    @Override
    public void addEmptyView() {
        adapter.setIsDataEmpty(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        presenter.onQuerySubmit(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        presenter.onQueryChange(newText);
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (editRefDialog != null && editRefDialog.isShowing()) {
            ref.setText(CommonUtils.getBarcode(requestCode, resultCode, data));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createEditRefDialog(final SellingStatusTxModel model) {
        permissionCheckerHelper = new PermissionCheckerHelper();
        editRefDialog = new Dialog(getActivity());
        editRefDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editRefDialog.setContentView(R.layout.dialog_edit_ref);
        ref = (EditText) editRefDialog.findViewById(R.id.ref_number);
        ref.setText(model.RefNum);
        TextView ConfirmButton = (TextView) editRefDialog.findViewById(R.id.confirm_button);
        View vScan = editRefDialog.findViewById(R.id.scan);
        vScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                permissionCheckerHelper.checkPermissions(getActivity(), getPermissions(), new PermissionCheckerHelper.PermissionCheckListener() {
                    @Override
                    public void onPermissionDenied(@NotNull String permissionText) {
                        permissionCheckerHelper.onPermissionDenied(getActivity(),permissionText);
                    }

                    @Override
                    public void onNeverAskAgain(@NotNull String permissionText) {
                        permissionCheckerHelper.onNeverAskAgain(getActivity(),permissionText);
                    }

                    @Override
                    public void onPermissionGranted() {
                        onStartBarcodeScanner();
                    }
                },"");
            }
        });
        ConfirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkEditRef(ref, model)) {
                    actionEditRefNum(ref.getText().toString(), model);
                    editRefDialog.dismiss();
                }
            }
        });

        editRefDialog.show();
    }

    private String[] getPermissions() {
        return new String[]{PERMISSION_CAMERA, PERMISSION_WRITE_EXTERNAL_STORAGE};
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
                presenter.refreshOnFilter();
            }

            @Override
            public void onFailed(String errorMsg) {
                CommonUtils.UniversalToast(getActivity(), errorMsg);
                progressDialog.dismiss();
                presenter.refreshOnFilter();
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
        if (model.isPickUp == 1) {
            menu.getMenuInflater().inflate(R.menu.shipping_status_menu_track_only, menu.getMenu());
        } else {
            menu.getMenuInflater().inflate(R.menu.shipping_status_menu, menu.getMenu());
        }
        if (model.DataList.getIsAllowedRetry() == 1) {
            menu.getMenu().findItem(R.id.action_retry).setVisible(true);
        }
        menu.setOnMenuItemClickListener(onMenuItemClick(model, new LVShopStatusInterface() {
            @Override
            public void onEditRef(SellingStatusTxModel model) {
                createEditRefDialog(model);
            }

            @Override
            public void onTrack(SellingStatusTxModel model) {
               /* String routingAppLink;
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
                RouteManager.route(getActivity(), routingAppLink);*/

                Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalLogistic.ORDER_TRACKING);
                intent.putExtra(ApplinkConst.Query.ORDER_TRACKING_ORDER_ID, model.OrderId);
                if (!TextUtils.isEmpty(model.liveTracking)) {
                    intent.putExtra(ApplinkConst.Query.ORDER_TRACKING_URL_LIVE_TRACKING, model.liveTracking);
                }
                startActivity(intent);
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
                } else if (item.getItemId() == R.id.action_retry) {
                    listener.onCourierRetry();
                    return true;
                } else {
                    return false;
                }
            }
        };
    }


    public interface LVShopStatusInterface {
        void onEditRef(SellingStatusTxModel model);

        void onTrack(SellingStatusTxModel model);

        void onCourierRetry();
    }

    public void onStartBarcodeScanner() {
        CommonUtils.requestBarcodeScanner(this, CustomScannerBarcodeActivity.class);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(getActivity(), requestCode, permissions, grantResults);
        }
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
