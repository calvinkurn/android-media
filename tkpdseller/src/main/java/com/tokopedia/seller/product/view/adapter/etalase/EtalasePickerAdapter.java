package com.tokopedia.seller.product.view.adapter.etalase;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.product.view.model.etalase.AddEtalaseViewModel;
import com.tokopedia.seller.product.view.model.etalase.EtalaseViewModel;
import com.tokopedia.seller.product.view.model.etalase.MyEtalaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class EtalasePickerAdapter extends BaseLinearRecyclerViewAdapter{
    private List<EtalaseViewModel> data = new ArrayList<>();
    private List<EtalaseViewModel> dataRendered = new ArrayList<>();
    private final EtalasePickerAdapterListener listener;
    private long selectedEtalase;
    private String query = "";

    public EtalasePickerAdapter(EtalasePickerAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == AddEtalaseViewModel.LAYOUT){
            return setAddEtalaseViewHolder(parent, viewType);
        } else if (viewType == MyEtalaseViewModel.LAYOUT) {
            return setMyEtalaseItemViewHolder(parent, viewType);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @NonNull
    private RecyclerView.ViewHolder setMyEtalaseItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(viewType, parent, false);
        return new MyEtalaseViewHolder(view, listener);
    }

    @NonNull
    private RecyclerView.ViewHolder setAddEtalaseViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(viewType, parent, false);
        return new AddEtalaseViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == AddEtalaseViewModel.LAYOUT){

        } else if (viewType == MyEtalaseViewModel.LAYOUT) {
            ((MyEtalaseViewHolder)holder).renderView(
                    (MyEtalaseViewModel) dataRendered.get(position),
                    ((MyEtalaseViewModel) dataRendered.get(position)).getEtalaseId() == selectedEtalase
            );
        } else {
            super.onBindViewHolder(holder, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (dataRendered.isEmpty() || isLoading() || isRetry()){
            return super.getItemViewType(position);
        } else {
            return dataRendered.get(position).getType();
        }
    }

    @Override
    public int getItemCount() {
        return getDataSize() + super.getItemCount();
    }

    private int getDataSize() {
        if (StringUtils.isBlank(query)) {
            dataRendered = data;
            return data.size();
        } else {
            int dataCount = 0;
            dataRendered = new ArrayList<>();
            for (EtalaseViewModel viewModel : data){
                if (viewModel instanceof AddEtalaseViewModel){
                    dataRendered.add(viewModel);
                    dataCount++;
                } else if (viewModel instanceof MyEtalaseViewModel &&
                        ((MyEtalaseViewModel) viewModel).getEtalaseName()
                                .toLowerCase()
                                .contains(query.toLowerCase())){
                    dataRendered.add(viewModel);
                    dataCount++;
                }
            }
            return dataCount;
        }
    }

    public void renderData(List<MyEtalaseViewModel> etalases) {
        data.add(new AddEtalaseViewModel());
        data.addAll(etalases);
        notifyDataSetChanged();
    }

    public void clearEtalaseList() {
        data.clear();
        notifyDataSetChanged();
    }

    public void setSelectedEtalase(long selectedEtalase) {
        this.selectedEtalase = selectedEtalase;
    }

    public void setQuery(String query) {
        this.query = query;
        notifyDataSetChanged();
    }

    public void clearQuery() {
        this.query = "";
        notifyDataSetChanged();
    }
}
