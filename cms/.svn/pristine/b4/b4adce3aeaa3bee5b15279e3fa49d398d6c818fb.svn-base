var calendarData=[];

var myCalendar;
var cruiseNumber = '';
$(document).ready(function() {
	var options = {
	      width: '600px',
	      height: '500px',
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
		url : "../RTkview_ViewWS/getRoyalCabinList.do",
		type : "POST",
		async : false,
		data:{
			cruise : cruiseNumber
		},
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var cabinList = data.content;
				if(cabinList.length == 0){
					return false;
				}
				
				var cabinHtml = '';
				var type = -1;
				for(var i = 0; i < cabinList.length; i++){
					var cabinObj = cabinList[i];
					var numberCode = cabinObj.numberCode;
					var nameCn = cabinObj.nameCn;
					var cabinType = cabinObj.type;
					if(type < 0 || type != cabinType){
						type = cabinType;
						if(type == 0){
							cabinHtml += "内舱房 ";
						}else if(type == 1){
							cabinHtml += "<br/>海景房 ";
						}else if(type == 2){
							cabinHtml += "<br/>阳台房 ";
						}else{
							cabinHtml += "<br/>套   房 ";
						}
					}
					cabinHtml += '<span class="label label-warning" id="span_'+ numberCode +'" style="width:66px;">'+ xyzGetDiv(nameCn, 0, 10) +'</span> ';
				}
				$("#cabinDivs").html(cabinHtml);
				$("#cabinDivs").attr("style","max-height:86px;overflow:auto;");
				
				for(var t = 0; t < cabinList.length; t++){
					var tempId = cabinList[t].numberCode;
					$("#span_"+tempId).click(function(){
						var cabinNumber = $(this).prop('id').split('_')[1];
						getTkviewListByCabinGroup(cabinNumber);
					});
				}
				getTkviewListByCabinGroup(cabinList[0].numberCode);
			}
		}
	});
	
	//日历单元格点击事件
	dateClick();
	
});

//舱型Div点击事件
function getTkviewListByCabinGroup(cabinNumber){
	$("#baseInfo").empty();
	$(".price-table").css("display","none");
	$("#tableDiv").css("display","none");
	
	$(".label").each(function(){
		var tempCabin = $(this).prop('id').split('_')[1];
		if(tempCabin == cabinNumber){
			$("#span_"+tempCabin).attr("class","label label-click");
		}else{
			$("#span_"+tempCabin).attr("class","label label-warning");
		}
	});
	
	$.ajax({
		url :"../RTkview_ViewWS/getRoyalTkviewDateList.do",
		type:"POST",
		data:{
			cruise : cruiseNumber,
			cabin : cabinNumber
		},
		async:false,
		dataType:"json",
		success:function(data) {
			if(data.status == 1){
				var tkviewList = data.content.tkviewList;
				var dateList = data.content.mothList;
				
				if(dateList.length < 1){
					return false;
				}
				
				for(var i = 0; i < tkviewList.length; i++){
					calendarData[new Date(tkviewList[i].shipmentTravelDate).Format("yyyy-M-d")]=JSON.stringify(tkviewList[i]);
				}
				myCalendar._options.tkviewData = calendarData;
				myCalendar.update();
				closeAllTabs("dateTabs");
				
				//生成月份tab
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
			url:"../RTkview_ViewWS/getRoyalTkviewPriceList.do",
			type:"POST",
			data:{
				cruise : cruiseNumber,
				tkview : tkviewNumber,
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
					var tkviewList = content.tkviewList;
					stockList = content.stockList;
					
					var tkviewHtml = '';
					for(var t = 0; t < tkviewList.length; t++){
						var tkviewObj = tkviewList[t];
						tkviewHtml += '<tr>';
						tkviewHtml += ' <td>航期代码：'+ tkviewObj.shipmentMark +'</td>';
						tkviewHtml += ' <td style="width:40%">';
						tkviewHtml += '  出发日期：'+ tkviewObj.shipmentTravelDate.substring(0,11);
						tkviewHtml += ' </td>';
						tkviewHtml += '</tr>';
						tkviewHtml += '<tr>';
						tkviewHtml += ' <td>所属邮轮：'+ tkviewObj.cruiseNameCn +'</td>';
						tkviewHtml += ' <td>邮轮代码：'+ tkviewObj.cruiseMark +'</td>';
						tkviewHtml += '</tr>';
						tkviewHtml += '<tr>';
						tkviewHtml += ' <td>所属舱型：'+ tkviewObj.cabinNameCn +'</td>';
						tkviewHtml += ' <td>舱型代码：'+ tkviewObj.cabinMark +'</td>';
						tkviewHtml += '</tr>';
						tkviewHtml += '<tr>';
						tkviewHtml += ' <td>单名名称：'+ tkviewObj.nameCn +'</td>';
						tkviewHtml += ' <td>单品代码：'+ tkviewObj.mark +'</td>';
						tkviewHtml += '</tr>';
					}
					$("#baseInfo").html(tkviewHtml);
					
					if(stockList.length == 0){
						$("#noMsg").css("display","block");
						$(".price-table").css("display","none");
						$("#tableDiv").css("display","none");
					}else{
						$("#noMsg").css("display","none");
						$(".price-table").css("display","block");
						$("#tableDiv").css("display","block");
						
						var stockHtml = '';
						for(var s = 0; s < stockList.length; s++){
							var stockObj = stockList[s];
							stockHtml += '<tr>';
							stockHtml += ' <td>'+ stockObj.priceMark +'</td>';
							stockHtml += ' <td>'+ stockObj.priceDesc +'</td>';
							stockHtml += ' <td>'+ stockObj.priceEffDate +'</td>';
							stockHtml += ' <td>'+ stockObj.priceEndDate +'</td>';
							stockHtml += ' <td>'+ stockObj.singleRoom +'</td>';
							stockHtml += ' <td>'+ stockObj.doubleRoom +'</td>';
							stockHtml += ' <td>'+ stockObj.tripleRoom +'</td>';
							stockHtml += ' <td>'+ stockObj.quadRoom +'</td>';
							stockHtml += '</tr>';
						}
						$("#stockListInfo").html(stockHtml);
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