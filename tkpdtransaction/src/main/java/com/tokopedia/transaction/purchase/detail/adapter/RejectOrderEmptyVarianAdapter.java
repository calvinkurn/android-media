package com.tokopedia.transaction.purchase.detail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.EmptyVarianProductEditable;

import java.util.List;

/**
 * Created by kris on 1/10/18. Tokopedia
 */

public class RejectOrderEmptyVarianAdapter extends RecyclerView
        .Adapter<RejectOrderEmptyVarianAdapter.RejectOrderEmptyVarianHolder>{

    private RejectOrderEmptyVarianAdapterListener listener;

    private List<EmptyVarianProductEditable> emptyVarianProductEditables;

    public RejectOrderEmptyVarianAdapter(
            List<EmptyVarianProductEditable> emptyVarianProductEditables,
            RejectOrderEmptyVarianAdapterListener listener) {
        this.emptyVarianProductEditables= emptyVarianProductEditables;
        this.listener = listener;
    }

    @Override
    public RejectOrderEmptyVarianHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_reject_empty_varian_adapter, parent, false);
        return new RejectOrderEmptyVarianHolder(view);
    }

    @Override
    public void onBindViewHolder(RejectOrderEmptyVarianHolder holder, int position) {
        holder.productName.setText(
                emptyVarianProductEditables.get(position).getProductName()
        );
        holder.productPrice.setText(
                emptyVarianProductEditables.get(position).getProductPrice()
        );
        holder.additionalNote.setText(
                emptyVarianProductEditables.get(position).getProductDescription()
        );
        ImageHandler.LoadImage(
                holder.productImage, emptyVarianProductEditables.get(position).getProductImage()
        );
        holder.productLayout.setOnClickListener(
                onProductLayoutClickedListener(emptyVarianProductEditables.get(position))
        );
        holder.additionalNote.setOnClickListener(
                onProductLayoutClickedListener(emptyVarianProductEditables.get(position))
        );
    }

    public List<EmptyVarianProductEditable> getEmptyVarianProductEditables() {
        return emptyVarianProductEditables;
    }

    @Override
    public int getItemCount() {
        return emptyVarianProductEditables.size();
    }

    class RejectOrderEmptyVarianHolder extends RecyclerView.ViewHolder {

        private ViewGroup productLayout;

        private TextView productName;

        private TextView productPrice;

        private TextView additionalNote;;

        private ImageView productImage;

        RejectOrderEmptyVarianHolder(View itemView) {
            super(itemView);
            productLayout = itemView.findViewById(R.id.product_layout);
            productName = itemView.findViewById(R.id.order_detail_product_name);
            productPrice = itemView.findViewById(R.id.order_detail_product_price);
            additionalNote = itemView.findViewById(R.id.additional_notes);
            productImage = itemView.findViewById(R.id.product_image);
        }
    }

    private View.OnClickListener onProductLayoutClickedListener(
            final EmptyVarianProductEditable emptyVarianProductEditable
    ) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemSelected(emptyVarianProductEditable);
            }
        };
    }

    public interface RejectOrderEmptyVarianAdapterListener {
        void onItemSelected(EmptyVarianProductEditable emptyVarianProductEditable);
    }

}
