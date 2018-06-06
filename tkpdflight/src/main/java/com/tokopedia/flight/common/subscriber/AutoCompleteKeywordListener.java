package com.tokopedia.flight.common.subscriber;

public interface AutoCompleteKeywordListener {

    void onTextReceive(String keyword);

    void onError(Throwable e);
}
