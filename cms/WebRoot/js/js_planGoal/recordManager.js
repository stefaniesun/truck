$(document).ready(function() {
	xyzTextbox("content");
	
	initTable();
	
	$("#recordQueryButton").click(function(){
		loadTable();
	});
	
}); 

function initTable(){
	var toolbar = []; 
	if(xyzControlButton("buttonCode_x20171013161002")){
		toolbar[toolbar.length]={
			text: '新增',  
			border:'1px solid #bbb',  
			iconCls: 'icon-add', 
			handler: function(){
				addRecordButton();
			}
		};
	}
	
	if(xyzControlButton("buttonCode_x20171013161003")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
			text: '编辑',  
			border:'1px solid #bbb',  
			iconCls: 'icon-edit', 
			handler: function(){
				editRecordButton();
			}
		};
	}	
	
	if(xyzControlButton("buttonCode_x20171013161004")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
			text: '删除',  
			border:'1px solid #bbb',  
			iconCls: 'icon-remove', 
			handler: function(){
				deleteRecordButton();
			}
		};
	}
	
	xyzgrid({
		table : 'recordManagerTable',
		title : "会议记录列表",
		url:'../RecordWS/queryRersonList.do',
		toolbar:toolbar,
		singleSelect : false, 
		idField : 'numberCode',
		columns : [[
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'编号',hidden:true,halign:'center'},
			{field:'content',title:'内容',halign:'center',width:300,
			   formatter:function(value, row, index){
				  return xyzGetDiv(value, 0, 200);
			   }
			},
			{field:'details',title:'详情',align:'center',
			   formatter: function(value,row,index){
				   var html = "";
				   if(xyzControlButton("buttonCode_x20171013161005")){
				 	  html = xyzGetA("详情","addDetail",[row.numberCode, index],"点我查看和添加详情!","blue");
				   }else{
					  html = xyzGetA("详情","checkDetail",[row.numberCode, index],"点我查看详情!","blue");
				   }
				   return html;
			   }
			}, 
			{field:'addDate',title:'添加时间',hidden:true,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'alterDate',title:'修改时间',width:83,halign:'center',sortable:true,order:'desc',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'remark',title:'备注',width:100,halign:'center',
		    	 formatter:function(value, row, index){
		    		 return xyzGetDiv(value, 0, 100);
		    	 }
            }
		]]
	});
	
}

function loadTable(){
	
	var content = $("#content").val();
	
	$("#recordManagerTable").datagrid('load',{
		content : content
	});
	
}

function addRecordButton(){
	
	xyzdialog({
		dialog : 'dialogFormDiv_addRecordButton',
		title : '新增会议记录',
	    href : '../jsp_planGoal/addRecord.html',
	    fit : false,  
	    width: 450,
	    height : 450,
	    buttons:[{
			text:'确定',
			handler:function(){
				addRecordSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addRecordButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			
		}
	});
	
}

function addRecordSubmit(){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var content = $("#contentForm").val();
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../RecordWS/addRerson.do",
		type : "POST",
		data : {
			content : content,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#recordManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_addRecordButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function editRecordButton(){
	
	var records = $("#recordManagerTable").datagrid("getChecked");
	if(records.length != 1){
		top.$.messager.alert("提示","请先选中单个对象!","info");
		return;
	}
	var row = records[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editRecordButton',
		title : '编辑会议记录',
	    href : '../jsp_planGoal/editRecord.html',
	    fit : false,  
	    width: 450,
	    height : 450,
	    buttons : [{
			text:'编辑',
			handler:function(){
				editRecordSubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editRecordButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			
			$("#contentForm").val(row.content);
			$("#remarkForm").val(row.remark);
			$("#contentForm").validatebox({
				required : true
			});
		}
	});
	
}

function editRecordSubmit(numberCode){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var content = $("#contentForm").val();
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../RecordWS/editRerson.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			content : content,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#recordManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_editRecordButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function deleteRecordButton(){
	
	var numberCodes = $.map($("#recordManagerTable").datagrid("getChecked"),
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
				url : "../RecordWS/deleteRerson.do",
				type : "POST",
				data : {
					numberCodes : numberCodes
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						$("#recordManagerTable").datagrid("reload");
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

function addDetail(numberCode,index) {
	$('#recordManagerTable').datagrid('selectRow',index);    
	var recordDetail = $('#recordManagerTable').datagrid('getSelected');    
	
	var htmlContent = "<div>";
	htmlContent += "<textarea name='ckeditor' class='value' ></textarea>";
	htmlContent += "</div>";
	xyzdialog({
		dialog : 'dialogFormDiv_addDetail',
		title : '会议记录详情',
		content : htmlContent,
		fit : false,
		width : 804,
		height : 500,
		buttons : [ {
			text : '确定',
			handler : function() {
				addDetailSubmit(numberCode);
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_addDetail").dialog("destroy");
			}
		} ],
		onOpen : function() {
			$("textarea[name='ckeditor']").ckeditor({
				width : 780,
				height : 265,
				toolbar : 'Basic'
			});
			$("textarea[class='value']").val(recordDetail.detail);
		}
	});
}

function addDetailSubmit(numberCode) {
	var value = $("textarea[class='value']").val();
	
	$.ajax({
		url : '../RecordWS/addDetails.do',
		type : "POST",
		data : {
			numberCode : numberCode,
			detail : value
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#recordManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_addDetail").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function checkDetail(numberCode,index){
	$('#recordManagerTable').datagrid('selectRow',index);    
	var recordDetail = $('#recordManagerTable').datagrid('getSelected');    
	
	var htmlContent = "<div>";
	htmlContent += "<textarea name='ckeditor' class='value'></textarea>";
	htmlContent += "</div>";
	xyzdialog({
		dialog : 'dialogFormDiv_checkDetail',
		title : '会议详情',
		content : htmlContent,
		fit : false,
		width : 804,
		height : 500,
		buttons : [ {
			text : '关闭',
			handler : function() {
				$("#dialogFormDiv_checkDetail").dialog("destroy");
			}
		} ],
		onOpen : function() {
			$("textarea[name='ckeditor']").ckeditor({
				width : 780,
				height : 265,
				readOnly:true
			});
			$("textarea[class='value']").val(recordDetail.detail);
		}
	});
}