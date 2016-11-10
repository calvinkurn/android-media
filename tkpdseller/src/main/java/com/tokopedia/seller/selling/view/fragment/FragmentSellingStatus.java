package com.tokopedia.seller.selling.view.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.tracking.activity.TrackingActivity;
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

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
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
public class FragmentSellingStatus extends BaseFragment<SellingStatusTransaction> implements SellingStatusTransactionView, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R2.id.order_list)
    RecyclerView recyclerView;
    @Bind(R2.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;
    @Bind(R2.id.root)
    CoordinatorLayout rootView;
    SearchView searchTxt;
    @Bind(R2.id.fab)
    FloatingActionButton fab;

    private static final String ORDER_ID = "OrderID";

    private LinearLayoutManager linearLayoutManager;
    private BaseSellingAdapter adapter;
    private Dialog editRefDialog;
    private BottomSheetDialog bottomSheetDialog;
    private View filterView;
    private TkpdProgressDialog progressDialog;
    private boolean isVisibleToUser;

    public static FragmentSellingStatus newInstance() {

        Bundle args = new Bundle();
        FragmentSellingStatus fragment = new FragmentSellingStatus();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        filterView = getActivity().getLayoutInflater().inflate(R.layout.filter_layout_selling_status, null);
        searchTxt = ButterKnife.findById(filterView, R.id.search);
        int searchPlateId = searchTxt.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchTxt.findViewById(searchPlateId);
        searchPlate.setBackgroundColor(Color.TRANSPARENT);
        searchTxt.setOnQueryTextListener(this);
        swipeToRefresh.setOnRefreshListener(this);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(filterView);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        if(isVisibleToUser){
            onCallNetwork();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
        initPresenter();
        if (isVisibleToUser && isDataEmpty()) {
            onCallNetwork();
        }
        presenter.checkValidationToSendGoogleAnalytic(isVisibleToUser, getActivity());
        super.setUserVisibleHint(isVisibleToUser);
    }

    private boolean isDataEmpty() {
        try {
            return (presenter.getPaging().getPage() == 1 && !isLoading() && adapter.getListData().size() == 0);
        } catch (Exception e){
            return true;
        }
    }

    @Override
    protected void initPresenter() {
        if(presenter==null) {
            presenter = new SellingStatusTransactionImpl(this);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shipping_status;
    }

    @Override
    public void setupRecyclerView() {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading() && linearLayoutManager.findLastVisibleItemPosition() == linearLayoutManager.getItemCount() - 1) {
                    presenter.loadMore(getActivity());
                }
            }
        });
    }

    @Override
    public void initAdapter() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new BaseSellingAdapter<SellingStatusTxModel, ViewHolder>(SellingStatusTxModel.class, R.layout.selling_transaction_list_item, ViewHolder.class) {
            @Override
            protected void populateViewHolder(FragmentSellingStatus.ViewHolder viewHolder, final SellingStatusTxModel model, int position) {
                viewHolder.bindDataModel(getActivity(), model);
                viewHolder.setOnItemClickListener(new BaseSellingViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClicked(int position) {
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
            protected FragmentSellingStatus.ViewHolder getViewHolder(int mModelLayout, ViewGroup parent) {
                ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);
                return new FragmentSellingStatus.ViewHolder(view);
            }
        };
        adapter.setOnRetryListener(new BaseSellingAdapter.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                onCallNetwork();
            }
        });
    }

    @Override
    public void displayLoadMore(boolean isLoadMore) {
        adapter.setIsLoading(isLoadMore);
    }

    @Override
    public boolean isLoading() {
        return adapter.getItemViewType(linearLayoutManager.findLastCompletelyVisibleItemPosition()) == TkpdState.RecyclerView.VIEW_LOADING;
    }

    @Override
    public void setPullEnabled(boolean isPullEnabled) {
        swipeToRefresh.setEnabled(isPullEnabled);
    }

    @Override
    public int getDataSize() {
        return adapter.getListData() != null ? adapter.getListData().size() : -1;
    }

    @Override
    public void onCallNetwork() {
        if (getActivity() != null) {
            adapter.setIsLoading(!swipeToRefresh.isRefreshing());
            presenter.callNetworkStatus(getActivity(), searchTxt.getQuery().toString());
        }
    }


    @Override
    public void onCallStatusLoadMore(List<SellingStatusTxModel> data) {
        if (swipeToRefresh.isRefreshing()) {
            adapter.clearData();
            swipeToRefresh.setRefreshing(false);
        }
        adapter.setListModel(data);
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
    public void onMessageError(int type, Object... data) {

    }

    @Override
    public void onRefresh() {
        onCallNetwork();
    }

    @Override
    public void onNoResult() {
        swipeToRefresh.setRefreshing(false);
        adapter.clearData();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.clearData();
        adapter.setIsLoading(true);
        onCallNetwork();
        bottomSheetDialog.hide();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.length() == 0){
            adapter.clearData();
            adapter.setIsLoading(true);
            onCallNetwork();
            bottomSheetDialog.hide();
        }
        return false;
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
                FragmentSellingStatusPermissionsDispatcher.onStartBarcodeScannerWithCheck(FragmentSellingStatus.this);
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
                onCallNetwork();
            }

            @Override
            public void onFailed(String errorMsg) {
                CommonUtils.UniversalToast(getActivity(), errorMsg);
                progressDialog.dismiss();
                adapter.clearData();
                onCallNetwork();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R2.id.fab)
    public void onClick() {
        bottomSheetDialog.show();
    }

    public interface LVShopStatusInterface {
        void onEditRef(SellingStatusTxModel model);

        void onTrack(SellingStatusTxModel model);
    }

    public static class ViewHolder extends BaseSellingViewHolder<SellingStatusTxModel> {
        @Bind(R2.id.icon)
        ImageView icon;
        @Bind(R2.id.subtitle)
        TextView subtitle;
        @Bind(R2.id.title)
        TextView title;
        @Bind(R2.id.overflow_btn)
        LinearLayout overflow_btn;
        @Bind(R2.id.deadline_view)
        LinearLayout deadLineContainer;
        @Bind(R2.id.status)
        TextView status;
        @Bind(R2.id.deadline_date)
        TextView deadlineDate;
        @Bind(R2.id.invoice)
        TextView invoice;
        @Bind(R2.id.list_item)
        LinearLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setOnItemClickListener(final OnItemClickListener clickListener) {
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
        }

        public void bindDataModel(Context context, SellingStatusTxModel model) {
            ImageHandler.loadImageCircle2(context, icon, model.AvatarUrl);
            title.setText(model.UserName);
            subtitle.setText(model.LastStatus);
            invoice.setText(model.Invoice);
            if (!CommonUtils.checkNullForZeroJson(model.DeadlineFinish)) {
                deadLineContainer.setVisibility(View.GONE);
            } else if (status.getText().toString().trim().equals("Transaksi selesai")) {
                deadLineContainer.setVisibility(View.GONE);
            } else {
                deadLineContainer.setVisibility(View.VISIBLE);
                deadlineDate.setText(model.DeadlineFinish);
            }
            setOverflow(model);
        }

        private void setOverflow(SellingStatusTxModel model) {
            if (!model.Permission.equals("0") && (model.OrderStatus.equals("500") || model.OrderStatus.equals("501") || model.OrderStatus.equals("520") || model.OrderStatus.equals("530"))) {
                overflow_btn.setVisibility(View.VISIBLE);
            } else {
                overflow_btn.setVisibility(View.GONE);
            }
        }

    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onStartBarcodeScanner() {
        startActivityForResult(CommonUtils.requestBarcodeScanner(), 0);
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
