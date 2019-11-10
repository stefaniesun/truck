$(document).ready(function() {
	initSecurityUserList();
	xyzTextbox("username");
	$("#dateStart").datetimebox("setValue",new Date().Format("%y-%M-%d")+" 08:00:00");
	$("#dateEnd").datetimebox("setValue",new Date().Format("%y-%M-%d")+" 23:59:59");
	initTable();
	$("#userLogQueryButton").click(function(){
		loadTable();
	});
	$("#userLogQueryButton2").click(function(){
		loadTableOther();
	});
});

function initTable(){
	xyzgrid({
		table : 'userLogTable',
		title:'用户列表',
		url:'../LogWS/getLogOperListForCheck.do',
		idField:'iidd',
		singleSelect : false,
		columns:[[
			{field:'username',title:'账号',width:120},
			{field:'nickName',title:'昵称',width:120,
		    	formatter: function(value,row,index){
		    		for(var p in securityUserList){
		    			if(row.username==securityUserList[p].value){
		    				return securityUserList[p].text;
		    			}
		    		}
				}
		    },
			{field:'dateStart',title:'最早时间',width:140},
			{field:'dateEnd',title:'最晚时间',width:140},
			{field:'count',title:'次数',width:40},
			{field:'ipInfo',title:'IP内容',width:400,
				formatter: function(value,row,index){
					return xyzGetDiv(value, 0, 100);
				}
			}
		]],
		onClickCell:function(rowIndex, field,value){
			var columnObj=$("#userLogTable").datagrid("getColumnOption",field);
			if(field=="ipInfo"){
				xyzdialog({
					dialog : 'dialogFormDiv_userLogTable_dataContent',
					title : columnObj.title,
				    content : '<textarea id="myTextareaForm" style="width:750px;height:500px;"></textarea>',
				    width:800,
				    height:600,
				    fit:false,
				    buttons:[{
						text:'关闭',
						handler:function(){
							$("#dialogFormDiv_userLogTable_dataContent").dialog("destroy");
						}
					}],
					onOpen : function(){
						$("#myTextareaForm").val(value);
					}
				});
			}
		},
		queryParams : {
			dateStart : $("#dateStart").datebox("getText"),
			dateEnd : $("#dateEnd").datebox("getText")
		}
	});
}

function loadTable(){
	var username = $("#username").val();
	var dateStart = $("#dateStart").datebox("getText");
	var dateEnd = $("#dateEnd").datebox("getText");
	$("#userLogTable").datagrid('load',{
		username : username,
		dateStart : dateStart,
		dateEnd : dateEnd
	});
}

function loadTableOther(){
	var dateStart = $("#dateStart").datebox("getText");
	var dateEnd = $("#dateEnd").datebox("getText");
	$.ajax({
		url:'../LogWS/getLogOperListForOther.do',
		type : "POST",
		data : {
			dateStart : dateStart,
			dateEnd : dateEnd
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				xyzdialog({
					dialog : 'dialogFormDiv_loadTableOther',
					title : "计划项详情",
					fit : false,
					width : 750,
					height :500,
					content : "<div><table style='width:710px;'><tr><td style='width:100px;'>用户</td><td id='usernameOther' style='width:600px;word-break:break-all;word-wrap:break-word;'></td></tr><tr><td>昵称</td><td id='nickNameOther' style='word-break:break-all;word-wrap:break-word;'></td></tr></table></div>",
				    buttons : [{
						text : '返回',
						handler : function(){
						$("#dialogFormDiv_loadTableOther").dialog("destroy");
						}
					}],
					onOpen:function(){
						$("#usernameOther").html(data.content.username);
						$("#nickNameOther").html(data.content.nickName);
					}
				});
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function initSecurityUserList(){
	$.ajax({
		url:'../AdminUserWS/getSecurityUserList.do',
		type : "POST",
		data : {},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				securityUserList = data.content;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}
