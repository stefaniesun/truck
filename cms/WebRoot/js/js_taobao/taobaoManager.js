var cruiseNumber = "";
$(document).ready(function() {
	xyzTextbox("outId");
	xyzTextbox("itemId");
	xyzTextbox("itemTitle");
	
	if (!xyzControlButton("buttonCode_y20161027103001")) {
		$("addTaobaoItemButton").remove();
	}
	if (!xyzControlButton("buttonCode_y20161107151010")) {
		$("addTaobaoAllItemButton").remove();
	}
	if(!xyzControlButton("buttonCode_x20170609163001")){
		$("addProductButton").remove();
		$("addProductByMafengwoButton").remove();
	}
	
	/* 获取/更新宝贝 */
	$("#addTaobaoItemButton").click(function(){
		addTaobaoItemButton();
	});
	
	/* 获取所有宝贝 */
	$("#addTaobaoAllItemButton").click(function(){
		addTaobaoAllItemButton();
	});
	/*推送美团渠道下面的淘宝数据给美团*/
	$("#addProductButton").click(function(){
		addProductButton("");
	});
	
	/*推送马蜂窝渠道下面的淘宝数据给马蜂窝*/
	/*$("#addProductByMafengwoButton").click(function(){
		addProductByMafengwoButton("");
	});*/
	
	xyzCombobox({
		combobox : 'channel',
		url : '../ListWS/getChannelList.do',
		mode: 'remote',
		lazy : false
	});
	
	$("#itemStatus").combobox('select','');
	$("#itemType").combobox('select','');
	
	$("#taobaoQueryButton").click(function(){
		loadTable(cruiseNumber);
	});
	
	initTable();
	setColor();
	tabsData();
});

function initTable(){
	
	var toolbar = [];
	if (xyzControlButton("buttonCode_y20161114170218")) {
		toolbar[toolbar.length]={
			text: '新增',
			iconCls: 'icon-add',
			handler: function(){setTaobaoDeails("add","");}
		};
	}
	
	if (xyzControlButton("buttonCode_y20161114170218")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length]={
			text: '编辑',
			iconCls: 'icon-edit',
			handler: function(){setTaobaoDeails();}
		}; 			
	}
	
	if (xyzControlButton("buttonCode_x20161114170201")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length]={
			text: 'SKU同步',
			iconCls: 'icon-add',
			handler: function(){syncTaobaoTravelItem();}
		};
	}
	
	if (xyzControlButton("buttonCode_s20161110164101")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length]={
			text: '宝贝克隆',
			iconCls: 'icon-add',
			handler: function(){copyTaobaoTravelItem();}
		};
	}
	
	if (xyzControlButton("buttonCode_y20161110174117")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length]={
				text: '批量上传',
				iconCls: 'icon-add',
				handler: function(){publishTaobaoItem();}
		};
	}
	
	if (xyzControlButton("buttonCode_y20161107150709")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length]={
				text: '批量上架',
				iconCls: 'icon-edit',
				handler: function(){editOnlineUp('批量上架',1);}
		};
	}
	
	if (xyzControlButton("buttonCode_y20161107150709")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length]={
				text: '批量下架',
				iconCls: 'icon-edit',
				handler: function(){editOnlineUp('批量下架',0);}
		};
	}
	
	if (xyzControlButton("buttonCode_y20161104150708")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length]={
			text: '删除',
			iconCls: 'icon-remove',
			handler: function(){deleteTaobaoItemButton();}
		};
	}
	
	if (xyzControlButton("buttonCode_h20171026152100")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length]={
			text: '生成比价Excel',
			iconCls: 'icon-edit',
			handler: function(){downloadCompareExcelButton();}
		};
	}
	
	if (xyzControlButton("buttonCode_h20171104154700")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length]={
			text: '上传更新SKU表',
			iconCls: 'icon-edit',
			handler: function(){importSkuExcelButton();}
		};
	}
	if (xyzControlButton("buttonCode_y20171106155101")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length]={
			text: '锁定日志',
			iconCls: 'icon-edit',
			handler: function(){queryLogWorkListByCruiseButton();}
		};
	}
	if (xyzControlButton("buttonCode_x20171120105501")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length]={
			text: '批量上传SKU',
			iconCls: 'icon-edit',
			handler: function(){
				batchUploadSkuButton();
			}
		};
	}
	
	xyzgrid({
		table : 'taobaoManagerTable',
		toolbar : toolbar,
		url : '../TaobaoWS/queryTaobaoItem.do',
		singleSelect : false, 
		pageSize : 10,
		idField : 'numberCode',
		height : 600,
		onSelect:function(rowIndex, rowData){
			top.$('#taobaoManagerTable').accordion("select","通用备注");
			if(rowData.remark!=undefined){
				top.$("#remarkTop").text(rowData.remark);
			}
		},
		columns:[[
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'编号',hidden:true,halign:'center'},
			{field:'taobaoTravelItem',title:'外部编号',hidden:true,halign:'center'},
			{field:'channelNameCn',title:'渠道名称',width:100,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value ,0 ,80);
				}
			},
			{field:'itemId',title:'宝贝ID',halign:'center',
				formatter:function(value ,row ,index){
					var html = "";
					if(row.channel!='meituan'){
						html += xyzGetA(value,"checkItem",[row.itemUrl],"点击查看详情","blue");
					}else{
						html += row.numberCode;
					}
					return html;
				}
			},
			{field:'title',title:'标题',width:300,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value ,0 ,100);
				}
			},
			{field:'cruise',title:'邮轮编号',hidden:true,halign:'center'},
			{field:'cruiseNameCn',title:'邮轮',width:100,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value ,0 ,100);
				}
			},
			{field:'itemType',title:'类型',halign:'center',
				formatter:function(value ,row ,index){
					if (value == 1) {
						return "国内跟团游";
					}else if(value == 2){
						return "国内自由行";
					}else if(value == 3){
						return "出境跟团游";
					}else if(value == 4){
						return "出境自由行";
					}else if(value == 5){
						return "境外当地玩乐";
					}else if(value == 6){
						return "国际邮轮";
					}else if(value == 9){
						return "国内邮轮";
					}
				}	
			},//1-国内跟团游 2- 国内自由行 3-出境跟团游 4- 出境自由行 5-境外当地玩乐 6-国际邮轮 9-国内邮轮
			{field:'fromLocations',title:'出发地',halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value ,0 ,16);
				}
			},
			{field:'toLocations',title:'目的地',halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value ,0 ,20);
				}
			},
			{field:'tripMaxDays',title:'行程天数',align:'center',halign:'center'},
			{field:'onlineOper',title:'宝贝操作',halign:'center',
				formatter : function(value, row, index) {
					var html = "";
					if(row.channel!='meituan' && row.channel!='mafengwo'){
						if (row.itemStatus == 0 || row.itemStatus == 1) {
	    			  		html += "正常 ";
	  					}else if (row.itemStatus == -1) {
	  						html += "用户删除 ";
	  					}else if (row.itemStatus == -2) {
	  						html += "用户下架 ";
	  					}else if (row.itemStatus == -3) {
	  						html += "小二下架 ";
	  					}else if (row.itemStatus == -4) {
	  						html += "小二删除 ";
	  					}else if (row.itemStatus == -5) {
	  						html += "从未上架 ";
	  					}
						
						if (xyzControlButton("buttonCode_y20161116170220")) {
							if (!xyzIsNull(row.itemId)) {
								html += xyzGetA("上传","updateTaobaoItem",[row.numberCode,row.title,row.title],"上传宝贝","blue");
							}
						}
						if (xyzIsNull(row.itemId)) {
							if(xyzControlButton("buttonCode_y20161110174117")){
								html = "未发布 "+xyzGetA("上传","publishTaobaoItem",[row.numberCode],"上传","blue");
							}
						}else if(xyzControlButton("buttonCode_y20161107150709")){
							if (row.itemStatus == 0 || row.itemStatus == 1) {
								html += ' '+xyzGetA("下架","editOnlineStatus",[row.numberCode,"下架",row.title],"设置下架","blue");
							}else if (row.itemStatus == -2) {
								html += ' '+xyzGetA("上架","editOnlineStatus",[row.numberCode,"上架",row.title],"设置上架","blue");
							}else if (row.itemStatus == -5) {//从未上架
								html += ' '+xyzGetA("上架","editOnlineStatus",[row.numberCode,"上架",row.title],"设置上架","blue");
		  					}
						}
					}else if(row.channel=='meituan'){
						//row.channel=='meituan'   row.channelNameCn=="美团"
		        		if(row.channel=='meituan' && xyzControlButton("buttonCode_x20170609163001")){
							html += ' '+xyzGetA("更新到美团","addProductButton",[row.taobaoTravelItem],"更新到美团","blue");
						}
					}
					else{
						if(row.channel=='mafengwo' && xyzControlButton("buttonCode_x20170609163001")){
							html += ' '+xyzGetA("更新到马蜂窝","addProductByMafengwoButton",[row.taobaoTravelItem],"更新到马蜂窝","blue");
						}
					}
					
					return html;
				}	
			},
			{field:'skuOper',title:'SKU操作',halign:'center',
				formatter : function(value, row, index) {
					var html = "";
					if(xyzControlButton("buttonCode_y20161102153006")){
	        			 html = xyzGetA("设置SKU","skuManage",[row.numberCode,row.cruise,row.channel,row.cruiseNameCn],"设置SKU","blue");
	        		 }
					if(row.channel=='meituan'){//美团
						if(xyzControlButton("buttonCode_x20170609163002")){
		        			 html += ' '+xyzGetA("上传SKU","pushProductButton",[row.numberCode,"meituan"],"上传SKU","blue");
		        		}
					}else if(row.channel=='mafengwo'){
						if(xyzControlButton("buttonCode_x20170609163002")){ //蚂蜂窝
		        			 html += ' '+xyzGetA("上传SKU","pushProductButton",[row.numberCode,"mafengwo"],"上传SKU","blue");
		        		}
					}else{ //淘宝
		        		 if(xyzControlButton("buttonCode_y20161107150708") && !xyzIsNull(row.itemId)){
		        			 html += ' '+xyzGetA("上传SKU","overrideTaobaoSkuButton",[row.numberCode],"更新SKU","blue");
		        		 }
					}
	        		return html;
				}
			},
			{field:'otherOper',title:'其它操作',halign:'center',
	        	  formatter: function(value,row,index){
	        		  var html = "";
	        		  if(xyzControlButton("buttonCode_y20161027163003")){
	        			  html += xyzGetA("图片","upLoadImages",[row.numberCode,row.title,row.picUrls],"点我查看和上传宝贝图片!","blue");
	        		  }
	        		  if(xyzControlButton("buttonCode_y20161027163004")){
	        			  html += ' '+xyzGetA("详情","addDetail",[row.numberCode,index],"详情","blue");
	        		  }
	        		  if(xyzControlButton("buttonCode_y20161109110316")){
	        			  html += ' '+xyzGetA("日志记录","getTaoBaoLog",[row.numberCode],"点我查看日志记录!","blue");
	        		  }
	        		  return html;
	        	  }
			},
			{field:'remark',title:'备注',width:100,halign:'center',hidden:true,
				formatter:function(value ,row ,index){
					return xyzGetDiv(value ,0 ,100);
				}
			},
			{field:'addDate',title:'添加时间',hidden:true,sortable:true,order:'desc',halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'alterDate',title:'修改时间',hidden:true,sortable:true,order:'desc',halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			}
		]]
	});
}

/*设置背景颜色(有更新的SKU库存)*/
function setColor(){
	
	$("#taobaoManagerTable").datagrid({
		rowStyler : function(index,row){
			if(row.isUpdate == 1){
				return 'background-color:#f7dbdd;';
			}
		}
	});
	
}

function downloadCompareExcelButton(){

	xyzdialog({
		dialog : 'dialogFormDiv_compareExcelButton',
		title : '生成比价excel',
		fit:false,
		width : 800,
		height : 500,
	    href : '../jsp_taobao/compareExcel.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				downloadCompareExcelSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_compareExcelButton").dialog("destroy");
			}
		}],onLoad : function(){
			xyzCombobox({
				required:true,
				combobox:'priceCruiseForm',
				url:'../ListWS/getCruiseList.do',
				mode:'remote',
			});
			xyzTextbox("shopName_0");
			xyzTextbox("sourceCode_0");
			for(var k = 0; k < 4; k++){
				addcompareHtml();
			}
		}
	});
}

function addcompareHtml(){ 
	var tempId = $($(".compareExcelClass:last")).attr('id');
	tempLength = tempId.split('_')[1];
	tempLength = parseInt(tempLength)+1;
	var  addcompareHtml = '';
	addcompareHtml+='<tr  id = "compareExcel_'+tempLength+'" class = "compareExcelClass">';
	addcompareHtml +='<td style="text-align:right;">店铺名称</td>';
	addcompareHtml +='<td><input id="shopName_'+tempLength+'" type="text" style="width:300px;" /></td>';
	addcompareHtml +='<td style="text-align:right;">数据源</td>';
	addcompareHtml +='<td><input id="sourceCode_'+tempLength+'" type="text" style="width:300px;"/></td>';
	addcompareHtml +='</tr>';
	$("tr[id^=compareExcel_]:last").after(addcompareHtml);
	xyzTextbox("shopName_"+tempLength+"");
	xyzTextbox("sourceCode_"+tempLength+"");
	
}
 
