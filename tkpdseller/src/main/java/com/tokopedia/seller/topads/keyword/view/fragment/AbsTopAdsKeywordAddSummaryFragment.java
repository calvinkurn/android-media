package com.tokopedia.seller.topads.keyword.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordAddDetailActivity;

import java.util.ArrayList;

/**
 * Created by hendry on 5/18/2017.
 */

public abstract class AbsTopAdsKeywordAddSummaryFragment extends Fragment {

    public static final String TAG = AbsTopAdsKeywordAddSummaryFragment.class.getSimpleName();

    public static final String EXTRA_GROUP_ID = "grp_id";
    public static final String EXTRA_GROUP_NAME = "grp_name";

    public static final String SAVED_BROAD = "svd_broad";
    public static final String SAVED_PHRASE = "svd_grp_name";
    public static final String SAVED_EXACT = "svd_exact";
    public static final String SAVED_SERVER_KEYWORD_COUNT = "svd_server";

    public static final int BROAD_REQ_CODE = 100;
    public static final int PHRASE_REQ_CODE = 200;
    public static final int EXACT_REQ_CODE = 300;

    private int max_words_in_group;

    protected int groupId;
    protected String groupName;
    private ArrayList<String> broadMatchList = new ArrayList<>();
    private ArrayList<String> phraseMatchList = new ArrayList<>();
    private ArrayList<String> exactMatchList = new ArrayList<>();
    private TextView textKeywordGroupCount;
    private TextView textKeywordAddTotal;
    private View buttonSave;

    int serverKeywordCount = -1;
    private TextView textBroadMatchCount;
    private TextView textPhraseMatchCount;
    private TextView textExactMatchCount;

