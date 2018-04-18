package com.tokopedia.profile.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.SessionRouter;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.SessionApplinkUrl;
import com.tokopedia.core.ManagePeople;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.peoplefave.activity.PeopleFavoritedShop;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.component.Tabs;
import com.tokopedia.profile.ProfileComponentInstance;
import com.tokopedia.profile.di.DaggerTopProfileComponent;
import com.tokopedia.profile.di.TopProfileModule;
import com.tokopedia.profile.view.adapter.TopProfileTabPagerAdapter;
import com.tokopedia.profile.view.fragment.DisabledPostFragment;
import com.tokopedia.profile.view.fragment.TopProfileFragment;
import com.tokopedia.profile.view.listener.TopProfileActivityListener;
import com.tokopedia.profile.view.listener.TopProfileFragmentListener;
import com.tokopedia.profile.view.viewmodel.TopProfileSectionItem;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginemail.view.activity.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.analytics.TopProfileAnalytics.Action.CLICK_ON_MANAGE_ACCOUNT;
import static com.tokopedia.analytics.TopProfileAnalytics.Category.TOP_PROFILE;
import static com.tokopedia.analytics.TopProfileAnalytics.Event.EVENT_CLICK_TOP_PROFILE;

/**
 * @author by milhamj on 08/02/18.
 */

