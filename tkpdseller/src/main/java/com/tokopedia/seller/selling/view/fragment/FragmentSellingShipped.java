package com.tokopedia.seller.selling.view.fragment;

import android.view.View;
import android.widget.SearchView;

/**
 * @author okasurya on 8/1/18.
 */
public class FragmentSellingShipped extends FragmentSellingTransaction {

    public static final String STATUS_SHIPPED = "2";

    public static FragmentSellingShipped newInstance() {
        return new FragmentSellingShipped();
    }

    @Override
    public String getFilter() {
        return STATUS_SHIPPED;
    }

    @Override
    public void initView() {
        super.initView();
        startDate.setVisibility(View.GONE);
        endDate.setVisibility(View.GONE);
        spinnerFilter.setVisibility(View.GONE);
        searchbtn.setVisibility(View.GONE);
        searchTxt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.onQuerySubmit(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.onQueryChange(newText);
                return false;
            }
        });
    }
}
