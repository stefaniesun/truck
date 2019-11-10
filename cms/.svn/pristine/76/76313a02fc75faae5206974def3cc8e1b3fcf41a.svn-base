$(document).ready(function() {
	
	xyzAjax({
		//url : "../ListWS/getCruiseList.do",
		url : "../TkviewWS/queryTkviewGroupCruise.do",
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
					var cruise = cruiseObj[0];
					var cruiseNameCn = cruiseObj[1];
					$("#cruiseTabs").tabs('add',{
					    title : cruiseNameCn, 
					    id : cruise,
					    closable : false
					});
				}
				
				$("#cruiseTabs").tabs({
					onSelect : function(title, index){
						$('#cruiseTabs').tabs('select',title);
						
						var cruiseObj = cruiseList[index];
						var cruise = cruiseObj[0];
						
						if (!($(".tabs-panels").find("div[id='"+ cruise +"']").find("iframe").length > 0) ) { 
							var content = '<iframe src="../jsp_resources/tkviewTypeView.html?cruise='+ cruise +'" ';
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