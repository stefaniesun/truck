/**
 * 订单处理
 * 参数: autoId 就是那个autoId
 **/
function xyzInitQueryToolForOrder(autoId){
	yqqQueryPlugin.create({
		pluginId : autoId,
		autoId : autoId,
		data : [
		    {
		    	key : "buyer",
		    	value : "买家账号"
		    },
		    {
		    	key : "channel",
		    	value : "渠道",
		    	type : "combobox",
		    	data : {
		    		url : '../ListWS/getChannelList.do'
		    	}
		    },
		    {
		    	key : "ptview",
		    	value : "产品",
		    	type : "combobox",
		    	data : {
		    		url : '../ListWS/getPtviewList.do',
		    		lazy : false,
		    		mode : 'remote'
		    	}
		    },
		    {
		    	key:"product",
		    	value:"宝贝",
		    	type:"combobox",
		    	data:{
		    		url : '../ListWS/getTaobaoProductList.do'
		    	}
		    },
		    {
		    	key:"tkview",
		    	value:"单品",
		    	type : "combobox",
		    	data:{
		    		url : '../ListWS/getTkviewList.do',
		    		lazy : false,
		    		mode : 'remote'
		    	}
		    },
		    {
		    	key : "provider",
		    	value : "供应商",
		    	type:"combobox",
		    	data :{
		    		url : '../ListWS/getProviderList.do'
		    	}
		    },
		    {
		    	key : "personInfo",
		    	value : "出行信息"
		    },
	    	{
	    		key : "operatorAdd",
	    		value : "录单人"
	    	},
	    	{
	    		key : "remark",
	    		value : "自定义备注"
	    	}
		]
	});
}