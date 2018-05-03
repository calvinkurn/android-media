package com.tokopedia.seller.product.edit.view.adapter;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.PrefixEditText;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.utils.ProductPriceRangeUtils;
import com.tokopedia.seller.product.edit.view.model.edit.ProductWholesaleViewModel;
import com.tokopedia.seller.product.edit.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.util.CurrencyIdrTextWatcher;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by yoshua on 02/05/18.
 */

public class WholesaleAddAdapter extends BaseLinearRecyclerViewAdapter {
    private List<WholesaleModel> wholesaleModels;
    private Listener listener;

    WholesaleModel currentModel;

    public WholesaleAddAdapter() {
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
                        .inflate(R.layout.item_product_add_wholesale, viewGroup, false);
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
        notifyItemInserted(wholesaleModels.size() - 1);
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

    public synchronized void removeAll() {
        wholesaleModels.clear();
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
        if (isWithinDataset(wholesaleModels.size() - 1)) {
            return getItem(wholesaleModels.size() - 1);
        }
        return null;
    }

    public void addAllWholeSalePrice(List<ProductWholesaleViewModel> wholesalePrice) {
        for (int i = 0; i < wholesalePrice.size(); i++) {

            WholesaleModel wholesaleModel = new WholesaleModel(
                    wholesalePrice.get(i).getMinQty(),
                    wholesalePrice.get(i).getPriceValue()
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
            productWholesaleViewModel.setMinQty(wholesaleModel.getQtyMin());
            productWholesaleViewModel.setPriceValue(wholesaleModel.getQtyPrice());

            productWholesaleViewModels.add(productWholesaleViewModel);
        }
        return productWholesaleViewModels;
    }

    public interface Listener {
        /**
         * notify data size changed
         *
         * @param currentSize size of {@link WholesaleAddAdapter#wholesaleModels} not included
         *                    parent adapter size.
         */
        void notifySizeChanged(int currentSize);

        @CurrencyTypeDef
        int getCurrencyType();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private final PrefixEditText etRangeWholesale;
        private final TextInputLayout tilRangeWholesale;
        private final ImageView imageWholesale;
        private final PrefixEditText etWholeSalePrice;
        private final TextInputLayout tilWholeSalePrice;
        private final Locale dollarLocale = Locale.US;
        private final Locale idrLocale = new Locale("in", "ID");
        private NumberFormat formatter;

        public ViewHolder(View itemView) {
            super(itemView);

            if (listener == null) {
                throw new IllegalArgumentException("listener must be implemented !!");
            }

            formatter();

            etRangeWholesale = itemView.findViewById(R.id.et_range_whole_sale);
            tilRangeWholesale = itemView.findViewById(R.id.til_range_whole_sale);
            imageWholesale = itemView.findViewById(R.id.image_whole_sale);
            etWholeSalePrice = itemView.findViewById(R.id.et_whole_sale_price);
            tilWholeSalePrice = itemView.findViewById(R.id.til_whole_sale_price);

            etRangeWholesale.setPrefix(MethodChecker.fromHtml("&#8805; &emsp;").toString());
            etWholeSalePrice.setPrefix(MethodChecker.fromHtml("Rp ").toString());

            imageWholesale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(getAdapterPosition());
                }
            });
        }

        protected void formatter() {
            formatter = NumberFormat.getNumberInstance(dollarLocale);
            switch (listener.getCurrencyType()) {
                case CurrencyTypeDef.TYPE_USD:
                    formatter.setMinimumFractionDigits(2);
                    break;
                default:
                case CurrencyTypeDef.TYPE_IDR:
                    formatter.setMinimumFractionDigits(0);
                    break;
            }
        }

        public void bindData(final WholesaleModel wholesaleModel) {
            formatter();
            NumberFormat quantityNumberFormat = new DecimalFormat();
            quantityNumberFormat.setMinimumIntegerDigits(0);
            String qtyMin = quantityNumberFormat.format(wholesaleModel.getQtyMin());
            etRangeWholesale.setText(qtyMin);
            String qtyPrice = wholesaleModel.getQtyPrice() + "";
            etWholeSalePrice.setText(formatValue(qtyPrice));
            etWholeSalePrice.addTextChangedListener(new CurrencyIdrTextWatcher(etWholeSalePrice){
                @Override
                public void onNumberChanged(double number) {
                    wholesaleModel.setQtyPrice(number);
                    isPriceValid(wholesaleModel, getAdapterPosition(), tilWholeSalePrice);
                }
            });
            if(!TextUtils.isEmpty(wholesaleModel.getStatus())){
                tilWholeSalePrice.setErrorEnabled(true);
                tilWholeSalePrice.setError(wholesaleModel.getStatus());
            }
        }

        private String formatValue(String s) {
            String valueString = CurrencyFormatHelper.removeCurrencyPrefix(s);
            valueString = CurrencyFormatHelper.RemoveNonNumeric(valueString);
            if (TextUtils.isEmpty(valueString)) {
                return null;
            }
            double value = Double.parseDouble(valueString);
            return formatter.format(value);
        }
    }

    protected boolean isPriceValid(WholesaleModel model, int position, TextInputLayout tilWholeSalePrice) {
        if (!ProductPriceRangeUtils.isPriceValid(model.getQtyPrice(), listener.getCurrencyType(), false)) {
            tilWholeSalePrice.setErrorEnabled(true);
            tilWholeSalePrice.setError(tilWholeSalePrice.getContext().getString(R.string.product_error_product_price_not_valid,
                    ProductPriceRangeUtils.getMinPriceString(listener.getCurrencyType(), false),
                    ProductPriceRangeUtils.getMaxPriceString(listener.getCurrencyType(), false)));
            setStatusElementAfterCurrent(position, tilWholeSalePrice, "perbaiki dulu atasnya");
            return false;
        }

        if(position > 0){
            if (model.getQtyPrice() >= getItem(position-1).getQtyPrice()) {
                tilWholeSalePrice.setErrorEnabled(true);
                tilWholeSalePrice.setError(tilWholeSalePrice.getContext().getString(R.string.product_price_should_be_cheaper_than_previous_wholesale_price));
                setStatusElementAfterCurrent(position, tilWholeSalePrice,"perbaiki dulu atasnya");
                return false;
            }
            else{
                tilWholeSalePrice.setError(null);
                tilWholeSalePrice.setErrorEnabled(false);
                setStatusElementAfterCurrent(position, tilWholeSalePrice,"");
            }
        }

        return true;
    }

    private void setStatusElementAfterCurrent(int position, TextInputLayout tilWholeSalePrice, String message){
        for(int i = position+1; i < getItemSize(); i++){
            wholesaleModels.get(i).setStatus(message);
            final int j = i;
            tilWholeSalePrice.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemChanged(j);
                }
            });
        }
    }
}
