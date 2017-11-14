package com.tokopedia.inbox.inboxmessage.adapter.databinder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomPresenter;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 5/20/16.
 */
public class LoadMoreDataBinder extends DataBinder<LoadMoreDataBinder.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.btn_load_more)
        Button loadMore;

        @BindView(R2.id.progressBar)
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    ChatRoomPresenter presenter;
    Boolean isLoading = false;


    public LoadMoreDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }


    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_load_more, parent, false));
        return holder;
    }

    @Override
    public void bindViewHolder(final ViewHolder holder, int position) {

        if(isLoading){
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.loadMore.setVisibility(View.GONE);
        }else{
            holder.progressBar.setVisibility(View.GONE);
            holder.loadMore.setVisibility(View.VISIBLE);
        }

       holder.loadMore.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               holder.loadMore.setVisibility(View.GONE);
               holder.progressBar.setVisibility(View.VISIBLE);
               presenter.onLoadMore();
           }
       });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setPresenter(ChatRoomPresenter presenter) {
        this.presenter = presenter;
    }

    public void setIsLoading(Boolean isLoading) {
        this.isLoading = isLoading;
    }

    public Boolean getIsLoading() {
        return isLoading;
    }
}
