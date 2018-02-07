package com.tokopedia.transaction.purchase.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customadapter.LazyListView;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.purchase.activity.ConfirmPaymentActivity;
import com.tokopedia.transaction.purchase.activity.TxVerDetailActivity;
import com.tokopedia.transaction.purchase.adapter.TxVerAdapter;
import com.tokopedia.transaction.purchase.dialog.CancelTransactionDialog;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractor;
import com.tokopedia.transaction.purchase.listener.TxVerViewListener;
import com.tokopedia.transaction.purchase.model.response.txverification.TxVerData;
import com.tokopedia.transaction.purchase.presenter.TxVerificationPresenter;
import com.tokopedia.transaction.purchase.presenter.TxVerificationPresenterImpl;
import com.tokopedia.transaction.purchase.receiver.TxListUIReceiver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author Angga.Prasetiyo on 24/05/2016.
 */
@RuntimePermissions
public class TxVerificationFragment extends BasePresenterFragment<TxVerificationPresenter>
        implements TxVerViewListener, AdapterView.OnItemClickListener,
        RefreshHandler.OnRefreshHandlerListener, LazyListView.LazyLoadListener,
        TxVerAdapter.ActionListener, TxListUIReceiver.ActionListener {
    public static final int REQUEST_VERIFICATION_DETAIL = 2;
    private static final String TAG = TxVerificationFragment.class.getSimpleName();

    @BindView(R2.id.order_list)
    LazyListView lvTXVerification;

    private View loadMoreView;
    private TkpdProgressDialog progressDialog;
    private TxVerAdapter txVerAdapter;

    private PagingHandler mPaging = new PagingHandler();
    private RefreshHandler refreshHandler;
    private boolean isLoadMoreTerminated = true;
    private boolean isLoading = false;
    private ImageUploadHandler imageUploadHandler;
    private TxVerData txVerDataToUpload;
    private TxListUIReceiver txUIReceiver;

    public static TxVerificationFragment createInstance() {
        return new TxVerificationFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new TxVerificationPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_transaction_verification_list_tx_module;
    }

    @SuppressLint("InflateParams")
    @Override
    protected void initView(View view) {
        loadMoreView = LayoutInflater.from(getActivity()).inflate(R.layout.footer_list_view, null);
        progressDialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setViewListener() {
        lvTXVerification.setOnItemClickListener(this);
        refreshHandler = new RefreshHandler(getActivity(), getView(), this);
        refreshHandler.setPullEnabled(true);
        lvTXVerification.setAdapter(txVerAdapter);
        lvTXVerification.setOnLazyLoadListener(this);
        lvTXVerification.setOnItemClickListener(this);
    }

    @Override
    protected void initialVar() {
        txVerAdapter = new TxVerAdapter(getActivity(), this);
        txUIReceiver = new TxListUIReceiver(this);
        IntentFilter intentFilter = new IntentFilter(TxListUIReceiver.FILTER_ACTION);
        getActivity().registerReceiver(txUIReceiver, intentFilter);

    }

    @Override
    protected void setActionVar() {
        initialData();
    }

    private void initialData() {
        if (getUserVisibleHint() && !isLoading && getActivity() != null
                && (txVerAdapter == null || txVerAdapter.getCount() == 0)) {
            refreshHandler.startRefresh();
        }
    }

    @Override
    public void renderDataList(List<TxVerData> txVerDataList, boolean hasNext, int typeRequest) {
        NetworkErrorHelper.removeEmptyState(getView());
        if (refreshHandler.isRefreshing()) {
            refreshHandler.finishRefresh();
            refreshHandler.setPullEnabled(true);
        }
        if (typeRequest == TxOrderNetInteractor.TypeRequest.PULL_REFRESH) {
            mPaging.resetPage();
            txVerAdapter.clear();
            txVerAdapter.notifyDataSetChanged();
        }
        if (hasNext) mPaging.nextPage();
        mPaging.setHasNext(hasNext);
        txVerAdapter.addAll(txVerDataList);
        txVerAdapter.notifyDataSetChanged();
        isLoading = false;
        isLoadMoreTerminated = false;
        refreshHandler.setPullEnabled(true);
        lvTXVerification.removeFooterView(loadMoreView);
    }

    @Override
    public void showFailedLoadMoreData(String message) {
        isLoading = false;
        lvTXVerification.removeFooterView(loadMoreView);
        isLoadMoreTerminated = true;
        if (getView() != null) NetworkErrorHelper.createSnackbarWithAction(getActivity(), message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getData(TxOrderNetInteractor.TypeRequest.LOAD_MORE);

                    }
                }).showRetrySnackbar();
    }

    @Override
    public void showFailedPullRefresh(String message) {
        isLoading = false;
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
        if (getView() != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void showFailedResetData(String message) {
        isLoading = false;
        lvTXVerification.removeFooterView(loadMoreView);
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
        if (getView() != null)
            NetworkErrorHelper.showEmptyState(getActivity(),
                    getView(),
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            refreshHandler.startRefresh();
                        }
                    });
    }

    @Override
    public void showCancelTransactionDialog(String message, String paymentId) {
        CancelTransactionDialog dialog = CancelTransactionDialog.showCancelTransactionDialog(
                message, paymentId
        );
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
    }

    @Override
    public void showNoConnectionLoadMoreData(String message) {
        isLoading = false;
        lvTXVerification.removeFooterView(loadMoreView);
        isLoadMoreTerminated = true;
        if (getView() != null) NetworkErrorHelper.createSnackbarWithAction(getActivity(), message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getData(TxOrderNetInteractor.TypeRequest.LOAD_MORE);

                    }
                }).showRetrySnackbar();
    }

    @Override
    public void showNoConnectionPullRefresh(String message) {
        isLoading = false;
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
        if (getView() != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void showNoConnectionResetData(String message) {
        isLoading = false;
        lvTXVerification.removeFooterView(loadMoreView);
        refreshHandler.finishRefresh();
        refreshHandler.setPullEnabled(true);
        if (getView() != null)
            NetworkErrorHelper.showEmptyState(getActivity(),
                    getView(),
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            refreshHandler.startRefresh();
                        }
                    });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && !isLoading && getActivity() != null
                && (txVerAdapter == null || txVerAdapter.getCount() == 0)) {
            if (getView() != null) NetworkErrorHelper.hideEmptyState(getView());
            refreshHandler.startRefresh();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void showEmptyData(int typeRequest) {
        isLoading = false;
        lvTXVerification.removeFooterView(loadMoreView);
        if (refreshHandler.isRefreshing()) refreshHandler.finishRefresh();
        switch (typeRequest) {
            case TxOrderNetInteractor.TypeRequest.INITIAL:
                lvTXVerification.addNoResult();
                break;
        }
    }

    @Override
    public void showProcessGetData(int typeRequest) {
        lvTXVerification.removeNoResult();
        switch (typeRequest) {
            case TxOrderNetInteractor.TypeRequest.INITIAL:
                if (!refreshHandler.isRefreshing()) {
                    refreshHandler.setRefreshing(true);
                    refreshHandler.setPullEnabled(false);
                }
                break;
            case TxOrderNetInteractor.TypeRequest.LOAD_MORE:
                lvTXVerification.addFooterView(loadMoreView);
                break;
            case TxOrderNetInteractor.TypeRequest.PULL_REFRESH:
                if (!refreshHandler.isRefreshing()) {
                    refreshHandler.setRefreshing(true);
                    refreshHandler.setPullEnabled(false);
                }
                break;
        }
    }

    @Override
    public void showSnackbarWithMessage(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void resetData() {
        txVerAdapter.clear();
        mPaging.resetPage();
        txVerAdapter.notifyDataSetChanged();
        refreshHandler.startRefresh();
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showProgressLoading() {
        progressDialog.setCancelable(false);
        progressDialog.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showToastMessage(String message) {
        View view = getView();
        if (view != null) Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        else CommonUtils.UniversalToast(getActivity(), message);
    }

    @Override
    public void showDialog(Dialog dialog) {
        if (!dialog.isShowing()) dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        if (dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return getString(resId);
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    ) {
        return null;
    }

    @Override
    public void closeView() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.processToTxVerificationDetail(getActivity(), txVerAdapter.getItem(position));
    }

    @Override
    public void onRefresh(View view) {
        if (!isLoading) {
            mPaging.resetPage();
            getData(txVerAdapter.getCount() == 0 ? TxOrderNetInteractor.TypeRequest.INITIAL
                    : TxOrderNetInteractor.TypeRequest.PULL_REFRESH);
        }
    }

    @Override
    public void onLazyLoad(View view) {
        if (mPaging.CheckNextPage() && !isLoading && !isLoadMoreTerminated) {
            getData(TxOrderNetInteractor.TypeRequest.LOAD_MORE);
        }
    }

    private void getData(int typeRequest) {
        if (getView() != null) NetworkErrorHelper.hideEmptyState(getView());
        presenter.getPaymentVerification(getActivity(), mPaging.getPage(), typeRequest);
    }

    @Override
    public void actionEditPayment(TxVerData data) {
        presenter.processEditPayment(getActivity(), data);
    }

    @Override
    public void actionUploadProof(TxVerData data) {
        this.txVerDataToUpload = data;
        imageUploadHandler = ImageUploadHandler.createInstance(this);
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setMessage(getString(R.string.dialog_upload_option));
        myAlertDialog.setPositiveButton(getString(R.string.title_gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TxVerificationFragmentPermissionsDispatcher.onActionImagePickerWithCheck(
                        TxVerificationFragment.this
                );

            }
        });
        myAlertDialog.setNegativeButton(getString(R.string.title_camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TxVerificationFragmentPermissionsDispatcher.onActionCameraWithCheck(
                        TxVerificationFragment.this
                );

            }
        });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void actionCancelTransaction(TxVerData data) {
        presenter.processCancelTransaction(getActivity(), data);
    }

    @SuppressLint("InlinedApi")
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onActionCamera() {
        imageUploadHandler.actionCamera();
    }

    @SuppressLint("InlinedApi")
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void onActionImagePicker() {
        imageUploadHandler.actionImagePicker();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ImageUploadHandler.REQUEST_CODE:
                String imagePath = null;
                if (data != null && data.getStringExtra(GalleryBrowser.IMAGE_URL) != null) {
                    imagePath = data.getExtras().getString(GalleryBrowser.IMAGE_URL, null);
                } else if (imageUploadHandler != null
                        && imageUploadHandler.getCameraFileloc() != null) {
                    imagePath = imageUploadHandler.getCameraFileloc();
                }
                presenter.uploadProofImageWSV4(getActivity(), imagePath, txVerDataToUpload);
                break;
            case TxVerDetailActivity.REQUEST_EDIT_PAYMENT:
                if (resultCode == ConfirmPaymentActivity.RESULT_FORM_FAILED
                        && data.hasExtra(ConfirmPaymentActivity.EXTRA_MESSAGE_ERROR_GET_FORM)) {
                    NetworkErrorHelper.showSnackbar(
                            getActivity(), data.getStringExtra(
                                    ConfirmPaymentActivity.EXTRA_MESSAGE_ERROR_GET_FORM
                            )
                    );
                }
                break;
            case REQUEST_VERIFICATION_DETAIL:
                if (resultCode == TxVerDetailActivity.RESULT_INVOICE_FAILED
                        && data.hasExtra(TxVerDetailActivity.EXTRA_MESSAGE_ERROR_GET_INVOICE)) {
                    NetworkErrorHelper.showSnackbar(
                            getActivity(), data.getStringExtra(
                                    TxVerDetailActivity.EXTRA_MESSAGE_ERROR_GET_INVOICE
                            )
                    );
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void confirmCancelPayment(String paymentId) {
        presenter.confirmCancelTransaction(getActivity(), paymentId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
        getActivity().unregisterReceiver(txUIReceiver);
    }

    @Override
    public void forceRefreshListData() {
        resetData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TxVerificationFragmentPermissionsDispatcher.onRequestPermissionsResult(
                TxVerificationFragment.this, requestCode, grantResults
        );

    }

    @SuppressLint("InlinedApi")
    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        RequestPermissionUtil.onShowRationale(getActivity(), request, listPermission);
    }

    @SuppressLint("InlinedApi")
    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(
                getActivity(), request, Manifest.permission.READ_EXTERNAL_STORAGE
        );
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.CAMERA);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.CAMERA);
    }

    @SuppressLint("InlinedApi")
    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(
                getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
        );
    }

    @SuppressLint("InlinedApi")
    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(
                getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
        );
    }

    @SuppressLint("InlinedApi")
    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        RequestPermissionUtil.onPermissionDenied(getActivity(), listPermission);
    }

    @SuppressLint("InlinedApi")
    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        RequestPermissionUtil.onNeverAskAgain(getActivity(), listPermission);
    }
}
