package xyz.util;




/**
 * 常量
 * @author 姚成成
 */
public final class Constant {
	//返回值
	public static final String result_status = "status";//
	public static final String result_msg = "msg";//
	public static final String result_content = "content";//
	
	public static String QUERY_DATA_TOKEN="";//客服页面时效性验证token
	
	//计划任务类型
	public static final String PLAN_TYPE_UPDATE_CRUISE="update_cruise";//基于邮轮维度自动同步更新
	public static final String PLAN_TYPE_UPDATE_TKVIEW="update_tkview";//基于单品维度自动同步更新
	
	public static final String[] possessorNotSeeButtonCodes = new String[]{
        "buttonCode_x20161207100202",
        "buttonCode_x20161207100203",
        "buttonCode_x20161207100204",
        "buttonCode_x20161207110502",
        "buttonCode_x20161207110503",
        "buttonCode_x20161207110504",
        "buttonCode_x20161207110505",
        "buttonCode_x20161207195602",
        "buttonCode_x20161207195603",
        "buttonCode_x20161207195604",
        "buttonCode_x20161207195605",
        "buttonCode_y20161215110606",
        "buttonCode_x20161207200102",
        "buttonCode_x20161207200103",
        "buttonCode_x20161207200104",
        "buttonCode_x20161207200502",
        "buttonCode_x20161207200503",
        "buttonCode_x20161207200504",
        "buttonCode_x20161207200202",
        "buttonCode_x20161207200203",
        "buttonCode_x20161207200204",
        "buttonCode_x20161207200302",
        "buttonCode_x20161207200303",
        "buttonCode_x20161207200304",
        "buttonCode_x20161207200305",
        "buttonCode_x20161207200402",
        "buttonCode_x20161207200403",
        "buttonCode_x20161207200404",
        "buttonCode_x20161207200405",
        "buttonCode_x20161207200406",
        "buttonCode_x20161213113304",
        "buttonCode_x20161213113305",
        "buttonCode_y20160115101501",
        "buttonCode_s20150708143401",
        "buttonCode_s20150708143402",
        "buttonCode_s20150708143403",
        "buttonCode_s20150708143404",
        "buttonCode_y20161211143405",
        "buttonCode_y20161211143405",
        "buttonCode_y20161211143406",
        "buttonCode_y20161211143407",
        "buttonCode_y20161211143408",
        "buttonCode_y20161211143409",
        "buttonCode_y20161211143410",
        "buttonCode_y20161211143411",
        "buttonCode_y20161211143412",
        "buttonCode_y20161211143413",
        "buttonCode_y20161211143414",
        "buttonCode_y20161211143415",
        "buttonCode_y20161211160000"
    };
	
	//session过期时间
	public static final long sessionTimes = 1000*60*60;
	
	public static final String separator = ",";  //分隔符
	
	public static final int list_max_result = 100;
	
	//通用map键
	public static final String content = "content";
	public static final String msg = "msg";
	
	//通用空字符串
	public static final String nulo = "";
	
	//通用淘宝接口URL
	public static final String taobaoUrl = "http://gw.api.taobao.com/router/rest";
	
	/**
     * 通用编号
     */
	public static final String numberCode = "numberCode";
	
	/**
     * 通用实体类名
     */
	public static final String possessor = "Possessor";
	public static final String tkview = "Tkview";
	public static final String stock = "Stock";
	public static final String provider = "Provider";
	public static final String provider_type = "provider";
	public static final String cruise = "Cruise";
	public static final String shipment = "Shipment";
	public static final String cabin = "Cabin";
	public static final String ptview = "Ptview";
	public static final String taobaoTravelItem = "TaobaoTravelItem";
	public static final String taobaoBaseInfo = "TaobaoBaseInfo";
	public static final String taobaoBookingRule = "TaobaoBookingRule";
	public static final String taobaoCruiseItemExt = "TaobaoCruiseItemExt";
	public static final String taobaoOrderTbt = "TaobaoOrderTbt";
	public static final String taobaoOrderTbo = "TaobaoOrderTbo";
	public static final String taobaoSkuInfo = "TaobaoSkuInfo";
	public static final String taobaoSkuInfoDetail = "TaobaoSkuInfoDetail";
	public static final String channel = "Channel";
	public static final String distributor = "Distributor";
	public static final String securityPosition = "SecurityPosition";
	public static final String orderContent = "OrderContent";
	public static final String airway = "Airway";
	public static final String area = "Area";
	public static final String company = "Company";
	
	public static final String taobao_order_status_wait_buyer_pay = "WAIT_BUYER_PAY";
	public static final String taobao_order_status_wait_buyer_confirm_goods = "WAIT_BUYER_CONFIRM_GOODS";
	public static final String taobao_order_status_wait_seller_send_goods = "WAIT_SELLER_SEND_GOODS";
	public static final String taobao_order_status_trade_finished = "TRADE_FINISHED";
	public static final String taobao_order_status_trade_closed = "TRADE_CLOSED";
	public static final String taobao_order_status_trade_closed_by_taobao = "TRADE_CLOSED_BY_TAOBAO";
	public static final String taobao_order_status_trade_no_create_pay = "TRADE_NO_CREATE_PAY";
	public static final String taobao_order_status_other = "OTHER";
	
	public static final String taobao_order_refund_status_wait_seller_agree = "WAIT_SELLER_AGREE";
	public static final String taobao_order_refund_status_wait_buyer_return_goods = "WAIT_BUYER_RETURN_GOODS";
	public static final String taobao_order_refund_status_wait_seller_confirm_goods = "WAIT_SELLER_CONFIRM_GOODS";
	public static final String taobao_order_refund_status_closed = "CLOSED";
	public static final String taobao_order_refund_status_success = "SUCCESS";
	public static final String taobao_order_refund_status_seller_refuse_buyer = "SELLER_REFUSE_BUYER";
	public static final String taobao_order_refund_status_no_refund = "NO_REFUND";
	
