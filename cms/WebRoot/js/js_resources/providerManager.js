$(document).ready(function(){
	
	xyzTextbox("nameCn");
	
	initTable();
	
	yqqQueryPlugin.create({
		pluginId : "providerAutoId",
		autoId : "providerAutoId",
		data : [
					{
						key : "nameCn",
						value : "名称【模】"
					},
					{
						key : "joiner",
						value : "渠道对接人【模】"
					},
					{
						key : "weChat",
						value : "微信【模】"
					},
					{
						key : "talkGroup",
						value : "讨论组【模】"
					},
					{
				    	key : "status",
				    	value : "负责和客户签订合同 ",
				    	type : "combobox",
				    	data : {
				    		multiple:true,
				    		data:[{text:'是',value:'1'},
	  							  {text:'否',value:'0'},
	  							  {text:'用自己的合同',value:'2'},
	  							  {text:'其它',value:'3'}]
				    	}
				    }
	        ]
	});
	
	$("#providerQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = []; 
	if(xyzControlButton("buttonCode_y20161208093601")){
		toolbar[toolbar.length]={
				text: '新增',  
				border:'1px solid #bbb',
				iconCls: 'icon-add', 
				handler: function(){
					addProviderButton();
				}
		};
	}
	
	if(xyzControlButton("buttonCode_y20161208093602")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
			text: '编辑',  
			border:'1px solid #bbb',  
			iconCls: 'icon-edit', 
			handler: function(){
				editProviderButton();
			}
		};
	}	
	if(xyzControlButton("buttonCode_y20161208093603")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
				text: '删除',  
				border:'1px solid #bbb',  
				iconCls: 'icon-remove', 
				handler: function(){
					deleteProviderButton();
				}
		};
	}
	if(xyzControlButton("buttonCode_x20170830165302")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
				text: '导出Excel模版',  
				border:'1px solid #bbb',  
				iconCls: 'icon-', 
				handler: function(){
					exportExcelButton();
				}
		};
	}
	if(xyzControlButton("buttonCode_x20170830165301")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
				text: '上传Excel',  
				border:'1px solid #bbb',  
				iconCls: 'icon-edit', 
				handler: function(){
					importExcelButton();
				}
		};
	}
	
	xyzgrid({
		table : 'providerManagerTable',
		title : "供应商列表  <span style='color:red;'>数值越小代表等级越高</span>",
		url:'../ProviderWS/queryProviderList.do',
		toolbar:toolbar,
		fit : true,  
		singleSelect : true, 
		idField : 'numberCode',
		columns:[[
	          {field:'checkboxTemp',checkbox:true,hidden:true},
	          {field:'numberCode',title:'编号',width:80,hidden:true,halign:'center'},
	          {field:'grade',title:'等级',align:'center'},
	          {field:'nameCn',title:'名称',width:100,halign:'center',
	        	  formatter:function(value ,row ,index){
	        		  return xyzGetDiv(value ,0 ,60);
	        	  }
	          },
	          {field:'mark',title:'查询编号',width:60,halign:'center'},
	          {field:'isResponsible',title:'负责和客户签订合同',halign:'center',
	        	  formatter:function(value ,row ,index){
	        		  if(value == 1){
	        			  return "是";
	        		  }else if(value == 0){
	        			  return "否";
	        		  }else if(value == 2){
	        			  return "用自己的合同";
	        		  }else if(value == 3){
	        			  return "其它";
	        		  }
	        	  }
	          },
	          {field:'status',title:'合作',align:'center',
	        	  formatter:function(value, row, index){
	  				if(value == 1) {
	  					return "<span style='font-size:18px;font-weight:bold;'>√</span>";
	  				}else{
	  					return "<span style='font-size:18px;font-weight:bold;'>×</span>";
	  				}
	  			},
	  			styler:function(value, row, index) {
	  				if(value == 1) {
	  					return "background-color:green";
	  				}else{
	  					return "background-color:red";
	  				}
	  			}
	          },
	          {field:'account',title:'操作',halign:'center',
	        	  formatter: function(value, row, index){
					var btnTemp = "";
					if(xyzControlButton("buttonCode_x20170321170102")){
						btnTemp += xyzGetA("账户设置("+ row.accountSum +")","setAccount",[row.numberCode, row.nameCn],"点我设置账户信息","blue");
					}
					if(xyzControlButton("buttonCode_x20170321170103")){
						btnTemp += " &nbsp; ";
						btnTemp += xyzGetA("对接人设置("+ row.joinerSum +")","setJoiner",[row.numberCode, row.nameCn],"点我设置对接人信息","blue");
					}
					return btnTemp;
				}
	          },
	          {field:'address',title:'材料寄到地址',width:100,halign:'center',
	        	  formatter:function(value, row, index){
	        		  return xyzGetDiv(value, 0, 100);
	        	  }
	          },
	          {field:'weChat',title:'微信',width:60,halign:'center',
	        	  formatter:function(value ,row ,index){
	        		  return xyzGetDiv(value ,0 ,60);
	        	  }
	          },
	          {field:'talkGroup',title:'讨论组',width:60,halign:'center',
	        	  formatter:function(value ,row ,index){
	        		  return xyzGetDiv(value ,0 ,60);
	        	  }
	          },
	          {field:'sign',title:'与客户签订方式',width:90,halign:'center',
	        	  formatter:function(value ,row ,index){
	        		  return xyzGetDiv(value ,0 ,80);
	        	  }
	          },
	          {field:'settlement',title:'结算方式',width:60,halign:'center',
	        	  formatter:function(value ,row ,index){
	        		  return xyzGetDiv(value ,0 ,60);
	        	  }
	          },
	          {field:'policy',title:'后返政策',width:60,halign:'center',
	        	  formatter:function(value ,row ,index){
	        		  return xyzGetDiv(value ,0 ,60);
	        	  }
	          },
			  {field:'addDate',title:'添加时间',hidden:true,halign:'center',
	        	  formatter:function(value ,row ,index){
						 return xyzGetDivDate(value);
				 }
	          },
			  {field:'alterDate',title:'修改时间',halign:'center',
				 formatter:function(value ,row ,index){
					 return xyzGetDivDate(value);
				 }
			  }
		]]
	});
}

