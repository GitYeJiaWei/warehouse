package com.ioter.warehouse.bean;

import java.io.Serializable;

/**
 * @author YJW
 * @create 2018/7/23
 * @Describe
 */
public class StoresBean implements Serializable {
        /**
         * Id : 012
         * StoreName : 门店代码特殊字符
         * Address : null
         * Contact : null
         * Telephone : null
         * ReaderTimeout : null
         * HangerTime : null
         * FittingTime : null
         * UploadDataTime : null
         * Status : 0
         * Description : null
         * SysVersion : 8
         * CreateDate : 2018-07-17T10:56:35
         * CreateUser :
         * LogDate : 2018-07-23T09:15:42
         * LogUser :
         * InCurrentGroup : false
         */

        private String Id;
        private String StoreName;
        private Object Address;
        private Object Contact;
        private Object Telephone;
        private Object ReaderTimeout;
        private Object HangerTime;
        private Object FittingTime;
        private Object UploadDataTime;
        private int Status;
        private Object Description;
        private int SysVersion;
        private String CreateDate;
        private String CreateUser;
        private String LogDate;
        private String LogUser;
        private boolean InCurrentGroup;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getStoreName() {
            return StoreName;
        }

        public void setStoreName(String StoreName) {
            this.StoreName = StoreName;
        }

        public Object getAddress() {
            return Address;
        }

        public void setAddress(Object Address) {
            this.Address = Address;
        }

        public Object getContact() {
            return Contact;
        }

        public void setContact(Object Contact) {
            this.Contact = Contact;
        }

        public Object getTelephone() {
            return Telephone;
        }

        public void setTelephone(Object Telephone) {
            this.Telephone = Telephone;
        }

        public Object getReaderTimeout() {
            return ReaderTimeout;
        }

        public void setReaderTimeout(Object ReaderTimeout) {
            this.ReaderTimeout = ReaderTimeout;
        }

        public Object getHangerTime() {
            return HangerTime;
        }

        public void setHangerTime(Object HangerTime) {
            this.HangerTime = HangerTime;
        }

        public Object getFittingTime() {
            return FittingTime;
        }

        public void setFittingTime(Object FittingTime) {
            this.FittingTime = FittingTime;
        }

        public Object getUploadDataTime() {
            return UploadDataTime;
        }

        public void setUploadDataTime(Object UploadDataTime) {
            this.UploadDataTime = UploadDataTime;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }

        public Object getDescription() {
            return Description;
        }

        public void setDescription(Object Description) {
            this.Description = Description;
        }

        public int getSysVersion() {
            return SysVersion;
        }

        public void setSysVersion(int SysVersion) {
            this.SysVersion = SysVersion;
        }

        public String getCreateDate() {
            return CreateDate;
        }

        public void setCreateDate(String CreateDate) {
            this.CreateDate = CreateDate;
        }

        public String getCreateUser() {
            return CreateUser;
        }

        public void setCreateUser(String CreateUser) {
            this.CreateUser = CreateUser;
        }

        public String getLogDate() {
            return LogDate;
        }

        public void setLogDate(String LogDate) {
            this.LogDate = LogDate;
        }

        public String getLogUser() {
            return LogUser;
        }

        public void setLogUser(String LogUser) {
            this.LogUser = LogUser;
        }

        public boolean isInCurrentGroup() {
            return InCurrentGroup;
        }

        public void setInCurrentGroup(boolean InCurrentGroup) {
            this.InCurrentGroup = InCurrentGroup;
        }
}
