$(document).ready(function() {
	var params = getUrlParameters();
	cruise = params.cruise;
	
	initTable();
	
});

function initTable(){	
	addTabs(0 , "售卖模式" , "sale"); //默认
	addTabs(1 , "包船模式" , "packetBoat");
	
	$("#tkviewModeTabs").tabs({
		onSelect : function(title, index){
			$('#tkviewModeTabs').tabs('select',title);
			var mode = 'sale';  //默认
			if(index == 1){
				mode = 'packetBoat';
			}
			if (!($(".tabs-panels").find("div[id='"+ mode +"']").find("iframe").length > 0) ) { 
				var content = '<iframe src="../jsp_resources/tkviewManager.html?cruise='+ cruise +'&mode='+ mode +'" ';
				content += ' style="width:100%;height:90%;border:1px;">';
		    	content += '</iframe>';
		    	var tab = $('#tkviewModeTabs').tabs('getSelected');
		    	$('#tkviewModeTabs').tabs('update', {
		    		tab : tab,
		    		options : {
		    			content: content
		    		}
		    	});
			}
	    }
	});
	
	$('#tkviewModeTabs').tabs({
		select : 0
	});
	
}

function addTabs(index, title, mode){
	var content = '';
	if(index == 0){
		content += '<iframe src="../jsp_resources/tkviewManager.html?cruise='+ cruise +'&mode='+ mode +'" ';
		content += ' style="width:100%;height:90%;border:1px;">';
    	content += '</iframe>';
	}
	$("#tkviewModeTabs").tabs('add',{
	    title : title,    
	    id : mode,
	    content : content,
	    closable : false
	});
	
}