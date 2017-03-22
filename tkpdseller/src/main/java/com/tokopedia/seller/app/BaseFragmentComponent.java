package com.tokopedia.seller.app;

/**
 * Created by sebastianuskh on 3/21/17.
 */

public interface BaseFragmentComponent<F extends BaseDiFragment> {
    void inject(F fragment);
}
