package com.tokopedia.seller.selling.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

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
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.selling.view.viewHolder.StatusViewHolder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Erry on 7/19/2016.
 */
@RuntimePermissions
public class FragmentSellingStatus extends BaseFragment<SellingStatusTransaction> implements SellingStatusTransactionView, SearchView.OnQueryTextListener {

    @BindView(R2.id.order_list)
    RecyclerView recyclerView;
    @BindView(R2.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;
    @BindView(R2.id.root)
    CoordinatorLayout rootView;
    SearchView searchTxt;
    @BindView(R2.id.fab)
    FloatingActionButton fab;

    private PagingHandler mPaging;

    private static final String ORDER_ID = "OrderID";
    public static final int REQUEST_CODE_BARCODE = 1;

    private LinearLayoutManager linearLayoutManager;
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
        ScreenTracking.screenLoca(AppScreen.SCREEN_LOCA_SHIPPINGSTATUS);
        ScreenTracking.screenLoca(AppScreen.SCREEN_TX_SHOP_SHIPPING_STATUS);
        presenter.checkValidationToSendGoogleAnalytic(isVisibleToUser, getActivity());
    }


    @Override
    protected void initPresenter() {
        if(presenter==null) {
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
    public void onMessageError(int type, Object... data) {

    }

    @Override
    public void initHandlerAndAdapter() {
        setRetainInstance(true);
        mPaging = new PagingHandler();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new BaseSellingAdapter<SellingStatusTxModel, StatusViewHolder>(SellingStatusTxModel.class, getActivity(),  R.layout.selling_transaction_list_item, StatusViewHolder.class) {
            @Override
            protected void populateViewHolder(StatusViewHolder viewHolder, final SellingStatusTxModel model, int position) {
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
                        intent.putExtra(SellingDetailActivity.TYPE_EXTRA, SellingDetailActivity.Type.STATUS);
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
        initView();
        return view;
    }

    public void initView() {
        refresh = new RefreshHandler(getActivity(), rootView, onRefreshListener());
        setRefreshPullEnable(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        filterView = getActivity().getLayoutInflater().inflate(R.layout.filter_layout_selling_status, null);
        searchTxt = ButterKnife.findById(filterView, R.id.search);
        int searchPlateId = searchTxt.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchTxt.findViewById(searchPlateId);
        searchPlate.setBackgroundColor(Color.TRANSPARENT);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(filterView);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_CODE_BARCODE) {
                if(editRefDialog != null && editRefDialog.isShowing()) {
                    ref.setText(CommonUtils.getBarcode(data));
                }
            }
        }
    }

    private void createEditRefDialog(final SellingStatusTxModel model) {
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
                FragmentSellingStatusPermissionsDispatcher.onStartBarcodeScannerWithCheck(FragmentSellingStatus.this);
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
                presenter.getStatusTransactionList(getUserVisibleHint(), SellingStatusTransactionImpl.Type.STATUS);
            }

            @Override
            public void onFailed(String errorMsg) {
                CommonUtils.UniversalToast(getActivity(), errorMsg);
                progressDialog.dismiss();
                adapter.clearData();
                presenter.getStatusTransactionList(getUserVisibleHint(), SellingStatusTransactionImpl.Type.STATUS);
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


    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onStartBarcodeScanner() {
        startActivityForResult(CommonUtils.requestBarcodeScanner(), REQUEST_CODE_BARCODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FragmentSellingStatusPermissionsDispatcher.onRequestPermissionsResult(FragmentSellingStatus.this, requestCode, grantResults);
    }


    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onShowRationale(getActivity(), request, listPermission);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(getActivity(),Manifest.permission.CAMERA);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(),Manifest.permission.CAMERA);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onPermissionDenied(getActivity(),listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onNeverAskAgain(getActivity(),listPermission);
    }
}
