package com.tokopedia.transaction.cart.adapter;

import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.transaction.cart.model.CartInsurance;
import com.tokopedia.transaction.cart.model.CartItemEditable;
import com.tokopedia.transaction.cart.model.calculateshipment.ProductEditData;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;
import com.tokopedia.transaction.cart.model.cartdata.TransactionList;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 11/10/16.
 */

public class CartItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_CART_ITEM = R.layout.cart_item_holder;
    private final Fragment hostFragment;
    private final CartAction cartAction;
    private boolean editMode;

    private List<Object> dataList = new ArrayList<>();


    public interface CartAction {
        void onCancelCart(TransactionList data);

        void onCancelCartProduct(TransactionList data, CartProduct cartProduct);

        void onChangeShipment(TransactionList data);

        void onSubmitEditCart(TransactionList cartData, List<ProductEditData> cartProductEditDataList);
    }

    public void enableEditMode() {
        this.editMode = true;
        this.notifyDataSetChanged();
    }

    public void disableEditMode() {
        this.editMode = false;
        this.notifyDataSetChanged();
    }

    public CartItemAdapter(Fragment hostFragment, CartAction cartAction) {
        this.hostFragment = hostFragment;
        this.cartAction = cartAction;
    }

    public void fillDataList(List<TransactionList> dataList) {
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
        holder.holderActionEditor.setVisibility(editMode ? View.VISIBLE : View.GONE);
        
        ArrayAdapter<CartInsurance> cartInsuranceAdapter
                = new ArrayAdapter<>(
                hostFragment.getActivity(), android.R.layout.simple_spinner_item,
                CartInsurance.createListForAdapter()
        );
        holder.spUseInsurance.setAdapter(cartInsuranceAdapter);

        if (editMode) {
            holder.holderActionEditor.setVisibility(View.VISIBLE);
            holder.holderContainer.setCardBackgroundColor(
                    hostFragment.getResources().getColor(R.color.grey_100)
            );
            adapterProduct.enableEditMode();
        } else {
            holder.holderActionEditor.setVisibility(View.GONE);
            holder.holderContainer.setCardBackgroundColor(
                    hostFragment.getResources().getColor(R.color.white)
            );
            adapterProduct.disableEditMode();
        }

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
        holder.cbDropshiper.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        holder.holderDropshiperForm.setVisibility(
                                isChecked ? View.VISIBLE : View.GONE);
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
                            enableEditMode();
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
                disableEditMode();
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
        @Bind(R2.id.holder_container)
        CardView holderContainer;
        @Bind(R2.id.tv_error_1)
        TextView tvError1;
        @Bind(R2.id.tv_error_2)
        TextView tvError2;
        @Bind(R2.id.holder_error)
        LinearLayout holderError;
        @Bind(R2.id.tv_shop_name)
        TextView tvShopName;
        @Bind(R2.id.btn_overflow)
        ImageView btnOverflow;
        @Bind(R2.id.rv_cart_product)
        RecyclerView rvCartProduct;
        @Bind(R2.id.cb_dropshiper)
        CheckBox cbDropshiper;
        @Bind(R2.id.et_dropshiper_name)
        EditText etDropshiperName;
        @Bind(R2.id.til_et_dropshiper_name)
        TextInputLayout tilEtDropshiperName;
        @Bind(R2.id.et_dropshiper_phone)
        EditText etDropshiperPhone;
        @Bind(R2.id.til_et_dropshiper_phone)
        TextInputLayout tilEtDropshiperPhone;
        @Bind(R2.id.holder_dropshiper_form)
        LinearLayout holderDropshiperForm;
        @Bind(R2.id.holder_detail_cart_toggle)
        RelativeLayout holderDetailCartToggle;
        @Bind(R2.id.tv_shipping_address)
        TextView tvShippingAddress;
        @Bind(R2.id.tv_shipment)
        TextView tvShipment;
        @Bind(R2.id.sp_use_insurance)
        Spinner spUseInsurance;
        @Bind(R2.id.sp_shipment_option_choosen)
        Spinner spShipmentOptionChoosen;
        @Bind(R2.id.tv_weight)
        TextView tvWeight;
        @Bind(R2.id.tv_sub_total)
        TextView tvSubTotal;
        @Bind(R2.id.tv_shipping_cost)
        TextView tvShippingCost;
        @Bind(R2.id.tv_insurance_price)
        TextView tvInsurancePrice;
        @Bind(R2.id.tv_additional_cost)
        TextView tvAdditionalCost;
        @Bind(R2.id.holder_detail_cart)
        LinearLayout holderDetailCart;
        @Bind(R2.id.tv_total_price)
        TextView tvTotalPrice;

        @Bind(R2.id.holder_action_editor)
        LinearLayout holderActionEditor;
        @Bind(R2.id.btn_save)
        TextView btnSave;
        @Bind(R2.id.btn_cancel)
        TextView btnCancel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
