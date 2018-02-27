package com.tokopedia.transaction.checkout.domain;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.domain.response.addtocart.AddToCartDataResponse;
import com.tokopedia.transaction.checkout.domain.response.cartlist.CartDataListResponse;
import com.tokopedia.transaction.checkout.domain.response.checkout.CheckoutDataResponse;
import com.tokopedia.transaction.checkout.domain.response.checkpromocodecartlist.CheckPromoCodeCartListDataResponse;
import com.tokopedia.transaction.checkout.domain.response.checkpromocodefinal.CheckPromoCodeFinalDataResponse;
import com.tokopedia.transaction.checkout.domain.response.couponlist.CouponDataResponse;
import com.tokopedia.transaction.checkout.domain.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transaction.checkout.domain.response.resetcart.ResetCartDataResponse;
import com.tokopedia.transaction.checkout.domain.response.shippingaddress.ShippingAddressDataResponse;
import com.tokopedia.transaction.checkout.domain.response.shippingaddressform.ShipmentAddressFormDataResponse;
import com.tokopedia.transaction.checkout.domain.response.updatecart.UpdateCartDataResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 07/02/18.
 */

public class CartRepositoryDataDummy implements ICartRepository {

    @Inject
    public CartRepositoryDataDummy() {
    }

    @Override
    public Observable<CartDataListResponse> getCartList(TKPDMapParam<String, String> param) {
        return Observable.just(param)
                .map(new Func1<TKPDMapParam<String, String>, CartDataListResponse>() {
                    @Override
                    public CartDataListResponse call(TKPDMapParam<String, String> tkpdMapParam) {
                        return generateDummyDataCartList();
                    }
                });
    }

    @Override
    public Observable<DeleteCartDataResponse> deleteCartData(TKPDMapParam<String, String> param) {
        return Observable.just(param).map(new Func1<TKPDMapParam<String, String>, DeleteCartDataResponse>() {
            @Override
            public DeleteCartDataResponse call(TKPDMapParam<String, String> tkpdMapParam) {
                return generateDummyDeleteCart();
            }
        });
    }

    @Override
    public Observable<AddToCartDataResponse> addToCartData(TKPDMapParam<String, String> param) {
        return null;
    }

    @Override
    public Observable<UpdateCartDataResponse> updateCartData(TKPDMapParam<String, String> param) {
        return null;
    }

    @Override
    public Observable<ShippingAddressDataResponse> shippingAddress(TKPDMapParam<String, String> param) {
        return null;
    }

    @Override
    public Observable<ShipmentAddressFormDataResponse> getShipmentAddressForm(TKPDMapParam<String, String> param) {
        return null;
    }

    @Override
    public Observable<ResetCartDataResponse> resetCart(TKPDMapParam<String, String> param) {
        return null;
    }

    @Override
    public Observable<CheckoutDataResponse> checkout(TKPDMapParam<String, String> param) {
        return null;
    }

    @Override
    public Observable<CheckPromoCodeCartListDataResponse> checkPromoCodeCartList(TKPDMapParam<String, String> param) {
        return null;
    }

    @Override
    public Observable<CheckPromoCodeFinalDataResponse> checkPromoCodeCartShipment(TKPDMapParam<String, String> param) {
        return null;
    }

    @Override
    public Observable<CouponDataResponse> getCouponList(TKPDMapParam<String, String> param) {
        return null;
    }

    private DeleteCartDataResponse generateDummyDeleteCart() {
        String jsonRes = "{\n" +
                "        \"success\": 1,\n" +
                "        \"message\": null,\n" +
                "        \"data\": null\n" +
                "    }";

        return new Gson().fromJson(jsonRes, DeleteCartDataResponse.class);
    }

