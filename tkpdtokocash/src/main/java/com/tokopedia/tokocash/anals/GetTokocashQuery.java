package com.tokopedia.tokocash.anals;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseReader;

import java.io.IOException;
import java.util.List;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("Apollo GraphQL")
public final class GetTokocashQuery implements Query<GetTokocashQuery.Data, GetTokocashQuery.Data, Operation.Variables> {
  public static final String OPERATION_DEFINITION = "query GetTokocashQuery {\n"
      + "  wallet {\n"
      + "    __typename\n"
      + "    linked\n"
      + "    balance\n"
      + "    rawBalance\n"
      + "    errors {\n"
      + "      __typename\n"
      + "      name\n"
      + "      message\n"
      + "    }\n"
      + "    text\n"
      + "    total_balance\n"
      + "    raw_total_balance\n"
      + "    hold_balance\n"
      + "    raw_hold_balance\n"
      + "    redirect_url\n"
      + "    applinks\n"
      + "    ab_tags {\n"
      + "      __typename\n"
      + "      tag\n"
      + "    }\n"
      + "    action {\n"
      + "      __typename\n"
      + "      text\n"
      + "      redirect_url\n"
      + "      applinks\n"
      + "      visibility\n"
      + "    }\n"
      + "  }\n"
      + "}";

  public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

  private final Variables variables;

  public GetTokocashQuery() {
    this.variables = Operation.EMPTY_VARIABLES;
  }

  @Override
  public String queryDocument() {
    return QUERY_DOCUMENT;
  }

  @Override
  public GetTokocashQuery.Data wrapData(GetTokocashQuery.Data data) {
    return data;
  }

  @Override
  public Variables variables() {
    return variables;
  }

  @Override
  public ResponseFieldMapper<GetTokocashQuery.Data> responseFieldMapper() {
    return new Data.Mapper();
  }

  public static class Data implements Operation.Data {
    private final @Nullable Wallet wallet;

    public Data(@Nullable Wallet wallet) {
      this.wallet = wallet;
    }

    public @Nullable Wallet wallet() {
      return this.wallet;
    }

    @Override
    public String toString() {
      return "Data{"
        + "wallet=" + wallet
        + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Data) {
        Data that = (Data) o;
        return ((this.wallet == null) ? (that.wallet == null) : this.wallet.equals(that.wallet));
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h = 1;
      h *= 1000003;
      h ^= (wallet == null) ? 0 : wallet.hashCode();
      return h;
    }

    public static final class Mapper implements ResponseFieldMapper<Data> {
      final Wallet.Mapper walletFieldMapper = new Wallet.Mapper();

      final Field[] fields = {
        Field.forObject("wallet", "wallet", null, true, new Field.ObjectReader<Wallet>() {
          @Override public Wallet read(final ResponseReader reader) throws IOException {
            return walletFieldMapper.map(reader);
          }
        })
      };

      @Override
      public Data map(ResponseReader reader) throws IOException {
        final Wallet wallet = reader.read(fields[0]);
        return new Data(wallet);
      }
    }

    public static class Error {
      private final @Nullable String name;

      private final @Nullable String message;

      public Error(@Nullable String name, @Nullable String message) {
        this.name = name;
        this.message = message;
      }

      public @Nullable String name() {
        return this.name;
      }

      public @Nullable String message() {
        return this.message;
      }

      @Override
      public String toString() {
        return "Error{"
          + "name=" + name + ", "
          + "message=" + message
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Error) {
          Error that = (Error) o;
          return ((this.name == null) ? (that.name == null) : this.name.equals(that.name))
           && ((this.message == null) ? (that.message == null) : this.message.equals(that.message));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (name == null) ? 0 : name.hashCode();
        h *= 1000003;
        h ^= (message == null) ? 0 : message.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Error> {
        final Field[] fields = {
          Field.forString("name", "name", null, true),
          Field.forString("message", "message", null, true)
        };

        @Override
        public Error map(ResponseReader reader) throws IOException {
          final String name = reader.read(fields[0]);
          final String message = reader.read(fields[1]);
          return new Error(name, message);
        }
      }
    }

    public static class Ab_tag {
      private final @Nonnull String tag;

      public Ab_tag(@Nonnull String tag) {
        this.tag = tag;
      }

      public @Nonnull String tag() {
        return this.tag;
      }

      @Override
      public String toString() {
        return "Ab_tag{"
          + "tag=" + tag
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Ab_tag) {
          Ab_tag that = (Ab_tag) o;
          return ((this.tag == null) ? (that.tag == null) : this.tag.equals(that.tag));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (tag == null) ? 0 : tag.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Ab_tag> {
        final Field[] fields = {
          Field.forString("tag", "tag", null, false)
        };

        @Override
        public Ab_tag map(ResponseReader reader) throws IOException {
          final String tag = reader.read(fields[0]);
          return new Ab_tag(tag);
        }
      }
    }

    public static class Action {
      private final @Nonnull String text;

      private final @Nonnull String redirect_url;

      private final @Nonnull String applinks;

