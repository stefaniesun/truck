$(document).ready(function() {
	
	initTable();
	
	xyzInitQueryToolForOrder("orderContentAutoId");

	xyzTextbox('clientCode');
	
	$("#orderContentQueryButton").click(function() {
		loadTable();
	});

	$("#relaxOrderCoreButton").click(function() {
		if (relaxOrderCoreAjax()) {
			top.$.messager.alert("提示", "操作成功!", "info");
			$("#orderCoreTable").datagrid("reload");
		}
	});
	
});

function initTable() {
	var toolbar = [];
	if (xyzControlButton("buttonCode_y20161215100506")) {
		toolbar[toolbar.length] = {
			text : '内部下单',
			iconCls : 'icon-add',
			handler : function() {
				addInsideOrderContentButton();
			}
		};
		toolbar[toolbar.length] = '-';
	}
	
	var columns = [
		{field:'checkboxTemp',checkbox:true,hidden:true},
		{field:'orderNum',title:'订单编号',width:126,align:'center',
			formatter : function(value, row, index) {
				if(xyzControlButton("buttonCode_s20161209095401") && row.countClient != row.countOver) {
					return xyzGetA(value,"managerOrderContent",[row.orderNum],"点我查看订单详情","blue");
				}else{
					return value;
				}
			}
		},
		{field:'countClient',title:'票数',width:32,align:'center'},
		{field:'isHang',title:'挂起',width:32,align:'center',
			formatter:function(value, row, index){
				if(value == 0) {
					return "<span style='font-size:18px;font-weight:bold;'>×</span>";
				}else{
					return "<span style='font-size:18px;font-weight:bold;'>√</span>";
				}
			},
			styler:function(value, row, index) {
				if(value == 0) {
					return "background-color:green";
				}else{
					return "background-color:red";
				}
			}
		},
		{field:'isPay',title:'付款',width:30,align:'center',
			formatter:function(value, row, index){
				if(value == 0) {
					return "<span style='font-size:18px;font-weight:bold;'>×</span>";
				}else{
					return "<span style='font-size:18px;font-weight:bold;'>√</span>";
				}
			},
			styler:function(value, row, index) {
				if(value == 1) {
					return "background-color:green";
				}else{
					return "background-color:red";
				}
			}
		},
		{field:'countSend',title:'发货',width:32,align:'center',
			formatter:function(value, row, index){
				if(value == row.countClient) {
					return "<span style='font-size:18px;font-weight:bold;'>√</span>";
				}else{
					return "<span style='font-size:18px;font-weight:bold;'>×</span>";
				}
			},
			styler:function(value, row, index) {
				if(value == row.countClient) {
					return "background-color:green";
				}else{
					return "background-color:red";
				}
			}
		},
		{field:'countRefound',title:'退款',width:32,align:'center',
			formatter:function(value, row, index){
				if(value == 1) {
					return "<span style='font-size:18px;font-weight:bold;'>√</span>";
				}else{
					return "<span style='font-size:18px;font-weight:bold;'>×</span>";
				}
			},
			styler:function(value, row, index) {
				if(value > 0) {
					return "background-color:gold";
				}else{
					return "background-color:green";
				}
			}
		},
		{field:'isOver',title:'冻结',width:32,align:'center',
			formatter:function(value, row, index){
				if(value == 1) {
					return "<span style='font-size:18px;font-weight:bold;'>√</span>";
				}else{
					return "<span style='font-size:18px;font-weight:bold;'>×</span>";
				}
			},
			styler:function(value, row, index) {
				if(value > 0) {
					return "background-color:green";
				}else{
					return "background-color:red";
				}
			}
		},
		{field:'product',title:'商品/产品编号',width:30,align:'center',hidden:true,
			formatter:function(value, row, index) {
				return xyzGetDiv(value);
			}
		},
		{field:'tkview',title:'单品编号',width:60,align:'center',hidden:true,
			formatter:function(value, row, index) {
				return xyzGetDiv(value);
			}
		},
		{field:'stock',title:'库存编号',width:80,hidden:true,halign:'center',
			formatter:function(value,row,index){
            	return xyzGetDiv(value, 0, 100);
            }
		},
		{field:'orderPrice',title:'订单金额',width:60,sortable:true,align:'center',
			formatter: function(value,row,index){
	    		return xyzGetPrice(value);
			}
		},
		{field:'payAmount',title:'付款金额',width:60,align:'center',
			formatter: function(value,row,index){
	    		return xyzGetPrice(value);
			}
		},
		{field:'cruiseNameCn',title:'邮轮名称',width:60,halign:'center',
	    	formatter: function(value,row,index){
	    		return xyzGetDiv(value, 0, 100);
			}
	    },
	    {field:'shipment',title:'航期编号',width:60,hidden:true,halign:'center',
	    	formatter: function(value,row,index){
	    		return xyzGetDiv(value, 0, 100);
			}
	    },
		{field:'shipmentMark',title:'航期代码',width:70,halign:'center',
	    	formatter: function(value,row,index){
	    		return xyzGetDiv(value, 0, 70);
			}
		},
		{field:'shipmentTravelDate',title:'出行时间',hidden:true,width:60,align:'center',
			formatter: function(value,row,index){
	    		return xyzGetDivDate(value);
			}
		},
		{field:'flag',title:'旗子',align:'center',
			formatter:function(value, row, index) {
				value = xyzIsNull(value)?0:value;
				var temp = "<img src='../image/other/bulb_"+value+".png' style='cursor: pointer;'";
				 if(xyzControlButton("buttonCode_x20161211150301")){
		        	temp += " onclick='changeDetailFlagColor(\""+row.clientCode+"\","+value+")'";
				}
		        temp += "/>";
		        return temp;
			}
		},
		{field:'cabinNameCn',title:'舱型',width:100,halign:'center',
			formatter:function(value, row, index){
				return xyzGetDiv(value,0,100);
			}
		},
		{field:'productNameCn',title:'商品/产品',width:100,halign:'center',
			formatter:function(value, row, index) {
				return xyzGetDiv(value,0,100);
			}
		},
		{field:'tkviewNameCn',title:'单品',width:100,halign:'center',
			formatter:function(value, row, index) {
				return xyzGetDiv(value,0,100);
			}
		},
		{field:'providerNameCn',title:'供应商',width:80,halign:'center',
			formatter:function(value, row, index) {
				return xyzGetDiv(value,0,80);
			}
		},
		{field:'source',title:'来源',width:38,align:'center',
			formatter:function(value, row, index) {
				return xyzGetDiv(value,0,38);
			}
		}, 
		{field:'buyer',title:'买家',width:50,halign:'center',
			formatter:function(value, row, index) {
				return xyzGetDiv(value, 0,50);
			}
		},
		{field:'tid',title:'宝贝订单编号',width:85,halign:'center',
			formatter:function(value, row, index) {
				return xyzGetDiv(value);
			}
		},
		{field:'channelNameCn',title:'渠道',width:100,halign:'center',
			formatter:function(value, row, index){
				return xyzGetDiv(value,0,100);
			}
		},
		{field:'addDate',title:'录单日',sortable:true,width:50,hidden:true,halign:'center',
			formatter:function(value, row, index) {
				return xyzGetDiv(value, 5, 5);
			}
		}, 
		{field:'sendDate',title:'发货时间',sortable:true,hidden:true,halign:'center',
			formatter:function(value, row, index) {
				return xyzGetDiv(value, 5, 5);
			}
		}, 
		{field:'payDate',title:'到账时间',sortable:true,hidden:true,halign:'center',
			formatter:function(value, row, index) {
				return xyzGetDivDate(value);
			}
		},
		{field:'overDate',title:'冻结时间',sortable:true,hidden:true,halign:'center',
			formatter:function(value, row, index) {
				return xyzGetDivDate(value);
			}
		}, 
		{field:'alterDate',title:'修改时间',sortable:true,hidden:true,halign:'center',
			formatter : function(value, row, index) {
				return xyzGetDivDate(value);
			}
		}, 
		{field:'linkman',title:'联系姓名',width:60,hidden:true,halign:'center'},
		{field:'linkPhone',title:'联系电话',width:100,hidden:true,halign:'center'},
		{field:'deleteOrderCore',title:"操作",width:32,align:'center',
			formatter:function(value, row, index) {
				if (xyzControlButton("buttonCode_s20161209095402")) {
					var img1 = "<img src='../image/other/deletePage.png' alt='删' title='删除' style='cursor:pointer;' onclick='deleteOrderCore(\""+ row.orderNum + "\");'/>";
					return img1;
				}
				return "";
			}
		}
	];
	
	xyzgrid({
		table:'orderContentManagerTable',
		title:"订单列表",
		url:'../OrderContentWS/queryOrderContentList.do',
		idField:'orderNum',
		onSelect:function(rowIndex, rowData) {
			setOrderCoreRemark(rowData.orderNum);
		},
		toolbar:toolbar,
		view:false,
		columns:[columns]
	});
}

