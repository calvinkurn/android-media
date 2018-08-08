package com.tokopedia.tkpdpdp.listener;

import android.support.v4.app.Fragment;

import com.tokopedia.core.product.listener.ViewListener;

/**
 * @author Angga.Prasetiyo on 09/11/2015.
 */
public interface ProductInfoView extends ViewListener {

    void inflateFragment(Fragment fragment, String tag);

}
