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
public final class DeleteKolComment implements Mutation<DeleteKolComment.Data, DeleteKolComment.Data, DeleteKolComment.Variables> {
  public static final String OPERATION_DEFINITION = "mutation DeleteKolComment($idComment: Int!) {\n"
      + "  delete_comment_kol(idComment: $idComment) {\n"
      + "    __typename\n"
      + "    error\n"
      + "    data {\n"
      + "      __typename\n"
      + "      success\n"
      + "    }\n"
      + "  }\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final DeleteKolComment.Variables variables;

  public DeleteKolComment(int idComment) {
    variables = new DeleteKolComment.Variables(idComment);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public DeleteKolComment.Data wrapData(DeleteKolComment.Data data) {
    return data;
  }

  @Override
  public DeleteKolComment.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<DeleteKolComment.Data> responseFieldMapper() {
    return new Data.Mapper();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Variables extends Operation.Variables {
    private final int idComment;

    private final transient Map<String, Object> valueMap = new LinkedHashMap<>();

    Variables(int idComment) {
      this.idComment = idComment;
      this.valueMap.put("idComment", idComment);
    }

    public int idComment() {
      return idComment;
    }

    @Override
    public Map<String, Object> valueMap() {
      return Collections.unmodifiableMap(valueMap);
    }
  }

  public static final class Builder {
    private int idComment;

    Builder() {
    }

    public Builder idComment(int idComment) {
      this.idComment = idComment;
      return this;
    }

    public DeleteKolComment build() {
      return new DeleteKolComment(idComment);
    }
  }

  public static class Data implements Operation.Data {
    private final @Nullable Delete_comment_kol delete_comment_kol;

    public Data(@Nullable Delete_comment_kol delete_comment_kol) {
      this.delete_comment_kol = delete_comment_kol;
    }

    public @Nullable Delete_comment_kol delete_comment_kol() {
      return this.delete_comment_kol;
    }

    @Override
    public String toString() {
      return "Data{"
        + "delete_comment_kol=" + delete_comment_kol
        + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Data) {
        Data that = (Data) o;
        return ((this.delete_comment_kol == null) ? (that.delete_comment_kol == null) : this.delete_comment_kol.equals(that.delete_comment_kol));
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h = 1;
      h *= 1000003;
      h ^= (delete_comment_kol == null) ? 0 : delete_comment_kol.hashCode();
      return h;
    }

    public static final class Mapper implements ResponseFieldMapper<Data> {
      final Delete_comment_kol.Mapper delete_comment_kolFieldMapper = new Delete_comment_kol.Mapper();

      final Field[] fields = {
        Field.forObject("delete_comment_kol", "delete_comment_kol", new UnmodifiableMapBuilder<String, Object>(1)
          .put("idComment", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "idComment")
          .build())
        .build(), true, new Field.ObjectReader<Delete_comment_kol>() {
          @Override public Delete_comment_kol read(final ResponseReader reader) throws IOException {
            return delete_comment_kolFieldMapper.map(reader);
          }
        })
      };

      @Override
      public Data map(ResponseReader reader) throws IOException {
        final Delete_comment_kol delete_comment_kol = reader.read(fields[0]);
        return new Data(delete_comment_kol);
      }
    }

    public static class Data1 {
      private final @Nullable Integer success;

      public Data1(@Nullable Integer success) {
        this.success = success;
      }

      public @Nullable Integer success() {
        return this.success;
      }

      @Override
      public String toString() {
        return "Data1{"
          + "success=" + success
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Data1) {
          Data1 that = (Data1) o;
          return ((this.success == null) ? (that.success == null) : this.success.equals(that.success));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (success == null) ? 0 : success.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Data1> {
        final Field[] fields = {
          Field.forInt("success", "success", null, true)
        };

        @Override
        public Data1 map(ResponseReader reader) throws IOException {
          final Integer success = reader.read(fields[0]);
          return new Data1(success);
        }
      }
    }

    public static class Delete_comment_kol {
      private final @Nullable String error;

      private final @Nullable Data1 data;

      public Delete_comment_kol(@Nullable String error, @Nullable Data1 data) {
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
        return "Delete_comment_kol{"
          + "error=" + error + ", "
          + "data=" + data
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Delete_comment_kol) {
          Delete_comment_kol that = (Delete_comment_kol) o;
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

      public static final class Mapper implements ResponseFieldMapper<Delete_comment_kol> {
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
        public Delete_comment_kol map(ResponseReader reader) throws IOException {
          final String error = reader.read(fields[0]);
          final Data1 data = reader.read(fields[1]);
          return new Delete_comment_kol(error, data);
        }
      }
    }
  }
}
