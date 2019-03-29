package com.tokopedia.inbox.rescenter.edit.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.rescenter.edit.customadapter.LastProductTroubleAdapter;
import com.tokopedia.inbox.rescenter.edit.listener.SellerEditResCenterListener;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created on 8/29/16.
 */
public class EditSummaryResCenterView extends BaseView<EditResCenterFormData, SellerEditResCenterListener> {

    @BindView(R2.id.solution_text)
    TextView solutionText;
    @BindView(R2.id.remark)
    TextView remark;
    @BindView(R2.id.product_recyclerview)
    RecyclerView productRecyclerView;
    @BindView(R2.id.chevron_up)
    ImageView flagCollapse;

    private EditResCenterFormData data;

    public EditSummaryResCenterView(Context context) {
        super(context);
    }

    public EditSummaryResCenterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(SellerEditResCenterListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_edit_rescenter_summary_seller;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull EditResCenterFormData data) {
        this.data = data;
        if (!isRelatedProduct(data)) {
            solutionText.setText(data.getForm().getResolutionLast().getLastTroubleString());
            remark.setVisibility(GONE);
            productRecyclerView.setVisibility(GONE);
        } else {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            LastProductTroubleAdapter adapter = new LastProductTroubleAdapter(data.getForm().getResolutionLast().getLastProductTrouble());
            productRecyclerView.setHasFixedSize(true);
            productRecyclerView.setLayoutManager(layoutManager);
            productRecyclerView.setAdapter(adapter);
            solutionText.setVisibility(GONE);
            remark.setVisibility(GONE);
        }
    }

    private boolean isRelatedProduct(EditResCenterFormData data) {
        return data.getForm().getResolutionLast().getLastProductRelated() == 1;
    }

    @OnClick(R2.id.action_collapse)
    public void onCollapse() {
        if (isRelatedProduct(data)) {
            flagCollapse.setImageResource(productRecyclerView.getVisibility() == VISIBLE ? R.drawable.chevron_down : R.drawable.chevron_up);
            productRecyclerView.setVisibility(productRecyclerView.getVisibility() == VISIBLE ? GONE : VISIBLE);
        } else {
            flagCollapse.setImageResource(solutionText.getVisibility() == VISIBLE ? R.drawable.chevron_down : R.drawable.chevron_up);
            solutionText.setVisibility(solutionText.getVisibility() == VISIBLE ? GONE : VISIBLE);
            remark.setVisibility(GONE);
        }
    }

}
