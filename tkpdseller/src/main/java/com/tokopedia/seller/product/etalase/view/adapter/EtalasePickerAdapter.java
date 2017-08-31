package com.tokopedia.seller.product.etalase.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.product.etalase.view.model.EtalaseViewModel;
import com.tokopedia.seller.product.etalase.view.model.MyEtalaseItemViewModel;
import com.tokopedia.seller.product.etalase.view.model.MyEtalaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class EtalasePickerAdapter extends BaseLinearRecyclerViewAdapter {

    private List<EtalaseViewModel> data = new ArrayList<>();
    private List<EtalaseViewModel> dataRendered = new ArrayList<>();
    private final EtalasePickerAdapterListener listener;
    private long selectedEtalase;
    private String query = "";
    private boolean hasNextPage;
    private int page = 1;

    public EtalasePickerAdapter(EtalasePickerAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MyEtalaseItemViewModel.LAYOUT) {
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == MyEtalaseItemViewModel.LAYOUT) {
            ((MyEtalaseViewHolder) holder).renderView(
                    (MyEtalaseItemViewModel) dataRendered.get(position),
                    ((MyEtalaseItemViewModel) dataRendered.get(position)).getEtalaseId() == selectedEtalase
            );
        } else {
            super.onBindViewHolder(holder, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (dataRendered.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return dataRendered.get(position).getType();
        }
    }

    private boolean isLastItemPosition(int position) {
        return position >= dataRendered.size();
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
            for (EtalaseViewModel viewModel : data) {
                if (viewModel instanceof MyEtalaseItemViewModel &&
                        ((MyEtalaseItemViewModel) viewModel).getEtalaseName()
                                .toLowerCase()
                                .contains(query.toLowerCase())) {
                    dataRendered.add(viewModel);
                    dataCount++;
                }
            }
            return dataCount;
        }
    }

    public void renderData(MyEtalaseViewModel etalases) {
        page++;
        hasNextPage = etalases.isHasNextPage();
        data.addAll(etalases.getEtalaseList());
        notifyDataSetChanged();
    }

    public void clearEtalaseList() {
        page = 1;
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

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public int getPage() {
        return page;
    }
}
