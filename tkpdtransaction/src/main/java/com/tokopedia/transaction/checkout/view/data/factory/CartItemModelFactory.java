package com.tokopedia.transaction.checkout.view.data.factory;

import com.tokopedia.transaction.checkout.view.data.CartItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 31/01/18
 */

public class CartItemModelFactory {

    public static List<CartItemModel> getDummyCartItemModelList() {
        List<CartItemModel> cartItemModels = new ArrayList<>();

        cartItemModels.add(createDummyCartItemModel("Kaos Adidas Camo Tongue Tee... White & Red, XS",
                "Rp200.000",
                "3kg",
                "5%",
                1,
                "Saya pesan warna merah yah min.. jangan sampai salah kirim barangnya gan!",
                "https://s1.bukalapak.com/img/1856437411/s-194-194/original_Kaos_Adidas_.jpg",
                true,
                true));

        cartItemModels.add(createDummyCartItemModel("Kaos Partai",
                "Rp200.000",
                "3kg",
                "5%",
                1,
                "Minta nomor hape modelnya dong",
                "https://s1.bukalapak.com/img/1856437411/s-194-194/original_Kaos_Adidas_.jpg",
                true,
                true));

        cartItemModels.add(createDummyCartItemModel("Kaos KW Original",
                "Rp200.000",
                "3kg",
                "5%",
                1,
                "Saya pesen kaosnya yang KW ya, tapi original!",
                "https://s1.bukalapak.com/img/1856437411/s-194-194/original_Kaos_Adidas_.jpg",
                true,
                true));

        return cartItemModels;
    }

    public static CartItemModel createDummyCartItemModel(String productName,
                                                  String productPrice,
                                                  String productWeight,
                                                  String cashback,
                                                  int totalProductItem,
                                                  String noteToSeller,
                                                  String productImageUrl,
                                                  boolean poAvailable,
                                                  boolean freeReturn) {

        CartItemModel cartItemModel = new CartItemModel();
        cartItemModel.setProductName(productName);
        cartItemModel.setProductPriceFormatted(productPrice);
        cartItemModel.setProductWeightFormatted(productWeight);
        cartItemModel.setCashback(cashback);
        cartItemModel.setTotalProductItem(totalProductItem);
        cartItemModel.setNoteToSeller(noteToSeller);
        cartItemModel.setProductImageUrl(productImageUrl);
        cartItemModel.setPoAvailable(poAvailable);
        cartItemModel.setFreeReturn(freeReturn);

        return cartItemModel;
    }
}
