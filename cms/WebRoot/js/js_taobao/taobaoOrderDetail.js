$(document).ready(function() {

	winWidth = window.innerWidth;

	var parmaeters = getUrlParameters();
	tid = parmaeters.tid;
	
	$("#loadOrderCoreQueryButton").click(function() {
		loadOrderContent();
	});
			
	initOrderContent();
	
	$("#orderContentForLinkmanButton").click(function(){
		updateOrderContentForLinkman();
	});
});

function initOrderContent() {
	var toolbar = [];
	if (xyzControlButton("buttonCode_y20161128164003")) {
		toolbar[toolbar.length] = {
			text : '下单',
			iconCls : 'icon-edit',
			handler : function() {
				addOrderContentByTaobaoButton();
			}
		};
	}
	
	xyzgrid({
		table : 'oidTable',
		singleSelect : false,
		idField : 'tid',
		toolbar : toolbar,
		title : "子订单",
		height : 'auto',
		columns : [[
			{field : 'checkboxTemp',checkbox : true},
			{field : 'title',title : '宝贝标题',halign:'center',
				formatter:function(value, row, index) {
					return xyzGetDiv(value ,0 ,200);
				}
			},
			{field : 'numIid',title : '宝贝ID',halign:'center',
				formatter:function(value ,row ,index){
					var html = xyzGetA(value,"checkItem",[value],"点击查看详情","blue");
					return html;
				}
			},
			{field : 'oid',title : '子订单编号',width:100,halign:'center',
				formatter:function(value, row, index) {
					return xyzGetDiv(value ,0 ,200);
				}
			},
			{field : 'buyNick',title : '买家昵称',width:100,halign:'center',
				formatter:function(value, row, index) {
					return xyzGetDiv(value ,0 ,200);
				}
			},
			{field : 'skuPropertiesName',title : 'SKU名称',width:100,halign:'center',
				formatter:function(value, row, index) {
					return xyzGetDiv(value ,0 ,200);
				}
			},
			{field : 'outSkuId',title : '单品编号',width:100,hidden:true,halign:'center',
				formatter:function(value, row, index) {
					return xyzGetDiv(value ,0 ,200);
				}
			},
			{field : 'price',title : '价格',width:100,halign:'center'},
			{field : 'payment',title : '买家付款',width:100,halign:'center'},
			{field : 'num',title : '数量',width:100,halign:'center'},
			{field : 'status',title : '状态',width:100,halign:'center',
				formatter:function(value, row, index) {
					var html = "";
					if (value == "WAIT_BUYER_PAY") {
						html = xyzGetDiv("等待买家付款",0,200);
					}else if (value == "WAIT_BUYER_CONFIRM_GOODS") {
						html = xyzGetDiv("卖家已发货",0,200);
					}else if (value == "WAIT_SELLER_SEND_GOODS") {
						html = xyzGetDiv("买家已付款",0,200);
					}else if (value == "TRADE_FINISHED") {
						html = xyzGetDiv("交易成功",0,200);
					}else if (value == "TRADE_CLOSED") {
						html = xyzGetDiv("交易关闭",0,200);
					}else if (value == "TRADE_CLOSED_BY_TAOBAO") {
						html = xyzGetDiv("交易被淘宝关闭",0,200);
					}else if (value == "TRADE_NO_CREATE_PAY") {
						html = xyzGetDiv("没有创建外部交易(支付宝交易)",0,200);
					}else if (value == "OTHER") {
						html = xyzGetDiv("其他状态",0,200);
					}
                    return html;
                }
			},
			{field : 'refundStatus',title : '退款状态',width:100,halign:'center',
				formatter:function(value, row, index) {
					var html = "";
					if (value == "WAIT_SELLER_AGREE") {
						html = xyzGetDiv("买家已经申请退款,等待卖家同意",0,200);
					}else if (value == "WAIT_BUYER_RETURN_GOODS") {
						html = xyzGetDiv("卖家已经同意退款,等待 买家退货",0,200);
					}else if (value == "WAIT_SELLER_CONFIRM_GOODS") {
						html = xyzGetDiv("买家已经退货,等待卖家确认收货",0,200);
					}else if (value == "CLOSED") {
						html = xyzGetDiv("退款关闭",0,200);
					}else if (value == "SUCCESS") {
						html = xyzGetDiv("退 款成功",0,200);
					}else if (value == "SELLER_REFUSE_BUYER") {
						html = xyzGetDiv("卖家拒绝退款",0,200);
					}else if (value == "NO_REFUND") {
						html = xyzGetDiv("没有退款",0,200);
					}
                    return html;
                }
			},
			{field : 'remark',title : '备注',width:100,halign:'center',
				formatter:function(value, row, index) {
					return xyzGetDiv(value ,0 ,200);
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
			{field : 'username',title : '操作人',width : 60,halign:'center'},
			{field : 'value',title : '记录标识',width : 110,halign:'center'},
			{field : 'addDate',title : '添加时间',width : 120,halign:'center'},
			{field : 'remark',title : '备注',width : 700,halign:'center',
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
		url : "../TaobaoPoolWS/queryTaobaoOrderDetail.do",
		type : "POST",
		data : {
			tid : tid
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				var tempOrderContent = data.content.OrderContents;
				
				var  orderContent= {
					status : 1, 
					content : {total : tempOrderContent.length,rows : tempOrderContent}
				};

				$("#oidTable").datagrid({data : orderContent});
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

function checkItem(value){
	window.open("https://items.alitrip.com/item.htm?id="+value);
}

function addOrderContentByTaobaoButton(){
	
	var data = $('#oidTable').datagrid('getData').rows;   
	
	var tid = data[0].tid;
	
	xyzdialog({
		dialog : 'dialogFormDiv_addTaoaboOrderContentButton',
		title : '淘宝下单',
		href : '../jsp_taobao/addOrderContentByTaobao.html',
		fit : true,
		width : 500,
		height : 550,
		buttons:[{
			text:'确定',
			handler:function() {
				addOrderContentByTaobaoSubmit(tid);
			}
		}, {
			text:'取消',
			handler:function() {
				$("#dialogFormDiv_addTaoaboOrderContentButton").dialog("destroy");
			}
		}],
		onLoad:function(){
			
			xyzCombobox({
				required:false,
				combobox:'shipmentForm',
				url:'../ListWS/getShipmentList.do',
				mode: 'remote',
				onChange : function(newValue, oldValue){
					
					$('input[id^="ptviewForm_"]').combobox('clear');   
					$('input[id^="ptviewForm_"]').combobox('reload');    
					
				},
				lazy:false
			});
			
			xyzCombobox({
				required:true,
				combobox:'tkviewForm_0',
				url:'../ListWS/getTkviewList.do',
				mode: 'remote',
				onBeforeLoad: function(param){
					var shipment = $("#shipmentForm").combobox('getValue');
					param.shipment = shipment; 
				},
				onSelect: function(record){
					getStock(record.value,0);
				},
				lazy:true
			});
			
			xyzCombobox({
				required:true,
				combobox:'distributor',
				url:'../ListWS/getDistributorNameAndPhoneList.do',
				mode: 'remote',
				onSelect: function(record){
					$("#linkMan").textbox('setValue',record.text);
					$("#linkPhone").textbox('setValue',record.value);
				},
				lazy:false
			});
			
			var payment = 0;
			
			for ( var i = 0; i < data.length; i++) {
				
				var html = '<tr>';
					html += '<td>';
					html += '<input type="hidden" class="oidValue" value="'+data[i].oid+'" id="oid_'+i+'"/>';
					html += '</td>';
					html += '<td colspan="10" class="oid" id="oid_'+i+'">【'+data[i].oid+'】</td>';
					html += '</tr>';
					html += '<tr><td></td><td colspan="10" class="sku_info" id="sku_'+i+'">';
					html += data[i].skuPropertiesName+'数量【'+data[i].num+'】付款【'+data[i].payment+'】';
					html += '</td></tr>';
				
				$("#linkPhoneTr").after(html);
					
				payment += parseInt(data[i].payment);
			}
			
			$("#payment").numberbox('setValue',payment);
			
			for ( var i = 0; i < data.length; i++) {
				$("#count_0").numberbox({
					required:true,
					min:data[i].num,
					precision:0,
					icons: [{
						iconCls:'icon-clear',
						handler: function(e){
							$(e.data.target).numberbox('clear');
						}
					}]
				});
				$("#count_0").numberbox('setValue',data[i].num);
			}
			
			$("#earnest").numberbox({
				required:true,
				min:0,
				precision:2,
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});
			$("#linkMan").textbox({
				required: true,
				validType:['length[0,50]'],
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).textbox('clear');
					}
				}]
			});
			$("#linkPhone").textbox({
				required: true,
				validType:['phone','length[8,11]'],
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).textbox('clear');
					}
				}]
			});
			$("#endDateForm").datetimebox({
				editable:false,
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).datetimebox('clear');
					}
				}]
			});
		}
	});
}

