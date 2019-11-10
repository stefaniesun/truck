function addCookie(cookieKey,cookieValue,days){
	if(days==0){
		$.cookie(cookieKey,cookieValue, 
				{
			path : '/'
				}
		);
	}else{
		$.cookie(cookieKey,cookieValue, 
				{
			expires : days,
			path : '/'
				}
		);
	}
}

function getCookie(cookieKey){
	var cookieValue = $.cookie(cookieKey);
	if(cookieValue==null){
		cookieValue = "";
	}
	return cookieValue;
}

function deleteCookie(cookieKey){
	$.cookie(cookieKey,null,
		{
			path : '/'
		}
	);
}

function getCookie(cookieKey){
	var cookieValue = $.cookie(cookieKey);
	if(cookieValue==null){
		cookieValue = "";
	}
	return cookieValue;
}

