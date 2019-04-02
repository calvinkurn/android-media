package com.tokopedia.inbox.rescenter.inbox.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.inbox.rescenter.inbox.fragment.InboxResCenterFragment;
import com.tokopedia.inbox.rescenter.inbox.listener.ResCenterView;
import com.tokopedia.inbox.rescenter.inbox.presenter.ResCenterImpl;
import com.tokopedia.inbox.rescenter.inbox.presenter.ResCenterPresenter;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@Deprecated
public class InboxResCenterActivity extends DrawerPresenterActivity<ResCenterPresenter>
        implements ResCenterView {

    public static final String TAG = InboxResCenterActivity.class.getSimpleName();
    public static final String EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION";

    @BindView(R2.id.pager)
    public ViewPager viewPager;
    @BindView(R2.id.indicator)
    public TabLayout tabLayout;

    private SectionsPagerAdapter pagerAdapter;
    private List<Model> list;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, InboxResCenterActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_INBOX_RESOLUTION_CENTER;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.clearNotif(this, getIntent());
        presenter.initAnalytics(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setLocalyticFlow(this);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        presenter = new ResCenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_res_center;
    }

    @Override
    protected void initView() {
        super.initView();
        presenter.initView(this);
    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.RESOLUTION_CENTER;
    }

    @Override
    protected void setViewListener() {
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
    }

    @Override
    protected void initVar() {
        initFragmentList();
        initFragmentAdapter();
    }

    @Override
    public void initFragmentList() {
        list = new ArrayList<>();
        presenter.initFragmentList(this, list);
    }

    @Override
    public void initFragmentAdapter() {
        pagerAdapter = new SectionsPagerAdapter(getFragmentManager());
    }

    @Override
    protected void setActionVar() {
        presenter.onActionVar(this);
    }

    @Override
    public void setAdapter() {
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void setTabLayout() {
        for (Model model : list) {
            tabLayout.addTab(tabLayout.newTab().setText(model.titleFragment));
        }
    }

    @Override
    public void setOffScreenPageLimit() {
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
    }

    @Override
    public Bundle getBundleArguments() {
        return getIntent().getExtras();
    }

    @Override
    public void setTabPosition(int i) {
        viewPager.setCurrentItem(i, true);
    }

    @Override
    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return InboxResCenterFragment.createFragment(list.get(position));
        }

        @Override
        public int getCount() {
            return list.size();
        }

    }

    public static class Model implements Parcelable {
        public int typeFragment;
        public String titleFragment;

        public Model(int typeFragment, String titleFragment) {
            this.typeFragment = typeFragment;
            this.titleFragment = titleFragment;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.typeFragment);
            dest.writeString(this.titleFragment);
        }

        protected Model(Parcel in) {
            this.typeFragment = in.readInt();
            this.titleFragment = in.readString();
        }

        public static final Parcelable.Creator<Model> CREATOR = new Parcelable.Creator<Model>() {
            @Override
            public Model createFromParcel(Parcel source) {
                return new Model(source);
            }

            @Override
            public Model[] newArray(int size) {
                return new Model[size];
            }
        };
    }
}
