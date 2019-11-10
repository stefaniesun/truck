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
	xyzTextbox("accountNumber");
	xyzTextbox("cashAccount");
	xyzTextbox("bankOfDeposit");
	
	$("#isEnable").combobox({
		icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	
	initTable();
	
	$("#accountQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = []; 
	if(xyzControlButton("buttonCode_x20171031163601")){
		toolbar[toolbar.length]={
				text: '新增',  
				border:'1px solid #bbb',
				iconCls: 'icon-add', 
				handler: function(){
					addAccountButton();
				}
		};
	}
	
	if(xyzControlButton("buttonCode_x20171031163602")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
			text: '编辑',  
			border:'1px solid #bbb',  
			iconCls: 'icon-edit', 
			handler: function(){
				editAccountButton();
			}
		};
	}	
	if(xyzControlButton("buttonCode_x20171031163604")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
				text: '删除',  
				border:'1px solid #bbb',  
				iconCls: 'icon-remove', 
				handler: function(){
					deleteAccountButton();
				}
		};
	}
	
	xyzgrid({
		table : 'accountManagerTable',
		title : "供应商账户列表 ",
		url:'../AccountWS/queryAccountList.do',
		idField : 'numberCode',
		toolbar : toolbar,
		fit : true,  
		singleSelect : true, 
		queryParams : {
			isEnable : -1
		},
		columns:[[
	          {field:'checkboxTemp',checkbox:true},
	          {field:'numberCode',title:'编号',width:80,hidden:true,halign:'center'},
	          {field:'accountNumber',title:'对公帐号',align:'center'},
	          {field:'cashAccount',title:'收款户名',halign:'center',
	        	  formatter:function(value, row, index){
	        		  return xyzGetDiv(value, 0, 60);
	        	  }
	          },
	          {field:'bankOfDeposit',title:'对账行号',halign:'center',
	        	  formatter:function(value, row, index){
	        		  return xyzGetDiv(value, 0, 60);
	        	  }
	          },
	          {field:'isEnable',title:'是否启用',halign:'center',align:'center',
	             formatter: function(value,row,index){
			    	return '<input style="width:42px;height:18px;" class="switchbutton isEnableSwitchbutton" data-options="checked:'+(value==1?'true':'false')+'" data-infoTableCode="'+row.numberCode+'"/>';
				 }
	          },
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
		]],
		onLoadSuccess : function(data){
			$('.isEnableSwitchbutton').switchbutton({ 
			      onText : '启用',
			      offText : '关闭',
			      onChange: function(checked){ 
			    	  editIsEnable($(this).attr('data-infoTableCode'));
			      } 
			});
		}
	});
}

function loadTable(){
	var provider = $("#provider").combobox("getValue");
	var isEnable = xyzIsNull($("#isEnable").combobox("getValue"))==true?-1:parseInt($("#isEnable").combobox("getValue"));
	var accountNumber = $("#accountNumber").val();
	var cashAccount = $("#cashAccount").val();
	var bankOfDeposit = $("#bankOfDeposit").val();
	
	$("#accountManagerTable").datagrid('load', {
		provider : provider,
		isEnable : isEnable,
		accountNumber : accountNumber,
		cashAccount : cashAccount,
		bankOfDeposit : bankOfDeposit
	});
}

