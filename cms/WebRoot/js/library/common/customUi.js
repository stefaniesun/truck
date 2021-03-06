$.extend($.fn.combobox.methods, {    
    getValue: function(jq){    
		var value = jq.combo('getValue');
		var text = jq.combo('getText');
		var data = jq.combobox('getData');
		var valueField = jq.combobox('options').valueField;
		var textField = jq.combobox('options').textField;
		var flag = false;
		for(var i=0;i<data.length;i++){
			if(data[i][valueField]==value && data[i][textField]==text){
				flag = true;
				break;
			}
		}
        return flag==true?value:undefined;
    }
});

function xyzgrid(d){
	var pathName = window.location.pathname;
	pathName = pathName.substring(pathName.lastIndexOf("/")+1,pathName.lastIndexOf("."));
	var keyCode = d.table+"_"+pathName;
	if($("#toolsDiv_"+d.table).length==0){
		var toolsDiv = '<div id="toolsDiv_'+d.table+'">'
		 + '<a href="#" class="icon-add" title="点我显示列" onclick="xyzDataGridShowCellToolFunction(\''+d.table+'\')"></a>'
		 + '<a href="#" class="icon-remove" title="点我隐藏列" onclick="xyzDataGridHideCellToolFunction(\''+d.table+'\')"></a>'
		 + '<a href="#" class="icon-lock" title="点我保存格式" onclick="xyzDataGridSaveFormatToolFunction(\''+d.table+'\',\''+keyCode+'\')"></a>'
		 + '</div>';
		$("#"+d.table).before(toolsDiv);
	}
	
	/**重新规划显示、隐藏**/
	{
		d.frozenColumns = xyzIsNull(d.frozenColumns)||d.frozenColumns==[]?[[]]:d.frozenColumns;
		d.columns = xyzIsNull(d.columns)||d.columns==[]?[[]]:d.columns;
		for(var q=0;q<d.frozenColumns.length;q++){
			for(var qq=0;qq<d.frozenColumns[q].length;qq++){
				if(!xyzIsNull(d.frozenColumns[q][qq].title)){
					d.frozenColumns[q][qq].title="<div style='font-weight:bold;'>"+d.frozenColumns[q][qq].title+"</div>";
				}
			}
		}
		for(var q=0;q<d.columns.length;q++){
			for(var qq=0;qq<d.columns[q].length;qq++){
				if(!xyzIsNull(d.columns[q][qq].title)){
					d.columns[q][qq].title="<div style='font-weight:bold;'>"+d.columns[q][qq].title+"</div>";
				}
			}
		}
		{
			var userOpersValue = xyzGetUserOpers(keyCode);
			if(!xyzIsNull(userOpersValue)){
				var hiddenCols = userOpersValue.split("#####");
				if(!xyzIsNull(hiddenCols[0])){
					var hiddenCs = hiddenCols[0].split(",");
					for(var q=0;q<d.frozenColumns.length;q++){
						for(var qq=0;qq<d.frozenColumns[q].length;qq++){
							var pH = false;
							for(var p=0;p<hiddenCs.length;p++){
								if(d.frozenColumns[q][qq].field==hiddenCs[p]){
									pH = true;
								}
							}
							d.frozenColumns[q][qq].hidden=pH;
						}
					}
					for(var q=0;q<d.columns.length;q++){
						for(var qq=0;qq<d.columns[q].length;qq++){
							var pH = false;
							for(var p=0;p<hiddenCs.length;p++){
								if(d.columns[q][qq].field==hiddenCs[p] && d.columns[q].hidden!=true){
									pH = true;
								}
							}
							d.columns[q][qq].hidden=pH;
						}
					}
				}
				if(!xyzIsNull(hiddenCols[1])){
					var pp = parseInt(hiddenCols[1]);
					d.pageSize = d.pageSize==undefined?pp:d.pageSize;
				}
			}
		}
	}
	
	$("#"+d.table).datagrid({
		pageNumber : d.pageNumber==undefined?1:d.pageNumber,
		pageSize : d.pageSize==undefined?10:d.pageSize,
		title : d.title==undefined?undefined:d.title,
		sortName : d.sortName==undefined?'':d.sortName,
		sortOrder : d.sortOrder==undefined?'':d.sortOrder,
		collapsible : d.collapsible==undefined?true:d.collapsible,//默认有折叠按钮
		collapsed : d.collapsed==undefined?false:d.collapsed,//定义初始化的时候是否折叠
		url : d.url==undefined?undefined:d.url,
		data : d.data==undefined?undefined:d.data,
		showFooter : d.showFooter==undefined?undefined:d.showFooter,
		autoRowHeight:false,
		
		view:d.view?detailview:undefined,
		detailFormatter:d.detailFormatter!=undefined?d.detailFormatter:function(rowIndex, rowData){
			var tableId = d.table;
			var fields1 = $('#'+tableId).datagrid('getColumnFields',true);
			var fields2 = $('#'+tableId).datagrid('getColumnFields');
			var fields = [];
			for(var i=0; i<fields1.length; i++){
				var field = fields1[i];
				var col = $('#'+tableId).datagrid('getColumnOption', field);
				if(col.hidden==true && col.checkbox!=true){
					fields[fields.length]=field;
				}
			}
			for(var i=0; i<fields2.length; i++){
				var field = fields2[i];
				var col = $('#'+tableId).datagrid('getColumnOption', field);
				if(col.hidden==true && col.checkbox!=true){
					fields[fields.length]=field;
				}
			}
			
			var resultStr = "<div style='margin-top:15px;margin-bottom:15px;'><table><tr style='height:28px;'>";
			if(fields.length<10){
				for(var i=0; i<fields.length; i++){
					var field = fields[i];
					var col = $('#'+tableId).datagrid('getColumnOption', field);
					resultStr += "<td>"+col.title+"</td>";
				}
				resultStr+="</tr><tr style='height:28px;'>";
				for(var i=0; i<fields.length; i++){
					var field = fields[i];
					var col = $('#'+tableId).datagrid('getColumnOption', field);
					resultStr += "<td>";
					resultStr += col.formatter?col.formatter(rowData[col.field],rowData):rowData[col.field];
					resultStr += "</td>";
				}
			}else{
				var pppppp = Math.ceil(fields.length/2);
				for(var i=0; i<pppppp;i++){
					var field = fields[i];
					var col = $('#'+tableId).datagrid('getColumnOption', field);
					resultStr += "<td>"+col.title+"</td>";
				}
				resultStr+="</tr><tr style='height:28px;'>";
				for(var i=0; i<pppppp; i++){
					var field = fields[i];
					var col = $('#'+tableId).datagrid('getColumnOption', field);
					resultStr += "<td>";
					resultStr += col.formatter?col.formatter(rowData[col.field],rowData):rowData[col.field];
					resultStr += "</td>";
				}
				resultStr+="</tr><tr style='height:18px;'></tr><tr style='height:28px;'>";
				for(var i=pppppp; i<fields.length;i++){
					var field = fields[i];
					var col = $('#'+tableId).datagrid('getColumnOption', field);
					resultStr += "<td>"+col.title+"</td>";
				}
				resultStr+="</tr><tr style='height:28px;'>";
				for(var i=pppppp; i<fields.length; i++){
					var field = fields[i];
					var col = $('#'+tableId).datagrid('getColumnOption', field);
					resultStr += "<td>";
					resultStr += col.formatter?col.formatter(rowData[col.field],rowData):rowData[col.field];
					resultStr += "</td>";
				}
			}
			
			
			resultStr+="</tr></table></div>";
			return resultStr;
		},
		onExpandRow:d.onExpandRow!=undefined?d.onExpandRow:undefined,
		
		rowStyler:function(index,row){
			return "height:"+(xyzIsNull(d.rowHeight)?36:d.rowHeight)+"px;";
		},
		toolbar : d.toolbar==undefined?'':$.isArray(d.toolbar)?d.toolbar:"#"+d.toolbar,
		loadFilter : d.loadFilter==undefined?function(data){
				if(data.status==1){
					return data.content;
				}else{
					top.$.messager.alert("警告",data.msg,"warning");
					return {total : 0 , rows : []};
				}
			}:d.loadFilter,
		nowrap : d.nowrap==undefined?true:d.nowrap,//是否不换行，true不换行
		border : d.border==undefined?true:d.border,//边框
		height : d.height==undefined?xyzGetHeight($("#"+d.table).position().top+42):d.height,//高度
		width : d.width==undefined?undefined:d.width,//高度
		singleSelect : d.singleSelect==undefined?true:d.singleSelect,//单选
		fitColumns : d.fitColumns==undefined?false:d.fitColumns,//自适应
		striped : d.striped==undefined?false:d.striped,//斑马线
		pagination : d.pagination==undefined?true:d.pagination,//分页
		pagePosition : d.pagePosition==undefined?"bottom":d.pagePosition,//分页条文职
		rownumbers : d.rownumbers==undefined?true:d.rownumbers,//行号
		checkOnSelect : d.checkOnSelect==undefined?true:d.checkOnSelect,//点行选框
		selectOnCheck : d.selectOnCheck==undefined?true:d.selectOnCheck,//点框选行
		pageList : d.pageList==undefined?[3,5,8,10,15,20,30,50]:d.pageList,//分页
		idField : d.idField==undefined?'numberCode':d.idField,
		columns : d.columns,
		frozenColumns : d.frozenColumns==undefined?undefined:d.frozenColumns,//冻结列
		queryParams : d.queryParams==undefined?undefined:d.queryParams,//查询条件
		onCheck : d.onCheck==undefined?undefined:d.onCheck,
		onDblClickCell : d.onDblClickCell==undefined?undefined:d.onDblClickCell,
		onClickCell : d.onClickCell==undefined?undefined:d.onClickCell,
		tools:'#toolsDiv_'+d.table,
		onLoadSuccess : function(data){
			if(d.onLoadSuccess!=undefined){
				d.onLoadSuccess(data);
			}
		},
		onBeforeLoad : function(param){
			$("#"+d.table).datagrid("clearChecked");
			$("#"+d.table).datagrid("clearSelections");
		},
		onSelect : function(rowIndex,rowData){
			top.$('#remarkGreatDiv').accordion("select","通用备注");
			if(rowData.remark!=undefined){
				top.$("#remarkTop").text(rowData.remark);
			}
			if(!xyzIsNull(d.onSelect)){
				d.onSelect(rowIndex,rowData);
			}
		}
	});//datagrid() end
};//cgrid end

