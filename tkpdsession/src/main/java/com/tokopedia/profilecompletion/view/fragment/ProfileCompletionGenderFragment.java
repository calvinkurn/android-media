package com.tokopedia.profilecompletion.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionPresenter;
import com.tokopedia.session.R;


/**
 * Created by stevenfredian on 7/3/17.
 */

public class ProfileCompletionGenderFragment extends BasePresenterFragment<ProfileCompletionPresenter> {

    public static final String TAG = "gender";
    private ProfileCompletionFragment view;
    private View proceed;
    private RadioGroup radioGroup;
    private View avaWoman;
    private View avaMan;
    private View skip;

    public static ProfileCompletionGenderFragment createInstance(ProfileCompletionFragment view) {
        ProfileCompletionGenderFragment fragment = new ProfileCompletionGenderFragment();
        fragment.view = view;
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = view.getPresenter();
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_profile_completion_gender;
    }

    @Override
    protected void initView(View view) {
        avaMan = view.findViewById(R.id.ava_man);
        avaWoman = view.findViewById(R.id.ava_woman);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        proceed = this.view.getView().findViewById(R.id.proceed);
        skip = this.view.getView().findViewById(R.id.skip);
    }

    @Override
    protected void setViewListener() {
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

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }
}
