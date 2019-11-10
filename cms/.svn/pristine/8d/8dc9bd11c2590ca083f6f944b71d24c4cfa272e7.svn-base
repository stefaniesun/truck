$(document).ready(function() {
	
	initTable();
	
	$("#manualButton").click(function(){
		manualButton();
	});
	
});

function initTable(){
	var toolbar = [];

	xyzgrid({
		table : 'rTaskManagerTable',
		title : "列表",
		url : '../R_TaskWS/queryTaskList.do',
		toolbar : toolbar,
		idField : 'numberCode',
		pageSize : 10,
		height : 600,
		queryParams:{},
		columns : [ [
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'编号',hidden:true,halign:'center'},
			{field:'startTime',title:'开始时间',halign:'center'},
			{field:'endTime',title:'结束时间',halign:'center'},
			{field:'auto',title:'下行方式',halign:'center',
				formatter:function(value ,row ,index){
					//1:自动下行  0:手动下行
					if(value == 1){
						return "自动下行 ";
					}
					return "手动下行";
				}
			},
			{field:'status',title:'状态',halign:'center',
				formatter:function(value ,row ,index){
					if(value == 1){
						return "同步完成 ";
					}
					return "正在后台同步更新";
				}
			},
			{field:'remark',title:'备注',hidden:true,halign:'center'}
		]]
	});
	
}

function manualButton(){
	$.ajax({
		url : '../R_TaskWS/manualUpdateDataOper.do',
		type : 'POST',
		dataType : 'json',
		async : false,
		success : function(data) {
			if (data.status == 1) {
				top.$.messager.alert("提示","同步更新程序开始执行，大概需要5分钟!","info");
				$("#rTaskManagerTable").datagrid("reload");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}