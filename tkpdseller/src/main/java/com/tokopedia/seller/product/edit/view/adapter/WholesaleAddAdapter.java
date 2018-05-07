package com.tokopedia.seller.product.edit.view.adapter;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.PrefixEditText;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.utils.ProductPriceRangeUtils;
import com.tokopedia.seller.product.edit.view.model.edit.ProductWholesaleViewModel;
import com.tokopedia.seller.product.edit.view.model.wholesale.WholesaleModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by yoshua on 02/05/18.
 */

public class WholesaleAddAdapter extends RecyclerView.Adapter<WholesaleAddAdapter.ViewHolder> {

    private List<WholesaleModel> wholesaleModels;
    private Listener listener;
    private int currentPositionFocusPriceEdittext = 0;
    private double mainPrice;

    public WholesaleAddAdapter(double mainPrice) {
        wholesaleModels = new CopyOnWriteArrayList<>();
        this.mainPrice = mainPrice;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public WholesaleAddAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.item_product_add_wholesale, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NumberFormat quantityNumberFormat = new DecimalFormat();
        quantityNumberFormat.setMinimumIntegerDigits(0);
        holder.etRangeWholesale.setText(holder.formatValue((double)wholesaleModels.get(position).getQtyMin()));
        holder.etWholeSalePrice.setText(holder.formatValue(wholesaleModels.get(position).getQtyPrice()));
        holder.isPriceValid(wholesaleModels.get(position), position);
        holder.initFocus(position);

        if(!TextUtils.isEmpty(wholesaleModels.get(position).getStatusPrice())){
            holder.setErrorEditText(position);
        } else {
            holder.removeErrorEdittext();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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


            etWholeSalePrice.addTextChangedListener(new NumberTextWatcher(etWholeSalePrice){
                @Override
                public void onNumberChanged(double number) {
                    if(wholesaleModels.get(getAdapterPosition()).isFocusPrice()){
                        wholesaleModels.get(getAdapterPosition()).setQtyPrice(number);
                        wholesaleModels.get(getAdapterPosition()).setFocusPrice(false);
                        currentPositionFocusPriceEdittext = getAdapterPosition();
                        etWholeSalePrice.post(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });
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

        private void initFocus(int position){
            if(currentPositionFocusPriceEdittext == position){
                wholesaleModels.get(position).setFocusPrice(true);
            }

            if(wholesaleModels.get(position).isFocusPrice()){
                etWholeSalePrice.requestFocus();
            }
        }

        private void setFocusFalse(){
            for(int i = 0; i < getItemSize(); i++){
                wholesaleModels.get(i).setFocusQty(false);
                wholesaleModels.get(i).setFocusPrice(false);
            }
        }

        private void removeErrorEdittext(){
            tilWholeSalePrice.setError(null);
            etWholeSalePrice.setEnabled(true);
            tilWholeSalePrice.setErrorEnabled(false);
        }

        private void setErrorEditText(int position){
            tilWholeSalePrice.setErrorEnabled(true);
            tilWholeSalePrice.setError(wholesaleModels.get(position).getStatusPrice());
            if(wholesaleModels.get(position).getStatusPrice().equalsIgnoreCase(tilWholeSalePrice.getContext().getString(R.string.product_fix_previous_wholesale)))
            {
                etWholeSalePrice.setEnabled(false);
            } else {
                etWholeSalePrice.setEnabled(true);
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

        private String formatValue(Double value) {
            return formatter.format(value);
        }

        private void isPriceValid(WholesaleModel model, int position) {
            if (!ProductPriceRangeUtils.isPriceValid(model.getQtyPrice(), listener.getCurrencyType(), false)) {
                model.setStatusPrice(tilWholeSalePrice.getContext().getString(R.string.product_error_product_price_not_valid,
                        ProductPriceRangeUtils.getMinPriceString(listener.getCurrencyType(), false),
                        ProductPriceRangeUtils.getMaxPriceString(listener.getCurrencyType(), false)));
                return;
            }

            if(position > 0){
                if(!getItem(position-1).getStatusPrice().equalsIgnoreCase("")){
                    model.setStatusPrice(tilWholeSalePrice.getContext().getString(R.string.product_fix_previous_wholesale));
                    return;
                }
                if (model.getQtyPrice() >= getItem(position-1).getQtyPrice()) {
                    model.setStatusPrice(tilWholeSalePrice.getContext().getString(R.string.product_price_should_be_cheaper_than_previous_wholesale_price));
                    return;
                }
            } else {
                if (model.getQtyPrice() >= mainPrice) {
                    model.setStatusPrice(tilWholeSalePrice.getContext().getString(R.string.product_price_should_be_cheaper_than_main_price));
                    return;
                }
            }

            model.setStatusPrice("");
        }

        private boolean isQtyValid(WholesaleModel model, final int position, TextInputLayout tilRangeWholesale, PrefixEditText etRangeWholesale) {
            if(position > 0){
                if (model.getQtyMin() <= getItem(position-1).getQtyMin()) {
                    tilRangeWholesale.setErrorEnabled(true);
                    tilRangeWholesale.setError(tilRangeWholesale.getContext().getString(R.string.wholesale_qty_not_valid));
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
                    return false;
                }
            }

            tilRangeWholesale.setError(null);
            etRangeWholesale.setEnabled(true);
            tilRangeWholesale.setErrorEnabled(false);
            return true;
        }

        private void refreshLayout(){
            notifyItemRangeChanged(0, getItemSize());
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return wholesaleModels.size();
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

        void notifySizeChanged(int currentSize);

        @CurrencyTypeDef
        int getCurrencyType();
    }
}
