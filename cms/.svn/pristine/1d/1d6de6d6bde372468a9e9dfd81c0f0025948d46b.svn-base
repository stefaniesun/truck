var tkviewNumber = '';
var tempMode = 'sale';
$(document).ready(function() {
	
	var params = getUrlParameters();
	cruiseNumber = params.cruise;
	tkviewMode = params.mode;
	
	xyzCombobox({
		combobox : 'queryCabin',
		url : '../ListWS/getCabinListByCruise.do',
		mode : 'remote',
		lazy : false, 
		onBeforeLoad: function(param){
			param.cruise = cruiseNumber;
	   	}
	});
	xyzTextbox("queryNameCn");
	xyzTextbox("queryMark");
	xyzCombobox({
		combobox : 'mouth',
		url : '../ListWS/getShipmentMouthList.do',
		mode : 'remote',
		multiple : true,
		lazy : false, 
		onBeforeLoad: function(param){
			param.cruise = cruiseNumber;
	   	}
	});
	xyzCombobox({
		combobox : 'shipment',
		url : '../ListWS/getTkviewShipmentList.do',
		mode : 'remote',
		multiple : true,
		lazy : false, 
		onBeforeLoad: function(param){
			param.cruise = cruiseNumber;
			param.mouth = $("#mouth").combobox("getValue");
			param.isTkview = 0;
	   	}
	});
	xyzCombobox({
		combobox : 'provider',
		url : '../ListWS/getProviderList.do',
		mode : 'remote',
		multiple : true,
		lazy : false,
		icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	
	initTable();
	
	/*查询*/
	$("#tkviewQueryButton").click(function(){
		loadTable();
	});
	
	/* 释放全部单品 */
	$("#tkviewReleaseButton").click(function(){
		$.ajax({
			url : "../TkviewWS/editTkviewRelease.do",
			type : "POST",
			data : {
				mode : tkviewMode
			},
			async : false,
			dataType : "json",
			success : function(data) {
				if(data.status==1){
					$("#tkviewManagerTable").datagrid("reload");
					top.$.messager.alert("提示","操作成功!","info");
				}else{
					top.$.messager.alert("警告",data.msg,"warning");
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
			}
		});
	});
	
	/* 自动同步更新 */
	$("#editSkuButton").click(function(){
		autoEditToSkuButton();
	});
	
	/* 批量新增/编辑库存 */
	$("#addStockListButton").click(function(){
		addStockListButton();
	});
	
	/*删除单品*/
	$("#deleteTkvewByCabinButton").click(function(){
		deleteTkvewByCabinButton();
	});
	
	/* 一键实库 */
	$("#realStockButton").click(function(){
		realStockButton();
	});
	
	$('#tkviewManagerTable').datagrid({
		onClickRow : function(rowIndex, rowData){
			tkviewNumber = rowData.numberCode;
			if(!xyzIsNull(tkviewNumber)){
				addTkviewRelease();
			}
		}
	});
	
});

function addTkviewRelease(){
	var str = "";
	if(xyzIsNull(tkviewNumber)){
		return str;
	}
	
	$.ajax({
		url : "../TkviewWS/editOperationData.do",
		type : "POST",
		data : {
			cruise : cruiseNumber,
			tkviewNumber : tkviewNumber,
			mode : tkviewMode
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				str = data.content;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
	return str;
}

function initTable(){
	var toolbarTkview = [];
	if(xyzControlButton("buttonCode_y20161207153901")){
		toolbarTkview[toolbarTkview.length]={
				text: '单个新增',
				iconCls: 'icon-add',
				handler: function(){addTkviewButton();}
		};
	}
	if(xyzControlButton("buttonCode_y20161208105010")){
		toolbarTkview[toolbarTkview.length]='-';
		toolbarTkview[toolbarTkview.length]={
				text: '快捷新增',
				iconCls: 'icon-add',
				handler: function(){addFastlyTkviewButton();}
		};
	}
	if(xyzControlButton("buttonCode_y20161208105010")){
		toolbarTkview[toolbarTkview.length]='-';
		toolbarTkview[toolbarTkview.length]={
				text: '完整新增',
				iconCls: 'icon-edit',
				handler: function(){addQuickTkviewButton();}
		};
	}
	if(xyzControlButton("buttonCode_y20161207153902")){
		toolbarTkview[toolbarTkview.length]='-';
		toolbarTkview[toolbarTkview.length]={
				text: '编辑',
				iconCls: 'icon-edit',
				handler: function(){editTkviewButton();}
		};
	}
	if(xyzControlButton("buttonCode_x20170822181002")){
		toolbarTkview[toolbarTkview.length]='-';
		toolbarTkview[toolbarTkview.length]={
			text: '批量备注',
			iconCls: 'icon-edit',
			handler: function(){
				editTkviewRemarkButton();
			}
		};
	}
	if(xyzControlButton("buttonCode_y20161207153903")){
		toolbarTkview[toolbarTkview.length]='-';
		toolbarTkview[toolbarTkview.length]={
				text: '删除',
				iconCls: 'icon-remove',
				handler: function(){deleteTkviewButton();}
		};
	}
	
	xyzgrid({
		table : 'tkviewManagerTable',
		title : "单品列表",
		toolbar : toolbarTkview,
		url:'../TkviewWS/queryTkviewList.do',
		singleSelect : true, 
		checkOnSelect : false,
		selectOnCheck : true,
		height : 310,
		pageSize : 5,
		idField : 'numberCode',
		queryParams : {
			cruise : cruiseNumber,
			mode : tkviewMode
		},
		onSelect : function(rowIndex, rowData){
			tkviewNumber = rowData.numberCode;
			var provider = $("#provider").combobox("getValue");
			$("#stockManagerTable").datagrid('load',{
				tkview : tkviewNumber,
				provider : provider
			});
			setColor();
			
			top.$('#remarkGreatDiv').accordion("select","通用备注");
			if(!xyzIsNull(rowData.remark)){
				top.$("#remarkTop").text(rowData.remark);
			}
		},
		columns:[[
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'单品编号',hidden:true,align:'center'},
			{field:'nameCn',title:'单品名称',halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value ,0 ,100);
				}
			},
			{field:'mark',title:'单品代码',sortable:true,halign:'center'},//order:'desc',
			{field:'cruise',title:'邮轮编号',hidden:true,halign:'center'},
			{field:'cruiseNameCn',title:'所属邮轮',width:100,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value ,0 ,100);
				}
			},
			{field:'shipment',title:'航期编号',hidden:true,align:'center'},
			{field:'shipmentMark',title:'航期代码',sortable:true,halign:'center'},
			{field:'shipmentTravelDate',title:'出发日期',align:'center',
				formatter:function(value ,row ,index){
					return value.substring(0, 10);
				}
			},
			{field:'cabin',title:'舱型编号',width:60,hidden:true,halign:'center'},
			{field:'cabinNameCn',title:'舱型名称',width:100,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value,0,100);
				}
			},
			{field:'volume',title:'容量',align:'center',sortable:true},
         	{field:'setFlag',title:'操作',align:'center',
         		formatter:function(value ,row ,index){
         			var div = "";
     				div += xyzGetA("日志记录","getTkviewLog",[row.numberCode],"点我查看日志记录!","blue");
         			return div;
         		}
         	},
         	{field:'stockCount',title:'库存资源',align:'center'},
         	{field:'addDate',title:'添加时间',hidden:true,sortable:true,
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'alterDate',title:'修改时间',hidden:true,sortable:true,
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'remark',title:'备注',width:150,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value ,0 ,150);
				}
			}
		]]
	});
	
	var toolbarStock = [];
	if(xyzControlButton("buttonCode_y20161208105005")){
		toolbarStock[toolbarStock.length]={
			text: '新增库存',
			iconCls: 'icon-add',
			handler: function(){
				addStockButton();
			}
		};
	}
	if(xyzControlButton("buttonCode_y20161208105007")){
		toolbarStock[toolbarStock.length]='-';
		toolbarStock[toolbarStock.length]={
			text: '编辑库存',
			iconCls: 'icon-edit',
			handler: function(){
				editStockButton();
			}
		};
	}
	if(xyzControlButton("buttonCode_y20161208105008")){
		toolbarStock[toolbarStock.length]='-';
		toolbarStock[toolbarStock.length]={
			text: '删除库存',
			iconCls: 'icon-remove',
			handler: function(){
				deleteStockButton();
			}
		};
	}
	
	xyzgrid({
		table : 'stockManagerTable',
		title : "库存列表 ",
		toolbar : toolbarStock,
		url:'../StockWS/queryStockListByTkview.do',
		singleSelect : true,
		height : 216,
		pageSize : 50,
		idField : 'numberCode',
		columns:[[
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'编号',hidden:true,align:'center'},
			{field:'priority',title:'优先级',halign:'center',
				formatter:function(value ,row ,index){
					var button = "";
					if(xyzControlButton("buttonCode_x20170511103901")){
						if(index != 0){
							button += "<img style='width:20px;height:20px;' src='../image/background/upup.png' onclick='editPriority(\""+row.numberCode+"\",0)'/>";
						}
						var length = $("#stockManagerTable").datagrid('getRows').length;
						if ((index+1) < length) {
							button +=  "<img style='width:20px;height:20px;' src='../image/background/downdown.png' onclick='editPriority(\""+row.numberCode+"\",1)'/>";
						}
					}
					return button;
				}
			},
			{field:'providerNameCn',title:'供应商',halign:'center',
				formatter : function(value, row, index) {
					return xyzGetDiv(value, 0, 10);
				}
			},
			{field:'type',title:'类型',halign:'center',
				formatter : function(value, row, index) {
					return value==0?"现询":"实库";
				}
			},
			{field:'nickName',title:'库存别名',width:80,halign:'center'},
			{field:'cost',title:'成本价',sortable:true,order:'desc',align:'center',
				formatter : function(value, row, index) {
					return xyzGetPrice(value);
				}
			},
			{field:'costRemark',title:'成本说明',halign:'center',
				formatter : function(value, row, index) {
					return xyzGetDiv(value, 0, 20);
				}
			},
			{field:'stock',title:'库存(间)',sortable:true,order:'desc',align:'center',
				formatter : function(value, row, index) {
					if(value < 0){
						var html = "<span style='color:red;'>"+value+"</span>";
						return html;
					}
					return value;
				}
			},
			{field:'useStock',title:'使用库存(间)',sortable:true,order:'desc',align:'center',
				formatter : function(value, row, index) {
					 if(value < 0){
						var html = "<span style='color:red;'>"+value+"</span>";
						return html;
					}
					return value;
				}
			},
			{field:'restStock',title:'剩余库存(间)',align:'center',
				formatter : function(value, row, index) {
					//不准改这玩意
					var restStock = row.stock - (row.useStock - row.useSurpass); 
					if(restStock < 0){
						var html = "<span style='color:red;'>"+restStock+"</span>";
						return html;
					}
					return restStock;
				}
			},
			{field:'surpass',title:'可超卖数(间)',sortable:true,order:'desc',align:'center',
		    	formatter : function(value, row, index) {
					if(value < 0){
						var html = "<span style='color:red;'>"+value+"</span>";
						return html;
					}
					return value;
				}
			},
			{field:'useSurpass',title:'已超卖数(间)',align:'center',
				formatter : function(value, row, index) {
					if(value < 0){
						var html = "<span style='color:red;'>"+value+"</span>";
						return html;
					}
					return value;
				}
		    },
			{field:'overSurpass',title:'剩余超卖数(间)',align:'center',
		    	formatter : function(value, row, index) {
		    		var overSurpass = row.surpass - row.useSurpass;
					if(value < 0){
						var html = "<span style='color:red;'>"+overSurpass+"</span>";
						return html;
					}
					return overSurpass;
				}
		    },
			{field:'referencePossessorNameCn',title:'引用机构名称',hidden:true,halign:'center'},
			{field:'referenceTkviewNameCn',title:'引用单品名称',hidden:true,halign:'center'},
			{field:'overDate',title:'有效期',halign:'center',sortable:true,order:'desc',
				formatter : function(value, row, index) {
					return xyzGetDivDate(value);
				}
			},
			{field:'isValid',title:'生效',align:'center',
				formatter : function(value, row, index) {
					var nowDate = new Date().getTime()-new Date(row.overDate).getTime();
					if(nowDate > 0){
						return "无效";
					}
					return "有效";
				},
				styler:function(value, row, index) {
					var nowDate = new Date().getTime()-new Date(row.overDate).getTime();
					if(nowDate > 0) {
						return "background-color:red";
					}
					return "background-color:green";
				}
			},
			{field:'remark',title:'备注',hidden:true,halign:'center'},
			{field:'addDate',title:'添加时间',hidden:true,align:'center'},
			{field:'alterDate',title:'修改时间',hidden:true,align:'center'}
		]]
	});

}

