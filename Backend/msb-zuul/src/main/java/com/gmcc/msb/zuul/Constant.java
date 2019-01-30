package com.gmcc.msb.zuul;
/**
 * @author Yuan Chunhai
 */
public final class Constant {



    private Constant(){
    }

    public static final String REQUEST_TIME = "requestTime";
    public static String ROUTING_DATE = "routingDate";
    public static String RESPOND_DATE = "respondDate";
    public static final String SERVICE_ID = "serviceId";

    public static final String API_ID = "api_id";
    public static final String API_INFO = "api_info";
    public static final String APP_ID = "app_id";
    public static final String APP_INFO = "app_info";
    public static final String APP_APP_ID = "app_app_id";
    public static final String USER_ID = "user_id";

    public static final String DATE_FORMAT = "yyyyMMddHHmmss";


    public static final int CHECK_APP_STATUS_FILTER_ORDER = 10;

    public static final int CHECK_TOKEN_FILTER_ORDER = 11;

    public static final int CHECK_API_STATUS_FILTER_ORDER = 12;

    public static final int CHECK_APP_ORDER_API_FILTER_ORDER = 13;

    public static final int CHECK_API_ACCESS_FILTER_ORDER = 14;

    public static final int SIGN_FILTER_ORDER = 18;



}
