package com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel;


import com.google.gson.Gson;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactory;
import com.tokopedia.topads.sdk.domain.model.Data;

import java.util.ArrayList;
import java.util.List;

public class TopAdsViewModel implements Visitable<ProductListTypeFactory> {


    private List<Data> dataList = new ArrayList<>();

    public TopAdsViewModel() {
        Data data = new Gson().fromJson(getDummyData(), Data.class);
        for (int i = 0; i < 2; i++) {
            dataList.add(data);
        }
    }

    public TopAdsViewModel(List<Data> dataList) {
        this.dataList = dataList;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


    private String getDummyData(){
        return "{\n" +
                "\t\t\"id\": \"17311939\",\n" +
                "\t\t\"ad_ref_key\": \"\",\n" +
                "\t\t\"redirect\": \"https://www.tokopedia.com/mrmaidstore\",\n" +
                "\t\t\"sticker_id\": \"0\",\n" +
                "\t\t\"sticker_image\": \"\",\n" +
                "\t\t\"product_click_url\": \"https://ta.tokopedia.com/promo/v1/clicks/HAthopHhH_jp6_th6sJEH_1pHp17oArhHAtp6AHp6snEHmdFoAKf6_yEg9BGqMzUZMggQj2fgAo6QJBkQfBoe7BpZ3O6HcoD692qu7gN3_-Sq1Y2Z9P-q9P2y_-3o3ea69BqzsBE3_UN8u2_Z_g-qjV2_JoGP3Uao32q17jfZ32zHOBkZMW68BJO_VP3Q1N939z6zVBR_Sjhqu21e_oHuJOE3926Qu21uMOoz7Od_Bo-r7BW69BxufzFyMFNPfoW63Wju7dF3A-Dq7BkQfBoe7BpZ37N83V9gICiQABEy1rNPOKaQcW-qMY2_1o-r7BXzsVq3JtO3AoZqVtp_3Bvq1B2_JoG8Bja69BqusB2yf7NHfHau3Bvq1BR_c2CHJYJ3_u6uVuN_M2sHjYJu9B68jBEZ3BRq3Ha_SgsQugMyp-3qcoW_MY-qMY2_1H7P7NJgp-vuV1h_32gH7NkgpVoqMoN_9BG6e?page=1\\u0026number_ads_req=2\\u0026post_alg=cpc_shop_unq\\u0026n_candidate_ads=2083631\\u0026alg=def\\u0026src=hotlist\\u0026ab_test=N\\u0026t=android\\u0026sid=d1iEB2rwNwc%3AAPA91bEHvJYaUK_G8swJ9qNncAzZN8UkpnWvwmJtwGT7tg07BTU-0czaa1x-mzqngDJu0GgreJAioaC9OHUCkbHyDfzZYPBdrTghRodlEF9NMneoAY-OuPhp4f7SKgCBsmtBDNZ5xz2ZGZzdim9e1if_LyKertXMDA\\u0026uid=0\\u0026r=https%3A%2F%2Fwww.tokopedia.com%2Fmrmaidstore%2Fantivirus-kaspersky-total-security-3-device-2-tahun%3Fsrc%3Dtopads\\u0026number_of_ads=2\\u0026hotlist_id=1758\",\n" +
                "\t\t\"shop_click_url\": \"https://ta.tokopedia.com/promo/v1/clicks/HAthopHhH_jp6_th6sJEH_1pHp17oArhHAtp6AHp6snEHmdFoAKf6_yEg9BGqMzUZMggQj2fgAo6QJBkQfBoe7BpZ3O6HcoD692qu7gN3_-Sq1Y2Z9P-q9P2y_-3o3ea69BqzsBE3_UN8u2_Z_g-qjV2_JoGP3Uao32q17jfZ32zHOBkZMW68BJO_VP3Q1N939z6zVBR_Sjhqu21e_oHuJOE3926Qu21uMOoz7Od_Bo-r7BW69BxufzFyMFNPfoW63Wju7dF3A-Dq7BkQfBoe7BpZ37N83V9gICiQABEy1rNPOKaQcW-qMY2_1o-r7BXzsVq3JtO3AoZqVtp_3Bvq1B2_JoG8Bja69BqusB2yf7NHfHau3Bvq1BR_c2CHJYJ3_u6uVuN_M2sHjYJu9B68jBEZ3BRq3Ha_SgsQugMyp-3qcoW_MY-qMY2_1H7P7NJgp-vuV1h_32gH7NkgpVoqMoN_9BG6e?src=hotlist\\u0026alg=def\\u0026number_of_ads=2\\u0026hotlist_id=1758\\u0026sid=d1iEB2rwNwc%3AAPA91bEHvJYaUK_G8swJ9qNncAzZN8UkpnWvwmJtwGT7tg07BTU-0czaa1x-mzqngDJu0GgreJAioaC9OHUCkbHyDfzZYPBdrTghRodlEF9NMneoAY-OuPhp4f7SKgCBsmtBDNZ5xz2ZGZzdim9e1if_LyKertXMDA\\u0026number_ads_req=2\\u0026uid=0\\u0026r=https%3A%2F%2Fwww.tokopedia.com%2Fmrmaidstore\\u0026ab_test=N\\u0026page=1\\u0026t=android\\u0026post_alg=cpc_shop_unq\\u0026n_candidate_ads=2083631\",\n" +
                "\t\t\"product\": {\n" +
                "\t\t\t\"id\": \"280600171\",\n" +
                "\t\t\t\"name\": \"Antivirus Kaspersky Total Security 3 Device 2 Tahun\",\n" +
                "\t\t\t\"image\": {\n" +
                "\t\t\t\t\"m_url\": \"https://ta.tokopedia.com/promo/v1/views/H_tF6sJRHpJh6_HN6sJEH_tho_Hpo_e7opJF6MusrprXPcY0QRCBgcBxbMNBPmY2Q3r5yfVsqc15HpnFbRCaQfzOyReWHZ4aHAJdbpr5HsK5H_Jaopnp6Ar5H_Jaopnp6APwH_PMHAKa6_HWysKRoa776AUab3JdHpKWg_rNHcHRgMHNg_1R9pUFoAxwH_jfoiNFQMrE6snEHmdFoAKf6_yEg9BGqMzUZMggQj2fgAo6QJBkQfBoe7BpZ3O6HcoD692qu7gN3_-Sq1Y2Z9P-q9P2y_-3o3ea69BqzsBE3_UN8u2_Z_g-qjV2_JoGP3Uao32q17jfZ32zHOBkZMW68BJO_VP3Q1N939z6zVBR_Sjhqu21e_oHuJOE3926Qu21uMOoz7Od_Bo-r7BW69BxufzFyMFNPfoW63Wju7dF3A-Dq7BkQfBoe7BpZ37N83V9gICiQABEy1rNPOKaQcW-qMY2_1o-r7BXzsVq3JtO3AoZqVtp_3Bvq1B2_JoG8Bja69BqusB2yf7NHfHau3Bvq1BR_c2CHJYJ3_u6uVuN_M2sHjYJu9B68jBEZ3BRq3Ha_SgsQugMyp-3qcoW_MY-qMY2_1H7P7NJgp-vuV1h_32gH7NkgpVoqMoN_9BG6e?t=android\\u0026ab_test=N\\u0026number_of_ads=2\\u0026uid=0\\u0026n_candidate_ads=2083631\\u0026number_ads_req=2\\u0026sid=d1iEB2rwNwc%3AAPA91bEHvJYaUK_G8swJ9qNncAzZN8UkpnWvwmJtwGT7tg07BTU-0czaa1x-mzqngDJu0GgreJAioaC9OHUCkbHyDfzZYPBdrTghRodlEF9NMneoAY-OuPhp4f7SKgCBsmtBDNZ5xz2ZGZzdim9e1if_LyKertXMDA\\u0026post_alg=cpc_shop_unq\\u0026hotlist_id=1758\\u0026src=hotlist\\u0026alg=def\\u0026page=1\",\n" +
                "\t\t\t\t\"s_url\": \"https://ta.tokopedia.com/promo/v1/views/H_tF6sJRHpJh6_HN6sJEH_tho_Hpo_e7opJF6MusrprXPcY0QRCBgcBxbMNBPmY2Q3r5yfVsqc15HsnFb9ohP3VagZYFrMYjP3o7b_J5Hsnh6m4RbpUdbpJhHsrFHpKRbpJhHsrFHpKR9pJRgsndHsjpb3UdoprWoAKaHiOx6AHdb31R6_Csofgs631OoO4aHAed9pJNosyXrcNS6stF6snXHAedosjf6MuNZM2jZJ2M33NGPMep_Mh-qMY2_1o-r7BW_sCsQABE3BPc8ujagfBvq1BRZ3BRq3JausujHsBN3jyN8Bja69Bq17jfZ32Cq1hAZSuiHsuk3Bo-ojBk1_ogqj20_S2zo1h9uMO6uOB7_jzgP7NNH3BquJJp_Vzo8BBE_MOquVgW_1Po8JN_Z9o-Q_BNyuPjrc-D69PsQ_B0gVP6HVKaQcW-qMY2_1o-r7BW69BxufzFyMFN8MVI69PyHMh0Z325q1OAZ9o-Qjyh3BxGouKp1MxqH7O2_fB-q1hAZS2gHsBN3ByNq3oW6_osHBu2_fB-P7hke_CvzVjO_Bzu81Nkyp-vzVuN_S2-8jB2PfBsHjNfyfO3gMHauMxsQ1N5Z325q1OAoIP6zcra_OzuH1Ok3_o6qMrh_32s81ONZsj?t=android\\u0026ab_test=N\\u0026number_of_ads=2\\u0026uid=0\\u0026n_candidate_ads=2083631\\u0026number_ads_req=2\\u0026sid=d1iEB2rwNwc%3AAPA91bEHvJYaUK_G8swJ9qNncAzZN8UkpnWvwmJtwGT7tg07BTU-0czaa1x-mzqngDJu0GgreJAioaC9OHUCkbHyDfzZYPBdrTghRodlEF9NMneoAY-OuPhp4f7SKgCBsmtBDNZ5xz2ZGZzdim9e1if_LyKertXMDA\\u0026post_alg=cpc_shop_unq\\u0026hotlist_id=1758\\u0026src=hotlist\\u0026alg=def\\u0026page=1\",\n" +
                "\t\t\t\t\"xs_url\": \"https://ta.tokopedia.com/promo/v1/views/H_tF6sJRHpJh6_HN6sJEH_tho_Hpo_e7opJF6MusrprXPcY0QRCBgcBxbMNBPmY2Q3r5yfVsqc15H_nFb9ohP3VagZYFrMYjP3o7b_J5Hsnh6m4RbpUdbpJhHsrFHpKRbpJhHsrFHpKR9pJRgsndHsjpb3UdoprWoAKaHiOx6AHdb31R6_Csofgs631OoO4aHAed9pJNosyXrcNS6stF6snXHAedosjf6MuNZM2jZJ2M33NGPMep_Mh-qMY2_1o-r7BW_sCsQABE3BPc8ujagfBvq1BRZ3BRq3JausujHsBN3jyN8Bja69Bq17jfZ32Cq1hAZSuiHsuk3Bo-ojBk1_ogqj20_S2zo1h9uMO6uOB7_jzgP7NNH3BquJJp_Vzo8BBE_MOquVgW_1Po8JN_Z9o-Q_BNyuPjrc-D69PsQ_B0gVP6HVKaQcW-qMY2_1o-r7BW69BxufzFyMFN8MVI69PyHMh0Z325q1OAZ9o-Qjyh3BxGouKp1MxqH7O2_fB-q1hAZS2gHsBN3ByNq3oW6_osHBu2_fB-P7hke_CvzVjO_Bzu81Nkyp-vzVuN_S2-8jB2PfBsHjNfyfO3gMHauMxsQ1N5Z325q1OAoIP6zcra_OzuH1Ok3_o6qMrh_32s81ONZsj?t=android\\u0026ab_test=N\\u0026number_of_ads=2\\u0026uid=0\\u0026n_candidate_ads=2083631\\u0026number_ads_req=2\\u0026sid=d1iEB2rwNwc%3AAPA91bEHvJYaUK_G8swJ9qNncAzZN8UkpnWvwmJtwGT7tg07BTU-0czaa1x-mzqngDJu0GgreJAioaC9OHUCkbHyDfzZYPBdrTghRodlEF9NMneoAY-OuPhp4f7SKgCBsmtBDNZ5xz2ZGZzdim9e1if_LyKertXMDA\\u0026post_alg=cpc_shop_unq\\u0026hotlist_id=1758\\u0026src=hotlist\\u0026alg=def\\u0026page=1\",\n" +
                "\t\t\t\t\"m_ecs\": \"https://ecs7.tokopedia.net/img/cache/300/product-1/2018/7/28/11270387/11270387_17f08293-b877-4822-a838-e790c7fc9e57_2048_1966.png\",\n" +
                "\t\t\t\t\"s_ecs\": \"https://ecs7.tokopedia.net/img/cache/200-square/product-1/2018/7/28/11270387/11270387_17f08293-b877-4822-a838-e790c7fc9e57_2048_1966.png\",\n" +
                "\t\t\t\t\"xs_ecs\": \"https://ecs7.tokopedia.net/img/cache/100-square/product-1/2018/7/28/11270387/11270387_17f08293-b877-4822-a838-e790c7fc9e57_2048_1966.png\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"uri\": \"https://www.tokopedia.com/mrmaidstore/antivirus-kaspersky-total-security-3-device-2-tahun?src=topads\",\n" +
                "\t\t\t\"relative_uri\": \"mrmaidstore/antivirus-kaspersky-total-security-3-device-2-tahun\",\n" +
                "\t\t\t\"price_format\": \"Rp 245.000\",\n" +
                "\t\t\t\"wholesale_price\": [],\n" +
                "\t\t\t\"count_talk_format\": \"8\",\n" +
                "\t\t\t\"count_review_format\": \"11\",\n" +
                "\t\t\t\"category\": {\n" +
                "\t\t\t\t\"id\": \"20\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"product_preorder\": false,\n" +
                "\t\t\t\"product_wholesale\": false,\n" +
                "\t\t\t\"free_return\": \"https://ecs7.tokopedia.net/img/blank.gif\",\n" +
                "\t\t\t\"product_cashback\": true,\n" +
                "\t\t\t\"product_new_label\": false,\n" +
                "\t\t\t\"product_cashback_rate\": \"5\",\n" +
                "\t\t\t\"product_rating\": 98,\n" +
                "\t\t\t\"labels\": [{\n" +
                "\t\t\t\t\"title\": \"Cashback 5%\",\n" +
                "\t\t\t\t\"color\": \"#42b549\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"top_label\": [],\n" +
                "\t\t\t\"bottom_label\": [\"Cashback 5%\"]\n" +
                "\t\t},\n" +
                "\t\t\"shop\": {\n" +
                "\t\t\t\"id\": \"1338743\",\n" +
                "\t\t\t\"name\": \"MrMaid Store\",\n" +
                "\t\t\t\"domain\": \"mrmaidstore\",\n" +
                "\t\t\t\"location\": \"Jakarta\",\n" +
                "\t\t\t\"city\": \"Jakarta\",\n" +
                "\t\t\t\"gold_shop\": true,\n" +
                "\t\t\t\"gold_shop_badge\": true,\n" +
                "\t\t\t\"lucky_shop\": \"https://clover.tokopedia.com/badges/merchant/v1?shop_id=1338743\",\n" +
                "\t\t\t\"uri\": \"https://www.tokopedia.com/mrmaidstore\",\n" +
                "\t\t\t\"owner_id\": \"11270387\",\n" +
                "\t\t\t\"is_owner\": false,\n" +
                "\t\t\t\"badges\": [{\n" +
                "\t\t\t\t\"title\": \"Free Return\",\n" +
                "\t\t\t\t\"image_url\": \"https://ecs7.tokopedia.net/img/blank.gif\",\n" +
                "\t\t\t\t\"show\": false\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"title\": \"Gold Merchant\",\n" +
                "\t\t\t\t\"image_url\": \"https://ecs7.tokopedia.net/img/gold-active-large.png\",\n" +
                "\t\t\t\t\"show\": true\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t\"applinks\": \"tokopedia://product/280600171\"\n" +
                "\t}";
    }
}
