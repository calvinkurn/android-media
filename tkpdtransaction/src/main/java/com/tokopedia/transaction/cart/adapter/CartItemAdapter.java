package com.tokopedia.transaction.cart.adapter;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatCheckBox;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.addtocart.utils.KeroppiConstants;
import com.tokopedia.transaction.cart.model.CartInsurance;
import com.tokopedia.transaction.cart.model.CartItemEditable;
import com.tokopedia.transaction.cart.model.CartPartialDeliver;
import com.tokopedia.transaction.cart.model.calculateshipment.ProductEditData;
import com.tokopedia.transaction.cart.model.cartdata.CartCourierPrices;
import com.tokopedia.transaction.cart.model.cartdata.CartItem;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;
import com.tokopedia.transaction.cart.model.cartdata.CartShop;
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
    private static final int FIRST_PRODUCT_INDEX = 0;
    private static final int EDIT_MENU_INDEX = 0;
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
            renderDetailCartItem(holderItemCart, cartData, cartItemEditable);
            renderEditableMode(holderItemCart, cartItemEditable.isEditMode(), adapterProduct);
            renderPartialDeliverOption(holderItemCart, cartData);
            if (unEditable(cartData)) {
                holderItemCart.spShipmentOptionChoosen.setEnabled(false);
                holderItemCart.spUseInsurance.setEnabled(false);
                holderItemCart.cbDropshiper.setVisibility(View.GONE);
            } else {
                renderDropShipperOption(holderItemCart, cartItemEditable);
            }
            renderHolderViewListener(holderItemCart, cartData, adapterProduct, position);
            renderInsuranceOption(holderItemCart, cartItemEditable);

        }
    }

    private boolean unEditable(CartItem cartData) {
        return cartData.getCartProducts().get(FIRST_PRODUCT_INDEX).getProductHideEdit() != null
                && cartData.getCartProducts().get(FIRST_PRODUCT_INDEX).getProductHideEdit() == 1;
    }

    public void setRates(CartCourierPrices cartCourierPrices) {
        int position = cartCourierPrices.getCartIndex();
        final CartItemEditable cartItemEditable = (CartItemEditable) dataList.get(position);
        cartItemEditable.setCartCourierPrices(cartCourierPrices);
        removeCartErrors(cartItemEditable);
        cartItemEditable.setInsuranceUsedInfo(cartCourierPrices.getInsuranceUsedInfo());
        cartItemEditable.setInsuranceType(cartCourierPrices.getInsuranceMode());
        if (cartCourierPrices.getInsuranceMode() == KeroppiConstants.InsuranceType.NO) {
            cartItemEditable.setUseInsurance(false);
        } else if (cartCourierPrices.getInsuranceMode() == KeroppiConstants.InsuranceType.MUST) {
            cartItemEditable.setUseInsurance(true);
        }
    }

    private void removeCartErrors(CartItemEditable cartItemEditable) {
        cartItemEditable.getCartItem().setCartErrorMessage1("0");
        cartItemEditable.getCartItem().setCartErrorMessage2("0");
    }

    public void setCartItemError(int position, String errorMessage1, String errorMessage2) {
        final CartItemEditable cartItemEditable = (CartItemEditable) dataList.get(position);
        cartItemEditable.getCartItem().setCartErrorMessage1(errorMessage1);
        cartItemEditable.getCartItem().setCartErrorMessage2(errorMessage2);
        cartItemEditable.setCartCourierPrices(new CartCourierPrices());
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

    public void fillDataList(String keroToken, List<CartItem> dataList) {
        for (int i = 0, dataListSize = dataList.size(); i < dataListSize; i++) {
            CartItem data = dataList.get(i);
            CartItemEditable cartItemEditable = new CartItemEditable(data);
            cartItemEditable.setKeroToken(keroToken);
            cartItemEditable.setUseInsurance(isInsuranced(data));
            this.dataList.add(cartItemEditable);
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

    private void renderDetailCartItem(ViewHolder holder,
                                      CartItem cartData,
                                      CartItemEditable cartItemEditable) {
        holder.tvShopName.setText(MethodChecker.fromHtml(cartData.getCartShop().getShopName()));
        holder.tvWeight.setText(String.format("%s Kg", cartData.getCartTotalWeight()));
        holder.tvShippingAddress.setText(String.format("%s (Ubah)",
                MethodChecker.fromHtml(cartData.getCartDestination().getReceiverName())));
        holder.tvShipment.setText(String.format("%s - %s (Ubah)",
                MethodChecker.fromHtml(cartData.getCartShipments().getShipmentName()),
                MethodChecker.fromHtml(cartData.getCartShipments().getShipmentPackageName())));
        if (cartItemEditable.getCartCourierPrices() != null) {
            holder.tvTotalPrice.setVisibility(View.VISIBLE);
            holder.tvShippingCost.setText(cartItemEditable.getCartCourierPrices()
                    .getShipmentPriceIdr());
            holder.tvSubTotal.setText(cartItemEditable.getCartCourierPrices()
                    .getCartProductPriceIdr());
            holder.tvTotalPrice.setText(cartItemEditable.getCartCourierPrices()
                    .getCartSubtotalIdr());
            if (cartItemEditable.isUseInsurance())
                holder.tvAdditionalCost.setText(cartItemEditable.getCartCourierPrices()
                        .getSumAdditionFeeInsuranceIdr());
            else holder.tvAdditionalCost.setText(cartItemEditable.getCartCourierPrices()
                    .getAdditionFeeIdr());
            holder.totalPriceProgressBar.setVisibility(View.GONE);
            if (unEditable(cartData)) {
                holder.tvShippingAddress.setText(MethodChecker
                        .fromHtml(cartData.getCartDestination().getReceiverName()));
                holder.tvShipment.setText(String.format("%s - %s",
                        MethodChecker.fromHtml(cartData.getCartShipments().getShipmentName()),
                        MethodChecker.fromHtml(cartData.getCartShipments()
                                .getShipmentPackageName())));
                holder.tvShippingAddress.setEnabled(false);
                holder.tvShipment.setEnabled(false);
            } else {
                holder.tvShippingAddress.setText(String.format("%s (Ubah)",
                        MethodChecker.fromHtml(cartData.getCartDestination().getReceiverName())));
                holder.tvShipment.setText(String.format("%s - %s (Ubah)",
                        MethodChecker.fromHtml(cartData.getCartShipments().getShipmentName()),
                        MethodChecker.fromHtml(cartData.getCartShipments()
                                .getShipmentPackageName())));
                holder.tvShippingAddress.setEnabled(true);
                holder.tvShipment.setEnabled(true);
            }
        } else holder.tvTotalPrice.setVisibility(View.GONE);
    }

    private void renderHolderViewListener(final ViewHolder holder, final CartItem cartData,
                                          final CartProductItemAdapter adapterProduct,
                                          final int position) {
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
                if (unEditable(cartData)) {
                    popupMenu.getMenu().getItem(EDIT_MENU_INDEX).setVisible(false);
                } else {
                    popupMenu.getMenu().getItem(EDIT_MENU_INDEX).setVisible(true);
                }
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
                    KeyboardHandler.hideSoftKeyboard(hostFragment.getActivity());
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

        holder.tvShopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItemActionListener.onShopDetailInfoClicked(cartData.getCartShop());
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

            @Override
            public void onProductCartItemClicked(ProductPass productPass) {
                if (cartItemActionListener != null)
                    cartItemActionListener.onCartProductDetailClicked(productPass);
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

            holder.totalPriceLayout.setVisibility(View.GONE);
            holder.calculationLayout.setVisibility(View.GONE);
            holder.totalPriceProgressBar.setVisibility(View.GONE);
        } else {
            holder.calculationLayout.setVisibility(View.VISIBLE);
            holder.totalPriceLayout.setVisibility(View.VISIBLE);
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
        if (cartData.getCartShipments().getShipmentPickUp() == 1) {
            holder.cbDropshiper.setVisibility(View.GONE);
        } else {
            holder.cbDropshiper.setVisibility(View.VISIBLE);
        }
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
                    holder.tilEtDropshiperName.requestFocus();
                    if (cartItemActionListener != null)
                        cartItemActionListener.onDropShipperOptionChecked();
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
        List<CartPartialDeliver> partialDeliverList;
        if (!cartData.getCartPartial().equalsIgnoreCase("1")
                || cartData.getCartTotalProduct() <= 1) {
            partialDeliverList = CartPartialDeliver.createListForCancelDeliverPartial();
        } else {
            partialDeliverList = CartPartialDeliver.createListForDeliverPartial();
        }
        ArrayAdapter<CartPartialDeliver> cartPartialDeliverAdapter
                = new ArrayAdapter<>(
                hostFragment.getActivity(), android.R.layout.simple_spinner_item,
                partialDeliverList
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
        holder.spShipmentOptionChoosen.setEnabled(cartData.getCartTotalProduct() > 1);
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
                ((CartItemEditable) dataList.get(i)).setCartStringForDeliverOption("");
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
                ((CartItemEditable) dataList.get(i)).setCartStringForDeliverOption(
                        cartData.getCartString()
                );
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

    private void renderInsuranceOption(ViewHolder holder, final CartItemEditable cartItemEditable) {
        final CartItem cartData = cartItemEditable.getCartItem();
        final boolean isUseInsurance = cartItemEditable.isUseInsurance();
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
                    cartItemActionListener.onUpdateInsuranceCartItem(cartItemEditable, isUse);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (isProductMustInsurance(cartData.getCartProducts())) {
            holder.spUseInsurance.setEnabled(false);
        } else if (unEditable(cartData)) {
            holder.spUseInsurance.setEnabled(false);
        } else if (cartItemEditable.getInsuranceType() == KeroppiConstants.InsuranceType.MUST ||
                cartItemEditable.getInsuranceType() == KeroppiConstants.InsuranceType.NO) {
            holder.spUseInsurance.setEnabled(false);
        } else if (cartItemEditable.getInsuranceType() == KeroppiConstants.InsuranceType.OPTIONAL) {
            holder.spUseInsurance.setEnabled(true);
        } else {
            holder.spUseInsurance.setEnabled(true);
        }

        cartInsuranceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spUseInsurance.setAdapter(cartInsuranceAdapter);

        if (cartItemEditable.getInsuranceUsedInfo() == null || cartItemEditable.getInsuranceUsedInfo().length() == 0) {
            holder.imgInsuranceInfo.setVisibility(View.GONE);
        } else {
            holder.imgInsuranceInfo.setVisibility(View.VISIBLE);
        }
        holder.imgInsuranceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetView bottomSheetView = new BottomSheetView(hostFragment.getActivity());
                bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                        .BottomSheetFieldBuilder()
                        .setTitle(hostFragment.getActivity().getString(R.string.title_bottomsheet_insurance))
                        .setBody(cartItemEditable.getInsuranceUsedInfo())
                        .setImg(R.drawable.ic_insurance)
                        .build());

                bottomSheetView.show();
            }
        });

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

    private boolean isInsuranced(CartItem cartItem) {
        return (isProductUseInsurance(cartItem.getCartProducts()));
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
            holder.btnOverflow.setVisibility(View.GONE);
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
            holder.btnOverflow.setVisibility(View.VISIBLE);
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
        holder.btnOverflow.setVisibility(isEditMode ? View.GONE : View.VISIBLE);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.holder_container)
        LinearLayout holderContainer;
        @BindView(R2.id.calculation_layout)
        LinearLayout calculationLayout;
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
        @BindView(R2.id.total_price_layout)
        LinearLayout totalPriceLayout;
        @BindView(R2.id.total_price_progress_bar)
        ProgressBar totalPriceProgressBar;
        @BindView(R2.id.img_insurance_info)
        ImageView imgInsuranceInfo;

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

        void onUpdateInsuranceCartItem(CartItemEditable cartItemEditable, boolean useInsurance);

        void onCartProductDetailClicked(ProductPass productPass);

        void onShopDetailInfoClicked(CartShop cartShop);

        void onDropShipperOptionChecked();
    }
}
