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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.R2;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.search.domain.model.SearchItem;
import com.tokopedia.discovery.search.view.adapter.viewmodel.DefaultViewModel;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author erry on 20/02/17.
 */

public class DefaultSearchResultAdapter extends RecyclerView.Adapter<DefaultSearchResultAdapter.ViewHolder> {

    private static final String TAG = DefaultSearchResultAdapter.class.getSimpleName();
    private DefaultViewModel model;
    private final ItemClickListener clickListener;
    private String searchTerm = "";
    private final Context context;

    public DefaultSearchResultAdapter(Context context, ItemClickListener clickListener) {
        model = new DefaultViewModel();
        this.context = context;
        this.clickListener = clickListener;
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

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.title)
        TextView resultTxt;
        @BindView(R2.id.sublabel)
        TextView label;
        @BindView(R2.id.icon)
        ImageView icon;
        @BindView(R2.id.container)
        LinearLayout containerTxt;

        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @OnClick(R2.id.icon)
        void onIconClicked() {
            SearchItem item = model.getSearchItems().get(getAdapterPosition());
            switch (model.getId()) {
                case "recent_search":
                    clickListener.onDeleteRecentSearchItem(item);
                    break;
                default:
                    clickListener.copyTextToSearchView(item.getKeyword());
                    break;
            }
        }


        @OnClick(R2.id.container)
        void onTextClicked() {
            SearchItem item = model.getSearchItems().get(getAdapterPosition());
            item.setEventAction(model.getId());
            clickListener.onItemClicked(item);
        }

        private void bindView(SearchItem searchItem) {
            int startIndex = indexOfSearchQuery(searchItem.getKeyword());
            if (startIndex == -1) {
                resultTxt.setText(searchItem.getKeyword().toLowerCase());
            } else {
                SpannableString highlightedTitle = new SpannableString(searchItem.getKeyword());
                highlightedTitle.setSpan(new TextAppearanceSpan(context, R.style.searchTextHiglight),
                        0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                highlightedTitle.setSpan(new TextAppearanceSpan(context, R.style.searchTextHiglight),
                        startIndex + searchTerm.length(),
                        searchItem.getKeyword().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                resultTxt.setText(highlightedTitle);
            }
            switch (model.getId()) {
                case "in_category":
                    icon.setImageResource(R.drawable.ic_diagonal_arrow);
                    label.setVisibility(View.VISIBLE);
                    label.setText(String.format(context.getString(R.string.formated_in_category), searchItem.getRecom()));
                    break;
                case "recent_search":
                    label.setVisibility(View.GONE);
                    icon.setImageResource(R.drawable.ic_close_default);
                    break;
                default:
                    icon.setImageResource(R.drawable.ic_diagonal_arrow);
                    label.setVisibility(View.GONE);
                    break;
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