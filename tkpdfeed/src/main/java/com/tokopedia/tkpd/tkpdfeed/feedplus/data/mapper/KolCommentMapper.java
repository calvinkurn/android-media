package com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper;

import android.text.TextUtils;

import com.tkpdfeed.feeds.GetKolComments;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.util.TimeConverter;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolCommentViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolComments;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * @author by nisie on 11/2/17.
 */

public class KolCommentMapper implements Func1<GetKolComments.Data, KolComments> {

    @Override
    public KolComments call(GetKolComments.Data data) {
        if (data != null
                && data.get_kol_list_comment() != null
                && data.get_kol_list_comment().data() != null
                && (data.get_kol_list_comment().error() == null
                || TextUtils.isEmpty(data.get_kol_list_comment().error()))) {
            return convertToDomain(data.get_kol_list_comment().data());
        } else if (data != null
                && data.get_kol_list_comment() != null
                && (data.get_kol_list_comment().error() != null
                && !TextUtils.isEmpty(data.get_kol_list_comment().error()))) {
            throw new ErrorMessageException(data.get_kol_list_comment().error());
        } else {
            throw new ErrorMessageException(MainApplication.getAppContext().getString(R.string
                    .default_request_error_unknown));
        }
    }

    private KolComments convertToDomain(GetKolComments.Data.Data1 data) {
        return new KolComments(data.lastcursor() == null ? "" : data.lastcursor(),
                data.has_next_page() == null ? false : data.has_next_page(),
                convertToList(data.comment()));
    }

    private ArrayList<KolCommentViewModel> convertToList(List<GetKolComments.Data.Comment>
                                                                 comments) {
        ArrayList<KolCommentViewModel> list = new ArrayList<>();
        if (comments != null)
            for (GetKolComments.Data.Comment comment : comments) {
                list.add(new KolCommentViewModel(
                        comment.id() == null ? "0" : comment.id().toString(),
                        comment.userID() == null ? "" : comment.userID().toString(),
                        comment.userPhoto() == null ? "" : comment.userPhoto(),
                        comment.userName() == null ? "" : comment.userName(),
                        comment.comment() == null ? "" : comment.comment(),
                        TimeConverter.generateTime(comment.create_time() == null ? "" : comment
                                .create_time()),
                        comment.isKol() == null ? false : comment.isKol(),
                        comment.isCommentOwner() == null ? false : comment.isCommentOwner()));
            }
        return list;
    }
}
