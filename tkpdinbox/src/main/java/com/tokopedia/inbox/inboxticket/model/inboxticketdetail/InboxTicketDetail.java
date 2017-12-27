
package com.tokopedia.inbox.inboxticket.model.inboxticketdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InboxTicketDetail {

    @SerializedName("show_rating")
    @Expose
    private boolean showRating;
    @SerializedName("ticket_reply")
    @Expose
    private TicketReply ticketReply;
    @SerializedName("ticket")
    @Expose
    private Ticket ticket;

    public boolean isShowRating() {
        return showRating;
    }

    public void setShowRating(boolean showRating) {
        this.showRating = showRating;
    }

    /**
     * 
     * @return
     *     The ticketReply
     */
    public TicketReply getTicketReply() {
        return ticketReply;
    }

    /**
     * 
     * @param ticketReply
     *     The ticket_reply
     */
    public void setTicketReply(TicketReply ticketReply) {
        this.ticketReply = ticketReply;
    }

    /**
     * 
     * @return
     *     The ticket
     */
    public Ticket getTicket() {
        return ticket;
    }

    /**
     * 
     * @param ticket
     *     The ticket
     */
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

}
