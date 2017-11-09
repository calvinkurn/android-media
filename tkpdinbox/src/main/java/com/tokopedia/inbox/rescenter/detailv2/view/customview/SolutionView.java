package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.SolutionData;

/**
 * Created by hangnadi on 3/9/17.
 */

public class SolutionView extends BaseView<SolutionData, DetailResCenterFragmentView> {

    private View actionEdit;
    private TextView informationText;
    private TextView solutionText;
    private TextView problemText;

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
        informationText = (TextView) view.findViewById(R.id.tv_last_solution_date);
        solutionText = (TextView) view.findViewById(R.id.tv_last_solution);
        problemText = (TextView) view.findViewById(R.id.tv_last_problem);
    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull SolutionData data) {
        setVisibility(VISIBLE);
        informationText.setText(MethodChecker.fromHtml(generateInformationText(data)));
        solutionText.setText(data.getSolutionText());
        actionEdit.setVisibility(GONE);
        problemText.setText(data.getSolutionProblem());
    }

    private String generateInformationText(SolutionData data) {
        return getContext().getString(R.string.template_last_solution_provider_v2, data.getSolutionProviderName(), data.getSolutionDate());
    }

}