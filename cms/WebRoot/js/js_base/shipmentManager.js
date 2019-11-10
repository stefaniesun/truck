var cruiseList = null;
var startDate = null;
$(document).ready(function(){
	//给结束日期框赋值
	$('#startDate').datebox('setValue',new Date().Format("yyyy-MM-dd"));
	startDate = $('#startDate').datebox("getValue");
	
	xyzCombobox({
		combobox:'cruise',
		mode: 'remote',
		url:'../ListWS/getCruiseList.do'
	});
	
	xyzTextbox("nameCn");
	xyzTextbox("mark");
	
	initTable();
	initTabs();
	
	$("#shipmentQueryButton").click(function(){
		loadTable();
	});
	
});

var rows = 0;
function initTable(){
	var toolbar = [];
	if (xyzControlButton("buttonCode_x20161207200402")) {
		toolbar[toolbar.length] = {
			text : '新增',
			border : '1px solid #bbb',
			iconCls : 'icon-add',
			handler : function() {
				addShipmentButton();
			}
		};
	}
	
	if (xyzControlButton("buttonCode_z20170206095200")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length] = {
			text : '批量新增',
			border : '1px solid #bbb',
			iconCls : 'icon-add',
			handler : function() {
				addMoreShipmentsButton(); 
			}
		};
	}
	
	if (xyzControlButton("buttonCode_x20161207200403")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length] = {
			text : '编辑',
			border : '1px solid #bbb',
			iconCls : 'icon-edit',
			handler : function() {
				editShipmentButton(); 
			}
		};
	}
	if (xyzControlButton("buttonCode_x20161207200404")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length] = {
			text : '删除',
			border : '1px solid #bbb',
			iconCls : 'icon-remove',
			handler : function() {
				deleteShipmentButton();
			}
		};
	}
	
	if (xyzControlButton("buttonCode_x20161207200406")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length] = {
			text : '克隆',
			border : '1px solid #bbb',
			iconCls : 'icon-add',
			handler : function() {
				cloneShipmentButton(); 
			}
		};
	}
	
	xyzgrid({
		table : 'shipmentManagerTable',
		url : '../ShipmentWS/queryShipmentList.do',		
		toolbar : toolbar,
		title : '航期列表',
		singleSelect : false,
		idField : 'numberCode',
		columns : [ [
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'航期编号',width:74,halign:'center',
				formatter : function(value, row, index) {
					return xyzGetDiv(value, 0, 16);
				}
			},
			{field:'mark',title:'航期代码',width:74,halign:'center'},
			{field:'travelDate',title:'出发日期',width:80,align:'center',sortable:true,order:'desc',
				formatter : function(value, row, index) {
					return value.substring(0, 10);
				}
			},
			{field:'travelEndDate',title:'结束日期',width:80,align:'center',sortable:true,order:'desc',
				formatter : function(value, row, index) {
					return value.substring(0, 10);
				}
			},
			{field:'finalSaleDate',title:'最后售卖日期',width:90,align:'center',sortable:true,order:'desc',
				formatter : function(value, row, index) {
					return xyzGetDivDate(value);
				}
			},
			{field:'cruise',title:'邮轮编号',hidden:true,halign:'center'},
			{field:'cruiseNameCn',title:'邮轮名称',width:90,halign:'center',
        	  formatter:function(value ,row ,index){
					return xyzGetDiv(value, 0, 50);
				}
			},
			{field:'area',title:'航区编号',hidden:true,halign:'center'},
			{field:'areaNameCn',title:'航区名称',halign:'center',
        	  formatter:function(value ,row ,index){
				return xyzGetDiv(value, 0, 30);
			  }
			},
			{field:'startPlace',title:'出发地',halign:'center',
        	  formatter:function(value ,row ,index){
				return xyzGetDiv(value, 0, 3);
			  }
			},
			{field:'tripDays',title:'航程天数',align:'center'},
			{field:'airway',title:'航线编号',hidden:true,align:'center'},
			{field:'airwayNameCn',title:'航线名称',width:148,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value, 0, 40);
				}
			},
			{field:'voyage',title:'操作',align:'center',
				formatter: function(value,row,index){
					var btnTemp = "";
					if(xyzControlButton("buttonCode_x20161213113305")){
						btnTemp += xyzGetA("设置航线","setAirway",[row.numberCode,row.airway,row.airwayNameCn],"点我设置航线","blue");
					}
					if(xyzControlButton("buttonCode_x20161213113303")){
						btnTemp += " "+xyzGetA("查看行程路线信息","voyageManager",[row.numberCode,row.tripDays,row.travelDate,row.travelEndDate,row.airwayNameCn],"点我管理行程路线","blue");
					}
					return btnTemp;
				}
			},
			{field:'detail',title:'详情',align:'center',
				formatter : function(value, row, index) {
					var html = "";
	        		  if(xyzControlButton("buttonCode_x20161207200405")){
	        			  html = xyzGetA("详情","addDetail",[row.numberCode,index],"详情","blue");
	        		  }else {
	        			  html = xyzGetA("详情","checkDetail",[row.numberCode,index],"查看详情","blue");
	        		  }
	        		  return html;
				}
			},
		    {field:'addDate',title:'添加时',width:80,hidden:true,align:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'alterDate',title:'修改时',width:100,align:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'remark',title:'备注',width:90,align:'center',
		    	formatter:function(value, row, index){
		    		return xyzGetDiv(value, 0, 40);
		    	}
		    }
		] ],
		onLoadSuccess : function(data){
			rows = data.rows.length;
		}
	});
}

