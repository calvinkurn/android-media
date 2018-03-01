package com.tokopedia.profile.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.profile.ProfileComponentInstance;
import com.tokopedia.profile.di.DaggerProfileContentComponent;
import com.tokopedia.profile.di.ProfileContentModule;
import com.tokopedia.profile.view.customview.PartialUserDataView;
import com.tokopedia.profile.view.customview.PartialUserInfoView;
import com.tokopedia.profile.view.customview.PartialUserShopView;
import com.tokopedia.profile.view.listener.ProfileContentListener;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;
import com.tokopedia.session.R;
import javax.inject.Inject;
import static android.view.View.VISIBLE;

/**
 * @author by milhamj on 08/02/18.
 */

public class TopProfileFragment extends BaseDaggerFragment implements ProfileContentListener.View{

    private static final String PARAM_USER_ID = "user_id";

    private PartialUserDataView partialUserDataView;
    private PartialUserInfoView partialUserInfoView;
    private PartialUserShopView partialUserShopView;

    private String userId;

    @Inject
    ProfileContentListener.Presenter presenter;

    public static TopProfileFragment newInstance(String userId) {
        TopProfileFragment fragment = new TopProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_profile, container, false);
        presenter.attachView(this);
        initVar();
        initView(view);
        presenter.initView(userId);
        return view;
    }

    @Override
    protected void initInjector() {
        DaggerProfileContentComponent.builder()
                .profileComponent(ProfileComponentInstance.getProfileComponent(getActivity().getApplication()))
                .profileContentModule(new ProfileContentModule())
                .build()
                .inject(this);
    }

    @Override
    protected String getScreenName() {
        //TODO milhamj
        return null;
    }

    private void initVar() {
        userId = getArguments().getString(PARAM_USER_ID);
    }

    private void initView(View view){
        partialUserDataView = view.findViewById(R.id.profile_data);
        partialUserInfoView = view.findViewById(R.id.profile_info);
        partialUserShopView = view.findViewById(R.id.profile_shop);

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onSuccessGetProfileData(TopProfileViewModel topProfileViewModel) {
        partialUserDataView.renderData(topProfileViewModel);
        partialUserInfoView.renderData(topProfileViewModel);
        partialUserShopView.renderData(topProfileViewModel);
    }

    @Override
    public void onErrorGetProfileData(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }
}
