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
import com.tokopedia.design.text.watcher.NumberTextWatcher;
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

    private final String PRICE = "price";
    private final String QTY = "qty";

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

            etRangeWholesale.addTextChangedListener(new NumberTextWatcher(etRangeWholesale) {
                @Override
                public void onNumberChanged(double minQuantity) {
                    if(wholesaleModels.get(getAdapterPosition()).isFocusQty()){
                        wholesaleModels.get(getAdapterPosition()).setQtyMin((int)minQuantity);
                        isQtyValid(wholesaleModels.get(getAdapterPosition()), getAdapterPosition(), tilRangeWholesale, etRangeWholesale);
                    }
                }
            });
            etRangeWholesale.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        setFocusFalse();
                        wholesaleModels.get(getAdapterPosition()).setFocusQty(true);
                    }
                }
            });

            etWholeSalePrice.setPrefix(MethodChecker.fromHtml("Rp ").toString());

            etWholeSalePrice.addTextChangedListener(new CurrencyIdrTextWatcher(etWholeSalePrice){
                @Override
                public void onNumberChanged(double number) {
                    if(wholesaleModels.get(getAdapterPosition()).isFocusPrice()){
                        wholesaleModels.get(getAdapterPosition()).setQtyPrice(number);
                        isPriceValid(wholesaleModels.get(getAdapterPosition()), getAdapterPosition(), tilWholeSalePrice, etWholeSalePrice);
                    }
                }
            });
            etWholeSalePrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        setFocusFalse();
                        wholesaleModels.get(getAdapterPosition()).setFocusPrice(true);
                    }
                }
            });

            imageWholesale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(getAdapterPosition());
                }
            });
        }

        private void setFocusFalse(){
            for(int i = 0; i < getItemSize(); i++){
                wholesaleModels.get(i).setFocusQty(false);
                wholesaleModels.get(i).setFocusPrice(false);
            }
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

        public void bindData(WholesaleModel wholesaleModel) {
            formatter();
            NumberFormat quantityNumberFormat = new DecimalFormat();
            quantityNumberFormat.setMinimumIntegerDigits(0);
            String qtyMin = quantityNumberFormat.format(wholesaleModel.getQtyMin());
            etRangeWholesale.setText(qtyMin);
            String qtyPrice = wholesaleModel.getQtyPrice() + "";
            etWholeSalePrice.setText(formatValue(qtyPrice));
            if(!TextUtils.isEmpty(wholesaleModel.getStatusPrice())){
                tilWholeSalePrice.setErrorEnabled(true);
                tilWholeSalePrice.setError(wholesaleModel.getStatusPrice());
                if(wholesaleModel.getStatusPrice().equalsIgnoreCase(tilWholeSalePrice.getContext().getString(R.string.product_fix_previous_wholesale)))
                {
                    etWholeSalePrice.setEnabled(false);
                } else {
                    etWholeSalePrice.setEnabled(true);
                }
            } else {
                tilWholeSalePrice.setError(null);
                etWholeSalePrice.setEnabled(true);
                tilWholeSalePrice.setErrorEnabled(false);
            }

            if(!TextUtils.isEmpty(wholesaleModel.getStatusQty())){
                tilRangeWholesale.setErrorEnabled(true);
                tilRangeWholesale.setError(wholesaleModel.getStatusQty());
                if(wholesaleModel.getStatusQty().equalsIgnoreCase(tilRangeWholesale.getContext().getString(R.string.product_fix_previous_wholesale)))
                {
                    etRangeWholesale.setEnabled(false);
                } else {
                    etRangeWholesale.setEnabled(true);
                }
            } else {
                tilRangeWholesale.setError(null);
                etRangeWholesale.setEnabled(true);
                tilRangeWholesale.setErrorEnabled(false);
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

    protected boolean isPriceValid(WholesaleModel model, final int position, TextInputLayout tilWholeSalePrice, PrefixEditText etWholeSalePrice) {
        //cek harga sesuai standart
        if (!ProductPriceRangeUtils.isPriceValid(model.getQtyPrice(), listener.getCurrencyType(), false)) {
            tilWholeSalePrice.setErrorEnabled(true);
            tilWholeSalePrice.setError(tilWholeSalePrice.getContext().getString(R.string.product_error_product_price_not_valid,
                    ProductPriceRangeUtils.getMinPriceString(listener.getCurrencyType(), false),
                    ProductPriceRangeUtils.getMaxPriceString(listener.getCurrencyType(), false)));
            setStatusElementAfterCurrent(position, tilWholeSalePrice, tilWholeSalePrice.getContext().getString(R.string.product_fix_previous_wholesale), PRICE);
            return false;
        }

        //cek bukan posisi pertama untuk cek keatas
        if(position > 0){
            if (model.getQtyPrice() >= getItem(position-1).getQtyPrice()) {
                //kalo posisi terpilih lebih besar dari atasnya, set error posisi terpilih lalu set disable bawahnya
                tilWholeSalePrice.setErrorEnabled(true);
                tilWholeSalePrice.setError(tilWholeSalePrice.getContext().getString(R.string.product_price_should_be_cheaper_than_previous_wholesale_price));
                setStatusElementAfterCurrent(position, tilWholeSalePrice,tilWholeSalePrice.getContext().getString(R.string.product_fix_previous_wholesale), PRICE);
                return false;
            } else if (position < wholesaleModels.size()-1 && getItem(position+1).getQtyPrice() >= model.getQtyPrice()) {
                //cek posisi bukan paling bawah dan kalo bawah lebih besar dari posisi terpilih, set error bawah dan focus ke bawah lalu set disable bawahnya
                wholesaleModels.get(position).setStatusPrice("");
                tilWholeSalePrice.setError(null);
                etWholeSalePrice.setEnabled(true);
                tilWholeSalePrice.setErrorEnabled(false);
                wholesaleModels.get(position+1).setStatusPrice(tilWholeSalePrice.getContext().getString(R.string.product_price_should_be_cheaper_than_previous_wholesale_price));
                tilWholeSalePrice.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemChanged(position+1);
                    }
                });
                setStatusElementAfterCurrent(position+1, tilWholeSalePrice,tilWholeSalePrice.getContext().getString(R.string.product_fix_previous_wholesale), PRICE);
                return false;
            }
        }

        //harga sesuai hilangkan semua error di edittext termasuk di bawahnya
        tilWholeSalePrice.setError(null);
        etWholeSalePrice.setEnabled(true);
        tilWholeSalePrice.setErrorEnabled(false);
        setStatusElementAfterCurrent(position, tilWholeSalePrice,"", PRICE);
        return true;
    }

    protected boolean isQtyValid(WholesaleModel model, final int position, TextInputLayout tilRangeWholesale, PrefixEditText etRangeWholesale) {
        if(position > 0){
            if (model.getQtyMin() <= getItem(position-1).getQtyMin()) {
                tilRangeWholesale.setErrorEnabled(true);
                tilRangeWholesale.setError(tilRangeWholesale.getContext().getString(R.string.wholesale_qty_not_valid));
                setStatusElementAfterCurrent(position, tilRangeWholesale,tilRangeWholesale.getContext().getString(R.string.product_fix_previous_wholesale), QTY);
                return false;
            } else if (position < wholesaleModels.size()-1 && getItem(position+1).getQtyMin() <= model.getQtyMin()) {
                wholesaleModels.get(position).setStatusQty("");
                tilRangeWholesale.setError(null);
                etRangeWholesale.setEnabled(true);
                tilRangeWholesale.setErrorEnabled(false);
                wholesaleModels.get(position+1).setStatusQty(tilRangeWholesale.getContext().getString(R.string.wholesale_qty_not_valid));
                tilRangeWholesale.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemChanged(position+1);
                    }
                });
                setStatusElementAfterCurrent(position+1, tilRangeWholesale,tilRangeWholesale.getContext().getString(R.string.product_fix_previous_wholesale), QTY);
                return false;
            }
        }

        tilRangeWholesale.setError(null);
        etRangeWholesale.setEnabled(true);
        tilRangeWholesale.setErrorEnabled(false);
        setStatusElementAfterCurrent(position, tilRangeWholesale,"", QTY);
        return true;
    }

    private void setStatusElementAfterCurrent(int position, TextInputLayout til, String message, String type){
        for(int i = position+1; i < getItemSize(); i++){
            if(type.equalsIgnoreCase(PRICE)){
                wholesaleModels.get(i).setStatusPrice(message);
            } else {
                wholesaleModels.get(i).setStatusQty(message);
            }
            final int j = i;
            til.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemChanged(j);
                }
            });
        }
    }
}
