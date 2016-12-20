package com.tokopedia.transaction.cart.adapter;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.tokopedia.transaction.cart.model.cartdata.CartItem;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;
import com.tokopedia.transaction.customview.expandablelayout.ExpandableLayoutListenerAdapter;
import com.tokopedia.transaction.customview.expandablelayout.ExpandableLinearLayout;
import com.tokopedia.transaction.customview.expandablelayout.Utils;

import java.text.MessageFormat;
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
    private final CartItemActionListener cartItemActionListener;

    private List<Object> dataList = new ArrayList<>();
    private SparseBooleanArray expandState = new SparseBooleanArray();

    public CartItemAdapter(Fragment hostFragment, CartItemActionListener cartItemActionListener) {
        this.hostFragment = hostFragment;
        this.cartItemActionListener = cartItemActionListener;
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
            final CartItemEditable cartItemEditable = (CartItemEditable) dataList.get(position);
            final CartItem cartData = cartItemEditable.getCartItem();
            final CartProductItemAdapter adapterProduct = new CartProductItemAdapter(hostFragment);

            renderErrorCartItem(holderItemCart, cartData);
            renderCartProductList(holderItemCart, cartData, adapterProduct);
            renderDetailCartItem(holderItemCart, cartData);
            renderInsuranceOption(holderItemCart, cartData);
            renderEditableMode(holderItemCart, cartItemEditable.isEditMode(), adapterProduct);
            renderPartialDeliverOption(holderItemCart, cartData);
            renderDropShipperOption(holderItemCart, cartItemEditable);
            renderHolderViewListener(holderItemCart, cartData, adapterProduct, position);
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

    public void fillDataList(List<CartItem> dataList) {
        for (int i = 0, dataListSize = dataList.size(); i < dataListSize; i++) {
            CartItem data = dataList.get(i);
            this.dataList.add(new CartItemEditable(data));
            this.expandState.append(i, false);
        }
        this.notifyDataSetChanged();
    }

    public List<CartItemEditable> getDataList() {
        List<CartItemEditable> cartItemEditables = new ArrayList<>();
        for (Object object : dataList) {
            if (object instanceof CartItemEditable) {
                cartItemEditables.add(((CartItemEditable) object).finalizeAllData());
            }
        }
        return cartItemEditables;
    }

    public void refreshCartItem(CartItemEditable cartItemEditable) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getCartItem().getCartString()
                    .equals(cartItemEditable.getCartItem().getCartString())) {
                this.notifyItemChanged(i);
            }
        }
    }

    private void renderDetailCartItem(ViewHolder holder, CartItem cartData) {
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
        holder.tvAdditionalCost.setText(cartData.getCartTotalLogisticFeeIdr());
    }

    private void renderHolderViewListener(final ViewHolder holder, final CartItem cartData,
                                          final CartProductItemAdapter adapterProduct, final int position) {
        holder.tvShippingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItemActionListener.onChangeShipment(cartData);
            }
        });
        holder.tvShipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItemActionListener.onChangeShipment(cartData);
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
                            cartItemActionListener.onCancelCartItem(cartData);
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
                    cartItemActionListener.onSubmitEditCartItem(cartData,
                            adapterProduct.getCartProductEditDataList());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.holderDetailCart.setInRecyclerView(true);
        holder.holderDetailCart.setInterpolator(
                Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR)
        );
        holder.holderDetailCart.setExpanded(expandState.get(position));
        holder.holderDetailCart.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                createRotateAnimator(holder.ivIconToggleDetail, 0f, 180f).start();
                expandState.put(position, true);
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(holder.ivIconToggleDetail, 180f, 0f).start();
                expandState.put(position, false);
            }
        });

        holder.ivIconToggleDetail.setRotation(expandState.get(position) ? 180f : 0f);
        holder.holderDetailCartToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                holder.holderDetailCart.toggle();
            }
        });
    }

    private ObjectAnimator createRotateAnimator(final View target, final float from,
                                                final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    private void renderCartProductList(ViewHolder holder, final CartItem cartData,
                                       CartProductItemAdapter adapterProduct) {
        adapterProduct.setCartProductAction(new CartProductItemAdapter.CartProductAction() {
            @Override
            public void onCancelCartProduct(CartProduct cartProduct) {
                if (cartItemActionListener != null)
                    cartItemActionListener.onCancelCartProduct(cartData, cartProduct);
            }
        });
        adapterProduct.fillDataList(cartData.getCartProducts());
        holder.rvCartProduct.setLayoutManager(new LinearLayoutManager(hostFragment.getActivity()));
        holder.rvCartProduct.setAdapter(adapterProduct);

    }

    private void renderErrorCartItem(ViewHolder holder, CartItem cartData) {
        if ((cartData.getCartErrorMessage2() != null
                && !cartData.getCartErrorMessage2().equals("0"))
                || (cartData.getCartErrorMessage1() != null
                && !cartData.getCartErrorMessage1().equals("0"))) {
            holder.holderError.setVisibility(View.VISIBLE);
            holder.tvError1.setText(MessageFormat.format("{0}", cartData.getCartErrorMessage1()));
            holder.tvError2.setText(MessageFormat.format("{0}", cartData.getCartErrorMessage2()));
        } else {
            holder.holderError.setVisibility(View.GONE);
        }
    }

    private void renderDropShipperOption(final ViewHolder holder,
                                         final CartItemEditable cartItemEditable) {
        switch (cartItemEditable.getErrorType()) {
            case CartItemEditable.ERROR_DROPSHIPPER_NAME:
                holder.tilEtDropshiperName.setErrorEnabled(true);
                holder.tilEtDropshiperName.setError(
                        hostFragment.getString(R.string.label_error_form_dropshipper_name_empty)
                );
                break;
            case CartItemEditable.ERROR_DROPSHIPPER_PHONE:
                holder.tilEtDropshiperPhone.setErrorEnabled(true);
                holder.tilEtDropshiperPhone.setError(
                        hostFragment.getString(R.string.label_error_form_dropshipper_phone_empty)
                );
                break;
        }
        final CartItem cartData = cartItemEditable.getCartItem();

        holder.etDropshiperName.setText(cartItemEditable.getDropShipperName() != null
                ? cartItemEditable.getDropShipperName() : "");
        holder.etDropshiperPhone.setText(cartItemEditable.getDropShipperPhone() != null
                ? cartItemEditable.getDropShipperPhone() : "");

        final TextWatcher textWatcherDropShipperName
                = getWatcherEtDropShipperName(cartItemEditable, cartData, holder);
        final TextWatcher textWatcherDropShipperPhone
                = getWatcherEtDropShipperPhone(cartItemEditable, cartData, holder);

        holder.cbDropshiper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateDropShipperCartName(
                            cartData, holder.etDropshiperName.getText().toString()
                    );
                    updateDropShipperCartPhone(
                            cartData, holder.etDropshiperPhone.getText().toString()
                    );
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
        holder.cbDropshiper.setChecked(cartItemEditable.isDropShipper());
    }

    private void renderPartialDeliverOption(ViewHolder holder, final CartItem cartData) {
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
                }
        );
    }

    private void updateDropShipperCartName(CartItem cartData, String dropShipperName) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getCartItem().getCartString()
                    .equals(cartData.getCartString())) {
                ((CartItemEditable) dataList.get(i)).setDropShipperName(dropShipperName);
                return;
            }
        }
    }

    private void updateDropShipperCartPhone(CartItem cartData, String dropShipperPhone) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getCartItem().getCartString()
                    .equals(cartData.getCartString())) {
                ((CartItemEditable) dataList.get(i)).setDropShipperPhone(dropShipperPhone);
                return;
            }
        }
    }

    private void deletePartialDeliverCartData(CartItem cartData) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getCartItem().getCartString()
                    .equals(cartData.getCartString())) {
                ((CartItemEditable) dataList.get(i)).setPartialDeliver(false);
                ((CartItemEditable) dataList.get(i)).setCartStringForDeliverOption(
                        cartData.getCartString()
                );
                return;
            }
        }
    }

    private void insertPartialDeliverCartData(CartItem cartData) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getCartItem().getCartString()
                    .equals(cartData.getCartString())) {
                ((CartItemEditable) dataList.get(i)).setPartialDeliver(true);
                ((CartItemEditable) dataList.get(i)).setCartStringForDeliverOption("");
                return;
            }
        }
    }

    private void deleteDropShipperCartData(CartItem cartData) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getCartItem().getCartString()
                    .equals(cartData.getCartString())) {
                ((CartItemEditable) dataList.get(i)).setDropShipper(false);
                ((CartItemEditable) dataList.get(i)).setCartStringForDropShipperOption("");
                return;
            }
        }
    }

    private void insertDropShipperCartData(CartItem cartData) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getCartItem().getCartString()
                    .equals(cartData.getCartString())) {
                ((CartItemEditable) dataList.get(i)).setDropShipper(true);
                ((CartItemEditable) dataList.get(i)).setCartStringForDropShipperOption(
                        cartData.getCartString()
                );
                return;
            }
        }
    }

    private void renderInsuranceOption(ViewHolder holder, final CartItem cartData) {
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
                    cartItemActionListener.onUpdateInsuranceCartItem(cartData, isUse);
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
            if (cartProduct.getProductMustInsurance().equals("1")) return true;
        }
        return false;
    }

    private boolean isProductUseInsurance(List<CartProduct> cartProducts) {
        for (CartProduct cartProduct : cartProducts) {
            if (cartProduct.getProductMustInsurance().equals("1")
                    || cartProduct.getProductUseInsurance() == 1) return true;
        }
        return false;
    }

    private void enableEditMode(CartItem cartData) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getCartItem().getCartString()
                    .equals(cartData.getCartString())) {
                ((CartItemEditable) dataList.get(i)).setEditMode(true);
                this.notifyItemChanged(i);
                return;
            }
        }
    }

    private void disableEditMode(CartItem cartData) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof CartItemEditable
                    && ((CartItemEditable) dataList.get(i)).getCartItem().getCartString()
                    .equals(cartData.getCartString())) {
                ((CartItemEditable) dataList.get(i)).setEditMode(false);
                this.notifyItemChanged(i);
                return;
            }
        }
    }

    @NonNull
    private TextWatcher getWatcherEtDropShipperPhone(final CartItemEditable data,
                                                     final CartItem cartData,
                                                     final ViewHolder holder) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    holder.tilEtDropshiperPhone.setError(null);
                    holder.tilEtDropshiperPhone.setErrorEnabled(false);
                } else {
                    holder.tilEtDropshiperPhone.setErrorEnabled(true);
                    holder.tilEtDropshiperPhone.setError(
                            hostFragment.getString(R.string.label_error_form_dropshipper_phone_empty)
                    );
                }
                if (!s.toString().equalsIgnoreCase(data.getDropShipperPhone()))
                    updateDropShipperCartPhone(cartData, s.toString());
            }
        };
    }

    @NonNull
    private TextWatcher getWatcherEtDropShipperName(final CartItemEditable data,
                                                    final CartItem cartData,
                                                    final ViewHolder holder) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    holder.tilEtDropshiperName.setError(null);
                    holder.tilEtDropshiperName.setErrorEnabled(false);
                } else {
                    holder.tilEtDropshiperName.setErrorEnabled(true);
                    holder.tilEtDropshiperName.setError(
                            hostFragment.getString(R.string.label_error_form_dropshipper_name_empty)
                    );
                }
                if (!s.toString().equalsIgnoreCase(data.getDropShipperName()))
                    updateDropShipperCartName(cartData, s.toString());
            }
        };
    }

    @SuppressWarnings("deprecation")
    private void renderEditableMode(ViewHolder holder, boolean isEditMode,
                                    CartProductItemAdapter adapterProduct) {
        if (isEditMode) {
            holder.holderActionEditor.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.holderContainer.setBackground(
                        hostFragment.getResources().getDrawable(R.drawable.bg_cart_item_editable_mode)
                );
            } else {
                holder.holderContainer.setBackgroundDrawable(
                        hostFragment.getResources().getDrawable(R.drawable.bg_cart_item_editable_mode)
                );
            }
            holder.btnOverflow.setEnabled(false);
            adapterProduct.enableEditMode();
            adapterProduct.notifyDataSetChanged();
        } else {
            holder.holderActionEditor.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.holderContainer.setBackground(
                        hostFragment.getResources().getDrawable(R.drawable.bg_cart_item_normal_mode)
                );
            } else {
                holder.holderContainer.setBackgroundDrawable(
                        hostFragment.getResources().getDrawable(R.drawable.bg_cart_item_normal_mode)
                );
            }
            holder.btnOverflow.setEnabled(true);
            adapterProduct.disableEditMode();
            adapterProduct.notifyDataSetChanged();
        }
        holder.holderActionEditor.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
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
        AppCompatCheckBox cbDropshiper;
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
        @BindView(R2.id.tv_total_price)
        TextView tvTotalPrice;
        @BindView(R2.id.holder_detail_cart)
        ExpandableLinearLayout holderDetailCart;
        @BindView(R2.id.iv_icon_detail_cart_toggle)
        ImageView ivIconToggleDetail;
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

    public interface CartItemActionListener {
        void onCancelCartItem(CartItem data);

        void onCancelCartProduct(CartItem data, CartProduct cartProduct);

        void onChangeShipment(CartItem data);

        void onSubmitEditCartItem(CartItem cartData, List<ProductEditData> cartProductEditDataList);

        void onUpdateInsuranceCartItem(CartItem cartData, boolean useInsurance);
    }
}
