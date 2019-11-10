//初始化  有加密，无动态令牌,正式
$(document).ready(function() {
	$("#usernameForm").validatebox({    
	    required: true,
	    validType: 'length[3,20]'
	});
	
	$("#passwordForm").validatebox({    
	    required: true,
	    validType: 'length[3,20]'
	});
	changeCheckCode();
	$("#checkCodeFont").click(function(){
		changeCheckCode();
	});
	
	$("#alterPasswordButton").click(function(){
		alterPassword();
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
				window.location.href = "xyzsecurity/mainFrameduanyi.html";
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
				if(location.hostname=="xt.duanyi.com.cn"){
					window.location.href = "xtWork/xtWork.html";
				}else{
					window.location.href = "xyzsecurity/mainFrameduanyi.html";
				}
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function alterPassword(){
	var url = "xyzsecurity/alterPassword.html";
	var tt = $(window).height()-50;
	xyzdialog({
		dialog : 'dialogFormDiv_alterPassword',
		title : "修改密码",
	    content : "<iframe id='dialogFormDiv_alterPasswordIframe' frameborder='0' name=''></iframe>",
	    closable : true,
	    fit:false,
	    width:1000,
	    height:tt
	});
	var tempWidth = $("#dialogFormDiv_alterPassword").css("width");
	var tempHeight = $("#dialogFormDiv_alterPassword").css("height");
	var tempWidth2 = parseInt(tempWidth.split("px")[0]);
	var tempHeight2 = parseInt(tempHeight.split("px")[0]);
	$("#dialogFormDiv_alterPasswordIframe").css("width",(tempWidth2-20)+"px");
	$("#dialogFormDiv_alterPasswordIframe").css("height",(tempHeight2-20)+"px");
	$("#dialogFormDiv_alterPasswordIframe").attr("src",url);
}