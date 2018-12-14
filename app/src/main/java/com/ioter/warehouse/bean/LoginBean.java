package com.ioter.warehouse.bean;

import java.util.List;

/**
 * @author YJW
 * @create 2018/7/23
 * @Describe
 */
public class LoginBean extends BaseEntity{

        /**
         * Id : 1
         * UserName : Admin
         * Password : 123
         * RealName : 管理员
         * SimpleSpelling : null
         * Gender : null
         * Mobile : null
         * Email : null
         * FirstVisitTime : null
         * PreviousVisitTime : null
         * LastVisitTime : null
         * LogOnCount : null
         * Status : 0
         * Description : 113
         * SysVersion : 22
         * CreateDate : null
         * CreateUser : null
         * LogDate : 2018-07-18T10:57:03
         * LogUser :
         * InCurrentGroup : false
         * Groups : null
         * Factorys : null
         * Warehouses : null
         * Stores : [{"Id":"012","StoreName":"门店代码特殊字符","Address":null,"Contact":null,"Telephone":null,"ReaderTimeout":null,"HangerTime":null,"FittingTime":null,"UploadDataTime":null,"Status":0,"Description":null,"SysVersion":8,"CreateDate":"2018-07-17T10:56:35","CreateUser":"","LogDate":"2018-07-23T09:15:42","LogUser":"","InCurrentGroup":false},{"Id":"10","StoreName":"门店48","Address":null,"Contact":null,"Telephone":null,"ReaderTimeout":null,"HangerTime":null,"FittingTime":null,"UploadDataTime":null,"Status":0,"Description":null,"SysVersion":4,"CreateDate":"2018-07-17T11:06:40","CreateUser":"","LogDate":"2018-07-19T10:35:36","LogUser":"","InCurrentGroup":false},{"Id":"11","StoreName":"门店50","Address":null,"Contact":null,"Telephone":null,"ReaderTimeout":null,"HangerTime":null,"FittingTime":null,"UploadDataTime":null,"Status":0,"Description":null,"SysVersion":4,"CreateDate":"2018-07-17T11:07:09","CreateUser":"","LogDate":"2018-07-20T14:06:17","LogUser":"","InCurrentGroup":false},{"Id":"12","StoreName":"1212","Address":"1212123","Contact":"121","Telephone":"1212","ReaderTimeout":null,"HangerTime":null,"FittingTime":null,"UploadDataTime":null,"Status":1,"Description":"1212122","SysVersion":6,"CreateDate":"2018-06-28T14:30:57","CreateUser":null,"LogDate":"2018-07-23T11:01:17","LogUser":"","InCurrentGroup":false}]
         */

        private String Id;
        private String UserName;
        private String Password;
        private String RealName;
        private Object SimpleSpelling;
        private Object Gender;
        private Object Mobile;
        private Object Email;
        private Object FirstVisitTime;
        private Object PreviousVisitTime;
        private Object LastVisitTime;
        private Object LogOnCount;
        private int Status;
        private String Description;
        private int SysVersion;
        private Object CreateDate;
        private Object CreateUser;
        private String LogDate;
        private String LogUser;
        private boolean InCurrentGroup;
        private Object Groups;
        private Object Factorys;
        private Object Warehouses;
        private List<StoresBean> Stores;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }

        public String getRealName() {
            return RealName;
        }

        public void setRealName(String RealName) {
            this.RealName = RealName;
        }

        public Object getSimpleSpelling() {
            return SimpleSpelling;
        }

        public void setSimpleSpelling(Object SimpleSpelling) {
            this.SimpleSpelling = SimpleSpelling;
        }

        public Object getGender() {
            return Gender;
        }

        public void setGender(Object Gender) {
            this.Gender = Gender;
        }

        public Object getMobile() {
            return Mobile;
        }

        public void setMobile(Object Mobile) {
            this.Mobile = Mobile;
        }

        public Object getEmail() {
            return Email;
        }

        public void setEmail(Object Email) {
            this.Email = Email;
        }

        public Object getFirstVisitTime() {
            return FirstVisitTime;
        }

        public void setFirstVisitTime(Object FirstVisitTime) {
            this.FirstVisitTime = FirstVisitTime;
        }

        public Object getPreviousVisitTime() {
            return PreviousVisitTime;
        }

        public void setPreviousVisitTime(Object PreviousVisitTime) {
            this.PreviousVisitTime = PreviousVisitTime;
        }

        public Object getLastVisitTime() {
            return LastVisitTime;
        }

        public void setLastVisitTime(Object LastVisitTime) {
            this.LastVisitTime = LastVisitTime;
        }

        public Object getLogOnCount() {
            return LogOnCount;
        }

        public void setLogOnCount(Object LogOnCount) {
            this.LogOnCount = LogOnCount;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public int getSysVersion() {
            return SysVersion;
        }

        public void setSysVersion(int SysVersion) {
            this.SysVersion = SysVersion;
        }

        public Object getCreateDate() {
            return CreateDate;
        }

        public void setCreateDate(Object CreateDate) {
            this.CreateDate = CreateDate;
        }

        public Object getCreateUser() {
            return CreateUser;
        }

        public void setCreateUser(Object CreateUser) {
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

        public Object getGroups() {
            return Groups;
        }

        public void setGroups(Object Groups) {
            this.Groups = Groups;
        }

        public Object getFactorys() {
            return Factorys;
        }

        public void setFactorys(Object Factorys) {
            this.Factorys = Factorys;
        }

        public Object getWarehouses() {
            return Warehouses;
        }

        public void setWarehouses(Object Warehouses) {
            this.Warehouses = Warehouses;
        }

        public List<StoresBean> getStores() {
            return Stores;
        }

        public void setStores(List<StoresBean> Stores) {
            this.Stores = Stores;
        }



}
