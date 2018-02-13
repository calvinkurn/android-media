package com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper;

import com.tkpdfeed.feeds.FeedQuery;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.InspirationItemDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.TopPicksDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.ContentFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FavoriteCtaDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.InspirationDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.KolCtaDomain;
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
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads.Badge;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads.Category;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads.Data;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads.ImageProduct;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads.ImageShop;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads.Product;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads.ProductImage;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads.Shop;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads.WholesalePrice;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

import static com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads.Data.DISPLAY_PRODUCT;

/**
 * @author ricoharisin .
 */

public class FeedListMapper implements Func1<FeedQuery.Data, FeedDomain> {
    @Override
    public FeedDomain call(FeedQuery.Data data) {
        return convertToDataFeedDomain(data);
    }

    private ProductFeedDomain createProductFeedDomain(String cursor,
                                                      FeedQuery.Data.Product product,
                                                      List<WholesaleDomain> wholesaleDomains) {
        if (product == null) return null;
        return new ProductFeedDomain(product.id(), product.name(), product.price(),
                product.image(), product.image_single(), wholesaleDomains, product.freereturns(),
                product.preorder(), product.cashback(), (String) product.url(),
                product.productLink(), product.wishlist(),
                product.rating(), String.valueOf(product.price_int()), cursor);
    }

    private PromotionFeedDomain createPromotionFeedDomain(FeedQuery.Data.Promotion promotion) {
        if (promotion == null) return null;
        return new PromotionFeedDomain(promotion.id(), promotion.name(), promotion.type(),
                promotion.thumbnail(), promotion.feature_image(), promotion.description(),
                promotion.periode(), promotion.code(), promotion.url().toString(), promotion
                .min_transcation());
    }

    private WholesaleDomain createWholesaleDomain(FeedQuery.Data.Wholesale wholesale) {
        return new WholesaleDomain(wholesale.qty_min_fmt());
    }

    private List<WholesaleDomain> convertToWholesaleDomain(List<FeedQuery.Data.Wholesale> wholesales) {
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
                               List<FeedQuery.Data.Product> products) {
        List<ProductFeedDomain> productFeedDomains = new ArrayList<>();
        if (products != null) {
            for (int i = 0; i < products.size(); i++) {

                FeedQuery.Data.Product product = products.get(i);

                List<WholesaleDomain> wholesaleDomains = convertToWholesaleDomain(product.wholesale());

                productFeedDomains.add(createProductFeedDomain(cursor, product, wholesaleDomains));
            }
        }

        return productFeedDomains;
    }

    private List<PromotionFeedDomain>
    convertToPromotionFeedDomain(List<FeedQuery.Data.Promotion> promotions) {
        List<PromotionFeedDomain> promotionFeedDomains = new ArrayList<>();
        if (promotions != null) {
            for (int i = 0; i < promotions.size(); i++) {
                promotionFeedDomains.add(createPromotionFeedDomain(promotions.get(i)));
            }
        }

        return promotionFeedDomains;
    }

    private ShopFeedDomain createShopFeedDomain(FeedQuery.Data.Shop shop) {
        if (shop == null) return null;
        return new ShopFeedDomain(shop.id(), shop.name(), shop.avatar(), shop.isOfficial(),
                shop.isGold(), (String) shop.url(), shop.shopLink(), shop.shareLinkDescription(),
                shop.shareLinkURL());
    }

    private ContentFeedDomain
    createContentFeedDomain(FeedQuery.Data.Content content,
                            List<ProductFeedDomain> productFeedDomains,
                            List<PromotionFeedDomain> promotionFeedDomains,
                            List<OfficialStoreDomain> officialStoreDomains,
                            List<TopPicksDomain> topPicksDomains,
                            List<InspirationDomain> inspirationDomains,
                            KolPostDomain kolPostDomain,
                            KolRecommendationDomain kolRecommendations,
                            FavoriteCtaDomain favoriteCtaDomain,
                            KolCtaDomain kolCtaDomain,
                            List<Data> topadsData) {
        if (content == null) return null;
        return new ContentFeedDomain(content.type(),
                content.total_product() != null ? content.total_product() : 0,
                productFeedDomains,
                promotionFeedDomains,
                officialStoreDomains,
                topPicksDomains,
                inspirationDomains,
                kolPostDomain,
                kolRecommendations,
                favoriteCtaDomain,
                kolCtaDomain,
                content.status_activity(),
                topadsData);
    }

