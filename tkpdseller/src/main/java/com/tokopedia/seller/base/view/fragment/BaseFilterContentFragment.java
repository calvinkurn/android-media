package com.tokopedia.seller.base.view.fragment;

import com.tokopedia.base.list.seller.view.fragment.BasePresenterFragment;
import com.tokopedia.seller.base.view.listener.BaseFilterContentViewListener;

/**
 * @author normansyahputa on 5/26/17.
 *         <p>
 *         {@link com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsFilterContentFragment}
 */
@Deprecated
public abstract class BaseFilterContentFragment<P> extends BasePresenterFragment<P> implements BaseFilterContentViewListener {
    protected BaseFilterContentFragment.Callback callback;
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
