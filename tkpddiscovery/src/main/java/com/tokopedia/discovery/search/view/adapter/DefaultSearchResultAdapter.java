package com.tokopedia.discovery.search.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
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
import com.tokopedia.discovery.view.history.DetailSearchHistoryViewHolder;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author erry on 20/02/17.
 */

public class DefaultSearchResultAdapter extends RecyclerView.Adapter<DefaultSearchResultAdapter.ViewHolder> {

    private DefaultViewModel model;
    private final ItemClickListener clickListener;
    private String searchTerm = "";
    private TextAppearanceSpan highlightTextSpan;

    public DefaultSearchResultAdapter(Context context, ItemClickListener clickListener) {
        model = new DefaultViewModel();
        this.clickListener = clickListener;
        highlightTextSpan = new TextAppearanceSpan(context, com.tokopedia.core.R.style.searchTextHiglight);
    }

    public void setModel(DefaultViewModel model) {
        this.model = model;
        notifyDataSetChanged();
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.default_search_child_list_item, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchItem searchItem = model.getSearchItems().get(position);
        holder.bindView(searchItem);
    }

    @Override
    public int getItemCount() {
        return model.getSearchItems().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R2.id.title)
        TextView resultTxt;
        @BindView(R2.id.sublabel)
        TextView label;
        @BindView(R2.id.icon)
        ImageView icon;

        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            icon.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R2.id.icon:
                    break;
                default:
                    clickListener.onItemClicked(model.getSearchItems().get(getAdapterPosition()));
            }
        }

        private void bindView(SearchItem searchItem){
            int startIndex = indexOfSearchQuery(searchItem.getKeyword());
            if(startIndex == -1){
                resultTxt.setText(searchItem.getKeyword().toLowerCase());
            } else {
                SpannableString highlightedTitle = new SpannableString(searchItem.getKeyword());
                highlightedTitle.setSpan(highlightTextSpan, startIndex, startIndex + searchTerm.length(), 0);
                resultTxt.setText(highlightedTitle);
            }
            resultTxt.setText(searchItem.getKeyword());
            switch (model.getId()){
                case in_category:
                    label.setVisibility(View.VISIBLE);
                    label.setText(String.format(context.getString(R.string.formated_in_category), searchItem.getRecom()));
                    break;
                case recent_search:
                    icon.setImageResource(R.drawable.ic_clear_black);
                    break;
                default:
                    label.setVisibility(View.GONE);
                    icon.setImageResource(R.drawable.ic_diagonal_arrow);
            }
        }
    }

    private int indexOfSearchQuery(String displayName) {
        if (!TextUtils.isEmpty(searchTerm)) {
            return displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()));
        }
        return -1;
    }

}