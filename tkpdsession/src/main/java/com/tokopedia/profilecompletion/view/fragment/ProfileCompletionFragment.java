package com.tokopedia.profilecompletion.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepositoryImpl;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.profilecompletion.domain.model.GetUserInfoDomainData;
import com.tokopedia.profilecompletion.view.listener.GetProfileListener;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionPresenter;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionPresenterImpl;
import com.tokopedia.session.R;

/**
 * Created by stevenfredian on 6/22/17.
 */

public class ProfileCompletionFragment extends BasePresenterFragment<ProfileCompletionPresenter>
                                    implements GetProfileListener {

    ProgressBar progressBar;
    ViewPager viewPager;
    TextView percentText;
    TextView proceed;
    RadioGroup radioGroup;
    private View avaMan;
    private View avaWoman;
    private TextInputEditText date;
    private AutoCompleteTextView month;
    private TextInputEditText year;


    public static ProfileCompletionFragment createInstance() {
        return new ProfileCompletionFragment();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.getUserInfo();
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

        Bundle bundle = new Bundle();
        SessionHandler sessionHandler = new SessionHandler(getActivity());
        String authKey = sessionHandler.getAccessToken(getActivity());
        authKey = sessionHandler.getTokenType(getActivity()) + " " + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);

        AccountsService accountsService = new AccountsService(bundle);

        ProfileSourceFactory profileSourceFactory =
                new ProfileSourceFactory(
                        getActivity(),
                        accountsService,
                        new GetUserInfoMapper()
                );

        GetUserInfoUseCase getUserInfoUseCase = new GetUserInfoUseCase(
                new JobExecutor(),
                new UIThread(),
                new ProfileRepositoryImpl(profileSourceFactory)
        );


        presenter = new ProfileCompletionPresenterImpl(this, getUserInfoUseCase);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_profile_completion;
    }

    @Override
    protected void initView(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.ProgressBar);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        percentText = (TextView) view.findViewById(R.id.percentText);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        proceed = (TextView) view.findViewById(R.id.proceed);
    }

    @Override
    protected void setViewListener() {
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    fragment = getChildFragmentManager().findFragmentByTag("date");
                }

                if(fragment != null){
                    avaMan = getView().findViewById(R.id.fragment_container).findViewById(R.id.ava_man);
                    avaWoman = getView().findViewById(R.id.fragment_container).findViewById(R.id.ava_woman);
                    radioGroup = (RadioGroup) getView().findViewById(R.id.fragment_container).findViewById(R.id.radioGroup);
                    date = (TextInputEditText) getView().findViewById(R.id.fragment_container).findViewById(R.id.date);
                    month = (AutoCompleteTextView) getView().findViewById(R.id.fragment_container).findViewById(R.id.month);
                    year = (TextInputEditText) getView().findViewById(R.id.fragment_container).findViewById(R.id.year);
                    Toast.makeText(getActivity(),date.getText()+" "+month.getText()+" "+year.getText(), Toast.LENGTH_LONG).show();
                }else {
                    int selected = radioGroup.getCheckedRadioButtonId();
                }
            }
        });
    }

    @Override
    protected void initialVar() {
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onGetUserInfo(GetUserInfoDomainData getUserInfoDomainData) {
        progressBar.setProgress(getUserInfoDomainData.getCompletion());
        percentText.setText(String.format("%s%%", String.valueOf(progressBar.getProgress())));
        loadFragment(getUserInfoDomainData);
    }

    private void loadFragment(GetUserInfoDomainData getUserInfoDomainData) {
        ProfileCompletionDateFragment genderFragment = ProfileCompletionDateFragment.createInstance(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, genderFragment, ProfileCompletionDateFragment.TAG).commit();
        }
    }
}
