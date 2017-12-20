package com.tokopedia.session.changephonenumber.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by milhamj on 20/12/17.
 */

public class ChangePhoneNumberEmailFragment extends BaseDaggerFragment implements ChangePhoneNumberInputFragmentListener.View {

    private Unbinder unbinder;

    public static ChangePhoneNumberEmailFragment newInstance() {
        ChangePhoneNumberEmailFragment fragment = new ChangePhoneNumberEmailFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.fragment_change_phone_number_email, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        initView(parentView);
        setViewListener();
        initVar();
        //TODO presenter.attachView(this);
        return parentView;
    }

    private void initView(View view) {

    }

    private void setViewListener() {

    }

    private void initVar() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
