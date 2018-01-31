package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.transaction.checkout.view.data.CourierItemData;
import com.tokopedia.transaction.checkout.view.data.ShipmentItemData;
import com.tokopedia.transaction.checkout.view.view.IShipmentChoiceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class ShipmentChoicePresenter extends BaseDaggerPresenter<IShipmentChoiceView>
        implements IShipmentChoicePresenter {

    private List<ShipmentItemData> shipments = new ArrayList<>();

    @Override
    public void attachView(IShipmentChoiceView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {

    }

    @Override
    public void loadShipmentChoice() {
        getView().showLoading();
        shipments = DummyCreator.createDummyShipmentChoices();
        getView().showData();
        getView().hideLoading();
    }

    @Override
    public List<ShipmentItemData> getShipmentChoices() {
        return shipments;
    }


    static class DummyCreator {

        private static String[] shipmentItemDataIds = {"0", "1", "2", "3", "4", "5"};
        private static String[] shipmentItemDataTypes = {"Custom", "Instan", "Same Day", "Next Day", "Reguler", "Kargo"};
        private static String[] shipmentItemDataPriceRanges = {"Rp 0", "Rp 40.00 - Rp 55.000", "Rp 20.000",
                "Rp 18.000", "Rp 9.000", "Rp 20.000"};
        private static String[] shipmentItemDataDeliveryTimeRanges = {null, "4 jam perjalanan", "6-8 jam perjalanan",
                "1-2 hari kerja", "2-7 hari kerja", "7-10 hari kerja"};


        private static String[] courierItemDataIds = {"0", "1", "2", "3", "4", "5"};
        private static String[] courierItemDataNames = {"Shop own logistic", "Go-Send", "JNE Reguler", "J&T Reguler",
                "Pos Indonesia Kilat Khusus", "Wahana"};
        private static String[] courierItemDataPrices = {"Rp 0", "Rp 10.000", "Rp 9.000", "Rp 8.000",
                "Rp 7.000", "Rp 6.000"};
        private static String[] courierItemDataDeliveryTimeRanges = {null, "4-6 jam*", "Max 8 jam*",
                "1-2 hari kerja*", "2-3 hari kerja*", "3-7 hari kerja*"};
        private static String[] courierItemDataDeliverySchedules = {null, "Jadwal pengiriman 09:00-16:00 WIB",
                null, null, null, null};
        private static String[] courierItemDataInsurancePrices = {null, "Rp 2.000", "Rp 1.000", "Rp 2.000",
                "Rp 1.000", "Rp 1.000"};
        private static String[] courierItemDataAdditionalPrices = {null, null, "Rp 5.000", "Rp 2.500", "Rp 0", "Rp 0"};
        private static String[] courierItemDataInfos = {
                "Kurir toko adalah layanan pengiriman dengan menggunakan kurir dari toko terkait",
                null, null, null, null, null};
        private static Integer[] courierItemDataInsuranceTypes = {1, 1, 2, 3, 1, 2};
        private static Integer[] courierItemDataInsuranceUsedDefaults = {1, 1, 2, 2, 1, 2};
        private static String[] courierItemDataInsuranceUsedInfos = {
                null, null,
                "Biaya ganti rugi senilai harga barang hingga Rp50.000.000, dengan biaya sebesar 0.2% dari harga barang",
                "Biaya ganti rugi senilai harga barang hingga Rp50.000.000, dengan biaya sebesar 0.2% dari harga barang",
                null,
                "Wahana menerapkan asuransi otomatis apabila harga barang lebih besar atau sama dengan Rp300.000 dengan biaya 0.5% dari total harga barang"};
        private static Integer[] courierItemDataInsuranceUsedTypes = {1, 2, 2, 2, 2, 1};

        static List<ShipmentItemData> createDummyShipmentChoices() {
            List<ShipmentItemData> shipments = new ArrayList<>();

            for (int i = 0; i < shipmentItemDataIds.length; i++) {
                ShipmentItemData shipmentItemData = new ShipmentItemData();
                shipmentItemData.setId(shipmentItemDataIds[i]);
                shipmentItemData.setType(shipmentItemDataTypes[i]);
                shipmentItemData.setPriceRange(shipmentItemDataPriceRanges[i]);
                shipmentItemData.setDeliveryTimeRange(shipmentItemDataDeliveryTimeRanges[i]);
                shipmentItemData.setCourierItemData(createDummyCourierChoices());

                shipments.add(shipmentItemData);
            }

            return shipments;
        }

        static List<CourierItemData> createDummyCourierChoices() {
            List<CourierItemData> couriers = new ArrayList<>();

            for (int i = 0; i < courierItemDataIds.length; i++) {
                CourierItemData courierItemData = new CourierItemData();
                courierItemData.setId(courierItemDataIds[i]);
                courierItemData.setSelected(false);
                courierItemData.setName(courierItemDataNames[i]);
                courierItemData.setDeliveryPrice(courierItemDataPrices[i]);
                courierItemData.setEstimatedTimeDelivery(courierItemDataDeliveryTimeRanges[i]);
                courierItemData.setDeliverySchedule(courierItemDataDeliverySchedules[i]);
                courierItemData.setAdditionalPrice(courierItemDataAdditionalPrices[i]);
                courierItemData.setCourierInfo(courierItemDataInfos[i]);
                courierItemData.setInsurancePrice(courierItemDataInsurancePrices[i]);
                courierItemData.setInsuranceType(courierItemDataInsuranceTypes[i]);
                courierItemData.setInsuranceUsedDefault(courierItemDataInsuranceUsedDefaults[i]);
                courierItemData.setInsuranceUsedInfo(courierItemDataInsuranceUsedInfos[i]);
                courierItemData.setInsuranceUsedType(courierItemDataInsuranceUsedTypes[i]);

                couriers.add(courierItemData);
            }

            return couriers;
        }

    }

}
