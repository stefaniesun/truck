$(document).ready(function() {
	$.ajaxSettings.async = false;
	setSystemSkin();
});

function setSystemSkin(){
	var currentTheme = top.$("#currentSkin").val();
	if(!xyzIsNull(currentTheme) && currentTheme!="bootstrap"){
		var url = '<link rel="stylesheet" type="text/css" href="../js/library/easyui/themes/'+currentTheme+'/easyui.css"></link>';
		$("head").append(url);
	}
}

function pxTool(value){
	var screenWidth = window.screen.width;	
	return value*screenWidth/1500;
}

function chinaDate(date){
	var year = date.split("-")[0];
	var month = parseInt(date.split("-")[1]);
	var day = parseInt(date.split("-")[2]);
	var result = year+"年"+month+"月"+day+"日";
	return result;
}


function AjaxError(XMLHttpRequest, textStatus, errorThrown) {
	var ErrorInfo = new Array();
	ErrorInfo["parsererror"] = "解析出错！1.长时间未操作可重新登录；2.核查提交数据的正确性。";
	ErrorInfo["timeout"] = "请求超时！1.核查网络状况；2.核查提交数据的正确性。";
	ErrorInfo["error"] = "请求出错！1.核查您的权限；2.核查提交数据的正确性。";
	ErrorInfo["notmodified"] = "网络异常！1.核查浏览器配置；2.关闭浏览器重试。";
	top.$.messager.alert("错误",ErrorInfo[textStatus]+"（提示：这是一个良性错误！）","error");
}

function xyzIsNull(obj){
	if(obj==undefined || obj==null || obj==="" || obj===''){
		return true;
	}else{
		return false;
	}
}

String.prototype.trim=function(){
    return this.replace(/(^\s*)|(\s*$)/g, "");
};

Date.prototype.Format = function(fmt) { // author: meizz
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt)){
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}
	for (var k in o){
		if (new RegExp("(" + k + ")").test(fmt)){
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		}
	}
	return fmt;
};

/**
 * 按钮控制方法
 */
function xyzControlButton(key){
	var authArr = top.currentUserButtons;
	for(var i=0;i<authArr.length;i++){
		if(authArr[i]==key){
			return true;
		}
	}
	return false;
}

function xyzGetUserOpers(keyCode){
	var operArr = top.currentUserOpers;
	if(!xyzIsNull(operArr)){
		for(var i=0;i<operArr.length;i++){
			if(operArr[i].keyCode==keyCode){
				return operArr[i].content;
			}
		}
	}
	return null;
}

function xyzJsonToObject(str){
	if(xyzIsNull(str)){
		return "";
	}else{
		str = str.replace(/\n/g, " ");
		str = str.replace(/\r/g, " ");
		str = str.replace(/\t/g, " ");
		return JSON.parse(str);
	}
}

function xyzCK(d){
	if(d.id==undefined){
		alert("xyz say : 初始化ckeditor 竟敢不传ID");
		return;
	}
	$("#"+d.id).ckeditor({
	width : d.width == undefined?1000:d.width,
	height : d.height == undefined?200:d.height,
	toolbarStartupExpanded : d.toolbarStartupExpanded == undefined?true:d.toolbarStartupExpanded,//显示工具栏，默认显示
	toolbarCanCollapse : d.toolbarCanCollapse == undefined?true:d.toolbarCanCollapse,//折叠按钮，默认有
	toolbar : d.toolbar == undefined?'Full':d.toolbar, //工具栏，默认是Full   可选Basic(简洁)
	skin : d.skin == undefined?'office2013':d.skin, //主题  可选 bootstrapck   office2013  moono
	baseFloatZIndex : d.baseFloatZIndex == undefined?'10000':d.baseFloatZIndex //编辑器的z-index值
	});
}

function xyzGetChannelTypeNameCn(channelType){
	try{
		xyzErpChannelTypes;
	}catch(e){
		xyzErpChannelTypes = null;
	}
	if(xyzIsNull(xyzErpChannelTypes)){
		$.ajax({
			url : "../ListWS/getPlatformList.do",
			type : "POST",
			data : {},
			async : false,
			dataType : "json",
			success : function(data) {
				if(data instanceof Array){
					xyzErpChannelTypes = data;
				}else{
					if(data.status==1){
						xyzErpChannelTypes = data.content;
					}else{
						xyzErpChannelTypes = [];
					}
				}
			}
		});
	}
	for(var xyzErpChannelTypeIndex in xyzErpChannelTypes){
		if(xyzErpChannelTypes[xyzErpChannelTypeIndex].value==channelType){
			return xyzErpChannelTypes[xyzErpChannelTypeIndex].text;
		}
	}
	return channelType;
}

function xyzLog(fmt){
	if(navigator.userAgent.indexOf("MSIE")>0) {  
		alert(JSON.stringify(fmt));
	}else{
		console.info(fmt);
	}
}

