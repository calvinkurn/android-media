package com.tokopedia.inbox.rescenter.create.util;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB_Table;

import java.util.List;

/**
 * Created on 7/12/16.
 */
public class LocalCacheManager {

    public static class AttachmentCreateResCenter {

        private String orderID;
        private String imageLocalPath;
        private String imageUrl;

        public AttachmentCreateResCenter() {
        }

        public static AttachmentCreateResCenter Builder(String orderID) {
            AttachmentCreateResCenter foo = new AttachmentCreateResCenter();
            foo.orderID = orderID;
            return foo;
        }

        public String getImageLocalPath() {
            return imageLocalPath;
        }

        public AttachmentCreateResCenter setImageLocalPath(String imageLocalPath) {
            this.imageLocalPath = imageLocalPath;
            return this;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public AttachmentCreateResCenter setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public void save() {
            AttachmentResCenterVersion2DB db = new AttachmentResCenterVersion2DB();
            db.orderID = orderID;
            db.imagePath = getImageLocalPath();
            db.imageUrl = getImageUrl();
            db.modulName = AttachmentResCenterVersion2DB.MODULE_CREATE_RESCENTER;
            db.save();
        }

        public List<AttachmentResCenterVersion2DB> getCache() {
            List<AttachmentResCenterVersion2DB> mList = new Select().from(AttachmentResCenterVersion2DB.class)
                    .where(AttachmentResCenterVersion2DB_Table.orderID.is(orderID))
                    .and(AttachmentResCenterVersion2DB_Table.modulName.is(AttachmentResCenterVersion2DB.MODULE_CREATE_RESCENTER))
                    .queryList();
            return mList;
        }

        public void clearAll() {
            for (AttachmentResCenterVersion2DB data : getCache()) {
                data.delete();
            }
        }

        public void remove(AttachmentResCenterVersion2DB attachmentReplyResCenterDB) {
            new Delete().from(AttachmentResCenterVersion2DB.class)
                    .where(AttachmentResCenterVersion2DB_Table.orderID.is(orderID))
                    .and(AttachmentResCenterVersion2DB_Table.modulName.is(AttachmentResCenterVersion2DB.MODULE_CREATE_RESCENTER))
                    .and(AttachmentResCenterVersion2DB_Table.id.is(attachmentReplyResCenterDB.getId()))
                    .execute();
        }
    }
}