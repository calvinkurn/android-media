package com.tokopedia.tokocash.activation.data;

import com.tokopedia.tokocash.activation.presentation.model.ActivateTokoCashData;
import com.tokopedia.tokocash.network.exception.WalletException;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 2/1/18.
 */

public class ActivateTokoCashMapper implements Func1<ActivateTokoCashEntity, ActivateTokoCashData> {

    @Override
    public ActivateTokoCashData call(ActivateTokoCashEntity activateTokoCashEntity) {
        if (activateTokoCashEntity != null) {
            ActivateTokoCashData activateTokoCashData = new ActivateTokoCashData();
            activateTokoCashData.setSuccess(activateTokoCashEntity.getSuccess() == 1);
            return activateTokoCashData;
        }
        throw new WalletException("empty");
    }
}