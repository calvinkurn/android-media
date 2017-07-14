package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListFragment2;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.seller.goldmerchant.statistic.di.component.DaggerGMTransactionComponent;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.view.adapter.GMStatisticTransactionTableAdapter;
import com.tokopedia.seller.goldmerchant.statistic.view.adapter.model.GMStatisticTransactionTableModel;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionTablePresenter;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionTableView;
import com.tokopedia.seller.lib.widget.DateLabelView;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

/**
 * @author normansyahputa on 7/13/17.
 */
public class GMStatisticTransactionTableFragment extends BaseListFragment2<GMStatisticTransactionTableModel>
        implements GMStatisticTransactionTableView {
    public static final String TAG = "GMStatisticTransactionT";

    @Inject
    GMStatisticTransactionTablePresenter presenter;

    Date startDate;
    Date endDate;
    @GMTransactionTableSortBy
    int sortBy = GMTransactionTableSortBy.DELIVERED_AMT;
    @GMTransactionTableSortType
    int sortType = GMTransactionTableSortType.ASCENDING;
    private DateLabelView dateLabelView;
    private LinearLayout sortTypeContainer;
    private boolean showingSimpleDialog;
    private String[] gmStatSortBy;
    private String[] gmStatSortType;

    public static Fragment createInstance(long startDate, long endDate) {
        Bundle bundle = new Bundle();
        bundle.putLong(GMStatisticTransactionTableView.START_DATE, startDate);
        bundle.putLong(GMStatisticTransactionTableView.END_DATE, endDate);
        GMStatisticTransactionTableFragment fragment = new GMStatisticTransactionTableFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
    }

    @Override
    protected void searchData() {
        super.searchData();
        presenter.loadData(startDate, endDate, sortBy, sortType);
    }

    @Override
    protected Intent getDatePickerIntent() {
        return null;
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerGMTransactionComponent
                .builder()
                .datePickerComponent(datePickerComponent)
                .build().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null && getArguments() != null) {
            if (getArguments().getLong(GMStatisticTransactionTableView.START_DATE, -1) != -1) {
                startDate = getDate(getArguments().getLong(GMStatisticTransactionTableView.START_DATE));
                dumpStartDateLong();
            }
            if (getArguments().getLong(GMStatisticTransactionTableView.END_DATE, -1) != -1) {
                endDate = getDate(getArguments().getLong(GMStatisticTransactionTableView.END_DATE));
                dumpEndDateLong();
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        dateLabelView = (DateLabelView) view.findViewById(R.id.date_label_view);
        dateLabelView.setVisibility(View.VISIBLE);
        sortTypeContainer = (LinearLayout) view.findViewById(R.id.sort_type_container);
        sortTypeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortBy();
            }
        });

        gmStatSortBy = getResources().getStringArray(R.array.gm_stat_sort_by);
        gmStatSortType = getResources().getStringArray(R.array.gm_stat_sort_type);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_transaction_table_list;
    }

    private Date getDate(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return calendar.getTime();
    }

    public void dumpStartDateLong() {
        Log.d(TAG, "dumpStartDateLong : " + startDate.toString());
    }

    public void dumpEndDateLong() {
        Log.d(TAG, "dumpEndDateLong : " + endDate.toString());
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
        presenter.detachView();
    }

    private void showSortBy() {
        showBottomSheetDialog(gmStatSortBy, new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                switch (GMStatisticUtil.findSelection(gmStatSortBy, item.getTitle().toString())) {
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
                Log.d("Item click", item.getTitle() + " findSelection : " + sortBy);
                searchData();
                showingSimpleDialog = false;
            }
        });
    }

    private void showSortType() {
        showBottomSheetDialog(gmStatSortType, new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem menuItem) {
                switch (GMStatisticUtil.findSelection(gmStatSortBy, menuItem.getTitle().toString())) {
                    case 0:
                        sortType = GMTransactionTableSortType.ASCENDING;
                        break;
                    default:
                    case 1:
                        sortType = GMTransactionTableSortType.DESCENDING;
                        break;
                }
                Log.d("Item click", menuItem.getTitle() + " findSelection : " + sortType);
                searchData();
                showingSimpleDialog = false;
            }
        });
    }

    private void showBottomSheetDialog(final String[] text, BottomSheetItemClickListener bottomSheetItemClickListener) {
        showingSimpleDialog = true;
        BottomSheetBuilder bottomSheetBuilder = new BottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setItemLayout(R.layout.bottomsheetbuilder_list_adapter_without_padding)
                .addTitleItem(getString(R.string.gold_merchant_transaction_summary_text));

        for (int i = 0; i < text.length; i++) {
            bottomSheetBuilder.addItem(i, text[i], null);
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

    @Override
    protected String getScreenName() {
        return null;
    }
}
