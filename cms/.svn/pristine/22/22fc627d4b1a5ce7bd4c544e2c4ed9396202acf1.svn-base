$(document).ready(function() {
	xyzCombobox({
		combobox:'area',
		url : '../ListWS/getAreaList.do',
		mode: 'remote'
	});
	xyzTextbox("nameCn");
	
	initTable();
	
	$("#airwayQueryButton").click(function(){
		loadTable();
	});
});

function initTable(){
	var toolbar = [];
	if (xyzControlButton("buttonCode_x20161207200202")) {
		toolbar[toolbar.length] = {
			text : '新增',
			border : '1px solid #bbb',
			iconCls : 'icon-add',
			handler : function() {
				addAirwayButton();
			}
		};
	}

	if (xyzControlButton("buttonCode_x20161207200203")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length] = {
			text : '编辑',
			border : '1px solid #bbb',
			iconCls : 'icon-edit',
			handler : function() {
				editAirwayButton(); 
			}
		};
	}

	if (xyzControlButton("buttonCode_x20161207200204")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length] = {
			text : '删除',
			border : '1px solid #bbb',
			iconCls : 'icon-remove',
			handler : function() {
				deleteAirwayButton();
			}
		};
	}

	xyzgrid({
		table : 'airwayManagerTable',
		title : "航线列表",
		url : '../AirwayWS/queryAirwayList.do',
		toolbar : toolbar,
		singleSelect : false,
		idField : 'numberCode',
		columns : [ [
			{field:'checkboxTemp',checkbox:true},
			{field:'area',title:'航区编号',hidden:true,halign:'center'},
			{field:'areaNameCn',title:'航区名称',halign:'center'},
			{field:'numberCode',title:'航线编号',hidden:true,halign:'center'},
			{field:'nameCn',title:'航线名称',width:300,halign:'center',sortable:true,order:'desc',
        	  formatter:function(value ,row ,index){
				return xyzGetDiv(value,0,300);
        	  }
			},
			{field:'days',title:'几天',align:'center'},
			{field:'nights',title:'几晚',hidden:true,align:'center'},
		    {field:'addDate',title:'添加时间',hidden:true,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'alterDate',title:'修改时间',width:85,halign:'center',sortable:true,order:'desc',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'remark',title:'备注',width:200,halign:'center',
				formatter:function(value ,row ,index){
	    		  return xyzGetDiv(value ,0 ,200);
				}
            }
		] ]
	});
}

function loadTable(){
	var area = $("#area").combobox("getValue");
	var nameCn = $("#nameCn").val();
	
	$("#airwayManagerTable").datagrid('load',{
		area : area,
		nameCn :nameCn
	});
}

function addAirwayButton(){
	xyzdialog({
		dialog : 'dialogFormDiv_addAirwayButton',
		title : '新增航线',
		href : '../jsp_base/addAirway.html',
		buttons : [ {
			text : '确定',
			handler : function() {
				addAirwaySubmit();
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_addAirwayButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			xyzCombobox({
				required : true,
				combobox : 'areaForm',
				url : '../ListWS/getAreaList.do',
				mode : 'remote',
				panelHeight : "auto",
				icons : [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			
			xyzTextbox("nameCnForm");
			$("#nameCnForm").textbox({
				required : true
			});
			
			$("#daysForm").numberbox({    
				required : true,
			    min : 1, 
			    icons : [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});  
			
			/*xyzTextbox("markForm");
			$("#markForm").textbox({
				required : true
			});*/
			
		}
	});
}

function addAirwaySubmit(){
	if(!$("form").form('validate')) {
		return false;
	}
	
	var area = $("#areaForm").combobox("getValue");
	var mark = "";   //$("#markForm").val();
	var days = $("#daysForm").numberbox("getValue");
	var nights = days-1;
	var nameCn = $("#nameCnForm").val();
	var remark = $("#remarkForm").val();

	$.ajax({
		url : "../AirwayWS/addAirway.do",
		type : "POST",
		data : {
			area : area,
			nameCn : nameCn,
			mark : mark,
			days : days,
			nights : nights,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#airwayManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_addAirwayButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editAirwayButton(){
	var airways = $("#airwayManagerTable").datagrid("getChecked");
	if (airways.length != 1) {
		top.$.messager.alert("提示", "请先选中单个对象!", "info");
		return false;
	}
	var row = airways[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editAirwayButton',
		title : '编辑航线【'+ row.nameCn +'】',
		href : '../jsp_base/editAirway.html',
		buttons : [ {
			text : '确定',
			handler : function() {
				editAirwaySubmit(row.numberCode);
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_editAirwayButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			
			xyzCombobox({
				required : true,
				combobox : 'areaForm',
				url : '../ListWS/getAreaList.do',
				mode : 'remote',
				panelHeight : 'auto',
				icons : [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			$("#areaForm").combobox({
				value : row.area
			});
			
			$("#daysForm").numberbox({    
				required : true,
			    min : 1, 
			    icons : [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});   
			$("#daysForm").numberbox({
				value : row.days
			});
			
			xyzTextbox("nameCnForm");
			$("#nameCnForm").textbox("setValue", row.nameCn);
			
			$("#remarkForm").val(row.remark);
			
			$("#nameCnForm").textbox({
				required : true
			});
			
			/*xyzTextbox("markForm");
			$("#markForm").textbox("setValue", row.mark);
			$("#markForm").textbox({
				required : true
			});*/
			
		}
	});
}

function editAirwaySubmit(numberCode){
	if(!$("form").form('validate')) {
		return false;
	}

	var area = $("#areaForm").combobox("getValue");
	var mark = ""; //$("#markForm").val();
	var days = $("#daysForm").numberbox("getValue");
	var nights = days-1;
	var remark = $("#remarkForm").val();
	var nameCn = $("#nameCnForm").val();
	
	$.ajax({
		url : "../AirwayWS/editAirway.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			area : area,
			nameCn : nameCn,
			mark : mark,
			days : days,
			nights : nights,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#airwayManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_editAirwayButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deleteAirwayButton(){
	var numberCodes = $.map($("#airwayManagerTable").datagrid("getChecked"),
		function(p) {
			return p.numberCode;
		}
	).join(",");

	if (xyzIsNull(numberCodes)) {
		top.$.messager.alert("提示", "请先选中需要删除的对象!", "info");
		return;
	}
	
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		if(r){
			$.ajax({
				url : "../AirwayWS/deleteAirway.do",
				type : "POST",
				data : {
					numberCodes : numberCodes
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.status == 1) {
						$("#airwayManagerTable").datagrid("reload");
						top.$.messager.alert("提示", "操作成功!", "info");
					} else {
						top.$.messager.alert("警告", data.msg, "warning");
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					top.window.AjaxError(XMLHttpRequest, textStatus,
							errorThrown);
				}
			});
		}
	});
}