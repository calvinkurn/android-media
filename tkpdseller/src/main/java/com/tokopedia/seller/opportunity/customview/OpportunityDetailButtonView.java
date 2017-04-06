package com.tokopedia.seller.opportunity.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.listener.OpportunityView;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityItemViewModel;

/**
 * Created by hangnadi on 2/27/17.
 */

public class OpportunityDetailButtonView extends BaseView<OpportunityItemViewModel, OpportunityView> {

    TextView actionDelete;
    TextView actionConfirm;

    public OpportunityDetailButtonView(Context context) {
        super(context);
    }

    public OpportunityDetailButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(OpportunityView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_opportunity_button_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        actionDelete = (TextView) view.findViewById(R.id.action_delete);
        actionConfirm = (TextView) view.findViewById(R.id.action_confirm);
    }

    @Override
    protected void setViewListener() {
    }

    @Override
    public void renderData(@NonNull OpportunityItemViewModel data) {
        actionDelete.setVisibility(VISIBLE);
        actionConfirm.setVisibility(VISIBLE);

        actionDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onActionDeleteClicked();
            }
        });

        actionConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onActionConfirmClicked();
            }
        });
    }
}
