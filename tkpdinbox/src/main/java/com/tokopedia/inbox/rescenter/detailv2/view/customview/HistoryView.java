package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.HistoryAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.HistoryData;

import java.util.Collections;

/**
 * Created by hangnadi on 3/9/17.
 */

public class HistoryView extends BaseView<HistoryData, DetailResCenterFragmentView> {

    private View actionSeeMore;
    private RecyclerView listHistory;

    public HistoryView(Context context) {
        super(context);
    }

    public HistoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(DetailResCenterFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_history_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        actionSeeMore = view.findViewById(R.id.action_history_more);
        listHistory = (RecyclerView) view.findViewById(R.id.list_history);
    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull HistoryData data) {
        if (data.getHistoryList() == null || data.getHistoryList().isEmpty()) {
            return;
        }
        setVisibility(VISIBLE);
        Collections.reverse(data.getHistoryList());
        initRecyclerView(data);
        actionSeeMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setOnActionMoreHistoryClick();
            }
        });
    }

    private void initRecyclerView(HistoryData data) {
        listHistory.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        listHistory.setLayoutManager(mLayoutManager);

        HistoryAdapter adapter
                = HistoryAdapter.createLimitInstance(data.getHistoryList(), listener);
        listHistory.setAdapter(adapter);
    }
}
