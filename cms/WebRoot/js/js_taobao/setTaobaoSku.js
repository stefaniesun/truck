$(document).ready(function() {
	
	var parmaeters = getUrlParameters();
	baseInfo = parmaeters.baseInfo;
	cruise = parmaeters.cruise;
	channel = parmaeters.channel;
	
	var buttonHtml = '';
	if (xyzControlButton("buttonCode_y20161108110313")) {
		buttonHtml += '<a id="addBtn" href="javascript:void(0);" class="easyui-linkbutton">新增</a>&nbsp;';
	}
	if (xyzControlButton("buttonCode_sy20161202170221")) {
		buttonHtml += '<a id="relationBtn" href="javascript:void(0);" class="easyui-linkbutton">新增关联套餐</a>&nbsp; ';
	}
	if (xyzControlButton("buttonCode_y20161108110314")) {
		buttonHtml += '<a id="editBtn" href="javascript:void(0);" class="easyui-linkbutton">编辑</a>&nbsp; ';
	}
	if (xyzControlButton("buttonCode_y20161108110315")) {
		buttonHtml += '<a id="deleteBtn" href="javascript:void(0);" class="easyui-linkbutton">删除</a>&nbsp; ';
	}
	if(channel !="meituan" && xyzControlButton("buttonCode_y20161116170220")){
		buttonHtml += '<a id="updateSkuStockBtn" href="javascript:void(0);" class="easyui-linkbutton">单个更新SKU</a>&nbsp; ';
	}
	if(channel !="meituan" && xyzControlButton("buttonCode_x20170908153801")){
		buttonHtml += '<a id="updateSkuStockListBtn" href="javascript:void(0);" class="easyui-linkbutton">批量更新SKU</a> ';
	}
	$("#queryDiv").html(buttonHtml);
	
	/*
	 * 新增
	 */
	if (xyzControlButton("buttonCode_y20161108110313")) {
		$("#addBtn").linkbutton({
			iconCls : 'icon-add',
			onClick : function(){
				addTaobaoSkuButton();
			}
		});
	}
	/*
	 * 新增关联套餐
	 */
	if (xyzControlButton("buttonCode_sy20161202170221")) {
		$("#relationBtn").linkbutton({
			iconCls : 'icon-add',
			onClick : function(){
				addTaobaoSkuRelationStockButton();
			}
		});
	}
	/*
	 * 编辑
	 */
	if (xyzControlButton("buttonCode_y20161108110314")) {
		$("#editBtn").linkbutton({
			iconCls : 'icon-edit',
			onClick : function(){
				editTaobaoSkuButton();
			}
		});
	}
	/*
	 * 删除
	 */
	if (xyzControlButton("buttonCode_y20161108110315")) {
		$("#deleteBtn").linkbutton({
			iconCls : 'icon-remove',
			onClick : function(){
				deleteTaobaoSkuButton();
			}
		});
	}
	/*
	 * 单个更新SKU
	 */
	if (channel !="meituan" && xyzControlButton("buttonCode_y20161116170220")) {
		$("#updateSkuStockBtn").linkbutton({
			iconCls : 'icon-edit',
			onClick : function(){
				updateSkuStockButton();
			}
		});
	}
	/*
	 * 批量更新SKU
	 */
	if (channel !="meituan" && xyzControlButton("buttonCode_x20170908153801")) {
		$("#updateSkuStockListBtn").linkbutton({
			iconCls : 'icon-edit',
			onClick : function(){
				updateSkuStockListButton();
			}
		});
	}
	initTable();
	
	tkviewMap = null;
	
});

function initTable(){
	
	xyzAjax({
		url : "../TaobaoWS/getTaobaoSkuOper.do",
		type : "POST",
		data : {
			baseInfo : baseInfo
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var skuList = data.content.rows;
				for(var s = 0; s < skuList.length; s++){
					var sku = skuList[s];
					addTabs(s,sku.packageName,sku.numberCode,sku.isUpdate);
				}
				
				$('#skuPackageTabs').tabs({
					select : 0
				}); 
				
				$("#skuPackageTabs").tabs({
					onSelect : function(title,index){
						$('#skuPackageTabs').tabs('select',title);
						if (!($(".tabs-panels").find("div[id='"+skuList[index].numberCode+"']").find("iframe").length > 0) ) { 
							var content = '<iframe src="../jsp_taobao/taobaoSkuCalendar.html?skuInfos='+skuList[index].numberCode+'&baseInfo='+baseInfo+'&cruise='+cruise+'" ';
					    	content += ' style="width:100%;height:90%;border:1px;">';
					    	content += '</iframe>';
					    	var tab = $('#skuPackageTabs').tabs('getSelected');
					    	$('#skuPackageTabs').tabs('update', {
					    		tab : tab,
					    		options : {
					    			content: content
					    		}
					    	});
						}
				    }
				});
				
			}
		}
	});
	
}