function loadTable(){
	var tab = $('#shipmentManagerTabs').tabs('getSelected');
	var cruise = tab.attr("id"); 
	
	var startDate = $("#startDate").datebox("getValue");
	var endDate = $("#endDate").datebox("getValue");
	var mark = $("#mark").val();
	
	$("#shipmentManagerTable").datagrid('load',{
		cruise : cruise,
		startDate : startDate,
		endDate : endDate,
		mark : mark
	});
	
	var tab = $("#shipmentManagerTabs").tabs("getSelected");
	var nameCn = $(".tabs-selected").text(); 
	var arry = nameCn.split("(");
	$('#shipmentManagerTabs').tabs('update', {
		tab : tab,
		options: {
			title: arry[0]+"("+ rows +")"
		}
	});
	
}

function initTabs(){
	
	xyzAjax({
		url : "../ShipmentWS/queryShipmentGroupCruise.do",
		type : "POST",
		async : false,
		dataType : "json",
		data : {},
		success : function(data) {
			if(data.status==1){
				cruiseList = data.content;
				for(var t = 0; t < cruiseList.length; t++){
					var cruiseObj = cruiseList[t];
					var cruise = cruiseObj[0];
					var cruiseNameCn = cruiseObj[1];
					addTabs(cruiseNameCn+"("+ cruiseObj[2] +")" , cruise);
				}
				$("#shipmentManagerTabs").tabs({
					onSelect : function(title, index){
						var tab = $('#shipmentManagerTabs').tabs('getSelected');
						var cruise = tab.attr("id"); 
						loadTable(cruise);
				    }
				});
				$('#shipmentManagerTabs').tabs({
					select : 0
				});
			}
		}
	});
}

function addTabs(title, cruise){
	$("#shipmentManagerTabs").tabs('add',{
	    title : title,    
	    id : cruise,
	    content : '',
	    closable : false
	});
	
}

function loadTabs(cruiseNumber, nameCn, type){
	var arry = [];
	var pd = true;
	for(var t = 0; t < cruiseList.length; t++){
		var cruiseObj = cruiseList[t];
		var cruise = cruiseObj[0];
		if(cruise == cruiseNumber && type=="delete"){
			 $('#shipmentManagerTabs').tabs('close', cruiseNumber);
			 pd = false;
		}else{
			arry[arry.length] = cruiseObj;
		}
	}
	if(pd){
		var cruiseObj = [cruiseNumber, nameCn, 1];
		arry[arry.length] = cruiseObj;
	}
	
}