function setColor(){
	
	$("#stockManagerTable").datagrid({
		rowStyler : function(index, row){
			if (index == 0){
				//return 'background-color:#ff1493;';
				return 'color:#9400d3;';
			}
		}
	});
	
}

function loadTable(){
	
	var cabin = $("#queryCabin").combobox("getValue");
	var nameCn = $("#queryNameCn").val();
	var mark = $("#queryMark").val();
	var mouth = $("#mouth").combobox("getValue");
	var shipment = $("#shipment").combobox("getValue");
	var provider = $("#provider").combobox("getValue");
	
	$("#tkviewManagerTable").datagrid('load',{
		cruise: cruiseNumber,
		cabin : cabin,
		shipment : shipment,
        nameCn : nameCn,
        mark : mark,
        mouth : mouth,
        mode : tkviewMode,
        provider : provider
	});
}

function editPriority(numberCode, isUp){
	
	$.ajax({
		url : "../StockWS/setPriority.do",
		type : "POST",
		data : {
			numberCode : numberCode, 
			isUp : isUp
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#tkviewManagerTable").datagrid("reload");
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

function addTkviewButton(){
	if(!xyzIsNull(tkviewNumber)){
		var str = addTkviewRelease();
		if(!xyzIsNull(str)){
			top.$.messager.alert("提示",str,"info");
			return false;
		}
	}
	
	xyzdialog({
		dialog : 'dialogFormDiv_addTkviewButton',
		title : '单个新增单品',
		fit:false,
		width : 450,
		height : 450,
	    href : '../jsp_resources/addTkview.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				addTkviewSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addTkviewButton").dialog("destroy");
			}
		}],onLoad : function(){
			xyzTextbox("nameCnForm");
			xyzTextbox("markForm");
			
			xyzCombobox({
				required : true,
				combobox : 'cruiseForm',
				url : '../ListWS/getCruiseList.do',
				mode : 'remote',
				panelMaxHeight : 260,
				onChange : function(newValue, oldValue){
					$("#shipmentForm").combobox("clear");
					$("#shipmentForm").combobox("reload");
					$("#cabinForm").combobox("clear");
					$("#cabinForm").combobox("reload");
					$("#nameCnForm").textbox("clear");
				}
			});
			
			if("noValue"!=cruiseNumber){
				$("#cruiseForm").combobox({
					value : cruiseNumber
				});
			}
			
			xyzCombobox ({
				required : true,
				combobox:'shipmentForm',
				url : '../ListWS/getShipmentList.do',
				mode : 'remote',
				panelMaxHeight : 250,
				onBeforeLoad : function(param){
					param.cruise = $("#cruiseForm").combobox("getValue");
					param.isTkview = 0;   //为1时是：单品航期
				},
				onChange : function(newValue, oldValue){
					$("#cabinForm").combobox("clear");
					$("#cabinForm").combobox("reload");
					$("#nameCnForm").textbox("clear");
				}
			});
			
			xyzCombobox({
				required : true,
				combobox : 'cabinForm',
				url : '../ListWS/getCabinListByCruise.do',
				mode : 'remote',
				panelMaxHeight : 240,
				onBeforeLoad : function(param){
					param.cruise = $("#cruiseForm").combobox("getValue");
				},
				onChange : function(newValue, oldValue){
					$("#nameCnForm").textbox("clear");
				},
				onSelect : function(record){
					var shipment = $("#shipmentForm").combobox("getText");
					var shipmentMark = shipment.substring(10);
					var text= '['+shipmentMark+']'+record.text;
					$("#nameCnForm").textbox("setValue",text);
					$("#nameCnForm").textbox({
						required : true
					});
				}
			});
			
		}
	});
	
}

function addTkviewSubmit(){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").val();
	var mark = $("#markForm").val();
	if (!xyzIsNull(mark)) {
		var reg = /^\w+$/;
		if(!reg.test(mark)){
			top.$.messager.alert("提示","代码只能输入字母或者数字!","info");
			return;
		}
	}
	var cruise = $("#cruiseForm").combobox("getValue");
	var shipment = $("#shipmentForm").combobox("getValue");
	var cabin = $("#cabinForm").combobox("getValue");
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../TkviewWS/addTkview.do",
		type : "POST",
		data : {
			nameCn : nameCn,
			mark : mark,
			cruise : cruise,
			shipment : shipment,
			cabin : cabin,
			remark : remark,
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#tkviewManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_addTkviewButton").dialog("destroy");
				if(cruiseNumber != cruise){
					window.location.href='../jsp_resources/tkviewGroupCruise.html';
				}
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function addFastlyTkviewButton() {
	
	if(!xyzIsNull(tkviewNumber)){
		var str = addTkviewRelease();
		if(!xyzIsNull(str)){
			top.$.messager.alert("提示",str,"info");
			return false;
		}
	}
	
	xyzdialog({
		dialog : 'dialogFormDiv_addFastlyTkviewButton',
		title : '快捷新增单品',
	    href : '../jsp_resources/addFastlyTkview.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				addFastlyTkviewSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addFastlyTkviewButton").dialog("destroy");
			}
		}],onLoad : function(){
			
			xyzCombobox({
				required : true,
				combobox : 'cruiseForm',
				url : '../ListWS/getCruiseList.do',
				mode : 'remote',
				panelMaxHeight : 330,
				onChange : function(newValue, oldValue){
					$("#shipmentForm").combobox("clear");
					$("#shipmentForm").combobox("reload");
					addFastlyHtml();
				}
			});
			
			if("noValue"!=cruiseNumber){
				$("#cruiseForm").combobox({
					value : cruiseNumber
				});
			}
			addFastlyHtml();
			
			$("#checkAll").click(function(){
				var checkValue = $("#checkAllValue").val();
				if(checkValue=='1'){
					$(".shipmentClass").removeAttr("checked");
					checkValue = '0';
				}else{
					$(".shipmentClass").attr("checked",true);
					checkValue = '1';
				}
				$("#checkAllValue").val(checkValue);
			});
			
		}
	});
}

