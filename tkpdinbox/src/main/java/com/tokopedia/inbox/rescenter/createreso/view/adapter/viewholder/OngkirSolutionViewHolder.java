package com.tokopedia.inbox.rescenter.createreso.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionDetailFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.OngkirSolutionModel;

/**
 * @author by yfsx on 09/08/18.
 */
public class OngkirSolutionViewHolder extends AbstractViewHolder<OngkirSolutionModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_ongkir_solution;

    private SolutionDetailFragmentListener.View mainView;
    private ImageView ivImage;
    private TextView tvTitle;
    private LinearLayout llQty;
    private BaseSolutionRefundView solutionRefundView;
    private RelativeLayout extraLayout;

    public OngkirSolutionViewHolder(View itemView, SolutionDetailFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        solutionRefundView = itemView.findViewById(R.id.view_solution);
        ivImage = itemView.findViewById(R.id.iv_product_image);
        tvTitle = itemView.findViewById(R.id.tv_title);
        llQty = itemView.findViewById(R.id.ll_qty);
        extraLayout = itemView.findViewById(R.id.layout_extra);
    }

    @Override
    public void bind(OngkirSolutionModel element) {
        initView(element);
        initViewListener(element);
    }

    private void initView(OngkirSolutionModel model) {
        ivImage.setImageDrawable(ContextCompat.getDrawable(mainView.getContext(), R.drawable.ic_ongkir));
        tvTitle.setText(model.getProblem().getName());
        llQty.setVisibility(View.GONE);
        solutionRefundView.bind(model.getProblem(), mainView, mainView.getComplaintResult(model.getOrder()));
        extraLayout.setVisibility(model.isLastItem() ? View.GONE : View.VISIBLE);
    }

    private void initViewListener(OngkirSolutionModel model) {

    }
}
