package com.tokopedia.transaction.checkout.view.view.shipmentform;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.data.mapper.ShipmentRatesDataMapper;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressPriceSummaryData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentCartData;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentDetailData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.router.ICartCheckoutModuleRouter;
import com.tokopedia.transaction.checkout.view.adapter.MultipleAddressShipmentAdapter;
import com.tokopedia.transaction.checkout.view.view.shippingoptions.ShipmentDetailActivity;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;
import com.tokopedia.transaction.pickuppoint.domain.usecase.GetPickupPointsUseCase;
import com.tokopedia.transaction.pickuppoint.view.activity.PickupPointActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.transaction.checkout.view.view.shippingoptions.ShipmentDetailActivity.EXTRA_SHIPMENT_DETAIL_DATA;
import static com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract.Constant.INTENT_DATA_POSITION;
import static com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract.Constant.INTENT_DATA_STORE;

/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressShipmentFragment extends TkpdFragment
        implements MultipleAddressShipmentAdapter.MultipleAddressShipmentAdapterListener {
    public static final String ARG_EXTRA_SHIPMENT_FORM_DATA = "ARG_EXTRA_SHIPMENT_FORM_DATA";
    public static final String ARG_EXTRA_CART_PROMO_SUGGESTION = "ARG_EXTRA_CART_PROMO_SUGGESTION";

    private static final int REQUEST_CODE_SHIPMENT_DETAIL = 11;
    private static final int REQUEST_CHOOSE_PICKUP_POINT = 12;

    @Inject
    IMultipleAddressShipmentPresenter presenter;

    private TextView totalPayment;

    private MultipleAddressShipmentAdapter shipmentAdapter;

    public static MultipleAddressShipmentFragment newInstance(CartShipmentAddressFormData cartShipmentAddressFormData,
                                                              CartPromoSuggestion cartPromoSuggestionData) {
        MultipleAddressShipmentFragment fragment = new MultipleAddressShipmentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_EXTRA_SHIPMENT_FORM_DATA, cartShipmentAddressFormData);
        bundle.putParcelable(ARG_EXTRA_CART_PROMO_SUGGESTION, cartPromoSuggestionData);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MultipleAddressShipmentFragment newInstance() {
        return new MultipleAddressShipmentFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.multiple_address_shipment_fragment, container, false);
        totalPayment = view.findViewById(R.id.total_payment_text_view);
        ViewGroup totalPaymentLayout = view.findViewById(R.id.total_payment_layout);
        RecyclerView orderAddressList = view.findViewById(R.id.order_shipment_list);
        orderAddressList.setLayoutManager(new LinearLayoutManager(getActivity()));
        shipmentAdapter = new MultipleAddressShipmentAdapter(
                dataList(),
                dummyPriceSummaryData(),
                this);
        orderAddressList.setAdapter(shipmentAdapter);
        orderAddressList.addOnScrollListener(onRecyclerViewScrolledListener(totalPaymentLayout));
        totalPayment.setText(shipmentAdapter.getTotalPayment());
        return view;
    }

    private List<MultipleAddressShipmentAdapterData> dataList() {
        List<MultipleAddressShipmentAdapterData> list = new ArrayList<>();
        list.add(dummyData());
        list.add(dummyData());
        return list;
    }

    private MultipleAddressShipmentAdapterData dummyData() {
        MultipleAddressShipmentAdapterData data = new MultipleAddressShipmentAdapterData();
        data.setSenderName("Adidas");
        data.setProductImageUrl("https://t00.deviantart.net/Qgvu_0dClD_BotaDpLBflGKcvbI=/300x200/filters:fixed_height(100,100):origin()/pre00/69b2/th/pre/f/2013/143/9/1/pusheen_the_cat_png_15_by_13taylorswiftlover13-d66chev.png");
        data.setProductName("Kaos Adidas Camo Tongue Tee...White & Red, XS");
        data.setProductPrice("Rp200.000");
        data.setProductPriceNumber(200000);
        data.setDestinationDistrictId("2283");
        data.setDestinationDistrictName("Kelapa Gading");
        data.setTokenPickup("Tokopedia%2BKero:juMixO/k%2ButV%2BcQ4pVNm3FSG1pw%3D");
        data.setUnixTime("1515753331");
        data.setItemData(dummyItemData());
        data.setShipmentCartData(dummyShipmentCartData());
        return data;
    }

    private MultipleAddressItemData dummyItemData() {
        MultipleAddressItemData data = new MultipleAddressItemData();
        data.setProductWeight("3Kg");
        data.setProductQty("1");
        data.setProductNotes("Saya pesan warna merah yah min.. jangan sampai salah\n" +
                "kirim barangnya gan!");
        data.setAddressTitle("Alamat Kantor");
        data.setAddressReceiverName("Agus Maulana");
        data.setAddress("Jl. Letjen S. Parman Kav.77, Wisma 77 Tower 2,\n" +
                "Tokopedia Lt. 2, Jakarta, 0817 1234 5678");
        return data;
    }

    private ShipmentCartData dummyShipmentCartData() {
        ShipmentCartData shipmentCartData = new ShipmentCartData();
        shipmentCartData.setCategoryIds("4");
        shipmentCartData.setInsurance(2000);
        shipmentCartData.setWeight(500);
        shipmentCartData.setInsurancePrice(5000);
        shipmentCartData.setAdditionalFee(1000);
        shipmentCartData.setDeliveryPriceTotal(50000);
        return shipmentCartData;
    }

    private void showCancelPickupBoothDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.label_dialog_title_cancel_pickup);
        builder.setMessage(R.string.label_dialog_message_cancel_pickup_booth);
        builder.setPositiveButton(R.string.title_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shipmentAdapter.unSetPickupPoint(position);
                shipmentAdapter.notifyItemChanged(position);
            }
        });
        builder.setNegativeButton(R.string.title_no, null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onConfirmedButtonClicked(List<MultipleAddressShipmentAdapterData> addressDataList,
                                         MultipleAddressPriceSummaryData data) {
        presenter.sendData(addressDataList, data);
    }

    @Override
    public void onChooseShipment(MultipleAddressShipmentAdapterData addressAdapterData) {
        ShipmentDetailData shipmentDetailData;
        if (shipmentAdapter.getShipmentDetailData() != null) {
            shipmentDetailData = shipmentAdapter.getShipmentDetailData();
        } else {
            ShipmentRatesDataMapper shipmentRatesDataMapper = new ShipmentRatesDataMapper();
            shipmentDetailData = shipmentRatesDataMapper.getShipmentDetailData(addressAdapterData);
        }
        startActivityForResult(ShipmentDetailActivity.createInstance(getActivity(), shipmentDetailData),
                REQUEST_CODE_SHIPMENT_DETAIL);
    }

    @Override
    public void onChoosePickupPoint(MultipleAddressShipmentAdapterData addressAdapterData, int position) {
        startActivityForResult(PickupPointActivity.createInstance(
                getActivity(),
                position,
                addressAdapterData.getDestinationDistrictName(),
                GetPickupPointsUseCase.generateParams(addressAdapterData)
        ), REQUEST_CHOOSE_PICKUP_POINT);
    }

    @Override
    public void onClearPickupPoint(MultipleAddressShipmentAdapterData addressAdapterData, int position) {
        showCancelPickupBoothDialog(position);
    }

    @Override
    public void onEditPickupPoint(MultipleAddressShipmentAdapterData addressAdapterData, int position) {
        startActivityForResult(PickupPointActivity.createInstance(
                getActivity(),
                position,
                addressAdapterData.getDestinationDistrictName(),
                GetPickupPointsUseCase.generateParams(addressAdapterData)
        ), REQUEST_CHOOSE_PICKUP_POINT);
    }

    @Override
    public void onPromoSuggestionClicked(MultipleAddressPriceSummaryData priceSummaryData) {

    }

    @Override
    public void onHachikoClicked(MultipleAddressPriceSummaryData addressPriceSummaryData) {
        if (getActivity().getApplication() instanceof ICartCheckoutModuleRouter) {
            startActivityForResult(
                    ((ICartCheckoutModuleRouter) getActivity().getApplication())
                            .tkpdCartCheckoutGetLoyaltyNewCheckoutMarketplaceCartShipmentIntent(
                                    getActivity(), "", addressPriceSummaryData.isCouponActive()
                            ), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE
            );
        }
    }

    private RecyclerView.OnScrollListener onRecyclerViewScrolledListener(
            final ViewGroup totalPaymentLayout
    ) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();
                CommonUtils.dumper("totalItemCount " + totalItemCount);
                CommonUtils.dumper("lastVisibleItem " + lastVisibleItem);
                if (lastVisibleItem == totalItemCount - 1) {
                    totalPaymentLayout.setVisibility(View.GONE);
                } else {
                    totalPaymentLayout.setVisibility(View.VISIBLE);
                }

            }
        };
    }

    private MultipleAddressPriceSummaryData dummyPriceSummaryData() {
        MultipleAddressPriceSummaryData data = new MultipleAddressPriceSummaryData();
        data.setAdditionalFee(10000);
        data.setInsurancePrice(1000);
        data.setPromoDiscount(4000);
        data.setQuantity(3);
        data.setTotalProductPrice(10000);
        return new MultipleAddressPriceSummaryData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHOOSE_PICKUP_POINT:
                    Store pickupBooth = data.getParcelableExtra(INTENT_DATA_STORE);
                    int position = data.getIntExtra(INTENT_DATA_POSITION, 0);
                    shipmentAdapter.setPickupPoint(pickupBooth, position);
                    shipmentAdapter.notifyItemChanged(position);
                    totalPayment.setText(shipmentAdapter.getTotalPayment());
                    break;
                case REQUEST_CODE_SHIPMENT_DETAIL:
                    ShipmentDetailData shipmentDetailData = data.getParcelableExtra(EXTRA_SHIPMENT_DETAIL_DATA);
                    shipmentAdapter.setShipmentDetailData(shipmentDetailData);
                    shipmentAdapter.notifyDataSetChanged();
                    totalPayment.setText(shipmentAdapter.getTotalPayment());
                    break;
            }
        }
    }
}
