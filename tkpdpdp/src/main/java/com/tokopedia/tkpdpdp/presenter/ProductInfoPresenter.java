package com.tokopedia.tkpdpdp.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.model.share.ShareData;

/**
 * @author Angga.Prasetiyo on 18/11/2015.
 */
public interface ProductInfoPresenter {

    void initialFragment(@NonNull Context context, Uri uri, Bundle bundle);

    void processToShareProduct(Context context, @NonNull ShareData data);
}
