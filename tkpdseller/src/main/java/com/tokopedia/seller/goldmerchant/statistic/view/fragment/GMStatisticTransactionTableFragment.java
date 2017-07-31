package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListDateFragment;
import com.tokopedia.seller.common.bottomsheet.BottomSheetBuilder;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.seller.goldmerchant.statistic.di.component.DaggerGMTransactionComponent;
import com.tokopedia.seller.goldmerchant.statistic.di.module.GMStatisticModule;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticDateUtils;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.view.adapter.GMStatisticTransactionTableAdapter;
import com.tokopedia.seller.goldmerchant.statistic.view.adapter.model.GMStatisticTransactionTableModel;
import com.tokopedia.seller.goldmerchant.statistic.view.builder.CheckedBottomSheetBuilder;
import com.tokopedia.seller.goldmerchant.statistic.view.listener.GMStatisticTransactionTableView;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionTablePresenter;

import java.util.Date;

import javax.inject.Inject;

/**
 * @author normansyahputa on 7/13/17.
 */
public class GMStatisticTransactionTableFragment extends BaseListDateFragment<GMStatisticTransactionTableModel>
        implements GMStatisticTransactionTableView {
    public static final String TAG = "GMStatisticTransactionT";
    public static final int START_PAGE = 0;

    @Inject
    GMStatisticTransactionTablePresenter transactionTablePresenter;

    @GMTransactionTableSortBy
    int sortBy = GMTransactionTableSortBy.DELIVERED_AMT; // default to Pendapatan Bersih
    @GMTransactionTableSortType
    int sortType = GMTransactionTableSortType.DESCENDING; // this is for DESCENDING default
    private boolean showingSimpleDialog;
    private String[] gmStatSortBy;
    private boolean[] sortBySelections;
    private int sortByIndexSelection = 2; // default to Pendapatan Bersih
    private String[] gmStatSortType;
    private boolean[] sortTypeSelections;
    private int sortTypeIndexSelection = 0; // this is for DESCENDING default
    private TextView tvSortBy;
    private int savedSortTypeAfterChangeKeyFigure;
    private int savedSortByAfterChangeKeyFigure;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        transactionTablePresenter.attachView(this);
    }

    @Override
    public void loadDataByDateAndPage(DatePickerViewModel datePickerViewModel, int page) {
        // when we pulldown to refresh, we default the sorttype to default
        if (savedSortByAfterChangeKeyFigure!= 0) {
            reselectSortType();
        }
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
    protected void initInjector() {
        DaggerGMTransactionComponent
                .builder()
                .goldMerchantComponent(getComponent(GoldMerchantComponent.class))
                .gMStatisticModule(new GMStatisticModule())
                .build().inject(this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_gmstat_transaction_table, menu);
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
        return R.layout.fragment_transaction_table_list;
    }

    @Override
    public void onItemClicked(GMStatisticTransactionTableModel gmStatisticTransactionTableModel) {

    }

    @Override
    protected BaseListAdapter getNewAdapter() {
        GMStatisticTransactionTableAdapter adapter = new GMStatisticTransactionTableAdapter();
        adapter.setSortBy(sortBy);
        return adapter;
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
                        sortBy = GMTransactionTableSortBy.ORDER_SUM;
                        break;
                    case 1:
                        sortBy = GMTransactionTableSortBy.TRANS_SUM;
                        break;
                    default:
                    case 2:
                        sortBy = GMTransactionTableSortBy.DELIVERED_AMT;
                        break;

                }
                showingSimpleDialog = false;
                if (previousSortBy == sortBy) {
                    return;
                }
                resetSelectionSortBy(sortByIndexSelection);
                tvSortBy.setText(item.getTitle());
                GMStatisticTransactionTableAdapter gmAdapter = (GMStatisticTransactionTableAdapter)adapter;
                gmAdapter.setSortBy(sortBy);
                gmAdapter.notifyDataSetChanged();

                // save sort type after change key figure
                if (savedSortByAfterChangeKeyFigure == 0) {
                    savedSortTypeAfterChangeKeyFigure = sortType;
                    savedSortByAfterChangeKeyFigure = previousSortBy;
                    //reset it, but retrieve it again when doing sort
                    sortType = -1;
                    resetSelectionSortType(sortType);
                }
                // no need to search data
            }
        });
    }

    private void reselectSortType(){
        sortType = savedSortTypeAfterChangeKeyFigure;
        savedSortByAfterChangeKeyFigure = 0;
        int sortTypeSelection = (sortType == GMTransactionTableSortType.DESCENDING)? 0 : 1;
        resetSelectionSortType(sortTypeSelection);
    }

    private void showSortType() {
        // retrieve saved sort type
        if (savedSortByAfterChangeKeyFigure!= 0 && savedSortByAfterChangeKeyFigure == sortBy) {
            reselectSortType();
        }
        showBottomSheetDialog(getString(R.string.gm_sort), gmStatSortType, sortTypeSelections, new BottomSheetItemClickListener() {
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
                Log.d("Item click", menuItem.getTitle() + " findSelection : " + sortType);
                showingSimpleDialog = false;
                if (previousSortType == sortType) {
                    return;
                }
                resetSelectionSortType(sortTypeIndexSelection);
                searchData();
            }
        });
    }

    private void showBottomSheetDialog(String bottomDialogTitle, final String[] text, final boolean[] selections, BottomSheetItemClickListener bottomSheetItemClickListener) {
        showingSimpleDialog = true;
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
        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                showingSimpleDialog = false;
            }
        });
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
