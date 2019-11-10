$(document).ready(function() {
	xyzTextbox("goal");
	xyzCombobox({
		combobox : 'person',
		url : '../ListWS/getPersonList.do',
		mode : 'remote',
		lazy : false,
		icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	
	$("#type").combobox({
		icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	
	initTable();
	
	$("#planGoalQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = [];
	if (xyzControlButton("buttonCode_x20171012181002")) {
		toolbar[toolbar.length] = {
			text : '新增',
			border : '1px solid #bbb',
			iconCls : 'icon-add',
			handler : function() {
				addPlanGoalButton();
			}
		};
	}
	if (xyzControlButton("buttonCode_x20171012181003")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length] = {
			text : '编辑',
			border : '1px solid #bbb',
			iconCls : 'icon-edit',
			handler : function() {
				editPlanGoalButton();
			}
		};
	}
	if (xyzControlButton("buttonCode_x20171012181004")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length] = {
			text : '编辑类型',
			border : '1px solid #bbb',
			iconCls : 'icon-edit',
			handler : function() {
				editTypeButton();
			}
		};
	}
	if (xyzControlButton("buttonCode_x20171012181007")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length] = {
			text : '删除',
			border : '1px solid #bbb',
			iconCls : 'icon-remove',
			handler : function() {
				deletePlanGoalButton();
			}
		};
	}

	xyzgrid({
		table : 'planGoalManagerTable',
		title : "计划目标列表",
		url : '../PlanGoalWS/queryPlanGoalList.do',
		toolbar : toolbar,
		singleSelect : false,
		idField : 'numberCode',
		columns : [[
             {field:'checkboxTemp',checkbox:true},
             {field:'numberCode',title:'编号',hidden:true,halign:'center'},
 		     {field:'type',title:'类型',align:'center',width:70,
		    	 formatter:function(value ,row ,index){//0:需求、1:bug、2:优化、3:其他
		    		 if(value == "0"){
		    			 return "需求";
		    		 }else if(value == "1"){
		    			return "bug";
		    		 }else if(value == "2"){
		    			return "优化";
		    		 }
		    		 return "其他";
		    	 }
		     },
 		     {field:'state',title:'状态',align:'center',sortable:true,order:'desc',
		    	 formatter:function(value, row, index){
	        	    return '<input style="width:56px;height:18px;" class="switchbutton stateSwitchbutton" data-options="checked:'+(value==0?'true':'false')+'" data-infoTableCode="'+row.numberCode+'"/>';
				 }
 		     },
 		     {field:'delay',title:'是否延后',align:'center',sortable:true,order:'desc',
         	    formatter:function(value, row, index){
         	    	return '<input style="width:40px;height:18px;" class="switchbutton delaySwitchbutton" data-options="checked:'+(value==0?'true':'false')+'" data-infoTableCode="'+row.numberCode+'"/>';
 				}
             },
 		     {field:'goal',title:'内容',width:300,halign:'center',
 		    	formatter:function(value, row, index){
		    		 return xyzGetDiv(value, 0, 260);
		    	 }
 		     },
 		    {field:'founder',title:'发布人编号',align:'center',hidden:true},
 		    {field:'person',title:'负责人编号',align:'center',hidden:true},
 		    {field:'founderName',title:'发布人',align:'center'},
		    {field:'personName',title:'负责人',align:'center'},
 		    {field:'endTime',title:'完成时间',width:85,halign:'center',sortable:true,order:'desc',
 		    	formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
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
		]],
		onLoadSuccess : function(data){
			$('.stateSwitchbutton').switchbutton({ 
			      onText : '完成',
			      offText : '未完成',
			      onChange: function(checked){ 
			    	  editState($(this).attr('data-infoTableCode'));
			      } 
			});
			$('.delaySwitchbutton').switchbutton({ 
			      onText : '是',
			      offText : '否',
			      onChange: function(checked){ 
			    	  editDelay($(this).attr('data-infoTableCode'));
			      } 
			});
		}
	});
	
}

function loadTable(){
	var type = $("#type").combobox("getValue");
	var person = $("#person").combobox("getValue");
	var goal = $("#goal").val();
	
	$("#planGoalManagerTable").datagrid('load',{
		type : type,
		goal : goal,
		person : person
	});
}

function addPlanGoalButton(){
	xyzdialog({
		dialog : 'dialogFormDiv_addPlanGoalButton',
		title : '新增计划目标',
	    href : '../jsp_planGoal/addPlanGoal.html',
	    fit : false,  
	    width: 450,
	    height : 450,
	    buttons:[{
			text:'确定',
			handler:function(){
				addPlanGoalSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addPlanGoalButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzCombobox({
				required : true,
				combobox : 'founderForm',
				url : '../ListWS/getPersonList.do',
				mode : 'remote',
				lazy : false,
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			xyzCombobox({
				required : true,
				combobox : 'personForm',
				url : '../ListWS/getPersonList.do',
				mode : 'remote',
				lazy : false,
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			
			$("#typeForm").combobox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			$("#endTimeForm").datetimebox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).datetimebox('clear');
					}
				}]
			});
			
			xyzTextbox("nameCnForm");
			
			$("input[name='radioState']").click(function(){
				var oldValue = $("#stateForm").val();
				var newValue = $("input[name='radioState']:checked").val();
				
				if(oldValue != newValue){
					$("#stateForm").val(newValue);
				}
			});
			
			$("input[name='radioDelay']").click(function(){
				var oldValue = $("#delayForm").val();
				var newValue = $("input[name='radioDelay']:checked").val();
				
				if(oldValue != newValue){
					$("#delayForm").val(newValue);
				}
			});
			
		}
	});
}

function addPlanGoalSubmit(){
	if(!$("form").form('validate')){
		return false;
	}
	
	var type = $("#typeForm").combobox('getValue');
	var state = $("#stateForm").val();
	var delay = $("#delayForm").val();
	var goal = $("#goalForm").val();
	var founder = $("#founderForm").combobox('getValue');
	var person = $("#personForm").combobox('getValue');
	var endTime = $("#endTimeForm").datetimebox('getValue');
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../PlanGoalWS/addPlanGoal.do",
		type : "POST",
		data : {
			type : type,
			state : state,
			delay : delay, 
			goal : goal,
			founder : founder,
			person : person,
			endTime : endTime,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#planGoalManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_addPlanGoalButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editPlanGoalButton(){
	var goals = $("#planGoalManagerTable").datagrid("getChecked");
	if(goals.length != 1){
		top.$.messager.alert("提示","请先选中单个对象!","info");
		return;
	}
	var row = goals[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editPlanGoalButton',
		title : '编辑计划目标',
	    href : '../jsp_planGoal/editPlanGoal.html',
	    fit : false,  
	    width: 450,
	    height : 450,
	    buttons : [{
			text : '确定',
			handler:function(){
				editPlanGoalSubmit(row.numberCode);
			}
		},{
			text : '取消',
			handler:function(){
				$("#dialogFormDiv_editPlanGoalButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzCombobox({
				combobox : 'founderForm',
				url : '../ListWS/getPersonList.do',
				mode : 'remote',
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			xyzCombobox({
				combobox : 'personForm',
				url : '../ListWS/getPersonList.do',
				mode : 'remote',
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			
			$("#typeForm").combobox({
				value : row.type,
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			$("#endTimeForm").datetimebox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).datetimebox('clear');
					}
				}]
			});
			
			if(row.state == 1){
				$("input[name='radioState']").eq(0).attr("checked",true);
			}else{
				$("input[name='radioState']").eq(1).attr("checked",true);
			}
			$("#stateForm").val(row.state);
			
			if(row.delay == 1){
				$("input[name='radioDelay']").eq(0).attr("checked",true);
			}else{
				$("input[name='radioDelay']").eq(1).attr("checked",true);
			}
			$("#delayForm").val(row.delay);
			
			$("#goalForm").val(row.goal);
			
			$("#founderForm").combobox({
				value : row.founder
			});
			$("#personForm").combobox({
				value : row.person
			});
			$("#endTimeForm").datetimebox({
				value : row.endTime
			});
			$("#remarkForm").val(row.remark);
			
			$("#typeForm").combobox({
				required : true
			});
			$("#founderForm").combobox({
				required : true
			});
			$("#personForm").combobox({
				required : true
			});
			$("#endTimeForm").datebox({
				required : true
			});

			$("input[name='radioState']").click(function(){
				var oldValue = $("#stateForm").val();
				var newValue = $("input[name='radioState']:checked").val();
				
				if(oldValue != newValue){
					$("#stateForm").val(newValue);
				}
			});
			$("input[name='radioDelay']").click(function(){
				var oldValue = $("#delayForm").val();
				var newValue = $("input[name='radioDelay']:checked").val();
				
				if(oldValue != newValue){
					$("#delayForm").val(newValue);
				}
			});
			
		}
	});
}

function editPlanGoalSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var type = $("#typeForm").combobox('getValue');
	var state = $("#stateForm").val();
	var delay = $("#delayForm").val();
	var goal = $("#goalForm").val();
	var founder = $("#founderForm").combobox('getValue');
	var person = $("#personForm").combobox('getValue');
	var endTime = $("#endTimeForm").datebox('getValue');
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../PlanGoalWS/editPlanGoal.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			type : type,
			state : state,
			delay : delay, 
			goal : goal,
			founder : founder,
			person : person,
			endTime : endTime,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#planGoalManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_editPlanGoalButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editTypeButton(){
	var goals = $("#planGoalManagerTable").datagrid("getChecked");
	if(goals.length != 1){
		top.$.messager.alert("提示","请先选中单个对象!","info");
		return;
	}
	var row = goals[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editTypeButton',
		title : '编辑计划目标状态',
	    href : '../jsp_planGoal/editType.html',
	    fit : false,  
	    width: 280,
	    height : 184,
	    buttons : [{
			text : '确定',
			handler:function(){
				editTypeSubmit(row.numberCode);
			}
		},{
			text : '取消',
			handler:function(){
				$("#dialogFormDiv_editTypeButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			
			$("#typeForm").combobox({
				value : row.type,
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			
			$("#typeForm").combobox({
				required : true
			});
		}
	});
	
}

function editTypeSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var type = $("#typeForm").combobox("getValue");
	
	$.ajax({
		url : "../PlanGoalWS/editPlanGoalByType.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			type : type
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#planGoalManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_editTypeButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});    
	
}

function deletePlanGoalButton(){
	
	var numberCodes = $.map($("#planGoalManagerTable").datagrid("getChecked"),
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
				url : "../PlanGoalWS/deletePlanGoal.do",
				type : "POST",
				data : {
					numberCodes : numberCodes
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						$("#planGoalManagerTable").datagrid("reload");
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

function editState(numberCode){
	
	$.ajax({
		url : "../PlanGoalWS/editState.do",
		type : "POST",
		data : {
			numberCode : numberCode
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status!=1){
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});     
	
}

function editDelay(numberCode){
	
	$.ajax({
		url : "../PlanGoalWS/editDelay.do",
		type : "POST",
		data : {
			numberCode : numberCode
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status!=1){
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});  
	
}