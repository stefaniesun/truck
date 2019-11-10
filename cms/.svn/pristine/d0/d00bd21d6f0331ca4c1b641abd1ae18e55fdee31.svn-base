$(document).ready(function() {
	initTable();
	
	xyzTextbox("tid");
	xyzTextbox("nickName");
	
	$("#taobaoQueryButton").click(function() {
		loadTable();
	});
});

function initTable(){
	var toolbar = [];

	xyzgrid({
		table : 'taobaoPoolManagerTable',
		title : "淘宝池",
		url : '../TaobaoPoolWS/queryTaobaoPool.do',
		toolbar : toolbar,
		singleSelect : true,
		pageSize : 10,
		idField : 'numberCode',
		columns : [ [
			{field:'checkboxTemp',checkbox:true,hidden:true},
			{field:'tid',title:'订单编号',align:'center',
				formatter:function(value, row, index) {
					return xyzGetA(value,"oidManager",[value],"点我查看订单详情!","blue");
				}
			},
			{field:'sellerNick',title:'店铺名称',align:'center',
				formatter:function(value, row, index) {
					return xyzGetDiv(value ,0 ,200);
				}
			},
			{field:'receivedPayment',title:'卖家收款',width:70,halign:'center'},
			{field:'status',title:'状态',halign:'center',
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
			{field:'buyNick',title:'买家昵称',halign:'center',
				formatter:function(value, row, index) {
					return xyzGetDiv(value ,0 ,100);
				}
			},
			{field:'travelContactName',title:'联系人姓名',halign:'center',
				formatter:function(value, row, index) {
					return xyzGetDiv(value ,0 ,100);
				}
			},
			{field:'travelContactMobile',title:'联系人电话',halign:'center',
				formatter:function(value, row, index) {
					return xyzGetDiv(value ,0 ,100);
				}
			},
			{field:'travelContactMail',title:'联系人邮箱',halign:'center',
				formatter:function(value, row, index) {
					return xyzGetDiv(value ,0 ,100);
				}
			},
			{field:'tripStartDate',title:'出行日期',halign:'center',
				formatter:function(value, row, index) {
					return xyzGetDiv(value ,0 ,11);
				}
			},
			{field:'buyerMessage',title:'买家信息',width:200,halign:'center',
				formatter:function(value, row, index) {
					return xyzGetDiv(value ,0 ,200);
				}
			},
			{field:'addDate',title:'添加时间',hidden:true,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'alterDate',title:'修改时间',sortable:true,order:'desc',halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field : 'remark',title : '备注',width:120,halign:'center',
				formatter:function(value, row, index) {
					return xyzGetDiv(value ,0 ,100);
				}
			}
			
		] ]
	});
}

function loadTable(){
	var tid = $("#tid").val();
	var nickName = $("#nickName").val();
	$("#taobaoPoolManagerTable").datagrid('load', {
		tid : tid,
		nickName : nickName
	});
}

function oidManager(tid){
	
	xyzdialog({
        dialog:'dialogFormDiv_managerTaobaoOrderO',
        title:"订单详情【订单号" + tid + "】",
        content:"<iframe id='dialogFormDiv_managerTaobaoOrderOIframe' frameborder='0' name='"
                + tid + "'></iframe>",
        buttons:[{
            text:'返回',
            handler:function() {
                $("#dialogFormDiv_managerTaobaoOrderO").dialog("destroy");
                $("#taobaoPoolManagerTable").datagrid("reload");
            }
        }]
    });
    var tempWidth = $("#dialogFormDiv_managerTaobaoOrderO").css("width");
    var tempHeight = $("#dialogFormDiv_managerTaobaoOrderO").css("height");
    var tempWidth2 = parseInt(tempWidth.split("px")[0]);
    var tempHeight2 = parseInt(tempHeight.split("px")[0]);
    $("#dialogFormDiv_managerTaobaoOrderOIframe").css("width",(tempWidth2 - 20) + "px");
    $("#dialogFormDiv_managerTaobaoOrderOIframe").css("height",(tempHeight2 - 50) + "px");
    $("#dialogFormDiv_managerTaobaoOrderOIframe").attr("src","../jsp_taobao/taobaoOrderDetail.html?tid="+tid);
	
}
