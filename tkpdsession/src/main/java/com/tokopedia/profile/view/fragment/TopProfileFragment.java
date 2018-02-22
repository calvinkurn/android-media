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
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;
import com.tokopedia.session.R;

import static android.view.View.VISIBLE;

/**
 * @author by milhamj on 08/02/18.
 */

public class TopProfileFragment extends BaseDaggerFragment {

    private TopProfileViewModel topProfileViewModel;
    private FrameLayout partialUserDataView;

    RelativeLayout bannerIncompleteProfile;
    RelativeLayout partialPhoneNumber;
    RelativeLayout partialEmail;
    RelativeLayout partialGender;
    RelativeLayout partialBirthDate;
    TextView verifiedPhoneNumber;
    TextView verifiedEmail;
    TextView dataPhoneNumber;
    TextView dataEmail;
    TextView dataGender;
    TextView dataBirthDate;

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
        initViewData(view);
    }

    @Override
    protected void initInjector() {
        getChildFragmentManager().findFragmentByTag("HAI");
        //TODO milhamj
    }

    @Override
    protected String getScreenName() {
        //TODO milhamj
        return null;
    }

    private void initView(View view){
        partialUserDataView = view.findViewById(R.id.partial_profile_user_data);
        bannerIncompleteProfile = view.findViewById(R.id.rl_incomplete_profile);
        partialPhoneNumber = view.findViewById(R.id.rl_phone_number);
        partialEmail = view.findViewById(R.id.rl_email);
        partialGender = view.findViewById(R.id.rl_gender);
        partialBirthDate = view.findViewById(R.id.rl_birth_date);
        verifiedPhoneNumber = view.findViewById(R.id.tv_verified_phone_number);
        verifiedEmail = view.findViewById(R.id.tv_verified_email);
        dataPhoneNumber = view.findViewById(R.id.tv_phone_number);
        dataEmail = view.findViewById(R.id.tv_email);
        dataGender = view.findViewById(R.id.tv_gender);
        dataBirthDate = view.findViewById(R.id.tv_birth_date);
    }

    private void initViewData(View view){
        partialUserDataView = view.findViewById(R.id.partial_profile_user_data);
        if (topProfileViewModel.getCompletion() < 100) {
            bannerIncompleteProfile.setVisibility(VISIBLE);
        }
        if (!topProfileViewModel.getPhoneNumber().equals("")) {
            partialPhoneNumber.setVisibility(VISIBLE);
            dataPhoneNumber.setText(topProfileViewModel.getPhoneNumber());
            if (topProfileViewModel.isPhoneVerified()) {
                verifiedPhoneNumber.setVisibility(VISIBLE);
            }
        }
        if (!topProfileViewModel.getEmail().equals("")) {
            partialEmail.setVisibility(VISIBLE);
            dataEmail.setText(topProfileViewModel.getEmail());
            if (topProfileViewModel.isEmailVerified()) {
                verifiedEmail.setVisibility(VISIBLE);
            }
        }
        if (!topProfileViewModel.getBirthDate().equals("")) {
            partialBirthDate.setVisibility(VISIBLE);
            dataBirthDate.setText(topProfileViewModel.getBirthDate());
        }

        if (!topProfileViewModel.getGender().equals("")) {
            partialGender.setVisibility(VISIBLE);
            dataGender.setText(topProfileViewModel.getGender());
        }
    }

    private void setDummydata(){
        this.topProfileViewModel = new TopProfileViewModel();
        this.topProfileViewModel.setCompletion(90);
        this.topProfileViewModel.setPhoneNumber("081818818");
        this.topProfileViewModel.setPhoneVerified(true);
        this.topProfileViewModel.setEmail("alvin@email.com");
        this.topProfileViewModel.setEmailVerified(false);
        this.topProfileViewModel.setBirthDate("23 April 1992");
        this.topProfileViewModel.setGender("");
    }
}
