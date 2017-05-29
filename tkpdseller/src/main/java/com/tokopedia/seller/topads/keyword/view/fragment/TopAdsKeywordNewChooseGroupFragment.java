package com.tokopedia.seller.topads.keyword.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.TkpdTextInputLayout;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.keyword.di.component.DaggerTopAdsKeywordNewChooseGroupComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsKeywordNewChooseGroupModule;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsKeywordNewChooseGroupView;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordNewChooseGroupPresenter;
import com.tokopedia.seller.topads.view.adapter.TopAdsAutoCompleteAdapter;
import com.tokopedia.seller.topads.view.widget.TopAdsCustomAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 2/22/17.
 */

public class TopAdsKeywordNewChooseGroupFragment extends BaseDaggerFragment implements TopAdsKeywordNewChooseGroupView {

    @Inject
    public TopAdsKeywordNewChooseGroupPresenter topAdsKeywordNewChooseGroupPresenter;
    TkpdTextInputLayout inputLayoutChooseGroup;
    TopAdsCustomAutoCompleteTextView autoCompleteChooseGroup;
    Button buttonNext;
    private TopAdsAutoCompleteAdapter adapterChooseGroup;

    private ArrayList<String> groupNames = new ArrayList<>();
    private List<GroupAd> groupAds = new ArrayList<>();
    private String choosenId = "";

    public static Fragment createInstance() {
        Fragment fragment = new TopAdsKeywordNewChooseGroupFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_ads_keyword_new_choose_group, container, false);
        topAdsKeywordNewChooseGroupPresenter.attachView(this);

        buttonNext = (Button) view.findViewById(R.id.button_submit);
        adapterChooseGroup = new TopAdsAutoCompleteAdapter(getActivity(), R.layout.item_top_ads_autocomplete_text);
        inputLayoutChooseGroup = (TkpdTextInputLayout) view.findViewById(R.id.input_layout_choose_group);
        autoCompleteChooseGroup = (TopAdsCustomAutoCompleteTextView) view.findViewById(R.id.choose_group_auto_text);


        adapterChooseGroup.setListenerGetData(new TopAdsAutoCompleteAdapter.ListenerGetData() {
            @Override
            public ArrayList<String> getData() {
                return groupNames;
            }
        });
        autoCompleteChooseGroup.setAdapter(adapterChooseGroup);
        autoCompleteChooseGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                topAdsKeywordNewChooseGroupPresenter.searchGroupName(editable.toString());
            }
        });
        autoCompleteChooseGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                autoCompleteChooseGroup.lockView();
                if (groupAds.get(i) != null) {
                    choosenId = groupAds.get(i).getId();
                }
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("id", choosenId);
            }
        });
        return view;
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
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onGetGroupAdList(List<GroupAd> groupAds) {
        this.groupAds.clear();
        this.groupAds.addAll(groupAds);
        groupNames.clear();
        inputLayoutChooseGroup.setError(null);
        for (GroupAd groupAd : groupAds) {
            groupNames.add(groupAd.getName());
        }
        adapterChooseGroup.getFilter().filter(autoCompleteChooseGroup.getText());
    }

    @Override
    public void onGetGroupAdListError() {
        inputLayoutChooseGroup.setError(getString(R.string.error_connection_problem));
    }

    @Override
    public void onSearchAdLoaded(@NonNull List adList, boolean isEndOfFile) {

    }

    @Override
    public void onSearchAdLoaded(@NonNull List adList, int totalItem) {

    }

    @Override
    public void onLoadSearchAdError() {

    }
}