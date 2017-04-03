package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ButtonData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.StatusData;

/**
 * Created by hangnadi on 3/8/17.
 */

public class StatusView extends BaseView<StatusData, DetailResCenterFragmentView> {

    private TextView lastStatus;
    private View actionDiscuss;
    private View actionResponse;
    private ButtonData buttonData;

    private BottomSheetDialog dialog;
    private View separator;
    private View actionEdit;
    private View actionAcceptProduct;
    private View actionAcceptSolutionVertical;
    private View actionHelp;
    private View actionCancelResolutionVertical;
    private View actionInputAwbNumber;

    public StatusView(Context context) {
        super(context);
    }

    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(DetailResCenterFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_status_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        lastStatus = (TextView) view.findViewById(R.id.textview_last_status);
        actionDiscuss = view.findViewById(R.id.action_discuss);
        actionResponse = view.findViewById(R.id.action_response);
        separator = view.findViewById(R.id.separator);
    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    private boolean isAnyButtonVisible() {
        return actionEdit.getVisibility() == VISIBLE ||
                actionAcceptProduct.getVisibility() == VISIBLE ||
                actionAcceptSolutionVertical.getVisibility() == VISIBLE ||
                actionCancelResolutionVertical.getVisibility() == VISIBLE ||
                actionHelp.getVisibility() == VISIBLE ||
                actionInputAwbNumber.getVisibility() == VISIBLE;
    }

    @Override
    public void renderData(@NonNull StatusData data) {
        setVisibility(VISIBLE);
        lastStatus.setText(data.getStatusText());
        actionDiscuss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setOnActionDiscussClick();
            }
        });

        setButton();
        actionResponse.setVisibility(isAnyButtonVisible() ? VISIBLE : GONE);
        actionResponse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }

    private void setButton() {
        dialog = new BottomSheetDialog(getContext());
        View bottomSheetView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.layout_rescenter_button_view, null);

        actionEdit = bottomSheetView.findViewById(R.id.action_edit_solution);
        actionAcceptSolutionVertical = bottomSheetView.findViewById(R.id.action_accept_solution);
        actionAcceptProduct = bottomSheetView.findViewById(R.id.action_accept_product);
        actionHelp = bottomSheetView.findViewById(R.id.action_help);
        actionCancelResolutionVertical = bottomSheetView.findViewById(R.id.action_cancel_resolution);
        actionInputAwbNumber = bottomSheetView.findViewById(R.id.action_input_awb_number);
        separator = bottomSheetView.findViewById(R.id.separator);

        actionEdit.setOnClickListener(new ActionEditSolutionClickListener());
        actionAcceptProduct.setOnClickListener(new ActionAcceptProductClickListener());
        actionHelp.setOnClickListener(new ActionHelpClickListener());
        actionAcceptSolutionVertical.setOnClickListener(new ActionAcceptSolutionClickListener());
        actionCancelResolutionVertical.setOnClickListener(new ActionCancelResolutionClickListener());
        actionInputAwbNumber.setOnClickListener(new ActionInputAwbNumberClickListener());

        actionEdit.setVisibility(
                canEdit() || canAppealSolution() ?
                        VISIBLE : GONE
        );

        actionAcceptProduct.setVisibility(
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

        separator.setVisibility(
                actionEdit.getVisibility() == VISIBLE && actionHelp.getVisibility() == VISIBLE ?
                        VISIBLE : GONE
        );

        dialog.setContentView(bottomSheetView);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog dialog = (BottomSheetDialog) dialogInterface;
                FrameLayout frameLayout = (FrameLayout) dialog.findViewById(R.id.design_bottom_sheet);
                if (frameLayout != null) {
                    BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(frameLayout);
                    behavior.setHideable(false);
                }
            }
        });
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
            dialog.dismiss();
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
            dialog.dismiss();
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
            dialog.dismiss();
            listener.setOnActionAcceptProductClick();
        }
    }

    private class ActionHelpClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
            listener.setOnActionHelpClick();
        }
    }

    private class ActionInputAwbNumberClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
            listener.setOnActionInputAwbNumberClick();
        }
    }

    private class ActionCancelResolutionClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            dialog.dismiss();
            listener.setOnActionCancelResolutionClick();
        }
    }
}
