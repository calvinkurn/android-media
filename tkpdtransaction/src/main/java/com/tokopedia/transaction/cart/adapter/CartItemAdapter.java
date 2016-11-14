package com.tokopedia.transaction.cart.adapter;

import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.tokopedia.transaction.cart.model.CartItemEditable;
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

    private List<Object> dataList = new ArrayList<>();

    public interface CartAction {
        void onCancelCart(TransactionList data);

        void onCancelCartProduct(TransactionList data, CartProduct cartProduct);

        void onChangeShipment(TransactionList data);
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
        holder.tvShopName.setText(data.getTransactionList().getCartShop().getShopName());
        final CartProductItemAdapter adapterProduct = new CartProductItemAdapter(hostFragment);
        adapterProduct.fillDataList(data.getTransactionList().getCartProducts());
        holder.rvCartProduct.setLayoutManager(new LinearLayoutManager(hostFragment.getActivity()));
        holder.rvCartProduct.setAdapter(adapterProduct);
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
                            cartAction.onCancelCart(data.getTransactionList());
                            return true;
                        } else if (i == R.id.action_cart_edit) {
                            holder.holderActionEditor.setVisibility(View.VISIBLE);
                            adapterProduct.isEditableMode(true);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
        holder.tvInsurancePrice.setText(data.getTransactionList().getCartInsurancePriceIdr());
        holder.tvShippingCost.setText(data.getTransactionList().getCartShippingRateIdr());
        holder.tvSubTotal.setText(data.getTransactionList().getCartTotalProductPriceIdr());
        holder.tvTotalPrice.setText(data.getTransactionList().getCartTotalAmountIdr());
        holder.tvWeight.setText(data.getTransactionList().getCartTotalWeight());
        holder.tvShippingAddress.setText(String.format("%s (Ubah)",
                data.getTransactionList().getCartDestination().getReceiverName()));
        holder.tvShipment.setText(String.format("%s - %s (Ubah)",
                data.getTransactionList().getCartShipments().getShipmentName(),
                data.getTransactionList().getCartShipments().getShipmentPackageName()));
        holder.tvShippingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartAction.onChangeShipment(data.getTransactionList());
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) instanceof CartItemEditable) {
            return TYPE_CART_ITEM;
        }
        return super.getItemViewType(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R2.id.holder_container)
        LinearLayout holderContainer;
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
