package com.tokopedia.inbox.rescenter.shipping.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.database.model.AttachmentResCenterDB;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.shipping.interactor.NetworkParam;
import com.tokopedia.inbox.rescenter.shipping.interactor.RetrofitInteractor;
import com.tokopedia.inbox.rescenter.shipping.interactor.RetrofitInteractorImpl;
import com.tokopedia.inbox.rescenter.shipping.model.InputShippingParamsGetModel;
import com.tokopedia.inbox.rescenter.shipping.model.InputShippingParamsPostModel;
import com.tokopedia.inbox.rescenter.shipping.model.ResCenterKurir;
import com.tokopedia.inbox.rescenter.shipping.view.InputShippingFragmentView;

import static com.tokopedia.inbox.rescenter.shipping.fragment.InputShippingFragment.EXTRA_PARAM_ATTACHMENT;
import static com.tokopedia.inbox.rescenter.shipping.fragment.InputShippingFragment.EXTRA_PARAM_MODEL;

/**
 * Created by hangnadi on 12/13/16.
 */
public class InputShippingFragmentImpl implements InputShippingFragmentPresenter {

    private static final String TAG = InputShippingFragmentPresenter.class.getSimpleName();
    private static final int REQUEST_CODE_SCAN_BARCODE = 19;

    private final GlobalCacheManager cacheManager;
    private final RetrofitInteractor retrofit;
    private final InputShippingFragmentView viewListener;

    public InputShippingFragmentImpl(InputShippingFragmentView viewListener) {
        this.viewListener = viewListener;
        this.cacheManager = new GlobalCacheManager();
        this.retrofit = new RetrofitInteractorImpl();
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putParcelable(EXTRA_PARAM_MODEL, viewListener.getParamsModel());
        state.putParcelableArrayList(EXTRA_PARAM_ATTACHMENT, viewListener.getAttachmentData());
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        viewListener.setParamsModel((InputShippingParamsGetModel) savedState.getParcelable(EXTRA_PARAM_MODEL));
        viewListener.setAttachmentData(savedState.<AttachmentResCenterDB>getParcelableArrayList(EXTRA_PARAM_ATTACHMENT));
    }

