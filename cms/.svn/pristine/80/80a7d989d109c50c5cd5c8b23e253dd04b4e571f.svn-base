$(document).ready(function() {
	JPlaceHolder.init();
	
	$("#loginSubmit").click(function(){
		reserveLogin();
	});
	
});

function reserveLogin(){
	
	var tid = $("#tidForm").val();
	var phone = $("#phoneForm").val();
	
	if(xyzIsNull(tid)){
		alert("订单号不能为空");
		return false;
	}
	if(xyzIsNull(phone)){
		alert("预留电话不能为空!");
		return false;
	}else{
		var phoneReg = /^1[0-9]{10}$/;
		if(!phoneReg.test(phone)){
			alert("预留电话格式不正确!");
			return false;
		}
	}
	
	$.ajax({
		url : "ReserveWS/reserveLogin.reserve",
		type : "POST",
		data : {
			tid : tid,
			phone : phone
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var notPass = '';
				var fail = "success";
				if(!xyzIsNull(data.content)){
					var reviewList = data.content.reviewList;
					var failList = data.content.failList;
					var passCount = data.content.passCount;
					if(passCount == 0){
						alert("审核成功,不能修改预约信息!");
					}else{
						if(reviewList.length > 0){
							for(var r = 0; r < reviewList.length; r++){
								notPass += reviewList[r];
							}
						}
						if(failList.length > 0){
							fail = "fail";
							for(var f = 0; f < failList.length ; f++){
								notPass += failList[f];
							}
						}
						window.location.href = "jsp_reserve/cruise_content.html?tid="+tid+"&notPass="+notPass+"&fail="+fail;
					}
				}else{
					window.location.href = "jsp_reserve/cruise_content.html?tid="+tid+"&notPass="+notPass+"&fail="+fail;
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