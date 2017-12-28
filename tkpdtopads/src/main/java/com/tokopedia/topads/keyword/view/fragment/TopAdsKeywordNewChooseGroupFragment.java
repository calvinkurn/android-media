package com.tokopedia.topads.keyword.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.design.text.TkpdTextInputLayout;
import com.tokopedia.seller.product.edit.utils.ViewUtils;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.common.view.fragment.TopAdsBaseStepperFragment;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsAutoCompleteAdapter;
import com.tokopedia.topads.dashboard.view.widget.TopAdsCustomAutoCompleteTextView;
import com.tokopedia.topads.keyword.constant.KeywordTypeDef;
import com.tokopedia.topads.keyword.di.component.DaggerTopAdsKeywordNewChooseGroupComponent;
import com.tokopedia.topads.keyword.di.module.TopAdsKeywordNewChooseGroupModule;
import com.tokopedia.topads.keyword.helper.KeywordTypeMapper;
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordNewChooseGroupView;
import com.tokopedia.topads.keyword.view.model.TopAdsKeywordStepperModel;
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordNewChooseGroupPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 2/22/17.
 */

public class TopAdsKeywordNewChooseGroupFragment extends TopAdsBaseStepperFragment<TopAdsKeywordStepperModel> implements TopAdsKeywordNewChooseGroupView {

    public static final String TAG = TopAdsKeywordNewChooseGroupFragment.class.getSimpleName();
    public static final int ADD_REQUEST_CODE = 100;
    private static final String EXTRA_CHOOSEN_GROUP = "EXTRA_CHOOSEN_GROUP";
    private static final String EXTRA_IS_POSITIVE = "is_pos";
    private static final String SAVED_GROUP_ID = "grp_id";
    private static final String SAVED_GROUP_NAME = "grp_nm";
    private static final String SAVED_KEYWORD_COUNT = "key_count";
    private static final String SAVED_SPINNER_POS = "spinner_pos";
    @Inject
    public TopAdsKeywordNewChooseGroupPresenter topAdsKeywordNewChooseGroupPresenter;

    private TopAdsAutoCompleteAdapter adapterChooseGroup;
    private TkpdTextInputLayout inputLayoutChooseGroup;
    private TopAdsCustomAutoCompleteTextView autoCompleteChooseGroup;

    private boolean isPositive;

    private ArrayList<String> groupNames = new ArrayList<>();
    private List<GroupAd> groupAds = new ArrayList<>();
    private String chosenId = "";
    private int keywordCount = 0;
    private View buttonNext;
    private SpinnerTextView spinnerKeywordType;
    private View viewGroupKeywordInfo;
    private TextView textKeywordTitle;
    private TextView textKeywordDesc;
    private TextView textKeywordExample;

    private @KeywordTypeDef int keywordType = -1;
    private String groupId;

    public static Fragment newInstance() {
        return new TopAdsKeywordNewChooseGroupFragment();
    }

    @Override
    protected void saveStepperModel(TopAdsKeywordStepperModel stepperModel) {
        stepperModel.setGroupId(chosenId);
        stepperModel.setChoosenId(chosenId);
        stepperModel.setGroupName(autoCompleteChooseGroup.getText().toString());
        stepperModel.setKeywordType(keywordType);
        stepperModel.setServerCount(keywordCount);
        stepperModel.setMaxWords(getResources().getInteger(R.integer.top_ads_keyword_max_in_group) - keywordCount);
        stepperModel.setLocalWords(new ArrayList<String>());
    }

    @Override
    protected void initiateStepperModel() {
        if(stepperModel == null)
            stepperModel = new TopAdsKeywordStepperModel();
    }

    @Override
    protected void goToNextPage() {
        //validate if server max not reached
        if (keywordCount >= getResources().getInteger(R.integer.top_ads_keyword_max_in_group)) {
            CommonUtils.UniversalToast(getActivity(), getString(R.string.top_ads_keyword_per_group_reach_limit));
            return;
        }
        // go to add keyword activity
        if (stepperListener != null) {
            stepperListener.goToNextPage(stepperModel);
            // hide loading
        }
    }

