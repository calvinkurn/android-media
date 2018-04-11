package com.tokopedia.seller.reputation.data.mapper;

import android.support.annotation.NonNull;

import com.tokopedia.seller.reputation.data.model.response.SellerReputationResponse;
import com.tokopedia.seller.reputation.domain.model.SellerReputationDomain;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;


/**
 * @author normansyahputa on 3/16/17.
 */
@Deprecated
public class ReputationReviewMapper implements Func1<Response<SellerReputationResponse>, SellerReputationDomain> {
    @Inject
    public ReputationReviewMapper() {
    }

    @NonNull
    public static SellerReputationDomain getSellerReputationDomains(
            SellerReputationResponse sellerReputationResponse
    ) {
        SellerReputationDomain sellerReputationDomain =
                new SellerReputationDomain();
        SellerReputationResponse.Links links = sellerReputationResponse.getLinks();
        List<SellerReputationResponse.Data> list = sellerReputationResponse.getList();

        List<SellerReputationDomain.Data> resultlist = new ArrayList<>();
        for (SellerReputationResponse.Data data : list) {
            SellerReputationDomain.Data res = new
                    SellerReputationDomain.Data();

            res.setDate(data.getDate());
            res.setInformation(data.getInformation());
            res.setPenaltyScore(data.getPenaltyScore());

            resultlist.add(res);
        }

        SellerReputationDomain.Links resLinks = new SellerReputationDomain.Links();
        resLinks.setNext(links.getNext());
        resLinks.setPrev(links.getPrev());
        resLinks.setSelf(links.getSelf());

        sellerReputationDomain.setLinks(resLinks);
        sellerReputationDomain.setList(resultlist);

        return sellerReputationDomain;

    }

    @Override
    public SellerReputationDomain call(Response<SellerReputationResponse> sellerReputationResponseResponse) {
        return getSellerReputationDomains(sellerReputationResponseResponse.body());
    }
}
