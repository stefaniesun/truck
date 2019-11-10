var calendarData=[];

var myCalendar;

var checkedSkuStock = '';
var checkedDate = '';
var minTkview = '';
var shipObj = null;
$(document).ready(function() {
	var options = {
	      width: '600px',
	      height: '450px',
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
	ptviewSku = params.ptviewSku;
	ptview = params.ptview;
	cruise = params.cruise;
	shipment = params.shipment;
	shipObj = getShipmentTravelDate();
	
	/* 关联的单品种类  */
	queryTkviewType();
	
	var buttonHtml = '';
	if (xyzControlButton("buttonCode_x20170602153003")) {
		buttonHtml += '<a id="addBtn" href="javascript:void(0);" class="easyui-linkbutton">新增库存</a>&nbsp;&nbsp;';
	}
	if (xyzControlButton("buttonCode_x20170602153005")) {
		buttonHtml += '<a id="deleteBtn" href="javascript:void(0);" class="easyui-linkbutton">删除库存</a>';
	}
	$("#skuStockButton").html(buttonHtml);
	/* 新增 */
	if (xyzControlButton("buttonCode_x20170602153003")) {
		$("#addBtn").linkbutton({
			iconCls : 'icon-add',
			onClick : function(){
				addPtviewStockButton();
			}
		});
	}
	/* 删除 */
	if (xyzControlButton("buttonCode_x20170602153005")) {
		$("#deleteBtn").linkbutton({
			iconCls : 'icon-remove',
			onClick : function(){
				deletePtviewStockButton();
			}
		});
	}
	
	/* 单元格点击事件 */
	$(".item-nolunar").click(function() { //天数格添加点击事件。
		var numberCode = $(this).attr("numberCode");
		var date = $(this).attr("data");
		if(!xyzIsNull(numberCode) && !xyzIsNull(date)){
			checkedDate = date;
			checkedSkuStock = numberCode;
			getTkviewScotkData(date,numberCode);
		}
    });
	
	/*获取日历库存及日期*/
	$.ajax({
		url:"../PtviewSkuStockWS/getPtviewSkuStockDateList.do",
		type:"POST",
		data:{
			ptviewSku : ptviewSku
		},
		async:false,
		dataType:"json",
		success:function(data) {
			if(data.status==1){
				//生成月份tab集合
				var dateList = data.content.dateList;
				//SKU库存集合
				var rows = data.content.ptviewSkuStockList;
				
				if(dateList.length == 0){
					top.$.messager.alert("警告","当前SKU无日历价格库存信息!","warning");
					return;
				}
				
				for(var i=0;i<rows.length;i++){
					calendarData[new Date(rows[i].date).Format("yyyy-M-d")]=JSON.stringify(rows[i]);
				}
				myCalendar._options.skuData = calendarData;
				myCalendar.update();
				
				for(var i = 0; i < dateList.length; i++){
					var arr=dateList[i].split('-');
					$("#dateTabs").tabs('add',{
					    title : arr[0]+'年'+arr[1]+'月',    
					    closable : false
					});
				}
				var arr = dateList[0].split('-');
				myCalendar.update(arr[1],arr[0]);
				
				$("#dateTabs").tabs({
					onSelect : function(title,index){
						var arr = dateList[index].split('-');
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
	
	$("#tableDiv").css("display","none");
	
	$(".aPrice").hide();
	$(".price").hide();
	
});

/*获取关联的单品种类，用于展示名称*/
function queryTkviewType(){
	$.ajax({
		url : "../PtviewSkuStockWS/getTkviewTypeList.do",
		type : "POST",
		data : {
			ptviewSku : ptviewSku
		},
		async:false,
		dataType:"json",
		success:function(data) {
			if(data.status==1){
				var tkviewTypeList = data.content;
				var html = '';
				for(var t = 0; t < tkviewTypeList.length; t++){
					var tkviewTypeObj = tkviewTypeList[t];
					var id = tkviewTypeObj.numberCode;
					html += '<code id="'+ id +'">'+ tkviewTypeObj.cabinNameCn +'</code>&nbsp;';
				}
				if(tkviewTypeList.length == 0){
					$("#tkviewTypeDiv").html("未绑定单品");
				}else{
					$("#tkviewTypeDiv").html("绑定单品:"+html);
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

/* 单元格点击事件  */
function getTkviewScotkData(date, skuDetailNumberCode){

	var nameCn =$('.tabs-selected', parent.document).text();
	$("#tkviewStockCode").html(nameCn+" &nbsp; "+date);
	
	$(".form-inline").css("display","block");
	
	$.ajax({
		url:"../PtviewSkuStockWS/getPtviewSkuStockListByNumberCode.do",
		type:"POST",
		data:{
			numberCode : skuDetailNumberCode,
			ptviewSku : ptviewSku,
			date : date
		},
		async:false,
		dataType:"json",
		success:function(data) {
			if(data.status==1){
				var stockList = data.content.stockList;
				var detail = data.content.skuStockObj;
				$("#stockForm").val(detail.stock);
				var bPrice = detail.bPrice;
				var cPrice = detail.cPrice;
				$("#bPriceForm").val(xyzGetPrice(bPrice));
				$("#cPriceForm").val(xyzGetPrice(cPrice));
				
				var aBPrice = detail.aBPrice;
				var aCPrice = detail.aCPrice;
				if(!xyzIsNull(aBPrice) && !xyzIsNull(aCPrice) && aBPrice>0 && aCPrice>0){
					$(".aPrice").show();
					$("#aBPriceForm").val(xyzGetPrice(aBPrice));
					$("#aCPriceForm").val(xyzGetPrice(aCPrice));
				}
				var minPrice = xyzGetPrice(detail.price);
				var sBPrice = detail.sBPrice;
				var sCPrice = detail.sCPrice;
				var mBPrice = detail.mBPrice;
				var mCPrice = detail.mCPrice;
				if(!xyzIsNull(sBPrice) && !xyzIsNull(sCPrice) && !xyzIsNull(mBPrice) && !xyzIsNull(mCPrice)
					&& sBPrice>0 && sCPrice>0 && mBPrice>0 && mCPrice>0){
					$(".price").show();
					$("#sBPriceForm").val(xyzGetPrice(sBPrice));
					$("#sCPriceForm").val(xyzGetPrice(sCPrice));
					$("#mBPriceForm").val(xyzGetPrice(mBPrice));
					$("#mCPriceForm").val(xyzGetPrice(mCPrice));
					$("#mcPriceDiv").before('<br/><br/>');
				}
				
				var stockHtml = '';
				if(stockList.length < 1){
					$("#noneMsg").css("display","block");
					$(".price-table").css("display","none");
					$("#tableDiv").css("display","none");
				}else{
					$("#noneMsg").css("display","none");
					$(".price-table").css("display","block");
					$("#tableDiv").css("display","block");
					var minStock = 0;
					var tkviewNumber = '';
					for(var s = 0; s < stockList.length; s++){
						var stockObj = stockList[s];
						var numberCode = stockObj.numberCode;
						stockHtml += '<tr>';
						stockHtml += ' <td>';
						if(s == 0 || tkviewNumber != stockObj.tkview){
							tkviewNumber = stockObj.tkview;
							if(s == 0){
								minTkview = stockObj.tkview;
								stockHtml += '  <code>'+ stockObj.tkviewNameCn.substring(12) +'</code>';
							}else{
								stockHtml += stockObj.tkviewNameCn.substring(12) ;
							}
							stockHtml += '  <input type="hidden" id="tkview_'+ numberCode +'" value="'+ tkviewNumber +'"/>';
						}
						stockHtml += ' </td>';
						stockHtml += ' <td>';
						if(s==0){
							stockHtml += '  <code>【'+ stockObj.providerMark + "】"+stockObj.providerNameCn +'</code>';
						}else{
							stockHtml += ' 【'+ stockObj.providerMark + "】"+stockObj.providerNameCn;
						}
						stockHtml += ' </td>';
						stockHtml += ' <td>';
						var cost = xyzGetPrice(stockObj.cost);//成本价
						if(s == 0){
							minStock = stockObj.stock;
						}
						if(s==0){
							stockHtml += '  <code>'+ cost +'</code>';
						}else{
							stockHtml += cost;
						}
						stockHtml += ' </td>';
						stockHtml += ' <td>';
						stockHtml += '  <code>'+ stockObj.stock +'</code>';
						stockHtml += ' </td>';
						stockHtml += '  <td>';
						if(s==0){
							stockHtml += '  <code>'+ stockObj.costRemark +'</code>';
						}else{
							stockHtml += stockObj.costRemark;
						}
						stockHtml += ' </td>';
						stockHtml += '</tr>';
					}
					$("#stockTbody").html(stockHtml);
					
					$("#priceForm").val(minPrice);
					$("#stockForm").val(minStock);
					
					/*保存*/
					$("#submit_btn").click(function(){
						if(xyzIsNull(checkedDate) && xyzIsNull(checkedSkuStock)){
							return false;
						}
						var stock = $("#stockForm").val();
						submitBtnData(checkedSkuStock, minTkview, stock);
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

/* 保存--编辑 */
function submitBtnData(checkedSkuStock, minTkview, stock){
	
	var bPrice = $("#bPriceForm").val();
	var cPrice = $("#cPriceForm").val();
	var aBPrice = $("#aBPriceForm").val();
	var sBPrice = $("#sBPriceForm").val();
	var aCPrice = $("#aCPriceForm").val();
	var sCPrice = $("#sCPriceForm").val();
	var mBprice = $("#mBPriceForm").val();
	var mCprice = $("#mCPriceForm").val();
	
	$.ajax({
		url : "../PtviewSkuStockWS/editPtviewSkuStock.do",
		type : "POST",
		data : {
			numberCode : checkedSkuStock,
			outTkview : minTkview,
			stock : stock,
			bPrice : xyzSetPrice(bPrice), 
            cPrice : xyzSetPrice(cPrice),
            aBPrice : xyzSetPrice(aBPrice),
            aCPrice : xyzSetPrice(aCPrice),
            sBPrice : xyzSetPrice(sBPrice), 
            sCPrice : xyzSetPrice(sCPrice),
            mBPrice : xyzSetPrice(mBprice),
            mCPrice : xyzSetPrice(mCprice)
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				window.location.reload(); 
				$(".aPrice").hide();
				$(".price").hide();
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

function getShipmentTravelDate(){
	var shipObj = null;
	$.ajax({
		url:"../PtviewSkuStockWS/getShipmentTravelDate.do",
		type:"POST",
		data:{
			shipment : shipment
		},
		async:false,
		dataType:"json",
		success:function(data) {
			if(data.status==1){
				shipObj = data.content;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
	return shipObj;
}

/* 新增  */
function addPtviewStockButton(){
	var html = '<center>';
	html += '<form>';
	html += '<div style="height:160px;overflow:auto;">';
	html += '<table id="theahTr" cellpadding="4" cellspacing="4" style="width:500px;">';
	html += ' <thead>';
	html += '  <tr>';
	html += '	 <th>单品名称</th> ';
	html += '    <th>供应商代码</th> ';
	html += '    <th>供应商</th> ';
	html += '    <th>库存</th> ';
	html += '    <th>成本价</th> ';
	html += '    <th>成本说明</th> ';
	html += '   </tr>';
	html += '  </thead>';
	html += ' <tbody id="tkviewTbody"></tbody>';
	html += '</table></div>';
	
	html += '<table style="width:480px;margin-top:16px;text-align:center;">';
	html += ' <tr>';
	html += '  <th style="text-align:right;height:30px;"><p>日期&nbsp;</p></th>';
	html += '  <td style="text-align:left;">';
	html += '   <p>&nbsp;&nbsp;&nbsp;<span id="dateForm" style="width:100px;"></span><p>';
	html += '  </td>';
	html += '  <th style="text-align:right;height:30px;"><p>库存&nbsp;</p></th>';
	html += '  <td>';
	html += '   <p><input id="stockAddForm" type="text" style="width:80px;" />&nbsp;&nbsp;&nbsp;';
	html += '  <p></td>';
	html += ' </tr>';
	
	html += ' <tr>';
	html += '  <th style="text-align:right;"><p>&nbsp;&nbsp;分销价&nbsp;</p></th>';
	html += '  <td>';
	html += '   <p><input id="b_priceForm" type="text" style="width:150px;" /></p>';
	html += '  </td>';
	html += '  <th style="text-align:right;"><p>&nbsp;&nbsp;散客价&nbsp;</p></th>';
	html += '  <td>';
	html += '   <p><input id="c_priceForm" type="text" style="width:150px;" /></p>';
	html += '  </td>';
	html += ' </tr>';
	html += ' <tr>';
	html += '  <th style="text-align:right;"><p>单人分销价&nbsp;</p></th>';
	html += '  <td>';
	html += '   <p><input id="a_b_priceForm" class="aPriceInput" type="text" style="width:150px;"/></p>';
	html += '  </td>';
	html += '  <th style="text-align:right;"><p>&nbsp;&nbsp;单人散客价&nbsp;<p></th>';
	html += '  <td>';
	html += '   <p><input id="a_c_priceForm" class="aPriceInput" type="text" style="width:150px;"/><p>';
	html += '  </td>';
	html += ' </tr>';
	html += ' <tr>';
	html += '  <th style="text-align:right;"><p>1/2分销价&nbsp;</p></th>';
	html += '  <td>';
	html += '   <p><input id="s_b_priceForm" class="priceInput" type="text" style="width:150px;" /><p>';
	html += '  </td>';
	html += '  <th style="text-align:right;"><p>&nbsp;&nbsp;1/2散客价&nbsp;<p></th>';
	html += '  <td>';
	html += '   <p><input id="s_c_priceForm" class="priceInput" type="text" style="width:150px;" /></p>';
	html += '  </td>';
	html += ' </tr>';
	html += ' <tr class="priceTr">';
	html += '  <th style="text-align:right;"><p>3/4分销价&nbsp;<p></th>';
	html += '  <td>';
	html += '   <p><input id="m_b_priceForm" class="priceInput" type="text" style="width:150px;" /></p>';
	html += '  </td>';
	html += '  <th style="text-align:right;"><p>&nbsp;&nbsp;3/4散客价&nbsp;</p></th>';
	html += '  <td>';
	html += '   <p><input id="m_c_priceForm" class="priceInput" type="text" style="width:150px;" /></p>';
	html += '  </td>';
	html += ' </tr>';

	html += ' </table>';
	html += '</form>';
	html += '</center>';
	
	xyzdialog({
		dialog : 'dialogFormDiv_addSkuStockButton',
		title : '新增SKU日历库存',
		content : html,
		fit : false,
		width : 600,
		height : 540,
		buttons : [ {
			text : '确定',
			handler : function() {
				addSkuStockSubmit();
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_addSkuStockButton").dialog("destroy");
			}
		} ],
		onOpen : function() {
			
			$("#stockAddForm").numberbox({
				required : true,
				min : 0,
				validType :'length[1,5]',
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});
			$("#dateForm").html(shipObj.travelDate.substring(0, 10));
			
			$("#b_priceForm").numberbox({    
				required : true,
				min : 1,
				precision : 2,
				validType :'length[1,10]',
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});
			$("#c_priceForm").numberbox({    
				required : true,
				min : 1,
				precision : 2,
				validType :'length[1,10]',
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});
			
			$("#a_b_priceForm").numberbox({    
			    required : true,
			    min : 0.00,
				precision : 2,
				icons : [{
					iconCls : 'icon-clear',
					handler : function(e){
						$(e.data.target).numberbox('clear');
					}
				}],
				onChange : function(newValue, oldValue){
					aPriceOnChange();
				}
			}); 
			$("#a_c_priceForm").numberbox({    
			    required : true,
			    min : 0.00,
				precision : 2,
				icons : [{
					iconCls : 'icon-clear',
					handler : function(e){
						$(e.data.target).numberbox('clear');
					}
				}],
				onChange : function(newValue, oldValue){
					aPriceOnChange();
				}
			}); 
			
			$("#s_b_priceForm").numberbox({    
			    required : true,
			    min : 0.00,
				precision : 2,
				icons : [{
					iconCls : 'icon-clear',
					handler : function(e){
						$(e.data.target).numberbox('clear');
					}
				}],
				onChange : function(newValue, oldValue){
					priceInputOnChange();
				}
			}); 
			$("#m_b_priceForm").numberbox({    
			    required : true,
			    min : 0.00,
				precision : 2,
				icons : [{
					iconCls : 'icon-clear',
					handler : function(e){
						$(e.data.target).numberbox('clear');
					}
				}],
				onChange : function(newValue, oldValue){
					priceInputOnChange();
				}
			}); 
			$("#s_c_priceForm").numberbox({    
			    required : true,
			    min : 0.00,
				precision : 2,
				icons : [{
					iconCls : 'icon-clear',
					handler : function(e){
						$(e.data.target).numberbox('clear');
					}
				}],
				onChange : function(newValue, oldValue){
					priceInputOnChange();
				}
			}); 
			$("#m_c_priceForm").numberbox({    
			    required : true,
			    min : 0.00,
				precision : 2,
				icons : [{
					iconCls : 'icon-clear',
					handler : function(e){
						$(e.data.target).numberbox('clear');
					}
				}],
				onChange : function(newValue, oldValue){
				    priceInputOnChange();
				}
			});
			
			getStockByTkview();
		}
	});
	
}

function aPriceOnChange(){
	var aBPrice = $("#a_b_priceForm").numberbox('getValue');
	var aCPrice = $("#a_c_priceForm").numberbox('getValue');
	
	var sBPrice = $("#s_b_priceForm").numberbox('getValue');
	var mBprice = $("#m_b_priceForm").numberbox('getValue');
	var sCPrice = $("#s_c_priceForm").numberbox('getValue');
	var mCprice = $("#m_c_priceForm").numberbox('getValue');
	if (xyzIsNull(aBPrice) && xyzIsNull(aCPrice)) {
		$(".priceInput").numberbox({    
			required : true,
			disabled : false
		});
	}else {
		$(".priceInput").numberbox({    
			required:false,
			disabled:true,
			value:''
		}); 
	}
		
	if(xyzIsNull(sBPrice) && xyzIsNull(sCPrice) && xyzIsNull(mBprice) && xyzIsNull(mCprice)) {
		$(".aPriceInput").numberbox({
			required:true,
			disabled:false
		});
	}else {
		$(".aPriceInput").numberbox({
			required:false,
			disabled:true,
			value:''
		});
	}
}

function priceInputOnChange(){
	var aBPrice = $("#a_b_priceForm").numberbox('getValue');
	var aCPrice = $("#a_c_priceForm").numberbox('getValue');
	
	var sBPrice = $("#s_b_priceForm").numberbox('getValue');
	var mBprice = $("#m_b_priceForm").numberbox('getValue');
	var sCPrice = $("#s_c_priceForm").numberbox('getValue');
	var mCprice = $("#m_c_priceForm").numberbox('getValue');
	if (xyzIsNull(aBPrice) && xyzIsNull(aCPrice)) {
		$("#s_b_priceForm").numberbox({    
			required:true,
			disabled:false
		}); 
		$("#m_b_priceForm").numberbox({    
			required:true,
			disabled:false
		}); 
		$("#s_c_priceForm").numberbox({    
			required:true,
			disabled:false
		}); 
		$("#m_c_priceForm").numberbox({    
			required:true,
			disabled:false
		}); 
	}else {
		$("#s_b_priceForm").numberbox({    
			required:false,
			disabled:true,
			value:''
		}); 
		$("#m_b_priceForm").numberbox({    
			required:false,
			disabled:true,
			value:''
		}); 
		$("#sCPriceForm").numberbox({    
			required:false,
			disabled:true,
			value:''
		}); 
		$("#m_c_priceForm").numberbox({    
			required:false,
			disabled:true,
			value:''
		});
	}
	
	if(xyzIsNull(sBPrice) && xyzIsNull(sCPrice) && xyzIsNull(mBprice) && xyzIsNull(mCprice)) {
		$("#a_b_priceForm").numberbox({
			required:true,
			disabled:false
		});
		$("#a_c_priceForm").numberbox({
			required:true,
			disabled:false
		});
	}else {
		$("#a_b_priceForm").numberbox({
			required:false,
			disabled:true,
			value:''
		});
		$("#a_c_priceForm").numberbox({
			required:false,
			disabled:true,
			value:''
		});
   }
}

var minStock = 0;
var minPrice = 0;
function getStockByTkview(value){
	
	$.ajax({
		url:"../PtviewSkuStockWS/getStockByTkview.do",
		type:"POST",
		data:{
			shipment : shipment,
			ptviewSku : ptviewSku
		},
		async:false,
		dataType:"json",
		success:function(data) {
			if(data.status==1){
				var content = data.content;
				var stockList = content.stockList;
				
				$("#theahTr").show();
				$("#tkviewTbody").empty();
				var html = '';
				if(stockList.length > 0){
					var tkviewNumber = '';
					for(var t = 0; t < stockList.length; t++){
						var stockObj = stockList[t];
						html += '<tr id="stcokTr_'+ stockObj.numberCode +'" class="stcokTr_'+stockObj.tkview+'">';
						html += ' <td>';
						if(xyzIsNull(tkviewNumber) || tkviewNumber != stockObj.tkview){
							tkviewNumber = stockObj.tkview;
							boolean = true;
							var tkviewNameCn = stockObj.tkviewNameCn.substring(12);
							if(tkviewNameCn.length > 6){
								tkviewNameCn = xyzGetDiv(tkviewNameCn, 0, 6)+"...";
							}
							html += tkviewNameCn;
						}
						html += '<input id="tkviewHidden_'+ stockObj.numberCode +'" type="hidden" value="'+ tkviewNumber +'"/>';
						html += '</td>';
						html += ' <td>'+ stockObj.providerMark +'</td>';
						var providerNameCn = stockObj.providerNameCn;
						if(providerNameCn.length > 6){
							providerNameCn = xyzGetDiv(stockObj.providerNameCn,0,6)+"...";
						}
						html += ' <td>'+ providerNameCn +'</td>';
						var type = stockObj.type==1?stockObj.stock:"现询";
						html += ' <td>'+ type +'</td>';
						html += ' <td>'+ xyzGetPrice(stockObj.cost) +'</td>';
						html += ' <td>'+ stockObj.costRemark +'</td>';
						html += '</tr>';
					}
					$("#tkviewTbody").append(html);
					
					$("#stcokTr_"+stockList[0].numberCode).attr("style","color:red;");
					
					minStock = stockList[0].stock;
					minTkview = stockList[0].tkview;
					var cost = xyzGetPrice(stockList[0].cost);//成本价
					minPrice = parseFloat(cost);
					$("#stockAddForm").numberbox('setValue', minStock);
				}else{
					top.$.messager.alert("温馨提示","请先新增单品及库存信息!","info");
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

function addSkuStockSubmit(){
	if(!$("form").form('validate')){
		return false;
	}
	if($(this).is('#tkviewTbody')){
		if(!xyzIsNull($("input[id^='tkviewHidden_']").eq(0).prop('id'))){
			top.$.messager.alert("温馨提示","请先新增单品及库存信息!","info");
			return false;
		}
	}
	var date = $("#dateForm").html();
	var stock = $("#stockAddForm").numberbox('getValue');
	var stockNumberCode = $("input[id^='tkviewHidden_']").eq(0).prop('id').split('_')[1];
	var bPrice = $("#b_priceForm").numberbox('getValue');
	var cPrice = $("#c_priceForm").numberbox('getValue');
	var aBPrice = $("#a_b_priceForm").numberbox('getValue');
	var sBPrice = $("#s_b_priceForm").numberbox('getValue');
	var aCPrice = $("#a_c_priceForm").numberbox('getValue');
	var sCPrice = $("#s_c_priceForm").numberbox('getValue');
	var mBprice = $("#m_b_priceForm").numberbox('getValue');
	var mCprice = $("#m_c_priceForm").numberbox('getValue');
	
	$.ajax({
		url : "../PtviewSkuStockWS/addPtviewSkuStock.do",
		type : "POST",
		data : {
			ptviewSku : ptviewSku,
			outTkview : minTkview,
			date : date,
			bPrice : xyzSetPrice(bPrice), 
            cPrice : xyzSetPrice(cPrice),
            aBPrice : xyzSetPrice(aBPrice),
            aCPrice : xyzSetPrice(aCPrice),
            sBPrice : xyzSetPrice(sBPrice), 
            sCPrice : xyzSetPrice(sCPrice),
            mBPrice : xyzSetPrice(mBprice),
            mCPrice : xyzSetPrice(mCprice),
			stock : stock,
			outStock : stockNumberCode
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#dialogFormDiv_addSkuStockButton").dialog("destroy");
				top.$.messager.alert("提示","操作成功!","info");
				window.location.reload(); 
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deletePtviewStockButton(){
	
	if(xyzIsNull(checkedSkuStock)){
		top.$.messager.alert("提示","请先勾选需要删除的对象!","info");
		return;
	}
	
	$.messager.confirm('确认','您确认想要删除库存信息吗？',function(r){    
		if(r){
			$.ajax({
				url : '../PtviewSkuStockWS/deletePtviewSkuStock.do',
				type : "POST",
				data : {
					numberCode : checkedSkuStock,
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.status == 1) {
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