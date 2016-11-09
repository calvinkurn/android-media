
package com.tokopedia.inbox.contactus.model.contactusform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketFormData {

    @SerializedName("ticket_category_back_url")
    @Expose
    private String ticketCategoryBackUrl;
    @SerializedName("ticket_category_login_status")
    @Expose
    private String ticketCategoryLoginStatus;
    @SerializedName("ticket_category_breadcrumb")
    @Expose
    private String ticketCategoryBreadcrumb;
    @SerializedName("ticket_category_attachment_status")
    @Expose
    private String ticketCategoryAttachmentStatus;
    @SerializedName("ticket_category_invoice_status")
    @Expose
    private String ticketCategoryInvoiceStatus;

    /**
     * 
     * @return
     *     The ticketCategoryBackUrl
     */
    public String getTicketCategoryBackUrl() {
        return ticketCategoryBackUrl;
    }

    /**
     * 
     * @param ticketCategoryBackUrl
     *     The ticket_category_back_url
     */
    public void setTicketCategoryBackUrl(String ticketCategoryBackUrl) {
        this.ticketCategoryBackUrl = ticketCategoryBackUrl;
    }

    /**
     * 
     * @return
     *     The ticketCategoryLoginStatus
     */
    public String getTicketCategoryLoginStatus() {
        return ticketCategoryLoginStatus;
    }

    /**
     * 
     * @param ticketCategoryLoginStatus
     *     The ticket_category_login_status
     */
    public void setTicketCategoryLoginStatus(String ticketCategoryLoginStatus) {
        this.ticketCategoryLoginStatus = ticketCategoryLoginStatus;
    }

    /**
     * 
     * @return
     *     The ticketCategoryBreadcrumb
     */
    public String getTicketCategoryBreadcrumb() {
        return ticketCategoryBreadcrumb;
    }

    /**
     * 
     * @param ticketCategoryBreadcrumb
     *     The ticket_category_breadcrumb
     */
    public void setTicketCategoryBreadcrumb(String ticketCategoryBreadcrumb) {
        this.ticketCategoryBreadcrumb = ticketCategoryBreadcrumb;
    }

    /**
     * 
     * @return
     *     The ticketCategoryAttachmentStatus
     */
    public String getTicketCategoryAttachmentStatus() {
        return ticketCategoryAttachmentStatus;
    }

    /**
     * 
     * @param ticketCategoryAttachmentStatus
     *     The ticket_category_attachment_status
     */
    public void setTicketCategoryAttachmentStatus(String ticketCategoryAttachmentStatus) {
        this.ticketCategoryAttachmentStatus = ticketCategoryAttachmentStatus;
    }

    /**
     * 
     * @return
     *     The ticketCategoryInvoiceStatus
     */
    public String getTicketCategoryInvoiceStatus() {
        return ticketCategoryInvoiceStatus;
    }

    /**
     * 
     * @param ticketCategoryInvoiceStatus
     *     The ticket_category_invoice_status
     */
    public void setTicketCategoryInvoiceStatus(String ticketCategoryInvoiceStatus) {
        this.ticketCategoryInvoiceStatus = ticketCategoryInvoiceStatus;
    }

}
