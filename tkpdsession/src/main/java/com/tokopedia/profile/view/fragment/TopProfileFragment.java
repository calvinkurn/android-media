package com.tokopedia.profile.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.profile.view.customview.PartialUserDataView;
import com.tokopedia.profile.view.customview.PartialUserInfoView;
import com.tokopedia.profile.view.customview.PartialUserShopView;
import com.tokopedia.profile.view.listener.TopProfileFragmentListener;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;
import com.tokopedia.session.R;

/**
 * @author by milhamj on 08/02/18.
 */

public class TopProfileFragment extends TkpdBaseV4Fragment
        implements TopProfileFragmentListener.View {

    private PartialUserDataView partialUserDataView;
    private PartialUserInfoView partialUserInfoView;
    private PartialUserShopView partialUserShopView;

    public static TopProfileFragment newInstance() {
        TopProfileFragment fragment = new TopProfileFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_profile, container, false);

        initView(view);
        return view;
    }

    @Override
    protected String getScreenName() {
        //TODO milhamj
        return null;
    }

    private void initView(View view) {
        partialUserDataView = view.findViewById(R.id.profile_data);
        partialUserInfoView = view.findViewById(R.id.profile_info);
        partialUserShopView = view.findViewById(R.id.profile_shop);
    }

    @Override
    public void renderData(TopProfileViewModel topProfileViewModel) {
        partialUserDataView.renderData(topProfileViewModel);
        partialUserInfoView.renderData(topProfileViewModel);
        partialUserShopView.renderData(topProfileViewModel);
    }


}