function addTabs(index,title,skuNumber,isUpdate){
	var content = '';
	if(index == 0){
		content += '<iframe src="../jsp_taobao/taobaoSkuCalendar.html?skuInfos='+skuNumber+'&baseInfo='+baseInfo+'&cruise='+cruise+'" ';
    	content += ' style="width:100%;height:90%;border:1px;">';
    	content += '</iframe>';
	}
	
	$("#skuPackageTabs").tabs('add',{
	    title : title,    
	    id : skuNumber,
	    content : content,
	    closable : false
	});
	
	if(isUpdate == 1){
		$(".tabs li:last-child a span").eq(0).attr("style","background-color:#f7dbdd;");
	}
	
}

function addTaobaoSkuButton(){
	
	xyzdialog({
		dialog : 'dialogFormDiv_addTaobaoSkuButton',
		title : '新增SKU',
		href : '../jsp_taobao/addTaobaoSku.html',
		fit : false,
		width : 450,
		height : 230,
		buttons : [ {
			text : '确定',
			handler : function() {
				addTaobaoSkuSubmit();
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_addTaobaoSkuButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			xyzCombobox({
				combobox : 'tkviewTypeForm',
				url : '../ListWS/getTkviewTypeList.do',
				mode: 'remote',
				editable : false,
				lazy : false,
				multiple : true,
				panelMaxHeight : 230,
				onBeforeLoad: function(param){
					param.cruise = cruise;
				},
				onChange: function(newValue,oldValue){
					$("#tkviewTypeHidden").val(newValue);
				},
				icons : [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			
		}
	});
	
}

function addTaobaoSkuSubmit(){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").val();
	var tkviewType = $("#tkviewTypeHidden").val();
	
	$.ajax({
		url : '../TaobaoWS/addTaobaoSku.do',
		type : "POST",
		data : {
			baseInfo : baseInfo,
			skuNameCn : nameCn,
			tkviewType : tkviewType
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_addTaobaoSkuButton").dialog("destroy");
				
				window.location.reload(); 
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function addTaobaoSkuRelationStockButton(){
	
	xyzdialog({
		dialog : 'dialogFormDiv_addTaobaoSkuRelationStockButton',
		title : '批量添加关联SKU及库存',
		href : '../jsp_taobao/addTaobaoSkuRelationStock.html',
		fit : true,
		buttons : [ {
			text : '确定',
			handler : function() {
				addTaobaoSkuRelationStockSubmit();
			}
		},{
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_addTaobaoSkuRelationStockButton").dialog("destroy");
			}
		}],
		onLoad:function(){
			initHtml();
		}
	});
}

function initHtml(){
	
	$.ajax({
		url : '../TaobaoWS/getTkviewByTaobaoCruise.do',
		type : "POST",
		data : {
			baseInfo : baseInfo
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				var content = data.content;
				tkviewMap = content;
				
				var html = '';
				var tempKey = '';
				for ( var key in content) {
					var tkviewList = content[key];
					for ( var i = 0; i < tkviewList.length; i++) {
						var tkviewObj = tkviewList[i];
						if (tempKey != key) {
							html += '<tr><td>'+key.substr(12)+'</td>';
							html += '<td><span>'+tkviewObj.shipmentTravelDate.substr(0,10)+'</span></td>';
							html += '<td><input style="width:100px;" class="typeClass" id="type_'+tkviewObj.numberCode+'" /></td>';
							html += '<td><input style="width:100px;" class="priceClass" id="price_'+tkviewObj.numberCode+'" value="0" /></td>';
							html += '<td><input style="width:100px;" class="stockClass" id="stock_'+tkviewObj.numberCode+'" value="'+tkviewObj.stock+'"/></td>';
							html += '<td><img class="lockClass" id="lock_'+tkviewObj.numberCode+'" alt="点我忽略此单品" src="../image/other/unlock.png" title="点我忽略此单品" style="cursor: pointer;"></td>';
							html += '<input type="hidden" id="isLock_'+tkviewObj.numberCode+'" value="0"/>';
							html += '<input type="hidden" id="tkview_'+tkviewObj.numberCode+'" value="'+tkviewObj.numberCode+'"/></tr>';
						}else {
							html += '<tr><td></td>';
							html += '<td><span>'+tkviewObj.shipmentTravelDate.substr(0,10)+'</span></td>';
							html += '<td><input style="width:100px;" class="typeClass" id="type_'+tkviewObj.numberCode+'" /></td>';
							html += '<td><input style="width:100px;" class="priceClass" id="price_'+tkviewObj.numberCode+'" value="0" /></td>';
							html += '<td><input style="width:100px;" class="stockClass" id="stock_'+tkviewObj.numberCode+'" value="'+tkviewObj.stock+'"/></td>';
							html += '<td><img class="lockClass" id="lock_'+tkviewObj.numberCode+'" alt="点我忽略此单品" src="../image/other/unlock.png" title="点我忽略此单品" style="cursor: pointer;"></td>';
							html += '<input type="hidden" id="isLock_'+tkviewObj.numberCode+'" value="0"/>';
							html += '<input type="hidden" id="tkview_'+tkviewObj.numberCode+'" value="'+tkviewObj.numberCode+'"/></tr>';
						}
						
						tempKey = key;
					}
				}
				
				$("#tkviewTable").append(html);
				
				$(".typeClass").combobox({
					required: true, 
					editable : false,
					valueField: 'value',
					textField: 'text',
					data: [{
						value: '1',
						text: '成人'
					},{
						value: '2',
						text: '儿童'
					},{
						value: '3',
						text: '单房差'
					}]
				});
				
				$(".priceClass").numberbox({
					required: true, 
					min:0,    
				    precision:2, 
					icons: [{
						iconCls:'icon-clear',
						handler: function(e){
							$(e.data.target).numberbox('clear');
						}
					 }]
				});
				$(".stockClass").numberbox({
					required: true, 
					min:0,    
					precision:0, 
					icons: [{
						iconCls:'icon-clear',
						handler: function(e){
							$(e.data.target).numberbox('clear');
						}
					}]
				});
				
				$(".lockClass").click(function(){
					var tempId = $(this).prop('id').split('_')[1];
					lockClick(tempId);
				});
				getExistSkuByBaseInfo();
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function lockClick(tempId){
	var tempValue = $("#isLock_"+tempId).val();
	if (tempValue == '1') {
		$("#lock_"+tempId).attr('src','../image/other/unlock.png');
		$("#lock_"+tempId).attr('alt','点我忽略此单品');
		$("#lock_"+tempId).attr('title','点我忽略此单品');
		$("#isLock_"+tempId).val('0');
		
		$("#type_"+tempId).combobox({
			required : true,
			disabled : false							
		});
		
		$("#price_"+tempId).numberbox({
			required : true,
			disabled : false,
			value : 0
		});
		
		$("#stock_"+tempId).numberbox({
			required : true,
			disabled : false							
		});
		
	}else {
		$("#lock_"+tempId).attr('src','../image/other/lock.png');
		$("#lock_"+tempId).attr('alt','点我启用此单品');
		$("#lock_"+tempId).attr('title','点我启用此单品');
		$("#isLock_"+tempId).val(1);
		
		$("#type_"+tempId).combobox({
			required : false,
			disabled : true							
		});
		
		$("#price_"+tempId).numberbox({
			required : false,
			disabled : true,
			value : 0
		});
		
		$("#stock_"+tempId).numberbox({
			required : false,
			disabled : true							
		});
	}
}

function getExistSkuByBaseInfo(){
	$.ajax({
		url : '../TaobaoWS/getExistSkuByBaseInfo.do',
		type : "POST",
		data : {
			baseInfo : baseInfo
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				var content = data.content;
				for ( var i = 0; i < content.length; i++) {
					lockClick(content[i]);
				}
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function addTaobaoSkuRelationStockSubmit(){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var skuJson = {};
	
	for ( var key in tkviewMap) {
		var tempSkuJson = [];
		var tkviewList = tkviewMap[key];
		for ( var i = 0; i < tkviewList.length; i++) {
			var tkviewObj = tkviewList[i];

			var isLock = $("#isLock_"+tkviewObj.numberCode).val();
			if (isLock == '0') {
				var type = $("#type_"+tkviewObj.numberCode).combobox('getValue');
				var price = $("#price_"+tkviewObj.numberCode).numberbox('getValue');
				var stock = $("#stock_"+tkviewObj.numberCode).numberbox('getValue');
				var tkview = $("#tkview_"+tkviewObj.numberCode).val();
				
				var tempJson = {
					date : tkviewObj.shipmentTravelDate.substr(0,10),
					type : type,
					price : xyzSetPrice(price),
					stock : stock,
					tkview : tkview
				};
				
				tempSkuJson[tempSkuJson.length] = tempJson;
			}
		}
		
		skuJson[key] = tempSkuJson;
	}
	
	var skuAndStockJson = [];
	
	skuAndStockJson[skuAndStockJson.length] = skuJson;
	
	$.ajax({
		url : '../TaobaoWS/addRelationSkuAndStock.do',
		type : "POST",
		data : {
			skuJsonStr : JSON.stringify(skuAndStockJson),
			baseInfo : baseInfo
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#dialogFormDiv_addTaobaoSkuRelationStockButton").dialog("destroy");
				top.$.messager.alert("提示", "操作成功!", "info");
				
				window.location.reload(); 
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function editTaobaoSkuButton(){
	
	var selectTabs = $("#skuPackageTabs").tabs("getSelected");
	var skuNumberCode = selectTabs.attr("id"); 
	var packageName = $(".tabs-selected").text(); 
	
	xyzdialog({
		dialog : 'dialogFormDiv_editTaobaoSkuButton',
		title : '编辑SKU',
		href : '../jsp_taobao/editTaobaoSku.html',
		fit : false,
		width : 450,
		height : 230,
		buttons : [ {
			text : '确定',
			handler : function() {
				editTaobaoSkuSubmit(skuNumberCode);
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_editTaobaoSkuButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			$("#nameCnForm").val(packageName);
			$('#nameCnForm').validatebox({    
			    required : true   
			});
			
			xyzCombobox({
				combobox : 'tkviewTypeForm',
				url : '../ListWS/getTkviewTypeList.do',
				mode: 'remote',
				editable : false,
				lazy : false,
				multiple : true,
				panelMaxHeight : 230,
				onBeforeLoad: function(param){
					param.cruise = cruise;
				},
				onChange: function(newValue,oldValue){
					$("#tkviewTypeHidden").val(newValue);
				},
				icons : [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			
			getTkviewTypeData(skuNumberCode);
		}
	});
	
}

function getTkviewTypeData(numberCode){
	
	$.ajax({
		url : "../TaobaoWS/getRelationTkviewType.do",
		type : "POST",
		data : {
			taobaoSku :  numberCode
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var tkviewType = data.content;
				
				if(!xyzIsNull(tkviewType)){
					$("#tkviewTypeForm").combobox({
						value : tkviewType.split(','),
						onChange: function(newValue,oldValue){
							$("#tkviewTypeHidden").val(newValue);
						}
					});
					$("#tkviewTypeHidden").val(tkviewType);
				}
				
			}else{
				top.$.messager.alert("警告","没有关联的航线日期单品!","warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function editTaobaoSkuSubmit(skuNumberCode){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").val();
	var tkviewType = $("#tkviewTypeHidden").val();
	
	$.ajax({
		url : '../TaobaoWS/editTaobaoSku.do',
		type : "POST",
		data : {
			baseInfo : baseInfo,
			skuInfo : skuNumberCode,
			skuNameCn : nameCn,
			tkviewType : tkviewType
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_editTaobaoSkuButton").dialog("destroy");
				window.location.reload(); 
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function deleteTaobaoSkuButton(){
	
	var selectTabs = $("#skuPackageTabs").tabs("getSelected");
	var sku = selectTabs.attr("id"); 
	
	$.messager.confirm('确认','您确认想要删除SKU吗？',function(r){    
		if(r){
			$.ajax({
				url : '../TaobaoWS/deleteTaobaoSku.do',
				type : "POST",
				data : {
					baseInfo : baseInfo,
					skuInfos : sku,
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.status == 1) {
						window.location.reload(); 
						
						$("#dialogFormDiv_deleteTaobaoSkuButton").dialog("destroy");
						top.$.messager.alert("提示", "操作成功!", "info");
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

function updateSkuStockButton(){
	var selectTabs = $("#skuPackageTabs").tabs("getSelected");
	var skuNumberCode = selectTabs.attr("id"); 
	var packageName = $(".tabs-selected").text(); 
	
	xyzdialog({
		dialog : 'dialogFormDiv_updateSkuStockButton',
		title : '单个更新SKU',
		href : '../jsp_taobao/updateSkuStock.html',
		height : 400, 
		buttons : [ {
			text : '确定',
			handler : function() {
				updateSkuStockSubmit(skuNumberCode);
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_updateSkuStockButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			querySkuUpdate(skuNumberCode);
			
			$("#skuNameCnSpan").html(packageName);
		}
	});
}

function querySkuUpdate(skuNumberCode){
	
	$.ajax({
		url:"../TaobaoWS/queryStockByNumberCodes.do",
		type:"POST",
		data:{
			sku : skuNumberCode
		},
		async:false,
		dataType:"json",
		success:function(data) {
			if(data.status==1){
				var stockList = data.content;
				
				$("#tkviewTbody").empty();
				var stockHtml = '';
				for(var s = 0; s < stockList.length; s++){
					var stockObj = stockList[s];
					if(stockObj.isUpdate==1){
						stockHtml += '<tr id="stockTr_'+ s +'" style="background-color:#f7dbdd;">';
					}else{
						stockHtml += '<tr id="stockTr_'+ s +'">';
					}
					stockHtml += ' <td>';
					if(stockObj.isUpdate==1){
						stockHtml += '  <input id="isUpdete_'+ s +'" type="checkbox" checked="checked"/>';
						stockHtml += '  <input id="isUpdeteValue_'+ s +'" type="hidden" value="1"/>';
					}else{
						stockHtml += '  <input id="isUpdete_'+ s +'" type="checkbox"/>';
						stockHtml += '  <input id="isUpdeteValue_'+ s +'" type="hidden" value="0"/>';
					}
					stockHtml += ' </td>';
					
					stockHtml += ' <td>'+ xyzGetDiv(stockObj.date,0,10) +'</td>';
					stockHtml += ' <td>'+ stockObj.stock +'</td>';
					var type = "成人";
					if(stockObj.priceType == 2){
						type = "儿童";
					}else if(stockObj.priceType == 3){
						type = "单房差";
					}
					stockHtml += ' <td>'+ type +'</td>';
					stockHtml += ' <td>';
					stockHtml += ' 	<input id="numberCodeForm_'+ s +'" type="hidden" value="'+ stockObj.numberCode +'"/>';
					stockHtml += ' 	<input id="priceForm_'+ s +'" type="text" />';
					stockHtml += ' </td>';
					stockHtml += ' <td>';
					stockHtml += '  <input id="checkbox_'+ s +'" type="checkbox" />';
					stockHtml += ' 	<input id="checkValueForm_'+ s +'" type="hidden" value="'+ stockObj.isNormal +'"/>';
					stockHtml += ' </td>';
					stockHtml += '</tr>';
				}
				$("#StockTbody").append(stockHtml);
				
				for(var s = 0; s < stockList.length; s++){
					var stockObj = stockList[s];
					var price = xyzGetPrice(stockObj.price);
					
					if(stockObj.isNormal == 0){
						$("#priceForm_"+s).numberbox({
							required : false,
							disabled : true,
							min : 0,
							validType : 'length[1,50]',
							precision : 2,
							value : price,
							icons : [{
								iconCls:'icon-clear',
								handler: function(e){
									$(e.data.target).numberbox('clear');
								}
							}]
						});
					}else{
						$("#priceForm_"+s).numberbox({
							required: true,  
							min : 0,
							validType : 'length[1,50]',
							precision : 2,
							value : price,
							icons : [{
								iconCls:'icon-clear',
								handler: function(e){
									$(e.data.target).numberbox('clear');
								}
							}]
						});
					}
					
					$("input[id^='isUpdete_"+s+"']").click(function(){
						var id = $(this).prop('id').split('_')[1];
						var isUpdateValue = $("#isUpdeteValue_"+id).val();
						if(isUpdateValue == 1){
							isUpdateValue = 0;
						}else{
							isUpdateValue = 1; //更新
						}
						$("#isUpdeteValue_"+id).val(isUpdateValue);
					});
					
					$("#checkbox_"+s).click(function(){
						var id = $(this).prop('id').split('_')[1];
						var checkValue = $("#checkValueForm_"+id).val();
						if(checkValue == '0'){
							$("#priceForm_"+id).numberbox({
								required : true,
								disabled : false
							});
							checkValue = '1';
						}else{
							$("#priceForm_"+id).numberbox({
								required : false,
								disabled : true
							});
							checkValue = '0';
						}
						$("#checkValueForm_"+id).val(checkValue);
						if(xyzControlButton("buttonCode_x20170320172601")){
							var skuNumber = $("#numberCodeForm_"+id).val();
							editIsNormal(skuNumber, checkValue);
						}
					});
					
				}
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function editIsNormal(skuNumber, checkValue){
	
	$.ajax({
		url : "../TaobaoWS/editIsNormalByNumberCode.do",
		type : "POST",
		data : {
			numberCode : skuNumber,
			isNormal : checkValue
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status != 1){
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function updateSkuStockSubmit(skuNumberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var stockJson = [];
	var count = 0;
	$("tr[id^='stockTr_']").each(function(){
		var index = $(this).prop('id').split('_')[1];
		var isUpdeteValue = $("#isUpdeteValue_"+index).val();
		if(isUpdeteValue == 1){
			var numberCode = $("#numberCodeForm_"+index).val();
			var price = $("#priceForm_"+index).numberbox('getValue');
			var isNormal = $("#checkValueForm_"+index).val();
			
			var stockObj = {
				numberCode : numberCode,
				price : price,
				isNormal : isNormal
			};
			stockJson[stockJson.length] = stockObj;
			count ++;
		}
	});
	
	if(count == 0){
		top.$.messager.alert("提示","请选择要更新的SKU日历库存!","info");
		return false;
	}
	
	$.ajax({
		url : "../TaobaoWS/skuPriceModifyOper.do",
		type : "POST",
		data : {
			sku : skuNumberCode,
			stockJson : JSON.stringify(stockJson)
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var tab = $('#skuPackageTabs').tabs('getSelected');
				var content = '<iframe src="../jsp_taobao/taobaoSkuCalendar.html?skuInfos='+skuNumberCode+'&baseInfo='+baseInfo+'&cruise='+cruise+'" ';
		    	content += ' style="width:100%;height:100%;border:1px;">';
		    	content += '</iframe>';
		    	$('#skuPackageTabs').tabs('update', {
		    		tab : tab,
		    		options : {
		    			content: content
		    		}
		    	});
				
				$("#dialogFormDiv_updateSkuStockButton").dialog("destroy");
				
				top.$.messager.alert("提示","操作成功!","info");
			}else{
				var msg = data.msg.split(";");
				var successStr = msg[0];
				var stockList = successStr.split(",");
				if(!xyzIsNull(successStr)){
					$("tr[id^='stockTr_']").each(function(){
						var index = $(this).prop('id').split('_')[1];
						var numberCode = $("#numberCodeForm_"+index).val();
						for(var s = 0; s < stockList.length; s++){
							if(numberCode == stockList[s]){
								$("#stockTr_"+s).attr("style","background-color:white;");
								$("#isUpdeteValue_"+s).val('0');
								$("#isUpdete_"+s).attr("checked",false);
							}
						}
					});
				}
				
				top.$.messager.alert("警告", msg[1] ,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});

}

function updateSkuStockListButton(){
	xyzdialog({
		dialog : 'dialogFormDiv_updateSkuStockListButton',
		title : '批量更新SKU',
		href : '../jsp_taobao/updateSkuStockList.html',
		height : 400, 
		buttons : [ {
			text : '确定',
			handler : function() {
				updateSkuStockListSubmit();
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_updateSkuStockListButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			getSkuStockListByBaseInfo();
		}
	});
	
}

var skuNumber = [];
function getSkuStockListByBaseInfo(){
	
	$.ajax({
		url:"../TaoBaoSkuWS/getSkuStockListByBaseInfo.do",
		type:"POST",
		data:{
			baseInfo : baseInfo
		},
		async:false,
		dataType:"json",
		success:function(data) {
			if(data.status==1){
				var skuList = data.content;
				
				var skuHtml = '';
				var stockHtml = '';
				for(var k = 0; k < skuList.length; k++){
					var skuObj = skuList[k];
					var sku = skuObj.numberCode;
					var packageName = skuObj.packageName;
					if(k > 0){
						skuHtml += '&nbsp;';
					}
					if(k == 0){
						skuHtml += '<button id="sku_'+ sku +'" value="'+ packageName +'" onClick="skuClick(\''+ sku +'\')" class="click">'+ xyzGetDiv(packageName, 0, 6) +'</button> ';
						stockHtml += '<center><div id="stock_'+ sku +'">';
					}else{
						skuHtml += '<button id="sku_'+ sku +'" value="'+ packageName +'" onClick="skuClick(\''+ sku +'\')" class="un_click">'+ xyzGetDiv(packageName, 0, 6) +'</button> ';
						stockHtml += '<center><div id="stock_'+ sku +'" style="display:none;">';
					}
					//库存信息
					var detailList = skuObj.detailList;
					if(detailList.length < 1){
						stockHtml += '<span style="font-weight:bold;font-size:16px;">没有库存信息</span>';
					}else{
						skuNumber[skuNumber.length] = sku;
						stockHtml += ' <table cellpadding="6" cellspacing="6" style="text-align:center;">';
						stockHtml += '  <tr>';
						stockHtml += '   <th>是否更新</th>';
						stockHtml += '   <th>日期</th>';
						stockHtml += '   <th>库存(人)</th>';
						stockHtml += '   <th>价格类型</th>';
						stockHtml += '   <th>价格</th>';
						stockHtml += '   <th>手动改价</th>';
						stockHtml += '  </tr>';
						for(var d = 0; d < detailList.length; d++){
							var stockObj = detailList[d];
							if(stockObj.isUpdate==1){//更新
								stockHtml += '<tr id="stockTr_'+ sku +'_'+ d +'" style="background-color:#f7dbdd;">';
							}else{
								stockHtml += '<tr id="stockTr_'+ sku +'_'+ d +'">';
							}
							stockHtml += ' <td>';
							if(stockObj.isUpdate==1){//更新
								stockHtml += '  <input id="isUpdete_'+ sku +'_'+ d +'" type="checkbox" checked="checked"/>';
								stockHtml += '  <input id="isUpdeteValue_'+ sku +'_'+ d +'" type="hidden" value="1"/>';
							}else{
								stockHtml += '  <input id="isUpdete_'+ sku +'_'+ d +'" type="checkbox"/>';
								stockHtml += '  <input id="isUpdeteValue_'+ sku +'_'+ d +'" type="hidden" value="0"/>';
							}
							stockHtml += ' </td>';
							stockHtml += ' <td>'+ xyzGetDiv(stockObj.date, 0, 10) +'</td>';
							stockHtml += ' <td>'+ stockObj.stock +'</td>';
							var type = "成人";
							if(stockObj.priceType == 2){
								type = "儿童";
							}else if(stockObj.priceType == 3){
								type = "单房差";
							}
							stockHtml += ' <td>'+ type +'</td>';
							var price = xyzGetPrice(stockObj.price);
							stockHtml += ' <td>';
							stockHtml += ' 	<input id="numberCodeForm_'+ sku +'_'+ d +'" type="hidden" value="'+ stockObj.numberCode +'"/>';
							stockHtml += '  <input id="priceForm_'+ sku +'_'+ d +'" value="'+ price +'"/>';
							stockHtml += ' </td>';
							stockHtml += ' <td>';
							if(stockObj.isNormal  == 0){//自动加价逻辑
								stockHtml += '  <input id="checkbox_'+ sku +'_'+ d +'" type="checkbox" />';
							}else{//手动加价
								stockHtml += '  <input id="checkbox_'+ sku +'_'+ d +'" type="checkbox" checked="checked" />';
							}
							stockHtml += ' 	<input id="checkValueForm_'+ sku +'_'+ d +'" type="hidden" value="'+ stockObj.isNormal +'"/>';
							stockHtml += ' </td>';
							stockHtml += '</tr>';
						}
						stockHtml += ' </table>';
					}
					stockHtml += '</div></center>';
				}
				$("#skuDiv").html(skuHtml);
				$("#skuDetailDiv").html(stockHtml);
				
				for(var k = 0; k < skuList.length; k++){
					var skuObj = skuList[k];
					var sku = skuObj.numberCode;
					
					//鼠标移上效果
					$("#sku_"+sku).mouseover(function(){
						 $(this).addClass("mouseover");
					});
					$("#sku_"+sku).mouseleave(function(){
					   $(this).removeClass("mouseover");
					});
					
					var detailList = skuObj.detailList;
					if(detailList.length < 1){
						continue;
					}
					for(var s = 0; s < detailList.length; s++){
						var stockObj = detailList[s];
						//var price = xyzGetPrice(stockObj.price);
						if(stockObj.isNormal == 0){
							$("#priceForm_"+sku+"_"+s).attr("disabled","disabled");
						}else{
							$("#priceForm_"+sku+"_"+s).removeAttr("disabled");
						}
						$("#priceForm_"+sku+"_"+s).focusout(function(){
							var value = $(this).val();
							if(xyzIsNull(value)){
								top.$.messager.alert("温馨提示","第【"+ (s+1) +"】行的价格为空!","info");
								return false;
							}
							var reg = /^\d{1,15}.\d{0,2}$/;
							if(!xyzIsNull(value) && !reg.test(value)){
								top.$.messager.alert("温馨提示","第【"+ (s+1) +"】行的价格只能输入数字!","info");
								//$(this).val(price);
								$(this).val("");
								return false;
							}
						});
						
						//是否更新
						$("#isUpdete_ "+ sku +"_"+s).click(function(){
							var id = $(this).prop('id').split('_')[1];
							var index = $(this).prop('id').split('_')[2];
							var isUpdateValue = $("#isUpdeteValue_"+ id +"_"+ index).val();
							if(isUpdateValue == 1){
								isUpdateValue = 0;
							}else{
								isUpdateValue = 1; //更新
							}
							$("#isUpdeteValue_"+ id +"_"+ index).val(isUpdateValue);
						});
						
						//手动改价 1, 0:正常加价逻辑
						$("#checkbox_"+ sku +"_"+ s).click(function(){
							var id = $(this).prop('id').split('_')[1];
							var index = $(this).prop('id').split('_')[2];
							var checkValue = $("#checkValueForm_"+ id +"_"+index).val();
							if(checkValue == '0'){
								$("#priceForm_"+ id +"_"+ index).removeAttr("disabled");
								checkValue = '1';
							}else{
								$("#priceForm_"+ id +"_"+ index).attr("disabled","disabled");
								checkValue = '0';
							}
							$("#checkValueForm_"+ id +"_"+index).val(checkValue);
							if(xyzControlButton("buttonCode_x20170320172601")){
								var skuNumber = $("#numberCodeForm_"+ id +"_"+index).val();
								editIsNormal(skuNumber, parseInt(checkValue));
							}
						});
						
					}
				}
				$.parser.parse();
			}
		}
	});
	
}

//点击事件
function skuClick(sku){
	//SKU套餐事件
	$("button[id^='sku_']").each(function(){
		var id = $(this).prop('id').split('_')[1];
		$(this).removeClass();
		if(sku == id){
			$(this).addClass("click");
		}else{
			$(this).addClass("un_click");
		}
	});
	
	//SKU库存事件
	$("div[id^='stock_']").each(function(){
		var id = $(this).prop('id').split('_')[1];
		if(sku == id){
			$("#stock_"+id).show();
		}else{
			$("#stock_"+id).hide();
		}
	});
	
}

function updateSkuStockListSubmit(){
	var skuJson = [];
	var count = 0;
	for(var s = 0; s < skuNumber.length; s++){
		var skuId = skuNumber[s];
		var packageName = $("#sku_"+skuId).val();
		var stockJson = [];
		$("#stock_"+ skuId +" tr[id^='stockTr_"+ skuId +"_']").each(function(){
			var index = $(this).prop('id').split('_')[2];
			var isUpdeteValue = $("#isUpdeteValue_"+ skuId +"_"+ index).val();
			if(isUpdeteValue == 1){
				var numberCode = $("#numberCodeForm_"+ skuId +"_"+ index).val();
				var price = $("#priceForm_"+ skuId +"_"+ index).val();
				var isNormal = $("#checkValueForm_"+ skuId +"_"+ index).val();
				
				var reg = /^\d{1,15}.\d{0,2}$/;
				if(xyzIsNull(price)){
					top.$.messager.alert("温馨提示","【"+ packageName +"】SKU套餐下:第<"+ (s+1) +">行的价格为空!","info");
					return false;
				}else if(!reg.test(price)){
					top.$.messager.alert("温馨提示","【"+ packageName +"】SKU套餐下:第<"+ (s+1) +">行的价格只能输入数字!","info");
					$("#priceForm_"+ skuId +"_"+ index).val("");
					return false;
				}
				
				var tempPrice = parseFloat(price);
				if(tempPrice <= 1000){
					$.messager.confirm('确认','确定【'+ packageName +'】SKU套餐下:第<'+ (s+1) +'>行的价格小于等于1000吗？',function(r){    
						if(!r){
							$("#priceForm_"+ skuId +"_"+ index).val("");
							return false;
						}
					});
				}
				
				var stockObj = {
					numberCode : numberCode,
					price : price,
					isNormal : isNormal
				};
				stockJson[stockJson.length] = stockObj;
				count ++;
			}
		});
		
		var skuObj = {
			sku : skuId,
			stock : stockJson
		};
		skuJson[skuJson.length] = skuObj;
	}
	
	if(count == 0){
		top.$.messager.alert("提示","请选择要更新的SKU日历库存!","info");
		return false;
	}
	
	var pd = false;  //标识符
	for(var s = 0; s < skuNumber.length; s++){
		var skuId = skuNumber[s];
		var packageName = $("#sku_"+skuId).val();
		$("#stock_"+ skuId +" tr[id^='stockTr_"+ skuId +"_']").each(function(){
			var index = $(this).prop('id').split('_')[2];
			var isUpdeteValue = $("#isUpdeteValue_"+ skuId +"_"+ index).val();
			if(isUpdeteValue == 1){
				var price = $("#priceForm_"+ skuId +"_"+ index).val();
				
				var tempPrice = parseFloat(price);
				if(tempPrice <= 1000){
					$.messager.confirm('确认','确定【'+ packageName +'】SKU套餐下:第<'+ (s+1) +'>行的价格小于等于1000吗？',function(r){    
						if(!r){
							$("#priceForm_"+ skuId +"_"+ index).val("");
							pd = true;
							return false;
						}
					});
				}
			}
		});
		
		if(pd){
			return false;
		}
		
	}
	
	if(!pd){
		$.ajax({
			url : "../TaoBaoSkuWS/updateSkuStockList.do",
			type : "POST",
			data : {
				skuJson : JSON.stringify(skuJson)
			},
			async : false,
			dataType : "json",
			success : function(data) {
				if(data.status==1){
					window.location.reload();
					$("#dialogFormDiv_updateSkuStockListButton").dialog("destroy");
					top.$.messager.alert("提示","操作成功!","info");
				}else{
					top.$.messager.alert("警告", data.msg ,"warning");
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
			}
		});
		
	}
	
}

function workClick(numberCode){
	
	var isLock = $("#lockHidden_"+numberCode).val();
	
	if(isLock == 0){
		isLock = 1;
		$("#lockWOrk_"+numberCode).attr('src','../image/other/lock.png');
		$("#lockWOrk_"+numberCode).attr('alt','点我解锁价格');
		$("#lockWOrk_"+numberCode).attr('title','点我解锁价格');
	}else{
		isLock = 0;
		$("#lockWOrk_"+numberCode).attr('src','../image/other/unlock.png');
		$("#lockWOrk_"+numberCode).attr('alt','点我锁定价格');
		$("#lockWOrk_"+numberCode).attr('title','点我锁定价格');
	}
	$("#lockHidden_"+numberCode).val(isLock);
	
	$.ajax({
		url : "../TaoBaoSkuWS/editDetailByNumberCode.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			isLock : isLock
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status == 1){
				var logWork = data.content;
				
				var html = '<tr>';
				html += ' <td>'+ logWork.username +'</td>';
				html += ' <td>'+ xyzGetDivDate(logWork.addDate) +'</td>';
				html += ' <td>'+ xyzGetDiv(logWork.remark, 0, 40) +'</td>';
				html += '</tr>';
				$("#logWorkTable").append(html);
			}else{
				top.$.messager.alert("警告", data.msg ,"warning");
			}
		}
	});
	
}