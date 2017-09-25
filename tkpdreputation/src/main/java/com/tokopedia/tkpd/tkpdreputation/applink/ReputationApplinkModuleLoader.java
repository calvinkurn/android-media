package com.tokopedia.tkpd.tkpdreputation.applink;

import com.airbnb.deeplinkdispatch.DeepLinkEntry;
import com.airbnb.deeplinkdispatch.DeepLinkModule;
import com.airbnb.deeplinkdispatch.Parser;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;

import java.util.Arrays;
import java.util.List;

/**
 * @author by nisie on 9/20/17.
 */

@DeepLinkModule
public class ReputationApplinkModuleLoader implements Parser {
    private static final List<DeepLinkEntry> REGISTRY = Arrays.asList(
            new DeepLinkEntry("tokopedia://review", DeepLinkEntry.Type.METHOD, InboxReputationActivity.class, "getCallingIntent")
//            ,
//            new DeepLinkEntry("tokopedia://product/{product_id}/review", DeepLinkEntry.Type.METHOD, ReputationProduct.class, "getCallingIntent")
    );

    @Override
    public DeepLinkEntry parseUri(String uri) {
        for (DeepLinkEntry entry : REGISTRY) {
            if (entry.matches(uri)) {
                return entry;
            }
        }
        return null;
    }
}