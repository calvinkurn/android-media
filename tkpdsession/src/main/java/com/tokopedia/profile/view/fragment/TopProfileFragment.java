package com.tokopedia.profile.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.profile.ProfileComponentInstance;
import com.tokopedia.profile.view.customview.PartialUserDataView;
import com.tokopedia.profile.view.customview.PartialUserInfoView;
import com.tokopedia.profile.view.customview.PartialUserShopView;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;
import com.tokopedia.session.R;

import static android.view.View.VISIBLE;

/**
 * @author by milhamj on 08/02/18.
 */

public class TopProfileFragment extends BaseDaggerFragment {

    private TopProfileViewModel topProfileViewModel;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDummydata();
        initView(view);
    }

    @Override
    protected void initInjector() {
        DaggerProfileContentComponent.builder()
                .profileComponent(ProfileComponentInstance.getProfileComponent(getActivity().getApplication()))
                .build
                .inject(this);
    }

    @Override
    protected String getScreenName() {
        //TODO milhamj
        return null;
    }

    private void initView(View view){
        partialUserDataView = view.findViewById(R.id.profile_data);
        partialUserInfoView = view.findViewById(R.id.profile_info);
        partialUserShopView = view.findViewById(R.id.profile_shop);
        partialUserDataView.renderData(topProfileViewModel);
        partialUserInfoView.renderData(topProfileViewModel);
        partialUserShopView.renderData(topProfileViewModel);
    }

    private void setDummydata(){
        topProfileViewModel = new TopProfileViewModel();
        topProfileViewModel.setCompletion(90);
        topProfileViewModel.setPhoneNumber("081818818");
        topProfileViewModel.setPhoneVerified(true);
        topProfileViewModel.setEmail("alvin@email.com");
        topProfileViewModel.setEmailVerified(false);
        topProfileViewModel.setBirthDate("23 April 1992");
        topProfileViewModel.setGender("");
        topProfileViewModel.setSummaryScore("80%");
        topProfileViewModel.setPositiveScore("40");
        topProfileViewModel.setNetralScore("31");
        topProfileViewModel.setNegativeScore("0");

        topProfileViewModel.setShopName("Namanya Siapa Hayo");
        topProfileViewModel.setShopLogo("https://ecs7.tokopedia.net/img/cache/215/shops-1/2016/8/14/279371/279371_bc651460-1a61-4fea-858d-173c4dc0db91.jpg");
        topProfileViewModel.setGoldShop(true);
        topProfileViewModel.setOfficialShop(true);
        topProfileViewModel.setShopLocation("Papua");
        topProfileViewModel.setShopLastOnline("23 Jan 1992");

        topProfileViewModel.setIsUser(true);
    }
}
