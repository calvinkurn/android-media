package com.tokopedia.seller.topads.domain.model;

import java.util.List;

/**
 * @author normansyahputa on 3/3/17.
 */
public class ProductListDomain {
    List<ProductDomain> productDomains;
    boolean eof;

    public ProductListDomain() {
    }

    public ProductListDomain(List<ProductDomain> productDomains, boolean eof) {
        this.productDomains = productDomains;
        this.eof = eof;
    }

    public List<ProductDomain> getProductDomains() {
        return productDomains;
    }

    public void setProductDomains(List<ProductDomain> productDomains) {
        this.productDomains = productDomains;
    }

    public boolean isEof() {
        return eof;
    }

    public void setEof(boolean eof) {
        this.eof = eof;
    }
}
