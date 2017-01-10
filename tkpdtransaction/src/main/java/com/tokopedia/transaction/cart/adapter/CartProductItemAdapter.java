package com.tokopedia.transaction.cart.adapter;

import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.cart.model.CartProductItemEditable;
import com.tokopedia.transaction.cart.model.calculateshipment.ProductEditData;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 11/10/16.
 */
class CartProductItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_PRODUCT_ITEM = R.layout.holder_item_product_cart_tx_module;
    private final Fragment hostFragment;

    private List<Object> dataList = new ArrayList<>();
    private boolean editMode;
    private CartProductAction cartProductAction;

    void setCartProductAction(CartProductAction cartProductAction) {
        this.cartProductAction = cartProductAction;
    }

    interface CartProductAction {
        void onCancelCartProduct(CartProduct cartProduct);

        void onProductCartItemClicked(ProductPass productPass);
    }

    CartProductItemAdapter(Fragment hostFragment) {
        this.hostFragment = hostFragment;
    }

    void fillDataList(List<CartProduct> dataList) {
        for (CartProduct data : dataList) {
            this.dataList.add(new CartProductItemEditable(data));
        }
        this.notifyDataSetChanged();
    }

    void disableEditMode() {
        resetEditState();
        editMode = false;
        this.notifyDataSetChanged();
    }

    void enableEditMode() {
        editMode = true;
        this.notifyDataSetChanged();
    }

    List<ProductEditData> getCartProductEditDataList() throws IllegalAccessException {
        if (editMode) {
            List<ProductEditData> productEditDatas = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                Object object = dataList.get(i);
                if (object instanceof CartProductItemEditable) {
                    productEditDatas.add(((CartProductItemEditable) object).getProductEditData());
                }
            }
            return productEditDatas;
        } else {
            throw new IllegalAccessException("is not edit mode!!");
        }
    }

    private void resetEditState() {
        for (int i = 0; i < dataList.size(); i++) {
            Object object = dataList.get(i);
            if (object instanceof CartProductItemEditable) {
                ((CartProductItemEditable) dataList.get(i)).setProductEditData(
                        ProductEditData.initInstance(((CartProductItemEditable) object)
                                .getCartProduct())
                );
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PRODUCT_ITEM) {
            return new ProductItemHolder(LayoutInflater.from(
                    hostFragment.getActivity()).inflate(viewType, parent, false
            ));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int i = getItemViewType(position);
        if (i == TYPE_PRODUCT_ITEM) {
            final ProductItemHolder productItemHolder = (ProductItemHolder) holder;
            final CartProductItemEditable item = (CartProductItemEditable) dataList.get(position);
            bindProductItemHolder(productItemHolder, item);
            bindProductItemEditableListener(productItemHolder, item, position);
        }
    }

    private void bindProductItemEditableListener(ProductItemHolder holder,
                                                 CartProductItemEditable item, final int position) {
        holder.etNotesProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (dataList.get(position) instanceof CartProductItemEditable) {
                    ((CartProductItemEditable) dataList.get(position))
                            .getProductEditData().setProductNotes(s.toString()
                    );
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.etQuantityProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (dataList.get(position) instanceof CartProductItemEditable) {
                    int qty = ((CartProductItemEditable) dataList.get(position))
                            .getProductEditData().getProductQuantity();
                    try {
                        ((CartProductItemEditable) dataList.get(position))
                                .getProductEditData().setProductQuantity(
                                Integer.parseInt(s.toString())
                        );
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        ((CartProductItemEditable) dataList.get(position))
                                .getProductEditData().setProductQuantity(qty);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void bindProductItemHolder(ProductItemHolder holder, final CartProductItemEditable item) {
        renderEditableMode(holder);

        final CartProduct cartProduct = item.getCartProduct();

        if (item.getCartProduct().getProductErrorMsg() != null
                && !item.getCartProduct().getProductErrorMsg().isEmpty()
                && !item.getCartProduct().getProductErrorMsg().equalsIgnoreCase("0")) {
            holder.tvError.setText(item.getCartProduct().getProductErrorMsg());
            holder.tvError.setVisibility(View.VISIBLE);
        } else {
            holder.tvError.setVisibility(View.GONE);
        }

        if (cartProduct.getProductPreorder() != null
                && cartProduct.getProductPreorder().getStatus() == 1) {
            holder.tvPreorderLabel.setVisibility(View.VISIBLE);
            holder.tvPreorderPeriod.setVisibility(View.VISIBLE);
            holder.tvPreorderPeriod.setText(
                    hostFragment.getString(
                            R.string.hint_preorder_text).replace(
                            "YYY", cartProduct.getProductPreorder().getProcessTime()
                    )
            );
        } else {
            holder.tvPreorderLabel.setVisibility(View.GONE);
            holder.tvPreorderPeriod.setVisibility(View.GONE);
        }

        holder.tvNameProduct.setText(item.getCartProduct().getProductName());
        holder.tvPriceProduct.setText(item.getCartProduct().getProductPriceIdr());
        holder.tvWeightProduct.setText(
                String.format("%s Kg", item.getCartProduct().getProductWeight())
        );
        holder.etQuantityProduct.setEnabled(editMode);
        holder.etNotesProduct.setEnabled(editMode);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartProductAction != null)
                    cartProductAction.onCancelCartProduct(item.getCartProduct());
            }
        });
        holder.etQuantityProduct.setText(item.getTempQuantity());
        holder.etNotesProduct.setText(Html.fromHtml(item.getTempNotes()));

        holder.tvNameProduct.setOnClickListener(getOnProductDetailClickListener(cartProduct));
        holder.ivPicProduct.setOnClickListener(getOnProductDetailClickListener(cartProduct));
        holder.tvPriceProduct.setOnClickListener(getOnProductDetailClickListener(cartProduct));
        ImageHandler.loadImageRounded2(
                hostFragment, holder.ivPicProduct, item.getCartProduct().getProductPic()
        );

    }

    @NonNull
    private View.OnClickListener getOnProductDetailClickListener(final CartProduct cartProduct) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartProductAction.onProductCartItemClicked(
                        ProductPass.Builder.aProductPass()
                                .setProductId(cartProduct.getProductId())
                                .setProductImage(cartProduct.getProductPic())
                                .setProductPrice(cartProduct.getProductPriceIdr())
                                .setProductName(cartProduct.getProductName())
                                .setProductUri(cartProduct.getProductUrl())
                                .build()
                );
            }
        };
    }

    private void renderEditableMode(ProductItemHolder holder) {
        if (editMode) {
            holder.etNotesProduct.setEnabled(true);
            holder.etQuantityProduct.setEnabled(true);
        } else {
            holder.etNotesProduct.setEnabled(false);
            holder.etQuantityProduct.setEnabled(false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) instanceof CartProductItemEditable) {
            return TYPE_PRODUCT_ITEM;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ProductItemHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.tv_error)
        TextView tvError;
        @BindView(R2.id.tv_preorder_label)
        TextView tvPreorderLabel;
        @BindView(R2.id.btn_delete)
        ImageView btnDelete;
        @BindView(R2.id.iv_pic_product)
        ImageView ivPicProduct;
        @BindView(R2.id.tv_name_product)
        TextView tvNameProduct;
        @BindView(R2.id.tv_price_product)
        TextView tvPriceProduct;
        @BindView(R2.id.tv_weight_product)
        TextView tvWeightProduct;
        @BindView(R2.id.tv_preorder_period)
        TextView tvPreorderPeriod;
        @BindView(R2.id.et_quantity_product)
        EditText etQuantityProduct;
        @BindView(R2.id.et_notes_product)
        EditText etNotesProduct;

        ProductItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
