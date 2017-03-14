package com.tokopedia.ride.bookingride;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.ride.R;
import com.tokopedia.ride.base.presentation.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class UberProductFragment extends BaseFragment {

    public static UberProductFragment newInstance(){
        return new UberProductFragment();
    }

    public UberProductFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_uber_product;
    }

}
