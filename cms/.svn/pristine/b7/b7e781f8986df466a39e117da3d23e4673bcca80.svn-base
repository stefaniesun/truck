$(document).ready(function() {
	
	xyzAjax({
		url : "../RTkview_ViewWS/queryRoyalCruiseList.do",
		type : "POST",
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var cruiseList = data.content;
				if(cruiseList.length == 0){
					return false;
				}
				
				for(var i = 0; i < cruiseList.length; i++){
					var cruiseObj = cruiseList[i];
					var cruise = cruiseObj.numberCode;
					var cruiseNameCn = cruiseObj.nameCn;
					$("#cruiseTabs").tabs('add',{
					    title : cruiseNameCn, 
					    id : cruise,
					    closable : false
					});
				}
				
				$("#cruiseTabs").tabs({
					onSelect : function(title, index){
						$('#cruiseTabs').tabs('select',title);
						var cruise = cruiseList[index].numberCode;
						if (!($(".tabs-panels").find("div[id='"+ cruise +"']").find("iframe").length > 0) ) { 
							var content = '<iframe src="../jsp_rbase/rTkviewView.html?cruise='+ cruise +'" ';
					    	content += ' style="width:100%;height:100%;border:1px;">';
					    	content += '</iframe>';
					    	var tab = $('#cruiseTabs').tabs('getSelected');
					    	$('#cruiseTabs').tabs('update', {
					    		tab : tab,
					    		options : {
					    			content: content
					    		}
					    	});
						}
				    }
				});
				
				$('#cruiseTabs').tabs({
					select : 0
				}); 
				
			}
		}
	});

});