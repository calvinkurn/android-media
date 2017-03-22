package com.tokopedia.seller.shop.setting.data.source.cache.db;

/**
 * Created by sebastianuskh on 3/21/17.
 */

class DistrictDataManagerException extends RuntimeException {

    public static final String ERROR_MESSAGE = "Failed to store the district Data";

    public DistrictDataManagerException() {
        super(ERROR_MESSAGE);
    }
}
