package com.tokopedia.seller.topads.keyword.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.seller.R;

import java.util.ArrayList;

/**
 * Created by hendry on 5/19/2017.
 */

public class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder> {

    private ArrayList<String> keywordList;

    OnKeywordAdapterListener onKeywordAdapterListener;

    public interface OnKeywordAdapterListener {
        void onKeywordRemoved();
    }

    public void setOnKeywordAdapterListener(OnKeywordAdapterListener onKeywordAdapterListener) {
        this.onKeywordAdapterListener = onKeywordAdapterListener;
    }

    public KeywordAdapter(@NonNull ArrayList<String> keywordList) {
        this.keywordList = keywordList;
    }

    public void setKeywordList(@NonNull ArrayList<String> keywordList) {
        this.keywordList = keywordList;
        notifyDataSetChanged();
    }

    public void addKeyword(String keyword) {
        this.keywordList.add(0, keyword);
        notifyItemInserted(0);
    }

    public ArrayList<String> getKeywordList() {
        return keywordList;
    }

    @Override
    public KeywordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_chip_keyword, parent, false);
        return new KeywordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(KeywordViewHolder holder, int position) {
        holder.bindTo(keywordList.get(position));
    }

    @Override
    public int getItemCount() {
        return keywordList.size();
    }

    class KeywordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewKeyword;
        View imageViewDelete;

        public KeywordViewHolder(View itemView) {
            super(itemView);
            textViewKeyword = (TextView) itemView.findViewById(R.id.textview_keyword_item);
            imageViewDelete = itemView.findViewById(R.id.image_view_delete);
            imageViewDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == imageViewDelete) {
                int positionToDelete = getAdapterPosition();
                if (positionToDelete < 0 || positionToDelete >= keywordList.size()) {
                    return;
                }
                keywordList.remove(positionToDelete);
                notifyItemRemoved(positionToDelete);
                if (onKeywordAdapterListener != null) {
                    onKeywordAdapterListener.onKeywordRemoved();
                }
            }
        }

        public void bindTo(String keyword) {
            textViewKeyword.setText(keyword);
        }
    }

}
