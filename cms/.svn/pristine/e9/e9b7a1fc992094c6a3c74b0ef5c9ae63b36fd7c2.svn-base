$(document).ready(function() {
	
	$('#startDate').datebox('setValue',new Date().Format("yyyy-MM-dd"));
	startDate = $('#startDate').datebox("getValue");
	xyzTextbox("nameCn");
	
	$("#ptviewQueryButton").click(function(){
		loadTable();
	});
	
	$("#cruiseSortDiv").hide();
	if(xyzControlButton("buttonCode_x20170525170709")){
		$("#cruiseSortDiv").show();
		$("#cruiseSortButton").click(function(){
			cruiseSortButton();
		});
	}
	
	initTable();
	initTabs();
	
});
var cruiseNumber = '';
function initTable(){
	
	var toolbar = [];
	if (xyzControlButton("buttonCode_x20170525170703")) {
		toolbar[toolbar.length]={
			text: '新增',
			iconCls: 'icon-add',
			handler: function(){
				addPtviewButton();
			}
		};
	}
	if (xyzControlButton("buttonCode_x20170525170707")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length]={
			text: '克隆',
			iconCls: 'icon-add',
			handler: function(){
				clonePtviewButton();
			}
		}; 			
	}
	if (xyzControlButton("buttonCode_x20170525170708")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length]={
			text: '同步',
			iconCls: 'icon-add',
			handler: function(){
				syncPtviewButton();
			}
		}; 			
	}
	if (xyzControlButton("buttonCode_x20170525170704")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length]={
			text: '编辑',
			iconCls: 'icon-edit',
			handler: function(){
				editPtviewButton();
			}
		}; 			
	}
	if (xyzControlButton("buttonCode_x20170525170705")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length]={
			text: '删除',
			iconCls: 'icon-remove',
			handler: function(){
				deletePtviewButton();
			}
		}; 			
	}
	
	var title = '<div style="height:60px;width:auto;">';
	title += '<div style="float:left;">产品信息列表</div>';
	title += '<div id="ptviewCruiseTabs" style="width:auto;height:38px;float:left;">';
	title += '</div>';
	title += '</div>';
	
	xyzgrid({
		table : 'ptviewManagerTable',
		title : title,
		toolbar : toolbar,
		url : '../PtviewWS/queryPtviewListByCruise.do',
		idField : 'numberCode',
		pageSize : 10,
		height : 540,
		singleSelect : false, 
		columns:[[
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'编号',hidden:true,halign:'center'},
			{field:'nameCn',title:'名称',halign:'center',width:160,
				formatter:function(value, row, index){
					return xyzGetDiv(value, 0, 40);
				}
			},
			{field:'basePrice',title:'基础价格',halign:'center',
				formatter : function(value, row, index) {
					return xyzGetPrice(value);
				}
			},
			{field:'company',title:'邮轮公司编号',hidden:true,halign:'center',width:120},
			{field:'companyNameCn',title:'邮轮公司名称',halign:'center',width:120,
				formatter:function(value, row, index){
					return xyzGetDiv(value, 0, 20);
				}
			},
			{field:'cruise',title:'邮轮编号',hidden:true,halign:'center',width:80,},
			{field:'cruiseNameCn',title:'邮轮名称',halign:'center',width:80,
				formatter:function(value, row, index){
					return xyzGetDiv(value, 0, 20);
				}
			},
			{field:'travelDate',title:'航期',halign:'center',width:80,
				formatter : function(value, row, index) {
					return value.substring(0, 10);
				}	
			},
			{field:'airway',title:'航线编号',halign:'center',hidden:true,width:80},
			{field:'airwayNameCn',title:'航线',halign:'center',width:80,
				formatter:function(value, row, index){
					return xyzGetDiv(value, 0, 20);
				}
			},
			{field:'days',title:'旅游天数',halign:'center',halign:'center',
				formatter:function(value, row, index){
					return value +"天"+ (value-1) +"晚";
				}
			},
			{field:'fromPlace',title:'出发地',align:'center',width:60,
				formatter:function(value, row, index){
					return xyzGetDiv(value, 0, 20);
				}
			},
			{field:'shipMark',title:'航期代码',halign:'center',width:80,
				formatter:function(value, row, index){
					return xyzGetDiv(value, 0, 20);
				}
			},
			{field:'dateMark',title:'日期短码',halign:'center',width:80,
				formatter:function(value, row, index){
					return xyzGetDiv(value, 0, 20);
				}
			},
			{field:'isOpen',title:'是否开启',align:'center',
				formatter:function(value, row, index){
					return '<input style="width:42px;height:18px;" class="switchbutton isEnableSwitchbutton" data-options="checked:'+(value=='开'?'true':'false')+'" data-infoTableCode="'+row.numberCode+'"/>';
				}
			},
			{field:'image',title:'主图',align:'center',width:80,
				formatter:function(value, row, index){
					 var content = xyzIsNull(value)?"上传图片":"<img src='"+value+"' width='60' height='30'/>";
					 var html = "";
	        		  if(xyzControlButton("buttonCode_y20171103174002")){
	        			  html = xyzGetA(content,"upLoadImage",[row.numberCode,row.nameCn,row.image],"点我查看和上传产品主图片!","blue");
	        		  }else {
	        			  html = xyzGetA(content,"checkImage",[row.nameCn,row.image],"点我查看产品主图片!","blue");
	        		  }
	        		  return html;
				}
			},
			{field:'detailImage',title:'详情图',align:'center',width:50,
				formatter:function(value, row, index){
					var html = "";
					var count ="";
					if(!xyzIsNull(value)){
						count = value.split(",").length;
					}else{
						count = 0;
					}
					if(xyzControlButton("buttonCode_y20171103174003")){
						html = xyzGetA("图片("+count+")","upLoadDetailImages",[row.numberCode,row.nameCn,row.detailImage],"点我查看和上传产品详情图片!","blue");
					}else {
						html = xyzGetA("图片("+count+")","checkDetailImages",[row.nameCn,row.detailImage],"点我查看产品详情图片!","blue");
					}
					return html;
				}
			},
			/*{field:'shipUp',title:'上船地点',halign:'center'},
			{field:'shipDown',title:'下船地点',halign:'center'},*/
			{field:'skuOper',title:'SKU管理',align:'center',width:80,
				formatter : function(value, row, index) {
					var html = "";
	        		if(xyzControlButton("buttonCode_x20170602103801")){
	        			html = xyzGetA("管理SKU","skuManager",[row.numberCode, row.cruise, row.nameCn, row.shipment],"管理SKU","blue");
	        		}
	        	    return html;
				}
			},
			{field:'addDate',title:'添加时间',hidden:true,halign:'center',width:80,
				formatter:function(value, row, index){
					return xyzGetDivDate(value);
				}
			},
			{field:'alterDate',title:'修改时间',halign:'center',width:80,
				formatter:function(value, row, index){
					return xyzGetDivDate(value);
				}
			},
			{field:'remark',title:'备注',halign:'center',width:80,
				formatter:function(value, row, index){
					return xyzGetDiv(value, 0, 50);
				}
			}
		]],
		onLoadSuccess : function(data){
			$('.isEnableSwitchbutton').switchbutton({ 
			      onText : '开',
			      offText : '关',
			      onChange: function(checked){ 
			    	  editIsOpen($(this).attr('data-infoTableCode'));
			      } 
			});
		}
	});
}
function editIsOpen(numberCode){
	xyzAjax({
		url : "../PtviewWS/editIsOpen.do",
		data : {
			numberCode : numberCode
		},
		success : function(data) {
			if(data.status !=1){
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
}

function initTabs(){
	xyzAjax({
		url : "../PtviewWS/queryCruiseList.do",
		type : "POST",
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var cruiseList = data.content;
				if(cruiseList.length == 0){
					return false;
				}
				$("#ptviewCruiseTabs").tabs({
					onSelect : function(title,index){
						loadTable();
				    }
				});
				for(var t = 0; t < cruiseList.length; t++){
					var cruiseObj = cruiseList[t];
					var cruise = cruiseObj.numberCode;
					var cruiseNameCn = cruiseObj.nameCn;
					var company = cruiseObj.company;
					addTabs(t, cruiseNameCn, cruise, company);
				}
			}
			$('#ptviewCruiseTabs').tabs({
				select : 0
			});
		}
	});
}

function addTabs(index, title, cruise, company){
	
	$("#ptviewCruiseTabs").tabs('add',{
	    title : title,    
	    id : cruise,
	    content : '',
	    closable : false
	});
	
	loadTable(cruise);
}

function loadTable(){
	var tab = $('#ptviewCruiseTabs').tabs('getSelected');
	var cruise = tab.attr("id"); 

	var nameCn = $("#nameCn").val();
	var startDate = $("#startDate").datebox("getValue");
	var endDate = $("#endDate").datebox("getValue");
	
	$("#ptviewManagerTable").datagrid('load',{
		cruise : cruise,
		nameCn : nameCn,
		startDate : startDate,
		endDate : endDate,
	});
	
}

function addPtviewButton(){
	
	xyzdialog({
		dialog : 'dialogFormDiv_addPtviewButton',
		title : '新增',
	    href : '../jsp_ptview/addPtview.html',
	    fit : false,
	    width : 600,
		height : 500,
	    buttons:[{
			text:'确定',
			handler:function(){
				addPtviewSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addPtviewButton").dialog("destroy");
			}
		}],onLoad : function(){
			xyzCombobox({
				required : true,
				combobox : 'cruiseForm',
				url : '../ListWS/getCruiseList.do',
				lazy : false, 
				mode : 'remote',
				onSelect : function(){
					addShipmentHtml();
				},
			});
		}
	});
	
}

function addPtviewSubmit(){
	if(!$("form").form('validate')){
		return false;
	}

	var cruise = $("#cruiseForm").combobox('getValue');
    var shipmentJson = [];
    $("#shipTbody input[id^='ship_']").each(function(){
    	if($(this).attr("checked") == "checked"){
    		var shipment =$(this).attr("shimpmentNumberCode"); 
    		shipmentJson += shipment+",";
    	}
    });
    if (shipmentJson.length == 0) {
    	top.$.messager.alert("提示","请至少选择一个航期！","info");
    	return false;
	}
	$.ajax({
		url : "../PtviewWS/addPtview.do",
		type : "POST",
		data : {
			cruise : cruise, 
			shipmentJson :shipmentJson
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status == 1){
				$("#dialogFormDiv_addPtviewButton").dialog("destroy");
				$("#ptviewManagerTable").datagrid("reload");
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
function addShipmentHtml() {
	var cruise = $("#cruiseForm").combobox("getValue");
	$.ajax({
		url : "../StockWS/getShipmentByCruise.do",
		type : "POST",
		data : {
			cruise : cruise
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var shimpmentList = data.content;
				var shipNum = 0;
				var shipHtml = '';
				for(var k = 0; k < shimpmentList.length; k++){
					var shipObj = shimpmentList[k];
					var numberCode = shimpmentList[k].numberCode;
					if(k % 4 == 0){
						shipNum = shipNum + 3;
						shipHtml += '<tr style=" display:block;margin:20px 0;">';
					}
					shipHtml += '<td width="120px">';
					shipHtml += ' <input id="ship_'+ shipObj.numberCode +'" type="checkbox" checked="checked" shimpmentNumberCode="'+ numberCode +'" ">';
					shipHtml += ' <label for="ship_'+ shipObj.numberCode +'">';
					shipHtml += xyzGetDiv(shipObj.travelDate, 2, 8)+'</label>';
					shipHtml += '</td>';
					if(k == shipNum){
						shipHtml += '</tr>';
					}
					if(k == (shimpmentList.length-1) && k < shipNum){
						for(var h = 0; h < (shipNum-k-1); h++){
							shipHtml += '<td></td>';
						}
						shipHtml += '</tr>';
					}
				}
				$("#shipTbody").html(shipHtml);
				$("#checkShipmentAll").click(function(){
					 var checkShipmentAll = document.getElementById('checkShipmentAll');
					  if(checkShipmentAll.checked){
						  $("#shipTbody input[id^='ship_']").each(function(){
								$(this).attr("checked",true);
						    });
					  }else{
						  $("#shipTbody input[id^='ship_']").each(function(){
								$(this).attr("checked",false);
						  });
					  }
				});
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editPtviewButton(){
	var ptviews = $("#ptviewManagerTable").datagrid("getChecked");
	if (ptviews.length != 1) {
		top.$.messager.alert("提示", "请先选中单个对象!", "info");
		return false;
	}
	var row = ptviews[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editPtviewButton',
		title : '编辑',
	    href : '../jsp_ptview/editPkview.html',
	    fit : false,
	    width : 600,
		height : 500,
	    buttons:[{
			text:'确定',
			handler:function(){
				editPtviewSubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editPtviewButton").dialog("destroy");
			}
		}],onLoad : function(){
			
			xyzTextbox("nameCnForm");
			$("#nameCnForm").textbox("setValue",row.nameCn);
			$("#nameCnForm").textbox({
				required : true
			});
		}
	});
}

function editPtviewSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").textbox("getValue");

	$.ajax({
		url : "../PtviewWS/editPtview.do",
		type : "POST",
		data : {
			numberCode : numberCode, 
			nameCn : nameCn
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status == 1){
				$("#dialogFormDiv_editPtviewButton").dialog("destroy");
				top.$.messager.alert("提示","操作成功!","info");
				$("#ptviewManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deletePtviewButton(){
	var numberCodes = $.map($("#ptviewManagerTable").datagrid("getChecked"),
		function(p){
			return p.numberCode;
		}
	).join(",");
	if (numberCodes.length < 1) {
		top.$.messager.alert("提示", "请选择要删除的数据!", "info");
		return false;
	}
	
	$.messager.confirm('确认','您确认想要删除产品吗？',function(r){    
		if(r){
			$.ajax({
				url : '../PtviewWS/deletePtview.do',
				type : "POST",
				data : {
					numberCodes : numberCodes
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.status == 1) {
						top.$.messager.alert("提示", "操作成功!", "info");
						$("#ptviewManagerTable").datagrid("reload");
					} else {
						top.$.messager.alert("警告", data.msg, "warning");
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
				}
			});
		}
	});
}

function clonePtviewButton(){
	var rows = $("#ptviewManagerTable").datagrid("getChecked");
	if (rows.length != 1) {
		top.$.messager.alert("提示", "请先选中单个对象!", "info");
		return false;
	}
	var row = rows[0];
	
	var numberCode = row.numberCode;
	var html = '<form><center><br/>';
	html += '<input type="hidden" id="numberCode" value="'+ numberCode +'"/>';
	html += '<table id="skuTable" border="1" cellpadding="2" cellspacing="0" style="text-align:center;width:850px;">';
	html += '</table>';
	
	xyzdialog({
		dialog:'dialogFormDiv_clonePtviewButton',
		title : "产品克隆",
		content : html,
		buttons:[{
			text:'确定',
			handler:function(){
				clonePtviewSubmit(numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_clonePtviewButton").dialog("destroy");
			}
		}],
		onOpen : function() {
			xyzAjax({
				url:'../PtviewWS/getPtviewSkuByNumberCode.do',
				data : {
					numberCode : numberCode
				},
				success:function(data){
					if (data.status == 1) {
						var skuList = data.content;
						var skuHtml = '';
						skuHtml += '<caption>';
						skuHtml += ' 克隆 <span style="font-size:16px;font-weight:bold;">【'+row.nameCn+'】</span>到<br/><br/>';
						skuHtml += ' 产品<input type="text" id="ptviewForm" style="width:460px;"/><br/><br/>';
						skuHtml += '</caption>';
						skuHtml += '<thead>';
						skuHtml += ' <tr>';
						skuHtml += '  <th style="width:240px;font-size:14px;">SKU</th>';
						skuHtml += '  <th colspan="5" style="font-size:14px;height:18px;">';
						skuHtml += '    SKU库存<span style="color:red;">(温馨提示:<img src="../image/other/unlock.png" style="width:16px;height:16px;"/>克隆,';
						skuHtml += '    <img src="../image/other/lock.png" style="width:16px;height:16px;"/>不克隆)</span>';
						skuHtml += '  </th>';
						skuHtml += ' </tr>';
						skuHtml += '</thead>';
						skuHtml += '<tbody>';
						for(var k = 0; k < skuList.length; k++){
							var skuObj = skuList[k];
							var skuNumberCode = skuObj.numberCode;
							var nameCn = skuObj.nameCn;
							var skuStockList = skuObj.stockList;
							
							skuHtml += ' <tr id="skuTr_'+skuNumberCode+'">';
							skuHtml += '  <th rowspan="'+ (skuStockList.length+1) +'">';
							skuHtml += '   <input type="hidden" id="skuNumberCode_'+skuNumberCode+'" value="'+ skuNumberCode +'"/>';
							skuHtml += '   <span style="width:240px;">'+ nameCn +'</span>';
							skuHtml += '  </th>';
							skuHtml += '  <th style="width:180px;">时间</th>';
							skuHtml += '  <th style="width:130px;">库存类型</th>';
							skuHtml += '  <th style="width:100px;">库存</th>';
							skuHtml += '  <th style="width:100px;">价格</th>';
							skuHtml += '  <th style="text-align:center;" >';
							if(skuStockList.length+1 > 1){
								skuHtml += '   操作<br/>';
								skuHtml += '   <img src="../image/other/unlock.png" title="点我不克隆产品SKU套餐及库存" ';
								skuHtml += '    id="skuImg_'+skuNumberCode+'" ';
								skuHtml += '    onclick="cloneImg(\''+skuNumberCode+'\')" >';
								skuHtml += '    <input id="isPass_'+skuNumberCode+'" type="hidden" value="0"/>&nbsp;';
							}
							skuHtml += '  </th>';
							skuHtml += ' </tr>';
							for(var m = 0; m < skuStockList.length; m++){
								var stockObj = skuStockList[m];
								var number = stockObj.numberCode;
								var date = stockObj.date;
								var priceType = stockObj.priceType;
								var type = "";
								if(priceType == 1){
									type = "成人价";
								}else if(priceType == 2){ 
									type = "儿童价";
								}else{//priceType == 3
									type = "单房差";
								}
								var price = stockObj.price;
								var stock = stockObj.stock;
								skuHtml += '<tr id="stockTr_'+skuNumberCode+'_'+number+'">';
								skuHtml += ' <td style="width:180px;">';
								skuHtml += '  <input type="hidden" id="numberCode_'+skuNumberCode+'_'+number+'" value="'+ number +'"/>';
								skuHtml += '  <span style="width:180px;">'+ xyzGetDiv(date ,0 ,10) +'</span>';
								skuHtml += ' </td>';
								skuHtml += ' <td style="width:130px;">';
								skuHtml += '  <span style="width:130px;">'+ type +'</span>';
								skuHtml += ' </td>';
								skuHtml += ' <td style="width:100px;">';
								skuHtml += '  <span style="width:100px;">'+ stock +'</span>';
								skuHtml += ' </td>';
								skuHtml += ' <td style="width:100px;">';
								skuHtml += '  <span style="width:100px;">'+ xyzGetPrice(price) +'</span>';
								skuHtml += ' </td>';
								skuHtml += ' <td style="width:100px;">';
								skuHtml += '  <img src="../image/other/unlock.png" title="点我不克隆该产品SKU套餐及库存" ';
								skuHtml += '   id="skuImg_'+skuNumberCode+'_'+number+'" ';
								skuHtml += '   onclick="cloneOneImg(\''+skuNumberCode+'\',\''+number+'\')" />';
								skuHtml += '  <input id="isPass_'+skuNumberCode+'_'+number+'" type="hidden" value="0" />&nbsp;';
								skuHtml += '</td>';
								skuHtml += '</tr>';
							}
						}
						skuHtml += '</tbody>';
						$("#skuTable").html(skuHtml);
						xyzCombobox({
							required : true,
							combobox : 'ptviewForm',
							url : '../ListWS/getPtviewAllList.do',
							mode: 'remote',
							lazy : false,
							onBeforeLoad: function(param){
								param.ptview = numberCode;
						   	}
						});
						
					} else {
						top.$.messager.alert("警告", data.msg, "warning");
					}
				}
			});
		}
	});
}

function cloneImg(numberCode){
	var isPass = $("#isPass_"+numberCode).val();
	var stockList = $.map($("tr[id^='stockTr_"+numberCode+"_']"),
		function(p){
			return $(p).attr("id").split("_")[2];
		}
	);
	if(isPass == '0'){
		$("#skuImg_"+numberCode).attr("src","../image/other/lock.png");
		$("#skuImg_"+numberCode).attr("title","点我克隆产品SKU套餐及库存");
		isPass = '1';
		$("#isPass_"+numberCode).val(isPass);
		
		for(var j = 0; j < stockList.length; j++){
			$("#skuImg_"+numberCode+"_"+stockList[j]).attr("src","../image/other/lock.png");
			$("#skuImg_"+numberCode+"_"+stockList[j]).attr("title","点我克隆该产品SKU套餐及库存");
			$("#isPass_"+numberCode+"_"+stockList[j]).val(isPass);
		}
	}else{
		$("#skuImg_"+numberCode).attr("src","../image/other/unlock.png");
		$("#skuImg_"+numberCode).attr("title","点我不克隆产品SKU套餐及库存");
		isPass = '0';
		$("#isPass_"+numberCode).val(isPass);
		
		for(var j = 0; j < stockList.length; j++){
			$("#skuImg_"+numberCode+"_"+stockList[j]).attr("src","../image/other/unlock.png");
			$("#skuImg_"+numberCode+"_"+stockList[j]).attr("title","点我不克隆该产品SKU套餐及库存");
			$("#isPass_"+numberCode+"_"+stockList[j]).val(isPass);
		}
	}
	
}

function cloneOneImg(skuNumberCode, stockNumberCode){
	var isPass = $("#isPass_"+skuNumberCode+"_"+stockNumberCode).val();
	if(isPass == '0'){
		$("#skuImg_"+skuNumberCode+"_"+stockNumberCode).attr("src","../image/other/lock.png");
		$("#skuImg_"+skuNumberCode+"_"+stockNumberCode).attr("title","点我不克隆该产品SKU套餐及库存");
		isPass = '1';
		$("#isPass_"+skuNumberCode+"_"+stockNumberCode).val(isPass);
	}else{
		$("#skuImg_"+skuNumberCode+"_"+stockNumberCode).attr("src","../image/other/unlock.png");
		$("#skuImg_"+skuNumberCode+"_"+stockNumberCode).attr("title","点我克隆该产品SKU套餐及库存");
		isPass = '0';
		$("#isPass_"+skuNumberCode+"_"+stockNumberCode).val(isPass);
	}
	
	var tempCount = 0;
	var stockList = $.map($("tr[id^='skuImg_"+skuNumberCode+"_']"),
		function(p){
			return $(p).attr("id").split("_")[2];
		}
	);
	for(var k = 0; k < stockList.length; k++){
		var skuIsPass = $("#isPass_"+skuNumberCode+"_"+stockList[k]).val();
		if(skuIsPass == isPass){
			tempCount++;
		}
	}
	if(tempCount == stockList.length){
		if(isPass == '0'){
			$("#skuImg_"+skuNumberCode).attr("src","../image/other/unlock.png");
			$("#skuImg_"+skuNumberCode).attr("title","点我不克隆产品SKU套餐及库存");
			$("#isPass_"+skuNumberCode).val(isPass);
		}else{
			$("#skuImg_"+skuNumberCode).attr("src","../image/other/lock.png");
			$("#skuImg_"+skuNumberCode).attr("title","点我克隆产品SKU套餐及库存");
			$("#isPass_"+skuNumberCode).val(isPass);
		}
	}else{
		$("#skuImg_"+numberCode).attr("src","../image/other/unlock.png");
		$("#skuImg_"+numberCode).attr("title","点我不克隆产品SKU套餐及库存");
		$("#isPass_"+numberCode).val('0');
	}
}

function clonePtviewSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var ptview = $("#ptviewForm").combobox("getValue");
	var skuList = $.map($("tr[id^='skuTr_']"),
		function(p){
			return $(p).attr("id").split("_")[1];
		}
	);
	var skuJson = [];
	for(var s = 0; s < skuList.length; s++){
		var skuId = skuList[s];
		var skuIsPass = $("#isPass_"+skuId).val();
		
		if(skuIsPass == '0'){
			var skuNumber = $("#skuNumberCode_"+skuId).val();
			
			//SKU套餐库存
			var stockList = $.map($("tr[id^='stockTr_"+skuId+"_']"),
				function(p){
					return $(p).attr("id").split("_")[2];
				}
			);
			
			var stockJsonStr = "";
			for(var d = 0; d < stockList.length; d++){
				var skuDetailId = stockList[d];
				var isPass = $("#isPass_"+skuId+"_"+skuDetailId).val();
				if(isPass == '0'){//克隆
					var detailNumber = $("#numberCode_"+skuId+"_"+skuDetailId).val();
					stockJsonStr = stockJsonStr + detailNumber;
					if(d < (stockList.length-1)){
						stockJsonStr = stockJsonStr + ",";
					}
				}
			}
			
			var skuData = {
				skuNumber : skuNumber,
				stock : stockJsonStr
			};
			skuJson[skuJson.length] = skuData;
		}
	}
	
	$.ajax({
		url : "../PtviewWS/clonePtviewOper.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			ptview : ptview,
			skuJson : JSON.stringify(skuJson)
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#ptviewManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_clonePtviewButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function syncPtviewButton(){
	var rows = $("#ptviewManagerTable").datagrid("getChecked");
	if (rows.length != 1) {
		top.$.messager.alert("提示", "请先选中单个对象!", "info");
		return false;
	}
	var row = rows[0];
	
	var numberCode = row.numberCode;
	var html = '<form><center><br/>';
	html += '<input type="hidden" id="numberCode" value="'+ numberCode +'"/>';
	html += '<table id="skuTable" border="1" cellpadding="2" cellspacing="0" style="text-align:center;width:850px;">';
	html += '</table>';
	
	xyzdialog({
		dialog:'dialogFormDiv_syncPtviewButton',
		title : "产品同步",
		content : html,
		buttons:[{
			text:'确定',
			handler:function(){
				syncPtviewSubmit(numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_syncPtviewButton").dialog("destroy");
			}
		}],
		onOpen : function() {
			xyzAjax({
				url:'../PtviewWS/getPtviewSkuByNumberCode.do',
				data : {
					numberCode : numberCode
				},
				success:function(data){
					if (data.status == 1) {
						var skuList = data.content;
						var skuHtml = '';
						skuHtml += '<caption>';
						skuHtml += ' 同步 <span style="font-size:16px;font-weight:bold;">【'+row.nameCn+'】</span>到<br/><br/>';
						skuHtml += ' 产品<input type="text" id="ptviewForm" style="width:460px;"/><br/><br/>';
						skuHtml += '</caption>';
						skuHtml += '<thead>';
						skuHtml += ' <tr>';
						skuHtml += '  <th style="width:240px;font-size:14px;">SKU</th>';
						skuHtml += '  <th colspan="5" style="font-size:14px;height:18px;">';
						skuHtml += '    SKU库存<span style="color:red;">(温馨提示:<img src="../image/other/unlock.png" style="width:16px;height:16px;"/>同步,';
						skuHtml += '    <img src="../image/other/lock.png" style="width:16px;height:16px;"/>不同步)</span>';
						skuHtml += '  </th>';
						skuHtml += ' </tr>';
						skuHtml += '</thead>';
						skuHtml += '<tbody>';
						for(var k = 0; k < skuList.length; k++){
							var skuObj = skuList[k];
							var skuNumberCode = skuObj.numberCode;
							var nameCn = skuObj.nameCn;
							var skuStockList = skuObj.stockList;
							
							skuHtml += ' <tr id="skuTr_'+skuNumberCode+'">';
							skuHtml += '  <th rowspan="'+ (skuStockList.length+1) +'">';
							skuHtml += '   <input type="hidden" id="skuNumberCode_'+skuNumberCode+'" value="'+ skuNumberCode +'"/>';
							skuHtml += '   <span style="width:240px;">'+ nameCn +'</span>';
							skuHtml += '  </th>';
							skuHtml += '  <th style="width:180px;">时间</th>';
							skuHtml += '  <th style="width:130px;">库存类型</th>';
							skuHtml += '  <th style="width:100px;">库存</th>';
							skuHtml += '  <th style="width:100px;">价格</th>';
							skuHtml += '  <th style="text-align:center;" >';
							if(skuStockList.length+1 > 1){
								skuHtml += '   操作<br/>';
								skuHtml += '   <img src="../image/other/unlock.png" title="点我不同步产品SKU套餐及库存" ';
								skuHtml += '    id="skuImg_'+skuNumberCode+'" ';
								skuHtml += '    onclick="syncImg(\''+skuNumberCode+'\')" >';
								skuHtml += '    <input id="isPass_'+skuNumberCode+'" type="hidden" value="0"/>&nbsp;';
							}
							skuHtml += '  </th>';
							skuHtml += ' </tr>';
							for(var m = 0; m < skuStockList.length; m++){
								var stockObj = skuStockList[m];
								var number = stockObj.numberCode;
								var date = stockObj.date;
								var priceType = stockObj.priceType;
								var type = "";
								if(priceType == 1){
									type = "成人价";
								}else if(priceType == 2){ 
									type = "儿童价";
								}else{//priceType == 3
									type = "单房差";
								}
								var price = stockObj.price;
								var stock = stockObj.stock;
								skuHtml += '<tr id="stockTr_'+skuNumberCode+'_'+number+'">';
								skuHtml += ' <td style="width:180px;">';
								skuHtml += '  <input type="hidden" id="numberCode_'+skuNumberCode+'_'+number+'" value="'+ number +'"/>';
								skuHtml += '  <span style="width:180px;">'+ xyzGetDiv(date ,0 ,10) +'</span>';
								skuHtml += ' </td>';
								skuHtml += ' <td style="width:130px;">';
								skuHtml += '  <span style="width:130px;">'+ type +'</span>';
								skuHtml += ' </td>';
								skuHtml += ' <td style="width:100px;">';
								skuHtml += '  <span style="width:100px;">'+ stock +'</span>';
								skuHtml += ' </td>';
								skuHtml += ' <td style="width:100px;">';
								skuHtml += '  <span style="width:100px;">'+ xyzGetPrice(price) +'</span>';
								skuHtml += ' </td>';
								skuHtml += ' <td style="width:100px;">';
								skuHtml += '  <img src="../image/other/unlock.png" title="点我不同步该产品SKU套餐及库存" ';
								skuHtml += '   id="skuImg_'+skuNumberCode+'_'+number+'" ';
								skuHtml += '   onclick="syncOneImg(\''+skuNumberCode+'\',\''+number+'\')" />';
								skuHtml += '  <input id="isPass_'+skuNumberCode+'_'+number+'" type="hidden" value="0" />&nbsp;';
								skuHtml += '</td>';
								skuHtml += '</tr>';
							}
						}
						skuHtml += '</tbody>';
						$("#skuTable").html(skuHtml);
						xyzCombobox({
							required : true,
							combobox : 'ptviewForm',
							url : '../ListWS/getPtviewAllList.do',
							mode: 'remote',
							lazy : false,
							onBeforeLoad: function(param){
								param.ptview = numberCode;
						   	}
						});
						
					} else {
						top.$.messager.alert("警告", data.msg, "warning");
					}
				}
			});
		}
	});
}

function syncImg(numberCode){
	var isPass = $("#isPass_"+numberCode).val();
	var stockList = $.map($("tr[id^='stockTr_"+numberCode+"_']"),
		function(p){
			return $(p).attr("id").split("_")[2];
		}
	);
	if(isPass == '0'){
		$("#skuImg_"+numberCode).attr("src","../image/other/lock.png");
		$("#skuImg_"+numberCode).attr("title","点我同步产品SKU套餐及库存");
		isPass = '1';
		$("#isPass_"+numberCode).val(isPass);
		
		for(var j = 0; j < stockList.length; j++){
			$("#skuImg_"+numberCode+"_"+stockList[j]).attr("src","../image/other/lock.png");
			$("#skuImg_"+numberCode+"_"+stockList[j]).attr("title","点我同步该产品SKU套餐及库存");
			$("#isPass_"+numberCode+"_"+stockList[j]).val(isPass);
		}
	}else{
		$("#skuImg_"+numberCode).attr("src","../image/other/unlock.png");
		$("#skuImg_"+numberCode).attr("title","点我不同步产品SKU套餐及库存");
		isPass = '0';
		$("#isPass_"+numberCode).val(isPass);
		
		for(var j = 0; j < stockList.length; j++){
			$("#skuImg_"+numberCode+"_"+stockList[j]).attr("src","../image/other/unlock.png");
			$("#skuImg_"+numberCode+"_"+stockList[j]).attr("title","点我不同步该产品SKU套餐及库存");
			$("#isPass_"+numberCode+"_"+stockList[j]).val(isPass);
		}
	}
	
}

function syncOneImg(skuNumberCode, stockNumberCode){
	var isPass = $("#isPass_"+skuNumberCode+"_"+stockNumberCode).val();
	if(isPass == '0'){
		$("#skuImg_"+skuNumberCode+"_"+stockNumberCode).attr("src","../image/other/lock.png");
		$("#skuImg_"+skuNumberCode+"_"+stockNumberCode).attr("title","点我不同步该产品SKU套餐及库存");
		isPass = '1';
		$("#isPass_"+skuNumberCode+"_"+stockNumberCode).val(isPass);
	}else{
		$("#skuImg_"+skuNumberCode+"_"+stockNumberCode).attr("src","../image/other/unlock.png");
		$("#skuImg_"+skuNumberCode+"_"+stockNumberCode).attr("title","点我同步该产品SKU套餐及库存");
		isPass = '0';
		$("#isPass_"+skuNumberCode+"_"+stockNumberCode).val(isPass);
	}
	
	var tempCount = 0;
	var stockList = $.map($("tr[id^='skuImg_"+skuNumberCode+"_']"),
		function(p){
			return $(p).attr("id").split("_")[2];
		}
	);
	for(var k = 0; k < stockList.length; k++){
		var skuIsPass = $("#isPass_"+skuNumberCode+"_"+stockList[k]).val();
		if(skuIsPass == isPass){
			tempCount++;
		}
	}
	if(tempCount == stockList.length){
		if(isPass == '0'){
			$("#skuImg_"+skuNumberCode).attr("src","../image/other/unlock.png");
			$("#skuImg_"+skuNumberCode).attr("title","点我不同步产品SKU套餐及库存");
			$("#isPass_"+skuNumberCode).val(isPass);
		}else{
			$("#skuImg_"+skuNumberCode).attr("src","../image/other/lock.png");
			$("#skuImg_"+skuNumberCode).attr("title","点我同步产品SKU套餐及库存");
			$("#isPass_"+skuNumberCode).val(isPass);
		}
	}else{
		$("#skuImg_"+numberCode).attr("src","../image/other/unlock.png");
		$("#skuImg_"+numberCode).attr("title","点我不同步产品SKU套餐及库存");
		$("#isPass_"+numberCode).val('0');
	}
}

function syncPtviewSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var ptview = $("#ptviewForm").combobox("getValue");
	var skuList = $.map($("tr[id^='skuTr_']"),
		function(p){
			return $(p).attr("id").split("_")[1];
		}
	);
	var skuJson = [];
	for(var s = 0; s < skuList.length; s++){
		var skuId = skuList[s];
		var skuIsPass = $("#isPass_"+skuId).val();
		
		if(skuIsPass == '0'){
			var skuNumber = $("#skuNumberCode_"+skuId).val();
			
			//SKU套餐库存
			var stockList = $.map($("tr[id^='stockTr_"+skuId+"_']"),
				function(p){
					return $(p).attr("id").split("_")[2];
				}
			);
			
			var stockJsonStr = "";
			for(var d = 0; d < stockList.length; d++){
				var skuDetailId = stockList[d];
				var isPass = $("#isPass_"+skuId+"_"+skuDetailId).val();
				if(isPass == '0'){//克隆
					var detailNumber = $("#numberCode_"+skuId+"_"+skuDetailId).val();
					stockJsonStr = stockJsonStr + detailNumber;
					if(d < (stockList.length-1)){
						stockJsonStr = stockJsonStr + ",";
					}
				}
			}
			
			var skuData = {
				skuNumber : skuNumber,
				stock : stockJsonStr
			};
			skuJson[skuJson.length] = skuData;
		}
	}
	
	$.ajax({
		url : "../PtviewWS/syncPtviewOper.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			ptview : ptview,
			skuJson : JSON.stringify(skuJson)
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#ptviewManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_syncPtviewButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function skuManager(numberCode, cruise, nameCn, shipment){
	xyzdialog({
        dialog:'dialogFormDiv_skuManager',
        title:"管理产品【"+ nameCn +"】的SKU套餐",
        content:"<iframe id='dialogFormDiv_skuManagerIframe' frameborder='0' name='"+ numberCode +"'></iframe>",
        buttons:[{
            text:'返回',
            handler:function() {
            	$("#dialogFormDiv_skuManager").dialog("destroy");
            }
        }]
    });
    var tempWidth = $("#dialogFormDiv_skuManager").css("width");
    var tempHeight = $("#dialogFormDiv_skuManager").css("height");
    var tempWidth2 = parseInt(tempWidth.split("px")[0]);
    var tempHeight2 = parseInt(tempHeight.split("px")[0]);
    $("#dialogFormDiv_skuManagerIframe").css("width",(tempWidth2-2) + "px");
    $("#dialogFormDiv_skuManagerIframe").css("height",(tempHeight2-50) + "px");
    $("#dialogFormDiv_skuManagerIframe").attr("src","../jsp_ptview/ptviewSkuManager.html?ptview="+numberCode+"&cruise="+cruise+"&shipment="+shipment);
}

function cruiseSortButton(){
	var cruiseList = [];
	$.ajax({
		url : "../PtviewWS/getSortNumCruise.do",
		type : "POST",
		data : {
			page : 0,
			rows : 100
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				cruiseList = data.content.rows;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
	var htmlContent = '<center>';
	htmlContent += '<div id="sortDiv" style="margin-top:4px;">';
	for (var i = 0; i < cruiseList.length; i++) {
		var cruiseObj = cruiseList[i];
		if((i != 0 && i%17 == 0)){
			htmlContent += '</div>';
		}
		if(i == 0 || i%17 == 0){
			htmlContent += '<div style="display:inline-block;vertical-align:top;margin:2px;">';
		}
		htmlContent += ' <div class="sortDroppable" style="width:245px;height:35px;padding:3px;position:relative;border:solid 1px #F2E0E0;box-sizing:border-box;text-align:left;vertical-align:middle;">';
		htmlContent += '  <span style="display:inline-block;width:25px;font-size:14px;padding-right:3px;color:#A09C1B;text-align:center;">';
		htmlContent += '   <em>'+ (i+1) +'</em>';
		htmlContent += '  </span>';
		var nameCn = "【"+ cruiseObj.mark +"】"+cruiseObj.nameCn;
		htmlContent += '  <span class="easyui-draggable sortDraggable" data-options="revert:true" ';
		htmlContent += '  style="display:inline-block;width:200px;background:#DCE2F3;font-size:15px;padding:2px;" ';
		htmlContent += '  data-infoCode="'+cruiseObj.numberCode+'">'+ xyzGetDiv(nameCn, 0, 14) +'</span>';
		htmlContent += '</div>';
	}
	htmlContent += '</div>';
	htmlContent += '</center>';
	
	xyzdialog({
		dialog : 'dialogFormDiv_cruiseSortButton',
		title : '排序',
		fit:true,
	    content : htmlContent,
	    buttons:[{
			text:'确定',
			handler:function(){
				cruiseSortSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_cruiseSortButton").dialog("destroy");
			}
		}],
		onOpen:function(){
			$(".sortDraggable").draggable({
				revert : true,
				onBeforeDrag : function(e){
					var width = $(this).css("width");
					$(this).css("width",width);
				},
			});
			$(".sortDroppable").droppable({
				onDrop : function(e,source){
					var newHtml = $(source).prop("outerHTML");
					var info = $(".sortDraggable");
					var newNum = info.index(source);
					var oldNum = info.index($(this).children(".sortDraggable"));
					
					if(newNum > oldNum){
						var tempHtml = "";
						info.each(function(i,n){
							if(i > oldNum && i <= newNum){
								$(n).parent().append(tempHtml);
								$(n).remove();
							}
							if(i >= oldNum && i < newNum){
								tempHtml = $(n).prop("outerHTML");
							}
						});
					} else if(newNum < oldNum){
						var tempElementArray = info.slice(newNum,oldNum+1);
						var tempHtmlArray = $.map(tempElementArray,function(p){
							return $(p).prop("outerHTML");
						});
						tempElementArray.each(function(i,n){
							if(i < tempElementArray.length-1){
								$(n).parent().append(tempHtmlArray[i+1]);
								$(n).remove();
							}
						});
					}
					$(this).children(".sortDraggable").remove();
					$(this).append(newHtml);
					$.parser.parse();
				}
			});
		}
	});
}

function cruiseSortSubmit(){
	var numberCodes = $.map($("#sortDiv .easyui-draggable"),function(p){
		return $(p).attr("data-infoCode");
	}).join(",");
	
	$.ajax({
		url : "../PtviewWS/editSortNumCruise.do",
		type : "POST",
		data : {
			numberCodes : numberCodes
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_cruiseSortButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

//查看或编辑主图片
function upLoadImage(numberCode,nameCn,imagesUrl){
	if(xyzIsNull(numberCode)){
		top.$.messager.alert("提示","错误的参数!","info");
		return;
	}
	var htmlContent = "<div id='xyzUpImageDiv'></div><br/>"
					+ "<div id='xyzUpImageButton' style='margin-left:100px;width:200px;height:20px;'></div>";
	xyzdialog({
		dialog : 'dialogFormDiv_upLoadImage',
		title : '上传图片 【'+nameCn+'】',
		content : htmlContent,
	    fit : false,  
	    height : 300,
	    width : 500,
	    buttons : [{
			text:'确定',
			handler:function(){
				upLoadImageSubmit(numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_upLoadImage").dialog("destroy");
			}
		}],
		onOpen : function(){
			//初始化图片容器
			xyzPicPreview.create({
				xyzPicPreview:'xyzUpImageDiv',
				//初始化后要立即展示的链接
				imageUrls:imagesUrl,
				maxCount:1
			});
			//上传容器
			xyzDropzone.create({
				xyzDropzone:'xyzUpImageButton',//容器div，允许使用逗号分隔传入多个ID则同时初始化多个xyzDropzone
				params:{"derictoryCode":"ptview"},//上传时需要同时提交的参数键值对
				acceptedExtName:".jpg,.png,.jpeg,.gif,.bmp",//允许文件类型
				maxFilesize:"1024kb",//允许上传的单个文件大小（单位kb）
				btnText:'点击或拖拽文件至此',
				success:function(result){
					xyzPicPreview.addPic('xyzUpImageDiv',result.content.url);
				}
			});
		}
	});
}
function upLoadImageSubmit(numberCode){
	var urls = xyzPicPreview.getAllPic("xyzUpImageDiv").join(",");

	$.ajax({
		url : "../PtviewWS/editImage.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			urls : urls
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#ptviewManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_upLoadImage").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}
//查看主图片
function checkImage(nameCn,imagesUrl){
	
	var htmlContent = "<div id='xyzUploadImagesDiv'></div><br/>"
		+ "<div id='xyzUpImagesButton' style='margin-left:100px;width:200px;height:20px;'></div>";
	
	xyzdialog({
		dialog : 'dialogFormDiv_Images',
		title : '查看图片 【'+nameCn+'】',
		content : htmlContent,
		fit : false,  
		height : 300,
		width: 500,
		buttons:[{
			text:'关闭',
			handler:function(){
				$("#dialogFormDiv_Images").dialog("destroy");
			}
		}],
		onOpen : function(){
			xyzPicPreview.create({
				xyzPicPreview:'xyzUploadImagesDiv',
				//初始化后要立即展示的链接
				imageUrls:imagesUrl,
				maxCount:1
			});
		}
	});
}
//查看或编辑详情图片
function upLoadDetailImages(numberCode,nameCn,imagesUrl){
	if(xyzIsNull(numberCode)){
		top.$.messager.alert("提示","错误的参数!","info");
		return;
	}
	var htmlContent = "<div id='xyzUpDetailImagesDiv'></div><br/>"
					+ "<div id='xyzUpDetailImagesButton' style='margin-left:100px;width:200px;height:20px;'></div>";
	xyzdialog({
		dialog : 'dialogFormDiv_upLoadDetailImages',
		title : '上传图片 【'+nameCn+'】',
		content : htmlContent,
	    fit : false,  
	    height : 300,
	    width : 500,
	    buttons : [{
			text:'确定',
			handler:function(){
				upLoadDetailImagesSubmit(numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_upLoadDetailImages").dialog("destroy");
			}
		}],
		onOpen : function(){
			//初始化图片容器
			xyzPicPreview.create({
				xyzPicPreview:'xyzUpDetailImagesDiv',
				//初始化后要立即展示的链接
				imageUrls:imagesUrl,
				maxCount:4
			});
			//上传容器
			xyzDropzone.create({
				xyzDropzone:'xyzUpDetailImagesButton',//容器div，允许使用逗号分隔传入多个ID则同时初始化多个xyzDropzone
				params:{"derictoryCode":"Detailptview"},//上传时需要同时提交的参数键值对
				acceptedExtName:".jpg,.png,.jpeg,.gif,.bmp",//允许文件类型
				maxFilesize:"1024kb",//允许上传的单个文件大小（单位kb）
				btnText:'点击或拖拽文件至此',
				success:function(result){
					xyzPicPreview.addPic('xyzUpDetailImagesDiv',result.content.url);
				}
			});
		}
	});
}
function upLoadDetailImagesSubmit(numberCode){
	var urls = xyzPicPreview.getAllPic("xyzUpDetailImagesDiv").join(",");

	$.ajax({
		url : "../PtviewWS/editDetailImages.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			urls : urls
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#ptviewManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_upLoadDetailImages").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}
//详情图片查看
function checkDetailImages(nameCn,imagesUrl){
	
	var htmlContent = "<div id='xyzUploadDetailImagesDiv'></div><br/>"
		+ "<div id='xyzUpDetailImagesButton' style='margin-left:100px;width:200px;height:20px;'></div>";
	
	xyzdialog({
		dialog : 'dialogFormDiv_checkDetailImages',
		title : '查看详情图片 【'+nameCn+'】',
		content : htmlContent,
		fit : false,  
		height : 300,
		width: 500,
		buttons:[{
			text:'关闭',
			handler:function(){
				$("#dialogFormDiv_checkDetailImages").dialog("destroy");
			}
		}],
		onOpen : function(){
			xyzPicPreview.create({
				xyzPicPreview:'xyzUploadDetailImagesDiv',
				//初始化后要立即展示的链接
				imageUrls:imagesUrl,
				maxCount:4
			});
		}
	});
}