	public static final String taobao_oper_name_cn = "CMS自动录单";
	
	public static final String list_cruise_not_null_error = "请先在邮轮下拉框选中对象!";
	
	/**
	 * 售卖相关常量
	 */
	public static final int ptview_defualt_flag = 0; //产品默认上下架状态 (下架)
	public static final int order_status_false = 0; //订单的一些状态 否 (如:付款, 发货, 退款, 退库存...)
	public static final int order_status_true = 1; //订单的一些状态 真 (如:付款, 发货, 退款, 退库存...)
	public static final String order_resource_taobao = "taobao";  //订单来源 淘宝
	public static final String order_resource_cms = "cms"; //订单来源 cms 系统内部下单
	public static final String order_resource_wx = "wx";   //订单来源 威信
	public static final String order_resource_pc = "pc";   //订单来源 pc分销平台
	public static final int order_flag_gray = 0;   //订单旗子 - 灰色
	public static final int order_flag_red = 1;   //订单旗子 - 红色
	public static final int order_flag_yellow = 2;   //订单旗子 - 黄色
	public static final int order_flag_green = 3;   //订单旗子 - 绿色
	public static final int order_flag_blue = 4;   //订单旗子 - 蓝色
	public static final int order_flag_purple = 5;   //订单旗子 - 紫色
	public static final int order_flag_black = 6;   //订单旗子 - 黑色
	
	/**
	 * 机构权限相关
	 */
	public static final String relate_type_channel = "channel";
	public static final String relate_type_ptview = "ptview";
	public static final String relate_type_tkview = "tkview";
	public static final String relate_type_provider = "provider";
	public static final String relate_type_position = "security_position";
	public static final String relate_type_distributor = "distributor";
	
	//所有订单查询时用到的时间类型
	public static final String q_order_content_all_date_type = ",add_date,alter_date,shipment_travel_date,pay_date,over_date,refound_money_date,send_date,";
	//所有订单查询时用到的状态
	public static final String q_order_content_all_status = ",pay,notPay,refoundMoney,notRefoundMoney,refoundStock,notRefoundStock,over,notOver,send,notSend,surpass,notSurpass,flag_0,flag_1,flag_2,flag_3,flag_4,flag_5,flag_6,";
	public static final String q_order_status_pay = "pay";  //已付款
	public static final String q_order_status_not_pay = "notPay";//未付款
	public static final String q_order_status_refound_money = "refoundMoney";//有退款
	public static final String q_order_status_not_refound_money = "notRefoundMoney";//无退款
	public static final String q_order_status_refount_stock = "refoundStock";//有回退库存
	public static final String q_order_status_not_refount_stock = "notRefoundStock";//无回退库存
	public static final String q_order_status_send = "send";//已发货
	public static final String q_order_status_not_send = "notSend";//未发货
	public static final String q_order_status_surpass = "surpass";//有超卖
	public static final String q_order_status_not_surpass = "notSurpass";//无超卖
	public static final String q_order_status_over = "over";//订单结束
	public static final String q_order_status_not_over = "notOver";//订单未结束
	public static final String q_order_status_flag = "flag";//订单旗子标识
	public static final String q_order_status_flag_underline = "_";//旗子状态分隔符
	public static final String q_order_status_split_str_comma = ",";//订单状态查询分隔符
	public static final int order_content_status_true = 1; //订单is_xxx状态 是
	public static final int order_content_status_false = 0;//订单is_xxx状态 否
	public static final int q_order_status_flag_split_length = 2;//订单未结束
	public static final int q_order_status_flag_split_index = 1;//订单未结束
	
	//单品常量
	public static final String tkview_mark = "mark";
	public static final String tkview_cabin = "cabin";
	public static final String tkview_cabin_name_cn = "cabinNameCn";
	public static final String tkview_shipment = "shipment";
	public static final String tkview_content_all_status = ",mark,shipmentMark,volume,addDate,providerNameCn,alterDate,cost,votes,isReference,flag,overDate,finalSaleDate,";
	
	//库存常量
	public static final String stock_weChat = "weChat";
	public static final String stock_talkGroup = "talkGroup";
	public static final String stock_joiner = "joiner";
	public static final String stock_status = "status";
	public static final String stock_name_cn = "nameCn";
	
	//渠道
	public static final String channel_sessionKey_url = "https://oauth.taobao.com/token";
	public static final String channel_grant_type = "grant_type";
	public static final String channel_authorization_code = "authorization_code";
	public static final String channel_code = "code";
	public static final String channel_client_id = "client_id";
	public static final String channel_client_secret = "client_secret";
	public static final String channel_redirect_uri = "redirect_uri";
	public static final String channel_view = "view";
	public static final String channel_web = "web";
	public static final String channel_access_token = "access_token";

	//TaobaoSTSVCIMP
	public static final String taobao_trades_sold_map_key = "trades_sold_increment_get_response";
	public static final String taobao_trades_map_key = "trades";
	public static final String taobao_tid = "tid";
	public static final String taobao_oid = "oid";
	public static final String taobao_work_type = "taobao";
	public static final String taobao_order_tbt_content_key = "orderTbtObj";
	public static final String taobao_order_tbo_content_key = "orderTboList";
	public static final String taobao_error_response = "error_response";
	
	public static final String shipment_content_all_status = ",travelDate,travelEndDate,finalSaleDate,";
	
	public static final String airway_content_all_status = ",nameCn,alterDate,";

}