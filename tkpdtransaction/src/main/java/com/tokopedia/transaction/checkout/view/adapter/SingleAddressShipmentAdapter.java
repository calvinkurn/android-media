package com.tokopedia.transaction.checkout.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest.Data;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
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
import com.tokopedia.transaction.checkout.view.view.shipmentform.SingleAddressShipmentFragment;
import com.tokopedia.transaction.checkout.view.viewholder.CartPromoSuggestionViewHolder;
import com.tokopedia.transaction.checkout.view.viewholder.CartSellerItemViewHolder;
import com.tokopedia.transaction.checkout.view.viewholder.CartVoucherPromoViewHolder;
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

    private static final int ITEM_VIEW_RECIPIENT_ADDRESS = R.layout.view_item_shipment_recipient_address;
    private static final int ITEM_VIEW_SHIPMENT_COST = R.layout.view_item_shipment_cost_details;
    private static final int ITEM_VIEW_CART = R.layout.item_shipped_product_details;

    private List<Object> shipmentDataList;
    private ActionListener actionListener;

    private RecipientAddressModel recipientAddress;
    private ShipmentCostModel shipmentCost;

    private ShipmentDataRequestConverter requestConverter;

    private ArrayList<ShowCaseObject> showCaseObjectList;

    private Context context;

    @Inject
    public SingleAddressShipmentAdapter(ActionListener actionListener,
                                        ShipmentDataRequestConverter requestConverter) {
        shipmentDataList = new ArrayList<>();
        showCaseObjectList = new ArrayList<>();
        this.actionListener = actionListener;
        this.requestConverter = requestConverter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, viewGroup, false);

        if (viewType == CartVoucherPromoViewHolder.TYPE_VIEW_PROMO) {
            return new CartVoucherPromoViewHolder(view, actionListener);
        } else if (viewType == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            return new CartPromoSuggestionViewHolder(view, actionListener);
        } else if (viewType == ITEM_VIEW_RECIPIENT_ADDRESS) {
            RelativeLayout rlShipmentRecipientAddressLayout = view.findViewById(R.id.rl_shipment_recipient_address_header);
            showCaseObjectList.add(new ShowCaseObject(rlShipmentRecipientAddressLayout,
                    "Alamat Pengiriman",
                    "Pastikan alamat pengiriman sudah sesuai dengan\nyang kamu inginkan",
                    ShowCaseContentPosition.UNDEFINED)
            );
            return new RecipientAddressViewHolder(view, actionListener);
        } else if (viewType == ITEM_VIEW_CART) {
            LinearLayout llShipmentOptionLayout = view.findViewById(R.id.ll_shipment_option_view_layout);
            showCaseObjectList.add(new ShowCaseObject(llShipmentOptionLayout,
                    "Pilih Kurir Pengiriman",
                    "Gunakan layanan jasa pengiriman yang didukung oleh\ntoko ini.",
                    ShowCaseContentPosition.UNDEFINED)
            );
            return new CartSellerItemViewHolder(view, context, actionListener);
        } else if (viewType == ITEM_VIEW_SHIPMENT_COST) {
            return new ShipmentCostViewHolder(view, actionListener);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        Object data = shipmentDataList.get(position);

        if (viewType == CartVoucherPromoViewHolder.TYPE_VIEW_PROMO) {
            ((CartVoucherPromoViewHolder) viewHolder).bindData((CartItemPromoHolderData) data, position);
        } else if (viewType == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            ((CartPromoSuggestionViewHolder) viewHolder).bindData((CartPromoSuggestion) data, position);
        } else if (viewType == ITEM_VIEW_RECIPIENT_ADDRESS) {
            ((RecipientAddressViewHolder) viewHolder).bindViewHolder((RecipientAddressModel) data);
        } else if (viewType == ITEM_VIEW_CART) {
            ((CartSellerItemViewHolder) viewHolder).bindViewHolder((CartSellerItemModel) data,
                    shipmentCost, recipientAddress);
            setShowCase();
        } else if (viewType == ITEM_VIEW_SHIPMENT_COST) {
            ((ShipmentCostViewHolder) viewHolder).bindViewHolder((ShipmentCostModel) data);
        }
    }

    @Override
    public int getItemCount() {
        return shipmentDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = shipmentDataList.get(position);

        if (item instanceof CartItemPromoHolderData) {
            return CartVoucherPromoViewHolder.TYPE_VIEW_PROMO;
        } else if (item instanceof CartPromoSuggestion) {
            return CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION;
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
        for (int i = 0; i < shipmentDataList.size(); i++) {
            Object object = shipmentDataList.get(i);
            if (object instanceof CartItemPromoHolderData) {
                shipmentDataList.set(i, cartPromo);
                checkDataForCheckout();
                notifyItemChanged(i);
            } else if (object instanceof CartPromoSuggestion) {
                ((CartPromoSuggestion) object).setVisible(false);
                notifyItemChanged(i);
            }
        }
        notifyItemChanged(getItemCount() - 1);
    }

    private void checkDataForCheckout() {
        boolean availableCheckout = true;
        for (Object object : shipmentDataList) {
            if (object instanceof CartSellerItemModel) {
                if (((CartSellerItemModel) object).getSelectedShipmentDetailData() == null ||
                        ((CartSellerItemModel) object).isError()) {
                    availableCheckout = false;
                }
            }
        }
        if (availableCheckout) {
            actionListener.onCartDataEnableToCheckout();
        } else {
            actionListener.onCartDataDisableToCheckout();
        }
    }

    public void addPromoVoucherData(CartItemPromoHolderData cartItemPromoHolderData) {
        shipmentDataList.add(cartItemPromoHolderData);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void addPromoSuggestionData(CartPromoSuggestion cartPromoSuggestion) {
        shipmentDataList.add(cartPromoSuggestion);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void addAddressShipmentData(RecipientAddressModel recipientAddress) {
        this.recipientAddress = recipientAddress;
        shipmentDataList.add(recipientAddress);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void addCartItemDataList(List<CartSellerItemModel> cartItemList) {
        shipmentDataList.addAll(cartItemList);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void addShipmentCostData(ShipmentCostModel shipmentCost) {
        this.shipmentCost = shipmentCost;
        shipmentDataList.add(shipmentCost);
        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public void removeData(int position) {
        shipmentDataList.remove(position);
        notifyItemRemoved(position);
        checkDataForCheckout();
    }

    public void updateSelectedAddress(RecipientAddressModel recipientAddress) {
        for (Object item : shipmentDataList) {
            if (item instanceof RecipientAddressModel) {
                int index = shipmentDataList.indexOf(item);
                shipmentDataList.set(index, recipientAddress);
                this.recipientAddress = recipientAddress;

                notifyItemChanged(index);
                checkDataForCheckout();
                return;
            }
        }
    }

    public RecipientAddressModel getSelectedAddressRecipient() {
        return recipientAddress;
    }

    public void clearData() {
        shipmentDataList.clear();
        notifyDataSetChanged();
    }

    public interface ActionListener extends CartAdapterActionListener {

        void onAddOrChangeAddress();

        void onChooseShipment(int position, CartSellerItemModel cartSellerItemModel,
                              RecipientAddressModel recipientAddressModel);

        void onChoosePickupPoint(RecipientAddressModel addressAdapterData);

        void onClearPickupPoint(RecipientAddressModel addressAdapterData);

        void onEditPickupPoint(RecipientAddressModel addressAdapterData);

        void onTotalPaymentChange(ShipmentCostModel shipmentCostModel);

        void onFinishChoosingShipment(List<Data> data, List<DataCheckoutRequest> checkoutRequest);

        void onShowPromoMessage(String promoMessage);

        void onHidePromoMessage();

        void onRemovePromoCode();

    }

    public void setPickupPoint(Store store) {
        if (recipientAddress != null) {
            recipientAddress.setStore(store);
        }
        notifyDataSetChanged();
    }

    public void unSetPickupPoint() {
        recipientAddress.setStore(null);
        notifyDataSetChanged();
    }

    public boolean hasSetAllCourier() {
        for (Object itemData : shipmentDataList) {
            if (itemData instanceof CartSellerItemModel) {
                if (((CartSellerItemModel) itemData).getSelectedShipmentDetailData() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<CartSellerItemModel> getCartSellerItemModelList() {
        List<CartSellerItemModel> cartSellerItemModels = new ArrayList<>();
        for (Object itemView : shipmentDataList) {
            if (itemView instanceof CartSellerItemModel) {
                cartSellerItemModels.add((CartSellerItemModel) itemView);
            }
        }

        return cartSellerItemModels;
    }

    public void updatePromo(CheckPromoCodeCartShipmentResult.DataVoucher dataVoucher) {
        if (dataVoucher != null) {
            shipmentCost.setPromoPrice(dataVoucher.getVoucherAmount());
            shipmentCost.setPromoMessage(dataVoucher.getVoucherPromoDesc());
            for (int i = 0; i < shipmentDataList.size(); i++) {
                Object itemAdapter = shipmentDataList.get(i);
                if (itemAdapter instanceof CartPromoSuggestion) {
                    ((CartPromoSuggestion) itemAdapter).setVisible(false);
                    notifyItemChanged(i);
                }
            }
        } else {
            shipmentCost.setPromoPrice(0);
            shipmentCost.setPromoMessage(null);
            for (int i = 0; i < shipmentDataList.size(); i++) {
                Object itemAdapter = shipmentDataList.get(i);
                if (itemAdapter instanceof CartItemPromoHolderData) {
                    ((CartItemPromoHolderData) itemAdapter).setPromoNotActive();
                    notifyItemChanged(i);
                } else if (itemAdapter instanceof CartPromoSuggestion) {
                    ((CartPromoSuggestion) itemAdapter).setVisible(true);
                    notifyItemChanged(i);
                }
            }
        }
        notifyItemChanged(getItemCount() - 1);
    }

    public boolean hasAppliedPromoCode() {
        for (Object itemAdapter : shipmentDataList) {
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

        shipmentCost.setShippingFee(0);
        shipmentCost.setInsuranceFee(0);

        List<CartSellerItemModel> cartSellerItemList = new ArrayList<>();

        for (Object item : shipmentDataList) {
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

                shipmentCost.setShippingFee(shipmentCost.getShippingFee()
                        + cartSellerItem.getShippingFee());
                shipmentCost.setInsuranceFee(shipmentCost.getInsuranceFee()
                        + cartSellerItem.getInsuranceFee());

                // Check if all cart shops have shipping courier
                if (cartSellerItem.getSelectedShipmentDetailData() == null) {
                    isCourierComplete = false;
                }
            }

            counter++;
        }

        shipmentCost.setTotalPrice(calculateTotalPrice(shipmentCost));
        actionListener.onTotalPaymentChange(shipmentCost);

        if (isCourierComplete) {
            RequestData requestData = getRequestPromoData(cartSellerItemList);
            actionListener.onFinishChoosingShipment(requestData.getPromoRequestData(),
                    requestData.getCheckoutRequestData());
        }

        notifyDataSetChanged();
        checkDataForCheckout();
    }

    public RequestData getRequestPromoData(List<CartSellerItemModel> cartSellerItemList) {
        return requestConverter.generateRequestData(cartSellerItemList, recipientAddress);
    }

    private double calculateTotalPrice(ShipmentCostModel shipmentCost) {
        return shipmentCost.getShippingFee() == 0 ?
                0 : shipmentCost.getTotalItemPrice()
                + shipmentCost.getInsuranceFee()
                + shipmentCost.getShippingFee()
                - shipmentCost.getPromoPrice();
    }

    private void setShowCase() {
        if (!ShowCasePreference.hasShown(context, SingleAddressShipmentFragment.class.getName())) {
            createShowCaseDialog().show((Activity) context,
                    SingleAddressShipmentFragment.class.getName(),
                    showCaseObjectList
            );
        }
    }

    private ShowCaseDialog createShowCaseDialog() {
        return new ShowCaseBuilder()
                .customView(R.layout.show_case_checkout)
                .prevStringRes(R.string.show_case_prev)
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.spacing_show_case)
                .arrowWidth(R.dimen.arrow_width_show_case)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .textSizeRes(R.dimen.fontvs)
                .finishStringRes(R.string.show_case_finish)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(false)
                .build();
    }

    public static class RequestData {

        private List<Data> promoRequestData;
        private List<DataCheckoutRequest> checkoutRequestData;

        @Inject
        public RequestData() {
            promoRequestData = new ArrayList<>();
            checkoutRequestData = new ArrayList<>();
        }

        public List<Data> getPromoRequestData() {
            return promoRequestData;
        }

        public void setPromoRequestData(List<Data> promoRequestData) {
            this.promoRequestData = promoRequestData;
        }

        public List<DataCheckoutRequest> getCheckoutRequestData() {
            return checkoutRequestData;
        }

        public void setCheckoutRequestData(List<DataCheckoutRequest> checkoutRequestData) {
            this.checkoutRequestData = checkoutRequestData;
        }

    }

}