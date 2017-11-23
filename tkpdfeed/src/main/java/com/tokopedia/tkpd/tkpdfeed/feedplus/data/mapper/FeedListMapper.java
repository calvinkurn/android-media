package com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper;

import com.tkpdfeed.feeds.Feeds;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.TopPicksDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.ContentFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.inspiration.DataInspirationDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.ProductFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.PromotionFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.ShopFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.SourceFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.WholesaleDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.inspiration.InspirationPaginationDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.inspiration.InspirationRecommendationDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.BadgeDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.DataDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.LabelDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.OfficialStoreDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.OfficialStoreProductDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.ShopDomain;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * @author ricoharisin .
 */

public class FeedListMapper implements Func1<Feeds.Data, FeedDomain> {
    @Override
    public FeedDomain call(Feeds.Data data) {
        return convertToDataFeedDomain(data);
    }

    private ProductFeedDomain createProductFeedDomain(String cursor,
                                                      Feeds.Data.Product product,
                                                      List<WholesaleDomain> wholesaleDomains) {
        if (product == null) return null;
        return new ProductFeedDomain(product.id(), product.name(), product.price(),
                product.image(), product.image_single(), wholesaleDomains, product.freereturns(),
                product.preorder(), product.cashback(), (String) product.url(),
                product.productLink(), product.wishlist(),
                product.rating(), cursor);
    }

    private PromotionFeedDomain createPromotionFeedDomain(Feeds.Data.Promotion promotion) {
        if (promotion == null) return null;
        return new PromotionFeedDomain(promotion.id(), promotion.name(), promotion.type(),
                promotion.thumbnail(), promotion.feature_image(), promotion.description(),
                promotion.periode(), promotion.code(), promotion.url().toString(), promotion
                .min_transcation());
    }

    private WholesaleDomain createWholesaleDomain(Feeds.Data.Wholesale wholesale) {
        return new WholesaleDomain(wholesale.qty_min_fmt());
    }

    private List<WholesaleDomain> convertToWholesaleDomain(List<Feeds.Data.Wholesale> wholesales) {
        List<WholesaleDomain> wholesaleDomains = new ArrayList<>();
        if (wholesales != null) {
            for (int i = 0; i < wholesales.size(); i++) {
                wholesaleDomains.add(createWholesaleDomain(wholesales.get(i)));
            }
        }

        return wholesaleDomains;
    }

    private List<ProductFeedDomain>
    convertToProductFeedDomain(String cursor,
                               List<Feeds.Data.Product> products) {
        List<ProductFeedDomain> productFeedDomains = new ArrayList<>();
        if (products != null) {
            for (int i = 0; i < products.size(); i++) {

                Feeds.Data.Product product = products.get(i);

                List<WholesaleDomain> wholesaleDomains = convertToWholesaleDomain(product.wholesale());

                productFeedDomains.add(createProductFeedDomain(cursor, product, wholesaleDomains));
            }
        }

        return productFeedDomains;
    }

    private List<PromotionFeedDomain>
    convertToPromotionFeedDomain(List<Feeds.Data.Promotion> promotions) {
        List<PromotionFeedDomain> promotionFeedDomains = new ArrayList<>();
        if (promotions != null) {
            for (int i = 0; i < promotions.size(); i++) {
                promotionFeedDomains.add(createPromotionFeedDomain(promotions.get(i)));
            }
        }

        return promotionFeedDomains;
    }

    private ShopFeedDomain createShopFeedDomain(Feeds.Data.Shop shop) {
        if (shop == null) return null;
        return new ShopFeedDomain(shop.id(), shop.name(), shop.avatar(), shop.isOfficial(),
                shop.isGold(), (String) shop.url(), shop.shopLink(), shop.shareLinkDescription(),
                shop.shareLinkURL());
    }

    private ContentFeedDomain
    createContentFeedDomain(Feeds.Data.Content content,
                            List<ProductFeedDomain> productFeedDomains,
                            List<PromotionFeedDomain> promotionFeedDomains,
                            List<OfficialStoreDomain> officialStoreDomains,
                            List<TopPicksDomain> topPicksDomains) {
        if (content == null) return null;
        return new ContentFeedDomain(content.type(),
                content.total_product() != null ? content.total_product() : 0,
                productFeedDomains,
                promotionFeedDomains,
                officialStoreDomains,
                topPicksDomains,
                content.status_activity());
    }

