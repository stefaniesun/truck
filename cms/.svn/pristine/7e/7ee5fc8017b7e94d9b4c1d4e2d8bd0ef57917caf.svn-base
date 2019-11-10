 $(document).ready(function() {

	winWidth = window.innerWidth;

	var parmaeters = getUrlParameters();
	orderNum = parmaeters.orderNum;
	
	
	$("#currentOrderNum").val(orderNum);

	$("#loadOrderCoreQueryButton").click(function() {
		loadOrderContent();
	});
			
	setOrderCoreRemark(orderNum);
	initOrderContent();
	
	cruise = $("#cruise").val();
	shipment = $("#shipment").val();
	
	$("#orderContentForLinkmanButton").click(function(){
		updateOrderContentForLinkman();
	});
});
 
function initOrderContent() {
	var toolbarNo = [];
	var toolbarYes = [];
	if (xyzControlButton("buttonCode_s20161215100501")) {
		toolbarNo[toolbarNo.length] = {
				text : '发货',
				iconCls : 'icon-edit',
				handler : function() {
					updateOrderContentSend();
				}
		};
	}
	if (xyzControlButton("buttonCode_x20161211133102")) {
		toolbarNo[toolbarNo.length] = '-';
		toolbarNo[toolbarNo.length] = {
			text : '新增收款',
			iconCls : 'icon-edit',
			handler : function() {
				editOrderContentByPay();
			}
		};
	}
	if (xyzControlButton("buttonCode_s20161215100502")) {
		toolbarNo[toolbarNo.length] = '-';
		toolbarNo[toolbarNo.length] = {
			text : '退款',
			iconCls : 'icon-edit',
			handler : function() {
				refoundMoneyAndStockButton();
			}
		};
	}
	if (xyzControlButton("buttonCode_s20161215100503")) {
		toolbarNo[toolbarNo.length] = '-';
		toolbarNo[toolbarNo.length] = {
			text : '修改成本价',
			iconCls : 'icon-edit',
			handler : function() {
				editTkviewCostButton();
			}
		};
	}
	if (xyzControlButton("buttonCode_s20161215100504")) {
		toolbarNo[toolbarNo.length] = '-';
		toolbarNo[toolbarNo.length] = {
			text : '修改单品和数量',
			iconCls : 'icon-edit',
			handler : function() {
				editOrderTkviewAndCountButton();
			}
		};
	}
	
	xyzgrid({
		table : 'noClientTable',
		pagination : false,
		singleSelect : false,
		onSelect : function(index,row){
			setOrderCoreRemark(row.orderNum);
		},
		idField : 'clientCode',
		toolbar : toolbarNo,
		title : "未发货的单",
		height : 'auto',
		frozenColumns : [[
			{field:'checkboxTemp',checkbox:true},
			{field:'cabin',title:'舱型编号',hidden:true,halign:'center'},
			{field:'flag',title:'旗子',width:30,align:'center',
            	formatter: function(value,row,index){
					value = xyzIsNull(value)?0:value;
					var temp = "<img src='../image/other/bulb_"+value+".png' style='cursor: pointer;'";
					 if(xyzControlButton("buttonCode_x20161211150301")){
			        	temp += " onclick='changeDetailFlagColor(\""+row.clientCode+"\","+value+")'";
					}
			        temp += "/>";
			        return temp;
				}
            },
			{field:'personInfo',width:30,halign:'center',
				formatter : function(value, row, index) {
					var temp = xyzIsNull(row.personInfo) ? 0 : value
							.split(",").length;
					var countHtml = "<img src='../js/library/easyui/themes/icons/help.png' id='personInfo_\""
							+ row.clientCode
							+ "\"' title='点我查看出行人信息' onclick='updateOrderPerson(\""
							+ row.clientCode
							+ "\",\""
							+ row.personInfo
							+ "\",\""+row.isImplement+"\")' />" + temp;
					return countHtml;
				}
			},
			{field:'clientCode',title:'票单号',width:120,halign:'center'},
			{field:'count',title:'数量',width:40,align:'center'},
			{field:'productPrice',title:'单价',width:55,align:'center',
				formatter:function(value, row, index) {
					return xyzGetPrice(value);
				}
			},
			{field:'cost',title:'成本',width:55,align:'center',
				formatter:function(value, row, index) {
					return xyzGetPrice(value);
				}
			},
			{field:'isPay',title:'付款',width:30,align:'center',
				formatter : function(value, row, index) {
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
			{field : 'isSurpass',title : '超卖 ',width : 30,align:'center',
				formatter : function(value, row, index) {
					if(value == 0) {
						return "<span style='font-size:18px;font-weight:bold;'>×</span>";
					}else{
						var html = "<span style='font-size:18px;font-weight:bold;'>√</span>";
						return html;
					}
				},
				styler : function(value, row, index) {
					if(value == 0) {
						return "background-color:green";
					}else{
						return "background-color:gold";
					}
				}
			},
			 {field:'isOver',title:'结束',width:30,align:'center',
                formatter:function(value, row, index) {
                    if(value == 0) {
                        return "<span style='font-size:18px;font-weight:bold;'>×</span>";
                    }else{
                        return "<span style='font-size:18px;font-weight:bold;'>√</span>";
                    }
                },
                styler:function(value, row, index) {
                    if(value == 0) {
                        return "background-color:gold";
                    }else{
                        return "background-color:green";
                    }
                }
            }
		]],
		columns : [[
            {field:'oid',title:'渠道票单号',width:120,hidden:true,halign:'center'},
            {field:'cruiseNameCn',title:'邮轮名称',width:70,halign:'center',
            	formatter:function(value, row, index) {
            		return xyzGetDiv(value,0,70);
            	}
            },
            {field:'shipmentMark',title:'航期代码',width:70,halign:'center',
            	formatter:function(value, row, index) {
            		return xyzGetDiv(value,0,70);
            	}
            },
            {field:'productNameCn',title:'商品名称',width:120,halign:'center',
            	formatter:function(value, row, index) {
            		return xyzGetDiv(value,0,100);
            	}
            },
			{field:'tkviewNameCn',title:'单品名称',width:120,hidden:true,halign:'center',
				formatter : function(value, row, index) {
					return xyzGetDiv(value,0,100);
				}
			},
			{field:'providerNameCn',title:'供应商',width:70,halign:'center'},
            {field:'source',title:'来源',width:40},
			{field:'refoundAmount',title:'退款',width:30,align:'center',
            	formatter : function(value, row, index) {
					return xyzGetPrice(value);
				}
			},
			{field:'isRefoundStock',title:'是否退库存',width:60,align:'center',hidden:true,
				formatter : function(value, row, index) {
					if(value == 0){
						return "否";
					}
					return "是";
				}
			},
			{field:'payDate',title:'付款时间',width:70,halign:'center',
				formatter : function(value, row, index) {
					return xyzGetDivDate(value);
				}
			},
			{field:'refoundMoneyDate',title:'退款时间',width:70,halign:'center',
				formatter : function(value, row, index) {
					return xyzGetDivDate(value);
				}
			},
			{field:'operaterAlter',title:'修改人',width:50,halign:'center',
				formatter:function(value, row, index) {
            		return xyzGetDiv(value,0,50);
            	}
			},
			{field:'addDate',title:'创建时间',width:70,hidden:true,halign:'center',
				formatter : function(value, row, index) {
					return xyzGetDivDate(value);
				}
			},
			{field:'alterDate',title:'修改时间',width:70,halign:'center',
				formatter : function(value, row, index) {
					return xyzGetDivDate(value);
				}
			}
		]]
	});
	
	xyzgrid({
		table : 'yesClientTable',
		pagination : false,
		singleSelect : false,
		onSelect : function(rowIndex,rowData){
			setOrderCoreRemark(orderNum);
		},
		idField : 'clientCode',
		toolbar : toolbarYes,
		title : "已发货的单",
		height : 'auto',
		frozenColumns : [[
			{field:'checkboxTemp',checkbox:true},
			{field:'cabinType',title:'舱型编号',hidden:true,halign:'center'},
			{field:'flag',title:'旗子',width:30,align:'center',
            	formatter: function(value,row,index){
					value = xyzIsNull(value)?0:value;
					var temp = "<img src='../image/other/bulb_"+value+".png' style='cursor: pointer;'";
					 if(xyzControlButton("buttonCode_x20161211150301")){
			        	temp += " onclick='changeDetailFlagColor(\""+row.clientCode+"\","+value+")'";
					}
			        temp += "/>";
			        return temp;
				}
            },
			{field:'personInfo',width:30,halign:'center',
				formatter:function(value, row, index) {
					var temp = xyzIsNull(row.personInfo) ? 0 : value
							.split(",").length;
					var countHtml = "<img src='../js/library/easyui/themes/icons/help.png' id='personInfo_\""
							+ row.clientCode
							+ "\"' title='点我查看出行信息' onclick='updateOrderPerson(\""
							+ row.clientCode
							+ "\",\""
							+ row.personInfo
							+ "\",\""+row.isImplement+"\")' />" + temp;
					return countHtml;
				}
			},
			{field:'clientCode',title:'票单号',width:120,halign:'center'},
			{field:'count',title:'数量',width:40,align:'center'},
			{field:'productPrice',title:'单价',width:55,align:'center',
				formatter:function(value, row, index) {
					return xyzGetPrice(value);
				}
			},
			{field:'cost',title:'成本',width:55,align:'center',
				formatter:function(value, row, index) {
					return xyzGetPrice(value);
				}
			},
			{field:'isPay',title:'付款',width:30,align:'center',
				formatter:function(value, row, index) {
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
			{field:'isSurpass',title:'超卖 ',width:30,align:'center',
				formatter : function(value, row, index) {
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
						return "background-color:gold";
					}
				}
			},
			 {field:'isOver',title:'结束',width:30,align:'center',
                formatter:function(value, row, index) {
                    if (value == 0) {
                        return "<span style='font-size:18px;font-weight:bold;'>×</span>";
                    } else {
                        return "<span style='font-size:18px;font-weight:bold;'>√</span>";
                    }
                },
                styler:function(value, row, index) {
                    if(value == 0) {
                        return "background-color:gold";
                    }else{
                        return "background-color:red";
                    }
                }
            }
		]],
		columns : [[
            {field:'productNameCn',title:'商品名称',width:120,halign:'center',
            	formatter:function(value, row, index) {
            		return xyzGetDiv(value,0,100);
            	}
            },
			{field:'tkviewNameCn',title:'单品名称',width:120,halign:'center',
				formatter : function(value, row, index) {
					return xyzGetDiv(value,0,100);
				}
			},
			{field:'providerNameCn',title:'供应商',width:70,align:'center'},
            {field:'source',title:'来源',width:40,hidden:true,halign:'center'},
			{field:'refoundAmount',title:'退款',width:30,align:'center'},
			{field:'isRefoundStock',title:'是否退库存',width:60,align:'center',hidden:true,
				formatter : function(value, row, index) {
					if(value == 0){
						return "否";
					}
					return "是";
				}
			},
			{field:'refoundMoneyDate',title:'退款时间',width :70,halign:'center',
				formatter:function(value, row, index) {
					return xyzGetDivDate(value);
				}
			},
			{field:'payDate',title:'付款时间',width:70,halign:'center',
				formatter:function(value, row, index) {
					return xyzGetDivDate(value);
				}
			},
			{field:'sendDate',title:'出票时间',width:70,halign:'center',
				formatter:function(value, row, index) {
					return xyzGetDivDate(value);
				}
			},
			{field:'operaterAlter',title:'修改人',width:50,halign:'center',
				formatter:function(value, row, index) {
            		return xyzGetDiv(value,0,100);
            	}
			},
			{field:'addDate',title:'创建时间',width:70,hidden:true,halign:'center',
				formatter:function(value, row, index) {
					return xyzGetDivDate(value);
				}
			},
			{field : 'alterDate',title : '修改时间',width :70,halign:'center',
				formatter : function(value, row, index) {
					return xyzGetDivDate(value);
				}
			}
		]]
	});
	
	xyzgrid({
		table : 'logWorkTable',
		pagination : false,
		singleSelect : false,
		idField : 'iidd',
		title : "操作日志",
		height : 'auto',
		columns : [[
			{field : 'username',title : '操作人',width : 60},
			{field : 'value',title : '记录标识',width : 110},
			{field : 'addDate',title : '添加时间',width : 120},
			{field : 'remark',title : '备注',width : 700,
				formatter : function(value, row, index) {
					return "<div style='color:blue;' title='"+ value + "'>" + value + "</div>";
					}
				}
			]]
		});
		
		loadOrderContent();
}
 
 function loadOrderContent() {
		$.ajax({
			url : "../OrderContentWS/getOrderContentDetail.do",
		type : "POST",
		data : {
			orderNum : orderNum
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				var tempList = data.content.orderContents;
				var refundPrice = data.content.refundPrice;
				var orderContentOne = tempList[0];
				$("#shipmentMark").text(orderContentOne.shipmentMark);
				$("#linkmanT").text(orderContentOne.linkMan);
				$("#linkPhoneT").text(orderContentOne.linkPhone);
				$("#emailT").text(orderContentOne.email);
				$("#addressT").text(orderContentOne.address);
				$("#orderPrice").html("订单金额 "+xyzGetPrice(orderContentOne.orderPrice));
				$("#payAmount").html("收款金额 "+xyzGetPrice(orderContentOne.payAmount));
				$("#earnest").html("定金 "+xyzGetPrice(orderContentOne.earnest));
				$("#refoundAmount").html("退款金额 "+xyzGetPrice(refundPrice));
				$("#shipmentTravelDate").html("出行时间"+xyzGetDivDate(orderContentOne.shipmentTravelDate));
				
				if (xyzIsNull(orderContentOne.linkMan)) {
					$("#linkmanTd").hide();
				}else {
					$("#linkmanTd").show();
				}
				
				if (xyzIsNull(orderContentOne.linkPhone)) {
					$("#linkPhoneTd").hide();
				}else {
					$("#linkPhoneTd").show();
				}
				
				if (xyzIsNull(orderContentOne.email)) {
					$("#emailTd").hide();
				}else {
					$("#emailTd").show();
				}
				
				if (xyzIsNull(orderContentOne.address)) {
					$("#addressTd").hide();
				}else {
					$("#addressTd").show();
				}
				
				var countCost = 0;
				var yesClientTemp = new Array();
				var noClientTemp = new Array();
				for (var i = 0; i < tempList.length; i++) {
					countCost += tempList[i].cost*tempList[i].count;
					if (tempList[i].isSend == 1) {
						yesClientTemp[yesClientTemp.length] = tempList[i];
					} else {
						noClientTemp[noClientTemp.length] = tempList[i];
					}
				}
				$("#countCost").html("总成本价 "+xyzGetPrice(countCost));
				
				//付款金额-退款金额-成本价
				var profit = orderContentOne.payAmount - refundPrice - countCost;
				
				$("#inPrice").html("净利润 "+xyzGetPrice(profit));
				var dataYes = {
					status : 1,
					content : {total : yesClientTemp.length,rows : yesClientTemp}
				};
				var dataNo = {
					status : 1, 
					content : {total : noClientTemp.length,rows : noClientTemp}
				};
				$("#noClientTable").datagrid({data : dataNo});
				$("#yesClientTable").datagrid({data : dataYes});
				var logWorkList = data.content.logWorkList;
				var dataLogWork = {
					status : 1,
					content : {total : logWorkList.length,rows : logWorkList}
				};
				$("#logWorkTable").datagrid({data : dataLogWork});
				
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus,errorThrown);
		}
	});
};

function editOrderContentByPay() {
	var orderNum = $("#currentOrderNum").val();
	var html = "<center><form><table id='cabinTableForm' style='text-align: center;'>";
	html += " <tr>";
	html += " <td> 订单编号 </td>";
	html += " <td>";
	html += " <span>"+orderNum+"<span>";
	html += " </td>";
	html += " </tr>";
	html += " <tr>";
	html += " <td> 收款金额  </td>";
	html += " <td>";
	html += " <input id='price' type='text' />";
	html += " </td>";
	html += " </tr>";
	html += "</table></form></center>";
	
	xyzdialog({
		dialog : 'dialogFormDiv_editOrderContentByPay',
		title : '修改付款状态',
		content : html,
		fit : false,
		width : 300,
		height : 150,
		buttons : [{
			text : '确定',
			handler : function() {
				editOrderContentByPaySubmit(orderNum);
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_editOrderContentByPay").dialog("destroy");
			}
		}],
		onOpen:function(){
			$("#price").numberbox({
				required:true,
				min:0,
				max:9999999999,
			    precision:2,
			    icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});
		}
	});
};

function editOrderContentByPaySubmit(orderNum) {
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var price = $("#price").numberbox("getValue");
	
	$.ajax({
		url : "../OrderContentWS/editOrderContentByPay.do",
		type : "POST",
		data : {
			orderNum : orderNum,
			money : xyzSetPrice(price)
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				loadOrderContent();
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_editOrderContentByPay").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus,errorThrown);
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
			tag : '票单'
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				loadOrderContent();
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

function setOrderCoreRemark(orderNum){
	top.$('#remarkGreatDiv').accordion("select","订单备注");
	var result = "";
	$.ajax({
		url : "../OrderContentWS/getOrderRemark.do",
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
	if(xyzIsNull(result)){
		top.$("#orderCoreRemarkTop").html("");
	}else{
		var remarkHtml = "";
		var orderRemark = result.orderRemark;
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
		
		var channelRemark = result.channelRemark;
		top.$("#channelRemarkTop").html(channelRemark);
	}
}

function updateOrderContentForLinkman(){
	var html = '<br/>';
	    html += '<table cellspacing="4">';
		html += '<tr>';
		html += '<td style="text-align:right;">';
		html += '联系人';
		html += '</td>';
		html += '<td>';
		html += '<input type="text" id="linkmanForm" style="width:200px;" />';
		html += '</td>';
		html += '</tr>';
		html += '<tr>';
		html += '<td style="text-align:right;">';
		html += '联系人电话';
		html += '</td>';
		html += '<td>';
		html += '<input type="text" id="linkPhoneForm" style="width:200px;" />';
		html += '</td>';
		html += '</tr>';
		html += '<tr>';
		html += '<td style="text-align:right;">';
		html += '邮箱';
		html += '</td>';
		html += '<td>';
		html += '<input type="text" id="emailForm" style="width:200px;" />';
		html += '</td>';
		html += '</tr>';
		html += '<tr>';
		html += '<td style="text-align:right;">';
		html += '收货地址';
		html += '</td>';
		html += '<td>';
		html += '<input type="text" id="addressForm" style="width:200px;" />';
		html += '</td>';
		html += '</tr>';
		html += '</table>';
	
	xyzdialog({
		dialog:'dialogFormDiv_updateOrderContentForLinkman',
		title:"修改联系人",
		width:350,
		height:220,
		content : html,
		fit:false,
		buttons:[{
			text:'确定',
			handler:function(){
				updateOrderContentForLinkmanSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_updateOrderContentForLinkman").dialog("destroy");
			}
		}],
		onOpen : function() {
			$("#linkmanForm").val($("#linkmanT").text());
			$("#linkPhoneForm").val($("#linkPhoneT").text());
			$("#emailForm").val($("#emailT").text());
			$("#addressForm").val($("#addressT").text());
			
			xyzTextbox("linkmanForm");
			xyzTextbox("linkPhoneForm");
			xyzTextbox("emailForm");
			xyzTextbox("addressForm");
		}
	});
}

function updateOrderContentForLinkmanSubmit(){
	var linkman = $("#linkmanForm").val();
	var linkPhone = $("#linkPhoneForm").val();
	var email = $("#emailForm").val();
	var address = $("#addressForm").val();
	
	var reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	
	if (!reg.test(email)) {
		top.$.messager.alert("提示","请输入正确的邮箱格式!","info");
		return;
	}
	
	var phoneReg = /^1[0-9]{10}$/;
	var companyPhoneReg = /^(0[0-9]{2,3})?(\-)?(\ )?([2-9][0-9]{6,7})+$/;
	if((!xyzIsNull(linkPhone)) && (!companyPhoneReg.test(linkPhone)) && (!phoneReg.test(linkPhone)) ){
		top.$.messager.alert("提示","请输入正确的电话格式!","info");
		return;
	}
	
	$.ajax({
		url:"../OrderContentWS/updateOrderContentForLinkman.do",
		type:"POST",
		data:{
			orderNum : orderNum,
			linkman : linkman,
			linkPhone : linkPhone,
			email : email,
			address : address
		},
		async:false,
		dataType:'json',
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功!","info");
				loadOrderContent();
				$("#dialogFormDiv_updateOrderContentForLinkman").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editTkviewCostButton(){
	
	var noClient = $("#noClientTable").datagrid('getChecked'); 
	if (noClient.length < 1 ) {
		top.$.messager.alert("提示","请选择未出票的单个对象!","info");
		return;
	}
	if ((noClient.length > 1 && noClient.length < 1)) {
		top.$.messager.alert("提示","请选择未出票的单个对象!","info");
		return;
	}
	
	var html = '<center><form><br/>';
		html += '<table cellspacing="6">';
		html += '<tr>';
		html += '<td style="text-align:right;">成本价</td>';
		html += '<td>';
		html += '<input id="costForm" style="width:120px;"/>';
		html += '</td>';
		html += '</tr>';
		html += '</table>';
		html += '</form></center>';
		
	xyzdialog({
		dialog : 'dialogFormDiv_editTkviewCostButton',
		title : '修改付款状态',
		content : html,
		fit : false,
		width : 300,
		height : 150,
		buttons : [{
			text : '确定',
			handler : function() {
				if (noClient.length > 0) {
					editTkviewCostSubmit(noClient[0].clientCode);
				}else if (yesClient.length > 0) {
					editTkviewCostSubmit(yesClient[0].clientCode);
				}
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_editTkviewCostButton").dialog("destroy");
			}
		}],
		onOpen:function(){
			if (noClient.length > 0) {
				$('#costForm').numberbox({    
				    min:0.01,
				    max:99999,
				    precision:2,
				    icons: [{
						iconCls:'icon-clear',
						handler: function(e){
							$(e.data.target).numberbox('clear');
						}
					}]
				});  
				$("#costForm").numberbox('setValue',xyzGetPrice(noClient[0].cost));
				$('#costForm').numberbox({    
					required: true   
				}); 
				
			}else if (yesClient.length > 0) {
				$('#costForm').numberbox({    
				    min:0.01,
				    max:99999,
				    precision:2,
				    icons: [{
						iconCls:'icon-clear',
						handler: function(e){
							$(e.data.target).numberbox('clear');
						}
					}]
				});  
				$("#costForm").numberbox('setValue',xyzGetPrice(yesClient[0]));
				$('#costForm').numberbox({    
					required: true   
				}); 
			}
		}
	});
	
}

function editTkviewCostSubmit(clientCode){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var cost = $("#costForm").numberbox('getValue');
	
	$.ajax({
		url : '../OrderContentWS/updateOrderContentCost.do',
		type : "POST",
		data : {
			clientCode:clientCode,
			cost:xyzSetPrice(cost)
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				loadOrderContent();
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_editTkviewCostButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus,errorThrown);
		}
	});
	
}

function editOrderTkviewAndCountButton(){
	
	var noClient = $("#noClientTable").datagrid('getChecked'); 
	if (noClient.length < 1 ) {
		top.$.messager.alert("提示","请选择未出票的单个对象!","info");
		return;
	}
	if ((noClient.length > 1 && noClient.length < 1)) {
		top.$.messager.alert("提示","请选择未出票的单个对象!","info");
		return;
	}
	
	var html = '<center><form><br/>';
		html += '<table cellspacing="6">';
		html += '<tr>';
		html += '<td style="text-align:right;">单品</td>';
		html += '<td>';
		html += '<input id="tkviewForm" style="width:120px;"/>';
		html += '</td>';
		html += '</tr>';
		html += '<tr>';
		html += '<td>';
		html += '数量';
		html += '</td>';
		html += '<td>';
		html += '<input id="countForm" style="width:120px;"/>';
		html += '</td>';
		html += '</tr>';
		html += '</table>';
		html += '</form></center>';
		
	xyzdialog({
		dialog : 'dialogFormDiv_editOrderTkviewAndCountButton',
		title : '修改单品和数量',
		content : html,
		fit : false,
		width : 300,
		height : 200,
		buttons : [{
			text : '确定',
			handler : function() {
				if (noClient.length > 0) {
					editOrderTkviewAndCountSubmit(noClient[0].clientCode);
				}else if (yesClient.length > 0) {
					editOrderTkviewAndCountSubmit(yesClient[0].clientCode);
				}
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_editOrderTkviewAndCountButton").dialog("destroy");
			}
		}],
		onOpen:function(){
			if (noClient.length > 0) {
				
				xyzCombobox({
					combobox:'tkviewForm',
					url:'../ListWS/getTkviewList.do',
					mode:'remote',
					lazy:false,
					icons: [{
						iconCls:'icon-clear',
						handler: function(e){
							$(e.data.target).combobox('clear');
						}
					}]
				});
				$("#tkviewForm").combobox('setValue',noClient[0].tkview);
				xyzCombobox({
					required: true
				});
				
				$('#countForm').numberbox({    
				    min:0.01,
				    max:99999,
				    icons: [{
						iconCls:'icon-clear',
						handler: function(e){
							$(e.data.target).numberbox('clear');
						}
					}]
				});  
				$("#countForm").numberbox('setValue',xyzGetPrice(noClient[0].count));
				$('#countForm').numberbox({    
					required: true   
				}); 
				
			}else if (yesClient.length > 0) {
				
				xyzCombobox({
					combobox:'tkviewForm',
					url:'../ListWS/getTkviewList.do',
					mode:'remote',
					lazy:false,
					icons: [{
						iconCls:'icon-clear',
						handler: function(e){
							$(e.data.target).combobox('clear');
						}
					}]
				});
				$("#tkviewForm").combobox('setValue',yesClient[0].tkview);
				xyzCombobox({
					required: true
				});
				
				$('#countForm').numberbox({    
				    min:0.01,
				    max:99999,
				    icons: [{
						iconCls:'icon-clear',
						handler: function(e){
							$(e.data.target).numberbox('clear');
						}
					}]
				});  
				$("#countForm").numberbox('setValue',xyzGetPrice(yesClient[0].count));
				$('#countForm').numberbox({    
					required: true   
				});  
			}
		}
	});
	
}

function editOrderTkviewAndCountSubmit(clientCode){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var count = $("#countForm").numberbox('getValue');
	var tkview = $("#tkviewForm").combobox('getValue');
	
	$.ajax({
		url : '../OrderContentWS/updateOrderContentTkviewAndCount.do',
		type : "POST",
		data : {
			clientCode:clientCode,
			count:count,
			tkview:tkview
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				loadOrderContent();
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_editOrderTkviewAndCountButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus,errorThrown);
		}
	});
	
}

function refoundMoneyAndStockButton(){
	
	var noClient = $("#noClientTable").datagrid('getChecked'); 
	var yesClient = $("#yesClientTable").datagrid('getChecked'); 
	if (noClient.length < 1 && yesClient.length < 1) {
		top.$.messager.alert("提示","请选择单个对象!","info");
		return;
	}
	if ((noClient.length > 1 && noClient.length < 1)|| (yesClient.length > 1 && yesClient.length < 1)) {
		top.$.messager.alert("提示","请选择单个对象!","info");
		return;
	}
	
	if (noClient.isPay == 0 || yesClient.isPay == 0) {
		top.$.messager.alert("提示","未付款不能退款!","info");
		return;
	}
	
	var html = '<center><form><br/>';
	html += '<table cellspacing="6">';
	html += '<tr>';
	html += '<td style="text-align:right;">退款金额</td>';
	html += '<td>';
	html += '<input id="refoundMoneyForm" style="width:120px;"/>';
	html += '</td>';
	html += '</tr>';
	html += '<tr>';
	html += '<td>';
	html += '退库存数量';
	html += '</td>';
	html += '<td>';
	html += '<input id="refoundStockForm" style="width:120px;"/>';
	html += '</td>';
	html += '</tr>';
	html += '</table>';
	html += '</form></center>';
	
	xyzdialog({
		dialog:'dialogFormDiv_refoundMoneyAndStockButton',
		title:"手工退款以及库存",
		width:300,
		height:'auto',
		content : html,
		fit:false,
		buttons:[{
			text:'确定',
			handler:function(){
				if (noClient.length > 0) {
					refoundMoneyAndStockSubmit(noClient[0].clientCode);
				}else if (yesClient.length > 0) {
					refoundMoneyAndStockSubmit(yesClient[0].clientCode);
				}
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_refoundMoneyAndStockButton").dialog("destroy");
			}
		}],
		onOpen:function(){
				
			$('#refoundMoneyForm').numberbox({    
			    min:0,
			    max:99999,
			    value:0,
			    icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});  
			$('#refoundStockForm').numberbox({    
				min:0,
				max:99999,
				value:0,
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});  
		}
	});
};

function refoundMoneyAndStockSubmit(clientCode) {
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var money = $("#refoundMoneyForm").numberbox('getValue');
	var stock = $("#refoundStockForm").numberbox('getValue');
	
	$.ajax({
		url : "../OrderContentWS/updateOrderContentRefound.do",
		type : "POST",
		data : {
			clientCode:clientCode,
			money:xyzSetPrice(money),
			stock:stock
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				loadOrderContent();
				$("#dialogFormDiv_refoundMoneyAndStockButton").dialog("destroy");
				top.$.messager.alert("提示", "操作成功!", "info");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus,errorThrown);
		}
	});
}

function updateOrderContentSend(){
	
	var noClient = $("#noClientTable").datagrid('getChecked'); 
	if (noClient.length < 1) {
		top.$.messager.alert("提示","请选择未出票的单个对象!","info");
		return;
	}
	if ((noClient.length > 1 && noClient.length < 1)) {
		top.$.messager.alert("提示","请选择未出票的单个对象!","info");
		return;
	}
	
	if (noClient.isPay == 0) {
		top.$.messager.alert("提示","未付款不能发货!","info");
		return;
	}
	
	if (!confirm("发货将会扣库存,是否确认发货?")) {
		return;
	}
	
	var clientCode = "";
	if (noClient.length > 0) {
		clientCode = noClient[0].clientCode;
	}else if (yesClient.length > 0) {
		clientCode = yesClient[0].clientCode;
	}
	
	$.ajax({
		url : '../OrderContentWS/updateOrderContentSend.do',
		type : 'POST',
		data : {
			clientCode : clientCode, 
			isSend : 1
		},
		dataType : 'json',
		async : false,
		success : function(data) {
			if (data.status == 1) {
				loadOrderContent();
				top.$.messager.alert("提示", "操作成功。", "info");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function updateOrderPerson(clientCode, numberCodesStr,isImplement) {
	var numberCodes = [];
	if (!xyzIsNull(numberCodesStr)) {
		numberCodes = numberCodesStr.split(",");
	}
	xyzUpdatePersonInfo({
		flag : true,
		numberCodes : numberCodes,
		callbackFunction : function(numberCodes) {
			updateOrderPersonSubmit(clientCode, numberCodes,isImplement);
		}
	});
}

function updateOrderPersonSubmit(clientCode, numberCodes,isImplement) {
	if(isImplement==1){
		 	$.ajax({
				url : "../OrderContentWS/updateOrderContentForPerson.do",
				type : "POST",
				data : {
					clientCode : clientCode,
					personInfo : numberCodes.join(","),
					isImplement : isImplement
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.status == 1) {
						loadOrderContent();
						top.$.messager.alert("提示", "操作成功", "info");
						$("#dialogFormDiv_updateOrderPerson").dialog("destroy");
					} else {
						top.$.messager.alert("警告", data.msg, "warning");
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					top.window.AjaxError(XMLHttpRequest, textStatus,errorThrown);
				}
			});
	}else{
		$.ajax({
				url : "../OrderContentWS/updateOrderContentForPerson.do",
				type : "POST",
				data : {
					clientCode : clientCode,
					personInfo : numberCodes.join(","),
					isImplement : isImplement
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.status == 1) {
						top.$.messager.alert("提示", "操作成功!", "info");
						$("#dialogFormDiv_updateOrderPerson").dialog("destroy");
						loadOrderContent();
					} else {
						top.$.messager.alert("警告", data.msg, "warning");
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					top.window.AjaxError(XMLHttpRequest, textStatus,errorThrown);
				}
			});
	}
}
