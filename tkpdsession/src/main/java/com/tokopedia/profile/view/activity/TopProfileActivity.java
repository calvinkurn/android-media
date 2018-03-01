package com.tokopedia.profile.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.SessionRouter;
import com.tokopedia.abstraction.base.view.activity.BaseEmptyActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.ManagePeople;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.peoplefave.activity.PeopleFavoritedShop;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.tab.Tabs;
import com.tokopedia.profile.common.di.ProfileComponent;
import com.tokopedia.profile.view.adapter.ProfileTabPagerAdapter;
import com.tokopedia.profile.view.fragment.TopProfileFragment;
import com.tokopedia.profile.view.listener.TopProfileActivityListener;
import com.tokopedia.profile.view.viewmodel.ProfileSectionItem;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;
import com.tokopedia.session.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 08/02/18.
 */

public class TopProfileActivity extends BaseEmptyActivity
        implements TopProfileActivityListener.View {

    private static final String EXTRA_PARAM_USER_ID = "user_id";
    private static final String TITLE_PROFILE = "Info Akun";
    private static final String TITLE_POST = "Post";
    private static final String ZERO = "0";
    private static final int MANAGE_PEOPLE_CODE = 13;

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private Tabs tabLayout;
    private ViewPager viewPager;
    private ImageView avatar;
    private TextView name;
    private LinearLayout buttonManageAccount;
    private LinearLayout buttonFollow;
    private ImageView buttonFollowImage;
    private TextView buttonFollowText;
    private TextView title;
    private TextView description;
    private LinearLayout followingLayout;
    private TextView followingValue;
    private LinearLayout followersLayout;
    private TextView followersValue;
    private View followersSeparator;
    private LinearLayout favoriteShopLayout;
    private TextView favoriteShopValue;

    private String userId;
    private TopProfileViewModel topProfileViewModel;

    private ProfileComponent profileComponent;

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
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        initVar();
        initView();
        setupToolbar();
        setViewListener();
        loadSection();
    }

    private void initVar(){
        if (getIntent().getExtras() != null) {
            userId = getIntent().getExtras().getString(EXTRA_PARAM_USER_ID, "");
        }
    }

    private void initView() {
        appBarLayout = findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        toolbar = findViewById(R.id.toolbar);
        tabLayout =findViewById(R.id.tab_profile);
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
    }

    private void setViewListener() {
        followingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topProfileViewModel != null) {
                    startActivity(((TkpdCoreRouter) getApplicationContext())
                            .getKolFollowingPageIntent(
                                    TopProfileActivity.this,
                                    topProfileViewModel.getUserId())
                    );
                }
            }
        });

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
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_top_profile;
    }

    @Override
    public void populateData(TopProfileViewModel viewModel) {
        topProfileViewModel = viewModel;

        ImageHandler.loadImageCircle2(avatar.getContext(), avatar, topProfileViewModel.getAvatar());

        name.setText(topProfileViewModel.getName());
        followingValue.setText(topProfileViewModel.getFollowing());
        setTextDisabledOrNot(followingValue, topProfileViewModel.getFollowing());
        favoriteShopValue.setText(topProfileViewModel.getFavoritedShop());
        setTextDisabledOrNot(favoriteShopValue, topProfileViewModel.getFavoritedShop());

        if (topProfileViewModel.isKol()) {
            name.setCompoundDrawables(
                    MethodChecker.getDrawable(this, R.drawable.ic_kol_badge), null, null, null);
            title.setVisibility(View.VISIBLE);
            title.setText(topProfileViewModel.getTitle());
            description.setVisibility(View.VISIBLE);
            description.setText(topProfileViewModel.getBiodata());
            followersValue.setText(topProfileViewModel.getFollowers());
            setTextDisabledOrNot(followersValue, topProfileViewModel.getFollowers());

            if (!topProfileViewModel.isUser()) {
                buttonFollow.setVisibility(View.VISIBLE);

                if (topProfileViewModel.isFollowed()) {
                    enableFollowButton();
                } else {
                    disableFollowButton();
                }
            }
        } else {
            followersLayout.setVisibility(View.GONE);
            followersSeparator.setVisibility(View.GONE);
        }

        if (topProfileViewModel.isUser()) {
            buttonManageAccount.setVisibility(View.VISIBLE);
        }
    }

    private void setTextDisabledOrNot(TextView textView, String value) {
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
                R.drawable.bg_button_white_enabled_border));
        buttonFollowText.setText(R.string.follow);
        buttonFollowText.setTextColor(MethodChecker.getColor(this,
                R.color.white));
        buttonFollowImage.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_profile, menu);
        return true;
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
        switch (requestCode) {
            case MANAGE_PEOPLE_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    //TODO milhamj refresh on activity result
                }
                break;
            default:
                break;
        }
    }

    private void setupToolbar() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(
                            topProfileViewModel != null ? topProfileViewModel.getName() : ""
                    );
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
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

    private void loadSection(){
        List<ProfileSectionItem> profileSectionItemList = new ArrayList<>();

        if (getApplicationContext() instanceof SessionRouter) {
            //TODO milhamj change this userid
            BaseDaggerFragment kolPostFragment =
                    ((SessionRouter) getApplicationContext()).getKolPostFragment(userId);
            profileSectionItemList.add(new ProfileSectionItem(TITLE_POST, kolPostFragment));
        }
        TopProfileFragment profileFragment = TopProfileFragment.newInstance(userId);
        profileSectionItemList.add(new ProfileSectionItem(TITLE_PROFILE, profileFragment));

        ProfileTabPagerAdapter profileTabPagerAdapter =
                new ProfileTabPagerAdapter(getSupportFragmentManager());
        profileTabPagerAdapter.setItemList(profileSectionItemList);
        viewPager.setAdapter(profileTabPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