    private SourceFeedDomain createSourceFeedDomain(
            Feeds.Data.Source source, ShopFeedDomain shopFeedDomain) {
        if (source == null) return null;
        return new SourceFeedDomain(source.type(), shopFeedDomain);
    }

    private FeedDomain convertToDataFeedDomain(Feeds.Data data) {

        return new FeedDomain(convertToFeedDomain(data),
                convertToInspiration(data),
                data.feed().links().pagination().has_next_page());
    }

    private List<DataInspirationDomain> convertToInspiration(Feeds.Data data) {
        List<Feeds.Data.Datum1> datumList = data.inspiration().data();
        List<DataInspirationDomain> dataInspirationDomains = new ArrayList<>();
        if (datumList != null) {
            for (int i = 0; i < datumList.size(); i++) {
                Feeds.Data.Datum1 datum = datumList.get(i);

                dataInspirationDomains.add(
                        createDataInspirationDomain(
                                datum.source(),
                                datum.title(),
                                datum.foreign_title(),
                                convertToInspirationPagination(datum.pagination()),
                                convertToInspirationRecommendation(datum.recommendation())));
            }
        }
        return dataInspirationDomains;
    }

    private List<InspirationRecommendationDomain>
    convertToInspirationRecommendation(List<Feeds.Data.Recommendation> recommendations) {
        List<InspirationRecommendationDomain> listRecommendation = new ArrayList<>();
        if (recommendations != null) {
            for (Feeds.Data.Recommendation recommendation : recommendations) {
                listRecommendation.add(new InspirationRecommendationDomain(
                        recommendation.id(),
                        recommendation.name(),
                        String.valueOf(recommendation.url()),
                        String.valueOf(recommendation.image_url()),
                        recommendation.price()));
            }
        }
        return listRecommendation;
    }

    private InspirationPaginationDomain convertToInspirationPagination(
            Feeds.Data.Pagination1 pagination) {
        return new InspirationPaginationDomain(
                pagination.current_page(),
                pagination.next_page(),
                pagination.prev_page());
    }

    private DataInspirationDomain
    createDataInspirationDomain(String source,
                                String title,
                                String foreign_title,
                                InspirationPaginationDomain pagination,
                                List<InspirationRecommendationDomain> recommendation) {
        return new DataInspirationDomain(source,
                title,
                foreign_title,
                pagination,
                recommendation);
    }

    private List<OfficialStoreDomain> convertToOfficialStoresFeedDomain(
            List<Feeds.Data.Official_store> official_stores) {
        List<OfficialStoreDomain> listStores = new ArrayList<>();
        if (official_stores != null) {
            for (Feeds.Data.Official_store officialStore : official_stores) {
                listStores.add(new OfficialStoreDomain(
                        officialStore.shop_id() != null ? officialStore.shop_id() : 0,
                        officialStore.shop_apps_url() != null ? officialStore.shop_apps_url() : "",
                        officialStore.shop_name() != null ? officialStore.shop_name() : "",
                        officialStore.logo_url() != null ? officialStore.logo_url() : "",
                        officialStore.microsite_url() != null ? officialStore.microsite_url() : "",
                        officialStore.brand_img_url() != null ? officialStore.brand_img_url() : "",
                        officialStore.is_owner() != null ? officialStore.is_owner() : false,
                        officialStore.shop_tagline() != null ? officialStore.shop_tagline() : "",
                        officialStore.is_new() != null ? officialStore.is_new() : false,
                        officialStore.title() != null ? officialStore.title() : "",
                        officialStore.mobile_img_url() != null ? officialStore.mobile_img_url() : "",
                        officialStore.feed_hexa_color() != null ? officialStore.feed_hexa_color() : "",
                        officialStore.redirect_url_app() != null ? officialStore
                                .redirect_url_app() : "",
                        convertToOfficialStoreProducts(officialStore.products())
                ));
            }
        }
        return listStores;
    }

