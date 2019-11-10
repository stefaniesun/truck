$(document).ready(function(){
	xyzTextbox("nameCn");
	
	initTable();
	
	$("#areaQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = []; 
	if(xyzControlButton("buttonCode_x20161207200102")){
		toolbar[toolbar.length]={
				text: '新增',  
				border:'1px solid #bbb',  
				iconCls: 'icon-add', 
				handler: function(){
					addAreaButton();
				}
		};
	}
	
	if(xyzControlButton("buttonCode_x20161207200103")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
			text: '编辑',  
			border:'1px solid #bbb',  
			iconCls: 'icon-edit', 
			handler: function(){
				editAreaButton();
			}
		};
	}	
	
	if(xyzControlButton("buttonCode_x20161207200104")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
				text: '删除',  
				border:'1px solid #bbb',  
				iconCls: 'icon-remove', 
				handler: function(){
					deleteAreaButton();
				}
		};
	}
	
	xyzgrid({
		table : 'areaManagerTable',
		title : "航区列表",
		url:'../AreaWS/queryAreaList.do',
		toolbar:toolbar,
		singleSelect : false, 
		idField : 'numberCode',
		columns : [[
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'航区编号',hidden:true,halign:'center'},
			{field:'nameCn',title:'航区名称',width:100,halign:'center',
			   formatter:function(value ,row ,index){
				  return xyzGetDiv(value,0,100);
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
		    {field:'remark',title:'备注',width:120,halign:'center',
			   formatter:function(value ,row ,index){
				   return xyzGetDiv(value ,0 ,120);
			   }
		    }
		]]
	});
}

function loadTable(){
	var nameCn = $("#nameCn").val();

	$("#areaManagerTable").datagrid('load', {
		nameCn : nameCn
	});
}

function addAreaButton(){
	xyzdialog({
		dialog : 'dialogFormDiv_addAreaButton',
		title : '新增航区',
	    href : '../jsp_base/addArea.html',
	    fit : false,  
	    width : 450,
	    height : 450,
	    buttons : [{
			text : '新增',
			handler:function(){
				addAreaSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addAreaButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("nameCnForm");
		}
	});
}

function addAreaSubmit(){
	if(!$("form").form('validate')){
		return false;
	}

	var nameCn = $("#nameCnForm").textbox("getValue");
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../AreaWS/addArea.do",
		type : "POST",
		data : {
			nameCn : nameCn,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#areaManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_addAreaButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	}); 
}

function editAreaButton(){
	var areas = $("#areaManagerTable").datagrid("getChecked");
	if(areas.length != 1){
		top.$.messager.alert("提示","请先选中单个对象!","info");
		return;
	}
	var row = areas[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editAreaButton',
		title : '编辑【'+ row.nameCn +'】',
	    href : '../jsp_base/editArea.html',
	    fit : false,  
	    width: 450,
	    height : 450,
	    buttons : [{
			text:'编辑',
			handler:function(){
				editAreaSubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editAreaButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("nameCnForm");
			
			$("#nameCnForm").textbox('setValue',row.nameCn);
			$("#remarkForm").val(row.remark);
			
			$("#nameCnForm").textbox({
				required : true
			});
		}
	});
}

function editAreaSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").textbox("getValue");
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../AreaWS/editArea.do",
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
				$("#areaManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_editAreaButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deleteAreaButton(){
	var areas = $.map($("#areaManagerTable").datagrid("getChecked"),
		function(p){
	       return p.numberCode;
	    }
	).join(",");
	
	if(xyzIsNull(areas)){
		top.$.messager.alert("提示","请先选中需要删除的对象!","info");
		return;
	}
	
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		if(r){
			$.ajax({
				url : "../AreaWS/deleteArea.do",
				type : "POST",
				data : {
					numberCodes : areas
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						$("#areaManagerTable").datagrid("reload");
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