$(document).ready(function() {
	xyzTextbox("queryNameCn");
	xyzTextbox("queryMark");
	$("#cabinSpan").hide();
	xyzCombobox({
		combobox : 'cruiseMark',
		url : '../ListWS/getRoyalCruiseList.do',
		mode : 'remote',
		lazy : false,
		onChange : function(newValue, oldValue){
			if(!xyzIsNull(newValue)){
				$("#cabinSpan").show();
				if(newValue != oldValue){
					$("#cabinMark").combobox("clear");
					$("#mouth").combobox("clear");
					$("#shipment").combobox("clear");
				}
			}else{
				$("#cabinMark").combobox("clear");
				$("#mouth").combobox("clear");
				$("#shipment").combobox("clear");
				$("#cabinSpan").hide();
			}
		},
		icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
				$("#cabinMark").combobox("clear");
				$("#mouth").combobox("clear");
				$("#shipment").combobox("clear");
				$("#cabinSpan").hide();
			}
		}]
	});
	
	xyzCombobox({
		combobox : 'cabinMark',
		url : '../ListWS/getCabinListByCruise.do',
		mode : 'remote',
		lazy : false, 
		onBeforeLoad: function(param){
			param.cruise = $("#cruiseMark").combobox("getValue");
	   	},
	   	icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	xyzCombobox({
		combobox : 'mouth',
		url : '../ListWS/getRoyalShipmentMouthList.do',
		mode : 'remote',
		multiple : true,
		lazy : false, 
		onBeforeLoad: function(param){
			param.cruise = $("#cruiseMark").combobox("getValue");
	   	},
	   	icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	xyzCombobox({
		combobox : 'shipment',
		url : '../ListWS/getRoyalShipmentList.do',
		mode : 'remote',
		multiple : true,
		lazy : false, 
		onBeforeLoad: function(param){
			param.cruise = $("#cruiseMark").combobox("getValue");
			param.mouth = $("#mouth").combobox("getValue");
			param.isTkview = 0;
	   	},
	   	icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	
	initTable();
	
	/*读取txt文件*/
	$("#readTxtButton").click(function(){
		readTxtButton();
	});
	
	$("#tkviewQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbarTkview = [];
	
	xyzgrid({
		table : 'tkivewManagerTable',
		title : "单品列表",
		toolbar : toolbarTkview,
		url : '../R_TkviewWS/queryRTkviewList.do',
		singleSelect : true, 
		selectOnCheck : true,
		height : 316,
		pageSize : 5,
		idField : 'numberCode',
		queryParams : {},
		onSelect : function(rowIndex, rowData){
			tkviewNumber = rowData.numberCode;
			$("#stockManagerTable").datagrid('load',{
				tkview : tkviewNumber
			});
			
			top.$('#remarkGreatDiv').accordion("select","通用备注");
			if(rowData.remark!=undefined){
				top.$("#remarkTop").text(rowData.remark);
			}
		},
		columns:[[
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'单品编号',hidden:true,align:'center'},
			{field:'nameCn',title:'单品名称',halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value ,0 ,100);
				}
			},
			{field:'mark',title:'单品代码',align:'center',},
			{field:'cruise',title:'邮轮编号',hidden:true,halign:'center'},
			{field:'cruiseMark',title:'邮轮代码',align:'center',},
			{field:'cruiseNameCn',title:'邮轮名称',width:100,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value ,0 ,100);
				}
			},
			{field:'shipment',title:'航期编号',align:'center'},
			{field:'shipmentMark',title:'航期代码',halign:'center'},
			{field:'shipmentTravelDate',title:'出发日期',align:'center',
				formatter:function(value ,row ,index){
					return value.substring(0, 10);
				}
			},
			{field:'cabin',title:'舱型编号',width:60,hidden:true,halign:'center'},
			{field:'cabinMark',title:'舱型代码',align:'center',},
			{field:'volume',title:'舱型容量',align:'center',},
			{field:'cabinNameCn',title:'舱型名称',width:100,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value,0,100);
				}
			}
		]]
	});
	
	var toolbarStock = [];
	xyzgrid({
		table : 'stockManagerTable',
		title : "库存列表 ",
		toolbar : toolbarStock,
		url : '../R_StockWS/queryRStockList.do',
		singleSelect : true,
		height : 290,
		pageSize : 5,
		idField : 'numberCode',
		columns:[[
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'编号',hidden:true,align:'center'},
			{field:'providerNameCn',title:'供应商',halign:'center',
				formatter : function(value, row, index) {
					return xyzGetDiv(value, 0, 10);
				}
			},
			{field:'priceMark',title:'价格代码',align:'center'},
			{field:'priceDesc',title:'价格描述',halign:'center',
				formatter : function(value, row, index) {
					return xyzGetDiv(value, 0, 30);
				}
			},
			{field:'priceEffDate',title:'价格生效日',align:'center'},
			{field:'priceEndDate',title:'价格到期日',align:'center'},
			{field:'singleRoom',title:'单人间均价',align:'center',
		    	formatter : function(value, row, index) {
					if(value < 0){
						var html = "<span style='color:red;'>"+value+"</span>";
						return html;
					}
					return value;
				}
			},
			{field:'doubleRoom',title:'双人间均价',align:'center',
				formatter : function(value, row, index) {
					if(value < 0){
						var html = "<span style='color:red;'>"+value+"</span>";
						return html;
					}
					return value;
				}
		    },
			{field:'tripleRoom',title:'三人间均价',align:'center',
		    	formatter : function(value, row, index) {
					if(value < 0){
						var html = "<span style='color:red;'>"+value+"</span>";
						return html;
					}
					return value;
				}
		    },
			{field:'quadRoom',title:'四人间均价',halign:'center',
		    	formatter : function(value, row, index) {
					if(value < 0){
						var html = "<span style='color:red;'>"+value+"</span>";
						return html;
					}
					return value;
				}
			}
		]]
	});

}

function loadTable(){
	var cruiseMark = $("#cruiseMark").combobox("getValue");
	var cabinMark = $("#cabinMark").combobox("getValue");
	var nameCn = $("#queryNameCn").val();
	var mark = $("#queryMark").val();
	var mouth = $("#mouth").combobox("getValue");
	var shipment = $("#shipment").combobox("getValue");

	$("#tkivewManagerTable").datagrid('load', {
		cruiseMark : cruiseMark,
		cabinMark : cabinMark,
		nameCn : nameCn,
		mark : mark,
		mouth : mouth,
		shipment : shipment
	});
}

function readTxtButton(){
	
	$.ajax({
		url : "../R_TkviewWS/readTxtOper.do",
		type : "POST",
		data : {},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status == 1){
				top.$.messager.alert("提示",data.content,"info");
				$("#tkivewManagerTable").datagrid("reload");
				$("#stockManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}