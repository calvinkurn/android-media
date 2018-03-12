package com.tokopedia.discovery.imagesearch.domain.usecase;

import com.aliyuncs.transform.UnmarshallerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sachinbansal on 3/9/18.
 */

public class NewImageSearchResponseUnmarshaller {
    public NewImageSearchResponseUnmarshaller() {
    }

    public static NewImageSearchResponse unmarshall(NewImageSearchResponse searchItemResponse, UnmarshallerContext context) {
        searchItemResponse.setRequestId(context.stringValue("NewImageSearchResponse.RequestId"));
        searchItemResponse.setSuccess(context.booleanValue("NewImageSearchResponse.Success"));
        searchItemResponse.setMessage(context.stringValue("NewImageSearchResponse.Message"));
        searchItemResponse.setCode(context.integerValue("NewImageSearchResponse.Code"));
        Head head = new Head();
        head.setSearchTime(context.integerValue("NewImageSearchResponse.Head.SearchTime"));
        head.setDocsFound(context.integerValue("NewImageSearchResponse.Head.DocsFound"));
        head.setDocsReturn(context.integerValue("NewImageSearchResponse.Head.DocsReturn"));
        searchItemResponse.setHead(head);
        PicInfo picInfo = new PicInfo();
        picInfo.setCategory(context.stringValue("NewImageSearchResponse.PicInfo.Category"));
        picInfo.setRegion(context.stringValue("NewImageSearchResponse.PicInfo.Region"));
        List<Category> allCategory = new ArrayList();

        for (int i = 0; i < context.lengthValue("NewImageSearchResponse.PicInfo.AllCategory.Length"); ++i) {
            Category category = new Category();
            category.setName(context.stringValue("NewImageSearchResponse.PicInfo.AllCategory[" + i + "].Name"));
            category.setId(context.stringValue("NewImageSearchResponse.PicInfo.AllCategory[" + i + "].Id"));
            allCategory.add(category);
        }

        picInfo.setCategoryList(allCategory);
        searchItemResponse.setPicInfo(picInfo);
        ArrayList<Auction> auctions = new ArrayList();

        for (int i = 0; i < context.lengthValue("NewImageSearchResponse.Auctions.Length"); ++i) {
            Auction auction = new Auction();
            auction.setCustContent(context.stringValue("NewImageSearchResponse.Auctions[" + i + "].CustContent"));
            auction.setItemId(context.stringValue("NewImageSearchResponse.Auctions[" + i + "].ItemId"));
            auction.setSortExprValues(context.stringValue("NewImageSearchResponse.Auctions[" + i + "].SortExprValues"));
            auction.setCatId(context.stringValue("NewImageSearchResponse.Auctions[" + i + "].CatId"));
            auction.setPicName(context.stringValue("NewImageSearchResponse.Auctions[" + i + "].PicName"));
            auctions.add(auction);
        }

        searchItemResponse.setAuctionsArrayList(auctions);
        return searchItemResponse;
    }
}