function xyzdialog(d){
	$("body").append("<div id='"+d.dialog+"'></div>");
	var height = d.height==undefined?200:d.height;
	height = height>$(window).height()-40?$(window).height()-40:height;
	var width = d.width==undefined?300:d.width;
	width = width>$(window).width()-20?$(window).width()-20:width;
	$("#"+d.dialog).dialog({
		title : d.title==undefined?'对话框':d.title,
		height : height,
		width : width,
		modal : d.modal==undefined?true:d.modal,//锁住当前页面
		closable : d.closable==undefined?false:d.closable,//关闭
	    cache : d.cache==undefined?false:d.cache,//缓存
	    fit : d.fit==undefined?true:d.fit,//全屏
	    href : d.href==undefined?undefined:d.href,
	    content : d.content==undefined?undefined:d.content,
	    buttons : d.buttons==undefined?undefined:d.buttons,
		onLoad : d.onLoad==undefined?undefined:d.onLoad,
		onOpen : d.onOpen==undefined?undefined:d.onOpen,
		resizable : d.resizable==undefined?false:d.resizable,
		draggable : d.draggable==undefined?false:d.draggable
	});
	//set center
	if(d.center != undefined || d.center == true){
		$("#"+d.dialog).dialog("center");
	}
};//cdialog end

/*
 * combobox必须
 * url必须 
 * lazy可选:延迟加载,默认延迟
 * valueField可选
 * textField可选
 */
