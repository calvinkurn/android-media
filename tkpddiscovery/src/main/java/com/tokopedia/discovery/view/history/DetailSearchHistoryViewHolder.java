package com.tokopedia.discovery.view.history;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.discovery.model.history.SearchHistoryAdapter;
import com.tokopedia.discovery.model.history.SearchHistoryModel;
import com.tokopedia.discovery.presenter.history.SearchHistoryImpl;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Toped18 on 7/4/2016.
 */
public class DetailSearchHistoryViewHolder extends RecyclerView.ViewHolder {


    private final SearchHistoryImpl.OnItemClickListener listener;
    @BindView(R2.id.detail_search_textview)
    TextView detailSearchTextView;
    @BindView(R2.id.icon_left)
    ImageView iconLeft;
    @BindView(R2.id.icon_right)
    ImageView iconRight;
    private static final String TAG = DetailSearchHistoryViewHolder.class.getSimpleName();
    private int type;
    private String urlHotlist = "";
    private String mSearchTerm;
    private TextAppearanceSpan highlightTextSpan;
    private SearchHistoryModel.Data detail;

    public static int getItemViewLayout() {
        return R.layout.detail_recyclerview_search_history;
    }

    public DetailSearchHistoryViewHolder(View itemView, SearchHistoryImpl.OnItemClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        highlightTextSpan = new TextAppearanceSpan(itemView.getContext(), R.style.searchTextHiglight);
        this.listener = listener;
    }

    public void setmSearchTerm(String mSearchTerm) {
        this.mSearchTerm = mSearchTerm;
    }

    public void setDetailSearchTextView(SearchHistoryModel.Data detail) {
        int startIndex = indexOfSearchQuery(detail.getTitle());
        this.detail = detail;
        if(startIndex == -1){
            detailSearchTextView.setText(detail.getTitle().toLowerCase());
        } else {
            SpannableString highlightedTitle = new SpannableString(detail.getTitle());
            highlightedTitle.setSpan(highlightTextSpan, startIndex, startIndex + mSearchTerm.length(), 0);
            detailSearchTextView.setText(highlightedTitle);
        }
        detailSearchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(DetailSearchHistoryViewHolder.this.detail, urlHotlist, type);
            }
        });
    }

    private int indexOfSearchQuery(String displayName) {
        if (!TextUtils.isEmpty(mSearchTerm)) {
            return displayName.toLowerCase(Locale.getDefault()).indexOf(mSearchTerm.toLowerCase(Locale.getDefault()));
        }
        return -1;
    }

    public void setIconLeft(int iconLeft) {
        this.iconLeft.setImageResource(iconLeft);
        this.iconLeft.setVisibility(View.VISIBLE);
    }

    public void setIconRight(int iconRight) {
        this.iconRight.setImageResource(iconRight);
        this.iconRight.setVisibility(View.VISIBLE);
        this.iconRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(DetailSearchHistoryViewHolder.this.detail, "", SearchHistoryAdapter.DELETE_ITEM);
            }
        });
    }

    public void hideIconLeft() {
        iconLeft.setVisibility(View.GONE);
    }

    public void hideIconRight() {
        iconRight.setVisibility(View.GONE);
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUrlHotlist(String urlHotlist) {
        this.urlHotlist = urlHotlist;
    }
}
