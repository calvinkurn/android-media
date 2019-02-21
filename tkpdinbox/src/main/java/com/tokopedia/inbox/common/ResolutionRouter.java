package com.tokopedia.inbox.common;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by meta on 21/02/19.
 */
public interface ResolutionRouter {
    Intent getBannerWebViewIntent(Activity activity, String url);
}
