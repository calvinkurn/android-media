package com.tokopedia.transaction.graphql.logistics;

import java.lang.IllegalStateException;
import java.lang.String;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("Apollo GraphQL")
public final class OngkirRatesInput {
  private final @Nonnull String names;

  private final @Nonnull String origin;

  private final @Nonnull String destination;

  private final @Nonnull String weight;

  private final @Nonnull String token;

  private final @Nonnull String ut;

  private final @Nullable String from;

  private final @Nonnull String insurance;

  private final @Nonnull String product_insurance;

  private final @Nonnull String order_value;

  private final @Nonnull String cat_id;

  OngkirRatesInput(@Nonnull String names, @Nonnull String origin, @Nonnull String destination,
      @Nonnull String weight, @Nonnull String token, @Nonnull String ut, @Nullable String from,
      @Nonnull String insurance, @Nonnull String product_insurance, @Nonnull String order_value,
      @Nonnull String cat_id) {
    this.names = names;
    this.origin = origin;
    this.destination = destination;
    this.weight = weight;
    this.token = token;
    this.ut = ut;
    this.from = from;
    this.insurance = insurance;
    this.product_insurance = product_insurance;
    this.order_value = order_value;
    this.cat_id = cat_id;
  }

  public @Nonnull String names() {
    return this.names;
  }

  public @Nonnull String origin() {
    return this.origin;
  }

  public @Nonnull String destination() {
    return this.destination;
  }

  public @Nonnull String weight() {
    return this.weight;
  }

  public @Nonnull String token() {
    return this.token;
  }

  public @Nonnull String ut() {
    return this.ut;
  }

  public @Nullable String from() {
    return this.from;
  }

  public @Nonnull String insurance() {
    return this.insurance;
  }

  public @Nonnull String product_insurance() {
    return this.product_insurance;
  }

  public @Nonnull String order_value() {
    return this.order_value;
  }

  public @Nonnull String cat_id() {
    return this.cat_id;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private @Nonnull String names;

    private @Nonnull String origin;

    private @Nonnull String destination;

    private @Nonnull String weight;

    private @Nonnull String token;

    private @Nonnull String ut;

    private @Nullable String from;

    private @Nonnull String insurance;

    private @Nonnull String product_insurance;

    private @Nonnull String order_value;

    private @Nonnull String cat_id;

    Builder() {
    }

    public Builder names(@Nonnull String names) {
      this.names = names;
      return this;
    }

    public Builder origin(@Nonnull String origin) {
      this.origin = origin;
      return this;
    }

    public Builder destination(@Nonnull String destination) {
      this.destination = destination;
      return this;
    }

    public Builder weight(@Nonnull String weight) {
      this.weight = weight;
      return this;
    }

    public Builder token(@Nonnull String token) {
      this.token = token;
      return this;
    }

    public Builder ut(@Nonnull String ut) {
      this.ut = ut;
      return this;
    }

    public Builder from(@Nullable String from) {
      this.from = from;
      return this;
    }

    public Builder insurance(@Nonnull String insurance) {
      this.insurance = insurance;
      return this;
    }

    public Builder product_insurance(@Nonnull String product_insurance) {
      this.product_insurance = product_insurance;
      return this;
    }

    public Builder order_value(@Nonnull String order_value) {
      this.order_value = order_value;
      return this;
    }

    public Builder cat_id(@Nonnull String cat_id) {
      this.cat_id = cat_id;
      return this;
    }

    public OngkirRatesInput build() {
      if (names == null) throw new IllegalStateException("names can't be null");
      if (origin == null) throw new IllegalStateException("origin can't be null");
      if (destination == null) throw new IllegalStateException("destination can't be null");
      if (weight == null) throw new IllegalStateException("weight can't be null");
      if (token == null) throw new IllegalStateException("token can't be null");
      if (ut == null) throw new IllegalStateException("ut can't be null");
      if (insurance == null) throw new IllegalStateException("insurance can't be null");
      if (product_insurance == null) throw new IllegalStateException("product_insurance can't be null");
      if (order_value == null) throw new IllegalStateException("order_value can't be null");
      if (cat_id == null) throw new IllegalStateException("cat_id can't be null");
      return new OngkirRatesInput(names, origin, destination, weight, token, ut, from, insurance, product_insurance, order_value, cat_id);
    }
  }
}
