package com.tokopedia.seller.topads.lib.datepicker;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nathaniel on 1/16/2017.
 */

@SuppressWarnings("deprecation")
public class PeriodAdapter extends RecyclerView.Adapter {
    private static final Locale locale = new Locale("in", "ID");
    DatePickerRules datePickerRules;
    private List<SetDateFragment.BasePeriodModel> basePeriodModels;
    private DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);
    private RecyclerView mRecyclerView;
    private SetDateFragment.PeriodListener periodListener = new SetDateFragment.PeriodListener() {
        @Override
        public void updateCheck(boolean checked, int index) {
            for (int i = 0; i < basePeriodModels.size(); i++) {
                if (index != i) {
                    if (basePeriodModels.get(i) instanceof PeriodRangeModel) {
                        PeriodRangeModel prm = (PeriodRangeModel) basePeriodModels.get(i);
                        prm.isChecked = false;
                    }
                }
            }

            if (mRecyclerView != null && !mRecyclerView.isComputingLayout()) {
                PeriodAdapter.this.notifyDataSetChanged();
            }
        }

        @Override
        public boolean isAllNone(boolean isChecked, int index) {
            return false;
        }
    };

    public PeriodAdapter(final View itemView, long sDate, long eDate, long minStartDate, long maxEndDate, int maxDateRange) {
        basePeriodModels = new ArrayList<>();

        Calendar instance = Calendar.getInstance();
        long tomorrow = instance.getTimeInMillis();

        Log.d("MNORMANSYAH", "max limit ## " + dateFormat.format(tomorrow) +
                " minLimit " + dateFormat.format(minStartDate) +
                " max End Date " + dateFormat.format(maxEndDate));

        datePickerRules = new DatePickerRules(maxEndDate, minStartDate, maxDateRange);
        datePickerRules.setDatePickerRulesListener(new DatePickerRules.DatePickerRulesListener() {

            @Override
            public void successDate(long sDate, long eDate) {
                basePeriodModels.clear();
                StartOrEndPeriodModel startOrEndPeriodModel = new StartOrEndPeriodModel(true, false, "Tanggal Mulai");
                startOrEndPeriodModel.setStartDate(sDate);
                basePeriodModels.add(startOrEndPeriodModel);

                if (eDate == -1) {
                    PeriodAdapter.this.notifyDataSetChanged();
                    return;
                }

                startOrEndPeriodModel = new StartOrEndPeriodModel(false, true, "Tanggal Selesai");
                startOrEndPeriodModel.setEndDate(eDate);
                basePeriodModels.add(startOrEndPeriodModel);
                PeriodAdapter.this.notifyDataSetChanged();
            }
        });
        datePickerRules.seteDate(eDate);
        datePickerRules.setsDate(sDate);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case PeriodRangeModel.TYPE:
                return new BasePeriodViewHolder(LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_date_picker_periode, parent, false));
            case StartOrEndPeriodModel.TYPE:
            default:
                return new CustomDateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_picker_custom, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (basePeriodModels.get(position).type) {
            case PeriodRangeModel.TYPE:
                BasePeriodViewHolder bpvh = ((BasePeriodViewHolder) holder);
                bpvh.setPeriodListener(periodListener);
                bpvh.bindData((PeriodRangeModel) basePeriodModels.get(position));
                break;
            case StartOrEndPeriodModel.TYPE:
                CustomDateViewHolder customDateViewHolder = (CustomDateViewHolder) holder;
//                    customDateViewHolder.setDateValidationListener(dateValidationListener);
                customDateViewHolder.setDatePickerRules(datePickerRules);
                customDateViewHolder.bindData((StartOrEndPeriodModel) basePeriodModels.get(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (basePeriodModels.get(position).type) {
            case PeriodRangeModel.TYPE:
            case StartOrEndPeriodModel.TYPE:
                return basePeriodModels.get(position).type;
        }
        throw new RuntimeException("please register type to PeriodAdapter");
    }

    public void setBasePeriodModels(List<SetDateFragment.BasePeriodModel> basePeriodModels) {
        this.basePeriodModels = basePeriodModels;
    }

    @Override
    public int getItemCount() {
        return basePeriodModels.size();
    }
}