    private List<OfficialStoreProductDomain> convertToOfficialStoreProducts(
            List<Feeds.Data.Product1> products) {
        List<OfficialStoreProductDomain> listProduct = new ArrayList<>();
        if (products != null) {
            for (Feeds.Data.Product1 product : products) {
                listProduct.add(new OfficialStoreProductDomain(
                        product.brand_id(),
                        product.brand_logo(),
                        convertToOfficialStoreProductData(product.data())
                ));
            }
        }
        return listProduct;
    }

    private DataDomain convertToOfficialStoreProductData(Feeds.Data.Data1 data) {
        return new DataDomain(data.id(),
                data.name(),
                data.url_app(),
                data.image_url(),
                data.image_url_700(),
                data.price(),
                convertToOfficialStoreProductShopDomain(data.shop()),
                data.original_price(),
                data.discount_percentage(),
                data.discount_expired(),
                convertToOfficialStoreProductBadge(data.badges()),
                convertToOfficialStoreProductLabel(data.labels()));
    }

    private List<LabelDomain> convertToOfficialStoreProductLabel(List<Feeds.Data.Label> labels) {
        List<LabelDomain> listLabel = new ArrayList<>();
        if (labels != null) {
            for (Feeds.Data.Label label : labels) {
                listLabel.add(new LabelDomain(
                        label.title(),
                        label.color()
                ));
            }
        }
        return listLabel;
    }

    private List<BadgeDomain> convertToOfficialStoreProductBadge(List<Feeds.Data.Badge> badges) {
        List<BadgeDomain> listBadge = new ArrayList<>();
        if (badges != null) {
            for (Feeds.Data.Badge badge : badges) {
                listBadge.add(new BadgeDomain(
                        badge.title(),
                        (String) badge.image_url()));
            }
        }
        return listBadge;
    }

    private ShopDomain convertToOfficialStoreProductShopDomain(Feeds.Data.Shop1 shop) {
        return new ShopDomain(shop.name(),
                shop.url(),
                shop.url_app(), shop.location());
    }


    private List<DataFeedDomain> convertToFeedDomain(Feeds.Data data) {
        List<Feeds.Data.Datum> datumList = data.feed().data();
        List<DataFeedDomain> dataFeedDomains = new ArrayList<>();
        if (datumList != null) {
            for (int i = 0; i < datumList.size(); i++) {
                Feeds.Data.Datum datum = datumList.get(i);
                List<ProductFeedDomain> productFeedDomains = convertToProductFeedDomain(datum
                        .cursor(), datum
                        .content().products());
                List<PromotionFeedDomain> promotionFeedDomains =
                        convertToPromotionFeedDomain(datum.content().promotions());
                List<OfficialStoreDomain> officialStoreDomains =
                        convertToOfficialStoresFeedDomain(datum.content().official_store());
                List<TopPicksDomain> topPicksDomains =
                        convertToTopPicksDomain(datum.content().top_picks());
                ShopFeedDomain shopFeedDomain = createShopFeedDomain(datum.source().shop());
                ContentFeedDomain contentFeedDomain = createContentFeedDomain(
                        datum.content(),
                        productFeedDomains,
                        promotionFeedDomains,
                        officialStoreDomains,
                        topPicksDomains
                );
                SourceFeedDomain sourceFeedDomain =
                        createSourceFeedDomain(datum.source(), shopFeedDomain);

                dataFeedDomains.add(createDataFeedDomain(datum,
                        contentFeedDomain, sourceFeedDomain));
            }
        }
        return dataFeedDomains;
    }

    private List<TopPicksDomain> convertToTopPicksDomain(List<Feeds.Data.Top_pick> top_picks) {
        List<TopPicksDomain> listToppicks = new ArrayList<>();
        if (top_picks != null) {
            for (Feeds.Data.Top_pick topPick : top_picks) {
                listToppicks.add(new TopPicksDomain(
                                topPick.name(),
                                topPick.url(),
                                topPick.image_url(),
                                topPick.image_landscape_url(),
                                topPick.is_parent()
                        )
                );
            }
        }
        return listToppicks;
    }

    private DataFeedDomain createDataFeedDomain(Feeds.Data.Datum datum,
                                                ContentFeedDomain contentFeedDomain,
                                                SourceFeedDomain sourceFeedDomain) {
        return new DataFeedDomain(datum.id(), datum.create_time(), datum.type(), datum.cursor(),
                sourceFeedDomain, contentFeedDomain);
    }


}