function addShipmentButton(){
	var shipments = $("#shipmentManagerTable").datagrid("getChecked");
	var row = null;
	if (shipments.length > 0) {
		row = shipments[0];
	}
	
	xyzdialog({
		dialog : 'dialogFormDiv_addShipmentButton',
		title : '新增航期',
		href : '../jsp_base/addShipment.html',
		fit : false,
		width : 450,
		height : 450,
		buttons : [ {
			text : '确定',
			handler : function() {
				addShipmentSubmit();
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_addShipmentButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			xyzTextbox("startPlaceForm");
			
			xyzCombobox({
				required : true,
				combobox : 'cruiseForm',
				url : '../ListWS/getCruiseList.do',
				mode : 'remote',
				lazy : true,
				panelMaxHeight : 300,
				onHidePanel : function(){
					var cruise = $(this).combobox("getText");
					var shipMark = cruise.substring(1,cruise.indexOf('】'));
					if(!xyzIsNull($('#travelDateForm').datebox("getValue"))){
						var travel = $('#travelDateForm').datebox("getValue");
						var date = new Date(travel);
				    	var tempMonth = (date.getMonth()+1);
				    	var tempDay = date.getDate();
				    	tempMonth = tempMonth<10?'0'+tempMonth:tempMonth;
				    	tempDay = tempDay<10?'0'+tempDay:tempDay;
				    	var text = (date.getFullYear()+''+tempMonth+''+tempDay).substr(2,6);
				    	shipMark += text;
					}
					$("#markForm").html(shipMark);
				},
				onChange : function (newValue,oldValue){
					if(xyzIsNull(newValue)) {
						var mark = $("#markForm").html();
						if(!xyzIsNull(mark) && mark.length > 6){
							mark = mark.substring(0, mark.length-6);
						}
						$("#markForm").html(mark);	
					}
				}
			});
			
			if(!xyzIsNull(row)){
				$("#cruiseForm").combobox({
					value : row.cruise
				});
			}
			
			xyzCombobox({
				required : true,
				combobox : 'areaForm',
				url : '../ListWS/getAreaList.do',
				mode: 'remote',
				lazy : false,
				onChange : function() {
					$("#airwayForm").combobox("clear");
					$("#airwayForm").combobox("reload");
				}
			});
			
			xyzCombobox({
				required : true,
				combobox : 'airwayForm',
				url : '../ListWS/getAirwayList.do',
				mode: 'remote',
				lazy : false,
				onBeforeLoad: function(param){
					var area = $("#areaForm").combobox("getValue");
					param.area = area;
					param.language = 'js';
				}
			});
			
			$('#travelDateForm').datebox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).datebox('clear');
						var mark = $("#markForm").html();
						if(!xyzIsNull(mark) && mark.length > 6){
							mark = mark.substring(0, mark.length-6);
						}
						$("#markForm").html(mark);
					}
				}],
			    onHidePanel: function(){
			    	var travel = $(this).datebox('getValue');
			    	var date = new Date(travel);
			    	
			    	var nowDate = new Date();
			    	if(date.getTime() < nowDate.getTime()){
			    		top.$.messager.alert("提示", "出发日期不能小于等于当前日期!", "info");
			    		return false;
			    	}
			    	
			    	//航期代码
			    	if(!(xyzIsNull($("#cruiseForm").combobox("getText")) && xyzIsNull(travel)) ){
			    		var year = date.getFullYear();
			    		var month = date.getMonth()+1;
				    	var day = date.getDate();
			    		month = month<10 ? ("0"+month) : month;
			    		day = day<10 ? ("0"+day) : day;
				    	var text = (year +''+month+''+day).substr(2,6);
				    	var cruise = $("#cruiseForm").combobox("getText");
				    	$("#markForm").html(cruise.substring(1,cruise.indexOf('】')) + text);
			    	}
			    	
			    	//默认最后售卖日期
			    	var tempDate = new Date(date.getTime()-3*24*60*60*1000);
		    		var tempYear = tempDate.getFullYear();
		    		var tempMonth = tempDate.getMonth()+1;
		    		var tempDay = tempDate.getDate();
		    		var tempFinalSale = tempYear+"-"+tempMonth+"-"+tempDay+" 00:00:00";
		    		$("#finalSaleDateForm").datetimebox('setValue', tempFinalSale);
			    }
			});
			
			$('#finalSaleDateForm').datetimebox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).datetimebox('clear');
					}
				}],
				onHidePanel: function(){
					var finalSale = new Date($(this).datetimebox('getValue'));
					
			    	var travel = new Date($('#travelDateForm').datebox("getValue"));
			    	
			    	if(travel.getTime() < finalSale.getTime()){
			    		top.$.messager.alert("提示", "最后售卖日期不能晚于出发日期!", "info");
			    		var tempDate = new Date(travel.getTime()-3*24*60*60*1000);
			    		var tempYear = tempDate.getFullYear();
			    		var tempMonth = tempDate.getMonth()+1;
			    		var tempDay = tempDate.getDate();
			    		var tempFinalSale = tempYear+"-"+tempMonth+"-"+tempDay+" 00:00:00";
			    		$(this).datetimebox('setValue', tempFinalSale);
			    		return false;
			    	}
				}
			});
		}
	});
}