function addFastlyHtml() {
	
	var cruise = $("#cruiseForm").combobox("getValue");
	
	$.ajax({
		url : "../TkviewWS/getCabinByCruiseList.do",
		type : "POST",
		data : {
			cruise : cruise
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var cabinList = data.content.cabinList;
				var shimpmentList = data.content.shipmentList;
				if (cabinList.length == 0) {
					top.$.messager.alert("提示","舱型不存在,请先添加舱型!","info");
					$("#tkviewFastlyTable").empty();
					$("#shipmentTr").html("");
					return false;
				}
				if (shimpmentList.length == 0) {
					top.$.messager.alert("提示","航期不存在,请先添加航期!","info");
					$("#tkviewFastlyTable").empty();
					$("#shipmentTr").html("");
					return false;
				}
				$("#shipmentTr").html("");
				
				var shipmentHtml = '';
				for (var j = 0; j < shimpmentList.length; j++) {
					var tempId = shimpmentList[j].travelDate;
					var numberCode = shimpmentList[j].numberCode;
					shipmentHtml += '&nbsp&nbsp <input type="checkbox" ';
					shipmentHtml += ' class="shipmentClass" shimpmentNumberCode="'+ numberCode +'" ';
					shipmentHtml += ' id="shipment_'+ tempId +'" onClick="inputCheck(\''+ numberCode +'\')"';
					shipmentHtml += ' value="'+ tempId.substring(0, 10) +'" checked="checked"/>';
					shipmentHtml += tempId.substring(0, 10);
					if((j+1)%6 == 0){
				    	shipmentHtml += ' <br/>';
				    }
				}
				$("#shipmentTr").append(shipmentHtml);
				
				var cabinHtml = "";
					cabinHtml +='<tr >';
					cabinHtml +='<th style="width:150px;">舱型</th>';
					cabinHtml +='<th style="width:150px;">单品名称</th>';
				    cabinHtml +='<th style="width:150px;">单品代码</th>';
					cabinHtml +='<th style="width:150px;">备注</th>';
					cabinHtml += '<th>';
					cabinHtml += '<img id="addFastlyTkviewHtml" alt="点我添加单品" src="../image/other/addPage.gif" title="点我添加单品" style="cursor:pointer;">';
					cabinHtml += '</th>';
					cabinHtml += '</tr>';
					for ( var i = 0; i < cabinList.length; i++) {
						var cabinValue = cabinList[i].nameCn; 
						cabinHtml += '<tr class = "tkviewFastlyTrClass" id = "tkviewFastlyTr_'+i+'" >';
						cabinHtml += '<td><input disabled="true"id = "cabinForm_'+i+'" value = '+cabinValue+' numberCode="'+cabinList[i].numberCode+'" style="width:150px;"></td>';
						cabinHtml += '<td><input type="text" id = "nameCnForm_'+i+'" value = '+cabinValue+' style="width:150px;"/> </td>';
						cabinHtml += '<td><input type="text" id = "markForm_'+i+'" style="width:150px;"/> </td>';
						cabinHtml += '<td><input type="text" id = "remarkForm_'+i+'" style="width:150px;"/> </td>';
						cabinHtml += '<td><img onclick = "deleteTkviewHtml(\''+i+'\')" alt="点我删除单品" src="../image/other/delete.png" title="点我删除单品" style="cursor: pointer;"></td>';
						cabinHtml += '</tr>';
					}
					$("#tkviewFastlyTable").html(cabinHtml);
					
					$("#addFastlyTkviewHtml").click(function() {
						var tempId = $($(".tkviewFastlyTrClass:last")).attr('id');
						if(tempId == null){
							var tempLength = 0;
							var fastlyTkviewHtml= '';
							fastlyTkviewHtml += '<tr class = "tkviewFastlyTrClass" id = "tkviewFastlyTr_'+tempLength+'" >';
							fastlyTkviewHtml += '<td><input id = "cabinForm_'+tempLength+'"  style="width:150px;"></td>';
							fastlyTkviewHtml += '<td><input type="text" id = "nameCnForm_'+tempLength+'" style="width:150px;"/> </td>';
							fastlyTkviewHtml += '<td><input type="text" id = "markForm_'+tempLength+'" style="width:150px;"/> </td>';
							fastlyTkviewHtml += '<td><input type="text" id = "remarkForm_'+tempLength+'" style="width:150px;"/> </td>';
							fastlyTkviewHtml += '<td><img onclick = "deleteTkviewHtml(\''+tempLength+'\')" alt="点我删除单品" src="../image/other/delete.png" title="点我删除单品" style="cursor: pointer;"></td>';
							fastlyTkviewHtml += '</tr>';
							$("#tkviewFastlyTable").append(fastlyTkviewHtml);
							
							$("#nameCnForm_"+tempLength).textbox({
								required : true,
								validType:'length[0,50]'
							});
							$('#remarkForm_'+tempLength).textbox({
								validType:'length[0,100]'
							});
							$('#markForm_'+tempLength).textbox({
								validType:'length[0,50]'
							});
							xyzTextbox('nameCnForm_'+tempLength);
							xyzTextbox('markForm_'+tempLength);
							xyzTextbox('remarkForm_'+tempLength);						
							xyzCombobox({
								required:true,
								combobox:'cabinForm_'+tempLength,
								url:'../ListWS/getCabinIsOpenByCruiseList.do',
								mode:'remote',
								onChange : function(newValue,oldValue) {
									if(newValue != oldValue){
										$('#nameCnForm_'+tempLength).textbox('setValue',"");
									}
								},
								onBeforeLoad:function(param){
									param.cruise = $("#cruiseForm").combobox("getValue");
									param.isOpen = "开";
								}	
							});
						}
						if(tempId != null){
							tempLength = tempId.split('_')[1];
							tempLength = parseInt(tempLength)+1;
							
							var fastlyTkviewHtml= '';
							fastlyTkviewHtml += '<tr class = "tkviewFastlyTrClass" id = "tkviewFastlyTr_'+tempLength+'" >';
							fastlyTkviewHtml += '<td><input id="cabinForm_'+tempLength+'" style="width:150px;"></td>';
							fastlyTkviewHtml += '<td><input type="text" id = "nameCnForm_'+tempLength+'" style="width:150px;"/> </td>';
							fastlyTkviewHtml += '<td><input type="text" id = "markForm_'+tempLength+'" style="width:150px;"/> </td>';
							fastlyTkviewHtml += '<td><input type="text" id = "remarkForm_'+tempLength+'" style="width:150px;"/> </td>';
							fastlyTkviewHtml += '<td><img onclick = "deleteTkviewHtml(\''+tempLength+'\')" id = "" alt="点我删除单品" src="../image/other/delete.png" title="点我删除单品" style="cursor: pointer;"></td>';
							fastlyTkviewHtml += '</tr>';
							$(".tkviewFastlyTrClass:last").after(fastlyTkviewHtml);
							
							$("#nameCnForm_"+tempLength).textbox({
								required : true,
								validType:'length[0,50]'
							});
							$('#remarkForm_'+tempLength).textbox({
								validType:'length[0,100]'
							});
							$('#markForm_'+tempLength).textbox({
								validType:'length[0,50]'
							});
							xyzTextbox('nameCnForm_'+tempLength);
							xyzTextbox('markForm_'+tempLength);
							xyzTextbox('remarkForm_'+tempLength);						
							xyzCombobox({
								required:true,
								combobox:'cabinForm_'+tempLength,
								url:'../ListWS/getCabinIsOpenByCruiseList.do',
								mode:'remote',
								onChange : function(newValue,oldValue) {
									if(newValue != oldValue){
										$('#nameCnForm_'+tempLength).textbox('setValue',"");
									}
								},
							    onBeforeLoad:function(param){
									param.cruise = $("#cruiseForm").combobox("getValue");
									param.isOpen = "开";
								}	
							});
						}
					});
					
					for ( var k = 0; k < $(".tkviewFastlyTrClass").length; k++) {
						
						var tempId = $($(".tkviewFastlyTrClass")[k]).attr('id');
						var numberCode = tempId.split('_')[1];
						$("#nameCnForm_"+numberCode).textbox({
							required : true,
							validType:'length[0,50]'
						});
						xyzTextbox('nameCnForm_'+numberCode);
						xyzTextbox('markForm_'+numberCode);
						$('#remarkForm_'+numberCode).textbox({
							validType:'length[0,100]'
						});
						$('#markForm_'+numberCode).textbox({
							validType:'length[0,50]'
						});
						xyzTextbox('remarkForm_'+numberCode);						
						xyzCombobox({
							required:true,
							combobox:'cabinForm_'+numberCode,
							url:'../ListWS/getCabinIsOpenByCruiseList.do',
							mode:'remote',
						    onBeforeLoad:function(param){
								param.cruise = $("#cruiseForm").combobox("getValue");
								param.isOpen = "开";
							}
						});
						$('#cabinForm_'+numberCode).combobox("setValue",$('#cabinForm_'+numberCode).attr("numberCode"));
						$("#nameCnForm_"+numberCode).textbox("setValue",$('#cabinForm_'+numberCode).combobox("getText"));
					}
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function inputCheck(numberCode){
	var value = $("#value_"+numberCode).val();
	if(value=='1'){
		value = '0';
	}else{
		value = '1';
	}
	$("#value_"+numberCode).val(value);
	
	var length = $(".shipmentClass").length;
	var count = 0;
	$("input[id^='shipment_']").each(function(){
    	if($(this).attr("checked") == "checked"){
    		count++;
    	}
    });
	if(length == count){//全选
		$("#checkAll").attr("checked",true);
	    $("#checkAllValue").val("1");
	}else{
		$("#checkAll").attr("checked",false);
		$("#checkAllValue").val("0");
	}
}

function deleteTkviewHtml(id) {
	$("#tkviewFastlyTr_"+id).remove();
}

function addFastlyTkviewSubmit() {   
	
	if(!$("form").form('validate')){
		return false;
	}
	var cruise = $("#cruiseForm").combobox('getValue');
	var tkviewJson = [];
    var shipmentJson = [];
    var shipCount = 0;
    $(".shipmentClass").each(function(){
    	if($(this).attr("checked") == "checked"){
    		var shipment =$(this).attr("shimpmentNumberCode"); 
    		shipmentJson += shipment+",";
    		shipCount++;
    	}
    });

    var cabinCount = 0;
    $(".tkviewFastlyTrClass").each(function(){
    	var nameCn = $(this).find("input[id^='nameCnForm']").val();
		var mark = $(this).find("input[id^='markForm']").val();
		var remark =  $(this).find("input[id^='remarkForm']").val();
		var cabin =   $(this).find("input[id^='cabinForm']").combobox('getValue');
		tempJson = {
			nameCn : nameCn,
			mark : mark,
			cabin : cabin,
			remark : remark
		};
        tkviewJson[tkviewJson.length] = tempJson;
        cabinCount++;
    });
    if (tkviewJson.length == 0) {
    	top.$.messager.alert("提示","单品信息不能为空!","info");
    	return false;
	}
	tkviewJson = JSON.stringify(tkviewJson);
	
	if (shipmentJson.length == 0) {
		top.$.messager.alert("提示","航期不能为空!","info");
		return false;
	}
	
	var str = "确定要新增 ";
	if(shipCount > 0){
		str += shipCount+"条航线 ";
	}
	if(cabinCount > 0){
		str += cabinCount+"条舱型";
	}
	str += "?";
	
	$.messager.confirm('确认', str,function(r) {
		if(r){
			$.ajax({
				url : "../TkviewWS/addFastlyTkview.do",
				type : "POST",
				data : {
					tkviewJsonStr : tkviewJson,
					shipmentStr : shipmentJson,
					cruise : cruise
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						$("#tkviewManagerTable").datagrid("reload");
						$("#dialogFormDiv_addFastlyTkviewButton").dialog("destroy");
						top.$.messager.alert("提示","操作成功!","info");
						if(cruiseNumber != cruise){
							location.href='../jsp_resources/tkviewGroupCruise.html&cruise='+cruise;
						}
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
function addQuickTkviewButton(){
	
	if(!xyzIsNull(tkviewNumber)){
		var str = addTkviewRelease();
		if(!xyzIsNull(str)){
			top.$.messager.alert("提示",str,"info");
			return false;
		}
	}
	
	xyzdialog({
		dialog : 'dialogFormDiv_addQuickTkviewButton',
		title : '完整新增单品',
	    href : '../jsp_resources/addQuickTkview.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				addQuickTkviewSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addQuickTkviewButton").dialog("destroy");
			}
		}],onLoad : function(){
			xyzCombobox({
				required : true,
				combobox : 'cruiseForm',
				url : '../ListWS/getCruiseList.do',
				mode : 'remote',
				panelMaxHeight : 300,
				onChange : function(newValue, oldValue){
					$("#shipmentForm").combobox("clear");
					$("#shipmentForm").combobox("reload");
					
					$("#cabinForm").combobox("clear");
					$("#cabinForm").combobox("reload");
					
					$("#nameCnForm").textbox("clear");
				},
				onSelect : function(record){
					$("#shipmentForm").combobox("clear");
					$("#shipmentForm").combobox("reload");
					
					$("#cabinForm").combobox("clear");
					$("#cabinForm").combobox("reload");
					
					$("#nameCnForm").textbox("clear");
				}
			});
			
			if("noValue"!=cruiseNumber){
				$("#cruiseForm").combobox({
					value : cruiseNumber
				});
			}
			
			xyzCombobox({
				required : true,
				combobox : 'shipmentForm',
				url : '../ListWS/getShipmentList.do',
				mode : 'remote',
				panelMaxHeight : 300,
				onBeforeLoad:function(param){
					param.cruise = $("#cruiseForm").combobox("getValue");
					param.isTkview = 0;
				},
			});
			
			$("#addTkviewHtml").click(function(){
				addTkviewHtml();
			});
			
			tkviewHtmlEvent(0);
		}
	});
}

function addTkviewHtml(){
	
	var tempLength = $(".tkviewTrClass:last").prop('id').split('_')[1];
	tempLength = parseInt(tempLength)+1;
	
	var html = "";
		html += '<tr class="tkviewTrClass" id="tkviewTrId_'+ tempLength +'">';
		html += ' <td>';
		html += '  <input id="cabinForm_'+ tempLength +'" style="width:150px"/>';
		html += ' </td>';
		html += ' <td>';
		html += '  <input id="tkviewNameCnForm_'+ tempLength +'" style="width:170px"/>';
		html += ' </td>';
		html += ' <td>';
		html += '  <input id="tkviewMarkForm_'+ tempLength +'" style="width:80px"/>';
		html += ' </td>';
		html += ' <td>';
		html += '  <input id="costForm_'+ tempLength +'" style="width:80px"/>';
		html += ' </td>';
		html += ' <td>';
		html += '  <input id="providerForm_'+ tempLength +'" style="width:100px"/>';
		html += ' </td>';
		html += ' <td>';
		html += '  <input id="stockForm_'+ tempLength +'" style="width:80px"/>';
		html += ' </td>';
		html += ' <td>';
		html += '  <input id="surpassForm_'+ tempLength +'" style="width:80px"/>';
		html += ' </td>';
		html += ' <td>';
		html += '  <img id="delete_'+ tempLength +'" alt="点我删除此单品" src="../image/other/delete.png" title="点我删除此单品" style="cursor:pointer;">';
		html += ' </td>';
		html += '</tr>';
	$(".tkviewTrClass:last").after(html);
	
	tkviewHtmlEvent(tempLength);
}

function tkviewHtmlEvent(index){
	
	xyzCombobox({
		combobox:'cabinForm_'+index,
		url:'../ListWS/getCabinListByShipment.do',
		required:true,
		mode:'remote',
		onBeforeLoad:function(param){
			param.shipmnet = $("#shipmentForm").combobox("getValue");
			param.isOpen = '开';
		},
		onSelect:function(record){
			if(!xyzIsNull(record.value)) {
				var shipment = $("#shipmentForm").combobox("getText");
				var shipmentMark = shipment.substring(10);
				var text= '['+shipmentMark+']'+record.text;
				$("#tkviewNameCnForm_"+index).textbox('setValue',text);
			}
		}
	});
	xyzCombobox({
		combobox:'providerForm_'+index,
		url:'../ListWS/getProviderList.do',
		required:true,
		mode:'remote',
	});
	xyzTextbox('tkviewNameCnForm_'+index);
	xyzTextbox('tkviewMarkForm_'+index);
	$('#tkviewNameCnForm_'+index).textbox({ 
		required:true
	});
	
	$("#surpassForm_"+index).val(0);
	
	$('#costForm_'+index).numberbox({    
	    min:0,    
	    max:999999,
	    precision:2,
	    required:true,
	    icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).numberbox('clear');
			}
		}]
	});  
	$('#stockForm_'+index).numberbox({ 
		required:true,
		min:0,    
		max:9999,
		icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).numberbox('clear');
			}
		}]
	});  
	$('#surpassForm_'+index).numberbox({ 
		required:true,
		min:0,    
		max:9999,
		icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).numberbox('clear');
			}
		}]
	});  
	
	$("#delete_"+index).click(function(){
		var tempIndex = $(this).prop('id').split('_')[1];
		$("#tkviewTrId_"+tempIndex).remove();
	});
}

