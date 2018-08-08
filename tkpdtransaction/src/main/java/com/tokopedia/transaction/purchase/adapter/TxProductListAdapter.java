package com.tokopedia.transaction.purchase.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.OneOnClick;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderProduct;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Angga.Prasetiyo on 28/04/2016.
 */
public class TxProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM_PRODUCT_ITEM
            = R.layout.holder_item_transaction_product_list_tx_module;

    private final ActionListener listener;
    private List<OrderProduct> dataList = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if (viewType == TYPE_ITEM_PRODUCT_ITEM) {
            return new ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final OrderProduct item = dataList.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tvProductName.setText(MethodChecker.fromHtml(item.getProductName()));
            viewHolder.tvNotes.setText(
                    MethodChecker.fromHtml(
                            item.getProductNotes().length() == 0 ?
                                    "-" : item.getProductNotes()
                    )
            );
            viewHolder.tvProductPrice.setText(
                    MessageFormat.format(
                            "{0} {1}",
                            holder.itemView.getContext().getString(R.string.title_rupiah),
                            item.getProductPrice()
                    )
            );
            viewHolder.tvDeliverQty.setText(
                    MessageFormat.format(" x {0} {1}", item.getOrderDeliverQuantity(),
                            holder.itemView.getContext().getString(R.string.title_item))
            );
            viewHolder.tvTotalPrice.setText(item.getOrderSubtotalPriceIdr());
            ImageHandler.loadImageRounded2(
                    holder.itemView.getContext(), viewHolder.ivProductPic, item.getProductPicture()
            );
            viewHolder.tvProductName.setOnClickListener(new OneOnClick() {
                @Override
                public void oneOnClick(View view) {
                    listener.actionToProductInfo(ProductPass.Builder.aProductPass()
                            .setProductId(item.getProductId())
                            .setProductName(item.getProductName())
                            .setProductImage(item.getProductPicture())
                            .setProductPrice(item.getProductPrice())
                            .build());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface ActionListener {
        void actionToProductInfo(ProductPass productPass);
    }

    public TxProductListAdapter(ActionListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM_PRODUCT_ITEM;
    }

    public void addAllDataList(List<OrderProduct> orderProducts) {
        this.dataList.clear();
        this.dataList.addAll(orderProducts);
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
