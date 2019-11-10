$(document).ready(function(){
	
	xyzTextbox("nameCn");
	xyzTextbox("mark");
	xyzTextbox("startPlace");
	xyzTextbox("port");
	
	xyzCombobox({
		combobox:'cruise',
		mode: 'remote',
		url:'../ListWS/getCruiseList.do',
		icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	
	
	initTable();
	
	$("#shipmentQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = [];
	
	xyzgrid({
		table : 'shipmentTable',
		url : '../QueryShipmentWS/queryShipmentList.do',		
		toolbar : toolbar,
		title : '航期列表',
		singleSelect : false,
		idField : 'numberCode',
		columns : [ [
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'航期编号',width:80,halign:'center',
				formatter : function(value, row, index) {
					return xyzGetDiv(value, 0, 20);
				}
			},
			{field:'mark',title:'代码',width:80,align:'center'},
			{field:'travelDate',title:'出发日期',width:80,align:'center',sortable:true,order:'desc',
				formatter : function(value, row, index) {
					return value.substring(0, 10);
				}
			},
			{field:'travelEndDate',title:'结束日期',width:80,align:'center',
				formatter : function(value, row, index) {
					return value.substring(0, 10);
				}
			},
			{field:'finalSaleDate',title:'最后售卖日期',width:90,align:'center',
				formatter : function(value, row, index) {
					return xyzGetDivDate(value);
				}
			},
			{field:'cruise',title:'邮轮编号',hidden:true,halign:'center'},
			{field:'cruiseNameCn',title:'邮轮',width:120,halign:'center',
        	  formatter:function(value, row, index){
					return xyzGetDiv(value, 0, 50);
				}
			},
			{field:'area',title:'航区编号',hidden:true,halign:'center'},
			{field:'areaNameCn',title:'航区',width:120,halign:'center',
        	  formatter:function(value, row, index){
				return xyzGetDiv(value, 0, 40);
			  }
			},
			{field:'startPlace',title:'出发地',halign:'center',
        	  formatter:function(value ,row ,index){
				return xyzGetDiv(value, 0, 4);
			  }
			},
			{field:'tripDays',title:'航程天数',align:'center'},
			{field:'airway',title:'航线编号',hidden:true,align:'center'},
			{field:'airwayNameCn',title:'航线',width:220,halign:'center',
				formatter:function(value, row, index){
					return xyzGetDiv(value, 0, 50);
				}
			},
			{field:'remark',title:'备注',width:100,align:'center',
		    	formatter:function(value, row, index){
		    		return xyzGetDiv(value, 0, 40);
		    	}
		    }
		] ]
	});
}

function loadTable(){
	var cruise = $("#cruise").combobox("getValue");
	var startDate = $("#startDate").datebox("getValue");
	var endDate = $("#endDate").datebox("getValue");
	var mark = $("#mark").val();
	var startPlace = $("#startPlace").val();
	var port = $("#port").val();
	
	$("#shipmentTable").datagrid('load',{
		startDate : startDate,
		endDate : endDate,
		startPlace : startPlace,
		port : port,
		mark : mark,
		cruise : cruise
	});
	
}