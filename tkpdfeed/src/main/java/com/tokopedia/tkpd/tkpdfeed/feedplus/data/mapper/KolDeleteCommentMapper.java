package com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper;

import android.text.TextUtils;

import com.tkpdfeed.feeds.DeleteKolComment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.DeleteKolCommentDomain;

import rx.functions.Func1;

/**
 * @author by nisie on 11/10/17.
 */

public class KolDeleteCommentMapper implements Func1<DeleteKolComment.Data, DeleteKolCommentDomain> {
    @Override
    public DeleteKolCommentDomain call(DeleteKolComment.Data data) {
        if (data != null
                && data.delete_comment_kol() != null
                && data.delete_comment_kol().data() != null
                && (data.delete_comment_kol().error() == null
                || TextUtils.isEmpty(data.delete_comment_kol().error()))) {
            return convertToDomain(data.delete_comment_kol().data());
        } else if (data != null
                && data.delete_comment_kol() != null
                && (data.delete_comment_kol().error() != null
                && !TextUtils.isEmpty(data.delete_comment_kol().error()))) {
            throw new ErrorMessageException(data.delete_comment_kol().error());
        } else {
            throw new ErrorMessageException(MainApplication.getAppContext().getString(R.string
                    .default_request_error_unknown));
        }
    }

    private DeleteKolCommentDomain convertToDomain(DeleteKolComment.Data.Data1 data) {
        return new DeleteKolCommentDomain(data.success() != null && data.success() == 1);
    }
}
