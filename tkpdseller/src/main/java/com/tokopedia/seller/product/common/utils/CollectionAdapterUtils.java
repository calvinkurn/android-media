package com.tokopedia.seller.product.common.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Created by nathan on 2/28/18.
 */

public class CollectionAdapterUtils implements JsonSerializer<Collection<?>> {

    private boolean isRemoveEmpty = false;

    public CollectionAdapterUtils withRemoveEmpty(boolean removeEmpty) {
        this.isRemoveEmpty = removeEmpty;
        return this;
    }
    @Override
    public JsonElement serialize(Collection<?> src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null ) {
            return null;
        }
        if (isRemoveEmpty) {
            if (src.isEmpty()) {
                return null;
            }
        }

        JsonArray array = new JsonArray();
        for (Object child : src) {
            JsonElement element = context.serialize(child);
            array.add(element);
        }
        return array;
    }
}
