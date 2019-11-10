$(document).ready(function(){
	xyzTextbox("nameCn");
	
	initTable();
	
	$("#excelQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = []; 
	if(xyzControlButton("buttonCode_x20170308112301")){
		toolbar[toolbar.length]={
				text: '上传excel',  
				border:'1px solid #bbb',  
				iconCls: 'icon-add', 
				handler: function(){
					importExcelDataButton();
			}
		};
	}
	
	if(xyzControlButton("buttonCode_x20170308112302")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
			text: '下载excel',  
			border:'1px solid #bbb',  
			iconCls: 'icon-edit', 
			handler: function(){
				editCompanyButton();
			}
		};
	}	
	
	if(xyzControlButton("buttonCode_y20171024104101")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
			text: '下载模板',  
			border:'1px solid #bbb',  
			iconCls: 'icon-edit', 
			handler: function(){
				downTemplateButton();
			}
		};
	}	
	
	xyzgrid({
		table : 'excelManagerTable',
		title : "上传excel列表",
		url:'../ExcelWS/queryExcelLogList.do',
		toolbar:toolbar,
		singleSelect : false, 
		idField : 'numberCode',
		columns : [[
			{field:'numberCode',title:'编号',hidden:true},
			{field : 'fileUrl',title : '文件路径',
			   formatter:function(value ,row ,index){
				  return "<a href='"+value+"'>"+value+"</a>";
			   }
			},
			{field:'addDate',title:'上传时间',halign:'center',
			   formatter:function(value ,row ,index){
				  return xyzGetDiv(value);
			   }
			},
			{field : 'username',title : '操作人',halign:'center'},
			{field : 'changeCount',title : '影响SKU条数',align:'center'}
		]]
	});
}

function loadTable(){
	var nameCn = $("#nameCn").textbox("getValue");

	$("#excelManagerTable").datagrid('load', {
		nameCn : nameCn
	});
}

function importExcelDataButton(){
	
	xyzdialog({
		dialog : 'dialogFormDiv_importExcelDataButton',
		title : '导入Excel',
		content : '<div id="xyzUploadifyGridDiv"></div><br/><div id="xyzUploadifyButton" style="margin-left:100px; width:200px; height:20px;"></div>',
		fit : false,
		width : 450,          
		height : 450,
	    buttons : [{
			text:'确定',
			handler:function(){
				importExcelDataSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_importExcelDataButton").dialog("destroy");
			}
		}],onOpen : function(){
			
			//初始化图片容器
			xyzPicPreview.create({
				xyzPicPreview : 'xyzUploadifyGridDiv',
				//初始化后要立即展示的链接
				maxCount : 1
			});
			//上传容器
			xyzDropzone.create({
				xyzDropzone : 'xyzUploadifyButton',//容器div，允许使用逗号分隔传入多个ID则同时初始化多个xyzDropzone
				params : {"derictoryCode":"tkview"},//上传时需要同时提交的参数键值对
				maxFiles : 1,//本控件最多允许上传的文件数量 默认10
				acceptedExtName : ".xls,.xlsx",//允许文件类型
				maxFilesize : "3072kb",//允许上传的单个文件大小（单位kb）
				btnText : '点击或拖拽文件至此',
				success : function(result){
					xyzPicPreview.addPic('xyzUploadifyGridDiv',result.content.url);
				}
			});
			
		}
	});
	
}

