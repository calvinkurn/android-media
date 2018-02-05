package com.tokopedia.tokocash.activation.data;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/1/18.
 */

public interface ActivateTokoCashDataStore {

    Observable<ActivateTokoCashEntity> requestOTPWallet();

    Observable<ActivateTokoCashEntity> linkedWalletToTokoCash(HashMap<String, String> mapParam);
}
