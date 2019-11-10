$(document).ready(function() {
	
	position = $("#dialogFormDiv_managerPositionButtonListIframe",window.parent.document).attr("name");
	$("#currentPosition").val(position);
	
	initTable();
	xyzTextbox("nameCn");
	xyzTextbox("functionNameCn");
	
});

function initTable(){
	
	var toolbarTrue = [];
	
	toolbarTrue[toolbarTrue.length] = {
		iconCls: 'icon-search',
		text : '查询已选中操作',
		handler: function(){
			apiQueryTrue();
		}
	};
	toolbarTrue[toolbarTrue.length] = '-';
	toolbarTrue[toolbarTrue.length] = {
			iconCls: 'icon-remove',
			text : '删除岗位内操作',
			handler: function(){deletePositionButton();}
		};
	
	var toolbarFalse = [];
	toolbarFalse[toolbarFalse.length] = {
			iconCls: 'icon-search',
			text : '查询未选中操作',
			handler: function(){apiQueryFalse();}
		};
	toolbarFalse[toolbarFalse.length] = '-';
	toolbarFalse[toolbarFalse.length] = {
		iconCls: 'icon-add',
		text : '添加操作进岗位',
		handler: function(){
			addPositionButton();
		}
	};
	
	toolbarFalse[toolbarFalse.length] = '-';
	toolbarFalse[toolbarFalse.length] = {
		iconCls: 'icon-edit',
		text : '查询机构用户岗位',
		handler: function(){
			apiQueryFalse(1);
		}
	};
	
	xyzgrid({
		table : 'positionButtonTrueTable',
		title : '已选中的操作',
		url:'../AdminPositionWS/queryPositionButtonTrueList.do',
		toolbar : toolbarTrue,
		singleSelect : false,
		pagination : false,
		height:'auto',
		idField : 'numberCode',
		columns:[[
			{field:'functionNameCn',title:'所属功能',width:200,halign:'center',
				formatter: function(value,row,index){
					return "<div title='"+value+"'>"+value+"</div>";
				}
			},
			{field:'checkboxTemp',checkbox:true,width:40,halign:'center'},
			{field:'numberCode',title:'操作编号',width:200,halign:'center'},
			{field:'nameCn',title:'操作名称',width:500,halign:'center'}
		]],
		queryParams : {
			position : position
		}
	});
	
	xyzgrid({
		table : 'positionButtonFalseTable',
		title : '未选中的操作',
		url:'../AdminPositionWS/queryPositionButtonFalseList.do',
		toolbar : toolbarTrue,
		singleSelect : false,
		pagination : false,
		height:'auto',
		idField : 'numberCode',
		columns:[[
			{field:'groupNameCn',title:'所属功能',width:100,halign:'center'},
		    {field:'functionNameCn',title:'所属功能',width:150,halign:'center'},
			{field:'checkboxTemp',checkbox:true,width:40,halign:'center'},
			{field:'numberCode',title:'操作编号',width:200,halign:'center'},
			{field:'nameCn',title:'操作名称',width:500,halign:'center'}
			
		]],
		queryParams : {
			position : position
		}
	});
}

function addPositionButton(){
	var buttons = $.map($("#positionButtonFalseTable").datagrid("getChecked"),function(p){return p.numberCode;}).join(",");
	if(xyzIsNull(buttons)){
		top.$.messager.alert("提示","请先选中需要操作的对象！","info");
		return;
	}
	
	$.ajax({
		url : "../AdminPositionWS/addPositionButton.do",
		type : "POST",
		data : {
			position : position,
			buttons : buttons
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#positionButtonTrueTable").datagrid("reload");
				$("#positionButtonFalseTable").datagrid("reload");
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

function deletePositionButton(){
	var buttons = $.map($("#positionButtonTrueTable").datagrid("getChecked"),function(p){return p.numberCode;}).join(",");
	if(xyzIsNull(buttons)){
		top.$.messager.alert("提示","请先选中需要操作的对象！","info");
		return;
	}
	
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		if(r){
			$.ajax({
				url : "../AdminPositionWS/deletePositionButton.do",
				type : "POST",
				data : {
					position : position,
					buttons : buttons
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						$("#positionButtonTrueTable").datagrid("reload");
						$("#positionButtonFalseTable").datagrid("reload");
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

function apiQueryTrue(){
	
	var nameCn = $("#nameCn").textbox('getValue');
	var functionNameCn = $("#functionNameCn").textbox('getValue');

	$("#positionButtonTrueTable").datagrid('load',{
		position : position,
		nameCn : nameCn,
		functionNameCn : functionNameCn
	});
}

function apiQueryFalse(isPossessor){
	
	var nameCn = $("#nameCn").textbox('getValue');
	var functionNameCn = $("#functionNameCn").textbox('getValue');
	isPossessor = xyzIsNull(isPossessor)==true?0:isPossessor;
	
	$("#positionButtonFalseTable").datagrid('load',{
		position : position,
		nameCn : nameCn,
		functionNameCn : functionNameCn,
		isPossessor : isPossessor
	});
}
