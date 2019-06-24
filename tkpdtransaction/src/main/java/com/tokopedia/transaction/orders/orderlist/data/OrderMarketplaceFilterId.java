package com.tokopedia.transaction.orders.orderlist.data;

public interface OrderMarketplaceFilterId {

    /**
     * this Constant from GQL RESPONSE
     *
     * {
     * "label": "MarketPlace",
     * "orderCategory": "MARKETPLACE",
     * "labelBhasa": "Belanja",
     * "status": [
     * {
     * "label": "5",
     * "value": "Menunggu Konfirmasi"
     * },
     * {
     * "label": "12",
     * "value": "Pesanan Diproses"
     * },
     * {
     * "label": "13",
     * "value": "Pesanan Dikirim"
     * },
     * {
     * "label": "14",
     * "value": "Pesanan Tiba"
     * },
     * {
     * "label": "15",
     * "value": "Pesanan Dikomplain"
     * },
     * {
     * "label": "7",
     * "value": "Pesanan Selesai"
     * },
     * {
     * "label": "16",
     * "value": "Pesanan Dibatalkan"
     * },
     * {
     * "label": "17",
     * "value": "Pencarian Pengganti"
     * },
     * {
     * "label": "4",
     * "value": "Dalam Proses"
     * }
     * ]
     * }
     */
    String MENUNGGU_KONFIRMASI = "5";
    String PESANAN_DIPROSES = "12";
    String PESANAN_DIKIRIM = "13";
    String PESANAN_TIBA = "14";
    String PESANAN_DIKOMPLAIN = "15";
    String PESANAN_SELESAI = "7";
    String PESANAN_DIBATALKAN = "16";
    String PENCARIAN_PENGGANTI = "17";
    String DALAM_PROSES = "4";
}
