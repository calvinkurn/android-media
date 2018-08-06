package com.tokopedia.seller.selling.view.fragment;

/**
 * @author okasurya on 8/1/18.
 */
public class FragmentSellingDelivered extends FragmentSellingShipped {

    public static final String STATUS_DELIVERED = "7";

    public static FragmentSellingDelivered newInstance() {
        return new FragmentSellingDelivered();
    }

    @Override
    public String getFilter() {
        return STATUS_DELIVERED;
    }
}