function loadTable(){
	
	var mark = $("#mark").val();
	var nameCn = $("#nameCn").val();
	var temp = yqqQueryPlugin.getValue("providerAutoId");
	var queryJson = {};
    if(!xyzIsNull(temp)){
    	queryJson = xyzJsonToObject(temp);
    }
    var queryJsonStr = JSON.stringify(queryJson);
	
	$("#providerManagerTable").datagrid('load', {
		mark : mark,
		nameCn : nameCn,
		queryJson : queryJsonStr
	});
}

function addProviderButton(){
	xyzdialog({
		dialog : 'dialogFormDiv_addProviderButton',
		title : '新增供应商',
	    href : '../jsp_resources/addProvider.html',
	    fit : false,  
	    width: 600,
	    height : 600,
	    buttons:[{
			text:'确定',
			handler:function(){
				addProviderSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addProviderButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("nameCnForm");
			xyzTextbox("markForm");
			xyzTextbox("addressForm");
			xyzTextbox("weChatForm");
			xyzTextbox("talkGroupForm");
			xyzTextbox("signForm");
			xyzTextbox("settlementForm");
			xyzTextbox("policyForm");
			xyzTextbox("remarkForm");
		}
	});
}

function addProviderSubmit(){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").val();
	var mark = $("#markForm").val();
	var address = $("#addressForm").val();
	var weChat = $("#weChatForm").val();
	var talkGroup = $("#talkGroupForm").val();
	var isResponsible = $("#isResponsibleForm").combobox("getValue");
	var sign = $("#signForm").val();
	var settlement = $("#settlementForm").val();
	var policy = $("#policyForm").val();
	var status = $("#statusForm").combobox("getValue");
	var remark = $("#remarkForm").val();
	var grade=$("#gradeForm").combobox("getValue");
	
	if(xyzControlButton("buttonCode_x20170321170101")){
		var result = getProviderMarkData(mark);
		if("0"!=result){
			top.$.messager.alert("提示", result ,"info");
			return false;
		}
	}
	
	$.ajax({
		url : "../ProviderWS/addProvider.do",
		type : "POST",
		data : {
			mark :mark,
			nameCn :nameCn,
			address : address,
			weChat : weChat,
			talkGroup : talkGroup,
			isResponsible : isResponsible,
			sign : sign,
			settlement : settlement, 
			policy : policy,
			status : status,
			remark : remark,
			grade : grade
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#providerManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_addProviderButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function getProviderMarkData(mark, numberCode){
	var result = "0";
	
	$.ajax({
		url : "../ProviderWS/getProviderMarkList.do",
		type : "POST",
		data : {
			mark : mark,
			numberCode : numberCode
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var providerList = data.content;
				if(providerList.length > 0){
					var providerObj = providerList[0];
					result = "【"+providerObj.mark+"】查询编号已经存在!";
					return result;
				}
			}
		}
	});
	
	return result;
}

function editProviderButton(){
	var providers = $("#providerManagerTable").datagrid("getChecked");
	if(providers.length != 1){
		top.$.messager.alert("提示","请先选中单个对象!","info");
		return;
	}
	var row = providers[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editProviderButton',
		title : '编辑供应商',
	    href : '../jsp_resources/editProvider.html',
	    fit : false,  
	    width: 600,
	    height : 600,
	    buttons:[{
			text:'确定',
			handler:function(){
				editProviderSubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editProviderButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("nameCnForm");
			xyzTextbox("markForm");
			xyzTextbox("addressForm");
			xyzTextbox("weChatForm");
			xyzTextbox("talkGroupForm");
			xyzTextbox("signForm");
			xyzTextbox("settlementForm");
			xyzTextbox("policyForm");
			xyzTextbox("remarkForm");
			
			
			$("#nameCnForm").textbox("setValue",row.nameCn);
			$("#markForm").textbox("setValue",row.mark);
			$("#addressForm").textbox("setValue",row.address);
			$("#weChatForm").textbox("setValue",row.weChat);
			$("#talkGroupForm").textbox("setValue",row.talkGroup);
			$("#gradeForm").combobox("setValue",row.grade);
			$("#isResponsibleForm").combobox("setValue",row.isResponsible);
			$("#signForm").textbox("setValue",row.sign);
			$("#settlementForm").textbox("setValue",row.settlement);
			$("#policyForm").textbox("setValue",row.policy);
			$("#statusForm").combobox("setValue",row.status);
			$("#remarkForm").textbox("setValue",row.remark);
			$("#nameCnForm").textbox({
				required:true
			});
			$("#queryCodeForm").textbox({
				required:true
			});
		}
	});
}

function editProviderSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").val();
	var mark = $("#markForm").val();
	var address = $("#addressForm").val();
	var weChat = $("#weChatForm").val();
	var talkGroup = $("#talkGroupForm").val();
	var isResponsible = $("#isResponsibleForm").combobox("getValue");
	var sign = $("#signForm").val();
	var settlement = $("#settlementForm").val();
	var policy = $("#policyForm").val();
	var status = $("#statusForm").combobox("getValue");
	var remark = $("#remarkForm").val();
	var grade = $("#gradeForm").combobox("getValue");
	
	if(xyzControlButton("buttonCode_x20170321170101")){
		var result = getProviderMarkData(mark, numberCode);
		if("0"!=result){
			top.$.messager.alert("提示", result ,"info");
			return false;
		}
	}
	
	$.ajax({
		url : "../ProviderWS/editProvider.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			mark :mark,
			nameCn :nameCn,
			address : address,
			weChat : weChat,
			talkGroup : talkGroup,
			isResponsible : isResponsible,
			sign : sign,
			settlement : settlement, 
			policy : policy,
			status : status,
			remark : remark,
			grade : grade
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#providerManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_editProviderButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deleteProviderButton(){
	var providers = $.map($("#providerManagerTable").datagrid("getChecked"),
		function(p){
	       return p.numberCode;
	    }
	).join(",");
	if(xyzIsNull(providers)){
		top.$.messager.alert("提示","请先选中需要删除的对象!","info");
		return;
	}
	
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		if(r){
			$.ajax({
				url : "../ProviderWS/deleteProviders.do",
				type : "POST",
				data : {
					providers : providers
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						$("#providerManagerTable").datagrid("reload");
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

function exportExcelButton(){
	
	$.ajax({
		url : "../ProviderWS/exportExcelOper.do",
		type : "POST",
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				window.location.assign("../tempFile/"+data.content);
				$("#providerManagerTable").datagrid("reload");
				
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

function importExcelButton(){
	var contentHtml = '<div id="xyzUploadifyGridDiv"></div>';
	contentHtml += '<br/>';
	contentHtml += '<div id="xyzUploadifyButton" style="margin-left:100px;width:200px;height:20px;"></div>';
	
	xyzdialog({
		dialog : 'dialogFormDiv_importExcelButton',
		title : '上传Excel',
		content : contentHtml,
		fit : false,
		width : 450,          
		height : 450,
	    buttons : [{
			text:'确定',
			handler:function(){
				importExcelSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_importExcelButton").dialog("destroy");
			}
		}],onOpen : function(){
			//初始化图片容器
			xyzPicPreview.create({
				xyzPicPreview : 'xyzUploadifyGridDiv',
				//初始化后要立即展示的链接
				maxCount : 1
			});
			//上传容器
			xyzDropzone.create({
				xyzDropzone : 'xyzUploadifyButton',  //容器div，允许使用逗号分隔传入多个ID则同时初始化多个xyzDropzone
				params : {"derictoryCode":"provider"}, //上传时需要同时提交的参数键值对
				maxFiles : 1,                        //本控件最多允许上传的文件数量 默认10
				acceptedExtName : ".xls,.xlsx",      //允许文件类型
				maxFilesize : "3072kb",              //允许上传的单个文件大小（单位kb）
				xyzDropzone : 'xyzUploadifyButton',  //容器div，允许使用逗号分隔传入多个ID则同时初始化多个xyzDropzone
				btnText : '点击或拖拽文件至此',
				acceptedExtName : ".xls,.xlsx",      //允许文件类型
				params : {"derictoryCode":"provider"}, //上传时需要同时提交的参数键值对
				maxFilesize : "1024kb",              //允许上传的单个文件大小（单位kb）
				maxFiles : 100,                      //本控件最多允许上传的文件数量 默认10
				success : function(result){
					xyzPicPreview.addPic('xyzUploadifyGridDiv', result.content.url);
				}
			});
			
		}
	});
	
}

function importExcelSubmit(){
	
	var path = xyzPicPreview.getAllPic("xyzUploadifyGridDiv").join(",");
	if(xyzIsNull(path)){
		top.$.messager.alert("提示","请上传Excel文件!","info");
		return false;
	}
	
	$.ajax({
		url : "../ProviderWS/importExcelOper.do",
		type : "POST",
		data : {
			excelPath : path
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#dialogFormDiv_importExcelButton").dialog("destroy");
				top.$.messager.alert("提示","操作成功!","info");
				$("#providerManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function setAccount(provider, nameCn){
	var accoutList = null;
	$.ajax({
		url : "../AccountWS/queryAccountListByprovider.do",
		type : "POST",
		data : {
			provider : provider
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				accoutList = data.content;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
	xyzdialog({
		dialog : 'dialogFormDiv_setAccount',
		title : '设置供应商账户',
	    href : '../jsp_resources/setAccount.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				setAccountSubmit(provider);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_setAccount").dialog("destroy");
			}
		}],
		onLoad : function(){
			var html = '';
			html += '<caption>';
			html += ' 供应商【'+ nameCn +'】帐号信息&nbsp;&nbsp;&nbsp;';
			html += ' <img id="addImg" src="../image/other/addPage.gif"';
			html += '  alt="点我新增供应商账户" title="点我新增供应商账户" style="cursor:pointer;">';
			html += '</caption>';
			html += '<tr>';
			html += '	<th>对公帐号</th>';
			html += '	<th>收款户名</th>';
			html += '	<th>开户银行</th>';
			html += '	<th>状态</th>';
			html += '	<th>备注</th>';
			html += '	<th>操作</th>';
			html += '</tr>';
			if(accoutList.length > 0){
				for(var k = 0; k < accoutList.length; k++){
					var accoutObj = accoutList[k];
					var numberCode = accoutObj.numberCode;
					var accountNumber = accoutObj.accountNumber;
					var cashAccount = accoutObj.cashAccount;
					var bankOfDeposit = accoutObj.bankOfDeposit;
					var isEnable = accoutObj.isEnable;
					var remark = accoutObj.remark;
					
					html += '<tr id="old_'+ numberCode +'">';
					html += '   <td>';
					html += '		<input id="accountNumber_'+ numberCode +'" style="width:170px;" value="'+ accountNumber +'"/>';
					html += '   </td>';
					html += '   <td>';
					html += '		<input id="cashAccount_'+ numberCode +'" style="width:100px;" value="'+ cashAccount +'"/>';
					html += '   </td>';
					html += '   <td>';
					html += '		<input id="bankOfDeposit_'+ numberCode +'" style="width:170px;" value="'+ bankOfDeposit +'"/>';
					html += '   </td>';
					html += '	<td>';
					if(isEnable == 1){
						html += ' 	<input value="1" type="radio" name="enable_'+ numberCode +'" checked="checked"/>启用 ';
						html += ' 	<input value="0" type="radio" name="enable_'+numberCode +'"/>关闭';
						html += ' 	<input type="hidden" id="isEnable_'+ numberCode +'" value="1"/>';
					}else{
						html += ' 	<input value="1" type="radio" name="enable_'+ numberCode +'"/>启用 ';
						html += ' 	<input value="0" type="radio" name="enable_'+numberCode +'" checked="checked"/>关闭';
						html += ' 	<input type="hidden" id="isEnable_'+ numberCode +'" value="0"/>';
					}
					html += '	</td>';
					html += '   <td>';
					html += '		<input id="remark_'+ numberCode +'" style="width:130px;" value="'+ remark +'"/>';
					html += '   </td>';	
					html += '	<td>';
					html += '	  <img id="delete_old_'+ numberCode +'" src="../image/other/delete.png"';
					html += '     alt="点我删除供应商账户" title="点我删除供应商账户" style="cursor:pointer;">';
					html += '	</td>';
					html += '</tr>';
				}
			}
			html += '<tbody id="accountTbody"></tbody>';
			$("#accountTable").html(html);
			
			$("#addImg").click(function(){
				addAccountHtml();
			});
			
			if(accoutList.length > 0){
				for(var k = 0; k < accoutList.length; k++){
					var accoutObj = accoutList[k];
					var numberCode = accoutObj.numberCode;
					
					xyzTextbox("accountNumber_"+numberCode);
					xyzTextbox("cashAccount_"+numberCode);
					xyzTextbox("bankOfDeposit_"+numberCode);
					xyzTextbox("remark_"+numberCode);
					
					$("#accountNumber_"+numberCode).textbox({
						required : true
					});
					$("#cashAccount_"+numberCode).textbox({
						required : true
					});
					$("#bankOfDeposit_"+numberCode).textbox({
						required : true
					});
					
					$("input[type='radio'][name='enable_"+ numberCode +"']").click(function(){
						var oldValue = $("#isEnable_"+numberCode).val();
						var newValue = $("input[type='radio'][name='enable_"+ numberCode +"']:checked").val();
						
						if(oldValue != newValue){
							$("#isEnable_"+numberCode).val(newValue);
						}
					});
					
					$("#delete_old_"+numberCode).click(function(){
						deleteAccountHtml("old", numberCode);
					});
				}
			}else{
				addAccountHtml();
			}
			
		}
	});
	
}

function addAccountHtml(){
	
	var length = $("tr[id^='add_']").length;
	var index = 0;
	if(length > 0){
		index = parseInt( $("tr[id^='add_']").last().prop('id').split('_')[1] );
		parseInt(index);
		index += 1;
	}
	
	var html = '<tr id="add_'+ index +'">';
	html += '   <td>';
	html += '		<input id="accountNumber_'+ index +'" style="width:170px;" />';
	html += '   </td>';
	html += '   <td>';
	html += '		<input id="cashAccount_'+ index +'" style="width:100px;" />';
	html += '   </td>';
	html += '   <td>';
	html += '		<input id="bankOfDeposit_'+ index +'" style="width:170px;" />';
	html += '   </td>';
	html += '	<td>';
	html += ' 		<input value="1" type="radio" name="enable_'+ index +'" checked="checked"/>启用 ';
	html += ' 		<input value="0" type="radio" name="enable_'+index +'"/>关闭';
	html += ' 		<input type="hidden" id="isEnable_'+ index +'" value="1"/>';
	html += '	</td>';
	html += '   <td>';
	html += '		<input id="remark_'+ index +'" style="width:130px;" />';
	html += '   </td>';	
	html += '	<td>';
	html += '	  <img id="delete_'+ index +'" src="../image/other/delete.png"';
	html += '     alt="点我删除供应商账户" title="点我删除供应商账户" style="cursor:pointer;">';
	html += '	</td>';
	html += '</tr>';
	$("#accountTbody").append(html);
	
	xyzTextbox("accountNumber_"+index);
	xyzTextbox("cashAccount_"+index);
	xyzTextbox("bankOfDeposit_"+index);
	xyzTextbox("remark_"+index);
	
	$("#accountNumber_"+index).textbox({
		required : true
	});
	$("#cashAccount_"+index).textbox({
		required : true
	});
	$("#bankOfDeposit_"+index).textbox({
		required : true
	});
	
	$("input[type='radio'][name='enable_"+ index +"']").click(function(){
		var oldValue = $("#isEnable_"+index).val();
		var newValue = $("input[type='radio'][name='enable_"+ index +"']:checked").val();
		
		if(oldValue != newValue){
			$("#isEnable_"+index).val(newValue);
		}
	});
	
	$("#delete_"+index).click(function(){
		deleteAccountHtml("add",index);
	});
	
}

function deleteAccountHtml(type, index){
	
	var oldLength = $("tr[id^='old_']").length;
	var length = $("tr[id^='add_']").length;
	var sum = oldLength+length;
	
	if(sum > 1){
		if("add"==type){
			$("#add_"+index).remove();
		}else{
			$("#old_"+index).remove();
		}
	}else{
		top.$.messager.alert("温馨提示","至少保留一个账户信息!","info");
		return false;
	}
	
}

function setAccountSubmit(provider){
	if(!$("form").form('validate')){
		return false;
	}
	
	var accountJson = [];
	
	var countTr = 0;
	/*原有的账户信息*/
	var oldLength = $("tr[id^='old_']").length;
	countTr = countTr + oldLength;
	if(oldLength > 0){
		$("tr[id^='old_']").each(function(){
			var index = $(this).prop('id').split('_')[1];
			
			var accountNumber = $("#accountNumber_"+index).val();
			var cashAccount = $("#cashAccount_"+index).val();
			var bankOfDeposit = $("#bankOfDeposit_"+index).val();
			var isEnable = $("#isEnable_").val();
			var remark = $("#remark_"+index).val();
			
			var tempJson = {
				numberCode : index,
				accountNumber : accountNumber,
				cashAccount : cashAccount,
				bankOfDeposit : bankOfDeposit,
				isEnable : isEnable,
				remark : remark
			};
				
			accountJson[accountJson.length] = tempJson;
		});
	}
	
	var addLength = $("tr[id^='add_']").length;
	countTr = countTr + addLength;
	if(addLength > 0){
		$("tr[id^='add_']").each(function(){
			var index = $(this).prop('id').split('_')[1];
			
			var accountNumber = $("#accountNumber_"+index).val();
			var cashAccount = $("#cashAccount_"+index).val();
			var bankOfDeposit = $("#bankOfDeposit_"+index).val();
			var isEnable = $("#isEnable_").val();
			var remark = $("#remark_"+index).val();
			
			var tempJson = {
				numberCode : "",
				accountNumber : accountNumber,
				cashAccount : cashAccount,
				bankOfDeposit : bankOfDeposit,
				isEnable : isEnable,
				remark : remark
			};
				
			accountJson[accountJson.length] = tempJson;
		});
	}
	
	accountJson = JSON.stringify(accountJson);
	
	if(countTr < 1){
		top.$.messager.alert("提示", "至少有一条数据!", "info");
		return false;
	}
	
	$.ajax({
		url : "../ProviderWS/editAccountList.do",
		type : "POST",
		data : {
			provider : provider,
			accountJson : accountJson
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#dialogFormDiv_setAccount").dialog("destroy");
				top.$.messager.alert("提示","操作成功!","info");
				$("#providerManagerTable").datagrid("reload");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function setJoiner(provider, name_cn){

	var joinerList = null;
	$.ajax({
		url : "../JoinerWS/queryJoinerListByprovider.do",
		type : "POST",
		data : {
			provider : provider
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				joinerList = data.content;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
	xyzdialog({
		dialog : 'dialogFormDiv_setJoiner',
		title : '设置对接人信息',
	    href : '../jsp_resources/setJoiner.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				setJoinerSubmit(provider);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_setJoiner").dialog("destroy");
			}
		}],
		onLoad : function(){
			var html = '';
			html += '<caption>';
			html += ' 供应商【'+ name_cn +'】对接人信息&nbsp;&nbsp;&nbsp;';
			html += ' <img id="addImg" src="../image/other/addPage.gif"';
			html += '  alt="点我新增对接人信息" title="点我新增对接人信息" style="cursor:pointer;">';
			html += '</caption>';
			html += '<tr>';
			html += '	<th>姓名</th>';
			html += '	<th>电话</th>';
			html += '	<th>微信号</th>';
			html += '	<th>QQ号</th>';
			html += '	<th>备注</th>';
			html += '	<th>操作</th>';
			html += '</tr>';
			if(joinerList.length > 0){
				for(var k = 0; k < joinerList.length; k++){
					var joinerObj = joinerList[k];
					var numberCode = joinerObj.numberCode;
					var nameCn = joinerObj.nameCn;
					var phone = joinerObj.phone;
					var weChat = joinerObj.weChat;
					var remark = joinerObj.remark;
					
					html += '<tr id="old_'+ numberCode +'">';
					html += '   <td>';
					html += '		<input id="nameCn_'+ numberCode +'" style="width:120px;" value="'+ nameCn +'"/>';
					html += '   </td>';
					html += '   <td>';
					html += '		<input id="phone_'+ numberCode +'" style="width:120px;" value="'+ phone +'"/>';
					html += '   </td>';
					html += '   <td>';
					html += '		<input id="weChat_'+ numberCode +'" style="width:120px;" value="'+ weChat +'"/>';
					html += '   </td>';
					html += '   <td>';
					html += '		<input id="qq_'+ numberCode +'" style="width:120px;" value="'+ weChat +'"/>';
					html += '   </td>';
					html += '   <td>';
					html += '		<input id="remark_'+ numberCode +'" style="width:130px;" value="'+ remark +'"/>';
					html += '   </td>';	
					html += '	<td>';
					html += '	  <img id="delete_old_'+ numberCode +'" src="../image/other/delete.png"';
					html += '     alt="点我删除对接人信息" title="点我对接人信息" style="cursor:pointer;">';
					html += '	</td>';
					html += '</tr>';
				}
			}
			html += '<tbody id="joinerTbody"></tbody>';
			$("#joinerTable").html(html);
			
			$("#addImg").click(function(){
				addJoinerHtml();
			});
			
			if(joinerList.length > 0){
				for(var k = 0; k < joinerList.length; k++){
					var joinerObj = joinerList[k];
					var numberCode = joinerObj.numberCode;
					
					xyzTextbox("nameCn_"+numberCode);
					xyzTextbox("phone_"+numberCode);
					xyzTextbox("weChat_"+numberCode);
					xyzTextbox("qq_"+numberCode);
					xyzTextbox("remark_"+numberCode);
					
					$("#nameCn_"+numberCode).textbox({
						required : true
					});
					/*$("#phone_"+numberCode).textbox({
						required : true
					});
					$("#weChat_"+numberCode).textbox({
						required : true
					});
					$("#qq_"+numberCode).textbox({
						required : true
					});*/
					
					$("#delete_old_"+numberCode).click(function(){
						deleteJoinerHtml("old", numberCode);
					});
				}
			}else{
				addJoinerHtml();
			}
			
		}
	});
}

function addJoinerHtml(){
	var length = $("tr[id^='add_']").length;
	var index = 0;
	if(length > 0){
		index = parseInt( $("tr[id^='add_']").last().prop('id').split('_')[1] );
		parseInt(index);
		index += 1;
	}
	
	var html = '<tr id="add_'+ index +'">';
	html += '   <td>';
	html += '		<input id="nameCn_'+ index +'" style="width:120px;" />';
	html += '   </td>';
	html += '   <td>';
	html += '		<input id="phone_'+ index +'" style="width:120px;" />';
	html += '   </td>';
	html += '   <td>';
	html += '		<input id="weChat_'+ index +'" style="width:120px;" />';
	html += '   </td>';
	html += '   <td>';
	html += '		<input id="qq_'+ index +'" style="width:120px;" />';
	html += '   </td>';
	html += '   <td>';
	html += '		<input id="remark_'+ index +'" style="width:130px;" />';
	html += '   </td>';	
	html += '	<td>';
	html += '	  <img id="delete_'+ index +'" src="../image/other/delete.png"';
	html += '     alt="点我删除对接人信息" title="点我对接人信息" style="cursor:pointer;">';
	html += '	</td>';
	html += '</tr>';
	$("#joinerTbody").append(html);
	
	xyzTextbox("nameCn_"+index);
	xyzTextbox("phone_"+index);
	xyzTextbox("weChat_"+index);
	xyzTextbox("qq_"+index);
	xyzTextbox("remark_"+index);
	
	$("#nameCn_"+index).textbox({
		required : true
	});
	/*$("#phone_"+index).textbox({
		required : true
	});
	$("#weChat_"+index).textbox({
		required : true
	});
	$("#qq_"+index).textbox({
		required : true
	});*/
	
	$("#delete_"+index).click(function(){
		deleteJoinerHtml("add", index);
	});
	
}

function deleteJoinerHtml(type, index){
	
	var oldLength = $("tr[id^='old_']").length;
	var length = $("tr[id^='add_']").length;
	var sum = oldLength+length;
	
	if(sum > 1){
		if("add" == type){
			$("#add_"+index).remove();
		}else{
			$("#old_"+index).remove();
		}
	}else{
		top.$.messager.alert("温馨提示","至少保留一个对接人信息!","info");
		return false;
	}
	
}

function setJoinerSubmit(provider){
	if(!$("form").form('validate')){
		return false;
	}
	
	var joinerJson = [];
	
	var countTr = 0;
	/*原有的对接人信息*/
	var oldLength = $("tr[id^='old_']").length;
	countTr = countTr + oldLength;
	if(oldLength > 0){
		$("tr[id^='old_']").each(function(){
			var index = $(this).prop('id').split('_')[1];
			
			var nameCn = $("#nameCn_"+index).val();
			var phone = $("#phone_"+index).val();
			var weChat = $("#weChat_"+index).val();
			var qq = $("#qq_"+index).val();
			var remark = $("#remark_"+index).val();
			
			var tempJson = {
				numberCode : index,
				nameCn : nameCn,
				phone : phone,
				weChat : weChat,
				qq : qq,
				remark : remark
			};
				
			joinerJson[joinerJson.length] = tempJson;
		});
	}
	
	var addLength = $("tr[id^='add_']").length;
	countTr = countTr + addLength;
	if(addLength > 0){
		$("tr[id^='add_']").each(function(){
			var index = $(this).prop('id').split('_')[1];
			
			var nameCn = $("#nameCn_"+index).val();
			var phone = $("#phone_"+index).val();
			var weChat = $("#weChat_"+index).val();
			var qq = $("#qq_"+index).val();
			var remark = $("#remark_"+index).val();
			
			var tempJson = {
				numberCode : "",
				nameCn : nameCn,
				phone : phone,
				weChat : weChat,
				qq : qq,
				remark : remark
			};
			
			joinerJson[joinerJson.length] = tempJson;
		});
	}
	
	joinerJson = JSON.stringify(joinerJson);
	
	if(countTr < 1){
		top.$.messager.alert("提示", "至少有一条数据!", "info");
		return false;
	}
	
	$.ajax({
		url : "../ProviderWS/editJoinerList.do",
		type : "POST",
		data : {
			provider : provider,
			joinerJson : joinerJson
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#dialogFormDiv_setJoiner").dialog("destroy");
				top.$.messager.alert("提示","操作成功!","info");
				$("#providerManagerTable").datagrid("reload");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}