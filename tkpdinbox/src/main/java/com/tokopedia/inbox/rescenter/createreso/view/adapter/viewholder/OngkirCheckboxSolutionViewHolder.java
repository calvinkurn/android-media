package com.tokopedia.inbox.rescenter.createreso.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionDetailFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ComplaintResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.OngkirCheckboxSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.OngkirSolutionModel;

/**
 * @author by yfsx on 09/08/18.
 */
public class OngkirCheckboxSolutionViewHolder extends AbstractViewHolder<OngkirCheckboxSolutionModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_ongkir_checkbox_solution;

    private SolutionDetailFragmentListener.View mainView;
    private TextView tvTitle;
    private BaseSolutionRefundView solutionRefundView;
    private RelativeLayout extraLayout;
    private AppCompatCheckBox checkBox;
    private ComplaintResult complaintResult;
    private View middleSeparator;

    public OngkirCheckboxSolutionViewHolder(View itemView, SolutionDetailFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        solutionRefundView = itemView.findViewById(R.id.view_solution);
        tvTitle = itemView.findViewById(R.id.tv_title);
        checkBox = itemView.findViewById(R.id.ic_checkbox);
        extraLayout = itemView.findViewById(R.id.layout_extra);
        middleSeparator = itemView.findViewById(R.id.separator_middle);
    }

    @Override
    public void bind(OngkirCheckboxSolutionModel element) {
        complaintResult = mainView.getComplaintResult(element.getOrder());
        initView(element);
        initViewListener(element);
    }

    private void initView(OngkirSolutionModel model) {
        tvTitle.setText(model.getProblem().getName());
        extraLayout.setVisibility(model.isLastItem() ? View.GONE : View.VISIBLE);
        solutionRefundView.setVisibility(complaintResult.problem.amount != 0 ? View.VISIBLE : View.GONE);
        middleSeparator.setVisibility(checkBox.isChecked()? View.GONE : View.VISIBLE);
        solutionRefundView.bind(model.getProblem(), mainView, complaintResult);
    }

    private void initViewListener(OngkirSolutionModel model) {
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            solutionRefundView.setVisibility(compoundButton.isChecked() ? View.VISIBLE : View.GONE);
            middleSeparator.setVisibility(compoundButton.isChecked() ? View.VISIBLE : View.GONE);
        });
    }
}
