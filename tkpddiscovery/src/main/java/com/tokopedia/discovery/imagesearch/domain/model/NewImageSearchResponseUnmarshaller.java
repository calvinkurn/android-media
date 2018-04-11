package com.tokopedia.discovery.imagesearch.domain.model;

import com.aliyuncs.transform.UnmarshallerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sachinbansal on 3/9/18.
 */

public class NewImageSearchResponseUnmarshaller {
    public NewImageSearchResponseUnmarshaller() {
    }

    public static ImageSearchItemResponse unmarshall(ImageSearchItemResponse searchItemResponse, UnmarshallerContext context) {
        searchItemResponse.setRequestId(context.stringValue("ImageSearchItemResponse.RequestId"));
        searchItemResponse.setSuccess(context.booleanValue("ImageSearchItemResponse.Success"));
        searchItemResponse.setMessage(context.stringValue("ImageSearchItemResponse.Message"));
        searchItemResponse.setCode(context.integerValue("ImageSearchItemResponse.Code"));
        Head head = new Head();
        head.setSearchTime(context.integerValue("ImageSearchItemResponse.Head.SearchTime"));
        head.setDocsFound(context.integerValue("ImageSearchItemResponse.Head.DocsFound"));
        head.setDocsReturn(context.integerValue("ImageSearchItemResponse.Head.DocsReturn"));
        searchItemResponse.setHead(head);
        PicInfo picInfo = new PicInfo();
        picInfo.setCategory(context.stringValue("ImageSearchItemResponse.PicInfo.Category"));
        picInfo.setRegion(context.stringValue("ImageSearchItemResponse.PicInfo.Region"));
        List<Category> allCategory = new ArrayList();

        for (int i = 0; i < context.lengthValue("ImageSearchItemResponse.PicInfo.AllCategory.Length"); ++i) {
            Category category = new Category();
            category.setName(context.stringValue("ImageSearchItemResponse.PicInfo.AllCategory[" + i + "].Name"));
            category.setId(context.stringValue("ImageSearchItemResponse.PicInfo.AllCategory[" + i + "].Id"));
            allCategory.add(category);
        }

        picInfo.setCategoryList(allCategory);
        searchItemResponse.setPicInfo(picInfo);
        ArrayList<Auction> auctions = new ArrayList();

        for (int i = 0; i < context.lengthValue("ImageSearchItemResponse.Auctions.Length"); ++i) {
            Auction auction = new Auction();
            auction.setCustContent(context.stringValue("ImageSearchItemResponse.Auctions[" + i + "].CustContent"));
            auction.setItemId(context.stringValue("ImageSearchItemResponse.Auctions[" + i + "].ItemId"));
            auction.setSortExprValues(context.stringValue("ImageSearchItemResponse.Auctions[" + i + "].SortExprValues"));
            auction.setCatId(context.stringValue("ImageSearchItemResponse.Auctions[" + i + "].CatId"));
            auction.setPicName(context.stringValue("ImageSearchItemResponse.Auctions[" + i + "].PicName"));
            auctions.add(auction);
        }

        searchItemResponse.setAuctionsArrayList(auctions);
        return searchItemResponse;
    }
}
