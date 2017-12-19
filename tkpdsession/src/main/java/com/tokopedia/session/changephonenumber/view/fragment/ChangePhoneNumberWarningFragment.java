package com.tokopedia.session.changephonenumber.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberWarningFragmentListener;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by milhamj on 18/12/17.
 */

public class ChangePhoneNumberWarningFragment extends BaseDaggerFragment implements ChangePhoneNumberWarningFragmentListener.View {

    private WarningViewModel viewModel;
    private Unbinder unbinder;

    public static ChangePhoneNumberWarningFragment newInstance() {
        ChangePhoneNumberWarningFragment fragment = new ChangePhoneNumberWarningFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.fragment_change_phone_number_warning, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        initView(parentView);
        initialVar();
        //TODO presenter.attachView(this);
        return parentView;
    }

    private void initView(View view) {

    }

    private void initialVar() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        //TODO presenter.detachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }


}
