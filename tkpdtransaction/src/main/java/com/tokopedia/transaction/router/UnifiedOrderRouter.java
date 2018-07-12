package com.tokopedia.transaction.router;

import android.content.Context;
import android.content.Intent;

public interface UnifiedOrderRouter {
    Intent getHomeIntent(Context context);
}
