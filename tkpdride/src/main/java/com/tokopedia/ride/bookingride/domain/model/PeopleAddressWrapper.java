package com.tokopedia.ride.bookingride.domain.model;

import java.util.List;

/**
 * Created by alvarisi on 4/6/17.
 */

public class PeopleAddressWrapper {
    private PeopleAddressPaging paging;
    private List<PeopleAddress> addresses;

    public PeopleAddressPaging getPaging() {
        return paging;
    }

    public void setPaging(PeopleAddressPaging paging) {
        this.paging = paging;
    }

    public List<PeopleAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<PeopleAddress> addresses) {
        this.addresses = addresses;
    }
}
