package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by yoasfs on 14/08/17.
 */

public interface ProductProblemPresenter {

    void initFragment(@NonNull Context context, Uri uriData, Bundle bundleData);
}
