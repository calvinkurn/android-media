package com.tokopedia.inbox.rescenter.inbox.facade;

import android.app.Activity;

import java.util.Map;

/**
 * Created on 4/7/16.
 */
public interface Facade {

    void initInboxData(Activity activity, Map<String, String> params);

    void loadMoreInboxData(Activity activity, Map<String, String> params);

    void forceFinish();
}
