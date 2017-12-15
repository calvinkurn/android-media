package com.tokopedia.seller.opportunity.adapter;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 3/1/17.
 */

public class OpportunityListAdapter extends BaseLinearRecyclerViewAdapter {

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productAvatar;
        TextView productName;

        TextView productPrice;

        TextView deadline;

        View mainView;

        TextView reputationPoint;

        public ViewHolder(View itemView) {
            super(itemView);
            productAvatar = (ImageView) itemView.findViewById(R.id.product_avatar);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            productPrice = (TextView) itemView.findViewById(R.id.product_price);
            deadline = (TextView) itemView.findViewById(R.id.deadline);
            mainView = itemView.findViewById(R.id.opportunity_view);
            reputationPoint = (TextView) itemView.findViewById(R.id.reputation_point);

            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() >= 0 && getAdapterPosition() < list.size()) {
                        list.get(getAdapterPosition()).setPosition(getAdapterPosition());
                        listener.goToDetail(list.get(getAdapterPosition()));
                    }
                }
            });

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

    public OpportunityListAdapter(OpportunityListener listener) {
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
        String color = list.get(position).getOrderDeadline().getDeadlineColor();

        Drawable deadlineDrawable = holder.deadline.getBackground();
        deadlineDrawable.clearColorFilter();
        if (!TextUtils.isEmpty(color)) {
            int colorInt = Color.parseColor(color);
            ColorFilter cf = new
                    PorterDuffColorFilter(colorInt, PorterDuff.Mode
                    .MULTIPLY);
            deadlineDrawable.setColorFilter(cf);
        }

        setReputationPoint(holder, list.get(position));
    }

    private void setReputationPoint(ViewHolder holder, OpportunityItemViewModel opportunityItemViewModel) {
        if (opportunityItemViewModel.getReplacementMultiplierValue() != 0
                && !TextUtils.isEmpty(opportunityItemViewModel.getReplacementMultiplierColor())
                && !TextUtils.isEmpty(opportunityItemViewModel.getReplacementMultiplierText())) {
            holder.reputationPoint.setVisibility(View.VISIBLE);

            String color = opportunityItemViewModel.getReplacementMultiplierColor();
            int colorInt = Color.parseColor(color);
            Drawable reputationDrawable = holder.reputationPoint.getBackground();

            ColorFilter cf = new
                    PorterDuffColorFilter(colorInt, PorterDuff.Mode
                    .MULTIPLY);
            reputationDrawable.setColorFilter(cf);

            holder.reputationPoint.setText(opportunityItemViewModel.getReplacementMultiplierText());
            holder.reputationPoint.setTextColor(colorInt);

        } else {
            holder.reputationPoint.setVisibility(View.GONE);
        }
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
