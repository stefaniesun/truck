$(document).ready(function(){
	xyzCombobox({
		combobox:'provider',
		url : '../ListWS/getProviderList.do',
		mode: 'remote',
		icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	xyzTextbox("nameCn");
	xyzTextbox("phone");
	xyzTextbox("weChat");
	
	initTable();
	
	$("#joinerQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = []; 
	if(xyzControlButton("buttonCode_x20171102100101")){
		toolbar[toolbar.length]={
				text: '新增',  
				border:'1px solid #bbb',
				iconCls: 'icon-add', 
				handler: function(){
					addJoinerButton();
				}
		};
	}
	
	if(xyzControlButton("buttonCode_x20171102100102")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
			text: '编辑',  
			border:'1px solid #bbb',  
			iconCls: 'icon-edit', 
			handler: function(){
				editJoinerButton();
			}
		};
	}	
	if(xyzControlButton("buttonCode_x20171102100103")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
				text: '删除',  
				border:'1px solid #bbb',  
				iconCls: 'icon-remove', 
				handler: function(){
					deleteJoinerButton();
				}
		};
	}
	
	xyzgrid({
		table : 'joinerManagerTable',
		title : "对接人列表 ",
		url : '../JoinerWS/queryJoinerList.do',
		idField : 'numberCode',
		toolbar : toolbar,
		fit : true,  
		singleSelect : true, 
		columns:[[
	          {field:'checkboxTemp',checkbox:true},
	          {field:'numberCode',title:'编号',width:80,hidden:true,halign:'center'},
	          {field:'nameCn',title:'姓名',align:'center'},
	          {field:'phone',title:'电话',halign:'center'},
	          {field:'weChat',title:'微信号',halign:'center'},
	          {field:'qq',title:'QQ号',halign:'center'},
	          {field:'remark',title:'备注',
				 formatter:function(value, row, index){
		    		 return xyzGetDiv(value ,0 ,200);
		    	 }
			  },
			  {field:'addDate',title:'添加时间',hidden:true,halign:'center',
	        	  formatter:function(value, row, index){
						 return xyzGetDivDate(value);
				 }
	          },
			  {field:'alterDate',title:'修改时间',halign:'center',
				 formatter:function(value, row, index){
					 return xyzGetDivDate(value);
				 }
			  }
		]]
	});
}

function loadTable(){
	var provider = $("#provider").combobox("getValue");
	var nameCn = $("#nameCn").val();
	var phone = $("#phone").val();
	var weChat = $("#weChat").val();
	
	$("#joinerManagerTable").datagrid('load', {
		provider : provider,
		nameCn : nameCn,
		phone : phone,
		weChat : weChat
	});
}

function addJoinerButton(){
	xyzdialog({
		dialog : 'dialogFormDiv_addJoinerButton',
		title : '新增对接人信息',
	    href : '../jsp_resources/addJoiner.html',
	    fit : false,  
	    width: 450,
	    height : 450,
	    buttons : [{
			text:'确定',
			handler:function(){
				addJoinerSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addJoinerButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzCombobox({
				required : true,
				combobox:'providerForm',
				url : '../ListWS/getProviderList.do',
				mode: 'remote',
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			xyzTextbox("nameCnForm");
			xyzTextbox("phoneForm");
			xyzTextbox("weChatForm");
			xyzTextbox("qqForm");
			xyzTextbox("remarkForm");
			
			$("#nameCnForm").textbox({
				required : true
			});
			/*$("#phoneForm").textbox({
				required : true
			});
			$("#weChatForm").textbox({
				required : true
			});
			$("#qqForm").textbox({
				required : true
			});*/
			
		}
	});
	
}

function addJoinerSubmit(){
	if(!$("form").form('validate')){
		return false;
	}
	
	var provider = $("#providerForm").combobox("getValue");
	var nameCn = $("#nameCnForm").val();
	var phone = $("#phoneForm").val();
	var weChat = $("#weChatForm").val();
	var qq = $("#qqForm").val();
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../AccountWS/addAccount.do",
		type : "POST",
		data : {
			nameCn : nameCn,
			phone : phone,
			weChat : weChat,
			provider : provider,
			qq : qq,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#dialogFormDiv_addJoinerButton").dialog("destroy");
				top.$.messager.alert("提示","操作成功!","info");
				$("#joinerManagerTable").datagrid("reload");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function editJoinerButton(){
	var numberCodes = $("#joinerManagerTable").datagrid("getChecked");
	if(numberCodes.length != 1){
		top.$.messager.alert("提示","请先选中单个对象!","info");
		return;
	}
	var row = numberCodes[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editJoinerButton',
		title : '编辑对接人信息',
	    href : '../jsp_resources/editJoiner.html',
	    fit : false,  
	    width : 450,
	    height : 450,
	    buttons : [{
			text:'确定',
			handler:function(){
				editJoinerSubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editJoinerButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzCombobox({
				required : true,
				combobox:'providerForm',
				url : '../ListWS/getProviderList.do',
				mode: 'remote',
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			
			xyzTextbox("nameCnForm");
			xyzTextbox("phoneForm");
			xyzTextbox("weChatForm");
			xyzTextbox("qqForm");
			xyzTextbox("remarkForm");
			
			$("#providerForm").combobox("setValue", row.provider);
			$("#nameCnForm").textbox("setValue", row.nameCn);
			$("#phoneForm").textbox("setValue", row.phone);
			$("#weChatForm").textbox("setValue", row.weChat);
			$("#qqForm").textbox("setValue", row.qq);
			$("#remarkForm").textbox("setValue", row.remark);
			
			$("#nameCnForm").textbox({
				required : true
			});
			/*$("#phoneForm").textbox({
				required : true
			});
			$("#weChatForm").textbox({
				required : true
			});
			$("#qqForm").textbox({
				required : true
			});*/
			
		}
	});
	
}

function editJoinerSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var provider = $("#providerForm").combobox("getValue");
	var nameCn = $("#nameCnForm").val();
	var phone = $("#phoneForm").val();
	var weChat = $("#weChatForm").val();
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../JoinerWS/editJoiner.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			nameCn : nameCn,
			phone : phone,
			weChat : weChat,
			provider : provider,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#dialogFormDiv_addJoinerButton").dialog("destroy");
				top.$.messager.alert("提示","操作成功!","info");
				$("#joinerManagerTable").datagrid("reload");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deleteJoinerButton(){
	
	var numberCodes = $.map($("#joinerManagerTable").datagrid("getChecked"),
		function(p){
	       return p.numberCode;
	    }
	).join(",");
	if(xyzIsNull(numberCodes)){
		top.$.messager.alert("提示","请先选中需要删除的对象!","info");
		return;
	}
	
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		if(r){
			$.ajax({
				url : "../JoinerWS/deleteJoiner.do",
				type : "POST",
				data : {
					numberCodes : numberCodes
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						$("#joinerManagerTable").datagrid("reload");
						top.$.messager.alert("提示","操作成功!","info");
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