    @Override
    public void onFirstTimeLaunched() {
        try {
            String json = cacheManager.getValueString(viewListener.getParamsModel().getResolutionID());
            if (json != null) {
                ResCenterKurir shippingModel = convertCacheToModel(json);
                renderInputShippingForm(shippingModel);
                renderPreviousShipping(shippingModel);
                showLoading(false);
                showMainPage(true);
            } else {
                requestShippingList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            requestShippingList();
        }
    }

    private void showMainPage(boolean isVisible) {
        viewListener.getMainView().setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void showLoading(boolean isVisible) {
        viewListener.getLoadingView().setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private ResCenterKurir convertCacheToModel(String json) {
        return CacheUtil.convertStringToModel(json, new TypeToken<ResCenterKurir>() {}.getType());
    }

    private void requestShippingList() {
        retrofit.getShippingList(viewListener.getActivity(),
                generateGetShippingListParams(),
                new RetrofitInteractor.GetKurirListener() {

                    @Override
                    public void onStart() {
                        showLoading(true);
                        showMainPage(false);
                    }

                    @Override
                    public void onSuccess(ResCenterKurir kurirList) {
                        storeCacheKurirList(kurirList);
                        renderInputShippingForm(kurirList);
                        renderPreviousShipping(kurirList);
                        showLoading(false);
                        showMainPage(true);
                    }

                    @Override
                    public void onTimeOut(NetworkErrorHelper.RetryClickedListener listener) {
                        viewListener.showTimeOutMessage(listener);
                        showLoading(false);
                        showMainPage(false);
                    }

                    @Override
                    public void onError(String message) {
                        Log.d(TAG, message);
                        viewListener.showErrorMessage(message);
                        showLoading(false);
                        showMainPage(false);
                    }
                });
    }

    private void renderPreviousShipping(ResCenterKurir shippingModel) {
        if (isInstanceForEdit()) {
            viewListener.getShippingRefNum().setText(viewListener.getParamsModel().getShippingRefNum());

            for (ResCenterKurir.Kurir kurir : shippingModel.getList()) {
                if (kurir.getShipmentId().equals(viewListener.getParamsModel().getShippingID())) {
                    viewListener.getShippingSpinner().setSelection(shippingModel.getList().indexOf(kurir) + 1);
                }
            }
        }
    }

    private TKPDMapParam generateGetShippingListParams() {
        return AuthUtil.generateParamsNetwork(viewListener.getActivity(), NetworkParam.getShippingListParams());
    }

    private void renderInputShippingForm(ResCenterKurir shippingListModel) {
        viewListener.renderSpinner(shippingListModel.getList());
    }

    private void storeCacheKurirList(ResCenterKurir kurirList) {
        cacheManager.setKey(viewListener.getParamsModel().getResolutionID());
        cacheManager.setValue(
                CacheUtil.convertModelToString(kurirList, new TypeToken<ResCenterKurir>() {}.getType())
        );
        cacheManager.setCacheDuration(1800000); // expired in 30minutes
        cacheManager.store();
    }

    @Override
    public void onScanBarcodeClick() {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        viewListener.startActivityForResult(intent, REQUEST_CODE_SCAN_BARCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN_BARCODE:
                    viewListener.renderInputShippingRefNum(data != null ? data.getStringExtra("SCAN_RESULT") : "");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        retrofit.unSubscribe();
    }

    @Override
    public void onConfirrmButtonClick() {
        InputShippingParamsPostModel params = generatePostParams();
        if (isValidToSubmit(params)) {
            doStoreShippingService(params);
        }
    }

    private InputShippingParamsPostModel generatePostParams() {
        return new InputShippingParamsPostModel.Builder()
                .setResolutionID(viewListener.getParamsModel().getResolutionID())
                .setConversationID(viewListener.getParamsModel().getConversationID())
                .setShippingNumber(viewListener.getShippingRefNum().getText().toString())
                .setAttachmentList(viewListener.getAttachmentData())
                .setShippingID(generateShippingID())
                .build();
    }

    private String generateShippingID() {
        try {
            return getSelectedKurir().getShipmentId();
        } catch (Exception e) {
            return "";
        }
    }

    private void doStoreShippingService(InputShippingParamsPostModel params) {
        retrofit.storeShippingService(viewListener.getActivity(),
                params,
                new RetrofitInteractor.PostShippingListener() {

                    @Override
                    public void onStart() {
                        showLoading(true);
                        showMainPage(false);
                    }

                    @Override
                    public void onSuccess() {
                        viewListener.finishAsSuccessResult();
                        showLoading(false);
                        showMainPage(true);
                    }

                    @Override
                    public void onTimeOut() {
                        viewListener.toastTimeOutMessage();
                        showLoading(false);
                        showMainPage(true);
                    }

                    @Override
                    public void onError(String message) {
                        Log.d(TAG, message);
                        viewListener.toastErrorMessage(message);
                        showLoading(false);
                        showMainPage(true);
                    }
                });
    }

    private boolean isValidToSubmit(InputShippingParamsPostModel params) {
        viewListener.getErrorSpinner().setVisibility(View.GONE);
        viewListener.getShippingRefNum().setError(null);

        if (params.getShippingNumber().replaceAll("\\s+","").length() == 0) {
            viewListener.getShippingRefNum().setError(viewListener.getActivity().getString(R.string.error_field_required));
            return false;
        }

        if (params.getShippingNumber().length() < 8 || params.getShippingNumber().length() > 17) {
            viewListener.getShippingRefNum().setError(viewListener.getActivity().getString(R.string.error_receipt_number));
            return false;
        }

        if (params.getShippingID().isEmpty()) {
            viewListener.getErrorSpinner().setVisibility(View.VISIBLE);
            return false;
        }

        if (isInstanceForEdit()
                && isShippingRefNumEditted(params.getShippingNumber())
                && isShippingEditted(params.getShippingID())) {
            viewListener.getShippingRefNum().setError(viewListener.getActivity().getString(R.string.error_update_receipt_number));
            return false;
        }

        return true;
    }

    private boolean isInstanceForEdit() {
        return viewListener.getParamsModel().getConversationID() != null || !viewListener.getParamsModel().getConversationID().isEmpty();
    }

    private boolean isShippingRefNumEditted(String shippingRefNum) {
        return shippingRefNum.equals(viewListener.getParamsModel().getShippingRefNum());
    }

    private boolean isShippingEditted(String shippingID) {
        return !shippingID.equals(viewListener.getParamsModel().getShippingID());
    }

    private ResCenterKurir.Kurir getSelectedKurir() throws Exception {
        return (ResCenterKurir.Kurir) viewListener.getShippingSpinner().getItemAtPosition(viewListener.getShippingSpinner().getSelectedItemPosition() - 1);
    }
}
