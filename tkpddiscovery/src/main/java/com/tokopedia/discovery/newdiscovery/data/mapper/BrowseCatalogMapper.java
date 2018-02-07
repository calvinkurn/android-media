package com.tokopedia.discovery.newdiscovery.data.mapper;

import com.google.gson.Gson;
import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.discovery.model.Breadcrumb;
import com.tokopedia.core.network.entity.discovery.BrowseCatalogModel;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.discovery.newdiscovery.domain.model.BreadCrumbDomain;
import com.tokopedia.discovery.newdiscovery.domain.model.CatalogDomainModel;
import com.tokopedia.discovery.newdiscovery.domain.model.CatalogItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 10/12/17.
 */

public class BrowseCatalogMapper implements Func1<Response<String>, CatalogDomainModel> {

    private final Gson gson;

    public BrowseCatalogMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public CatalogDomainModel call(Response<String> response) {
        if (response.isSuccessful()) {
            BrowseCatalogModel pojo = gson.fromJson(response.body(), BrowseCatalogModel.class);
            if (pojo != null) {
                return mappingPojo(pojo);
            } else {
                throw new MessageErrorException("");
            }
        } else {
            throw new RuntimeHttpErrorException(response.code());
        }
    }

    private CatalogDomainModel mappingPojo(BrowseCatalogModel pojo) {
        CatalogDomainModel domain = new CatalogDomainModel();
        domain.setCatalogList(mappingCatalogList(pojo.result.catalogs));
        domain.setUriNext(pojo.result.paging.getUriNext());
        domain.setShareURL(pojo.result.shareUrl);
        domain.setBreadCrumbList(mappingBreadCrumb(pojo.result.breadcrumb));
        return domain;
    }

    private List<CatalogItem> mappingCatalogList(BrowseCatalogModel.Catalogs[] catalogs) {
        List<CatalogItem> list = new ArrayList<>();
        for (BrowseCatalogModel.Catalogs catalog : catalogs) {
            CatalogItem domain = new CatalogItem();
            domain.setCatalogID(catalog.catalogId);
            domain.setCatalogName(catalog.catalogName);
            domain.setCatalogPrice(catalog.catalogPrice);
            domain.setCatalogProductCounter(catalog.catalogCountProduct);
            domain.setCatalogDesc(catalog.catalogDescription);
            domain.setCatalogImage(catalog.catalogImage);
            domain.setCatalogImage300(catalog.catalogImage300);
            domain.setCatalogURL(catalog.catalogUri);
            list.add(domain);
        }
        return list;
    }

    private List<BreadCrumbDomain> mappingBreadCrumb(List<Breadcrumb> breadcrumb) {
        List<BreadCrumbDomain> list = new ArrayList<>();
        for (Breadcrumb pojo : breadcrumb) {
            BreadCrumbDomain domain = new BreadCrumbDomain();
            domain.setID(pojo.id);
            domain.setName(pojo.name);
            domain.setUrl(pojo.href);
            domain.setIdentifier(pojo.identifier);
            domain.setParentID(pojo.parentId);
            domain.setTreeCount(pojo.tree);
            if (pojo.child != null && !pojo.child.isEmpty()) {
                domain.setChild(mappingPojoBreadCrumbs2(pojo.child));
            }
            list.add(domain);
        }
        return list;
    }

    private List<BreadCrumbDomain> mappingPojoBreadCrumbs2(List<Breadcrumb> breadcrumbList) {
        List<BreadCrumbDomain> list = new ArrayList<>();
        for (Breadcrumb pojo : breadcrumbList) {
            BreadCrumbDomain domain = new BreadCrumbDomain();
            domain.setID(pojo.id);
            domain.setName(pojo.name);
            domain.setUrl(pojo.href);
            domain.setIdentifier(pojo.identifier);
            domain.setParentID(pojo.parentId);
            domain.setTreeCount(pojo.tree);
            if (pojo.child != null && !pojo.child.isEmpty()) {
                domain.setChild(mappingPojoBreadCrumbs3(pojo.child));
            }
            list.add(domain);
        }
        return list;
    }

    private List<BreadCrumbDomain> mappingPojoBreadCrumbs3(List<Breadcrumb> child) {
        List<BreadCrumbDomain> list = new ArrayList<>();
        for (Breadcrumb pojo : child) {
            BreadCrumbDomain domain = new BreadCrumbDomain();
            domain.setID(pojo.id);
            domain.setName(pojo.name);
            domain.setUrl(pojo.href);
            domain.setIdentifier(pojo.identifier);
            domain.setParentID(pojo.parentId);
            domain.setTreeCount(pojo.tree);
            list.add(domain);
        }
        return list;
    }
}
