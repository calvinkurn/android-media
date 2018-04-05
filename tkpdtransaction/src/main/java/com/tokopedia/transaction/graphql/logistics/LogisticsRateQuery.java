package com.tokopedia.transaction.graphql.logistics;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseReader;
import com.apollographql.apollo.api.internal.UnmodifiableMapBuilder;
import com.apollographql.apollo.api.internal.Utils;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("Apollo GraphQL")
public final class LogisticsRateQuery implements Query<LogisticsRateQuery.Data, LogisticsRateQuery.Data, LogisticsRateQuery.Variables> {
  public static final String OPERATION_DEFINITION = "query LogisticsRateQuery($input: OngkirRatesInput!) {\n"
      + "  ongkir(input: $input) {\n"
      + "    __typename\n"
      + "    rates {\n"
      + "      __typename\n"
      + "      id\n"
      + "      type\n"
      + "      attributes {\n"
      + "        __typename\n"
      + "        id\n"
      + "        shipper_id\n"
      + "        shipper_name\n"
      + "        service_id\n"
      + "        service_name\n"
      + "        origin_id\n"
      + "        origin_name\n"
      + "        origin_geoloc\n"
      + "        origin_zip_code\n"
      + "        destination_id\n"
      + "        destination_name\n"
      + "        destination_zip_code\n"
      + "        destination_geoloc\n"
      + "        weight\n"
      + "        service_etd\n"
      + "        weight_service\n"
      + "        shipper_id\n"
      + "        products {\n"
      + "          __typename\n"
      + "          service_id\n"
      + "          group_code\n"
      + "          service_desc\n"
      + "          shipper_name\n"
      + "          shipper_id\n"
      + "          shipper_product_id\n"
      + "          shipper_product_name\n"
      + "          shipper_product_desc\n"
      + "          is_show_map\n"
      + "          price\n"
      + "          formatted_price\n"
      + "          etd\n"
      + "          min_etd\n"
      + "          max_etd\n"
      + "          check_sum\n"
      + "          ut\n"
      + "          max_hour_id\n"
      + "          desc_hour_id\n"
      + "          max_hour\n"
      + "          desc_hour\n"
      + "          insurance_price\n"
      + "          insurance_type\n"
      + "          insurance_type_info\n"
      + "          weight_product\n"
      + "          weight_order_spid\n"
      + "          insurance_used_type\n"
      + "          insurance_used_info\n"
      + "          insurance_used_default\n"
      + "        }\n"
      + "      }\n"
      + "    }\n"
      + "  }\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final LogisticsRateQuery.Variables variables;

