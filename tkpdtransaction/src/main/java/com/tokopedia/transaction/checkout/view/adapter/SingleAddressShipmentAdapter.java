package com.tokopedia.transaction.checkout.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentDetailData;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartSellerItemModel;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartPromo;
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

    private static final String TAG = SingleAddressShipmentAdapter.class.getSimpleName();

    private static final int ITEM_VIEW_PROMO =
            R.layout.holder_item_cart_promo;
    private static final int ITEM_VIEW_PROMO_SUGGESTION =
            R.layout.holder_item_cart_potential_promo;
    private static final int ITEM_VIEW_RECIPIENT_ADDRESS =
            R.layout.view_item_shipment_recipient_address;
    private static final int ITEM_VIEW_SHIPMENT_COST =
            R.layout.view_item_shipment_cost_details;
    private static final int ITEM_VIEW_CART =
            R.layout.item_shipped_product_details;

    private List<Object> mShipmentDataList;
    private ActionListener mActionListener;

    private RecipientAddressModel mRecipientAddress;
    private ShipmentCostModel mShipmentCost;

    @Inject
    public SingleAddressShipmentAdapter(ActionListener actionListener) {
        mShipmentDataList = new ArrayList<>();
        mActionListener = actionListener;
    }

    public void changeDataSet(List<Object> shipmentDataList) {
        mShipmentDataList = shipmentDataList;
    }

    public void setPickupPoint(Store store) {
        if (mRecipientAddress != null) {
            mRecipientAddress.setStore(store);
        }
    }

    public void unSetPickupPoint() {
        mRecipientAddress.setStore(null);
    }

    public void updateSelectedShipment(int position, ShipmentDetailData shipmentDetailData) {
        int counter = 0;
        for (Object item : mShipmentDataList) {
            if (item instanceof CartSellerItemModel && counter == position) {
                ((CartSellerItemModel) item).setSelectedShipmentDetailData(shipmentDetailData);
                break;
            }
            counter++;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context mContext = viewGroup.getContext();
        View view = LayoutInflater.from(mContext).inflate(viewType, viewGroup, false);

        if (viewType == ITEM_VIEW_PROMO) {
            return new CartPromoViewHolder(view, mActionListener);
        } else if (viewType == ITEM_VIEW_PROMO_SUGGESTION) {
            return new CartPromoSuggestionViewHolder(view, mActionListener);
        } else if (viewType == ITEM_VIEW_RECIPIENT_ADDRESS) {
            return new RecipientAddressViewHolder(view, mActionListener);
        } else if (viewType == ITEM_VIEW_CART) {
            return new CartSellerItemViewHolder(view, mActionListener);
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
            ((CartPromoViewHolder) viewHolder).bindViewHolder((CartPromo) data, position);
        } else if (viewType == ITEM_VIEW_PROMO_SUGGESTION) {
            ((CartPromoSuggestionViewHolder) viewHolder).bindViewHolder((CartPromoSuggestion) data, position);
        } else if (viewType == ITEM_VIEW_RECIPIENT_ADDRESS) {
            mRecipientAddress = (RecipientAddressModel) data;
            ((RecipientAddressViewHolder) viewHolder).bindViewHolder(mRecipientAddress);
        } else if (viewType == ITEM_VIEW_CART) {
            ((CartSellerItemViewHolder) viewHolder).bindViewHolder((CartSellerItemModel) data,
                    mShipmentCost, mRecipientAddress);
        } else if (viewType == ITEM_VIEW_SHIPMENT_COST) {
            mShipmentCost = (ShipmentCostModel) data;
            ((ShipmentCostViewHolder) viewHolder).bindViewHolder(mShipmentCost);
        }
    }

    @Override
    public int getItemCount() {
        return mShipmentDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mShipmentDataList.get(position);

        if (item instanceof CartPromo) {
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

    public interface ActionListener {

        void onAddOrChangeAddress();

        void onChooseShipment(int position, CartSellerItemModel cartSellerItemModel,
                              RecipientAddressModel recipientAddressModel);

        void onChoosePickupPoint(RecipientAddressModel addressAdapterData);

        void onClearPickupPoint(RecipientAddressModel addressAdapterData);

        void onEditPickupPoint(RecipientAddressModel addressAdapterData);

        void onCartPromoSuggestionActionClicked(CartPromoSuggestion cartPromoSuggestion, int position);

        void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion cartPromoSuggestion, int position);

        void onCartPromoUseVoucherPromoClicked(CartPromo cartPromo, int position);

        void onCartPromoCancelVoucherPromoClicked(CartPromo cartPromo, int position);

        void onCartPromoTrackingSuccess(CartPromo cartPromo, int position);

        void onCartPromoTrackingCancelled(CartPromo cartPromo, int position);

        void onTotalPaymentChange(ShipmentCostModel shipmentCostModel);

    }

}