package com.tokopedia.inbox.rescenter.utils;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB_Table;

import java.util.List;

public class LocalCacheManager {

    private static String TAG = LocalCacheManager.class.getSimpleName();

    public static class ImageAttachment {

        private String resolutionID;
        private String imageLocalPath;
        private String imageUrl;

        public ImageAttachment() {
        }

        public static ImageAttachment Builder(String resolutionID) {
            ImageAttachment foo = new ImageAttachment();
            foo.resolutionID = resolutionID;
            return foo;
        }

        public String getImageLocalPath() {
            return imageLocalPath;
        }

        public ImageAttachment setImageLocalPath(String imageLocalPath) {
            this.imageLocalPath = imageLocalPath;
            return this;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public ImageAttachment setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public void save() {
            AttachmentResCenterVersion2DB db = new AttachmentResCenterVersion2DB();
            db.resolutionID = resolutionID;
            db.imagePath = getImageLocalPath();
            db.imageUrl = getImageUrl();
            db.modulName = AttachmentResCenterVersion2DB.MODULE_DETAIL_RESCENTER;
            db.save();
        }

        public List<AttachmentResCenterVersion2DB> getCache() {
            List<AttachmentResCenterVersion2DB> mList = SQLite.select().from(AttachmentResCenterVersion2DB.class)
                    .where(AttachmentResCenterVersion2DB_Table.resolutionID.is(resolutionID))
                    .queryList();
            return mList;
        }

        public void clearAll() {
            for (AttachmentResCenterVersion2DB data : getCache()) {
                data.delete();
            }
        }

        public void remove(AttachmentResCenterVersion2DB attachmentReplyResCenterDB) {
            SQLite.delete().from(AttachmentResCenterVersion2DB.class)
                    .where(AttachmentResCenterVersion2DB_Table.resolutionID.is(resolutionID))
                    .and(AttachmentResCenterVersion2DB_Table.id.is(attachmentReplyResCenterDB.getId()))
                    .execute();
        }
    }

    public static class AttachmentCreateResCenter {

        private String orderID;
        private String imageLocalPath;
        private String imageUrl;

        public AttachmentCreateResCenter() {
        }

        public static AttachmentCreateResCenter Builder(String resolutionID) {
            AttachmentCreateResCenter foo = new AttachmentCreateResCenter();
            foo.orderID = resolutionID;
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
            List<AttachmentResCenterVersion2DB> mList = SQLite.select().from(AttachmentResCenterVersion2DB.class)
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
            SQLite.delete().from(AttachmentResCenterVersion2DB.class)
                    .where(AttachmentResCenterVersion2DB_Table.orderID.is(orderID))
                    .and(AttachmentResCenterVersion2DB_Table.modulName.is(AttachmentResCenterVersion2DB.MODULE_CREATE_RESCENTER))
                    .and(AttachmentResCenterVersion2DB_Table.id.is(attachmentReplyResCenterDB.getId()))
                    .execute();
        }
    }

    public static class AttachmentEditResCenter {

        private String resolutionId;
        private String imageLocalPath;
        private String imageUrl;

        public AttachmentEditResCenter() {
        }

        public static AttachmentEditResCenter Builder(String resolutionID) {
            AttachmentEditResCenter foo = new AttachmentEditResCenter();
            foo.resolutionId = resolutionID;
            return foo;
        }

        public String getImageLocalPath() {
            return imageLocalPath;
        }

        public AttachmentEditResCenter setImageLocalPath(String imageLocalPath) {
            this.imageLocalPath = imageLocalPath;
            return this;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public AttachmentEditResCenter setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public void save() {
            AttachmentResCenterVersion2DB db = new AttachmentResCenterVersion2DB();
            db.resolutionID = resolutionId;
            db.imagePath = getImageLocalPath();
            db.imageUrl = getImageUrl();
            db.modulName = AttachmentResCenterVersion2DB.MODULE_EDIT_RESCENTER;
            db.save();
        }

        public List<AttachmentResCenterVersion2DB> getCache() {
            List<AttachmentResCenterVersion2DB> mList = SQLite.select().from(AttachmentResCenterVersion2DB.class)
                    .where(AttachmentResCenterVersion2DB_Table.resolutionID.is(resolutionId))
                    .and(AttachmentResCenterVersion2DB_Table.modulName.is(AttachmentResCenterVersion2DB.MODULE_EDIT_RESCENTER))
                    .queryList();
            return mList;
        }

        public void clearAll() {
            for (AttachmentResCenterVersion2DB data : getCache()) {
                data.delete();
            }
        }

        public void remove(AttachmentResCenterVersion2DB attachmentReplyResCenterDB) {
            SQLite.delete().from(AttachmentResCenterVersion2DB.class)
                    .where(AttachmentResCenterVersion2DB_Table.resolutionID.is(resolutionId))
                    .and(AttachmentResCenterVersion2DB_Table.modulName.is(AttachmentResCenterVersion2DB.MODULE_EDIT_RESCENTER))
                    .and(AttachmentResCenterVersion2DB_Table.id.is(attachmentReplyResCenterDB.getId()))
                    .execute();
        }
    }

    public static class AttachmentShippingResCenter {

        private String resolutionId;
        private String imageLocalPath;
        private String imageUrl;
        private String imageUUID;


        public AttachmentShippingResCenter() {
        }

        public static AttachmentShippingResCenter Builder(String resolutionID) {
            AttachmentShippingResCenter foo = new AttachmentShippingResCenter();
            foo.resolutionId = resolutionID;
            return foo;
        }

        public String getImageLocalPath() {
            return imageLocalPath;
        }

        public AttachmentShippingResCenter setImageLocalPath(String imageLocalPath) {
            this.imageLocalPath = imageLocalPath;
            return this;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public AttachmentShippingResCenter setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public String getImageUUID() {
            return imageUUID;
        }

        public AttachmentShippingResCenter setImageUUID(String imageUUID) {
            this.imageUUID = imageUUID;
            return this;
        }

        public void save() {
            AttachmentResCenterVersion2DB db = new AttachmentResCenterVersion2DB();
            db.resolutionID = resolutionId;
            db.imagePath = getImageLocalPath();
            db.imageUrl = getImageUrl();
            db.imageUUID = getImageUUID();
            db.modulName = AttachmentResCenterVersion2DB.MODULE_SHIPPING_RESCENTER;
            db.save();
        }

        public List<AttachmentResCenterVersion2DB> getCache() {
            List<AttachmentResCenterVersion2DB> mList = SQLite.select().from(AttachmentResCenterVersion2DB.class)
                    .where(AttachmentResCenterVersion2DB_Table.resolutionID.is(resolutionId))
                    .and(AttachmentResCenterVersion2DB_Table.modulName.is(AttachmentResCenterVersion2DB.MODULE_SHIPPING_RESCENTER))
                    .queryList();
            return mList;
        }

        public void clearAll() {
            for (AttachmentResCenterVersion2DB data : getCache()) {
                data.delete();
            }
        }

        public void remove(AttachmentResCenterVersion2DB attachmentReplyResCenterDB) {
            SQLite.delete().from(AttachmentResCenterVersion2DB.class)
                    .where(AttachmentResCenterVersion2DB_Table.resolutionID.is(resolutionId))
                    .and(AttachmentResCenterVersion2DB_Table.modulName.is(AttachmentResCenterVersion2DB.MODULE_SHIPPING_RESCENTER))
                    .and(AttachmentResCenterVersion2DB_Table.id.is(attachmentReplyResCenterDB.getId()))
                    .execute();
        }
    }
}