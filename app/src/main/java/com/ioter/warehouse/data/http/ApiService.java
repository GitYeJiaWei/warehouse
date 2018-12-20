package com.ioter.warehouse.data.http;

import com.ioter.warehouse.bean.BaseBean;
import com.ioter.warehouse.bean.LoginBean;
import com.ioter.warehouse.bean.StockBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiService
{
    //String BASE_URL = "http://mall.ioter-e.com:8097/api/";
    String BASE_URL =  "http://192.168.66.3:8107/api/";
    //最后要加斜杠

   /* @FormUrlEncoded
    @POST("Center/Login")//登录
    Observable<BaseBean<LoginBean>> login(@FieldMap Map<String, String> params);*/

   /*
    @FormUrlEncoded
    @POST("Center/StoreIn")//门店收货
    Observable<BaseBean> stockIn(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Center/GetAllEpcList")//获取盘点列表
    Observable<BaseBean<List<Check>>> getStockTakeList(@FieldMap Map<String, String> params);
*/
    //----------------------------------------------------
    //http://192.168.66.3:8107/api/user/Login?userName=admin&password=123
    @GET("user/Login")
    Observable<BaseBean<LoginBean>> login(@QueryMap Map<String ,String> params);

    //http://192.168.66.3:8107/api/StockIn/QueryAsn?stockInId=ASN201812130002&orderNo=""
    @GET("StockIn/QueryAsn")
    Observable<BaseBean<List<StockBean>>> stock(@QueryMap Map<String,String> params);
}
