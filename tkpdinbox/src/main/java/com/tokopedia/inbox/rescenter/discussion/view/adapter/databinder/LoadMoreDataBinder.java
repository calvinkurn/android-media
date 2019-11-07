package com.tokopedia.inbox.rescenter.discussion.view.adapter.databinder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;
import com.tokopedia.inbox.R;


/**
 * Created by nisie on 3/31/17.
 */

public class LoadMoreDataBinder extends DataBinder<LoadMoreDataBinder.ViewHolder> {

    public interface LoadMoreListener {
        void onLoadMore();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        Button loadMore;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            loadMore =itemView.findViewById(R.id.btn_load_more);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    LoadMoreDataBinder.LoadMoreListener listener;
    Boolean isLoading = false;


    public LoadMoreDataBinder(DataBindAdapter dataBindAdapter, LoadMoreDataBinder.LoadMoreListener listener) {
        super(dataBindAdapter);
        this.listener = listener;
    }


    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_load_more, parent, false));
        return holder;
    }

    @Override
    public void bindViewHolder(final ViewHolder holder, int position) {

        if (isLoading) {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.loadMore.setVisibility(View.GONE);
        } else {
            holder.progressBar.setVisibility(View.GONE);
            holder.loadMore.setVisibility(View.VISIBLE);
        }

        holder.loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.loadMore.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.VISIBLE);
                listener.onLoadMore();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setIsLoading(Boolean isLoading) {
        this.isLoading = isLoading;
    }

    public Boolean getIsLoading() {
        return isLoading;
    }

}