      private final @Nonnull String visibility;

      public Action(@Nonnull String text, @Nonnull String redirect_url, @Nonnull String applinks,
          @Nonnull String visibility) {
        this.text = text;
        this.redirect_url = redirect_url;
        this.applinks = applinks;
        this.visibility = visibility;
      }

      public @Nonnull String text() {
        return this.text;
      }

      public @Nonnull String redirect_url() {
        return this.redirect_url;
      }

      public @Nonnull String applinks() {
        return this.applinks;
      }

      public @Nonnull String visibility() {
        return this.visibility;
      }

      @Override
      public String toString() {
        return "Action{"
          + "text=" + text + ", "
          + "redirect_url=" + redirect_url + ", "
          + "applinks=" + applinks + ", "
          + "visibility=" + visibility
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Action) {
          Action that = (Action) o;
          return ((this.text == null) ? (that.text == null) : this.text.equals(that.text))
           && ((this.redirect_url == null) ? (that.redirect_url == null) : this.redirect_url.equals(that.redirect_url))
           && ((this.applinks == null) ? (that.applinks == null) : this.applinks.equals(that.applinks))
           && ((this.visibility == null) ? (that.visibility == null) : this.visibility.equals(that.visibility));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (text == null) ? 0 : text.hashCode();
        h *= 1000003;
        h ^= (redirect_url == null) ? 0 : redirect_url.hashCode();
        h *= 1000003;
        h ^= (applinks == null) ? 0 : applinks.hashCode();
        h *= 1000003;
        h ^= (visibility == null) ? 0 : visibility.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Action> {
        final Field[] fields = {
          Field.forString("text", "text", null, false),
          Field.forString("redirect_url", "redirect_url", null, false),
          Field.forString("applinks", "applinks", null, false),
          Field.forString("visibility", "visibility", null, false)
        };

        @Override
        public Action map(ResponseReader reader) throws IOException {
          final String text = reader.read(fields[0]);
          final String redirect_url = reader.read(fields[1]);
          final String applinks = reader.read(fields[2]);
          final String visibility = reader.read(fields[3]);
          return new Action(text, redirect_url, applinks, visibility);
        }
      }
    }

    public static class Wallet {
      private final boolean linked;

      private final @Nonnull String balance;

      private final int rawBalance;

      private final @Nullable List<Error> errors;

      private final @Nonnull String text;

      private final @Nonnull String total_balance;

      private final int raw_total_balance;

      private final @Nonnull String hold_balance;

      private final int raw_hold_balance;

      private final @Nonnull String redirect_url;

      private final @Nonnull String applinks;

      private final @Nullable List<Ab_tag> ab_tags;

      private final @Nonnull Action action;

      public Wallet(boolean linked, @Nonnull String balance, int rawBalance,
          @Nullable List<Error> errors, @Nonnull String text, @Nonnull String total_balance,
          int raw_total_balance, @Nonnull String hold_balance, int raw_hold_balance,
          @Nonnull String redirect_url, @Nonnull String applinks, @Nullable List<Ab_tag> ab_tags,
          @Nonnull Action action) {
        this.linked = linked;
        this.balance = balance;
        this.rawBalance = rawBalance;
        this.errors = errors;
        this.text = text;
        this.total_balance = total_balance;
        this.raw_total_balance = raw_total_balance;
        this.hold_balance = hold_balance;
        this.raw_hold_balance = raw_hold_balance;
        this.redirect_url = redirect_url;
        this.applinks = applinks;
        this.ab_tags = ab_tags;
        this.action = action;
      }

      public boolean linked() {
        return this.linked;
      }

      public @Nonnull String balance() {
        return this.balance;
      }

      public int rawBalance() {
        return this.rawBalance;
      }

      public @Nullable List<Error> errors() {
        return this.errors;
      }

      public @Nonnull String text() {
        return this.text;
      }

      public @Nonnull String total_balance() {
        return this.total_balance;
      }

      public int raw_total_balance() {
        return this.raw_total_balance;
      }

      public @Nonnull String hold_balance() {
        return this.hold_balance;
      }

      public int raw_hold_balance() {
        return this.raw_hold_balance;
      }

      public @Nonnull String redirect_url() {
        return this.redirect_url;
      }

      public @Nonnull String applinks() {
        return this.applinks;
      }

      public @Nullable List<Ab_tag> ab_tags() {
        return this.ab_tags;
      }

      public @Nonnull Action action() {
        return this.action;
      }

      @Override
      public String toString() {
        return "Wallet{"
          + "linked=" + linked + ", "
          + "balance=" + balance + ", "
          + "rawBalance=" + rawBalance + ", "
          + "errors=" + errors + ", "
          + "text=" + text + ", "
          + "total_balance=" + total_balance + ", "
          + "raw_total_balance=" + raw_total_balance + ", "
          + "hold_balance=" + hold_balance + ", "
          + "raw_hold_balance=" + raw_hold_balance + ", "
          + "redirect_url=" + redirect_url + ", "
          + "applinks=" + applinks + ", "
          + "ab_tags=" + ab_tags + ", "
          + "action=" + action
          + "}";
      }

