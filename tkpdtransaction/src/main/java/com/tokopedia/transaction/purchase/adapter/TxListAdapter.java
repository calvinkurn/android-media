package com.tokopedia.transaction.purchase.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.OneOnClick;
import com.tokopedia.core.customView.TextViewCopyable;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.purchase.fragment.TxListFragment;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Angga.Prasetiyo on 21/04/2016.
 */
public class TxListAdapter extends ArrayAdapter<OrderData> {
    private static final String HAS_BUTTON = "1";
    private final LayoutInflater inflater;
    private final int instanceType;
    private final Context context;
    private final ActionListener actionListener;

    public interface ActionListener {
        void actionToDetail(OrderData data);

        void actionInvoice(OrderData data);

        void actionConfirmDeliver(OrderData data);

        void actionComplainConfirmDeliver(OrderData orderData);

        void actionTrackOrder(OrderData data);

        void actionReject(OrderData data);

        void actionDispute(OrderData orderData, int state);

        void actionShowComplain(OrderData orderData);

        void actionCopyResiNumber(String resiNumber);

        void actionCancelReplacement(OrderData orderData);

        void actionComplain(OrderData orderData);
    }

    public TxListAdapter(Context context, int instanceType, ActionListener actionListener) {
        super(context, R.layout.holder_item_transaction_list_tx_module, new ArrayList<OrderData>());
        this.context = context;
        this.actionListener = actionListener;
        this.inflater = LayoutInflater.from(context);
        this.instanceType = instanceType;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.holder_item_transaction_list_tx_module, parent, false
            );
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OrderData item = getItem(position);
        renderButtonOverflow(holder, item);
        renderShippingReferenceNumber(holder, item);
        renderOthersNormalContent(holder, item);

