package com.tkpdfeed.feeds;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseReader;
import com.apollographql.apollo.api.internal.UnmodifiableMapBuilder;
import com.tkpdfeed.feeds.type.CustomType;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("Apollo GraphQL")
public final class HomeFeedQuery implements Query<HomeFeedQuery.Data, HomeFeedQuery.Data, HomeFeedQuery.Variables> {
  public static final String OPERATION_DEFINITION = "query HomeFeedQuery($userID: Int!, $limit: Int!, $cursor: String) {\n"
          + "  feed(limit: $limit, cursor: $cursor, userID: $userID, source: \"home\") {\n"
          + "    __typename\n"
          + "    data {\n"
          + "      __typename\n"
          + "      id\n"
          + "      create_time\n"
          + "      type\n"
          + "      cursor\n"
          + "      source {\n"
          + "        __typename\n"
          + "        type\n"
          + "        shop {\n"
          + "          __typename\n"
          + "          id\n"
          + "          name\n"
          + "          avatar\n"
          + "          isOfficial\n"
          + "          isGold\n"
          + "          url\n"
          + "          shopLink\n"
          + "          shareLinkDescription\n"
          + "          shareLinkURL\n"
          + "        }\n"
          + "      }\n"
          + "      content {\n"
          + "        __typename\n"
          + "        type\n"
          + "        inspirasi {\n"
          + "          __typename\n"
          + "          experiment_version\n"
          + "          source\n"
          + "          title\n"
          + "          foreign_title\n"
          + "          widget_url\n"
          + "          pagination {\n"
          + "            __typename\n"
          + "            current_page\n"
          + "            next_page\n"
          + "            prev_page\n"
          + "          }\n"
          + "          recommendation {\n"
          + "            __typename\n"
          + "            id\n"
          + "            name\n"
          + "            url\n"
          + "            click_url\n"
          + "            app_url\n"
          + "            image_url\n"
          + "            price\n"
          + "            recommendation_type\n"
          + "          }\n"
          + "        }\n"
          + "        topads {\n"
          + "          __typename\n"
          + "          id\n"
          + "          ad_ref_key\n"
          + "          redirect\n"
          + "          sticker_id\n"
          + "          sticker_image\n"
          + "          product_click_url\n"
          + "          shop_click_url\n"
          + "          product {\n"
          + "            __typename\n"
          + "            id\n"
          + "            name\n"
          + "            image {\n"
          + "              __typename\n"
          + "              s_url\n"
          + "              s_ecs\n"
          + "            }\n"
          + "            price_format\n"
          + "          }\n"
          + "          shop {\n"
          + "            __typename\n"
          + "            id\n"
          + "          }\n"
          + "          applinks\n"
          + "        }\n"
          + "      }\n"
          + "    }\n"
          + "    links {\n"
          + "      __typename\n"
          + "      self\n"
          + "      pagination {\n"
          + "        __typename\n"
          + "        has_next_page\n"
          + "      }\n"
          + "    }\n"
          + "    meta {\n"
          + "      __typename\n"
          + "      total_data\n"
          + "    }\n"
          + "  }\n"
          + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final HomeFeedQuery.Variables variables;