function getUrlParameters(){
	var result ={};
	var q = location.search.substr(1);
	 var qs = q.split("&");   
	 if (qs) {   
	 for (var i=0;i<qs.length;i++) {
		 var key = qs[i].substring(0,qs[i].indexOf("="));
		 result[key] = qs[i].substring(qs[i].indexOf("=")+1);
	  }   
	 }   
	 return result;
}

//判断是否是IE9或其他浏览器   IE9以下浏览器返回flase 其他返回true
function isIE9(){
	var flag = true;   
	if(navigator.userAgent.indexOf("MSIE")>0) {    
	    if(navigator.userAgent.indexOf("MSIE 6.0")>0){    
		    flag = false;   
			return flag;
	    }    
	    if(navigator.userAgent.indexOf("MSIE 7.0")>0){   
		    flag = false;   
		    return flag;
	    }    
	    if(navigator.userAgent.indexOf("MSIE 8.0")>0){   
		   flag = false;   
		   return flag;
	    }    
	    if(navigator.userAgent.indexOf("MSIE 9.0")>0){   
	    	return flag;
	    }    
	}else{
			flag = true;   
	}
    return flag;
}
//判断是否是IE10或其他浏览器   IE10以下浏览器返回flase 其他返回true
function isIE10(){
	var flag = true;   
	if(navigator.userAgent.indexOf("MSIE")>0) {    
	    if(navigator.userAgent.indexOf("MSIE 6.0")>0){    
		    flag = false;   
			return flag;
	    }    
	    if(navigator.userAgent.indexOf("MSIE 7.0")>0){   
		    flag = false;   
		    return flag;
	    }    
	    if(navigator.userAgent.indexOf("MSIE 8.0")>0){   
		   flag = false;   
		   return flag;
	    }    
	    if(navigator.userAgent.indexOf("MSIE 9.0")>0){   
	    	flag = false;   
	    	return flag;
	    }    
	}else{   
			flag = true;   
	}    	
    return flag;
}

function xyzAjax(p){
//	var port = window.location.port==""?"":":"+window.location.port;
//	var targetServer = window.location.protocol+"//"+window.location.hostname+port+"/ebmb2b/";
	$.ajax({
		url : p.url.indexOf('../')==0?p.url:xyzGetFullUrl(p.url),
		type : ('type' in p)?p.type:"POST",
		data : ('data' in p)?p.data:{},
		async : ('async' in p)?p.async:false,
		dataType : ('dataType' in p)?p.dataType:"json",
		success : p.success,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown, ('errorTip' in p)?p.errorTip:"layer");
		}
	});
};

function xyzGetFullUrl(url){
	var port = window.location.port==""?"":":"+window.location.port;
	var targetServer = window.location.protocol+"//"+window.location.hostname+port+(window.location.pathname.indexOf('/cms')==0?'/cms/':'/');
	return targetServer+url;
};

//判断当前浏览器和版本
function browserInfo(){
    var ua = navigator.userAgent.toLowerCase();
	var match = /(chrome)[ \/]([\w.]+)/.exec( ua ) ||
		/(webkit)[ \/]([\w.]+)/.exec( ua ) ||
		/(opera)(?:.*version|)[ \/]([\w.]+)/.exec( ua ) ||
		/(msie) ([\w.]+)/.exec( ua ) ||
		ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec( ua ) ||
		[];
    return {browser: match[1] || "",version: match[2] || "0"};
}

//身份证验证
function xyzCheckCard(card){
	var Y,JYM,S, M;
    var idcard_array = new Array();
	var idcard = card;
	var area = { 11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江", 31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外",};
	var Errors = new Array("true", "身份证号码校验错误!", "身份证非法地区!");
	if (area[parseInt(idcard.substr(0, 2))] == null){
		return Errors[2];
	}
	switch (idcard.length){
	case 15:
		if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0 || ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0)) {
			ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/; //测试出生日期的合法性 
		}else {
			ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/; //测试出生日期的合法性 
		}
		if (ereg.test(idcard)){
			return Errors[0];
		}else{
			return Errors[1];
		}
		break;
	case 18:
		if (parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0)) {
			ereg = /^[1-9][0-9]{5}19|20[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/; //闰年出生日期的合法性正则表达式 
		}else {
			ereg = /^[1-9][0-9]{5}19|20[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/; //平年出生日期的合法性正则表达式 
		}
		 if (ereg.test(idcard)){
			for(var i=0;i<idcard.length;i++){
				idcard_array[i]=idcard.substr(i,1);
			}
			S = parseInt(idcard_array[0])*7+ parseInt(idcard_array[1])*9+ parseInt(idcard_array[2])*10+ parseInt(idcard_array[3])*5+ parseInt(idcard_array[4])*8+ parseInt(idcard_array[5])*4+ parseInt(idcard_array[6])*2+ parseInt(idcard_array[7]) * 1 + 
			parseInt(idcard_array[8]) * 6 + parseInt(idcard_array[9]) * 3 + 
			parseInt(idcard_array[10]) * 7  + parseInt(idcard_array[11]) * 9  + parseInt(idcard_array[12]) * 10  + parseInt(idcard_array[13]) * 5  + parseInt(idcard_array[14]) * 8  + parseInt(idcard_array[15]) * 4 + parseInt(idcard_array[16]) * 2 ;
	         
		    Y = S % 11;
		    JYM = "10X98765432";
		    M = JYM.substr(Y,1);
		    if (M == idcard_array[17]){
	           return Errors[0];
	       }else{
	    	   return Errors[1];
		   };
	     }else{
	    	 return Errors[1];
		 }
	 default:
		 return Errors[1];	 
         break;
    }
}

