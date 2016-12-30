package com.tokopedia.transaction.purchase.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.OneOnClick;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderProduct;

import java.text.MessageFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Angga.Prasetiyo on 28/04/2016.
 */
public class TxProductListAdapter extends ArrayAdapter<OrderProduct> {
    private final LayoutInflater inflater;
    private final ActionListener listener;
    private final Context context;

    public interface ActionListener {
        void actionToProductInfo(ProductPass productPass);
    }

    public TxProductListAdapter(Context context, ActionListener listener) {
        super(context, R.layout.holder_item_transaction_product_list_tx_module,
                new ArrayList<OrderProduct>());
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.holder_item_transaction_product_list_tx_module, null
            );
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OrderProduct item = getItem(position);
        holder.tvProductName.setText(MethodChecker.fromHtml(item.getProductName()));
        holder.tvNotes.setText(
                MethodChecker.fromHtml(
                        item.getProductNotes().length() == 0 ?
                                "-" : item.getProductNotes()
                )
        );
        holder.tvProductPrice.setText(
                MessageFormat.format(
                        "{0} {1}",
                        context.getString(R.string.title_rupiah),
                        item.getProductPrice()
                )
        );
        holder.tvDeliverQty.setText(MessageFormat.format(" x {0} {1}",
                item.getOrderDeliverQuantity(), context.getString(R.string.title_item)));
        holder.tvTotalPrice.setText(item.getOrderSubtotalPriceIdr());
        ImageHandler.loadImageRounded2(context, holder.ivProductPic, item.getProductPicture());

        holder.tvProductName.setOnClickListener(new OneOnClick() {
            @Override
            public void oneOnClick(View view) {
                listener.actionToProductInfo(ProductPass.Builder.aProductPass()
                        .setProductId(getItem(position).getProductId())
                        .setProductName(getItem(position).getProductName())
                        .setProductImage(getItem(position).getProductPicture())
                        .setProductPrice(getItem(position).getProductPrice())
                        .build());
            }
        });
        return convertView;
    }

    class ViewHolder {
        @BindView(R2.id.product_image)
        ImageView ivProductPic;
        @BindView(R2.id.product_name)
        TextView tvProductName;
        @BindView(R2.id.product_price)
        TextView tvProductPrice;
        @BindView(R2.id.total_order)
        TextView tvDeliverQty;
        @BindView(R2.id.total_price)
        TextView tvTotalPrice;
        @BindView(R2.id.message)
        TextView tvNotes;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
