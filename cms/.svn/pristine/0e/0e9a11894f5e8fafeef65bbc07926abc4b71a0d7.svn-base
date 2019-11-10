$(document).ready(function() {
	position = $("#dialogFormDiv_managerPositionButtonTreeIframe",window.parent.document).attr("name");
	$("#currentPosition").val(position);
	
	initTable();
});

function initTable(){
	$("#positionButtonTrueTable").tree({
		url:'../AdminPositionWS/queryPositionButtonTrueTree.do',
		animate : false,
		checkbox:true,
		lines:true,
		cascadeCheck : true,
		onlyLeafCheck:false,
		queryParams : {
			position : position
		},
		formatter:function(node){
			return node.text.length>100?node.text.substr(1,100)+"...":node.text;
		},
		loadFilter : function(data){
			if(data.status==1){
				return data.content;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
				return [];
			}
		}
	});
	
	$("#positionButtonFalseTable").tree({
		url:'../AdminPositionWS/queryPositionButtonFalseTree.do',
		animate : false,
		checkbox:true,
		lines:true,
		cascadeCheck : true,
		onlyLeafCheck:false,
		queryParams : {
			position : position
		},
		formatter:function(node){
			return node.text.length>100?node.text.substr(1,100)+"...":node.text;
		},
		loadFilter : function(data){
			if(data.status==1){
				return data.content;
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
				return [];
			}
		}
	});
}

function addPositionButton(){
	var buttons = $.map($("#positionButtonFalseTable").tree("getChecked"),function(p){return p.id;}).join(",");
	if(xyzIsNull(buttons)){
		top.$.messager.alert("提示","请先选中需要操作的对象！","info");
		return;
	}
	
	$.ajax({
		url : "../AdminPositionWS/addPositionButton.do",
		type : "POST",
		data : {
			position : position,
			buttons : buttons
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#positionButtonTrueTable").tree("reload");
				$("#positionButtonFalseTable").tree("reload");
				top.$.messager.alert("提示","操作成功！","info");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deletePositionButton(){
	var buttons = $.map($("#positionButtonTrueTable").tree("getChecked"),function(p){return p.id;}).join(",");
	if(xyzIsNull(buttons)){
		top.$.messager.alert("提示","请先选中需要操作的对象！","info");
		return;
	}
	
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		if(r){
			$.ajax({
				url : "../AdminPositionWS/deletePositionButton.do",
				type : "POST",
				data : {
					position : position,
					buttons : buttons
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						$("#positionButtonTrueTable").tree("reload");
						$("#positionButtonFalseTable").tree("reload");
						top.$.messager.alert("提示","操作成功！","info");
					}else{
						top.$.messager.alert("警告",data.msg,"warning");
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
				}
			});
		}
	});
	
}

function apiQueryTrue(){
	$("#positionButtonTrueTable").tree("reload");
}

function apiQueryFalse(){
	$("#positionButtonFalseTable").tree("reload");
}