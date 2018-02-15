package com.tokopedia.profilecompletion.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract;
import com.tokopedia.session.R;

import java.text.DateFormatSymbols;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by stevenfredian on 7/3/17.
 */

public class ProfileCompletionGenderFragment extends BaseDaggerFragment {

    public static final String TAG = "gender";
    private ProfileCompletionFragment view;
    private View proceed;
    private RadioGroup radioGroup;
    private View avaWoman;
    private View avaMan;
    private View skip;
    private View progress;
    private Unbinder unbinder;
    private ProfileCompletionContract.Presenter presenter;


    public static ProfileCompletionGenderFragment createInstance(ProfileCompletionFragment view) {
        ProfileCompletionGenderFragment fragment = new ProfileCompletionGenderFragment();
        fragment.view = view;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_profile_completion_gender, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        initView(parentView);
        setViewListener();
        initialVar();
        return parentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected int getFragmentLayout() {
        return R.layout.fragment_profile_completion_gender;
    }

    protected void initView(View view) {
        avaMan = view.findViewById(R.id.ava_man);
        avaWoman = view.findViewById(R.id.ava_woman);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        proceed = this.view.getView().findViewById(R.id.proceed);
        skip = this.view.getView().findViewById(R.id.skip);
        progress = this.view.getView().findViewById(R.id.progress);
        this.view.canProceed(false);
    }

    protected void setViewListener() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                view.canProceed(true);
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View selected = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                int idx = radioGroup.indexOfChild(selected);
                if (selected == avaMan) {
                    idx = EditUserProfileUseCase.MALE;
                } else if (selected == avaWoman) {
                    idx = EditUserProfileUseCase.FEMALE;
                }
                presenter.editUserInfo(idx);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.skipView(TAG);
            }
        });
    }

    protected void initialVar() {
        presenter = view.getPresenter();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
