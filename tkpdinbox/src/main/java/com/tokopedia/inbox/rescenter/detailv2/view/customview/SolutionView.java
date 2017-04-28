package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.SolutionData;

/**
 * Created by hangnadi on 3/9/17.
 */

public class SolutionView extends BaseView<SolutionData, DetailResCenterFragmentView> {

    private View actionEdit;
    private TextView actionDiscuss;
    private TextView informationText;
    private TextView solutionText;

    public SolutionView(Context context) {
        super(context);
    }

    public SolutionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(DetailResCenterFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_solution_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        actionEdit = view.findViewById(R.id.action_edit);
        actionDiscuss = (TextView) view.findViewById(R.id.action_discuss);
        informationText = (TextView) view.findViewById(R.id.tv_last_solution_date);
        solutionText = (TextView) view.findViewById(R.id.tv_last_solution);
    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull SolutionData data) {
        setVisibility(VISIBLE);
        informationText.setText(generateInformationText(data));
        solutionText.setText(data.getSolutionText());
        actionEdit.setOnClickListener(new SolutionViewOnClickListener());
        actionDiscuss.setText(
                getContext().getString(listener.isSeller() ?
                        R.string.action_discuss_with_buyer : R.string.action_discuss_with_seller
                )
        );
        actionDiscuss.setOnClickListener(new SolutionViewOnClickListener());
        actionEdit.setVisibility(data.isEditAble() ? VISIBLE : GONE);
    }

    private String generateInformationText(SolutionData data) {
        return getContext().getString(R.string.template_last_solution_provider, data.getSolutionProvider(), data.getSolutionDate());
    }

    private class SolutionViewOnClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.action_edit) {
                listener.setOnActionEditSolutionClick();
            } else if (view.getId() == R.id.action_discuss) {
                listener.setOnActionDiscussClick();
            }
        }
    }
}