function loadTable() {
	
	var clientCode = $("#clientCode").val();
	var status = $("#status").combobox("getValues").join(',');
	var dateType = $("#dateType").combobox("getValue");
	var startDate = $("#startDate").datebox("getValue");
	var endDate = $("#endDate").datebox("getValue");
	var queryJson = yqqQueryPlugin.getValue("orderContentAutoId");
	
	$("#orderContentManagerTable").datagrid("load", {
		clientCode : clientCode,
		status : status,
		dateType : dateType,
		startDate : startDate,
		endDate : endDate,
		queryJson : queryJson
	});
}

function loadTableByKefu() {
	yqqQueryPlugin.setValue({
		pluginId : "workHandleAutoId",
		values : [
			{
				leftKey : "operatorAdd",
				leftValue : "录单人",
				rightKey : top.currentUserUsername,
				rightValue : top.currentUserUsername
			}
		]
	});
	$("#flagStr").combobox("setValue","noClient");
	loadTable();
}

function managerOrderContent(orderNum,channel) {
	var flag = holdOrderCoreAjax(orderNum);
	if (!flag) {
		return;
	}
	xyzdialog({
		dialog : 'dialogFormDiv_managerOrderContent',
		title : "订单处理【订单号" + orderNum + "】",
		content : "<iframe id='dialogFormDiv_managerOrderContentIframe' frameborder='0'></iframe>",
		buttons : [ {
			text : '返回',
			handler : function() {
				relaxOrderCoreAjax();
				$("#dialogFormDiv_managerOrderContent").dialog("destroy");
				$("#orderCoreTable").datagrid("reload");
			}
		} ]
	});
	var tempWidth = $("#dialogFormDiv_managerOrderContent").css("width");
	var tempHeight = $("#dialogFormDiv_managerOrderContent").css("height");
	var tempWidth2 = parseInt(tempWidth.split("px")[0]);
	var tempHeight2 = parseInt(tempHeight.split("px")[0]);
	$("#dialogFormDiv_managerOrderContentIframe").css("width",(tempWidth2 - 20) + "px");
	$("#dialogFormDiv_managerOrderContentIframe").css("height",(tempHeight2 - 50) + "px");
	$("#dialogFormDiv_managerOrderContentIframe").attr("src","../jsp_core/orderContentDetailManager.html?orderNum="+orderNum+"&isHandleFlag=1");
}

