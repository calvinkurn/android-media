package com.tokopedia.seller.selling.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.session.baseFragment.BaseFragment;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.seller.selling.SellingService;
import com.tokopedia.seller.selling.presenter.Shipping;
import com.tokopedia.seller.selling.presenter.ShippingImpl;
import com.tokopedia.seller.selling.presenter.ShippingView;
import com.tokopedia.seller.selling.presenter.adapter.BaseSellingAdapter;
import com.tokopedia.seller.selling.view.viewHolder.BaseSellingViewHolder;
import com.tokopedia.seller.selling.view.viewHolder.ShippingViewHolder;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Toped10 on 7/28/2016.
 */
@RuntimePermissions
public class FragmentSellingShipping extends BaseFragment<Shipping> implements ShippingView {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    View rootView;
    private View filterView;
    SearchView search;
    Spinner dueDate;
    Spinner shippingService;
    private TkpdProgressDialog progressDialog;

    public static final int REQUEST_CODE_BARCODE = 1;
    public static final int REQUEST_CODE_PROCESS_RESULT = 2;

    private int getBarcodePosition;
    private BottomSheetDialog bottomSheetDialog;
    private PagingHandler page;
    private RefreshHandler refresh;
    @SuppressWarnings("all")
    private BaseSellingAdapter adapter;
    private MultiSelector multiSelector = new MultiSelector();
    public ActionMode actionMode;
    private boolean inhibitSpinnerShipping = true;
    private boolean inhibitSpinnerDuedate = true;
    private boolean shouldRefreshList = false;


    public static FragmentSellingShipping createInstance() {
        FragmentSellingShipping fragment = new FragmentSellingShipping();
        return fragment;
    }

    @Override
    protected void initPresenter() {
        if (presenter == null) {
            presenter = new ShippingImpl(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.order_list);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        rootView = view.findViewById(R.id.root);
        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shop_shipping_confirmation;
    }

    @Override
    public int getFragmentId() {
        return 0;
    }

    public void requestBarcodeScanner(int pos) {
        getBarcodePosition = pos;
        scanBarCode();
    }

    public void scanBarCode() {
        FragmentSellingShippingPermissionsDispatcher.onScanBarcodeWithCheck(FragmentSellingShipping.this);
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onScanBarcode() {
        CommonUtils.requestBarcodeScanner(this, CustomScannerBarcodeActivity.class);
    }

    public void requestRefNumDialog(final int pos) {
        presenter.requestRefNumDialog(pos, getActivity());
    }

    ModalMultiSelectorCallback selectionMode = new ModalMultiSelectorCallback(multiSelector) {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            super.onCreateActionMode(actionMode, menu);
            FragmentSellingShipping.this.actionMode = actionMode;
            hideFab();
            actionMode.setTitle("1");
            getActivity().getMenuInflater().inflate(R.menu.shipping_confirm_multi, menu);
            refresh.setPullEnabled(false);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.action_multi_send) {
                // Need to finish the action mode before doing the following,
                // not after. No idea why, but it crashes.
                presenter.onMultiConfirm(actionMode, multiSelector.getSelectedPositions());
                adapter.notifyDataSetChanged();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            super.onDestroyActionMode(actionMode);
            enableFilter();
            showFab();
            refresh.setPullEnabled(true);
            multiSelector.clearSelections();
            presenter.updateListDataChecked(false);
        }
    };

    @Override
    public void clearMultiSelector() {
        multiSelector.clearSelections();
    }

    @Override
    public void initHandlerAndAdapter() {
        setRetainInstance(true);
        page = new PagingHandler();
        adapter = new BaseSellingAdapter<ShippingImpl.Model, ShippingViewHolder>(ShippingImpl.Model.class, getActivity(), R.layout.selling_shipping_list_item, ShippingViewHolder.class) {
            @Override
            protected void populateViewHolder(final ShippingViewHolder viewHolder, final ShippingImpl.Model model, int position) {
                viewHolder.bindDataModel(getActivity(), model);
                viewHolder.vRefNumber.setOnClickListener(onGetRefNumDialog(position));
                viewHolder.CameraBut.setOnClickListener(onGetBarcodeListener(position));
                viewHolder.BtnOverflow.setOnClickListener(onOverflowClicked(position));
                viewHolder.setOnItemClickListener(new BaseSellingViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClicked(int position) {
                        if (!multiSelector.tapSelection(viewHolder)) {
                            if (adapter.isLoading()) {
                                getPaging().setPage(getPaging().getPage() - 1);
                                presenter.onFinishConnection();
                            }
                            moveToDetail(position);
                        } else {
                            presenter.updateListDataChecked(position, multiSelector.isSelected(position, viewHolder.getItemId()));
                            if (multiSelector.getSelectedPositions().size() == 0) {
                                actionMode.finish();
                                multiSelector.refreshAllHolders();
                            } else {
                                actionMode.setTitle(multiSelector.getSelectedPositions().size() + "");
                            }
                        }
                    }

                    @Override
                    public void onLongClicked(int position) {
                        if (multiSelector.getSelectedPositions().size() == 0) {
                            ((AppCompatActivity) getActivity()).startSupportActionMode(selectionMode);
                            multiSelector.setSelected(viewHolder, true);
                        } else {
                            multiSelector.tapSelection(viewHolder.getAdapterPosition(), viewHolder.getItemId());
                            actionMode.setTitle(multiSelector.getSelectedPositions().size() + "");
                            if (multiSelector.getSelectedPositions().size() == 0) {
                                actionMode.finish();
                                multiSelector.refreshAllHolders();
                            }
                        }
                        presenter.updateListDataChecked(position, multiSelector.isSelected(position, viewHolder.getItemId()));
                    }

                });
            }

            @Override
            protected ShippingViewHolder getViewHolder(int mModelLayout, ViewGroup parent) {
                ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);
                return new ShippingViewHolder(view, multiSelector);
            }
        };
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    private void moveToDetail(int position) {
        presenter.moveToDetail(position);
    }

