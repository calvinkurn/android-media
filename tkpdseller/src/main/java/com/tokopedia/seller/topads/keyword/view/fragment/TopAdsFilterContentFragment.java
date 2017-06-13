package com.tokopedia.seller.topads.keyword.view.fragment;

import com.tokopedia.seller.topads.view.fragment.BasePresenterFragment;
import com.tokopedia.seller.topads.view.listener.TopAdsFilterContentFragmentListener;

/**
 * @author normansyahputa on 5/26/17.
 *         <p>
 *         {@link com.tokopedia.seller.topads.view.fragment.TopAdsFilterContentFragment}
 */

public abstract class TopAdsFilterContentFragment<P> extends BasePresenterFragment<P> implements TopAdsFilterContentFragmentListener {
    protected TopAdsFilterContentFragment.Callback callback;
    /**
     * Sign for title filter list
     */
    private boolean active;

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void onStatusChanged(boolean active);

    }
}
