var calendarData=[];

var myCalendar;
var cruiseNumber = '';
$(document).ready(function() {
	
	var options = {
	      width: '610px',
	      height: '510px',
	      language: 'CH', //语言
	      showLunarCalendar: false, //阴历
	      showHoliday: false, //休假
	      showFestival: false, //节日
	      showLunarFestival: false, //农历节日
	      showSolarTerm: false, //节气
	      showMark: true, //标记
	      timeRange: {
	        startYear: 2017,
	        endYear: 2018
	      },
	      theme: {
	        changeAble: false,
	        weeks: {
	          backgroundColor: '#FBEC9C',
	          fontColor: '#4A4A4A',
	          fontSize: '20px',
	        },
	        days: {
	          backgroundColor: '#ffffff',
	          fontColor: '#565555',
	          fontSize: '24px'
	        },
	        todaycolor: 'orange',
	        activeSelectColor: 'orange',
	      }
	 };
	myCalendar= new SimpleCalendar('#container',options);
	
	var params = getUrlParameters();
	cruiseNumber = params.cruise;
	
	xyzAjax({
		url : "../TkviewWS/getTkviewTabList.do",
		type : "POST",
		async : false,
		data:{
			cruise : cruiseNumber
		},
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var tkviewGroupList = data.content;
				if(tkviewGroupList.length == 0){
					return false;
				}
				
				var cabinHtml = '';
				for(var i = 0; i < tkviewGroupList.length; i++){
					var tkviewNumberCode = tkviewGroupList[i].numberCode;
					var tkviewNameCn = tkviewGroupList[i].nameCn.substring(12);
					cabinHtml += '<span class="label label-warning" id="span_'+ tkviewNumberCode +'" style="width:66px;" value="'+tkviewNameCn+'">'+ xyzGetDiv(tkviewNameCn ,0 ,10) +'</span> ';
				}
				$("#cabinDivs").html(cabinHtml);
				$("#cabinDivs").attr("style","max-height:86px;overflow:auto;");
				
				for(var t = 0; t < tkviewGroupList.length; t++){
					var tempId = tkviewGroupList[t].numberCode;
					$("#span_"+tempId).click(function(){
						var tkviewNumber = $(this).prop('id').split('_')[1];
						getTkviewListByTkviewGroup(tkviewNumber);
					});
				}
				getTkviewListByTkviewGroup(tkviewGroupList[0].numberCode);
			}
		}
	});
	
	//日历单元格点击事件
	dateClick();
	
});

