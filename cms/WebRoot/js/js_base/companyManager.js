$(document).ready(function(){
	xyzTextbox("nameCn");
	
	initTable();
	
	$("#companyQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = []; 
	if(xyzControlButton("buttonCode_x20161207100202")){
		toolbar[toolbar.length]={
				text: '新增',  
				border:'1px solid #bbb',  
				iconCls: 'icon-add', 
				handler: function(){
					addCompanyButton();
			}
		};
	}
	
	if(xyzControlButton("buttonCode_x20161207100203")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
			text: '编辑',  
			border:'1px solid #bbb',  
			iconCls: 'icon-edit', 
			handler: function(){
				editCompanyButton();
			}
		};
	}	
	
	if(xyzControlButton("buttonCode_x20161207100204")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
				text: '删除',  
				border:'1px solid #bbb',  
				iconCls: 'icon-remove', 
				handler: function(){
					deleteCompanyButton();
				}
		};
	}
	
	xyzgrid({
		table : 'companyManagerTable',
		title : "邮轮公司列表",
		url:'../CompanyWS/queryCompanyList.do',
		toolbar:toolbar,
		singleSelect : false, 
		idField : 'numberCode',
		columns : [[
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'邮轮公司编号',hidden:true},
			{field:'nameCn',title:'公司名称',width:150,halign:'center',
			   formatter:function(value ,row ,index){
				  return xyzGetDiv(value,0,150);
			   }
			},
			{field:'nameEn',title:'英文名',width:120,halign:'center',
			   formatter:function(value ,row ,index){
				  return xyzGetDiv(value,0,120);
			   }
			},
			{field:'websiteAddress',title:'官网地址',width:200,halign:'center',
				   formatter:function(value ,row ,index){
					 var  btnTemp = xyzGetA(value,"window.open",[value],"点我查看官网","");
					 return btnTemp ;
				   }
				},
		    {field:'addDate',title:'添加时间',hidden:true,halign:'center',
			   formatter:function(value ,row ,index){
				  return xyzGetDivDate(value);
			   }
		    },
		    {field:'alterDate',title:'修改时间',width:85,halign:'center',
			   formatter:function(value ,row ,index){
				  return xyzGetDivDate(value);
			   }
		    },
		    {field:'remark',title:'备注',width:200,halign:'center',
			   formatter:function(value ,row ,index){
				   return xyzGetDiv(value ,0 ,200);
			   }
		    }
		]]
	});
}

function loadTable(){
	var nameCn = $("#nameCn").textbox("getValue");

	$("#companyManagerTable").datagrid('load', {
		nameCn : nameCn
	});
}

function addCompanyButton(){
	xyzdialog({
		dialog : 'dialogFormDiv_addCompanyButton',
		title : '新增邮轮公司',
	    href : '../jsp_base/addCompany.html',
	    fit : false,  
	    width: 450,
	    height : 450,
	    buttons:[{
			text:'确定',
			handler:function(){
				addCompanySubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addCompanyButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("nameCnForm");
			xyzTextbox("nameEnForm");
			xyzTextbox("websiteAddressForm");
		}
	});
}

function addCompanySubmit() {
	if(!$("form").form('validate')){
		return false;
	}

	var nameCn = $("#nameCnForm").textbox("getValue");
	var nameEn = ($("#nameEnForm").textbox("getValue")).replace(/\s+/g,' ');
	var websiteAddress = $("#websiteAddressForm").textbox("getValue");
	var remark = $("#remarkForm").val();
	
	var nameEnReg =  /^([A-za-z]+[\s]?)+[A-za-z\s]$/;
	if(!xyzIsNull(nameEn) && !nameEnReg.test(nameEn)){
		top.$.messager.alert("提示","英文名称只能输入字母、空格!","info");
		return false;
	}
	
	$.ajax({
		url : "../CompanyWS/addCompany.do",
		type : "POST",
		data : {
			nameCn : nameCn,
			nameEn : nameEn,
			websiteAddress : websiteAddress,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#companyManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_addCompanyButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editCompanyButton(){
	var companys = $("#companyManagerTable").datagrid("getChecked");
	
	if(companys.length != 1){
		top.$.messager.alert("提示","请先选中单个对象!","info");
		return;
	}
	var row = companys[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editCompanyButton',
		title : '编辑【'+ row.nameCn +'】',
	    href : '../jsp_base/editCompany.html',
	    fit : false,  
	    width: 450,
	    height : 450,
	    buttons:[{
			text:'确定',
			handler:function(){
				editCompanySubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editCompanyButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("nameCnForm");
			xyzTextbox("nameEnForm");
			xyzTextbox("websiteAddressForm");
			
			$("#nameCnForm").textbox('setValue',row.nameCn);
			$("#nameEnForm").textbox('setValue',row.nameEn);
			$("#websiteAddressForm").textbox('setValue',row.websiteAddress);
			$("#remarkForm").val(row.remark);
			
			$('#nameCnForm').textbox({    
			    required:true 
			});
		}
	});
}

function editCompanySubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").textbox("getValue");
	var nameEn = $("#nameEnForm").textbox("getValue");
	var websiteAddress = $("#websiteAddressForm").textbox("getValue");
	var remark = $("#remarkForm").val();
	
	var nameEnReg = /^([A-za-z]+[\s]?)+[A-za-z\s]$/;
	if(!xyzIsNull(nameEn) && !nameEnReg.test(nameEn)){
		top.$.messager.alert("提示","英文名称只能输入字母、空格!","info");
		return false;
	}
	
	$.ajax({
		url : "../CompanyWS/editCompany.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			nameCn : nameCn,
			nameEn : nameEn,
			websiteAddress : websiteAddress,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#companyManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_editCompanyButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deleteCompanyButton(){
	var companys = $.map($("#companyManagerTable").datagrid("getChecked"),
		function(p){
	       return p.numberCode;
	    }
	).join(",");
	if(xyzIsNull(companys)){
		top.$.messager.alert("提示","请先选中需要删除的对象!","info");
		return;
	}
	
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		if(r){
			$.ajax({
				url : "../CompanyWS/deleteCompany.do",
				type : "POST",
				data : {
					numberCodes : companys
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						$("#companyManagerTable").datagrid("reload");
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