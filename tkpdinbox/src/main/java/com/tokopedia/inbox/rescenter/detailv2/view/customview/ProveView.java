package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.create.customview.BaseView;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ProveData;

/**
 * Created by yfsx on 10/11/17.
 */

public class ProveView extends BaseView<ProveData, DetailResCenterFragmentView> {

    private RecyclerView rvAttachment;
    private String remark;

    public ProveView(Context context) {
        super(context);
    }

    public ProveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(DetailResCenterFragmentView detailResCenterFragmentView) {

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

    }

    @Override
    public void renderData(@NonNull ProveData proveData) {

    }
}
