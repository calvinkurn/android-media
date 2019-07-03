package com.tokopedia.inbox.rescenter.utils;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB_Table;
import com.tokopedia.core.database.model.InboxResCenterFilterDB;
import com.tokopedia.core.database.model.InboxResCenterFilterDB_Table;
import com.tokopedia.core.database.model.StateResCenterDetailDB;
import com.tokopedia.core.database.model.StateResCenterDetailDB_Table;

import java.util.List;

public class LocalCacheManager {

    private static String TAG = LocalCacheManager.class.getSimpleName();

    public static class StateDetailResCenter {

        private final String resolutionID;
        private Integer byCustomer;
        private Integer bySeller;
        private Integer lastFlagReceived;
        private Integer lastTroubleType;
        private String lastTroubleString;
        private Integer lastSolutionType;
        private String lastSolutionString;
        private String orderPriceFmt;
        private String shippingPriceFmt;
        private Integer orderPriceRaw;
        private Integer shippingPriceRaw;

        public StateDetailResCenter(String resolutionID) {
            this.resolutionID = resolutionID;
        }

        public static StateDetailResCenter Builder(String resolutionID) {
            return new StateDetailResCenter(resolutionID);
        }

        public int getByCustomer() {
            return byCustomer;
        }

        public StateDetailResCenter setByCustomer(int byCustomer) {
            this.byCustomer = byCustomer;
            return this;
        }

        public int getBySeller() {
            return bySeller;
        }

        public StateDetailResCenter setBySeller(int bySeller) {
            this.bySeller = bySeller;
            return this;
        }

        public int getLastFlagReceived() {
            return lastFlagReceived;
        }

        public StateDetailResCenter setLastFlagReceived(int lastFlagReceived) {
            this.lastFlagReceived = lastFlagReceived;
            return this;
        }

        public int getLastTroubleType() {
            return lastTroubleType;
        }

        public StateDetailResCenter setLastTroubleType(int lastTroubleType) {
            this.lastTroubleType = lastTroubleType;
            return this;
        }

        public String getLastTroubleString() {
            return lastTroubleString;
        }

        public StateDetailResCenter setLastTroubleString(String lastTroubleString) {
            this.lastTroubleString = lastTroubleString;
            return this;
        }

        public Integer getLastSolutionType() {
            return lastSolutionType;
        }

        public StateDetailResCenter setLastSolutionType(Integer lastSolutionType) {
            this.lastSolutionType = lastSolutionType;
            return this;
        }

        public String getLastSolutionString() {
            return lastSolutionString;
        }

        public StateDetailResCenter setLastSolutionString(String lastSolutionString) {
            this.lastSolutionString = lastSolutionString;
            return this;
        }

        public String getOrderPriceFmt() {
            return orderPriceFmt;
        }

        public StateDetailResCenter setOrderPriceFmt(String orderPriceFmt) {
            this.orderPriceFmt = orderPriceFmt;
            return this;
        }

        public String getShippingPriceFmt() {
            return shippingPriceFmt;
        }

        public StateDetailResCenter setShippingPriceFmt(String shippingPriceFmt) {
            this.shippingPriceFmt = shippingPriceFmt;
            return this;
        }

        public int getOrderPriceRaw() {
            return orderPriceRaw;
        }

        public StateDetailResCenter setOrderPriceRaw(int orderPriceRaw) {
            this.orderPriceRaw = orderPriceRaw;
            return this;
        }

        public int getShippingPriceRaw() {
            return shippingPriceRaw;
        }

        public StateDetailResCenter setShippingPriceRaw(int shippingPriceRaw) {
            this.shippingPriceRaw = shippingPriceRaw;
            return this;
        }

        public void save() {
            StateResCenterDetailDB cache = new StateResCenterDetailDB();
            cache.resolutionID = resolutionID;
            cache.byCustomer = getByCustomer();
            cache.bySeller = getBySeller();
            cache.lastFlagReceived = getLastFlagReceived();
            cache.lastTroubleType = getLastTroubleType();
            cache.lastTroubleString = getLastTroubleString();
            cache.lastSolutionType = getLastSolutionType();
            cache.lastSolutionString = getLastSolutionString();
            cache.orderPriceFmt = getOrderPriceFmt();
            cache.shippingPriceFmt = getShippingPriceFmt();
            cache.orderPriceRaw = getOrderPriceRaw();
            cache.shippingPriceRaw = getShippingPriceRaw();
            cache.save();
        }

