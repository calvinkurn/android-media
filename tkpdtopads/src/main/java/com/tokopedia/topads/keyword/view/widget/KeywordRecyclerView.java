package com.tokopedia.topads.keyword.view.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.tokopedia.topads.R;
import com.tokopedia.topads.keyword.view.adapter.KeywordAdapter;
import java.util.ArrayList;

/**
 * Created by Hendry on 4/7/2017.
 */

public class KeywordRecyclerView extends FrameLayout {

    private KeywordAdapter adapter;

    public KeywordRecyclerView(Context context) {
        super(context);
        init();
    }

    public KeywordRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public KeywordRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        applyAttrs(attrs);
        init();
    }

    private void applyAttrs(AttributeSet attrs) {

    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_keywords_view, this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build();
        recyclerView.setLayoutManager(chipsLayoutManager);

        adapter = new KeywordAdapter(getContext(), new ArrayList<String>());

        // disable animation
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        // add margin between Items
        recyclerView.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.item_space),
                getResources().getDimensionPixelOffset(R.dimen.item_space)));
        recyclerView.setAdapter(adapter);
    }

    public void setOnKeywordAdapterListener(KeywordAdapter.OnKeywordAdapterListener onKeywordAdapterListener) {
        adapter.setOnKeywordAdapterListener(onKeywordAdapterListener);
    }

    public void setErrorKeywordList(ArrayList<String> errorKeywordList){
        adapter.setErrorKeywordList(errorKeywordList);
    }

    public void setKeywordList(ArrayList<String> keywordList) {
        adapter.setKeywordList(keywordList);
    }

    public ArrayList<String> getKeywordList() {
        return adapter.getKeywordList();
    }
    public void addKeyword(String keyword) {
        adapter.addKeyword(keyword);
    }


}