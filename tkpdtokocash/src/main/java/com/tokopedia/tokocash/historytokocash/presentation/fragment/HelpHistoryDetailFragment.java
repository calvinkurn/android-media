package com.tokopedia.tokocash.historytokocash.presentation.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.di.TokoCashComponent;
import com.tokopedia.tokocash.historytokocash.presentation.activity.HelpHistoryDetailActivity;
import com.tokopedia.tokocash.historytokocash.presentation.adapter.HelpHistoryAdapter;
import com.tokopedia.tokocash.historytokocash.presentation.contract.HelpHistoryDetailContract;
import com.tokopedia.tokocash.historytokocash.presentation.model.HelpHistoryTokoCash;
import com.tokopedia.tokocash.historytokocash.presentation.presenter.HelpHistoryDetailPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 10/16/17.
 */

public class HelpHistoryDetailFragment extends BaseDaggerFragment implements HelpHistoryDetailContract.View {

    private static final int MIN_CHAR_HELP_DETAIL = 30;

    private Spinner spinnerHistoryHelp;
    private EditText historyHelpDetail;
    private Button cancelHelp;
    private Button sendHelp;

    private String selectedCategory;
    private String transactionId;
    private int positionCategorySelected;
    private HelpHistoryAdapter adapter;
    private ProgressBar progressBar;
    private ActionListener listener;

    @Inject
    HelpHistoryDetailPresenter presenter;

    public static HelpHistoryDetailFragment newInstance(String transactionId) {
        HelpHistoryDetailFragment fragment = new HelpHistoryDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(HelpHistoryDetailActivity.TRANSACTION_ID, transactionId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_history_detail, container, false);
        spinnerHistoryHelp = view.findViewById(R.id.spinner_history_help);
        historyHelpDetail = view.findViewById(R.id.history_help_detail);
        cancelHelp = view.findViewById(R.id.cancel_send_help);
        sendHelp = view.findViewById(R.id.send_help);
        progressBar = view.findViewById(R.id.progress_bar_help);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInjector();
        presenter.attachView(this);
        transactionId = getArguments().getString(HelpHistoryDetailActivity.TRANSACTION_ID);

        selectedCategory = "";
        presenter.getHelpCategoryHistory();
    }


    @Override
    public void loadHelpHistoryData(final List<HelpHistoryTokoCash> helpHistoryTokoCashes) {
        adapter = new HelpHistoryAdapter(getActivity(), android.R.layout.simple_spinner_item, helpHistoryTokoCashes);
        spinnerHistoryHelp.setAdapter(adapter);
        spinnerHistoryHelp.setSelection(adapter.getCount());
        spinnerHistoryHelp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                spinnerHistoryHelp.setSelection(position);
                positionCategorySelected = position;
                selectedCategory = helpHistoryTokoCashes.get(position).getTranslation();
                setValidationButtonSendHelp();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cancelHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBackToHome();
            }
        });

        sendHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.submitHelpHistory(getString(R.string.tokocash), historyHelpDetail.getText().toString(), selectedCategory, transactionId);
            }
        });

        historyHelpDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setValidationButtonSendHelp();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean isValidForm() {
        return positionCategorySelected != adapter.getCount() && historyHelpDetail.length() > MIN_CHAR_HELP_DETAIL;
    }

    private void setValidationButtonSendHelp() {
        if (isValidForm()) {
            sendHelp.setEnabled(true);
            sendHelp.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        } else {
            sendHelp.setEnabled(false);
            sendHelp.setTextColor(ContextCompat.getColor(getActivity(), R.color.digital_voucher_notes));
        }
    }

    @Override
    public void successSubmitHelpHistory() {
        Toast.makeText(getActivity(), getString(R.string.success_message_send_help), Toast.LENGTH_SHORT).show();
        listener.successSubmitHelp();
    }

    @Override
    public void showErrorHelpHistory(Throwable throwable) {
        String errorMessage = ErrorHandler.getErrorMessage(getActivity(), throwable);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void showProgressLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressLoading() {
        if (progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(TokoCashComponent.class).inject(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ActionListener) activity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroyView();
    }

    public interface ActionListener {
        void successSubmitHelp();

        void onBackToHome();
    }
}