        public StateDetailResCenter getCache() {
            try {
                StateResCenterDetailDB cache = SQLite.select().from(StateResCenterDetailDB.class)
                        .where(StateResCenterDetailDB_Table.resolutionID.is(resolutionID))
                        .querySingle();
                setByCustomer(cache.byCustomer);
                setBySeller(cache.bySeller);
                setLastSolutionType(cache.lastSolutionType);
                setLastSolutionString(cache.lastSolutionString);
                setLastTroubleType(cache.lastTroubleType);
                setLastTroubleString(cache.lastTroubleString);
                setLastFlagReceived(cache.lastFlagReceived);
                setOrderPriceFmt(cache.orderPriceFmt);
                setOrderPriceRaw(cache.orderPriceRaw);
                setShippingPriceFmt(cache.shippingPriceFmt);
                setShippingPriceRaw(cache.shippingPriceRaw);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public void clear() {
            SQLite.delete().from(StateResCenterDetailDB.class).where(StateResCenterDetailDB_Table.resolutionID.is(resolutionID)).execute();
        }
    }

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

    public static class Filter {

        public String inboxTabID;
        public String inboxTabName;
        public int filterStatus;
        public String filterStatusText;
        public int filterRead;
        public String filterReadText;
        public int filterTime;
        public String filterTimeText;

        public Filter() {
        }

        public static Filter Builder(String inboxTabID, String inboxTabName) {
            Filter filter = new Filter();
            filter.setInboxTabID(inboxTabID);
            filter.setInboxTabName(inboxTabName);
            return filter;
        }

        public void save() throws Exception {
            InboxResCenterFilterDB db = new InboxResCenterFilterDB();
            db.inboxTabID = getInboxTabID();
            db.inboxTabName = getInboxTabName();
            db.filterRead = getFilterRead();
            db.filterReadText = getFilterReadText();
            db.filterStatus = getFilterStatus();
            db.filterStatusText = getFilterStatusText();
            db.filterTime = getFilterTime();
            db.filterTimeText = getFilterTimeText();
            db.save();
        }

        public Filter getCache() {
            InboxResCenterFilterDB cache = SQLite.select().from(InboxResCenterFilterDB.class)
                    .where(InboxResCenterFilterDB_Table.inboxTabID.is(getInboxTabID()))
                    .querySingle();

            if (cache == null) {
                setFilterRead(0);
                setFilterStatus(0);
                setFilterTime(0);
                return this;
            }

            setInboxTabName(cache.inboxTabName);
            setFilterRead(cache.filterRead);
            setFilterReadText(cache.filterReadText);
            setFilterStatus(cache.filterStatus);
            setFilterStatusText(cache.filterStatusText);
            setFilterTime(cache.filterTime);
            setFilterTimeText(cache.filterTimeText);
            return this;
        }

        public void clear() {
            SQLite.delete().from(InboxResCenterFilterDB.class)
                    .where(InboxResCenterFilterDB_Table.inboxTabID.is(getInboxTabID()))
                    .execute();
        }

        public String getInboxTabID() {
            return inboxTabID;
        }

        public Filter setInboxTabID(String inboxTabID) {
            this.inboxTabID = inboxTabID;
            return this;
        }

        public String getInboxTabName() {
            return inboxTabName;
        }

        public Filter setInboxTabName(String inboxTabName) {
            this.inboxTabName = inboxTabName;
            return this;
        }

        public int getFilterStatus() {
            return filterStatus;
        }

        public Filter setFilterStatus(int filterStatus) {
            this.filterStatus = filterStatus;
            return this;
        }

        public String getFilterStatusText() {
            return filterStatusText;
        }

        public Filter setFilterStatusText(String filterStatusText) {
            this.filterStatusText = filterStatusText;
            return this;
        }

        public int getFilterRead() {
            return filterRead;
        }

        public Filter setFilterRead(int filterRead) {
            this.filterRead = filterRead;
            return this;
        }

        public String getFilterReadText() {
            return filterReadText;
        }

        public Filter setFilterReadText(String filterReadText) {
            this.filterReadText = filterReadText;
            return this;
        }

        public int getFilterTime() {
            return filterTime;
        }

        public Filter setFilterTime(int filterTime) {
            this.filterTime = filterTime;
            return this;
        }

        public String getFilterTimeText() {
            return filterTimeText;
        }

        public Filter setFilterTimeText(String filterTimeText) {
            this.filterTimeText = filterTimeText;
            return this;
        }
    }
}