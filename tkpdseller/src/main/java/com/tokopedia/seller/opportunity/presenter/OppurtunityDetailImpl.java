package com.tokopedia.seller.opportunity.presenter;

import com.tokopedia.seller.opportunity.listener.OppurtunityDetailView;

/**
 * Created by hangnadi on 2/27/17.
 */
public class OppurtunityDetailImpl implements OppurtunityDetailPresenter {

    private final OppurtunityDetailView viewListener;

    public OppurtunityDetailImpl(OppurtunityDetailView viewListener) {
        this.viewListener = viewListener;
    }
}