function xyzCombobox(c){
	var xyzComboboxData = {};
	xyzComboboxData.valueField = c.valueField==undefined?'value':c.valueField;
	xyzComboboxData.textField = c.textField==undefined?'text':c.textField;
	xyzComboboxData.loadFilter = function(data){
		if(data instanceof Array){
			return data;
		}else{
			if(data.status==1){
				return data.content;
			}else{
				return [];
			}
		}
	};
	var xyzComboboxLazy = c.lazy==undefined?true:c.lazy;
	if(xyzComboboxLazy || c.mode=='remote'){
		xyzComboboxData.onClickIcon = function(index){
			var ttps = $(this).combobox("getIcon",index).attr("class");
			if(ttps.indexOf("combo-arrow")>-1){
				$(this).combobox("reload",c.url);
			}
		};
	}
	
	if(xyzComboboxLazy && c.mode!='remote'){
		xyzComboboxData.onShowPanel = function(){
			if($(this).combobox("getData").length==0){
				$(this).combobox("reload",c.url);
			}
			if(c.onShowPanel!=undefined){
				c.onShowPanel();
			}
		};
	}else{
		xyzComboboxData.url = c.url;
		xyzComboboxData.onShowPanel = c.onShowPanel==undefined?undefined:c.onShowPanel;
	}
	xyzComboboxData.onShowPanel = c.onShowPanel==undefined?undefined:c.onShowPanel;
	xyzComboboxData.onBeforeLoad = c.onBeforeLoad==undefined?undefined:c.onBeforeLoad;
	xyzComboboxData.onLoadSuccess = c.onLoadSuccess==undefined?undefined:c.onLoadSuccess;
	xyzComboboxData.onSelect = c.onSelect==undefined?undefined:c.onSelect;
	xyzComboboxData.onChange = c.onChange==undefined?undefined:c.onChange;
	xyzComboboxData.onHidePanel = c.onHidePanel==undefined?undefined:c.onHidePanel;
	xyzComboboxData.required = c.required==undefined?false:c.required;
	xyzComboboxData.editable = c.editable==undefined?true:c.editable;
	xyzComboboxData.multiple = c.multiple==undefined?false:c.multiple;
	xyzComboboxData.mode = c.mode==undefined?'local':c.mode;
	xyzComboboxData.icons = c.icons==undefined?undefined:c.icons;
	xyzComboboxData.panelHeight = 'auto';
	xyzComboboxData.panelMaxHeight = c.panelMaxHeight==undefined?600:c.panelMaxHeight;
	xyzComboboxData.panelMinHeight = c.panelMinHeight==undefined?20:c.panelMinHeight;
	$('#'+c.combobox).combobox(xyzComboboxData);
}

function xyzTextbox(id){
	$("#"+id).textbox({
		icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).textbox('setValue', '');
			}
		}]
	});
}

$.extend($.fn.validatebox.defaults.rules,{
    trim:{    
        validator: function(value,param){
        	if(param[0]==true){
        		if(/(^\s+)|(\s+$)/.test(value)){
        			return false;
        		}
        	}
        	return true;
        },    
        message:'请删除前后空白'
    }    
});


function xyzGetHeight(height){
	var heightT = parseInt(top.$("#centerContentTabs").css("height").split("px")[0]);
	return heightT-height;
}

function xyzGetCurrentRow(table,field,value){
	var rows = $("#"+table).datagrid("getRows");
	var rowT = [];
	for(var i=0;i<rows.length;i++){
		var row = rows[i];
		if(row[field]==value){
			rowT [rowT.length] = row;
		}
	}
	if(rowT.length!=1){
		return null;
	}else{
		return rowT[0];
	}
}

function xyzAddComboboxRow(div,record){
	var dataT = $("#"+div).combobox("getData");
	dataT.splice(0,0,record);
	$("#"+div).combobox("loadData",dataT);
}

function xyzDeleteComboboxRow(div,value){
	var dataT = $("#"+div).combobox("getData");
	var dataN = [];
	for(var ppp in dataT){
		if(dataT[ppp].value!=value){
			dataN[dataN.length] = dataT[ppp];
		}
	}
	$("#"+div).combobox("loadData",dataN);
}

function xyzOnChangeGetText(div,value){
	var result = "";
	var dataT = $("#"+div).combobox("getData");
	for(var ppp in dataT){
		if(dataT[ppp].value==value){
			result = dataT[ppp].text;
		}
	}
	return result;
}

function xyzDataGridShowCellToolFunction(tableId,e){
	//window.event适用于IE chrome 内核的浏览器 arguments.callee.caller.arguments[0]适用于FF
	var e = window.event || arguments.callee.caller.arguments[0];
	//e.pageX适用于FF chrome 内核的浏览器 e.x适用于IE
	var pageX = e.pageX || e.x;
	var pageY = e.pageY || e.y;
	
	if($('#columnMenuAdd_'+tableId).attr("id")){
		$('#columnMenuAdd_'+tableId).menu("destroy");
	}
	$('body').append('<div id="columnMenuAdd_'+tableId+'"/>');
	var cmenu = $('#columnMenuAdd_'+tableId);
	cmenu.menu({
		onClick: function(item){
			$('#'+tableId).datagrid('showColumn', item.name);
			cmenu.menu("destroy");
		}
	});
	var fields = $('#'+tableId).datagrid('getColumnFields',true);
	var fields2 = $('#'+tableId).datagrid('getColumnFields');
	for(var p=0; p<fields2.length; p++){
		fields[fields.length]=fields2[p];
	}
	for(var i=0; i<fields.length; i++){
		var field = fields[i];
		var col = $('#'+tableId).datagrid('getColumnOption', field);
		if(col.hidden==true && col.checkbox!=true){
			cmenu.menu('appendItem', {
				text: col.title,
				name: field,
				iconCls: 'icon-empty'
			});
		}
	}
	$('#columnMenuAdd_'+tableId).menu('show', {    
		left:pageX,
		top:pageY
	});
}

