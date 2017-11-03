package com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper;

import com.tkpdfeed.feeds.GetKolComments;
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
        return convertToDomain(data.get_kol_list_comment().data());
    }

    private KolComments convertToDomain(GetKolComments.Data.Data1 data) {
        if (data != null) return null;
        return new KolComments(data.lastcursor(),
                convertToList(data.comment()));
    }

    private ArrayList<KolCommentViewModel> convertToList(List<GetKolComments.Data.Comment>
                                                                 comments) {
        ArrayList<KolCommentViewModel> list = new ArrayList<>();
        if (comments != null)
            for (GetKolComments.Data.Comment comment : comments) {
                list.add(new KolCommentViewModel(
                        comment.userPhoto() == null ? "" : comment.userPhoto(),
                        comment.userName() == null ? "" : comment.userPhoto(),
                        comment.comment() == null ? "" : comment.userPhoto(),
                        "10 jam yg lalu"));
            }
        return list;
    }
}
