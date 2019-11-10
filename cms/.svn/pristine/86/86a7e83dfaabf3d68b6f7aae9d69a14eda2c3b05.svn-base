//初始化  有加密，有动态令牌,正式
$(document).ready(function() {
	$("#usernameForm").validatebox({    
	    required: true,
	    validType: 'length[3,20]'
	});
 
	$("#passwordForm").validatebox({    
	    required: true,
	    validType: 'length[3,20]'
	});
	$("#otpCodeForm").validatebox({
		required: true,
	    validType: 'length[6,8]'
	});
	changeCheckCode();
	$("#checkCodeFont").click(function(){
		changeCheckCode();
	});
});

function changeCheckCode(){
	var getRandomColor = function(){ 
	    return '#'+('00000'+(Math.random()*0x1000000<<0).toString(16)).slice(-6);
	};
	$("#checkCodeDiv").css({
		"background-color" : getRandomColor(),
		"color" : getRandomColor()
	});
	var getRandomMath = 1+Math.floor(Math.random()*9);
	$("#checkCodeDiv").text(getRandomMath);
}

function loginFast(){
	$.ajax({
		url : "LoginWS/decideLogin.do",
		type : "POST",
		data : {
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				window.location.href = "jsp_base/mainFrame.html";
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function login(){
	if(!$("#loginForm").form('validate')){
		return false;
	}
	{
		var checkCodeForm = $("input[name='checkCodeForm']:checked");
		var checkCode = new Number();
		for(var i = 0;i<checkCodeForm.length;i++){
			checkCode += parseInt(checkCodeForm.get(i).value);
		}
		var checkCodeValue = parseInt($("#checkCodeDiv").text());
		if(checkCode!=checkCodeValue){
			top.$.messager.alert("警告","验证码输入错误","warning");
			changeCheckCode();
			$("input[name='checkCodeForm']").removeAttr("checked");
			return;
		}
	}
	var username = $("#usernameForm").val();
	var password = $.md5($("#passwordForm").val()).substr(8,16);
	var otpCode = $("#otpCodeForm").val();
	
	var otpIsSynch = $("input[name='otpIsSynchForm']:checked").val();
	if(xyzIsNull(otpIsSynch)){
		otpIsSynch = 0;
	}
	
	$.ajax({
		url : "LoginWS/loginOtp.do",
		type : "POST",
		data : {
			username : username,
			password : password,
			otpCode : otpCode,
			otpIsSynch : otpIsSynch,
			indateHours : 0,
			phoneType : "pc",
			phoneCode : "pc"
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				//addLoginCookie(data.content.apikey,parseInt(indateHours)/24);
				addLoginCookie(data.content.apikey,7);
				window.location.href = "jsp_base/mainFrame.html";
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}