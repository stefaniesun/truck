$(document).ready(function() {
	
	initTable();
	
});

function initTable(){
	xyzAjax({
		url : "../TkviewWS/queryTkviewGroupCruise.do",
		type : "POST",
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status == 1){
				var crusieList = data.content;
				
				var sum = 0;
				for(var t = 0; t < crusieList.length; t++){
					var cruiseObj = crusieList[t];
					var cruise = cruiseObj[0];
					var cruiseNameCn = cruiseObj[1];
					var count = cruiseObj[2];
					addTabs(t , cruiseNameCn+"("+ count +")" , cruise);
					sum += count;
				}
				addTabs(crusieList.length+1 , "取消选择("+ sum +")" , "noValue");
				
				$('#tkviewManagerTabs').tabs({
					select : crusieList.length+1
				});
				
				$("#tkviewManagerTabs").tabs({
					onSelect : function(title, index){
						var tab = $('#tkviewManagerTabs').tabs('getSelected');
						var cruise = tab.attr("id"); 
						
						if (!($(".tabs-panels").find("div[id='"+ cruise +"']").find("iframe").length > 0) ) { 
							var content = '<iframe src="../jsp_resources/tkviewMode.html?cruise='+ cruise +'" ';
							content += ' style="width:100%;height:100%;border:1px;">';
					    	content += '</iframe>';
					    	var tab = $('#tkviewManagerTabs').tabs('getSelected');
					    	$('#tkviewManagerTabs').tabs('update', {
					    		tab : tab,
					    		options : {
					    			content: content
					    		}
					    	});
						}
						
				    }
				});
				
			}
		}
	});
}

function addTabs(index, title, cruise){
	var content = '';
	if(index == 0){
		content += '<iframe src="../jsp_resources/tkviewMode.html?cruise='+ cruise +'" ';
		content += ' style="width:100%;height:100%;border:1px;">';
    	content += '</iframe>';
	}
	
	if(!$('#tkviewManagerTabs').tabs('exists', title)) {
		$("#tkviewManagerTabs").tabs('add',{
		    title : title,    
		    id : cruise,
		    content : content,
		    closable : false
		});
	}
	
}