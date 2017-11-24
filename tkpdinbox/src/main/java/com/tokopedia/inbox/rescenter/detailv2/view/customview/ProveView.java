package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.create.customview.BaseView;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ProveAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ProveData;

/**
 * Created by yfsx on 10/11/17.
 */

public class ProveView extends BaseView<ProveData, DetailResCenterFragmentView> {

    ProveAdapter adapter;
    private RecyclerView rvAttachment;
    private TextView tvRemark;
    private Context context;

    public ProveView(Context context) {
        super(context);
    }

    public ProveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void initView(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        rvAttachment = (RecyclerView) view.findViewById(R.id.rv_attachment);
        tvRemark = (TextView) view.findViewById(R.id.tv_remark);
        adapter = new ProveAdapter(context);
    }

    @Override
    public void setListener(DetailResCenterFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_prove_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull ProveData proveData) {
        setVisibility(VISIBLE);
        tvRemark.setText(proveData.getRemark());
        initRecyclerView(proveData);
    }

    private void initRecyclerView(ProveData data) {
        rvAttachment.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvAttachment.setHasFixedSize(true);
        rvAttachment.setAdapter(adapter);
        adapter.setAttachmentDataList(data.getAttachment());
        adapter.notifyDataSetChanged();
    }
}
