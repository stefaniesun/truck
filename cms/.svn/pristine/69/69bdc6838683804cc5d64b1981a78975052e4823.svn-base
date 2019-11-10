$(document).ready(function(){
	xyzTextbox("mark");
	
	initTable();
	
	$("#areaQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = []; 
	
	xyzgrid({
		table : 'areaManagerTable',
		title : "航区列表",
		url:'../R_AreaWS/queryRAreaList.do',
		toolbar:toolbar,
		singleSelect : false, 
		idField : 'numberCode',
		columns : [[
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'航区编号',hidden:true,halign:'center'},
			{field:'mark',title:'航区代码',halign:'center'}
		]]
	});
}

function loadTable(){
	var mark = $("#mark").val();

	$("#areaManagerTable").datagrid('load', {
		mark : mark
	});
}