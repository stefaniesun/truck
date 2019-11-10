/**
 * 出行人信息管理插件
 * 参数: flag:是否允许修改, 默认:false(不允许修改)
 * 		numberCodes:以前的数据, 用于查询datagrid,默认:空数组
 * 		callbackFunction : 回调函数, 在完成操作之后, 调用的方法
 **/
function xyzUpdatePersonInfo(data){
	xyzPersonInfoDatagridIsOper = false;
	var flag = 'flag' in data?data.flag:false;
	var buttons = [];
	if(flag){
		buttons[buttons.length] = {
			text:'确定',
			handler:function(){
				if(xyzPersonInfoDatagridIsOper==true){
					xyzUpdatePersonInfoSubmit(data);
				}else{
					$("#dialogFormDiv_xyzUpdatePersonInfo").dialog("destroy");
				}
			}
		};
	}else{
		buttons[buttons.length] = {
			text:'确定',
			handler:function(){
				$("#dialogFormDiv_xyzUpdatePersonInfo").dialog("destroy");
			}
		};
	}
	buttons[buttons.length] = {
			text:'关闭',
			handler:function(){
				$("#dialogFormDiv_xyzUpdatePersonInfo").dialog("destroy");
			}
		};
	
	var htmlContent = '<div><table id="xyzPersonInfoTable"></table></div>';
	
	if(flag){
		htmlContent += '<div id="xyzPersonInfoToolbar" style="display:none">';
		htmlContent += '<table>';
		htmlContent += '<tr>';
		htmlContent += '<td>';
		htmlContent += '<a href="#" id="xyzPersonInfoToolbar1" class="easyui-linkbutton" data-options="iconCls:\'icon-add\',plain:true" onclick="addPersonInfoButton();">添加出行信息</a>';
		htmlContent += '</td>';
		htmlContent += '<td>';
		htmlContent += '<div class=\"datagrid-btn-separator\"></div>';
		htmlContent += '</td>';
		htmlContent += '<td>';
		htmlContent += '<a href="#" id="xyzPersonInfoToolbar2" class="easyui-linkbutton" data-options="iconCls:\'icon-remove\',plain:true" onclick="deleteCheckedRow();">删除选中行</a>';
		htmlContent += '</td>';
		htmlContent += '<td>';
		htmlContent += '<div class=\"datagrid-btn-separator\"></div>';
		htmlContent += '</td>';
		htmlContent += '<td>';
		htmlContent += '<a href="#" id="xyzPersonInfoToolbar3" class="easyui-linkbutton" data-options="iconCls:\'icon-edit\',plain:true" onclick="beginEditCheckedRow();">编辑选中行</a>';
		htmlContent += '</td>';
		htmlContent += '<td>';
		htmlContent += '<div class=\"datagrid-btn-separator\"></div>';
		htmlContent += '</td>';
		htmlContent += '<td>';
		htmlContent += '<a href="#" id="xyzPersonInfoToolbar4" class="easyui-linkbutton" data-options="iconCls:\'icon-edit\',plain:true" onclick="endEditCheckedRow();">保存修改</a>';
		htmlContent += '</td>';
		htmlContent += '<td>';
		htmlContent += '<div class=\"datagrid-btn-separator\"></div>';
		htmlContent += '</td>';
		htmlContent += '<td>';
		htmlContent += '<input type="text" id="personInfoList" style="width:400px;"/>';
		htmlContent += '</td>';
		htmlContent += '<td>';
		htmlContent += '<a href="#" id="xyzPersonInfoToolbar5" class="easyui-linkbutton" data-options="iconCls:\'icon-add\',plain:true" onclick="addPersonInfoToList();">添加熟悉的出行人</a>';
		htmlContent += '</td>';
		htmlContent += '</tr>';
		htmlContent += '</table>';
		htmlContent += '</div>';
	}
	
	xyzdialog({
		dialog : 'dialogFormDiv_xyzUpdatePersonInfo',
		title : '出行凭证管理',
	    content : htmlContent,
	    fit:false,
	    width:1070,
	    height:550,
	    buttons:buttons,
		onOpen : function(){
			if(flag){
				$("#personInfoList").combogrid({
					panelWidth : 440,
					delay : 500,
					mode: 'remote',
					url: '../PersonInfoWS/getPersonInfoList.do',
				    idField: 'numberCode',
				    textField: 'realName',
				    loadFilter : function(data){
						if(data.status==1){
							return data.content;
						}else{
							top.$.messager.alert("警告",data.msg,"warning");
							return {total : 0 , rows : []};
						}
					},
				    columns: [[    
						{field:'realName',title:'姓名',width:80},
						{field:'ename',title:'英文名',width:100},
						{field:'card',title:'证件号',width:140}
				    ]]
				});
				initTableForPersonInfoForOper(data.numberCodes);
			}else{
				initTableForPersonInfoForNoOper(data.numberCodes);
			}
		}
	});
}

