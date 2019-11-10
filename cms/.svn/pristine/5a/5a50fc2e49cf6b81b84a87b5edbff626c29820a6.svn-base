$(document).ready(function() {
	xyzTextbox("nameCn");
	
	initTable();
	
	$("#planGoalQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = []; 
	if(xyzControlButton("buttonCode_x20171013141002")){
		toolbar[toolbar.length]={
			text: '新增',  
			border:'1px solid #bbb',  
			iconCls: 'icon-add', 
			handler: function(){
				addPersonButton();
			}
		};
	}
	
	if(xyzControlButton("buttonCode_x20171013141003")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
			text: '编辑',  
			border:'1px solid #bbb',  
			iconCls: 'icon-edit', 
			handler: function(){
				editPersonButton();
			}
		};
	}	
	
	if(xyzControlButton("buttonCode_x20171013141004")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
			text: '删除',  
			border:'1px solid #bbb',  
			iconCls: 'icon-remove', 
			handler: function(){
				deletePersonButton();
			}
		};
	}
	
	xyzgrid({
		table : 'personManagerTable',
		title : "员工列表",
		url:'../PersonWS/queryPersonList.do',
		toolbar:toolbar,
		singleSelect : false, 
		idField : 'numberCode',
		columns : [[
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'编号',hidden:true,halign:'center'},
			{field:'nameCn',title:'姓名',halign:'center',
			   formatter:function(value, row, index){
				  return xyzGetDiv(value, 0, 6);
			   }
			},
		    {field:'post',title:'职位',align:'center',
			   formatter:function(value, row, index){
				   if(value == 0){
					   return "后台";
				   }else if(value == 1){
					   return "前端";
				   }
				   return "UI";
			   }
		    },
		    {field:'sex',title:'性别',halign:'center'}
		]]
	});
	
}

function loadTable(){
	var nameCn = $("#nameCn").val();

	$("#personManagerTable").datagrid('load', {
		nameCn : nameCn
	});
}

function addPersonButton(){
	xyzdialog({
		dialog : 'dialogFormDiv_addPersonButton',
		title : '新增员工',
	    href : '../jsp_planGoal/addPerson.html',
	    fit : false,  
	    width : 450,
	    height : 400,
	    buttons : [{
			text : '新增',
			handler:function(){
				addPersonSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addPersonButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("nameCnForm");
			
			$("#postForm").combobox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			
			$("input[type='radio']").click(function(){
				var oldValue = $("#sexForm").val();
				var newValue = $("input[type='radio']:checked").val();
				
				if(oldValue != newValue){
					$("#sexForm").val(newValue);
				}
			});
		}
	});
}

function addPersonSubmit(){
	if(!$("form").form('validate')){
		return false;
	}

	var nameCn = $("#nameCnForm").textbox("getValue");
	var post = $("#postForm").combobox("getValue");
	var sex = $("#sexForm").val();
	
	$.ajax({
		url : "../PersonWS/addPerson.do",
		type : "POST",
		data : {
			nameCn : nameCn,
			post : post,
			sex : sex
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#personManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_addPersonButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	}); 
}

function editPersonButton(){
	var persons = $("#personManagerTable").datagrid("getChecked");
	if(persons.length != 1){
		top.$.messager.alert("提示","请先选中单个对象!","info");
		return;
	}
	var row = persons[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editPersonButton',
		title : '编辑员工【'+ row.nameCn +'】',
	    href : '../jsp_planGoal/editPerson.html',
	    fit : false,  
	    width: 450,
	    height : 400,
	    buttons : [{
			text:'编辑',
			handler:function(){
				editPersonSubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editPersonButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("nameCnForm");
			
			$("#nameCnForm").textbox('setValue', row.nameCn);
			
			$("#postForm").combobox({
				value : row.post,
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			if(row.sex == '男'){
				$("input[type='radio']").eq(0).attr("checked",true);
			}else{
				$("input[type='radio']").eq(1).attr("checked",true);
			}
			$("#sexForm").val(row.sex);	
			
			$("#nameCnForm").textbox({
				required : true
			});
			$("#postForm").combobox({
				required : true
			});
			$("input[type='radio']").click(function(){
				var oldValue = $("#sexForm").val();
				var newValue = $("input[type='radio']:checked").val();
				
				if(oldValue != newValue){
					$("#sexForm").val(newValue);
				}
			});
		}
	});
}

function editPersonSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").textbox("getValue");
	var post = $("#postForm").combobox("getValue");
	var sex = $("#sexForm").val();
	
	$.ajax({
		url : "../PersonWS/editPerson.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			nameCn : nameCn,
			post : post,
			sex : sex
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#personManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_editPersonButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deletePersonButton(){
	var numberCodes = $.map($("#personManagerTable").datagrid("getChecked"),
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
				url : "../PersonWS/deletePerson.do",
				type : "POST",
				data : {
					numberCodes : numberCodes
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						$("#personManagerTable").datagrid("reload");
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