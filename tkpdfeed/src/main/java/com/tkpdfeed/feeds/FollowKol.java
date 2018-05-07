package com.tkpdfeed.feeds;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseReader;
import com.apollographql.apollo.api.internal.UnmodifiableMapBuilder;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("Apollo GraphQL")
public final class FollowKol implements Mutation<FollowKol.Data, FollowKol.Data, FollowKol.Variables> {
  public static final String OPERATION_DEFINITION = "mutation FollowKol($userID: Int!, $action: Int!) {\n"
      + "  do_follow_kol(userID: $userID, action: $action) {\n"
      + "    __typename\n"
      + "    error\n"
      + "    data {\n"
      + "      __typename\n"
      + "      status\n"
      + "    }\n"
      + "  }\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final FollowKol.Variables variables;

  public FollowKol(int userID, int action) {
    variables = new FollowKol.Variables(userID, action);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public FollowKol.Data wrapData(FollowKol.Data data) {
    return data;
  }

  @Override
  public FollowKol.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<FollowKol.Data> responseFieldMapper() {
    return new Data.Mapper();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Variables extends Operation.Variables {
    private final int userID;

    private final int action;

    private final transient Map<String, Object> valueMap = new LinkedHashMap<>();

    Variables(int userID, int action) {
      this.userID = userID;
      this.action = action;
      this.valueMap.put("userID", userID);
      this.valueMap.put("action", action);
    }

    public int userID() {
      return userID;
    }

    public int action() {
      return action;
    }

    @Override
    public Map<String, Object> valueMap() {
      return Collections.unmodifiableMap(valueMap);
    }
  }

  public static final class Builder {
    private int userID;

    private int action;

    Builder() {
    }

    public Builder userID(int userID) {
      this.userID = userID;
      return this;
    }

    public Builder action(int action) {
      this.action = action;
      return this;
    }

    public FollowKol build() {
      return new FollowKol(userID, action);
    }
  }

  public static class Data implements Operation.Data {
    private final @Nullable Do_follow_kol do_follow_kol;

    public Data(@Nullable Do_follow_kol do_follow_kol) {
      this.do_follow_kol = do_follow_kol;
    }

    public @Nullable Do_follow_kol do_follow_kol() {
      return this.do_follow_kol;
    }

    @Override
    public String toString() {
      return "Data{"
        + "do_follow_kol=" + do_follow_kol
        + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Data) {
        Data that = (Data) o;
        return ((this.do_follow_kol == null) ? (that.do_follow_kol == null) : this.do_follow_kol.equals(that.do_follow_kol));
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h = 1;
      h *= 1000003;
      h ^= (do_follow_kol == null) ? 0 : do_follow_kol.hashCode();
      return h;
    }

    public static final class Mapper implements ResponseFieldMapper<Data> {
      final Do_follow_kol.Mapper do_follow_kolFieldMapper = new Do_follow_kol.Mapper();

      final Field[] fields = {
        Field.forObject("do_follow_kol", "do_follow_kol", new UnmodifiableMapBuilder<String, Object>(2)
          .put("action", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "action")
          .build())
          .put("userID", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "userID")
          .build())
        .build(), true, new Field.ObjectReader<Do_follow_kol>() {
          @Override public Do_follow_kol read(final ResponseReader reader) throws IOException {
            return do_follow_kolFieldMapper.map(reader);
          }
        })
      };

      @Override
      public Data map(ResponseReader reader) throws IOException {
        final Do_follow_kol do_follow_kol = reader.read(fields[0]);
        return new Data(do_follow_kol);
      }
    }

    public static class Data1 {
      private final @Nullable Integer status;

      public Data1(@Nullable Integer status) {
        this.status = status;
      }

      public @Nullable Integer status() {
        return this.status;
      }

      @Override
      public String toString() {
        return "Data1{"
          + "status=" + status
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Data1) {
          Data1 that = (Data1) o;
          return ((this.status == null) ? (that.status == null) : this.status.equals(that.status));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (status == null) ? 0 : status.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Data1> {
        final Field[] fields = {
          Field.forInt("status", "status", null, true)
        };

        @Override
        public Data1 map(ResponseReader reader) throws IOException {
          final Integer status = reader.read(fields[0]);
          return new Data1(status);
        }
      }
    }

    public static class Do_follow_kol {
      private final @Nullable String error;

      private final @Nullable Data1 data;

      public Do_follow_kol(@Nullable String error, @Nullable Data1 data) {
        this.error = error;
        this.data = data;
      }

      public @Nullable String error() {
        return this.error;
      }

      public @Nullable Data1 data() {
        return this.data;
      }

      @Override
      public String toString() {
        return "Do_follow_kol{"
          + "error=" + error + ", "
          + "data=" + data
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Do_follow_kol) {
          Do_follow_kol that = (Do_follow_kol) o;
          return ((this.error == null) ? (that.error == null) : this.error.equals(that.error))
           && ((this.data == null) ? (that.data == null) : this.data.equals(that.data));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (error == null) ? 0 : error.hashCode();
        h *= 1000003;
        h ^= (data == null) ? 0 : data.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Do_follow_kol> {
        final Data1.Mapper data1FieldMapper = new Data1.Mapper();

        final Field[] fields = {
          Field.forString("error", "error", null, true),
          Field.forObject("data", "data", null, true, new Field.ObjectReader<Data1>() {
            @Override public Data1 read(final ResponseReader reader) throws IOException {
              return data1FieldMapper.map(reader);
            }
          })
        };

        @Override
        public Do_follow_kol map(ResponseReader reader) throws IOException {
          final String error = reader.read(fields[0]);
          final Data1 data = reader.read(fields[1]);
          return new Do_follow_kol(error, data);
        }
      }
    }
  }
}
