/*
 * Created by Administrator on 2016/12/22.
 */
/*$(function(){
    obtainHeight();
    $(window).resize(function(){
        obtainHeight();
    });
    $("#immediately").click(function(){
        $(".cruise-content").css("display","none");
        $(".appointment-success").css('display','block');
    });
});*/

$(document).ready(function() {
    obtainHeight();
    $(window).resize(function(){
        obtainHeight();
    });
    
    var params = getUrlParameters();
	tid = params.tid;
	notPass = params.notPass;
	fail = params.fail;
	$("#tidSpan").html(tid);
    if(fail=="fail"){
    	$("#failDiv").html("审核失败,请重新输入信息");
    }
    initData(tid,notPass);
    
    $("#immediately").click(function(){
    	dataSubmit();
    });
	
});

function obtainHeight(){
    var height=$(window).height();
    var borderAddHeight=height-175;
    $(".border-ddd").css('height',borderAddHeight+'px');
}

var json = [];
function initData(tid,notPass){
	
	$.ajax({
		url : '../ReserveWS/queryOrderListByTid.reserve',
		type : "POST",
		data : {
			tid : tid,
			notPass : notPass
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var taobaoList = data.content.taobaoList;
				var tbtList = data.content.tbtList;
				var orderList = data.content.orderList;
				
				$("#createdSpan").html(tbtList[0][1]);
				var reserveHtml = '';
				for(var t = 0; t < orderList.length; t++){
					var taobaoObj = taobaoList[t];  //淘宝订单对象
					var orderObj = orderList[t];  //平台订单对象
					var tempId = orderObj[3];   //淘宝票单号
					
					reserveHtml += '<div id="reserveDiv_'+ tempId +'">';
					/*商品信息*/
					reserveHtml += ' <div class="commodity-information">';
					reserveHtml += '  <div>商品名称：'+ taobaoObj[0] +'</div>';
					var strArry = taobaoObj[1].split(';');
					reserveHtml += '  <div class="pull-left">'+ strArry[0].replace(":","：") +'</div>';
					reserveHtml += '  <div class="pull-left">数量：'+ taobaoObj[2] +'</div>';
					reserveHtml += '  <div class="pull-left">'+ strArry[3].replace(":","：") +'</div>';
					reserveHtml += '  <div class="pull-left">出游日期：'+ orderObj[0].substring(0, 10) +'</div>';
					reserveHtml += '  <div class="clear"></div>';
					reserveHtml += ' </div>';
					
					if(!xyzIsNull(orderObj[4])){
						var personInfo = orderObj[4];
						var strJson = {
							oid : tempId,
							person : personInfo
						};
						json[json.length] = strJson;
					}
					
					/*出行人信息*/
					var count = taobaoObj[2] * orderObj[1];
					for(var p = 0; p < count; p++){
						reserveHtml += ' <div class="fill-inContent" id="personDiv_'+tempId+'_'+ p +'">';
						reserveHtml += '  <div>出行人'+ (p+1) +'：</div>';
						
						reserveHtml += '  <input type="hidden" id="numberForm_'+ tempId +'_'+ p +'">';
						reserveHtml += '  <div class="direct-input pull-left">';
						reserveHtml += '   <p>姓名：</p>';
						reserveHtml += '   <input type="text" id="realNameForm_'+ tempId +'_'+ p +'">';
						reserveHtml += '  </div>';
						
						reserveHtml += '  <div class="direct-input pull-left">';
						reserveHtml += '   <p>英文名：</p>';
						reserveHtml += '   <input type="text" id="enameForm_'+ tempId +'_'+ p +'">';
						reserveHtml += '  </div>';
		            
						reserveHtml += '  <div class="direct-input pull-left" >';
						reserveHtml += '   <p>性别：</p>';
						reserveHtml += '   <div id="sexDiv_'+ tempId +'_'+ p +'"></div>';
						reserveHtml += '  </div>';
						
						reserveHtml += '  <div class="direct-input pull-left" >';
						reserveHtml += '   <p>客户类型：</p>';
						reserveHtml += '   <div id="typeDiv_'+ tempId +'_'+ p +'"></div>';
						reserveHtml += '  </div>';
						reserveHtml += '  <div class="clear"></div>';
		            
						reserveHtml += '  <div class="passport-input pull-left">';
						reserveHtml += '   <p>护照：</p>';
						reserveHtml += '   <input type="text" id="passportForm_'+ tempId +'_'+ p +'">';
						reserveHtml += '  </div>';
						
						reserveHtml += '  <div class="passport-input pull-left">';
						reserveHtml += '   <p>国籍：</p>';
						reserveHtml += '   <input type="text" id="nationForm_'+ tempId +'_'+ p +'">';
						reserveHtml += '  </div>';
		            
						reserveHtml += '  <div class="passport-input pull-left" >';
						reserveHtml += '   <p>护照过期日期：</p>';
						reserveHtml += '   <div id="expireDateDiv_'+ tempId +'_'+ p +'"></div>';
						reserveHtml += '  </div>';
						
						reserveHtml += '  <div class="passport-input pull-left">';
						reserveHtml += '   <p>身份证号：</p>';
						reserveHtml += '   <input type="text" id="cardForm_'+ tempId +'_'+ p +'">';
						reserveHtml += '  </div>';
		                
						reserveHtml += '  <div class="passport-input pull-left">';
						reserveHtml += '   <p>户籍地：</p>';
						reserveHtml += '   <input type="text" id="domicileForm_'+ tempId +'_'+ p +'">';
						reserveHtml += '  </div>';
						
						reserveHtml += '  <div class="passport-input pull-left" >';
						reserveHtml += '   <p>出生日期：</p>';
						reserveHtml += '   <div id="birthdayDiv_'+ tempId +'_'+ p +'"></div>';
						reserveHtml += '  </div>';
						reserveHtml += '  <div class="clear"></div>';
						
						reserveHtml += ' </div>';
					}
					reserveHtml += '</div>';
				}
				$("#reserveDataDiv").html(reserveHtml);
				
				for(var k = 0; k < orderList.length; k++){
					var taobaoObj = taobaoList[k];  //淘宝订单对象
					var orderObj = orderList[k];  //平台订单对象
					var id = orderObj[3];  //淘宝票单号
					
					var count = taobaoObj[2] * orderObj[1];
					for(var r = 0; r < count; r++){
						
						$("#expireDateDiv_"+ id +"_"+r).xyzuiDatebox({
							format:'yyyy-MM-dd',
							placeholder:"请选择护照过期日期",
							editable : false,
							validator:function(id,date){
								return date.valueOf() < new Date().valueOf();
							}
						});
						$("#birthdayDiv_"+ id +"_"+r).xyzuiDatebox({
							format:'yyyy-MM-dd',
							placeholder:"请选择出生日期",
							editable : false,
							validator:function(id,date){
								return date.valueOf() > new Date().valueOf()-86400000;
							}
						});
						
						$("#sexDiv_"+ id +"_"+r).xyzuiCombobox({
							editable:false,
							data:[
							   {"value":"男","text":"男"},
							   {"value":"女","text":"女"}],
							placeholder:"请选择性别"
						});
						$("#typeDiv_"+ id +"_"+r).xyzuiCombobox({
							editable:false,
							data:[
							   {"value":"成人","text":"成人"},
							   {"value":"儿童","text":"儿童"},
							   {"value":"老人","text":"老人"}],
							placeholder:"请选择客户类型"
						});
						JPlaceHolder.init();
					}
				}
				var JsonStr = JSON.stringify(json);
				if(!xyzIsNull(JsonStr)){
					getPersonData(JsonStr);
				}
				
			}else{
				alert(data.msg);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function dataSubmit(){
	var tidArry = $.map($("div[id^='reserveDiv_']"),
		function(p){
			return $(p).attr("id").split("_")[1];
		}
	);
	
	var personJson = [];
	for(var t = 0; t < tidArry.length; t++){
		var tempOid = tidArry[t];
		var personArry = $.map($("div[id^='personDiv_"+ tempOid +"_']"),
			function(p){
				return $(p).attr("id").split("_")[2];
			}
		);
		/*出行人信息*/
		for(var p = 0; p < personArry.length; p++){
			var id = personArry[p];
			var numberCode = $("#numberForm_"+ tempOid +"_"+id).val();
			var realName = $("#realNameForm_"+ tempOid +"_"+id).val();
			var ename = $("#enameForm_"+ tempOid +"_"+id).val();
			var sex = $("#sexDiv_"+ tempOid +"_"+id).xyzuiCombobox('getValue');
			var type = $("#typeDiv_"+ tempOid +"_"+id).xyzuiCombobox('getValue');
			var passport = $("#passportForm_"+ tempOid +"_"+id).val();
			var nation = $("#nationForm_"+ tempOid +"_"+id).val();
			var expireDate = $("#expireDateDiv_"+ tempOid +"_"+id).xyzuiDatebox('getValue');
			var card = $("#cardForm_"+ tempOid +"_"+id).val();
			var domicile = $("#domicileForm_"+ tempOid +"_"+id).val();
			var birthday =  $("#birthdayDiv_"+ tempOid +"_"+id).xyzuiDatebox('getValue');
			
			if(xyzIsNull(realName)){
				alert("出行人"+ (id+1) +"姓名不能为空!");
				return;
			}else if(realName.length > 50){
				alert("出行人"+ (id+1) +"姓名长度不能超过50!");
				return;
			}else{
				var realNameReg = /^[\u4E00-\u9FA5A-Za-z]+$/;
				if(!realNameReg.test(realName)){
					alert("出行人"+ (id+1) +"姓名格式错误!");
					return;
				}
			}
			
			if(xyzIsNull(ename)){
				var enameReg = /^(\w+[-\s]?)+[\w-\s]$/;
				if(!enameReg.test(ename)){
					alert("出行人"+ (id+1) +"英文名格式错误!");
					return;
				}
			}
			
			if(xyzIsNull(sex)){
				alert("出行人"+ (id+1) +"请选择性别!");
				return;
			}
			
			if(xyzIsNull(type)){
				alert("出行人"+ (id+1) +"请选择客户类型!");
				return;
			}
			
			if(xyzIsNull(passport)){
				alert("出行人"+ (id+1) +"护照不能为空!");
				return;
			}else{
				if (passport.length > 15 || passport.length < 6) {
					alert("出行人"+ (id+1) +"护照长度错误!");
					return;
				};
				var passportReg =  /^1[45][0-9]{7}|G[0-9]{8}|P[0-9]{7}|S[0-9]{7,8}|D[0-9]+$/;
				if (passportReg.test(passport) != true) {
					alert("出行人"+ (id+1) +"护照格式错误!");
					return;
				}
			}
			
			if(xyzIsNull(nation)){
				alert("出行人"+ (id+1) +"国籍不能为空!");
				return;
			}else if(nation.length > 50){
				alert("出行人"+ (id+1) +"国籍长度不能超过50!");
				return;
			}
			
			if(xyzIsNull(expireDate)){
				alert("出行人"+ (id+1) +"证件过期日期不能为空!");
				return;
			}
			var nowDate = new Date();
			var nowYear = nowDate.getFullYear();
			var nowMonth = (nowDate.getMonth()+1);
			var nowDay = nowDate.getDate();
			
			var temp_year = parseInt(expireDate.substr(0,4));
			var temp_month = parseInt(expireDate.substr(5,7));
			var temp_day = parseInt(expireDate.substr(8));
			if(nowYear > temp_year){
				alert("出行人"+ id +"证件过期日期不能大于等于当前日期!");
				return;
			}else if(nowYear==temp_year){
				if((nowMonth > temp_month) || (nowMonth == temp_month && nowDay >= temp_day)){
		    		alert("出行人"+ id +"证件过期日期不能大于等于当前日期!");
					return;
				}
			}
			
			if(xyzIsNull(card)){
				alert("出行人"+ id +"身份证不能为空!");
				return;
			}else{
				card = card.replace(/x/,"X");
				var temp = /(^\d{15}$)|(^\d{17}([0-9]|X)$)/;
				if (temp.test(card) != true) {
					alert("出行人"+ id +"身份证格式错误!");
					isPass = true;
					return;
				}
			}
			
			if(xyzIsNull(domicile)){
				alert("户籍地不能为空!");
				return;
			}else if(domicile.length > 100){
				alert("户籍地长度不能超过100!");
				return;
			}
			
			if(xyzIsNull(birthday)){
				alert("生日不能为空!");
				return;
			}
			var tempYear = parseInt(birthday.substr(0,4));
			var tempMonth = parseInt(birthday.substr(5,7));
			var tempDay = parseInt(birthday.substr(8));
			if(nowYear < tempYear){
				alert("生日不能小于当前日期!");
				return;
			}else if(nowYear==tempYear){
				if((nowMonth < tempMonth) || (nowMonth == tempMonth && nowDay < tempDay)){
		    		alert("生日不能小于当前日期!");
					return;
				}
			}
			
			var personObj = {
				oid : tempOid,
				numberCode : numberCode,
				realName : realName,
				ename : ename,
				sex : sex,
				type : type,
				passport : passport,
				nation : nation,
				expireDate : expireDate,
				card : card,
				domicile : domicile,
				birthday : birthday
			};
			personJson[personJson.length] = personObj;
		}
	}
	
	$.ajax({
		url : "../ReserveWS/savePersonData.reserve",
		type : "POST",
		data : {
			personJson : JSON.stringify(personJson)
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$(".cruise-content").css("display","none");
		        $(".appointment-success").css('display','block');
		        
		        $("#againReserve").click(function(){
		        	window.location.href = "../login.html";
		        });
			}else{
				alert(data.msg);			
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function getPersonData(JsonStr){
	$.ajax({
		url : '../ReserveWS/getPersonByNumberCode.reserve',
		type : "POST",
		data : {
			json : JsonStr
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var personList = data.content;
				for(var i = 0; i < json.length; i++){
					var perObj = json[i];
					var tempId = perObj.oid;
					var personStr = perObj.person;
					var personArray = personStr.split(",");
					for(var y = 0; y < personArray.length; y++){
						for(var s = 0; s < personList.length; s++){
							var personObj = personList[s];
							if(personArray[y] == personObj.numberCode){
								$("#numberForm_"+tempId+"_"+y).val(personObj.numberCode);
								$("#realNameForm_"+tempId+"_"+y).val(personObj.realName);
								$("#enameForm_"+tempId+"_"+y).val(personObj.ename);
								$("#sexDiv_"+tempId+"_"+y).xyzuiCombobox('setValue',personObj.sex);
								$("#typeDiv_"+tempId+"_"+y).xyzuiCombobox('setValue',personObj.personType);
								$("#passportForm_"+tempId+"_"+y).val(personObj.passport);
								$("#nationForm_"+tempId+"_"+y).val(personObj.nation);
								$("#expireDateDiv_"+tempId+"_"+y).xyzuiDatebox('setValue', personObj.expireDate);
								$("#cardForm_"+tempId+"_"+y).val(personObj.card);
								$("#domicileForm_"+tempId+"_"+y).val(personObj.domicile);
								$("#birthdayDiv_"+tempId+"_"+y).xyzuiDatebox('setValue', personObj.birthday);
								break;
							}
						}
					}
				}
			}else{
				alert(data.msg);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}