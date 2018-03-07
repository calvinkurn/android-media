package com.tkpdfeed.feeds.model.type;

import com.apollographql.apollo.api.ScalarType;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated("Apollo GraphQL")
public enum CustomType implements ScalarType {
  URL {
    @Override
    public String typeName() {
      return "URL";
    }

    @Override
    public Class javaType() {
      return Object.class;
    }
  }
}
