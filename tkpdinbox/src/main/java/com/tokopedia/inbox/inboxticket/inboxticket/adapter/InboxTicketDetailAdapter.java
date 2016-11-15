package com.tokopedia.inbox.inboxticket.inboxticket.adapter;

import android.content.Context;

import com.tokopedia.inbox.inboxticket.inboxticket.model.inboxticketdetail.InboxTicketDetail;
import com.tokopedia.inbox.inboxticket.inboxticket.model.inboxticketdetail.Ticket;
import com.tokopedia.inbox.inboxticket.inboxticket.model.inboxticketdetail.TicketReply;
import com.tokopedia.inbox.inboxticket.inboxticket.model.inboxticketdetail.TicketReplyDatum;
import com.tokopedia.inbox.inboxticket.inboxticket.presenter.InboxTicketDetailFragmentPresenter;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;

import java.util.ArrayList;

/**
 * Created by Nisie on 4/25/16.
 */
public class InboxTicketDetailAdapter extends DataBindAdapter {

    private static final int VIEW_HEADER = 100;
    private static final int VIEW_TICKET_DETAIL = 101;
    private static final int VIEW_RATING = 102;
    private static final int HEADER = 1;

    HeaderTicketDataBinder headerView;
    TicketDataBinder ticketView;
    InboxTicketDetail data;


    public InboxTicketDetailAdapter(Context context, InboxTicketDetailFragmentPresenter presenter) {
        super();
        data = new InboxTicketDetail();
        data.setTicketReply(new TicketReply());
        data.setTicket(new Ticket());
        data.getTicketReply().setTicketReplyData(new ArrayList<TicketReplyDatum>());
        headerView = new HeaderTicketDataBinder(this, data, context);
        headerView.setPresenter(presenter);
        ticketView = new TicketDataBinder(this, new ArrayList<TicketReplyDatum>(), context);
    }


    public static InboxTicketDetailAdapter createAdapter(Context context, InboxTicketDetailFragmentPresenter presenter) {
        return new InboxTicketDetailAdapter(context, presenter);
    }

    @Override
    public int getItemCount() {
        return (data.getTicket() != null ? 1 : 0)
                + data.getTicketReply().getTicketReplyData().size();

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_HEADER;
        } else {
            return VIEW_TICKET_DETAIL;
        }
    }

    @Override
    public DataBinder getDataBinder(int viewType) {
        switch (viewType) {
            case VIEW_HEADER:
                return headerView;
            default:
                return ticketView;
        }
    }

    @Override
    public int getBinderPosition(int position) {
        switch (getItemViewType(position)) {
            case VIEW_HEADER:
                return 0;
            default:
                return position - HEADER;
        }
    }

    public void setData(InboxTicketDetail data) {
        this.data = data;
        headerView.setData(data);
        ticketView.setList(data.getTicketReply().getTicketReplyData());
        notifyDataSetChanged();
    }

    public InboxTicketDetail getData() {
        return data;
    }

    public TicketDataBinder getTicketView(){
        return ticketView;
    }
}
