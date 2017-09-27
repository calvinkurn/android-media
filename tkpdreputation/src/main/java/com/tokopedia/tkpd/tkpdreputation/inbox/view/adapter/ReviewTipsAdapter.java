package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ReviewTips;

import java.util.ArrayList;

/**
 * @author by nisie on 9/26/17.
 */

public class ReviewTipsAdapter extends RecyclerView.Adapter<ReviewTipsAdapter.ViewHolder> {

    ArrayList<ReviewTips> list;
    private boolean isExpanded;

    private ReviewTipsAdapter() {
        this.list = new ArrayList<>();
        this.isExpanded = false;
    }

    public static ReviewTipsAdapter createInstance() {
        return new ReviewTipsAdapter();
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    public void collapse() {
        int size = this.list.size();
        this.list.clear();
        notifyItemRangeRemoved(0, size);
        setExpanded(false);
    }

    public void expand() {
        list.add(new ReviewTips(MainApplication.getAppContext().getString(R.string.tips1_title),
                MainApplication.getAppContext().getString(R.string.tips1_tips)
        ));
        list.add(new ReviewTips(MainApplication.getAppContext().getString(R.string.tips2_title),
                MainApplication.getAppContext().getString(R.string.tips2_tips)
        ));
        list.add(new ReviewTips(MainApplication.getAppContext().getString(R.string.tips3_title),
                MainApplication.getAppContext().getString(R.string.tips3_tips)
        ));
        notifyItemRangeInserted(0, list.size());
        setExpanded(true);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView tips;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            tips = (TextView) itemView.findViewById(R.id.tips);
        }
    }

    @Override
    public ReviewTipsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_review_tips, parent, false));
    }

    @Override
    public void onBindViewHolder(ReviewTipsAdapter.ViewHolder holder, int position) {
        holder.tips.setText(list.get(position).getTips());
        holder.title.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
