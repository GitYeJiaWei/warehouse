package com.ioter.warehouse.bean;

import java.util.List;

/**
 * @author YJW
 * @create 2018/7/23
 * @Describe
 */
public class LoginBean extends BaseEntity{


    /**
     * Success : true
     * Message : 登录成功！
     * ResultObj : {"UserId":"1","UserName":"admin","ListWarehouse":[{"WhId":"2","WhName":"2"},{"WhId":"171d59a5-6b9a-42d9-ba16-e2505fb08d36","WhName":"jimeiku"}]}
     */

        /**
         * UserId : 1
         * UserName : admin
         * ListWarehouse : [{"WhId":"2","WhName":"2"},{"WhId":"171d59a5-6b9a-42d9-ba16-e2505fb08d36","WhName":"jimeiku"}]
         */

        private String UserId;
        private String UserName;
        private List<StoresBean> ListWarehouse;

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String UserId) {
            this.UserId = UserId;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public List<StoresBean> getListWarehouse() {
            return ListWarehouse;
        }

        public void setListWarehouse(List<StoresBean> ListWarehouse) {
            this.ListWarehouse = ListWarehouse;
        }
}