    @Override
    protected void populateView(TopAdsKeywordStepperModel stepperModel) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initiateStepperModel();
        isPositive = stepperModel.isPositive();
        groupId = stepperModel.getGroupId();
        adapterChooseGroup = new TopAdsAutoCompleteAdapter(getActivity(), R.layout.item_autocomplete_text);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,  @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_ads_keyword_new_choose_group, container, false);

        buttonNext = view.findViewById(R.id.button_next);
        inputLayoutChooseGroup = (TkpdTextInputLayout) view.findViewById(R.id.input_layout_choose_group);
        autoCompleteChooseGroup = (TopAdsCustomAutoCompleteTextView) inputLayoutChooseGroup.findViewById(R.id.choose_group_auto_text);
        autoCompleteChooseGroup.setAdapter(adapterChooseGroup);

        spinnerKeywordType = (SpinnerTextView) view.findViewById(R.id.spinner_keyword_type);

        viewGroupKeywordInfo = view.findViewById(R.id.view_group_keyword_info);
        textKeywordTitle = (TextView) viewGroupKeywordInfo.findViewById(R.id.text_keyword_title);
        textKeywordDesc = (TextView) viewGroupKeywordInfo.findViewById(R.id.text_keyword_desc);
        textKeywordExample = (TextView) viewGroupKeywordInfo.findViewById(R.id.text_keyword_example);
        viewGroupKeywordInfo.setVisibility(View.GONE);

        // update from savedInstance
        String groupName = null;
        int spinnerPos = -1;
        if (savedInstanceState != null) {
            chosenId = savedInstanceState.getString(SAVED_GROUP_ID);
            groupName = savedInstanceState.getString(SAVED_GROUP_NAME);
            keywordCount = savedInstanceState.getInt(SAVED_KEYWORD_COUNT);
            spinnerPos = savedInstanceState.getInt(SAVED_SPINNER_POS);
        }
        if (TextUtils.isEmpty(chosenId)) {
            inputLayoutChooseGroup.setErrorSuccessEnabled(false);
        } else {
            autoCompleteChooseGroup.setText(groupName);
            autoCompleteChooseGroup.lockView();
            inputLayoutChooseGroup.setSuccess(getString(R.string.top_ads_keywords_in_groups, keywordCount));
        }
        if (spinnerPos > -1) {
            spinnerKeywordType.setSpinnerPosition(spinnerPos);
            keywordType = KeywordTypeMapper.mapToDef(isPositive, spinnerPos);
            setKeywordsInfo(keywordType);
        }

        view.requestFocus();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setChooseGroupListener();
        setSpinnerListener();
        setButtonNextListener();

        checkButtonNextEnabled();
    }

    private void setChooseGroupListener() {
        autoCompleteChooseGroup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if(isFocus) {
                    topAdsKeywordNewChooseGroupPresenter.searchGroupName("");
                }
            }
        });
        autoCompleteChooseGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!autoCompleteChooseGroup.isEnabled() || autoCompleteChooseGroup.isPerformingCompletion()) {
                    return;
                }
                inputLayoutChooseGroup.hideErrorSuccess();
                buttonNext.setEnabled(false);
                chosenId = "";
                topAdsKeywordNewChooseGroupPresenter.searchGroupName(editable.toString());
            }
        });
        autoCompleteChooseGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                autoCompleteChooseGroup.lockView();
                if (groupAds.get(i) != null) {
                    GroupAd groupAd = groupAds.get(i);
                    chosenId = groupAd.getId();
                    keywordCount = isPositive ? groupAd.getPositiveCount() : groupAd.getNegativeCount();
                    inputLayoutChooseGroup.setSuccess(getString(R.string.top_ads_keywords_in_groups, keywordCount));
                    checkButtonNextEnabled();
                }
            }
        });

        if(groupId != null){
            autoCompleteChooseGroup.setText(groupId);
            autoCompleteChooseGroup.lockView();
            topAdsKeywordNewChooseGroupPresenter.searchGroupName("");
        }

        adapterChooseGroup.setListenerGetData(new TopAdsAutoCompleteAdapter.ListenerGetData() {
            @Override
            public ArrayList<String> getData() {
                return groupNames;
            }
        });
    }

    private void setSpinnerListener() {
        spinnerKeywordType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                keywordType = KeywordTypeMapper.mapToDef(isPositive, position);
                setKeywordsInfo(keywordType);
                checkButtonNextEnabled();
            }
        });
    }

    private void setButtonNextListener() {
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextClicked();
            }
        });
    }

    private void setKeywordsInfo(@KeywordTypeDef int keywordType) {
        switch (keywordType) {
            case KeywordTypeDef.KEYWORD_TYPE_PHRASE:
                textKeywordTitle.setText(MethodChecker.fromHtml(getString(R.string.top_ads_keyword_phrase_match)));
                textKeywordDesc.setText(MethodChecker.fromHtml(getString(R.string.top_ads_keyword_phrase_match_positive_description)));
                textKeywordExample.setText(MethodChecker.fromHtml(getString(R.string.top_ads_keyword_phrase_match_example)));
                viewGroupKeywordInfo.setVisibility(View.VISIBLE);
                break;
            case KeywordTypeDef.KEYWORD_TYPE_EXACT:
                textKeywordTitle.setText(MethodChecker.fromHtml(getString(R.string.top_ads_keyword_exact_match)));
                textKeywordDesc.setText(MethodChecker.fromHtml(getString(R.string.top_ads_keyword_exact_match_positive_description)));
                textKeywordExample.setText(MethodChecker.fromHtml(getString(R.string.top_ads_keyword_exact_match_example)));
                viewGroupKeywordInfo.setVisibility(View.VISIBLE);
                break;
            case KeywordTypeDef.KEYWORD_TYPE_NEGATIVE_PHRASE:
                textKeywordTitle.setText(MethodChecker.fromHtml(getString(R.string.top_ads_keyword_phrase_match)));
                textKeywordDesc.setText(MethodChecker.fromHtml(getString(R.string.top_ads_keyword_phrase_match_negative_description)));
                textKeywordExample.setText(MethodChecker.fromHtml(getString(R.string.top_ads_keyword_phrase_match_example)));
                viewGroupKeywordInfo.setVisibility(View.VISIBLE);
                break;
            case KeywordTypeDef.KEYWORD_TYPE_NEGATIVE_EXACT:
                textKeywordTitle.setText(MethodChecker.fromHtml(getString(R.string.top_ads_keyword_exact_match)));
                textKeywordDesc.setText(MethodChecker.fromHtml(getString(R.string.top_ads_keyword_exact_match_negative_description)));
                textKeywordExample.setText(MethodChecker.fromHtml(getString(R.string.top_ads_keyword_exact_match_example)));
                viewGroupKeywordInfo.setVisibility(View.VISIBLE);
                break;
            default:
                viewGroupKeywordInfo.setVisibility(View.GONE);
        }
    }

    private void checkButtonNextEnabled() {
        if (!TextUtils.isEmpty(chosenId) && keywordType > -1) {
            buttonNext.setEnabled(true);
        } else {
            buttonNext.setEnabled(false);
        }
    }

    @Override
    protected void initInjector() {
        DaggerTopAdsKeywordNewChooseGroupComponent.builder()
                .topAdsKeywordNewChooseGroupModule(new TopAdsKeywordNewChooseGroupModule())
                .topAdsComponent(TopAdsComponentUtils.getTopAdsComponent(this))
                .build()
                .inject(this);
        topAdsKeywordNewChooseGroupPresenter.attachView(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = new Intent();
                    intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
                break;
        }
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
        for (GroupAd groupAd : groupAds) {
            if(groupId != null) {
                if (groupAd.getName().equals(groupId)) {
                    chosenId = groupAd.getId();
                }
                groupId = null; // this is just for the first time only
            }
            groupNames.add(groupAd.getName());
        }
        autoCompleteChooseGroup.showDropDownFilter();
    }

    @Override
    public void onGetGroupAdListError(Throwable e) {
        inputLayoutChooseGroup.setError(ViewUtils.getGeneralErrorMessage(getContext(), e));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topAdsKeywordNewChooseGroupPresenter.detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_GROUP_ID, chosenId);
        outState.putString(SAVED_GROUP_NAME, autoCompleteChooseGroup.getText() == null? "" :
                autoCompleteChooseGroup.getText().toString());
        outState.putInt(SAVED_KEYWORD_COUNT, keywordCount);
        outState.putInt(SAVED_SPINNER_POS, spinnerKeywordType.getSpinnerPosition());
    }
}