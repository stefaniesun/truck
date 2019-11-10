function holdOrderCoreAjax(orderNum){
	var result = false;
	$.ajax({
		url : "../OrderContentWS/holdOrderCoreOper.do",
		type : "POST",
		data : {
			orderNum : orderNum
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				result = true;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	return result;
}

function relaxOrderCoreAjax(){
	var result = false;
	$.ajax({
		url : "../OrderContentWS/relaxOrderCoreOper.do",
		type : "POST",
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				result = true;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	return result;
}

function setOrderCoreRemark(orderNum){
	top.$('#remarkGreatDiv').accordion("select","订单备注");
	var result = "";
	$.ajax({
		url : "../OrderContentWS/getOrderContentRemark.do",
		type : "POST",
		data : {
			orderNum : orderNum
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				result = data.content;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
	top.$("#orderNumTop").val(orderNum);
	top.$("#remarkOrderNum").html(orderNum);
	
	if(xyzIsNull(result)){
		top.$("#orderCoreRemarkTop").html("");
	}else{
		var remarkHtml = "";
		var orderRemark = result;
		try{
			var orderRemarkJson = xyzJsonToObject(orderRemark);
			for(var i=0;i<orderRemarkJson.length;i++){
				var temp = orderRemarkJson[i];
				remarkHtml += '<span title="操作人:'+temp.alterOperator+', 操作时间:'+temp.alterDate+'">【'+temp.remark+'】</span><br/>';
			}
		}catch(e){
			remarkHtml = orderRemark.replace(/【/g,"<br/>【");
		}
		top.$("#orderCoreRemarkTop").html(remarkHtml);
	}
}

function showOrderCore(orderNum){
	xyzdialog({
		dialog : 'dialogFormDiv_showOrderCore',
		title : "订单编号【"+orderNum+"】",
		content : "<iframe id='dialogFormDiv_showOrderCoreIframe' frameborder='0'></iframe>",
	    buttons:[{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_showOrderCore").dialog("destroy");
			}
		}]
	});
	var tempWidth = $("#dialogFormDiv_showOrderCore").css("width");
	var tempHeight = $("#dialogFormDiv_showOrderCore").css("height");
	var tempWidth2 = parseInt(tempWidth.split("px")[0]);
	var tempHeight2 = parseInt(tempHeight.split("px")[0]);
	$("#dialogFormDiv_showOrderCoreIframe").css("width",(tempWidth2-20)+"px");
	$("#dialogFormDiv_showOrderCoreIframe").css("height",(tempHeight2-50)+"px");
	$("#dialogFormDiv_showOrderCoreIframe").attr("src","../jsp_core/orderContentDetailManager.html?orderNum="+orderNum);
}
