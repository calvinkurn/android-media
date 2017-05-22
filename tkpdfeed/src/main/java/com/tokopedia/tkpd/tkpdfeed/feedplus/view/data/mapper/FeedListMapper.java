package com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper;

import com.tkpdfeed.feeds.Feeds;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.ContentFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.ProductFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.PromotionFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.ShopFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.SourceFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.WholesaleDomain;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * @author ricoharisin .
 */

public class FeedListMapper implements Func1<Feeds.Data, List<DataFeedDomain>> {
    @Override
    public List<DataFeedDomain> call(Feeds.Data data) {
        return convertToDataFeedDomain(data.feed().data());
    }

    private ProductFeedDomain createProductFeedDomain(Feeds.Data.Product product, List<WholesaleDomain> wholesaleDomains) {
        return new ProductFeedDomain(product.id(), product.name(), product.price(),
                product.image(), product.image_single(), wholesaleDomains, product.freereturns(),
                product.preorder(), product.cashback(), product.url(), product.productLink(), product.wishlist(),
                product.rating());
    }

    private PromotionFeedDomain createPromotionFeedDomain(Feeds.Data.Promotion promotion) {
        return new PromotionFeedDomain(promotion.id(), promotion.name(), promotion.type(),
                promotion.thumbnail(), promotion.feature_image(), promotion.description(),
                promotion.periode(), promotion.code(), promotion.url(), promotion.min_transcation());
    }

    private WholesaleDomain createWholesaleDomain(Feeds.Data.Wholesale wholesale) {
        return new WholesaleDomain(wholesale.qty_min_fmt());
    }

    private List<WholesaleDomain> convertToWholesaleDomain(List<Feeds.Data.Wholesale> wholesales) {
        List<WholesaleDomain> wholesaleDomains = new ArrayList<>();
        for (int i  = 0; i < wholesales.size(); i++) {
            wholesaleDomains.add(createWholesaleDomain(wholesales.get(i)));
        }

        return wholesaleDomains;
    }

    private List<ProductFeedDomain> convertToProductFeedDomain(List<Feeds.Data.Product> products) {
        List<ProductFeedDomain> productFeedDomains = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            Feeds.Data.Product product = products.get(i);

            List<WholesaleDomain> wholesaleDomains = convertToWholesaleDomain(product.wholesale());

            productFeedDomains.add(createProductFeedDomain(product, wholesaleDomains));
        }

        return productFeedDomains;
    }

    private List<PromotionFeedDomain> convertToPromotionFeedDomain(List<Feeds.Data.Promotion> promotions) {
        List<PromotionFeedDomain> promotionFeedDomains = new ArrayList<>();
        for (int i = 0; i < promotions.size(); i++) {
            promotionFeedDomains.add(createPromotionFeedDomain(promotions.get(i)));
        }

        return  promotionFeedDomains;
    }

    private ShopFeedDomain createShopFeedDomain(Feeds.Data.Shop shop) {
        return new ShopFeedDomain(shop.id(), shop.name(), shop.avatar(), shop.isOfficial(),
                shop.isGold(), shop.url(), shop.shopLink());
    }

    private ContentFeedDomain createContentFeedDomain(Feeds.Data.Content content,
                                                      List<ProductFeedDomain> productFeedDomains,
                                                      List<PromotionFeedDomain> promotionFeedDomains) {
        return new ContentFeedDomain(content.type(),
                content.total_product(), productFeedDomains, promotionFeedDomains, content.status_activity());
    }

    private  SourceFeedDomain createSourceFeedDomain(Feeds.Data.Source source, ShopFeedDomain shopFeedDomain) {
        return new SourceFeedDomain(source.type(), shopFeedDomain);
    }

    private List<DataFeedDomain> convertToDataFeedDomain(List<Feeds.Data.Datum> datumList) {
        List<DataFeedDomain> dataFeedDomains = new ArrayList<>();
        for (int i = 0 ; i < datumList.size(); i++) {
            Feeds.Data.Datum datum = datumList.get(i);
            List<ProductFeedDomain> productFeedDomains = convertToProductFeedDomain(datum.content().products());
            List<PromotionFeedDomain> promotionFeedDomains = convertToPromotionFeedDomain(datum.content().promotions());
            ShopFeedDomain shopFeedDomain = createShopFeedDomain(datum.source().shop());
            ContentFeedDomain contentFeedDomain = createContentFeedDomain(datum.content(), productFeedDomains, promotionFeedDomains);
            SourceFeedDomain sourceFeedDomain = createSourceFeedDomain(datum.source(), shopFeedDomain);

            dataFeedDomains.add(createDataFeedDomain(datum, contentFeedDomain, sourceFeedDomain));
        }

        return dataFeedDomains;
    }

    private DataFeedDomain createDataFeedDomain(Feeds.Data.Datum datum,
                                                ContentFeedDomain contentFeedDomain,
                                                SourceFeedDomain sourceFeedDomain) {
        return new DataFeedDomain(datum.id(), datum.create_time(), datum.type(), datum.cursor(),
                sourceFeedDomain, contentFeedDomain);
    }


}
