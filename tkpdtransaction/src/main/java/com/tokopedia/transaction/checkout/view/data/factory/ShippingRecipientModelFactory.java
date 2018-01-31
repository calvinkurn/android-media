package com.tokopedia.transaction.checkout.view.data.factory;

import com.tokopedia.transaction.checkout.view.data.ShippingRecipientModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 31/01/18
 */
public class ShippingRecipientModelFactory {

    public static List<ShippingRecipientModel> getDummyShippingRecipientModelList() {
        List<ShippingRecipientModel> shippingRecipientModelList = new ArrayList<>();

        shippingRecipientModelList.add(createDummyShippingRecipientModel("Agus Maulana",
                "Jl. Letjen S. Parman Kav.77, Wisma 77 Tower 2,\nTokopedia Lt. 2, Jakarta, 0817 1234 5678"));
        shippingRecipientModelList.add(createDummyShippingRecipientModel("Pooh Panda",
                "Jl. Letjen S. Parman Kav.77, Wisma 77 Tower 2,\nTokopedia Lt. 2, Jakarta, 0817 1234 5678"));

        return shippingRecipientModelList;
    }

    public static ShippingRecipientModel getDummyShippingRecipientModel() {
        return createDummyShippingRecipientModel("Agus Maulana",
                "Jl. Letjen S. Parman Kav.77, Wisma 77 Tower 2,\nTokopedia Lt. 2, Jakarta, 0817 1234 5678");
    }

    private static ShippingRecipientModel createDummyShippingRecipientModel(String recipientName,
                                                                       String recipientAddress) {

        ShippingRecipientModel shippingRecipientModel = new ShippingRecipientModel();
        shippingRecipientModel.setRecipientName(recipientName);
        shippingRecipientModel.setRecipientAddress(recipientAddress);

        return shippingRecipientModel;
    }

}
