var calendarData=[];
var myCalendar = null;

var stockJson = "";
var tkviewJson = "";
var clickDate = "";
$(document).ready(function() {
	var options = {
	      width: '600px',
	      height: '520px',
	      language: 'CH',            //语言(中:CH  英:EN)
	      showLunarCalendar: false,  //关闭阴历日期
	      showHoliday: false,        //关闭休假
	      showFestival: false,       //关闭节日
	      showLunarFestival: false,   //关闭农历节日
	      showSolarTerm: false,      //关闭二十四节气
	      showMark: true,            //显示标记
	      timeRange: {
	        startYear: 2017,
	        endYear: 2049
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
	 myCalendar = new SimpleCalendar('#container', options);
	
	$("#cruiseSpan").hide();
	//价格区间
	$(".prcieInput").numberbox({    
	    min : 0, 
	    precision : 2,
	    icons : [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).numberbox('clear');
			}
		}]
	});  
	//出发日期区间
	$(".dateInput").datebox({
		icons : [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).datebox('clear');
			}
		}]
	});
	
	//出发地
	xyzCombobox({
		combobox:'startPlace',
		url : '../ListWS/getStartPlace.do',
		mode: 'remote',
		icons : [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	//目的地
	xyzCombobox({
		combobox:'toPlace',
		url : '../ListWS/getToPlace.do',
		mode: 'remote',
		icons : [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	//邮轮
	xyzCombobox({
		combobox:'cruise',
		url : '../ListWS/getCruiseList.do',
		mode: 'remote',
		icons : [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}],
		onSelect : function(record){
			if(xyzIsNull(record.value)){
				$("#cruiseSpan").hide();
			}else{
				$("#cruiseSpan").show();
				$("#airway").combobox('clear');
				$("#cabin").combobox('clear');
			}
		},
		onChange : function(newValue, oldValue){
			if(xyzIsNull(newValue)){
				$("#cruiseSpan").hide();
			}else{
				$("#cruiseSpan").show();
				$("#airway").combobox('clear');
				$("#cabin").combobox('clear');
			}
		}
	});
	//航线
	xyzCombobox({
		combobox:'airway',
		url : '../ListWS/getAirwayListByCruise.do',
		mode: 'remote',
		onBeforeLoad : function(param){
			param.cruise = $("#cruise").combobox("getValue");
		},
		icons : [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	//舱型
	xyzCombobox({
		combobox:'cabin',
		url : '../ListWS/getCabinListByCruise.do',
		mode: 'remote',
		onBeforeLoad : function(param){
			param.cruise = $("#cruise").combobox("getValue");
		},
		icons : [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	//几天几晚
	$("#days").combobox({    
	    icons : [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	}); 
	/*$(".numInput").numberbox({    
	    min : 0, 
	    icons : [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).numberbox('clear');
			}
		}]
	});  */
	//供应商
	xyzCombobox({
		combobox:'provider',
		url : '../ListWS/getProviderList.do',
		mode: 'remote',
		icons : [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	
	initTable();
	
	/*查询*/
	$("#queryButton").click(function(){
		$("#stockTbody").empty();
		$("#noneMsg").hide();
		$("#tableDiv").hide();
		
	   $("#dateTabs ul").empty();
		loadTable();
	});
	
	//天数格添加点击事件。
	$(".item-nolunar").click(function() { 
		$("#stockTbody").empty();
		$("#noneMsg").hide();
		$("#tableDiv").hide();
		
		stockJson = $(this).find(".sc-stock").attr("stocks");
		tkviewJson = $(this).find(".sc-price").attr("tkviews");
		clickDate = $(this).find(".sc-stock").attr("date");
		
		if(!xyzIsNull(clickDate)){
			clickDateOper(tkviewJson, stockJson, clickDate);
		}
    });
	
});

var clickCalendarData=[];
var clickCalendar = null;

var stocks = "";
var tkviews = "";
var dates = "";
function loadTable(){
	
	var minPrice = $("#minPrice").numberbox("getValue");
	if(xyzIsNull(minPrice)){
		minPrice = 0;
	}
	var maxPrice = $("#maxPrice").numberbox("getValue");
	if(xyzIsNull(maxPrice)){
		maxPrice = 0;
	}
	var startDate = $("#startDate").datebox("getValue");
	var endDate = $("#endDate").datebox("getValue");
	var startPlace = $("#startPlace").combobox("getValue");
	var toPlace = $("#toPlace").combobox("getValue");
	var cruise = $("#cruise").combobox("getValue");
	var airway = $("#airway").combobox("getValue");
	var cabin = $("#cabin").combobox("getValue");
	var days = $("#days").combobox("getValue");
	if(xyzIsNull(parseInt(days))){
		days = 0;
	}
	var nights = -1;
	if(parseInt(days) > 0){
		nights = parseInt(days) -1;
	}
	var provider = $("#provider").combobox("getValue");
	
	var options = {
	      width: '600px',
	      height: '520px',
	      language: 'CH',            //语言(中:CH  英:EN)
	      showLunarCalendar: false,  //关闭阴历日期
	      showHoliday: false,        //关闭休假
	      showFestival: false,       //关闭节日
	      showLunarFestival: false,   //关闭农历节日
	      showSolarTerm: false,      //关闭二十四节气
	      showMark: true,            //显示标记
	      timeRange: {
	        startYear: 2017,
	        endYear: 2049
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
	clickCalendar = new SimpleCalendar('#container', options);
	
	$.ajax({
		url : "../QueryResourcesWS/queryTkviewStockList.do",
		type : "POST",
		data : {
			minPrice : minPrice,
			maxPrice : maxPrice,
            startDate : startDate, 
            endDate : endDate,
            startPlace : startPlace, 
            toPlace : toPlace,
            cruise : cruise, 
            airway : airway, 
            cabin : cabin,
            days : days, 
            nights : nights, 
            provider : provider
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var resultMap = data.content;
				var mothList = resultMap.mothList;
				var resultList = resultMap.resultList;
				
				for(var d = 0; d < resultList.length; d++){
					var dateObj = resultList[d];
					clickCalendarData[new Date(dateObj.date).Format("yyyy-MM-dd")] = JSON.stringify(dateObj);
				}
				clickCalendar._options.skuData = clickCalendarData;
				clickCalendar.update();
				
				for(var i = 0; i < mothList.length; i++){
					var moth = mothList[i]+"";
					var mothArry = moth.split('-');
					$("#dateTabs").tabs('add',{
					    title : mothArry[0]+'年'+mothArry[1]+'月',    
					    id : moth,
					    closable : false
					});
				}
				var moth = mothList[0].split('-');
				clickCalendar.update(moth[1],moth[0]);
				
				$("#dateTabs").tabs({
					onSelect : function(title, index){
						var moth = mothList[index]+"";
						var mothArry = moth.split('-');
						clickCalendar.update(mothArry[1], mothArry[0]);
						
						$(".sc-select-year").find("option[value='"+ mothArry[0] +"']").attr("selected",true);
						$(".sc-select-month").find("option[value='"+ parseFloat(mothArry[1]) +"']").attr("selected",true);
						initMark(mothList[index], clickCalendar, clickCalendarData);
				    }
				});
				
				//天数格添加点击事件。
				$(".item-nolunar").click(function() { 
					$("#stockTbody").empty();
					$("#noneMsg").hide();
					$("#tableDiv").hide();
					
					stocks = $(this).find(".sc-stock").attr("stocks");
					tkviews = $(this).find(".sc-price").attr("tkviews");
					dates = $(this).find(".sc-stock").attr("date");
					
					if(!xyzIsNull(dates)){
						clickDateOper(tkviews, stocks, dates);
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

/*获取日历库存及日期*/
function initTable(){
	var minPrice = $("#minPrice").numberbox("getValue");
	if(xyzIsNull(minPrice)){
		minPrice = 0;
	}
	var maxPrice = $("#maxPrice").numberbox("getValue");
	if(xyzIsNull(maxPrice)){
		maxPrice = 0;
	}
	var startDate = $("#startDate").datebox("getValue");
	var endDate = $("#endDate").datebox("getValue");
	var startPlace = $("#startPlace").combobox("getValue");
	var toPlace = $("#toPlace").combobox("getValue");
	var cruise = $("#cruise").combobox("getValue");
	var airway = $("#airway").combobox("getValue");
	var cabin = $("#cabin").combobox("getValue");
	var days = $("#days").numberbox("getValue");
	if(xyzIsNull(days)){
		days = 0;
	}
	/*var nights = $("#nights").numberbox("getValue");
	if(xyzIsNull(nights)){
		nights = -1;
	}*/
	var nights = days -1;
	var provider = $("#provider").combobox("getValue");
	
	$.ajax({
		url : "../QueryResourcesWS/queryTkviewStockList.do",
		type : "POST",
		data : {
			minPrice : minPrice,
			maxPrice : maxPrice,
            startDate : startDate, 
            endDate : endDate,
            startPlace : startPlace, 
            toPlace : toPlace,
            cruise : cruise, 
            airway : airway, 
            cabin : cabin,
            days : days, 
            nights : nights, 
            provider : provider
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var resultMap = data.content;
				var mothList = resultMap.mothList;
				//var dateList = resultMap.dateList;
				var resultList = resultMap.resultList;
				
				for(var d = 0; d < resultList.length; d++){
					var dateObj = resultList[d];
					calendarData[new Date(dateObj.date).Format("yyyy-MM-dd")] = JSON.stringify(dateObj);
				}
				myCalendar._options.skuData = calendarData;
				myCalendar.update();
				
				for(var i = 0; i < mothList.length; i++){
					var moth = mothList[i]+"";
					var mothArry = moth.split('-');
					$("#dateTabs").tabs('add',{
					    title : mothArry[0]+'年'+mothArry[1]+'月',    
					    id : moth,
					    closable : false
					});
				}
				var moth = mothList[0].split('-');
				myCalendar.update(moth[1],moth[0]);
				
				$("#dateTabs").tabs({
					onSelect : function(title, index){
						var moth = mothList[index]+"";
						var mothArry = moth.split('-');
						myCalendar.update(mothArry[1], mothArry[0]);
						
						$(".sc-select-year").find("option[value='"+ mothArry[0] +"']").attr("selected",true);
						$(".sc-select-month").find("option[value='"+ parseFloat(mothArry[1]) +"']").attr("selected",true);
						initMark(mothList[index], myCalendar, calendarData);
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

//初始化出行日期
function initMark(tempDate, calendar, calendarData){
	
	var year = $(".sc-select-year").val();
	var momth = $(".sc-select-month").val();
	calendar.updateSelect(year, momth);
	
	$(".sc-item").each(function(){
		$(this).find(".sc-price").remove();
		$(this).find(".sc-stock").remove();
		$(this).find(".day").css("color","#C1C0C0");
		
		momth = $(".sc-select-month").val();
		var day = parseInt($(this).find(".day").html());
		if($(this).hasClass("sc-othermenth")){
			if(day < 8){
				momth = momth+1;
			}if(day > 23){
				momth = momth-1;
			}
		}
		momth = momth < 10 ? "0"+momth :momth;
		day = day < 10 ? "0"+day : day;
		
		if(tempDate==(year+"-"+momth) && calendarData[year+"-"+momth+"-"+day]){
			var detail = $.parseJSON(calendarData[year+"-"+momth+"-"+day]);
			
			var minPrice = xyzGetPrice(detail.minPrice);
			var maxPrice = xyzGetPrice(detail.maxPrice);
			
			var arry = minPrice.toString().split('.');
			minPrice = arry[1]<1?arry[0] : arry[1] ;
			var arryMax = maxPrice.toString().split('.');
			maxPrice = arryMax[1]<1?arryMax[0] : arryMax[1];
			
			$(this).css("cursor","pointer");
			$(this).find(".day").css("color","#000000");
			var stockhtml = "";
			var pricehtml = "";
			if(detail.minStock < 1){
				stockhtml = "<span stocks='"+ detail.stockStr +"' date='"+ (year+"-"+momth+"-"+day) +"' ";
				stockhtml += "class='sc-stock' style='color:#a9a9a9;'>余"+ detail.minStock +"<span>";
				
				pricehtml = "<span tkviews='"+ detail.tkviewStr +"' class='sc-price' ";
				pricehtml += "style='font-size:14px;color:#a9a9a9;'>";
				if(!xyzIsNull(maxPrice)){
					minPrice = minPrice +"<br/>-<dfn style='color:#a9a9a9;'>¥</dfn>"+ maxPrice;
				}
				pricehtml += "<dfn style='color:#a9a9a9;'>¥</dfn>"+ minPrice +"<span>";
			}else{
				stockhtml = "<span stocks='"+ detail.stockStr +"' date='"+ (year+"-"+momth+"-"+day) +"' class='sc-stock'>余"+ detail.minStock +"<span>";
				if(!xyzIsNull(maxPrice)){
					minPrice = minPrice +"<br/>-<dfn>¥</dfn>"+ maxPrice;
				}
				pricehtml ="<span tkviews='"+ detail.tkviewStr +"' class='sc-price' style='font-size:14px;'><dfn>¥</dfn>"+ minPrice +"<span>";
			}
			$(this).find(".day").after(pricehtml);
			$(this).find(".day").after(stockhtml);
		}
		
	});
}

function clickDateOper(tkviewStr, stockStr, dateStr){
	
	$.ajax({
		url : "../QueryResourcesWS/clickDateOper.do",
		type : "POST",
		data : {
			tkviewJson : tkviewStr, 
			stockJson : stockStr, 
			date : dateStr
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status == 1){
				var list = data.content;
				
				$("#stockTbody").empty();
				if(list.length < 1){
					$("#noneMsg").show();
					$("#tableDiv").hide();
				}else{
					$("#noneMsg").hide();
					$("#tableDiv").show();
					
					var html = '';
					for(var k = 0; k < list.length; k++){
						var map = list[k];
						var cruise = map.cruise;  //邮轮名称
						var rowspan = map.rowspan;  //跨行
						var pd = true;
						
						var tkivewList = map.tkivewList;  //单品集合
						for(var t = 0; t < tkivewList.length; t++){
							var tkviewObj = tkivewList[t];
							
							var stockList = tkviewObj.stocks;  //库存集合
							for(var h = 0; h < stockList.length; h++){
								var stockObj = stockList[h];  
								
								html += '<tr id="tr_'+ tkviewObj.numberCode +'_'+ stockObj.numberCode +'">';
								if(pd){
									html += ' <td rowspan="'+ rowspan +'">'+ cruise +'</td>';
									pd = false;
								}
								if(h == 0){
									html += ' <td rowspan="'+ stockList.length +'">'+ tkviewObj.nameCn +'</td>';
								}
								html += ' <td>'+ stockObj.providerNameCn +'</td>';
								html += ' <td>'+ stockObj.stock +'</td>';
								html += ' <td>'+ xyzGetPrice(stockObj.cost) +'</td>';
								html += ' <td>'+ stockObj.costRemark +'</td>';
								html += '</tr>';
							}
							
						}
					}
					$("#stockTbody").html(html);
				}
			}
		}
	});
}