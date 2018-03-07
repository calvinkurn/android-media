package com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper;

import com.google.gson.Gson;
import com.tkpdfeed.feeds.HomeFeedQuery;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.InspirationItemDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.TopPicksDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.ContentFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FavoriteCtaDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.InspirationDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.KolPostDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.KolRecommendationDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.KolRecommendationItemDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.ProductFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.PromotionFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.ShopFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.SourceFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.WholesaleDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.BadgeDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.DataDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.LabelDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.OfficialStoreDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.OfficialStoreProductDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.ShopDomain;
import com.tokopedia.topads.sdk.domain.model.Data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by henrypriyono on 12/29/17.
 */

public class HomeFeedMapper implements Func1<HomeFeedQuery.Data, FeedDomain> {
    @Override
    public FeedDomain call(HomeFeedQuery.Data data) {
        return convertToDataFeedDomain(data);
    }

    private FeedDomain convertToDataFeedDomain(HomeFeedQuery.Data data) {

        return new FeedDomain(convertToFeedDomain(data),
                data.feed().links().pagination().has_next_page());
    }

    private List<DataFeedDomain> convertToFeedDomain(HomeFeedQuery.Data data) {
        List<HomeFeedQuery.Data.Datum> datumList = data.feed().data();
        List<DataFeedDomain> dataFeedDomains = new ArrayList<>();
        if (datumList != null) {
            for (int i = 0; i < datumList.size(); i++) {
                HomeFeedQuery.Data.Datum datum = datumList.get(i);
                if (datum.content() != null) {

                    List<InspirationDomain> inspirationDomains = convertToInspirationDomain(datum
                            .content().inspirasi());
                    List<Data> topAdsList = convertToTopadsDomain(datum.content().topads());
                    ContentFeedDomain contentFeedDomain = createContentFeedDomain(
                            datum.content(),
                            inspirationDomains,
                            topAdsList
                    );
                    SourceFeedDomain sourceFeedDomain =
                            createSourceFeedDomain(datum.source());

                    dataFeedDomains.add(createDataFeedDomain(datum,
                            contentFeedDomain, sourceFeedDomain));
                }
            }
        }
        return dataFeedDomains;
    }

    private List<Data> convertToTopadsDomain(List<HomeFeedQuery.Data.Topad> topads) {
        List<Data> list = new ArrayList<>();
        if(topads !=null){
            for (HomeFeedQuery.Data.Topad topad : topads){
                try {
                    list.add(new Data(new JSONObject(new Gson().toJson(topad, HomeFeedQuery.Data.Topad.class))));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    private List<InspirationDomain> convertToInspirationDomain(List<HomeFeedQuery.Data.Inspirasi> inspirasi) {
        List<InspirationDomain> listInspiration = new ArrayList<>();
        if (inspirasi != null) {
            for (HomeFeedQuery.Data.Inspirasi inspiration : inspirasi) {
                listInspiration.add(new InspirationDomain(
                        inspiration.experiment_version(),
                        inspiration.source(),
                        inspiration.title(),
                        inspiration.foreign_title(),
                        inspiration.widget_url(),
                        convertToInspirationItemDomainList(inspiration.recommendation())
                ));
            }
        }
        return listInspiration;
    }

    private List<InspirationItemDomain> convertToInspirationItemDomainList(List<HomeFeedQuery.Data
            .Recommendation> recommendations) {
        List<InspirationItemDomain> listItemInspiration = new ArrayList<>();
        if (recommendations != null) {
            for (HomeFeedQuery.Data.Recommendation recommendation : recommendations) {
                listItemInspiration.add(new InspirationItemDomain(
                        recommendation.id(),
                        recommendation.name(),
                        recommendation.url().toString(),
                        recommendation.click_url(),
                        recommendation.app_url(),
                        recommendation.image_url().toString(),
                        recommendation.price(),
                        recommendation.recommendation_type(),
                        recommendation.price()
                ));
            }
        }
        return listItemInspiration;
    }

    private ContentFeedDomain
    createContentFeedDomain(HomeFeedQuery.Data.Content content,
                            List<InspirationDomain> inspirationDomains,
                            List<Data> topAdsList) {
        if (content == null) return null;
        return new ContentFeedDomain(content.type(),
                0,
                null,
                null,
                null,
                null,
                inspirationDomains,
                topAdsList,
                null,
                null,
                null,
                null,
                "");
    }

    private SourceFeedDomain createSourceFeedDomain(
            HomeFeedQuery.Data.Source source) {
        if (source == null) return null;
        return new SourceFeedDomain(source.type(), null);
    }

    private DataFeedDomain createDataFeedDomain(HomeFeedQuery.Data.Datum datum,
                                                ContentFeedDomain contentFeedDomain,
                                                SourceFeedDomain sourceFeedDomain) {
        return new DataFeedDomain(datum.id(), datum.create_time(), datum.type(), datum.cursor(),
                sourceFeedDomain, contentFeedDomain);
    }
}