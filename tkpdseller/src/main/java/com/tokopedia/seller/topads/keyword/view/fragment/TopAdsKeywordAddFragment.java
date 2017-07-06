package com.tokopedia.seller.topads.keyword.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.core.network.retrofit.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.response.Error;
import com.tokopedia.core.network.retrofit.response.TextErrorObject;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.utils.ViewUtils;
import com.tokopedia.seller.topads.keyword.di.component.DaggerTopAdsKeywordAddComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsKeywordAddModule;
import com.tokopedia.seller.topads.keyword.view.adapter.KeywordAdapter;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsKeywordAddView;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordAddPresenter;
import com.tokopedia.seller.topads.keyword.view.widget.KeywordRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by hendry on 5/18/2017.
 */

public class TopAdsKeywordAddFragment extends BaseDaggerFragment
        implements KeywordAdapter.OnKeywordAdapterListener, TopAdsKeywordAddView {

    public static final String TAG = TopAdsKeywordAddFragment.class.getSimpleName();

    @Inject
    TopAdsKeywordAddPresenter topAdsKeywordAddPresenter;

    public static final String EXTRA_GROUP_ID = "grp_id";
    public static final String EXTRA_KEYWORD_TYPE = "keyword_typ";
    public static final String EXTRA_SERVER_COUNT = "server_cnt";
    public static final String EXTRA_MAX_WORDS = "max_words";
    public static final String EXTRA_LOCAL_WORDS = "lcl_wrds";
    public static final String EXTRA_ERROR_WORDS = "err_wrds";

    public static final int MIN_WORDS = 5;

    OnSuccessSaveKeywordListener onSuccessSaveListener;
    private ArrayList<String> errorStringList = new ArrayList<>();

    public interface OnSuccessSaveKeywordListener {
        void onSuccessSave(ArrayList<String> keyWordsList);
    }

    private ArrayList<String> localWords = new ArrayList<>();
    private int serverCount;
    private int maxKeyword;
    private String groupId;
    private int keywordType;
    private KeywordRecyclerView keywordRecyclerView;
    private View buttonAddKeyword;
    private TextInputLayout textInputKeyword;
    private EditText editTextKeyword;
    private TextView textViewKeywordCurrentMax;
    private TextView textViewTotalKeyworGroup;
    private View buttonSave;
    private TkpdProgressDialog progressDialog;

    public static TopAdsKeywordAddFragment newInstance(String groupId,
                                                       int keywordType,
                                                       int serverCount,
                                                       int maxWords,
                                                       ArrayList<String> localWords) {
        TopAdsKeywordAddFragment fragment = new TopAdsKeywordAddFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_GROUP_ID, groupId);
        args.putInt(EXTRA_KEYWORD_TYPE, keywordType);
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
        groupId = args.getString(EXTRA_GROUP_ID);
        keywordType = args.getInt(EXTRA_KEYWORD_TYPE);
        serverCount = args.getInt(EXTRA_SERVER_COUNT);
        maxKeyword = args.getInt(EXTRA_MAX_WORDS);
        if (savedInstanceState == null) {
            localWords = args.getStringArrayList(EXTRA_LOCAL_WORDS);
        } else {
            localWords = savedInstanceState.getStringArrayList(EXTRA_LOCAL_WORDS);
            errorStringList = savedInstanceState.getStringArrayList(EXTRA_ERROR_WORDS);
        }
    }

    @Override
    protected void initInjector() {
        DaggerTopAdsKeywordAddComponent.builder()
                .topAdsKeywordAddModule(new TopAdsKeywordAddModule())
                .appComponent(getComponent(AppComponent.class))
                .build()
                .inject(this);
        topAdsKeywordAddPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_top_ads_keyword_add, container, false);
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
                // check if contain alphanumeric
                String keyword = editable.toString();
                if (!hasInvalidChar(keyword)) {
                    String validKeyword = StringUtils.omitPunctuationAndDoubleSpace(keyword).toLowerCase();
                    if (!TextUtils.isEmpty(validKeyword) && isValidateWordAndCheckLocalSuccess(validKeyword)) {
                        textInputKeyword.setErrorEnabled(false);
                    }
                }
                checkAddButtonEnabled();
            }
        });
        editTextKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onButtonAddClicked();
                    return true;
                }
                return false;
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

    private void onButtonSaveClicked() {
        showLoading();
        topAdsKeywordAddPresenter.addKeyword(groupId, keywordType, keywordRecyclerView.getKeywordList());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topAdsKeywordAddPresenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onSuccessSaveKeyword() {
        hideLoading();
        CommonUtils.UniversalToast(getActivity(),
                getString(R.string.top_ads_keyword_has_been_added));
        if (onSuccessSaveListener != null) {
            onSuccessSaveListener.onSuccessSave(keywordRecyclerView.getKeywordList());
        }
    }

    @Override
    public void onFailedSaveKeyword(Throwable e) {
        hideLoading();
        CommonUtils.UniversalToast(getActivity(), ViewUtils.getErrorMessage(getContext(), e));
        if (e instanceof ResponseErrorException) {
            List<Error> errorList = ((ResponseErrorException) e).getErrorList();
            errorStringList = convertResponseErrorToErrorStringList(errorList);
            keywordRecyclerView.setErrorKeywordList(errorStringList);
        }
    }

    private ArrayList<String> convertResponseErrorToErrorStringList(List<Error> errorList) {
        ArrayList<String> tempErrorStringList = new ArrayList<>();
        if (errorList == null || errorList.size() == 0) {
            return tempErrorStringList;
        }
        for (int i = 0, sizei = errorList.size(); i < sizei; i++) {
            List<TextErrorObject> textErrorObjectList = errorList.get(i).getObjectListParse(TextErrorObject[].class);
            if (textErrorObjectList != null && textErrorObjectList.size() > 0) {
                for (int j=0, sizej= textErrorObjectList.size(); j <sizej ; j++) {
                    tempErrorStringList.add(textErrorObjectList.get(j).getText());
                }
            }
        }
        return tempErrorStringList;
    }

    private void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void showLoading() {
        if (progressDialog == null) {
            progressDialog = new TkpdProgressDialog(getActivity(),
                    TkpdProgressDialog.NORMAL_PROGRESS);
        }
        progressDialog.showDialog();
    }

    private void onButtonAddClicked() {
        // check if max words has been reached
        if (getLocalKeyWordSize() == maxKeyword) {
            textInputKeyword.setError(getString(R.string.top_ads_keyword_per_group_reach_limit));
            return;
        }

        String keyword = editTextKeyword.getText().toString();

        // check if contain alphanumeric
        if (hasInvalidChar(keyword)) {
            return;
        }

        String validKeyword = StringUtils.omitPunctuationAndDoubleSpace(keyword);
        if (TextUtils.isEmpty(validKeyword)) {
            textInputKeyword.setError(getString(R.string.top_ads_keyword_must_be_filled));
            return;
        }

        if (!isValidateWordAndCheckLocalSuccess(validKeyword)) {
            return;
        }

        textInputKeyword.setErrorEnabled(false);

        // add to recyclerview
        keywordRecyclerView.addKeyword(validKeyword);
        editTextKeyword.setText("");
        if (!buttonSave.isEnabled()) {
            buttonSave.setEnabled(true);
        }
        setCurrentMaxKeyword();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // START VALIDATOR
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private boolean isValidateWordAndCheckLocalSuccess(String validKeyword) {
        // validate no more 5 words
        if (!hasValidWordLength(validKeyword)) {
            return false;
        }

        // validate if keyword has existed in local
        if (keywordAlreadyInLocal(validKeyword)) {
            return false;
        }
        return true;
    }

    private boolean hasInvalidChar(String keyword) {
        // check if contain alphanumeric
        if (StringUtils.containNonSpaceAlphaNumeric(keyword)) {
            textInputKeyword.setError(getString(R.string.top_ads_keyword_must_only_letter_or_digit));
            return true;
        }
        return false;
    }

    private boolean hasValidWordLength(String validKeyword) {
        String words[] = validKeyword.split("\\s|\\n", MIN_WORDS + 1);
        if (words.length > MIN_WORDS) {
            textInputKeyword.setError(getString(R.string.top_ads_keyword_cannot_more_than_5));
            return false;
        }
        return true;
    }

    private boolean keywordAlreadyInLocal(String validKeyword) {
        List<String> keywordList = keywordRecyclerView.getKeywordList();
        boolean hasInputtedInLocal = false;
        for (int i = 0, sizei = keywordList.size(); i < sizei; i++) {
            if (validKeyword.equalsIgnoreCase(keywordList.get(i))) {
                hasInputtedInLocal = true;
                break;
            }
        }

        if (hasInputtedInLocal) {
            textInputKeyword.setError(getString(R.string.top_ads_keyword_has_existed));
            return true;
        }
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // END VALIDATOR
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        keywordRecyclerView.setKeywordList(localWords);
        keywordRecyclerView.setErrorKeywordList(errorStringList);
        setCurrentMaxKeyword();
        setServerKeyword();
        checkAddButtonEnabled();
    }

    private void setCurrentMaxKeyword() {
        textViewKeywordCurrentMax.setText(getString(R.string.top_ads_keywords_total_current_and_max,
                getLocalKeyWordSize(),
                maxKeyword));
    }

    private void setServerKeyword() {
        textViewTotalKeyworGroup.setText(getString(R.string.top_ads_keywords_in_groups, serverCount));
    }

    private int getLocalKeyWordSize() {
        return keywordRecyclerView.getKeywordList().size();
    }

    @Override
    public void onKeywordRemoved() {
        if (getLocalKeyWordSize() == 0) {
            buttonSave.setEnabled(false);
        }
        setCurrentMaxKeyword();
    }

    private void checkAddButtonEnabled() {
        String currentKeywordString = editTextKeyword.getText().toString();
        if (TextUtils.isEmpty(currentKeywordString) && buttonAddKeyword.isEnabled()) {
            buttonAddKeyword.setEnabled(false);
        } else if (!TextUtils.isEmpty(currentKeywordString) && !buttonAddKeyword.isEnabled()) {
            buttonAddKeyword.setEnabled(true);
        }
    }

    public boolean isButtonSaveEnabled() {
        return buttonSave.isEnabled();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(EXTRA_LOCAL_WORDS, keywordRecyclerView.getKeywordList());
        outState.putStringArrayList(EXTRA_ERROR_WORDS, errorStringList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onSuccessSaveListener = (OnSuccessSaveKeywordListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSuccessSaveListener = null;
    }
}
