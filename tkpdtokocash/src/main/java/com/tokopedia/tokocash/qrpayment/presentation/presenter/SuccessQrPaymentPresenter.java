package com.tokopedia.tokocash.qrpayment.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.qrpayment.presentation.contract.SuccessQrPaymentContract;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 1/22/18.
 */

public class SuccessQrPaymentPresenter extends BaseDaggerPresenter<SuccessQrPaymentContract.View>
        implements SuccessQrPaymentContract.Presenter {

    private CacheManager globalCacheManager;

    @Inject
    public SuccessQrPaymentPresenter(CacheManager globalCacheManager) {
        this.globalCacheManager = globalCacheManager;
    }

    @Override
    public void deleteCacheTokoCashBalance() {
        globalCacheManager.delete(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE);
    }
}
