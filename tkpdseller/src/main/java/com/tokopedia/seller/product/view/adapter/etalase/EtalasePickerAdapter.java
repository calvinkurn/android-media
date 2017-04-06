package com.tokopedia.seller.product.view.adapter.etalase;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private final EtalasePickerAdapterListener listener;

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
                    (MyEtalaseViewModel) data.get(position)
            );
        } else {
            super.onBindViewHolder(holder, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data.isEmpty() || isLoading() || isRetry()){
            return super.getItemViewType(position);
        } else {
            return data.get(position).getType();
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + super.getItemCount();
    }

    public void renderData(List<MyEtalaseViewModel> etalases) {
        data.add(new AddEtalaseViewModel());
        data.addAll(etalases);
    }

    public void clearEtalaseList() {
        data.clear();
    }
}
