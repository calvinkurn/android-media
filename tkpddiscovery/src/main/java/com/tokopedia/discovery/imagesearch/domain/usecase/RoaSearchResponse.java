package com.tokopedia.discovery.imagesearch.domain.usecase;

import com.aliyuncs.AcsResponse;
import com.aliyuncs.transform.UnmarshallerContext;


/**
 * Created by sachinbansal on 1/10/18.
 */

class RoaSearchResponse extends AcsResponse {

    @Override
    public RoaSearchResponse getInstance(UnmarshallerContext context) {
        return RoaSearchResponseUnmarshaller.unmarshall(this, context);
    }
}

