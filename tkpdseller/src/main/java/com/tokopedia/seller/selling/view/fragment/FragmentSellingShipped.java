package com.tokopedia.seller.selling.view.fragment;

/**
 * @author okasurya on 8/1/18.
 */
public class FragmentSellingShipped extends FragmentSellingTransaction {
    public static FragmentSellingShipped newInstance() {
        return new FragmentSellingShipped();
    }

    @Override
    public String getFilter() {
        return "2";
    }
}
