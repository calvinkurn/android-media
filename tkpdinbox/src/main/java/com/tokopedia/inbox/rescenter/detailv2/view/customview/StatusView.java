package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.StatusData;

/**
 * Created by hangnadi on 3/8/17.
 */

public class StatusView extends BaseView<StatusData, DetailResCenterFragmentView> {

    private TextView lastStatus;
    private View actionDiscuss;
    private RelativeLayout rlActionDiscuss;

    public StatusView(Context context) {
        super(context);
    }

    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(DetailResCenterFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_status_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        rlActionDiscuss = (RelativeLayout) view.findViewById(R.id.rv_action_discuss);
        lastStatus = (TextView) view.findViewById(R.id.textview_last_status);
        actionDiscuss = view.findViewById(R.id.action_discuss);
    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull StatusData data) {
        setVisibility(VISIBLE);
        lastStatus.setText(data.getStatusText());
        rlActionDiscuss.setVisibility(GONE);
        actionDiscuss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setOnActionDiscussClick();
            }
        });
    }

}
