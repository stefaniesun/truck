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