package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ButtonData;

/**
 * Created by hangnadi on 3/8/17.
 */

public class ButtonView extends BaseView<ButtonData, DetailResCenterFragmentView> {

    private ButtonData buttonData;
    private View actionAcceptProduct;
    private View actionAcceptSolution;
    private View actionEdit;

    public ButtonView(Context context) {
        super(context);
    }

    public ButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(DetailResCenterFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_button_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        actionEdit = view.findViewById(R.id.action_edit_solution);
        actionAcceptSolution = view.findViewById(R.id.action_accept_solution);
        actionAcceptProduct = view.findViewById(R.id.action_accept_product);
    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull ButtonData data) {
        setButtonData(data);
        actionEdit.setOnClickListener(new ButtonViewOnClickListener());
        actionEdit.setVisibility(canEdit() ? VISIBLE : GONE);
        actionAcceptProduct.setVisibility(canAcceptProduct() ? VISIBLE : GONE);
        actionAcceptSolution.setVisibility(canAcceptSolution() ? VISIBLE : GONE);
        setVisibility(isAnyButtonVisible() ? VISIBLE : GONE);
    }

    private boolean isAnyButtonVisible() {
        return actionEdit.getVisibility() == VISIBLE ||
                actionAcceptProduct.getVisibility() == VISIBLE ||
                actionAcceptSolution.getVisibility() == VISIBLE;
    }

    public void setButtonData(ButtonData buttonData) {
        this.buttonData = buttonData;
    }

    public ButtonData getButtonData() {
        return buttonData;
    }

    private boolean canAcceptProduct() {
        return getButtonData().isShowAcceptProduct();
    }

    private boolean canEdit() {
        return getButtonData().isShowEdit();
    }

    private boolean canAcceptSolution() {
        return getButtonData().isShowAcceptSolution();
    }

    private class ButtonViewOnClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.action_accept_product) {
                listener.setOnActionAcceptProductClick();
            } else if (view.getId() == R.id.action_accept_solution) {
                listener.setOnActionAcceptSolutionClick();
            } else if (view.getId() == R.id.action_edit) {
                listener.setOnActionEditSolutionClick();
            }
        }
    }
}
