package com.tokopedia.profilecompletion.view.fragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory;
import com.tokopedia.profilecompletion.data.mapper.EditUserInfoMapper;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepositoryImpl;
import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.core.profile.model.GetUserInfoDomainData;
import com.tokopedia.profilecompletion.view.ProgressBarAnimation;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.profilecompletion.view.listener.EditProfileListener;
import com.tokopedia.profilecompletion.view.listener.GetProfileListener;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletion;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionPresenter;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionPresenterImpl;
import com.tokopedia.session.R;

/**
 * Created by stevenfredian on 6/22/17.
 */

public class ProfileCompletionFragment extends BasePresenterFragment<ProfileCompletionPresenter>
        implements GetProfileListener, EditProfileListener, ProfileCompletion.View {

    ProgressBar progressBar;
    ViewPager viewPager;
    TextView percentText;
    TextView proceed;
    private ProgressBarAnimation animation;
    private GetUserInfoDomainData data;
    private String filled;
    private TextView skip;


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
                        new GetUserInfoMapper(),
                        new EditUserInfoMapper()
                );

        GetUserInfoUseCase getUserInfoUseCase = new GetUserInfoUseCase(
                new JobExecutor(),
                new UIThread(),
                new ProfileRepositoryImpl(profileSourceFactory)
        );

        EditUserProfileUseCase editUserProfileUseCase = new EditUserProfileUseCase(
                new JobExecutor(),
                new UIThread(),
                new ProfileRepositoryImpl(profileSourceFactory)
        );

        presenter = new ProfileCompletionPresenterImpl(this, getUserInfoUseCase, editUserProfileUseCase);
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
        skip = (TextView) view.findViewById(R.id.skip);
    }

    @Override
    protected void setViewListener() {
    }

    @Override
    protected void initialVar() {
        animation = new ProgressBarAnimation(progressBar);
        filled = "filled";
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onGetUserInfo(GetUserInfoDomainData getUserInfoDomainData) {
        this.data = getUserInfoDomainData;
        testDummyData();
        updateProgressBar(0, data.getCompletion());
        loadFragment(getUserInfoDomainData);
    }

    private void testDummyData() {
        data.setCompletion(50);
        data.setPhoneVerified(false);
        data.setGender(0);
//        data.setBday("0");
    }

    private void updateProgressBar(int oldValue, int newValue) {
        data.setCompletion(newValue);
        animation.setValue(oldValue, newValue);
        animation.setDuration(500);
        progressBar.startAnimation(animation);
        progressBar.setProgress(data.getCompletion());
        percentText.setText(String.format("%s%%", String.valueOf(progressBar.getProgress())));

        int[] colors = getResources().getIntArray(R.array.green_indicator);
        int indexColor = (newValue - 50) / 10;

        LayerDrawable shape = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.horizontal_progressbar);
        GradientDrawable runningBar = (GradientDrawable) ((ScaleDrawable) shape.findDrawableByLayerId(R.id.progress_col)).getDrawable();
        runningBar.setColor(colors[indexColor]);
        runningBar.mutate();
        progressBar.setProgressDrawable(shape);
    }

    private void loadFragment(GetUserInfoDomainData getUserInfoDomainData) {

        FragmentTransaction transaction = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            transaction = getChildFragmentManager().beginTransaction();
        }
        if (!getUserInfoDomainData.isPhoneVerified()) {
            ProfilePhoneVerifCompletionFragment verifCompletionFragment = ProfilePhoneVerifCompletionFragment.createInstance(this);
            transaction.replace(R.id.fragment_container, verifCompletionFragment, ProfilePhoneVerifCompletionFragment.TAG).commit();
        }else if (checkingIsEmpty(getUserInfoDomainData.getBday())) {
            ProfileCompletionDateFragment dateFragment = ProfileCompletionDateFragment.createInstance(this);
            transaction.replace(R.id.fragment_container, dateFragment, ProfileCompletionDateFragment.TAG).commit();
        }else if (checkingIsEmpty(String.valueOf(getUserInfoDomainData.getGender()))) {
            ProfileCompletionGenderFragment genderFragment = ProfileCompletionGenderFragment.createInstance(this);
            transaction.replace(R.id.fragment_container, genderFragment, ProfileCompletionGenderFragment.TAG).commit();
        }  else {
            ((ProfileCompletionActivity) getActivity()).onFinishedForm();
        }
    }

    private boolean checkingIsEmpty(String item) {
        return item == null || item.length() == 0 || item.equals("0");
    }

    public ProfileCompletionPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onSuccessEditProfile(int edit) {
        if (edit == EditUserProfileUseCase.EDIT_DOB) {
            data.setBday(filled);
            updateProgressBar(data.getCompletion(), data.getCompletion() + 10);
        } else if (edit == EditUserProfileUseCase.EDIT_GENDER) {
            data.setGender(3);
            updateProgressBar(data.getCompletion(), data.getCompletion() + 10);
        } else if (edit == EditUserProfileUseCase.EDIT_VERIF) {
            data.setPhoneVerified(true);
            updateProgressBar(data.getCompletion(), data.getCompletion() + 30);
        }
        loadFragment(data);
    }

    @Override
    public void onFailedEditProfile(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void skipView(String tag) {
        switch (tag){
            case ProfileCompletionGenderFragment.TAG:
                data.setGender(3);
                loadFragment(data);
                break;
            case ProfileCompletionDateFragment.TAG:
                data.setBday(filled);
                loadFragment(data);
                break;
            case ProfilePhoneVerifCompletionFragment.TAG:
                data.setPhoneVerified(true);
                loadFragment(data);
                break;
            default:
                break;
        }
    }

    public GetUserInfoDomainData getData() {
        return data;
    }
}
