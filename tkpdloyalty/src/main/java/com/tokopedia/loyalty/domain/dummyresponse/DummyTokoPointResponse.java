package com.tokopedia.loyalty.domain.dummyresponse;

/**
 * @author anggaprasetiyo on 08/12/17.
 */

public interface DummyTokoPointResponse {
    String RESPONSE_DRAWER_DATA = "{\n" +
            "  \"off_flag\": 0,\n" +
            "  \"has_notif\": 0,\n" +
            "  \"mainpage_url\": \"https://gw-staging.tokopedia.com/tokopoints\",\n" +
            "  \"user_tier\": {\n" +
            "    \"tier_id\": 1,\n" +
            "    \"tier_name\": \"basic\",\n" +
            "    \"tier_image_url\": \"https://ecs7.tokopedia.net/img/tier_diamond.png\",\n" +
            "    \"reward_points\": 1000\n" +
            "  },\n" +
            "  \"pop_up_notif\": {\n" +
            "    \"title\": \"Selamat anda menjadi basic Member Tokopedia\",\n" +
            "    \"text\": \"Nikmati penambahan benefit yang anda dapat lihat di <a href='www.tokopedia.com/tokoplus'>TokoPlus</a>\",\n" +
            "    \"image_url\": \"https://ecs7.tokopedia.net/img/diamond.png\",\n" +
            "    \"button_text\": \"lanjutkan\",\n" +
            "    \"button_url\": \"https://www.tokopedia.com/hachiko/mainpage\",\n" +