function downloadCompareExcelSubmit(){
	
	var cruise = $("#priceCruiseForm").combobox("getValue");
	var shopNames = '';
	var sourceCodes = '';
	$("#compareExcel  input[id^='shopName_']").each(function(i,n){
		var name = $(this).val();
		if(!xyzIsNull(name)){
			shopNames += name +"&CMS&";
		}
	 });
	$("#compareExcel  input[id^='sourceCode_']").each(function(i,n){
		var name = $(this).val();
		if(!xyzIsNull(name)){
			var sourceName=name;
			sourceCodes += sourceName +"&CMS&";
		}
		if(!xyzIsNull(name)){
			$("#shopName_"+i).textbox({
				required : true
			});
		}
	 });
	if(!$("form").form('validate')){
		return false;
	}
	$.ajax({
		url : '../ExcelWS/downCompareData.do',
		type : "POST",
		data : {
			cruise : cruise,
			shopName : shopNames,
			sourceCode : sourceCodes
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				top.$.messager.alert("提示", "操作成功!", "info");
				window.location.assign("../tempFile/"+data.content);
				$("#dialogFormDiv_compareExcelButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function tabsData(){
	xyzAjax({
		url : "../TaobaoWS/queryTaobaoGroupCruise.do",
		type : "POST",
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var taobaoList = data.content.taobaoList;
				var countList = data.content.countList;
				
				if(taobaoList.length == 0){
					return false;
				}
				
				var html = '';
				var isUpdate = '0';
				for(var t = 0; t < taobaoList.length; t++){
					var taobaoObj = taobaoList[t];
					var cruise = taobaoObj.cruise;
					var cruiseNameCn = taobaoObj.cruiseNameCn;
					
					var update = '0';
					for(var k = 0; k < countList.length; k ++){
						var countObj = countList[k];
						if(cruise==countObj[1]){ //有更新
							isUpdate = '1';
							update = '1';
							break;
						}
					}
					html += '<button id="cruise_'+ cruise +'_'+ update +'" value="'+ cruiseNameCn +'">'+ xyzGetDiv(cruiseNameCn, 0, 6) +'</button> ';
					if(t > 0 && t%11 == 0){  //每12个换行
						html += '<br/>';
					}
				}
				html += '<button id="cruise_noValue_'+ isUpdate +'" value="取消选择"  >'+ xyzGetDiv("取消选择", 0, 6) +'</button> ';
				$("#taobaoManagerToolbar").html(html);
				
				$("#toolbarTable").datagrid({
					 toolbar : "#taobaoManagerToolbar",
					 title : "宝贝列表"
				});
				
				for(var t = 0; t < taobaoList.length; t++){
					var taobaoObj = taobaoList[t];
					var cruise = taobaoObj.cruise;
					
					var update = '0';
					for(var k = 0; k < countList.length; k ++){
						var countObj = countList[k];
						if(cruise==countObj[1]){ //有更新
							isUpdate = '1';
							update = '1';
							$("#cruise_"+cruise+"_"+update).addClass("updata");
							break;
						}
					}
					if(update == '1'){
						$("#cruise_"+cruise+"_"+update).mouseover(function(){
							 $(this).addClass("mouseover-updata");
						});
						$("#cruise_"+cruise+"_"+update).mouseleave(function(){
						   $(this).removeClass("mouseover-updata");
						});
					}else{
						$("#cruise_"+cruise+"_"+update).mouseover(function(){
							 $(this).addClass("mouseover");
						});
						$("#cruise_"+cruise+"_"+update).mouseleave(function(){
						   $(this).removeClass("mouseover");
						});
					}
					
					$("#cruise_"+cruise+"_"+update).click(function(){
						var id = $(this).prop('id').split('_')[1];
						cruiseBtnClick(id);
					});
				}
				
				
				if(isUpdate == '1'){
					$("#cruise_noValue_"+isUpdate).addClass("updata");
					$("#cruise_noValue_"+isUpdate).mouseover(function(){
						 $(this).addClass("mouseover-updata");
					});
					$("#cruise_noValue_"+isUpdate).mouseleave(function(){
					   $(this).removeClass("mouseover-updata");
					});
				}else{
					$("#cruise_noValue_"+isUpdate).mouseover(function(){
						 $(this).addClass("mouseover");
					});
					$("#cruise_noValue_"+isUpdate).mouseleave(function(){
					   $(this).removeClass("mouseover");
					});
				}
				$("#cruise_noValue_"+isUpdate).click(function(){
					cruiseBtnClick("noValue");
				});
				//默认选中
				cruiseBtnClick("noValue");
				
			}
		}
	});
}

function cruiseBtnClick(index){
	
	loadTable(index);
	
	$("button[id^='cruise_']").each(function(){
		var id = $(this).prop('id').split('_')[1];
		var isUpdate = $(this).prop('id').split('_')[2];
		
		if(index == id){
			if(isUpdate == '1'){
				$(this).addClass("updata-click");
			}else{
				$(this).addClass("click");
			}
		}else{
			if(isUpdate == '1'){
				$(this).removeClass("updata-click");
			}else{
				$(this).removeClass("click");
			}
		}
	});
	
}

//点击事件
function cruiseClick(index){
	
	/*$("button[id^='cruise_']").each(function(){
		var id = $(this).prop('id').split('_')[1];
		
		if(index == id){
			$(this).addClass("click");
		}else{
			$(this).removeClass("click");
		}
	});*/
	
}

function loadTable(cruise){
	//cruiseClick(cruise);
	cruiseNumber = cruise;
	
	var itemId = $("#itemId").val();
	var itemTitle = $("#itemTitle").val();
	var channel = $("#channel").combobox('getValue');
	var itemStatus = $("#itemStatus").combobox('getValue');
	var itemType = $("#itemType").combobox('getValue');
	var outId = $("#outId").val();
	
	$("#taobaoManagerTable").datagrid('load',{
		cruise : cruise,
		itemId : itemId,
		itemTitle : itemTitle,
		channel : channel,
		itemStatus : itemStatus,
		outId : outId,
		itemType : itemType
	});
	
}

function checkItem(value){
	window.open(value);
}

function addDetail(numberCode,index) {
	$('#taobaoManagerTable').datagrid('selectRow',index);    
	var baseInfoData = $('#taobaoManagerTable').datagrid('getSelected');    
	
	var htmlContent = "<div>";
	htmlContent += "<textarea name='ckeditor' class='value'></textarea>";
	htmlContent += "</div>";
	xyzdialog({
		dialog : 'dialogFormDiv_addDetail',
		title : '宝贝详情',
		content : htmlContent,
		fit : false,
		width : 804,
		height : 440,
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
			$("textarea[class='value']").val(baseInfoData.desc);
		}
	});
}

function getTaoBaoLog(numberCode){
	
	var htmlContent = '<table id = "logWorkTable"></table>';

	xyzdialog({
		dialog:'dialogFormDiv_getTaoBaoLog',
		title:"查看日志",
	    fit:false,
	    width:800,
	    height:600,
	    content:htmlContent,
	    buttons:[{
			text:'关闭',
			handler:function(){
				$("#dialogFormDiv_getTaoBaoLog").dialog("destroy");
			}
		}],
		onOpen:function(){
			xyzgrid({
				table : 'logWorkTable',
				url:'../TaobaoWS/getTaobaoLog.do',
				pagination : false,
				singleSelect : false,
				title : "操作日志",
				height : 'auto',
				queryParams: {
					numberCode: numberCode,
				},
				columns : [[
					{field : 'username',title : '操作人',width : 60},
					{field : 'value',title : '记录标识',width : 110},
					{field : 'addDate',title : '添加时间',width : 120},
					{field : 'remark',title : '备注',width : 400,
						formatter : function(value, row, index) {
							return "<div title='"+ value + "'>" + value + "</div>";
						}
					}
				]]
			});
		}
	});
}

function addDetailSubmit(numberCode) {
	
	var value = $("textarea[class='value']").val();
	
	$.ajax({
		url : '../TaobaoWS/addDetail.do',
		type : "POST",
		data : {
			taobaoBaseInfo : numberCode,
			detail : value
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#taobaoManagerTable").datagrid("reload");
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

function upLoadImages(numberCode,nameCn,imagesUrl){
	if(xyzIsNull(numberCode)){
		top.$.messager.alert("提示","错误的参数!","info");
		return;
	}

	var htmlContent = "<div id='xyzUploadifyGridDiv'></div><br/>"
	+ "<div id='xyzUploadifyButton' style='margin-left:100px;width:200px;height:20px;'></div>";
	xyzdialog({
		dialog : 'dialogFormDiv_upLoadImages',
		title : '上传图片 【'+nameCn+'】',
		content : htmlContent,
	    fit : false,  
	    height : 300,
	    width: 500,
	    buttons:[{
			text:'确定',
			handler:function(){
				upLoadImagesSubmit(numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_upLoadImages").dialog("destroy");
			}
		}],
		onOpen : function(){
			
			//初始化图片容器
			xyzPicPreview.create({
				xyzPicPreview:'xyzUploadifyGridDiv',
				//初始化后要立即展示的链接
				imageUrls:imagesUrl,
				maxCount:5
			});
			//上传容器
			xyzDropzone.create({
				xyzDropzone:'xyzUploadifyButton',//容器div，允许使用逗号分隔传入多个ID则同时初始化多个xyzDropzone
				params:{"derictoryCode":"taobao"},//上传时需要同时提交的参数键值对
				acceptedExtName:".jpg,.png,.jpeg,.gif,.bmp",//允许文件类型
				maxFilesize:"1024kb",//允许上传的单个文件大小（单位kb）
				btnText:'点击或拖拽文件至此',
				success:function(result){
					xyzPicPreview.addPic('xyzUploadifyGridDiv',result.content.url);
				}
			});
		}
	});
}

function upLoadImagesSubmit(numberCode){
	var urls = xyzPicPreview.getAllPic("xyzUploadifyGridDiv").join(",");

	$.ajax({
		url : "../TaobaoWS/addImages.do",
		type : "POST",
		data : {
			taobaoBaseInfo : numberCode,
			urls : urls
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#taobaoManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_upLoadImages").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function skuManage(numberCode,cruise,channel,cruiseNameCn){
	if(xyzIsNull(cruise)){
		top.$.messager.alert("提示","请先关联邮轮!","info");
		return;
	}
	
	xyzdialog({
        dialog:'dialogFormDiv_managerSku',
        title:"设置SKU【"+ cruiseNameCn +"】",
        content:"<iframe id='dialogFormDiv_managerSkuIframe' frameborder='0' name='"+ numberCode +"'></iframe>",
        buttons:[{
            text:'返回',
            handler:function() {
            	$("#dialogFormDiv_managerSku").dialog("destroy");
            }
        }]
    });
    var tempWidth = $("#dialogFormDiv_managerSku").css("width");
    var tempHeight = $("#dialogFormDiv_managerSku").css("height");
    var tempWidth2 = parseInt(tempWidth.split("px")[0]);
    var tempHeight2 = parseInt(tempHeight.split("px")[0]);
    $("#dialogFormDiv_managerSkuIframe").css("width",(tempWidth2) + "px");
    $("#dialogFormDiv_managerSkuIframe").css("height",(tempHeight2) + "px");
    $("#dialogFormDiv_managerSkuIframe").attr("src","../jsp_taobao/setTaobaoSku.html?baseInfo="+numberCode+"&cruise="+cruise+"&channel="+channel);
	
}

function addTaobaoItemButton(){
	
	xyzdialog({
		dialog : 'dialogFormDiv_addTaobaoItemButton',
		title : '获取店铺宝贝',
		href : '../jsp_taobao/getTaobaoItem.html',
		fit : false,
		width : 400,
		height : 300,
		buttons : [ {
			text : '确定',
			handler : function() {
				addTaobaoItemSubmit();
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_addTaobaoItemButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			xyzCombobox({
				combobox : 'channelForm',
				url : '../ListWS/getChannelList.do',
				mode: 'remote',
				lazy : false,
				onBeforeLoad: function(param){
					param.isMeituan = "";
				},
				required: true
			});
		}
	});
	
}

function addTaobaoItemSubmit(){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var itemId = $("#itemIdForm").val();
	var channel = $("#channelForm").combobox('getValue');
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../TaobaoWS/getTaobaoItemByIdOper.do",
		type : "POST",
		data : {
			id : itemId,
			channel : channel,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var content = data.content;
				$("#taobaoManagerTable").datagrid("reload");
				top.$.messager.alert("提示","总数【"+content.addAllCount+"】成功【"+content.addCount+"】失败【"+content.rest+"】","info");
				$("#dialogFormDiv_addTaobaoItemButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function addTaobaoAllItemButton(){
	
	xyzdialog({
		dialog : 'dialogFormDiv_addTaobaoAllItemButton',
		title : '获取店铺所有宝贝',
		href : '../jsp_taobao/getTaobaoAllItem.html',
		fit : false,
		width : 450,
		height : 450,
		buttons : [ {
			text : '确定',
			handler : function() {
				addTaobaoAllItemSubmit();
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_addTaobaoAllItemButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			xyzCombobox({
				combobox : 'channelForm',
				url : '../ListWS/getChannelList.do',
				mode: 'remote',
				lazy : false,
				onBeforeLoad: function(param){
					param.isMeituan = "";
				},
				required: true
			});
		}
	});
	
}

function addTaobaoAllItemSubmit(){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var channel = $("#channelForm").combobox('getValue');
	var remark = $("#remarkForm").val();
	
	if (xyzIsNull(channel)) {
		top.$.messager.alert("提示","请选择渠道!","info");
	}
	
	xyzProgressBar({
		id : 'addTaobaoAllItemSubmit',
		time : 1,
		func : function(){
			$.ajax({
				url : "../TaobaoWS/addAllTaobaoIdOper.do",
				type : "POST",
				data : {
					channel : channel,
					remark : remark
				},
				async : true,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						xyzCloseProgressBar('addTaobaoAllItemSubmit');
						var content = data.content;
						$("#taobaoManagerTable").datagrid("reload");
						top.$.messager.alert("提示","总数【"+content.addAllCount+"】成功【"+content.addCount+"】失败【"+content.rest+"】","info");
						$("#dialogFormDiv_addTaobaoAllItemButton").dialog("destroy");
					}else{
						xyzCloseProgressBar('addTaobaoAllItemSubmit');
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

function addProductButton(travelItem){
	
	downloadCompareExcelButton();
}

function importSkuExcelButton(){
	
	xyzdialog({
		dialog : 'dialogFormDiv_importSkuExcelButton',
		title : '导入Excel',
		content : '<div id="xyzUploadifyGridDiv"></div><br/><div id="xyzUploadifyButton" style="margin-left:100px; width:200px; height:20px;"></div>',
		fit : false,
		width : 450,          
		height : 450,
	    buttons : [{
			text:'确定',
			handler:function(){
				importSkuExcelSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_importSkuExcelButton").dialog("destroy");
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
				xyzDropzone : 'xyzUploadifyButton',  //容器div，允许使用逗号分隔传入多个ID则同时初始化多个xyzDropzone
				btnText : '点击或拖拽文件至此',
				acceptedExtName : ".xls,.xlsx",      //允许文件类型
				params : {"derictoryCode":"tkview"}, //上传时需要同时提交的参数键值对
				maxFilesize : "1024kb",              //允许上传的单个文件大小（单位kb）
				maxFiles : 100,                      //本控件最多允许上传的文件数量 默认10
				success : function(result){
					//document.getElementById('vvv').value=result.content.url;
					xyzPicPreview.addPic('xyzUploadifyGridDiv',result.content.url);
				}
			});
			
		}
	});
	
}


function importSkuExcelSubmit(){
	var path = xyzPicPreview.getAllPic("xyzUploadifyGridDiv").join(",");
	if(xyzIsNull(path)){
		top.$.messager.alert("提示","请先上传Excel文件!","info");
		return false;
	}
	
	$.ajax({
		url : "../ExcelWS/importSkuExcelOper.do",
		type : "POST",
		data : {
			excelPath : path
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#dialogFormDiv_importSkuExcelButton").dialog("destroy");
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

function deleteTaobaoItemButton(){
	
	var taobaos = $.map($("#taobaoManagerTable	").datagrid("getChecked"),function(p){return p.taobaoTravelItem;}).join(",");
	
	if(xyzIsNull(taobaos)){
		top.$.messager.alert("提示","请先勾选需要删除的对象!","info");
		return;
	}
	$.messager.confirm('确认','您确认想要删除宝贝吗?',function(r){    
		if(r){
			$.ajax({
				url : '../TaobaoWS/deleteTaobao.do',
				type : "POST",
				data : {
					numberCodes : taobaos,
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.status == 1) {
						$("#taobaoManagerTable").datagrid("reload");
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

function editOnlineStatus(numberCode,status,title){
	$.messager.confirm('确认','您确认想要'+status+'【'+title+'】吗?',function(r){    
		if(r){
			$.ajax({
				url : '../TaobaoWS/editOnlineStatus.do',
				type : "POST",
				data : {
					taobaoBaseInfo : numberCode,
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.status == 1) {
						$("#taobaoManagerTable").datagrid("reload");
						top.$.messager.alert("提示", "【"+title+"】已"+status+"", "info");
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

function editOnlineUp(status,isUp){
	
	var numberCodes = $.map($("#taobaoManagerTable").datagrid("getChecked"),function(p){return p.numberCode;}).join(",");
	
	if(xyzIsNull(numberCodes)){
		top.$.messager.alert("提示","请先勾选需要"+status+"的对象!","info");
		return;
	}
	
	if (isUp == 1) {
		isUp = true;
	}else {
		isUp = false;
	}
	
	$.messager.confirm('确认','您确认想要'+status+'吗？',function(r){
		if(r){
			$.ajax({
				url : '../TaobaoWS/editOnlineStatus.do',
				type : "POST",
				data : {
					taobaoBaseInfo : numberCodes,
					isUp : isUp
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.status == 1) {
						$("#taobaoManagerTable").datagrid("reload");
						top.$.messager.alert("提示", "【"+title+"】已"+status+"", "info");
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

function overrideTaobaoSkuButton(baseInfo){
	$.messager.confirm('确认','您确认想要更新宝贝SKU吗？',function(r){    
		if(r){
			$.ajax({
				url : '../TaobaoWS/overrideTaobaoSkuOper.do',
				type : "POST",
				data : {
					taobaoBaseInfo : baseInfo,
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.status == 1) {
						$("#taobaoManagerTable").datagrid('reload');
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

function publishTaobaoItem(baseInfo){
	
	if (xyzIsNull(baseInfo)) {
		baseInfo = $.map($("#taobaoManagerTable	").datagrid("getChecked"),
			function(p){
				return p.numberCode;
			}
		).join(",");
		
		if(xyzIsNull(baseInfo)){
			top.$.messager.alert("提示","请先勾选需要上传的对象!","info");
			return;
		}
	}
	
	$.messager.confirm('确认','您确认想要上传宝贝吗？',function(r){    
		if(r){
			$.ajax({
				url : '../TaobaoWS/publishTaobaoItemOper.do',
				type : "POST",
				data : {
					baseInfo : baseInfo,
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.status == 1) {
						$("#taobaoManagerTable").datagrid('reload');
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

function setTaobaoDeails(flag,numberCode){
	if (xyzIsNull(numberCode) && flag != "add") {
		var rows = $("#taobaoManagerTable").datagrid('getChecked');
		if(rows.length != 1){
			top.$.messager.alert("提示", "请选择单个对象!", "info");
			return;
		}
		var row = rows[0];
		numberCode = row.numberCode;
	}

	xyzdialog({
		dialog : 'dialogFormDiv_setTaobaoDeails',
		title : '',
	    href : '../jsp_taobao/taobaoDeails.html',
	    buttons:[{
			text:'保存',
			handler:function(){
				saveTaobao();
			}
		},{
			text:'返回',
			handler:function(){
				$("#dialogFormDiv_setTaobaoDeails").dialog("destroy");
			}
		}],
		onLoad : function(){
			if (flag == "add") {
				editTaobao();
			}else {
				$.ajax({
					url : "../TaobaoWS/getTaobaoAllInfo.do",
					type : "POST",
					data : {
						baseInfo : numberCode
					},
					async : false,
					dataType : "json",
					success : function(data) {
						if(data.status==1){
							editTaobao(data.content);
							tempData = data.content;
						}else{
							top.$.messager.alert("警告",data.msg,"warning");
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
					}
				});
			}
			
			$("#baseInfo_number_code").val(numberCode);
			
			$('#taobao_save_button').bind('click', function(){
				 saveTaobao();  
		    }); 
			
		}
	});

}

function initTaobaoData(data){
	
	$(".subTitlesHideTr").hide();
	
	var taobaoBaseInfoObj = data.taobaoBaseInfoObj;
	var taobaoBookingRulesList	 = data.taobaoBookingRulesList;
	var taobaoCruiseItemExtObj = data.taobaoCruiseItemExtObj;
	var taobaoSaleInfoObj = data.taobaoSaleInfoObj;
	
	//baseinfo(商品基本信息)
	$("#taobao_title").html(taobaoBaseInfoObj.title);
	$("#taobao_city").html(taobaoBaseInfoObj.city);
	$("#taobao_trip_max_days").html(taobaoBaseInfoObj.tripMaxDays);
	$("#taobao_accom_nights").html(taobaoBaseInfoObj.accomNights);
	$("#taobao_prov").html(taobaoBaseInfoObj.prov);
	$("#taobao_from_locations").html(taobaoBaseInfoObj.fromLocations);
	$("#taobao_to_locations").html(taobaoBaseInfoObj.toLocations);
	
	var subTitles = taobaoBaseInfoObj.subTitles.split(',');
	var tempSubTitles = "";
	for ( var i = 0; i < subTitles.length; i++) {
		if (!xyzIsNull(subTitles[i])) {
			tempSubTitles += subTitles[i]+",";
		}
	}
	tempSubTitles = tempSubTitles.substring(0, tempSubTitles.length-1);
	$("#taobao_sub_titles_td_0").html(tempSubTitles);
	//cruiseItem(商品邮轮信息)
	$("#taobao_cruise_company").html(taobaoCruiseItemExtObj.cruiseCompany);
	$("#taobao_ship_name").html(taobaoCruiseItemExtObj.shipName);
	$("#taobao_cruise_line").html(taobaoCruiseItemExtObj.cruiseLine);
	$("#taobao_ship_up").html(taobaoCruiseItemExtObj.shipUp);
	$("#taobao_ship_down").html(taobaoCruiseItemExtObj.shipDown);
	$("#taobao_ship_fee_include_td").html(taobaoCruiseItemExtObj.shipFeeInclude);
	
	//saleInfo(商品售卖信息)
	$("#taobao_sale_type_td").html(taobaoSaleInfoObj.saleType==0?'普通':'预约');
	$("#taobao_sub_stock_span").html(taobaoSaleInfoObj.subStock==0?'拍下':'付款');
	$("#taobao_start_combo_date_td").html(taobaoSaleInfoObj.startComboDate.substring(0,10));
	$("#taobao_end_combo_date_span").html(taobaoSaleInfoObj.endComboDate.substring(0,10));
	$("#taobao_merchant_td").html(taobaoSaleInfoObj.merchant);
	$("#taobao_network_id_span").html(taobaoSaleInfoObj.networkId);
	$("#taobao_duration_span").html(taobaoSaleInfoObj.duration);
	$("#taobao_has_discount_span").html(taobaoSaleInfoObj.hasDiscount=='true'?'是':'否');
	$("#taobao_has_showcase_span").html(taobaoSaleInfoObj.hasShowcase=='true'?'是':'否');
	$("#taobao_support_onsale_auto_refund_span").html(taobaoSaleInfoObj.supportOnsaleAutoRefund=='true'?'是':'否');
	$("#taobao_has_invoice_span").html(taobaoSaleInfoObj.hasInvoice=='true'?'是':'否');
	
	var sellerCids = taobaoSaleInfoObj.sellerCids.split(',');
	
	for ( var i = 0; i < sellerCids.length; i++) {
		$("#taobao_seller_cids_only").append('<span>'+sellerCids[i]+'</span>&nbsp;&nbsp;');
	}
	
	//booking(商品规则信息：费用、预约。。。)
	var html = '';
	for ( var i = 0; i < taobaoBookingRulesList.length; i++) {
		var ruleType = taobaoBookingRulesList[i].ruleType == 'Fee_Included'?'费用包含':'预定须知';
		html += '<tr class="taobao_rule_tr_0">';
		html +=	' <td style="text-align: right;">预定规则类型:</td>';
		html +=	' <td colspan="2">';
		html +=	'  <span class="taobao_rule_type_class">'+ruleType+'</span>';
		html +=	' </td>';
		html +=	'</tr>';
		html +=	'<tr class="taobao_rule_tr_1">';
		html +=	' <td style="text-align: right;">预定规则描述:</td>';
		html +=	' <td style="width:100px;">';
		html +=	'  <span class="taobao_rule_desc_class">'+taobaoBookingRulesList[i].ruleDesc+'</span>';
		html +=	' </td>';
		html +=	'</tr>';
	}
	$("#taobao_book_rule").after(html);
	
}

function taobaoEvent(){
	
	$('.taobao_seller_cids').combobox({    
		required:true,
		url:'../ListWS/getCidByChannel.do',
		onBeforeLoad: function(param){
			param.channel = $("#channelHidden").val();
		}   
	});  

	$('.addSeller_cids').linkbutton({    
	    iconCls: 'icon-add'   
	});  
	
	$('.deleteSeller_cids').linkbutton({    
		iconCls: 'icon-no'   
	}); 
	
	$('.deletetaobao_rule').linkbutton({    
		iconCls: 'icon-no'   
	}); 
	
	$('.addtaobao_sku').linkbutton({    
	    iconCls: 'icon-add'   
	});  
	
	$('.deletetaobao_sku').linkbutton({    
		iconCls: 'icon-no'   
	}); 
	
	$('.addtaobao_sku_details').linkbutton({    
		iconCls: 'icon-add'   
	});  
	
	$('.deletetaobao_sku_details').linkbutton({    
		iconCls: 'icon-no'   
	}); 
	
	$(".addSeller_cids").click(function () {
		var tempIndex = (parseInt($(".taobao_seller_cids_tr:last").prop('id').split('_')[4])+1);
		if (isNaN(tempIndex)) {
			tempIndex = 1;
		}
		var html = '<tr class="taobao_seller_cids_tr" id="taobao_seller_cides_tr_'+tempIndex+'">';
		html +=	' <td>	</td>';
		html +=	' <td>';
		html +=	'  <input value="" class="taobao_seller_cids" id="taobao_seller_cids_'+tempIndex+'" style="width:250px;"/>';
		html += '  <a class="deleteSeller_cids" id="deleteSeller_cids_'+tempIndex+'" title="减一项" style="cursor:pointer;"></a>';
		html += ' </td>';
		html +=	'</tr>';
		$(".taobao_seller_cids_tr:last").after(html);
		
		xyzCombobox ({
			required:true,
			combobox:'taobao_seller_cids_'+tempIndex,
			url:'../ListWS/getCidByChannel.do',
			onBeforeLoad: function(param){
				param.channel = $("#channelHidden").val();
			}
		});
		
		$('#deleteSeller_cids_'+tempIndex).linkbutton({    
			iconCls: 'icon-no'   
		});
		$('#deleteSeller_cids_'+tempIndex).click(function () {
			var tempIndex = $(this).prop('id').split('_')[2];
			$("#taobao_seller_cides_tr_"+tempIndex).remove();
			
		});
	});
	
	$(".deleteSeller_cids").click(function () {
		var tempIndex = $(this).prop('id').split('_')[2];
		$("#taobao_seller_cides_tr_"+tempIndex).remove();
		
	});
	
}

function editTaobao(data){
	$(".subTitlesHideTr").show();
		
	var addHtml = '<tr>';
	addHtml += '<td style="text-align:right;">邮轮: </td>';
	if(xyzIsNull(data)) {
		addHtml += '<td>';
		addHtml += ' <input id="cruise" style="width:200px;"/>';
		addHtml += '</td>';
		addHtml += '<td style="text-align:right;">渠道: </td>';
		addHtml += '<td>';
		addHtml += ' <input id="taobaoChannel" style="width:200px;"/>';
	}else{
		addHtml += '<td colspan="3" style="text-align: center;">';
		addHtml += ' <input id="cruise" style="width:538px;"/>';
	}
	addHtml += '</td>';
	addHtml += '</tr>';
	$("#add").after(addHtml);
	
	xyzCombobox({
		combobox : 'cruise',
		url : '../ListWS/getShipmentCruiseList.do',
		mode: 'remote',
		lazy : false,
		required : true,  
		onChange : function(newValue,oldValue){
			if(newValue != oldValue){
				getCruiseData(newValue);
			}
		}
	});
	
	if(!xyzIsNull(data)) {
		$("#cruise").combobox('setValue',data.taobaoBaseInfoObj.cruise);
	}else{
		xyzCombobox({
			combobox : 'taobaoChannel',
			url : '../ListWS/getChannelList.do',
			mode: 'remote',
			lazy : false,
			required: true,
			onBeforeLoad: function(param){
				param.isMeituan = "0";
			},
			onChange: function(newValue, oldValue){
				$("#channelHidden").val(newValue);
			}
		});
	}
	
	var taobaoBaseInfoObj = "";
	var taobaoBookingRulesList	 = "";
	var taobaoCruiseItemExtObj = "";
	var taobaoSaleInfoObj = "";
	var taobaoTravelItemObj = "";
	
	if (!xyzIsNull(data)) {
		taobaoBaseInfoObj = data.taobaoBaseInfoObj;
		taobaoBookingRulesList	 = data.taobaoBookingRulesList;
		taobaoCruiseItemExtObj = data.taobaoCruiseItemExtObj;
		taobaoSaleInfoObj = data.taobaoSaleInfoObj;
		taobaoTravelItemObj = data.taobaoTravelItemObj;
	}
	
	$("#channelHidden").val(taobaoTravelItemObj.channle);
	
	var title = "";
	var prov = "";
	var city = "";
	var tripMaxDays = "";
	var accomNights = "";
	var fromLocations = "";
	var toLocations = "";
	
	if (!xyzIsNull(taobaoBaseInfoObj)) {
		title = taobaoBaseInfoObj.title;
		city = taobaoBaseInfoObj.city;
		tripMaxDays = taobaoBaseInfoObj.tripMaxDays;
		accomNights = taobaoBaseInfoObj.accomNights;
		prov = taobaoBaseInfoObj.prov;
		fromLocations = taobaoBaseInfoObj.fromLocations;
		toLocations = taobaoBaseInfoObj.toLocations;
	}
	
	//宝贝标题
	var titleHtml = '<input type="text" id="taobao_title_from" style="width:538px;" value="'+title+'"/>';
	$("#taobao_title").html(titleHtml);
	$("#taobao_title_from").validatebox({    
	    required: true
	});  
	
	//宝贝省份
	var provHtml = '<input type="text" id="taobao_prov_from" style="width:200px;" value="'+prov+'"/>';
	$("#taobao_prov").html(provHtml);
	$("#taobao_prov_from").validatebox({    
	    required: true
	}); 
	
	//宝贝城市
	var cityHtml = '<input type="text" id="taobao_city_from" style="width:200px;" value="'+city+'"/>';
	$("#taobao_city").html(cityHtml);
	$("#taobao_city_from").validatebox({    
	    required: true
	}); 
	
	//几天
	var dayHtml = '<input type="text" id="taobao_trip_max_days_from" style="width:80px;" value="'+tripMaxDays+'"/>';
	$("#taobao_trip_max_days").html(dayHtml);
	$('#taobao_trip_max_days_from').numberbox({    
	    min:0,
	    max:9999999,
	    required: true
	});  
	
	//几晚
	var nightHtml = '<input type="text" id="taobao_accom_nights_from" style="width:80px;" value="'+accomNights+'"/>';
	$("#taobao_accom_nights").html(nightHtml);
	$('#taobao_accom_nights_from').numberbox({    
	    min:0,
	    max:9999999,
	    required: true
	}); 
	
	//出发地
	var fromLocationHtml = '<input type="text" id="taobao_from_locations_from"';
	fromLocationHtml += ' style="width:200px;" value="'+fromLocations+'"/>';
	$("#taobao_from_locations").html(fromLocationHtml);
	$("#taobao_from_locations_from").validatebox({    
	    required: true
	});
	
	//目的地
	var toLocationHtml = '<input type="text" id="taobao_to_locations_from" style="width:120px;"';
	toLocationHtml += ' value="'+toLocations+'"/>(","号隔开)';
	$("#taobao_to_locations").html(toLocationHtml);
	$("#taobao_to_locations_from").validatebox({    
	    required: true
	});
	
	//商品亮点
	var subTitles = [""];
	if (!xyzIsNull(taobaoBaseInfoObj)) {
		subTitles = taobaoBaseInfoObj.subTitles.split(',');
	}
	for (var i = 0; i < subTitles.length; i++) {	
		var sub_titleHtml = '<textarea rows="3" cols="74" id="taobao_sub_titles" class="taobao_sub_titles" style="width:538px;">'+subTitles[i]+'</textarea>';
		$("#taobao_sub_titles_td_"+i).html(sub_titleHtml);
	}
	
	var cruiseCompany = "";
	var shipName = "";
	var cruiseLine = "";
	var shipUp = "";
	var shipDown = "";
	var shipFeeInclude = "";
	
	if (!xyzIsNull(taobaoCruiseItemExtObj)) {
		cruiseCompany = taobaoCruiseItemExtObj.cruiseCompany;
		shipName = taobaoCruiseItemExtObj.shipName;
		cruiseLine = taobaoCruiseItemExtObj.cruiseLine;
		shipUp = taobaoCruiseItemExtObj.shipUp;
		shipDown = taobaoCruiseItemExtObj.shipDown;
		shipFeeInclude = taobaoCruiseItemExtObj.shipFeeInclude;
	}
	
	//邮轮公司
	var companyHtml = '<input type="text" id="taobao_cruise_company_from" style="width:200px;" value="'+cruiseCompany+'"/>';
	$("#taobao_cruise_company").html(companyHtml);
	$("#taobao_cruise_company_from").validatebox({    
	    required: true
	}); 
	
	//邮轮名称
	var cruiseHtml = '<input type="text" id="taobao_ship_name_from" style="width:200px;" value="'+shipName+'"/>';
	$("#taobao_ship_name").html(cruiseHtml);
	$("#taobao_ship_name_from").validatebox({    
	    required: true
	}); 
	
	//邮轮航线
	var airwayHtml = '<input type="text" id="taobao_cruise_line_from" style="width:538px;" value="'+cruiseLine+'"/>';
	$("#taobao_cruise_line").html(airwayHtml);
	$("#taobao_cruise_line_from").validatebox({    
	    required: true
	});
	
	//上船地点
	var upHtml = '<input type="text" id="taobao_ship_up_from" style="width:200px;" value="'+shipUp+'"/>';
	$("#taobao_ship_up").html(upHtml);
	$("#taobao_ship_up_from").validatebox({    
	    required: true
	});
	
	//下船地点
	var downHtml = '<input type="text" id="taobao_ship_down_from" style="width:200px;" value="'+shipDown+'"/>';
	$("#taobao_ship_down").html(downHtml);
	$("#taobao_ship_down_from").validatebox({    
	    required: true
	});
	
	//邮轮费用包含
	var ship_feeHtml = '<input type="text" id="taobao_ship_fee_include_from" style="width:538px;" value="'+shipFeeInclude+'"/>';
	$("#taobao_ship_fee_include_td").html(ship_feeHtml);
	$("#taobao_ship_fee_include_from").validatebox({    
	    required: true
	});
	
	//saleInfo
	$("#taobao_sale_type_combobox").combobox({
		required:true,
		editable:false
	});

	var saleType = "";
	if (!xyzIsNull(taobaoSaleInfoObj)) {
		saleType = taobaoSaleInfoObj.saleType;
	}
	//$("#taobao_sale_type_combobox").combobox('select',saleType);
	$("#taobao_sale_type_combobox").combobox({
		value : saleType
	});
	
	var subStock = "";
	if (!xyzIsNull(taobaoSaleInfoObj)) {
		subStock = taobaoSaleInfoObj.subStock;
	}
	$("#taobao_sub_stock_combobox").combobox('select',subStock);
	$('#taobao_sub_stock_combobox').combobox({    
		required:true,
		editable:false
	}); 
	
	$('#taobao_start_combo_date_datebox').datetimebox({    
	    required:true
	}); 
	
	var startComboDate = "";
	if (!xyzIsNull(taobaoSaleInfoObj)) {
		startComboDate = taobaoSaleInfoObj.startComboDate;
	}
	$('#taobao_start_combo_date_datebox').datetimebox('setValue',startComboDate);
	
	$('#taobao_end_combo_date_datebox').datetimebox({    
	    required:true
	});
	
	var endComboDate = "";
	if (!xyzIsNull(taobaoSaleInfoObj)) {
		endComboDate = taobaoSaleInfoObj.endComboDate;
	}
	$('#taobao_end_combo_date_datebox').datetimebox('setValue',endComboDate);
	
	$("#taobao_duration_from").numberbox('setValue',taobaoSaleInfoObj.duration);    
	$("#taobao_duration_from").numberbox({    
	    min:0,
	    max:9999999,
	    required: true
	}); 
	
	$('#taobao_merchant').val(taobaoSaleInfoObj.merchant);
	$('#taobao_merchant').validatebox({    
	    required: false 
	});
	
	$('#taobao_network_id').val(taobaoSaleInfoObj.networkId);
	$('#taobao_network_id').validatebox({    
	    required: false 
	});
	
	var hasDiscount = "";
	var hasShowcase = "";
	var supportOnsaleAutoRefund = "";
	var hasInvoice = "";
	
	if (!xyzIsNull(taobaoSaleInfoObj)) {
		hasDiscount = taobaoSaleInfoObj.hasDiscount;
		hasShowcase = taobaoSaleInfoObj.hasShowcase;
		supportOnsaleAutoRefund = taobaoSaleInfoObj.supportOnsaleAutoRefund;
		hasInvoice = taobaoSaleInfoObj.hasInvoice;
	}
	
	$("#taobao_has_discount_span").html('<input type="checkbox" id="taobao_has_discount"/>');
	if (hasDiscount == 'true') {
		$("#taobao_has_discount").prop('checked',true);
	}else {
		$("#taobao_has_discount").prop('checked',false);
	}
	
	$("#taobao_has_showcase_span").html('<input type="checkbox" id="taobao_has_showcase"/>');
	if (hasShowcase == 'true') {
		$("#taobao_has_showcase").prop('checked',true);
	}else {
		$("#taobao_has_showcase").prop('checked',false);
	}
	
	$("#taobao_support_onsale_auto_refund_span").html('<input type="checkbox" id="taobao_support_onsale_auto_refund" />');
	if (supportOnsaleAutoRefund=='true') {
		$("#taobao_support_onsale_auto_refund").prop('checked',true);
	}else {
		$("#taobao_support_onsale_auto_refund").prop('checked',false);
	}
	
	$("#taobao_has_invoice_span").html('<input type="checkbox" id="taobao_has_invoice"/>');
	if (hasInvoice=='true') {
		$("#taobao_has_invoice").prop('checked',true);
	}else {
		$("#taobao_has_invoice").prop('checked',false);
	}
	
	var sellerCids = [""];
	
	if (!xyzIsNull(taobaoSaleInfoObj)) {
		sellerCids = taobaoSaleInfoObj.sellerCids.split(',');
	}
	$("#taobao_seller_cids_only").html('');
	for ( var i = 0; i < sellerCids.length; i++) {
		if (i == 0) {
			var html = "";
				html = '<input value="'+sellerCids[i]+'" class="taobao_seller_cids"';
				html += ' id="taobao_seller_cids_'+i+'" style="width:250px;"/>&nbsp;';
				html += '<a class="addSeller_cids" id="addSeller_cids_0"  title="加一项" style="cursor:pointer;" href="javascript:;"></a>';
			 
			$("#taobao_seller_cides_one_td").html(html);
		}else {
			var html = "";
				html = '<tr class="taobao_seller_cids_tr" id="taobao_seller_cides_tr_'+i+'">';
				html += ' <td></td>';
				html += ' <td>';
				html += '  <input value="'+sellerCids[i]+'" class="taobao_seller_cids" ';
				html += '   id="taobao_seller_cids_'+i+'" style="width:250px;"/>&nbsp;';
				html += '  <a class="deleteSeller_cids" id="deleteSeller_cids_'+i+'" title="减一项" style="cursor:pointer;"></a>';
				html += ' </td>';
				html += '</tr>';
				
			$(".taobao_seller_cids_tr:last").after(html);
		}
	}
	
	//booking
	var html = '';
	$(".taobao_rule_tr_0").remove();
	$(".taobao_rule_tr_1").remove();
	if (xyzIsNull(taobaoBookingRulesList)) {
		taobaoBookingRulesList = [""];
	}
	
	var bookingRulesLength = taobaoBookingRulesList.length < 3 ? 3 : taobaoBookingRulesList.length;
	for ( var i = 0; i < bookingRulesLength; i++) {
		var ruleType = ""; 
		if (!xyzIsNull(taobaoBookingRulesList[i])) {
			ruleType = taobaoBookingRulesList[i].ruleType; 
		}
		
		if (!xyzIsNull(ruleType)) {
			if (ruleType == "Fee_Included") {
				$("#taobao_rule_desc_fee_Included").val(taobaoBookingRulesList[i].ruleDesc);
			}else if (ruleType == "Fee_Excluded") {
				$("#taobao_rule_desc_fee_Excluded").val(taobaoBookingRulesList[i].ruleDesc);
			}else if (ruleType == "Order_Info") {
				$("#taobao_rule_desc_order_Info").val(taobaoBookingRulesList[i].ruleDesc);
			}
		}
	}
	
	$('#taobao_rule_desc_fee_Included').validatebox({    
	    required: true
	});  
	$('#taobao_rule_desc_fee_Excluded').validatebox({    
		required: true
	});  
	$('#taobao_rule_desc_order_Info').validatebox({    
		required: true
	});  

	//suk
	$(".taobao_sku_title_tr").remove();
	$(".taobao_sku_tr").remove();
	
	taobaoEvent();
	
	$(".deletetaobao_sku").click(function () {
		
		var tempTitleId = $(this).prop('id').split('_');
		
		$("tr[id^='sku_title_"+tempTitleId[2]+"_']").remove();
		$("tr[id^='sku_details_"+tempTitleId[2]+"_']").remove();
		
	});
}

function changeSaleInput(){
	
	var flag = true;

	var taobaoSaleType = $("#taobao_sale_type_combobox").combobox('getValue');
	var taobaoSubStock = $("#taobao_sub_stock_combobox").combobox('getValue');
	var taobaoStartComboDate = $("#taobao_start_combo_date_datebox").datetimebox('getValue');
	var taobaoEndComboDate = $("#taobao_end_combo_date_datebox").datetimebox('getValue');
	var taobaoDuration = $("#taobao_duration_from").numberbox('getValue');
	var taobaoMerchant = $("#taobao_merchant_from").val();
	var taobaoNetworkId = $("#taobao_network_id").val();
	
	if (!xyzIsNull(taobaoSaleType)) {
		flag = false;
	}else if (!xyzIsNull(taobaoSubStock)) {
		flag = false;
	}else if (!xyzIsNull(taobaoStartComboDate)) {
		flag = false;
	}else if (!xyzIsNull(taobaoEndComboDate)) {
		flag = false;
	}else if (!xyzIsNull(taobaoDuration)) {
		flag = false;
	}else if (!xyzIsNull(taobaoMerchant)) {
		flag = false;
	}else if (!xyzIsNull(taobaoNetworkId)) {
		flag = false;
	}
	
	if (flag) {
		$("#taobao_sale_type_combobox").combobox({
			required:false
		});
		$("#taobao_sub_stock_combobox").combobox({
			required:false
		});
		$('#taobao_start_combo_date_datebox').datetimebox({    
		    required:false   
		});
		$('#taobao_end_combo_date_datebox').datetimebox({    
		    required:false   
		});
		$("#taobao_duration_from").numberbox({    
		    min:0,
		    max:9999999,
		    required: false 
		}); 
		$('#taobao_merchant_from').validatebox({    
		    required: false 
		});
		
		$('#taobao_network_id_from').validatebox({    
		    required: false 
		});
	}else {
		$("#taobao_sale_type_combobox").combobox({
			required:true
		});
		$("#taobao_sub_stock_combobox").combobox({
			required:true
		});
		$('#taobao_start_combo_date_datebox').datetimebox({    
		    required:true   
		});
		$('#taobao_end_combo_date_datebox').datetimebox({    
		    required:true   
		});
		$("#taobao_duration_from").numberbox({    
		    min:0,
		    max:9999999,
		    required: true 
		}); 
	}
	
}

function getCruiseData(cruise){
	
	$.ajax({
		url : '../TaobaoWS/getCruiseData.do',
		type : "POST",
		data : {
			cruise : cruise
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				var content = data.content;
				var cruise = content.cruise;
				var travel = content.travel;
				var end = content.end;
	
				if(!xyzIsNull(cruise)) {
					$("#taobao_start_combo_date_datebox").datetimebox('setValue',travel);
					$("#taobao_end_combo_date_datebox").datetimebox('setValue',end);
					$("#taobao_cruise_company_from").val(cruise.companyNameCn);
					$("#taobao_ship_name_from").val(cruise.nameCn);
					$('#taobao_cruise_company_from').validatebox({    
						required: true 
					});
					$('#taobao_ship_name_from').validatebox({    
						required: true 
					});
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

function addtaobaoSku(){
	
	var tempTitleId = $(".sku_title_class:last").prop('id').split('_');
	
	var skuHtml = '';
	
	skuHtml += '<tr id="sku_title_'+(parseInt(tempTitleId[2])+1)+'_0" class="sku_title_class">';
	skuHtml += '<td style="width: 76px;"><input type="text"/></td>';
	skuHtml += '<td>&nbsp;<a class="addtaobao_sku" id="addtaobao_sku_'+(parseInt(tempTitleId[2])+1)+'_0" onclick="addtaobaoSku()"  title="加一项" style="cursor:pointer;"></a>';
	skuHtml += '<a class="deletetaobao_sku" id="deletetaobao_sku_'+(parseInt(tempTitleId[2])+1)+'_0" title="减一项" style="cursor:pointer;"></a>';
	skuHtml += '</td></tr>';
	
	skuHtml += '<tr class="taobao_sku_tr_'+(parseInt(tempTitleId[2])+1)+'" id="sku_details_'+(parseInt(tempTitleId[2])+1)+'_0">';
	skuHtml += '<td style="text-align: right;">日期</td>';
	skuHtml += '<td class="taobao_sku_class" style="width: 173px;"><input class="taobao_date" id="taobao_date_'+(parseInt(tempTitleId[2])+1)+'_0"/></td>';
	skuHtml += '<td colspan="6">';
	skuHtml += '价格类型:';
	skuHtml += '<select id="sku_type_combobox_'+(parseInt(tempTitleId[2])+1)+'_0" class="easyui-combobox" name="dept" style="width:200px;">  '; 
	skuHtml += '<option value="1">成人</option>';   
	skuHtml += '<option value="2">儿童</option>';   
	skuHtml += '<option value="3">单房差</option>';   
	skuHtml += '</select>';
	skuHtml += '价格<input class="taobao_price_class" id="taobao_price_'+(parseInt(tempTitleId[2])+1)+'_0" />';
	skuHtml += '库存<input class="taobao_stock_class" id="taobao_stock_'+(parseInt(tempTitleId[2])+1)+'_0" />';
	skuHtml += '&nbsp;<a class="addtaobao_sku_details" onclick="addSkuDetails('+(parseInt(tempTitleId[2])+1)+')" id="addtaobao_sku_0"  title="加一项" style="cursor:pointer;"></a>';
	skuHtml += '<a class="deletetaobao_sku_details" onclick="deleteSkuDetails('+(parseInt(tempTitleId[2])+1)+')" id="deletetaobao_sku_0" title="减一项" style="cursor:pointer;"></a>';
	skuHtml += '</td> ';
	skuHtml += '</tr>';
	
	$("#sku_details_"+tempTitleId[2]+"_"+tempTitleId[3]).after(skuHtml);
	
	$('.addtaobao_sku').linkbutton({    
		iconCls: 'icon-add'   
	});  
	
	$('.deletetaobao_sku').linkbutton({    
		iconCls: 'icon-no'   
	}); 
	
	$('.addtaobao_sku_details').linkbutton({    
		iconCls: 'icon-add'   
	});  
	
	$('.deletetaobao_sku_details').linkbutton({    
		iconCls: 'icon-no'   
	}); 
	
	$('#taobao_date_'+(parseInt(tempTitleId[2])+1)+'_0').datebox({    
	    required:true   
	}); 
	
	$('#taobao_price_'+(parseInt(tempTitleId[2])+1)+'_0').numberbox({    
	    min:0,    
	    precision:2    
	});  

	$('#taobao_stock_'+(parseInt(tempTitleId[2])+1)+'_0').numberbox({    
	    min:0
	});
	
	$('#sku_type_combobox_'+(parseInt(tempTitleId[2])+1)+'_0').combobox({
		 required:true
	});
	
	$(".deletetaobao_sku").click(function () {
		
		var tempTitleId = $(this).prop('id').split('_');
		
		$("tr[id^='sku_title_"+tempTitleId[2]+"_']").remove();
		$("tr[id^='sku_details_"+tempTitleId[2]+"_']").remove();
		
	});
	
}

function addSkuDetails(id){
	
	var tempIndex = $(".taobao_sku_tr_"+id).length;
	
	skuHtml = '<tr class="taobao_sku_tr_'+id+'" id="sku_details_'+id+'_'+tempIndex+'">';
	skuHtml += '<td style="text-align: right;">日期</td>';
	skuHtml += '<td class="taobao_sku_class" style="width: 173px;"><input class="taobao_date" id="taobao_date_'+id+'_'+tempIndex+'" /></td>';
	skuHtml += '<td colspan="6">';
	skuHtml += '价格类型:';
	skuHtml += '<select id="sku_type_combobox_'+id+'_'+tempIndex+'" class="easyui-combobox" name="dept" style="width:200px;">  '; 
	skuHtml += '<option value="1">成人</option>';   
	skuHtml += '<option value="2">儿童</option>';   
	skuHtml += '<option value="3">单房差</option>';   
	skuHtml += '</select>';
	skuHtml += '价格<input id="taobao_price_'+id+'_'+tempIndex+'" />';
	skuHtml += '库存<input id="taobao_stock_'+id+'_'+tempIndex+'" />';
	skuHtml += '</td> ';
	skuHtml += '</tr>';
	
	$(".taobao_sku_tr_"+id).eq(tempIndex-1).after(skuHtml);
	
	$('#taobao_date_'+id+'_'+tempIndex).datebox({    
	    required:true   
	}); 
	
	$('#taobao_price_'+id+'_'+tempIndex).numberbox({    
	    min:0,    
	    precision:2    
	});  

	$('#taobao_stock_'+id+'_'+tempIndex).numberbox({    
	    min:0
	});
	
	$('#sku_type_combobox_'+id+'_'+tempIndex).combobox({
		 required:true
	});
}

function deleteSkuDetails(id){
	
	var tempIndex = $(".taobao_sku_tr_"+id).length;
	
	$(".taobao_sku_tr_"+id).eq(tempIndex-1).remove();
	
}

//保存
function saveTaobao(){
	if(!$("form").form('validate')){
		return false;
	}
	
	var taobaoTripMaxDays = $("#taobao_trip_max_days_from").numberbox('getValue');
	var taobaoAccomNights = $("#taobao_accom_nights_from").numberbox('getValue');
	var taobaoTitle = $("#taobao_title_from").val();
	var taobaoCity = $("#taobao_city_from").val();
	var taobaoProv = $("#taobao_prov_from").val();
	var taobaoFromLocations = $("#taobao_from_locations_from").val();
	var taobaoToLocations = $("#taobao_to_locations_from").val();
	
	var taobaoSubTitles = "";
	$(".taobao_sub_titles").each(function(){
		taobaoSubTitles += $(this).val()+",";
	});
	taobaoSubTitles = taobaoSubTitles.substring(0, taobaoSubTitles.length-1);
	
	var base = {
			"taobaoTitle" : taobaoTitle,
			"taobaoCity" : taobaoCity,
			"taobaoProv" : taobaoProv,
			"taobaoFromLocations" : taobaoFromLocations,
			"taobaoToLocations" : taobaoToLocations,
			"taobaoTripMaxDays" : taobaoTripMaxDays,
			"taobaoAccomNights" : taobaoAccomNights,
			"taobaoSubTitles" : taobaoSubTitles
	};
	
	var taobaoCruiseCompany = $("#taobao_cruise_company_from").val();
	var taobaoShipName = $("#taobao_ship_name_from").val();
	var taobaoCruiseLine = $("#taobao_cruise_line_from").val();
	var taobaoShipUp = $("#taobao_ship_up_from").val();
	var taobaoShipDown = $("#taobao_ship_down_from").val();
	var taobaoShipFeeInclude = $("#taobao_ship_fee_include_from").val();
	
	var cruiseInfo = {
		"taobaoCruiseCompany" : taobaoCruiseCompany,
		"taobaoShipName" : taobaoShipName,
		"taobaoCruiseLine" : taobaoCruiseLine,
		"taobaoShipUp" : taobaoShipUp,
		"taobaoShipDown" : taobaoShipDown,
		"taobaoShipFeeInclude" : taobaoShipFeeInclude
	};
	
	var taobaoSaleType = $("#taobao_sale_type_combobox").combobox('getValue');
	var taobaoSubStock = $("#taobao_sub_stock_combobox").combobox('getValue');
	var taobaoStartComboDate = $("#taobao_start_combo_date_datebox").datetimebox('getValue');
	var taobaoEndComboDate = $("#taobao_end_combo_date_datebox").datetimebox('getValue');
	
	if(taobaoEndComboDate < taobaoStartComboDate){
		return top.$.messager.alert("提示","结束日期不能小于开始日期");
	}
	
	var taobaoMerchant = $("#taobao_merchant").val();
	var taobaoNetworkId = $("#taobao_network_id").val();
	var taobaoDuration = $("#taobao_duration_from").numberbox('getValue');
	var taobaoSellerCids = "";
	$(".taobao_seller_cids").each(function(){
		taobaoSellerCids += xyzIsNull($(this).combobox('getValue'))==true?$(this).combobox('getText'):$(this).combobox('getValue');
	});
	taobaoSellerCids = taobaoSellerCids.substring(0, taobaoSellerCids.length-1);
	
	var taobaoHasDiscount = $("#taobao_has_discount").prop('checked')==true?1:0;
	var taobaoHasShowcase = $("#taobao_has_showcase").prop('checked')==true?1:0;
	var taobaoSupportOnsaleAutoRefund = $("#taobao_support_onsale_auto_refund").prop('checked')==true?1:0;
	var taobaoHasInvoice = $("#taobao_has_invoice").prop('checked')==true?1:0;
	
	var saleInfo = {
		"taobaoSaleType" : taobaoSaleType,
		"taobaoSubStock" : taobaoSubStock,
		"taobaoStartComboDate" : taobaoStartComboDate,
		"taobaoEndComboDate" : taobaoEndComboDate,
		"taobaoMerchant" : taobaoMerchant,
		"taobaoNetworkId" : taobaoNetworkId,
		"taobaoDuration" : taobaoDuration,
		"taobaoSellerCids" : taobaoSellerCids,
		"taobaoHasDiscount" : taobaoHasDiscount,
		"taobaoHasShowcase" : taobaoHasShowcase,
		"taobaoSupportOnsaleAutoRefund" : taobaoSupportOnsaleAutoRefund,
		"taobaoHasInvoice" : taobaoHasInvoice,
	};
	
	var rules = [];
	var taobaoRuleDescFeeIncluded = $("#taobao_rule_desc_fee_Included").val(); //费用包含
	var taobaoRuleDescFeeExcluded = $("#taobao_rule_desc_fee_Excluded").val(); //费用不包含
	var taobaoRuleDescOrderInfo = $("#taobao_rule_desc_order_Info").val(); //预定须知
	
	var tempRule = {"taobaoRule" : "Fee_Included",
	                "taobaoRuleDesc" : taobaoRuleDescFeeIncluded
	};
	rules[rules.length] = tempRule;
	
	tempRule = {"taobaoRule" : "Fee_Excluded",
			"taobaoRuleDesc" : taobaoRuleDescFeeExcluded
	};
	rules[rules.length] = tempRule;
	
	tempRule = {"taobaoRule" : "Order_Info",
			"taobaoRuleDesc" : taobaoRuleDescOrderInfo
	};
	rules[rules.length] = tempRule;
	
	var baseinfoNumberCode = $("#baseInfo_number_code").val();
	
	base = JSON.stringify(base);
	cruiseInfo = JSON.stringify(cruiseInfo);
	saleInfo = JSON.stringify(saleInfo);
	rules = JSON.stringify(rules);
	
	var channel = "";
	if ($("#taobaoChannel").length > 0) {
		channel = $("#taobaoChannel").combobox('getValue');
	}
	var cruise = $("#cruise").combobox('getValue');
	
	$.ajax({
		url : '../TaobaoWS/saveTaobaoItem.do',
		type : "POST",
		data : {
			baseInfo : baseinfoNumberCode,
			baseJsonStr : base,
			cruiseInfoJsonStr : cruiseInfo,
			saleInfoJsonStr : saleInfo,
			rulesJsonStr : rules,
			channel : channel,
			cruise : cruise
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#taobaoManagerTable").datagrid("reload");
				$("#dialogFormDiv_setTaobaoDeails").dialog("destroy");
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

//宝贝克隆
function copyTaobaoTravelItem(){
	var taobaoTravelItem = $("#taobaoManagerTable").datagrid("getSelections")[0];
	if (taobaoTravelItem == undefined) {
		top.$.messager.alert("提示","请选择宝贝!","info");
		return;
	}
	
	var travelItem = taobaoTravelItem.taobaoTravelItem;
	var html = '<form><center><br/>';
	html += '<input type="hidden" id="taobaoTravelItem" value="'+ travelItem +'"/>';
	html += '<table id="skuTable" border="1" cellpadding="2" cellspacing="0" style="text-align:center;width:850px;">';
	html += '</table>';
	
	xyzdialog({
		dialog:'dialogFormDiv_copyTaobaoTravelItem',
		title : "宝贝克隆",
		content : html,
		buttons:[{
			text:'确定',
			handler:function(){
				copyTaobaoTravelItemSubmit(travelItem);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_copyTaobaoTravelItem").dialog("destroy");
			}
		}],
		onOpen : function() {
			xyzAjax({
				url:'../TaobaoWS/getTaobaoItemByNumberCode.do',
				data : {
					numberCode : travelItem
				},
				success:function(data){
					if (data.status == 1) {
						var skuList = data.content.skuList;
						var stockList = data.content.stockList;
						var skuHtml = '';
						skuHtml += '<caption>';
						skuHtml += ' 克隆 <span style="font-size:16px;font-weight:bold;">【'+taobaoTravelItem.channelNameCn+'】【'+ taobaoTravelItem.title +'】</span>到<br/><br/>';
						skuHtml += ' 渠道 <input type="text" id="channelForm" style="width:460px;"/><br/><br/>';
						skuHtml += '</caption>';
						skuHtml += '<thead>';
						skuHtml += ' <tr>';
						skuHtml += '  <th style="width:240px;font-size:14px;">SKU</th>';
						skuHtml += '  <th colspan="5" style="font-size:14px;height:18px;">';
						skuHtml += '    SKU库存<span style="color:red;">(温馨提示:<img src="../image/other/unlock.png" style="width:16px;height:16px;"/>复制,<img src="../image/other/lock.png" style="width:16px;height:16px;"/>不复制)</span>';
						skuHtml += '  </th>';
						skuHtml += ' </tr>';
						skuHtml += '</thead>';
						skuHtml += '<tbody>';
						for(var k = 0; k < skuList.length; k++){
							var skuNumberCode = skuList[k].numberCode;
							var packageName = skuList[k].packageName;
							
							var skuStockList = stockList[skuNumberCode];
							skuHtml += ' <tr id="skuTr_'+skuNumberCode+'">';
							skuHtml += '  <th rowspan="'+ (skuStockList.length+1) +'">';
							skuHtml += '   <input type="hidden" id="skuNumberCode_'+skuNumberCode+'" value="'+ skuNumberCode +'"/>';
							skuHtml += '   <span style="width:240px;">'+ packageName +'</span>';
							skuHtml += '  </th>';
							skuHtml += '  <th style="width:180px;">时间</th>';
							skuHtml += '  <th style="width:130px;">库存类型</th>';
							skuHtml += '  <th style="width:100px;">库存</th>';
							skuHtml += '  <th style="width:100px;">价格</th>';
							skuHtml += '  <th style="width:100px;height:28px;text-align:center;" >';
							
							if(skuStockList.length+1 > 1){
								skuHtml += '   <span style="float:left;margin-left:9px;margin-top:5px;">操作</span>';
								skuHtml += '   <span style="float:left;">';
								skuHtml += '    <img src="../image/other/unlock.png" title="点我不复制宝贝及库存" ';
								skuHtml += '     id="skuImg_'+skuNumberCode+'" ';
								skuHtml += '    onclick="operateImg(\''+skuNumberCode+'\',\'复制\')" >';
								skuHtml += '    <input id="isPass_'+skuNumberCode+'" type="hidden" value="0"/>&nbsp;';
								
								skuHtml += '    <img src="../image/other/unlock.png" title="点我锁定价格(克隆后的宝贝库存价格不能更改)" ';
								skuHtml += '    id="isLockImg_'+skuNumberCode+'" ';
								skuHtml += '    onclick="skuLockImg(\''+skuNumberCode+'\')" >';
								skuHtml += '    <input id="isLock_'+skuNumberCode+'" type="hidden" value="0"/>';
								
								skuHtml += '   </span>';
							}
							
							skuHtml += '  </th>';
							skuHtml += ' </tr>';
							for(var m = 0; m < skuStockList.length; m++){
								var number = skuStockList[m].numberCode;
								var date = skuStockList[m].date;
								var priceType = skuStockList[m].priceType;
								var type = "";
								if(priceType == 1){
									type = "成人价";
								}else if(priceType == 2){ 
									type = "儿童价";
								}else{//priceType == 3
									type = "单房差";
								}
								var price = skuStockList[m].price;
								var stock = skuStockList[m].stock;
								skuHtml += '<tr id="skuDetailTr_'+skuNumberCode+'_'+number+'">';
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
								skuHtml += ' <td style="width:100px;text-align:right;">';
								skuHtml += '  <img src="../image/other/unlock.png" title="点我不复制该宝贝库存" ';
								skuHtml += '   id="skuImg_'+skuNumberCode+'_'+number+'" ';
								skuHtml += '   onclick="operateOneImg(\''+skuNumberCode+'\',\''+number+'\',\'复制\')">';
								skuHtml += '  <input id="isPass_'+skuNumberCode+'_'+number+'" type="hidden" value="0" />&nbsp;';
								
								skuHtml += ' <img src="../image/other/unlock.png" title="点我锁定该价格(克隆后的宝贝库存价格不能更改)" ';
								skuHtml += '  id="isLockImg_'+skuNumberCode+'_'+number+'" ';
								skuHtml += '  onclick="isLockOneImg(\''+skuNumberCode+'\',\''+number+'\')" >';
								skuHtml += ' <input id="isLock_'+skuNumberCode+'_'+number+'" type="hidden" value="0"/>';
								
								skuHtml += '</td>';
								skuHtml += '</tr>';
							}
						}
						skuHtml += '</tbody>';
						$("#skuTable").html(skuHtml);
						xyzCombobox({
							required:true,
							combobox : 'channelForm',
							url : '../ListWS/getChannelList.do',
							mode: 'remote',
							lazy : false,
							onBeforeLoad: function(param){
								param.isMeituan = "";
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

function operateImg(numberCode, type){
	var isPass = $("#isPass_"+numberCode).val();
	var skuStockList = $.map($("tr[id^='skuDetailTr_"+numberCode+"_']"),
		function(p){
			return $(p).attr("id").split("_")[2];
		}
	);
	if(isPass == '0'){
		$("#skuImg_"+numberCode).attr("src","../image/other/lock.png");
		$("#skuImg_"+numberCode).attr("title","点我"+ type +"宝贝及库存");
		isPass = '1';
		$("#isPass_"+numberCode).val(isPass);
		for(var j = 0; j < skuStockList.length; j++){
			$("#skuImg_"+numberCode+"_"+skuStockList[j]).attr("src","../image/other/lock.png");
			$("#skuImg_"+numberCode+"_"+skuStockList[j]).attr("title","点我"+ type +"该宝贝库存");
			$("#isPass_"+numberCode+"_"+skuStockList[j]).val(isPass);
		}
	}else{
		$("#skuImg_"+numberCode).attr("src","../image/other/unlock.png");
		$("#skuImg_"+numberCode).attr("title","点我不"+ type +"宝贝及库存");
		isPass = '0';
		$("#isPass_"+numberCode).val(isPass);
		for(var j = 0; j < skuStockList.length; j++){
			$("#skuImg_"+numberCode+"_"+skuStockList[j]).attr("src","../image/other/unlock.png");
			$("#skuImg_"+numberCode+"_"+skuStockList[j]).attr("title","点我不"+ type +"该宝贝库存");
			$("#isPass_"+numberCode+"_"+skuStockList[j]).val(isPass);
		}
	}
}

function skuLockImg(numberCode){
	var skuLock = $("#isLock_"+numberCode).val();
	var skuStockList = $.map($("tr[id^='skuDetailTr_"+numberCode+"_']"),
		function(p){
			return $(p).attr("id").split("_")[2];
		}
	);
	if(skuLock == '1'){
		$("#isLockImg_"+numberCode).attr("src","../image/other/unlock.png");
		$("#isLockImg_"+numberCode).attr("title","点我锁定价格(克隆后的宝贝库存价格不能更改)");
		$("#isLock_"+numberCode).val('0');
		
		for(var j = 0; j < skuStockList.length; j++){
			$("#isLockImg_"+numberCode+"_"+skuStockList[j]).attr("src","../image/other/unlock.png");
			$("#isLockImg_"+numberCode+"_"+skuStockList[j]).attr("点我锁定该价格(克隆后的宝贝库存价格不能更改)");
			$("#isLock_"+numberCode+"_"+skuStockList[j]).val('0');
		}
	}else{
		$("#isLockImg_"+numberCode).attr("src","../image/other/lock.png");
		$("#isLockImg_"+numberCode).attr("title","点我不锁定价格(克隆后的宝贝库存价格可以更改)");
		$("#isLock_"+numberCode).val('1');
		
		for(var j = 0; j < skuStockList.length; j++){
			$("#isLockImg_"+numberCode+"_"+skuStockList[j]).attr("src","../image/other/lock.png");
			$("#isLockImg_"+numberCode+"_"+skuStockList[j]).attr("title","点我不锁定该价格(克隆后的宝贝库存价格可以更改)");
			$("#isLock_"+numberCode+"_"+skuStockList[j]).val('1');
		}
	}
}

function operateOneImg(numberCode, number, type){
	
	var isPass = $("#isPass_"+numberCode+"_"+number).val();
	if(isPass == '0'){
		$("#skuImg_"+numberCode+"_"+number).attr("src","../image/other/lock.png");
		$("#skuImg_"+numberCode+"_"+number).attr("title","点我"+type+"该宝贝库存");
		isPass = '1';
		$("#isPass_"+numberCode+"_"+number).val(isPass);
	}else{
		$("#skuImg_"+numberCode+"_"+number).attr("src","../image/other/unlock.png");
		$("#skuImg_"+numberCode+"_"+number).attr("title","点我不"+type+"该宝贝库存");
		isPass = '0';
		$("#isPass_"+numberCode+"_"+number).val(isPass);
	}
	
	var tempCount = 0;
	var skuStockList = $.map($("tr[id^='skuDetailTr_"+numberCode+"_']"),
		function(p){
			return $(p).attr("id").split("_")[2];
		}
	);
	for(var k = 0; k < skuStockList.length; k++){
		var skuIsPass = $("#isPass_"+numberCode+"_"+skuStockList[k]).val();
		if(skuIsPass == isPass){
			tempCount++;
		}
	}
	if(tempCount == skuStockList.length){
		if(isPass == '0'){
			$("#skuImg_"+numberCode).attr("src","../image/other/unlock.png");
			$("#skuImg_"+numberCode).attr("title","点我不"+type+"宝贝及库存");
			$("#isPass_"+numberCode).val(isPass);
		}else{
			$("#skuImg_"+numberCode).attr("src","../image/other/lock.png");
			$("#skuImg_"+numberCode).attr("title","点我"+type+"宝贝及库存");
			$("#isPass_"+numberCode).val(isPass);
		}
	}else{
		$("#skuImg_"+numberCode).attr("src","../image/other/unlock.png");
		$("#skuImg_"+numberCode).attr("title","点我不"+type+"宝贝及库存");
		$("#isPass_"+numberCode).val('0');
	}
}

function isLockOneImg(numberCode,number){
	var isLock = $("#isLock_"+numberCode+"_"+number).val();
	if(isLock == '1'){
		$("#isLockImg_"+numberCode+"_"+number).attr("src","../image/other/unlock.png");
		$("#isLockImg_"+numberCode+"_"+number).attr("title","点我锁定该价格(克隆后的宝贝库存价格不能更改)");
		isLock = '0';
		$("#isLock_"+numberCode+"_"+number).val(isLock);
	}else{
		$("#isLockImg_"+numberCode+"_"+number).attr("src","../image/other/lock.png");
		$("#isLockImg_"+numberCode+"_"+number).attr("title","点我不锁定该价格(克隆后的宝贝库存价格可以更改)");
		isLock = '1';
		$("#isLock_"+numberCode+"_"+number).val(isLock);
	}
	
	var skuStockList = $.map($("tr[id^='skuDetailTr_"+numberCode+"_']"),
		function(p){
			return $(p).attr("id").split("_")[2];
		}
	);
	var cout = 0;
	for(var j = 0; j < skuStockList.length; j++){
		var lock = $("#isLock_"+numberCode+"_"+skuStockList[j]).val();
		if(lock == isLock){
			cout++;
		}
	}
	if(cout == skuStockList.length){
		if(isLock=='1'){
			$("#isLockImg_"+numberCode).attr("src","../image/other/lock.png");
			$("#isLockImg_"+numberCode).attr("title","点我不锁定价格(克隆后的宝贝库存价格可以更改)");
			$("#isLock_"+numberCode).val('1');
		}else{
			$("#isLockImg_"+numberCode).attr("src","../image/other/unlock.png");
			$("#isLockImg_"+numberCode).attr("title","点我锁定价格(克隆后的宝贝库存价格不能更改)");
			$("#isLock_"+numberCode).val('0');
		}
	}else{
		$("#isLockImg_"+numberCode).attr("src","../image/other/unlock.png");
		$("#isLockImg_"+numberCode).attr("title","点我锁定价格(克隆后的宝贝库存价格不能更改)");
		$("#isLock_"+numberCode).val('0');
	}
}

function copyTaobaoTravelItemSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	var skuJson = {};
	var stockJson = [];
	
	var channel = $("#channelForm").combobox("getValue");
	
	var skuList = $.map($("tr[id^='skuTr_']"),
		function(p){
			return $(p).attr("id").split("_")[1];
		}
	);
	for(var s = 0; s < skuList.length; s++){
		var skuId = skuList[s];
		var skuIsPass = $("#isPass_"+skuId).val();
		var skuIsLock = $("#isLock_"+skuId).val();
		
		if(skuIsPass == '0'){
			var skuNumber = $("#skuNumberCode_"+skuId).val();
			
			//宝贝库存
			var skuStockList = $.map($("tr[id^='skuDetailTr_"+skuId+"_']"),
				function(p){
					return $(p).attr("id").split("_")[2];
				}
			);
			var stockJsonStr = "";
			var stockLock = [];
			for(var d = 0; d < skuStockList.length; d++){
				var skuDetailId = skuStockList[d];
				var isPass = $("#isPass_"+skuId+"_"+skuDetailId).val();
				if(isPass == '0'){//复制
					var detailNumber = $("#numberCode_"+skuId+"_"+skuDetailId).val();
					stockJsonStr = stockJsonStr + detailNumber;
					if(d < (skuStockList.length-1)){
						stockJsonStr = stockJsonStr + ",";
					}
					
					var lock = $("#isLock_"+skuId+"_"+skuDetailId).val();
					var skuStock = {
						numberCode: detailNumber,
						isLock : lock
					};
					stockLock[stockLock.length] = skuStock;
				}
			}
			skuJson[skuNumber] = stockJsonStr;
			var skuData = {
				skuNumber : skuNumber,
				isLock : skuIsLock,
				stock : stockLock
			};
			stockJson[stockJson.length] = skuData;
		}
	}
	
	$.ajax({
		url : "../TaobaoWS/copyTaobaoTravelItemOper.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			channel : channel,
			skuJson : JSON.stringify(skuJson),
			stockJson : JSON.stringify(stockJson)
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#taobaoManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_copyTaobaoTravelItem").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

//同步宝贝
function syncTaobaoTravelItem(){
	var taobaoTravelItem = $("#taobaoManagerTable").datagrid("getSelections")[0];
	if (taobaoTravelItem == undefined) {
		top.$.messager.alert("提示","请选择宝贝!","info");
		return;
	}
	
	var travelItem = taobaoTravelItem.taobaoTravelItem;
	var html = '<form><center><br/>';
	html += '<input type="hidden" id="taobaoTravelItem" value="'+ travelItem +'"/>';
	html += '<table id="skuTableForm" border="1" cellpadding="2" cellspacing="0" style="text-align:center;width:850px;">';
	html += '</table>';
	
	xyzdialog({
		dialog:'dialogFormDiv_syncTaobaoTravelItem',
		title:"SKU同步",
		content : html,
		buttons:[{
			text:'确定',
			handler:function(){
				syncTaobaoTravelItemSubmit(travelItem);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_syncTaobaoTravelItem").dialog("destroy");
			}
		}],
		onOpen : function() {
			xyzAjax({
				url:'../TaobaoWS/getTaobaoItemByNumberCode.do',
				data : {
					numberCode : travelItem
				},
				success:function(data){
					if (data.status == 1) {
						var skuList = data.content.skuList;
						var stockList = data.content.stockList;
						
						var skuHtml = '';
						skuHtml += '<caption  id = "skuCaption_0" class = "skuCaptioonClass">';
						skuHtml += ' 渠道 <input type="text" id="channelForm_0" style="width:150px;"/>';
						skuHtml += ' 宝贝 <input type="text" id="travelItemForm_0" style="width:450px;"/>&nbsp&nbsp';
						skuHtml += '<img id="addSkuHtml" alt="点我添加渠道和宝贝!" src="../image/other/addPage.gif" title="点我添加渠道和宝贝 !"style="cursor: pointer;"><br/><br/>';
						skuHtml += '</caption>';
						skuHtml += '<caption>';
						skuHtml += ' 同步<span style="font-size:16px;font-weight:bold;">【'+taobaoTravelItem.channelNameCn+'】【'+ taobaoTravelItem.title +'】</span>到<br/><br/>';
						skuHtml += ' 同步模式：<input type="radio" name="type" value="0" checked="checked">覆盖模式（覆盖掉原有SKU数据）</input><input type="radio" name="type" value="1">更新模式（保留原有SKU数据）</input><br/><br/>';
						skuHtml += '</caption>';
						skuHtml += '<thead>';
						skuHtml += ' <tr>';
						skuHtml += '  <th style="width:240px;font-size:14px;">SKU</th>';
						skuHtml += '  <th colspan="5" style="font-size:14px;height:18px;">';
						skuHtml += '    SKU库存<span style="color:red;">(温馨提示:<img src="../image/other/unlock.png" style="width:16px;height:16px;"/>同步,<img src="../image/other/lock.png" style="width:16px;height:16px;"/>不同步)</span>';
						skuHtml += '  </th>';
						skuHtml += ' </tr>';
						skuHtml += '</thead>';
						skuHtml += '<tbody>';
						for(var k = 0; k < skuList.length; k++){
							var skuNumberCode = skuList[k].numberCode;
							var packageName = skuList[k].packageName;
							var isSync = skuList[k].isSync;
							
							var skuStockList = stockList[skuNumberCode];
							skuHtml += ' <tr id="skuTr_'+skuNumberCode+'">';
							skuHtml += '  <th rowspan="'+ (skuStockList.length+1) +'">';
							skuHtml += '   <input type="hidden" id="skuNumberCode_'+skuNumberCode+'" value="'+ skuNumberCode +'"/>';
							skuHtml += '   <span style="width:240px;">'+ packageName +'</span>';
							skuHtml += '  </th>';
							skuHtml += '  <th style="width:180px;">时间</th>';
							skuHtml += '  <th style="width:130px;">库存类型</th>';
							skuHtml += '  <th style="width:100px;">库存</th>';
							skuHtml += '  <th style="width:100px;">价格</th>';
							skuHtml += '  <th style="width:100px;height:28px;text-align:center;">';
							if(skuStockList.length+1 > 1){
								skuHtml += '   <span style="float:left;margin-left:9px;margin-top:5px;" >操作</span>';
								if(isSync == 1){
									skuHtml += '<span style="float:left;"><img src="../image/other/lock.png" title="点我同步宝贝及库存" id="skuImg_'+skuNumberCode+'" onclick="operateImg(\''+skuNumberCode+'\',\'同步\')" >';
									skuHtml += '<input id="isPass_'+skuNumberCode+'" type="hidden" value="1"/></span>';
								}else{
									skuHtml += '<span style="float:left;"><img src="../image/other/unlock.png" title="点我不同步宝贝及库存" id="skuImg_'+skuNumberCode+'" onclick="operateImg(\''+skuNumberCode+'\',\'同步\')" >';
									skuHtml += '<input id="isPass_'+skuNumberCode+'" type="hidden" value="0"/></span>';
								}
							}
							skuHtml += '  </th>';
							skuHtml += ' </tr>';
							
							for(var m = 0; m < skuStockList.length; m++){
								var number = skuStockList[m].numberCode;
								var date = skuStockList[m].date;
								var priceType = skuStockList[m].priceType;
								var stockIsSync = skuStockList[m].isSync;
								
								var type = "";
								if(priceType == 1){
									type = "成人价";
								}else if(priceType == 2){ 
									type = "儿童价";
								}else{//priceType == 3
									type = "单房差";
								}
								var price = skuStockList[m].price;
								var stock = skuStockList[m].stock;
								skuHtml += '<tr id="skuDetailTr_'+skuNumberCode+'_'+number+'">';
								skuHtml += ' <td style="width:180px;">';
								skuHtml += '  <input type="hidden" id="numberCode_'+skuNumberCode+'_'+number+'" value="'+ number +'"/>';
								skuHtml += '  <span style="width:180px;">'+ xyzGetDiv(date ,0 ,10) +'</span>';
								skuHtml += ' </td>';
								skuHtml += ' <td style="width:130px;">';
								skuHtml += '  <span style="width:130px;">'+ type +'</span>';
								skuHtml += ' </td>';
								skuHtml += ' <td style="width:90px;">';
								skuHtml += '  <span style="width:90px;">'+ stock +'</span>';
								skuHtml += ' </td>';
								skuHtml += ' <td style="width:90px;">';
								skuHtml += '  <span style="width:90px;">'+ xyzGetPrice(price) +'</span>';
								skuHtml += ' </td>';
								skuHtml += ' <td style="width:100px;">';
								if(isSync == 1){
									skuHtml += '  <img src="../image/other/lock.png" title="点我同步该宝贝库存" id="skuImg_'+skuNumberCode+'_'+number+'" onclick="operateOneImg(\''+skuNumberCode+'\',\''+number+'\',\'同步\')">';
									skuHtml += '  <input id="isPass_'+skuNumberCode+'_'+number+'" type="hidden" value="1"/>';
								}else{
									if(stockIsSync == 1){
										skuHtml += '  <img src="../image/other/lock.png" title="点我同步该宝贝库存" id="skuImg_'+skuNumberCode+'_'+number+'" onclick="operateOneImg(\''+skuNumberCode+'\',\''+number+'\',\'同步\')">';
										skuHtml += '  <input id="isPass_'+skuNumberCode+'_'+number+'" type="hidden" value="1"/>';
									}else{
										skuHtml += '  <img src="../image/other/unlock.png" title="点我不同步该宝贝库存" id="skuImg_'+skuNumberCode+'_'+number+'" onclick="operateOneImg(\''+skuNumberCode+'\',\''+number+'\',\'同步\')">';
										skuHtml += '  <input id="isPass_'+skuNumberCode+'_'+number+'" type="hidden" value="0"/>';
									}
								}
								skuHtml += ' </td>';
								skuHtml += '</tr>';
							}
						}
						skuHtml += '</tbody>';
						$("#skuTableForm").html(skuHtml);
						$("#addSkuHtml").click(function() {
							var tempId = $($(".skuCaptioonClass:last")).attr('id');
							tempLength = tempId.split('_')[1];
							tempLength = parseInt(tempLength)+1;
							var skuListHtml = "";
							skuListHtml += '<caption  id = "skuCaption_'+tempLength+'" class = "skuCaptioonClass">';
							skuListHtml += ' 渠道 <input type="text" id="channelForm_'+tempLength+'" style="width:150px;"/>';
							skuListHtml += ' 宝贝 <input type="text" id="travelItemForm_'+tempLength+'" style="width:450px;"/>&nbsp&nbsp';
							skuListHtml += '<img onclick ="deleteSkuHtml(\''+tempLength+'\')" alt="点我删除渠道和宝贝!" src="../image/other/delete.png" title="点我删除渠道和宝贝!" style="cursor: pointer;"><br/><br/>';
							skuListHtml += '</caption>';
							$(".skuCaptioonClass:last").after(skuListHtml);
							xyzCombobox({
								required:true,
								combobox : 'channelForm_'+tempLength,
								url : '../ListWS/getChannelList.do',
								mode: 'remote',
								lazy : false,
								onBeforeLoad: function(param){
									param.isMeituan = "";
								},
								onChange : function(newValue, oldValue){
									xyzCombobox({
										required:true,
										combobox : 'travelItemForm_'+tempLength,
										url : '../ListWS/getTravelItemByChannel.do',
										mode: 'remote',
										lazy : false,
										onBeforeLoad: function(param){
											param.travelItem = travelItem;
											var channel = $("#channelForm_"+tempLength).combobox("getValue");
											param.channel = channel;
										}
									});
								}
							});
							xyzCombobox({
								required:true,
								combobox : 'travelItemForm_'+tempLength,
								url : '../ListWS/getTravelItemByChannel.do',
								mode: 'remote',
								lazy : false,
								onBeforeLoad: function(param){
									param.travelItem = travelItem;
									var channel = $("#channelForm_"+tempLength).combobox("getValue");
									if(!xyzIsNull(channel)){
										param.channel = channel;
									}
								}
							});
						});
						xyzCombobox({
							required:true,
							combobox : 'channelForm_0',
							url : '../ListWS/getChannelList.do',
							mode: 'remote',
							lazy : false,
							onChange : function(newValue, oldValue){
								xyzCombobox({
									required:true,
									combobox : 'travelItemForm_0',
									url : '../ListWS/getTravelItemByChannel.do',
									mode: 'remote',
									lazy : false,
									onBeforeLoad: function(param){
										param.isMeituan = "";
									},
									onBeforeLoad: function(param){
										param.travelItem = travelItem;
										var channel = $("#channelForm_0").combobox("getValue");
										param.channel = channel;
									}
								});
							}
						});
						xyzCombobox({
							required:true,
							combobox : 'travelItemForm_0',
							url : '../ListWS/getTravelItemByChannel.do',
							mode: 'remote',
							lazy : false,
							onBeforeLoad: function(param){
								param.travelItem = travelItem;
								var channel = $("#channelForm_0").combobox("getValue");
								if(!xyzIsNull(channel)){
									param.channel = channel;
								}
							}
						});
					}else{
						top.$.messager.alert("警告", data.msg, "warning");
					}
				}
			});
		}
	});
}

function deleteSkuHtml(id){
	
	$("#skuCaption_"+id).remove();
	
}

function syncTaobaoTravelItemSubmit(syncTravelItem){
	if(!$("form").form('validate')){
		return false;
	}
	
	var travelItems = '';
	 $(".skuCaptioonClass").each(function(){
		var numberCode = $(this).find("input[id^='travelItemForm_']").combobox("getValue");
		travelItems += numberCode +",";
	 });
    
	var skuJson = []; //套餐
	var skuList = $.map($("tr[id^='skuTr_']"),
		function(p){
			return $(p).attr("id").split("_")[1];
		}
	);
	for(var s = 0; s < skuList.length; s++){
		var stockJson = [];  //库存
		var skuId = skuList[s];
		var skuIsPass = $("#isPass_"+skuId).val();
		if(xyzIsNull(skuIsPass)){
			continue;
		}
		var skuNumber = $("#skuNumberCode_"+skuId).val();
		if(skuIsPass == '0'){
			//宝贝库存
			var skuStockList = $.map($("tr[id^='skuDetailTr_"+skuId+"_']"),
				function(p){
					return $(p).attr("id").split("_")[2];
				}
			);
			for(var d = 0; d < skuStockList.length; d++){
				var skuDetailId = skuStockList[d];
				var isPass = $("#isPass_"+skuId+"_"+skuDetailId).val();
				var stockNumber = $("#numberCode_"+skuId+"_"+skuDetailId).val();
				var stock = {
					stock : stockNumber,
					isSycn : isPass
				};
				stockJson[stockJson.length] = stock;
			}
		}
		var sku = {
			numberCode : skuNumber,
			isSycn : skuIsPass,
			stock : stockJson
		};
		skuJson[skuJson.length] = sku;
	}
	var type= $("input[name='type']:checked").val();
	
	$.ajax({
		url : "../TaobaoWS/syncTaobaoTravelItemOper.do",
		type : "POST",
		data : {
			syncNumberCode : syncTravelItem,
			skuJson : JSON.stringify(skuJson),
			travelItems : travelItems,
			type : type
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#taobaoManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_syncTaobaoTravelItem").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function updateTaobaoItem(numberCode,title){
	
	$.messager.confirm('确认','您确认想要更新【'+title+'】吗？',function(r){    
		if(r){
			$.ajax({
				url : '../TaobaoWS/updateTaobaoItem.do',
				type : "POST",
				data : {
					baseInfo : numberCode,
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.status == 1) {
						$("#taobaoManagerTable").datagrid("reload");
						top.$.messager.alert("提示", "【"+title+"】已更新", "info");
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

//上传SKU
function pushProductButton(numberCode, channel){
	
	var skuList = [];
	$.ajax({
		url : '../TaobaoWS/getSkuInfo.do',
		type : "POST",
		data : {
			baseInfo : numberCode
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				skuList = data.content;
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
	if(xyzIsNull(skuList)){
		top.$.messager.alert("温馨提示", "请设置SKU", "info");
		return false;
	}
	
	xyzdialog({
		dialog : 'dialogFormDiv_pushProductButton',
		title : '上传SKU',
	    href : '../jsp_taobao/pushProduct.html',
	    fit : false,
	    width : 600,
	    height : 600,
	    buttons:[{
			text:'确定',
			handler:function(){
				pushProductSubmit(numberCode, channel);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_pushProductButton").dialog("destroy");
			}
		}],onLoad : function(){
		
			var html = '';
			for(var k = 0; k < skuList.length; k++){
				var skuObj = skuList[k];
				var numberCode = skuObj.numberCode;
				var packageName = skuObj.packageName;
				var travelItem = skuObj.taobaoTravelItem;
				html += '<tr id="skuTr_'+ numberCode +'">';
				html += ' <td>'+ numberCode +'</td>';
				html += ' <td>'+ packageName +'</td>';
				html += ' <td>';
				html += '  <input type="checkbox" id="sku_'+ numberCode +'" class="skuInfo"/>';
				html += '  <input type="hidden" id="travelItem_'+ numberCode +'" value="'+ travelItem +'"/>';
				html += ' </td>';
				html += '</tr>';
			}
			$("#skuTbady").html(html);
		}
	});
	
}

function pushProductSubmit(numberCode , channel){

	var skuJson = [];
    $(".skuInfo").each(function(){
    	if($(this).attr("checked") == "checked"){
    		var skuNumber = $(this).prop('id').split('_')[1]; 
    		skuJson[skuJson.length] = skuNumber;
    	}
    });
    
    if(xyzIsNull(skuJson)){
    	top.$.messager.alert("提示","请选择SKU套餐!","info");
    	return false;
    }
    
    $.ajax({
		url : "../QueryTaobaoWS/pushProductOper.do",
		type : "POST",
		data : {
			channel : channel,
			baseInfo : numberCode,
			skuJson : JSON.stringify(skuJson)
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#dialogFormDiv_pushProductButton").dialog("destroy");
				top.$.messager.alert("提示","操作成功!","info");
				$("#taobaoManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}


function queryLogWorkListByCruiseButton(){
	/*var tabOne = $('#taobaoManagerTabs').tabs('getSelected');
	var cruiseOne = tabOne.attr("id");
	
	var tabTwo = $('#cruiseTabs').tabs('getSelected');
	var cruiseTwo = tabTwo.attr("id");
	var cruise = "";
	if(!xyzIsNull(cruiseOne)){
		cruise = cruiseOne;
	}
	if(!xyzIsNull(cruiseTwo)){
		cruise = cruiseTwo;
	}
	if(xyzIsNull(cruise)){
		top.$.messager.alert("温馨提示", "请选择邮轮具体的Tabs!", "info");
		return false;
	}*/
	if(xyzIsNull(cruiseNumber)){
		top.$.messager.alert("温馨提示", "请选择邮轮具体的Tabs!", "info");
		return false;
	}
	
	xyzdialog({
		dialog : 'dialogFormDiv_queryLogWorkListByCruiseButton',
		title : '锁定日志',
		href : '../jsp_taobao/editLogWorkByCruise.html',
		buttons : [ {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_queryLogWorkListByCruiseButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			$.ajax({
				url : "../TaobaoWS/queryTaobaoLogByCruiseWorkList.do",
				type : "POST",
				data : {
					cruise : cruise
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						var content = data.content;
						var detailLiist = content.dataList;
						var logWorkList = content.logWorkList;
						var channelNameCnArry=[];
						var cruiseNameCnArry=[];
						var cabinArry = [];  //跨行数组
						var html = '';
						if(detailLiist.length > 0){
							var cruiseNameCnIndex = 1;
							var cruiseNameCn = "";
							var channelNameCnIndex = 1;
							var channelNameCn = "";
							var cabinIndex = 1;  //跨几行
							var packageName = "";
							for(var d = 0; d < detailLiist.length; d++){
								var detailObj = detailLiist[d];
								var numberCode = detailObj.numberCode;
									
								var date = detailObj.date.substring(0,10);
								var price = xyzGetPrice(detailObj.price);
								var stock = detailObj.stock;
								var isLock = detailObj.isLock;
								
								html += '<tr id="tr_'+ numberCode +'">';
								if(xyzIsNull(cruiseNameCn)){
									cruiseNameCn = detailObj.cruiseNameCn;
									html += ' <td id="td_'+ cruiseNameCn +'">'+ cruiseNameCn +'</td>';
									cruiseNameCnIndex = 1;
								}else if(cruiseNameCn != detailObj.cruiseNameCn){
									cruiseNameCnArry[cruiseNameCnArry.length] = cruiseNameCn+'-'+cruiseNameCnIndex;
									cruiseNameCn = detailObj.cruiseNameCn;
									html += ' <td id="td_'+ cruiseNameCn +'">'+ cruiseNameCn +'</td>';
									cruiseNameCnIndex = 1;
								}else{
									cruiseNameCnIndex++;
								}
								if(xyzIsNull(channelNameCn)){
									channelNameCn = detailObj.channelNameCn;
									html += ' <td id="td_'+ channelNameCn +'">'+ channelNameCn +'</td>';
									channelNameCnIndex = 1;
								}else if(channelNameCn != detailObj.channelNameCn){
									channelNameCnArry[channelNameCnArry.length] = channelNameCn+'-'+channelNameCnIndex;
									channelNameCn = detailObj.channelNameCn;
									html += ' <td id="td_'+ channelNameCn +'">'+ channelNameCn +'</td>';
									channelNameCnIndex = 1;
								}else{
									channelNameCnIndex++;
								}
								if(xyzIsNull(packageName)){
									packageName = detailObj.packageName;
									html += ' <td id="td_'+ packageName +'">'+ packageName +'</td>';
									cabinIndex = 1;
								}else if(packageName != detailObj.packageName){
									cabinArry[cabinArry.length] = packageName+'-'+cabinIndex;
									packageName = detailObj.packageName;
									html += ' <td id="td_'+ packageName +'">'+ packageName +'</td>';
									cabinIndex = 1;
								}else{
									cabinIndex++;
								}
								html += ' <td>'+ date +'</td>';
								html += ' <td>'+ price +'</td>';
								html += ' <td>'+ stock +'</td>';
								html += ' <td>';
								if(isLock == 0){//不锁定
									html += '  <img id="lockWOrk_'+ numberCode +'" alt="点我锁定价格" src="../image/other/unlock.png" onClick="workClick(\''+ numberCode +'\')" title="点我锁定价格" style="cursor: pointer;">';
								}else{
									html += '  <img id="lockWOrk_'+ numberCode +'" alt="点我解锁价格" src="../image/other/lock.png" onClick="workClick(\''+ numberCode +'\')"  title="点我不锁定价格" style="cursor: pointer;">';
								}
								html += ' <input id="lockHidden_'+ numberCode +'" type="hidden" value="'+ isLock +'"> ';
								html += ' </td>';
								html += '</tr>';
							}
							cruiseNameCnArry[cruiseNameCnArry.length] = cruiseNameCn+'-'+cruiseNameCnIndex;
							channelNameCnArry[channelNameCnArry.length] = channelNameCn+'-'+channelNameCnIndex;
							cabinArry[cabinArry.length] = packageName+'-'+cabinIndex;
						}else{
							html += '<tr>';
							html += ' <th colspan="5">该宝贝下没有任何SKU日历库存</th>';
							html += '</tr>';
						}
						$("#skuAllTable").html(html);
						
						if(cruiseNameCnArry.length > 0){
							for(var d = 0; d < cruiseNameCnArry.length; d++){
								var arryObj = cruiseNameCnArry[d];
								var objArry = arryObj.split("-");
								$("#td_"+objArry[0]).attr("rowspan",objArry[1]);
							}
						}
						if(channelNameCnArry.length > 0){
							for(var d = 0; d < channelNameCnArry.length; d++){
								var arryObj = channelNameCnArry[d];
								var objArry = arryObj.split("-");
								$("#td_"+objArry[0]).attr("rowspan",objArry[1]);
							}
						}
						if(cabinArry.length > 0){
							for(var d = 0; d < cabinArry.length; d++){
								var arryObj = cabinArry[d];
								var objArry = arryObj.split("-");
								$("#td_"+objArry[0]).attr("rowspan",objArry[1]);
							}
						}
						
						if(logWorkList.length > 0){
							var logHtml = '';
							for(var k = 0; k < logWorkList.length; k++){
								var logObj = logWorkList[k];
								var username = logObj.username;
								var addDate = logObj.addDate;
								var remark = logObj.remark;
								var value = logObj.value;
								
								for(var d = 0; d < detailLiist.length; d++){
									var detailObj = detailLiist[d];
									var numberCode = detailObj.numberCode;
									if(value == numberCode){
										logHtml += '<tr>';
										logHtml += ' <td>'+ username +'</td>';
										logHtml += ' <td>'+ addDate +'</td>';
										logHtml += ' <td>'+ xyzGetDiv(remark, 0, 60) +'</td>';
										logHtml += '</tr>';
										break;
									}
								}
							}
							$("#logWorkAllTable").html(logHtml);
						}
						
					}else{
						top.$.messager.alert("警告", data.msg ,"warning");
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
				}
			});
		}
	});
	
}

function batchUploadSkuButton(){
	/*var tabOne = $('#taobaoManagerTabs').tabs('getSelected');
	var cruiseOne = tabOne.attr("id");
	
	var tabTwo = $('#cruiseTabs').tabs('getSelected');
	var cruiseTwo = tabTwo.attr("id");
	var cruise = "";
	if(!xyzIsNull(cruiseOne)){
		cruise = cruiseOne;
	}
	if(!xyzIsNull(cruiseTwo)){
		cruise = cruiseTwo;
	}*/
	
	if(xyzIsNull(cruiseNumber) || cruiseNumber == "notValue"){
		top.$.messager.alert("温馨提示", "请选择具体的邮轮!", "info");
		return false;
	}
	
	$.messager.confirm('确认','您确认批量上传SKU吗？',function(r){    
		if(r){
			$.ajax({
				url : '../TaobaoWS/batchUploadSkuOper.do',
				type : "POST",
				data : {
					cruise : cruise,
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.status == 1) {
						top.$.messager.alert("提示", "操作成功!", "info");
						$("#taobaoManagerTable").datagrid('reload');
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
function workClick(numberCode){
	var isLock = $("#lockHidden_"+numberCode).val();
	if(isLock == '0'){
		$("#lockWOrk_"+numberCode).attr("src","../image/other/lock.png");
		$("#lockWOrk_"+numberCode).attr("title","点我不锁定价格");
		isLock = '1';
		$("#lockHidden_"+numberCode).val(isLock);
	}else{
		$("#lockWOrk_"+numberCode).attr("src","../image/other/unlock.png");
		$("#lockWOrk_"+numberCode).attr("title","点我锁定价格");
		isLock = '0';
		$("#lockHidden_"+numberCode).val(isLock);
	}
	var newIsLock = $("#lockHidden_"+numberCode).val();

	$.ajax({
		url : '../TaoBaoSkuWS/editDetailByNumberCode.do',
		type : "POST",
		data : {
			numberCode : numberCode,
			isLock : newIsLock
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#taobaoManagerTable").datagrid('reload');
				
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

/* 上传SKU到蚂蜂窝 */
function addProductByMafengwoButton(travelItem){
	
	$.ajax({
		url : '../QueryTaobaoWS/addProduct.do',
		type : "POST",
		data : {
			channel : "mafengwo",
			travelItem : travelItem
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
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