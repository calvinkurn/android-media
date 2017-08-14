package com.tokopedia.tkpd.tkpdreputation.inbox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputationItem;
import com.tokopedia.core.inboxreputation.presenter.InboxReputationFragmentPresenter;
import com.tokopedia.core.util.LabelUtils;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.customview.ReputationView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 8/11/17.
 */

public class InboxReputationAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int BUYER = 1;
    private static final int SELLER = 2;
    private static final int VIEW_REPUTATION = 100;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textDeadline;
        View deadline;
        TextView invoice;
        ImageView notification;
        ImageView avatar;
        TextView name;
        ReputationView reputation;
        TextView date;
        TextView action;

        public ViewHolder(View itemView) {
            super(itemView);
            textDeadline = (TextView) itemView.findViewById(R.id.deadline_text);
            deadline = itemView.findViewById(R.id.deadline);
            invoice = (TextView) itemView.findViewById(R.id.invoice);
            notification = (ImageView) itemView.findViewById(R.id.notification);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            name = (TextView) itemView.findViewById(R.id.name);
            reputation = (ReputationView) itemView.findViewById(R.id.reputation);
            date = (TextView) itemView.findViewById(R.id.date);
            action = (TextView) itemView.findViewById(R.id.action);
        }
    }

    private List<InboxReputationItem> list;

    public InboxReputationAdapter() {
        super();
        this.list = new ArrayList<>();
    }

    public static InboxReputationAdapter createAdapter() {
        return new InboxReputationAdapter();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_REPUTATION:
                return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.inbox_reputation_item, viewGroup, false));
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_REPUTATION:
                bindReputation((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }

    }

    private void bindReputation(ViewHolder holder, int position) {
//        holder.name.setText(list.get(position).getRevieweeName());
//        holder.date.setText(list.get(position).getCreateTime());
//        setDeadline(holder, position);
//        ImageHandler.LoadImage(holder.avatar, list.get(position).getRevieweeImageUrl());
//        setReputation(holder, position);
//        if (list.get(position).isShowBookmark()) {
//            holder.notification.setVisibility(View.VISIBLE);
//        } else {
//            holder.notification.setVisibility(View.GONE);
//        }
    }

    private void setReputation(ViewHolder holder, int position) {

    }

    private void setDeadline(ViewHolder holder, int position) {
        if (list.get(position).getCanShowReputationDay()) {
            holder.deadline.setVisibility(View.VISIBLE);
            holder.textDeadline.setText(list.get(position).getReputationDaysLeft());
        } else {
            holder.deadline.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return 5 + super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_REPUTATION;
        }
    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }


}
