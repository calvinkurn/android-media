package com.tokopedia.discovery.search.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.R2;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.search.domain.model.SearchItem;
import com.tokopedia.discovery.search.view.SearchContract;
import com.tokopedia.discovery.search.view.adapter.viewmodel.DefaultViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author erry on 20/02/17.
 */

public class DefaultSearchResultAdapter extends RecyclerView.Adapter<DefaultSearchResultAdapter.ViewHolder> {

    private DefaultViewModel model;
    private final ItemClickListener clickListener;

    public DefaultSearchResultAdapter(ItemClickListener clickListener) {
        model = new DefaultViewModel();
        this.clickListener = clickListener;
    }

    public void setModel(DefaultViewModel model) {
        this.model = model;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SearchContract.View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.default_search_child_list_item, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchItem searchItem = model.getSearchItems().get(position);
        holder.resultTxt.setText(searchItem.getKeyword());
        switch (model.getId()){
            case recent_search:
                holder.icon.setImageResource(R.drawable.ic_clear_black);
                break;
            default:
                holder.icon.setImageResource(R.drawable.ic_diagonal_arrow);
        }
    }

    @Override
    public int getItemCount() {
        return model.getSearchItems().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R2.id.title)
        TextView resultTxt;
        @BindView(R2.id.icon)
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClicked(model.getSearchItems().get(getAdapterPosition()));
        }
    }

}