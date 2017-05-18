package com.tokopedia.seller.topads.keyword.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.seller.R;

import java.util.ArrayList;

/**
 * Created by Test on 5/18/2017.
 */

public class TopAdsKeywordAddDetailFragment extends Fragment {

    public static final String TAG = TopAdsKeywordAddDetailFragment.class.getSimpleName();

    public static final String EXTRA_GROUP_NAME = "grp_nm";
    public static final String EXTRA_KEYWORD_NAME = "key_nm";
    public static final String EXTRA_SERVER_WORDS = "server_wrds";
    public static final String EXTRA_LOCAL_WORDS = "lcl_wrds";

    private int maxKeywordInGroup;


    private String groupName;
    private String keywordName;
    private ArrayList<String> serverWords = new ArrayList<>();
    private ArrayList<String> localWords = new ArrayList<>();
    private RecyclerView recyclerView;
    private View buttonAddKeyword;
    private TextInputLayout textInputKeyword;
    private EditText editTextKeyword;
    private TextView textViewKeywordCurrentMax;
    private TextView textViewTotalKeyworGroup;
    private View buttonSave;

    public static TopAdsKeywordAddDetailFragment newInstance(String groupName,
                                                             String keywordName,
                                                             ArrayList<String> serverWords,
                                                             ArrayList<String> localWords) {
        TopAdsKeywordAddDetailFragment fragment = new TopAdsKeywordAddDetailFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_GROUP_NAME, groupName);
        args.putString(EXTRA_KEYWORD_NAME, keywordName);
        args.putStringArrayList(EXTRA_SERVER_WORDS, serverWords);
        args.putStringArrayList(EXTRA_LOCAL_WORDS, localWords);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        maxKeywordInGroup = getContext().getResources().getInteger(R.integer.topads_max_keyword_in_group);

        Bundle args = getArguments();
        groupName = args.getString(EXTRA_GROUP_NAME);
        keywordName = args.getString(EXTRA_KEYWORD_NAME);
        serverWords = args.getStringArrayList(EXTRA_SERVER_WORDS);
        if (savedInstanceState == null) {
            localWords = args.getStringArrayList(EXTRA_LOCAL_WORDS);
        } else {
            localWords = savedInstanceState.getStringArrayList(EXTRA_LOCAL_WORDS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_top_ads_keyword_add_detail, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        buttonAddKeyword = v.findViewById(R.id.button_add_keyword);
        textInputKeyword = (TextInputLayout) v.findViewById(R.id.text_input_layout_word);
        editTextKeyword = textInputKeyword.getEditText();

        textViewKeywordCurrentMax = (TextView) v.findViewById(R.id.text_keyword_current_and_max);
        textViewKeywordCurrentMax.setText(getString(R.string.top_ads_total_keywords_current_and_max,
                                                        getLocalKeyWordSize() +getServerKeyWordSize(),
                                                        maxKeywordInGroup));
        textViewTotalKeyworGroup = (TextView) v.findViewById(R.id.text_keyword_group);
        textViewTotalKeyworGroup.setText(getString(R.string.top_ads_keywords_in_groups, getServerKeyWordSize()));

        buttonSave = v.findViewById(R.id.button_save);
        buttonSave.setEnabled(false);
        return v;
    }

    private int getLocalKeyWordSize(){
        if (localWords == null)  {
            return 0;
        }
        return localWords.size();
    }

    private int getServerKeyWordSize(){
        if (serverWords == null)  {
            return 0;
        }
        return serverWords.size();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(EXTRA_LOCAL_WORDS, localWords);
    }
}
