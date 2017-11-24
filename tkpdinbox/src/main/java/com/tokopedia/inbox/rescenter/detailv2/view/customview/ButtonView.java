package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ButtonData;

/**
 * Created by hangnadi on 3/8/17.
 */

public class ButtonView extends BaseView<ButtonData, DetailResCenterFragmentView> {

    private ButtonData buttonData;
    private TextView actionEdit;
    private TextView actionFinishResolution;
    private TextView actionAcceptSolutionVertical;
    private TextView actionHelp;
    private TextView actionCancelResolutionVertical;
    private TextView actionInputAwbNumber;
    private TextView actionInputAddress;

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
        return R.layout.layout_rescenter_button_bottom_sheet_view;
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
        actionFinishResolution = view.findViewById(R.id.action_accept_product);
        actionHelp = view.findViewById(R.id.action_help);
        actionCancelResolutionVertical = view.findViewById(R.id.action_cancel_resolution);
        actionInputAwbNumber = view.findViewById(R.id.action_input_awb_number);
        actionInputAddress = view.findViewById(R.id.action_input_address);

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull ButtonData data) {
        setButtonData(data);
        setButtonText(data);
        setButtonSheetDialog();
        setVisibility(isAnyButtonVisible() ? VISIBLE : GONE);
        listener.setOnDiscussionButtonPosition(isAnyButtonVisible());
    }

    private void setButtonText(ButtonData data) {
        actionEdit.setText(data.getEditLabel());
        actionFinishResolution.setText(data.getFinishComplaintLabel());
        actionHelp.setText(data.getAskHelpLabel());
        actionAcceptSolutionVertical.setText(data.getAcceptLabel());
        actionCancelResolutionVertical.setText(data.getCancelLabel());
        actionInputAwbNumber.setText(data.getInputAwbLabel());
        actionInputAddress.setText(data.getInputAddressLabel());
    }

    private void setButtonSheetDialog() {

        actionEdit.setOnClickListener(new ActionEditSolutionClickListener());
        actionFinishResolution.setOnClickListener(new ActionFinishResolutionClickListener());
        actionHelp.setOnClickListener(new ActionHelpClickListener());
        actionAcceptSolutionVertical.setOnClickListener(new ActionAcceptSolutionClickListener());
        actionCancelResolutionVertical.setOnClickListener(new ActionCancelResolutionClickListener());
        actionInputAwbNumber.setOnClickListener(new ActionInputAwbNumberClickListener());
        actionInputAddress.setOnClickListener(new ActionInputAddressClickListener());

        actionEdit.setVisibility(
                canEdit() || canAppealSolution() ?
                        VISIBLE : GONE
        );

        actionFinishResolution.setVisibility(
                canAcceptProduct() ?
                        VISIBLE : GONE
        );

        actionHelp.setVisibility(
                canAskHelp() ?
                        VISIBLE : GONE
        );

        actionAcceptSolutionVertical.setVisibility(
                isShowAcceptSolutionButton() ?
                        VISIBLE : GONE
        );

        actionCancelResolutionVertical.setVisibility(
                isShowCancelResolutionButton() ?
                        VISIBLE : GONE
        );

        actionInputAwbNumber.setVisibility(
                isShowInputAwbNumber() ?
                        VISIBLE : GONE
        );

        actionInputAddress.setVisibility(
                isShowInputAddress() ?
                        VISIBLE : GONE
        );

        if (actionInputAwbNumber.getVisibility() == VISIBLE ||
                actionFinishResolution.getVisibility() == VISIBLE ||
                actionAcceptSolutionVertical.getVisibility() == VISIBLE ||
                actionInputAddress.getVisibility() == VISIBLE) {
            actionCancelResolutionVertical.setBackgroundResource(R.drawable.btn_transparent_disable);
            actionCancelResolutionVertical.setTextColor(ContextCompat.getColor(getContext(), R.color.tkpd_main_green));
        } else {
            actionCancelResolutionVertical.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tkpd_main_green));
            actionCancelResolutionVertical.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }

        if (actionFinishResolution.getVisibility() == VISIBLE ||
                actionAcceptSolutionVertical.getVisibility() == VISIBLE) {
            actionInputAwbNumber.setBackgroundResource(R.drawable.btn_transparent_disable);
            actionInputAwbNumber.setTextColor(ContextCompat.getColor(getContext(), R.color.tkpd_main_green));
        } else {
            actionInputAwbNumber.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tkpd_main_green));
            actionInputAwbNumber.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }
    }

    private boolean isAnyButtonVisible() {
        return actionEdit.getVisibility() == VISIBLE ||
                actionFinishResolution.getVisibility() == VISIBLE ||
                actionAcceptSolutionVertical.getVisibility() == VISIBLE ||
                actionCancelResolutionVertical.getVisibility() == VISIBLE ||
                actionHelp.getVisibility() == VISIBLE ||
                actionInputAwbNumber.getVisibility() == VISIBLE ||
                actionInputAddress.getVisibility() == VISIBLE;
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

    private boolean isShowInputAddress() {
        return canInputAddress();
    }

    private boolean canInputAddress() {
        return getButtonData().isShowInputAddress();
    }

    private class ActionAcceptSolutionClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (canAcceptSolution() && !canAcceptAdminSolution()) {
                listener.setOnActionAcceptSolutionClick();
            } else if (canAcceptAdminSolution() && !canAcceptSolution()) {
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

    private class ActionFinishResolutionClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            listener.setOnActionFinishResolutionClick();
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

    private class ActionCancelResolutionClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            listener.setOnActionCancelResolutionClick();
        }
    }

    private class ActionInputAddressClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            listener.setOnActionInputAddressClick();
        }
    }
}
