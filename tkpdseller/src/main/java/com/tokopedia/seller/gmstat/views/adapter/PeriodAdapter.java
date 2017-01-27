package com.tokopedia.seller.gmstat.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.DatePickerRules;
import com.tokopedia.seller.gmstat.views.listeners.PeriodListener;
import com.tokopedia.seller.gmstat.views.models.BasePeriodModel;
import com.tokopedia.seller.gmstat.views.models.PeriodRangeModel;
import com.tokopedia.seller.gmstat.views.models.StartOrEndPeriodModel;
import com.tokopedia.seller.gmstat.views.viewholder.BasePeriodViewHolder;
import com.tokopedia.seller.gmstat.views.viewholder.CustomViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by normansyahputa on 1/19/17.
 */
@SuppressWarnings("deprecation")
public class PeriodAdapter extends RecyclerView.Adapter{
    public DatePickerRules datePickerRules;
    List<BasePeriodModel> basePeriodModels;
    private static final Locale locale = new Locale("in","ID");
    DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);

    PeriodListener periodListener = new PeriodListener() {
        @Override
        public void updateCheck(boolean checked, int index) {
            for(int i=0;i<basePeriodModels.size();i++){
                if(index != i){
                    if(basePeriodModels.get(i) instanceof PeriodRangeModel){
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

    @Deprecated
    public PeriodAdapter(){
        basePeriodModels = new ArrayList<>();
    }

    public PeriodAdapter(final View itemView, long sDate, long eDate){
        basePeriodModels = new ArrayList<>();

        Calendar instance = Calendar.getInstance();
        long tomorrow = instance.getTimeInMillis();

        instance = Calendar.getInstance();
        instance.set(2015, 6, 25);
        long minLimit = instance.getTimeInMillis();

        instance = Calendar.getInstance();
        instance.add(Calendar.DATE, -1);
        long yesterday = instance.getTimeInMillis();

        Log.d("MNORMANSYAH", "max limit ## "+dateFormat.format(tomorrow)+
                " minLimit "+ dateFormat.format(minLimit) +
                " max End Date "+dateFormat.format(yesterday));

        datePickerRules = new DatePickerRules(tomorrow, minLimit, 60, yesterday);
        datePickerRules.setDatePickerRulesListener(new DatePickerRules.DatePickerRulesListener() {
            @Override
            public void exceedSDate() {
                Toast.makeText(itemView.getContext(), "exceed start date", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void exceedEDate() {
                Toast.makeText(itemView.getContext(), "exceed end date", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void resetToSDate(long sDate, long eDate) {
                basePeriodModels.clear();
                StartOrEndPeriodModel startOrEndPeriodModel = new StartOrEndPeriodModel(true, false, "Tanggal Mulai");
                startOrEndPeriodModel.setStartDate(sDate);
                basePeriodModels.add(startOrEndPeriodModel);
                startOrEndPeriodModel = new StartOrEndPeriodModel(false, true, "Tanggal Selesai");
                startOrEndPeriodModel.setEndDate(eDate);
                basePeriodModels.add(startOrEndPeriodModel);

                PeriodAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void resetToEDate(long sDate, long eDate) {
                basePeriodModels.clear();
                StartOrEndPeriodModel startOrEndPeriodModel = new StartOrEndPeriodModel(true, false, "Tanggal Mulai");
                startOrEndPeriodModel.setStartDate(sDate);
                basePeriodModels.add(startOrEndPeriodModel);
                startOrEndPeriodModel = new StartOrEndPeriodModel(false, true, "Tanggal Selesai");
                startOrEndPeriodModel.setEndDate(eDate);
                basePeriodModels.add(startOrEndPeriodModel);

                PeriodAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void successSDate(long sDate, long eDate) {
                basePeriodModels.clear();
                StartOrEndPeriodModel startOrEndPeriodModel = new StartOrEndPeriodModel(true, false, "Tanggal Mulai");
                startOrEndPeriodModel.setStartDate(sDate);
                basePeriodModels.add(startOrEndPeriodModel);

                if(eDate == -1) {
                    PeriodAdapter.this.notifyDataSetChanged();
                    return;
                }

                startOrEndPeriodModel = new StartOrEndPeriodModel(false, true, "Tanggal Selesai");
                startOrEndPeriodModel.setEndDate(eDate);
                basePeriodModels.add(startOrEndPeriodModel);
                PeriodAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void successEDate(long sDate, long eDate) {
                basePeriodModels.clear();
                StartOrEndPeriodModel startOrEndPeriodModel = new StartOrEndPeriodModel(true, false, "Tanggal Mulai");
                startOrEndPeriodModel.setStartDate(sDate);
                basePeriodModels.add(startOrEndPeriodModel);

                if(sDate == -1) {
                    PeriodAdapter.this.notifyDataSetChanged();
                    return;
                }
                startOrEndPeriodModel = new StartOrEndPeriodModel(false, true, "Tanggal Selesai");
                startOrEndPeriodModel.setEndDate(eDate);
                basePeriodModels.add(startOrEndPeriodModel);

                PeriodAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void promptUserExceedLimit() {
                Toast.makeText(itemView.getContext(), "Tidak dapat memilih tanggal ini", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void promptUserBelowLimit() {
                Toast.makeText(itemView.getContext(), "Data tidak tersedia pada periode ini", Toast.LENGTH_SHORT).show();
            }
        });
        datePickerRules.seteDate(eDate);
        datePickerRules.setsDate(sDate);
    }

    private RecyclerView mRecyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case PeriodRangeModel.TYPE:
                return new BasePeriodViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.periode_layout, parent, false));
            case StartOrEndPeriodModel.TYPE:
            default:
                return new CustomViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (basePeriodModels.get(position).type){
            case PeriodRangeModel.TYPE:
                BasePeriodViewHolder bpvh = ((BasePeriodViewHolder)holder);
                bpvh.setPeriodListener(periodListener);
                bpvh.bindData((PeriodRangeModel) basePeriodModels.get(position));
                break;
            case StartOrEndPeriodModel.TYPE:
                CustomViewHolder customViewHolder = (CustomViewHolder) holder;
                customViewHolder.setDatePickerRules(datePickerRules);
                customViewHolder.bindData((StartOrEndPeriodModel) basePeriodModels.get(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (basePeriodModels.get(position).type){
            case PeriodRangeModel.TYPE:
            case StartOrEndPeriodModel.TYPE:
                return basePeriodModels.get(position).type;
        }
        throw new RuntimeException("please register type to PeriodAdapter");
    }

    public List<BasePeriodModel> getBasePeriodModels() {
        return basePeriodModels;
    }

    public void setBasePeriodModels(List<BasePeriodModel> basePeriodModels) {
        this.basePeriodModels = basePeriodModels;
    }

    @Override
    public int getItemCount() {
        return basePeriodModels.size();
    }
}