    private SourceFeedDomain createSourceFeedDomain(
            FeedQuery.Data.Source source, ShopFeedDomain shopFeedDomain) {
        if (source == null) return null;
        return new SourceFeedDomain(source.type(), shopFeedDomain);
    }

    private FeedDomain convertToDataFeedDomain(FeedQuery.Data data) {

        return new FeedDomain(convertToFeedDomain(data),
                data.feed().links().pagination().has_next_page());
    }

    private List<OfficialStoreDomain> convertToOfficialStoresFeedDomain(
            List<FeedQuery.Data.Official_store> official_stores) {
        List<OfficialStoreDomain> listStores = new ArrayList<>();
        if (official_stores != null
                && official_stores.size() == 1
                && official_stores.get(0).products().size() == 4) {
            for (FeedQuery.Data.Official_store officialStore : official_stores) {
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
            List<FeedQuery.Data.Product1> products) {
        List<OfficialStoreProductDomain> listProduct = new ArrayList<>();
        if (products != null) {
            for (FeedQuery.Data.Product1 product : products) {
                listProduct.add(new OfficialStoreProductDomain(
                        product.brand_id(),
                        product.brand_logo(),
                        convertToOfficialStoreProductData(product.data())
                ));
            }
        }
        return listProduct;
    }

    private DataDomain convertToOfficialStoreProductData(FeedQuery.Data.Data1 data) {
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

    private List<LabelDomain> convertToOfficialStoreProductLabel(List<FeedQuery.Data.Label> labels) {
        List<LabelDomain> listLabel = new ArrayList<>();
        if (labels != null) {
            for (FeedQuery.Data.Label label : labels) {
                listLabel.add(new LabelDomain(
                        label.title(),
                        label.color()
                ));
            }
        }
        return listLabel;
    }

    private List<BadgeDomain> convertToOfficialStoreProductBadge(List<FeedQuery.Data.Badge> badges) {
        List<BadgeDomain> listBadge = new ArrayList<>();
        if (badges != null) {
            for (FeedQuery.Data.Badge badge : badges) {
                listBadge.add(new BadgeDomain(
                        badge.title(),
                        (String) badge.image_url()));
            }
        }
        return listBadge;
    }

    private ShopDomain convertToOfficialStoreProductShopDomain(FeedQuery.Data.Shop1 shop) {
        return new ShopDomain(shop.name(),
                shop.url_app(),
                shop.location()
        );
    }


    private List<DataFeedDomain> convertToFeedDomain(FeedQuery.Data data) {
        List<FeedQuery.Data.Datum> datumList = data.feed().data();
        List<DataFeedDomain> dataFeedDomains = new ArrayList<>();
        if (datumList != null) {
            for (int i = 0; i < datumList.size(); i++) {
                FeedQuery.Data.Datum datum = datumList.get(i);
                List<ProductFeedDomain> productFeedDomains = convertToProductFeedDomain(datum
                        .cursor(), datum
                        .content().products());
                List<PromotionFeedDomain> promotionFeedDomains =
                        convertToPromotionFeedDomain(datum.content().promotions());
                List<OfficialStoreDomain> officialStoreDomains =
                        convertToOfficialStoresFeedDomain(datum.content().official_store());
                List<TopPicksDomain> topPicksDomains =
                        convertToTopPicksDomain(datum.content().top_picks());
                List<InspirationDomain> inspirationDomains = convertToInspirationDomain(datum
                        .content().inspirasi());
                ShopFeedDomain shopFeedDomain = createShopFeedDomain(datum.source().shop());
                KolPostDomain kolPostDomain = createKolPostDomain(datum);
                KolRecommendationDomain kolRecommendations
                        = convertToKolRecommendationDomain(datum.content().kolrecommendation());
                FavoriteCtaDomain favoriteCta
                        = convertToFavoriteCtaDomain(datum.content().favorite_cta());
                KolCtaDomain kolCtaDomain = datum.content().kol_cta() != null ?
                        convertToKolCtaDomain(datum.content().kol_cta()) :
                        null;
                List<Data> topadsData = convertToTopadsDomain(datum.content().display(),
                        datum.content().topads());
                ContentFeedDomain contentFeedDomain = createContentFeedDomain(
                        datum.content(),
                        productFeedDomains,
                        promotionFeedDomains,
                        officialStoreDomains,
                        topPicksDomains,
                        inspirationDomains,
                        kolPostDomain,
                        kolRecommendations,
                        favoriteCta,
                        kolCtaDomain,
                        topadsData
                );
                SourceFeedDomain sourceFeedDomain =
                        createSourceFeedDomain(datum.source(), shopFeedDomain);

                dataFeedDomains.add(createDataFeedDomain(datum,
                        contentFeedDomain, sourceFeedDomain));
            }
        }
        return dataFeedDomains;
    }

    private KolRecommendationDomain convertToKolRecommendationDomain(FeedQuery.Data.Kolrecommendation kolrecommendation) {
        if (kolrecommendation == null) {
            return null;
        } else {
            KolRecommendationDomain domain = new KolRecommendationDomain(kolrecommendation
                    .headerTitle() == null ? "" : kolrecommendation.headerTitle(),
                    kolrecommendation.exploreLink() == null ? "" : kolrecommendation.exploreLink(),
                    convertToListKolRecommendation(kolrecommendation.kols()));
            return domain;
        }

    }

    private FavoriteCtaDomain convertToFavoriteCtaDomain(FeedQuery.Data.Favorite_cta favoriteCta) {
        if (favoriteCta == null) {
            return null;
        } else {
            FavoriteCtaDomain domain = new FavoriteCtaDomain(favoriteCta
                    .title_id() == null ? "" : favoriteCta.title_id(),
                    favoriteCta.subtitle_id() == null ? "" : favoriteCta.subtitle_id());
            return domain;
        }
    }

    private List<KolRecommendationItemDomain> convertToListKolRecommendation(List<FeedQuery.Data.Kol> kolrecommendation) {
        List<KolRecommendationItemDomain> list = new ArrayList<>();
        if (kolrecommendation != null) {
            for (FeedQuery.Data.Kol recommendation : kolrecommendation) {
                list.add(new KolRecommendationItemDomain(
                        recommendation.userName() == null ? "" : recommendation.userName(),
                        recommendation.userId() == null ? 0 : recommendation.userId(),
                        recommendation.userPhoto() == null ? "" : recommendation.userPhoto(),
                        recommendation.isFollowed() == null ? false : recommendation.isFollowed(),
                        recommendation.info() == null ? "" : recommendation.info(),
                        recommendation.url() == null ? "" : recommendation.url()
                ));
            }
        }
        return list;
    }

    private KolPostDomain createKolPostDomain(FeedQuery.Data.Datum datum) {
        if (datum.content().kolpost() != null) {
            FeedQuery.Data.Kolpost kolpost = datum.content()
                    .kolpost();
            FeedQuery.Data.Content1 content = datum.content()
                    .kolpost().content().get(0);
            return new KolPostDomain(
                    kolpost.id() == null ? 0 : kolpost.id(),
                    content.imageurl() == null ? "" : content.imageurl(),
                    kolpost.description() == null ? "" : kolpost.description(),
                    kolpost.commentCount() == null ? 0 : kolpost.commentCount(),
                    kolpost.likeCount() == null ? 0 : kolpost.likeCount(),
                    kolpost.isLiked() == null ? false : kolpost.isLiked(),
                    kolpost.isFollowed() == null ? false : kolpost.isFollowed(),
                    kolpost.createTime() == null ? "" : kolpost.createTime(),
                    content.tags().get(0).price() == null ? "" : content.tags().get(0).price(),
                    content.tags().get(0).link() == null ? "" : content.tags().get(0).link(),
                    content.tags().get(0).url() == null ? "" : content.tags().get(0).url(),
                    kolpost.userName() == null ? "" : kolpost.userName(),
                    kolpost.userPhoto() == null ? "" : kolpost.userPhoto(),
                    content.tags().get(0).type() == null ? "" : content.tags().get(0).type(),
                    content.tags().get(0).caption() == null ? "" : content.tags().get(0).caption(),
                    content.tags().get(0).id() == null ? 0 : content.tags().get(0).id(),
                    kolpost.userInfo() == null ? "" : kolpost.userInfo(),
                    kolpost.headerTitle() == null ? "" : kolpost.headerTitle(),
                    kolpost.userUrl() == null ? "" : kolpost.userUrl(),
                    kolpost.userId() == null ? 0 : kolpost.userId(),
                    kolpost.showComment(),
                    datum.content().type() == null ? "" : datum.content().type());
        } else if (datum.content().followedkolpost() != null) {
            FeedQuery.Data.Followedkolpost kolpost = datum.content()
                    .followedkolpost();
            FeedQuery.Data.Content2 content = datum.content()
                    .followedkolpost().content().get(0);
            return new KolPostDomain(
                    kolpost.id() == null ? 0 : kolpost.id(),
                    content.imageurl() == null ? "" : content.imageurl(),
                    kolpost.description() == null ? "" : kolpost.description(),
                    kolpost.commentCount() == null ? 0 : kolpost.commentCount(),
                    kolpost.likeCount() == null ? 0 : kolpost.likeCount(),
                    kolpost.isLiked() == null ? false : kolpost.isLiked(),
                    kolpost.isFollowed() == null ? false : kolpost.isFollowed(),
                    kolpost.createTime() == null ? "" : kolpost.createTime(),
                    content.tags().get(0).price() == null ? "" : content.tags().get(0).price(),
                    content.tags().get(0).link() == null ? "" : content.tags().get(0).link(),
                    content.tags().get(0).url() == null ? "" : content.tags().get(0).url(),
                    kolpost.userName() == null ? "" : kolpost.userName(),
                    kolpost.userPhoto() == null ? "" : kolpost.userPhoto(),
                    content.tags().get(0).type() == null ? "" : content.tags().get(0).type(),
                    content.tags().get(0).caption() == null ? "" : content.tags().get(0).caption(),
                    content.tags().get(0).id() == null ? 0 : content.tags().get(0).id(),
                    kolpost.userInfo() == null ? "" : kolpost.userInfo(),
                    "",
                    kolpost.userUrl() == null ? "" : kolpost.userUrl(),
                    kolpost.userId() == null ? 0 : kolpost.userId(),
                    kolpost.showComment(),
                    datum.content().type() == null ? "" : datum.content().type());
        } else {
            return null;
        }
    }

    private List<InspirationDomain> convertToInspirationDomain(List<FeedQuery.Data.Inspirasi> inspirasi) {
        List<InspirationDomain> listInspiration = new ArrayList<>();
        if (inspirasi != null) {
            for (FeedQuery.Data.Inspirasi inspiration : inspirasi) {
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

    private List<InspirationItemDomain> convertToInspirationItemDomainList(List<FeedQuery.Data
            .Recommendation> recommendations) {
        List<InspirationItemDomain> listItemInspiration = new ArrayList<>();
        if (recommendations != null) {
            for (FeedQuery.Data.Recommendation recommendation : recommendations) {
                listItemInspiration.add(new InspirationItemDomain(
                        recommendation.id(),
                        recommendation.name(),
                        recommendation.url().toString(),
                        recommendation.click_url(),
                        recommendation.app_url(),
                        recommendation.image_url().toString(),
                        recommendation.price(),
                        recommendation.recommendation_type(),
                        String.valueOf(recommendation.price_int())
                ));
            }
        }
        return listItemInspiration;
    }

    private List<TopPicksDomain> convertToTopPicksDomain(List<FeedQuery.Data.Top_pick> top_picks) {
        List<TopPicksDomain> listToppicks = new ArrayList<>();
        if (top_picks != null) {
            for (FeedQuery.Data.Top_pick topPick : top_picks) {
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

    private KolCtaDomain convertToKolCtaDomain(FeedQuery.Data.Kol_cta kol_cta) {
        return new KolCtaDomain(
                kol_cta.img_header(),
                kol_cta.click_applink(),
                kol_cta.button_text(),
                kol_cta.title(),
                kol_cta.subtitle());
    }

    private List<Data> convertToTopadsDomain(String display, List<FeedQuery.Data.Topad> topadList) {
        ArrayList<Data> topadDataDomainList = new ArrayList<>();
        if (topadList != null && display != null && !display.isEmpty()) {

            for (FeedQuery.Data.Topad topad : topadList) {
                topadDataDomainList.add(
                        new Data(topad.id(),
                                topad.ad_ref_key(),
                                topad.redirect() == null ? "" :
                                        topad.redirect().toString(),
                                topad.sticker_id(),
                                topad.sticker_image() == null ? "" :
                                        topad.sticker_image().toString(),
                                topad.product_click_url() == null ? "" :
                                        topad.product_click_url().toString(),
                                topad.shop_click_url() == null ? "" :
                                        topad.shop_click_url().toString(),
                                topad.shop() == null ? null :
                                        convertToTopadsShop(topad.shop()),
                                topad.product() == null ? null :
                                        convertToTopadsProduct(topad.product()),
                                false,
                                display)
                );
            }
        }
        return topadDataDomainList;
    }

    private Shop convertToTopadsShop(FeedQuery.Data.Shop2 topadShop) {
        return new Shop(topadShop.id(),
                topadShop.name(),
                topadShop.domain(),
                topadShop.tagline(),
                topadShop.location(),
                topadShop.city(),
                topadShop.image_shop() == null ? null :
                        convertToTopadsImageShop(topadShop.image_shop()),
                topadShop.gold_shop() == null ? false :
                        topadShop.gold_shop(),
                topadShop.gold_shop_badge() == null ? false :
                        topadShop.gold_shop_badge(),
                topadShop.lucky_shop() == null ? "" :
                        topadShop.lucky_shop().toString(),
                topadShop.shop_is_official() == null ? false :
                        topadShop.shop_is_official(),
                topadShop.uri() == null ? "" :
                        topadShop.uri().toString(),
                topadShop.image_product() == null ? null :
                        convertToTopadsImageProduct(topadShop.image_product()),
                topadShop.owner_id(),
                topadShop.is_owner() == null ? false : topadShop.is_owner(),
                topadShop.badges() == null ? null :
                        convertToTopadsBadge(topadShop.badges()));
    }

    private ImageShop convertToTopadsImageShop(FeedQuery.Data.Image_shop imageShop) {
        return new ImageShop(imageShop.cover_ecs().toString(),
                imageShop.s_url().toString(),
                imageShop.xs_url().toString(),
                imageShop.cover_ecs().toString(),
                imageShop.s_ecs().toString(),
                imageShop.xs_ecs().toString());
    }

    private List<ImageProduct> convertToTopadsImageProduct(List<FeedQuery.Data.Image_product>
                                                                   imageProductList) {
        ArrayList<ImageProduct> imageProductDomainList = new ArrayList<>();
        if (imageProductList != null) {
            for (FeedQuery.Data.Image_product imageProduct : imageProductList) {
                imageProductDomainList.add(
                        new ImageProduct(imageProduct.product_id(),
                                imageProduct.product_name(),
                                imageProduct.image_url().toString())
                );
            }
        }
        return imageProductDomainList;
    }

    private List<Badge> convertToTopadsBadge(List<FeedQuery.Data.Badge1> badgeList) {
        ArrayList<Badge> badgeDomainList = new ArrayList<>();
        if (badgeList != null) {
            for (FeedQuery.Data.Badge1 badge : badgeList) {
                badgeDomainList.add(
                        new Badge(String.valueOf(badge.title()),
                                badge.image_url() == null ? "" : badge.image_url().toString())
                );
            }
        }
        return badgeDomainList;
    }

    private Product convertToTopadsProduct(FeedQuery.Data.Product2 topadProduct) {
        return new Product(topadProduct.id(),
                topadProduct.name(),
                topadProduct.image() == null ? null :
                        convertToTopadsProductImage(topadProduct.image()),
                topadProduct.uri() == null ? "" : topadProduct.uri().toString(),
                topadProduct.relative_uri(),
                topadProduct.price_format(),
                topadProduct.count_talk_format(),
                topadProduct.count_review_format(),
                topadProduct.category() == null ? null :
                        convertToTopadsCategory(topadProduct.category()),
                topadProduct.product_preorder() == null ? false : topadProduct.product_preorder(),
                topadProduct.product_wholesale() == null ? false : topadProduct.product_wholesale(),
                topadProduct.free_return() == null ? "" : topadProduct.free_return().toString(),
                topadProduct.product_cashback() == null ? false : topadProduct.product_cashback(),
                topadProduct.product_cashback_rate(),
                topadProduct.product_rating() == null ? 0 : topadProduct.product_rating(),
                null,
                null);
    }

    private ProductImage convertToTopadsProductImage(FeedQuery.Data.Image productImage) {
        return new ProductImage(productImage.m_url().toString(),
                productImage.s_url().toString(),
                productImage.xs_url().toString(),
                productImage.m_ecs().toString(),
                productImage.s_ecs().toString(),
                productImage.xs_ecs().toString());
    }

    private Category convertToTopadsCategory(FeedQuery.Data.Category category) {
        return new Category(category.id());
    }

    private DataFeedDomain createDataFeedDomain(FeedQuery.Data.Datum datum,
                                                ContentFeedDomain contentFeedDomain,
                                                SourceFeedDomain sourceFeedDomain) {
        return new DataFeedDomain(datum.id(), datum.create_time(), datum.type(), datum.cursor(),
                sourceFeedDomain, contentFeedDomain);
    }


}
