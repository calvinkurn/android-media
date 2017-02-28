package com.tokopedia.seller.opportunity.presenter;

import com.tokopedia.seller.opportunity.listener.OppurtunityView;

/**
 * Created by hangnadi on 2/27/17.
 */
public class OppurtunityImpl implements OppurtunityPresenter {
    private final OppurtunityView view;

    public OppurtunityImpl(OppurtunityView view) {
        this.view = view;
    }
}