    private CartDataListResponse generateDummyDataCartList() {
        String jsonDummy = "{\n" +
                "  \"errors\": [],\n" +
                "  \"max_quantity\": 10000,\n" +
                "  \"max_char_note\": 144,\n" +
                "  \"messages\": {\n" +
                "    \"ErrorCheckoutPriceLimit\": \"Maksimum biaya yang dapat Anda checkout adalah {{value}}\",\n" +
                "    \"ErrorFieldBetween\": \"Jumlah barang harus diisi antara 1 - {{value}}\",\n" +
                "    \"ErrorFieldMaxChar\": \"Catatan terlalu panjang, maksimum {{value}} karakter.\",\n" +
                "    \"ErrorFieldRequired\": \"Jumlah barang harus diisi\",\n" +
                "    \"ErrorProductAvailableStock\": \"Stok tersedia:{{value}}\",\n" +
                "    \"ErrorProductAvailableStockDetail\": \"Harap kurangi jumlah barang\",\n" +
                "    \"ErrorProductMaxQuantity\": \"Maksimal pembelian produk ini adalah {{value}} item\",\n" +
                "    \"ErrorProductMinQuantity\": \"Minimal pembelian produk ini adalah {{value}} item\"\n" +
                "  },\n" +
                "  \"promo_suggestion\": {\n" +
                "    \"cta\": \"Gunakan Sekarang!\",\n" +
                "    \"cta_color\": \"#42b549\",\n" +
                "    \"is_visible\": 1,\n" +
                "    \"promo_code\": \"TOKOCASH\",\n" +
                "    \"text\": \"[iOS] Cashback hingga 25% menggunakan Promo <b>TOKOCASH</b> !\"\n" +
                "  },\n" +
                "  \"cart_list\": [\n" +
                "    {\n" +
                "      \"cart_id\": 35303,\n" +
                "      \"user_address_id\": 0,\n" +
                "      \"shop\": {\n" +
                "        \"shop_id\": 349207,\n" +
                "        \"user_id\": 2684597,\n" +
                "        \"shop_name\": \"Jarjit\",\n" +
                "        \"shop_image\": \"https://imagerouter-staging.tokopedia.com/img/215-square/shops-1/2017/4/6/349207/349207_f4e61676-56da-486d-bc0b-7da82280a36d.png\",\n" +
                "        \"shop_url\": \"https://staging.tokopedia.com/qc39\",\n" +
                "        \"shop_status\": 1,\n" +
                "        \"is_gold\": 0,\n" +
                "        \"is_gold_badge\": false,\n" +
                "        \"is_official\": 0,\n" +
                "        \"is_free_returns\": 0,\n" +
                "        \"address_id\": 5573,\n" +
                "        \"postal_code\": \"11480\",\n" +
                "        \"latitude\": \"-6.190071399999999\",\n" +
                "        \"longitude\": \"106.79719\",\n" +
                "        \"district_id\": 5573,\n" +
                "        \"district_name\": \"Jakarta\",\n" +
                "        \"origin\": 5573,\n" +
                "        \"address_street\": \"\",\n" +
                "        \"province_id\": 13,\n" +
                "        \"city_id\": 174,\n" +
                "        \"city_name\": \"DKI Jakarta\"\n" +
                "      },\n" +
                "      \"product\": {\n" +
                "        \"product_id\": 15124139,\n" +
                "        \"product_name\": \"R15 Predator\",\n" +
                "        \"product_price_fmt\": \"Rp 250.000\",\n" +
                "        \"product_price\": 250000,\n" +
                "        \"category_id\": 77,\n" +
                "        \"category\": \"Handphone & Tablet > Aksesoris Handphone > Lainnya\",\n" +
                "        \"catalog_id\": 0,\n" +
                "        \"wholesale_price\": null,\n" +
                "        \"product_weight_fmt\": \"250 gr\",\n" +
                "        \"product_condition\": 1,\n" +
                "        \"product_status\": 1,\n" +
                "        \"product_url\": \"https://staging.tokopedia.com/qc39/r15-predator\",\n" +
                "        \"product_returnable\": -1,\n" +
                "        \"is_freereturns\": 0,\n" +
                "        \"is_preorder\": 0,\n" +
                "        \"product_cashback\": \"\",\n" +
                "        \"product_min_order\": 1,\n" +
                "        \"product_rating\": 0,\n" +
                "        \"product_invenage_value\": 0,\n" +
                "        \"product_switch_invenage\": 0,\n" +
                "        \"product_price_currency\": 1,\n" +
                "        \"product_image\": {\n" +
                "          \"image_src\": \"https://imagerouter-staging.tokopedia.com/img/700/product-1/2017/8/2/2684597/2684597_d48f04b9-8b55-422b-8455-1bb1057a4109_640_480.jpg\",\n" +
                "          \"image_src_200_square\": \"https://imagerouter-staging.tokopedia.com/img/200-square/product-1/2017/8/2/2684597/2684597_d48f04b9-8b55-422b-8455-1bb1057a4109_640_480.jpg\",\n" +
                "          \"image_src_300\": \"https://imagerouter-staging.tokopedia.com/img/300/product-1/2017/8/2/2684597/2684597_d48f04b9-8b55-422b-8455-1bb1057a4109_640_480.jpg\",\n" +
                "          \"image_src_square\": \"https://imagerouter-staging.tokopedia.com/img/500-square/product-1/2017/8/2/2684597/2684597_d48f04b9-8b55-422b-8455-1bb1057a4109_640_480.jpg\"\n" +
                "        },\n" +
                "        \"product_all_images\": null,\n" +
                "        \"product_notes\": \"tolong segera kirim ya\",\n" +
                "        \"product_quantity\": 3,\n" +
                "        \"product_weight\": 250,\n" +
                "        \"product_weight_unit_code\": 1,\n" +
                "        \"product_weight_unit_text\": \"gr\",\n" +
                "        \"last_update_price\": 1501684268,\n" +
                "        \"is_update_price\": false,\n" +
                "        \"product_preorder\": {},\n" +
                "        \"product_showcase\": {\n" +
                "          \"name\": \"insta 1\",\n" +
                "          \"id\": 1400228\n" +
                "        }\n" +
                "      },\n" +
                "      \"errors\": [],\n" +
                "      \"messages\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"cart_id\": 35343,\n" +
                "      \"user_address_id\": 0,\n" +
                "      \"shop\": {\n" +
                "        \"shop_id\": 349207,\n" +
                "        \"user_id\": 2684597,\n" +
                "        \"shop_name\": \"Jarjit\",\n" +
                "        \"shop_image\": \"https://imagerouter-staging.tokopedia.com/img/215-square/shops-1/2017/4/6/349207/349207_f4e61676-56da-486d-bc0b-7da82280a36d.png\",\n" +
                "        \"shop_url\": \"https://staging.tokopedia.com/qc39\",\n" +
                "        \"shop_status\": 1,\n" +
                "        \"is_gold\": 0,\n" +
                "        \"is_gold_badge\": false,\n" +
                "        \"is_official\": 0,\n" +
                "        \"is_free_returns\": 0,\n" +
                "        \"address_id\": 5573,\n" +
                "        \"postal_code\": \"11480\",\n" +
                "        \"latitude\": \"-6.190071399999999\",\n" +
                "        \"longitude\": \"106.79719\",\n" +
                "        \"district_id\": 5573,\n" +
                "        \"district_name\": \"Jakarta\",\n" +
                "        \"origin\": 5573,\n" +
                "        \"address_street\": \"\",\n" +
                "        \"province_id\": 13,\n" +
                "        \"city_id\": 174,\n" +
                "        \"city_name\": \"DKI Jakarta\"\n" +
                "      },\n" +
                "      \"product\": {\n" +
                "        \"product_id\": 14286531,\n" +
                "        \"product_name\": \"topi gault\",\n" +
                "        \"product_price_fmt\": \"Rp 11.111\",\n" +
                "        \"product_price\": 11111,\n" +
                "        \"category_id\": 1674,\n" +
                "        \"category\": \"Gelang Kaki\",\n" +
                "        \"catalog_id\": 0,\n" +
                "        \"wholesale_price\": null,\n" +
                "        \"product_weight_fmt\": \"11 gr\",\n" +
                "        \"product_condition\": 1,\n" +
                "        \"product_status\": 1,\n" +
                "        \"product_url\": \"https://staging.tokopedia.com/qc39/topi-gault\",\n" +
                "        \"product_returnable\": 0,\n" +
                "        \"is_freereturns\": 0,\n" +
                "        \"is_preorder\": 0,\n" +
                "        \"product_cashback\": \"\",\n" +
                "        \"product_min_order\": 1,\n" +
                "        \"product_rating\": 0,\n" +
                "        \"product_invenage_value\": 0,\n" +
                "        \"product_switch_invenage\": 0,\n" +
                "        \"product_price_currency\": 1,\n" +
                "        \"product_image\": {\n" +
                "          \"image_src\": \"https://imagerouter-staging.tokopedia.com/img/700/product-1/2017/3/23/2684597/2684597_af261fce-6aba-4141-ab09-3ddc880997df_360_328.jpg\",\n" +
                "          \"image_src_200_square\": \"https://imagerouter-staging.tokopedia.com/img/200-square/product-1/2017/3/23/2684597/2684597_af261fce-6aba-4141-ab09-3ddc880997df_360_328.jpg\",\n" +
                "          \"image_src_300\": \"https://imagerouter-staging.tokopedia.com/img/300/product-1/2017/3/23/2684597/2684597_af261fce-6aba-4141-ab09-3ddc880997df_360_328.jpg\",\n" +
                "          \"image_src_square\": \"https://imagerouter-staging.tokopedia.com/img/500-square/product-1/2017/3/23/2684597/2684597_af261fce-6aba-4141-ab09-3ddc880997df_360_328.jpg\"\n" +
                "        },\n" +
                "        \"product_all_images\": null,\n" +
                "        \"product_notes\": \"\",\n" +
                "        \"product_quantity\": 15,\n" +
                "        \"product_weight\": 11,\n" +
                "        \"product_weight_unit_code\": 1,\n" +
                "        \"product_weight_unit_text\": \"gr\",\n" +
                "        \"last_update_price\": 1490290484,\n" +
                "        \"is_update_price\": false,\n" +
                "        \"product_preorder\": {},\n" +
                "        \"product_showcase\": {\n" +
                "          \"name\": \"otomotif\",\n" +
                "          \"id\": 988490\n" +
                "        }\n" +
                "      },\n" +
                "      \"errors\": [],\n" +
                "      \"messages\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"cart_id\": 35346,\n" +
                "      \"user_address_id\": 0,\n" +
                "      \"shop\": {\n" +
                "        \"shop_id\": 480125,\n" +
                "        \"user_id\": 5512646,\n" +
                "        \"shop_name\": \"tokoko\",\n" +
                "        \"shop_image\": \"https://imagerouter-staging.tokopedia.com/img/215-square/default_v3-shopnophoto.png\",\n" +
                "        \"shop_url\": \"https://staging.tokopedia.com/tokoko\",\n" +
                "        \"shop_status\": 1,\n" +
                "        \"is_gold\": 1,\n" +
                "        \"is_gold_badge\": false,\n" +
                "        \"is_official\": 0,\n" +
                "        \"is_free_returns\": 0,\n" +
                "        \"address_id\": 2258,\n" +
                "        \"postal_code\": \"11430\",\n" +
                "        \"latitude\": \"-6.1783437\",\n" +
                "        \"longitude\": \"106.80456400000003\",\n" +
                "        \"district_id\": 2258,\n" +
                "        \"district_name\": \"Palmerah\",\n" +
                "        \"origin\": 2258,\n" +
                "        \"address_street\": \"\",\n" +
                "        \"province_id\": 13,\n" +
                "        \"city_id\": 174,\n" +
                "        \"city_name\": \"DKI Jakarta\"\n" +
                "      },\n" +
                "      \"product\": {\n" +
                "        \"product_id\": 15139121,\n" +
                "        \"product_name\": \"keong\",\n" +
                "        \"product_price_fmt\": \"Rp 19.000\",\n" +
                "        \"product_price\": 19000,\n" +
                "        \"category_id\": 520,\n" +
                "        \"category\": \"Mainan & Hobi > Lainnya > Alat Berkebun & Hewan Peliharaan\",\n" +
                "        \"catalog_id\": 0,\n" +
                "        \"wholesale_price\": [\n" +
                "          {\n" +
                "            \"qty_min_fmt\": \"15\",\n" +
                "            \"qty_max_fmt\": \"20\",\n" +
                "            \"qty_min\": 15,\n" +
                "            \"qty_max\": 20,\n" +
                "            \"prd_prc\": 17000,\n" +
                "            \"prd_prc_fmt\": \"Rp 17.000\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"product_weight_fmt\": \"2.000 gr\",\n" +
                "        \"product_condition\": 1,\n" +
                "        \"product_status\": 1,\n" +
                "        \"product_url\": \"https://staging.tokopedia.com/tokoko/keong\",\n" +
                "        \"product_returnable\": 0,\n" +
                "        \"is_freereturns\": 0,\n" +
                "        \"is_preorder\": 0,\n" +
                "        \"product_cashback\": \"\",\n" +
                "        \"product_min_order\": 1,\n" +
                "        \"product_rating\": 100,\n" +
                "        \"product_invenage_value\": 0,\n" +
                "        \"product_switch_invenage\": 0,\n" +
                "        \"product_price_currency\": 1,\n" +
                "        \"product_image\": {\n" +
                "          \"image_src\": \"https://imagerouter-staging.tokopedia.com/img/700/product-1/2017/9/29/5512646/5512646_92f653fa-1cc0-4322-9c25-edaaff52ef4d_736_736.jpg\",\n" +
                "          \"image_src_200_square\": \"https://imagerouter-staging.tokopedia.com/img/200-square/product-1/2017/9/29/5512646/5512646_92f653fa-1cc0-4322-9c25-edaaff52ef4d_736_736.jpg\",\n" +
                "          \"image_src_300\": \"https://imagerouter-staging.tokopedia.com/img/300/product-1/2017/9/29/5512646/5512646_92f653fa-1cc0-4322-9c25-edaaff52ef4d_736_736.jpg\",\n" +
                "          \"image_src_square\": \"https://imagerouter-staging.tokopedia.com/img/500-square/product-1/2017/9/29/5512646/5512646_92f653fa-1cc0-4322-9c25-edaaff52ef4d_736_736.jpg\"\n" +
                "        },\n" +
                "        \"product_all_images\": null,\n" +
                "        \"product_notes\": \"tolong segera kirim ya\",\n" +
                "        \"product_quantity\": 16,\n" +
                "        \"product_weight\": 2000,\n" +
                "        \"product_weight_unit_code\": 1,\n" +
                "        \"product_weight_unit_text\": \"gr\",\n" +
                "        \"last_update_price\": 1508250112,\n" +
                "        \"is_update_price\": false,\n" +
                "        \"product_preorder\": {},\n" +
                "        \"product_showcase\": {\n" +
                "          \"name\": \"Dog toys\",\n" +
                "          \"id\": 1402968\n" +
                "        }\n" +
                "      },\n" +
                "      \"errors\": [],\n" +
                "      \"messages\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"cart_id\": 35349,\n" +
                "      \"user_address_id\": 0,\n" +
                "      \"shop\": {\n" +
                "        \"shop_id\": 480125,\n" +
                "        \"user_id\": 5512646,\n" +
                "        \"shop_name\": \"tokoko\",\n" +
                "        \"shop_image\": \"https://imagerouter-staging.tokopedia.com/img/215-square/default_v3-shopnophoto.png\",\n" +
                "        \"shop_url\": \"https://staging.tokopedia.com/tokoko\",\n" +
                "        \"shop_status\": 1,\n" +
                "        \"is_gold\": 1,\n" +
                "        \"is_gold_badge\": false,\n" +
                "        \"is_official\": 0,\n" +
                "        \"is_free_returns\": 0,\n" +
                "        \"address_id\": 2258,\n" +
                "        \"postal_code\": \"11430\",\n" +
                "        \"latitude\": \"-6.1783437\",\n" +
                "        \"longitude\": \"106.80456400000003\",\n" +
                "        \"district_id\": 2258,\n" +
                "        \"district_name\": \"Palmerah\",\n" +
                "        \"origin\": 2258,\n" +
                "        \"address_street\": \"\",\n" +
                "        \"province_id\": 13,\n" +
                "        \"city_id\": 174,\n" +
                "        \"city_name\": \"DKI Jakarta\"\n" +
                "      },\n" +
                "      \"product\": {\n" +
                "        \"product_id\": 15138786,\n" +
                "        \"product_name\": \"testpoready\",\n" +
                "        \"product_price_fmt\": \"Rp 80.000\",\n" +
                "        \"product_price\": 80000,\n" +
                "        \"category_id\": 1771,\n" +
                "        \"category\": \"Fashion Wanita > Atasan > Blouse\",\n" +
                "        \"catalog_id\": 0,\n" +
                "        \"wholesale_price\": null,\n" +
                "        \"product_weight_fmt\": \"200 gr\",\n" +
                "        \"product_condition\": 1,\n" +
                "        \"product_status\": 1,\n" +
                "        \"product_url\": \"https://staging.tokopedia.com/tokoko/testpoready\",\n" +
                "        \"product_returnable\": 0,\n" +
                "        \"is_freereturns\": 0,\n" +
                "        \"is_preorder\": 1,\n" +
                "        \"product_cashback\": \"\",\n" +
                "        \"product_min_order\": 1,\n" +
                "        \"product_rating\": 100,\n" +
                "        \"product_invenage_value\": 0,\n" +
                "        \"product_switch_invenage\": 0,\n" +
                "        \"product_price_currency\": 1,\n" +
                "        \"product_image\": {\n" +
                "          \"image_src\": \"https://imagerouter-staging.tokopedia.com/img/700/product-1/2017/9/27/5512646/5512646_e1ebed6b-ef9c-4119-bc67-7061e6043053_480_480.jpg\",\n" +
                "          \"image_src_200_square\": \"https://imagerouter-staging.tokopedia.com/img/200-square/product-1/2017/9/27/5512646/5512646_e1ebed6b-ef9c-4119-bc67-7061e6043053_480_480.jpg\",\n" +
                "          \"image_src_300\": \"https://imagerouter-staging.tokopedia.com/img/300/product-1/2017/9/27/5512646/5512646_e1ebed6b-ef9c-4119-bc67-7061e6043053_480_480.jpg\",\n" +
                "          \"image_src_square\": \"https://imagerouter-staging.tokopedia.com/img/500-square/product-1/2017/9/27/5512646/5512646_e1ebed6b-ef9c-4119-bc67-7061e6043053_480_480.jpg\"\n" +
                "        },\n" +
                "        \"product_all_images\": null,\n" +
                "        \"product_notes\": \"\",\n" +
                "        \"product_quantity\": 10,\n" +
                "        \"product_weight\": 200,\n" +
                "        \"product_weight_unit_code\": 1,\n" +
                "        \"product_weight_unit_text\": \"gr\",\n" +
                "        \"last_update_price\": 1506528067,\n" +
                "        \"is_update_price\": false,\n" +
                "        \"product_preorder\": {\n" +
                "          \"duration_text\": \"4 Day\",\n" +
                "          \"duration_day\": 4,\n" +
                "          \"duration_unit_code\": 1,\n" +
                "          \"duration_unit_text\": \"Day\",\n" +
                "          \"duration_value\": 4\n" +
                "        },\n" +
                "        \"product_showcase\": {\n" +
                "          \"name\": \"fashion\",\n" +
                "          \"id\": 1402871\n" +
                "        }\n" +
                "      },\n" +
                "      \"errors\": [],\n" +
                "      \"messages\": []\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        return new Gson().fromJson(jsonDummy, CartDataListResponse.class);
    }
}
