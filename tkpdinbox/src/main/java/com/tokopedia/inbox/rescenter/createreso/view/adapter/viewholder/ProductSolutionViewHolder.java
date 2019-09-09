package com.tokopedia.inbox.rescenter.createreso.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionDetailFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.ProductSolutionModel;

/**
 * @author by yfsx on 09/08/18.
 */
public class ProductSolutionViewHolder extends AbstractViewHolder<ProductSolutionModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_product_solution;

    private SolutionDetailFragmentListener.View mainView;
    private ImageView ivImage;
    private TextView tvTitle, tvQty;
    private LinearLayout llQty;
    private BaseSolutionRefundView solutionRefundView;

    public ProductSolutionViewHolder(View itemView, SolutionDetailFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        solutionRefundView = itemView.findViewById(R.id.view_solution);
        ivImage = itemView.findViewById(R.id.iv_product_image);
        tvTitle = itemView.findViewById(R.id.tv_title);
        llQty = itemView.findViewById(R.id.ll_qty);
        tvQty = itemView.findViewById(R.id.tv_qty);
    }

    @Override
    public void bind(ProductSolutionModel element) {
        initView(element);
        initViewListener(element);
    }

    private void initView(ProductSolutionModel model) {
        ImageHandler.LoadImage(ivImage, model.getProduct().getImage().getThumb());
        tvTitle.setText(model.getProduct().getName());
        tvQty.setText(String.valueOf(model.getProblem().getQty()));
        solutionRefundView.bind(model.getProblem(), mainView, mainView.getComplaintResult(model.getOrder()));
        mainView.initCheckedItem();
    }

    private void initViewListener(ProductSolutionModel model) {

    }
}
