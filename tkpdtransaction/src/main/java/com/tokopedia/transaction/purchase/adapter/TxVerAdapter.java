package com.tokopedia.transaction.purchase.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tkpd.library.utils.OneOnClick;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.purchase.activity.PaymentProcedureActivity;
import com.tokopedia.transaction.purchase.model.response.txverification.TxVerData;

import java.text.MessageFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Angga.Prasetiyo on 25/05/2016.
 */
public class TxVerAdapter extends ArrayAdapter<TxVerData> {

    private static final int PENDING_PAYMENT_MODE = 1;
    private static final int UNEDITABLE_PAYMENT_MODE = 2;
    private final LayoutInflater inflater;
    private final Context context;
    private final ActionListener actionListener;

    public TxVerAdapter(Context context, ActionListener actionListener) {
        super(
                context, R.layout.holder_item_transaction_verification_tx_module,
                new ArrayList<TxVerData>()
        );
        this.context = context;
        this.actionListener = actionListener;
        this.inflater = LayoutInflater.from(context);
    }

    public interface ActionListener {

        void actionEditPayment(TxVerData data);

        void actionUploadProof(TxVerData data);

        void actionCancelTransaction(TxVerData data);
    }

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.holder_item_transaction_verification_tx_module, null
            );
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TxVerData item = getItem(position);
        if (item == null) return convertView;
        holder.tvPaymentDate.setText(item.getPaymentDate());
        holder.btnOverflow.setOnClickListener(new OneOnClick() {
            @Override
            public void oneOnClick(View view) {
                showPopUp(view, item);
            }
        });
        holder.tvSysAccountNumber.setText(item.getSystemAccountNo());
        holder.tvSysAccountBankName.setText(item.getBankName());
        holder.tvPaymentRefNumber.setText(item.getPaymentRefNum());
        holder.tvPayementAmount.setText(item.getPaymentAmount());
        holder.tvUserAccountName.setText(item.getUserAccountName());
        holder.tvUserAccountBankName.setText(item.getUserBankName());

        switch (getTypePaymentMethod(item)) {
            case 1:
                renderKlikBCAHolder(item, holder);
                break;
            case 2:
                renderUnchangeableHolder(item, holder);
                break;
            case 3:
                renderNormalHolder(holder);
                break;
        }

        if(item.getHowtopay() != null && item.getHowtopay() == 1 && item.getHowtopayUrl() != null) {
            holder.btnPaymentProcedure.setVisibility(View.VISIBLE);
            holder.btnPaymentProcedure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPaymentProcedure(item.getHowtopayUrl());
                }
            });
        } else {
            holder.btnPaymentProcedure.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void openPaymentProcedure(String url) {
        context.startActivity(PaymentProcedureActivity.newIntent(context, url));
    }

    private void showPopUp(View view, final TxVerData data) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        int menuId;
        if(getTypePaymentMethod(data) == PENDING_PAYMENT_MODE
                || getTypePaymentMethod(data) == UNEDITABLE_PAYMENT_MODE) {
            menuId = R.menu.menu_transaction_payment_delete;
        } else if(data.getButton().getButtonUploadProof() == 0) {
            menuId = R.menu.menu_transaction_payment;
        } else {
            menuId = R.menu.menu_transaction_payment_upload;
        }
        inflater.inflate(menuId, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.action_edit) {
                    actionListener.actionEditPayment(data);
                    return true;
                } else if (i == R.id.action_upload) {
                    actionListener.actionUploadProof(data);
                    return true;
                } else if (i == R.id.action_cancel_transaction) {
                    actionListener.actionCancelTransaction(data);
                    return true;
                }else {
                    return false;
                }
            }
        });
        popup.show();
    }


    private void renderNormalHolder(ViewHolder holder) {
        holder.holderNormalPayment.setVisibility(View.VISIBLE);
        holder.holderUnchangeablePayment.setVisibility(View.GONE);
        holder.btnOverflow.setVisibility(View.VISIBLE);
    }

    private void renderUnchangeableHolder(TxVerData item, ViewHolder holder) {
        holder.holderNormalPayment.setVisibility(View.GONE);
        holder.holderUnchangeablePayment.setVisibility(View.VISIBLE);
        if(item.getUserAccountName().equals("-") || item.getUserAccountName().isEmpty()) {
            holder.tvSpecialPaymentMethod.setText(item.getBankName());
        } else {
            holder.tvSpecialPaymentMethod.setText(MessageFormat.format("Kode {0} : {1}",
                    item.getBankName(), item.getUserAccountName()));
        }
    }

    private void renderKlikBCAHolder(TxVerData item, ViewHolder holder) {
        holder.holderNormalPayment.setVisibility(View.GONE);
        holder.holderUnchangeablePayment.setVisibility(View.VISIBLE);
        holder.tvSpecialPaymentMethod.setText(item.getBankName());
    }

    private int getTypePaymentMethod(TxVerData item) {
        return (item.getBankName().contains(context.getString(R.string.parameter_klik))
                && item.getBankName().contains(context.getString(R.string.parameter_bca))) ||
                (item.getBankName().contains(context.getString(R.string.parameter_kartu))
                        && item.getBankName().contains(context.getString(R.string.parameter_kredit))) ? 1
                : item.getButton().getButtonEditPayment() == 0 &&
                item.getButton().getButtonUploadProof() == 0 &&
                item.getButton().getButtonViewProof() == 0 ? 2 : 3;
    }

    class ViewHolder {
        @BindView(R2.id.date)
        TextView tvPaymentDate;
        @BindView(R2.id.user_account)
        TextView tvUserAccountName;
        @BindView(R2.id.system_account)
        TextView tvSysAccountNumber;
        @BindView(R2.id.total_invoice)
        TextView tvPayementAmount;
        @BindView(R2.id.payment_ref)
        TextView tvPaymentRefNumber;
        @BindView(R2.id.user_bank_name)
        TextView tvUserAccountBankName;
        @BindView(R2.id.system_bank_name)
        TextView tvSysAccountBankName;
        @BindView(R2.id.payment_method_name)
        TextView tvSpecialPaymentMethod;
        @BindView(R2.id.but_overflow)
        View btnOverflow;
        @BindView(R2.id.normal_payment_info)
        LinearLayout holderNormalPayment;
        @BindView(R2.id.unchangeable_payment_info)
        LinearLayout holderUnchangeablePayment;
        @BindView(R2.id.button_payment_procedure)
        Button btnPaymentProcedure;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
