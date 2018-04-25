package com.tkpdfeed.feeds;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseReader;
import com.apollographql.apollo.api.internal.UnmodifiableMapBuilder;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("Apollo GraphQL")
public final class GetKolComments implements Query<GetKolComments.Data, GetKolComments.Data, GetKolComments.Variables> {
  public static final String OPERATION_DEFINITION = "query GetKolComments($idPost: Int!, $cursor: String, $limit: Int!) {\n"
      + "  get_kol_list_comment(idPost: $idPost, cursor: $cursor, limit: $limit) {\n"
      + "    __typename\n"
      + "    error\n"
      + "    data {\n"
      + "      __typename\n"
      + "      lastcursor\n"
      + "      total_data\n"
      + "      has_next_page\n"
      + "      comment {\n"
      + "        __typename\n"
      + "        id\n"
      + "        userID\n"
      + "        userName\n"
      + "        userPhoto\n"
      + "        comment\n"
      + "        create_time\n"
      + "        isKol\n"
      + "        isCommentOwner\n"
      + "      }\n"
      + "    }\n"
      + "  }\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final GetKolComments.Variables variables;

  public GetKolComments(int idPost, @Nullable String cursor, int limit) {
    variables = new GetKolComments.Variables(idPost, cursor, limit);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public GetKolComments.Data wrapData(GetKolComments.Data data) {
    return data;
  }

  @Override
  public GetKolComments.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<GetKolComments.Data> responseFieldMapper() {
    return new Data.Mapper();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Variables extends Operation.Variables {
    private final int idPost;

    private final @Nullable String cursor;

    private final int limit;

    private final transient Map<String, Object> valueMap = new LinkedHashMap<>();

    Variables(int idPost, @Nullable String cursor, int limit) {
      this.idPost = idPost;
      this.cursor = cursor;
      this.limit = limit;
      this.valueMap.put("idPost", idPost);
      this.valueMap.put("cursor", cursor);
      this.valueMap.put("limit", limit);
    }

    public int idPost() {
      return idPost;
    }

    public @Nullable String cursor() {
      return cursor;
    }

    public int limit() {
      return limit;
    }

    @Override
    public Map<String, Object> valueMap() {
      return Collections.unmodifiableMap(valueMap);
    }
  }

  public static final class Builder {
    private int idPost;

    private @Nullable String cursor;

    private int limit;

    Builder() {
    }

    public Builder idPost(int idPost) {
      this.idPost = idPost;
      return this;
    }

    public Builder cursor(@Nullable String cursor) {
      this.cursor = cursor;
      return this;
    }

    public Builder limit(int limit) {
      this.limit = limit;
      return this;
    }

    public GetKolComments build() {
      return new GetKolComments(idPost, cursor, limit);
    }
  }

  public static class Data implements Operation.Data {
    private final @Nullable Get_kol_list_comment get_kol_list_comment;

    public Data(@Nullable Get_kol_list_comment get_kol_list_comment) {
      this.get_kol_list_comment = get_kol_list_comment;
    }

    public @Nullable Get_kol_list_comment get_kol_list_comment() {
      return this.get_kol_list_comment;
    }

    @Override
    public String toString() {
      return "Data{"
        + "get_kol_list_comment=" + get_kol_list_comment
        + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Data) {
        Data that = (Data) o;
        return ((this.get_kol_list_comment == null) ? (that.get_kol_list_comment == null) : this.get_kol_list_comment.equals(that.get_kol_list_comment));
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h = 1;
      h *= 1000003;
      h ^= (get_kol_list_comment == null) ? 0 : get_kol_list_comment.hashCode();
      return h;
    }

    public static final class Mapper implements ResponseFieldMapper<Data> {
      final Get_kol_list_comment.Mapper get_kol_list_commentFieldMapper = new Get_kol_list_comment.Mapper();

      final Field[] fields = {
        Field.forObject("get_kol_list_comment", "get_kol_list_comment", new UnmodifiableMapBuilder<String, Object>(3)
          .put("cursor", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "cursor")
          .build())
          .put("limit", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "limit")
          .build())
          .put("idPost", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "idPost")
          .build())
        .build(), true, new Field.ObjectReader<Get_kol_list_comment>() {
          @Override public Get_kol_list_comment read(final ResponseReader reader) throws IOException {
            return get_kol_list_commentFieldMapper.map(reader);
          }
        })
      };

      @Override
      public Data map(ResponseReader reader) throws IOException {
        final Get_kol_list_comment get_kol_list_comment = reader.read(fields[0]);
        return new Data(get_kol_list_comment);
      }
    }

