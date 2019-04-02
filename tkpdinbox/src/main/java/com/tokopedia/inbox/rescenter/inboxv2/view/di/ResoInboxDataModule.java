package com.tokopedia.inbox.rescenter.inboxv2.view.di;

import com.tokopedia.inbox.rescenter.createreso.view.di.CreateResoModule;

import dagger.Module;

/**
 * Created by yfsx on 24/01/18.
 */

@ResoInboxScope
@Module(includes = {ResoInboxModule.class})
public class ResoInboxDataModule {

}