function addQuickTkviewSubmit(){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var shipment = $("#shipmentForm").combobox('getValue');
	var tkviewJson = [];
	var count = 0;  //价格小于等于1000的个数
	var addCount = 0;  //新增库存个数
	for ( var i = 0; i < $(".tkviewTrClass").length; i++) {
		var index = $(".tkviewTrClass").eq(i).prop('id').split('_')[1];
		
		var cabin = $("#cabinForm_"+index).combobox('getValue');
		var tkviewNameCn = $("#tkviewNameCnForm_"+index).textbox('getValue');
		var tkviewMark = $("#tkviewMarkForm_"+index).textbox('getValue');
		if (!xyzIsNull(tkviewMark)) {
			var reg = /^\w+$/;
			if(!reg.test(tkviewMark)){
				top.$.messager.alert("提示","代码只能输入字母或者数字!","info");
				return;
			}
		}
		var provider = $("#providerForm_"+index).combobox('getValue');
		var cost = $("#costForm_"+index).numberbox('getValue');
		var stock = $("#stockForm_"+index).numberbox('getValue');
		var surpass = $("#surpassForm_"+index).numberbox('getValue');
		
		if(cost <= 1000){
			count++;
		}
		
		var tempJson =  {
			cabin : cabin,
			tkviewNameCn : tkviewNameCn,
			tkviewMark : tkviewMark,
			provider : provider,
			cost : xyzSetPrice(cost),
			stock : stock,
			surpass : surpass
		};
		tkviewJson[tkviewJson.length] = tempJson;
		addCount++;
	}
	
	if(addCount > 0){
		$.messager.confirm('确认', "确定新增 "+ addCount+"条舱型?",function(r) {
			if(r){
				if(count > 0){
					$.messager.confirm('确认', "提价的数据中成本价有小于等于1000的,确定提交吗?",function(r) {
						if(r){
							$.ajax({
								url : "../TkviewWS/addQuickTkview.do",
								type : "POST",
								data : {
									tkviewJsonStr : JSON.stringify(tkviewJson),
									shipment : shipment
								},
								async : false,
								dataType : "json",
								success : function(data) {
									if(data.status==1){
										$("#tkviewManagerTable").datagrid("reload");
										$("#stockManagerTable").datagrid("reload");
										$("#dialogFormDiv_addQuickTkviewButton").dialog("destroy");
										top.$.messager.alert("提示","操作成功!","info");
									}else{
										top.$.messager.alert("警告",data.msg,"warning");
									}
								},
								error : function(XMLHttpRequest, textStatus, errorThrown) {
									top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
								}
							});
						}else{
							for(var i = 0; i < $(".tkviewTrClass").length; i++) {
								var index = $(".tkviewTrClass").eq(i).prop('id').split('_')[1];
								var cost = $("#costForm_"+index).numberbox('getValue');
								if(cost <= 1000){
									$("#costForm_"+index).numberbox('clear');
								}
							}
						}
					});
				}else{
					$.ajax({
						url : "../TkviewWS/addQuickTkview.do",
						type : "POST",
						data : {
							tkviewJsonStr : JSON.stringify(tkviewJson),
							shipment : shipment
						},
						async : false,
						dataType : "json",
						success : function(data) {
							if(data.status==1){
								$("#tkviewManagerTable").datagrid("reload");
								$("#stockManagerTable").datagrid("reload");
								$("#dialogFormDiv_addQuickTkviewButton").dialog("destroy");
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
			}
		});
	}
	
}

function editTkviewButton(){
	var tkviews = $("#tkviewManagerTable").datagrid("getChecked");
	if(tkviews.length != 1){
		top.$.messager.alert("提示","请先勾选单个对象!","info");
		return;
	}
	var row = tkviews[0];
	
	tkviewNumber = row.numberCode;
	var str = addTkviewRelease();
	if(!xyzIsNull(str)){
		top.$.messager.alert("提示",str,"info");
		return false;
	}
	
	xyzdialog({
		dialog : 'dialogFormDiv_editTkviewButton',
		title : '编辑单品【'+ row.nameCn +'】',
		fit:false,
		width : 450,
		height : 450,
	    href : '../jsp_resources/editTkview.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				editTkviewSubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editTkviewButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("nameCnForm");
			xyzTextbox("markForm");
			
			$("#cruiseForm").html(row.cruiseNameCn);
			$("#shipmentForm").html(row.shipment);
			$("#cabinForm").html(row.cabinNameCn);
			
			$("#nameCnForm").textbox("setValue",row.nameCn);
			$("#markForm").textbox("setValue",row.mark);
			$("#remarkForm").val(row.remark);
			
			$("#nameCnForm").textbox({
				required:true
			});
		}
	});
}

function editTkviewSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}

	var nameCn = $("#nameCnForm").val();
	var mark = $("#markForm").val();
	if (!xyzIsNull(mark)) {
		var reg = /^\w+$/;
		if(!reg.test(mark)){
			top.$.messager.alert("提示","代码只能输入字母或者数字!","info");
			return;
		}
	}
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../TkviewWS/editTkview.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			nameCn : nameCn,
			mark : mark,
			remark : remark,
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#tkviewManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_editTkviewButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editTkviewRemarkButton(){
	var shipList = getShipmentByCruise();
	var cabinList = getCabinByCruise();
	
	xyzdialog({
		dialog : 'dialogFormDiv_editTkviewRemarkButton',
		title : '单品批量备注',
	    href : '../jsp_resources/editTkviewRemark.html',
	    buttons : [{
			text : '确定',
			handler:function(){
				editTkviewRemarkSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editTkviewRemarkButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			$("#tkviewDiv").hide();
			
			//航期
			var shipNum = 0;
			var shipHtml = '';
			for(var k = 0; k < shipList.length; k++){
				var shipObj = shipList[k];
				if(k % 5 == 0){
					shipNum = shipNum + 4;
					shipHtml += '<tr>';
				}
				shipHtml += '<td>';
				shipHtml += ' <input id="ship_'+ shipObj.numberCode +'" type="checkbox" checked="checked" onClick="shipClick(\''+ shipObj.numberCode +'\')">';
				shipHtml += ' <label for="ship_'+ shipObj.numberCode +'">';
				shipHtml += xyzGetDiv(shipObj.travelDate, 2, 8)+'</label>';
				shipHtml += '</td>';
				if(k == shipNum){
					shipHtml += '</tr>';
				}
				if(k == (shipList.length-1) && k < shipNum){
					for(var h = 0; h < (shipNum-k-1); h++){
						shipHtml += '<td></td>';
					}
					shipHtml += '</tr>';
				}
			}
			$("#shipTbody").html(shipHtml);
			$("#shipSpan").html("选中"+shipList.length+"个航期");
			
			//舱型
			var cabinNum = 0;
			var cabinHtml = '';
			for(var k = 0; k < cabinList.length; k++){
				var cabinObj = cabinList[k];
				if(k % 5 == 0){
					cabinNum = cabinNum + 5;
					cabinHtml += '<tr>';
				}
				cabinHtml += '<td>';
				cabinHtml += ' <input id="cabin_'+ cabinObj.numberCode +'" type="checkbox" checked="checked" onClick="cabinClick(\''+ cabinObj.numberCode +'\')">';
				cabinHtml += ' <label for="cabin_'+ cabinObj.numberCode +'">'+xyzGetDiv(cabinObj.nameCn, 0, 7)+'</label>';
				cabinHtml += '</td>';
				if(k == cabinNum){
					cabinHtml += '</tr>';
				}
				if(k == (cabinList.length-1) && k < cabinNum){
					for(var h = 0; h < (cabinNum-k-1); h++){
						cabinHtml += '<td></td>';
					}
					cabinHtml += '</tr>';
				}
			}
			$("#cabinTbody").html(cabinHtml);
			$("#cabinSpan").html("选中"+cabinList.length+"个舱型");
			
			//航期全选事件
			$("#shipCheckAll").click(function(){
				var shipAll = $("#shipAll").val();
				if(shipAll=='0'){
					shipAll = '1';
					$("#shipTbody input[id^='ship_']").each(function(){
						$(this).attr("checked",false);
				    });
					$("#shipSpan").html("");
				}else{
					shipAll = '0';
					var shipCount = 0;
					$("#shipTbody input[id^='ship_']").each(function(){
						$(this).attr("checked",true);
						shipCount++;
				    });
					$("#shipSpan").html("选中"+shipCount+"个航期");
				}
				$("#shipAll").val(shipAll);
				
			});
			
			//舱型全选事件
			$("#cabinCheckAll").click(function(){
				var cabinAll = $("#cabinAll").val();
				if(cabinAll=='0'){
					cabinAll = '1';
					$("#cabinTbody input[id^='cabin_']").each(function(){
						$(this).attr("checked",false);
				    });
					$("#cabinSpan").html("");
				}else{
					cabinAll = '0';
					var cabinCount = 0;
					$("#cabinTbody input[id^='cabin_']").each(function(){
						$(this).attr("checked",true);
						cabinCount++;
				    });
					$("#cabinSpan").html("选中"+cabinCount+"个舱型");
				}
				$("#cabinAll").val(cabinAll);
			});
			
			//确定选择
			$("#queryBtn").click(function(){
				$("#tkviewDiv").show();
				
				var shipCount = 0;
				var shipStr = "";
				$("#shipTbody input[id^='ship_']").each(function(){
					var tempId = $(this).prop('id').split('_')[1];
			    	if($(this).attr("checked") == "checked"){
			    		shipStr = xyzIsNull(shipStr)==true?tempId:(shipStr+","+tempId);
			    		shipCount++;
			    	}
			    });
				$("#shipSpan").html("选中"+shipCount+"个航期");
				
				var cabinCount = 0;
				var cabinStr = "";
				$("#cabinTbody input[id^='cabin_']").each(function(){
					var tempId = $(this).prop('id').split('_')[1];
			    	if($(this).attr("checked") == "checked"){
			    		cabinStr = xyzIsNull(cabinStr)==true?tempId:(cabinStr+","+tempId);
			    		cabinCount ++;
			    	}
			    });
				$("#cabinSpan").html("选中"+cabinCount+"个舱型");
				
				if(xyzIsNull(shipStr) && xyzIsNull(cabinStr)){
					top.$.messager.alert("温馨提示","请选择航期或者舱型!","info");
					return false;
				}
				getTkviewListByShipmentAndCabin(shipStr, cabinStr);
			});
			
		}
	});
}

function getTkviewListByShipmentAndCabin(shipStr, cabinStr){
	
	$.ajax({
		url : "../TkviewWS/getTkviewListByShipmentAndCabin.do",
		type : "POST",
		data : {
			cruise : cruiseNumber,
			shipment : shipStr,
			cabin : cabinStr
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status == 1){
				var tkviewList = data.content;
				
				$("#tkviewTbody").empty();
				
				var traveDate = "";
				var cabinNameCn = "";
				var tkviewHtml = '';
				for(var k = 0; k < tkviewList.length; k++){
					var tkviewObj = tkviewList[k];
					
					tkviewHtml += '<tr>';
					if(k == 0 || traveDate != tkviewObj.shipmentTravelDate){
						tkviewHtml += ' <td colspan="4">';
						tkviewHtml += '  <hr color="#b0c4de"/>';
						tkviewHtml += ' </td>';
						tkviewHtml += '</tr>';
						tkviewHtml += '<tr>';
						traveDate = tkviewObj.shipmentTravelDate;
						tkviewHtml += ' <td>'+ traveDate.substring(0,10) +'</td>';
					}else{
						tkviewHtml += ' <td></td>';
					}
					if(k==0 || cabinNameCn!=tkviewObj.cabinNameCn){
						cabinNameCn = tkviewObj.cabinNameCn;
						tkviewHtml += ' <td>'+ xyzGetDiv(cabinNameCn, 0, 6) +'</td>';
					}else{
						tkviewHtml += ' <td></td>';
					}
					tkviewHtml += ' <td>'+ xyzGetDiv(tkviewObj.nameCn, 12, 10) +'</td>';
					tkviewHtml += ' <td>';
					tkviewHtml += '  <input id="isCheck_'+ tkviewObj.numberCode +'" type="checkbox" onClick="onTkviewClick(\''+ tkviewObj.numberCode +'\')"/>';
					tkviewHtml += ' </td>';
					tkviewHtml += '</tr>';
				}
				$("#tkviewTbody").html(tkviewHtml);
				
				for(var f = 0; f < tkviewList.length; f++){
					var tkviewObj = tkviewList[f];
					$("#isCheck_"+ tkviewObj.numberCode).attr("checked",true);
				}
				
				$("#checkAll").click(function(){
					if($(this).attr("checked") == "checked"){
						$("input[id^='isCheck_']").attr("checked",true);
					}else{
						$("input[id^='isCheck_']").attr("checked",false);
					}
				});
				
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
		
	});
}

function onTkviewClick(id){
	var count = 0;
	var checkCount = 0;
	$("input[id^='isCheck_']").each(function(){
		count++;
    	if($(this).attr("checked") == "checked"){
    		checkCount++;
    	}
	});
	
	if(count == checkCount){
		$("#checkAll").attr("checked",true);
	}else{
		$("#checkAll").attr("checked",false);
	}
}

function editTkviewRemarkSubmit(){
	if(!$("form").form('validate')){
		return false;
	}
	
	var checkCount = 0;
	var onCheckStr = "";
	$("input[id^='isCheck_']").each(function(){
		var id = $(this).prop('id').split('_')[1];
    	if($(this).attr("checked") == "checked"){
    		onCheckStr = xyzIsNull(onCheckStr)==true?id:(onCheckStr+","+id);
    		checkCount++;
    	}
    });
	if(xyzIsNull(onCheckStr)){
		top.$.messager.alert("温馨提示","请选择单品信息!","info");
		return false;
	}
	
	var remark = $("#remarkForm").val();
	
	$.messager.confirm('确认', '确定编辑'+checkCount+'条单品信息备注?',function(r) {
		  if(r){
			  $.ajax({
					url : "../TkviewWS/editTkviewRemark.do",
					type : "POST",
					data : {
						numbercode : onCheckStr,
						remark : remark
					},
					async : false,
					dataType : "json",
					success : function(data) {
						if(data.status==1){
							$("#tkviewManagerTable").datagrid("reload");
							$("#stockManagerTable").datagrid("reload");
							$("#dialogFormDiv_editTkviewRemarkButton").dialog("destroy");
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

function deleteTkviewButton(){
	var tkviews = $.map($("#tkviewManagerTable").datagrid("getChecked"),function(p){return p.numberCode;}).join(",");
	if(xyzIsNull(tkviews)){
		top.$.messager.alert("提示","请先勾选需要删除的对象!","info");
		return;
	}
	
	var str = addTkviewRelease();
	if(!xyzIsNull(str)){
		top.$.messager.alert("提示",str,"info");
		return false;
	}
	
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
	  if(r){
		  $.ajax({
			url : "../TkviewWS/deleteTkviews.do",
			type : "POST",
			data : {
				numberCodes : tkviews
			},
			async : false,
			dataType : "json",
			success : function(data) {
				if(data.status==1){
					$("#tkviewManagerTable").datagrid("reload");
					$("#stockManagerTable").datagrid("reload");
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

function addStockButton(){
	var rows = $("#tkviewManagerTable").datagrid("getSelections");
	if (rows.length != 1) {
		top.$.messager.alert("提示","请先勾选一个单品对象!","info");
		return;
	}
	
	var str = addTkviewRelease();
	if(!xyzIsNull(str)){
		top.$.messager.alert("提示",str,"info");
		return false;
	}
	
	var tkview = rows[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_addStockButton',
		title : '给< '+tkview.nameCn+' >新增库存',
		fit:false,
		width : 450,
		height : 450,
	    href : '../jsp_resources/addStock.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				addStockSubmit(tkview.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addStockButton").dialog("destroy");
			}
		}],onLoad : function(){
			xyzTextbox("nickNameForm");
			xyzTextbox("costRemarkForm");
			xyzTextbox("remarkForm");
			
			xyzCombobox({
				required : true,
				combobox : 'providerForm',
				url : '../ListWS/getProviderList.do',
				lazy : false,
				mode : 'remote',
				panelMaxHeight : 275
			});
			
			$("#typeForm").combobox({
				onHidePanel : function(){
					var type = $(this).combobox("getValue");
					if(type == 0){//现询
						$("#stockForm").numberbox({
							required : false,
							disabled : true
						});
						$("#surpassForm").numberbox({
							disabled : true
						});
					}else{//实库
						$("#stockForm").numberbox({
							required : true,
							disabled : false
						});
						$("#surpassForm").numberbox({
							disabled : false
						});
					}
				}
			});
			
			$("#costRemarkForm").textbox({
				validType:['length[0,1000]'],
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).textbox('clear');
					}
				}]
			});
			$(".numberboxForm").textbox({
				validType:['length[0,1000]'],
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).textbox('clear');
					}
				}]
			});
			
			$("#overDateForm").datetimebox("setValue", tkview.finalSaleDate);
		}
	});
	
}

function addStockSubmit(tkview){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nickName = $("#nickNameForm").val();
	var provider = $("#providerForm").combobox("getValue");
	
	var type = $("#typeForm").combobox("getValue");
	var stock = $("#stockForm").numberbox('getValue') == "" ? 0:$("#stockForm").numberbox('getValue');
	var surpass = $("#surpassForm").numberbox('getValue') == "" ? 0:$("#surpassForm").numberbox('getValue');
	
	var cost = $("#costForm").numberbox('getValue') == "" ? 0:$("#costForm").numberbox('getValue');
	var costRemark = $("#costRemarkForm").val();
	var finalSaleDate = $("#overDateForm").datetimebox("getValue");
	//var priority = $("#priorityForm").numberbox('getValue') == "" ? 50:$("#priorityForm").numberbox('getValue');
	var remark = $("#remarkForm").val();
	
	var overDate = new Date(Date.parse(finalSaleDate,"yyyy-MM-dd"));
	var nowDate = new Date(Date.parse(new Date(),"yyyy-MM-dd"));
	var day = parseInt((overDate.getTime() - nowDate.getTime()) / (1000 * 60 * 60 * 24));
	
	if(cost <= 1000){
		$.messager.confirm('确认', "确定成本价为【"+ cost +"】吗?",function(r) {
			if(r){
				if(day < 0){
					$.messager.confirm('确认', '库存有效期已失效,是否添加?',function(r) {
						if(r){
							addStock(tkview, nickName, provider, type, stock, surpass, cost, costRemark, finalSaleDate, 5, remark);
						}else{
							$("#overDateForm").datetimebox('clear');
						}
					});
				}else{
					addStock(tkview, nickName, provider, type, stock, surpass, cost, costRemark, finalSaleDate, 5, remark);
				}
			}else{
				$("#costForm").numberbox('clear');
			}
		});
	}else if(cost > 1000 && day < 0){
		$.messager.confirm('确认', '库存有效期已失效,是否添加?',function(r) {
			if(r){
				addStock(tkview, nickName, provider, type, stock, surpass, cost, costRemark, finalSaleDate, 5, remark);
			}else{
				$("#overDateForm").datetimebox('clear');
			}
		});
	}else{
		addStock(tkview, nickName, provider, type, stock, surpass, cost, costRemark, finalSaleDate, 5, remark);
	}
	
}

function addStock(tkview, nickName, provider, type, stock, surpass, cost, costRemark, finalSaleDate, priority, remark){
	
	$.ajax({
		url : "../StockWS/addStock.do",
		type : "POST",
		data : {
			tkview : tkview,
			nickName : nickName,
			provider : provider,
			type : type,
			stock : stock,
			surpass : surpass,
			cost : xyzSetPrice(cost),
			costRemark : costRemark,
            overDate : finalSaleDate,
            priority : priority,
            remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#stockManagerTable").datagrid("reload");
				$("#dialogFormDiv_addStockButton").dialog("destroy");
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

function editStockButton(){
	var rows = $("#tkviewManagerTable").datagrid("getSelections");
	if (rows.length != 1) {
		top.$.messager.alert("提示","请先勾选单个单品对象!","info");
		return;
	}
	
	var str = addTkviewRelease();
	if(!xyzIsNull(str)){
		top.$.messager.alert("提示",str,"info");
		return false;
	}
	
	var tkview = rows[0];
	
	var stocks = $("#stockManagerTable").datagrid("getChecked");
	if(stocks.length != 1){
		top.$.messager.alert("提示","请先勾选单个库存对象!","info");
		return;
	}
	var stockObj = stocks[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editStockButton',
		title : '编辑【'+tkview.nameCn+'】库存',
		fit:false,
		width : 450,
		height : 450,
	    href : '../jsp_resources/editStock.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				editStockSubmit(stockObj.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editStockButton").dialog("destroy");
			}
		}],onLoad : function(){
			xyzTextbox("nickNameForm");
			xyzTextbox("costRemarkForm");
			xyzTextbox("remarkForm");
			
			xyzCombobox({
				combobox : 'providerForm',
				url : '../ListWS/getProviderList.do',
				lazy : false,
				panelMaxHeight : 275
			});
			
			$("#nickNameForm").textbox("setValue", stockObj.nickName);
			$("#providerForm").combobox({
				value : stockObj.provider
			});
			$("#costForm").numberbox("setValue",xyzGetPrice(stockObj.cost));
			$("#costRemarkForm").textbox("setValue",stockObj.costRemark);
			
			var stockType=parseInt(stockObj.type);
			$("#typeForm").combobox({
				value : stockType
			});
			if(stockType == 0){
				$(".realStock").hide();
			}else{
				$("#stockForm").numberbox("setValue",stockObj.stock);
				$("#surpassForm").numberbox("setValue",stockObj.surpass);
				
				$(".numberboxForm").numberbox({
					required : true,
					icons : [{
						iconCls:'icon-clear',
						handler: function(e){
							$(e.data.target).numberbox('clear');
						}
					}]
				});
			}
			
			//$("#priorityForm").numberbox("setValue",stockObj.priority);
			$("#overDateForm").datetimebox({
				value : stockObj.overDate
			});
			$("#remarkForm").textbox("setValue",stockObj.remark);
			
			$("#providerForm").combobox({
				required : true
			});
			$("#overDateForm").datetimebox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).datetimebox('clear');
					}
				}]
			});
			
			 $('#typeForm').combobox({  
		        onChange:function(newValue,oldValue){  
	               if(newValue==0){
	            	   $(".realStock").hide();
	               }else{
	   				   $(".realStock").show();
	               }
		        }  
		    });  
		}
	});
	
}

function editStockSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nickName = $("#nickNameForm").textbox("getValue");
	var provider = $("#providerForm").combobox("getValue");
	var cost = $("#costForm").numberbox("getValue");
	var costRemark = $("#costRemarkForm").textbox("getValue");
	var stock = $("#stockForm").numberbox("getValue");
	var type = $("#typeForm").combobox("getValue");
	var surpass = $("#surpassForm").numberbox("getValue");
	var finalSaleDate = $("#overDateForm").datetimebox("getValue");
	//var priority = $("#priorityForm").numberbox("getValue");
	var remark = $("#remarkForm").textbox("getValue");
	
	var overDate = new Date(Date.parse(finalSaleDate,"yyyy-MM-dd"));
	var nowDate = new Date(Date.parse(new Date(),"yyyy-MM-dd"));
	var day = parseInt((overDate.getTime() - nowDate.getTime()) / (1000 * 60 * 60 * 24));
	
	if(cost <= 1000){
		$.messager.confirm('确认', "确定成本价为【"+ cost +"】吗?",function(r) {
			if(r){
				if(day < 0) {
					$.messager.confirm('确认', '库存有效期已失效,是否添加？',function(r) {
						if(r){
							editStockDetails(numberCode, nickName, provider, cost, costRemark, stock, surpass, finalSaleDate, 5, remark, type);
						}else{
							 $("#overDateForm").datetimebox('clear');
						}
					});
				}else{
					editStockDetails(numberCode, nickName, provider, cost, costRemark, stock, surpass, finalSaleDate, 5, remark, type);
				}
				
			}else{
				$("#costForm").numberbox('clear');
			}
		});
	}else if(cost > 1000 && day < 0){
		$.messager.confirm('确认', '库存有效期已失效,是否添加？',function(r) {
			if(r){
				editStockDetails(numberCode, nickName, provider, cost, costRemark, stock, surpass, finalSaleDate, 5, remark, type);
			}else{
				 $("#overDateForm").datetimebox('clear');
			}
		});
	}else{
		editStockDetails(numberCode, nickName, provider, cost, costRemark, stock, surpass, finalSaleDate, 5, remark, type);
	}
	
}

