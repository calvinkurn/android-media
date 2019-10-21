package com.tokopedia.inbox.rescenter.edit.customview;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.rescenter.edit.customadapter.LastProductTroubleAdapter;
import com.tokopedia.inbox.rescenter.edit.listener.SellerEditResCenterListener;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;

/**
 * Created on 8/29/16.
 */
public class EditSummaryResCenterView extends BaseView<EditResCenterFormData, SellerEditResCenterListener> {

    private TextView solutionText;
    private TextView remark;
    private RecyclerView productRecyclerView;
    private ImageView flagCollapse;
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

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        settingUpVariables(view);
        view.findViewById(R.id.action_collapse).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onCollapse();
            }
        });
    }

    private void settingUpVariables(View view) {
        solutionText = view.findViewById(R.id.solution_text);
        remark = view.findViewById(R.id.remark);
        productRecyclerView = view.findViewById(R.id.product_recyclerview);
        flagCollapse = view.findViewById(R.id.chevron_up);
    }
}
