package com.tokopedia.home.beranda.data.source.pojo;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.gson.annotations.Expose;
import com.tkpd.library.utils.CurrencyFormatHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class DynamicHomeChannel {
    @Expose
    private List<Channels> channels;

    public List<Channels> getChannels() {
        return channels;
    }

    public void setChannels(List<Channels> channels) {
        this.channels = channels;
    }

    public class Channels {
        public static final String LAYOUT_HERO = "hero_4_image";
        public static final String LAYOUT_3_IMAGE = "3_image";
        public static final String LAYOUT_SPRINT = "sprint_3_image";

        @Expose
        private String id;

        @Expose
        private String layout;

        @Expose
        private String name;

        @Expose
        private Grid[] grids;

        @Expose
        private Hero[] hero;

        @Expose
        private String type;

        @Expose
        private Header header;
        private String promoName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLayout() {
            return layout;
        }

        public void setLayout(String layout) {
            this.layout = layout;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Grid[] getGrids() {
            return grids;
        }

        public void setGrids(Grid[] grids) {
            this.grids = grids;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Header getHeader() {
            return header;
        }

        public void setHeader(Header header) {
            this.header = header;
        }

        public Hero[] getHero() {
            return hero;
        }

        public void setHero(Hero[] hero) {
            this.hero = hero;
        }

        public Map<String, Object> getEnhanceImpressionSprintSaleHomePage() {
            List<Object> list = convertProductEnhanceSprintSaleDataLayer(getGrids());
            return DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "homepage",
                    "eventAction", "sprint sale impression",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "impressions", DataLayer.listOf(
                                    list.toArray(new Object[list.size()])

                            ))
            );
        }

        private List<Object> convertProductEnhanceSprintSaleDataLayer(Grid[] grids) {
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < grids.length; i++) {
                Grid grid = grids[i];
                list.add(
                        DataLayer.mapOf(
                                "name", grid.getName(),
                                "id", grid.getId(),
                                "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(
                                        grid.getPrice()
                                )),
                                "brand", "none / other",
                                "category", "none / other",
                                "variant", "none / other",
                                "list", "/ - p1 - sprint sale",
                                "position", i + 1
                        )
                );
            }
            return list;
        }

        public Map<String, Object> getEnhanceClickSprintSaleHomePage(int position) {
            return DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "homepage",
                    "eventAction", "sprint sale click",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", "/ - p1 - sprint sale"),
                                    "products", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "name", getGrids()[position].getName(),
                                                    "id", getGrids()[position].getId(),
                                                    "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(
                                                            getGrids()[position].getPrice()
                                                    )),
                                                    "brand", "none / other",
                                                    "category", "none / other",
                                                    "variant", "none / other",
                                                    "list", "/ - p1 - sprint sale",
                                                    "position", position + 1
                                            )
                                    )
                            )
                    )
            );
        }

        public Map<String, Object> getEnhanceImpressionDynamicChannelHomePage() {
            List<Object> list = convertPromoEnhanceDynamicChannelDataLayer(getHero(), getGrids(), getPromoName());
            return DataLayer.mapOf(
                    "event", "promoView",
                    "eventCategory", "homepage",
                    "eventAction", "curated list banner impression",
                    "eventLabel", getHeader().getName(),
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            list.toArray(new Object[list.size()])
                                    )
                            )
                    )
            );
        }

        private List<Object> convertPromoEnhanceDynamicChannelDataLayer(Hero[] hero, Grid[] grids, String promoName) {
            List<Object> list = new ArrayList<>();
            if (hero != null) {
                list.add(DataLayer.mapOf(
                        "id", hero[0].getId(),
                        "name", promoName,
                        "creative", hero[0].getName(),
                        "position", String.valueOf(1)
                ));
            }

            if (grids != null) {
                for (int i = 0; i < grids.length; i++) {
                    Grid grid = grids[i];
                    list.add(
                            DataLayer.mapOf(
                                    "id", grid.getId(),
                                    "name", promoName,
                                    "creative", grid.getName(),
                                    "position", String.valueOf(i + 2)
                            )
                    );
                }
            }
            return list;
        }

        public Map<String, Object> getEnhanceClickDynamicChannelHomePage(Hero hero, int position) {
            return DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "homepage",
                    "eventAction", "curated list banner click",
                    "eventLabel", String.format("%s - %s", getHeader().getName(), getHeader().getApplink()),
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", hero.getId(),
                                                    "name", getPromoName(),
                                                    "creative", hero.getName(),
                                                    "position", String.valueOf(position)
                                            )
                                    )
                            )
                    )
            );
        }

        public Map<String, Object> getEnhanceClickDynamicChannelHomePage(Grid grid, int position) {
            return DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "homepage",
                    "eventAction", "curated list banner click",
                    "eventLabel", String.format("%s - %s", getHeader().getName(), getHeader().getApplink()),
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", grid.getId(),
                                                    "name", getPromoName(),
                                                    "creative", grid.getName(),
                                                    "position", String.valueOf(position)
                                            )
                                    )
                            )
                    )
            );
        }

        public void setPromoName(String promoName) {
            this.promoName = promoName;
        }

        public String getPromoName() {
            return promoName;
        }
    }

    public class Hero {
        @Expose
        private String id;

        @Expose
        private String imageUrl;

        @Expose
        private String name;

        @Expose
        private String applink;

        @Expose
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getApplink() {
            return applink;
        }

        public void setApplink(String applink) {
            this.applink = applink;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public class Grid {
        @Expose
        private String id;

        @Expose
        private String price;

        @Expose
        private String imageUrl;

        @Expose
        private String name;

        @Expose
        private String applink;

        @Expose
        private String url;

        @Expose
        private String discount;

        @Expose
        private String slashedPrice;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getApplink() {
            return applink;
        }

        public void setApplink(String applink) {
            this.applink = applink;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getSlashedPrice() {
            return slashedPrice;
        }

        public void setSlashedPrice(String slashedPrice) {
            this.slashedPrice = slashedPrice;
        }
    }

    public class Header {
        @Expose
        private String id;

        @Expose
        private String name;

        @Expose
        private String expiredTime;

        @Expose
        private String applink;

        @Expose
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getExpiredTime() {
            return expiredTime;
        }

        public void setExpiredTime(String expiredTime) {
            this.expiredTime = expiredTime;
        }

        public String getApplink() {
            return applink;
        }

        public void setApplink(String applink) {
            this.applink = applink;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