function addAccountButton(){
	xyzdialog({
		dialog : 'dialogFormDiv_addAccountButton',
		title : '新增供应商账户信息',
	    href : '../jsp_resources/addAccount.html',
	    fit : false,  
	    width: 450,
	    height : 450,
	    buttons:[{
			text:'确定',
			handler:function(){
				addAccountSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addAccountButton").dialog("destroy");
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
			xyzTextbox("accountNumberForm");
			xyzTextbox("cashAccountForm");
			xyzTextbox("bankOfDepositForm");
			xyzTextbox("remarkForm");
			
			$("#accountNumberForm").textbox({
				required : true
			});
			$("#cashAccountForm").textbox({
				required : true
			});
			$("#bankOfDepositForm").textbox({
				required : true
			});
			
			$("input[type='radio'][name='enable']").click(function(){
				var oldValue = $("#isEnableForm").val();
				var newValue = $("input[type='radio'][name='enable']:checked").val();
				
				if(oldValue != newValue){
					$("#isEnableForm").val(newValue);
				}
			});
			
		}
	});
}

function addAccountSubmit(){
	if(!$("form").form('validate')){
		return false;
	}
	
	var provider = $("#providerForm").combobox("getValue");
	var accountNumber = $("#accountNumberForm").val();
	var cashAccount = $("#cashAccountForm").val();
	var bankOfDeposit = $("#bankOfDepositForm").val();
	var isEnable = $("#isEnableForm").val();
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../AccountWS/addAccount.do",
		type : "POST",
		data : {
			accountNumber : accountNumber,
			cashAccount : cashAccount,
			bankOfDeposit : bankOfDeposit,
			isEnable : isEnable,
			provider : provider,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#dialogFormDiv_addAccountButton").dialog("destroy");
				top.$.messager.alert("提示","操作成功!","info");
				$("#accountManagerTable").datagrid("reload");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function editAccountButton(){
	var numberCodes = $("#accountManagerTable").datagrid("getChecked");
	if(numberCodes.length != 1){
		top.$.messager.alert("提示","请先选中单个对象!","info");
		return;
	}
	var row = numberCodes[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editAccountButton',
		title : '编辑供应商账户',
	    href : '../jsp_resources/editAccount.html',
	    fit : false,  
	    width : 450,
	    height : 450,
	    buttons : [{
			text:'确定',
			handler:function(){
				editAccountSubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editAccountButton").dialog("destroy");
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
			
			xyzTextbox("accountNumberForm");
			xyzTextbox("cashAccountForm");
			xyzTextbox("bankOfDepositForm");
			xyzTextbox("remarkForm");
			
			$("#providerForm").combobox("setValue", row.provider);
			
			$("#accountNumberForm").textbox("setValue", row.accountNumber);
			$("#cashAccountForm").textbox("setValue", row.cashAccount);
			$("#bankOfDepositForm").textbox("setValue", row.bankOfDeposit);
			$("#remarkForm").textbox("setValue", row.remark);
			
			if(row.isEnable == 1){
				$("input[type='radio'][name='enable']").eq(0).attr("checked","checked");
			}else{
				$("input[type='radio'][name='enable']").eq(1).attr("checked","checked");
			}
			$("#isEnableForm").val(row.isEnable);
			
			$("#accountNumberForm").textbox({
				required : true
			});
			$("#cashAccountForm").textbox({
				required : true
			});
			$("#bankOfDepositForm").textbox({
				required : true
			});
			
			$("input[type='radio'][name='enable']").click(function(){
				var oldValue = $("#isEnableForm").val();
				var newValue = $("input[type='radio'][name='enable']:checked").val();
				
				if(oldValue != newValue){
					$("#isEnableForm").val(newValue);
				}
			});
			
		}
	});
	
}

function editAccountSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var provider = $("#providerForm").combobox("getValue");
	var accountNumber = $("#accountNumberForm").val();
	var cashAccount = $("#cashAccountForm").val();
	var bankOfDeposit = $("#bankOfDepositForm").val();
	var isEnable = $("#isEnableForm").val();
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../AccountWS/addAccount.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			accountNumber : accountNumber,
			cashAccount : cashAccount,
			bankOfDeposit : bankOfDeposit,
			isEnable : isEnable,
			provider : provider,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#dialogFormDiv_addAccountButton").dialog("destroy");
				top.$.messager.alert("提示","操作成功!","info");
				$("#accountManagerTable").datagrid("reload");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deleteAccountButton(){
	
	var numberCodes = $.map($("#accountManagerTable").datagrid("getChecked"),
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
				url : "../AccountWS/deleteAccount.do",
				type : "POST",
				data : {
					numberCodes : numberCodes
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						$("#accountManagerTable").datagrid("reload");
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

function editIsEnable(numberCode){
	xyzAjax({
		url : "../AccountWS/editIsEnable.do",
		data : {
			numberCode : numberCode
		},
		success : function(data) {
			if(data.status !=1){
				top.$.messager.alert("警告",data.msg,"warning");
			}else{
				$("#accountManagerTable").datagrid("reload");
			}
		}
	});
}