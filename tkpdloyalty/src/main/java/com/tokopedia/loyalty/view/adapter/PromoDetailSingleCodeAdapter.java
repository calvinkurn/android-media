package com.tokopedia.loyalty.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.view.data.SingleCodeViewModel;
import com.tokopedia.loyalty.view.viewholder.PromoDetailSingleCodeViewHolder;

import java.util.List;

import static com.tokopedia.loyalty.view.viewholder.PromoDetailSingleCodeViewHolder.ITEM_VIEW_SINGLE_CODE;

/**
 * @author Aghny A. Putra on 27/03/18
 */

public class PromoDetailSingleCodeAdapter extends RecyclerView.Adapter<PromoDetailSingleCodeViewHolder> {

    private List<SingleCodeViewModel> singleCodeList;

    public PromoDetailSingleCodeAdapter(List<SingleCodeViewModel> singleCodeList) {
        this.singleCodeList = singleCodeList;
    }

    @Override
    public PromoDetailSingleCodeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context)
                .inflate(ITEM_VIEW_SINGLE_CODE, viewGroup, false);
        return new PromoDetailSingleCodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PromoDetailSingleCodeViewHolder holder, int position) {
        holder.bind(this.singleCodeList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.singleCodeList.size();
    }
}
