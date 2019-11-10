$(document).ready(function() {
	xyzTextbox("name");
	
	initTable();
	
	$("#userTagQueryButton").click(function(){
		loadTable();
	});
});


function initTable(){
	var toolbar = [];
	if(xyzControlButton("buttonCode_z20170122155402")){
		toolbar[toolbar.length] = {
			text : '新增',
			border : '1px solid #bbb',
			iconCls : 'icon-edit',
			handler : function() {
				addUserTagButton(); 
		    }
		};
		toolbar[toolbar.length] = '-';
	}
	
	if(xyzControlButton("buttonCode_z20170122155403")){
		toolbar[toolbar.length] = {
			text : '编辑',
			border : '1px solid #bbb',
			iconCls : 'icon-edit',
			handler : function() {
				editUserTagButton(); 
		    }
		};
		toolbar[toolbar.length] = '-';
	}
		
	xyzgrid({
		table : 'userTagManagerTable',
		title : "客户标签列表",
		url : '../UserTagWS/queryUserTagList.do',
		toolbar : toolbar,
		singleSelect : false,
		idField : 'numberCode',
		columns : [ [
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'标签编号',hidden:true},
			{field:'name',title:'客户名',
        	  formatter:function(value ,row ,index){
				return xyzGetDiv(value,0,150);
        	  }
			},
			{field:'remark',title:'备注',
				formatter:function(value ,row ,index){
	    		  return xyzGetDiv(value ,0 ,200);
				}
            },
		    {field:'addDate',title:'添加时',hidden:true,
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'alterDate',title:'修改时',width:85,
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			
		] ]
	});
}

function loadTable(){
	var name = $("#name").textbox("getValue");
	
	$("#userTagManagerTable").datagrid('load', {
		name :name
	});
}

function addUserTagButton(){
	
	xyzdialog({
		dialog : 'dialogFormDiv_addUserTagButton',
		title : '新增客户标签',
		href : '../jsp_custom/addUserTag.html',
		fit : false,
		width : 450,
	    height : 450,
		buttons : [ {
			text : '确定',
			handler : function() {
				addUserTagSubmit();
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_addUserTagButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			xyzTextbox("nameForm");
		}
	});
}

function addUserTagSubmit(numberCode){
	if(!$("form").form('validate')) {
		return false;
	}

	var name = $("#nameForm").textbox("getValue");
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../UserTagWS/addUserTag.do",
		type : "POST",
		data : {
			name : name,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#userTagManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_addUserTagButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editUserTagButton(){
	var userTags = $("#userTagManagerTable").datagrid("getChecked");
	if (userTags.length != 1) {
		top.$.messager.alert("提示", "请先选中单个对象!", "info");
		return false;
	}
	var row = userTags[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editUserTagButton',
		title : '编辑客户标签',
		href : '../jsp_custom/editUserTag.html',
		fit : false,
		width : 450,
	    height : 450,
		buttons : [ {
			text : '确定',
			handler : function() {
				editUserTagSubmit(row.numberCode);
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_editUserTagButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			xyzTextbox("nameForm");
			
			$("#nameForm").textbox("setValue", row.name);
			$("#remarkForm").val(row.remark);
		} 
	});
}

function editUserTagSubmit(numberCode){
	
	if(!$("form").form('validate')) {
		return false;
	}

	var name = $("#nameForm").textbox("getValue");
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../UserTagWS/editUserTag.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			name : name,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#userTagManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_editUserTagButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}