package com.tokopedia.inbox.rescenter.inbox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.inbox.rescenter.inbox.model.ResCenterHeader;
import com.tokopedia.inbox.rescenter.inbox.model.ResCenterInboxItem;
import com.tokopedia.inbox.rescenter.inbox.model.ResolutionDetail;
import com.tokopedia.inbox.rescenter.inbox.model.ResolutionList;

import java.util.List;

/**
 * Created on 4/4/16.
 */
public abstract class ResCenterExtendedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final ResCenterInboxItem loadingItem = new ResCenterInboxItem(ResCenterInboxItem.TYPE_LOADING);
    public static final ResCenterInboxItem noResultItem = new ResCenterInboxItem(ResCenterInboxItem.TYPE_NO_RESULT);
    public static final ResCenterInboxItem retryItem = new ResCenterInboxItem(ResCenterInboxItem.TYPE_RETRY);

    protected Context context;
    protected List<ResCenterInboxItem> list;

    public class InboxViewHolder extends RecyclerView.ViewHolder {

        View mainView;
        TextView labelText;
        TextView username;
        TextView solution;
        TextView invoiceText;
        TextView dateText;
        TextView resCenterStatus;
        ImageView iconStateExpired;
        View freeReturnView;

        public InboxViewHolder(View itemView) {
            super(itemView);
            mainView = itemView;
            labelText = (TextView) itemView.findViewById(R.id.type);
            username = (TextView) itemView.findViewById(R.id.customer);
            solution = (TextView) itemView.findViewById(R.id.solution);
            invoiceText = (TextView) itemView.findViewById(R.id.invoice_text);
            dateText = (TextView) itemView.findViewById(R.id.date);
            resCenterStatus = (TextView) itemView.findViewById(R.id.status);
            iconStateExpired = (ImageView) itemView.findViewById(R.id.expired);
            freeReturnView = itemView.findViewById(R.id.view_free_return);
        }
    }

    public class RetryViewHolder extends RecyclerView.ViewHolder {

        public View retryView;
        public View retryButton;

        public RetryViewHolder(View itemView) {
            super(itemView);
            retryView = itemView;
            retryButton = itemView.findViewById(R.id.main_retry);
        }
    }

    public class NoResultViewHolder extends RecyclerView.ViewHolder {

        public View noResultView;
        public TextView additionalInfoText;

        public NoResultViewHolder(View itemView) {
            super(itemView);
            noResultView = itemView;
            additionalInfoText = (TextView) itemView.findViewById(R.id.info_no_result);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        ImageView iconHeader;
        TextView totalPending;
        TextView amountPending;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            iconHeader = (ImageView) itemView.findViewById(R.id.icon);
            totalPending = (TextView) itemView.findViewById(R.id.desc_summary_day_reso);
            amountPending = (TextView) itemView.findViewById(R.id.desc_summary_amount_reso);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private final View loadingView;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            this.loadingView = itemView;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        switch (viewType) {
            case ResCenterInboxItem.TYPE_MAIN:
                return createMainViewHolder(parent);
            case ResCenterInboxItem.TYPE_NO_RESULT:
                return createNoResultViewHolder(parent);
            case ResCenterInboxItem.TYPE_HEADER:
                return createHeaderViewHolder(parent);
            case ResCenterInboxItem.TYPE_LOADING:
                return createLoadingViewHolder(parent);
            case ResCenterInboxItem.TYPE_RETRY:
                return createRetryViewHolder(parent);
            default:
                throw new RuntimeException("UnSupported View Type: "
                        + ResCenterInboxItem.getItemTypeString(viewType));
        }
    }

    protected NoResultViewHolder createNoResultViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_rescenter_no_result, parent, false);
        return new NoResultViewHolder(view);
    }

    protected LoadingViewHolder createLoadingViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.loading_layout, parent, false);
        return new LoadingViewHolder(view);
    }

    protected HeaderViewHolder createHeaderViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_summary_reso, parent, false);
        return new HeaderViewHolder(view);
    }

    protected RetryViewHolder createRetryViewHolder(ViewGroup parent) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (getItemCount() == 1) {
            view = inflater.inflate(R.layout.design_retry_footer_single, parent, false);
        } else {
            view = inflater.inflate(R.layout.design_retry_footer, parent, false);
        }
        return new RetryViewHolder(view);
    }

    protected InboxViewHolder createMainViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listview_res_center_v2, parent, false);
        return new InboxViewHolder(view);
    }

    private void setFitToWindow(View view) {
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.requestLayout();
    }

    private void setFitToContent(View view) {
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.requestLayout();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ResCenterInboxItem.TYPE_MAIN:
                ResolutionList listModel = (ResolutionList) list.get(position);
                ResolutionDetail detailModel = listModel.getResolutionDetail();

                bindMainView((InboxViewHolder) holder, detailModel);
                setViewByReadStatus((InboxViewHolder) holder, listModel.getResolutionReadStatus() == 1);
                if (listModel.getResolutionDetail().getResolutionBy().getBySeller() == 1) {
                    setListener((InboxViewHolder) holder,
                            listModel.getResolutionDetail().getResolutionLast().getLastResolutionId(),
                            "",
                            listModel.getResolutionDetail().getResolutionCustomer().getCustomerName());
                } else if (listModel.getResolutionDetail().getResolutionBy().getByCustomer() == 1) {
                    setListener((InboxViewHolder) holder,
                            listModel.getResolutionDetail().getResolutionLast().getLastResolutionId(),
                            listModel.getResolutionDetail().getResolutionShop().getShopName(),
                            "");
                }
                break;
            case ResCenterInboxItem.TYPE_NO_RESULT:
                bindNoresultView((NoResultViewHolder) holder);
                break;
            case ResCenterInboxItem.TYPE_HEADER:
                bindHeaderView((HeaderViewHolder) holder, (ResCenterHeader) list.get(position));
                setHeaderVisibility((HeaderViewHolder) holder, (ResCenterHeader) list.get(position));
                break;
            case ResCenterInboxItem.TYPE_LOADING:
                bindLoadingView((LoadingViewHolder) holder);
                break;
            case ResCenterInboxItem.TYPE_RETRY:
                bindRetryView((RetryViewHolder) holder);
                break;
            default:
                throw new RuntimeException("UnSupported View Type: "
                        + ResCenterInboxItem.getItemTypeString(getItemViewType(position)));
        }
    }

    protected abstract void setHeaderVisibility(HeaderViewHolder holder, ResCenterHeader resCenterHeader);

    protected abstract void setListener(InboxViewHolder holder, String resolutionID, String shopName, String username);

    protected void bindLoadingView(LoadingViewHolder holder) {
        if (getItemCount() == 1) {
            setFitToWindow(holder.loadingView);
        } else {
            setFitToContent(holder.loadingView);
        }
    }

    protected void bindNoresultView(NoResultViewHolder holder) {
        Log.d(this.getClass().getSimpleName(), "bindNoresultView: 1");
        if (isCurrentSellerTab()) {
            Log.d(this.getClass().getSimpleName(), "bindNoresultView: 2");
            holder.additionalInfoText.setVisibility(View.GONE);
        } else {
            holder.additionalInfoText.setVisibility(View.VISIBLE);
            if (getItemCount() == 1) {
                Log.d(this.getClass().getSimpleName(), "bindNoresultView: 3");
                setFitToWindow(holder.noResultView);
            } else {
                Log.d(this.getClass().getSimpleName(), "bindNoresultView: 4");
                setFitToContent(holder.noResultView);
            }
        }
    }

    private boolean isCurrentSellerTab() {
        Log.d(this.getClass().getSimpleName(), "isCurrentSellerTab: " + getCurrentTab());
        return getCurrentTab() == TkpdState.InboxResCenter.RESO_BUYER;
    }

    protected abstract int getCurrentTab();

    protected void bindRetryView(RetryViewHolder holder) {
        if (getItemCount() == 1) {
            setFitToWindow(holder.retryView);
        } else {
            setFitToContent(holder.retryView);
        }
    }

    protected abstract void setViewByReadStatus(InboxViewHolder holder, Boolean isRead);

    protected abstract void bindMainView(InboxViewHolder holder, ResolutionDetail model);

    protected abstract void bindHeaderView(HeaderViewHolder holder, ResCenterHeader headerModel);

    public void setLoading(boolean isShowLoading) {
        if (isShowLoading) {
            if (!list.contains(loadingItem)) {
                this.list.add(loadingItem);
                this.notifyDataSetChanged();
            }
        } else {
            this.list.remove(loadingItem);
            this.notifyDataSetChanged();
        }
    }

    public void setNoResult(boolean isShowNoResult) {
        if (isShowNoResult) {
            if (!list.contains(noResultItem)) {
                this.list.add(noResultItem);
                this.notifyDataSetChanged();
            }
        } else {
            this.list.remove(noResultItem);
            this.notifyDataSetChanged();
        }
    }

    public void addRetryOnHeader() {
        if (!list.contains(retryItem)) {
            /*if (getItemViewType(0) == ResCenterInboxItem.TYPE_HEADER) {
                this.list.add(1, retryItem);
            } else {
                this.list.add(0, retryItem);
            }*/
            this.list.clear();
            this.list.add(retryItem);
            this.notifyDataSetChanged();
        }
    }

    public void addRetryOnBottom() {
        if (!list.contains(retryItem)) {
            this.list.add(retryItem);
            this.notifyDataSetChanged();
        }
    }

    public void removeRetry() {
        this.list.remove(retryItem);
        this.notifyDataSetChanged();
    }
}