    public static Bundle createBundle(int groupId, String groupName) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_GROUP_ID, groupId);
        args.putString(EXTRA_GROUP_NAME, groupName);
        return args;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        max_words_in_group = getResources().getInteger(R.integer.topads_max_keyword_in_group);

        Bundle args = getArguments();
        if (args != null) {
            groupId = args.getInt(EXTRA_GROUP_ID);
            groupName = args.getString(EXTRA_GROUP_NAME);
        }

        if (savedInstanceState != null) {
            broadMatchList = savedInstanceState.getStringArrayList(SAVED_BROAD);
            phraseMatchList = savedInstanceState.getStringArrayList(SAVED_PHRASE);
            exactMatchList = savedInstanceState.getStringArrayList(SAVED_EXACT);
            serverKeywordCount = savedInstanceState.getInt(SAVED_SERVER_KEYWORD_COUNT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_top_ads_keyword_add_summary, container, false);
        TextView textGroupName = (TextView) v.findViewById(R.id.text_group_name);
        textGroupName.setText(groupName);
        textKeywordGroupCount = (TextView) v.findViewById(R.id.text_keyword_group_count);

        View buttonBroadMatch = v.findViewById(R.id.button_board_match);
        buttonBroadMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFull(broadMatchList)) {
                    return;
                }
                TopAdsKeywordAddDetailActivity.start(AbsTopAdsKeywordAddSummaryFragment.this,
                        getActivity(), BROAD_REQ_CODE, groupName, getString(R.string.top_ads_broad_match),
                        serverKeywordCount, max_words_in_group - serverKeywordCount - phraseMatchList.size() - exactMatchList.size(),
                        broadMatchList);
            }
        });
        View buttonPhraseMatch = v.findViewById(R.id.button_phrase_match);
        buttonPhraseMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFull(phraseMatchList)) {
                    return;
                }
                TopAdsKeywordAddDetailActivity.start(AbsTopAdsKeywordAddSummaryFragment.this,
                        getActivity(), PHRASE_REQ_CODE, groupName, getString(R.string.top_ads_phrase_match),
                        serverKeywordCount, max_words_in_group - serverKeywordCount - broadMatchList.size() - exactMatchList.size(),
                        phraseMatchList);
            }
        });
        View buttonExactMatch = v.findViewById(R.id.button_exact_match);
        buttonExactMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFull(exactMatchList)) {
                    return;
                }
                TopAdsKeywordAddDetailActivity.start(AbsTopAdsKeywordAddSummaryFragment.this,
                        getActivity(), EXACT_REQ_CODE, groupName, getString(R.string.top_ads_exact_match),
                        serverKeywordCount, max_words_in_group - serverKeywordCount - broadMatchList.size() - phraseMatchList.size(),
                        exactMatchList);
            }
        });

        textBroadMatchCount = (TextView) v.findViewById(R.id.text_broad_match_count);
        textPhraseMatchCount = (TextView) v.findViewById(R.id.text_phrase_match_count);
        textExactMatchCount = (TextView) v.findViewById(R.id.text_exact_match_count);

        textKeywordAddTotal = (TextView) v.findViewById(R.id.text_keyword_total);
        buttonSave = v.findViewById(R.id.button_save);
        buttonSave.setEnabled(false);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return v;
    }

    private boolean checkFull(ArrayList<String> list) {
        if (serverKeywordCount >= max_words_in_group) {
            CommonUtils.UniversalToast(getActivity(), getString(R.string.error_keyword_per_group_reach_limit));
            return true;
        }
        // cannot add more keyword if the local list is full
        // if the list has at least 1 data, user still can delete it.
        if (list.size() == 0 && getTotalLocalKeyword() == (max_words_in_group - serverKeywordCount)) {
            CommonUtils.UniversalToast(getActivity(), getString(R.string.error_keyword_per_group_reach_limit));
            return true;
        }
        return false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null || serverKeywordCount < 0) {
            // TODO presenter check count
            // TODO remove this test
            serverKeywordCount = 46;
        }
        // TODO after server load
        updateServerKeywordCount();
        updateLocalTotalKeywordCount();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLocalCount();
    }

    private void updateLocalCount() {
        updateLocalBroadMatchCount();
        updateLocalPhraseMatchCount();
        updateLocalExactMatchCount();
        updateLocalTotalKeywordCount();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case BROAD_REQ_CODE:
                    broadMatchList = data.getStringArrayListExtra(TopAdsKeywordAddDetailActivity.RESULT_WORDS);
                    break;
                case PHRASE_REQ_CODE:
                    phraseMatchList = data.getStringArrayListExtra(TopAdsKeywordAddDetailActivity.RESULT_WORDS);
                    break;
                case EXACT_REQ_CODE:
                    exactMatchList = data.getStringArrayListExtra(TopAdsKeywordAddDetailActivity.RESULT_WORDS);
                    break;
            }
        }
    }

    private void updateLocalBroadMatchCount() {
        textBroadMatchCount.setText(getString(R.string.count, broadMatchList.size()));
    }

    private void updateLocalPhraseMatchCount() {
        textPhraseMatchCount.setText(getString(R.string.count, phraseMatchList.size()));
    }

    private void updateLocalExactMatchCount() {
        textExactMatchCount.setText(getString(R.string.count, exactMatchList.size()));
    }

    private void updateServerKeywordCount() {
        textKeywordGroupCount.setText(getString(R.string.top_ads_keywords_in_groups, serverKeywordCount));
    }

    private void updateLocalTotalKeywordCount() {
        int totalLocalKeyword = getTotalLocalKeyword();

        textKeywordAddTotal.setText(getString(R.string.top_ads_total_keywords_current_and_max_summary,
                totalLocalKeyword,
                max_words_in_group - serverKeywordCount));

        buttonSave.setEnabled(totalLocalKeyword > 0);
    }

    private int getTotalLocalKeyword() {
        return broadMatchList.size() + phraseMatchList.size() + exactMatchList.size();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(SAVED_BROAD, broadMatchList);
        outState.putStringArrayList(SAVED_PHRASE, phraseMatchList);
        outState.putStringArrayList(SAVED_EXACT, exactMatchList);
        outState.putInt(SAVED_SERVER_KEYWORD_COUNT, serverKeywordCount);
    }

}