function initTableForPersonInfoForOper(numberCodes){
	xyzgrid({
		table : 'xyzPersonInfoTable',
		title : "出行人信息列表",
		url:'../PersonInfoWS/queryPersonInfoList.do',
		idField : 'numberCode',
		toolbar : 'xyzPersonInfoToolbar',
		height:'auto',
		pagination : false,
		singleSelect : true,
		columns:[[
		    {field:'checkboxTemp',checkbox:true},
		    {field:'numberCode',title:'出行人信息编号',hidden:true},
		    {field:'realName',title:'姓名',width:80,editor:{
			    	type:'text',
			    	options : {
			    		required:true,
			    		validType:'length[2,50]'
			    	}
	    		}
		    },
		    {field:'ename',title:'英文名',width:100,
		    	editor:{
			    	type:'validatebox',
			    	options : {validType:'length[2,50]'}
	    		}
		    },
		    {field:'card',title:'证件',width:140,
		    	editor:{
		    		type:'validatebox',
		    		options : {validType:'length[2,50]'}
		    	}
		    },
		    {field:'birthday',title:'出生日期',width:75,editor:'datebox'},
		    {field:'passport',title:'证件2',width:140,editor:{
		    	type:'validatebox',
		    	options : {validType:'length[2,50]'}
    		}},
		    {field:'expireDate',title:'过期日期',width:75,editor:'datebox'},
		    {field:'domicile',title:'户籍地',width:70,editor:{
		    	type:'validatebox',
		    	options : {validType:'length[2,50]'}
    		}},
		    {field:'sex',title:'性别',width:40,editor:{
		    	type:'combobox',
		    	options : {
		    		valueField:'value',
		    	    textField:'text',
		    	    data : [{text:'男',value:'男'},{text:'女',value:'女'}]
		    	}
		    }},
		    {field:'personType',title:'客户类型',width:64,editor:{
		    	type:'combobox',
		    	options : {
		    		valueField:'value',
		    	    textField:'text',
		    	    data : [{text:'成人',value:'成人'},
		    	            {text:'儿童',value:'儿童'},
		    	            {text:'婴儿',value:'婴儿'},
		    	            {text:'未知',value:'未知'}]
		    	}
		    }},
		    {field:'nation',title:'国籍',width:100,
		    	editor:{
			    	type:'validatebox',
			    	options : {validType:'length[2,50]'}
	    		}
		    },
		    {field:'addDate',title:'添加时间',hidden:true},
		    {field:'alterDate',title:'修改时间',hidden:true}
		]],
		queryParams:{
			numberCodes : numberCodes.join(",")
		}
	});
}

function initTableForPersonInfoForNoOper(numberCodes){
	xyzgrid({
		table : 'xyzPersonInfoTable',
		title : "出行人信息列表",
		url:'../PersonInfoWS/queryPersonInfoList.do',
		idField : 'numberCode',
		height:'auto',
		pagination : false,
		singleSelect : true,
		columns:[[
		    {field:'realName',title:'姓名',width:80},
		    {field:'ename',title:'英文名',width:100},
		    {field:'card',title:'证件1',width:140},
		    {field:'birthday',title:'出生日期',width:75},
		    {field:'passport',title:'证件2',width:140},
		    {field:'expireDate',title:'过期日期',width:75},
		    {field:'domicile',title:'户籍地',width:70},
		    {field:'sex',title:'性别',width:40},
		    {field:'personType',title:'客户类型',width:64},
		    {field:'nation',title:'国籍',width:100}
		]],
		queryParams:{
			numberCodes : numberCodes.join(",")
		}
	});
}