function addShipmentSubmit(){
	if(!$("form").form('validate')) {
		return false;
	}

	var cruise = $("#cruiseForm").combobox('getValue');
	var area = $("#areaForm").combobox('getValue');
	var airway = $("#airwayForm").combobox('getValue');
	var mark = $("#markForm").html();
	var travelDate = $("#travelDateForm").datebox("getValue");
	var finalSaleDate = $("#finalSaleDateForm").datetimebox("getValue");
	var startPlace = $("#startPlaceForm").textbox("getValue");
	if(xyzIsNull(startPlace)){
		startPlace = "任意城市";
	}
	var remark = $("#remarkForm").val();
	
	voyageList = $.map($("#tripTable tr[id^='voyageTr_']"),
		function(p){
			return $(p).attr("id").split("_")[1];
		}
	).join(",");
	var voyageJson = []; 
	for(var k = 0; k < voyageList.length; k++){
		var id = voyageList[k];
		var time = $("#timeForm_"+id).val();
		var port = $("#portForm_"+id).val();
		var arrival = $("#arrivalTimeForm_"+id).val();
		var leave = $("#leaveTimeForm_"+id).val();
		var detail = $("#detailForm_"+id).val();
		var voyageRemark = $("#remarkForm_"+id).val();
		
		var voyage = {
			time : time,
			port : port,
			arrivalTime : arrival,
			leaveTime : leave,
			detail : detail,
			remark : voyageRemark
		}; 
		voyageJson[voyageJson.length] = voyage;
	}
	
	$.ajax({
		url : "../ShipmentWS/addShipment.do",
		type : "POST",
		data : {
			cruise : cruise,
			area : area,
			airway : airway,
			mark:mark,
			travelDate : travelDate,
			finalSaleDate : finalSaleDate,
			startPlace : startPlace,
			remark : remark,
			voyageJson : JSON.stringify(voyageJson)
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#shipmentManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				window.location.reload();
				$("#dialogFormDiv_addShipmentButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function addMoreShipmentsButton() {
	var shipments = $("#shipmentManagerTable").datagrid("getChecked");
	var row = null;
	if (shipments.length > 0) {
		row = shipments[0];
	}
	
	xyzdialog({
		dialog : 'dialogFormDiv_addMoreShipmentsButton',
		title : '批量新增航期',
		href : '../jsp_base/addMoreShipments.html',
		fit : true,
		width : 450,
		height : 450,
		buttons : [ {
			text : '确定',
			handler : function() {
				addMoreShipmentsSubmit();
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_addMoreShipmentsButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			xyzTextbox("startPlaceForm");
			xyzCombobox({
				required : true,
				combobox : 'curiseForm',
				url : '../ListWS/getCruiseList.do',
				mode : 'remote',
				lazy : true,
				onChange : function() {
					var cruise = $(this).combobox("getText");
					var mark = cruise.substring(1,cruise.indexOf('】'));
					$("#cruiseMark").val(mark);
					editShipMark();
					
					$("#areaForm").combobox("clear");
					$("#areaForm").combobox("reload");
					
					$("#airwayForm").combobox("clear");
					$("#airwayForm").combobox("reload");
				}
			});
			
			if(!xyzIsNull(row)){
				$("#curiseForm").combobox({
					value : row.cruise
				});
			}
			
			xyzCombobox({
				required : true,
				combobox : 'areaForm',
				url : '../ListWS/getAreaList.do',
				mode: 'remote',
				lazy : false,
				onChange : function() {
					$("#airwayForm").combobox("clear");
					$("#airwayForm").combobox("reload");
				}
			});   
			
			xyzCombobox({
				required : true,
				combobox : 'airwayForm',
				url : '../ListWS/getAirwayList.do',
				mode: 'remote',
				lazy : false,
				onBeforeLoad: function(param){
					var area = $("#areaForm").combobox("getValue");
					param.area = area;
					param.language = 'js';
				}
			});
			
			$("#addShipmentHtml").click(function() {
				addShipmentHtml();
			});
			
			shipmentHtmlEvent(0);
		}
	});
}

function editShipMark(){
	var tempTrList = $.map($("tr[id^='shipmentTrId_']"),
		function(p){
			return $(p).attr("id").split("_")[1];
		}
	);
	
	var cruiseMark = $("#cruiseMark").val();
	for(var t = 0; t < tempTrList.length; t++){
		var dateShip = $("#travelDateForm_"+t).datebox("getValue");
		if(!xyzIsNull(dateShip)){
			var subDate = dateShip.substring(2);
			var mark = cruiseMark + subDate.replace(/-/g,"");
			$("#markForm_"+t).html(mark);
		}
	}
}

function addShipmentHtml() {
	var tempLength = $(".shipmentTrClass:last").prop('id').split('_')[1];
	tempLength = parseInt(tempLength)+1;
	var html = "";
	html += '<tr class="shipmentTrClass" id="shipmentTrId_'+tempLength+'">';
	html += ' <td>';
	html += ' <label id="markForm_'+tempLength+'" style="width:100px"></label>';
	html += ' </td>';
	html += ' <td>';
	html += '  <input id="travelDateForm_'+tempLength+'" style="width:170px"/>';
	html += ' </td>';
	html += ' <td>';
	html += '  <input id="finalSaleDateForm_'+tempLength+'" style="width:180px"/>';
	html += ' </td>';
	html += ' <td>';
	html += '  <input id="remarkForm_'+tempLength+'" style="width:200px"/>';
	html += ' </td>';
	html += ' <td>';
	html += '  <img id="delete_'+tempLength+'" alt="点我删除此单品" src="../image/other/delete.png" title="点我删除此单品" style="cursor:pointer;">';
	html += ' </td>';
	html += '</tr>';
	$("#shipTbody").append(html);
	shipmentHtmlEvent(tempLength);
}

function shipmentHtmlEvent(index) {
	
	xyzTextbox("remarkForm_"+index);
	$('#travelDateForm_'+index).datebox({
		required : true,
		icons : [{
			iconCls : 'icon-clear',
			handler: function(e){
				$(e.data.target).datebox('clear');
				var mark = $("#markForm_"+index).html();
				if(!xyzIsNull(mark) && mark.length > 6){
					mark = mark.substring(0, mark.length-6);
				}
				$("#markForm_"+index).html(mark);
			}
		}],
		onHidePanel: function(){
			var travel = $(this).datebox('getValue');
	    	var date = new Date(travel);
	    	
	    	var nowDate = new Date();
	    	if(date.getTime() < nowDate.getTime()){
	    		top.$.messager.alert("提示", "出发日期不能小于等于当前日期!", "info");
	    		$(this).datebox('clear');
	    		return false;
	    	}
	    	
	    	//航期代码
	    	var cruise = $('#curiseForm').combobox("getText");
	    	if(!(xyzIsNull(cruise) && xyzIsNull(travel)) ){
	    		var year = date.getFullYear();
	    		var month = date.getMonth()+1;
		    	var day = date.getDate();
	    		month = month<10 ? ("0"+month) : month;
	    		day = day<10 ? ("0"+day) : day;
		    	var text = (year +''+month+''+day).substr(2,6);
		    	$("#markForm_"+index).html(cruise.substring(1,cruise.indexOf('】')) + text);
	    	}
	    	
	    	//默认最后售卖日期
	    	var tempDate = new Date(date.getTime()-3*24*60*60*1000);
    		var tempYear = tempDate.getFullYear();
    		var tempMonth = tempDate.getMonth()+1;
    		var tempDay = tempDate.getDate();
    		var tempFinalSale = tempYear+"-"+tempMonth+"-"+tempDay+" 00:00:00";
    		$("#finalSaleDateForm_"+index).datetimebox('setValue', tempFinalSale);
	    }
	});
	
	$('#finalSaleDateForm_'+index).datetimebox({
		required : true,
		icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).datetimebox('clear');
			}
		}],
		onHidePanel: function(){
	    	var finalSale = new Date($(this).datetimebox('getValue'));
			
	    	var travel = new Date($('#travelDateForm_'+index).datebox("getValue"));
	    	
	    	if(travel.getTime() < finalSale.getTime()){
	    		top.$.messager.alert("提示", "最后售卖日期不能晚于出发日期!", "info");
	    		var tempDate = new Date(travel.getTime()-3*24*60*60*1000);
	    		var tempYear = tempDate.getFullYear();
	    		var tempMonth = tempDate.getMonth()+1;
	    		var tempDay = tempDate.getDate();
	    		var tempFinalSale = tempYear+"-"+tempMonth+"-"+tempDay+" 00:00:00";
	    		$(this).datetimebox('setValue', tempFinalSale);
	    		return false;
	    	}
		}
	});
	
	$("#delete_"+index).click(function() {
		var tempIndex = $(this).prop('id').split('_')[1];
		$("#shipmentTrId_"+tempIndex).remove();
	});		
}

function addMoreShipmentsSubmit() {
	if(!$("form").form('validate')){
		return false;
	}
	var cruise = $("#curiseForm").combobox("getValue");
	var area = $("#areaForm").combobox("getValue");
	var airway = $("#airwayForm").combobox("getValue");
	var startPlace = $("#startPlaceForm").textbox("getValue");
	if(xyzIsNull(startPlace)){
		startPlace = "任意城市";
	}
	var shipmentJson = [];
	for ( var i = 0; i < $(".shipmentTrClass").length; i++) {
		var index = $(".shipmentTrClass").eq(i).prop('id').split('_')[1];
		var mark = $("#markForm_"+index).html();
		var travelDate = $("#travelDateForm_"+index).datebox("getValue");
		var finalSaleDate = $("#finalSaleDateForm_"+index).datetimebox("getValue");
		var remark = $("#remarkForm_"+index).val();
		var tempJson =  {
				mark : mark,
				travelDate : travelDate,
				finalSaleDate : finalSaleDate,
				remark : remark
			};
		shipmentJson[shipmentJson.length] = tempJson;
	}
	shipmentJson = JSON.stringify(shipmentJson);
	$.ajax({
		url : "../ShipmentWS/addMoreShipment.do",
		type : "POST",
		data : {
			cruise :cruise,
			area :area,
			airway :airway,
			startPlace : startPlace,
			shipmentJsonStr : shipmentJson
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#shipmentManagerTable").datagrid("reload");
				$("#dialogFormDiv_addMoreShipmentsButton").dialog("destroy");
				window.location.reload();
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

function tripHtml(route){
	$.ajax({
		url : '../ShipmentWS/queryTripListByRoute.do',
		type : "POST",
		data : {
			route : route
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var tripList = data.content.tripList;
				$("#voyageTable").empty();
				var voyageHtml = '';
				tempPort += '<tr>';
				tempPort += ' <td colspan="6">航程</td>';
				tempPort += '</tr>';
				tempPort += '<tr>';
				tempPort += ' <td colspan="6"><hr style="color:#808080"></td>';
				tempPort += '</tr>';
				voyageHtml += '<tr>';
				voyageHtml += ' <th style="width:100px;">时间(第1天)</th>';
				voyageHtml += ' <th style="width:100px;">港口</th>';
				voyageHtml += ' <th>抵达时间(19:30)</th>';
				voyageHtml += ' <th>启航时间(07:00)</th>';
				voyageHtml += ' <th>航程明细</th>';
				voyageHtml += ' <th>备注</th>';
				voyageHtml += '</tr>';
				
				for(var v = 0 ; v < tripList.length; v++){
					var tripObj = tripList[v];
					voyageHtml += '<tr id="voyageTr_'+ v +'">';
					voyageHtml += ' <td>'+ tripObj.time;
					voyageHtml += '  <input id="timeForm_'+ v +'" type="hidden" value="'+ tripObj.time +'">';
					voyageHtml += '</td>';
					var portNameCn = tripObj.port=='sail'?'海上巡游':tripObj.portNameCn;
					voyageHtml += ' <td>'+ portNameCn;
					voyageHtml += '  <input id="portForm_'+ v +'" type="hidden" value="'+ tripObj.port +'">';
					voyageHtml += ' </td>';
					voyageHtml += ' <td>';
					voyageHtml += '	 <input id="arrivalTimeForm_'+ v +'" value="'+ tripObj.arrivalTime +'" type="text" style="width:100px;"/>';
					voyageHtml += '	</td>';
					voyageHtml += ' <td>';
					voyageHtml += '	 <input id="leaveTimeForm_'+ v +'" value="'+ tripObj.leaveTime +'" type="text" style="width:100px;"/>';
					voyageHtml += '	</td>';
					voyageHtml += ' <td>';
					voyageHtml += '	 <input id="detailForm_'+ v +'" value="'+ tripObj.detail +'" type="text" style="width:200px;"/>';
					voyageHtml += '	</td>';
					voyageHtml += ' <td>';
					voyageHtml += '	 <input id="remarkForm_'+ v +'" value="'+ tripObj.remark +'" type="text" style="width:100px;"/>';
					voyageHtml += '	</td>';
					voyageHtml += '</tr>';
				}
				$("#voyageTable").html(voyageHtml);
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editShipmentButton() {
	var shipments = $("#shipmentManagerTable").datagrid("getChecked");
	if (shipments.length != 1) {
		top.$.messager.alert("提示", "请先选中单个对象!", "info");
		return false;
	}

	var row = shipments[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editShipmentButton',
		title : '编辑【'+ row.mark +'】【'+ row.travelDate.substring(0, 10) +'】',
		href : '../jsp_base/editShipment.html',
		fit : false,
		width : 450,
		height : 450,
		buttons : [ {
			text : '确定',
			handler : function() {
				editShipmentSubmit(row.numberCode);
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_editShipmentButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			xyzTextbox("startPlaceForm");
			
			$('#finalSaleDateForm').datetimebox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).datetimebox('clear');
					}
				}],
				onHidePanel: function(){
					var finalSale = new Date($(this).datetimebox('getValue'));
					
			    	var travel = new Date($('#travelDateForm').datebox("getValue"));
			    	
			    	if(travel.getTime() < finalSale.getTime()){
			    		top.$.messager.alert("提示", "最后售卖日期不能晚于出发日期!", "info");
			    		var tempDate = new Date(travel.getTime()-3*24*60*60*1000);
			    		var tempYear = tempDate.getFullYear();
			    		var tempMonth = tempDate.getMonth()+1;
			    		var tempDay = tempDate.getDate();
			    		var tempFinalSale = tempYear+"-"+tempMonth+"-"+tempDay+" 00:00:00";
			    		$(this).datetimebox('setValue', tempFinalSale);
			    		return false;
			    	}
				}
			});
			
			$("#cruiseForm").html(row.cruiseNameCn);
			$("#areaForm").html(row.areaNameCn);
			$("#travelDateForm").html(row.travelDate.substr(0,10));
			$("#travelEndDateForm").html(row.travelEndDate.substr(0,10));
			
			$('#finalSaleDateForm').datetimebox("setValue", row.finalSaleDate);
			$("#startPlaceForm").textbox("setValue",row.startPlace);
			$("#remarkForm").val(row.remark);
		}
	});
}

function editShipmentSubmit(numberCode) {
	if(!$("form").form('validate')){
		return false;
	}
	
	var finalSaleDate = $("#finalSaleDateForm").datetimebox("getValue");
	var startPlace = $("#startPlaceForm").textbox("getValue");
	if(xyzIsNull(startPlace)){
		startPlace = "任意城市";
	}
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../ShipmentWS/editShipment.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			finalSaleDate : finalSaleDate,
			startPlace : startPlace,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#shipmentManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_editShipmentButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function getTkiewAndStocCount(shipments){
	var str = "您确认要删除选中的记录吗？";
	
	$.ajax({
		url : "../ShipmentWS/getTkiewAndStocCount.do",
		type : "POST",
		data : {
			shipments : shipments
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status == 1){
				var map = data.content;
				var tkview = map.tkview;
				var stock = map.stock;
				if(tkview > 0){
					str = "您确认要删除选中的记录以及相关联的单品吗？";
				}else if(stock > 0){
					str = "您确认要删除选中的记录以及相关联的单品和单品库存吗？";
				}
				
			}else{
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus,errorThrown);
		}
	});
	
	return str;
}

function deleteShipmentButton() {
	var numberCodes = $.map($("#shipmentManagerTable").datagrid("getChecked"),
		function(p) {
			return p.numberCode;
		}
	).join(",");
	
	if (xyzIsNull(numberCodes)) {
		top.$.messager.alert("提示", "请先选中需要删除的对象!", "info");
		return;
	}
	
	var str = getTkiewAndStocCount(numberCodes);
	
	$.messager.confirm('确认', str,function(r) {
		 if(r){
			$.ajax({
				url : "../ShipmentWS/deleteShipment.do",
				type : "POST",
				data : {
					numberCodes : numberCodes
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.status == 1) {
						$("#shipmentManagerTable").datagrid("reload");
						top.$.messager.alert("提示", "操作成功!", "info");
						window.location.reload();
					} else {
						top.$.messager.alert("警告", data.msg, "warning");
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					top.window.AjaxError(XMLHttpRequest, textStatus,errorThrown);
				}
			});
		}
	});
}

function quickCreateTkviewButton(){
	var shipments = $("#shipmentManagerTable").datagrid("getChecked");
	if (shipments.length != 1) {
		top.$.messager.alert("提示", "请先选中单个对象!", "info");
		return false;
	}
	
	var row = shipments[0];
	
	$.ajax({
		url : "../ShipmentWS/quickCreateTkviewOper.do",
		type : "POST",
		data : {
			numberCode : row.numberCode
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#shipmentManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_cloneShipmentButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function voyageManager(numberCode,tripDays,travelDate,travelEndDate,airwayNameCn){
	xyzdialog({
		dialog : 'dialogFormDiv_voyageManager',
		title : "行程路线管理【出发日期:"+travelDate.substring(0,10)+"】【结束日期:"+travelEndDate.substring(0,10)+"】",
	    content : "<iframe id='dialogFormDiv_voyageManagerIframe' frameborder='0'></iframe>",
	    buttons:[{
			text:'返回',
			handler:function(){
				$("#shipmentManagerTable").datagrid("reload");
				$("#dialogFormDiv_voyageManager").dialog("destroy");
			}
		}]
	});
	var tempWidth = $("#dialogFormDiv_voyageManager").css("width");
	var tempHeight = $("#dialogFormDiv_voyageManager").css("height");
	var tempWidth2 = parseInt(tempWidth.split("px")[0]);
	var tempHeight2 = parseInt(tempHeight.split("px")[0]);
	$("#dialogFormDiv_voyageManagerIframe").css("width",(tempWidth2)+"px");
	$("#dialogFormDiv_voyageManagerIframe").css("height",(tempHeight2)+"px");
	$("#dialogFormDiv_voyageManagerIframe").attr("src","../jsp_base/voyageManager.html?shipment="+numberCode+"&travelDate="+travelDate+"&tripDays="+tripDays+"&airwayNameCn="+airwayNameCn);
}

function addDetail(numberCode,index) {
	$('#shipmentManagerTable').datagrid('selectRow',index);    
	var shimpentData = $('#shipmentManagerTable').datagrid('getSelected');    
	
	var htmlContent = "<div>";
	htmlContent += "<textarea name='ckeditor'  class='value' ></textarea>";
	htmlContent += "</div>";
	xyzdialog({
		dialog : 'dialogFormDiv_addDetail',
		title : '航期详情',
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
			$("textarea[class='value']").val(shimpentData.detail);
		}
	});
}

function addDetailSubmit(numberCode) {
	
	var value = $("textarea[class='value']").val();
	
	$.ajax({
		url : '../ShipmentWS/addDetail.do',
		type : "POST",
		data : {
			shipment : numberCode,
			detail : value
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#shipmentManagerTable").datagrid("reload");
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
	
	$('#shipmentManagerTable').datagrid('selectRow',index);    
	var shimpentData = $('#shipmentManagerTable').datagrid('getSelected');    
	
	var htmlContent = "<div>";
	htmlContent += "<textarea name='ckeditor'  class='value'></textarea>";
	htmlContent += "</div>";
	xyzdialog({
		dialog : 'dialogFormDiv_checkDetail',
		title : '航期详情',
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
			$("textarea[class='value']").val(shimpentData.detail);
		}
	});
	
}

function setAirway(numberCode,airway,airwayNameCn){
	
	airwayNameCn = airwayNameCn.length>35?xyzGetDiv(airwayNameCn,0,35)+"...":xyzGetDiv(airwayNameCn,0,35);
	
	var html = "<center><br/><form>";
	html += " <table cellpadding='4' cellspacing='4'>";
	html += " <tr>";
	html += "  <td></td>";
	html += "  <td>"+ airwayNameCn +"</td>";
	html += " </tr>";
	html += " <tr>";
	html += "  <td>更换航线 </td>";
	html += "  <td>";
	html += "   <input id='airwayForm' type='text' style='width:360px;'/>";
	html += "  </td>";
	html += " </tr>";
	html += "</table>";
	html += "</form></center>";
	
	xyzdialog({
		dialog : 'dialogFormDiv_setAirway',
		title : '设置航线',
		content : html,
		fit : false,
		width : 480,
		height : 180,
		buttons : [{
			text : '确定',
			handler : function() {
				setAirwaySubmit(numberCode);
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_setAirway").dialog("destroy");
			}
		}],
		onOpen:function(){
			
			xyzCombobox({
				required : true,
				combobox : 'airwayForm',
				url : '../ListWS/getAirwayList.do',
				mode: 'remote',
				lazy : false
			});
			
			$("#airwayForm").combobox('select',airway);
		}
	});
}

function setAirwaySubmit(numberCode){
	
	var airway = $("#airwayForm").combobox('getValue');
	
	$.ajax({
		url : "../ShipmentWS/setShipmentAirway.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			airway : airway
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#shipmentManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_setAirway").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function cloneShipmentButton(){
	var shipments = $("#shipmentManagerTable").datagrid("getChecked");
	if (shipments.length != 1) {
		top.$.messager.alert("提示", "请先选中单个对象!", "info");
		return false;
	}
	var row = shipments[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_cloneShipmentButton',
		title : '克隆【'+ row.cruiseNameCn +'】【'+ row.mark +'】【行程天数:'+ row.tripDays +'】',
		href : '../jsp_base/cloneShipment.html',
		fit : false,
		width : 450,
		height : 450,
		buttons : [ {
			text : '确定',
			handler : function() {
				cloneShipmentSubmit(row.numberCode);
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_cloneShipmentButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			xyzTextbox("startPlaceForm");
			
			$('#travelDateForm').datebox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).datebox('clear');
						var mark = $("#markInput").val();
						$("#markForm").html(mark);
					}
				}],
			    onHidePanel: function(){
			    	var trave = $(this).datebox('getValue');
			    	var travelDate = new Date(trave);
			    	
			    	var nowDate = new Date();
			    	if(nowDate.getTime() >= travelDate.getTime()){
			    		top.$.messager.alert("提示", "出发日期不能小于等于当前日期!", "info");
			    		return false;
			    	}
			    	
			    	//航期代码
			    	var subTrave = trave.substring(2);
					var mark = $("#markInput").val() + subTrave.replace(/-/g,"");
					$("#markForm").html(mark);
			    	
			    	//默认最后售卖日期
			    	var tempDate = new Date(travelDate.getTime()-3*24*60*60*1000);
		    		var tempYear = tempDate.getFullYear();
		    		var tempMonth = tempDate.getMonth()+1;
		    		var tempDay = tempDate.getDate();
		    		var tempFinalSale = tempYear+"-"+tempMonth+"-"+tempDay+" 00:00:00";
		    		$("#finalSaleDateForm").datetimebox('setValue', tempFinalSale);
			    }
			});
			
			$('#finalSaleDateForm').datetimebox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).datetimebox('clear');
					}
				}],
				onHidePanel: function(){
					var finalSale = $(this).datetimebox('getValue').substring(0,10);
					var travel = $('#travelDateForm').datebox("getValue");
					
			    	var finalSaleDate = new Date(finalSale);
			    	var travelDate = new Date(travel);
			    	if(finalSaleDate.getTime() >= travelDate.getTime()){
			    		top.$.messager.alert("提示", "最后售卖日期不能晚于出发日期!", "info");
			    		
			    		var value = new Date(travelDate.getTime()-3*24*60*1000);
			    		var year = value.getFullYear();
				    	var month = value.getMonth()+1;
				    	var day = value.getDate();
				    	var tempFale = year+"-"+month+"-"+day+" 00:00:00";
			    		$(this).datetimebox("setValue",tempFale);
			    		return false;
			    	}
				}
			});
			
			xyzTextbox("startPlaceForm");
			$("#cruiseForm").html(row.cruiseNameCn);
			$("#areaForm").html(row.areaNameCn);
			$("#airwayForm").html(row.airwayNameCn);
			var mark = row.mark;
			$("#markInput").val(mark.substr(0,mark.length-6));
			$("#markForm").html(mark.substr(0,mark.length-6));
			$("#startPlaceForm").textbox("setValue", row.startPlace);
			$("#remarkForm").val(row.remark);
		}
	});
}

function cloneShipmentSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var travelDate = $('#travelDateForm').datebox("getValue");
	var mark = $("#markForm").html();
	var finalSaleDates = $('#finalSaleDateForm').datetimebox("getValue");
	var startPlace = $("#startPlaceForm").textbox("getValue");
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../ShipmentWS/cloneShipmentByNumberCodeOper.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			travelDate : travelDate,
			mark : mark,
			finalSaleDate : finalSaleDates,
			startPlace : startPlace,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#shipmentManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_cloneShipmentButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}