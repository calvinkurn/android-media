package com.tokopedia.seller.seller.info.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseSellerInfoModel(
        @SerializedName("notifcenter_detail")
        @Expose
        val notifData: NotifCenterDetail = NotifCenterDetail()
)

data class NotifCenterDetail(
        @SerializedName("list")
        @Expose
        val list: ArrayList<DataList> = arrayListOf(),

        @SerializedName("paging")
        @Expose
        val paging: SellerCenterPaging = SellerCenterPaging()
)

data class DataList(
        @SerializedName("notif_id")
        @Expose
        val notifId: String = "",

        @SerializedName("status")
        @Expose
        val status: Int = 0,

        @SerializedName("type_of_user")
        @Expose
        val type: Int = 0,

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("content")
        @Expose
        val content: String = "",

        @SerializedName("short_description")
        @Expose
        val shortDescription: String = "",

        @SerializedName("data_notification")
        @Expose
        val dataNotification: DataNotification = DataNotification(),

        @SerializedName("read_status")
        @Expose
        val readStatus: Int = 0,

        @SerializedName("create_time_unix")
        @Expose
        val createTimeUnix: Int = 0,

        @SerializedName("section_id")
        @Expose
        val sectionId: String = "",

        @SerializedName("type_link")
        @Expose
        val typeLink: Int = 0,

        @SerializedName("section_key")
        @Expose
        val sectionName: String = "",

        @SerializedName("section_icon")
        @Expose
        val sectionIcon: String = ""
) {
    fun getReadStatusInfo(): Boolean {
            return readStatus == 2
    }
}

data class DataNotification(
        @SerializedName("app_link")
        @Expose
        val appLink: String = "",

        @SerializedName("info_thumbnail_url")
        @Expose
        val infoThumbnailUrl: String = "",

        @SerializedName("desktop_link")
        @Expose
        val desktopLink: String = ""
)

data class SellerCenterPaging(
        @SerializedName("has_next")
        @Expose
        val hasNext: Boolean = false,

        @SerializedName("has_prev")
        @Expose
        val hasPrev: Boolean = false
)