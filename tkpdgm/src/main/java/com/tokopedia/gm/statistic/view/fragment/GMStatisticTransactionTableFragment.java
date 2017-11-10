package com.tokopedia.gm.statistic.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.customadapter.NoResultDataBinder;
import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.gm.common.di.component.GMComponent;
import com.tokopedia.gm.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.emptydatabinder.EmptyDataBinder;
import com.tokopedia.seller.base.view.fragment.BaseListDateFragment;
import com.tokopedia.seller.common.bottomsheet.BottomSheetBuilder;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.gm.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.gm.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.gm.statistic.di.component.DaggerGMStatisticTransactionComponent;
import com.tokopedia.gm.statistic.di.module.GMStatisticModule;
import com.tokopedia.gm.statistic.utils.GMStatisticDateUtils;
import com.tokopedia.seller.common.williamchart.util.GMStatisticUtil;
import com.tokopedia.gm.statistic.view.adapter.GMStatRetryDataBinder;
import com.tokopedia.gm.statistic.view.adapter.GMStatisticTransactionTableAdapter;
import com.tokopedia.gm.statistic.view.adapter.model.GMStatisticTransactionTableModel;
import com.tokopedia.seller.common.bottomsheet.custom.CheckedBottomSheetBuilder;
import com.tokopedia.gm.statistic.view.listener.GMStatisticTransactionTableView;
import com.tokopedia.gm.statistic.view.presenter.GMStatisticTransactionTablePresenter;

import java.util.Date;

import javax.inject.Inject;

/**
 * @author normansyahputa on 7/13/17.
 */