      @Override
      public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        if (o instanceof Wallet) {
          Wallet that = (Wallet) o;
          return this.linked == that.linked
           && ((this.balance == null) ? (that.balance == null) : this.balance.equals(that.balance))
           && this.rawBalance == that.rawBalance
           && ((this.errors == null) ? (that.errors == null) : this.errors.equals(that.errors))
           && ((this.text == null) ? (that.text == null) : this.text.equals(that.text))
           && ((this.total_balance == null) ? (that.total_balance == null) : this.total_balance.equals(that.total_balance))
           && this.raw_total_balance == that.raw_total_balance
           && ((this.hold_balance == null) ? (that.hold_balance == null) : this.hold_balance.equals(that.hold_balance))
           && this.raw_hold_balance == that.raw_hold_balance
           && ((this.redirect_url == null) ? (that.redirect_url == null) : this.redirect_url.equals(that.redirect_url))
           && ((this.applinks == null) ? (that.applinks == null) : this.applinks.equals(that.applinks))
           && ((this.ab_tags == null) ? (that.ab_tags == null) : this.ab_tags.equals(that.ab_tags))
           && ((this.action == null) ? (that.action == null) : this.action.equals(that.action));
        }
        return false;
      }

      @Override
      public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= Boolean.valueOf(linked).hashCode();
        h *= 1000003;
        h ^= (balance == null) ? 0 : balance.hashCode();
        h *= 1000003;
        h ^= rawBalance;
        h *= 1000003;
        h ^= (errors == null) ? 0 : errors.hashCode();
        h *= 1000003;
        h ^= (text == null) ? 0 : text.hashCode();
        h *= 1000003;
        h ^= (total_balance == null) ? 0 : total_balance.hashCode();
        h *= 1000003;
        h ^= raw_total_balance;
        h *= 1000003;
        h ^= (hold_balance == null) ? 0 : hold_balance.hashCode();
        h *= 1000003;
        h ^= raw_hold_balance;
        h *= 1000003;
        h ^= (redirect_url == null) ? 0 : redirect_url.hashCode();
        h *= 1000003;
        h ^= (applinks == null) ? 0 : applinks.hashCode();
        h *= 1000003;
        h ^= (ab_tags == null) ? 0 : ab_tags.hashCode();
        h *= 1000003;
        h ^= (action == null) ? 0 : action.hashCode();
        return h;
      }

      public static final class Mapper implements ResponseFieldMapper<Wallet> {
        final Error.Mapper errorFieldMapper = new Error.Mapper();

        final Ab_tag.Mapper ab_tagFieldMapper = new Ab_tag.Mapper();

        final Action.Mapper actionFieldMapper = new Action.Mapper();

        final Field[] fields = {
          Field.forBoolean("linked", "linked", null, false),
          Field.forString("balance", "balance", null, false),
          Field.forInt("rawBalance", "rawBalance", null, false),
          Field.forList("errors", "errors", null, true, new Field.ObjectReader<Error>() {
            @Override public Error read(final ResponseReader reader) throws IOException {
              return errorFieldMapper.map(reader);
            }
          }),
          Field.forString("text", "text", null, false),
          Field.forString("total_balance", "total_balance", null, false),
          Field.forInt("raw_total_balance", "raw_total_balance", null, false),
          Field.forString("hold_balance", "hold_balance", null, false),
          Field.forInt("raw_hold_balance", "raw_hold_balance", null, false),
          Field.forString("redirect_url", "redirect_url", null, false),
          Field.forString("applinks", "applinks", null, false),
          Field.forList("ab_tags", "ab_tags", null, true, new Field.ObjectReader<Ab_tag>() {
            @Override public Ab_tag read(final ResponseReader reader) throws IOException {
              return ab_tagFieldMapper.map(reader);
            }
          }),
          Field.forObject("action", "action", null, false, new Field.ObjectReader<Action>() {
            @Override public Action read(final ResponseReader reader) throws IOException {
              return actionFieldMapper.map(reader);
            }
          })
        };

        @Override
        public Wallet map(ResponseReader reader) throws IOException {
          final boolean linked = reader.read(fields[0]);
          final String balance = reader.read(fields[1]);
          final int rawBalance = reader.read(fields[2]);
          final List<Error> errors = reader.read(fields[3]);
          final String text = reader.read(fields[4]);
          final String total_balance = reader.read(fields[5]);
          final int raw_total_balance = reader.read(fields[6]);
          final String hold_balance = reader.read(fields[7]);
          final int raw_hold_balance = reader.read(fields[8]);
          final String redirect_url = reader.read(fields[9]);
          final String applinks = reader.read(fields[10]);
          final List<Ab_tag> ab_tags = reader.read(fields[11]);
          final Action action = reader.read(fields[12]);
          return new Wallet(linked, balance, rawBalance, errors, text, total_balance, raw_total_balance, hold_balance, raw_hold_balance, redirect_url, applinks, ab_tags, action);
        }
      }
    }
  }
}