function getStock(tkview,index){
	
	if(xyzIsNull(tkview)){
		$("#stock_"+index).html(0);
		return false;
	}
	
	$.ajax({
		url:"../TaobaoPoolWS/getStockByTkview.do",
		type:"POST",
		data:{
			tkview : tkview
		},
		async:false,
		dataType:"json",
		success:function(data) {
			if(data.status==1){
				$("#stock_"+index).html(xyzIsNull(data.content)==true?0:data.content);
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

function addTkview(){
	
	var maxLength = (Math.random() * 1000000).toFixed(0);
	html = " <tr id='tkviewTr_"+maxLength+"'>";
	html += "<td> </td>";
	html += " <td><input id='tkviewForm_"+maxLength+"' class='tkviewClass' style='width:300px;'/></td>";
	html += " <td><span id='stock_"+maxLength+"' style='text-align:left;'>0</span></td>";
	html += " <td><input id='count_"+maxLength+"' class='orderNum' type='text' style='width:100px;'></td>";
	html += " <td><img onclick='deleteTkview("+maxLength+")' src='../image/other/deletePage.png' alt='减一项' style='cursor: pointer;' /></td>";
	html += " </tr>";
	
	var tempIndex = $($(".tkviewClass")[$(".tkviewClass").length - 1]).attr('id').split('_')[1];
	$("#tkviewTr_"+tempIndex).after(html);
	
	xyzCombobox({
		required:true,
		combobox:'tkviewForm_'+maxLength,
		url:'../ListWS/getTkviewList.do',
		mode: 'remote',
		onBeforeLoad: function(param){
			var shipment = $("#shipmentForm").combobox('getValue');
			param.shipment = shipment; 
		},
		onSelect: function(record){
			getStock(record.value,maxLength);
		},
		lazy:true
	});
	
	$("#count_"+maxLength).numberbox({
		value:1,
		min:1,
		precision:0
	});
	
	$("#count_"+maxLength).numberbox({
		required:true,
		min:1,
		precision:0
	});
	
}

function deleteTkview(index){
	if($(".tkviewClass").length <= 1){
		top.$.messager.alert("警告", '最少保留一个单品!', "warning");
		return;
	}
	$("#tkviewTr_"+index).remove();
}

function addOrderContentByTaobaoSubmit(tid){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var tkviewJson = [];
	var tempTkviewIndex = $(".tkviewClass").length;
	var tempOidIndex = $(".oid").length;
	
	var tempLength = tempTkviewIndex <= tempOidIndex ? tempOidIndex : tempTkviewIndex;
	
	for ( var i = 0; i < tempLength; i++) {
		var tempIndex = xyzIsNull($(".tkviewClass").eq(i).prop('id'))==true?$(".tkviewClass").eq(i-1).prop('id').split('_')[1]:$(".tkviewClass").eq(i).prop('id').split('_')[1];
		
		var tempTkview = $("#tkviewForm_"+tempIndex).combobox('getValue');
		var tempCount = $("#count_"+tempIndex).val();
		
		var tempOid = "";
		if (i <= tempOidIndex) {
			tempOid = xyzIsNull($(".oidValue").eq(i).val())==$(".oidValue").eq(i-1).val()?"":$(".oidValue").eq(i).val();
		}
		
		var temp = {
			tempIndex : tempIndex,
			tkview : tempTkview,
            count : tempCount,
            oid : tempOid
		};
		tkviewJson[tkviewJson.length] = temp;
	}
	
	var tkviewJsonStr = JSON.stringify(tkviewJson);
	var payment = $("#payment").numberbox('getValue');
	var linkMan = $("#linkMan").textbox('getValue');
	var linkPhone = $("#linkPhone").textbox('getValue');
	var endDate = $("#endDateForm").datetimebox("getValue");
	var remark = $("#remark").val();
	
	$.ajax({
		url:"../TaobaoPoolWS/addOrderContentByTaobao.do",
		type:"POST",
		data:{
			tkviewJsonStr:tkviewJsonStr,
			payment:payment,
	        linkMan:linkMan,
	        linkPhone:linkPhone, 
	        endDate : endDate,
	        remark:remark,
	        tid : tid
		},
		async:false,
		dataType:"json",
		success:function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功!","info");
				loadOrderContent();
				$("#dialogFormDiv_addTaoaboOrderContentButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}