package com.tokopedia.inbox.rescenter.create.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created on 6/15/16.
 */
public interface CreateResCenterPresenter {

    void initFragment(@NonNull Context context, Uri uriData, Bundle bundleData);
    void initRecomplaintFragment(@NonNull Context context, String orderId, String resolutionId);
}
