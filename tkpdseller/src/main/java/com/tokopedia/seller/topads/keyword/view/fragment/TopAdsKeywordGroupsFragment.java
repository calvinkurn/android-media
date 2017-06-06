package com.tokopedia.seller.topads.keyword.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.keyword.di.component.DaggerTopAdsKeywordNewChooseGroupComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsKeywordNewChooseGroupModule;
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsKeywordGroupListAdapter;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsKeywordGroupListListener;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsKeywordGroupListView;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordNewChooseGroupPresenter;
import com.tokopedia.seller.topads.view.adapter.TopAdsBaseListAdapter;
import com.tokopedia.seller.topads.view.listener.TopAdsFilterContentFragmentListener;
import com.tokopedia.seller.topads.view.model.Ad;

import java.util.List;

import javax.inject.Inject;

/**
 * @author normansyahputa on 5/26/17.
 */

public class TopAdsKeywordGroupsFragment extends TopAdsBaseListFragment<TopAdsKeywordNewChooseGroupPresenter>
        implements TopAdsKeywordGroupListView, TopAdsFilterContentFragmentListener, TopAdsKeywordGroupListAdapter.Listener {

    private static final String TAG = "TopAdsKeywordGroupsFrag";
    protected TopAdsFilterContentFragment.Callback callback;
    @Inject
    TopAdsKeywordNewChooseGroupPresenter topAdsKeywordNewChooseGroupPresenter;
    private EditText groupFilterSearch;
    private GroupAd selection;

    /**
     * Sign for title filter list
     */
    private boolean active;
    private ImageView groupFilterImage;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            topAdsKeywordNewChooseGroupPresenter.searchGroupName(editable.toString());
        }
    };
    private View hideThings;
    private PopupMenu popupMenu;
    private TopAdsKeywordGroupListListener groupListAdapterListener;

    public static TopAdsKeywordGroupsFragment createInstance(GroupAd currentGroupAd) {
        TopAdsKeywordGroupsFragment topAdsKeywordGroupsFragment = new TopAdsKeywordGroupsFragment();
        Bundle argument = new Bundle();
        if (currentGroupAd != null)
            argument.putParcelable(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION, currentGroupAd);
        topAdsKeywordGroupsFragment.setArguments(argument);
        return topAdsKeywordGroupsFragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerTopAdsKeywordNewChooseGroupComponent.builder()
                .topAdsKeywordNewChooseGroupModule(new TopAdsKeywordNewChooseGroupModule())
                .appComponent(getComponent(AppComponent.class))
                .build()
                .inject(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        super.initialListener(activity);
        if (activity != null && activity instanceof TopAdsKeywordGroupListListener) {
            groupListAdapterListener = (TopAdsKeywordGroupListListener) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null && getArguments() != null) {
            selection = getArguments().getParcelable(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        topAdsKeywordNewChooseGroupPresenter.attachView(this);
        View view = super.onCreateView(inflater, container, savedInstanceState);
        groupFilterSearch = (EditText) view.findViewById(R.id.group_filter_search);
        groupFilterImage = (ImageView) view.findViewById(R.id.group_filter_search_icon);
        hideThings = view.findViewById(R.id.hide_things);
        hideThings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
        hideThings.setVisibility(View.INVISIBLE);
        popupMenu = new PopupMenu(getActivity(), hideThings);
        popupMenu.getMenuInflater().inflate(R.menu.popup_delete_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.delete_keyword) {
                    enableSelection();
                    return true;
                }
                return false;
            }
        });

        groupFilterSearch.addTextChangedListener(textWatcher);
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        swipeToRefresh.setEnabled(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        topAdsKeywordNewChooseGroupPresenter.searchGroupName("");
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_keyword_filter_group_name;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        topAdsKeywordNewChooseGroupPresenter.detachView();
    }

    @Override
    public void onItemClicked(Ad ad) {

    }

    @Override
    public void onGetGroupAdList(List<GroupAd> groupAds) {
        onSearchAdLoaded(groupAds, groupAds.size());
        if (selection != null) {
            notifySelect(selection);
        }
    }

    @Override
    public void onGetGroupAdListError() {

    }

    @Override
    protected TopAdsBaseListAdapter<GroupAd> getNewAdapter() {
        TopAdsKeywordGroupListAdapter topAdsKeywordGroupListAdapter = new TopAdsKeywordGroupListAdapter();
        topAdsKeywordGroupListAdapter.setListener(this);
        return topAdsKeywordGroupListAdapter;
    }

    @Override
    public String getTitle(Context context) {
        return "Filter Group";
    }

    @Override
    public Intent addResult(Intent intent) {
        if (selection != null)
            intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION, selection);

        return intent;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setCallback(TopAdsFilterContentFragment.Callback callback) {
        this.callback = callback;
    }

    @Override
    public void notifySelect(GroupAd groupAd) {
        if (groupListAdapterListener != null) {
            groupListAdapterListener.notifySelect(groupAd);
        }

        this.selection = groupAd;
        groupFilterSearch.removeTextChangedListener(textWatcher);
        groupFilterSearch.setText(groupAd.getName());
        groupFilterSearch.setFocusable(false);
        groupFilterSearch.setEnabled(false);
        groupFilterImage.setImageResource(R.drawable.ic_delete_keyword);
        recyclerView.setVisibility(View.GONE);
        hideThings.setVisibility(View.VISIBLE);
        groupFilterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableSelection();
            }
        });
    }

    protected void enableSelection() {
        if (groupListAdapterListener != null) {
            groupListAdapterListener.resetSelection();
        }

        selection = null;
        groupFilterSearch.setFocusableInTouchMode(true);
        groupFilterSearch.setEnabled(true);
        groupFilterImage.setImageResource(R.drawable.ic_search_black_24dp);
        groupFilterImage.setOnClickListener(null);
        groupFilterSearch.addTextChangedListener(textWatcher);
        groupFilterSearch.setText("");
        recyclerView.setVisibility(View.VISIBLE);
        hideThings.setVisibility(View.INVISIBLE);
    }
}