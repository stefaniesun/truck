function getLoginCookie(){
	var cookieValue = $.cookie('trucktrucktrucktrucktruck');
	if(cookieValue==null){
		cookieValue = "";
	}
	return cookieValue;
}

function deleteLoginCookie(){
	$.cookie('trucktrucktrucktrucktruck',null,
		{
			path : '/'
		}
	);
}

function setLoginCookie(xyzxyzxyzxyzxyzxyzxyzxyzxyzxyz){
	$.cookie('trucktrucktrucktrucktruck',xyzxyzxyzxyzxyzxyzxyzxyzxyzxyz);
}

function addLoginCookie(xyzxyzxyzxyzxyzxyzxyzxyzxyzxyz,days){
	if(days==0){
		$.cookie('trucktrucktrucktrucktruck',xyzxyzxyzxyzxyzxyzxyzxyzxyzxyz, 
			{
				path : '/'
			}
		);
	}else{
		$.cookie('trucktrucktrucktrucktruck',xyzxyzxyzxyzxyzxyzxyzxyzxyzxyz, 
			{
				expires : days,
				path : '/'
			}
		);
	}
}

