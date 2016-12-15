package com.tokopedia.inbox.rescenter.shipping.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.shipping.interactor.NetworkParam;
import com.tokopedia.inbox.rescenter.shipping.model.InputShippingParamsModel;
import com.tokopedia.inbox.rescenter.shipping.model.ResCenterKurir;
import com.tokopedia.inbox.rescenter.shipping.interactor.RetrofitInteractor;
import com.tokopedia.inbox.rescenter.shipping.interactor.RetrofitInteractorImpl;
import com.tokopedia.inbox.rescenter.shipping.view.InputShippingFragmentView;

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
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        viewListener.setParamsModel((InputShippingParamsModel) savedState.getParcelable(EXTRA_PARAM_MODEL));
    }

    @Override
    public void onFirstTimeLaunched() {
        try {
            String json = cacheManager.getValueString(viewListener.getParamsModel().getResolutionID());
            if (json != null) {
                renderInputShippingForm(convertCacheToModel(json));
                viewListener.showLoading(false);
                viewListener.showMainPage(true);
            } else {
                requestShippingList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            requestShippingList();
        }
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
                        viewListener.showLoading(true);
                        viewListener.showMainPage(false);
                    }

                    @Override
                    public void onSuccess(ResCenterKurir kurirList) {
                        storeCacheKurirList(kurirList);
                        renderInputShippingForm(kurirList);
                        viewListener.showLoading(false);
                        viewListener.showMainPage(true);
                    }

                    @Override
                    public void onTimeOut(NetworkErrorHelper.RetryClickedListener listener) {
                        viewListener.showTimeOutMessage(listener);
                        viewListener.showLoading(false);
                        viewListener.showMainPage(false);
                    }

                    @Override
                    public void onError(String message) {
                        Log.d(TAG, message);
                        viewListener.showErrorMessage(message);
                        viewListener.showLoading(false);
                        viewListener.showMainPage(false);
                    }
                });
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

}
