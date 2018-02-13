package com.tokopedia.transaction.checkout.view.data.factory;

import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 31/01/18
 */
public class ShipmentRecipientModelFactory {

    public static List<ShipmentRecipientModel> getDummyShipmentRecipientModelList() {
        List<ShipmentRecipientModel> shipmentRecipientModelList = new ArrayList<>();
        shipmentRecipientModelList.add(createDummyShipmentRecipientModel("0",
                true,
                "Headquarter",
                "Sherlock Holmes",
                "No. 221B, Baker Street, London",
                "0999 8800 1234"));

        shipmentRecipientModelList.add(createDummyShipmentRecipientModel("1",
                false,
                "Alamat Sarang",
                "Burung Hantu Tokped",
                "Jl. Letjen S. Parman Kav.77, Wisma 77 Tower 2,\nTokopedia Lt. 2, Jakarta",
                "0817 1234 5678"));

        shipmentRecipientModelList.add(createDummyShipmentRecipientModel("2",
                false,
                "Alamat Rumah Mertua",
                "Agus Maulana",
                "Jl. Letjen S. Parman Kav.77, Wisma 77 Tower 2,\nTokopedia Lt. 2, Jakarta",
                "0817 1234 5678"));

        return shipmentRecipientModelList;
    }

    public static ShipmentRecipientModel getDummyShipmentRecipientModel() {
        return createDummyShipmentRecipientModel("3",
                true,
                "Alamat Rumah",
                "Agus Maulana",
                "Jl. Letjen S. Parman Kav.77, Wisma 77 Tower 2,\nTokopedia Lt. 2, Jakarta",
                "0817 1777 2777");
    }

    private static ShipmentRecipientModel createDummyShipmentRecipientModel(String id,
                                                                            boolean isPrimerAddress,
                                                                            String addressDescription,
                                                                            String recipientName,
                                                                            String recipientAddress,
                                                                            String recipientPhone) {

        ShipmentRecipientModel shipmentRecipientModel = new ShipmentRecipientModel();
        shipmentRecipientModel.setId(id);
        shipmentRecipientModel.setPrimerAddress(isPrimerAddress);
        shipmentRecipientModel.setAddressIdentifier("Utama");
        shipmentRecipientModel.setRecipientName(recipientName);
        shipmentRecipientModel.setRecipientAddress(recipientAddress);
        shipmentRecipientModel.setRecipientAddressDescription(addressDescription);
        shipmentRecipientModel.setRecipientPhoneNumber(recipientPhone);
        shipmentRecipientModel.setDestinationDistrictId("2283");
        shipmentRecipientModel.setDestinationDistrictName("Kelapa Gading");
        shipmentRecipientModel.setTokenPickup("Tokopedia%2BKero:juMixO/k%2ButV%2BcQ4pVNm3FSG1pw%3D");
        shipmentRecipientModel.setUnixTime("1515753331");

        return shipmentRecipientModel;
    }

}
