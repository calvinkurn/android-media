package com.tokopedia.inbox.common;

import android.content.Context;
import android.content.Intent;

/**
 * Created by meta on 21/02/19.
 */
public interface ResolutionRouter {
    Intent getApplinkIntent(Context context, String applink);
}
