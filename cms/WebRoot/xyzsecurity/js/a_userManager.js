$(document).ready(function() {
	xyzTextbox("username");
	xyzTextbox("nickName");
	xyzCombobox({
		combobox : 'position',
		url : '../AdminUserWS/getAllPosition.do',
		multiple : true,
		valueField: 'numberCode',
		textField : 'nameCn',
		lazy : false
	});
	if(xyzIsNull(top.currentUserPossessor)){
		$("#q_possessor").show();
		xyzCombobox({
			combobox : 'possessor',
			url : '../ListWS/getPossessorList.do',
			lazy : false
		});
	}
	
	initTable();
	
	$("#userQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = [];
	if(xyzControlButton("buttonCode_y20141225153101")){
		toolbar[toolbar.length]={
				text: '新增',
				iconCls: 'icon-add',
				handler: function(){addUser();}
		};
	}
	if(xyzControlButton("buttonCode_y20141225153102")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
				text: '编辑',
				iconCls: 'icon-edit',
				handler: function(){editUser();}
		};
	}
	if(xyzControlButton("buttonCode_y20141225153103")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
				text: '删除',
				iconCls: 'icon-remove',
				handler: function(){deleteUser();}
		};
	}
	if(xyzControlButton("buttonCode_s20150625102901")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
				text: '重设密码',
				iconCls: 'icon-edit',
				handler: function(){editUserPassword();}
		};
	}
	
	var columns = [
           		{field:'checkboxTemp',checkbox:true},
           		{field:'username',title:'账号'},
			    {field:'nickName',title:'昵称',width:180,halign:'center',
			    	formatter: function(value,row,index){
			    		return xyzGetDiv(value,0,100);
					}
			    },
				{field:'operTemp1',title:'状态操作',align:'center',
					formatter: function(value,row,index){
						var buttonTemp1 = "";
						if(xyzControlButton("buttonCode_y20141225153104")){
							if(row.enabled==1){
								buttonTemp1 =  xyzGetA("关闭", "editUserEnabled", row.username, "点我关闭用户", "blue");
							}else{
								buttonTemp1 =  xyzGetA("启用", "editUserEnabled", row.username, "点我启用用户", "blue");
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
				{field:'position',title:'岗位操作',align:'center',
					formatter: function(value,row,index){
						var buttonTemp1 = "";
						if(xyzControlButton("buttonCode_y20141225153105")){
							buttonTemp1 = xyzGetA("岗位", "setUserPosition", [row.nickName,row.username,row.position], "点我设置岗位", "blue");
						}
						return buttonTemp1;
					}
				},
				{field:'positionNameCn',title:'岗位',align:'center'},
				{field:'isRepeat',title:'可重登',align:'center',
					formatter: function(value,row,index){
						if (value == 1){
							return '√';
						} else {
							return '×';
						}
					}
				},
				{field:'possessor',title:'机构操作',align:'center',
					formatter: function(value,row,index){
						var buttonTemp1 = "";
						if(xyzControlButton("buttonCode_s20150819095001")){
							buttonTemp1 = xyzGetA("设置机构", "setUserPossessor", [row.username,row.nickName,row.possessor], "点我设置机构", "blue");
						}
						return buttonTemp1;
					}
				},
				{field:'possessorNameCn',title:'机构',width:210,align:'center',
					formatter: function(value,row,index){
			    		return xyzGetDiv(value,0,100);
					}
				},
				{field:'alterDate',title:'修改时',align:'center',
					formatter:function(value,row,index){
						return xyzGetDivDate(value);
			    	}
				},
				{field:'addDate',title:'添加时',align:'center',
					formatter:function(value,row,index){
						return xyzGetDivDate(value);
			    	}
				}
			];
	
	if(!xyzControlButton("buttonCode_s20150819095001")){
		xyzRemoveItem(columns,"possessor","field");
	}
	if(!xyzControlButton("buttonCode_y20141225153105")){
		xyzRemoveItem(columns,"position","field");
	}
	
	xyzgrid({
		table : 'userManagerTable',
		title:'用户列表',
		url:'../AdminUserWS/queryUserList.do',
		toolbar : toolbar,
		idField:'username',
		singleSelect : false,
		columns : [columns]
	});
}

function loadTable(){
	var username = $("#username").val();
	var nickName = $("#nickName").val();
	var possessor = "";
	if(xyzIsNull(top.currentUserPossessor)){
		possessor = $("#possessor").combobox("getValues").join(",");
	}
	var position = $("#position").combobox("getValues").join(",");
	var enabled = $("#enabled").combobox("getValue");
	$("#userManagerTable").datagrid('load',{
		username : username,
		nickName : nickName,
		possessor : possessor,
		position : position,
		enabled : enabled
	});
}

function addUser(title){
	xyzdialog({
		dialog : 'dialogFormDiv_addUser',
		title : "新增用户",
	    href : '../xyzsecurity/a_addUser.html',
	    fit:false,
		width:400,
		height:300,
	    buttons:[{
			text:'确定',
			handler:function(){
				addUserSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addUser").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("usernameForm");
			xyzTextbox("nickNameForm");
		}
	});
}

function editUser(title){
	var users = $("#userManagerTable").datagrid("getChecked");
	if(users.length != 1){
		top.$.messager.alert("提示","请先选中单个对象！","info");
		return;
	}
	var row = users[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editUser',
		title : "编辑用户",
	    href : '../xyzsecurity/a_editUser.html',
	    fit:false,
	    height:300,
	    width:400,
	    buttons:[{
			text:'确定',
			handler:function(){
				editUserSubmit(row.username);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editUser").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("nickNameForm");
			
			$("#usernameForm").html(row.username);
			$("#nickNameForm").textbox("setValue",row.nickName);
			
			$("#nickNameForm").textbox({
				required : true
			});
		}
	});
}

function deleteUser(){
	var users = $.map($("#userManagerTable").datagrid("getChecked"),function(p){return p.username;}).join(",");
	if(xyzIsNull(users)){
		top.$.messager.alert("提示","请先选中需要删除的对象！","info");
		return;
	}
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
	if(r){
	 $.ajax({
		url : "../AdminUserWS/deleteUser.do",
		type : "POST",
		data : {
			users : users
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#userManagerTable").datagrid("reload");
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
	});
}

function addUserSubmit(){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var username = $("#usernameForm").textbox("getValue");
	var nickName = $("#nickNameForm").textbox("getValue");
	
	$.ajax({
		url : "../AdminUserWS/addUser.do",
		type : "POST",
		data : {
			username : username,
			nickName : nickName
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#userManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功！","info");
				$("#dialogFormDiv_addUser").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editUserSubmit(username){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var nickName = $("#nickNameForm").textbox("getValue");
	
	$.ajax({
		url : "../AdminUserWS/editUser.do",
		type : "POST",
		data : {
			username : username,
			nickName : nickName
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#userManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功！","info");
				$("#dialogFormDiv_editUser").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function setUserPosition(nickName,username,currentPosition){
	
	var currentUserName = top.currentUserUsername;
	if (currentUserName == username) {
		top.$.messager.alert("提示","不能修改自己的岗位!","info");
		return;
	}
	
	xyzdialog({
		dialog : 'dialogFormDiv_setUserPosition',
		title : "设置岗位["+nickName+"]["+username+"]",
	    content : '<div><input type="text" id="positionForm"></div>',
	    fit:false,
	    width:300,
	    height:200,
	    buttons:[{
			text:'确定',
			handler:function(){
				setUserPositionSubmit(username);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_setUserPosition").dialog("destroy");
			}
		}],
		onOpen : function(){
			xyzCombobox({
				combobox : 'positionForm',
				url : '../AdminUserWS/getAllPosition.do',
				lazy : false,
				valueField : 'numberCode',
				textField : 'nameCn'
			});
			
			$("#positionForm").combobox("setValue" ,currentPosition);
		}
	});
}

function setUserPositionSubmit(username){
	var position = $("#positionForm").combobox("getValue");
	$.ajax({
		url : "../AdminUserWS/setUserPosition.do",
		type : "POST",
		data : {
			username : username,
			position : position
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#userManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功！","info");
				$("#dialogFormDiv_setUserPosition").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editUserEnabled(username){
	$.ajax({
		url : "../AdminUserWS/editUserEnabled.do",
		type : "POST",
		data : {
			username : username
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#userManagerTable").datagrid("reload");
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

function editUserPassword(){
	var users = $("#userManagerTable").datagrid("getChecked");
	if(users.length != 1){
		top.$.messager.alert("提示","请先选中单个对象！","info");
		return;
	}
	var row = users[0];
	
	var html = '<center> <form> <br/>';
	html += '<table cellspacing="6" cellpadding="6">';
	html += ' <tr>';
	html += '  <td style="text-align:right;">用户名</td>';
	html += '  <td>'+row.username+'</td>';
	html += ' </tr>';
	html += ' <tr>';
	html += '  <td style="text-align:right;">昵称</td>';
	html += '  <td>'+row.nickName+'</td>';
	html += ' </tr>';
	html += ' <tr>';
	html += '  <td style="text-align:right;">新密码</td>';
	html += '  <td>';
	html += '   <input type="password" id="passwordForm" style="width:300px;" class="easyui-validatebox" data-options="required:true,validType:\'length[3,20]\'"/>';
	html += '  </td>';
	html += ' </tr>';
	html += ' <tr>';
	html += '   <td style="text-align:right;">确认密码</td>';
	html += '   <td>';
	html += '    <input type="password" id="password2Form" style="width:300px;" class="easyui-validatebox" data-options="required:true,validType:\'length[3,20]\'"/>';
	html += '   </td>';
	html += ' </tr>';
	html += '</table>';
	html += '</form> </center>';
	
	xyzdialog({
		dialog : 'editUserPassword',
		title : '重设['+row.username+']的登录密码',
		content : html,
	    fit : false,
	    width : 450,
	    height : 300,
	    buttons:[{
			text:'确定',
			handler:function(){
				editUserPasswordSubmit(row.username);
			}
		},{
			text:'取消',
			handler:function(){
				$("#editUserPassword").dialog("destroy");
			}
		}],
		onLoad : function(){
			$("#passwordForm").validatebox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).validatebox('clear');
					}
				}]
			});
			$("#password2Form").validatebox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).validatebox('clear');
					}
				}]
			});
		}
	});
}


function editUserPasswordSubmit(username){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var password = $("#passwordForm").val();
	var password2 = $("#password2Form").val();
	if(password!=password2){
		top.$.messager.alert("提示","两次输入的密码不一致，请重新输入！","info");
		return;
	}
	password3 = $.md5(password).substr(8,16);
	
	$.ajax({
		url : "../AdminUserWS/editUserPassword.do",
		type : "POST",
		data : {
			username : username,
			password : password3
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#editUserPassword").dialog("destroy");
				$("#userManagerTable").datagrid("reload");
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

function setUserPossessor(username ,nickName ,possessor){
	xyzdialog({
		dialog : 'dialogFormDiv_setUserPossessor',
		title : "给"+nickName+"设置机构",
	    fit:false,
	    width:300,
	    height:200,
	    content : '<div><input type="text" id="possessorForm" /></div>',
	    buttons:[{
			text:'确定',
			handler:function(){
				setUserPossessorSubmit(username);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_setUserPossessor").dialog("destroy");
			}
		}],
		onOpen:function(){
			xyzCombobox({
				combobox : 'possessorForm',
				url : '../ListWS/getPossessorList.do',
				lazy : false,
				mode : 'remote'
			});
			$("#possessorForm").combobox("setValue" ,possessor);
		}
	});
}

function setUserPossessorSubmit(username){
	var possessor = $("#possessorForm").combobox("getValue");
	$.ajax({
		url : "../AdminUserWS/setUserPossessor.do",
		type : "POST",
		data : {
			username : username,
			possessor : possessor
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功！","info");
				$("#userManagerTable").datagrid("reload");
				$("#dialogFormDiv_setUserPossessor").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}
