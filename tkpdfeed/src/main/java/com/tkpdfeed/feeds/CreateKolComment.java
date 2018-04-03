package com.tkpdfeed.feeds;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Operation;
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
import java.util.Map;
import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("Apollo GraphQL")
public final class CreateKolComment implements Mutation<CreateKolComment.Data, CreateKolComment.Data, CreateKolComment.Variables> {
  public static final String OPERATION_DEFINITION = "mutation CreateKolComment($idPost: Int!, $comment: String) {\n"
      + "  create_comment_kol(idPost: $idPost, comment: $comment) {\n"
      + "    __typename\n"
      + "    error\n"
      + "    data {\n"
      + "      __typename\n"
      + "      id\n"
      + "      user {\n"
      + "        __typename\n"
      + "        iskol\n"
      + "        id\n"
      + "        name\n"
      + "        photo\n"
      + "      }\n"
      + "      comment\n"
      + "      create_time\n"
      + "    }\n"
      + "  }\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final CreateKolComment.Variables variables;

  public CreateKolComment(int idPost, @Nullable String comment) {
    variables = new CreateKolComment.Variables(idPost, comment);
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public CreateKolComment.Data wrapData(CreateKolComment.Data data) {
    return data;
  }

  @Override
  public CreateKolComment.Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<CreateKolComment.Data> responseFieldMapper() {
    return new Data.Mapper();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Variables extends Operation.Variables {
    private final int idPost;

    private final @Nullable String comment;

    private final transient Map<String, Object> valueMap = new LinkedHashMap<>();

    Variables(int idPost, @Nullable String comment) {
      this.idPost = idPost;
      this.comment = comment;
      this.valueMap.put("idPost", idPost);
      this.valueMap.put("comment", comment);
    }

    public int idPost() {
      return idPost;
    }

    public @Nullable String comment() {
      return comment;
    }

    @Override
    public Map<String, Object> valueMap() {
      return Collections.unmodifiableMap(valueMap);
    }
  }

  public static final class Builder {
    private int idPost;

    private @Nullable String comment;

    Builder() {
    }

    public Builder idPost(int idPost) {
      this.idPost = idPost;
      return this;
    }

    public Builder comment(@Nullable String comment) {
      this.comment = comment;
      return this;
    }

    public CreateKolComment build() {
      return new CreateKolComment(idPost, comment);
    }
  }

  public static class Data implements Operation.Data {
    private final @Nullable Create_comment_kol create_comment_kol;

    public Data(@Nullable Create_comment_kol create_comment_kol) {
      this.create_comment_kol = create_comment_kol;
    }

    public @Nullable Create_comment_kol create_comment_kol() {
      return this.create_comment_kol;
    }

    @Override
    public String toString() {
      return "Data{"
        + "create_comment_kol=" + create_comment_kol
        + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Data) {
        Data that = (Data) o;
        return ((this.create_comment_kol == null) ? (that.create_comment_kol == null) : this.create_comment_kol.equals(that.create_comment_kol));
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h = 1;
      h *= 1000003;
      h ^= (create_comment_kol == null) ? 0 : create_comment_kol.hashCode();
      return h;
    }

    public static final class Mapper implements ResponseFieldMapper<Data> {
      final Create_comment_kol.Mapper create_comment_kolFieldMapper = new Create_comment_kol.Mapper();

      final Field[] fields = {
        Field.forObject("create_comment_kol", "create_comment_kol", new UnmodifiableMapBuilder<String, Object>(2)
          .put("comment", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "comment")
          .build())
          .put("idPost", new UnmodifiableMapBuilder<String, Object>(2)
            .put("kind", "Variable")
            .put("variableName", "idPost")
          .build())
        .build(), true, new Field.ObjectReader<Create_comment_kol>() {
          @Override public Create_comment_kol read(final ResponseReader reader) throws IOException {
            return create_comment_kolFieldMapper.map(reader);
          }
        })
      };

      @Override
      public Data map(ResponseReader reader) throws IOException {
        final Create_comment_kol create_comment_kol = reader.read(fields[0]);
        return new Data(create_comment_kol);
      }
    }

    public static class User {
      private final @Nullable Boolean iskol;

      private final @Nullable Integer id;

      private final @Nullable String name;

      private final @Nullable String photo;

      public User(@Nullable Boolean iskol, @Nullable Integer id, @Nullable String name,
          @Nullable String photo) {
        this.iskol = iskol;
        this.id = id;
        this.name = name;
        this.photo = photo;
      }

      public @Nullable Boolean iskol() {
        return this.iskol;
      }

      public @Nullable Integer id() {
        return this.id;
      }

      public @Nullable String name() {
        return this.name;
      }

      public @Nullable String photo() {
        return this.photo;
      }

      @Override
      public String toString() {
        return "User{"
          + "iskol=" + iskol + ", "
          + "id=" + id + ", "
          + "name=" + name + ", "
          + "photo=" + photo
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof User) {
          User that = (User) o;
          return ((this.iskol == null) ? (that.iskol == null) : this.iskol.equals(that.iskol))
           && ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
           && ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
           && ((this.photo == null) ? (that.photo == null) : this.photo.equals(that.photo));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (iskol == null) ? 0 : iskol.hashCode();
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (name == null) ? 0 : name.hashCode();
        h *= 1000003;
        h ^= (photo == null) ? 0 : photo.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<User> {
        final Field[] fields = {
          Field.forBoolean("iskol", "iskol", null, true),
          Field.forInt("id", "id", null, true),
          Field.forString("name", "name", null, true),
          Field.forString("photo", "photo", null, true)
        };

        @Override
        public User map(ResponseReader reader) throws IOException {
          final Boolean iskol = reader.read(fields[0]);
          final Integer id = reader.read(fields[1]);
          final String name = reader.read(fields[2]);
          final String photo = reader.read(fields[3]);
          return new User(iskol, id, name, photo);
        }
      }
    }

    public static class Data1 {
      private final @Nullable Integer id;

      private final @Nullable User user;

      private final @Nullable String comment;

      private final @Nullable String create_time;

      public Data1(@Nullable Integer id, @Nullable User user, @Nullable String comment,
          @Nullable String create_time) {
        this.id = id;
        this.user = user;
        this.comment = comment;
        this.create_time = create_time;
      }

      public @Nullable Integer id() {
        return this.id;
      }

      public @Nullable User user() {
        return this.user;
      }

      public @Nullable String comment() {
        return this.comment;
      }

      public @Nullable String create_time() {
        return this.create_time;
      }

      @Override
      public String toString() {
        return "Data1{"
          + "id=" + id + ", "
          + "user=" + user + ", "
          + "comment=" + comment + ", "
          + "create_time=" + create_time
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Data1) {
          Data1 that = (Data1) o;
          return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
           && ((this.user == null) ? (that.user == null) : this.user.equals(that.user))
           && ((this.comment == null) ? (that.comment == null) : this.comment.equals(that.comment))
           && ((this.create_time == null) ? (that.create_time == null) : this.create_time.equals(that.create_time));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : id.hashCode();
        h *= 1000003;
        h ^= (user == null) ? 0 : user.hashCode();
        h *= 1000003;
        h ^= (comment == null) ? 0 : comment.hashCode();
        h *= 1000003;
        h ^= (create_time == null) ? 0 : create_time.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Data1> {
        final User.Mapper userFieldMapper = new User.Mapper();

        final Field[] fields = {
          Field.forInt("id", "id", null, true),
          Field.forObject("user", "user", null, true, new Field.ObjectReader<User>() {
            @Override public User read(final ResponseReader reader) throws IOException {
              return userFieldMapper.map(reader);
            }
          }),
          Field.forString("comment", "comment", null, true),
          Field.forString("create_time", "create_time", null, true)
        };

        @Override
        public Data1 map(ResponseReader reader) throws IOException {
          final Integer id = reader.read(fields[0]);
          final User user = reader.read(fields[1]);
          final String comment = reader.read(fields[2]);
          final String create_time = reader.read(fields[3]);
          return new Data1(id, user, comment, create_time);
        }
      }
    }

    public static class Create_comment_kol {
      private final @Nullable String error;

      private final @Nullable Data1 data;

      public Create_comment_kol(@Nullable String error, @Nullable Data1 data) {
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
        return "Create_comment_kol{"
          + "error=" + error + ", "
          + "data=" + data
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Create_comment_kol) {
          Create_comment_kol that = (Create_comment_kol) o;
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

      public static final class Mapper implements ResponseFieldMapper<Create_comment_kol> {
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
        public Create_comment_kol map(ResponseReader reader) throws IOException {
          final String error = reader.read(fields[0]);
          final Data1 data = reader.read(fields[1]);
          return new Create_comment_kol(error, data);
        }
      }
    }
  }
}
