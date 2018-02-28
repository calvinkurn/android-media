package com.tokopedia.transaction.checkout.domain;

import java.util.List;

/**
 * @author anggaprasetiyo on 23/02/18.
 */

public interface IMapperUtil {
    String convertToString(List<String> stringList);

    boolean isEmpty(List<?> objectList);

    boolean isEmpty(String string);

    boolean isEmpty(Object object);

}
