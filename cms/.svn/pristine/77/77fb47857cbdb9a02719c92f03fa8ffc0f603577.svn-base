$(document).ready(function() {
	initTable();
	xyzTextbox("numberCode");
	xyzTextbox("nameCn");
	$("#positionQueryButton").click(function(){
		loadTable();
	});
});

function initTable(){
	
	var toolbar = [];
	
	toolbar[toolbar.length] = {
		iconCls: 'icon-add',
		text : '新建',
		handler: function(){
			var title = $(this).text();
			addPosition(title);
		}
	};
	
	toolbar[toolbar.length] = '-';
	
	toolbar[toolbar.length] = {
			iconCls: 'icon-edit',
			text : '编辑',
			handler: function(){editPosition();}
		};
	toolbar[toolbar.length] = '-';
	
	toolbar[toolbar.length] = {
		iconCls: 'icon-remove',
		text : '删除',
		handler: function(){deletePosition();}
	};
	
	xyzgrid({
		table : 'positionManagerTable',
		title:'岗位列表',
		url:'../AdminPositionWS/queryPositionList.do',
		toolbar : toolbar,
		singleSelect : false,
		columns:[[    
		    {field:'checkboxTemp',checkbox:true},  
		    {field:'numberCode',title:'岗位编号',width:180,halign:'center'},
			{field:'nameCn',title:'岗位名称',width:200,halign:'center',
		    	formatter:function(value, row, index) {
					return xyzGetDiv(value, 0, 100);
				}
			},
			{field:'countUser',title:'使用人数',halign:'center'},
			{field:'operTemp',title:'操作',halign:'center',
				formatter: function(value,row,index){
					var buttonTemp1 = xyzGetA("权限列表","managerPositionButtonList", [row.numberCode,row.nameCn], "点我设置权限列表", "blue");
					var buttonTemp2 = xyzGetA("权限树","managerPositionButtonTree", [row.numberCode,row.nameCn], "点我设置权限树", "blue");
					return buttonTemp1+" "+buttonTemp2;
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
			{field:'remark',title:'备注',width:390,halign:'center',
		    	formatter: function(value,row,index){
					return xyzGetDiv(value, 0, 100);
				}
			}
		]]
	});
}

function loadTable(){
	var numberCode = $("#numberCode").val();
	var nameCn = $("#nameCn").val();
	$("#positionManagerTable").datagrid('load',{
		numberCode : numberCode,
		nameCn : nameCn
	});
}

function addPosition(title){
	xyzdialog({
		dialog : 'dialogFormDiv_addPosition',
		title : '新增岗位',
	    href : '../xyzsecurity/a_addPosition.html',
	    fit : false,
	    width : 450, 
	    height : 450,
	    buttons:[{
			text:'确定',
			handler:function(){
				addPositionSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addPosition").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("numberCodeForm");
			xyzTextbox("nameCnForm");
		}
	});
}

function addPositionSubmit(){
	if(!$("form").form('validate')){
		return false;
	}
	
	var numberCode = $("#numberCodeForm").val();
	var nameCn = $("#nameCnForm").val();
	var remark = $("#remarkForm").val();

	$.ajax({
		url : "../AdminPositionWS/addPosition.do",
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
				$("#positionManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功！","info");
				$("#dialogFormDiv_addPosition").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editPosition(){
	var positions = $("#positionManagerTable").datagrid("getChecked");
	if(positions.length != 1){
		top.$.messager.alert("提示","请先选中单个对象！","info");
		return;
	}
	var row = positions[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editPosition',
		title : '编辑岗位',
	    href : '../xyzsecurity/a_editPosition.html',
	    fit : false,
	    width : 450,
	    height : 450,
	    buttons:[{
			text:'确定',
			handler:function(){
				editPositionSubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editPosition").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("nameCnForm");
			
			$("#numberCodeForm").html(row.numberCode);
			$("#nameCnForm").textbox("setValue",row.nameCn);
			$("#remarkForm").val(row.remark);
		}
	});
}

function editPositionSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").val();
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../AdminPositionWS/editPosition.do",
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
				$("#positionManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功！","info");
				$("#dialogFormDiv_editPosition").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function managerPositionButtonList(numberCode,nameCn){
	xyzdialog({
		dialog : 'dialogFormDiv_managerPositionButtonList',
		title : "管理岗位权限["+nameCn+"]",
	    content : "<iframe id='dialogFormDiv_managerPositionButtonListIframe' frameborder='0' name='"+numberCode+"'></iframe>",
	    buttons:[{
			text:'返回',
			handler:function(){
				$("#dialogFormDiv_managerPositionButtonList").dialog("destroy");
			}
		}]
	});
	var tempWidth = $("#dialogFormDiv_managerPositionButtonList").css("width");
	var tempHeight = $("#dialogFormDiv_managerPositionButtonList").css("height");
	var tempWidth2 = parseInt(tempWidth.split("px")[0]);
	var tempHeight2 = parseInt(tempHeight.split("px")[0]);
	$("#dialogFormDiv_managerPositionButtonListIframe").css("width",(tempWidth2-50)+"px");
	$("#dialogFormDiv_managerPositionButtonListIframe").css("height",(tempHeight2-50)+"px");
	$("#dialogFormDiv_managerPositionButtonListIframe").attr("src","../xyzsecurity/a_positionButtonManager.html");
}

function managerPositionButtonTree(numberCode,nameCn){
	xyzdialog({
		dialog : 'dialogFormDiv_managerPositionButtonTree',
		title : "管理岗位权限["+nameCn+"]",
	    content : "<iframe id='dialogFormDiv_managerPositionButtonTreeIframe' frameborder='0' name='"+numberCode+"'></iframe>",
	    buttons:[{
			text:'返回',
			handler:function(){
				$("#dialogFormDiv_managerPositionButtonTree").dialog("destroy");
			}
		}]
	});
	var tempWidth = $("#dialogFormDiv_managerPositionButtonTree").css("width");
	var tempHeight = $("#dialogFormDiv_managerPositionButtonTree").css("height");
	var tempWidth2 = parseInt(tempWidth.split("px")[0]);
	var tempHeight2 = parseInt(tempHeight.split("px")[0]);
	$("#dialogFormDiv_managerPositionButtonTreeIframe").css("width",(tempWidth2-50)+"px");
	$("#dialogFormDiv_managerPositionButtonTreeIframe").css("height",(tempHeight2-50)+"px");
	$("#dialogFormDiv_managerPositionButtonTreeIframe").attr("src","../xyzsecurity/a_positionButtonTree.html");
}

function deletePosition(){
	var positions = $.map($("#positionManagerTable").datagrid("getChecked"),function(p){return p.numberCode;}).join(",");
	if(xyzIsNull(positions)){
		top.$.messager.alert("提示","请先选中需要删除的对象！","info");
		return;
	}
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
	if(r){
	 $.ajax({
		url : "../AdminPositionWS/deletePosition.do",
		type : "POST",
		data : {
			positions : positions
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#positionManagerTable").datagrid("reload");
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