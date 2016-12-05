package com.tokopedia.transaction.cart.adapter;

import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.cart.model.CartInsurance;
import com.tokopedia.transaction.cart.model.CartItemEditable;
import com.tokopedia.transaction.cart.model.CartPartialDeliver;
import com.tokopedia.transaction.cart.model.calculateshipment.ProductEditData;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;
import com.tokopedia.transaction.cart.model.cartdata.TransactionList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author anggaprasetiyo on 11/10/16.
 */

public class CartItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_CART_ITEM = R.layout.holder_item_cart_tx_module;
    private final Fragment hostFragment;
    private final CartAction cartAction;

    private List<Object> dataList = new ArrayList<>();

    public List<CartItemEditable> getDataList() {
        List<CartItemEditable> cartItemEditables = new ArrayList<>();
        for (Object object : dataList) {
            if (object instanceof CartItemEditable) {
                cartItemEditables.add(((CartItemEditable) object).finalizeAllData());
            }
        }
        return cartItemEditables;
    }

    public void renderErrorCart(CartItemEditable cartItemEditable) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getTransactionList().getCartString()
                    .equals(cartItemEditable.getTransactionList().getCartString())) {
                this.notifyItemChanged(i);
            }
        }
    }

    public interface CartAction {
        void onCancelCart(TransactionList data);

        void onCancelCartProduct(TransactionList data, CartProduct cartProduct);

        void onChangeShipment(TransactionList data);

        void onSubmitEditCart(TransactionList cartData, List<ProductEditData> cartProductEditDataList);

        void onUpdateInsuranceCart(TransactionList cartData, boolean useInsurance);
    }

    private void enableEditMode(TransactionList cartData) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getTransactionList().getCartString()
                    .equals(cartData.getCartString())) {
                ((CartItemEditable) dataList.get(i)).setEditMode(true);
                this.notifyItemChanged(i);
                return;
            }
        }
    }

    private void disableEditMode(TransactionList cartData) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getTransactionList().getCartString()
                    .equals(cartData.getCartString())) {
                ((CartItemEditable) dataList.get(i)).setEditMode(false);
                this.notifyItemChanged(i);
                return;
            }
        }
    }

    public CartItemAdapter(Fragment hostFragment, CartAction cartAction) {
        this.hostFragment = hostFragment;
        this.cartAction = cartAction;
    }

    public void fillDataList(List<TransactionList> dataList) {
        //  dataList.clear();
        for (TransactionList data : dataList) {
            this.dataList.add(new CartItemEditable(data));
        }
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CART_ITEM) {
            return new ViewHolder(LayoutInflater.from(
                    hostFragment.getActivity()).inflate(viewType, parent, false
            ));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int i = getItemViewType(position);
        if (i == TYPE_CART_ITEM) {
            final ViewHolder holderItemCart = (ViewHolder) holder;
            final CartItemEditable itemData = (CartItemEditable) dataList.get(position);
            bindCartHolder(holderItemCart, itemData);
        }
    }

    private void bindCartHolder(final ViewHolder holder, final CartItemEditable data) {

        final TransactionList cartData = data.getTransactionList();

        if ((cartData.getCartErrorMessage2() != null
                && !cartData.getCartErrorMessage2().equals("0"))
                || (cartData.getCartErrorMessage1() != null
                && !cartData.getCartErrorMessage1().equals("0"))) {
            holder.holderError.setVisibility(View.VISIBLE);
            holder.tvError1.setText(cartData.getCartErrorMessage1() + "");
            holder.tvError2.setText(cartData.getCartErrorMessage2() + "");
        } else {
            holder.holderError.setVisibility(View.GONE);
        }

        final CartProductItemAdapter adapterProduct = new CartProductItemAdapter(hostFragment);
        adapterProduct.setCartProductAction(new CartProductItemAdapter.CartProductAction() {
            @Override
            public void onCancelCartProduct(CartProduct cartProduct) {
                if (cartAction != null)
                    cartAction.onCancelCartProduct(cartData, cartProduct);
            }
        });
        adapterProduct.fillDataList(cartData.getCartProducts());
        holder.rvCartProduct.setLayoutManager(new LinearLayoutManager(hostFragment.getActivity()));
        holder.rvCartProduct.setAdapter(adapterProduct);

        holder.tvShopName.setText(cartData.getCartShop().getShopName());
        holder.tvInsurancePrice.setText(cartData.getCartInsurancePriceIdr());
        holder.tvShippingCost.setText(cartData.getCartShippingRateIdr());
        holder.tvSubTotal.setText(cartData.getCartTotalProductPriceIdr());
        holder.tvTotalPrice.setText(cartData.getCartTotalAmountIdr());
        holder.tvWeight.setText(cartData.getCartTotalWeight());
        holder.tvShippingAddress.setText(String.format("%s (Ubah)",
                cartData.getCartDestination().getReceiverName()));
        holder.tvShipment.setText(String.format("%s - %s (Ubah)",
                cartData.getCartShipments().getShipmentName(),
                cartData.getCartShipments().getShipmentPackageName()));
        holder.holderActionEditor.setVisibility(data.isEditMode() ? View.VISIBLE : View.GONE);

        renderInsuranceOption(holder, cartData);
        renderEditableMode(holder, data, adapterProduct);
        renderPartialDeliverOption(holder, cartData);
        renderDropShipperOption(holder, data);

        holder.tvShippingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartAction.onChangeShipment(cartData);
            }
        });
        holder.holderDetailCartToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.holderDetailCart.setVisibility(
                        holder.holderDetailCart.getVisibility() == View.VISIBLE
                                ? View.GONE : View.VISIBLE
                );
            }
        });

        holder.btnOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(hostFragment.getActivity(), v);
                popupMenu.getMenuInflater().inflate(R.menu.cart_item_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int i = item.getItemId();
                        if (i == R.id.action_cart_delete) {
                            cartAction.onCancelCart(cartData);
                            return true;
                        } else if (i == R.id.action_cart_edit) {
                            enableEditMode(cartData);
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableEditMode(cartData);
                adapterProduct.disableEditMode();
            }
        });
        holder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cartAction.onSubmitEditCart(cartData,
                            adapterProduct.getCartProductEditDataList());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        if (data.getErrorType() != CartItemEditable.ERROR_NON) {
            holder.holderContainer.requestFocus();
        }
    }

    private void renderDropShipperOption(final ViewHolder holder, final CartItemEditable data) {
//        holder.etDropshiperPhone.addTextChangedListener(null);
//        holder.etDropshiperName.addTextChangedListener(null);


        switch (data.getErrorType()) {
            case CartItemEditable.ERROR_DROPSHIPPER_NAME:
                holder.tilEtDropshiperName.setErrorEnabled(true);
                holder.tilEtDropshiperName.setError("Harus diisi");
                break;
            case CartItemEditable.ERROR_DROPSHIPPER_PHONE:
                holder.tilEtDropshiperPhone.setErrorEnabled(true);
                holder.tilEtDropshiperPhone.setError("Harus diisi");
                break;
        }
        final TransactionList cartData = data.getTransactionList();


        holder.etDropshiperName.setText(data.getDropShipperName() != null
                ? data.getDropShipperName() : "");
        holder.etDropshiperPhone.setText(data.getDropShipperPhone() != null
                ? data.getDropShipperPhone() : "");

        final TextWatcher textWatcherDropShipperName = getWatcherEtDropShipperName(data, cartData, holder);
        final TextWatcher textWatcherDropShipperPhone = getWatcherEtDropShipperPhone(data, cartData, holder);

        holder.cbDropshiper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateDropShipperCartName(cartData, holder.etDropshiperName.getText().toString());
                    updateDropShipperCartPhone(cartData, holder.etDropshiperPhone.getText().toString());
                    holder.holderDropshiperForm.setVisibility(View.VISIBLE);
                    insertDropShipperCartData(cartData);
                    holder.etDropshiperName.addTextChangedListener(textWatcherDropShipperName);
                    holder.etDropshiperPhone.addTextChangedListener(textWatcherDropShipperPhone);
                } else {
                    holder.holderDropshiperForm.setVisibility(View.GONE);
                    holder.etDropshiperPhone.removeTextChangedListener(textWatcherDropShipperPhone);
                    holder.etDropshiperName.removeTextChangedListener(textWatcherDropShipperName);
                    deleteDropShipperCartData(cartData);
                }
            }
        });
        holder.cbDropshiper.setChecked(data.isDropShipper());

    }

    @NonNull
    private TextWatcher getWatcherEtDropShipperPhone(final CartItemEditable data,
                                                     final TransactionList cartData,
                                                     final ViewHolder holder) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holder.tilEtDropshiperPhone.setError(null);
                holder.tilEtDropshiperPhone.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("aftertextchanged phone", s.toString());
                if (!s.toString().equalsIgnoreCase(data.getDropShipperPhone()))
                    updateDropShipperCartPhone(cartData, s.toString());
            }
        };
    }

    @NonNull
    private TextWatcher getWatcherEtDropShipperName(final CartItemEditable data,
                                                    final TransactionList cartData,
                                                    final ViewHolder holder) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holder.tilEtDropshiperName.setError(null);
                holder.tilEtDropshiperName.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("aftertextchanged name", s.toString());
                if (!s.toString().equalsIgnoreCase(data.getDropShipperName()))
                    updateDropShipperCartName(cartData, s.toString());
            }
        };
    }

    private void renderPartialDeliverOption(ViewHolder holder, final TransactionList cartData) {
        ArrayAdapter<CartPartialDeliver> cartPartialDeliverAdapter
                = new ArrayAdapter<>(
                hostFragment.getActivity(), android.R.layout.simple_spinner_item,
                CartPartialDeliver.createListForAdapter()
        );
        cartPartialDeliverAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        holder.spShipmentOptionChoosen.setAdapter(cartPartialDeliverAdapter);
        holder.spShipmentOptionChoosen.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position,
                                               long id) {
                        boolean isPartial = ((CartPartialDeliver) parent.getAdapter()
                                .getItem(position)).getCode().equals("1");
                        if (isPartial) insertPartialDeliverCartData(cartData);
                        else deletePartialDeliverCartData(cartData);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
    }

    private void updateDropShipperCartName(TransactionList cartData, String dropShipperName) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getTransactionList().getCartString()
                    .equals(cartData.getCartString())) {
                ((CartItemEditable) dataList.get(i)).setDropShipperName(dropShipperName);
                //  this.notifyItemChanged(i);
                return;
            }
        }
    }

    private void updateDropShipperCartPhone(TransactionList cartData, String dropShipperPhone) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getTransactionList().getCartString()
                    .equals(cartData.getCartString())) {
                ((CartItemEditable) dataList.get(i)).setDropShipperPhone(dropShipperPhone);
                //  this.notifyItemChanged(i);
                return;
            }
        }
    }

    private void deletePartialDeliverCartData(TransactionList cartData) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getTransactionList().getCartString()
                    .equals(cartData.getCartString())) {
                ((CartItemEditable) dataList.get(i)).setPartialDeliver(false);
                ((CartItemEditable) dataList.get(i)).setCartStringForDeliverOption(
                        cartData.getCartString()
                );
                //  this.notifyItemChanged(i);
                return;
            }
        }
    }

    private void insertPartialDeliverCartData(TransactionList cartData) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getTransactionList().getCartString()
                    .equals(cartData.getCartString())) {
                ((CartItemEditable) dataList.get(i)).setPartialDeliver(true);
                ((CartItemEditable) dataList.get(i)).setCartStringForDeliverOption("");
                // this.notifyItemChanged(i);
                return;
            }
        }
    }

    private void deleteDropShipperCartData(TransactionList cartData) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getTransactionList().getCartString()
                    .equals(cartData.getCartString())) {
                ((CartItemEditable) dataList.get(i)).setDropShipper(false);
                ((CartItemEditable) dataList.get(i)).setCartStringForDropShipperOption("");
                //  this.notifyItemChanged(i);
                return;
            }
        }
    }

    private void insertDropShipperCartData(TransactionList cartData) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getTransactionList().getCartString()
                    .equals(cartData.getCartString())) {
                ((CartItemEditable) dataList.get(i)).setDropShipper(true);
                ((CartItemEditable) dataList.get(i)).setCartStringForDropShipperOption(
                        cartData.getCartString()
                );
                // this.notifyItemChanged(i);
                return;
            }
        }
    }

    private void renderInsuranceOption(ViewHolder holder, final TransactionList cartData) {
        final boolean isUseInsurance = cartData.getCartForceInsurance() == 1
                || cartData.getCartInsuranceProd() == 1
                || isProductUseInsurance(cartData.getCartProducts());
        List<CartInsurance> cartInsuranceList;
        if (isUseInsurance) cartInsuranceList = CartInsurance.createListForAdapterUseInsurance();
        else cartInsuranceList = CartInsurance.createListForAdapterNotUseInsurance();

        ArrayAdapter<CartInsurance> cartInsuranceAdapter
                = new ArrayAdapter<>(
                hostFragment.getActivity(), android.R.layout.simple_spinner_item,
                cartInsuranceList
        );
        int selectionIndexUseInsurance = 0;
        for (int i = 0, cartInsuranceListSize = cartInsuranceList.size();
             i < cartInsuranceListSize; i++) {
            CartInsurance cartInsurance = cartInsuranceList.get(i);
            if (cartInsurance.getCode().equals("1")) selectionIndexUseInsurance = i;
        }
        int selectionIndexNotUseInsurance = 0;
        for (int i = 0, cartInsuranceListSize = cartInsuranceList.size();
             i < cartInsuranceListSize; i++) {
            CartInsurance cartInsurance = cartInsuranceList.get(i);
            if (cartInsurance.getCode().equals("0")) selectionIndexNotUseInsurance = i;
        }

        if (isUseInsurance) holder.spUseInsurance.setSelection(selectionIndexUseInsurance);
        else holder.spUseInsurance.setSelection(selectionIndexNotUseInsurance);

        holder.spUseInsurance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boolean isUse = ((CartInsurance) parent.getAdapter()
                        .getItem(position)).getCode().equals("1");
                if (isUse != isUseInsurance) {
                    cartAction.onUpdateInsuranceCart(cartData, isUse);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (cartData.getCartForceInsurance() == 1
                || isProductMustInsurance(cartData.getCartProducts())) {
            holder.spUseInsurance.setEnabled(false);
        } else {
            holder.spUseInsurance.setEnabled(true);
        }

        if (cartData.getCartCannotInsurance() == 1) {
            holder.spUseInsurance.setEnabled(false);
        } else {
            holder.spUseInsurance.setEnabled(true);
        }

        cartInsuranceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spUseInsurance.setAdapter(cartInsuranceAdapter);
    }

    private boolean isProductMustInsurance(List<CartProduct> cartProducts) {
        for (CartProduct cartProduct : cartProducts) {
            if (cartProduct.getProductMustInsurance().equals("1")) {
                return true;
            }
        }
        return false;
    }

    private boolean isProductUseInsurance(List<CartProduct> cartProducts) {
        for (CartProduct cartProduct : cartProducts) {
            if (cartProduct.getProductMustInsurance().equals("1")
                    || cartProduct.getProductUseInsurance() == 1) {
                return true;
            }
        }
        return false;
    }

    private void renderEditableMode(ViewHolder holder, CartItemEditable data,
                                    CartProductItemAdapter adapterProduct) {
        if (data.isEditMode()) {
            holder.holderActionEditor.setVisibility(View.VISIBLE);
            holder.holderContainer.setCardBackgroundColor(
                    hostFragment.getResources().getColor(R.color.grey_100)
            );
            holder.btnOverflow.setEnabled(false);
            adapterProduct.enableEditMode();
            adapterProduct.notifyDataSetChanged();
        } else {
            holder.holderActionEditor.setVisibility(View.GONE);
            holder.holderContainer.setCardBackgroundColor(
                    hostFragment.getResources().getColor(R.color.white)
            );
            holder.btnOverflow.setEnabled(true);
            adapterProduct.disableEditMode();
            adapterProduct.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position) instanceof CartItemEditable
                ? TYPE_CART_ITEM : super.getItemViewType(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.holder_container)
        CardView holderContainer;
        @BindView(R2.id.tv_error_1)
        TextView tvError1;
        @BindView(R2.id.tv_error_2)
        TextView tvError2;
        @BindView(R2.id.holder_error)
        LinearLayout holderError;
        @BindView(R2.id.tv_shop_name)
        TextView tvShopName;
        @BindView(R2.id.btn_overflow)
        ImageView btnOverflow;
        @BindView(R2.id.rv_cart_product)
        RecyclerView rvCartProduct;
        @BindView(R2.id.cb_dropshiper)
        CheckBox cbDropshiper;
        @BindView(R2.id.et_dropshiper_name)
        EditText etDropshiperName;
        @BindView(R2.id.til_et_dropshiper_name)
        TextInputLayout tilEtDropshiperName;
        @BindView(R2.id.et_dropshiper_phone)
        EditText etDropshiperPhone;
        @BindView(R2.id.til_et_dropshiper_phone)
        TextInputLayout tilEtDropshiperPhone;
        @BindView(R2.id.holder_dropshiper_form)
        LinearLayout holderDropshiperForm;
        @BindView(R2.id.holder_detail_cart_toggle)
        RelativeLayout holderDetailCartToggle;
        @BindView(R2.id.tv_shipping_address)
        TextView tvShippingAddress;
        @BindView(R2.id.tv_shipment)
        TextView tvShipment;
        @BindView(R2.id.sp_use_insurance)
        Spinner spUseInsurance;
        @BindView(R2.id.sp_shipment_option_choosen)
        Spinner spShipmentOptionChoosen;
        @BindView(R2.id.tv_weight)
        TextView tvWeight;
        @BindView(R2.id.tv_sub_total)
        TextView tvSubTotal;
        @BindView(R2.id.tv_shipping_cost)
        TextView tvShippingCost;
        @BindView(R2.id.tv_insurance_price)
        TextView tvInsurancePrice;
        @BindView(R2.id.tv_additional_cost)
        TextView tvAdditionalCost;
        @BindView(R2.id.holder_detail_cart)
        LinearLayout holderDetailCart;
        @BindView(R2.id.tv_total_price)
        TextView tvTotalPrice;

        @BindView(R2.id.holder_action_editor)
        LinearLayout holderActionEditor;
        @BindView(R2.id.btn_save)
        TextView btnSave;
        @BindView(R2.id.btn_cancel)
        TextView btnCancel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