function importExcelDataSubmit(){
	var path = xyzPicPreview.getAllPic("xyzUploadifyGridDiv").join(",");
	if(xyzIsNull(path)){
		top.$.messager.alert("提示","请先上传Excel文件!","info");
		return false;
	}
	
	$.ajax({
		url : "../ExcelWS/importExcelDataOper.do",
		type : "POST",
		data : {
			excelPath : path
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#dialogFormDiv_importExcelDataButton").dialog("destroy");
				$("#excelManagerTable").datagrid("reload");
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

function editCompanyButton(){

	xyzdialog({
		dialog : 'dialogFormDiv_editCompanyButton',
		title : '导出Excel',
		fit : false,
		width : 450,
		height : 300,
	    href : '../jsp_resources/exportExcelData.html',
	    buttons:[{
			text:'导出',
			handler:function(){
				editCompanySubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editCompanyButton").dialog("destroy");
			}
		}],onLoad : function(){
			
			xyzCombobox({
				combobox : 'cruiseForm',
				url : '../ListWS/getCruiseList.do',
				mode : 'remote',
				icons : [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}],
				onChange : function(){
					$("#shipmentForm").combobox("clear");
					$("#shipmentForm").combobox("reload");
					$("#shipmentHidden").val("");
					
					$("#cabinForm").combobox("clear");
					$("#cabinForm").combobox("reload");
					$("#cabinHidden").val("");
					
					$("#providerForm").combobox("clear");
					$("#providerForm").combobox("reload");
					$("#providerdden").val("");
					
				},
			    onSelect : function(){
					$("#shipmentForm").combobox("clear");
					$("#shipmentForm").combobox("reload");
					$("#shipmentHidden").val("");
					
					$("#cabinForm").combobox("clear");
					$("#cabinForm").combobox("reload");
					$("#cabinHidden").val("");
					
					$("#providerForm").combobox("clear");
					$("#providerForm").combobox("reload");
					$("#providerdden").val("");
				}
			});
			
			xyzCombobox ({
				combobox:'shipmentForm',
				url : '../ListWS/getShipmentList.do',
				mode : 'remote',
				multiple : true,
				onBeforeLoad : function(param){
					param.cruise = $("#cruiseForm").combobox("getValue");
				},
				onChange : function(newValue, oldValue){
					$("#shipmentHidden").val(newValue);
					
					$("#providerForm").combobox("clear");
					$("#providerForm").combobox("reload");
					$("#providerdden").val("");
				},
				icons : [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			
			xyzCombobox({
				combobox:'cabinForm',
				url:'../ListWS/getCabinListByCruise.do',
				mode:'remote',
				multiple : true,
				onBeforeLoad : function(param){
					param.cruise = $("#cruiseForm").combobox("getValue");
				},
				icons : [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}],
				onChange : function(newValue, oldValue){
					$("#cabinHidden").val(newValue);
					
					$("#providerForm").combobox("clear");
					$("#providerForm").combobox("reload");
					$("#providerdden").val("");
				}
			});
			
			xyzCombobox({
				combobox:'providerForm',
				url:'../ListWS/getProviderByTkviewList.do',
				mode:'remote',
				multiple : true,
				onBeforeLoad : function(param){
					param.cruise = $("#cruiseForm").combobox("getValue");
					param.shipment = $("#shipmentHidden").val();
					param.cabin = $("#cabinHidden").val();
				},
				onChange: function(newValue, oldValue){
					$("#providerHidden").val(newValue);
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

function editCompanySubmit(){
	/*var provider = $("#providerForm").combobox("getValue");*/
	var provider = $("#providerHidden").val();
	var cruise = $("#cruiseForm").combobox("getValue");
/*	var shipment = $("#shipmentForm").combobox("getValue");
	var cabin = $("#cabinForm").combobox("getValue");*/
	var shipment = $("#shipmentHidden").val();
	var cabin = $("#cabinHidden").val();
	
	
	$.ajax({
		url : "../ExcelWS/exportExcelDataOper.do",
		type : "POST",
		async : false,
		dataType : "json",
		data : {
			provider : provider, 
			cruise : cruise,
			shipment : shipment, 
			cabin : cabin
		},
		success : function(data) {
			if(data.status==1){
				window.location.assign("../tempFile/"+data.content);
				$("#excelManagerTable").datagrid("reload");
				$("#dialogFormDiv_editCompanyButton").dialog("destroy");
				
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
function downTemplateButton(){

	xyzdialog({
		dialog : 'dialogFormDiv_downTemplateButton',
		title : '下载模板',
		fit : false,
		width : 450,
		height : 300,
	    href : '../jsp_resources/downTemplate.html',
	    buttons:[{
			text:'下载',
			handler:function(){
				downTemplateSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_downTemplateButton").dialog("destroy");
			}
		}],onLoad : function(){
			
			xyzCombobox({
				combobox : 'cruiseForm',
				url : '../ListWS/getCruiseList.do',
				mode : 'remote',
				icons : [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}],
				onChange : function(){
					$("#shipmentForm").combobox("clear");
					$("#shipmentForm").combobox("reload");
					$("#shipmentHidden").val("");
					
					$("#cabinForm").combobox("clear");
					$("#cabinForm").combobox("reload");
					$("#cabinHidden").val("");
				},
			    onSelect : function(){
					$("#shipmentForm").combobox("clear");
					$("#shipmentForm").combobox("reload");
					$("#shipmentHidden").val("");
					
					$("#cabinForm").combobox("clear");
					$("#cabinForm").combobox("reload");
					$("#cabinHidden").val("");
				}
			});
			
			xyzCombobox ({
				combobox:'shipmentForm',
				url : '../ListWS/getShipmentByTkviewList.do',
				mode : 'remote',
				multiple : true,
				onBeforeLoad : function(param){
					param.cruise = $("#cruiseForm").combobox("getValue");
				},
				onChange : function(newValue, oldValue){
					$("#shipmentHidden").val(newValue);
				},
				icons : [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]
			});
			
			xyzCombobox({
				combobox:'cabinForm',
				url:'../ListWS/getCabinByTkviewList.do',
				mode:'remote',
				multiple : true,
				onBeforeLoad : function(param){
					param.cruise = $("#cruiseForm").combobox("getValue");
				},
				icons : [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}],
				onChange : function(newValue, oldValue){
					$("#cabinHidden").val(newValue);
				}
			});
		}
	});
}

function downTemplateSubmit(){
	var cruise = $("#cruiseForm").combobox("getValue");
	var shipment = $("#shipmentHidden").val();
	var cabin = $("#cabinHidden").val();
	
	$.ajax({
		url : "../ExcelWS/downTemplate.do",
		type : "POST",
		async : false,
		dataType : "json",
		data : {
			cruise : cruise,
			shipment : shipment, 
			cabin : cabin
		},
		success : function(data) {
			if(data.status==1){
				window.location.assign("../tempFile/"+data.content);
				$("#excelManagerTable").datagrid("reload");
				$("#dialogFormDiv_downTemplateButton").dialog("destroy");
				
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