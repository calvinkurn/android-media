package com.tokopedia.tokocash.historytokocash.presentation.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.router.digitalmodule.sellermodule.TokoCashRouter;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.design.quickfilter.QuickFilterAdapter;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.historytokocash.data.mapper.FilterHistoryTokoCashMapper;
import com.tokopedia.tokocash.di.DaggerTokoCashComponent;
import com.tokopedia.tokocash.di.TokoCashComponent;
import com.tokopedia.tokocash.historytokocash.domain.GetHistoryDataUseCase;
import com.tokopedia.tokocash.historytokocash.presentation.DatePickerTokoCashUtil;
import com.tokopedia.tokocash.historytokocash.presentation.adapter.HistoryTokoCashAdapter;
import com.tokopedia.tokocash.historytokocash.presentation.contract.TokoCashHistoryContract;
import com.tokopedia.tokocash.historytokocash.presentation.model.HeaderHistory;
import com.tokopedia.tokocash.historytokocash.presentation.model.ItemHistory;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;
import com.tokopedia.tokocash.historytokocash.presentation.presenter.TokoCashHistoryPresenter;
import com.tokopedia.usecase.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 12/27/17.
 */

public class HistoryTokoCashActivity extends TActivity implements TokoCashHistoryContract.View,
        HasComponent<TokoCashComponent> {

    private static final int SELECTION_TYPE_PERIOD_DATE = 0;
    private static final int EXTRA_INTENT_DATE_PICKER = 50;
    private static final String FORMAT_DATE = "dd+MMM+yyyy";
    private static final String ALL_TRANSACTION_TYPE = "all";
    private static final String EXTRA_START_DATE = "EXTRA_START_DATE";
    private static final String EXTRA_END_DATE = "EXTRA_END_DATE";
    private static final String EXTRA_SELECTION_PERIOD = "EXTRA_SELECTION_PERIOD";
    private static final String EXTRA_SELECTION_TYPE = "EXTRA_SELECTION_TYPE";
    private static final String STATE_DATA_UPDATED = "state_data_updated";
    private static final String STATE_SAVED = "saved";

    private LinearLayout layoutDate;
    private TextView tvDate;
    private RecyclerView filterHistoryRecyclerView;
    private RecyclerView historyListRecyclerView;
    private RelativeLayout viewEmptyNoHistory;
    private LinearLayout mainContent;
    private TextView emptyTextTransaction;
    private ImageView emptyIconTransaction;
    private TextView emptyDescTransaction;
    private CoordinatorLayout rootView;
    private LinearLayout waitingTransactionView;
    private NestedScrollView nestedScrollView;
    private BottomActionView bottomActionView;

    private int datePickerSelection = 2;
    private int datePickerType = 0;
    private String typeFilterSelected = "";
    private String startDateFormatted = "";
    private String endDateFormatted = "";
    private boolean isLoadMore;
    private long startDate;
    private long endDate;
    private QuickFilterAdapter adapterFilter;
    private HistoryTokoCashAdapter adapterHistory;
    private SnackbarRetry messageSnackbar;
    private RefreshHandler refreshHandler;
    private String stateDataAfterFilter = "";

    private TokoCashHistoryData tokoCashHistoryData;
    private TokoCashComponent tokoCashComponent;
    private int oldScrollY = 0;

    @Inject
    TokoCashHistoryPresenter presenter;
    @Inject
    FilterHistoryTokoCashMapper headerMapper;


    @SuppressWarnings("unused")
    @DeepLink(Constants.Applinks.WALLET_TRANSACTION_HISTORY)
    public static Intent getcallingIntent(Context context, Bundle extras) {
        return HistoryTokoCashActivity.newInstance(context);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, HistoryTokoCashActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_history_tokocash);
        initInjector();
        presenter.attachView(this);
        initView();
        initVar();
        setActionVar();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_DATA_UPDATED, stateDataAfterFilter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        this.stateDataAfterFilter = savedInstanceState.getString(STATE_DATA_UPDATED);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stateDataAfterFilter.equals("")) {
            refreshHandler.startRefresh();
        }
        presenter.getWaitingTransaction();
    }

    private void initView() {
        layoutDate = (LinearLayout) findViewById(R.id.date_label_view);
        tvDate = (TextView) findViewById(R.id.text_view_date);
        filterHistoryRecyclerView = (RecyclerView) findViewById(R.id.filter_history_tokocash);
        historyListRecyclerView = (RecyclerView) findViewById(R.id.history_tokocash);
        viewEmptyNoHistory = (RelativeLayout) findViewById(R.id.view_empty_no_history);
        mainContent = (LinearLayout) findViewById(R.id.main_content);
        emptyTextTransaction = (TextView) findViewById(R.id.text_empty_page);
        emptyIconTransaction = (ImageView) findViewById(R.id.icon_empty_page);
        emptyDescTransaction = (TextView) findViewById(R.id.desc_empty_page);
        rootView = (CoordinatorLayout) findViewById(R.id.root_view);
        waitingTransactionView = (LinearLayout) findViewById(R.id.waiting_transaction_view);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        bottomActionView = (BottomActionView) findViewById(R.id.bottom_action_view);
    }

    private void initVar() {
        toolbar.setTitle(getString(R.string.title_menu_history_tokocash));
        initialRangeDateFilter();
        initialFilterRecyclerView();
        initialHistoryRecyclerView();

        refreshHandler = new RefreshHandler(this, getWindow().getDecorView().getRootView(),
                new RefreshHandler.OnRefreshHandlerListener() {
                    @Override
                    public void onRefresh(View view) {
                        if (refreshHandler.isRefreshing()) {
                            presenter.getInitHistoryTokoCash();
                        }
                    }
                });
    }

    private void setActionVar() {
        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Application application = HistoryTokoCashActivity.this.getApplication();
                if (application != null && application instanceof TokoCashRouter) {
                    Intent intent = ((TokoCashRouter) application)
                            .goToDatePicker(HistoryTokoCashActivity.this,
                                    DatePickerTokoCashUtil.getPeriodRangeModel(getApplicationContext()),
                                    startDate, endDate, datePickerSelection, datePickerType);
                    startActivityForResult(intent, EXTRA_INTENT_DATE_PICKER);
                    stateDataAfterFilter = STATE_SAVED;
                }
            }
        });

        bottomActionView.setButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nestedScrollView.scrollTo(0, 0);
            }
        });

        waitingTransactionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO arahin ke waiting
            }
        });

        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(
                new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        showBottomActionWhenScrollingUp();
                        loadMoreContentWhenReachBottom();
                    }
                }
        );
    }

    private void initialRangeDateFilter() {
        Application application = HistoryTokoCashActivity.this.getApplication();
        if (application != null && application instanceof TokoCashRouter) {
            Calendar startCalendar = Calendar.getInstance();
            Calendar endCalendar = Calendar.getInstance();
            startCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
            startCalendar.add(Calendar.DATE, -29);
            startDate = startCalendar.getTimeInMillis();
            endDate = endCalendar.getTimeInMillis();
            String getFormattedDatePicker =
                    ((TokoCashRouter) application)
                            .getRangeDateFormatted(HistoryTokoCashActivity.this, startDate, endDate);
            tvDate.setText(getFormattedDatePicker);
        }
    }

    private void initialFilterRecyclerView() {
        filterHistoryRecyclerView.setHasFixedSize(true);
        filterHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        adapterFilter = new QuickFilterAdapter();
        filterHistoryRecyclerView.setAdapter(adapterFilter);
    }

    private void initialHistoryRecyclerView() {
        adapterHistory = new HistoryTokoCashAdapter(new ArrayList<ItemHistory>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        historyListRecyclerView.setHasFixedSize(true);
        historyListRecyclerView.setLayoutManager(linearLayoutManager);
        historyListRecyclerView.setNestedScrollingEnabled(false);
        historyListRecyclerView.setAdapter(adapterHistory);
    }

    @NonNull
    private void showBottomActionWhenScrollingUp() {
        if (nestedScrollView.getScrollY() < oldScrollY && nestedScrollView.getScrollY() != 0) {
            bottomActionView.setVisibility(View.VISIBLE);
        } else {
            bottomActionView.setVisibility(View.GONE);
        }
        oldScrollY = nestedScrollView.getScrollY();
    }

    /**
     * Using nestedscrollview because this page contain swipe refresh & endless scroll view
     */
    @NonNull
    private void loadMoreContentWhenReachBottom() {
        View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));

        if (diff == 0) {
            if (isLoadMore) {
                presenter.getHistoryLoadMore();
            }
        }
    }

    @Override
    public void hideLoading() {
        if (refreshHandler != null && refreshHandler.isRefreshing())
            refreshHandler.finishRefresh();
    }

    @Override
    public void setHasNextPage(boolean hasNextPage) {
        isLoadMore = hasNextPage;
        adapterHistory.isNextUri(hasNextPage);
    }

    @Override
    public void renderDataTokoCashHistory(TokoCashHistoryData tokoCashHistoryData, boolean firstTimeLoad) {
        mainContent.setVisibility(View.VISIBLE);
        layoutDate.setVisibility(View.VISIBLE);
        filterHistoryRecyclerView.setVisibility(View.VISIBLE);
        historyListRecyclerView.setVisibility(View.VISIBLE);
        viewEmptyNoHistory.setVisibility(View.GONE);

        refreshHandler.finishRefresh();
        adapterFilter.setListener(getFilterTokoCashListener());
        adapterFilter.addQuickFilterItems(headerMapper.transform(
                removeTypeAllOnHeader(tokoCashHistoryData.getHeaderHistory())));
        adapterHistory.setListener(getItemHistoryListener());
        if (firstTimeLoad) {
            adapterHistory.addItemHistoryList(tokoCashHistoryData.getItemHistoryList());
        } else if (isLoadMore) {
            adapterHistory.addItemHistoryListLoadMore(tokoCashHistoryData.getItemHistoryList());
        }
    }

    @NonNull
    private List<HeaderHistory> removeTypeAllOnHeader(List<HeaderHistory> filterList) {
        for (int i = filterList.size() - 1; i > -1; i--) {
            if (filterList.get(i).getType().equals(ALL_TRANSACTION_TYPE)) {
                filterList.remove(filterList.get(i));
            }
        }
        return filterList;
    }

    @NonNull
    private QuickFilterAdapter.ActionListener getFilterTokoCashListener() {
        return new QuickFilterAdapter.ActionListener() {
            @Override
            public void clearFilter() {
                typeFilterSelected = "";
                refreshHandler.startRefresh();
            }

            @Override
            public void selectFilter(String typeFilter) {
                typeFilterSelected = typeFilter;
                presenter.getInitHistoryTokoCash();
            }
        };
    }

    @NonNull
    private HistoryTokoCashAdapter.ItemHistoryListener getItemHistoryListener() {
        return new HistoryTokoCashAdapter.ItemHistoryListener() {
            @Override
            public void onClickItemHistory(ItemHistory itemHistory) {
                //TODO masuk ke detail
            }
        };
    }

    @Override
    public void renderEmptyTokoCashHistory(List<HeaderHistory> headerHistoryList) {
        layoutDate.setVisibility(View.VISIBLE);
        filterHistoryRecyclerView.setVisibility(View.VISIBLE);
        historyListRecyclerView.setVisibility(View.GONE);
        viewEmptyNoHistory.setVisibility(View.VISIBLE);
        bottomActionView.setVisibility(View.GONE);

        refreshHandler.finishRefresh();
        for (HeaderHistory header : headerHistoryList) {
            if (header.isSelected()) {
                if (!header.getType().equals(ALL_TRANSACTION_TYPE)) {
                    emptyIconTransaction.setImageDrawable(ContextCompat.getDrawable(this,
                            R.drawable.ic_tokocash_no_history_category));
                    emptyDescTransaction.setText(getString(R.string.desc_empty_page_tokocash));
                    emptyTextTransaction.setText(getString(R.string.message_no_transaction) + " " + header.getName());
                } else {
                    emptyIconTransaction.setImageDrawable(ContextCompat.getDrawable(this,
                            R.drawable.ic_tokocash_no_transaction));
                    emptyDescTransaction.setText(getString(R.string.desc_empty_page_no_transaction_tokocash));
                    emptyTextTransaction.setText(getString(R.string.message_no_transaction));
                }
            }
        }
    }

    @Override
    public void renderErrorMessage(String message) {
        if (messageSnackbar == null) {
            messageSnackbar = NetworkErrorHelper.createSnackbarWithAction(this, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.getHistoryLoadMore();
                }
            });
        }
        messageSnackbar.showRetrySnackbar();
    }

    @Override
    public void renderEmptyPage(String message) {
        NetworkErrorHelper.showEmptyState(
                getApplicationContext(), rootView, message, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        refreshHandler.startRefresh();
                    }
                }
        );
    }

    @Override
    public void renderWaitingTransaction(TokoCashHistoryData tokoCashHistoryData) {
        this.tokoCashHistoryData = tokoCashHistoryData;
        waitingTransactionView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideWaitingTransaction() {
        waitingTransactionView.setVisibility(View.GONE);
    }

    @Override
    public RequestParams getHistoryTokoCashParam(boolean isWaitingTransaction, int page) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetHistoryDataUseCase.TYPE,
                isWaitingTransaction ? "pending" : typeFilterSelected);
        requestParams.putString(GetHistoryDataUseCase.START_DATE,
                isWaitingTransaction ? "" : startDateFormatted);
        requestParams.putString(GetHistoryDataUseCase.END_DATE,
                isWaitingTransaction ? "" : endDateFormatted);
        requestParams.putInt(GetHistoryDataUseCase.PAGE, page);
        return requestParams;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == EXTRA_INTENT_DATE_PICKER && intent != null) {
            Application application = HistoryTokoCashActivity.this.getApplication();
            if (application != null && application instanceof TokoCashRouter) {
                startDate = intent.getLongExtra(EXTRA_START_DATE, -1);
                endDate = intent.getLongExtra(EXTRA_END_DATE, -1);
                datePickerSelection = intent.getIntExtra(EXTRA_SELECTION_PERIOD, 1);
                datePickerType = intent.getIntExtra(EXTRA_SELECTION_TYPE, SELECTION_TYPE_PERIOD_DATE);
                String getFormattedDatePicker =
                        ((TokoCashRouter) application)
                                .getRangeDateFormatted(HistoryTokoCashActivity.this,
                                        startDate, endDate);
                tvDate.setText(getFormattedDatePicker);

                startDateFormatted = new SimpleDateFormat(FORMAT_DATE, Locale.ENGLISH).format(startDate);
                endDateFormatted = new SimpleDateFormat(FORMAT_DATE, Locale.ENGLISH).format(endDate);
                refreshHandler.startRefresh();
            }
        }
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public TokoCashComponent getComponent() {
        if (tokoCashComponent == null) initInjector();
        return tokoCashComponent;
    }

    private void initInjector() {
        tokoCashComponent = DaggerTokoCashComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
        tokoCashComponent.inject(this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyPresenter();
        super.onDestroy();
    }
}