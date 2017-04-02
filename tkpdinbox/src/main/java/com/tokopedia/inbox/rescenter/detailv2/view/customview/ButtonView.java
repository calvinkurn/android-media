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
    private View horizontalButtonView;
    private View actionAcceptProduct;
    private View actionEdit;
    private View actionHelp;
    private View actionAcceptSolutionVertical;
    private View actionAcceptSolutionHorizontal;
    private View actionCancelResolutionHorizontal;
    private View actionCancelResolutionVertical;
    private View actionInputAwbNumber;

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
        actionAcceptSolutionVertical = view.findViewById(R.id.action_accept_solution);
        actionAcceptSolutionHorizontal = view.findViewById(R.id.action_accept_solution_double);
        actionAcceptProduct = view.findViewById(R.id.action_accept_product);
        actionHelp = view.findViewById(R.id.action_help);
        actionCancelResolutionVertical = view.findViewById(R.id.action_cancel_resolution);
        actionCancelResolutionHorizontal = view.findViewById(R.id.action_cancel_resolution_double);
        horizontalButtonView = view.findViewById(R.id.view_double_button_seller);
    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull ButtonData data) {
        setButtonData(data);
        setClickListener();
        setActionVisibility();
        setVisibility(isAnyButtonVisible() ? VISIBLE : GONE);
    }

    private void setActionVisibility() {
        actionEdit.setVisibility(canEdit() || canAppealSolution() ? VISIBLE : GONE);
        actionAcceptProduct.setVisibility(canAcceptProduct() ? VISIBLE : GONE);
        actionHelp.setVisibility(canAskHelp() ? VISIBLE : GONE);

        actionAcceptSolutionHorizontal.setVisibility(
                isShowAcceptSolutionButton() &&  validToActionAcceptSolutionHorizontalView() ? VISIBLE : GONE);
        actionAcceptSolutionVertical.setVisibility(isShowAcceptSolutionButton()
                && actionAcceptSolutionHorizontal.getVisibility() != VISIBLE ? VISIBLE : GONE);

        actionCancelResolutionHorizontal.setVisibility(
                isShowCancelResolutionButton() &&  validToActionCancelHorizontalView() ? VISIBLE : GONE);
        actionCancelResolutionVertical.setVisibility(isShowCancelResolutionButton()
                && actionCancelResolutionHorizontal.getVisibility() != VISIBLE ? VISIBLE : GONE);

        horizontalButtonView.setVisibility(
                isAnyButtonHorizontalVisible() ? VISIBLE : GONE
        );

        actionInputAwbNumber.setVisibility(
            isShowInputAwbNumber() ? VISIBLE : GONE
        );
    }

    private boolean isAnyButtonHorizontalVisible() {
        return actionEdit.getVisibility() == VISIBLE ||
                actionAcceptSolutionHorizontal.getVisibility() == VISIBLE ||
                actionCancelResolutionHorizontal.getVisibility() == VISIBLE ||
                actionHelp.getVisibility() == VISIBLE;
    }

    private boolean validToActionCancelHorizontalView() {
        return !(actionEdit.getVisibility() == VISIBLE && actionHelp.getVisibility() == VISIBLE);
    }

    private boolean validToActionAcceptSolutionHorizontalView() {
        return !(actionEdit.getVisibility() == VISIBLE && actionHelp.getVisibility() == VISIBLE);
    }

    private void setClickListener() {
        actionEdit.setOnClickListener(new ActionEditSolutionClickListener());
        actionAcceptProduct.setOnClickListener(new ActionAcceptProductClickListener());
        actionHelp.setOnClickListener(new ActionHelpClickListener());

        actionAcceptSolutionVertical.setOnClickListener(new ActionAcceptSolutionClickListener());
        actionAcceptSolutionHorizontal.setOnClickListener(new ActionAcceptSolutionClickListener());

        actionCancelResolutionVertical.setOnClickListener(new ActionCancelResolutionClickListener(listener));
        actionCancelResolutionHorizontal.setOnClickListener(new ActionCancelResolutionClickListener(listener));
        actionInputAwbNumber.setOnClickListener(new ActionInputAwbNumberClickListener());
    }

    private boolean isAnyButtonVisible() {
        return actionEdit.getVisibility() == VISIBLE ||
                actionAcceptProduct.getVisibility() == VISIBLE ||
                actionAcceptSolutionVertical.getVisibility() == VISIBLE ||
                actionAcceptSolutionHorizontal.getVisibility() == VISIBLE ||
                actionCancelResolutionHorizontal.getVisibility() == VISIBLE ||
                actionCancelResolutionVertical.getVisibility() == VISIBLE ||
                actionHelp.getVisibility() == VISIBLE;
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

    private boolean isShowCancelResolutionButton() {
        return canCancelSolution();
    }

    private boolean canCancelSolution() {
        return getButtonData().isShowCancel();
    }

    private boolean isShowAcceptSolutionButton() {
        return canAcceptSolution() || canAcceptAdminSolution();
    }

    private boolean canAcceptSolution() {
        return getButtonData().isShowAcceptSolution();
    }

    private boolean canAcceptAdminSolution() {
        return getButtonData().isShowAcceptAdminSolution();
    }

    private boolean canAskHelp() {
        return getButtonData().isShowAskHelp();
    }

    private boolean canAppealSolution() {
        return getButtonData().isShowAppealSolution();
    }

    private boolean isShowInputAwbNumber() {
        return canInputAwbNumber();
    }

    private boolean canInputAwbNumber() {
        return getButtonData().isShowInputAwb();
    }

    private class ActionAcceptSolutionClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (canAcceptSolution() && !canAcceptAdminSolution()) {
                listener.setOnActionAcceptSolutionClick();
            } else if (canAcceptAdminSolution() && !canAcceptSolution()){
                listener.setOnActionAcceptAdminSolutionClick();
            }
        }
    }

    private class ActionEditSolutionClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (canEdit() && !canAppealSolution()) {
                listener.setOnActionEditSolutionClick();
            } else if (canAppealSolution() && !canEdit()) {
                listener.setOnActionAppealClick();
            }
        }
    }

    private class ActionAcceptProductClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            listener.setOnActionAcceptProductClick();
        }
    }

    private class ActionHelpClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            listener.setOnActionHelpClick();
        }
    }

    private class ActionInputAwbNumberClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            listener.setOnActionInputAwbNumberClick();
        }
    }
    public static class ActionCancelResolutionClickListener implements OnClickListener {
        private final DetailResCenterFragmentView listener;

        public ActionCancelResolutionClickListener(DetailResCenterFragmentView listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.setOnActionCancelResolutionClick();
        }
    }
}
