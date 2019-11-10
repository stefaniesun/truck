$(document).ready(function(){
	xyzCombobox({
		combobox : 'cruiseMark',
		url : '../ListWS/getRoyalCruiseList.do',
		mode : 'remote',
		lazy : false,
		icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	//xyzTextbox("cruiseMark");
	xyzTextbox("mark");
	xyzTextbox("airwayMark");
	xyzTextbox("areaMark");
	
	initTable();
	
	$("#shipmentQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = [];
	
	xyzgrid({
		table : 'shipmentManagerTable',
		title : "航期列表",
		url : '../R_ShipmentWS/queryRShipmentList.do',
		toolbar : toolbar,
		singleSelect : false,
		idField : 'numberCode',
		columns : [ [
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'航期编号',halign:'center',
				formatter : function(value, row, index) {
					return xyzGetDiv(value, 0, 8);
				}
			},
			{field:'mark',title:'航期代码',halign:'center',
				formatter : function(value, row, index) {
					return xyzGetDiv(value, 0, 10);
				}
		    },
			{field:'travelDate',title:'出发日期',width:80,align:'center',
				formatter : function(value, row, index) {
					return value.substring(0, 10);
				}
			},
			{field:'cruise',title:'邮轮编号',hidden:true,halign:'center'},
			{field:'cruiseMark',title:'邮轮代码',halign:'center'},
			{field:'cruiseNameCn',title:'邮轮名称',width:100,halign:'center',
        	  formatter:function(value ,row ,index){
					return xyzGetDiv(value, 0, 50);
				}
			},
			{field:'area',title:'航区编号',hidden:true,halign:'center'},
			{field:'areaMark',title:'航区代码',halign:'center',
	        	  formatter:function(value ,row ,index){
					return xyzGetDiv(value, 0, 6);
				  }
				},
			{field:'areaNameCn',title:'航区名称',halign:'center',
        	  formatter:function(value ,row ,index){
				return xyzGetDiv(value, 0, 15);
			  }
			},
			{field:'airway',title:'航线编号',hidden:true,align:'center'},
			{field:'airwayMark',title:'航线代码',halign:'center'},
			{field:'airwayNameCn',title:'航线名称',width:100,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value, 0, 50);
				}
			},
			{field:'tripDays',title:'航程天数',align:'center'},
			{field:'portMark',title:'出发港口代码',halign:'center'},
			{field:'portNameCn',title:'出发港口',halign:'center',
        	  formatter:function(value ,row ,index){
				return xyzGetDiv(value, 0, 40);
			  }
			},
			{field:'voyage',title:'操作',align:'center',
				formatter: function(value,row,index){
					var btnTemp = "";
					if(xyzControlButton("buttonCode_x20170727093502")){
						btnTemp += " "+xyzGetA("查看行程路线信息","voyageManager",[row.numberCode,row.travelDate,row.airwayMark],"点我管理行程路线","blue");
					}
					return btnTemp;
				}
			},
			{field:'shipDesc',title:'航期描述',width:100,align:'center',
		    	formatter:function(value ,row ,index){
		    		return xyzGetDiv(value ,0 ,100);
		    	}
		    }
		] ]
	});
}

function loadTable(){
	var cruiseMark = $("#cruiseMark").combobox("getValue");
	var mark = $("#mark").val();
	var airwayMark = $("#airwayMark").val();
	var areaMark = $("#areaMark").val();
	
	$("#shipmentManagerTable").datagrid('load',{
		cruiseMark : cruiseMark,
		mark : mark,
		airwayMark : airwayMark,
		areaMark : areaMark
	});
}

function voyageManager(numberCode,travelDate,airwayMark){
	xyzdialog({
		dialog : 'dialogFormDiv_voyageManager',
		title : "行程路线管理【出发日期:"+travelDate.substring(0,10)+"】",
	    content : "<iframe id='dialogFormDiv_voyageManagerIframe' frameborder='0'></iframe>",
	    buttons:[{
			text:'返回',
			handler:function(){
				$("#shipmentManagerTable").datagrid("reload");
				$("#dialogFormDiv_voyageManager").dialog("destroy");
			}
		}]
	});
	var tempWidth = $("#dialogFormDiv_voyageManager").css("width");
	var tempHeight = $("#dialogFormDiv_voyageManager").css("height");
	var tempWidth2 = parseInt(tempWidth.split("px")[0]);
	var tempHeight2 = parseInt(tempHeight.split("px")[0]);
	$("#dialogFormDiv_voyageManagerIframe").css("width",(tempWidth2)+"px");
	$("#dialogFormDiv_voyageManagerIframe").css("height",(tempHeight2)+"px");
	$("#dialogFormDiv_voyageManagerIframe").attr("src","../jsp_rbase/rVoyageManager.html?shipment="+numberCode+"&airwayMark="+airwayMark);
}