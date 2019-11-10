//初始化
$(document).ready(function() {
	initTable();
	xyzTextbox("tokenNum");
	$("#otpQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	
	var toolbar = [];
	
	if(xyzControlButton("buttonCode_q20141227103304")){
		toolbar[toolbar.length] = {
				iconCls: 'icon-add',
				text : '新建令牌',
				handler: function(){
					var title = $(this).text();
					addOtp(title);
				}
			};
		toolbar[toolbar.length] = '-';
	}
	
	if(xyzControlButton("buttonCode_q20141227103305")){
		toolbar[toolbar.length] = {
				iconCls: 'icon-remove',
				text : '删除令牌',
				handler: function(){deleteOtp();}
			};
	}
	
	xyzgrid({
		table : 'otpManagerTable',
		title:'令牌列表',
		url:'../AdminOptWS/queryOtpList.do',
		toolbar : toolbar,
		idField : 'tokenNum',
		singleSelect : false,
		columns:[[
		    {field:'checkboxTemp',checkbox:true},  
			{field:'tokenNum',title:'令牌编号',width:250},
			{field:'authkey',title:'令牌密钥',width:398},  
			{field:'countUser',title:'绑定用户数'},
			{field:'usernames',title:'绑定用户',width:250,
				formatter: function(value,row,index){
					return xyzGetDiv(value, 0, 14);
				}
			},
			{field:'alterDate',title:'修改时',
				formatter: function(value,row,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'addDate',title:'添加时',
				formatter: function(value,row,index){
					return xyzGetDivDate(value);
				}
			}
		]]
	});
}

function loadTable(){
	var tokenNum = $("#tokenNum").val();
	var flag = $("#flag").combobox("getValue");
	$("#otpManagerTable").datagrid('load',{
		tokenNum : tokenNum,
		flag : flag
	});
}

function addOtpFormSubmit(){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var tokenNum = $("#tokenNumForm").val();
	var authkey = $("#authkeyForm").val();
	
	$.ajax({
		url : "../AdminOptWS/addOtp.do",
		type : "POST",
		data : {
			tokenNum : tokenNum,
			authkey : authkey
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#otpManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功！","info");
				$("#dialogFormDiv_addOpt").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function addOtp(title){
	xyzdialog({
		dialog : 'dialogFormDiv_addOpt',
		title : title,
	    href : '../xyzsecurity/a_addOtp.html',
	    fit:false,
	    width:500,
	    buttons:[{
			text:'确定',
			handler:function(){
				addOtpFormSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addOpt").dialog("destroy");
			}
		}]
	});
}

function deleteOtp(){
	var otps = $.map($("#otpManagerTable").datagrid("getChecked"),function(p){return p.iidd;}).join(",");
	if(xyzIsNull(otps)){
		top.$.messager.alert("提示","请先选中需要删除的对象！","info");
		return;
	}
	
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		if(r){
			$.ajax({
				url : "../AdminOptWS/deleteOtp.do",
				type : "POST",
				data : {
					otps : otps
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						$("#otpManagerTable").datagrid("reload");
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
