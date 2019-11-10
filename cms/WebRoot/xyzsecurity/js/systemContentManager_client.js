$(document).ready(function() {
	initTable();
	xyzTextbox("nameKey");
	xyzTextbox("nameCn");
	$("#systemContentQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = [];
	if(xyzControlButton("buttonCode_s20160418110602")){
		toolbar[toolbar.length]={
				text: '新增配置信息',
				iconCls: 'icon-add',
				handler: function(){addSystemContent();}
		};
		toolbar[toolbar.length]='-';
	}
	if(xyzControlButton("buttonCode_s20160418110603")){
		toolbar[toolbar.length]={
				text: '编辑配置信息',
				iconCls: 'icon-add',
				handler: function(){editSystemContent();}
		};
		toolbar[toolbar.length]='-';
	}
	if(xyzControlButton("buttonCode_s20160418110604")){
		toolbar[toolbar.length]={
				text: '删除配置信息',
				iconCls: 'icon-add',
				handler: function(){deleteSystemContent();}
		};
		toolbar[toolbar.length]='-';
	}
	
	xyzgrid({
		table : 'systemContentManagerTable',
		url:'../SystemContentClientWS/querySystemContentList.do',
		toolbar : toolbar,
		idField : 'numberCode',
		title : '机构列表',
		singleSelect : false,
		columns:[[
		    {field:'checkboxTemp',checkbox:true},
		    {field:'nameKey',title:'英文名称',width:'200',
		    	formatter: function(value,row,index){
					return xyzGetDiv(value, 0, 100);
				}
		    },
		    {field:'nameCn',title:'中文名称',width:'300',
		    	formatter: function(value,row,index){
					return xyzGetDiv(value, 0, 100);
				}
		    },
		    {field:'value',title:'值',width:580,
		    	formatter: function(value,row,index){
					return xyzGetDiv(value, 0, 100);
				}
		    },
			{field:'remark',title:'备注',hidden:true},
			{field:'alterDate',title:'修改时间',hidden:true},
			{field:'addDate',title:'添加时间',hidden:true}
		]]
	});
}

function loadTable(){
	$("#systemContentManagerTable").datagrid('load',{
		nameKey : $("#nameKey").val(),
		nameCn : $("#nameCn").val()
	});
}

function addSystemContent(){
	
	xyzdialog({
		dialog:'dialogFormDiv_addSystemContent',
		title:'新增系统配置',
		fit:false,
		width:600,
		height:400,
		href:'../xyzsecurity/addSystemContent.html',
		buttons:[{
			text:'确定',
			handler:function(){
				addSystemContentSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addSystemContent").dialog("destroy");
			}
		}]
	});
	$("#dialogFormDiv_addSystemContent").css("backgroundColor","#FFFFFF");
}

function editSystemContent(){
	var systemContents = $("#systemContentManagerTable").datagrid("getChecked");
	if(systemContents.length != 1){
		top.$.messager.alert("提示","请先选中单个对象！","info");
		return;
	}
	var row = systemContents[0];
	xyzdialog({
		dialog : 'dialogFormDiv_editSystemContent',
		title : '编辑配置【'+row.nameCn+'】',
		fit:false,
		width:600,
		height:400,
	    href : '../xyzsecurity/editSystemContent.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				editSystemContentSubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editSystemContent").dialog("destroy");
			}
		}],
		onLoad : function(){
			$("#numberCodeForm").val(row.numberCode);
			$("#nameCnForm").val(row.nameCn);
			$("#nameKeyForm").val(row.nameKey);
			$("#valueForm").val(row.value);
			$("#remarkForm").val(row.remark);
		}
	});
	$("#dialogFormDiv_addSystemContent").css("backgroundColor","#FFFFFF");
}

function addSystemContentSubmit(){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").val();
	var nameKey = $("#nameKeyForm").val();
	var value = $("#valueForm").val();
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../SystemContentClientWS/addSystemContent.do",
		type : "POST",
		data : {
			nameKey : nameKey,
			nameCn : nameCn,
			value : value,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#systemContentManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功！","info");
				$("#dialogFormDiv_addSystemContent").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editSystemContentSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").val();
	var nameKey = $("#nameKeyForm").val();
	var value = $("#valueForm").val();
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../SystemContentClientWS/editSystemContent.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			nameKey : nameKey,
			nameCn : nameCn,
			value : value,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#systemContentManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功！","info");
				$("#dialogFormDiv_editSystemContent").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deleteSystemContent(){
	var systemContents = $.map($("#systemContentManagerTable").datagrid("getChecked"),function(p){return p.numberCode;}).join(",");
	if(xyzIsNull(systemContents)){
		top.$.messager.alert("提示","请先选中需要删除的对象！","info");
		return;
	}
	
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		if(r){
			$.ajax({
				url : "../SystemContentClientWS/deleteSystemContent.do",
				type : "POST",
				data : {
					numberCodes : systemContents
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						$("#systemContentManagerTable").datagrid("reload");
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