    @Override
    public void moveToDetailResult(Intent intent, int i) {
        startActivityForResult(intent, i);
    }

    private View.OnClickListener onGetRefNumDialog(final int pos) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRefNumDialog(pos);
            }
        };
    }

    private View.OnClickListener onGetBarcodeListener(final int pos) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestBarcodeScanner(pos);
            }
        };
    }

    private View.OnClickListener onOverflowClicked(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopUpMenu(v, position);
            }
        };
    }

    private void createPopUpMenu(View v, final int position) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.shipping_confirmation_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(onMenuItem(position));
        popup.show();
    }

    private PopupMenu.OnMenuItemClickListener onMenuItem(final int position) {
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = (item.getItemId());
                if (i == R.id.action_confirm) {
                    onConfirm(position);
                    return true;
                } else if (i == R.id.action_cancel) {
                    onCancel(position);
                    return true;
                } else if (i == R.id.action_detail_ship) {
                    onOpenDetail(position);
                    return true;
                } else {
                    return false;
                }
            }
        };
    }


    public void onOpenDetail(int pos) {
        presenter.onOpenDetail(pos, getActivity());
    }

    public void onConfirm(int pos) {
        moveToDetail(pos);
    }

    public void onCancel(int pos) {
        createCancelDialog(pos);
    }

    @Override
    public boolean getUserVisible() {
        return getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(actionMode != null && !isVisibleToUser){
            actionMode.finish();
        }
        initPresenter();
        presenter.getShippingList(isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        ScreenTracking.screen(AppScreen.SCREEN_TX_SHOP_CONFIRM_SHIPPING);
    }

    @Override
    public void setRefreshPullEnabled(boolean b) {
        refresh.setPullEnabled(b);
    }

    @Override
    public void disableFilter() {
//        search.clearFocus();
//        search.setEnabled(false);
//        search.setInputType(InputType.TYPE_NULL);
        shippingService.setEnabled(false);
        dueDate.setEnabled(false);
    }

    @Override
    public PagingHandler getPaging() {
        return page;
    }

    @Override
    public String getSearchInvoice() {
        return search.getQuery().toString();
    }

    @Override
    public int getDeadline() {
        return dueDate.getSelectedItemPosition();
    }

    @Override
    public String getSelectedShipping() {
        return shippingService.getSelectedItem().toString();
    }

    @Override
    public void removeLoading() {
        adapter.setIsLoading(false);
    }

    @Override
    public void enableFilter() {
        search.setEnabled(true);
        search.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
        shippingService.setEnabled(true);
        dueDate.setEnabled(true);
    }

    @Override
    public void setRefreshing(boolean b) {
        refresh.setRefreshing(b);
    }

    @Override
    public void removeRetry() {
        adapter.setIsRetry(false);
    }

    @Override
    public void finishRefresh() {
        refresh.finishRefresh();
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
    public void notifyDataSetChanged(List<ShippingImpl.Model> modelList) {
        adapter.clearData();
        adapter.setListModel(modelList);
    }

    private void createCancelDialog(int pos) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cancel_order);
        final EditText Remark = (EditText) dialog.findViewById(R.id.remark);
        TextView confirmButton = (TextView) dialog.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(onCancelListener(dialog, pos, Remark));
        dialog.show();
    }

    private View.OnClickListener onCancelListener(final Dialog dialog, final int pos, final EditText remark) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.cancelShipping(remark, pos, dialog, getActivity());
            }
        };
    }

    @Override
    public void showProgressDialog() {
        progressDialog.showDialog();
    }

    @Override
    public void hideProfressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public boolean isRefreshing() {
        return refresh.isRefreshing();
    }

    @Override
    public void addLoadingFooter() {
        adapter.setIsLoading(true);
    }

    @Override
    public View getRootView() {
        return getView();
    }

    @Override
    public void addRetry() {
        adapter.setIsRetry(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initRefreshView();
        initView();
        super.onViewCreated(view, savedInstanceState);
    }

    public void initView() {
        filterView = getActivity().getLayoutInflater().inflate(R.layout.filter_layout_selling_shipping, null);
        search = (SearchView) filterView.findViewById(R.id.search);
        int searchPlateId = search.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = search.findViewById(searchPlateId);
        searchPlate.setBackgroundColor(Color.TRANSPARENT);
        dueDate = (Spinner) filterView.findViewById(R.id.due_date);
        shippingService = (Spinner) filterView.findViewById(R.id.shipping);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(filterView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    public void setAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                presenter.onScrollView(((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition() == recyclerView.getLayoutManager().getItemCount() - 1);
            }
        });
    }

    @Override
    public void setListener() {
        adapter.setOnRetryListener(new BaseSellingAdapter.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                presenter.getShippingList(getUserVisibleHint());
            }
        });
        dueDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //to avoid called itemselected when oncreate
                if (inhibitSpinnerDuedate) {
                    inhibitSpinnerDuedate = false;
                } else {
                    presenter.doRefresh();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        shippingService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //to avoid called itemselected when oncreate
                if (inhibitSpinnerShipping) {
                    inhibitSpinnerShipping = false;
                } else {
                    presenter.doRefresh();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        search.setOnQueryTextListener(onSearchListener());
    }

    public void initRefreshView() {
        refresh = new RefreshHandler(getActivity(), rootView, onRefreshListener());
    }

    private SearchView.OnQueryTextListener onSearchListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.onQueryTextSubmit(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.onQueryTextChange(newText);
                return false;
            }
        };
    }

    @Override
    public void hideFilter() {
        bottomSheetDialog.hide();
    }

    private RefreshHandler.OnRefreshHandlerListener onRefreshListener() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                presenter.onRefreshHandler();
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        if (shouldRefreshList) {
            shouldRefreshList = false;
            refresh.setRefreshing(true);
            refresh.setIsRefreshing(true);
            presenter.onRefreshHandler();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.updateRefNumBarcode(getBarcodePosition,
                CommonUtils.getBarcode(requestCode, resultCode, data));
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PROCESS_RESULT:
                    shouldRefreshList = true;
                    break;
            }
        }
    }

    @Override
    public void onPause() {
        presenter.onFinishConnection();
        refresh.setRefreshing(false);
        super.onPause();
    }

    @Override
    public void showProgress() {
        if (!progressDialog.isProgress()) {
            progressDialog.showDialog();
        }
    }

    @Override
    public void ariseRetry(int type, Object... data) {
    }

    @Override
    public void setData(int type, Bundle data) {
        switch (type) {
            case SellingService.CONFIRM_MULTI_SHIPPING:
                progressDialog.dismiss();
                presenter.doRefresh();
                break;
        }
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        switch (type) {
            case SellingService.CONFIRM_MULTI_SHIPPING:
                NetworkErrorHelper.showSnackbar(getActivity());
                progressDialog.dismiss();
                presenter.doRefresh();
                break;
        }
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String error = (String) data[0];
        switch (type) {
            case SellingService.CONFIRM_MULTI_SHIPPING:
                showSnackBarError(error);
                progressDialog.dismiss();
                presenter.doRefresh();
                break;
        }
    }

    public void showSnackBarError(String error) {
        final Snackbar snackbar = SnackbarManager.make(getActivity(),
                error,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FragmentSellingShippingPermissionsDispatcher.onRequestPermissionsResult(
                FragmentSellingShipping.this, requestCode, grantResults);
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
