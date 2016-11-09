
package com.tokopedia.inbox.contactus.model.contactuscategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DefaultPosition {

    @SerializedName("ticket_category_index_tree_1")
    @Expose
    private int ticketCategoryIndexTree1;

    /**
     * 
     * @return
     *     The ticketCategoryIndexTree1
     */
    public int getTicketCategoryIndexTree1() {
        return ticketCategoryIndexTree1;
    }

    /**
     * 
     * @param ticketCategoryIndexTree1
     *     The ticket_category_index_tree_1
     */
    public void setTicketCategoryIndexTree1(int ticketCategoryIndexTree1) {
        this.ticketCategoryIndexTree1 = ticketCategoryIndexTree1;
    }

}