function editStockDetails(numberCode, nickName, provider, cost, costRemark, stock, surpass, finalSaleDate, priority, remark, type){
	$.ajax({
		url : "../StockWS/editStockDetails.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			nickName : nickName,
			provider : provider,
			cost : xyzSetPrice(cost),
			costRemark : costRemark,
			stock : stock,
			surpass : surpass,
			overDate : finalSaleDate,
			priority : priority,
			remark : remark,
			stockType : type
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#stockManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_editStockButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deleteStockButton(){
	var rows = $("#stockManagerTable").datagrid("getChecked");
	if(rows.length != 1){
		top.$.messager.alert("提示","请先勾选需要删除的库存对象!","info");
		return;
	}
	
	var str = addTkviewRelease();
	if(!xyzIsNull(str)){
		top.$.messager.alert("提示",str,"info");
		return false;
	}
	
	var stock = rows[0];
	
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		if(r){
			$.ajax({
				url : "../StockWS/deleteStock.do",
				type : "POST",
				data : {
					numberCode : stock.numberCode
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						$("#stockManagerTable").datagrid("reload");
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

function getTkviewLog(numberCode){
	
	var htmlContent = '<table id = "logWorkTable"></table>';

	xyzdialog({
		dialog:'dialogFormDiv_getTkviewLog',
		title:"查看日志",
	    fit : false,
	    width : 700,
	    height : 430,
	    content : htmlContent,
	    buttons : [{
			text:'关闭',
			handler:function(){
				$("#dialogFormDiv_getTkviewLog").dialog("destroy");
			}
		}],
		onOpen:function(){
			xyzgrid({
				table : 'logWorkTable',
				url:'../TkviewWS/getTkviewLog.do',
				pagination : false,
				singleSelect : false,
				title : "操作日志",
				height : 400,
				queryParams: {
					tkview: numberCode,
				},
				columns : [[
					{field : 'username',title : '操作人'},
					{field : 'value',title : '记录标识'},
					{field : 'addDate',title : '添加时间',
						formatter : function(value, row, index) {
							return xyzGetDivDate(value);
						}
					},
					{field : 'remark',title : '备注',width : 230,
						formatter : function(value, row, index) {
							return "<div title='"+ value + "'>" + value + "</div>";
						}
					}
				]]
			});
		}
	});
}

function getCabinByCruise(){
	var cabinList = [];
	
	$.ajax({
		url : "../StockWS/getCabinByCruise.do",
		type : "POST",
		data : {
			cruise : cruiseNumber
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				cabinList = data.content;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
	return cabinList;
	
}

function getShipmentByCruise(){
	var shipmentList = [];
	
	$.ajax({
		url : "../StockWS/getShipmentByCruise.do",
		type : "POST",
		data : {
			cruise : cruiseNumber
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				shipmentList = data.content;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
	return shipmentList;
}

function getShipmentByTkiew(){
	var tkviewList = [];
	
	$.ajax({
		url : "../StockWS/getShipmentByTkiew.do",
		type : "POST",
		data : {
			cruise : cruiseNumber
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				tkviewList = data.content;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
	return tkviewList;
}

function getCabinByTkiew(){
	var tkviewList = [];
	
	$.ajax({
		url : "../StockWS/getCabinByTkiew.do",
		type : "POST",
		data : {
			cruise : cruiseNumber
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				tkviewList = data.content;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
	return tkviewList;
}

function getStockByTkview(){
	var provideList = [];
	
	$.ajax({
		url : "../StockWS/getStockByTkview.do",
		type : "POST",
		async : false,
		dataType : "json",
		data : {
			cruise : cruiseNumber
		},
		success : function(data) {
			if(data.status==1){
				provideList = data.content;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
	return provideList;
}


function autoEditToSkuButton(){
	
	xyzdialog({
		dialog : 'dialogFormDiv_autoEditToSkuButton',
		title : '自动同步更新 ',
		href : '../jsp_resources/autoEditToSku.html',
		fit : false,
		width : 400,
		height : 200,
	    buttons : [{
			text:'确定',
			handler:function(){
				autoEditToSkuSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_autoEditToSkuButton").dialog("destroy");
			}
		}],
		onLoad : function(){
		}
	});
	
}

function autoEditToSkuSubmit(){
	var value = $("input[type='radio']:checked").val();
	
	$.ajax({
		url : "../TkviewWS/editStockToSku.do",
		type : "POST",
		async : false,
		dataType : "json",
		data : {
			cruise : cruiseNumber,
			updateMode : value
		},
		success : function(data) {
			if(data.status==1){
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

/* 航期点击事件 */
function shipClick(id){
	var shipAll = $("#shipAll").val();
	
	var num = 0;
	var count = 0;
	$("#shipTbody input[id^='ship_']").each(function(){
		num++;
		if($(this).attr("checked") == "checked"){
			count++;
		}
    });
	if(count > 0){
		$("#shipSpan").html("选中"+count+"个航期");
	}else{
		$("#shipSpan").html("");
	}
	
	if(num == count){//全选中
		shipAll = '0';
		$("#shipCheckAll").attr("checked",true);
	}else{
		shipAll = '1';
		$("#shipCheckAll").attr("checked",false);
	}
	$("#shipAll").val(shipAll);
	
}

/* 舱型点击事件 */
function cabinClick(id){
	var cabinAll = $("#cabinAll").val();
	
	var num = 0;
	var count = 0;
	$("#cabinTbody input[id^='cabin_']").each(function(){
		num++;
		if($(this).attr("checked") == "checked"){
			count++;
		}
    });
	if(count > 0){
		$("#cabinSpan").html("选中"+count+"个舱型");
	}else{
		$("#cabinSpan").html("");
	}
	
	if(num == count){//全选中
		cabinAll = '0';
		$("#cabinCheckAll").attr("checked",true);
	}else{
		cabinAll = '1';
		$("#cabinCheckAll").attr("checked",false);
	}
	$("#cabinAll").val(cabinAll);
	
}

/* 供应商点击事件 */
function providerClick(id){
	var providerAll = $("#providerAll").val();
	
	var num = 0;
	var count = 0;
	$("#providerTbody input[id^='provider_']").each(function(){
		num++;
		if($(this).attr("checked") == "checked"){
			count++;
		}
    });
	if(count > 0){
		$("#providerSpan").html("选中"+count+"个舱型");
	}else{
		$("#providerSpan").html("");
	}
	
	if(num == count){//全选中
		cabinAll = '0';
		$("#providerCheckAll").attr("checked",true);
	}else{
		cabinAll = '1';
		$("#providerCheckAll").attr("checked",false);
	}
	$("#providerAll").val(providerAll);
	
}

function addStockListButton(){
	var shipList = getShipmentByCruise();
	var cabinList = getCabinByCruise();
	
	xyzdialog({
		dialog : 'dialogFormDiv_addStockListButton',
		title : '批量新增/编辑库存',
	    href : '../jsp_resources/addStockList.html',
	    buttons : [{
			text:'确定',
			handler:function(){
				addStockListSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addStockListButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			//航期
			var shipNum = 0;
			var shipHtml = '';
			for(var k = 0; k < shipList.length; k++){
				var shipObj = shipList[k];
				if(k % 5 == 0){
					shipNum = shipNum + 4;
					shipHtml += '<tr>';
				}
				shipHtml += '<td>';
				shipHtml += ' <input id="ship_'+ shipObj.numberCode +'" type="checkbox" checked="checked" onClick="shipClick(\''+ shipObj.numberCode +'\')">';
				shipHtml += ' <label for="ship_'+ shipObj.numberCode +'">'+ xyzGetDiv(shipObj.travelDate, 2, 8)+'</label>';
				shipHtml += '</td>';
				if(k == shipNum){
					shipHtml += '</tr>';
				}
				if(k == (shipList.length-1) && k < shipNum){
					for(var h = 0; h < (shipNum-k-1); h++){
						shipHtml += '<td></td>';
					}
					shipHtml += '</tr>';
				}
			}
			$("#shipTbody").html(shipHtml);
			$("#shipSpan").html("选中"+shipList.length+"个航期");
			
			//舱型
			var cabinNum = 0;
			var cabinHtml = '';
			for(var k = 0; k < cabinList.length; k++){
				var cabinObj = cabinList[k];
				if(k % 5 == 0){
					cabinNum = cabinNum + 5;
					cabinHtml += '<tr>';
				}
				cabinHtml += '<td>';
				cabinHtml += ' <input id="cabin_'+ cabinObj.numberCode +'" type="checkbox" checked="checked" onClick="cabinClick(\''+ cabinObj.numberCode +'\')">';
				cabinHtml += ' <label for="cabin_'+ cabinObj.numberCode +'">'+xyzGetDiv(cabinObj.nameCn, 0, 7)+'</label>';
				cabinHtml += '</td>';
				if(k == cabinNum){
					cabinHtml += '</tr>';
				}
				if(k == (cabinList.length-1) && k < cabinNum){
					for(var h = 0; h < (cabinNum-k-1); h++){
						cabinHtml += '<td></td>';
					}
					cabinHtml += '</tr>';
				}
			}
			$("#cabinTbody").html(cabinHtml);
			$("#cabinSpan").html("选中"+cabinList.length+"个舱型");
			
			//航期全选事件
			$("#shipCheckAll").click(function(){
				var shipAll = $("#shipAll").val();
				if(shipAll=='0'){
					shipAll = '1';
					$("#shipTbody input[id^='ship_']").each(function(){
						$(this).attr("checked",false);
				    });
					$("#shipSpan").html("");
				}else{
					shipAll = '0';
					var shipCount = 0;
					$("#shipTbody input[id^='ship_']").each(function(){
						$(this).attr("checked",true);
						shipCount++;
				    });
					$("#shipSpan").html("选中"+shipCount+"个舱型");
				}
				$("#shipAll").val(shipAll);
			});
			
			//舱型全选事件
			$("#cabinCheckAll").click(function(){
				var cabinAll = $("#cabinAll").val();
				if(cabinAll=='0'){
					cabinAll = '1';
					$("#cabinTbody input[id^='cabin_']").each(function(){
						$(this).attr("checked",false);
				    });
					$("#cabinSpan").html("");
				}else{
					cabinAll = '0';
					var cabinCount = 0;
					$("#cabinTbody input[id^='cabin_']").each(function(){
						$(this).attr("checked",true);
						cabinCount++;
				    });
					$("#cabinSpan").html("选中"+cabinCount+"个舱型");
				}
				$("#cabinAll").val(cabinAll);
			});
			
			xyzCombobox({
				required : true,
				combobox : 'providerForm',
				url : '../ListWS/getProviderList.do',
				lazy : false,
				mode : 'remote',
				panelMaxHeight : 350
			});
			xyzTextbox("nickNameForm");
			xyzTextbox("costRemarkForm");
			xyzTextbox("remarkForm");
			
			$("#stockForm").numberbox({
				min:0,
				max:9999,
				required:true,
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});
			
			$("#stockForm").numberbox({
				min : 0,
				max : 9999,
				required : true,
				icons : [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
						$("#costForm").numberbox({
							required : true,
							disabled : false
						});
					}
				}],
				onChange : function(newValue, oldValue){
					if(newValue == 0){
						$("#costForm").numberbox({
							required : false,
							disabled : true
						});
					}else{
						$("#costForm").numberbox({
							required : true,
							disabled : false
						});
					}
				}
			});
			
			$("#costForm").numberbox({
				min:0,
				max:999999999,
				precision:2,
				required:true,
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});
			
			$("#surpassForm").numberbox({
				min:0,
				max:9999,
				value : 0,
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});
			
			//现询、实库--事件
			$("#typeForm").combobox({
				onHidePanel : function(){
					var type = $(this).combobox("getValue");
					if(type == 0){//现询
						$("#stockForm").numberbox({
							required : false,
							disabled : true
						});
						$("#surpassForm").numberbox({
							disabled : true
						});
					}else{//实库
						$("#stockForm").numberbox({
							required : true,
							disabled : false
						});
						$("#surpassForm").numberbox({
							disabled : false
						});
					}
				}
			});
			
		}
	});
}

function addStockListSubmit(){
	if(!$("form").form('validate')){
		return false;
	}
	
	var shipCount = 0;
	var cabinCount = 0;
	var shipStr = "";
	var cabinStr = "";
	$("#shipTbody input[id^='ship_']").each(function(){
    	if($(this).attr("checked") == "checked"){
    		var id = $(this).prop('id').split('_')[1];
    		shipStr = xyzIsNull(shipStr)==true?id:(shipStr+","+id);
    		shipCount++;
    	}
    });
	$("#cabinTbody input[id^='cabin_']").each(function(){
    	if($(this).attr("checked") == "checked"){
    		var id = $(this).prop('id').split('_')[1];
    		cabinStr = xyzIsNull(cabinStr)==true?id:(cabinStr+","+id);
    		cabinCount++;
    	}
    });
	
	if(xyzIsNull(cabinStr) && xyzIsNull(shipStr)){
		top.$.messager.alert("温馨提示","请选择航期或者舱型!","info");
		return false;
	}
	
	var nickName = $("#nickNameForm").val();
	var provider = $("#providerForm").combobox("getValue");
	var type = $("#typeForm").combobox("getValue");
	var stock = $("#stockForm").numberbox('getValue') == "" ? 0:$("#stockForm").numberbox('getValue');
	var surpass = $("#surpassForm").numberbox('getValue') == "" ? 0:$("#surpassForm").numberbox('getValue');
	var cost = $("#costForm").numberbox('getValue') == "" ? 0:$("#costForm").numberbox('getValue');
	var costRemark = $("#costRemarkForm").val();
	var remark = $("#remarkForm").val();
	
	var str = "确认新增/编辑 ";
	if(shipCount > 0){
		str += shipCount+"个航期 ";
	}
	if(cabinCount > 0){
		str += cabinCount+"个舱型 ";
	}
	str += "?";
	$.messager.confirm('确认', str,function(r) {
		if(r){
			if(cost <= 1000 && cost > 0){
				$.messager.confirm('确认', "确定成本价为【"+ cost +"】吗?",function(r) {
					if(r){
						$.ajax({
							url : "../StockWS/addStockList.do",
							type : "POST",
							data : {
								cabinStr : cabinStr,
								shipStr : shipStr,
								nickName : nickName,
								provider : provider,
								type : type,
								stock : stock,
								surpass : surpass,
								cost : xyzSetPrice(cost),
								costRemark : costRemark,
					            priority : 5,
					            remark : remark
							},
							async : false,
							dataType : "json",
							success : function(data) {
								if(data.status==1){
									$("#tkviewManagerTable").datagrid("reload");
									$("#stockManagerTable").datagrid("reload");
									$("#dialogFormDiv_addStockListButton").dialog("destroy");
									top.$.messager.alert("提示","操作成功!","info");
								}else{
									top.$.messager.alert("警告",data.msg,"warning");
								}
							},
							error : function(XMLHttpRequest, textStatus, errorThrown) {
								top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
							}
						});
					}else{
						$("#costForm").numberbox('clear');
					}
				});
			}else{
				$.ajax({
					url : "../StockWS/addStockList.do",
					type : "POST",
					data : {
						cabinStr : cabinStr,
						shipStr : shipStr,
						nickName : nickName,
						provider : provider,
						type : type,
						stock : stock,
						surpass : surpass,
						cost : xyzSetPrice(cost),
						costRemark : costRemark,
			            priority : 5,
			            remark : remark
					},
					async : false,
					dataType : "json",
					success : function(data) {
						if(data.status==1){
							$("#tkviewManagerTable").datagrid("reload");
							$("#stockManagerTable").datagrid("reload");
							$("#dialogFormDiv_addStockListButton").dialog("destroy");
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
		}
	});
	
}

function deleteTkvewByCabinButton(){
	xyzdialog({
		dialog : 'dialogFormDiv_deleteTkvewByCabinButton',
		title : '删除单品',
	    href : '../jsp_resources/deleteTkvewByCabin.html',
	    fit :false,
	    width : 400,
	    heigth : 300,
	    buttons : [{
			text:'确定',
			handler:function(){
				deleteTkvewByCabinSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_deleteTkvewByCabinButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzCombobox({
				//required : true,
				combobox : 'cabinByCruise',
				url : '../ListWS/getCabinListByCruise.do',
				mode : 'remote',
				lazy : false, 
				panelMaxHeight : 200,
				multiple : true,
				onBeforeLoad: function(param){
					param.cruise = cruiseNumber;
			   	},
			   	onChange : function(newValue, oldValue){
			   		$("#cabinInput").val(newValue);
			   	},
			   	icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			
			xyzCombobox({
				//required : true,
				combobox : 'shipByCruise',
				url : '../ListWS/getShipmentList.do',
				mode : 'remote',
				lazy : false, 
				panelMaxHeight : 180,
				multiple : true,
				onBeforeLoad: function(param){
					param.cruise = cruiseNumber;
					param.isTkview = 1;
			   	},
			   	onChange : function(newValue, oldValue){
			   		$("#shipInput").val(newValue);
			   	},
			   	icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			
			xyzCombobox({
				//required : true,
				combobox : 'providerByCruise',
				url : '../ListWS/getProviderByCruiseList.do',
				mode : 'remote',
				lazy : false, 
				panelMaxHeight : 160,
				multiple : true,
				onBeforeLoad: function(param){
					param.cruise = cruiseNumber;
			   	},
			   	onChange : function(newValue, oldValue){
			   		$("#providerInput").val(newValue);
			   	},
			   	icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			
		}
	});
	
}

function deleteTkvewByCabinSubmit (){
	var cabin = $("#cabinInput").val();  
	var shipment = $("#shipInput").val();  
	var provider = $("#providerInput").val();  
	
	if(xyzIsNull(cabin) && xyzIsNull(shipment) && xyzIsNull(provider)){
		top.$.messager.alert("温馨提示","请选择舱型、航期或者供应商!","info");
		return false;
	}
	
	$.messager.confirm('确认','确认要删除选择舱型关联的所有单品信息吗？',function(r){    
	    if (r){    
	    	
	    	$.ajax({
	    		url : "../TkviewWS/deleteTkviewByCabin.do",
	    		type : "POST",
	    		data : {
	    			cabin : cabin,
	    			shipment : shipment,
	    			provider : provider
	    		},
	    		async : false,
	    		dataType : "json",
	    		success : function(data) {
	    			if(data.status==1){
	    				$("#dialogFormDiv_deleteTkvewByCabinButton").dialog("destroy");
	    				$("#tkviewManagerTable").datagrid("reload");
	    				$("#stockManagerTable").datagrid("reload");
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

function  realStockButton(){

	var shipList = getShipmentByTkiew();
	var cabinList = getCabinByTkiew();
	var providerList =getStockByTkview();
	
	xyzdialog({
		dialog : 'dialogFormDiv_realStockButton',
		title : '一键实库 ',
	    href : '../jsp_resources/realStock.html',
	    fit:false,
		width : 800,
		height : 800,
	    buttons : [{
			text:'确定',
			handler:function(){
				realStockSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_realStockButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			//航期
			var shipNum = 0;
			var shipHtml = '';
			for(var k = 0; k < shipList.length; k++){
				var shipObj = shipList[k];
				if(k % 5 == 0){
					shipNum = shipNum + 4;
					shipHtml += '<tr style=" display:block;margin:20px 0;">';
				}
				shipHtml += '<td width="120px">';
				shipHtml += ' <input id="ship_'+ shipObj.shipment +'" type="checkbox" checked="checked" onClick="shipClick(\''+ shipObj.shipment +'\')">';
				shipHtml += ' <label for="ship_'+ shipObj.shipment +'">';
				shipHtml += xyzGetDiv(shipObj.shipmentTravelDate, 2, 8)+'</label>';
				shipHtml += '</td>';
				if(k == shipNum){
					shipHtml += '</tr>';
				}
				if(k == (shipList.length-1) && k < shipNum){
					for(var h = 0; h < (shipNum-k-1); h++){
						shipHtml += '<td></td>';
					}
					shipHtml += '</tr>';
					
				}
			}
			$("#shipTbody").html(shipHtml);
			$("#shipSpan").html("选中"+shipList.length+"个航期");
			
			//舱型
			var cabinNum = 0;
			var cabinHtml = '';
			for(var k = 0; k < cabinList.length; k++){
				var cabinObj = cabinList[k];
				if(k % 5 == 0){
					cabinNum = cabinNum + 5;
					cabinHtml += '<tr style=" display:block;margin:20px 0;">';
				}
				cabinHtml += '<td width="120px">';
				cabinHtml += ' <input id="cabin_'+ cabinObj.cabin +'" type="checkbox" checked="checked" onClick="cabinClick(\''+ cabinObj.cabin +'\')">';
				cabinHtml += ' <label for="cabin_'+ cabinObj.cabin +'">'+xyzGetDiv(cabinObj.cabinNameCn, 0, 7)+'</label>';
				cabinHtml += '</td>';
				if(k == cabinNum){
					cabinHtml += '</tr>';
				}
				if(k == (cabinList.length-1) && k < cabinNum){
					for(var h = 0; h < (cabinNum-k-1); h++){
						cabinHtml += '<td></td>';
					}
					cabinHtml += '</tr>';
				}
			}
			$("#cabinTbody").html(cabinHtml);
			$("#cabinSpan").html("选中"+cabinList.length+"个舱型");
			
			//供应商
			var providerNum = 0;
			var providerHtml = '';
			for(var k = 0; k < providerList.length; k++){
				var providerObj = providerList[k];
				if(k % 5 == 0){
					providerNum = providerNum + 5;
					providerHtml += '<tr style=" display:block;margin:20px 0;">';
				}
				providerHtml += '<td width="120px">';
				providerHtml += ' <input id="provider_'+ providerObj.provider +'" type="checkbox" checked="checked" onClick="providerClick(\''+ providerObj.provider +'\')">';
				providerHtml += ' <label for="cabin_'+ providerObj.provider +'">'+xyzGetDiv(providerObj.providerNameCn, 0, 7)+'</label>';
				providerHtml += '</td>';
				if(k == providerNum){
					providerHtml += '</tr>';
				}
				if(k == (providerList.length-1) && k < providerNum){
					for(var h = 0; h < (providerNum-k-1); h++){
						providerHtml += '<td></td>';
					}
					providerHtml += '</tr>';
				}
			}
			$("#providerTbody").html(providerHtml);
			$("#providerSpan").html("选中"+providerList.length+"个供应商");
			
			//航期全选事件
			$("#shipCheckAll").click(function(){
				var shipAll = $("#shipAll").val();
				if(shipAll=='0'){
					shipAll = '1';
					$("#shipTbody input[id^='ship_']").each(function(){
						$(this).attr("checked",false);
				    });
					$("#shipSpan").html("");
				}else{
					shipAll = '0';
					var shipCount = 0;
					$("#shipTbody input[id^='ship_']").each(function(){
						$(this).attr("checked",true);
						shipCount++;
				    });
					$("#shipSpan").html("选中"+shipCount+"个航期");
				}
				$("#shipAll").val(shipAll);
			});
			
			//舱型全选事件
			$("#cabinCheckAll").click(function(){
				var cabinAll = $("#cabinAll").val();
				if(cabinAll=='0'){
					cabinAll = '1';
					$("#cabinTbody input[id^='cabin_']").each(function(){
						$(this).attr("checked",false);
				    });
					$("#cabinSpan").html("");
				}else{
					cabinAll = '0';
					var cabinCount = 0;
					$("#cabinTbody input[id^='cabin_']").each(function(){
						$(this).attr("checked",true);
						cabinCount++;
				    });
					$("#cabinSpan").html("选中"+cabinCount+"个舱型");
				}
				$("#cabinAll").val(cabinAll);
			});
			
			//供应商全选事件
			$("#providerCheckAll").click(function(){
				var providerAll = $("#providerAll").val();
				if(providerAll=='0'){
					providerAll = '1';
					$("#providerTbody input[id^='provider_']").each(function(){
						$(this).attr("checked",false);
				    });
					$("#providerSpan").html("");
				}else{
					providerAll = '0';
					var providerCount = 0;
					$("#providerTbody input[id^='provider_']").each(function(){
						$(this).attr("checked",true);
						providerCount++;
				    });
					$("#providerSpan").html("选中"+providerCount+"个供应商");
				}
				$("#providerAll").val(providerAll);
			});
			
		}
	});
}

function realStockSubmit(){
	var cabinCount = 0;
	var cabinStr = "";
	$("#cabinTbody input[id^='cabin_']").each(function(){
		var tempId = $(this).prop('id').split('_')[1];
    	if($(this).attr("checked") == "checked"){
    		cabinStr = xyzIsNull(cabinStr)==true?tempId:(cabinStr+","+tempId);
    		cabinCount++;
    	}
    });
	
	var shipCount = 0;
	var shipmentStr = "";
	$("#shipTbody input[id^='ship_']").each(function(){
		var tempId = $(this).prop('id').split('_')[1];
    	if($(this).attr("checked") == "checked"){
    		shipmentStr = xyzIsNull(shipmentStr)==true?tempId:(shipmentStr+","+tempId);
    		shipCount++;
    	}
    });
	
	var providerCount = 0;
	var providerStr = "";
	$("#providerTbody input[id^='provider_']").each(function(){
		var tempId = $(this).prop('id').split('_')[1];
    	if($(this).attr("checked") == "checked"){
    		providerStr = xyzIsNull(providerStr)==true?tempId:(providerStr+","+tempId);
    		providerCount++;
    	}
    });
	if(xyzIsNull(cabinStr) && xyzIsNull(shipmentStr)&&xyzIsNull(providerStr)){
		top.$.messager.alert("温馨提示","请选择舱型或者航期或者供应商!","info");
		return false;
	}
	$.ajax({
		url : "../StockWS/editStockByCabinAndShipmentAndProvider.do",
		type : "POST",
		data : {
			cabin : cabinStr,
			shipment : shipmentStr,
			provider : providerStr
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#dialogFormDiv_realStockButton").dialog("destroy");
				$("#tkviewManagerTable").datagrid("reload");
				$("#stockManagerTable").datagrid("reload");
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