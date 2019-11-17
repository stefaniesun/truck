function xyzIsNull(obj){
	if(obj==undefined || obj==null || obj==="" || obj===''){
		return true;
	}else{
		return false;
	}
}

function addCustomerLoginCookie(data) {
    $.cookie('TRUCK_LOGIN_KEY', data.apikey,
        {
            path: '/'
        }
    );
}

function getCustomerLoginCookie(){
	var cookieValue = $.cookie('TRUCK_LOGIN_KEY');
	if(cookieValue==null){
		cookieValue = "";
	}
	return cookieValue;
}

function deleteCustomerLoginCookie(){
	$.cookie('TRUCK_LOGIN_KEY',null,
		{
			path : '/'
		}
	);
}
function getLoginCustomer(){
	var customer=localStorage.getItem("customer");
	return JSON.parse(customer);
}
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}