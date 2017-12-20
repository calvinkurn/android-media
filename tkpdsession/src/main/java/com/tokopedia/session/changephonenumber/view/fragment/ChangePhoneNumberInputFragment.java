package com.tokopedia.session.changephonenumber.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by milhamj on 20/12/17.
 */

public class ChangePhoneNumberInputFragment extends BaseDaggerFragment implements ChangePhoneNumberInputFragmentListener.View {

    private TextView oldPhoneNumber;
    private EditText newPhoneNumber;
    private TextView nextButton;

    private Unbinder unbinder;

    public static ChangePhoneNumberInputFragment newInstance() {
        ChangePhoneNumberInputFragment fragment = new ChangePhoneNumberInputFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.fragment_change_phone_number_input, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        initView(parentView);
        setViewListener();
        initVar();
        //TODO presenter.attachView(this);
        return parentView;
    }

    private void initView(View view) {
        oldPhoneNumber = view.findViewById(R.id.old_phone_number_value);
        newPhoneNumber = view.findViewById(R.id.new_phone_number_value);
        nextButton = view.findViewById(R.id.next_button);
    }

    private void setViewListener() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initVar() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
