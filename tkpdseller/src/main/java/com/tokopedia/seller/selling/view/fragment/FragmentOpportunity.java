package com.tokopedia.seller.selling.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalOrder;
import com.tokopedia.seller.R;

public class FragmentOpportunity extends Fragment {

    public static FragmentOpportunity newInstance() {
        return new FragmentOpportunity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_opportunity, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_check_opportunity).setOnClickListener(v -> {
            RouteManager.route(getActivity(), ApplinkConstInternalOrder.OPPORTUNITY);
        });
    }
}