    public static class Comment {
      private final @Nullable Integer id;

      private final @Nullable Integer userID;

      private final @Nullable String userName;

      private final @Nullable String userPhoto;

      private final @Nullable String comment;

      private final @Nullable String create_time;

      private final @Nullable Boolean isKol;

      private final @Nullable Boolean isCommentOwner;

      public Comment(@Nullable Integer id, @Nullable Integer userID, @Nullable String userName,
          @Nullable String userPhoto, @Nullable String comment, @Nullable String create_time,
          @Nullable Boolean isKol, @Nullable Boolean isCommentOwner) {
        this.id = id;
        this.userID = userID;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.comment = comment;
        this.create_time = create_time;
        this.isKol = isKol;
        this.isCommentOwner = isCommentOwner;
      }

      public @Nullable Integer id() {
        return this.id;
      }

      public @Nullable Integer userID() {
        return this.userID;
      }

      public @Nullable String userName() {
        return this.userName;
      }

      public @Nullable String userPhoto() {
        return this.userPhoto;
      }

      public @Nullable String comment() {
        return this.comment;
      }

      public @Nullable String create_time() {
        return this.create_time;
      }

      public @Nullable Boolean isKol() {
        return this.isKol;
      }

      public @Nullable Boolean isCommentOwner() {
        return this.isCommentOwner;
      }

      @Override
      public String toString() {
        return "Comment{"
          + "id=" + id + ", "
          + "userID=" + userID + ", "
          + "userName=" + userName + ", "
          + "userPhoto=" + userPhoto + ", "
          + "comment=" + comment + ", "
          + "create_time=" + create_time + ", "
          + "isKol=" + isKol + ", "
          + "isCommentOwner=" + isCommentOwner
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Comment) {
          Comment that = (Comment) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
           && ((this.userID == null) ? (that.userID == null) : this.userID.equals(that.userID))
           && ((this.userName == null) ? (that.userName == null) : this.userName.equals(that.userName))
           && ((this.userPhoto == null) ? (that.userPhoto == null) : this.userPhoto.equals(that.userPhoto))
           && ((this.comment == null) ? (that.comment == null) : this.comment.equals(that.comment))
           && ((this.create_time == null) ? (that.create_time == null) : this.create_time.equals(that.create_time))
           && ((this.isKol == null) ? (that.isKol == null) : this.isKol.equals(that.isKol))
           && ((this.isCommentOwner == null) ? (that.isCommentOwner == null) : this.isCommentOwner.equals(that.isCommentOwner));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (userID == null) ? 0 : userID.hashCode();
        h *= 1000003;
        h ^= (userName == null) ? 0 : userName.hashCode();
        h *= 1000003;
        h ^= (userPhoto == null) ? 0 : userPhoto.hashCode();
        h *= 1000003;
        h ^= (comment == null) ? 0 : comment.hashCode();
        h *= 1000003;
        h ^= (create_time == null) ? 0 : create_time.hashCode();
        h *= 1000003;
        h ^= (isKol == null) ? 0 : isKol.hashCode();
        h *= 1000003;
        h ^= (isCommentOwner == null) ? 0 : isCommentOwner.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Comment> {
        final Field[] fields = {
          Field.forInt("id", "id", null, true),
          Field.forInt("userID", "userID", null, true),
          Field.forString("userName", "userName", null, true),
          Field.forString("userPhoto", "userPhoto", null, true),
          Field.forString("comment", "comment", null, true),
          Field.forString("create_time", "create_time", null, true),
          Field.forBoolean("isKol", "isKol", null, true),
          Field.forBoolean("isCommentOwner", "isCommentOwner", null, true)
        };

        @Override
        public Comment map(ResponseReader reader) throws IOException {
          final Integer id = reader.read(fields[0]);
          final Integer userID = reader.read(fields[1]);
          final String userName = reader.read(fields[2]);
          final String userPhoto = reader.read(fields[3]);
          final String comment = reader.read(fields[4]);
          final String create_time = reader.read(fields[5]);
          final Boolean isKol = reader.read(fields[6]);
          final Boolean isCommentOwner = reader.read(fields[7]);
          return new Comment(id, userID, userName, userPhoto, comment, create_time, isKol, isCommentOwner);
        }
      }
    }

