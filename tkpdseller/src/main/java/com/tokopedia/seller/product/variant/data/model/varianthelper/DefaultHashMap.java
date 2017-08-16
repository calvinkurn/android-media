package com.tokopedia.seller.product.variant.data.model.varianthelper;

import java.util.HashMap;

/**
 * Created by User on 8/16/2017.
 */

public class DefaultHashMap<K,V> extends HashMap<K,V> {
    protected V defaultValue;
    public DefaultHashMap(V defaultValue) {
        this.defaultValue = defaultValue;
    }
    @Override
    public V get(Object k) {
        return containsKey(k) ? super.get(k) : defaultValue;
    }
}
