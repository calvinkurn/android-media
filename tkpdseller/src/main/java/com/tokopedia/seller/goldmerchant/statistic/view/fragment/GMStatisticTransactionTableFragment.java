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
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.seller.goldmerchant.statistic.di.component.DaggerGMTransactionComponent;
import com.tokopedia.seller.goldmerchant.statistic.di.module.GMStatisticModule;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatatisticDateUtils;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.view.adapter.GMStatisticTransactionTableAdapter;
import com.tokopedia.seller.goldmerchant.statistic.view.adapter.model.GMStatisticTransactionTableModel;
import com.tokopedia.seller.goldmerchant.statistic.view.builder.CheckedBottomSheetBuilder;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionTablePresenter;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionTableView;
import com.tokopedia.tkpdlib.bottomsheetbuilder.BottomSheetBuilder;
import com.tokopedia.tkpdlib.bottomsheetbuilder.adapter.BottomSheetItemClickListener;

import java.util.Date;

import javax.inject.Inject;

/**
 * @author normansyahputa on 7/13/17.
 */
public class GMStatisticTransactionTableFragment extends BaseListDateFragment<GMStatisticTransactionTableModel>
        implements GMStatisticTransactionTableView {
    public static final String TAG = "GMStatisticTransactionT";

    @Inject
    GMStatisticTransactionTablePresenter transactionTablePresenter;

    @GMTransactionTableSortBy
    int sortBy = GMTransactionTableSortBy.DELIVERED_AMT;
    @GMTransactionTableSortType
    int sortType = GMTransactionTableSortType.ASCENDING;
    private LinearLayout sortTypeContainer;
    private boolean showingSimpleDialog;
    private String[] gmStatSortBy;
    private boolean[] sortBySelections;
    private int sortByIndexSelection;
    private String[] gmStatSortType;
    private boolean[] sortTypeSelections;
    private int sortTypeIndexSelection = -1;
    private TextView tvSortBy;

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
    public void loadData() {
        super.loadData();
        searchData();
    }

    @Override
    protected void searchData() {
        super.searchData();
        transactionTablePresenter.loadData(new Date(datePickerViewModel.getStartDate()), new Date(datePickerViewModel.getEndDate()), sortBy, sortType, page);
    }

    @Override
    public Intent getDatePickerIntent() {
        return GMStatatisticDateUtils.getDatePickerIntent(getActivity(), datePickerViewModel);
    }

    @Override
    public void setDefaultDateViewModel() {
        datePickerViewModel = GMStatatisticDateUtils.getDefaultDatePickerViewModel();
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
        sortTypeContainer = (LinearLayout) view.findViewById(R.id.sort_by_container);
        sortTypeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortBy();
            }
        });
        tvSortBy = (TextView) sortTypeContainer.findViewById(R.id.sort_by_text);

        gmStatSortBy = getResources().getStringArray(R.array.gm_stat_sort_by);
        sortBySelections = new boolean[gmStatSortBy.length];
        sortBySelections[sortByIndexSelection] = true;

        gmStatSortType = getResources().getStringArray(R.array.gm_stat_sort_type);
        sortTypeSelections = new boolean[gmStatSortBy.length];
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
        return new GMStatisticTransactionTableAdapter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        transactionTablePresenter.detachView();
    }

    private void showSortBy() {
        showBottomSheetDialog(gmStatSortBy, sortBySelections, new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
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
                resetSelectionSortBy(sortByIndexSelection);
                tvSortBy.setText(item.getTitle());
                searchData();
                showingSimpleDialog = false;
            }
        });
    }

    private void showSortType() {
        showBottomSheetDialog(gmStatSortType, sortTypeSelections, new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem menuItem) {
                switch (sortTypeIndexSelection = GMStatisticUtil.findSelection(gmStatSortType, menuItem.getTitle().toString())) {
                    case 0:
                        sortType = GMTransactionTableSortType.ASCENDING;
                        break;
                    case 1:
                        sortType = GMTransactionTableSortType.DESCENDING;
                        break;
                    default:
                        sortType = -1;
                        break;
                }
                Log.d("Item click", menuItem.getTitle() + " findSelection : " + sortType);
                resetSelectionSortType(sortTypeIndexSelection);
                searchData();
                showingSimpleDialog = false;
            }
        });
    }

    private void showBottomSheetDialog(final String[] text, final boolean[] selections, BottomSheetItemClickListener bottomSheetItemClickListener) {
        showingSimpleDialog = true;
        BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(getString(R.string.filter));

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
}
