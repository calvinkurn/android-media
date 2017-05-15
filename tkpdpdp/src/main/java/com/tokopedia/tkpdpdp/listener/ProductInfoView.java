package com.tokopedia.tkpdpdp.listener;

import android.app.Fragment;

import com.tokopedia.core.product.listener.ViewListener;

/**
 * Created by Angga.Prasetiyo on 09/11/2015.
 */
public interface ProductInfoView extends ViewListener {

    void inflateFragment(Fragment fragment, String tag);

}
