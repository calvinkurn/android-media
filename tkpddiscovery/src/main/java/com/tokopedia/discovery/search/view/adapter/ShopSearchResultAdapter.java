package com.tokopedia.discovery.search.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.search.domain.model.SearchItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author erry on 23/02/17.
 */

public class ShopSearchResultAdapter extends RecyclerView.Adapter<ShopSearchResultAdapter.ViewHolder> {

    private List<SearchItem> items;
    private Context context;
    private String searchTerm;
    private final ItemClickListener clickListener;
    private String eventAction;

    public ShopSearchResultAdapter(Context context, ItemClickListener clickListener) {
        items = new ArrayList<>();
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setItems(List<SearchItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setEventAction(String eventAction) {
        this.eventAction = eventAction;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_search_child_list_item, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchItem searchItem = items.get(position);
        int startIndex = indexOfSearchQuery(searchItem.getKeyword());
        if(startIndex == -1){
            holder.resultTxt.setText(searchItem.getKeyword().toLowerCase());
        } else {
            SpannableString highlightedTitle = new SpannableString(searchItem.getKeyword());
            highlightedTitle.setSpan(new TextAppearanceSpan(context, R.style.searchTextHiglight),
                    0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            highlightedTitle.setSpan(new TextAppearanceSpan(context, R.style.searchTextHiglight),
                    startIndex + searchTerm.length(),
                    searchItem.getKeyword().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.resultTxt.setText(highlightedTitle);
        }
        holder.icon.setImageResource(R.drawable.ic_diagonal_arrow);
        Glide.with(context).load(searchItem.getImageURI()).into(holder.shopAvatar);
        if(searchItem.isOfficial()){
            holder.officialIcon.setVisibility(View.VISIBLE);
        } else {
            holder.officialIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    private int indexOfSearchQuery(String displayName) {
        if (!TextUtils.isEmpty(searchTerm)) {
            return displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()));
        }
        return -1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.title)
        TextView resultTxt;
        @BindView(R2.id.icon)
        ImageView icon;
        @BindView(R2.id.iv_official)
        ImageView officialIcon;
        @BindView(R2.id.shop_avatar)
        ImageView shopAvatar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R2.id.icon)
        void onCopyToSearch(){
            SearchItem searchItem = items.get(getAdapterPosition());
            clickListener.copyTextToSearchView(searchItem.getKeyword());
        }

        @OnClick(R2.id.title)
        void onItemClicked(){
            SearchItem searchItem = items.get(getAdapterPosition());
            searchItem.setEventAction(eventAction);
            UnifyTracking.eventClickAutoCompleteShopSearch(searchItem.getKeyword());
            clickListener.onItemClicked(searchItem);
        }

    }
}