function addPersonInfoToList(){
	var grid = $('#personInfoList').combogrid('grid');	// 获取数据表格对象
	var row = grid.datagrid('getSelected');	// 获取选择的行
	
	if(xyzIsNull(row)){
		return;
	}
	var rows = $("#xyzPersonInfoTable").datagrid("getRows");
	var index = rows.length;
	$("#xyzPersonInfoTable").datagrid("appendRow",{
		realName : row.realName,
		ename : row.ename,
		card : row.card,
		birthday : row.birthday,
		passport : row.passport,
		expireDate : row.expireDate,
		domicile : row.domicile,
		sex : row.sex,
		nation : row.nation,
		personType : row.personType
	});
	$("#xyzPersonInfoTable").datagrid("beginEdit",index);
	var realName = $('#xyzPersonInfoTable').datagrid('getEditor', {index:index,field:'realName'});
	realName.target.prop('disabled','true'); // 将名字设为只读
	xyzPersonInfoDatagridIsOper=true;
}

function addPersonInfoButton(){
	var rows = $("#xyzPersonInfoTable").datagrid("getRows");
	var index = rows.length;
	$("#xyzPersonInfoTable").datagrid("appendRow",{
		realName : '',
		ename : '',
		card : '',
		birthday : null,
		passport : '',
		expireDate : null,
		domicile : '',
		sex : '未知',
		nation : '',
		personType : '未知'
	});
	$("#xyzPersonInfoTable").datagrid("beginEdit",index);
	xyzPersonInfoDatagridIsOper=true;
}

function beginEditCheckedRow(){
	var checkedRows = $("#xyzPersonInfoTable").datagrid("getChecked");
	if(checkedRows.length!=1){
		top.$.messager.alert("提示","请先选中单个对象!","info");
		return;
	}
	var index = $("#xyzPersonInfoTable").datagrid("getRowIndex",checkedRows[0]);
	$("#xyzPersonInfoTable").datagrid("beginEdit",index); //开始编辑
	var realName = $('#xyzPersonInfoTable').datagrid('getEditor', {index:index,field:'realName'});
	realName.target.prop('disabled','true'); // 将名字设为只读
	$("#xyzPersonInfoTable").datagrid("unselectRow",index);//取消选中 (颜色问题)
	xyzPersonInfoDatagridIsOper=true;
}

function endEditCheckedRow(){
	$("#xyzPersonInfoTable").datagrid("acceptChanges");
}

function deleteCheckedRow(){
	var checkedRows = $("#xyzPersonInfoTable").datagrid("getChecked");
	if(checkedRows.length!=1){
		top.$.messager.alert("提示","请先选中单个对象!","info");
		return;
	}
	var index = $("#xyzPersonInfoTable").datagrid("getRowIndex",checkedRows[0]);
	$("#xyzPersonInfoTable").datagrid("deleteRow",index);
	xyzPersonInfoDatagridIsOper=true;
}

function xyzUpdatePersonInfoSubmit(data){
	$("#xyzPersonInfoTable").datagrid("acceptChanges");
	var rows = $("#xyzPersonInfoTable").datagrid("getRows");
	
	var newNumberCodes = [];
	for(var i=0;i<rows.length;i++){
		var obj = rows[i];
		if(xyzIsNull(obj.realName)){
			top.$.messager.alert("警告","姓名不能为空!","warning");
			return;
		}
	}
	
	for(var i=0;i<rows.length;i++){
		var obj = rows[i];
		if(xyzIsNull(obj.numberCode)){
			var currentNumberCode = addPersonInfoSubmit(obj);
			newNumberCodes[newNumberCodes.length]=currentNumberCode;
		}else{
			editPersonInfoSubmit(obj);
			newNumberCodes[newNumberCodes.length]=obj.numberCode;
		}
	}
	
	$("#dialogFormDiv_xyzUpdatePersonInfo").dialog("destroy");
	data["callbackFunction"](newNumberCodes);
}

function addPersonInfoSubmit(obj){
	var resultNumberCode = "";
	$.ajax({
		url : "../PersonInfoWS/addPersonInfo.do",
		type : "POST",
		data : {
			realName : obj.realName,
			ename : obj.ename,
			card : obj.card,
			birthday : obj.birthday,
			card2 : obj.passport,
			expireDate : obj.expireDate,
			hjd : obj.domicile,
			sex : obj.sex,
			nation : obj.nation,
			personType : obj.personType
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				resultNumberCode = data.content;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	return resultNumberCode;
}

function editPersonInfoSubmit(obj){
	
	$.ajax({
		url : "../PersonInfoWS/editPersonInfo.do",
		type : "POST",
		data : {
			numberCode : obj.numberCode,
			ename : obj.ename,
			card : obj.card,
			birthday : obj.birthday,
			card2 : obj.passport,
			expireDate : obj.expireDate,
			hjd : obj.domicile,
			sex : obj.sex,
			nation : obj.nation,
			personType : obj.personType
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}