$(document).ready(function() {
	
	var parmaeters = getUrlParameters();
	ptview = parmaeters.ptview;
	cruise = parmaeters.cruise;
	shipment = parmaeters.shipment;
	
	var buttonHtml = '';
	if (xyzControlButton("buttonCode_x20170602103802")) {
		buttonHtml += '<a id="addBtn" href="javascript:void(0);" class="easyui-linkbutton">新增</a>&nbsp;&nbsp;';
	}
	if (xyzControlButton("buttonCode_x20170602103803")) {
		buttonHtml += '<a id="editBtn" href="javascript:void(0);" class="easyui-linkbutton">编辑</a>&nbsp;&nbsp;';
	}
	if (xyzControlButton("buttonCode_x20170602103804")) {
		buttonHtml += '<a id="deleteBtn" href="javascript:void(0);" class="easyui-linkbutton">删除</a>';
	}
	$("#queryDiv").html(buttonHtml);
	
	/*
	 * 新增
	 */
	if (xyzControlButton("buttonCode_x20170602103802")) {
		$("#addBtn").linkbutton({
			iconCls : 'icon-add',
			onClick : function(){
				addPtviewSkuButton();
			}
		});
	}
	/*
	 * 编辑
	 */
	if (xyzControlButton("buttonCode_x20170602103803")) {
		$("#editBtn").linkbutton({
			iconCls : 'icon-edit',
			onClick : function(){
				editPtviewSkuButton();
			}
		});
	}
	/*
	 * 删除
	 */
	if (xyzControlButton("buttonCode_x20170602103804")) {
		$("#deleteBtn").linkbutton({
			iconCls : 'icon-remove',
			onClick : function(){
				deletePtviewSkuButton();
			}
		});
	}
	
	initTable();
	
	tkviewMap = null;
	
});

function initTable(){
	
	xyzAjax({
		url : "../PtviewSkuWS/queryPtviewSkuList.do",
		type : "POST",
		data : {
			ptview : ptview
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var skuList = data.content;
				for(var s = 0; s < skuList.length; s++){
					var skuObj = skuList[s];
					addTabs(s, skuObj.nameCn, skuObj.numberCode);
				}
				
				$('#ptviewSkuTabs').tabs({
					select : 0
				}); 
				
				$("#ptviewSkuTabs").tabs({
					onSelect : function(title,index){
						$('#ptviewSkuTabs').tabs('select',title);
						var numberCode = skuList[index].numberCode;
						if (!($(".tabs-panels").find("div[id='"+ numberCode +"']").find("iframe").length > 0) ) { 
							var content = '<iframe src="../jsp_ptview/ptviewSkuStock.html?ptviewSku='+numberCode+'&ptview='+ptview+'&cruise='+cruise+'&shipment='+shipment+'" ';
					    	content += ' style="width:100%;height:98%;border:1px;">';
					    	content += '</iframe>';
					    	
					    	var tab = $('#ptviewSkuTabs').tabs('getSelected');
					    	$('#ptviewSkuTabs').tabs('update', {
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

function addTabs(index, nameCn, numberCode){
	var content = '';
	if(index==0){
		content = '<iframe src="../jsp_ptview/ptviewSkuStock.html?ptviewSku='+numberCode+'&ptview='+ptview+'&cruise='+cruise+'&shipment='+shipment+'" ';
    	content += ' style="width:100%;height:100%;border:1px;">';
    	content += '</iframe>';
	}
	
	$("#ptviewSkuTabs").tabs('add',{
	    title : nameCn,    
	    id : numberCode,
	    content : content,
	    closable : false
	});
}

function addPtviewSkuButton(){
	
	xyzdialog({
		dialog : 'dialogFormDiv_addPtviewSkuButton',
		title : '新增SKU套餐',
		href : '../jsp_ptview/addPtviewSku.html',
		fit : false,
		width : 450,
		height : 200,
		buttons : [ {
			text : '确定',
			handler : function() {
				addPtviewSkuSubmit();
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_addPtviewSkuButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			xyzTextbox("nameCnForm");
			
			xyzCombobox({
				required : true,
				combobox : 'tkviewTypeForm',
				url : '../ListWS/getTkviewTypeList.do',
				mode: 'remote',
				editable : false,
				lazy : false,
				multiple : true,
				panelMaxHeight : 160,
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

function addPtviewSkuSubmit(){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").val();
	var tkviewType = $("#tkviewTypeHidden").val();
	
	$.ajax({
		url : '../PtviewSkuWS/addPtviewSku.do',
		type : "POST",
		data : {
			ptview : ptview,
			nameCn : nameCn,
			tkviewType : tkviewType
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#dialogFormDiv_addPtviewSkuButton").dialog("destroy");
				window.location.reload(); 
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

function editPtviewSkuButton(){
	
	var selectTabs = $("#ptviewSkuTabs").tabs("getSelected");
	var numberCode = selectTabs.attr("id"); 
	var nameCn = $(".tabs-selected").text(); 
	
	xyzdialog({
		dialog : 'dialogFormDiv_editPtviewSkuButton',
		title : '编辑SKU套餐',
		href : '../jsp_ptview/editPtviewSku.html',
		fit : false,
		width : 450,
		height : 230,
		buttons : [ {
			text : '确定',
			handler : function() {
				editPtviewSkuSubmit(numberCode);
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_editPtviewSkuButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			$("#nameCnForm").val(nameCn);
			$('#nameCnForm').validatebox({    
			    required : true   
			});
			xyzTextbox("nameCnForm");
			
			xyzCombobox({
				combobox : 'tkviewTypeForm',
				url : '../ListWS/getTkviewTypeList.do',
				mode: 'remote',
				editable : false,
				lazy : false,
				required : true,   
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
			
			$("#tkviewTypeForm").combobox({
				required:true
			});
			
			getTkviewTypeData(numberCode);
		}
	});
	
}

function getTkviewTypeData(numberCode){
	
	$.ajax({
		url : "../PtviewSkuWS/getRelationTkviewType.do",
		type : "POST",
		data : {
			numberCode :  numberCode
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

function editPtviewSkuSubmit(numberCode){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").val();
	var tkviewType = $("#tkviewTypeHidden").val();
	
	$.ajax({
		url : '../PtviewSkuWS/editPtviewSku.do',
		type : "POST",
		data : {
			numberCode : numberCode,
			nameCn : nameCn,
			tkviewType : tkviewType
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
		    	$("#dialogFormDiv_editPtviewSkuButton").dialog("destroy");
		    	window.location.reload(); 
		    	
		    	/*$('#ptviewSkuTabs').tabs({
					select : 0
				}); */
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

function deletePtviewSkuButton(){
	
	var selectTabs = $("#ptviewSkuTabs").tabs("getSelected");
	var numberCode = selectTabs.attr("id"); 
	
	$.messager.confirm('确认','您确认想要删除SKU套餐吗？',function(r){    
		if(r){
			$.ajax({
				url : '../PtviewSkuWS/deletePtviewSku.do',
				type : "POST",
				data : {
					numberCode : numberCode
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.status == 1) {
						$("#dialogFormDiv_deletePtviewSkuButton").dialog("destroy");
						window.location.reload();
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