package com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory;

import com.tokopedia.core.network.apiservices.mojito.MojitoNoRetryAuthService;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.AddWishlistMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.RemoveWishlistMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.cloud.AddWishlistCloudSource;

/**
 * @author by nisie on 5/30/17.
 */

public class WishlistFactory {

    private AddWishlistMapper addWishlistMapper;
    private RemoveWishlistMapper removeWishlistMapper;
    private MojitoNoRetryAuthService service;

    public WishlistFactory(AddWishlistMapper addWishlistMapper,
                           RemoveWishlistMapper removeWishlistMapper,
                           MojitoNoRetryAuthService service) {
        this.addWishlistMapper = addWishlistMapper;
        this.removeWishlistMapper = removeWishlistMapper;
        this.service = service;
    }

    public AddWishlistCloudSource createCloudWishlistSource() {
        return new AddWishlistCloudSource(
                service,
                addWishlistMapper,
                removeWishlistMapper);
    }

}
