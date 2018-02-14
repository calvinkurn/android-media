package com.tokopedia.ride.chat.utils;

import android.content.Context;

/**
 * Created by sachinbansal on 2/13/18.
 */

public interface ViewBuilderInterface {
    MessageView buildRecvView(Context context);

    MessageView buildSentView(Context context);
}
