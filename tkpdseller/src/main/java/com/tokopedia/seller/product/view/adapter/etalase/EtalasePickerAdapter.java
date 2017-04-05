package com.tokopedia.seller.product.view.adapter.etalase;

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

    public EtalasePickerAdapter() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == AddEtalaseViewModel.LAYOUT){
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(viewType, parent, false);
            return new AddEtalaseViewHolder(view);
        } else if (viewType == MyEtalaseViewModel.LAYOUT) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(viewType, parent, false);
            return new MyEtalaseViewHolder(view);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == AddEtalaseViewModel.LAYOUT){
            ((AddEtalaseViewHolder)holder).renderView();
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
}
