package com.tokopedia.inbox.inboxticket.adapter;

import android.content.Context;

import com.tokopedia.inbox.inboxticket.model.inboxticket.InboxTicket;
import com.tokopedia.inbox.inboxticket.model.inboxticketdetail.InboxTicketDetail;
import com.tokopedia.inbox.inboxticket.model.inboxticketdetail.Ticket;
import com.tokopedia.inbox.inboxticket.model.inboxticketdetail.TicketReply;
import com.tokopedia.inbox.inboxticket.model.inboxticketdetail.TicketReplyDatum;
import com.tokopedia.inbox.inboxticket.presenter.InboxTicketDetailFragmentPresenter;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;

import java.util.ArrayList;

/**
 * Created by Nisie on 4/25/16.
 */
public class InboxTicketDetailAdapter extends DataBindAdapter {

    private static final int VIEW_HEADER = 100;
    private static final int VIEW_TICKET_DETAIL = 101;
    private static final int HEADER = 1;

    private HeaderTicketDataBinder headerView;
    private TicketDataBinder ticketView;


    public InboxTicketDetailAdapter(Context context, InboxTicketDetailFragmentPresenter presenter) {
        super();
        InboxTicketDetail data = new InboxTicketDetail();
        data.setTicketReply(new TicketReply());
        data.setTicket(new Ticket());
        data.getTicketReply().setTicketReplyData(new ArrayList<TicketReplyDatum>());
        headerView = new HeaderTicketDataBinder(this, data, context);
        headerView.setPresenter(presenter);
        ticketView = new TicketDataBinder(this, data.getTicketReply().getTicketReplyData(), context);
    }


    public static InboxTicketDetailAdapter createAdapter(Context context, InboxTicketDetailFragmentPresenter presenter) {
        return new InboxTicketDetailAdapter(context, presenter);
    }

    @Override
    public int getItemCount() {
        return headerView.getItemCount() + ticketView.getItemCount();

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
        headerView.setData(data);
        ticketView.setList(data.getTicketReply().getTicketReplyData());
        updateView();
        notifyDataSetChanged();
    }

    public TicketDataBinder getTicketView() {
        return ticketView;
    }

    public HeaderTicketDataBinder getHeaderView() {
        return headerView;
    }

    public void addReply(TicketReplyDatum ticketReply) {

        if (getHeaderView().getData().getTicketReply().getTicketReplyData().size() >= 2) {
            getTicketView().getData().remove(0);
            getHeaderView().getData().getTicketReply().getTicketReplyData().remove(0);
        }

        getTicketView().getData().add(ticketReply);
        getHeaderView().getData().getTicket().setTicketTotalMessage(getHeaderView().getData().getTicket().getTicketTotalMessage() + 1);
        getHeaderView().getData().getTicketReply().getTicketReplyData().add(ticketReply);

    }

    public void updateView() {
        headerView.notifyDataSetChanged();
        ticketView.notifyDataSetChanged();
    }
}
