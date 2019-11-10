$(document).ready(function() {
	xyzTextbox("nickName");
	
	initTable();
	
	$("#customerQueryButton").click(function(){
		loadTable();
	});
});

function initTable(){
	var toolbar = [];
	if(xyzControlButton("buttonCode_x20170122111802")){
		toolbar[toolbar.length] = {
			text : '编辑',
			border : '1px solid #bbb',
			iconCls : 'icon-edit',
			handler : function() {
				editCustomerButton(); 
		    }
		};
	}
	toolbar[toolbar.length] = '-';
		
	xyzgrid({
		table : 'customerManagerTable',
		title : "客户列表",
		url : '../CustomerWS/queryCustomerList.do',
		toolbar : toolbar,
		singleSelect : false,
		idField : 'numberCode',
		columns : [ [
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'客户编号',hidden:true},
			{field:'phone',title:'电话',width:150,
        	  formatter:function(value ,row ,index){
				return xyzGetDiv(value,0,150);
        	  }
			},
			{field:'operTemp1',title:'状态操作',align:'center',
				formatter: function(value,row,index){
					var buttonTemp1 = "";
					if(xyzControlButton("buttonCode_y20141225153104")){
						if(row.enabled==1){
							buttonTemp1 =  xyzGetA("关闭", "editCustomerEnabled", row.numberCode, "点我关闭用户", "blue");
						}else{
							buttonTemp1 =  xyzGetA("启用", "editCustomerEnabled", row.numberCode, "点我启用用户", "blue");
						}
					}
					return buttonTemp1;
				}
			},
			{field:'enabled',title:'可用',align:'center',
				formatter: function(value,row,index){
					if (value == 1){
						return '<span style="font-size:18px;font-weight:bold;">√';
					} else {
						return '<span style="font-size:18px;font-weight:bold;">×</span>';
					}
				},
				styler : function(value,row,index){
					if(value == 1){
						return "background-color:green";
					}else {
						return "background-color:red";
					}
				}
			},
		    {field:'addDate',title:'注册时间',width:150,
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'remark',title:'备注',width:300,
				formatter:function(value ,row ,index){
	    		  return xyzGetDiv(value ,0 ,200);
				}
            }
		] ]
	});
}

function loadTable(){
	var nickName = $("#nickName").textbox("getValue");
	
	$("#customerManagerTable").datagrid('load', {
		nickName :nickName
	});
}

function editCustomerButton(){
	var customers = $("#customerManagerTable").datagrid("getChecked");
	if (customers.length != 1) {
		top.$.messager.alert("提示", "请先选中单个对象!", "info");
		return false;
	}
	var row = customers[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editCustomerButton',
		title : '编辑客户',
		href : '../jsp_custom/editCustomer.html',
		fit : false,
		width : 450,
	    height : 450,
		buttons : [ {
			text : '确定',
			handler : function() {
				editCustomerSubmit(row.numberCode);
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_editCustomerButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			xyzTextbox("nickNameForm");
			
			$("#nickNameForm").textbox("setValue", row.nickName);
			$("#remarkForm").val(row.remark);
		} 
	});
}

function editCustomerEnabled(numberCode){
	$.ajax({
		url : "../CustomerWS/editCustomerEnabled.do",
		type : "POST",
		data : {
			numberCode : numberCode
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#customerManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功！","info");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editCustomerSubmit(numberCode){
	if(!$("form").form('validate')) {
		return false;
	}

	var nickName = $("#nickNameForm").textbox("getValue");
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../CustomerWS/editCustomer.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			nickName : nickName,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#customerManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_editCustomerButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}