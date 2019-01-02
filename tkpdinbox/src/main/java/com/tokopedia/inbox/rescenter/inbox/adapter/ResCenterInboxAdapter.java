package com.tokopedia.inbox.rescenter.inbox.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.inbox.rescenter.inbox.model.ResCenterCounterPending;
import com.tokopedia.inbox.rescenter.inbox.model.ResCenterHeader;
import com.tokopedia.inbox.rescenter.inbox.model.ResCenterInboxItem;
import com.tokopedia.inbox.rescenter.inbox.model.ResolutionDetail;
import com.tokopedia.inbox.rescenter.inbox.presenter.InboxResCenterPresenter;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 4/1/16.
 */
public class ResCenterInboxAdapter extends ResCenterExtendedAdapter {

    private final InboxResCenterPresenter presenter;

    public ResCenterInboxAdapter(List<ResCenterInboxItem> list, InboxResCenterPresenter presenter) {
        this.list = list;
        this.presenter = presenter;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getItemType();
    }

    @Override
    protected void setViewByReadStatus(InboxViewHolder holder, Boolean isRead) {

    }

    @Override
    protected void bindMainView(InboxViewHolder holder, ResolutionDetail model) {
        setUserName(holder.username, model);
        setSolutionText(holder.solution, model.getResolutionLast().getLastSolutionString());
        setInvoiceText(holder.invoiceText, model.getResolutionOrder().getOrderInvoiceRefNum());
        setDateText(holder.dateText, model.getResolutionDispute().getDisputeUpdateTimeShort());
        setLabelText(holder.labelText, model.getResolutionBy().getUserLabel() + ":");
        setStatusText(holder.resCenterStatus, model.getResolutionDispute().getDisputeStatus());
        setViewByStateStatus(holder.iconStateExpired, model.getResolutionDispute().getDispute30Days() == 1);
        setViewByStateStatus(holder.freeReturnView, !(model.getResolutionOrder().getOrderFreeReturn() == null || model.getResolutionOrder().getOrderFreeReturn() == 0));
    }

    @Override
    protected void setListener(InboxViewHolder holder, final String resolutionID, final String shopName, final String username) {
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventResolutionDetail(view.getContext());
                presenter.setActionOnItemListClickListener(view.getContext(), resolutionID, shopName, username);
            }
        });
    }

    private void setViewByStateStatus(View view, boolean show) {
        if (show) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void setStatusText(TextView statusText, Integer disputeStatus) {
        statusText.setBackgroundColor(ContextCompat.getColor(context, TkpdState.ResCenterStatus.getColorStatus(disputeStatus)));
        statusText.setText(TkpdState.ResCenterStatus.getStringRes(disputeStatus));
    }

    private void setLabelText(TextView labelText, String label) {
        labelText.setText(label);
    }

    private void setDateText(TextView dateText, String disputeUpdateTimeShort) {
        dateText.setText(disputeUpdateTimeShort);
    }

    private void setInvoiceText(TextView invoiceText, String orderInvoiceRefNum) {
        invoiceText.setText(orderInvoiceRefNum);
    }

    private void setSolutionText(TextView solutionText, String lastSolutionText) {
        solutionText.setText(lastSolutionText);
    }

    private void setUserName(TextView customerName, ResolutionDetail detailModel) {
        if (detailModel.getResolutionBy().getByCustomer() == 1) {
            customerName.setText(detailModel.getResolutionShop().getShopName());
        } else {
            customerName.setText(detailModel.getResolutionCustomer().getCustomerName());
        }
    }

    @Override
    protected void bindHeaderView(HeaderViewHolder holder, ResCenterHeader headerModel) {
        ResCenterCounterPending counterPendingModel = headerModel.getCounterPending();
        String pendingAmountModel = headerModel.getPendingAmount();

        if (counterPendingModel != null) {
            String valueTotalPending = context.getString(R.string.desc_summary_day_reso).replace("XXX", String.valueOf(counterPendingModel.getTotalList())).replace("YYY", counterPendingModel.getPendingDays());
            holder.totalPending.setText(MethodChecker.fromHtml(valueTotalPending));
        }

        if (pendingAmountModel != null) {
            String valueAmountPending = context.getString(R.string.desc_summary_amount_reso).replace("ZZZ", pendingAmountModel);
            holder.amountPending.setText(MethodChecker.fromHtml(valueAmountPending));
        }
    }

    @Override
    protected void setHeaderVisibility(HeaderViewHolder holder, ResCenterHeader headerModel) {
        ResCenterCounterPending counterPendingModel = headerModel.getCounterPending();
        String pendingAmountModel = headerModel.getPendingAmount();

        if (counterPendingModel != null) {
            holder.totalPending.setVisibility(View.VISIBLE);
            holder.iconHeader.setVisibility(View.VISIBLE);
        } else {
            holder.totalPending.setVisibility(View.GONE);
            holder.iconHeader.setVisibility(View.INVISIBLE);
        }

        if (pendingAmountModel != null) {
            holder.amountPending.setVisibility(View.VISIBLE);
        } else {
            holder.amountPending.setVisibility(View.GONE);
        }
    }

    @Override
    protected void bindRetryView(RetryViewHolder holder) {

    }

    @Override
    protected void bindNoresultView(NoResultViewHolder holder) {
        super.bindNoresultView(holder);
        SpannableString stringNoResult = new SpannableString(context.getString(R.string.msg_no_res_center));

        String linkStatus = context.getString(R.string.msg_no_res_center1);
        String linkTransactions = context.getString(R.string.msg_no_res_center2);

        stringNoResult.setSpan(redirect(TransactionPurchaseRouter.createIntentPurchaseActivity(context), TransactionPurchaseRouter.TAB_POSITION_PURCHASE_STATUS_ORDER), stringNoResult.toString().indexOf(linkStatus), stringNoResult.toString().indexOf(linkStatus) + linkStatus.length(), 0);
        stringNoResult.setSpan(redirect(TransactionPurchaseRouter.createIntentPurchaseActivity(context), TransactionPurchaseRouter.TAB_POSITION_PURCHASE_ALL_ORDER), stringNoResult.toString().indexOf(linkTransactions), stringNoResult.toString().indexOf(linkTransactions) + linkTransactions.length(), 0);

        holder.additionalInfoText.setMovementMethod(LinkMovementMethod.getInstance());
        holder.additionalInfoText.setText(stringNoResult);
    }

    private ClickableSpan redirect(final Intent intent, final int stateTab) {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Bundle bundle = new Bundle();
                bundle.putInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION, stateTab);
                intent.putExtras(bundle);
                context.startActivity(intent);
                ((Activity) context).finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
                ds.setColor(ContextCompat.getColor(context, R.color.blue_link));
            }
        };
    }

    public void setList(ArrayList<ResCenterInboxItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    protected int getCurrentTab() {
        return presenter.getResCenterTabModel().typeFragment;
    }
}