function deleteOrderCore(orderNum) {
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		if(r){
			$.ajax({
				url : '../OrderContentWS/deleteOrderContent.do',
				type : 'POST',
				data : {
					orderNum : orderNum
				},
				dataType : 'json',
				async : false,
				success : function(data) {
					if (data.status == 1) {
						top.$.messager.alert("提示", "操作成功!", "info");
						$("#orderCoreTable").datagrid("reload");
					} else {
						top.$.messager.alert("警告", data.msg, "warning");
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
				}
			});
		   }
		});
}

function cleanOrderContentPrice() {
	$.ajax({
		url : '../WorkHandleWS/cleanOrderContentPrice.do',
		type : 'POST',
		data : {
			count : 100
		},
		dataType : 'json',
		async : false,
		success : function(data) {
			if (data.status == 1) {
				top.$.messager.alert("提示", "操作成功：清洗了" + data.content + "个订单",
						"info");
				$("#orderCoreTable").datagrid("reload");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function mergeOrderCore() {
	var contentHtml = "<form id='mergeOrderCoreFrom'><table align='center' id='orderCoreDetail'>";
	contentHtml += "<tr><td><img  src='../image/other/addPage.gif' onclick='addOrderCore()'/></td></tr>";
	contentHtml += "</table></form>";

	xyzdialog({
		dialog : 'dialogFormDiv_mergeOrderCore',
		title : "合并订单",
		content : contentHtml,
		fit : false,
		height : 200,
		width : 400,
		buttons : [ {
			text : '确定',
			handler : function() {
				mergeOrderCoreSubmit();
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_mergeOrderCore").dialog("destroy");
			}
		} ],
		onOpen : function() {

			$("#orderCoreDeForm").validatebox({
				required : true,
				validType : "length[0,100]"

			});
			$("#orderCoreDe1Form").validatebox({
				required : true,
				validType : "length[0,100]"

			});
			addOrderCore();
			addOrderCore();
		}
	});
}
function batchHandleOper() {
	var contentHtml = "<br/>模拟格式(前面是票号 后面是供单号 请注意:号和;号 均是英文字符)";
	contentHtml += "<div>";
	contentHtml += "<textarea id='batchHandleOperTempForm' style='width: 500px;height: 50px' readonly='readonly'></textarea>";
	contentHtml += "</div><br/>";
	contentHtml += "<div>";
	contentHtml += "<textarea id='batchHandleOperForm' style='width: 550px;height:200px'></textarea>";
	contentHtml += "</div>";
	
	xyzdialog({
		dialog : 'dialogFormDiv_batchHandleOper',
		title : "批量出票",
		content : contentHtml,
		fit : false,
		height : 400,
		width : 600,
		buttons : [ {
			text : '确定',
			handler : function() {
				batchHandleOperSubmit();
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_batchHandleOper").dialog("destroy");
			}
		} ],
		onOpen : function() {
			$("#batchHandleOperTempForm").val("ABCP1111111111111 : GDH001 ;\nABCP2222222222222 : GDH002 ;");
			$("#batchHandleOperForm").validatebox({
				required : true,
			});
		}
	});
}

function batchHandleOperSubmit() {
	if (!$("#batchHandleOperForm").form('validate')) {
		return false;
	}
	var batchHandleOper=$("#batchHandleOperForm").val();
	if(batchHandleOper.split("：").length>1||batchHandleOper.split("；").length>1){
		top.$.messager.alert("提示", ":号;号必须是英文状态下的！", "info");
		return;
	}
	$.ajax({
		url : '../WorkHandleWS/batchHandleOper.do',
		type : 'POST',
		data : {
			batchHandleOper : batchHandleOper
		},
		dataType : 'json',
		async : false,
		success : function(data) {
			if (data.status == 1) {
				var content=data.content;
				var contentHtml = "<div>";
				contentHtml += "<div>成功数量"+content.successCount+"</div>";
				contentHtml += "<div>失败数量"+content.errorCount+"</div><br/>";
				contentHtml += "<div>失败的票号</div>";
				contentHtml += "<textarea id='errorNum' style='width: 550px;height: 200px'></textarea>";
				contentHtml += "</div>";
				
				xyzdialog({
					dialog : 'dialogFormDiv_returnMsg',
					title : "出票详情",
					content : contentHtml,
					fit : false,
					height : 400,
					width : 600,
					buttons : [ {
						text:'确定',
						handler:function(){
							$("#dialogFormDiv_returnMsg").dialog("destroy");
						}
					}],onOpen:function(){
						$("#errorNum").val(content.errorClientCodes);
					}
				});
				$("#orderCoreTable").datagrid("reload");
				$("#dialogFormDiv_batchHandleOper").dialog("destroy");
				
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function addOrderCore() {
	var ppnum = $("#orderCoreDetail tr[id^='orderCoreDe_']").length;
	var contentHtml = "<tr id='orderCoreDe_" + ppnum
			+ "'><td>订单编号</td><td><input style='width:200px;' id='numberCodeForm_" + ppnum
			+ "' type='text'/></td></tr> ";
	$("#orderCoreDetail").append(contentHtml);
	$("#numberCodeForm_" + ppnum).validatebox({
		required : true,
		validType : 'length[0,100]'
	});
};
function mergeOrderCoreSubmit() {
	if (!$("#mergeOrderCoreFrom").form('validate')) {
		return false;
	}
	var orderCoreDeNum = $("#orderCoreDetail tr[id^='orderCoreDe_']").length;
	var orderCoreDeNumJsonStr = [];
	for ( var i = 0; i < orderCoreDeNum; i++) {
		var numberCode = $("#numberCodeForm_" + i).val();
		orderCoreDeNumJsonStr[orderCoreDeNumJsonStr.length] = numberCode;
	}
	var orderNums = orderCoreDeNumJsonStr.join(",");
	$.ajax({
		url : '../WorkHandleWS/mergeOrderCore.do',
		type : 'POST',
		data : {
			orderNums : orderNums
		},
		dataType : 'json',
		async : false,
		success : function(data) {
			if (data.status == 1) {
				top.$.messager.alert("提示", "操作成功,新的订单号是["+data.content+"]", "info");
				$("#orderCoreTable").datagrid("reload");
				$("#dialogFormDiv_mergeOrderCore").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function showReviewCore(orderNum) {
	var row = xyzGetCurrentRow("orderCoreTable", "orderNum", orderNum);
	var channelIn = (parseFloat(xyzIsNull(row.channelPrice) ? 0
			: row.channelPrice) + parseFloat(xyzIsNull(row.appendChannelPPrice) ? 0
			: row.appendChannelPPrice));
	var zhifubaoIn = (parseFloat(xyzIsNull(row.zhifubaoPrice) ? 0
			: row.zhifubaoPrice) + parseFloat(xyzIsNull(row.appendZhifubaoPrice) ? 0
			: row.appendZhifubaoPrice));
	var profit = (channelIn - row.priceShouldOut);
	var profitMargin = (profit / row.priceShouldOut);
	if (profitMargin == Infinity || isNaN(profitMargin)) {
		profitMargin = 0;
	}
	var countAccount = row.countAccount;

	var now = new Date().getTime;

	var dateInfo = row.dateInfo;
	var timeStrChange1 = null;
	var dateInfoFlag = true;
	if (!xyzIsNull(dateInfo)) {
		var dateInfoTime = dateInfo.split(",");
		var time1 = null;
		for ( var i = 0; i < dateInfoTime.length; i++) {
			var timeStr = new Date(dateInfoTime[i].replace(/-/g, "/"))
					.getTime();
			if (time1 == null) {
				timeStrChange1 = dateInfoTime[i];
				time1 = timeStr;
			} else if (timeStr > time1) {
				time1 = timeStr;
				timeStrChange1 = dateInfoTime[i];
			}
		}
		if ((now - time1) > 3 * 24 * 3600 * 1000) {
			dateInfoFlag = false;
		}
	}

	var accountDate = row.accountDate;
	var timeStrChange2 = null;
	var accountDateFlag = true;
	if (!xyzIsNull(accountDate)) {
		var accountDateTime = accountDate.split(",");
		var time2 = null;

		for ( var i = 0; i < accountDateTime.length; i++) {
			var timeStr = new Date(accountDateTime[i].replace(/-/g, "/"))
					.getTime();
			if (time2 == null) {
				timeStrChange2 = accountDateTime[i];
				time2 = timeStr;
			} else if (timeStr > time2) {
				time2 = timeStr;
				timeStrChange2 = accountDateTime[i];
			}
		}
		if ((now - time2) > 15 * 24 * 3600 * 1000) {
			accountDateFlag = false;
		}
	}

	var noTicket = row.count - row.countClient;
	var countRefund = row.countRefund;
	var contentHtml = "<table id='channelDetailTable_showReviewCore'>";
	contentHtml += "<tr><td>收款</td><td  style='text-align: center;'>"
			+ channelIn + "</td></tr>";
	contentHtml += "<tr><td>到账</td><td  style='text-align: center;'>"
			+ zhifubaoIn + "</td></tr>";
	contentHtml += "<tr><td>成本</td><td  style='text-align: center;'>"
			+ row.priceShouldOut + "</td></tr>";
	contentHtml += "<tr><td>利润</td><td id='profitForm' style='text-align: center;'>"
			+ profit + "</td></tr>";
	contentHtml += "<tr><td>利润率</td><td id='profitMarginForm' style='text-align: center;'>"
			+ profitMargin + "</td></tr>";
	contentHtml += "<tr><td>最晚出行日期</td><td id='dateInfoForm' style='text-align: center;'>"
			+ timeStrChange1 + "</td></tr>";
	contentHtml += "<tr><td>最晚退票日期</td><td id='accountDateForm' style='text-align: center;'>"
			+ timeStrChange2 + "</td></tr>";
	contentHtml += "<tr><td>未出票张数</td><td id='noTicketForm' style='text-align: center;'>"
			+ noTicket + "</td></tr>";
	contentHtml += "<tr><td>退票张数</td><td id='countRefundForm' style='text-align: center;'>"
			+ countRefund + "</td></tr>";
	contentHtml += "<tr><td>当前到账状态</td><td id='countAccountForm' style='text-align: center;'>"
			+ countAccount + "</td></tr>";
	contentHtml += "<tr><td>是否验证打款</td><td style=\"text-align:center\"><input type=\"checkbox\" value=\"1\" id=\"isChackPayPrice\"/></td></tr>";
	contentHtml += "</table>";

	var buttons = [];

	if (xyzControlButton("buttonCode_y20150609161101")) {
		buttons[buttons.length] = {
			text : '成本审核通过',
			handler : function() {
				updateOrderContentForCheck(row.orderNum, 1);
			}
		};
		buttons[buttons.length] = {
			text : '成本审核不通过',
			handler : function() {
				updateOrderContentForCheck(row.orderNum, 0);
			}
		};
	}
	buttons[buttons.length] = {
		text : '返回',
		handler : function() {
			$('#dialogFormDiv_showReviewCore').dialog('destroy');
		}
	};

	xyzdialog({
		dialog : 'dialogFormDiv_showReviewCore',
		title : "审核订单",
		position : 'none',
		content : contentHtml,
		fit : false,
		height : 400,
		width : 250,
		buttons : buttons,
		onOpen : function() {
			if (profit >= 100 || profit <= 0) {
				$("#profitForm").css("background-color", "red");
			}
			if ((profitMargin) > 0.5) {
				$("#profitMarginForm").css("background-color", "red");
			}
			if (noTicket > 0) {
				$("#noTicketForm").css("background-color", "red");
			}
			if (countRefund > 0) {
				$("#countRefundForm").css("background-color", "red");
			}
			if (countAccount < row.count) {
				$("#countAccountForm").css("background-color", "red");
			}
			if (dateInfoFlag) {
				$("#dateInfoForm").css("background-color", "red");
			}
			if (accountDateFlag) {
				$("#accountDateForm").css("background-color", "red");
			}
		}
	});
}

function updateOrderContentForCheck(orderNum, flag) {

	var flagPay = $("input[id='isChackPayPrice']:checked").val();
	if (xyzIsNull(flagPay)) {
		flagPay = 0;
	}

	$.ajax({
		url : "../WorkHandleWS/updateOrderContentForCheck.do",
		type : "POST",
		data : {
			orderNum : orderNum,
			flagCheck : flag,
			flagPay : flagPay
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#dialogFormDiv_showReviewCore").dialog("destroy");
				top.$.messager.alert("提示", "操作成功", "info");
				$("#orderCoreTable").datagrid("reload");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function changeDetailFlagColor(clientCode ,flagColor){
	var htmlContent = '';
	htmlContent += '<input name="flagColor" id="flagColor_0" type="radio" value="0"/><img src="../image/other/bulb_0.png" style="cursor: pointer;"/>';
	htmlContent += '<input name="flagColor" id="flagColor_1" type="radio" value="1"/><img src="../image/other/bulb_1.png" style="cursor: pointer;"/>';
	htmlContent += '<input name="flagColor" id="flagColor_2" type="radio" value="2"/><img src="../image/other/bulb_2.png" style="cursor: pointer;"/>';
	htmlContent += '<input name="flagColor" id="flagColor_3" type="radio" value="3"/><img src="../image/other/bulb_3.png" style="cursor: pointer;"/>';
	htmlContent += '<input name="flagColor" id="flagColor_4" type="radio" value="4"/><img src="../image/other/bulb_4.png" style="cursor: pointer;"/>';
	htmlContent += '<input name="flagColor" id="flagColor_5" type="radio" value="5"/><img src="../image/other/bulb_5.png" style="cursor: pointer;"/>';
	htmlContent += '<input name="flagColor" id="flagColor_6" type="radio" value="6"/><img src="../image/other/bulb_6.png" style="cursor: pointer;"/>';

	xyzdialog({
		dialog : 'dialogFormDiv_changeFlagColor',
		title : "修改["+clientCode+"]的标记(旗子)颜色",
	    fit:false,
	    width:400,
	    height:150,
	    content : htmlContent,
	    buttons:[{
			text:'确定',
			handler:function(){
				var color = $("[name='flagColor']:checked").val();
				changeDetailFlagColorSubmit(clientCode ,color);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_changeFlagColor").dialog("destroy");
			}
		}],
		onOpen:function(){
			var radio = $("[name='flagColor']");
			for(var i=0;i<radio.length;i++) {
		        if(radio[i].value==flagColor){
		        	radio[i].checked=true; 
		            break; 
		        }
		   }
		}
	});
}

function changeDetailFlagColorSubmit(clientCode ,flagColor){
	if(xyzIsNull(flagColor) || xyzIsNull(clientCode)){
		top.$.messager.alert("警告","操作失败:参数有误!","warning");
		return false;
	}
	
	$.ajax({
		url : "../OrderContentWS/editFlagColor.do",
		type : "POST",
		data : {
			clientCode : clientCode,
			flagColor : flagColor,
			tag : '订单'
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				loadTable();
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_changeFlagColor").dialog("destroy");
			}else{
				$("#dialogFormDiv_changeFlagColor").dialog("destroy");
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function addInsideOrderContentButton(){
	
	xyzdialog({
		dialog : 'dialogFormDiv_addInsideOrderContentButton',
		title : '内部下单',
		href : '../jsp_core/addInsideOrderContent.html',
		fit : true,
		width : 500,
		height : 550,
		buttons:[{
			text:'确定',
			handler:function() {
				addInsideOrderContentSubmit();
			}
		}, {
			text:'取消',
			handler:function() {
				$("#dialogFormDiv_addInsideOrderContentButton").dialog("destroy");
			}
		}],
		onLoad:function(){
			$("#count_0").numberbox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}],
				onChange : function (newValue,oldValue){
					if(newValue == oldValue || newValue != oldValue){
						var ptview = $("#ptviewForm_0").combobox('getValue');
						countPrice(ptview, 0);
					}
				}
			});
			
			xyzCombobox({
				required:true,
				combobox:'shipmentForm',
				url:'../ListWS/getPtviewShipmentList.do',
				mode: 'remote',
				onChange : function(newValue, oldValue){
					$('input[id^="ptviewForm_"]').combobox('clear');   
					$('input[id^="ptviewForm_"]').combobox('reload');    
				},
				lazy:false
			});
			
			xyzCombobox({
				required:true,
				combobox:'ptviewForm_0',
				url:'../ListWS/getPtviewList.do',
				mode: 'remote',
				onBeforeLoad: function(param){
					var shipment = $("#shipmentForm").combobox('getValue');
					param.shipment = shipment; 
				},
				onSelect: function(record){
					getStock(record.value,0);
					countPrice(record.value, 0);
				},
				lazy:true
			});
			
			xyzCombobox({
				required:true,
				combobox:'distributor',
				url:'../ListWS/getDistributorNameAndPhoneList.do',
				mode: 'remote',
				onSelect: function(record){
					$("#linkMan").textbox("setValue",record.text);
					$("#linkPhone").textbox("setValue",record.value);
					$("#linkMan").textbox({
						required:true
					});
					$("#linkPhone").textbox({
						required:true
					});
				},
				lazy:false
			});
			
			$("#count_0").numberbox('setValue','1');
			$("#count_0").numberbox({
				required:true,
				min:1,
				precision:0
			});
			$("#earnest").numberbox({
				required:true,
				min:0,
				precision:2,
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}],
			});
			
			xyzTextbox("linkMan");
			xyzTextbox("linkPhone");
			xyzTextbox("remark");
			$("#linkMan").textbox({
				required: true,
				validType:['length[0,50]']
			});
			$("#linkPhone").textbox({
				required: true,
				validType:['phone','length[8,11]']
			});
			$("#endDateForm").datetimebox({
				editable:false
			});
		}
	});
}

function addInsideOrderContentSubmit(){
	if(!$("form").form('validate')){
		return false;
	}
	
	var ptivewJson = [];
	for ( var i = 0; i < $(".ptviewClass").length; i++) {
		var tempIndex = $(".ptviewClass").eq(i).prop('id').split('_')[1];
		
		var tempPtview = $("#ptviewForm_"+tempIndex).combobox('getValue');
		var tempCount = $("#count_"+tempIndex).val();
		
		var temp = {
            ptview : tempPtview,
            count : tempCount
		};
		ptivewJson[ptivewJson.length] = temp;
	}
	
	var ptviewJsonStr = JSON.stringify(ptivewJson);
	var earnest = $("#earnest").val();
	var linkMan = $("#linkMan").textbox("getValue");
	var linkPhone = $("#linkPhone").textbox("getValue");
	var endDate = $("#endDateForm").datetimebox("getValue");
	var remark = $("#remark").val();
	
	$.ajax({
		url:"../OrderContentWS/addInsideOrderContent.do",
		type:"POST",
		data:{
			ptivewJson:ptviewJsonStr,
			earnest:xyzSetPrice(earnest),
	        linkMan:linkMan,
	        linkPhone:linkPhone, 
	        endDate : endDate,
	        remark:remark
		},
		async:false,
		dataType:"json",
		success:function(data) {
			if(data.status==1){
				loadTable();
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_addInsideOrderContentButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function addPtview(){
	
	var maxLength = (Math.random() * 1000000).toFixed(0);
	html = " <tr id='ptviewTr_"+maxLength+"'>";
	html += " <td></td>";
	html += " <td>";
	html += "  <input id='ptviewForm_"+maxLength+"' class='ptviewClass' style='width:300px;'/>";
	html += " </td>";
	html += " <td>";
	html += "  <span id='stock_"+maxLength+"' style='text-align:left;'>0</span>";
	html += " </td>";
	html += " <td>";
	html += "  <span id='ptviewPrice_"+maxLength+"' style='text-align:left;'>0</span>元";
	html += " </td>";
	html += " <td>";
	html += "  <input id='count_"+maxLength+"' type='text' style='width:100px;'>";
	html += " </td>";
	html += " <td>";
	html += "  <img onclick='deletePtview("+maxLength+")' src='../image/other/deletePage.png' alt='减一项' style='cursor:pointer;'/>";
	html += " </td>";
	html += " </tr>";
	
	var tempIndex = $($(".ptviewClass")[$(".ptviewClass").length - 1]).attr('id').split('_')[1];
	$("#ptviewTr_"+tempIndex).after(html);
	
	xyzCombobox({
		required:true,
		combobox:'ptviewForm_'+maxLength,
		url:'../ListWS/getPtviewList.do',
		mode: 'remote',
		onBeforeLoad: function(param){
			var shipment = $("#shipmentForm").combobox('getValue');
			param.shipment = shipment; 
		},
		onSelect: function(record){
			getStock(record.value,maxLength);
			countPrice(record.value, maxLength);
		},
		lazy:true
	});
	
	$("#count_"+maxLength).numberbox({
		icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).numberbox('clear');
			}
		}],
		onChange : function (newValue,oldValue){
			if(newValue == oldValue || newValue != oldValue){
				var ptview = $("#ptviewForm_"+maxLength).combobox('getValue');
				
				countPrice(ptview, maxLength);
			}
		}
	});
	
	$("#count_"+maxLength).numberbox('setValue','1');
	
	$("#count_"+maxLength).numberbox({
		required:true,
		min:1,
		precision:0
	});
	
}

function deletePtview(index){
	if($(".ptviewClass").length <= 1){
		top.$.messager.alert("警告", '最少保留一个产品!', "warning");
		return;
	}
	$("#ptviewTr_"+index).remove();
	countPrice("" ,"");
}

function countPrice(ptview,index){
	
	$.ajax({
		url:"../OrderContentWS/countPrice.do",
		type:"POST",
		data:{
			ptview:ptview
		},
		async:false,
		dataType:"json",
		success:function(data) {
			if(data.status==1){
				$("#ptviewPrice_"+index).html(xyzGetPrice(data.content));
				var price = 0;
				for ( var i = 0; i < $(".ptviewClass").length; i++) {
					var tempIndex = $(".ptviewClass").eq(i).prop('id').split("_")[1];
					var tempCount = parseInt(xyzIsNull($("#count_"+tempIndex).val())==true?0:$("#count_"+tempIndex).val());
					if (xyzIsNull(tempCount)) {
						tempCount = 0;
					}
					price += parseInt($("#ptviewPrice_"+tempIndex).html())*tempCount;
				}
				$("#orderPrice").html(price+"元");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function getStock(ptview,index){
	
	if(xyzIsNull(ptview)){
		$("#stock_"+index).html(0);
		return false;
	}
	
	$.ajax({
		url:"../OrderContentWS/getStockByPtview.do",
		type:"POST",
		data:{
			ptview : ptview
		},
		async:false,
		dataType:"json",
		success:function(data) {
			if(data.status==1){
				$("#stock_"+index).html(data.content);
				$("#count_"+index).numberbox({
					required:true,
					min:1,
					max:data.content,
					precision:0
				});
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}