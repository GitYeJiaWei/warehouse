package com.ioter.warehouse.common.util;

public class UIConstant
{
    public static final String MODEL = "model";
    public static final String IMEI = "imei";
    public static final String LANGUAGE = "la";
    public static final String os = "os";
    public static final String RESOLUTION = "resolution";
    public static final String SDK = "sdk";
    public static final String DENSITY_SCALE_FACTOR = "densityScaleFactor";
    public static final String PARAM ="p" ;
    public static final String TOKEN = "token";
    public static final String USER = "user";
    public static final String CATEGORY = "category";

    public static final int DEBUGMODE = 0x01;// 大于0 开发模式
    /**
     * 消息
     **/
    public static final int MSG_SHOW_WAIT = 1;
    public static final int MSG_HIDE_WAIT = 2;
    public static final int MSG_SHOW_Toast = 3;
    public static final int MSG_WELCOME_Delay = 4;
    public static final int MSG_BANNER_Delay = 5;
    public static final int MSG_USER_BEG = 100;
    public static final int MSG_RESULT_READ = MSG_USER_BEG + 1; // 常量读
    public static final int MSG_FLUSH_READTIME = MSG_USER_BEG + 2;
    public static final int MSG_UHF_POWERLOW = MSG_USER_BEG + 3;

    public static int low_power_soc = 10;
    public static int _NowAntennaNo = 1; // 读写器天线编号
    public static int _UpDataTime = 0; // 重复标签上传时间，控制标签上传速度不要太快
    public static int _Max_Power = 30; // 读写器最大发射功率
    public static int _Min_Power = 0; // 读写器最小发射功率
    public static String _NowReadParam = _NowAntennaNo + "|1"; // 读标签参数
    public static final String WATER_CODE = "watercode";


    //public static String BASEURL = "http://192.168.31.24";//本地
    //public static String BASEURL = "http://192.168.31.67:30000";//内网
    public static String BASEURL = "http://59.57.151.234:30000";//外网
    public static String BASESOAPACTION = "http://tempuri.org";

    public static String IP = "192.168.66.3";


    public static String getLoginUrl()
    {
        return BASEURL + "/UserService.svc?wsdl";
    }

    public static String getLoginSoapAction()
    {
        return BASESOAPACTION + "/IUserService/Login";
    }

    /**
     * 获取盘点url
     *
     * @return
     */
    public static String getCheckUrl()
    {
        return BASEURL + "/TakeStockService.svc?wsdl";
    }

    public static String getGetTakeStockSoapAction()
    {
        return BASESOAPACTION + "/ITakeStockService/GetTakeStock";
    }

    public static String getWriteTakeStockSoapAction()
    {
        return BASESOAPACTION + "/ITakeStockService/WriteTakeStock";
    }

    /**
     * 获取所有仓库及货架数据url
     *
     * @return
     */
    public static String getWarehouseUrl()
    {
        return BASEURL + "/WarehouseService.svc?wsdl";
    }

    public static String getWarehouseSoapAction()
    {
        return BASESOAPACTION + "/IWarehouseService/GetAllWh";
    }

    /**
     * 获取出入库url
     *
     * @return
     */
    public static String getEpcInOutUrl()
    {
        return BASEURL + "/StockService.svc?wsdl";
    }

    public static String getEpcInSoapAction()
    {
        return BASESOAPACTION + "/IStockService/StockIn";
    }

    public static String getEpcOutSoapAction()
    {
        return BASESOAPACTION + "/IStockService/StockOut";
    }

    //入店
    public static String getStoreInUrl()
    {
        return BASEURL + "/StoreService.svc?wsdl";
    }
    public static String getStoreInSoapAction()
    {
        return BASESOAPACTION + "/IStoreService/StoreIn";
    }

    /**
     * 获取产品数据
     *
     * @return
     */
    public static String getProductUrl()
    {
        return BASEURL + "/ProductService.svc?wsdl";
    }

    public static String getProductSoapAction()
    {
        return BASESOAPACTION + "/IProductService/GetInfo";
    }

    /**
     * 获取所有仓库和门店
     *
     * @return
     */
    public static String getAllWhAndStore()
    {
        return BASEURL + "/WarehouseService.svc?wsdl";
    }

    public static String getAllWhAndStoreSoapAction()
    {
        return BASESOAPACTION + "/IWarehouseService/GetAllWhAndStore";

    }
}
