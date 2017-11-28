package com.tokopedia.inbox.inboxmessage;

/**
 * Created by Nisie on 5/10/16.
 */
public interface InboxMessageConstant {

    int OFFSCREEN_PAGE_LIMIT = 3;
    int OPEN_DETAIL_MESSAGE = 1;

    String DEFAULT_ERROR_MOVE_INBOX = "Gagal memindahkan pesan";
    String MUST_REFRESH = "MUST_REFRESH";

    String MESSAGE_ALL = "inbox-message";
    String MESSAGE_ARCHIVE = "inbox-message-archive";
    String MESSAGE_SENT = "inbox-message-sent";
    String MESSAGE_TRASH = "inbox-message-trash";

    String PARAM_NAV = "nav";
    String PARAM_ALL = "all";
    String PARAM_UNREAD = "unread";
    String PARAM_MESSAGE = "message";
    String PARAM_MESSAGE_ID = "message_id";
    String PARAM_POSITION = "position";
    String PARAM_SENT_MESSAGE = "sent_message";
    String PARAM_SENDER_NAME = "fullname";
    String PARAM_SENDER_TAG = "tag";
    String PARAM_SENDER_ID = "sender_id";
    String PARAM_SENDER_IMAGE = "image";
    String PARAM_MODE = "mode";
    String PARAM_KEYWORD = "keyword";


    String MOVE_ALL = "move_to_inbox_all";
    String ARCHIVE_ALL = "archive_inbox_all";
    String DELETE_ALL = "delete_inbox_all";
    String DELETE_FOREVER = "delete_forever_inbox_all";
    String MOVE_DETAIL = "move_to_inbox_detail";
    String ARCHIVE_DETAIL = "archive_inbox_detail";
    String DELETE_DETAIL = "delete_inbox_detail";

    String INTENT_NAME = "InboxMessageService";

    int STATUS_SUCCESS = 1;
    int STATUS_ERROR = 2;

    String EXTRA_TYPE = "EXTRA_TYPE";
    String EXTRA_BUNDLE = "EXTRA_BUNDLE";
    String EXTRA_RECEIVER = "EXTRA_RECEIVER";
    String EXTRA_RESULT = "EXTRA_RESULT";
    String EXTRA_PARAM = "EXTRA_PARAM";


    int ACTION_ARCHIVE_MESSAGE = 111;
    int ACTION_UNDO_ARCHIVE_MESSAGE = 112;
    int ACTION_DELETE_MESSAGE = 113;
    int ACTION_UNDO_DELETE_MESSAGE = 114;
    int ACTION_MOVE_TO_INBOX = 115;
    int ACTION_UNDO_MOVE_TO_INBOX = 116;
    int ACTION_DELETE_FOREVER = 117;

    int ACTION_SEND_REPLY = 118;
    int ACTION_FLAG_SPAM = 119;
    int ACTION_UNDO_FLAG_SPAM = 120;
    int ACTION_MARK_AS_READ = 121;
    int ACTION_MARK_AS_UNREAD = 122;


    String PARAM_ARCHIVE_MESSAGE = "PARAM_ARCHIVE_MESSAGE";
    String PARAM_UNDO_ARCHIVE_MESSAGE = "PARAM_UNDO_ARCHIVE_MESSAGE";
    String PARAM_DELETE_MESSAGE = "PARAM_DELETE_MESSAGE";
    String PARAM_UNDO_DELETE_MESSAGE = "PARAM_UNDO_DELETE_MESSAGE";
    String PARAM_MOVE_TO_INBOX = "PARAM_MOVE_TO_INBOX";
    String PARAM_UNDO_MOVE_TO_INBOX = "PARAM_UNDO_MOVE_TO_INBOX";
    String PARAM_DELETE_FOREVER = "PARAM_DELETE_FOREVER";

    String PARAM_SEND_REPLY = "PARAM_SEND_REPLY";
    String PARAM_FLAG_SPAM = "PARAM_FLAG_SPAM";
    String PARAM_UNDO_FLAG_SPAM = "PARAM_UNDO_FLAG_SPAM";
    String PARAM_MARK_AS_READ = "PARAM_MARK_AS_READ";
    String PARAM_MARK_AS_UNREAD = "PARAM_MARK_AS_READ";

    String EXTRA_POSITION = "EXTRA_POSITION";
    String EXTRA_FLAGGED_MESSAGE = "EXTRA_FLAGGED_MESSAGE";


    String EXTRA_ERROR = "EXTRA_ERROR";

    int IS_ADMIN = 1;

    String FILTER_BUNDLE = "INBOX_MESSAGE_FILTER";
    String BUNDLE_POSITION = "POSITION";

    int VIEW_MESSAGE = 100;
    int STATE_SELECTED = 100;
    int STATE_READ = 1;
    int STATE_NOT_READ = 0;
    int STATE_BOTH = 2;


    int STATE_CHAT_UNREAD = 1;
    int STATE_CHAT_READ = 2;
    int STATE_CHAT_BOTH = 3;
}
