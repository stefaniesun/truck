package xyz.util;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import xyz.dao.CommonDao;

@Component
public final class OrderContentUtil {

	@Resource
	private CommonDao commonDao;
	
	@Resource
	ProductUtil productUtil;
	
	public String getOrderContentWhereSql(String queryJson,
	                                      String clientCode,
	                                      String status,
	                                      String dateType,
	                                      Date startDate,
	                                      Date endDate){
	    
        StringBuffer whereSql = new StringBuffer();
        
        if(StringUtil.yqqQueryExists(queryJson, "")){
            whereSql.append(" and order_num = '"+StringUtil.yqqQueryString(queryJson, Constant.nulo)+"'");
        }
        if(StringTool.isNotNull(clientCode)){
            whereSql.append(" and client_code = '"+clientCode+"'");
        }
        if(StringUtil.yqqQueryExists(queryJson, "tid")){
            whereSql.append(" and tid = '"+StringUtil.yqqQueryString(queryJson, "tid")+"'");
        }
        if(StringUtil.yqqQueryExists(queryJson, "oid")){
            whereSql.append(" and oid = '"+StringUtil.yqqQueryString(queryJson, "oid")+"'");
        }
        if(StringUtil.yqqQueryExists(queryJson, "product")){
            whereSql.append(" and product = '"+StringUtil.yqqQueryString(queryJson, "product")+"'");
        }
        if(StringUtil.yqqQueryExists(queryJson, "tkview")){
            whereSql.append(" and tkview = '"+StringUtil.yqqQueryString(queryJson, "tkview")+"'");
        }
        if(StringUtil.yqqQueryExists(queryJson, "flag")){
            whereSql.append(" and flag in ("+StringUtil.yqqQueryString(queryJson, "flag")+")");
        }
        if(StringTool.isNotNull(status)){
            for (String statusStr : status.split(Constant.separator)) {
                if(Constant.q_order_content_all_status.contains(statusStr)){
                    if(statusStr.equals(Constant.q_order_status_not_over)){
                        whereSql.append(" and is_over = 0");
                    }
                    if(statusStr.equals(Constant.q_order_status_over)){
                        whereSql.append(" and is_over = 1");
                    }
                    if(statusStr.equals(Constant.q_order_status_not_pay)){
                        whereSql.append(" and is_pay = 0");
                    }
                    if(statusStr.equals(Constant.q_order_status_pay)){
                        whereSql.append(" and is_pay = 1");
                    }
                    if(statusStr.equals(Constant.q_order_status_not_refound_money)){
                        whereSql.append(" and is_refound_money = 0");
                    }
                    if(statusStr.equals(Constant.q_order_status_refound_money)){
                        whereSql.append(" and is_refound_money = 1");
                    }
                    if(statusStr.equals(Constant.q_order_status_not_refount_stock)){
                        whereSql.append(" and is_refount_stock = 0");
                    }
                    if(statusStr.equals(Constant.q_order_status_refount_stock)){
                        whereSql.append(" and is_refount_stock = 1");
                    }
                    if(statusStr.equals(Constant.q_order_status_not_send)){
                        whereSql.append(" and is_send = 0");
                    }
                    if(statusStr.equals(Constant.q_order_status_send)){
                        whereSql.append(" and is_send = 1");
                    }
                    if(statusStr.equals(Constant.q_order_status_not_surpass)){
                        whereSql.append(" and is_surpass = 0");
                    }
                    if(statusStr.equals(Constant.q_order_status_surpass)){
                        whereSql.append(" and is_surpass = 1");
                    }
                }
            }
        }
        if(StringUtil.yqqQueryExists(queryJson, "cruise")){
            whereSql.append(" and cruise = '"+StringUtil.yqqQueryString(queryJson, "cruise")+"'");
        }
        if (StringUtil.yqqQueryExists(queryJson, "channel")) {
            whereSql.append(" and channel = '"+StringUtil.yqqQueryString(queryJson, "channel")+"'");
        }
        if (StringUtil.yqqQueryExists(queryJson, "ptview")) {
            whereSql.append(" and product = '"+StringUtil.yqqQueryString(queryJson, "ptview")+"'");
        }
        if(StringUtil.yqqQueryExists(queryJson, "shipment")){
            whereSql.append(" and shipment = '"+StringUtil.yqqQueryString(queryJson, "shipment")+"'");
        }
        if(StringUtil.yqqQueryExists(queryJson, "shipmentMark")){
            whereSql.append(" and shipment_mark = '"+StringUtil.yqqQueryString(queryJson, "shipmentMark")+"'");
        }
        if(StringUtil.yqqQueryExists(queryJson, "cabin")){
            whereSql.append(" and cabin = '"+StringUtil.yqqQueryString(queryJson, "cabin")+"'");
        }
        if(StringUtil.yqqQueryExists(queryJson, "provider")){
            whereSql.append(" and provider = '"+StringUtil.yqqQueryString(queryJson, "provider")+"'");
        }
        if(StringUtil.yqqQueryExists(queryJson, "source")){
            whereSql.append(" and source = '"+StringUtil.yqqQueryString(queryJson, "source")+"'");
        }
        if(StringUtil.yqqQueryExists(queryJson, "buyer")){
            whereSql.append(" and buyer = '"+StringUtil.yqqQueryString(queryJson, "buyer")+"'");
        }
        if(StringUtil.yqqQueryExists(queryJson, "linkMan")){
            whereSql.append(" and link_man = '"+StringUtil.yqqQueryString(queryJson, "linkMan")+"'");
        }
        if(StringUtil.yqqQueryExists(queryJson, "linkPhone")){
            whereSql.append(" and link_phone = '"+StringUtil.yqqQueryString(queryJson, "linkPhone")+"'");
        }
        if(StringUtil.yqqQueryExists(queryJson, "address")){
            whereSql.append(" and address = '"+StringUtil.yqqQueryString(queryJson, "address")+"'");
        }
        if(StringUtil.yqqQueryExists(queryJson, "email")){
            whereSql.append(" and email = '"+StringUtil.yqqQueryString(queryJson, "email")+"'");
        }
        if(StringUtil.yqqQueryExists(queryJson, "operaterAdd")){
            whereSql.append(" and operater_add = '"+StringUtil.yqqQueryString(queryJson, "operaterAdd")+"'");
        }
        if(StringUtil.yqqQueryExists(queryJson, "operaterAlter")){
            whereSql.append(" and operater_alter = '"+StringUtil.yqqQueryString(queryJson, "operaterAlter")+"'");
        }
        
        if(StringTool.isNotNull(dateType)){
            if(Constant.q_order_content_all_date_type.contains(dateType)){
                if(startDate != null){
                    whereSql.append(" and "+dateType+" >= '"+DateUtil.dateToLongString(startDate)+"'");
                }
                if(endDate != null){
                    whereSql.append(" and "+dateType+" <= '"+DateUtil.dateToLongString(endDate) + "'");
                }
            }
        }
        
        return whereSql.toString();
	}
}
