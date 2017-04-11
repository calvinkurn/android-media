package com.tokopedia.seller.product.domain.model;

import java.net.URI;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class ImageProductInputDomainModel {
    private String url;
    private URI uri;
    private String description;

    public String getUrl() {
        return url;
    }

    public URI getUri() {
        return uri;
    }

    public String getDescription() {
        return description;
    }
}
