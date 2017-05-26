package com.tokopedia.seller.topads.keyword.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.view.adapter.KeywordAdapter;
import com.tokopedia.seller.topads.keyword.view.widget.KeywordRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 5/18/2017.
 */

public class TopAdsKeywordAddDetailFragment extends Fragment implements KeywordAdapter.OnKeywordAdapterListener {

    public static final String TAG = TopAdsKeywordAddDetailFragment.class.getSimpleName();

    public static final String EXTRA_SERVER_COUNT = "server_cnt";
    public static final String EXTRA_MAX_WORDS = "max_words";
    public static final String EXTRA_LOCAL_WORDS = "lcl_wrds";

    public static final int MIN_WORDS = 5;

    private int maxKeyword;

    OnSaveKeywordListener onSaveKeywordListener;
    public interface OnSaveKeywordListener{
        void onSaveClicked(ArrayList<String> keyWordsList);
    }

    private ArrayList<String> localWords = new ArrayList<>();
    private int serverCount;
    private int maxWords;
    private KeywordRecyclerView keywordRecyclerView;
    private View buttonAddKeyword;
    private TextInputLayout textInputKeyword;
    private EditText editTextKeyword;
    private TextView textViewKeywordCurrentMax;
    private TextView textViewTotalKeyworGroup;
    private View buttonSave;

    public static TopAdsKeywordAddDetailFragment newInstance(int serverCount,
                                                             int maxWords,
                                                             ArrayList<String> localWords) {
        TopAdsKeywordAddDetailFragment fragment = new TopAdsKeywordAddDetailFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_SERVER_COUNT, serverCount);
        args.putInt(EXTRA_MAX_WORDS, maxWords);
        args.putStringArrayList(EXTRA_LOCAL_WORDS, localWords);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        serverCount = args.getInt(EXTRA_SERVER_COUNT);
        maxKeyword = args.getInt(EXTRA_MAX_WORDS);
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
        textInputKeyword = (TextInputLayout) v.findViewById(R.id.text_input_layout_word);
        editTextKeyword = textInputKeyword.getEditText();
        editTextKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkAddButtonEnabled();
            }
        });
        buttonAddKeyword = v.findViewById(R.id.button_add_keyword);
        buttonAddKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonAddClicked();
            }
        });

        keywordRecyclerView = (KeywordRecyclerView) v.findViewById(R.id.keyword_recycler_view);
        keywordRecyclerView.setOnKeywordAdapterListener(this);

        textViewKeywordCurrentMax = (TextView) v.findViewById(R.id.text_keyword_current_and_max);
        textViewTotalKeyworGroup = (TextView) v.findViewById(R.id.text_keyword_group);

        buttonSave = v.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonSaveClicked();
            }
        });
        buttonSave.setEnabled(false);
        return v;
    }

    private void onButtonSaveClicked(){
        if (onSaveKeywordListener!= null) {
            onSaveKeywordListener.onSaveClicked(keywordRecyclerView.getKeywordList());
        }
    }

    private void onButtonAddClicked(){
        // check if max words has been reached
        if (getLocalKeyWordSize()  == maxKeyword) {
            textInputKeyword.setError(getString(R.string.error_keyword_per_group_reach_limit));
            return;
        }

        String keyword = editTextKeyword.getText().toString();

        // check if contain alphanumeric
        if (StringUtils.containNonSpaceAlphaNumeric(keyword)) {
            textInputKeyword.setError(getString(R.string.error_keyword_must_only_letter_or_digit));
            return;
        }
        // revalidate double space, then check if empty
        String validKeyword = StringUtils.omitPunctuationAndDoubleSpace(keyword).toLowerCase();
        if (TextUtils.isEmpty(validKeyword)) {
            textInputKeyword.setError(getString(R.string.error_keyword_must_only_letter_or_digit));
            return;
        }

        // validate no more 5 words
        String words[] = validKeyword.split("\\s|\\n", MIN_WORDS + 1);
        if (words.length > MIN_WORDS) {
            textInputKeyword.setError(getString(R.string.error_keyword_cannot_more_than_5));
            return;
        }

        // validate if keyword has existed in local
        List<String> keywordList = keywordRecyclerView.getKeywordList();
        boolean hasInputtedInLocal = false;
        for (int i=0, sizei = keywordList.size(); i<sizei; i++) {
            if (validKeyword.equals(keywordList.get(i))) {
                hasInputtedInLocal = true;
                break;
            }
        }

        if (hasInputtedInLocal) {
            textInputKeyword.setError(getString(R.string.error_keyword_has_existed));
            return;
        }

        textInputKeyword.setErrorEnabled(false);

        // add to recyclerview
        keywordRecyclerView.addKeyword(validKeyword);
        editTextKeyword.setText("");
        if (! buttonSave.isEnabled()) {
            buttonSave.setEnabled(true);
        }
        setCurrentMaxKeyword();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        keywordRecyclerView.setKeywordList(localWords);
        setCurrentMaxKeyword();
        setServerKeyword();
        checkAddButtonEnabled();
    }

    private void setCurrentMaxKeyword(){
        textViewKeywordCurrentMax.setText(getString(R.string.top_ads_total_keywords_current_and_max,
                getLocalKeyWordSize(),
                maxKeyword));
    }

    private void setServerKeyword(){
        textViewTotalKeyworGroup.setText(getString(R.string.top_ads_keywords_in_groups, serverCount));
    }

    private int getLocalKeyWordSize(){
        return keywordRecyclerView.getKeywordList().size();
    }

    @Override
    public void onKeywordRemoved() {
        if (! buttonSave.isEnabled()) {
            buttonSave.setEnabled(true);
        }
        setCurrentMaxKeyword();
    }

    private void checkAddButtonEnabled(){
        String currentKeywordString = editTextKeyword.getText().toString();
        if (TextUtils.isEmpty(currentKeywordString) && buttonAddKeyword.isEnabled()) {
            buttonAddKeyword.setEnabled (false);
        } else if (!TextUtils.isEmpty(currentKeywordString) && !buttonAddKeyword.isEnabled()) {
            buttonAddKeyword.setEnabled (true);
        }
    }

    public boolean isButtonSaveEnabled(){
        return buttonSave.isEnabled();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(EXTRA_LOCAL_WORDS, keywordRecyclerView.getKeywordList());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onSaveKeywordListener = (OnSaveKeywordListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSaveKeywordListener = null;
    }
}