//单品Div点击事件
function getTkviewListByTkviewGroup(tkviewNumber){
	$("#baseInfo").empty();
	$(".price-table").css("display","none");
	$("#tableDiv").css("display","none");
	
	$(".label").each(function(){
		var tempTkview = $(this).prop('id').split('_')[1];
		if(tempTkview == tkviewNumber){
			$("#span_"+tempTkview).attr("class","label label-click");
		}else{
			$("#span_"+tempTkview).attr("class","label label-warning");
		}
	});
	
	$.ajax({
		url :"../TkviewWS/getTkviewListByTkviewGroup.do",
		type:"POST",
		data:{
			cruise : cruiseNumber,
			tkview : tkviewNumber
		},
		async:false,
		dataType:"json",
		success:function(data) {
			if(data.status == 1){
				//生成月份tab
				var dateList = data.content.dateList;
				var tkviewList = data.content.tkviewList;
				
				if(dateList.length < 1){
					return false;
				}
				
				for(var i = 0; i < tkviewList.length; i++){
					calendarData[new Date(tkviewList[i].shipmentTravelDate).Format("yyyy-M-d")]=JSON.stringify(tkviewList[i]);
				}
				myCalendar._options.tkviewData = calendarData;
				myCalendar.update();
				closeAllTabs("dateTabs");
				
				for(var i = 0; i < dateList.length; i++){
					var arr = dateList[i].split('-');
					$("#dateTabs").tabs('add',{
					    title : arr[0]+'年'+arr[1]+'月',    
					    closable : false
					});
				}
				
				var arr = (dateList[0]+"").split('-');
				myCalendar.update(arr[1],arr[0]);
				
				$("#dateTabs").tabs({
					onSelect : function(title,index){
						var arr = (dateList[index]+"").split('-');
						myCalendar.update(arr[1],arr[0]);
				    }
				});
				
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

//日历单元格点击事件
function dateClick(){
	//单元格点击事件开始
	var stockList = [];
	var tkviewNumber = '';
	$(".item-nolunar").click(function(){
		tkviewNumber = $(this).attr("numberCode");
		var date = $(this).attr("data");
		if(xyzIsNull(tkviewNumber)){
			$("#baseInfo").empty();
			$(".price-table").css("display","none");
			$("#tableDiv").css("display","none");
			return false;
		}
		
		$.ajax({
			url:"../TkviewWS/getTkviewViewData.do",
			type:"POST",
			data:{
				cruise : cruiseNumber,
				numberCode : tkviewNumber,
				date : date
			},
			async:false,
			dataType:"json",
			success:function(data) {
				if(data.status == 1){
					var content = data.content;
					if(xyzIsNull(content)){
						$("#baseInfo").empty();
						$("#stockListInfo tr").remove();
						return false;
					}
					var cruiseObj = data.content.cruise;
					var cabinObj = data.content.cabin;
					var tkviewObj = data.content.tkview;
					stockList = data.content.stockList;
					
					var baseHtml='';
					baseHtml += '<tr>';
					baseHtml += ' <td style="width:40%">';
					baseHtml += '  出发日期：'+ tkviewObj.shipmentTravelDate.substring(0,11);
					baseHtml += ' </td>';
					baseHtml += ' <td>单名名称：'+ tkviewObj.nameCn +'</td>';
					baseHtml += '</tr>';
					baseHtml += '<tr>';
					baseHtml += ' <td>所属邮轮：'+ cruiseObj.nameCn +'</td>';
					baseHtml += ' <td>舱型名称：'+ cabinObj.nameCn +'</td>';
					baseHtml += '</tr>';
					baseHtml += '<tr>';
					baseHtml += ' <td>航期编号：'+ tkviewObj.shipment +'</td>';
					baseHtml += ' <td>所属航线：'+ content.airway.nameCn +'</td>';
					baseHtml += '</tr>';
					/*baseHtml += '<tr>';
					baseHtml += ' <td>舱型容量：'+ cabinObj.volume +'</td>';
					baseHtml += ' <td></td>';
					baseHtml += '</tr>';*/
					$("#baseInfo").html(baseHtml);
					
					if(stockList.length == 0){
						$("#noMsg").css("display","block");
						$(".price-table").css("display","none");
						$("#tableDiv").css("display","none");
					}else{
						$("#noMsg").css("display","none");
						$(".price-table").css("display","block");
						$("#tableDiv").css("display","block");
						
						stockInitTable(tkviewNumber);
					}
				}else{
					$("#baseInfo").empty();
					$("#stockListInfo tr").remove();
					top.$.messager.alert("警告",data.msg,"warning");
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown) {
				top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
			}
		});
	});
    //单元格点击事件结束
}

function stockInitTable(tkviewNumber){
	var toolbarTkview = [];
	if(xyzControlButton("buttonCode_y20161208105005")){
		toolbarTkview[toolbarTkview.length]={
			text: '新增',
			iconCls: 'icon-add',
			handler: function(){
				addStockButton(tkviewNumber);
			}
		};
	}
	if(xyzControlButton("buttonCode_y20161208105007")){
		toolbarTkview[toolbarTkview.length]='-';
		toolbarTkview[toolbarTkview.length]={
			text: '编辑',
			iconCls: 'icon-edit',
			handler: function(){
				editStockButton();
			}
		};
	}
	if(xyzControlButton("buttonCode_y20161208105008")){
		toolbarTkview[toolbarTkview.length]='-';
		toolbarTkview[toolbarTkview.length]={
			text: '删除',
			iconCls: 'icon-remove',
			handler: function(){
				deleteStockButton();
			}
		};
	}
	
	xyzgrid({
		table : 'stockListInfo',
		title : "",
		toolbar : toolbarTkview,
		url : '../TkviewWS/getTkviewViewStockList.do',
		height : 310,
		pageSize : 5,
		idField : 'numberCode',
		queryParams : {
			numberCode : tkviewNumber
		},
		columns:[[
			{field:'checkboxTemp',checkbox:true},
			{field: 'numberCode',title:'编号',hidden:true,halign:'center'},
			{field : 'providerNameCn',title:'供应商',halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv('【'+ row.providerMark +'】'+value, 0, 80);
				}
			},
			{field : 'cost',title:'成本价',halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetPrice(value);
				}
			},
			{field : 'type',title:'库存类型',align:'center',
				formatter:function(value ,row ,index){
					if(value == 1){
						return "实库";
					}else if(value == 0){
						return "现询";
					}
					return "售罄";
				}
			},
			{field : 'stock',title:'库存',align:'center'},
			{field : 'remark',title:'成本说明',width:230,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value ,0 ,50);
				}
			}
		]]
	});
	
}

function closeAllTabs(id){  
    var arrTitle = new Array();  
    var tempId = "#"+id; //Tab所在的层的ID  
    var tabs = $(tempId).tabs("tabs");//获得所有小Tab  
    var tCount = tabs.length;  
    if(tCount > 0){  
        //收集所有Tab的title  
        for(var i = 0; i < tCount; i++){  
            arrTitle.push(tabs[i].panel('options').title); 
        }  
        //根据收集的title一个一个删除=====清空Tab  
        for(var i = 0; i < arrTitle.length; i++){  
            $(tempId).tabs("close",arrTitle[i]);  
        }  
    }  
}     

function addStockButton(tkviewNumber){
	
	xyzdialog({
		dialog : 'dialogFormDiv_addStockButton',
		title : '新增库存',
		fit : false,
		width : 430,
		height : 380,
	    href : '../jsp_resources/addStock.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				addStockSubmit(tkviewNumber);
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
				combobox:'providerForm',
				url:'../ListWS/getProviderList.do',
				lazy:false,
				mode:'remote',
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
			
		}
	});
	
}

function addStockSubmit(tkviewNumber){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var nickName = $("#nickNameForm").val();
	var provider = $("#providerForm").combobox("getValue");
	
	var stockType = $("#typeForm").combobox("getValue");
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
	
	if (day < 0) {
		$.messager.confirm('确认', '库存有效期已失效,是否添加？',function(r) {
			if(r){
				$.ajax({
					url : "../StockWS/addStock.do",
					type : "POST",
					data : {
						tkview : tkviewNumber,
						nickName : nickName,
						provider : provider,
						type : stockType,
						stock : stock,
						surpass : surpass,
						cost : xyzSetPrice(cost),
						costRemark : costRemark,
			            overDate : finalSaleDate,
			            priority : 5,
			            remark : remark
					},
					async : false,
					dataType : "json",
					success : function(data) {
						if(data.status==1){
							$("#stockListInfo").datagrid("reload");
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
		});
	}else {
		$.ajax({
			url : "../StockWS/addStock.do",
			type : "POST",
			data : {
				tkview : tkviewNumber,
				nickName : nickName,
				provider : provider,
				type : stockType,
				stock : stock,
				surpass : surpass,
				cost : xyzSetPrice(cost),
				costRemark : costRemark,
	            overDate : finalSaleDate,
	            priority : 5,
	            remark : remark
			},
			async : false,
			dataType : "json",
			success : function(data) {
				if(data.status==1){
					$("#stockListInfo").datagrid("reload");
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
	
}

function editStockButton(){
	
	var stocks = $("#stockListInfo").datagrid("getChecked");
	if(stocks.length != 1){
		top.$.messager.alert("提示","请先勾选单个库存对象!","info");
		return;
	}
	var stockObj = stocks[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editStockButton',
		title : '编辑库存',
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
				combobox:'providerForm',
				url:'../ListWS/getProviderList.do',
				lazy:false,
			});
			
			$("#providerForm").combobox({
				value : stockObj.provider
			});
			$("#costForm").numberbox("setValue",xyzGetPrice(stockObj.cost));
			$("#costRemarkForm").textbox("setValue",stockObj.costRemark);
		
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
		
			$("#overDateForm").datetimebox({
				value : stockObj.overDate
			});
			$("#remarkForm").textbox("setValue",stockObj.remark);
			
			$("#providerForm").combobox({
				required:true
			});
			$("#overDateForm").datetimebox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).datetimebox('clear');
					}
				}]
			});
		
		}
	});
	
}

function editStockSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nickName = $("#nickNameForm").val();
	var provider = $("#providerForm").combobox("getValue");
	var stockType = $("#typeForm").combobox("getValue");
	var cost = $("#costForm").numberbox("getValue");
	var costRemark = $("#costRemarkForm").textbox("getValue");
	var stock = $("#stockForm").numberbox("getValue");
	var surpass = $("#surpassForm").numberbox("getValue");
	var overDate = $("#overDateForm").datetimebox("getValue");
	//var priority = $("#priorityForm").numberbox("getValue");
	var remark = $("#remarkForm").textbox("getValue");
	
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
			overDate : overDate,
			priority : 5,
			remark : remark,
			stockType : stockType
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_editStockButton").dialog("destroy");
				$(".sc-selected").click();
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
	
	var rows = $("#stockListInfo").datagrid("getChecked");
	if(rows.length != 1){
		top.$.messager.alert("提示","请先勾选需要删除的库存对象!","info");
		return;
	}
	var stock = rows[0];
	
	$.messager.confirm('确定', '您确认想要删除记录吗？',function(r) {
		if(r){
			$.ajax({
				url : "../StockWS/deleteStock.do",
				type : "POST",
				data : {
					numberCode :  stock.numberCode
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						top.$.messager.alert("提示","操作成功!","info");
						$(".sc-selected").click();
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