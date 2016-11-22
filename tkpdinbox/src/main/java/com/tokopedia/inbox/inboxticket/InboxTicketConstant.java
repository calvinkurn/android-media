package com.tokopedia.inbox.inboxticket;

/**
 * Created by Nisie on 7/5/16.
 */
public interface InboxTicketConstant {

    int START_INBOX_TICKET_DETAIL = 200;

    int TICKET_OPEN = 1;
    int TICKET_CLOSED = 2;
    int RATING_GOOD = 1;
    int RATING_BAD = 2;

    String INTENT_NAME = "InboxMessageService";

    int STATUS_SUCCESS = 1;
    int STATUS_ERROR = 2;

    String EXTRA_TYPE = "EXTRA_TYPE";
    String EXTRA_BUNDLE = "EXTRA_BUNDLE";
    String EXTRA_RECEIVER = "EXTRA_RECEIVER";
    String EXTRA_RESULT = "EXTRA_RESULT";
    String EXTRA_PARAM = "EXTRA_PARAM";
    String EXTRA_ERROR = "EXTRA_ERROR";

    int ACTION_SET_RATING = 101;
    String PARAM_SET_RATING = "PARAM_ARCHIVE_MESSAGE";

    String STATUS_UNREAD = "unread";
    String STATUS_ALL = "";
    String TICKET_ID_BUNDLE = "ticket_id";
    String INBOX_ID_BUNDLE = "inbox_id";
    String POSITION_BUNDLE = "position";

}
