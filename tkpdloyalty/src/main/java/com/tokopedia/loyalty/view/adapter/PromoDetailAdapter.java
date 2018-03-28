package com.tokopedia.loyalty.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.loyalty.view.data.PromoCodeViewModel;
import com.tokopedia.loyalty.view.data.PromoDetailInfoHolderData;
import com.tokopedia.loyalty.view.data.PromoDetailTncHolderData;
import com.tokopedia.loyalty.view.viewholder.PromoDetailGroupCodeViewHolder;
import com.tokopedia.loyalty.view.viewholder.PromoDetailInfoViewHolder;
import com.tokopedia.loyalty.view.viewholder.PromoDetailSimpleCodeViewHolder;
import com.tokopedia.loyalty.view.viewholder.PromoDetailTnCViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.loyalty.view.viewholder.PromoDetailSimpleCodeViewHolder.ITEM_VIEW_SIMPLE_CODE;
import static com.tokopedia.loyalty.view.viewholder.PromoDetailTnCViewHolder.ITEM_VIEW_TNC;
import static com.tokopedia.loyalty.view.viewholder.PromoDetailGroupCodeViewHolder.ITEM_VIEW_GROUP_CODE;
import static com.tokopedia.loyalty.view.viewholder.PromoDetailInfoViewHolder.ITEM_VIEW_DETAIL_INFO;

/**
 * @author Aghny A. Putra on 26/03/18
 */

public class PromoDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnAdapterActionListener adapterActionListener;
    private List<Object> promoDetailObjectList;

    @Inject
    public PromoDetailAdapter() {
        this.promoDetailObjectList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, viewGroup, false);

        if (viewType == ITEM_VIEW_DETAIL_INFO) {
            return new PromoDetailInfoViewHolder(view);
        } else if (viewType == ITEM_VIEW_GROUP_CODE) {
            return new PromoDetailGroupCodeViewHolder(view, context, adapterActionListener);
        } else if (viewType == ITEM_VIEW_SIMPLE_CODE) {
            return new PromoDetailSimpleCodeViewHolder(view, adapterActionListener);
        } else if (viewType == ITEM_VIEW_TNC) {
            return new PromoDetailTnCViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        Object data = this.promoDetailObjectList.get(position);

        if (viewType == ITEM_VIEW_DETAIL_INFO) {
            ((PromoDetailInfoViewHolder) viewHolder).bind((PromoDetailInfoHolderData) data);
        } else if (viewType == ITEM_VIEW_GROUP_CODE) {
            ((PromoDetailGroupCodeViewHolder) viewHolder).bind((PromoCodeViewModel) data);
        } else if (viewType == ITEM_VIEW_SIMPLE_CODE) {
            ((PromoDetailSimpleCodeViewHolder) viewHolder).bind((String) data);
        } else if (viewType == ITEM_VIEW_TNC) {
            ((PromoDetailTnCViewHolder) viewHolder).bind((PromoDetailTncHolderData) data);
        }
    }

    @Override
    public int getItemCount() {
        return promoDetailObjectList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = promoDetailObjectList.get(position);

        if (item instanceof PromoDetailInfoHolderData) {
            return ITEM_VIEW_DETAIL_INFO;
        } else if (item instanceof PromoCodeViewModel) {
            return ITEM_VIEW_GROUP_CODE;
        } else if (item instanceof String) {
            return ITEM_VIEW_SIMPLE_CODE;
        } else if (item instanceof PromoDetailTncHolderData) {
            return ITEM_VIEW_TNC;
        }

        return super.getItemViewType(position);
    }

    public void setPromoDetail(List<Object> promoDetailObjectList) {
        this.promoDetailObjectList = new ArrayList<>(promoDetailObjectList);
    }

    public void setAdapterActionListener(OnAdapterActionListener adapterActionListener) {
        this.adapterActionListener = adapterActionListener;
    }

    public interface OnAdapterActionListener {

        void onItemPromoCodeCopyClipboardClicked(String promoCode);

        void onItemPromoCodeTooltipClicked();

    }
}
