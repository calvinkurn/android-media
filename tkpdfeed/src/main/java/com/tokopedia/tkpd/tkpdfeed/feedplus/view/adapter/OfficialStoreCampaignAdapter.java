package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreCampaignProductViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/18/17.
 */
public class OfficialStoreCampaignAdapter extends RecyclerView.Adapter<OfficialStoreCampaignAdapter.ViewHolder> {

    private static final String CASHBACK = "Cashback";
    protected ArrayList<OfficialStoreCampaignProductViewModel> list;
    private final FeedPlus.View viewListener;

    public OfficialStoreCampaignAdapter(FeedPlus.View viewListener) {
        this.viewListener = viewListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productName;
        public TextView productPrice;
        public TextView originalPrice;
        public TextView discountLabel;
        public ImageView productImage;
        public ImageView shopAva;
        public TextView shopName;
        public TextView cashback;
        public TextView wholesale;
        public TextView preorder;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            productName = (TextView) itemLayoutView.findViewById(R.id.title);
            productPrice = (TextView) itemLayoutView.findViewById(R.id.price);
            originalPrice = (TextView) itemLayoutView.findViewById(R.id.text_original_price);
            discountLabel = (TextView) itemLayoutView.findViewById(R.id.text_discount);
            productImage = (ImageView) itemLayoutView.findViewById(R.id.product_image);
            shopAva = (ImageView) itemLayoutView.findViewById(R.id.shop_ava);
            shopName = (TextView) itemLayoutView.findViewById(R.id.shop_name);
            cashback = (TextView) itemView.findViewById(R.id.cashback);
            wholesale = (TextView) itemView.findViewById(R.id.wholesale);
            preorder = (TextView) itemView.findViewById(R.id.preorder);
            productName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToProductDetail(String.valueOf(list.get(getAdapterPosition())
                            .getProductId()));
                }
            });

            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToProductDetail(String.valueOf(list.get(getAdapterPosition())
                            .getProductId()));
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_store_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ImageHandler.LoadImage(holder.shopAva, list.get(holder.getAdapterPosition()).getShopAva());
        holder.shopName.setText(list.get(position).getShopName());

        holder.productName.setText(MethodChecker.fromHtml(list.get(position).getName()));
        holder.productPrice.setText(list.get(position).getPrice());
        if (!TextUtils.isEmpty(list.get(position).getOriginalPrice()))
            holder.originalPrice.setText(list.get(position).getOriginalPrice());
        if (list.get(position).getDiscount() != 0) {
            holder.discountLabel.setVisibility(View.VISIBLE);
            String discountText = list.get(position).getDiscount() +
                    holder.discountLabel.getContext().getString(R.string.discount_off);
            holder.discountLabel.setText(discountText);
        } else {
            holder.discountLabel.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(list.get(position).getCashback()))
            holder.cashback.setVisibility(View.GONE);
        else {
            holder.cashback.setVisibility(View.VISIBLE);
            holder.cashback.setText(CASHBACK + " " + list.get(position).getCashback());
        }

        if (list.get(position).isWholesale())
            holder.wholesale.setVisibility(View.VISIBLE);
        else
            holder.wholesale.setVisibility(View.GONE);


        if (list.get(position).isPreorder())
            holder.preorder.setVisibility(View.VISIBLE);
        else
            holder.preorder.setVisibility(View.GONE);

        ImageHandler.LoadImage(holder.productImage, list.get(position).getImageSource());

    }

    @Override
    public int getItemCount() {
        if (list.size() > 6)
            return 6;
        else
            return list.size();
    }

    public void setList(ArrayList<OfficialStoreCampaignProductViewModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<OfficialStoreCampaignProductViewModel> getList() {
        return list;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

}