function xyzDataGridHideCellToolFunction(tableId){
	//window.event适用于IE chrome 内核的浏览器 arguments.callee.caller.arguments[0]适用于FF
	var e = window.event || arguments.callee.caller.arguments[0];
	//e.pageX适用于FF chrome 内核的浏览器 e.x适用于IE
	var pageX = e.pageX || e.x;
	var pageY = e.pageY || e.y;
	
	if($('#columnMenuSub_'+tableId).attr("id")){
		$('#columnMenuSub_'+tableId).menu("destroy");
	}
	$('body').append('<div id="columnMenuSub_'+tableId+'"/>');
	var cmenu = $('#columnMenuSub_'+tableId);
	cmenu.menu({
		onClick: function(item){
			$('#'+tableId).datagrid('hideColumn', item.name);
			cmenu.menu("destroy");
		}
	});
	var fields = $('#'+tableId).datagrid('getColumnFields',true);
	var fields2 = $('#'+tableId).datagrid('getColumnFields');
	for(var p=0; p<fields2.length; p++){
		fields[fields.length]=fields2[p];
	}
	for(var i=0; i<fields.length; i++){
		var field = fields[i];
		var col = $('#'+tableId).datagrid('getColumnOption', field);
		if(col.hidden!=true && col.checkbox!=true){
			cmenu.menu('appendItem', {
				text: col.title,
				name: field,
				iconCls:'icon-ok'
			});
		}
	}
	$('#columnMenuSub_'+tableId).menu('show', {    
		left:pageX,
		top:pageY 
	});
}

