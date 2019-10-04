package com.tokopedia.seller.seller.info.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.seller.R;
import com.tokopedia.seller.seller.info.di.scope.SellerInfoScope;
import javax.inject.Named;
import dagger.Module;
import dagger.Provides;

import static com.tokopedia.seller.seller.info.constant.SellerInfoConstantKt.SELLER_CENTER_RAW;

/**
 * Created by normansyahputa on 11/30/17.
 */
@SellerInfoScope
@Module
public class SellerInfoModule {

    @Named(SELLER_CENTER_RAW)
    @Provides
    public String provideRawSellerCenter(@ApplicationContext Context context){
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.notifcenter_list_raw);
    }
}