  public LogisticsRateQuery(@Nonnull OngkirRatesInput input) {
    Utils.checkNotNull(input, "input == null");
    variables = new LogisticsRateQuery.Variables(input);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public LogisticsRateQuery.Data wrapData(LogisticsRateQuery.Data data) {
    return data;
  }

  @Override
  public LogisticsRateQuery.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<LogisticsRateQuery.Data> responseFieldMapper() {
    return new Data.Mapper();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Variables extends Operation.Variables {
    private final @Nonnull OngkirRatesInput input;

    private final transient Map<String, Object> valueMap = new LinkedHashMap<>();

    Variables(@Nonnull OngkirRatesInput input) {
      this.input = input;
      this.valueMap.put("input", input);
    }

    public @Nonnull OngkirRatesInput input() {
      return input;
    }

    @Override
    public Map<String, Object> valueMap() {
      return Collections.unmodifiableMap(valueMap);
    }
  }

  public static final class Builder {
    private @Nonnull OngkirRatesInput input;

    Builder() {
    }

    public Builder input(@Nonnull OngkirRatesInput input) {
      this.input = input;
      return this;
    }

    public LogisticsRateQuery build() {
      if (input == null) throw new IllegalStateException("input can't be null");
      return new LogisticsRateQuery(input);
    }
  }

  public static class Data implements Operation.Data {
    private final @Nonnull Ongkir ongkir;

    public Data(@Nonnull Ongkir ongkir) {
      this.ongkir = ongkir;
    }

    public @Nonnull Ongkir ongkir() {
      return this.ongkir;
    }

    @Override
    public String toString() {
      return "Data{"
        + "ongkir=" + ongkir
        + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Data) {
        Data that = (Data) o;
        return ((this.ongkir == null) ? (that.ongkir == null) : this.ongkir.equals(that.ongkir));
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h = 1;
      h *= 1000003;
      h ^= (ongkir == null) ? 0 : ongkir.hashCode();
      return h;
    }

    public static final class Mapper implements ResponseFieldMapper<Data> {
      final Ongkir.Mapper ongkirFieldMapper = new Ongkir.Mapper();

      final Field[] fields = {
        Field.forObject("ongkir", "ongkir", new UnmodifiableMapBuilder<String, Object>(1)
          .put("input", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "input")
          .build())
        .build(), false, new Field.ObjectReader<Ongkir>() {
          @Override public Ongkir read(final ResponseReader reader) throws IOException {
            return ongkirFieldMapper.map(reader);
          }
        })
      };

      @Override
      public Data map(ResponseReader reader) throws IOException {
        final Ongkir ongkir = reader.read(fields[0]);
        return new Data(ongkir);
      }
    }

    public static class Product {
      private final @Nullable Integer service_id;

      private final @Nullable Integer group_code;

      private final @Nullable String service_desc;

      private final @Nullable String shipper_name;

      private final @Nullable Integer shipper_id;

      private final int shipper_product_id;

      private final @Nonnull String shipper_product_name;

      private final @Nullable String shipper_product_desc;

      private final int is_show_map;

      private final int price;

      private final @Nullable String formatted_price;

      private final @Nonnull String etd;

      private final int min_etd;

      private final int max_etd;

      private final @Nullable String check_sum;

      private final @Nullable String ut;

      private final @Nullable String max_hour_id;

      private final @Nullable String desc_hour_id;

      private final @Nullable String max_hour;

      private final @Nullable String desc_hour;

      private final @Nullable Integer insurance_price;

      private final @Nullable Integer insurance_type;

      private final @Nullable String insurance_type_info;

      private final @Nullable Integer weight_product;

      private final @Nullable Integer weight_order_spid;

      private final @Nullable Integer insurance_used_type;

      private final @Nullable String insurance_used_info;

      private final @Nullable Integer insurance_used_default;

      public Product(@Nullable Integer service_id, @Nullable Integer group_code,
          @Nullable String service_desc, @Nullable String shipper_name,
          @Nullable Integer shipper_id, int shipper_product_id,
          @Nonnull String shipper_product_name, @Nullable String shipper_product_desc,
          int is_show_map, int price, @Nullable String formatted_price, @Nonnull String etd,
          int min_etd, int max_etd, @Nullable String check_sum, @Nullable String ut,
          @Nullable String max_hour_id, @Nullable String desc_hour_id, @Nullable String max_hour,
          @Nullable String desc_hour, @Nullable Integer insurance_price,
          @Nullable Integer insurance_type, @Nullable String insurance_type_info,
          @Nullable Integer weight_product, @Nullable Integer weight_order_spid,
          @Nullable Integer insurance_used_type, @Nullable String insurance_used_info,
          @Nullable Integer insurance_used_default) {
        this.service_id = service_id;
        this.group_code = group_code;
        this.service_desc = service_desc;
        this.shipper_name = shipper_name;
        this.shipper_id = shipper_id;
        this.shipper_product_id = shipper_product_id;
        this.shipper_product_name = shipper_product_name;
        this.shipper_product_desc = shipper_product_desc;
        this.is_show_map = is_show_map;
        this.price = price;
        this.formatted_price = formatted_price;
        this.etd = etd;
        this.min_etd = min_etd;
        this.max_etd = max_etd;
        this.check_sum = check_sum;
        this.ut = ut;
        this.max_hour_id = max_hour_id;
        this.desc_hour_id = desc_hour_id;
        this.max_hour = max_hour;
        this.desc_hour = desc_hour;
        this.insurance_price = insurance_price;
        this.insurance_type = insurance_type;
        this.insurance_type_info = insurance_type_info;
        this.weight_product = weight_product;
        this.weight_order_spid = weight_order_spid;
        this.insurance_used_type = insurance_used_type;
        this.insurance_used_info = insurance_used_info;
        this.insurance_used_default = insurance_used_default;
      }

      public @Nullable Integer service_id() {
        return this.service_id;
      }

      public @Nullable Integer group_code() {
        return this.group_code;
      }

      public @Nullable String service_desc() {
        return this.service_desc;
      }

      public @Nullable String shipper_name() {
        return this.shipper_name;
      }

      public @Nullable Integer shipper_id() {
        return this.shipper_id;
      }

      public int shipper_product_id() {
        return this.shipper_product_id;
      }

      public @Nonnull String shipper_product_name() {
        return this.shipper_product_name;
      }

      public @Nullable String shipper_product_desc() {
        return this.shipper_product_desc;
      }

      public int is_show_map() {
        return this.is_show_map;
      }

      public int price() {
        return this.price;
      }

      public @Nullable String formatted_price() {
        return this.formatted_price;
      }

      public @Nonnull String etd() {
        return this.etd;
      }

      public int min_etd() {
        return this.min_etd;
      }

      public int max_etd() {
        return this.max_etd;
      }

      public @Nullable String check_sum() {
        return this.check_sum;
      }

      public @Nullable String ut() {
        return this.ut;
      }

      public @Nullable String max_hour_id() {
        return this.max_hour_id;
      }

      public @Nullable String desc_hour_id() {
        return this.desc_hour_id;
      }

      public @Nullable String max_hour() {
        return this.max_hour;
      }

      public @Nullable String desc_hour() {
        return this.desc_hour;
      }

      public @Nullable Integer insurance_price() {
        return this.insurance_price;
      }

      public @Nullable Integer insurance_type() {
        return this.insurance_type;
      }

      public @Nullable String insurance_type_info() {
        return this.insurance_type_info;
      }

      public @Nullable Integer weight_product() {
        return this.weight_product;
      }

      public @Nullable Integer weight_order_spid() {
        return this.weight_order_spid;
      }

      public @Nullable Integer insurance_used_type() {
        return this.insurance_used_type;
      }

      public @Nullable String insurance_used_info() {
        return this.insurance_used_info;
      }

      public @Nullable Integer insurance_used_default() {
        return this.insurance_used_default;
      }

      @Override
      public String toString() {
        return "Product{"
          + "service_id=" + service_id + ", "
          + "group_code=" + group_code + ", "
          + "service_desc=" + service_desc + ", "
          + "shipper_name=" + shipper_name + ", "
          + "shipper_id=" + shipper_id + ", "
          + "shipper_product_id=" + shipper_product_id + ", "
          + "shipper_product_name=" + shipper_product_name + ", "
          + "shipper_product_desc=" + shipper_product_desc + ", "
          + "is_show_map=" + is_show_map + ", "
          + "price=" + price + ", "
          + "formatted_price=" + formatted_price + ", "
          + "etd=" + etd + ", "
          + "min_etd=" + min_etd + ", "
          + "max_etd=" + max_etd + ", "
          + "check_sum=" + check_sum + ", "
          + "ut=" + ut + ", "
          + "max_hour_id=" + max_hour_id + ", "
          + "desc_hour_id=" + desc_hour_id + ", "
          + "max_hour=" + max_hour + ", "
          + "desc_hour=" + desc_hour + ", "
          + "insurance_price=" + insurance_price + ", "
          + "insurance_type=" + insurance_type + ", "
          + "insurance_type_info=" + insurance_type_info + ", "
          + "weight_product=" + weight_product + ", "
          + "weight_order_spid=" + weight_order_spid + ", "
          + "insurance_used_type=" + insurance_used_type + ", "
          + "insurance_used_info=" + insurance_used_info + ", "
          + "insurance_used_default=" + insurance_used_default
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Product) {
          Product that = (Product) o;
          return ((this.service_id == null) ? (that.service_id == null) : this.service_id.equals(that.service_id))
           && ((this.group_code == null) ? (that.group_code == null) : this.group_code.equals(that.group_code))
           && ((this.service_desc == null) ? (that.service_desc == null) : this.service_desc.equals(that.service_desc))
           && ((this.shipper_name == null) ? (that.shipper_name == null) : this.shipper_name.equals(that.shipper_name))
           && ((this.shipper_id == null) ? (that.shipper_id == null) : this.shipper_id.equals(that.shipper_id))
           && this.shipper_product_id == that.shipper_product_id
           && ((this.shipper_product_name == null) ? (that.shipper_product_name == null) : this.shipper_product_name.equals(that.shipper_product_name))
           && ((this.shipper_product_desc == null) ? (that.shipper_product_desc == null) : this.shipper_product_desc.equals(that.shipper_product_desc))
           && this.is_show_map == that.is_show_map
           && this.price == that.price
           && ((this.formatted_price == null) ? (that.formatted_price == null) : this.formatted_price.equals(that.formatted_price))
           && ((this.etd == null) ? (that.etd == null) : this.etd.equals(that.etd))
           && this.min_etd == that.min_etd
           && this.max_etd == that.max_etd
           && ((this.check_sum == null) ? (that.check_sum == null) : this.check_sum.equals(that.check_sum))
           && ((this.ut == null) ? (that.ut == null) : this.ut.equals(that.ut))
           && ((this.max_hour_id == null) ? (that.max_hour_id == null) : this.max_hour_id.equals(that.max_hour_id))
           && ((this.desc_hour_id == null) ? (that.desc_hour_id == null) : this.desc_hour_id.equals(that.desc_hour_id))
           && ((this.max_hour == null) ? (that.max_hour == null) : this.max_hour.equals(that.max_hour))
           && ((this.desc_hour == null) ? (that.desc_hour == null) : this.desc_hour.equals(that.desc_hour))
           && ((this.insurance_price == null) ? (that.insurance_price == null) : this.insurance_price.equals(that.insurance_price))
           && ((this.insurance_type == null) ? (that.insurance_type == null) : this.insurance_type.equals(that.insurance_type))
           && ((this.insurance_type_info == null) ? (that.insurance_type_info == null) : this.insurance_type_info.equals(that.insurance_type_info))
           && ((this.weight_product == null) ? (that.weight_product == null) : this.weight_product.equals(that.weight_product))
           && ((this.weight_order_spid == null) ? (that.weight_order_spid == null) : this.weight_order_spid.equals(that.weight_order_spid))
           && ((this.insurance_used_type == null) ? (that.insurance_used_type == null) : this.insurance_used_type.equals(that.insurance_used_type))
           && ((this.insurance_used_info == null) ? (that.insurance_used_info == null) : this.insurance_used_info.equals(that.insurance_used_info))
           && ((this.insurance_used_default == null) ? (that.insurance_used_default == null) : this.insurance_used_default.equals(that.insurance_used_default));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (service_id == null) ? 0 : service_id.hashCode();
        h *= 1000003;
        h ^= (group_code == null) ? 0 : group_code.hashCode();
        h *= 1000003;
        h ^= (service_desc == null) ? 0 : service_desc.hashCode();
        h *= 1000003;
        h ^= (shipper_name == null) ? 0 : shipper_name.hashCode();
        h *= 1000003;
        h ^= (shipper_id == null) ? 0 : shipper_id.hashCode();
        h *= 1000003;
        h ^= shipper_product_id;
        h *= 1000003;
        h ^= (shipper_product_name == null) ? 0 : shipper_product_name.hashCode();
        h *= 1000003;
        h ^= (shipper_product_desc == null) ? 0 : shipper_product_desc.hashCode();
        h *= 1000003;
        h ^= is_show_map;
        h *= 1000003;
        h ^= price;
        h *= 1000003;
        h ^= (formatted_price == null) ? 0 : formatted_price.hashCode();
        h *= 1000003;
        h ^= (etd == null) ? 0 : etd.hashCode();
        h *= 1000003;
        h ^= min_etd;
        h *= 1000003;
        h ^= max_etd;
        h *= 1000003;
        h ^= (check_sum == null) ? 0 : check_sum.hashCode();
        h *= 1000003;
        h ^= (ut == null) ? 0 : ut.hashCode();
        h *= 1000003;
        h ^= (max_hour_id == null) ? 0 : max_hour_id.hashCode();
        h *= 1000003;
        h ^= (desc_hour_id == null) ? 0 : desc_hour_id.hashCode();
        h *= 1000003;
        h ^= (max_hour == null) ? 0 : max_hour.hashCode();
        h *= 1000003;
        h ^= (desc_hour == null) ? 0 : desc_hour.hashCode();
        h *= 1000003;
        h ^= (insurance_price == null) ? 0 : insurance_price.hashCode();
        h *= 1000003;
        h ^= (insurance_type == null) ? 0 : insurance_type.hashCode();
        h *= 1000003;
        h ^= (insurance_type_info == null) ? 0 : insurance_type_info.hashCode();
        h *= 1000003;
        h ^= (weight_product == null) ? 0 : weight_product.hashCode();
        h *= 1000003;
        h ^= (weight_order_spid == null) ? 0 : weight_order_spid.hashCode();
        h *= 1000003;
        h ^= (insurance_used_type == null) ? 0 : insurance_used_type.hashCode();
        h *= 1000003;
        h ^= (insurance_used_info == null) ? 0 : insurance_used_info.hashCode();
        h *= 1000003;
        h ^= (insurance_used_default == null) ? 0 : insurance_used_default.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Product> {
        final Field[] fields = {
          Field.forInt("service_id", "service_id", null, true),
          Field.forInt("group_code", "group_code", null, true),
          Field.forString("service_desc", "service_desc", null, true),
          Field.forString("shipper_name", "shipper_name", null, true),
          Field.forInt("shipper_id", "shipper_id", null, true),
          Field.forInt("shipper_product_id", "shipper_product_id", null, false),
          Field.forString("shipper_product_name", "shipper_product_name", null, false),
          Field.forString("shipper_product_desc", "shipper_product_desc", null, true),
          Field.forInt("is_show_map", "is_show_map", null, false),
          Field.forInt("price", "price", null, false),
          Field.forString("formatted_price", "formatted_price", null, true),
          Field.forString("etd", "etd", null, false),
          Field.forInt("min_etd", "min_etd", null, false),
          Field.forInt("max_etd", "max_etd", null, false),
          Field.forString("check_sum", "check_sum", null, true),
          Field.forString("ut", "ut", null, true),
          Field.forString("max_hour_id", "max_hour_id", null, true),
          Field.forString("desc_hour_id", "desc_hour_id", null, true),
          Field.forString("max_hour", "max_hour", null, true),
          Field.forString("desc_hour", "desc_hour", null, true),
          Field.forInt("insurance_price", "insurance_price", null, true),
          Field.forInt("insurance_type", "insurance_type", null, true),
          Field.forString("insurance_type_info", "insurance_type_info", null, true),
          Field.forInt("weight_product", "weight_product", null, true),
          Field.forInt("weight_order_spid", "weight_order_spid", null, true),
          Field.forInt("insurance_used_type", "insurance_used_type", null, true),
          Field.forString("insurance_used_info", "insurance_used_info", null, true),
          Field.forInt("insurance_used_default", "insurance_used_default", null, true)
        };

        @Override
        public Product map(ResponseReader reader) throws IOException {
          final Integer service_id = reader.read(fields[0]);
          final Integer group_code = reader.read(fields[1]);
          final String service_desc = reader.read(fields[2]);
          final String shipper_name = reader.read(fields[3]);
          final Integer shipper_id = reader.read(fields[4]);
          final int shipper_product_id = reader.read(fields[5]);
          final String shipper_product_name = reader.read(fields[6]);
          final String shipper_product_desc = reader.read(fields[7]);
          final int is_show_map = reader.read(fields[8]);
          final int price = reader.read(fields[9]);
          final String formatted_price = reader.read(fields[10]);
          final String etd = reader.read(fields[11]);
          final int min_etd = reader.read(fields[12]);
          final int max_etd = reader.read(fields[13]);
          final String check_sum = reader.read(fields[14]);
          final String ut = reader.read(fields[15]);
          final String max_hour_id = reader.read(fields[16]);
          final String desc_hour_id = reader.read(fields[17]);
          final String max_hour = reader.read(fields[18]);
          final String desc_hour = reader.read(fields[19]);
          final Integer insurance_price = reader.read(fields[20]);
          final Integer insurance_type = reader.read(fields[21]);
          final String insurance_type_info = reader.read(fields[22]);
          final Integer weight_product = reader.read(fields[23]);
          final Integer weight_order_spid = reader.read(fields[24]);
          final Integer insurance_used_type = reader.read(fields[25]);
          final String insurance_used_info = reader.read(fields[26]);
          final Integer insurance_used_default = reader.read(fields[27]);
          return new Product(service_id, group_code, service_desc, shipper_name, shipper_id, shipper_product_id, shipper_product_name, shipper_product_desc, is_show_map, price, formatted_price, etd, min_etd, max_etd, check_sum, ut, max_hour_id, desc_hour_id, max_hour, desc_hour, insurance_price, insurance_type, insurance_type_info, weight_product, weight_order_spid, insurance_used_type, insurance_used_info, insurance_used_default);
        }
      }
    }

    public static class Attribute {
      private final @Nullable Integer id;

      private final @Nullable Integer shipper_id;

      private final @Nullable String shipper_name;

      private final @Nullable Integer service_id;

      private final @Nullable String service_name;

      private final int origin_id;

      private final @Nullable String origin_name;

      private final @Nullable String origin_geoloc;

      private final @Nullable Integer origin_zip_code;

      private final int destination_id;

      private final @Nullable String destination_name;

      private final @Nullable Integer destination_zip_code;

      private final @Nullable String destination_geoloc;

      private final int weight;

      private final @Nonnull String service_etd;

      private final @Nullable Integer weight_service;

      private final @Nonnull List<Product> products;

      public Attribute(@Nullable Integer id, @Nullable Integer shipper_id,
          @Nullable String shipper_name, @Nullable Integer service_id,
          @Nullable String service_name, int origin_id, @Nullable String origin_name,
          @Nullable String origin_geoloc, @Nullable Integer origin_zip_code, int destination_id,
          @Nullable String destination_name, @Nullable Integer destination_zip_code,
          @Nullable String destination_geoloc, int weight, @Nonnull String service_etd,
          @Nullable Integer weight_service, @Nonnull List<Product> products) {
        this.id = id;
        this.shipper_id = shipper_id;
        this.shipper_name = shipper_name;
        this.service_id = service_id;
        this.service_name = service_name;
        this.origin_id = origin_id;
        this.origin_name = origin_name;
        this.origin_geoloc = origin_geoloc;
        this.origin_zip_code = origin_zip_code;
        this.destination_id = destination_id;
        this.destination_name = destination_name;
        this.destination_zip_code = destination_zip_code;
        this.destination_geoloc = destination_geoloc;
        this.weight = weight;
        this.service_etd = service_etd;
        this.weight_service = weight_service;
        this.products = products;
      }

      public @Nullable Integer id() {
        return this.id;
      }

      public @Nullable Integer shipper_id() {
        return this.shipper_id;
      }

      public @Nullable String shipper_name() {
        return this.shipper_name;
      }

      public @Nullable Integer service_id() {
        return this.service_id;
      }

      public @Nullable String service_name() {
        return this.service_name;
      }

      public int origin_id() {
        return this.origin_id;
      }

      public @Nullable String origin_name() {
        return this.origin_name;
      }

      public @Nullable String origin_geoloc() {
        return this.origin_geoloc;
      }

      public @Nullable Integer origin_zip_code() {
        return this.origin_zip_code;
      }

      public int destination_id() {
        return this.destination_id;
      }

      public @Nullable String destination_name() {
        return this.destination_name;
      }

      public @Nullable Integer destination_zip_code() {
        return this.destination_zip_code;
      }

      public @Nullable String destination_geoloc() {
        return this.destination_geoloc;
      }

      public int weight() {
        return this.weight;
      }

      public @Nonnull String service_etd() {
        return this.service_etd;
      }

      public @Nullable Integer weight_service() {
        return this.weight_service;
      }

      public @Nonnull List<Product> products() {
        return this.products;
      }

      @Override
      public String toString() {
        return "Attribute{"
          + "id=" + id + ", "
          + "shipper_id=" + shipper_id + ", "
          + "shipper_name=" + shipper_name + ", "
          + "service_id=" + service_id + ", "
          + "service_name=" + service_name + ", "
          + "origin_id=" + origin_id + ", "
          + "origin_name=" + origin_name + ", "
          + "origin_geoloc=" + origin_geoloc + ", "
          + "origin_zip_code=" + origin_zip_code + ", "
          + "destination_id=" + destination_id + ", "
          + "destination_name=" + destination_name + ", "
          + "destination_zip_code=" + destination_zip_code + ", "
          + "destination_geoloc=" + destination_geoloc + ", "
          + "weight=" + weight + ", "
          + "service_etd=" + service_etd + ", "
          + "weight_service=" + weight_service + ", "
          + "products=" + products
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Attribute) {
          Attribute that = (Attribute) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
           && ((this.shipper_id == null) ? (that.shipper_id == null) : this.shipper_id.equals(that.shipper_id))
           && ((this.shipper_name == null) ? (that.shipper_name == null) : this.shipper_name.equals(that.shipper_name))
           && ((this.service_id == null) ? (that.service_id == null) : this.service_id.equals(that.service_id))
           && ((this.service_name == null) ? (that.service_name == null) : this.service_name.equals(that.service_name))
           && this.origin_id == that.origin_id
           && ((this.origin_name == null) ? (that.origin_name == null) : this.origin_name.equals(that.origin_name))
           && ((this.origin_geoloc == null) ? (that.origin_geoloc == null) : this.origin_geoloc.equals(that.origin_geoloc))
           && ((this.origin_zip_code == null) ? (that.origin_zip_code == null) : this.origin_zip_code.equals(that.origin_zip_code))
           && this.destination_id == that.destination_id
           && ((this.destination_name == null) ? (that.destination_name == null) : this.destination_name.equals(that.destination_name))
           && ((this.destination_zip_code == null) ? (that.destination_zip_code == null) : this.destination_zip_code.equals(that.destination_zip_code))
           && ((this.destination_geoloc == null) ? (that.destination_geoloc == null) : this.destination_geoloc.equals(that.destination_geoloc))
           && this.weight == that.weight
           && ((this.service_etd == null) ? (that.service_etd == null) : this.service_etd.equals(that.service_etd))
           && ((this.weight_service == null) ? (that.weight_service == null) : this.weight_service.equals(that.weight_service))
           && ((this.products == null) ? (that.products == null) : this.products.equals(that.products));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (shipper_id == null) ? 0 : shipper_id.hashCode();
        h *= 1000003;
        h ^= (shipper_name == null) ? 0 : shipper_name.hashCode();
        h *= 1000003;
        h ^= (service_id == null) ? 0 : service_id.hashCode();
        h *= 1000003;
        h ^= (service_name == null) ? 0 : service_name.hashCode();
        h *= 1000003;
        h ^= origin_id;
        h *= 1000003;
        h ^= (origin_name == null) ? 0 : origin_name.hashCode();
        h *= 1000003;
        h ^= (origin_geoloc == null) ? 0 : origin_geoloc.hashCode();
        h *= 1000003;
        h ^= (origin_zip_code == null) ? 0 : origin_zip_code.hashCode();
        h *= 1000003;
        h ^= destination_id;
        h *= 1000003;
        h ^= (destination_name == null) ? 0 : destination_name.hashCode();
        h *= 1000003;
        h ^= (destination_zip_code == null) ? 0 : destination_zip_code.hashCode();
        h *= 1000003;
        h ^= (destination_geoloc == null) ? 0 : destination_geoloc.hashCode();
        h *= 1000003;
        h ^= weight;
        h *= 1000003;
        h ^= (service_etd == null) ? 0 : service_etd.hashCode();
        h *= 1000003;
        h ^= (weight_service == null) ? 0 : weight_service.hashCode();
        h *= 1000003;
        h ^= (products == null) ? 0 : products.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Attribute> {
        final Product.Mapper productFieldMapper = new Product.Mapper();

        final Field[] fields = {
          Field.forInt("id", "id", null, true),
          Field.forInt("shipper_id", "shipper_id", null, true),
          Field.forString("shipper_name", "shipper_name", null, true),
          Field.forInt("service_id", "service_id", null, true),
          Field.forString("service_name", "service_name", null, true),
          Field.forInt("origin_id", "origin_id", null, false),
          Field.forString("origin_name", "origin_name", null, true),
          Field.forString("origin_geoloc", "origin_geoloc", null, true),
          Field.forInt("origin_zip_code", "origin_zip_code", null, true),
          Field.forInt("destination_id", "destination_id", null, false),
          Field.forString("destination_name", "destination_name", null, true),
          Field.forInt("destination_zip_code", "destination_zip_code", null, true),
          Field.forString("destination_geoloc", "destination_geoloc", null, true),
          Field.forInt("weight", "weight", null, false),
          Field.forString("service_etd", "service_etd", null, false),
          Field.forInt("weight_service", "weight_service", null, true),
          Field.forList("products", "products", null, false, new Field.ObjectReader<Product>() {
            @Override public Product read(final ResponseReader reader) throws IOException {
              return productFieldMapper.map(reader);
            }
          })
        };

        @Override
        public Attribute map(ResponseReader reader) throws IOException {
          final Integer id = reader.read(fields[0]);
          final Integer shipper_id = reader.read(fields[1]);
          final String shipper_name = reader.read(fields[2]);
          final Integer service_id = reader.read(fields[3]);
          final String service_name = reader.read(fields[4]);
          final int origin_id = reader.read(fields[5]);
          final String origin_name = reader.read(fields[6]);
          final String origin_geoloc = reader.read(fields[7]);
          final Integer origin_zip_code = reader.read(fields[8]);
          final int destination_id = reader.read(fields[9]);
          final String destination_name = reader.read(fields[10]);
          final Integer destination_zip_code = reader.read(fields[11]);
          final String destination_geoloc = reader.read(fields[12]);
          final int weight = reader.read(fields[13]);
          final String service_etd = reader.read(fields[14]);
          final Integer weight_service = reader.read(fields[15]);
          final List<Product> products = reader.read(fields[16]);
          return new Attribute(id, shipper_id, shipper_name, service_id, service_name, origin_id, origin_name, origin_geoloc, origin_zip_code, destination_id, destination_name, destination_zip_code, destination_geoloc, weight, service_etd, weight_service, products);
        }
      }
    }

    public static class Rates {
      private final @Nonnull String id;

      private final @Nullable String type;

      private final @Nullable List<Attribute> attributes;

      public Rates(@Nonnull String id, @Nullable String type,
          @Nullable List<Attribute> attributes) {
        this.id = id;
        this.type = type;
        this.attributes = attributes;
      }

      public @Nonnull String id() {
        return this.id;
      }

      public @Nullable String type() {
        return this.type;
      }

      public @Nullable List<Attribute> attributes() {
        return this.attributes;
      }

      @Override
      public String toString() {
        return "{"
          + "id=" + id + ", "
          + "type=" + type + ", "
          + "attributes=" + attributes
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Rates) {
          Rates that = (Rates) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
           && ((this.type == null) ? (that.type == null) : this.type.equals(that.type))
           && ((this.attributes == null) ? (that.attributes == null) : this.attributes.equals(that.attributes));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (type == null) ? 0 : type.hashCode();
        h *= 1000003;
        h ^= (attributes == null) ? 0 : attributes.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Rates> {
        final Attribute.Mapper attributeFieldMapper = new Attribute.Mapper();

        final Field[] fields = {
          Field.forString("id", "id", null, false),
          Field.forString("type", "type", null, true),
          Field.forList("attributes", "attributes", null, true, new Field.ObjectReader<Attribute>() {
            @Override public Attribute read(final ResponseReader reader) throws IOException {
              return attributeFieldMapper.map(reader);
            }
          })
        };

        @Override
        public Rates map(ResponseReader reader) throws IOException {
          final String id = reader.read(fields[0]);
          final String type = reader.read(fields[1]);
          final List<Attribute> attributes = reader.read(fields[2]);
          return new Rates(id, type, attributes);
        }
      }
    }

    public static class Ongkir {
      private final @Nullable Rates rates;

      public Ongkir(@Nullable Rates rates) {
        this.rates = rates;
      }

      public @Nullable Rates rates() {
        return this.rates;
      }

      @Override
      public String toString() {
        return "Ongkir{"
          + "rates=" + rates
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Ongkir) {
          Ongkir that = (Ongkir) o;
          return ((this.rates == null) ? (that.rates == null) : this.rates.equals(that.rates));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (rates == null) ? 0 : rates.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Ongkir> {
        final Rates.Mapper ratesFieldMapper = new Rates.Mapper();

        final Field[] fields = {
          Field.forObject("rates", "rates", null, true, new Field.ObjectReader<Rates>() {
            @Override public Rates read(final ResponseReader reader) throws IOException {
              return ratesFieldMapper.map(reader);
            }
          })
        };

        @Override
        public Ongkir map(ResponseReader reader) throws IOException {
          final Rates rates = reader.read(fields[0]);
          return new Ongkir(rates);
        }
      }
    }
  }
}
