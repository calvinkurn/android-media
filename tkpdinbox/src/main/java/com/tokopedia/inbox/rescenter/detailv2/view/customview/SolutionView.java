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
 * Created by yfsx on 3/9/17.
 */

public class SolutionView extends BaseView<SolutionData, DetailResCenterFragmentView> {

    private View actionEdit;
    private TextView informationText;
    private TextView solutionText;
    private TextView problemText;
    private TextView tvChange;
    private TextView tvSolutionTitle;

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
        tvChange = (TextView) view.findViewById(R.id.tv_change);
        tvSolutionTitle = (TextView) view.findViewById(R.id.tv_last_solution_title);
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
        tvSolutionTitle.setText(getResources().getString(
                R.string.title_section_last_solution_user_solution_title).
                replace(getResources().getString(R.string.title_section_last_solution_user_solution_identifier),
                        data.getSolutionProviderName()));
        tvChange.setVisibility(data.isEditAble() ? VISIBLE : GONE);
        tvChange.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setOnActionEditSolutionClick(false);
            }
        });
    }

    private String generateInformationText(SolutionData data) {
        return "<i>" + getContext().getString(R.string.template_last_solution_provider_v2, buildBoldName(data.getSolutionProviderName()), data.getSolutionDate()) + "</i>";
    }

    private String buildBoldName(String string){
        return "<b>" + string + "</b>";
    }

}