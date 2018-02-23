package com.tokopedia.tkpdstream.common.di.module;


import com.tokopedia.tkpdstream.common.di.scope.StreamScope;

import dagger.Module;

/**
 * @author by nisie on 2/1/18.
 */

@StreamScope
@Module(includes = {StreamNetModule.class})
public class StreamModule {

}
