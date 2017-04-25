package com.tokopedia.seller.product.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.upload.ProductWholesaleViewModel;
import com.tokopedia.seller.product.view.model.wholesale.WholesaleModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author normansyahputa on 4/21/17.
 */

public class WholesaleAdapter extends BaseLinearRecyclerViewAdapter {
    private List<WholesaleModel> wholesaleModels;
    private List<ProductWholesaleViewModel> productWholesaleViewModels;

    public WholesaleAdapter() {
        wholesaleModels = new CopyOnWriteArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case WholesaleModel.TYPE:
                View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_wholesale_layout, viewGroup, false);
                return new ViewHolder(itemLayoutView);
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case WholesaleModel.TYPE:
                ((ViewHolder) holder).bindData(
                        wholesaleModels.get(position)
                );
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (wholesaleModels.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return wholesaleModels.get(position).getType();
        }
    }

    @Override
    public int getItemCount() {
        return wholesaleModels.size() + super.getItemCount();
    }

    public synchronized int addItem(WholesaleModel wholesaleModel) {
        int lastIndex = wholesaleModels.size() - 1;
        WholesaleModel prev = getItem(lastIndex);

        if (prev != null) {
            wholesaleModel.setPrevWholesaleModel(wholesaleModels.get(lastIndex));
        }
        wholesaleModels.add(wholesaleModel);

        return wholesaleModels.size() - 1;
    }

    public synchronized void removeItem(int position) {

        int prevPosition = position - 1;
        WholesaleModel prevPositionModel = getItem(prevPosition);

        int nextPosition = position + 1;
        WholesaleModel nextPositionModel = getItem(nextPosition);

        if (nextPositionModel != null) {
            nextPositionModel.setPrevWholesaleModel(prevPositionModel);
        }

        if (isWithinDataset(position)) {
            wholesaleModels.remove(position);
        }

        notifyItemRemoved(position);
    }

    protected WholesaleModel getItem(int prevPosition) {
        if (isWithinDataset(prevPosition)) {
            return wholesaleModels.get(prevPosition);
        }
        return null;
    }

    private boolean isWithinDataset(int position) {
        return position >= 0 && position <= wholesaleModels.size() - 1;
    }

    private boolean isLastItemPosition(int position) {
        return position == wholesaleModels.size();
    }

    public WholesaleModel getLastItem() {
        if (isWithinDataset(wholesaleModels.size() - 1))
            return getItem(wholesaleModels.size() - 1);
        return null;
    }

    public List<WholesaleModel> getWholesaleModels() {
        return new ArrayList<>(wholesaleModels);
    }

    public void addAllWholeSalePrice(List<ProductWholesaleViewModel> wholesalePrice) {
        for (int i = 0; i < wholesalePrice.size(); i++) {
            int prev = i - 1;

            WholesaleModel wholesaleModel = new WholesaleModel(
                    wholesalePrice.get(i).getQtyMin(),
                    wholesalePrice.get(i).getQtyMax(),
                    wholesalePrice.get(i).getPrice()
            );

            if (isWithinDataset(prev)) {
                wholesaleModel.setPrevWholesaleModel(wholesaleModels.get(prev));
            }

            wholesaleModels.add(wholesaleModel);
        }
        notifyDataSetChanged();
    }

    public List<ProductWholesaleViewModel> getProductWholesaleViewModels() {
        List<ProductWholesaleViewModel> productWholesaleViewModels =
                new ArrayList<>();
        for (int i = 0; i < wholesaleModels.size(); i++) {
            ProductWholesaleViewModel productWholesaleViewModel
                    = new ProductWholesaleViewModel();
            WholesaleModel wholesaleModel = wholesaleModels.get(i);
            productWholesaleViewModel.setQtyMin(wholesaleModel.getQtyOne());
            productWholesaleViewModel.setQtyMax(wholesaleModel.getQtyTwo());
            productWholesaleViewModel.setPrice(wholesaleModel.getQtyPrice());
        }
        return productWholesaleViewModels;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textRangeWholesale;
        private final ImageView imageWholesale;
        private final TextView textWholeSalePrice;

        public ViewHolder(View itemView) {
            super(itemView);

            textRangeWholesale = (TextView) itemView.findViewById(R.id.text_range_whole_sale);
            imageWholesale = (ImageView) itemView.findViewById(R.id.image_whole_sale);
            textWholeSalePrice = (TextView) itemView.findViewById(R.id.text_whole_sale_price);

            imageWholesale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(getAdapterPosition());
                }
            });
        }

        public void bindData(WholesaleModel wholesaleModel) {
            textRangeWholesale.setText(itemView.getContext().getString(R.string.wholesale_range_format, wholesaleModel.getQtyOne() + "", wholesaleModel.getQtyTwo() + ""));
            textWholeSalePrice.setText(wholesaleModel.getQtyPrice() + "");
        }
    }
}
