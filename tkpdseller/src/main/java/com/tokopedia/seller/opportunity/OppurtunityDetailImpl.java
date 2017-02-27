package com.tokopedia.seller.opportunity;

/**
 * Created by hangnadi on 2/27/17.
 */
public class OppurtunityDetailImpl implements OppurtunityDetailPresenter {
    private final OppurtunityDetailView viewListener;

    public OppurtunityDetailImpl(OppurtunityDetailView viewListener) {
        this.viewListener = viewListener;
    }
}