function xyzIsPhone(phone){
	return (/^1[0-9]{10}$/g).test(phone);
}
function xyzIsEmail(email){
	var emailReg=/^\w+([-\.]\w+)*@\w+([\.-]\w+)*\.\w{2,4}$/;
	if (emailReg.test(email)){
		return true;
	}else{
		return false;
	}
}

function xyzGetDiv(content,start,length){
	if(xyzIsNull(content)){
		return "";
	}else{
		return "<span title='"+content+"' style='cursor:pointer'>"+content.substr(start,length)+"</span>";
	}
}

function xyzGetDivDate(content){
	if(xyzIsNull(content)){
		return "";
	}else{
		if(content.substr(11,8)=="00:00:00"){
			return "<span title='"+content+"' style='cursor:pointer'>"+content.substr(5,5)+"</span>";
		}else{
			return "<span title='"+content+"' style='cursor:pointer'>"+content.substr(5,11)+"</span>";
		}
	}
}

function xyzGetA(text,functionName,param,title,color){
	var result = "<span style='text-decoration:underline;cursor:pointer;";
	if(!xyzIsNull(color)){
		result+="color:"+color+";' ";
	}
	result+="'";
	if(!xyzIsNull(title)){
		result+=" title='"+title+"' ";
	}
	
	result += "onclick='"+functionName+"(";
	if(param instanceof Array){
		for(var p=0;p<param.length;p++){
			if(p!=0){
				result += ",";
			}
			if(param[p] instanceof Number){
				result += param[p];
			}else{
				result += "\""+param[p]+"\"";
			}
		}
	}else if(param instanceof Number){
		result += param;
	}else{
		result += "\""+param+"\"";
	}
	result += ")'>";
	result += text;
	result += "</span>";
	return result;
}

function xyzRemoveRe(t){
	if((typeof t)=="string" || t instanceof String){
		var strs = t.split(",");
		var newStr = xyzRemoveRe(strs);
		return newStr.join(",");
	}else if(t instanceof Array){
		var result = [];
		for(var p1 in t){
			var p3 = false;
			for(var p2 in result){
				if(t[p1]==result[p2]){
					p3 = true;
					break;
				}
			}
			if(p3==false){
				result[result.length] = t[p1];
			}
		}
		return result;
	}else{
		return t;
	}
}

function xyzRemoveItem(array,value,item){
	if(array instanceof Array){
		for(var a in array){
			if(xyzIsNull(item)){
				if(array[a]==value){
					array.splice(a,1);
					break;
				}
			}else{
				if(array[a][item]==value){
					array.splice(a,1);
					break;
				}
			}
		}
	}
	return array;
}

function powerResourceManager(type,typeNameCn,numberCode,nameCn){
	xyzdialog({
		dialog : 'dialogFormDiv_powerResourceManager',
		title : "管理"+typeNameCn+"["+nameCn+"]的权限渠道",
	    content : "<iframe id='dialogFormDiv_powerResourceManagerIframe' frameborder='0'></iframe>",
	    buttons:[{
			text:'返回',
			handler:function(){
				$("#dialogFormDiv_powerResourceManager").dialog("destroy");
			}
		}]
	});
	var tempWidth = $("#dialogFormDiv_powerResourceManager").css("width");
	var tempHeight = $("#dialogFormDiv_powerResourceManager").css("height");
	var tempWidth2 = parseInt(tempWidth.split("px")[0]);
	var tempHeight2 = parseInt(tempHeight.split("px")[0]);
	$("#dialogFormDiv_powerResourceManagerIframe").css("width",(tempWidth2-20)+"px");
	$("#dialogFormDiv_powerResourceManagerIframe").css("height",(tempHeight2-50)+"px");
	$("#dialogFormDiv_powerResourceManagerIframe").attr("src","../jsp_base/powerResourceManager.html?numberCode="+numberCode+"&type="+type);
}


/**
 * 通过用户名获取用户昵称
 */
function getNickNameByUsername(username){
	var result="";
	$.ajax({
		url : "../AdminUserWS/getNickNameByUsername.do",
		type : "POST",
		data : {
			username : username,
		},
		async : false,
		dataType : "json",
		success : function(data) {
			result=data.content;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	return result;
}