public class TopProfileActivity extends BaseSimpleActivity
        implements TopProfileActivityListener.View {

    private static final String EXTRA_PARAM_USER_ID = "user_id";
    private static final String TITLE_PROFILE = "Info Akun";
    private static final String TITLE_POST = "Post";
    private static final String ZERO = "0";
    private static final int MANAGE_PEOPLE_CODE = 13;
    private static final int LOGIN_REQUEST_CODE = 23;
    @Inject
    TopProfileActivityListener.Presenter presenter;
    private AppBarLayout appBarLayout;

    private Toolbar toolbar;

    private Tabs tabLayout;

    private ViewPager viewPager;

    private ImageView avatar;
    private ImageView buttonFollowImage;

    private TextView name;
    private TextView buttonFollowText;
    private TextView title;
    private TextView description;
    private TextView followingValue;
    private TextView followersValue;
    private TextView favoriteShopValue;
    private TextView errorText;
    private TextView buttonTryAgain;
    private TextView buttonFollowToolbar;
    private TextView tvTitleToolbar;

    private LinearLayout buttonManageAccount;
    private LinearLayout buttonFollow;
    private LinearLayout followingLayout;
    private LinearLayout followersLayout;
    private LinearLayout favoriteShopLayout;

    private View followersSeparator;
    private View tabSeparator;
    private View header;
    private View progressView;
    private View errorView;

    private String userId;
    private TopProfileViewModel topProfileViewModel;
    private TopProfileFragment profileFragment;

    @DeepLink(SessionApplinkUrl.PROFILE)
    public static Intent getCallingTopProfile(Context context, Bundle bundle) {
        return TopProfileActivity.newInstance(context, bundle.getString(EXTRA_PARAM_USER_ID, ""));
    }

    public static Intent newInstance(@NonNull Context context, @NonNull String userId) {
        Intent intent = new Intent(context, TopProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_USER_ID, userId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        initInjector();

        presenter.attachView(this);
        initVar();
        initView();
        setupToolbar();
        setViewListener();
        presenter.initView(userId);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_top_profile;
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    private void initInjector() {
        DaggerTopProfileComponent.builder()
                .profileComponent(ProfileComponentInstance.getProfileComponent(this
                        .getApplication()))
                .topProfileModule(new TopProfileModule())
                .build()
                .inject(this);
    }

    private void initVar() {
        if (getIntent().getExtras() != null) {
            userId = getIntent().getExtras().getString(EXTRA_PARAM_USER_ID, "");

            if (TextUtils.isEmpty(userId)) {
                throw new IllegalStateException("usedId can not be empty/null!");
            }
        }
    }

    private void initView() {
        appBarLayout = findViewById(R.id.app_bar_layout);
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_profile);
        viewPager = findViewById(R.id.pager);
        avatar = findViewById(R.id.avatar);
        name = findViewById(R.id.name);
        buttonManageAccount = findViewById(R.id.button_manage_account);
        buttonFollow = findViewById(R.id.button_follow);
        buttonFollowImage = findViewById(R.id.button_follow_image);
        buttonFollowText = findViewById(R.id.button_follow_text);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        followingLayout = findViewById(R.id.following_layout);
        followingValue = findViewById(R.id.following_value);
        followersLayout = findViewById(R.id.followers_layout);
        followersValue = findViewById(R.id.followers_value);
        followersSeparator = findViewById(R.id.followers_separator);
        favoriteShopLayout = findViewById(R.id.favorite_shop_layout);
        favoriteShopValue = findViewById(R.id.favorite_shop_value);
        header = findViewById(R.id.header);
        progressView = findViewById(R.id.progress_view);
        errorView = findViewById(R.id.error_view);
        errorText = findViewById(R.id.error_text);
        buttonTryAgain = findViewById(R.id.button_try_again);
        buttonFollowToolbar = findViewById(R.id.button_follow_toolbar);
        tvTitleToolbar = findViewById(R.id.tv_title_toolbar);
        tabSeparator = findViewById(R.id.separator_tab);
    }

    private void setViewListener() {
        if (!GlobalConfig.isSellerApp()) {
            followingLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (topProfileViewModel != null
                            && getApplicationContext() instanceof TkpdCoreRouter) {
                        startActivity(((TkpdCoreRouter) getApplicationContext())
                                .getKolFollowingPageIntent(
                                        TopProfileActivity.this,
                                        topProfileViewModel.getUserId())
                        );
                    }
                }
            });
        }

        favoriteShopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topProfileViewModel != null) {
                    startActivity(PeopleFavoritedShop.createIntent(
                            TopProfileActivity.this,
                            String.valueOf(topProfileViewModel.getUserId()))
                    );
                }
            }
        });

        buttonManageAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopProfileActivity.this, ManagePeople.class);
                startActivityForResult(intent, MANAGE_PEOPLE_CODE);

                if (getContext().getApplicationContext() instanceof AbstractionRouter) {
                    ((AbstractionRouter) getContext().getApplicationContext()).getAnalyticTracker()
                            .sendEventTracking(EVENT_CLICK_TOP_PROFILE,
                                    TOP_PROFILE,
                                    CLICK_ON_MANAGE_ACCOUNT,
                                    "");
                }
            }
        });

        buttonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followUnfollowKol();
            }
        });

        buttonFollowToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followUnfollowKol();
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public SessionRouter getSessionRouter() {
        return (SessionRouter) getApplicationContext();
    }

    @Override
    public void showMainView() {
        hideLoading();
        hideErrorScreen();
        header.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        hideErrorScreen();
        header.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        header.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        progressView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorScreen(String errorMessage, View.OnClickListener onClickListener) {
        hideLoading();
        header.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);

        if (errorMessage != null) errorText.setText(errorMessage);
        else errorText.setText(R.string.server_busy);

        buttonTryAgain.setOnClickListener(onClickListener);
    }

    @Override
    public void hideErrorScreen() {
        header.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        errorText.setText(R.string.server_busy);
        errorView.setVisibility(View.GONE);
        buttonTryAgain.setOnClickListener(null);
    }

    @Override
    public void onSuccessGetProfileData(TopProfileViewModel topProfileViewModel) {
        this.topProfileViewModel = topProfileViewModel;

        initTabLoad();
        populateData();
    }

    @Override
    public void onErrorGetProfileData(String message) {
        showErrorScreen(message, tryAgainOnlickListener());
    }

    @Override
    public void onSuccessFollowKol() {
        topProfileViewModel.setFollowed(!topProfileViewModel.isFollowed());

        if (!topProfileViewModel.isFollowed()) enableFollowButton();
        else disableFollowButton();
    }

    @Override
    public void onErrorFollowKol(String message) {
        showError(message);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.getTopProfileData(userId);
    }

    @Override
    public void onGoToLoginPage() {
        Intent intent = LoginActivity.getCallingIntent(getContext());
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    private void initTabLoad() {
        List<TopProfileSectionItem> topProfileSectionItemList = new ArrayList<>();

        if (topProfileViewModel.isKol()
                && !GlobalConfig.isSellerApp()
                && getApplicationContext() instanceof SessionRouter) {
            BaseDaggerFragment kolPostFragment =
                    ((SessionRouter) getApplicationContext()).getKolPostFragment(userId);
            topProfileSectionItemList.add(
                    new TopProfileSectionItem(TITLE_POST, kolPostFragment));
        }

        if (topProfileViewModel.isKol()
                && GlobalConfig.isSellerApp()) {
            DisabledPostFragment disabledPostFragment =
                    DisabledPostFragment.newInstance();
            topProfileSectionItemList.add(
                    new TopProfileSectionItem(TITLE_POST, disabledPostFragment));
        }

        if (topProfileViewModel.isUser()
                || !topProfileViewModel.isKol()) {
            if (profileFragment == null) {
                profileFragment = TopProfileFragment.newInstance();
            }
            topProfileSectionItemList.add(new TopProfileSectionItem(TITLE_PROFILE,
                    profileFragment));
            TopProfileFragmentListener.View fragmentListener = profileFragment;
            fragmentListener.renderData(topProfileViewModel);
        }

        TopProfileTabPagerAdapter topProfileTabPagerAdapter = new TopProfileTabPagerAdapter
                (getSupportFragmentManager());
        topProfileTabPagerAdapter.setItemList(topProfileSectionItemList);
        viewPager.setAdapter(topProfileTabPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void populateData() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (topProfileViewModel == null) return;

                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    tvTitleToolbar.setText(topProfileViewModel.getName());
                    tvTitleToolbar.setVisibility(View.VISIBLE);

                    if (topProfileViewModel.isKol() && !topProfileViewModel.isUser()) {
                        if (topProfileViewModel.isFollowed()) {
                            disableFollowToolbarButton();
                        } else {
                            enableFollowToolbarButton();
                        }
                        buttonFollowToolbar.setVisibility(View.VISIBLE);
                    } else {
                        buttonFollowToolbar.setVisibility(View.GONE);
                    }
                    isShow = true;

                } else if (isShow) {
                    tvTitleToolbar.setVisibility(View.GONE);
                    buttonFollowToolbar.setVisibility(View.GONE);
                    isShow = false;
                }
            }
        });

        tabSeparator.setVisibility(topProfileViewModel.isKol() && topProfileViewModel.isUser() ?
                View.VISIBLE : View.GONE);

        tabLayout.setVisibility(topProfileViewModel.isKol() && topProfileViewModel.isUser() ?
                View.VISIBLE : View.GONE);

        ImageHandler.loadImageCircle2(avatar.getContext(),
                avatar,
                topProfileViewModel.getUserPhoto());

        name.setText(topProfileViewModel.getName());
        followingValue.setText(topProfileViewModel.getFollowing());
        setTextDisabledOrNot(followingLayout,
                followingValue,
                topProfileViewModel.getFollowing());
        favoriteShopValue.setText(topProfileViewModel.getFavoritedShop());
        setTextDisabledOrNot(favoriteShopLayout,
                favoriteShopValue,
                topProfileViewModel.getFavoritedShop());

        if (topProfileViewModel.isKol()) {
            name.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(getContext(), R.drawable.ic_kol_badge),
                    null,
                    null,
                    null);
            if (!TextUtils.isEmpty(topProfileViewModel.getTitle())) {
                title.setVisibility(View.VISIBLE);
                title.setText(topProfileViewModel.getTitle());
            } else {
                title.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(topProfileViewModel.getBiodata())) {
                description.setVisibility(View.VISIBLE);
                description.setText(topProfileViewModel.getBiodata());
            } else {
                description.setVisibility(View.GONE);
            }
            followersLayout.setVisibility(View.VISIBLE);
            followersSeparator.setVisibility(View.VISIBLE);
            followersValue.setText(topProfileViewModel.getFollowers());
            setTextDisabledOrNot(followersLayout,
                    followersValue,
                    topProfileViewModel.getFollowers());

            if (!topProfileViewModel.isUser()) {
                buttonFollow.setVisibility(View.VISIBLE);

                if (topProfileViewModel.isFollowed()) {
                    disableFollowButton();
                } else {
                    enableFollowButton();
                }
            } else {
                buttonFollow.setVisibility(View.GONE);
            }
        } else {
            name.setCompoundDrawables(null, null, null, null);
            title.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            followersLayout.setVisibility(View.GONE);
            followersSeparator.setVisibility(View.GONE);
        }

        if (topProfileViewModel.isUser()) {
            buttonManageAccount.setVisibility(View.VISIBLE);
        } else {
            buttonManageAccount.setVisibility(View.GONE);
        }
    }

    private void setTextDisabledOrNot(View parentLayout, TextView textView, String value) {
        parentLayout.setEnabled(!value.trim().equals(ZERO));
        textView.setTextColor(
                MethodChecker.getColor(
                        this,
                        value.trim().equals(ZERO) ? R.color.disabled_text : R.color.black_70)
        );
    }

    private void enableFollowButton() {
        buttonFollow.setBackground(MethodChecker.getDrawable(this,
                R.drawable.bg_button_green_enabled));
        buttonFollowText.setText(R.string.follow);
        buttonFollowText.setTextColor(MethodChecker.getColor(this,
                R.color.white));
        buttonFollowImage.setVisibility(View.VISIBLE);
    }

    private void disableFollowButton() {
        buttonFollow.setBackground(MethodChecker.getDrawable(this,
                R.drawable.background_button_border_gray));
        buttonFollowText.setText(R.string.following);
        buttonFollowText.setTextColor(MethodChecker.getColor(this,
                R.color.black_54));
        buttonFollowImage.setVisibility(View.GONE);
    }

    private void enableFollowToolbarButton() {
        buttonFollowToolbar.setText(R.string.follow_with_plus);
        buttonFollowToolbar.setTextColor(getResources().getColor(R.color
                .tkpd_main_green));
    }

    private void disableFollowToolbarButton() {
        buttonFollowToolbar.setText(R.string.following);
        buttonFollowToolbar.setTextColor(getResources().getColor(R.color
                .font_black_secondary_54));
    }

    private void setupToolbar() {
        toolbar.setBackgroundResource(R.drawable.bg_white_toolbar_drop_shadow);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_webview_back_button);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void showError(String message) {
        if (message == null) {
            NetworkErrorHelper.showSnackbar(this);
        } else {
            NetworkErrorHelper.showSnackbar(this, message);
        }
    }

    private View.OnClickListener tryAgainOnlickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getTopProfileData(userId);
            }
        };
    }

    private void followUnfollowKol() {
        if (topProfileViewModel.isFollowed()) presenter.unfollowKol(userId);
        else presenter.followKol(userId);
    }

}
