package com.tokopedia.seller.product.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.view.model.upload.ProductWholesaleViewModel;
import com.tokopedia.seller.product.view.model.wholesale.WholesaleModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author normansyahputa on 4/21/17.
 */

public class WholesaleAdapter extends BaseLinearRecyclerViewAdapter {
    private List<WholesaleModel> wholesaleModels;
    private List<ProductWholesaleViewModel> productWholesaleViewModels;
    private Listener listener;

    public WholesaleAdapter() {
        wholesaleModels = new CopyOnWriteArrayList<>();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
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

    public int getItemSize() {
        return wholesaleModels.size();
    }

    public synchronized int addItem(WholesaleModel wholesaleModel) {
        wholesaleModels.add(wholesaleModel);
        return wholesaleModels.size() - 1;
    }

    public synchronized void removeItem(int position) {

        if (isWithinDataset(position)) {
            wholesaleModels.remove(position);
        }

        notifyItemRemoved(position);

        if (listener != null) {
            listener.notifySizeChanged(wholesaleModels.size());
        }
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

            WholesaleModel wholesaleModel = new WholesaleModel(
                    wholesalePrice.get(i).getQtyMin(),
                    wholesalePrice.get(i).getQtyMax(),
                    wholesalePrice.get(i).getPrice()
            );

            wholesaleModels.add(wholesaleModel);
        }
        notifyDataSetChanged();
    }

    public void clearAll() {
        wholesaleModels.clear();
    }

    public synchronized List<ProductWholesaleViewModel> getProductWholesaleViewModels() {
        List<ProductWholesaleViewModel> productWholesaleViewModels =
                new ArrayList<>();
        for (int i = 0; i < wholesaleModels.size(); i++) {
            ProductWholesaleViewModel productWholesaleViewModel
                    = new ProductWholesaleViewModel();
            WholesaleModel wholesaleModel = wholesaleModels.get(i);
            productWholesaleViewModel.setQtyMin(wholesaleModel.getQtyOne());
            productWholesaleViewModel.setQtyMax(wholesaleModel.getQtyTwo());
            productWholesaleViewModel.setPrice(wholesaleModel.getQtyPrice());

            productWholesaleViewModels.add(productWholesaleViewModel);
        }
        return productWholesaleViewModels;
    }

    public interface Listener {
        /**
         * notify data size changed
         *
         * @param currentSize size of {@link WholesaleAdapter#wholesaleModels} not included
         *                    parent adapter size.
         */
        void notifySizeChanged(int currentSize);

        @CurrencyTypeDef
        int getCurrencyType();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textRangeWholesale;
        private final ImageView imageWholesale;
        private final TextView textWholeSalePrice;
        private final Locale dollarLocale = Locale.US;
        private final Locale idrLocale = new Locale("in", "ID");
        private NumberFormat formatter;

        public ViewHolder(View itemView) {
            super(itemView);

            if (listener == null) {
                throw new IllegalArgumentException("listener must be implemented !!");
            }

            switch (listener.getCurrencyType()) {
                case CurrencyTypeDef.TYPE_USD:
                    formatter = NumberFormat.getNumberInstance(dollarLocale);
                    break;
                default:
                case CurrencyTypeDef.TYPE_IDR:
                    formatter = NumberFormat.getNumberInstance(idrLocale);
                    break;
            }

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
            String qtyOne = wholesaleModel.getQtyOne() + "";
            String qtyTwo = wholesaleModel.getQtyTwo() + "";
            textRangeWholesale.setText(itemView.getContext().getString(R.string.wholesale_range_format,
                    formatValue(qtyOne), formatValue(qtyTwo)));
            String qtyPrice = wholesaleModel.getQtyPrice() + "";
            textWholeSalePrice.setText(formatValue(qtyPrice));
        }

        private String formatValue(String s) {
            String valueString = CurrencyFormatHelper.removeCurrencyPrefix(s);
            valueString = CurrencyFormatHelper.RemoveNonNumeric(valueString);
            if (TextUtils.isEmpty(valueString)) {
                return null;
            }
            float value = Float.parseFloat(valueString);
            return formatter.format(value);
        }
    }
}
