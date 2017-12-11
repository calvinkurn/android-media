package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.MethodChecker;
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
    private TextView tvRemark, tvRemarkTitle;
    private Context context;
    private static final int MAX_CHAR = 250;

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
        tvRemarkTitle = (TextView) view.findViewById(R.id.tv_remark_title);
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
    public void renderData(@NonNull final ProveData proveData) {
        setVisibility(VISIBLE);
        tvRemark.setVisibility(GONE);
        tvRemarkTitle.setVisibility(GONE);
        rvAttachment.setVisibility(GONE);
        if (proveData.getRemark() != null && !proveData.getRemark().isEmpty()) {
            tvRemark.setText(getProveViewText(proveData.getRemark()));
            tvRemarkTitle.setVisibility(VISIBLE);
            tvRemark.setVisibility(VISIBLE);
            tvRemark.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tvRemark.getText().toString().endsWith(MainApplication.getAppContext()
                            .getString(R.string.string_read_more))) {
                        tvRemark.setText(proveData.getRemark());
                    }
                }
            });
        }
        if (proveData.getAttachment().size() != 0) {
            rvAttachment.setVisibility(VISIBLE);
            initRecyclerView(proveData);
        }
    }

    private void initRecyclerView(ProveData data) {
        rvAttachment.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvAttachment.setHasFixedSize(true);
        rvAttachment.setAdapter(adapter);
        adapter.setAttachmentDataList(data.getAttachment());
        adapter.notifyDataSetChanged();
    }

    private Spanned getProveViewText(String text) {
        if (MethodChecker.fromHtml(text).length() >
                MAX_CHAR) {
            String subDescription = MethodChecker.fromHtml(text).toString().substring(0,
                    MAX_CHAR);
            return MethodChecker.fromHtml(
                    subDescription.replaceAll("(\r\n|\n)", "<br />") + "... "
                            + "<font color='#42b549'>"
                            + MainApplication.getAppContext().getString(R.string.string_read_more)
                            + "</font>");
        } else {
            return MethodChecker.fromHtml(text);
        }
    }
}
