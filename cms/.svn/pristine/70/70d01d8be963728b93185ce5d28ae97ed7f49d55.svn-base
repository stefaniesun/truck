$(document).ready(function() {
	
	xyzCombobox({
		combobox:'cruise',
		url : '../ListWS/getCruiseList.do',
		mode: 'remote',
		lazy : false
	});
	
	xyzCombobox({
		combobox:'cabin',
		url : '../ListWS/getCabinByCruiseList.do',
		mode: 'remote',
		lazy : false,
		onBeforeLoad: function(param){
			param.cruise = $("#cruise").combobox("getValue");
		}

	});
	
	initTable();
	
	$("#tkviewTypeQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = [];
	
	xyzgrid({
		table : 'tkviewTypeManagerTable',
		title : "单品种类列表",
		url : '../TkviewTypeWS/queryTkviewTypeList.do',
		toolbar : toolbar ,
		singleSelect : false , 
		idField : 'numberCode',
		columns : [[
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'编号',hidden:true},
			{field:'cruise',title:'邮轮编号',hidden:true},
			{field:'cruiseNameCn',title:'邮轮名称',align:'center',
			    formatter:function(value ,row ,index){
				   return xyzGetDiv(value,0,100);
			    }
		    },
		    {field:'cabin',title:'舱型编号',hidden:true},
			{field:'cabinNameCn',title:'舱型名称',align:'center',
			    formatter:function(value ,row ,index){
				   return xyzGetDiv(value,0,100);
			    }
		    },
		    {field:'addDate',title:'添加时间',hidden:true,align:'center',
				formatter:function(value ,row ,index){
				   return xyzGetDivDate(value);
				}
			},
			{field:'alterDate',title:'修改时间',align:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'remark',title:'备注',width:130,align:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value ,0 ,130);
				}
			}
		]]
	});
	
}

function loadTable(){
	var cruise = $("#cruise").combobox("getValue");
	var cabin = $("#cabin").combobox("getValue");

	$("#tkviewTypeManagerTable").datagrid('load', {
		cruise : cruise,
		cabin : cabin
	});
}