    public static class Data1 {
      private final @Nullable String lastcursor;

      private final @Nullable Integer total_data;

      private final @Nullable Boolean has_next_page;

      private final @Nullable List<Comment> comment;

      public Data1(@Nullable String lastcursor, @Nullable Integer total_data,
          @Nullable Boolean has_next_page, @Nullable List<Comment> comment) {
        this.lastcursor = lastcursor;
        this.total_data = total_data;
        this.has_next_page = has_next_page;
        this.comment = comment;
      }

      public @Nullable String lastcursor() {
        return this.lastcursor;
      }

      public @Nullable Integer total_data() {
        return this.total_data;
      }

      public @Nullable Boolean has_next_page() {
        return this.has_next_page;
      }

      public @Nullable List<Comment> comment() {
        return this.comment;
      }

      @Override
      public String toString() {
        return "Data1{"
          + "lastcursor=" + lastcursor + ", "
          + "total_data=" + total_data + ", "
          + "has_next_page=" + has_next_page + ", "
          + "comment=" + comment
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Data1) {
          Data1 that = (Data1) o;
          return ((this.lastcursor == null) ? (that.lastcursor == null) : this.lastcursor.equals(that.lastcursor))
           && ((this.total_data == null) ? (that.total_data == null) : this.total_data.equals(that.total_data))
           && ((this.has_next_page == null) ? (that.has_next_page == null) : this.has_next_page.equals(that.has_next_page))
           && ((this.comment == null) ? (that.comment == null) : this.comment.equals(that.comment));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (lastcursor == null) ? 0 : lastcursor.hashCode();
        h *= 1000003;
        h ^= (total_data == null) ? 0 : total_data.hashCode();
        h *= 1000003;
        h ^= (has_next_page == null) ? 0 : has_next_page.hashCode();
        h *= 1000003;
        h ^= (comment == null) ? 0 : comment.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Data1> {
        final Comment.Mapper commentFieldMapper = new Comment.Mapper();

        final Field[] fields = {
          Field.forString("lastcursor", "lastcursor", null, true),
          Field.forInt("total_data", "total_data", null, true),
          Field.forBoolean("has_next_page", "has_next_page", null, true),
          Field.forList("comment", "comment", null, true, new Field.ObjectReader<Comment>() {
            @Override public Comment read(final ResponseReader reader) throws IOException {
              return commentFieldMapper.map(reader);
            }
          })
        };

        @Override
        public Data1 map(ResponseReader reader) throws IOException {
          final String lastcursor = reader.read(fields[0]);
          final Integer total_data = reader.read(fields[1]);
          final Boolean has_next_page = reader.read(fields[2]);
          final List<Comment> comment = reader.read(fields[3]);
          return new Data1(lastcursor, total_data, has_next_page, comment);
        }
      }
    }

    public static class Get_kol_list_comment {
      private final @Nullable String error;

      private final @Nullable Data1 data;

      public Get_kol_list_comment(@Nullable String error, @Nullable Data1 data) {
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
        return "Get_kol_list_comment{"
          + "error=" + error + ", "
          + "data=" + data
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Get_kol_list_comment) {
          Get_kol_list_comment that = (Get_kol_list_comment) o;
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

      public static final class Mapper implements ResponseFieldMapper<Get_kol_list_comment> {
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
        public Get_kol_list_comment map(ResponseReader reader) throws IOException {
          final String error = reader.read(fields[0]);
          final Data1 data = reader.read(fields[1]);
          return new Get_kol_list_comment(error, data);
        }
      }
    }
  }
}
