package com.tokopedia.seller.opportunity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityItemViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nisie on 3/1/17.
 */

public class OpportunityListAdapter extends BaseLinearRecyclerViewAdapter{

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.product_avatar)
        ImageView productAvatar;

        @BindView(R2.id.product_name)
        TextView productName;

        @BindView(R2.id.product_price)
        TextView productPrice;

        @BindView(R2.id.deadline)
        TextView deadline;

        @BindView(R2.id.opportunity_view)
        View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    ArrayList<OpportunityItemViewModel> list;

    private static final int VIEW_OPPORTUNITY = 100;
    private boolean actionEnabled;
    private OpportunityListener listener;

    public interface OpportunityListener {
        void goToDetail(OpportunityItemViewModel opportunityItemViewModel);
    }

    public static OpportunityListAdapter createInstance(OpportunityListener listener) {
        return new OpportunityListAdapter(listener);
    }

    public OpportunityListAdapter(OpportunityListener listener){
        this.listener = listener;
        this.list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_OPPORTUNITY:
                return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.listview_opportunity, viewGroup, false));
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_OPPORTUNITY:
                bindOpportunity((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void bindOpportunity(ViewHolder holder, final int position) {
        ImageHandler.LoadImage(holder.productAvatar, list.get(position).getOrderProducts().get(0).getProductPicture());
        holder.productName.setText(list.get(position).getOrderProducts().get(0).getProductName());
        holder.productPrice.setText(list.get(position).getOrderDetail().getDetailOpenAmountIdr());
        holder.deadline.setText(list.get(position).getOrderDeadline().getDeadlineProcess());
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.goToDetail(list.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size() + super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_OPPORTUNITY;
        }
    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }

    public List<OpportunityItemViewModel> getList() {
        return list;
    }

    public void setList(List<OpportunityItemViewModel> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }


}