function xyzDataGridSaveFormatToolFunction(tableId,keyCode){
	var contentT = [];
	var fields = $('#'+tableId).datagrid('getColumnFields',true);
	var fields2 = $('#'+tableId).datagrid('getColumnFields');
	for(var p=0; p<fields2.length; p++){
		fields[fields.length]=fields2[p];
	}
	for(var i=0; i<fields.length; i++){
		var field = fields[i];
		var col = $('#'+tableId).datagrid('getColumnOption', field);
		if(col.hidden==true){
			contentT[contentT.length]=fields[i];
		}
	}
	var content = contentT.join(",");
	
	var pageSize = $('#'+tableId).datagrid("options").pageSize;
	if(pageSize!=10){
		pageSize = pageSize>15?15:pageSize;
		content += "#####"+pageSize;
	}
	$.ajax({
		url : "../UserOperWS/addUserOper.do",
		type : "POST",
		data : {
			keyCode : keyCode,
			content : content
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功！下次登录或全页刷新后将按当前列展示。","info");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function xyzProgressBar(id, time){
	xyzdialog({
		dialog : 'dialogFormDiv_xyzProgressBar_'+id,
		title : '进度',
		fit:false,
		width:400,
		height:70,
	    content : '<div id="xyzProgressBar_'+id+'" style="width:400px;"></div>',
	    onOpen:function(){
	    	$('#xyzProgressBar_'+id).progressbar({
	    		width:384,
	    		height:34,
	    		value: 0
	    	});
	    	
	    	var functionTimeout = null;
	    	functionTimeout = function(){
	    		try{ //防止dialog已摧毁, 获取值时 报错!
	    			var value = $('#xyzProgressBar_'+id).progressbar('getValue');
		    		value += Math.floor(Math.random() * (100/time)/5);
		    		if (value <= 99){
			    		$('#xyzProgressBar_'+id).progressbar('setValue', value);
			    		setTimeout(functionTimeout,100);
			    	}
	    		}catch(e){
	    			;
	    		}
	    	};
	    	functionTimeout();
	    }
	});
}

function xyzProgressBar(param){
	
	var id = param.id;
	var time = param.time;
	
	xyzdialog({
		dialog : 'dialogFormDiv_xyzProgressBar_'+id,
		title : '进度',
		fit:false,
		width:400,
		height:70,
	    content : '<div id="xyzProgressBar_'+id+'" style="width:400px;"></div>',
	    onOpen:function(){
	    	$('#xyzProgressBar_'+id).progressbar({
	    		width:384,
	    		height:34,
	    		value: 0
	    	});
	    	
	    	var functionTimeout = null;
	    	functionTimeout = function(){
	    		try{ //防止dialog已摧毁, 获取值时 报错!
	    			var value = $('#xyzProgressBar_'+id).progressbar('getValue');
		    		value += Math.floor(Math.random() * (100/time)/5);
		    		if (value <= 99){
			    		$('#xyzProgressBar_'+id).progressbar('setValue', value);
			    		setTimeout(functionTimeout,100);
			    	}
	    		}catch(e){
	    			;
	    		}
	    	};
	    	
	    	functionTimeout();
	    	
	    	if('func' in param){
	    		param.func();
	    	}
	    }
	});
}

function xyzCloseProgressBar(id){
	$('#xyzProgressBar_'+id).progressbar('setValue', 100);
	setTimeout(function(){
		$("#dialogFormDiv_xyzProgressBar_"+id).dialog("destroy");
	},200);
}

function xyzGetPrice(price){
	return xyzIsNull(price)?0:(price / 100).toFixed(2);
}

function xyzSetPrice(price){
	return xyzIsNull(price)?0:(price * 100).toFixed(0);
}


(function(window){

	var yqqQueryPlugin = {}; //查询对象

	yqqQueryPlugin.data = {}; //存放数据的地方
	
	//关闭的图片
	yqqQueryPlugin.imgBase64 = "data:image/gif;base64,R0lGODlhCAAIANUAAP8HB/8NDf8MDP8eHv/S0v97e//a2v/h4f/l5f8KCv/Z2f+9vf+/v/9UVP/Hx/+wsP8YGP+oqP8REf/f3/9hYf8nJ/9gYP/g4P/k5P8GBv+iov+Vlf8fH/+MjP+rq/8vL/+urv8lJf+2tv8DA//o6P8BAf9tbf+Zmf8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M2IChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo1N0EzOTBFODFDQkExMUU2QUMyMjk4MDk0MEE0NzE3MSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo1N0EzOTBFOTFDQkExMUU2QUMyMjk4MDk0MEE0NzE3MSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjU3QTM5MEU2MUNCQTExRTZBQzIyOTgwOTQwQTQ3MTcxIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjU3QTM5MEU3MUNCQTExRTZBQzIyOTgwOTQwQTQ3MTcxIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+Af/+/fz7+vn49/b19PPy8fDv7u3s6+rp6Ofm5eTj4uHg397d3Nva2djX1tXU09LR0M/OzczLysnIx8bFxMPCwcC/vr28u7q5uLe2tbSzsrGwr66trKuqqainpqWko6KhoJ+enZybmpmYl5aVlJOSkZCPjo2Mi4qJiIeGhYSDgoGAf359fHt6eXh3dnV0c3JxcG9ubWxramloZ2ZlZGNiYWBfXl1cW1pZWFdWVVRTUlFQT05NTEtKSUhHRkVEQ0JBQD8+PTw7Ojk4NzY1NDMyMTAvLi0sKyopKCcmJSQjIiEgHx4dHBsaGRgXFhUUExIREA8ODQwLCgkIBwYFBAMCAQAAIfkEAAAAAAAsAAAAAAgACAAABjJATwZFRDVIIFGCaEF8RhpGwHSpEEubg4JTLHVIBAhxdHJICoYBKrIQEE2T0ANQRFEwQQA7";
	
	yqqQueryPlugin.create = function(data){
		
		var query = {
			autoId : (data.autoId == null ? undefined : data.autoId), //定义区域参数
			leftId : (data.leftId == null ? "query_Id" : data.leftId), //自定义摆放参数  -- 左边ID参数
			rightId : (data.rightId == null ? "query_rightId" : data.rightId), //自定义摆放参数  -- 右边ID参数
			selectId : (data.selectId == null ? "query_selectId" : data.selectId), //自定义摆放参数  -- 选中值区域参数
			pluginId : (data.pluginId == null ? undefined : data.pluginId), //标识id  //必传
			showSearch : (data.showSearch == null ? "all" : data.showSearch), //展示那个选择框  default展示默认文本框  tool展示工具  
			data : data.data == null ? [{}] : data.data, //查询数据
		};
		
		if(query.pluginId == null || query.pluginId == "" || query.pluginId == undefined){ //判断是否传参数
			alert("pluginId必传！请填写pluginId参数");
			return false; 
		}
		yqqQueryPlugin.data[query.pluginId] = {}; // 创建全局变量  以pluginId 作为识别
		yqqQueryPlugin.data[query.pluginId].q = query; //将query存入全局变量中  以json的形式   pluginId 为key

		//控制展示某个搜索框
		if(query.showSearch == "all"){
			yqqQueryPlugin.searchTool(query);
			yqqQueryPlugin.searchDefault(query);
		}else if(query.showSearch == "tool"){
			yqqQueryPlugin.searchTool(query);
		}else if(query.showSearch == "default"){
			yqqQueryPlugin.searchDefault(query);
		}
		
	};
	
	//生成工具
	yqqQueryPlugin.searchTool = function(query){
		//将用户传入的参数带上表示符
		if(yqqQueryPlugin.data[query.pluginId].q.autoId != null && "" != yqqQueryPlugin.data[query.pluginId].q.autoId && yqqQueryPlugin.data[query.pluginId].q.autoId != undefined){  //自动化
			
			yqqQueryPlugin.data[query.pluginId].leftId = query.leftId + "_" + query.pluginId; //左侧id
			yqqQueryPlugin.data[query.pluginId].rightId = query.rightId + "_" + query.pluginId; //右侧id
			yqqQueryPlugin.data[query.pluginId].selectId = query.selectId + "_" + query.pluginId; //选中id
			
			document.getElementById(yqqQueryPlugin.data[query.pluginId].q.autoId).style.position = "relative";
			
			var ati_html = '&nbsp;<input type="text" id="'+yqqQueryPlugin.data[query.pluginId].leftId+'"/>';
			ati_html += '&nbsp;<div style="display:inline;" id="'+yqqQueryPlugin.data[query.pluginId].rightId+'">';
			ati_html += '</div>';
			
			$("#" + yqqQueryPlugin.data[query.pluginId].q.autoId).append(ati_html);
			var selectsCss = "margin-top:2px; border : 1px solid #dcdcdc;border-radius: 6px;background: -webkit-linear-gradient(#f6f6f6, #ebebeb);background: -o-linear-gradient(#f6f6f6, #ebebeb);background: -moz-linear-gradient(#f6f6f6, #ebebeb);background: linear-gradient(#f6f6f6, #ebebeb);background:#f6f6f6";
			var abs_html = '<div style="position: absolute;z-index: 100;top: 35px;left: 100px;">';
			//div 样式 
			abs_html += '<div style="line-height:25px; display:none;float:right; '+selectsCss+'" id="'+yqqQueryPlugin.data[query.pluginId].selectId+'"></div>';
			abs_html += '</div>';
			$("#" + yqqQueryPlugin.data[query.pluginId].q.autoId).after(abs_html);
		}else{
			yqqQueryPlugin.data[query.pluginId].leftId = yqqQueryPlugin.data[query.pluginId].q.leftId; //左侧id
			yqqQueryPlugin.data[query.pluginId].rightId = yqqQueryPlugin.data[query.pluginId].q.rightId; //右侧id
			yqqQueryPlugin.data[query.pluginId].selectId = yqqQueryPlugin.data[query.pluginId].q.selectId; //选中id
		}
		
		var right_html = '<div style="display:inline" id="'+yqqQueryPlugin.data[query.pluginId].rightId+'_left"><input style="width : 140px;" type="text" id="'+yqqQueryPlugin.data[query.pluginId].rightId+'_text"/></div><div style="display:inline" id="'+yqqQueryPlugin.data[query.pluginId].rightId+'_right"></div>';

		$("#" + yqqQueryPlugin.data[query.pluginId].rightId).append(right_html);// 创建右侧input

		$('#' + yqqQueryPlugin.data[query.pluginId].rightId + '_text').textbox({
			icons : [{
		    	iconCls : 'icon-clear',
				handler : function(e){
					$(e.data.target).textbox('clear');
				}
		    }],
			onChange : function(newValue, oldValue){
				if(newValue == null || newValue == "" || newValue == undefined){
					return false;
				}
				yqqQueryPlugin.changeListener(query.pluginId);
			}
		}); //初始化  创建一个文本框  
		$('#' + yqqQueryPlugin.data[query.pluginId].rightId + '_combo').combobox(); //初始化  创建一个下拉框  
		
		$("#" + yqqQueryPlugin.data[query.pluginId].rightId + "_right").hide();
		
		var clearCss0 = "float:left;padding-left: 10px;padding-right: 10px; background: #fafafa;border-right:1px solid #dcdcdc; display: inline-block;color: #f00;cursor: pointer;";
		
		var save_select_html = "";
		save_select_html += '<span style="'+clearCss0+'">已选条件</span>';
		save_select_html += '<input type="hidden" id="'+yqqQueryPlugin.data[query.pluginId].selectId+'_save" /><div id="'+yqqQueryPlugin.data[query.pluginId].selectId+'_sp" style="float:left;"><ul style="margin : 0; height : 0; padding: 0; padding-left: 10px;padding-right: 10px;"></ul></div>';

		var clearCss = "float:left;padding-left: 10px;padding-right: 10px; background: #fafafa; border-left:1px solid #dcdcdc;display: inline-block;color: #f00;cursor: pointer;";
		save_select_html += '<span style="'+clearCss+'" onclick="yqqQueryPlugin.emptyValues(\''+query.pluginId+'\')">清空</span>';
		
		save_select_html += '<div style="clear:both;"></div>';
		
		$("#"+yqqQueryPlugin.data[query.pluginId].selectId).append(save_select_html); //添加隐藏域和展示选中的值

		(function(query){  //创建左侧下拉框
			var comboboxArr = [];
			var dv = "";
			
			for(var i = 0 ; i < query.data.length ; i++){
				var list = query.data[i];
				comboboxArr[comboboxArr.length] = {"id" : list.key, "text" : list.value};
			}
			
			if(comboboxArr.length > 0){
				dv = comboboxArr[0].id;
			}
			
			$("#" + yqqQueryPlugin.data[query.pluginId].leftId).combobox({    //创建easyui combobox
			    data : comboboxArr,    
			    valueField:'id',    
			    textField:'text',
			    width : '155',
			    panelHeight : 'auto',
			    panelMaxHeight : 600,
			    panelMinHeight : 20,
			    icons : [{
			    	iconCls : 'icon-clear',
					handler : function(e){
						$(e.data.target).combobox('clear');
					}
			    }],
			    onChange : function(){
			    	var value = $("#" + yqqQueryPlugin.data[query.pluginId].leftId).combobox("getValue");
			    	var l = {};
					for (var i = 0 ; i < query.data.length ; i++) {
						var list = query.data[i];
						if(list.key == value){
							l = list;
						}
					};

					yqqQueryPlugin.data[query.pluginId].list = l; //将选中的值赋给对象中list属性
					
					$('#' + yqqQueryPlugin.data[query.pluginId].rightId + '_text').textbox("clear");
					
					yqqQueryPlugin.createRight(query.pluginId);
			    }
			});  
			
			$("#" + yqqQueryPlugin.data[query.pluginId].leftId).combobox("select",dv);

		})(yqqQueryPlugin.data[query.pluginId].q);
	};
	
	yqqQueryPlugin.searchDefault = function(query){
		var default_html = '&nbsp;<input style="width : 150px;" type="text" id="'+yqqQueryPlugin.data[query.pluginId].q.autoId+'_default" />';
		
		$("#" + yqqQueryPlugin.data[query.pluginId].q.autoId).append(default_html);
		
		$("#" + yqqQueryPlugin.data[query.pluginId].q.autoId + "_default").textbox({
			prompt : "快捷",
			icons : [{
				iconCls : 'icon-clear',
				handler : function(e){
					$(e.data.target).textbox('clear');
				}
			}]
		});
	};
	
	yqqQueryPlugin.createRight = function(pluginId){  //生成右侧文本框
		if(yqqQueryPlugin.data[pluginId].list.type == 'combobox'){  //下拉框 进入这个判断
			
			var combobox = document.getElementById(yqqQueryPlugin.data[pluginId].rightId + '_combo');
			
			if(combobox != null && combobox != undefined){
				$('#' + yqqQueryPlugin.data[pluginId].rightId + '_combo').combobox("destroy");
			}
			
			//生成combobox所需的html
			var combobox_input = '<input style="width : 140px;" type="text" id="'+yqqQueryPlugin.data[pluginId].rightId+'_combo" />';
			$("#" + yqqQueryPlugin.data[pluginId].rightId + "_right").append(combobox_input);
			
			if(yqqQueryPlugin.data[pluginId].list.data == null || yqqQueryPlugin.data[pluginId].list.data == "" || yqqQueryPlugin.data[pluginId].list.data == undefined){
				alert("请传入combobox的参数！");
				return false;
			}
			
			yqqQueryPlugin.data[pluginId].list.data.combobox = yqqQueryPlugin.data[pluginId].rightId + '_combo';
			
			yqqQueryPlugin.data[pluginId].list.data.icons = [{
		    	iconCls : 'icon-clear',
				handler : function(e){
					$(e.data.target).combobox('clear');
				}
		    }];
			
			yqqQueryPlugin.data[pluginId].list.data.onChange = function(){ // 使参数绑定值选中事件  将值赋给结果集中去
				yqqQueryPlugin.changeListener(pluginId);
			};
			
			//隐藏普通文本框 显示下拉框
			$("#" + yqqQueryPlugin.data[pluginId].rightId + "_left").hide();
			$("#" + yqqQueryPlugin.data[pluginId].rightId + "_right").show();
			
			yqqQueryPlugin.xyzCombobox(yqqQueryPlugin.data[pluginId].list.data); //调用customUi.js 里面的方法

		}else{
			//隐藏下拉框  显示文本框
			$("#" + yqqQueryPlugin.data[pluginId].rightId + "_left").show();
			$("#" + yqqQueryPlugin.data[pluginId].rightId + "_right").hide();
		}
	};
	
	yqqQueryPlugin.changeListener = function(pluginId){
		
		var val = $("#" + yqqQueryPlugin.data[pluginId].selectId + "_save").val();

		var selects = [];

		if(val == undefined || val == null || (val+"".trim())==="" ||  (val+"".trim())===''){
		}else{
			selects = JSON.parse(val); //存值的对象
		}

		var list = yqqQueryPlugin.data[pluginId].list;	
		
		if(list == null || list == undefined || yqqQueryPlugin.isEmptyObject(list)){
			top.$.messager.alert("提示","请选择查询类型！","info");
			return;
		}
		
		if(list.type == "combobox"){

			var k = $("#" + yqqQueryPlugin.data[pluginId].rightId + "_combo").combobox("getValues");
			var v = $("#" + yqqQueryPlugin.data[pluginId].rightId + "_combo").combobox("getText");
			
			if(v.trim() == "" || v.trim() == null){
				return;
			}
			
			var f = true;
			for(var i = 0 ; i < selects.length ; i++){
				if(list.key == selects[i].leftKey){
					selects[i] = {  //将这次选中的值存入集合中
						leftKey : list.key,
						leftValue : list.value,
						rightKey : k.join(","),
						rightValue : v
					};
					f = false;
				}
			};
			
			if(f){
				selects[selects.length] = {  //将这次选中的值存入集合中
					leftKey : list.key,
					leftValue : list.value,
					rightKey : k.join(","),
					rightValue : v
				};
			}
			
		}else{

			var v = $("#" + yqqQueryPlugin.data[pluginId].rightId + "_text").textbox("getValue");

			if(v == null || v == undefined || v.replace(/(^\s*)|(\s*$)/g, "") == ""){
				return false;
			}
			
			var f = true;
			
			for(var i = 0 ; i < selects.length; i++){
				if(list.key == selects[i].leftKey){
					selects[i] = {
						leftKey : list.key,
						leftValue : list.value,
						rightKey : v,
						rightValue : v
					};
					f = false;
				};
			}

			if(f){
				selects[selects.length] = {  //将这次选中的值存入集合中
						leftKey : list.key,
						leftValue : list.value,
						rightKey : '',
						rightValue : v
				};		
			}
		}

		$("#" + yqqQueryPlugin.data[pluginId].selectId + "_save").val(JSON.stringify(selects)); //存入隐藏域中
		
		/**
		 * 改变文本的值
		 */
		$("#" + yqqQueryPlugin.data[pluginId].selectId + "_sp > ul").html("");//清空文本

		yqqQueryPlugin.createShowValuesHtml(pluginId,selects); //生成展示值的方法
	};

	yqqQueryPlugin.getValue = function(pluginId){
		
		if(pluginId == null || pluginId == "" || pluginId == undefined){
			alert("请传入pluginId!!!");
			return false;
		}

		var rev = {};
		
		var getToolValues = function(pluginId){
			var sArr = yqqQueryPlugin.data[pluginId].s;
			
			if(sArr == null || sArr.length < 1){
				return "";
			}
			
			var toolJson = {};
			
			for(var i = 0 ; i < sArr.length ; i++){
				var list = sArr[i];

				toolJson[list.leftKey] = {
						"id" : list.leftKey,
						"text" : list.leftValue,
						"queryId" : ((list.rightKey == null || list.rightKey == "") ? list.rightValue : list.rightKey).trim(),
						"queryText" : list.rightValue.trim()
				};
			}
			return toolJson;
		};
		
		var getDefaultValues = function(pluginId){
			var defaultValue = $("#" + yqqQueryPlugin.data[pluginId].q.autoId + "_default").textbox("getValue");
			if(xyzIsNull(defaultValue)){
				return "";
			}else{
				return {
						"id" : "queryCore",
						"text" : "",
						"queryId" : defaultValue.trim(),
						"queryText" : defaultValue.trim()
				};
			}
		};
		if(xyzIsNull(yqqQueryPlugin.data[pluginId].q.showSearch) || yqqQueryPlugin.data[pluginId].q.showSearch=="all"){
			var dp = getToolValues(pluginId);
			if(!xyzIsNull(dp)){
				rev = dp;
			}
			var dj = getDefaultValues(pluginId);
			if(!xyzIsNull(dj)){
				rev.queryCore = dj;
			}
		}else if(yqqQueryPlugin.data[pluginId].q.showSearch == "tool"){
			rev = getToolValues(pluginId);
		}else if(yqqQueryPlugin.data[pluginId].q.showSearch == "default"){
			rev.queryCore = getDefaultValues(pluginId);
		}
		if(yqqQueryPlugin.isEmptyObject(rev)){
			return "";
		}else{
			return JSON.stringify(rev);
		}
	};
	
	yqqQueryPlugin.emptyValues = function(pluginId){ //清空所选项
		yqqQueryPlugin.data[pluginId].s = [];
		$("#" + yqqQueryPlugin.data[pluginId].selectId + "_save").val(yqqQueryPlugin.data[pluginId].s); //清空值
		
		$("#" + yqqQueryPlugin.data[pluginId].selectId + "_sp > ul > li" ).remove(); //下面所有的li标签删除掉
		$("#" + yqqQueryPlugin.data[pluginId].selectId).hide();
		$("#" + yqqQueryPlugin.data[pluginId].q.autoId + '_default').textbox("clear");
		if(yqqQueryPlugin.data[pluginId].list.type == "combobox"){
			$("#" + yqqQueryPlugin.data[pluginId].rightId + "_combo").combobox("clear");
		}else {
			$("#" + yqqQueryPlugin.data[pluginId].rightId + '_text').textbox("clear");
		}
	};
	
	yqqQueryPlugin.isEmptyObject = function(e){  
	    for (var t in e) {
	    	t;
	    	return false;
	    }
	    return true;
	};
	
	yqqQueryPlugin.removeFiled = function(pluginId,i){  //删除元素
			
		var currentValue = $("#hiddenValue_"+i).val().split("&");
		
		var index = i;
		
		var sArr = yqqQueryPlugin.data[pluginId].s;
	
		if(xyzIsNull(sArr) || sArr.length < 1){
			alert("未发现该pluginId");
			return false;
		}
		
		for (var i = 0; i < sArr.length; i++) {  //获取当前元素的下标
			if (sArr[i].rightValue == currentValue[1] && sArr[i].leftKey == currentValue[0]) {
				sArr.splice(i, 1); //根据下标删除元素
				$("#" + yqqQueryPlugin.data[pluginId].selectId + "_save").val(JSON.stringify(sArr));
				break;
			}
		}
		$("#liValue_"+index).remove();
	};
	
	yqqQueryPlugin.xyzCombobox = function(c){
		var xyzComboboxData = {};
		xyzComboboxData.valueField = c.valueField==undefined?'value':c.valueField;
		xyzComboboxData.textField = c.textField==undefined?'text':c.textField;
		if(c.url != null && c.url != undefined && c.url != ""){
			xyzComboboxData.url = c.url;
		}else{
			xyzComboboxData.data = c.data;
		}
		xyzComboboxData.loadFilter = function(data){
			if(data instanceof Array){
				return data;
			}else{
				if(data.status==1){
					return data.content;
				}else{
					return [];
				}
			}
		};
		xyzComboboxData.onBeforeLoad = c.onBeforeLoad==undefined?undefined:c.onBeforeLoad;
		xyzComboboxData.onLoadSuccess = c.onLoadSuccess==undefined?undefined:c.onLoadSuccess;
		xyzComboboxData.onSelect = c.onSelect==undefined?undefined:c.onSelect;
		xyzComboboxData.onChange = c.onChange==undefined?undefined:c.onChange;
		xyzComboboxData.onHidePanel = c.onHidePanel==undefined?undefined:c.onHidePanel;
		xyzComboboxData.required = c.required==undefined?false:c.required;
		xyzComboboxData.editable = c.editable==undefined?true:c.editable;
		xyzComboboxData.multiple = c.multiple==undefined?false:c.multiple;
		xyzComboboxData.icons = c.icons==undefined?undefined:c.icons;
		xyzComboboxData.mode = c.mode==undefined?'local':c.mode;
		xyzComboboxData.panelMinHeight=600,
		xyzComboboxData.panelMaxHeight=1000,
		$('#'+c.combobox).combobox(xyzComboboxData);
	};
	
	yqqQueryPlugin.createShowValuesHtml = function(pluginId,data){
		yqqQueryPlugin.data[pluginId].s = data;  //将最新的的值赋给对象 方便后面取值
		
		var rightValue = [];
		var spanHtml = "";
		for(var i = 0 ; i < data.length ; i++){
			rightValue[rightValue.length] = data[i].rightValue;
			
			var str = data[i].rightValue;
			if(data[i].rightValue.length > 9){ //判断字符串是否大于10  大于10 出现省略号
				str = data[i].rightValue.substring(0,9) + "...";
			}
			var ml = "margin-left: 20px;";
			if(i == 0){
				ml = "margin-left: 0px;";
			}
			spanHtml += '<li id="liValue_'+i+'" style="float: left;list-style-type:none;'+ml+'" >';
				spanHtml += '<input type="hidden" id="hiddenValue_'+i+'" value="'+(data[i].leftKey+'&'+data[i].rightValue)+'"/>';
				spanHtml += '<span style="font-size: 13px;float: left;">'+data[i].leftValue+' :</span>&nbsp;<span id="tool_'+pluginId+'_'+i+'">'+str+'</span>';
				spanHtml += '<span style="position:relative;top:-7px;left : 5px;"><img onclick="yqqQueryPlugin.removeFiled(\''+pluginId+'\',\''+i+'\')" src="'+yqqQueryPlugin.imgBase64+'" height="8" width="8" /></span>';
			spanHtml += '</li>';
		}
		spanHtml += '<div style="clear : both; "></div>';
		$("#" + yqqQueryPlugin.data[pluginId].selectId).show();
		$("#" + yqqQueryPlugin.data[pluginId].selectId + "_sp > ul" ).append(spanHtml);
		
		for(var i = 0 ; i < data.length ; i++){
			$('#tool_'+pluginId+'_'+i).tooltip({  //绑定tooltip
				position: 'bottom',    
				content: '<span style="color:#fff">'+data[i].rightValue+'</span>',
				onShow: function(){        
					$(this).tooltip('tip').css({            
						backgroundColor: '#666',            
						borderColor: '#666',       
					});    
				}
			});
		}
	};
	
	yqqQueryPlugin.setValue = function(data){
		var values = yqqQueryPlugin.data[data.pluginId];
		
		if(values == null || values == undefined || values == ""){
			alert("未发现该pluginId！不能设置默认值");
		}
		
		var d = $("#" + values.selectId + "_save").val();
		if(!xyzIsNull(d)){
			var dj = xyzJsonToObject(d);
			var temp = [];
			
			for(var j = 0 ; j < data.values.length ; j++){
				var t = true;
				for(var i = 0; i < dj.length ; i++){
					if(data.values[j].leftKey == dj[i].leftKey){
						dj[i] = data.values[j];
						t = false;
					}
				}
				if(t){
					temp[temp.length] = data.values[j];
				}
			}	
			
			for(var k in temp){
				dj[dj.length] = temp[k];
			}
			$("#" + yqqQueryPlugin.data[data.pluginId].selectId + "_sp > ul > li" ).remove(); //下面所有的li标签删除掉
			data.values = dj;
		}
		$("#" + values.selectId + "_save").val(JSON.stringify(data.values)); //赋值
		yqqQueryPlugin.createShowValuesHtml(data.pluginId,data.values);
	};

	window.yqqQueryPlugin = yqqQueryPlugin;
})(window);