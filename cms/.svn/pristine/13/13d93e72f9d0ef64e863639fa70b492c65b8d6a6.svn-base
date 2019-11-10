$(document).ready(function() {
	initTable();
	xyzTextbox("nameCn");
	$("#possessorQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = [];
	if(xyzControlButton("buttonCode_s20150708143402")){
		toolbar[toolbar.length]={
				text: '新增',
				iconCls: 'icon-add',
				handler: function(){addPossessor();}
		};
	}
	if(xyzControlButton("buttonCode_s20150708143403")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
				text: '编辑',
				iconCls: 'icon-edit',
				handler: function(){editPossessor();}
		};
	}
	if(xyzControlButton("buttonCode_s20150708143404")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
				text: '删除',
				iconCls: 'icon-remove',
				handler: function(){deletePossessor();}
		};
		toolbar[toolbar.length]='-';
	}
	
	xyzgrid({
		table : 'possessorManagerTable',
		url : '../PossessorWS/queryPossessorList.do',
		toolbar : toolbar,
		title : '机构列表',
		singleSelect : false,
		columns:[[
		    {field:'checkboxTemp',checkbox:true},
		    {field:'nameCn',title:'机构名称',width:199,halign:'center',
		    	formatter: function(value,row,index){
					return xyzGetDiv(value, 0, 100);
				}
		    },
		    {field:'resourcesOper',title:'资源授权',halign:'center',
				formatter: function(value,row,index){
					var button = "";
					if(xyzControlButton("buttonCode_y20161211160000")){
						button = xyzGetA("资源授权","possessorResourceManager", [row.numberCode,row.nameCn], "点我设置资源", "blue");
					}
					return button;
				}
			},
			{field:'alterDate',title:'修改时间',halign:'center',
				formatter: function(value,row,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'addDate',title:'添加时间',halign:'center',
				formatter: function(value,row,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'remark',title:'备注',width:410,halign:'center',
				formatter: function(value,row,index){
					return xyzGetDiv(value, 0, 100);
				}
			}
		]]
	});
}

function loadTable(){
	var nameCn = $("#nameCn").val();
	
	$("#possessorManagerTable").datagrid('load',{
		nameCn : nameCn
	});
}

function addPossessor(){
	xyzdialog({
		dialog : 'dialogFormDiv_addPossessor',
		title : "新增机构",
		fit : false,
		width : 450,
		height : 450,
	    href : '../xyzsecurity/addPossessor.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				addPossessorSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addPossessor").dialog("destroy");
			}
		}],
		onLoad:function(){
			xyzTextbox("nameCnForm");
		}
	});
}

function addPossessorSubmit(){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").val();
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../PossessorWS/addPossessor.do",
		type : "POST",
		data : {
			nameCn : nameCn,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#possessorManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功！","info");
				$("#dialogFormDiv_addPossessor").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editPossessor(){
	var possessors = $("#possessorManagerTable").datagrid("getChecked");
	if(possessors.length != 1){
		top.$.messager.alert("提示","请先选中单个对象！","info");
		return;
	}
	var row = possessors[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editPossessor',
		title : "编辑机构",
		fit : false,
		width : 450,
		height : 450,
	    href : '../xyzsecurity/editPossessor.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				editPossessorSubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editPossessor").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("nameCnForm");
			$("#nameCnForm").textbox("setValue",row.nameCn);
			$("#remarkForm").val(row.remark);
		}
	});
}

function editPossessorSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").val();
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../PossessorWS/editPossessor.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			nameCn : nameCn,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#possessorManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功！","info");
				$("#dialogFormDiv_editPossessor").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deletePossessor(){
	var possessors = $.map($("#possessorManagerTable").datagrid("getChecked"),function(p){return p.numberCode;}).join(",");
	if(xyzIsNull(possessors)){
		top.$.messager.alert("提示","请先选中需要删除的对象！","info");
		return;
	}
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
	 if(r){
	  $.ajax({
		url : "../PossessorWS/deletePossessor.do",
		type : "POST",
		data : {
			numberCodes : possessors
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#possessorManagerTable").datagrid("reload");
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

function possessorResourceManager(numberCode ,nameCn ){
	xyzdialog({
		dialog : 'dialogFormDiv_possessorResourceManager',
		title : "管理["+nameCn+"]机构资源",
	    content : "<iframe id='dialogFormDiv_possessorResourceManagerIframe' frameborder='0'></iframe>",
	    buttons:[{
			text:'返回',
			handler:function(){
				$("#dialogFormDiv_possessorResourceManager").dialog("destroy");
			}
		}]
	});
	var tempWidth = $("#dialogFormDiv_possessorResourceManager").css("width");
	var tempHeight = $("#dialogFormDiv_possessorResourceManager").css("height");
	var tempWidth2 = parseInt(tempWidth.split("px")[0]);
	var tempHeight2 = parseInt(tempHeight.split("px")[0]);
	$("#dialogFormDiv_possessorResourceManagerIframe").css("width",(tempWidth2-50)+"px");
	$("#dialogFormDiv_possessorResourceManagerIframe").css("height",(tempHeight2-50)+"px");
	$("#dialogFormDiv_possessorResourceManagerIframe").attr("src","../jsp_base/possessorResourceManager.html?possessor="+numberCode);
}