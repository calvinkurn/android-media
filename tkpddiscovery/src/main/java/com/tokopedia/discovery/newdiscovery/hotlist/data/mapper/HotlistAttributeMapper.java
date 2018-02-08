package com.tokopedia.discovery.newdiscovery.hotlist.data.mapper;

import com.google.gson.Gson;
import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.discovery.newdiscovery.hotlist.data.pojo.PojoHotlistAttribute;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistAttributeModel;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistBreadcrumb;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistHashtagModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 10/6/17.
 */

public class HotlistAttributeMapper implements Func1<Response<String>, HotlistAttributeModel> {

    private final Gson gson;

    public HotlistAttributeMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public HotlistAttributeModel call(Response<String> response) {
        if (response.isSuccessful()) {

            PojoHotlistAttribute pojoHotlistAttr = gson.fromJson(response.body(), PojoHotlistAttribute.class);
            if (pojoHotlistAttr != null) {
                return mappingIntoDomainModel(pojoHotlistAttr.getData());
            } else {
                throw new MessageErrorException(response.errorBody().toString());
            }
        } else {
            throw new RuntimeHttpErrorException(response.code());
        }
    }

    private HotlistAttributeModel mappingIntoDomainModel(PojoHotlistAttribute.Data pojoHotlistAttr) {
        HotlistAttributeModel domainModel = new HotlistAttributeModel();
        domainModel.setHastTags(mappingPojoHotlistHashtag(pojoHotlistAttr.getHashtag()));
        domainModel.setBreadCrumbs(mappingPojoHotlistBreadCrumbs(pojoHotlistAttr.getBreadcrumb()));
        return domainModel;
    }

    private List<HotlistBreadcrumb> mappingPojoHotlistBreadCrumbs(List<PojoHotlistAttribute.Data.Breadcrumb> breadcrumbList) {
        List<HotlistBreadcrumb> list = new ArrayList<>();
        for (PojoHotlistAttribute.Data.Breadcrumb pojo : breadcrumbList) {
            HotlistBreadcrumb domain = new HotlistBreadcrumb();
            domain.setID(pojo.getId());
            domain.setName(pojo.getName());
            domain.setUrl(pojo.getHref());
            domain.setIdentifier(pojo.getIdentifier());
            domain.setParentID(pojo.getParentId());
            domain.setTreeCount(pojo.getTree());
            domain.setTotalData(pojo.getTotalData());
            if (pojo.getChild() != null && !pojo.getChild().isEmpty()) {
                domain.setChild(mappingPojoBreadCrumbs2(pojo.getChild()));
            }
            list.add(domain);
        }
        return list;
    }

    private List<HotlistBreadcrumb> mappingPojoBreadCrumbs2(List<PojoHotlistAttribute.Data.Breadcrumb> breadcrumbList) {
        List<HotlistBreadcrumb> list = new ArrayList<>();
        for (PojoHotlistAttribute.Data.Breadcrumb pojo : breadcrumbList) {
            HotlistBreadcrumb domain = new HotlistBreadcrumb();
            domain.setID(pojo.getId());
            domain.setName(pojo.getName());
            domain.setUrl(pojo.getHref());
            domain.setIdentifier(pojo.getIdentifier());
            domain.setParentID(pojo.getParentId());
            domain.setTreeCount(pojo.getTree());
            domain.setTotalData(pojo.getTotalData());
            if (pojo.getChild() != null && !pojo.getChild().isEmpty()) {
                domain.setChild(mappingPojoBreadCrumbs3(pojo.getChild()));
            }
            list.add(domain);
        }
        return list;
    }

    private List<HotlistBreadcrumb> mappingPojoBreadCrumbs3(List<PojoHotlistAttribute.Data.Breadcrumb> child) {
        List<HotlistBreadcrumb> list = new ArrayList<>();
        for (PojoHotlistAttribute.Data.Breadcrumb pojo : child) {
            HotlistBreadcrumb domain = new HotlistBreadcrumb();
            domain.setID(pojo.getId());
            domain.setName(pojo.getName());
            domain.setUrl(pojo.getHref());
            domain.setIdentifier(pojo.getIdentifier());
            domain.setParentID(pojo.getParentId());
            domain.setTreeCount(pojo.getTree());
            domain.setTotalData(pojo.getTotalData());
            list.add(domain);
        }
        return list;
    }

    private List<HotlistHashtagModel> mappingPojoHotlistHashtag(List<PojoHotlistAttribute.Data.Hashtag> hashtagList) {
        List<HotlistHashtagModel> list = new ArrayList<>();
        for (PojoHotlistAttribute.Data.Hashtag pojo : hashtagList) {
            HotlistHashtagModel model = new HotlistHashtagModel();
            model.setDepartmentID(pojo.getDepartmentId());
            model.setName(pojo.getName());
            model.setURL(pojo.getUrl());
            list.add(model);
        }
        return list;
    }

}
