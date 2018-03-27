package com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.listener.ChatroomContract;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 3/22/18.
 */

public class SprintSaleAdapter extends RecyclerView.Adapter<SprintSaleAdapter.ViewHolder> {

    private final ChatroomContract.View.SprintSaleViewHolderListener listener;
    List<SprintSaleProductViewModel> list;

    public static SprintSaleAdapter createInstance(ChatroomContract.View.SprintSaleViewHolderListener listener) {
        return new SprintSaleAdapter(listener);
    }

    private SprintSaleAdapter(ChatroomContract.View.SprintSaleViewHolderListener listener) {
        this.list = new ArrayList<>();
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView discountLabel;
        TextView priceBeforeDiscount;
        TextView price;
        TextView stockText;
        ProgressBar stockProgress;
        View mainLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            discountLabel = itemView.findViewById(R.id.discount_label);
            price = itemView.findViewById(R.id.price);
            priceBeforeDiscount = itemView.findViewById(R.id.price_before_discount);
            stockText = itemView.findViewById(R.id.stock_text);
            stockProgress = itemView.findViewById(R.id.stock_progress);
            mainLayout = itemView.findViewById(R.id.main_layout);

            priceBeforeDiscount.setPaintFlags(priceBeforeDiscount.getPaintFlags() | Paint
                    .STRIKE_THRU_TEXT_FLAG);

            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSprintSaleClicked(list.get(getAdapterPosition()).getProductUrl(),
                            list.get(getAdapterPosition()).getProductName());
                }
            });

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sprint_sale_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.stockText.setText(list.get(position).getStockText());
        holder.priceBeforeDiscount.setText(list.get(position).getProductPriceBeforeDiscount());
        holder.price.setText(list.get(position).getProductPrice());
        holder.discountLabel.setText(list.get(position).getDiscountLabel());

        holder.stockProgress.setProgress(list.get(position).getStockPercentage());
        ImageHandler.loadImage(holder.productImage.getContext(), holder.productImage,
                list.get(position).getProductImage(), R.drawable.ic_loading_toped_new);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<SprintSaleProductViewModel> listProducts) {
        this.list.clear();
        this.list.addAll(listProducts);
        notifyDataSetChanged();
    }

}
