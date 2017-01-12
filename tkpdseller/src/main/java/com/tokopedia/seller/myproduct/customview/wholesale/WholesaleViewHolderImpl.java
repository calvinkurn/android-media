package com.tokopedia.seller.myproduct.customview.wholesale;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.myproduct.utils.CurrencyFormatter;
import com.tokopedia.seller.myproduct.utils.PriceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sebastianuskh on 12/2/16.
 */
public class WholesaleViewHolderImpl extends RecyclerView.ViewHolder implements WholesaleViewHolder {

    private static final String TAG = "Wholesale View Holder";
    private int position;
    private WholesaleAdapter listener;
    private final int currency;

    @BindView(R2.id.wholesale_item_qty_one)
    EditText qtyOne;

    @BindView(R2.id.wholesale_item_qty_two)
    EditText qtyTwo;

    @BindView(R2.id.wholesale_item_qty_price)
    EditText qtyPrice;

    private boolean onPriceEdit = false;

    TextWatcher qtyOneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            listener.onUpdateData(WholesaleAdapterImpl.QTY_ONE, position, String.valueOf(s), true);
        }
    };

    TextWatcher qtyTwoTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            listener.onUpdateData(WholesaleAdapterImpl.QTY_TWO, position, String.valueOf(s), true);
        }
    };

    TextWatcher qtyPriceTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(onPriceEdit)
                return;
            onPriceEdit = true;
            String rawString = setPriceCurrency(s.toString());
            listener.onUpdateData(WholesaleAdapterImpl.QTY_PRICE, position, rawString, true);
            onPriceEdit = false;
        }
    };

    @OnClick(R2.id.button_delete_wholesale)
    void deleteWholesale(){
        listener.removeWholesaleItem(position);
    }

    public WholesaleViewHolderImpl(View itemView, int currency) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.currency = currency;
    }

    @Override
    public void bindView(WholesaleAdapter listener, int position, WholesaleModel wholesaleModel) {
        this.listener = listener;
        this.position = position;
        if(wholesaleModel != null) {
            this.qtyOne.setText(String.format("%d", wholesaleModel.getQtyOne()));
            this.qtyTwo.setText(String.format("%d", wholesaleModel.getQtyTwo()));
            this.qtyPrice.setText(String.format("%.00f", wholesaleModel.getQtyPrice()));

            String rawString = setPriceCurrency(String.format("%.00f", wholesaleModel.getQtyPrice()));
            listener.onUpdateData(WholesaleAdapterImpl.QTY_PRICE, position, rawString, false);

            /** add listener after first time we add the initial value so it will not checked at the first time */
            this.qtyOne.addTextChangedListener(qtyOneTextWatcher);
            this.qtyTwo.addTextChangedListener(qtyTwoTextWatcher);
            this.qtyPrice.addTextChangedListener(qtyPriceTextWatcher);
        }
    }

    @Override
    public void removeQtyOneTextWatcher() {
        this.qtyOne.removeTextChangedListener(qtyOneTextWatcher);
        this.qtyOne.setError(null);
    }

    @Override
    public void removeQtyTwoTextWatcher() {
        this.qtyTwo.removeTextChangedListener(qtyTwoTextWatcher);
        this.qtyTwo.setError(null);
    }

    @Override
    public void removeQtyPriceTextWatcher() {
        this.qtyPrice.removeTextChangedListener(qtyPriceTextWatcher);
        this.qtyPrice.setError(null);
    }

    @Override
    public void onQtyOneError(String error) {
        qtyOne.setError(error);
    }

    @Override
    public void onQtyTwoError(String error) {
        qtyTwo.setError(error);
    }

    @Override
    public void onQtyPriceError(String error) {
        qtyPrice.setError(error);
    }

    @Override
    public CharSequence getQtyOneError() {
        return qtyOne.getError();
    }

    @Override
    public CharSequence getQtyTwoError() {
        return qtyTwo.getError();
    }

    @Override
    public CharSequence getQtyPriceError() {
        return qtyPrice.getError();
    }

    private String setPriceCurrency(String s) {
        String rawString = "";
        switch (currency) {
            case PriceUtils.CURRENCY_RUPIAH:
                CurrencyFormatHelper.SetToRupiah(qtyPrice);
                rawString = CurrencyFormatter.getRawString(s.toString());
                break;
            case PriceUtils.CURRENCY_DOLLAR:
                CurrencyFormatHelper.SetToDollar(qtyPrice);
                rawString = CurrencyFormatter.getRawString(s.toString());
                break;
        }
        return rawString;
    }

}
