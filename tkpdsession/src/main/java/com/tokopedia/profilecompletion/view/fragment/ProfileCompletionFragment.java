package com.tokopedia.profilecompletion.view.fragment;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionComponent;
import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionPresenter;
import com.tokopedia.profilecompletion.view.util.ProgressBarAnimation;
import com.tokopedia.core.customView.TextDrawable;
import com.tokopedia.profilecompletion.view.viewmodel.ProfileCompletionViewModel;
import com.tokopedia.session.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by stevenfredian on 6/22/17.
 */

public class ProfileCompletionFragment extends BaseDaggerFragment
        implements ProfileCompletionContract.View {

    private static final String DEFAULT_EMPTY_BDAY = "0001-01-01T00:00:00Z";
    private static final String ARGS_DATA = "ARGS_DATA";
    ProgressBar progressBar;
    ViewPager viewPager;
    TextView percentText;
    TextView proceed;
    View progress;
    View main;
    View loading;
    FragmentTransaction transaction;
    @Inject
    ProfileCompletionPresenter presenter;
    private ProgressBarAnimation animation;
    private ProfileCompletionViewModel data;
    private String filled;
    private View skip;
    private Unbinder unbinder;
    private Pair<Integer, Integer> pair;
    private NetworkErrorHelper.RetryClickedListener retryAction;


    public static ProfileCompletionFragment createInstance() {
        return new ProfileCompletionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getParcelable(ARGS_DATA) != null)
            data = savedInstanceState.getParcelable(ARGS_DATA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.fragment_profile_completion, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        setHasOptionsMenu(true);
        initView(parentView);
        initialVar();
        presenter.attachView(this);
        return parentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, R.id.action_skip, 0, "");
        MenuItem menuItem = menu.findItem(R.id.action_skip); // OR THIS
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(getDraw());
        super.onCreateOptionsMenu(menu, inflater);
    }

    private Drawable getDraw() {
        TextDrawable drawable = new TextDrawable(getActivity());
        drawable.setText(getResources().getString(R.string.skip_form));
        drawable.setTextColor(R.color.black_70b);
        return drawable;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_skip) {
            skipView(findChildTag());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initView(View view) {
        progress = view.findViewById(R.id.progress);
        progressBar = (ProgressBar) view.findViewById(R.id.ProgressBar);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        percentText = (TextView) view.findViewById(R.id.percentText);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        proceed = (TextView) view.findViewById(R.id.proceed);
        skip = view.findViewById(R.id.skip);
        main = view.findViewById(R.id.layout_main);
        loading = view.findViewById(R.id.loading_layout);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARGS_DATA, data);
    }

    protected void initialVar() {
        animation = new ProgressBarAnimation(progressBar);
        filled = "filled";
        pair = new Pair<>(R.anim.slide_in_right, R.anim.slide_out_left);
        retryAction = new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                loading.setVisibility(View.VISIBLE);
                presenter.getUserInfo();
            }
        };
    }

    @Override
    public void onErrorGetUserInfo(String string) {
        loading.setVisibility(View.GONE);
        main.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), string, retryAction);
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
        if (indexColor < 0) {
            indexColor = 0;
        }
        LayerDrawable shape = (LayerDrawable) ContextCompat.getDrawable(getActivity(), R.drawable.horizontal_progressbar);
        GradientDrawable runningBar = (GradientDrawable) ((ScaleDrawable) shape.findDrawableByLayerId(R.id.progress_col)).getDrawable();
        runningBar.setColor(colors[indexColor]);
        runningBar.mutate();
        progressBar.setProgressDrawable(shape);
    }

    private void loadFragment(ProfileCompletionViewModel profileCompletionViewModel, Pair<Integer, Integer> pair) {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            transaction = getChildFragmentManager().beginTransaction();
        }
        transaction.setCustomAnimations(pair.first, pair.second);
        chooseFragment(profileCompletionViewModel);
    }

    private void chooseFragment(ProfileCompletionViewModel profileCompletionViewModel) {

        if (checkingIsEmpty(String.valueOf(profileCompletionViewModel.getGender()))) {
            ProfileCompletionGenderFragment genderFragment = ProfileCompletionGenderFragment.createInstance(this);
            transaction.replace(R.id.fragment_container, genderFragment, ProfileCompletionGenderFragment.TAG).commit();
        } else if (checkingIsEmpty(profileCompletionViewModel.getBday())) {
            ProfileCompletionDateFragment dateFragment = ProfileCompletionDateFragment.createInstance(this);
            transaction.replace(R.id.fragment_container, dateFragment, ProfileCompletionDateFragment.TAG).commit();
        } else if (!profileCompletionViewModel.isPhoneVerified()) {
            ProfileCompletionPhoneVerificationFragment verifCompletionFragment = ProfileCompletionPhoneVerificationFragment.createInstance(this);
            transaction.replace(R.id.fragment_container, verifCompletionFragment, ProfileCompletionPhoneVerificationFragment.TAG).commit();
        } else if (profileCompletionViewModel.getCompletion() == 100) {
            ((ProfileCompletionActivity) getActivity()).onFinishedForm();
        } else {
            getActivity().finish();
        }
    }

    private boolean checkingIsEmpty(String item) {
        return item == null || item.length() == 0 || item.equals("0")
                || item.equals(DEFAULT_EMPTY_BDAY);
    }

    public ProfileCompletionContract.Presenter getPresenter() {
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
            presenter.setMsisdnVerifiedToCache(true);
            updateProgressBar(data.getCompletion(), data.getCompletion() + 30);
        }
        setViewEnabled();
        loadFragment(data, pair);
    }

    private void setViewEnabled() {
        progress.setVisibility(View.GONE);
        proceed.setVisibility(View.VISIBLE);
        canProceed(true);
        proceed.setText(getString(R.string.continue_form));
        skip.setEnabled(true);
    }


    @Override
    public void disableView() {
        progress.setVisibility(View.VISIBLE);
        proceed.setVisibility(View.GONE);
        skip.setEnabled(false);
    }

    public void canProceed(boolean can) {
        proceed.setEnabled(can);
        if (can) {
            proceed.getBackground().setColorFilter(MethodChecker.getColor(getActivity(), R.color.medium_green), PorterDuff.Mode.SRC_IN);
            proceed.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
        } else {
            proceed.getBackground().setColorFilter(MethodChecker.getColor(getActivity(), R.color.grey_300), PorterDuff.Mode.SRC_IN);
            proceed.setTextColor(MethodChecker.getColor(getActivity(), R.color.grey_500));
        }
    }

    @Override
    public void onFailedEditProfile(String errorMessage) {
        setViewEnabled();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    private String findChildTag() {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fragment_container);
        if(fragment != null) {
            return fragment.getTag();
        }
        return "";
    }


    @Override
    public void skipView(String tag) {
        setViewEnabled();
        switch (tag) {
            case ProfileCompletionGenderFragment.TAG:
                data.setGender(3);
                loadFragment(data, pair);
                break;
            case ProfileCompletionDateFragment.TAG:
                data.setBday(filled);
                loadFragment(data, pair);
                break;
            case ProfileCompletionPhoneVerificationFragment.TAG:
                data.setPhoneVerified(true);
                loadFragment(data, pair);
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetUserInfo(ProfileCompletionViewModel profileCompletionViewModel) {
        main.setVisibility(View.VISIBLE);
        this.data = profileCompletionViewModel;
//        testDummyData();
        updateProgressBar(0, data.getCompletion());
        loading.setVisibility(View.GONE);
        loadFragment(profileCompletionViewModel, new Pair<>(0, 0));
    }

    public ProfileCompletionViewModel getData() {
        return data;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerAppComponent daggerAppComponent = (DaggerAppComponent) DaggerAppComponent.builder()
                .appModule(new AppModule(getContext()))
                .build();
        DaggerProfileCompletionComponent daggerProfileCompletionComponent
                = (DaggerProfileCompletionComponent) DaggerProfileCompletionComponent.builder()
                .appComponent(daggerAppComponent)
                .build();
        daggerProfileCompletionComponent.inject(this);
    }

}
