package com.tokopedia.transaction.checkout.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest.Data;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.data.entity.request.DataCheckoutRequest;
import com.tokopedia.transaction.checkout.domain.datamodel.CourierItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentDetailData;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartSellerItemModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.transaction.checkout.view.mapper.ShipmentDataRequestConverter;
import com.tokopedia.transaction.checkout.view.viewholder.CartPromoSuggestionViewHolder;
import com.tokopedia.transaction.checkout.view.viewholder.CartPromoViewHolder;
import com.tokopedia.transaction.checkout.view.viewholder.CartSellerItemViewHolder;
import com.tokopedia.transaction.checkout.view.viewholder.RecipientAddressViewHolder;
import com.tokopedia.transaction.checkout.view.viewholder.ShipmentCostViewHolder;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * @author Aghny A. Putra on 25/01/18
 */

public class SingleAddressShipmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_PROMO = R.layout.holder_item_cart_promo;
    private static final int ITEM_VIEW_PROMO_SUGGESTION = R.layout.view_item_promo_suggestion;
    private static final int ITEM_VIEW_RECIPIENT_ADDRESS = R.layout.view_item_shipment_recipient_address;
    private static final int ITEM_VIEW_SHIPMENT_COST = R.layout.view_item_shipment_cost_details;
    private static final int ITEM_VIEW_CART = R.layout.item_shipped_product_details;

    private List<Object> mShipmentDataList;
    private ActionListener mActionListener;

    private RecipientAddressModel mRecipientAddress;
    private ShipmentCostModel mShipmentCost;

    private ShipmentDataRequestConverter mRequestConverter;

    @Inject
    public SingleAddressShipmentAdapter(ActionListener actionListener,
                                        ShipmentDataRequestConverter requestConverter) {
        mShipmentDataList = new ArrayList<>();
        mActionListener = actionListener;
        mRequestConverter = requestConverter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, viewGroup, false);

        if (viewType == ITEM_VIEW_PROMO) {
            return new CartPromoViewHolder(view, mActionListener);
        } else if (viewType == ITEM_VIEW_PROMO_SUGGESTION) {
            return new CartPromoSuggestionViewHolder(view, mActionListener);
        } else if (viewType == ITEM_VIEW_RECIPIENT_ADDRESS) {
            return new RecipientAddressViewHolder(view, mActionListener);
        } else if (viewType == ITEM_VIEW_CART) {
            return new CartSellerItemViewHolder(view, context, mActionListener);
        } else if (viewType == ITEM_VIEW_SHIPMENT_COST) {
            return new ShipmentCostViewHolder(view, mActionListener);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        Object data = mShipmentDataList.get(position);

        if (viewType == ITEM_VIEW_PROMO) {
            ((CartPromoViewHolder) viewHolder).bindViewHolder((CartItemPromoHolderData) data, position);
        } else if (viewType == ITEM_VIEW_PROMO_SUGGESTION) {
            ((CartPromoSuggestionViewHolder) viewHolder).bindViewHolder((CartPromoSuggestion) data,
                    position);
        } else if (viewType == ITEM_VIEW_RECIPIENT_ADDRESS) {
            ((RecipientAddressViewHolder) viewHolder).bindViewHolder((RecipientAddressModel) data);
        } else if (viewType == ITEM_VIEW_CART) {
            ((CartSellerItemViewHolder) viewHolder).bindViewHolder((CartSellerItemModel) data,
                    mShipmentCost, mRecipientAddress);
        } else if (viewType == ITEM_VIEW_SHIPMENT_COST) {
            ((ShipmentCostViewHolder) viewHolder).bindViewHolder((ShipmentCostModel) data);
        }
    }

    @Override
    public int getItemCount() {
        return mShipmentDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mShipmentDataList.get(position);

        if (item instanceof CartItemPromoHolderData) {
            return ITEM_VIEW_PROMO;
        } else if (item instanceof CartPromoSuggestion) {
            return ITEM_VIEW_PROMO_SUGGESTION;
        } else if (item instanceof RecipientAddressModel) {
            return ITEM_VIEW_RECIPIENT_ADDRESS;
        } else if (item instanceof CartSellerItemModel) {
            return ITEM_VIEW_CART;
        } else if (item instanceof ShipmentCostModel) {
            return ITEM_VIEW_SHIPMENT_COST;
        }

        return super.getItemViewType(position);
    }

    public void updateItemPromoVoucher(CartItemPromoHolderData cartPromo) {
        for (int i = 0; i < mShipmentDataList.size(); i++) {
            Object object = mShipmentDataList.get(i);
            if (object instanceof CartItemPromoHolderData) {
                mShipmentDataList.set(i, cartPromo);
                notifyItemChanged(i);
                checkDataForCheckout();
                return;
            }
        }
    }

    private void checkDataForCheckout() {
        boolean availableCheckout = true;
        for (Object object : mShipmentDataList) {
            if (object instanceof CartSellerItemModel) {
                if (((CartSellerItemModel) object).getSelectedShipmentDetailData() == null) {
                    availableCheckout = false;
                }
            }
        }
        if (availableCheckout) {
            mActionListener.onCartDataEnableToCheckout();
        } else {
            mActionListener.onCartDataDisableToCheckout();
        }
    }

    public void addPromoVoucherData(CartItemPromoHolderData cartItemPromoHolderData) {
        mShipmentDataList.add(cartItemPromoHolderData);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void addPromoSuggestionData(CartPromoSuggestion cartPromoSuggestion) {
        mShipmentDataList.add(cartPromoSuggestion);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void addAddressShipmentData(RecipientAddressModel recipientAddress) {
        mRecipientAddress = recipientAddress;
        mShipmentDataList.add(recipientAddress);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void addCartItemDataList(List<CartSellerItemModel> cartItemList) {
        mShipmentDataList.addAll(cartItemList);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void addShipmentCostData(ShipmentCostModel shipmentCost) {
        mShipmentCost = shipmentCost;
        mShipmentDataList.add(shipmentCost);
    }

    public void removeData(int position) {
        mShipmentDataList.remove(position);
        notifyItemRemoved(position);
        checkDataForCheckout();
    }

    public void updateSelectedAddress(RecipientAddressModel recipientAddress) {
        for (Object item : mShipmentDataList) {
            if (item instanceof RecipientAddressModel) {
                int index = mShipmentDataList.indexOf(item);
                mShipmentDataList.set(index, recipientAddress);
                this.mRecipientAddress = recipientAddress;
                notifyItemChanged(index);
                checkDataForCheckout();
                return;
            }
        }
    }

    public RecipientAddressModel getSelectedAddressReceipent() {
        return mRecipientAddress;
    }

    public interface ActionListener {

        void onAddOrChangeAddress();

        void onChooseShipment(int position, CartSellerItemModel cartSellerItemModel,
                              RecipientAddressModel recipientAddressModel);

        void onChoosePickupPoint(RecipientAddressModel addressAdapterData);

        void onClearPickupPoint(RecipientAddressModel addressAdapterData);

        void onEditPickupPoint(RecipientAddressModel addressAdapterData);

        void onCartPromoSuggestionActionClicked(CartPromoSuggestion cartPromoSuggestion, int position);

        void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion cartPromoSuggestion, int position);

        void onCartPromoUseVoucherPromoClicked(CartItemPromoHolderData cartPromo, int position);

        void onCartPromoCancelVoucherPromoClicked(CartItemPromoHolderData cartPromo, int position);

        void onCartPromoTrackingSuccess(CartItemPromoHolderData cartPromo, int position);

        void onCartPromoTrackingCancelled(CartItemPromoHolderData cartPromo, int position);

        void onTotalPaymentChange(ShipmentCostModel shipmentCostModel);

        void onFinishChoosingShipment(List<Data> data, List<DataCheckoutRequest> checkoutRequest);

        void onCartDataEnableToCheckout();

        void onCartDataDisableToCheckout();

        void onShowPromoMessage(String promoMessage);

        void onHidePromoMessage();

        void onRemovePromoCode();

    }


    public void setPickupPoint(Store store) {
        if (mRecipientAddress != null) {
            mRecipientAddress.setStore(store);
        }
        notifyDataSetChanged();
    }

    public void unSetPickupPoint() {
        mRecipientAddress.setStore(null);
        notifyDataSetChanged();
    }

    public void updatePromo(CheckPromoCodeCartShipmentResult.DataVoucher dataVoucher) {
        if (dataVoucher != null) {
            mShipmentCost.setPromoPrice(dataVoucher.getVoucherAmount());
            mShipmentCost.setPromoMessage(dataVoucher.getVoucherPromoDesc());
        } else {
            mShipmentCost.setPromoPrice(0);
            mShipmentCost.setPromoMessage(null);
            for (Object itemAdapter : mShipmentDataList) {
                if (itemAdapter instanceof CartItemPromoHolderData) {
                    ((CartItemPromoHolderData) itemAdapter).setPromoNotActive();
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public boolean hasAppliedPromoCode() {
        for (Object itemAdapter : mShipmentDataList) {
            if (itemAdapter instanceof CartItemPromoHolderData) {
                return ((CartItemPromoHolderData) itemAdapter).getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_VOUCHER ||
                        ((CartItemPromoHolderData) itemAdapter).getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_COUPON;
            }
        }
        return false;
    }

    public void updateSelectedShipment(int position, ShipmentDetailData shipmentDetailData) {
        int counter = 0;
        boolean isCourierComplete = true;

        mShipmentCost.setShippingFee(0);
        mShipmentCost.setInsuranceFee(0);

        List<CartSellerItemModel> cartSellerItemList = new ArrayList<>();

        for (Object item : mShipmentDataList) {
            if (item instanceof CartSellerItemModel) {
                CartSellerItemModel cartSellerItem = (CartSellerItemModel) item;
                cartSellerItemList.add(cartSellerItem);

                // Item which its courier has been updated
                if (counter == position) {
                    CourierItemData courierItemData = shipmentDetailData.getSelectedCourier();

                    cartSellerItem.setSelectedShipmentDetailData(shipmentDetailData);
                    cartSellerItem.setShippingFee(courierItemData.getDeliveryPrice()
                            + courierItemData.getAdditionalPrice());

                    if (shipmentDetailData.getUseInsurance()) {
                        cartSellerItem.setInsuranceFee(courierItemData.getInsurancePrice());
                    }

                    cartSellerItem.setTotalPrice(cartSellerItem.getTotalItemPrice()
                            + cartSellerItem.getShippingFee()
                            + cartSellerItem.getInsuranceFee());
                }

                mShipmentCost.setShippingFee(mShipmentCost.getShippingFee()
                        + cartSellerItem.getShippingFee());
                mShipmentCost.setInsuranceFee(mShipmentCost.getInsuranceFee()
                        + cartSellerItem.getInsuranceFee());

                // Check if all cart shops have shipping courier
                if (cartSellerItem.getSelectedShipmentDetailData() == null) {
                    isCourierComplete = false;
                }
            }

            counter++;
        }

        mShipmentCost.setTotalPrice(calculateTotalPrice(mShipmentCost));
        mActionListener.onTotalPaymentChange(mShipmentCost);

        if (isCourierComplete) {
            RequestData requestData = mRequestConverter.generateRequestData(cartSellerItemList,
                    mRecipientAddress);
            mActionListener.onFinishChoosingShipment(requestData.getPromoRequestData(),
                    requestData.getCheckoutRequestData());
        }
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    private double calculateTotalPrice(ShipmentCostModel shipmentCost) {
        return shipmentCost.getShippingFee() == 0 ?
                0 : shipmentCost.getTotalItemPrice()
                + shipmentCost.getInsuranceFee()
                + shipmentCost.getShippingFee()
                - shipmentCost.getPromoPrice();
    }

    public static class RequestData {

        private List<Data> mPromoRequestData;
        private List<DataCheckoutRequest> mCheckoutRequestData;

        @Inject
        public RequestData() {
            mPromoRequestData = new ArrayList<>();
            mCheckoutRequestData = new ArrayList<>();
        }

        List<Data> getPromoRequestData() {
            return mPromoRequestData;
        }

        public void setPromoRequestData(List<Data> promoRequestData) {
            this.mPromoRequestData = promoRequestData;
        }

        List<DataCheckoutRequest> getCheckoutRequestData() {
            return mCheckoutRequestData;
        }

        public void setCheckoutRequestData(List<DataCheckoutRequest> checkoutRequestData) {
            this.mCheckoutRequestData = checkoutRequestData;
        }

    }
}