  public HomeFeedQuery(int userID, int limit, @Nullable String cursor) {
    variables = new HomeFeedQuery.Variables(userID, limit, cursor);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public HomeFeedQuery.Data wrapData(HomeFeedQuery.Data data) {
    return data;
  }

  @Override
  public HomeFeedQuery.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<HomeFeedQuery.Data> responseFieldMapper() {
    return new Data.Mapper();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Variables extends Operation.Variables {
    private final int userID;

    private final int limit;

    private final @Nullable String cursor;

    private final transient Map<String, Object> valueMap = new LinkedHashMap<>();

    Variables(int userID, int limit, @Nullable String cursor) {
      this.userID = userID;
      this.limit = limit;
      this.cursor = cursor;
      this.valueMap.put("userID", userID);
      this.valueMap.put("limit", limit);
      this.valueMap.put("cursor", cursor);
    }

    public int userID() {
      return userID;
    }

    public int limit() {
      return limit;
    }

    public @Nullable String cursor() {
      return cursor;
    }

    @Override
    public Map<String, Object> valueMap() {
      return Collections.unmodifiableMap(valueMap);
    }
  }

  public static final class Builder {
    private int userID;

    private int limit;

    private @Nullable String cursor;

    Builder() {
    }

    public Builder userID(int userID) {
      this.userID = userID;
      return this;
    }

    public Builder limit(int limit) {
      this.limit = limit;
      return this;
    }

    public Builder cursor(@Nullable String cursor) {
      this.cursor = cursor;
      return this;
    }

    public HomeFeedQuery build() {
      return new HomeFeedQuery(userID, limit, cursor);
    }
  }

  public static class Data implements Operation.Data {
    private final @Nullable Feed feed;

    public Data(@Nullable Feed feed) {
      this.feed = feed;
    }

    public @Nullable Feed feed() {
      return this.feed;
    }

    @Override
    public String toString() {
      return "Data{"
              + "feed=" + feed
              + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Data) {
        Data that = (Data) o;
        return ((this.feed == null) ? (that.feed == null) : this.feed.equals(that.feed));
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h = 1;
      h *= 1000003;
      h ^= (feed == null) ? 0 : feed.hashCode();
      return h;
    }

    public static final class Mapper implements ResponseFieldMapper<Data> {
      final Feed.Mapper feedFieldMapper = new Feed.Mapper();

      final Field[] fields = {
              Field.forObject("feed", "feed", new UnmodifiableMapBuilder<String, Object>(4)
                      .put("cursor", new UnmodifiableMapBuilder<String, Object>(2)
                              .put("kind", "Variable")
                              .put("variableName", "cursor")
                              .build())
                      .put("limit", new UnmodifiableMapBuilder<String, Object>(2)
                              .put("kind", "Variable")
                              .put("variableName", "limit")
                              .build())
                      .put("source", "home")
                      .put("userID", new UnmodifiableMapBuilder<String, Object>(2)
                              .put("kind", "Variable")
                              .put("variableName", "userID")
                              .build())
                      .build(), true, new Field.ObjectReader<Feed>() {
                @Override public Feed read(final ResponseReader reader) throws IOException {
                  return feedFieldMapper.map(reader);
                }
              })
      };

      @Override
      public Data map(ResponseReader reader) throws IOException {
        final Feed feed = reader.read(fields[0]);
        return new Data(feed);
      }
    }

    public static class Shop {
      private final @Nullable Integer id;

      private final @Nullable String name;

      private final @Nullable String avatar;

      private final @Nullable Boolean isOfficial;

      private final @Nullable Boolean isGold;

      private final @Nullable Object url;

      private final @Nullable String shopLink;

      private final @Nullable String shareLinkDescription;

      private final @Nullable String shareLinkURL;

      public Shop(@Nullable Integer id, @Nullable String name, @Nullable String avatar,
                  @Nullable Boolean isOfficial, @Nullable Boolean isGold, @Nullable Object url,
                  @Nullable String shopLink, @Nullable String shareLinkDescription,
                  @Nullable String shareLinkURL) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.isOfficial = isOfficial;
        this.isGold = isGold;
        this.url = url;
        this.shopLink = shopLink;
        this.shareLinkDescription = shareLinkDescription;
        this.shareLinkURL = shareLinkURL;
      }

      public @Nullable Integer id() {
        return this.id;
      }

      public @Nullable String name() {
        return this.name;
      }

      public @Nullable String avatar() {
        return this.avatar;
      }

      public @Nullable Boolean isOfficial() {
        return this.isOfficial;
      }

      public @Nullable Boolean isGold() {
        return this.isGold;
      }

      public @Nullable Object url() {
        return this.url;
      }

      public @Nullable String shopLink() {
        return this.shopLink;
      }

      public @Nullable String shareLinkDescription() {
        return this.shareLinkDescription;
      }

      public @Nullable String shareLinkURL() {
        return this.shareLinkURL;
      }

      @Override
      public String toString() {
        return "Shop{"
                + "id=" + id + ", "
                + "name=" + name + ", "
                + "avatar=" + avatar + ", "
                + "isOfficial=" + isOfficial + ", "
                + "isGold=" + isGold + ", "
                + "url=" + url + ", "
                + "shopLink=" + shopLink + ", "
                + "shareLinkDescription=" + shareLinkDescription + ", "
                + "shareLinkURL=" + shareLinkURL
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Shop) {
          Shop that = (Shop) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
                  && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
                  && ((this.avatar == null) ? (that.avatar == null) : this.avatar.equals(that.avatar))
                  && ((this.isOfficial == null) ? (that.isOfficial == null) : this.isOfficial.equals(that.isOfficial))
                  && ((this.isGold == null) ? (that.isGold == null) : this.isGold.equals(that.isGold))
                  && ((this.url == null) ? (that.url == null) : this.url.equals(that.url))
                  && ((this.shopLink == null) ? (that.shopLink == null) : this.shopLink.equals(that.shopLink))
                  && ((this.shareLinkDescription == null) ? (that.shareLinkDescription == null) : this.shareLinkDescription.equals(that.shareLinkDescription))
                  && ((this.shareLinkURL == null) ? (that.shareLinkURL == null) : this.shareLinkURL.equals(that.shareLinkURL));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (name == null) ? 0 : name.hashCode();
        h *= 1000003;
        h ^= (avatar == null) ? 0 : avatar.hashCode();
        h *= 1000003;
        h ^= (isOfficial == null) ? 0 : isOfficial.hashCode();
        h *= 1000003;
        h ^= (isGold == null) ? 0 : isGold.hashCode();
        h *= 1000003;
        h ^= (url == null) ? 0 : url.hashCode();
        h *= 1000003;
        h ^= (shopLink == null) ? 0 : shopLink.hashCode();
        h *= 1000003;
        h ^= (shareLinkDescription == null) ? 0 : shareLinkDescription.hashCode();
        h *= 1000003;
        h ^= (shareLinkURL == null) ? 0 : shareLinkURL.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Shop> {
        final Field[] fields = {
                Field.forInt("id", "id", null, true),
                Field.forString("name", "name", null, true),
                Field.forString("avatar", "avatar", null, true),
                Field.forBoolean("isOfficial", "isOfficial", null, true),
                Field.forBoolean("isGold", "isGold", null, true),
                Field.forCustomType("url", "url", null, true, CustomType.URL),
                Field.forString("shopLink", "shopLink", null, true),
                Field.forString("shareLinkDescription", "shareLinkDescription", null, true),
                Field.forString("shareLinkURL", "shareLinkURL", null, true)
        };

        @Override
        public Shop map(ResponseReader reader) throws IOException {
          final Integer id = reader.read(fields[0]);
          final String name = reader.read(fields[1]);
          final String avatar = reader.read(fields[2]);
          final Boolean isOfficial = reader.read(fields[3]);
          final Boolean isGold = reader.read(fields[4]);
          final Object url = reader.read(fields[5]);
          final String shopLink = reader.read(fields[6]);
          final String shareLinkDescription = reader.read(fields[7]);
          final String shareLinkURL = reader.read(fields[8]);
          return new Shop(id, name, avatar, isOfficial, isGold, url, shopLink, shareLinkDescription, shareLinkURL);
        }
      }
    }

    public static class Source {
      private final @Nullable Integer type;

      private final @Nullable Shop shop;

      public Source(@Nullable Integer type, @Nullable Shop shop) {
        this.type = type;
        this.shop = shop;
      }

      public @Nullable Integer type() {
        return this.type;
      }

      public @Nullable Shop shop() {
        return this.shop;
      }

      @Override
      public String toString() {
        return "Source{"
                + "type=" + type + ", "
                + "shop=" + shop
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Source) {
          Source that = (Source) o;
          return ((this.type == null) ? (that.type == null) : this.type.equals(that.type))
                  && ((this.shop == null) ? (that.shop == null) : this.shop.equals(that.shop));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (type == null) ? 0 : type.hashCode();
        h *= 1000003;
        h ^= (shop == null) ? 0 : shop.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Source> {
        final Shop.Mapper shopFieldMapper = new Shop.Mapper();

        final Field[] fields = {
                Field.forInt("type", "type", null, true),
                Field.forObject("shop", "shop", null, true, new Field.ObjectReader<Shop>() {
                  @Override public Shop read(final ResponseReader reader) throws IOException {
                    return shopFieldMapper.map(reader);
                  }
                })
        };

        @Override
        public Source map(ResponseReader reader) throws IOException {
          final Integer type = reader.read(fields[0]);
          final Shop shop = reader.read(fields[1]);
          return new Source(type, shop);
        }
      }
    }

    public static class Pagination {
      private final @Nullable Integer current_page;

      private final @Nullable Integer next_page;

      private final @Nullable Integer prev_page;

      public Pagination(@Nullable Integer current_page, @Nullable Integer next_page,
                        @Nullable Integer prev_page) {
        this.current_page = current_page;
        this.next_page = next_page;
        this.prev_page = prev_page;
      }

      public @Nullable Integer current_page() {
        return this.current_page;
      }

      public @Nullable Integer next_page() {
        return this.next_page;
      }

      public @Nullable Integer prev_page() {
        return this.prev_page;
      }

      @Override
      public String toString() {
        return "Pagination{"
                + "current_page=" + current_page + ", "
                + "next_page=" + next_page + ", "
                + "prev_page=" + prev_page
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Pagination) {
          Pagination that = (Pagination) o;
          return ((this.current_page == null) ? (that.current_page == null) : this.current_page.equals(that.current_page))
                  && ((this.next_page == null) ? (that.next_page == null) : this.next_page.equals(that.next_page))
                  && ((this.prev_page == null) ? (that.prev_page == null) : this.prev_page.equals(that.prev_page));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (current_page == null) ? 0 : current_page.hashCode();
        h *= 1000003;
        h ^= (next_page == null) ? 0 : next_page.hashCode();
        h *= 1000003;
        h ^= (prev_page == null) ? 0 : prev_page.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Pagination> {
        final Field[] fields = {
                Field.forInt("current_page", "current_page", null, true),
                Field.forInt("next_page", "next_page", null, true),
                Field.forInt("prev_page", "prev_page", null, true)
        };

        @Override
        public Pagination map(ResponseReader reader) throws IOException {
          final Integer current_page = reader.read(fields[0]);
          final Integer next_page = reader.read(fields[1]);
          final Integer prev_page = reader.read(fields[2]);
          return new Pagination(current_page, next_page, prev_page);
        }
      }
    }

    public static class Recommendation {
      private final @Nullable String id;

      private final @Nullable String name;

      private final @Nullable Object url;

      private final @Nullable String click_url;

      private final @Nullable String app_url;

      private final @Nullable Object image_url;

      private final @Nullable String price;

      private final @Nullable String recommendation_type;

      public Recommendation(@Nullable String id, @Nullable String name, @Nullable Object url,
                            @Nullable String click_url, @Nullable String app_url, @Nullable Object image_url,
                            @Nullable String price, @Nullable String recommendation_type) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.click_url = click_url;
        this.app_url = app_url;
        this.image_url = image_url;
        this.price = price;
        this.recommendation_type = recommendation_type;
      }

      public @Nullable String id() {
        return this.id;
      }

      public @Nullable String name() {
        return this.name;
      }

      public @Nullable Object url() {
        return this.url;
      }

      public @Nullable String click_url() {
        return this.click_url;
      }

      public @Nullable String app_url() {
        return this.app_url;
      }

      public @Nullable Object image_url() {
        return this.image_url;
      }

      public @Nullable String price() {
        return this.price;
      }

      public @Nullable String recommendation_type() {
        return this.recommendation_type;
      }

      @Override
      public String toString() {
        return "Recommendation{"
                + "id=" + id + ", "
                + "name=" + name + ", "
                + "url=" + url + ", "
                + "click_url=" + click_url + ", "
                + "app_url=" + app_url + ", "
                + "image_url=" + image_url + ", "
                + "price=" + price + ", "
                + "recommendation_type=" + recommendation_type
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Recommendation) {
          Recommendation that = (Recommendation) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
                  && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
                  && ((this.url == null) ? (that.url == null) : this.url.equals(that.url))
                  && ((this.click_url == null) ? (that.click_url == null) : this.click_url.equals(that.click_url))
                  && ((this.app_url == null) ? (that.app_url == null) : this.app_url.equals(that.app_url))
                  && ((this.image_url == null) ? (that.image_url == null) : this.image_url.equals(that.image_url))
                  && ((this.price == null) ? (that.price == null) : this.price.equals(that.price))
                  && ((this.recommendation_type == null) ? (that.recommendation_type == null) : this.recommendation_type.equals(that.recommendation_type));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (name == null) ? 0 : name.hashCode();
        h *= 1000003;
        h ^= (url == null) ? 0 : url.hashCode();
        h *= 1000003;
        h ^= (click_url == null) ? 0 : click_url.hashCode();
        h *= 1000003;
        h ^= (app_url == null) ? 0 : app_url.hashCode();
        h *= 1000003;
        h ^= (image_url == null) ? 0 : image_url.hashCode();
        h *= 1000003;
        h ^= (price == null) ? 0 : price.hashCode();
        h *= 1000003;
        h ^= (recommendation_type == null) ? 0 : recommendation_type.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Recommendation> {
        final Field[] fields = {
                Field.forString("id", "id", null, true),
                Field.forString("name", "name", null, true),
                Field.forCustomType("url", "url", null, true, CustomType.URL),
                Field.forString("click_url", "click_url", null, true),
                Field.forString("app_url", "app_url", null, true),
                Field.forCustomType("image_url", "image_url", null, true, CustomType.URL),
                Field.forString("price", "price", null, true),
                Field.forString("recommendation_type", "recommendation_type", null, true)
        };

        @Override
        public Recommendation map(ResponseReader reader) throws IOException {
          final String id = reader.read(fields[0]);
          final String name = reader.read(fields[1]);
          final Object url = reader.read(fields[2]);
          final String click_url = reader.read(fields[3]);
          final String app_url = reader.read(fields[4]);
          final Object image_url = reader.read(fields[5]);
          final String price = reader.read(fields[6]);
          final String recommendation_type = reader.read(fields[7]);
          return new Recommendation(id, name, url, click_url, app_url, image_url, price, recommendation_type);
        }
      }
    }

    public static class Inspirasi {
      private final @Nullable String experiment_version;

      private final @Nullable String source;

      private final @Nullable String title;

      private final @Nullable String foreign_title;

      private final @Nullable String widget_url;

      private final @Nullable Pagination pagination;

      private final @Nullable List<Recommendation> recommendation;

      public Inspirasi(@Nullable String experiment_version, @Nullable String source,
                       @Nullable String title, @Nullable String foreign_title, @Nullable String widget_url,
                       @Nullable Pagination pagination, @Nullable List<Recommendation> recommendation) {
        this.experiment_version = experiment_version;
        this.source = source;
        this.title = title;
        this.foreign_title = foreign_title;
        this.widget_url = widget_url;
        this.pagination = pagination;
        this.recommendation = recommendation;
      }

      public @Nullable String experiment_version() {
        return this.experiment_version;
      }

      public @Nullable String source() {
        return this.source;
      }

      public @Nullable String title() {
        return this.title;
      }

      public @Nullable String foreign_title() {
        return this.foreign_title;
      }

      public @Nullable String widget_url() {
        return this.widget_url;
      }

      public @Nullable Pagination pagination() {
        return this.pagination;
      }

      public @Nullable List<Recommendation> recommendation() {
        return this.recommendation;
      }

      @Override
      public String toString() {
        return "Inspirasi{"
                + "experiment_version=" + experiment_version + ", "
                + "source=" + source + ", "
                + "title=" + title + ", "
                + "foreign_title=" + foreign_title + ", "
                + "widget_url=" + widget_url + ", "
                + "pagination=" + pagination + ", "
                + "recommendation=" + recommendation
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Inspirasi) {
          Inspirasi that = (Inspirasi) o;
          return ((this.experiment_version == null) ? (that.experiment_version == null) : this.experiment_version.equals(that.experiment_version))
                  && ((this.source == null) ? (that.source == null) : this.source.equals(that.source))
                  && ((this.title == null) ? (that.title == null) : this.title.equals(that.title))
                  && ((this.foreign_title == null) ? (that.foreign_title == null) : this.foreign_title.equals(that.foreign_title))
                  && ((this.widget_url == null) ? (that.widget_url == null) : this.widget_url.equals(that.widget_url))
                  && ((this.pagination == null) ? (that.pagination == null) : this.pagination.equals(that.pagination))
                  && ((this.recommendation == null) ? (that.recommendation == null) : this.recommendation.equals(that.recommendation));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (experiment_version == null) ? 0 : experiment_version.hashCode();
        h *= 1000003;
        h ^= (source == null) ? 0 : source.hashCode();
        h *= 1000003;
        h ^= (title == null) ? 0 : title.hashCode();
        h *= 1000003;
        h ^= (foreign_title == null) ? 0 : foreign_title.hashCode();
        h *= 1000003;
        h ^= (widget_url == null) ? 0 : widget_url.hashCode();
        h *= 1000003;
        h ^= (pagination == null) ? 0 : pagination.hashCode();
        h *= 1000003;
        h ^= (recommendation == null) ? 0 : recommendation.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Inspirasi> {
        final Pagination.Mapper paginationFieldMapper = new Pagination.Mapper();

        final Recommendation.Mapper recommendationFieldMapper = new Recommendation.Mapper();

        final Field[] fields = {
                Field.forString("experiment_version", "experiment_version", null, true),
                Field.forString("source", "source", null, true),
                Field.forString("title", "title", null, true),
                Field.forString("foreign_title", "foreign_title", null, true),
                Field.forString("widget_url", "widget_url", null, true),
                Field.forObject("pagination", "pagination", null, true, new Field.ObjectReader<Pagination>() {
                  @Override public Pagination read(final ResponseReader reader) throws IOException {
                    return paginationFieldMapper.map(reader);
                  }
                }),
                Field.forList("recommendation", "recommendation", null, true, new Field.ObjectReader<Recommendation>() {
                  @Override public Recommendation read(final ResponseReader reader) throws IOException {
                    return recommendationFieldMapper.map(reader);
                  }
                })
        };

        @Override
        public Inspirasi map(ResponseReader reader) throws IOException {
          final String experiment_version = reader.read(fields[0]);
          final String source = reader.read(fields[1]);
          final String title = reader.read(fields[2]);
          final String foreign_title = reader.read(fields[3]);
          final String widget_url = reader.read(fields[4]);
          final Pagination pagination = reader.read(fields[5]);
          final List<Recommendation> recommendation = reader.read(fields[6]);
          return new Inspirasi(experiment_version, source, title, foreign_title, widget_url, pagination, recommendation);
        }
      }
    }

    public static class Image {
      private final @Nonnull Object s_url;

      private final @Nonnull Object s_ecs;

      public Image(@Nonnull Object s_url, @Nonnull Object s_ecs) {
        this.s_url = s_url;
        this.s_ecs = s_ecs;
      }

      public @Nonnull Object s_url() {
        return this.s_url;
      }

      public @Nonnull Object s_ecs() {
        return this.s_ecs;
      }

      @Override
      public String toString() {
        return "Image{"
                + "s_url=" + s_url + ", "
                + "s_ecs=" + s_ecs
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Image) {
          Image that = (Image) o;
          return ((this.s_url == null) ? (that.s_url == null) : this.s_url.equals(that.s_url))
                  && ((this.s_ecs == null) ? (that.s_ecs == null) : this.s_ecs.equals(that.s_ecs));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (s_url == null) ? 0 : s_url.hashCode();
        h *= 1000003;
        h ^= (s_ecs == null) ? 0 : s_ecs.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Image> {
        final Field[] fields = {
                Field.forCustomType("s_url", "s_url", null, false, CustomType.URL),
                Field.forCustomType("s_ecs", "s_ecs", null, false, CustomType.URL)
        };

        @Override
        public Image map(ResponseReader reader) throws IOException {
          final Object s_url = reader.read(fields[0]);
          final Object s_ecs = reader.read(fields[1]);
          return new Image(s_url, s_ecs);
        }
      }
    }

    public static class Product {
      private final @Nullable String id;

      private final @Nullable String name;

      private final @Nullable Image image;

      private final @Nullable String price_format;

      public Product(@Nullable String id, @Nullable String name, @Nullable Image image,
                     @Nullable String price_format) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price_format = price_format;
      }

      public @Nullable String id() {
        return this.id;
      }

      public @Nullable String name() {
        return this.name;
      }

      public @Nullable Image image() {
        return this.image;
      }

      public @Nullable String price_format() {
        return this.price_format;
      }

      @Override
      public String toString() {
        return "Product{"
                + "id=" + id + ", "
                + "name=" + name + ", "
                + "image=" + image + ", "
                + "price_format=" + price_format
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Product) {
          Product that = (Product) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
                  && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
                  && ((this.image == null) ? (that.image == null) : this.image.equals(that.image))
                  && ((this.price_format == null) ? (that.price_format == null) : this.price_format.equals(that.price_format));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (name == null) ? 0 : name.hashCode();
        h *= 1000003;
        h ^= (image == null) ? 0 : image.hashCode();
        h *= 1000003;
        h ^= (price_format == null) ? 0 : price_format.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Product> {
        final Image.Mapper imageFieldMapper = new Image.Mapper();

        final Field[] fields = {
                Field.forString("id", "id", null, true),
                Field.forString("name", "name", null, true),
                Field.forObject("image", "image", null, true, new Field.ObjectReader<Image>() {
                  @Override public Image read(final ResponseReader reader) throws IOException {
                    return imageFieldMapper.map(reader);
                  }
                }),
                Field.forString("price_format", "price_format", null, true)
        };

        @Override
        public Product map(ResponseReader reader) throws IOException {
          final String id = reader.read(fields[0]);
          final String name = reader.read(fields[1]);
          final Image image = reader.read(fields[2]);
          final String price_format = reader.read(fields[3]);
          return new Product(id, name, image, price_format);
        }
      }
    }

    public static class Shop1 {
      private final @Nullable String id;

      public Shop1(@Nullable String id) {
        this.id = id;
      }

      public @Nullable String id() {
        return this.id;
      }

      @Override
      public String toString() {
        return "Shop1{"
                + "id=" + id
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Shop1) {
          Shop1 that = (Shop1) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Shop1> {
        final Field[] fields = {
                Field.forString("id", "id", null, true)
        };

        @Override
        public Shop1 map(ResponseReader reader) throws IOException {
          final String id = reader.read(fields[0]);
          return new Shop1(id);
        }
      }
    }

    public static class Topad {
      private final @Nullable String id;

      private final @Nullable String ad_ref_key;

      private final @Nullable Object redirect;

      private final @Nullable String sticker_id;

      private final @Nullable Object sticker_image;

      private final @Nullable Object product_click_url;

      private final @Nullable Object shop_click_url;

      private final @Nullable Product product;

      private final @Nullable Shop1 shop;

      private final @Nullable String applinks;

      public Topad(@Nullable String id, @Nullable String ad_ref_key, @Nullable Object redirect,
                   @Nullable String sticker_id, @Nullable Object sticker_image,
                   @Nullable Object product_click_url, @Nullable Object shop_click_url,
                   @Nullable Product product, @Nullable Shop1 shop, @Nullable String applinks) {
        this.id = id;
        this.ad_ref_key = ad_ref_key;
        this.redirect = redirect;
        this.sticker_id = sticker_id;
        this.sticker_image = sticker_image;
        this.product_click_url = product_click_url;
        this.shop_click_url = shop_click_url;
        this.product = product;
        this.shop = shop;
        this.applinks = applinks;
      }

      public @Nullable String id() {
        return this.id;
      }

      public @Nullable String ad_ref_key() {
        return this.ad_ref_key;
      }

      public @Nullable Object redirect() {
        return this.redirect;
      }

      public @Nullable String sticker_id() {
        return this.sticker_id;
      }

      public @Nullable Object sticker_image() {
        return this.sticker_image;
      }

      public @Nullable Object product_click_url() {
        return this.product_click_url;
      }

      public @Nullable Object shop_click_url() {
        return this.shop_click_url;
      }

      public @Nullable Product product() {
        return this.product;
      }

      public @Nullable Shop1 shop() {
        return this.shop;
      }

      public @Nullable String applinks() {
        return this.applinks;
      }

      @Override
      public String toString() {
        return "Topad{"
                + "id=" + id + ", "
                + "ad_ref_key=" + ad_ref_key + ", "
                + "redirect=" + redirect + ", "
                + "sticker_id=" + sticker_id + ", "
                + "sticker_image=" + sticker_image + ", "
                + "product_click_url=" + product_click_url + ", "
                + "shop_click_url=" + shop_click_url + ", "
                + "product=" + product + ", "
                + "shop=" + shop + ", "
                + "applinks=" + applinks
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Topad) {
          Topad that = (Topad) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
                  && ((this.ad_ref_key == null) ? (that.ad_ref_key == null) : this.ad_ref_key.equals(that.ad_ref_key))
                  && ((this.redirect == null) ? (that.redirect == null) : this.redirect.equals(that.redirect))
                  && ((this.sticker_id == null) ? (that.sticker_id == null) : this.sticker_id.equals(that.sticker_id))
                  && ((this.sticker_image == null) ? (that.sticker_image == null) : this.sticker_image.equals(that.sticker_image))
                  && ((this.product_click_url == null) ? (that.product_click_url == null) : this.product_click_url.equals(that.product_click_url))
                  && ((this.shop_click_url == null) ? (that.shop_click_url == null) : this.shop_click_url.equals(that.shop_click_url))
                  && ((this.product == null) ? (that.product == null) : this.product.equals(that.product))
                  && ((this.shop == null) ? (that.shop == null) : this.shop.equals(that.shop))
                  && ((this.applinks == null) ? (that.applinks == null) : this.applinks.equals(that.applinks));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (ad_ref_key == null) ? 0 : ad_ref_key.hashCode();
        h *= 1000003;
        h ^= (redirect == null) ? 0 : redirect.hashCode();
        h *= 1000003;
        h ^= (sticker_id == null) ? 0 : sticker_id.hashCode();
        h *= 1000003;
        h ^= (sticker_image == null) ? 0 : sticker_image.hashCode();
        h *= 1000003;
        h ^= (product_click_url == null) ? 0 : product_click_url.hashCode();
        h *= 1000003;
        h ^= (shop_click_url == null) ? 0 : shop_click_url.hashCode();
        h *= 1000003;
        h ^= (product == null) ? 0 : product.hashCode();
        h *= 1000003;
        h ^= (shop == null) ? 0 : shop.hashCode();
        h *= 1000003;
        h ^= (applinks == null) ? 0 : applinks.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Topad> {
        final Product.Mapper productFieldMapper = new Product.Mapper();

        final Shop1.Mapper shop1FieldMapper = new Shop1.Mapper();

        final Field[] fields = {
                Field.forString("id", "id", null, true),
                Field.forString("ad_ref_key", "ad_ref_key", null, true),
                Field.forCustomType("redirect", "redirect", null, true, CustomType.URL),
                Field.forString("sticker_id", "sticker_id", null, true),
                Field.forCustomType("sticker_image", "sticker_image", null, true, CustomType.URL),
                Field.forCustomType("product_click_url", "product_click_url", null, true, CustomType.URL),
                Field.forCustomType("shop_click_url", "shop_click_url", null, true, CustomType.URL),
                Field.forObject("product", "product", null, true, new Field.ObjectReader<Product>() {
                  @Override public Product read(final ResponseReader reader) throws IOException {
                    return productFieldMapper.map(reader);
                  }
                }),
                Field.forObject("shop", "shop", null, true, new Field.ObjectReader<Shop1>() {
                  @Override public Shop1 read(final ResponseReader reader) throws IOException {
                    return shop1FieldMapper.map(reader);
                  }
                }),
                Field.forString("applinks", "applinks", null, true)
        };

        @Override
        public Topad map(ResponseReader reader) throws IOException {
          final String id = reader.read(fields[0]);
          final String ad_ref_key = reader.read(fields[1]);
          final Object redirect = reader.read(fields[2]);
          final String sticker_id = reader.read(fields[3]);
          final Object sticker_image = reader.read(fields[4]);
          final Object product_click_url = reader.read(fields[5]);
          final Object shop_click_url = reader.read(fields[6]);
          final Product product = reader.read(fields[7]);
          final Shop1 shop = reader.read(fields[8]);
          final String applinks = reader.read(fields[9]);
          return new Topad(id, ad_ref_key, redirect, sticker_id, sticker_image, product_click_url, shop_click_url, product, shop, applinks);
        }
      }
    }

    public static class Content {
      private final @Nullable String type;

      private final @Nullable List<Inspirasi> inspirasi;

      private final @Nullable List<Topad> topads;

      public Content(@Nullable String type, @Nullable List<Inspirasi> inspirasi,
                     @Nullable List<Topad> topads) {
        this.type = type;
        this.inspirasi = inspirasi;
        this.topads = topads;
      }

      public @Nullable String type() {
        return this.type;
      }

      public @Nullable List<Inspirasi> inspirasi() {
        return this.inspirasi;
      }

      public @Nullable List<Topad> topads() {
        return this.topads;
      }

      @Override
      public String toString() {
        return "Content{"
                + "type=" + type + ", "
                + "inspirasi=" + inspirasi + ", "
                + "topads=" + topads
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Content) {
          Content that = (Content) o;
          return ((this.type == null) ? (that.type == null) : this.type.equals(that.type))
                  && ((this.inspirasi == null) ? (that.inspirasi == null) : this.inspirasi.equals(that.inspirasi))
                  && ((this.topads == null) ? (that.topads == null) : this.topads.equals(that.topads));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (type == null) ? 0 : type.hashCode();
        h *= 1000003;
        h ^= (inspirasi == null) ? 0 : inspirasi.hashCode();
        h *= 1000003;
        h ^= (topads == null) ? 0 : topads.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Content> {
        final Inspirasi.Mapper inspirasiFieldMapper = new Inspirasi.Mapper();

        final Topad.Mapper topadFieldMapper = new Topad.Mapper();

        final Field[] fields = {
                Field.forString("type", "type", null, true),
                Field.forList("inspirasi", "inspirasi", null, true, new Field.ObjectReader<Inspirasi>() {
                  @Override public Inspirasi read(final ResponseReader reader) throws IOException {
                    return inspirasiFieldMapper.map(reader);
                  }
                }),
                Field.forList("topads", "topads", null, true, new Field.ObjectReader<Topad>() {
                  @Override public Topad read(final ResponseReader reader) throws IOException {
                    return topadFieldMapper.map(reader);
                  }
                })
        };

        @Override
        public Content map(ResponseReader reader) throws IOException {
          final String type = reader.read(fields[0]);
          final List<Inspirasi> inspirasi = reader.read(fields[1]);
          final List<Topad> topads = reader.read(fields[2]);
          return new Content(type, inspirasi, topads);
        }
      }
    }

    public static class Datum {
      private final @Nullable String id;

      private final @Nullable String create_time;

      private final @Nullable String type;

      private final @Nullable String cursor;

      private final @Nullable Source source;

      private final @Nullable Content content;

      public Datum(@Nullable String id, @Nullable String create_time, @Nullable String type,
                   @Nullable String cursor, @Nullable Source source, @Nullable Content content) {
        this.id = id;
        this.create_time = create_time;
        this.type = type;
        this.cursor = cursor;
        this.source = source;
        this.content = content;
      }

      public @Nullable String id() {
        return this.id;
      }

      public @Nullable String create_time() {
        return this.create_time;
      }

      public @Nullable String type() {
        return this.type;
      }

      public @Nullable String cursor() {
        return this.cursor;
      }

      public @Nullable Source source() {
        return this.source;
      }

      public @Nullable Content content() {
        return this.content;
      }

      @Override
      public String toString() {
        return "Datum{"
                + "id=" + id + ", "
                + "create_time=" + create_time + ", "
                + "type=" + type + ", "
                + "cursor=" + cursor + ", "
                + "source=" + source + ", "
                + "content=" + content
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Datum) {
          Datum that = (Datum) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
                  && ((this.create_time == null) ? (that.create_time == null) : this.create_time.equals(that.create_time))
                  && ((this.type == null) ? (that.type == null) : this.type.equals(that.type))
                  && ((this.cursor == null) ? (that.cursor == null) : this.cursor.equals(that.cursor))
                  && ((this.source == null) ? (that.source == null) : this.source.equals(that.source))
                  && ((this.content == null) ? (that.content == null) : this.content.equals(that.content));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (create_time == null) ? 0 : create_time.hashCode();
        h *= 1000003;
        h ^= (type == null) ? 0 : type.hashCode();
        h *= 1000003;
        h ^= (cursor == null) ? 0 : cursor.hashCode();
        h *= 1000003;
        h ^= (source == null) ? 0 : source.hashCode();
        h *= 1000003;
        h ^= (content == null) ? 0 : content.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Datum> {
        final Source.Mapper sourceFieldMapper = new Source.Mapper();

        final Content.Mapper contentFieldMapper = new Content.Mapper();

        final Field[] fields = {
                Field.forString("id", "id", null, true),
                Field.forString("create_time", "create_time", null, true),
                Field.forString("type", "type", null, true),
                Field.forString("cursor", "cursor", null, true),
                Field.forObject("source", "source", null, true, new Field.ObjectReader<Source>() {
                  @Override public Source read(final ResponseReader reader) throws IOException {
                    return sourceFieldMapper.map(reader);
                  }
                }),
                Field.forObject("content", "content", null, true, new Field.ObjectReader<Content>() {
                  @Override public Content read(final ResponseReader reader) throws IOException {
                    return contentFieldMapper.map(reader);
                  }
                })
        };

        @Override
        public Datum map(ResponseReader reader) throws IOException {
          final String id = reader.read(fields[0]);
          final String create_time = reader.read(fields[1]);
          final String type = reader.read(fields[2]);
          final String cursor = reader.read(fields[3]);
          final Source source = reader.read(fields[4]);
          final Content content = reader.read(fields[5]);
          return new Datum(id, create_time, type, cursor, source, content);
        }
      }
    }

    public static class Pagination1 {
      private final @Nullable Boolean has_next_page;

      public Pagination1(@Nullable Boolean has_next_page) {
        this.has_next_page = has_next_page;
      }

      public @Nullable Boolean has_next_page() {
        return this.has_next_page;
      }

      @Override
      public String toString() {
        return "Pagination1{"
                + "has_next_page=" + has_next_page
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Pagination1) {
          Pagination1 that = (Pagination1) o;
          return ((this.has_next_page == null) ? (that.has_next_page == null) : this.has_next_page.equals(that.has_next_page));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (has_next_page == null) ? 0 : has_next_page.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Pagination1> {
        final Field[] fields = {
                Field.forBoolean("has_next_page", "has_next_page", null, true)
        };

        @Override
        public Pagination1 map(ResponseReader reader) throws IOException {
          final Boolean has_next_page = reader.read(fields[0]);
          return new Pagination1(has_next_page);
        }
      }
    }

    public static class Links {
      private final @Nullable String self;

      private final @Nullable Pagination1 pagination;

      public Links(@Nullable String self, @Nullable Pagination1 pagination) {
        this.self = self;
        this.pagination = pagination;
      }

      public @Nullable String self() {
        return this.self;
      }

      public @Nullable Pagination1 pagination() {
        return this.pagination;
      }

      @Override
      public String toString() {
        return "Links{"
                + "self=" + self + ", "
                + "pagination=" + pagination
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Links) {
          Links that = (Links) o;
          return ((this.self == null) ? (that.self == null) : this.self.equals(that.self))
                  && ((this.pagination == null) ? (that.pagination == null) : this.pagination.equals(that.pagination));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (self == null) ? 0 : self.hashCode();
        h *= 1000003;
        h ^= (pagination == null) ? 0 : pagination.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Links> {
        final Pagination1.Mapper pagination1FieldMapper = new Pagination1.Mapper();

        final Field[] fields = {
                Field.forString("self", "self", null, true),
                Field.forObject("pagination", "pagination", null, true, new Field.ObjectReader<Pagination1>() {
                  @Override public Pagination1 read(final ResponseReader reader) throws IOException {
                    return pagination1FieldMapper.map(reader);
                  }
                })
        };

        @Override
        public Links map(ResponseReader reader) throws IOException {
          final String self = reader.read(fields[0]);
          final Pagination1 pagination = reader.read(fields[1]);
          return new Links(self, pagination);
        }
      }
    }

    public static class Meta {
      private final @Nullable Integer total_data;

      public Meta(@Nullable Integer total_data) {
        this.total_data = total_data;
      }

      public @Nullable Integer total_data() {
        return this.total_data;
      }

      @Override
      public String toString() {
        return "Meta{"
                + "total_data=" + total_data
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Meta) {
          Meta that = (Meta) o;
          return ((this.total_data == null) ? (that.total_data == null) : this.total_data.equals(that.total_data));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (total_data == null) ? 0 : total_data.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Meta> {
        final Field[] fields = {
                Field.forInt("total_data", "total_data", null, true)
        };

        @Override
        public Meta map(ResponseReader reader) throws IOException {
          final Integer total_data = reader.read(fields[0]);
          return new Meta(total_data);
        }
      }
    }

    public static class Feed {
      private final @Nullable List<Datum> data;

      private final @Nullable Links links;

      private final @Nullable Meta meta;

      public Feed(@Nullable List<Datum> data, @Nullable Links links, @Nullable Meta meta) {
        this.data = data;
        this.links = links;
        this.meta = meta;
      }

      public @Nullable List<Datum> data() {
        return this.data;
      }

      public @Nullable Links links() {
        return this.links;
      }

      public @Nullable Meta meta() {
        return this.meta;
      }

      @Override
      public String toString() {
        return "Feed{"
                + "data=" + data + ", "
                + "links=" + links + ", "
                + "meta=" + meta
                + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Feed) {
          Feed that = (Feed) o;
          return ((this.data == null) ? (that.data == null) : this.data.equals(that.data))
                  && ((this.links == null) ? (that.links == null) : this.links.equals(that.links))
                  && ((this.meta == null) ? (that.meta == null) : this.meta.equals(that.meta));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (data == null) ? 0 : data.hashCode();
        h *= 1000003;
        h ^= (links == null) ? 0 : links.hashCode();
        h *= 1000003;
        h ^= (meta == null) ? 0 : meta.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Feed> {
        final Datum.Mapper datumFieldMapper = new Datum.Mapper();

        final Links.Mapper linksFieldMapper = new Links.Mapper();

        final Meta.Mapper metaFieldMapper = new Meta.Mapper();

        final Field[] fields = {
                Field.forList("data", "data", null, true, new Field.ObjectReader<Datum>() {
                  @Override public Datum read(final ResponseReader reader) throws IOException {
                    return datumFieldMapper.map(reader);
                  }
                }),
                Field.forObject("links", "links", null, true, new Field.ObjectReader<Links>() {
                  @Override public Links read(final ResponseReader reader) throws IOException {
                    return linksFieldMapper.map(reader);
                  }
                }),
                Field.forObject("meta", "meta", null, true, new Field.ObjectReader<Meta>() {
                  @Override public Meta read(final ResponseReader reader) throws IOException {
                    return metaFieldMapper.map(reader);
                  }
                })
        };

        @Override
        public Feed map(ResponseReader reader) throws IOException {
          final List<Datum> data = reader.read(fields[0]);
          final Links links = reader.read(fields[1]);
          final Meta meta = reader.read(fields[2]);
          return new Feed(data, links, meta);
        }
      }
    }
  }
}
