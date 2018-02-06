package com.tokopedia.transaction.checkout.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.adapter.MultipleAddressShipmentAdapter;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressItemData;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressPriceSummaryData;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.view.presenter.IMultipleAddressShipmentPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressShipmentFragment extends TkpdFragment
        implements MultipleAddressShipmentAdapter.MultipleAddressShipmentAdapterListener {

    @Inject
    IMultipleAddressShipmentPresenter presenter;

    private TextView totalPayment;

    private MultipleAddressShipmentAdapter shipmentAdapter;

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
        totalPayment.setText(dummyPriceSummaryData().getTotalPaymentText());
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
        data.setItemData(dummyItemData());
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

    @Override
    public void onConfirmedButtonClicked(List<MultipleAddressShipmentAdapterData> addressDataList,
                                         MultipleAddressPriceSummaryData data) {
        presenter.sendData(addressDataList, data);
    }

    @Override
    public void onChooseShipment(MultipleAddressShipmentAdapterData addressAdapterData) {

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
}