        registerViewClickListener(holder, item);
        return convertView;
    }

    private void registerViewClickListener(ViewHolder holder, final OrderData item) {
        holder.btnOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, item);
            }
        });
        holder.tvInvoice.setOnClickListener(new OneOnClick() {
            @Override
            public void oneOnClick(View view) {
                actionListener.actionInvoice(item);
            }
        });

        holder.mainView.setOnClickListener(new OneOnClick() {
            @Override
            public void oneOnClick(View view) {
                actionListener.actionToDetail(item);
            }
        });

        holder.tvReceiveButton.setOnClickListener(new OneOnClick() {
            @Override
            public void oneOnClick(View view) {
                actionListener.actionConfirmDeliver(item);
            }
        });
        holder.tvTrackButton.setOnClickListener(new OneOnClick() {
            @Override
            public void oneOnClick(View view) {
                actionListener.actionTrackOrder(item);
            }
        });
        holder.tvRejectButton.setOnClickListener(new OneOnClick() {
            @Override
            public void oneOnClick(View view) {
                actionListener.actionReject(item);
            }
        });

        holder.tvRefNum.setOnCopiedListener(new TextViewCopyable.OnCopiedListener() {
            @Override
            public void onCopied(String textCopied) {
                actionListener.actionCopyResiNumber(textCopied);
            }
        });
    }

    private void renderOthersNormalContent(ViewHolder holder, OrderData item) {
        ImageHandler.loadImageCircle2(context, holder.imgShopAvatar, item.getOrderShop().getShopPic());
        String shopName = context.getString(R.string.title_buy_from)
                + " : " + MethodChecker.fromHtml(item.getOrderShop().getShopName());
        holder.tvShopName.setText(shopName);
        holder.tvStatus.setText(MethodChecker.fromHtml(item.getOrderLast().getLastBuyerStatus()));
        holder.tvInvoice.setText(item.getOrderDetail().getDetailInvoice());
        holder.tvDate.setText(MethodChecker.fromHtml(item.getOrderDetail().getDetailOrderDate()));

        holder.tvUploadTx.setVisibility(View.GONE);
        holder.tvPreOrder.setVisibility(item.getOrderDetail().getDetailPreorder() != null
                && item.getOrderDetail().getDetailPreorder().getPreorderStatus() == 1
                ? View.VISIBLE : View.GONE);
    }

    private void renderShippingReferenceNumber(ViewHolder holder, OrderData item) {
        if (item.getOrderDetail().getDetailShipRefNum() != null
                && !item.getOrderDetail().getDetailShipRefNum().equals("0")
                && !item.getOrderDetail().getDetailShipRefNum().equals("")) {
            holder.refAreaView.setVisibility(View.VISIBLE);
            holder.tvRefNum.setText(item.getOrderDetail().getDetailShipRefNum());
        } else {
            holder.refAreaView.setVisibility(View.GONE);
        }
    }

    private void renderButtonOverflow(ViewHolder holder, OrderData item) {
        switch (instanceType) {
            case TxListFragment.INSTANCE_STATUS:
                switch (Integer.parseInt(item.getOrderDetail().getDetailOrderStatus())) {
                    case TkpdState.OrderStatusState.ORDER_SHIPPING:
                    case TkpdState.OrderStatusState.ORDER_SHIPPING_REF_NUM_EDITED:
                    case TkpdState.OrderStatusState.ORDER_SHIPPING_TRACKER_INVALID:
                    case TkpdState.OrderStatusState.ORDER_WAITING_STATUS_FROM_SHIPPING_AGENCY:
                    case TkpdState.OrderStatusState.ORDER_CONFLICTED:
                        holder.btnOverflow.setVisibility(View.VISIBLE);
                        break;
                    default:
                        holder.btnOverflow.setVisibility(View.GONE);
                        break;
                }
                break;
            case TxListFragment.INSTANCE_RECEIVE:
                holder.btnOverflow.setVisibility(View.VISIBLE);
                break;
            default:
                holder.btnOverflow.setVisibility(View.GONE);
                break;
        }

        if (hasCancelReplacementButton(item))
            holder.btnOverflow.setVisibility(View.VISIBLE);
    }

    private boolean hasCancelReplacementButton(OrderData item) {
        return item != null &&
                item.getOrderButton() != null &&
                item.getOrderButton().getButtonCancelReplacement() != null &&
                item.getOrderButton().getButtonCancelReplacement().equals(HAS_BUTTON);
    }

    private void showPopup(View v, final OrderData item) {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(getMenuId(item), popup.getMenu());
        if (getMenuId(item) != R.menu.order_status_menu_cancel_replacement)
            addCancelReplacementMenu(item, popup);
        popup.setOnMenuItemClickListener(new OnMenuPopupClicked(item));
        popup.show();
    }

    private void addCancelReplacementMenu(OrderData item, PopupMenu popup) {
        if (hasCancelReplacementButton(item)) {
            popup.getMenu().add(Menu.NONE, R.id.action_cancel_replacement, Menu.NONE, getContext()
                    .getString(R.string.cancel_replacement));
        }
    }

    private int getMenuId(OrderData item) {
        int MenuID = 0;
        if (hasCancelReplacementButton(item))
            MenuID = R.menu.order_status_menu_cancel_replacement;

        switch (instanceType) {
            case TxListFragment.INSTANCE_RECEIVE:
                switch (Integer.parseInt(item.getOrderDetail().getDetailOrderStatus())) {
                    case TkpdState.OrderStatusState.ORDER_CONFLICTED:
                        MenuID = R.menu.order_status_menu_show_complain;
                        break;
                    default:
                        MenuID = (item.getOrderButton().getButtonComplaintReceived().equals(HAS_BUTTON)
                                || item.getOrderButton().getButtonComplaintNotReceived().equals(HAS_BUTTON))
                                ? R.menu.order_status_menu_confirm_complain
                                : R.menu.order_status_menu_confirm_reject;
                        break;
                }
                break;
            case TxListFragment.INSTANCE_STATUS:
                switch (Integer.parseInt(item.getOrderDetail().getDetailOrderStatus())) {
                    case TkpdState.OrderStatusState.ORDER_SHIPPING:
                    case TkpdState.OrderStatusState.ORDER_WAITING_STATUS_FROM_SHIPPING_AGENCY:
                    case TkpdState.OrderStatusState.ORDER_SHIPPING_REF_NUM_EDITED:
                    case TkpdState.OrderStatusState.ORDER_SHIPPING_TRACKER_INVALID:
                        MenuID = (item.getOrderButton().getButtonComplaintReceived().equals(HAS_BUTTON)
                                || item.getOrderButton().getButtonComplaintNotReceived().equals(HAS_BUTTON))
                                ? R.menu.order_status_menu_confirm_track_complain
                                : R.menu.order_status_menu_confirm_track_v2;
                        break;
                    case TkpdState.OrderStatusState.ORDER_CONFLICTED:
                        MenuID = R.menu.order_status_menu_show_complain;
                        break;


                    default:
                        if (item.getOrderButton().getButtonUploadProof().equals(HAS_BUTTON))
                            MenuID = R.menu.order_status_menu_upload;
                        break;
                }
                break;
        }

        return MenuID;
    }


    class ViewHolder {
        @BindView(R2.id.tv_preorder)
        TextView tvPreOrder;
        @BindView(R2.id.shop_avatar)
        ImageView imgShopAvatar;
        @BindView(R2.id.but_overflow)
        View btnOverflow;
        @BindView(R2.id.shop_name)
        TextView tvShopName;
        @BindView(R2.id.invoice_text)
        TextView tvInvoice;
        @BindView(R2.id.date)
        TextView tvDate;
        @BindView(R2.id.upload_button)
        TextView tvUploadTx;
        @BindView(R2.id.status)
        TextView tvStatus;
        @BindView(R2.id.receive_button)
        TextView tvReceiveButton;
        @BindView(R2.id.reject_button)
        TextView tvRejectButton;
        @BindView(R2.id.track_button)
        TextView tvTrackButton;
        @BindView(R2.id.main_view)
        View mainView;
        @BindView(R2.id.ref_area)
        View refAreaView;
        @BindView(R2.id.reference_num)
        TextViewCopyable tvRefNum;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private class OnMenuPopupClicked implements PopupMenu.OnMenuItemClickListener {
        private final OrderData orderData;

        OnMenuPopupClicked(OrderData item) {
            this.orderData = item;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.action_track) {
                actionListener.actionTrackOrder(orderData);
                return true;
            } else if (item.getItemId() == R.id.action_confirm_package) {
                actionListener.actionConfirmDeliver(orderData);
                return true;
            } else if (item.getItemId() == R.id.action_complain_confirm_package) {
                actionListener.actionComplainConfirmDeliver(orderData);
                return true;
            } else if (item.getItemId() == R.id.action_open_dispute) {
                actionListener.actionDispute(orderData, 0);
                return true;
            } else if (item.getItemId() == R.id.action_show_complain) {
                actionListener.actionShowComplain(orderData);
                return true;
            } else if (item.getItemId() == R.id.action_cancel_replacement) {
                actionListener.actionCancelReplacement(orderData);
                return true;
            } else if (item.getItemId() == R.id.action_complain) {
                actionListener.actionComplain(orderData);
                return true;
            } else {
                return false;
            }
        }
    }
}
