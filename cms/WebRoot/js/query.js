
var totalDataList=[];
var calendarData=[];
var array=[];
var queryList=[];
var cabinList=[];
var myCalendar;
var providerList;
var token;
$(document).ready(function() {
	
	if($(window).width()>1800){
		$("#stockTable").css("margin-left","-180px");
		$("#windowButton").remove();
	}
	
	layui.use('laydate', function(){
		  var laydate = layui.laydate;
		//日期范围
		  laydate.render({
			    elem: '#startDate'
			    ,min: 0 
		});
		  
		  laydate.render({
			    elem: '#endDate'
			    ,min: 0 
		});
	});
	
	
	layui.use('element', function(){
		  var $ = layui.jquery
		  ,element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
		  
		  //触发事件
		  var active = {
		    tabAdd: function(){
		      //新增一个Tab项
		      element.tabAdd('demo', {
		        title: '新选项'+ (Math.random()*1000|0) //用于演示
		        ,content: '内容'+ (Math.random()*1000|0)
		        ,id: new Date().getTime() //实际使用一般是规定好的id，这里以时间戳模拟下
		      })
		    }
		    ,tabDelete: function(othis){
		      //删除指定Tab项
		      element.tabDelete('demo', '44'); //删除：“商品管理”
		      
		      
		      othis.addClass('layui-btn-disabled');
		    }
		    ,tabChange: function(){
		      //切换到指定Tab项
		      element.tabChange('demo', '22'); //切换到：用户管理
		    }
		  };
		  
		  $('.site-demo-active').on('click', function(){
		    var othis = $(this), type = othis.data('type');
		    active[type] ? active[type].call(this, othis) : '';
		  });
		  
		  //Hash地址的定位
		  var layid = location.hash.replace(/^#test=/, '');
		  element.tabChange('test', layid);
		  
		  element.on('tab(test)', function(elem){
		    location.hash = 'test='+ $(this).attr('lay-id');
		  });
		  
		});
	
		layui.use('form', function(){
		  var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
		  //但是，如果你的HTML是动态生成的，自动渲染就会失效
		  //因此你需要在相应的地方，执行下述方法来手动渲染，跟这类似的还有 element.init();
		  $(".layui-select").css("width","235px");
		  $(".layui-form-label").css("width","100px");
		  
		  form.render();
		  
		$("#cruiseSelect").parents(".layui-select").find("dd").click(function(){
			var flag=true;
			var html=$(this).html();
			$("#cruiseQuery span").each(function(){
				console.log($(this).html());
				console.log(html);
				if($(this).html()==html){
					flag=false;
					return false;
				}
			});
			if(flag){
				$("#cruiseQuery").append('<div class="layui-unselect layui-form-checkbox layui-form-checked cruise-select" lay-skin=""><span>'+$(this).html()+'</span><i  class="layui-icon">&#xe618;</i></div>');
				queryList=totalDataList.concat();
				init();
				initCabinSelect();
			}
			
		});
		
		$("#providerSelect").parents(".layui-select").find("dd").click(function(){
			var flag=true;
			var html=$(this).html();
			$("#providerQuery span").each(function(){
				if($(this).html()==html){
					flag=false;
					return false;
				}
			});
			if(flag){
				$("#providerQuery").append('<div class="layui-unselect layui-form-checkbox layui-form-checked provider-select" lay-skin=""><span>'+$(this).html()+'</span><i class="layui-icon">&#xe618;</i></div>');
				 init();
			}
		});
		
		$("#placeSelect").parents(".layui-select").find("dd").click(function(){
			var flag=true;
			var html=$(this).html();
			$("#placeQuery span").each(function(){
				if($(this).html()==html){
					flag=false;
					return false;
				}
			});
			if(flag){
				$("#placeQuery").append('<div class="layui-unselect layui-form-checkbox layui-form-checked place-select" lay-skin=""><span>'+$(this).html()+'</span><i class="layui-icon">&#xe618;</i></div>');
				 init();
			}
		});
		
		});   
	
	var options = {
		      width: '700px',
		      height: '560px',
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
		 
		 $.ajax({
				url : "QueryResourcesWS/getTkviewStockList.do",
				type : "POST",
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						
						totalDataList=data.content.stockList;
						queryList=[];
						
						for(var i=0;i<totalDataList.length;i++){
							var obj={};
							obj.date=totalDataList[i].date;
							obj.min=totalDataList[i].min;
							obj.max=totalDataList[i].max;
							obj.stocks=totalDataList[i].stocks.concat();
							queryList[queryList.length]=obj;
						}
				
						var cruiseList=data.content.cruiseList;
						var companyList=data.content.companyList;
						providerList=data.content.providerList;
						var startPlaceList=data.content.startPlaceList;
						token=data.content.token;
						cabinList=data.content.cabinList;
						
						var selectHtml="<option value=''></option>";
						for(var i=0;i<companyList.length;i++){
							var temp="";
							for(var j=0;j<cruiseList.length;j++){
								if(cruiseList[j].company==companyList[i].numberCode){
									temp+="<option value='"+cruiseList[j].numberCode+"'>"+cruiseList[j].nameCn+"</option>";
								}
							}
							if(temp!=""){
								selectHtml+="<optgroup label='"+companyList[i].nameCn+"'>"+temp+"</optgroup>";
							}
						}
						
						$("#cruiseSelect").html(selectHtml);
						$("#cruiseCount").html("("+cruiseList.length+")");
						
						selectHtml="<option value=''></option>";
						for(var i=0;i<providerList.length;i++){
							selectHtml+="<option value='"+providerList[i].numberCode+"'>"+providerList[i].nameCn+"</option>";
						}
						$("#providerSelect").html(selectHtml);
						$("#providerCount").html("("+providerList.length+")");
						
						selectHtml="<option value=''></option>";
						for(var i=0;i<startPlaceList.length;i++){
							selectHtml+="<option value='"+startPlaceList[i]+"'>"+startPlaceList[i]+"</option>";
						}
						$("#placeSelect").html(selectHtml);
						$("#placeCount").html("("+startPlaceList.length+")");
						
						init();
						
						$(document).on("click",".sc-mark",function(){
							$("#extraInfo").css("display","block");
							var obj= JSON.parse(calendarData[$(this).attr("data")]);
							var stocks=obj.stocks;
							var infoHtml="<span style='font-size:20px;'>日期 : <span style='color: #f60;'>"+$(this).attr("data")+"</span>";
							infoHtml+="&nbsp;&nbsp;&nbsp;&nbsp;价格区间 : <span style='color: #f60;'><dfn>¥</dfn>"+parseInt(obj.min/100)+"-<dfn>¥</dfn>"+parseInt(obj.max/100)+"</span>";
							infoHtml+="&nbsp;&nbsp;&nbsp;&nbsp;资源个数 : <span style='color: #f60;'>"+stocks.length+"</span>";
							infoHtml+="</span>";
							$("#resourceInfo").html(infoHtml);
							var html="";
							var stockMap={};
							for(var i=0;i<stocks.length;i++){
								var tkviewName=stocks[i].tkviewNameCn;
								if(tkviewName.indexOf("[")!=-1){
									tkviewName=tkviewName.substring(tkviewName.indexOf("]")+1);
								}
								var obj=stockMap[stocks[i].cruiseNameCn];
								if(xyzIsNull(obj)){
									stockMap[stocks[i].cruiseNameCn]={};
								}
								var arr=stockMap[stocks[i].cruiseNameCn][tkviewName];
								if(xyzIsNull(arr)){
									stockMap[stocks[i].cruiseNameCn][tkviewName]=[];
								}
								stockMap[stocks[i].cruiseNameCn][tkviewName].push(stocks[i]);
							}
							
							for(var obj in stockMap){
								var subObj=stockMap[obj];
								var count=0;
								for(var sub in subObj){
									var stoObj=subObj[sub];
									count+=stoObj.length;
								}
								var flag=true;
								html+="<tr>";
								html+="<td rowspan='"+count+"'>"+obj+"</td>";
								
								for(var sub in subObj){
									
									var stoObj=subObj[sub];
									
									for(var i=0;i<stoObj.length;i++){
										if(!flag){
											flag=false;
											html+="<tr>";
										}
										if(!xyzIsNull(stoObj[i].remark)){
											html+="<td style='padding-left:20px;'><img title='"+stoObj[i].remark+"' class='tkviewRemark' src='image/query/remark.png' style='width:20px;cursor:pointer;margin-left:-20px;'>"+sub+"</td>";
										}else{
											html+="<td style='padding-left:20px;'>"+sub+"</td>";
										}
										html+="<td><span class='provider' style='cursor:pointer;'>"+stoObj[i].providerNameCn+"</span></td>";
										var airwayNameCn=stoObj[i].airwayNameCn;
										if(airwayNameCn.indexOf("】")!=-1){
											airwayNameCn=airwayNameCn.substring(airwayNameCn.indexOf("】")+1);
										}
										if(!xyzIsNull(stoObj[i].day)){
											var day=parseInt(stoObj[i].day);
											airwayNameCn+="【"+day+"天"+(day-1)+"晚】";
										}
										html+="<td>"+airwayNameCn+"</td>";
										html+="<td>"+parseInt(stoObj[i].cost/100)+"</td>";
										html+="<td>"+stoObj[i].stock+"</td>";
										html+="</tr>";
									}
									
								}
							}
							
							$("#resourceTable").html(html);
							
							
							
						}); 
						
						
						
					
						
						$(document).on("click",".layui-form-checkbox",function(){
							var id=$(this).parents("span").attr("id");
							
							if(id=="cruiseQuery"){
								if($(this).hasClass("layui-form-checked")){
									$(this).removeClass("layui-form-checked");
									$(this).removeClass("cruise-select");
								}else{
									$(this).addClass("layui-form-checked");
									$(this).addClass("cruise-select");
								}
								initCabinSelect();
							}else if(id=="cabinQuery"){
								if($(this).hasClass("layui-form-checked")){
									$(this).removeClass("layui-form-checked");
									$(this).removeClass("cabin-select");
								}else{
									$(this).addClass("layui-form-checked");
									$(this).addClass("cabin-select");
								}
							}else if(id=="providerQuery"){
								if($(this).hasClass("layui-form-checked")){
									$(this).removeClass("layui-form-checked");
									$(this).removeClass("provider-select");
								}else{
									$(this).addClass("layui-form-checked");
									$(this).addClass("provider-select");
								}
							}else if(id=="placeQuery"){
								if($(this).hasClass("layui-form-checked")){
									$(this).removeClass("layui-form-checked");
									$(this).removeClass("place-select");
								}else{
									$(this).addClass("layui-form-checked");
									$(this).addClass("place-select");
								}
							}
							 init();
						});
					
						
					}else{
						if(data.msg.indexOf("请重新登录")!=-1){
							
							var html='<div class="layui-form">';
							
							html+='<div class="layui-form-item" style="margin-top:30px;">';
							html+='<label class="layui-form-label">用户名</label>';
							html+='<div class="layui-input-inline">';
							html+='<input type="text" id="username"   autocomplete="off" class="layui-input">';
							html+='</div>';
							html+=' </div>';
							
							html+='<div class="layui-form-item">';
							html+='<label class="layui-form-label">密码</label>';
							html+='<div class="layui-input-inline">';
							html+='<input type="password"  id="password" autocomplete="off" class="layui-input">';
							html+='</div>';
							html+=' </div>';
							
							html+='<div class="layui-form-item">';
							html+=' <div class="layui-input-block" style="margin-left:180px;margin-top:20px;">';
							html+='<button class="layui-btn" id="loginButton">登陆</button>';
							html+=' </div>';
							html+=' </div>';
							
							html+=' </div>';
							
							
							layer.open({
								  type: 1,
								  title:'登陆',
								  skin: 'layui-layer-rim', //加上边框
								  area: ['420px', '240px'], //宽高
								  content: html
								});
						}else{
							layer.msg(data.msg);
						}
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown) {
					top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
				}
			});
		 
		 $("#queryButton").click(function(){
			 
			 queryList=[];
			 
			 var min=$("#min").val();
			 var max=$("#max").val();
			 var startDate=$("#startDate").val();
			 var endDate=$("#endDate").val();
			 if(!xyzIsNull(min)){
				 min=min*100;
			 }
			 if(!xyzIsNull(max)){
				 max=max*100;
			 }
		
			 for(var i=0;i<totalDataList.length;i++){
				
				 if(!xyzIsNull(min)&&totalDataList[i].max<min){
					continue;
				 }
				 if(!xyzIsNull(max)&&totalDataList[i].min>max){
					continue;
				 }
				 if(!xyzIsNull(startDate)&&totalDataList[i].date<startDate){
					 continue;
				 } 
				 if(!xyzIsNull(endDate)&&totalDataList[i].date>endDate){
					 continue;
				 }
				 var stocks=totalDataList[i].stocks;
				 var list=[];

				 var obj={};
				 for(var j=0;j<stocks.length;j++){
					 var price=parseInt(stocks[j].cost);
				 	if(!xyzIsNull(min)&&price<min){
						continue;
					 }
				 	if(!xyzIsNull(max)&&price>max){
						continue;
					 }
				 	list[list.length]=stocks[j];
				 }
				 
				 if(list.length==0){
						continue;
				 }
				 
				 var tempMin=parseInt(list[0].cost);
				 var tempMax=parseInt(list[0].cost);
				 for(var j=0;j<list.length;j++){
				 	if(tempMin>list[j].cost){
				 		tempMin=list[j].cost;
				 	}
				 	if(tempMax<list[j].cost){
				 		tempMax=list[j].cost;
				 	}
				 }
				 obj.min=tempMin;
				 obj.max=tempMax;
				 obj.stocks=list;
				 obj.date=totalDataList[i].date;
				 queryList[queryList.length]=obj;
			 }
			 init();
			 $("#extraInfo").css("display","none");
		 });
		 
		 
		 $("#clearButton").click(function(){
			 $("#min").val("");
			 $("#max").val("");
			 $("#startDate").val("");
			 $("#endDate").val("");
			 
			 $("#cruiseQuery").html("");
			 $("#cabinQuery").html("");
			 $("#providerQuery").html("");
			 $("#placeQuery").html("");
			 
				queryList=[];
				for(var i=0;i<totalDataList.length;i++){
					var obj={};
					obj.date=totalDataList[i].date;
					obj.min=totalDataList[i].min;
					obj.max=totalDataList[i].max;
					obj.stocks=totalDataList[i].stocks.concat();
					queryList[queryList.length]=obj;
				}
				init();
			 
		 });
		 
		 $("#windowButton").click(function(){
			 window.open("../query.html");
		 });
		 
		 $("#excelButton").click(function(){
			 $.ajax({
					url : "QueryResourcesWS/getExcel.xyz",
					type : "POST",
					async : false,
					dataType : "json",
					success : function(data) {
						if(data.status==1){
							
						}else{
							top.$.messager.alert("警告",data.msg,"warning");
						}
					},
					error:function(XMLHttpRequest, textStatus, errorThrown) {
						top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
					}
				});
		 });
		 
		 $(document).on('click', '.provider', function() {
			 var provider=$(this).html();
			 console.log(provider);
			 for(var i=0;i<providerList.length;i++){
				 if(provider==providerList[i].nameCn){
					 console.log(providerList[i]);
					 layer.tips('默认就是向右的', $(this));
				 }
			 }
	     });
	
});

function init(){
	
	/*$.ajax({
		url : "QueryResourcesWS/getValidateData.do",
		type : "POST",
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				if(data.content!=token){
					token=data.content;
					layer.msg("邮轮资源数据有更新");
					 window.location.reload();
				}
			}else{
				
				if(data.msg.indexOf("请重新登录")!=-1){
					
					var html='<div class="layui-form">';
					
					html+='<div class="layui-form-item" style="margin-top:30px;">';
					html+='<label class="layui-form-label">用户名</label>';
					html+='<div class="layui-input-inline">';
					html+='<input type="text" id="username"   autocomplete="off" class="layui-input">';
					html+='</div>';
					html+=' </div>';
					
					html+='<div class="layui-form-item">';
					html+='<label class="layui-form-label">密码</label>';
					html+='<div class="layui-input-inline">';
					html+='<input type="password"  id="password" autocomplete="off" class="layui-input">';
					html+='</div>';
					html+=' </div>';
					
					html+='<div class="layui-form-item">';
					html+=' <div class="layui-input-block" style="margin-left:180px;margin-top:20px;">';
					html+='<button class="layui-btn" id="loginButton">登陆</button>';
					html+=' </div>';
					html+=' </div>';
					
					html+=' </div>';
					
					layer.open({
						  type: 1,
						  title:'登陆',
						  skin: 'layui-layer-rim', //加上边框
						  area: ['420px', '240px'], //宽高
						  content: html
						});
				}else{
					layer.msg(data.msg);
				}
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			layer.msg(textStatus);
		}
	});*/
	
	var dataList=[];
	for(var i=0;i<queryList.length;i++){
		var obj={};
		obj.date=queryList[i].date;
		obj.min=queryList[i].min;
		obj.max=queryList[i].max;
		obj.stocks=queryList[i].stocks.concat();
		dataList[dataList.length]=obj;
	}
	
	if(dataList.length==0){
		return;
	}
	
	var tempList=[];
	var queryFlag=0;
	for(var i=0;i<dataList.length;i++){
		
		var stocks=dataList[i].stocks;
		var tempStocks=[];
		
		for(var j=0;j<stocks.length;j++){
			
			if($("#cruiseQuery .layui-form-checked").length>0){
				queryFlag=1;
				var flag=0;
				$("#cruiseQuery .layui-form-checked").each(function(){
					if($(this).find("span").html()==stocks[j].cruiseNameCn){
						flag=1;
						return true;
					}
				});
				if(flag==0){
					continue;
				}
			}
			
			if($("#cabinQuery .layui-form-checked").length>0){
				queryFlag=1;
				var flag=0;
				$("#cabinQuery .layui-form-checked").each(function(){
					if($(this).find("span").html()==stocks[j].cabinNameCn){
						flag=1;
						return true;
					}
				});
				if(flag==0){
					continue;
				}
			}
			
			if($("#providerQuery .layui-form-checked").length>0){
				queryFlag=1;
				var flag=0;
				$("#providerQuery .layui-form-checked").each(function(){
					if($(this).find("span").html()==stocks[j].providerNameCn){
						flag=1;
						return true;
					}
				});
				if(flag==0){
					continue;
				}
			}
			
			if($("#placeQuery .layui-form-checked").length>0){
				if(xyzIsNull(stocks[j].airwayNameCn)){
					continue;
				}
				queryFlag=1;
				var flag=0;
				$("#placeQuery .layui-form-checked").each(function(){
					var index=stocks[j].airwayNameCn.indexOf("】");
					var place="";
					if(index!=-1){
						place=stocks[j].airwayNameCn.substring(index+1);
						index=place.indexOf("-");
						if(index!=-1){
							palce=$.trim(place.substring(0,index));
						}
					}else{
						index=stocks[j].airwayNameCn.indexOf("-");
						if(index!=-1){
							place=$.trim(stocks[j].airwayNameCn.substring(index));
						}
					}
					if($(this).find("span").html()==palce){
						flag=1;
						return true;
					}
				});
				if(flag==0){
					continue;
				}
			}
			
			tempStocks[tempStocks.length]=stocks[j];
		}
		
		dataList[i].stocks=tempStocks;
		
		if(dataList[i].stocks.length!=0){
			tempList[tempList.length]=dataList[i];
		}
	}
	
	if(queryFlag==1){
		dataList=tempList;
	}
	
	if(dataList.length==0){
		layer.msg("没有匹配的邮轮资源");
		 $("#extraInfo").css("display","none");
		$(".sc-mark").css("background-color","#ffffff");
		$(".sc-mark .day").css("color","#C1C0C0");
		$(".sc-mark .lunar-day").html("");
		return;
	}
	
	//初始化月份
	var monthArray=[];
	for(var i=0;i<dataList.length;i++){
		if(monthArray.lemgth==0){
			monthArray[0]=dataList[i].date.substring(0,7);
		}else{
			if(monthArray[monthArray.length-1]!=dataList[i].date.substring(0,7)){
				monthArray[monthArray.length]=dataList[i].date.substring(0,7);
			}
		}
	}
	//console.log(monthArray);
	var html='';
	for(var i=0;i<monthArray.length;i++){
		var month=monthArray[i].replace('-','年');
		month+='月';
		if(i==0){
			html+='<li date="'+monthArray[i]+'" class="layui-this">'+month+'</li>';
		}else{
			html+='<li date="'+monthArray[i]+'">'+month+'</li>';
		}
	}
	$(".layui-tab-title").html(html);
	
	var rows = dataList;
	calendarData=[];
	for(var i=0;i<rows.length;i++){
		calendarData[new Date(rows[i].date).Format("yyyy-M-d")] = JSON.stringify(rows[i]);
	}
	myCalendar._options.skuData = calendarData;
	myCalendar.update();
	
	var arr = dataList[0].date.split('-');
	myCalendar.update(arr[1],arr[0]);
	
	$(".layui-tab-title li").click(function(){
		var dateArry = $(this).attr("date");
		var arr = dateArry.split('-');
		myCalendar.update(arr[1], arr[0]);
	});
}

$(document).on("click","#loginButton",function(){
	 var username=$.trim($("#username").val());
	 var password=$.trim($("#password").val());
	 var password = $.md5(password).substr(8,16);
	 if(xyzIsNull(username)){
		 layer.msg("请输入用户名");
		 return;
	 }
	 if(xyzIsNull(password)){
		 layer.msg("请输入密码");
		 return;
	 }
	 
		$.ajax({
			url : "LoginWS/login.xyz",
			type : "POST",
			data : {
				username : username,
				password : password,
				indateHours : 0,
				phoneType : "pc",
				phoneCode : "pc"
			},
			async : false,
			dataType : "json",
			success : function(data) {
				if(data.status==1){
					addLoginCookie(data.content.apikey,7);
					 window.location.reload();
				}else{
					layer.msg(data.msg);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				layer.msg(textStatus);
			}
		});
});

/*$("#loginButton").click(function(){
	 var username=$.trim($("#username").val());
	 var password=$.trim($("#password").val());
	 var password = $.md5(password).substr(8,16);
	 if(xyzIsNull(username)){
		 layer.msg("请输入用户名");
		 return;
	 }
	 if(xyzIsNull(password)){
		 layer.msg("请输入密码");
		 return;
	 }
	 
		$.ajax({
			url : "LoginWS/login.xyz",
			type : "POST",
			data : {
				username : username,
				password : password,
				indateHours : 0,
				phoneType : "pc",
				phoneCode : "pc"
			},
			async : false,
			dataType : "json",
			success : function(data) {
				if(data.status==1){
					addLoginCookie(data.content.apikey,7);
					 window.location.reload();
				}else{
					layer.msg(data.msg);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				layer.msg(textStatus);
			}
		});
	 
});*/

function initCabinSelect(){
	
	var cruiseArray=[];
	$("#cruiseQuery .cruise-select").each(function(){
		cruiseArray.push($(this).find("span").html());
	});
	
	if(cruiseArray.length==0){
		$("#cabinSelect").html("<option value=''></option>");
		return;
	}

	var selectHtml="<option value=''></option>";
	for(var i=0;i<cabinList.length;i++){
		var flag=false;
		for(var j=0;j<cruiseArray.length;j++){
			if(cruiseArray[j]==cabinList[i].cruiseCn){
				flag=true;
				break;
			}
		}
		if(!flag){
			continue;
		}
		var temp="";
		var list=cabinList[i].cabinList;
		for(var j=0;j<list.length;j++){
			temp+="<option value='"+list[j].numberCode+"'>"+list[j].nameCn+"</option>";
		}
		if(temp!=""){
			selectHtml+="<optgroup label='"+cabinList[i].cruiseCn+"'>"+temp+"</optgroup>";
		}
	}
	$("#cabinSelect").html(selectHtml);
	
	layui.use('form', function(){
		  var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
		  //但是，如果你的HTML是动态生成的，自动渲染就会失效
		  //因此你需要在相应的地方，执行下述方法来手动渲染，跟这类似的还有 element.init();
		  
		  form.render();
		  
		$("#cruiseSelect").parents(".layui-select").find("dd").click(function(){
			var flag=true;
			var html=$(this).html();
			$("#cruiseQuery span").each(function(){
				console.log($(this).html());
				console.log(html);
				if($(this).html()==html){
					flag=false;
					return false;
				}
			});
			if(flag){
				$("#cruiseQuery").append('<div class="layui-unselect layui-form-checkbox layui-form-checked cruise-select" lay-skin=""><span>'+$(this).html()+'</span><i  class="layui-icon">&#xe618;</i></div>');
				queryList=totalDataList.concat();
				init();
				initCabinSelect();
			}
			
		});
		
		$("#cabinSelect").parents(".layui-select").find("dd").click(function(){
			var flag=true;
			var html=$(this).html();
			$("#cabinQuery span").each(function(){
				if($(this).html()==html){
					flag=false;
					return false;
				}
			});
			if(flag){
				$("#cabinQuery").append('<div class="layui-unselect layui-form-checkbox layui-form-checked cabin-select" lay-skin=""><span>'+$(this).html()+'</span><i  class="layui-icon">&#xe618;</i></div>');
				init();
			}
		});
		
		$("#providerSelect").parents(".layui-select").find("dd").click(function(){
			var flag=true;
			var html=$(this).html();
			$("#providerQuery span").each(function(){
				if($(this).html()==html){
					flag=false;
					return false;
				}
			});
			if(flag){
				$("#providerQuery").append('<div class="layui-unselect layui-form-checkbox layui-form-checked provider-select" lay-skin=""><span>'+$(this).html()+'</span><i class="layui-icon">&#xe618;</i></div>');
				 init();
			}
		});
		
		$("#placeSelect").parents(".layui-select").find("dd").click(function(){
			var flag=true;
			var html=$(this).html();
			$("#placeQuery span").each(function(){
				if($(this).html()==html){
					flag=false;
					return false;
				}
			});
			if(flag){
				$("#placeQuery").append('<div class="layui-unselect layui-form-checkbox layui-form-checked place-select" lay-skin=""><span>'+$(this).html()+'</span><i class="layui-icon">&#xe618;</i></div>');
				 init();
			}
		});
		
	});   
}