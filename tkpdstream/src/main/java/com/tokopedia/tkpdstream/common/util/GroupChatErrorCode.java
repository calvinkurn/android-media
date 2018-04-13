package com.tokopedia.tkpdstream.common.util;

/**
 * @author by nisie on 2/21/18.
 */

public interface GroupChatErrorCode {
    int UNKNOWN = 1000;
    int UNKNOWN_HOST_EXCEPTION = 1001;
    int SOCKET_TIMEOUT_EXCEPTION = 1002;
    int IO_EXCEPTION = 1003;
    int WS_ERROR = 1004;
    int UNSUPPORTED_FLOW = 1005;
    int MALFORMED_DATA = 1006;

}
