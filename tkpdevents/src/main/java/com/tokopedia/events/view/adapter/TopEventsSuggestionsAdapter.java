package com.tokopedia.events.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.view.presenter.EventSearchPresenter;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.SearchViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pranaymohapatra on 25/01/18.
 */

public class TopEventsSuggestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SearchViewModel> categoryItems;
    private EventSearchPresenter mPresenter;
    private String highLightText;
    private String lowerhighlight;
    private String upperhighlight;
    private boolean isFooterAdded = false;

    private static final int ITEM = 1;
    private static final int FOOTER = 2;

    public TopEventsSuggestionsAdapter(Context context, List<SearchViewModel> categoryItems, EventSearchPresenter presenter) {
        this.mContext = context;
        this.mPresenter = presenter;
        this.categoryItems = categoryItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View v;
        switch (viewType) {
            case ITEM:
                v = inflater.inflate(R.layout.simple_recycler_item, parent, false);
                holder = new EventsTitleHolder(v);
                break;
            case FOOTER:
                v = inflater.inflate(R.layout.footer_layout, parent, false);
                holder = new FooterViewHolder(v);
                break;
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                ((EventsTitleHolder) holder).setEventTitle(position, categoryItems.get(position));
                break;
            case FOOTER:
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return categoryItems.size();
    }

    public void setHighLightText(String text) {
        if (text != null && text.length() > 0) {
            String first = text.substring(0, 1).toUpperCase();
            lowerhighlight = text.toLowerCase();
            upperhighlight = text.toUpperCase();
            highLightText = first + text.substring(1).toLowerCase();
        }
    }

    public void addFooter() {
        if (!isFooterAdded) {
            isFooterAdded = true;
            add(new SearchViewModel());
        }
    }

    private SearchViewModel getItem(int position) {
        return categoryItems.get(position);
    }

    public void add(SearchViewModel item) {
        categoryItems.add(item);
        notifyItemInserted(categoryItems.size() - 1);
    }

    public void addAll(List<SearchViewModel> items) {
        for (SearchViewModel item : items) {
            add(item);
        }
    }

    private void remove(SearchViewModel item) {
        int position = categoryItems.indexOf(item);
        if (position > -1) {
            categoryItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isFooterAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    private boolean isLastPosition(int position) {
        return (position == categoryItems.size() - 1);
    }

    public void removeFooter() {
        if (isFooterAdded) {
            isFooterAdded = false;

            int position = categoryItems.size() - 1;
            SearchViewModel item = getItem(position);

            if (item != null) {
                categoryItems.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM;
    }


    public class EventsTitleHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_simple_item)
        TextView tvEventTitle;

        SearchViewModel valueItem;
        int mPosition;

        private EventsTitleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setEventTitle(int position, SearchViewModel value) {
            this.valueItem = value;
            this.mPosition = position;
            SpannableString spannableString = new SpannableString(valueItem.getTitle());
            if (highLightText != null && !highLightText.isEmpty() && Utils.containsIgnoreCase(valueItem.getTitle(), highLightText)) {
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                int fromindex = valueItem.getTitle().indexOf(highLightText);
                if (fromindex == -1) {
                    fromindex = valueItem.getTitle().indexOf(lowerhighlight);
                }
                if (fromindex == -1) {
                    fromindex = valueItem.getTitle().indexOf(upperhighlight);
                }
                int toIndex = fromindex + highLightText.length();
                spannableString.setSpan(styleSpan, fromindex, toIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tvEventTitle.setText(spannableString);
        }

        @OnClick(R2.id.tv_simple_item)
        void onClickFilterItem() {
            mPresenter.onSearchResultClick(valueItem);
            notifyItemChanged(mPosition);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.loading_fl)
        View loadingLayout;

        private FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
