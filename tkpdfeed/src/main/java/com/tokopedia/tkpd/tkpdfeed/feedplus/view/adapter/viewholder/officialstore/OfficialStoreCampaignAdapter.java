package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.officialstore;

import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
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
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.LabelsAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreCampaignProductViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreCampaignViewModel;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/18/17.
 */
public class OfficialStoreCampaignAdapter extends RecyclerView.Adapter<OfficialStoreCampaignAdapter.ViewHolder> {

    private static final String DEFAULT_EMPTY_PRICE = "Rp 0";
    private final FeedPlus.View viewListener;
    private OfficialStoreCampaignViewModel officialStoreCampaignViewModel;

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
        public RecyclerView labels;
        public LabelsAdapter adapter;
        public ImageView freeReturn;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            productName = (TextView) itemLayoutView.findViewById(R.id.title);
            productPrice = (TextView) itemLayoutView.findViewById(R.id.price);
            originalPrice = (TextView) itemLayoutView.findViewById(R.id.text_original_price);
            discountLabel = (TextView) itemLayoutView.findViewById(R.id.text_discount);
            productImage = (ImageView) itemLayoutView.findViewById(R.id.product_image);
            shopAva = (ImageView) itemLayoutView.findViewById(R.id.shop_ava);
            shopName = (TextView) itemLayoutView.findViewById(R.id.shop_name);
            labels = (RecyclerView) itemLayoutView.findViewById(R.id.labels);
            freeReturn = (ImageView) itemLayoutView.findViewById(R.id.free_return);

            LinearLayoutManager layoutManager = new LinearLayoutManager(itemLayoutView.getContext
                    (), LinearLayoutManager.HORIZONTAL, false);
            labels.setLayoutManager(layoutManager);
            adapter = new LabelsAdapter();
            labels.setAdapter(adapter);

            productName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToProductDetailFromCampaign(
                            officialStoreCampaignViewModel.getPage(),
                            officialStoreCampaignViewModel.getRowNumber(),
                            String.valueOf(officialStoreCampaignViewModel.getListProduct().get
                                    (getAdapterPosition())
                                    .getProductId()),
                            officialStoreCampaignViewModel.getListProduct().get(getAdapterPosition()).getImageSourceSingle(),
                            officialStoreCampaignViewModel.getListProduct().get(getAdapterPosition()).getName(),
                            officialStoreCampaignViewModel.getListProduct().get(getAdapterPosition()).getPrice());
                }
            });

            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToProductDetailFromCampaign(
                            officialStoreCampaignViewModel.getPage(),
                            officialStoreCampaignViewModel.getRowNumber(),
                            String.valueOf(officialStoreCampaignViewModel.getListProduct().get(getAdapterPosition())
                                    .getProductId()),
                            officialStoreCampaignViewModel.getListProduct().get(getAdapterPosition()).getImageSourceSingle(),
                            officialStoreCampaignViewModel.getListProduct().get(getAdapterPosition()).getName(),
                            officialStoreCampaignViewModel.getListProduct().get(getAdapterPosition()).getPrice());
                }
            });
            shopAva.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToShopDetailFromCampaign(
                            officialStoreCampaignViewModel.getPage(),
                            officialStoreCampaignViewModel.getRowNumber(),
                            officialStoreCampaignViewModel.getListProduct().get(getAdapterPosition()).getShopUrl());
                }
            });
            shopName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToShopDetailFromCampaign(
                            officialStoreCampaignViewModel.getPage(),
                            officialStoreCampaignViewModel.getRowNumber(),
                            officialStoreCampaignViewModel.getListProduct().get(getAdapterPosition())
                                    .getShopUrl());
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
        ArrayList<OfficialStoreCampaignProductViewModel> list =
                officialStoreCampaignViewModel.getListProduct();

        ImageHandler.LoadImage(holder.shopAva, list.get(holder.getAdapterPosition()).getShopAva());
        holder.shopName.setText(MethodChecker.fromHtml(list.get(position).getShopName()));

        holder.productName.setText(MethodChecker.fromHtml(list.get(position).getName()));
        holder.productPrice.setText(list.get(position).getPrice());

        if (!TextUtils.isEmpty(list.get(position).getOriginalPrice())
                && !list.get(position).getOriginalPrice().equals(DEFAULT_EMPTY_PRICE)) {
            holder.originalPrice.setVisibility(View.VISIBLE);
            holder.originalPrice.setText(list.get(position).getOriginalPrice());
            holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.originalPrice.setVisibility(View.GONE);
        }

        if (list.get(position).getDiscount() != 0) {
            holder.discountLabel.setVisibility(View.VISIBLE);
            String discountText = list.get(position).getDiscount() +
                    holder.discountLabel.getContext().getString(R.string.discount_off);
            holder.discountLabel.setText(discountText);
        } else {
            holder.discountLabel.setVisibility(View.GONE);
        }

        ImageHandler.LoadImage(holder.productImage, list.get(position).getImageSource());

        if (!list.get(position).getLabels().isEmpty()) {
            holder.labels.setVisibility(View.VISIBLE);
            holder.adapter.setList(list.get(position).getLabels());
        } else {
            holder.labels.setVisibility(View.GONE);
        }

        if (list.get(position).isFreeReturn()) {
            holder.freeReturn.setVisibility(View.VISIBLE);
        } else
            holder.freeReturn.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return officialStoreCampaignViewModel.getListProduct().size();

    }

    public void setData(OfficialStoreCampaignViewModel officialStoreCampaignViewModel) {
        this.officialStoreCampaignViewModel = officialStoreCampaignViewModel;
        notifyDataSetChanged();
    }

    public ArrayList<OfficialStoreCampaignProductViewModel> getList() {
        return officialStoreCampaignViewModel.getListProduct();
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

}
