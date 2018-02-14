package com.tokopedia.tkpdpdp;

import rx.Observable;

/**
 * @author anggaprasetiyo on 13/02/18.
 */

public interface PdpGantengRouter {
    Observable<String> getAtcObsr();
}