            "    \"notes\": \"Happy wedding mas...\",\n" +
            "    \"catalog\": {\n" +
            "      \"title\": \"Free Ongkir\",\n" +
            "      \"sub_title\": \"Rp.20.000\",\n" +
            "      \"points\": 20000,\n" +
            "      \"thumbnail_url\": \"https://ecs7.tokopedia.net/img/thumb1.png\",\n" +
            "      \"thumbnail_url_mobile\": \"https://ecs7.tokopedia.net/img/thumb_mobile2.png\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    String RESPONSE_CATALOG_LIST = "{\n" +
            "  \"catalog\": [\n" +
            "    {\n" +
            "      \"id\": 2789,\n" +
            "      \"expired\": \"20 Okt 2017\",\n" +
            "      \"points\": 1000,\n" +
            "      \"title\": \"Free Ongkir\",\n" +
            "      \"sub_title\": \"Rp.20.000,-\",\n" +
            "      \"thumbnail_url\": \"https://ecs7.tokopedia.net/img/thumb1.png\",\n" +
            "      \"thumbnail_url_mobile\": \"https://ecs7.tokopedia.net/img/thumb_mobile2.png\",\n" +
            "      \"image_url\": \"https://ecs7.tokopedia.net/img/banner1.png\",\n" +
            "      \"image_url_mobile\": \"https://ecs7.tokopedia.net/img/mobile-banner1.png\",\n" +
            "      \"quota\": 12\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 2749,\n" +
            "      \"expired\": \"30 Okt 2017\",\n" +
            "      \"points\": 500,\n" +
            "      \"title\": \"Free Ongkir\",\n" +
            "      \"sub_title\": \"Rp.20.000,-\",\n" +
            "      \"thumbnail_url\": \"https://ecs7.tokopedia.net/img/thumb2.png\",\n" +
            "      \"thumbnail_url_mobile\": \"https://ecs7.tokopedia.net/img/thumb_mobile2.png\",\n" +
            "      \"image_url\": \"https://ecs7.tokopedia.net/img/banner2.png\",\n" +
            "      \"image_url_mobile\": \"https://ecs7.tokopedia.net/img/mobile-banner2.png\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    String RESPONSE_CATALOG_DETAIL = "{\n" +
            "  \"catalog\": [\n" +
            "    {\n" +
            "      \"id\": 2890,\n" +
            "      \"expired\": \"20 Okt 2017\",\n" +
            "      \"points\": 1000,\n" +
            "      \"title\": \"Gratis ongkir Lagi!!\",\n" +
            "      \"sub_title\": \"Hingga Rp.20.000,-\",\n" +
            "      \"image_url\": \"https://ecs7.tokopedia.net/img/banner1.png\",\n" +
            "      \"image_url_mobile\": \"https://ecs7.tokopedia.net/img/mobile-banner1.png\",\n" +
            "      \"overview\": \"Saatnya berkreasi tanpa batas dengan dihari sumpah pemuda\",\n" +
            "      \"how_to_use\": \"<ul><li>Promo berlaku selama dihari sabtu</li><li>Coupon hanya berlaku di official store</li></ul>\",\n" +
            "      \"tnc\": \"<ul><li>Coupon ini berlaku dengan minimum transaksi Rp 50.000.</li><li>Coupon berlaku untuk semua metode pembayaran yang tersedia di Tokopedia.</li></ul>\",\n" +
            "      \"quota\": 12,\n" +
            "      \"is_gift\": 1\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    String RESPONSE_CATALOG_FILTER = "{\n" +
            "  \"categories\": [\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"name\": \"Semua\",\n" +
            "      \"image_id\": \"semua\",\n" +
            "      \"image_url\": \"https://ecs7.tokopedia.net/img/semua.png\",\n" +
            "      \"index\": 1\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 1,\n" +
            "      \"name\": \"Jual Beli\",\n" +
            "      \"image_id\": \"jualbeli\",\n" +
            "      \"image_url\": \"https://ecs7.tokopedia.net/img/jualbeli.png\",\n" +
            "      \"index\": 2\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 3,\n" +
            "      \"name\": \"Digital\",\n" +
            "      \"image_id\": \"digital\",\n" +
            "      \"image_url\": \"https://ecs7.tokopedia.net/img/digital.png\",\n" +
            "      \"index\": 3\n" +
            "    }\n" +
            "  ],\n" +
            "  \"points_range\": [\n" +
            "    {\n" +
            "      \"id\": 1,\n" +
            "      \"text\": \"Sesuai point yang saya miliki\",\n" +
            "      \"index\": 1\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 2,\n" +
            "      \"text\": \"1 - 100 Point\",\n" +
            "      \"index\": 2\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 3,\n" +
            "      \"text\": \"101 - 200 Point\",\n" +
            "      \"index\": 3\n" +
            "    }\n" +
            "  ],\n" +
            "  \"sort_type\": [\n" +
            "    {\n" +
            "      \"id\": 1,\n" +
            "      \"text\": \"Semua\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 2,\n" +
            "      \"text\": \"Terbaru\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 3,\n" +
            "      \"text\": \"Trending\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    String RESPONSE_POINT_STATUS = "{\n" +
            "  \"tier_id\": 1,\n" +
            "  \"tier_name\": \"basic\",\n" +
            "  \"reward_points\": 1000\n" +
            "}";
    String RESPONSE_MAIN_PAGE = "{\n" +
            "  \"collection\": {\n" +
            "    \"tier\": {\n" +
            "      \"id\": 1,\n" +
            "      \"name\": \"Basic\",\n" +
            "      \"image_url\": \"https://ecs7.tokopedia.net/img/tier_diamond.png\"\n" +
            "    },\n" +
            "    \"total_member_points\": 350,\n" +
            "    \"reward_points\": 500000,\n" +
            "    \"next_member_points\": 5000,\n" +
            "    \"safe_member_points\": 500\n" +
            "  },\n" +
            "  \"detail_info\": {\n" +
            "    \"expiry\": \"400 Poin Kadaluarsa di 23 Jan 2018\",\n" +
            "    \"cut_off\": \"Silver member Anda akan diperbaharui pada 1 Januari 2019\",\n" +
            "    \"stay_amount\": 150,\n" +
            "    \"stay\": \"Tetap jadi Silver member di tahun 2019\",\n" +
            "    \"level_up_amount\": 4150,\n" +
            "    \"level_up\": \"Upgrade ke Gold Member\"\n" +
            "  },\n" +
            "  \"tiers\": [\n" +
            "    {\n" +
            "      \"img_thumb_url\": \"\",\n" +
            "      \"img_url\": \"\",\n" +
            "      \"content\": \"Classic Member Benefit <br><ul><li>Gratis ongkir 5x dalam 1 tahun</li><li>Poin & egg dikali 1.1</li></ul>\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    String RESPONSE_POINT_HISTORY = "{\n" +
            "  \"point_histories\": [\n" +
            "    {\n" +
            "      \"create_time\": \"05-04-2017 10:69:00\",\n" +
            "      \"create_time_desc\": \"Earned 5 Aprl 2017\",\n" +
            "      \"title\": \"Cashback dari Marketplace\",\n" +
            "      \"notes\": \"No. Invoice INV/4536456/45736356\",\n" +
            "      \"member_points\": 500,\n" +
            "      \"reward_points\": 200,\n" +
            "      \"history_type\": 1\n" +
            "    },\n" +
            "    {\n" +
            "      \"create_time\": \"05-04-2017 10:69:00\",\n" +
            "      \"create_time_desc\": \"Earned 5 Aprl 2017\",\n" +
            "      \"title\": \"Pembelian Marketplace\",\n" +
            "      \"notes\": \"No. Invoice INV/4536456/45736356\",\n" +
            "      \"member_points\": 0,\n" +
            "      \"reward_points\": -100,\n" +
            "      \"history_type\": 2\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    String RESPONSE_COUPON_REDEEM = "{\n" +
            "  \"coupons\": [\n" +
            "    {\n" +
            "      \"id\": 2890,\n" +
            "      \"owner\": 3265112,\n" +
            "      \"promo_id\": 2789,\n" +
            "      \"code\": \"PRE12345\",\n" +
            "      \"title\": \"Gratis ongkir Lagi!!\",\n" +
            "      \"description\": \"nikmati gratis ongkir sampai puass.. buruannn!!\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"reward_points\": 300\n" +
            "}";

    String RESPONSE_VALIDATE_REEDEM = "{\n" +
            "  \"is_valid\": 1,\n" +
            "  \"message_success\": \"Anda ama menukar 2000 poin dengan <b>Gratis Ongkir Rp. 40.000, masa berlaku 30 hari</b>\"\n" +
            "}";

    String RESPONSE_COUPON_LIST = "{\n" +
            "  \"coupons\": [\n" +
            "    {\n" +
            "      \"id\": 2890,\n" +
            "      \"promo_id\": 2789,\n" +
            "      \"code\": \"PRE12345\",\n" +
            "      \"expired\": \"20 Okt 2017\",\n" +
            "      \"title\": \"Gratis ongkir Lagi!!\",\n" +
            "      \"sub_title\": \"Hemat sampai 40k!\",\n" +
            "      \"description\": \"nikmati gratis ongkir sampai puass.. buruannn!!\",\n" +
            "      \"icon\": \"https://ecs7.tokopedia.net/img/icon1.png\",\n" +
            "      \"image_url\": \"https://ecs7.tokopedia.net/img/banner1.png\",\n" +
            "      \"image_url_mobile\": \"https://ecs7.tokopedia.net/img/mobile-banner1.png\",\n" +
            "      \"thumbnail_url\": \"https://ecs7.tokopedia.net/img/thumb1.png\",\n" +
            "      \"thumbnail_url_mobile\": \"https://ecs7.tokopedia.net/img/thumb_mobile2.png\",\n" +
            "      \"cta\": \"https://www.tokopedia.com/\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 2830,\n" +
            "      \"promo_id\": 2749,\n" +
            "      \"code\": \"PRE12365\",\n" +
            "      \"expired\": \"20 Okt 2017\",\n" +
            "      \"title\": \"Cashback gede Lagi!!\",\n" +
            "      \"sub_title\": \"Hemat sampai 40k!!\",\n" +
            "      \"description\": \"nikmati cashback sampai puass.. buruannn!!\",\n" +
            "      \"icon\": \"https://ecs7.tokopedia.net/img/icon2.png\",\n" +
            "      \"image_url\": \"https://ecs7.tokopedia.net/img/banner2.png\",\n" +
            "      \"image_url_mobile\": \"https://ecs7.tokopedia.net/img/mobile-banner2.png\",\n" +
            "      \"thumbnail_url\": \"https://ecs7.tokopedia.net/img/thumb1.png\",\n" +
            "      \"thumbnail_url_mobile\": \"https://ecs7.tokopedia.net/img/thumb_mobile2.png\",\n" +
            "      \"cta\": \"https://www.tokopedia.com/\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

}
