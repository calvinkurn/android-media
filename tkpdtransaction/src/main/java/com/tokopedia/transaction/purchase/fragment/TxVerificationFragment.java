package com.tokopedia.transaction.purchase.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customadapter.LazyListView;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
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

import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_ROTATE;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;

/**
 * @author Angga.Prasetiyo on 24/05/2016.
 */
public class TxVerificationFragment extends BasePresenterFragment<TxVerificationPresenter>
        implements TxVerViewListener, AdapterView.OnItemClickListener,
        RefreshHandler.OnRefreshHandlerListener, LazyListView.LazyLoadListener,
        TxVerAdapter.ActionListener, TxListUIReceiver.ActionListener {
    public static final int REQUEST_VERIFICATION_DETAIL = 2;
    public static final int REQUEST_CODE_TRANSFER_IMAGE = 111;
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

        ImagePickerBuilder builder = new ImagePickerBuilder(getString(R.string.choose_image),
                new int[]{TYPE_GALLERY, TYPE_CAMERA}, GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
                new ImagePickerEditorBuilder(
                        new int[]{ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE},
                        false,
                        null)
                ,null);
        Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
        startActivityForResult(intent, REQUEST_CODE_TRANSFER_IMAGE);
    }

    @Override
    public void actionCancelTransaction(TxVerData data) {
        presenter.processCancelTransaction(getActivity(), data);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_TRANSFER_IMAGE:
                String imagePath = null;

                if (data != null){
                    ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
                    if (imageUrlOrPathList!= null && imageUrlOrPathList.size() > 0) {
                        imagePath = imageUrlOrPathList.get(0);
                    }
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
        if(presenter != null && getActivity() != null) {
            presenter.confirmCancelTransaction(getActivity(), paymentId);
        }
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
}
