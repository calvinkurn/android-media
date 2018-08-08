package com.tokopedia.discovery.newdiscovery.domain.model;

/**
 * Created by hangnadi on 10/12/17.
 */

public class CatalogItem {
    private String catalogID;
    private String catalogName;
    private String catalogPrice;
    private String catalogProductCounter;
    private String catalogDesc;
    private String catalogImage;
    private String catalogImage300;
    private String catalogURL;

    public void setCatalogID(String catalogID) {
        this.catalogID = catalogID;
    }

    public String getCatalogID() {
        return catalogID;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogPrice(String catalogPrice) {
        this.catalogPrice = catalogPrice;
    }

    public String getCatalogPrice() {
        return catalogPrice;
    }

    public void setCatalogProductCounter(String catalogProductCounter) {
        this.catalogProductCounter = catalogProductCounter;
    }

    public String getCatalogProductCounter() {
        return catalogProductCounter;
    }

    public void setCatalogDesc(String catalogDesc) {
        this.catalogDesc = catalogDesc;
    }

    public String getCatalogDesc() {
        return catalogDesc;
    }

    public void setCatalogImage(String catalogImage) {
        this.catalogImage = catalogImage;
    }

    public String getCatalogImage() {
        return catalogImage;
    }

    public void setCatalogImage300(String catalogImage300) {
        this.catalogImage300 = catalogImage300;
    }

    public String getCatalogImage300() {
        return catalogImage300;
    }

    public void setCatalogURL(String catalogURL) {
        this.catalogURL = catalogURL;
    }

    public String getCatalogURL() {
        return catalogURL;
    }
}