public class GMStatisticTransactionTableFragment extends BaseListDateFragment<GMStatisticTransactionTableModel>
        implements GMStatisticTransactionTableView {

    public static final int START_PAGE = 0;
    @Inject
    GMStatisticTransactionTablePresenter transactionTablePresenter;
    @GMTransactionTableSortBy
    int sortBy = GMTransactionTableSortBy.DELIVERED_AMT; // default to Pendapatan Bersih
    @GMTransactionTableSortType
    int sortType = GMTransactionTableSortType.DESCENDING; // this is for DESCENDING default
    private TextView tvSortBy;
    private String[] gmStatSortBy;
    private boolean[] sortBySelections;
    private int sortByIndexSelection = 1; // default to Pendapatan Bersih
    private String[] gmStatSortType;
    private boolean[] sortTypeSelections;
    private int sortTypeIndexSelection = 0; // this is for DESCENDING default

    public static Fragment createInstance() {
        GMStatisticTransactionTableFragment fragment = new GMStatisticTransactionTableFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initInjector() {
        DaggerGMStatisticTransactionComponent
                .builder()
                .gMComponent(getComponent(GMComponent.class))
                .gMStatisticModule(new GMStatisticModule())
                .build().inject(this);
        transactionTablePresenter.attachView(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        LinearLayout sortByContainer = (LinearLayout) view.findViewById(R.id.sort_by_container);
        sortByContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortBy();
            }
        });
        tvSortBy = (TextView) sortByContainer.findViewById(R.id.sort_by_text);

        gmStatSortBy = getResources().getStringArray(R.array.gm_stat_sort_by);
        sortBySelections = new boolean[gmStatSortBy.length];
        sortBySelections[sortByIndexSelection] = true;

        tvSortBy.setText(gmStatSortBy[sortByIndexSelection]);

        gmStatSortType = getResources().getStringArray(R.array.gm_stat_sort_type);
        sortTypeSelections = new boolean[gmStatSortType.length];
        if (sortTypeIndexSelection > -1) {
            sortTypeSelections[sortTypeIndexSelection] = true;
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_gm_statistic_transaction_table_list;
    }

    @Override
    public void loadDataByDateAndPage(DatePickerViewModel datePickerViewModel, int page) {
        transactionTablePresenter.loadData(
                new Date(datePickerViewModel.getStartDate()),
                new Date(datePickerViewModel.getEndDate()),
                sortType,
                sortBy,
                page);
    }

    @Override
    public Intent getDatePickerIntent(DatePickerViewModel datePickerViewModel) {
        return GMStatisticDateUtils.getDatePickerIntent(getActivity(), datePickerViewModel);
    }

    @Override
    public DatePickerViewModel getDefaultDateViewModel() {
        return GMStatisticDateUtils.getDefaultDatePickerViewModel();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_gm_statistic_transaction_table, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sort) {
            showSortType();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(GMStatisticTransactionTableModel gmStatisticTransactionTableModel) {
        // Do nothing
    }

    @Override
    protected BaseListAdapter getNewAdapter() {
        GMStatisticTransactionTableAdapter adapter = new GMStatisticTransactionTableAdapter();
        adapter.setSortBy(sortBy);
        return adapter;
    }

    @Override
    protected NoResultDataBinder getEmptyViewDefaultBinder() {
        EmptyDataBinder emptyTransactionDataBinder = new EmptyDataBinder(adapter, R.drawable.ic_transaction_table_empty);
        emptyTransactionDataBinder.setEmptyTitleText(null);
        emptyTransactionDataBinder.setEmptyContentText(getString(R.string.gm_statistic_transaction_table_no_data));
        emptyTransactionDataBinder.setEmptyContentItemText(null);
        return emptyTransactionDataBinder;
    }

    @Override
    protected NoResultDataBinder getEmptyViewNoResultBinder() {
        EmptyDataBinder emptyTransactionDataBinder = new EmptyDataBinder(adapter, R.drawable.ic_transaction_table_empty);
        emptyTransactionDataBinder.setEmptyTitleText(null);
        emptyTransactionDataBinder.setEmptyContentText(getString(R.string.gm_statistic_transaction_table_no_data));
        emptyTransactionDataBinder.setEmptyContentItemText(null);
        return emptyTransactionDataBinder;
    }

    @Override
    public RetryDataBinder getRetryViewDataBinder(BaseListAdapter adapter) {
        return new GMStatRetryDataBinder(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        transactionTablePresenter.detachView();
    }

    private void showSortBy() {
        showBottomSheetDialog(getString(R.string.filter), gmStatSortBy, sortBySelections, new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                int previousSortBy = sortBy;
                switch (sortByIndexSelection = GMStatisticUtil.findSelection(gmStatSortBy, item.getTitle().toString())) {
                    case 0:
                        sortBy = GMTransactionTableSortBy.TRANS_SUM;
                        break;
                    default:
                    case 1:
                        sortBy = GMTransactionTableSortBy.DELIVERED_AMT;
                        break;
                }
                if (previousSortBy == sortBy) {
                    return;
                }
                String itemTitle = item.getTitle().toString();

                resetSelectionSortBy(sortByIndexSelection);
                tvSortBy.setText(itemTitle);
                GMStatisticTransactionTableAdapter gmAdapter = (GMStatisticTransactionTableAdapter)adapter;
                gmAdapter.setSortBy(sortBy);
                gmAdapter.notifyDataSetChanged();

                loadData();

                UnifyTracking.eventClickGMStatFilterTypeProductSold(itemTitle);
            }
        });
    }

    private void showSortType() {
        showBottomSheetDialog(getString(R.string.gm_statistic_sort), gmStatSortType, sortTypeSelections, new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem menuItem) {
                int previousSortType = sortType;
                switch (sortTypeIndexSelection = GMStatisticUtil.findSelection(gmStatSortType, menuItem.getTitle().toString())) {
                    case 0:
                        sortType = GMTransactionTableSortType.DESCENDING;
                        break;
                    case 1:
                        sortType = GMTransactionTableSortType.ASCENDING;
                        break;
                    default:
                        sortType = -1;
                        break;
                }
                if (previousSortType == sortType) {
                    return;
                }
                resetSelectionSortType(sortTypeIndexSelection);

                // we load data, instead search data to reset the page to 1
                loadData();
            }
        });
    }

    private void showBottomSheetDialog(String bottomDialogTitle, final String[] text, final boolean[] selections, BottomSheetItemClickListener bottomSheetItemClickListener) {
        BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(bottomDialogTitle);

        for (int i = 0; i < text.length; i++) {
            if (bottomSheetBuilder instanceof CheckedBottomSheetBuilder) {
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(i, text[i], null, selections[i]);
            } else {
                bottomSheetBuilder.addItem(i, text[i], null);
            }
        }

        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(bottomSheetItemClickListener)
                .createDialog();
        bottomSheetDialog.show();
    }

    private void resetSelectionSortType(int newSelection) {
        for (int i = 0; i < sortTypeSelections.length; i++) {
            sortTypeSelections[i] = i == newSelection;
        }
    }

    private void resetSelectionSortBy(int newSelection) {
        for (int i = 0; i < sortBySelections.length; i++) {
            sortBySelections[i] = i == newSelection;
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected int getStartPage() {
        return START_PAGE;
    }
}