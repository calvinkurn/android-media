package com.tokopedia.tokocash.activation.domain;

import com.tokopedia.tokocash.activation.presentation.model.ActivateTokoCashData;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/1/18.
 */

public interface IActivateRepository {

    Observable<ActivateTokoCashData> requestOTPWallet();

    Observable<ActivateTokoCashData> linkedWalletToTokoCash(HashMap<String, String> mapParam);
}
