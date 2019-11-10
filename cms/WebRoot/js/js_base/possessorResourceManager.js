$(document).ready(function() {
	
	var param = getUrlParameters();
	var possessor = param.possessor;
	
	resourceType = "Tkview";
	
	xyzTextbox("allTypeNameCn");
	xyzTextbox("allTypeNumber");
	
	$("#possessorResource_possessor").val(possessor);
	
	$("#typeCombobox").combobox({
		editable : false,
		onChange : function (newValue, oldValue){
			resourceType = newValue;
			initTable();
		}
	});
	
	initTable();
	//initPossessorResourceList();
	
});

function initTable(){
	
	var resourceTypeCn = "";
	var trueListUrl = "";
	var falseListUrl = "";
	
	if (resourceType == "Tkview") {
		resourceTypeCn = "单品";
		trueListUrl = "../TkviewWS/queryPossessorTkviewTrueList.do";
		falseListUrl = "../TkviewWS/queryPossessorTkviewFalseList.do";
	}else if (resourceType == "Ptview") {
		resourceTypeCn = "产品";
		trueListUrl = "../PtviewWS/queryPossessorPtviewTrueList.do";
		falseListUrl = "../PtviewWS/queryPossessorPtviewFalseList.do";
	}else if (resourceType == "Provider") {
		resourceTypeCn = "供应商";
		trueListUrl = "../ProviderWS/queryPossessorProviderTrueList.do";
		falseListUrl = "../ProviderWS/queryPossessorProviderFalseList.do";
	}else if (resourceType == "Channel") {
		resourceTypeCn = "渠道";
		trueListUrl = "../ChannelWS/queryPossessorChannelTrueList.do";
		falseListUrl = "../ChannelWS/queryPossessorChannelFalseList.do";
	}else if (resourceType == "Distributor") {
		resourceTypeCn = "分销商";
		trueListUrl = "../DistributorWS/queryPossessorDistributorTrueList.do";
		falseListUrl = "../DistributorWS/queryPossessorDistributorFalseList.do";
	}else if (resourceType == "security_position") {
		resourceTypeCn = "岗位";
		trueListUrl = "../AdminPositionWS/queryPossessorPositionTrueList.do";
		falseListUrl = "../AdminPositionWS/queryPossessorPositionFalseList.do";
	}else {
		return;
	}
	
	var nameCn = $("#nameCn").val();
	var numberCode = $("#numberCode").val();
	var possessor = $("#possessorResource_possessor").val();
	
	var toolbarTrue = [];
	
	toolbarTrue[toolbarTrue.length] = {
			iconCls: 'icon-search',
			text : '查询已选中'+resourceTypeCn,
			handler: function(){
				possessorResourceQueryTrue();
		}
	};
	
	toolbarTrue[toolbarTrue.length] = '-';
	
	toolbarTrue[toolbarTrue.length] = {
			iconCls: 'icon-remove',
			text : '删除机构的'+resourceTypeCn,
			handler: function(){deletePossessorResource();}
		};
	
	var toolbarFalse = [];
	
	toolbarFalse[toolbarFalse.length] = {
		iconCls: 'icon-search',
		text : '查询未选中'+resourceTypeCn,
		handler: function(){
			possessorResourceQueryFalse();
		}
	};
	
	toolbarFalse[toolbarFalse.length] = '-';
	
	toolbarFalse[toolbarFalse.length] = {
			iconCls: 'icon-add',
			text : '给机构添加'+resourceTypeCn,
			handler: function(){addPossessorResource();}
		};
	
	xyzgrid({
		table : 'possessorResourceTrueTable',
		title : '已选中的'+resourceTypeCn,
		url:trueListUrl,
		pageList : [5,10,15,30,50],
		pageSize : 5,
		toolbar : toolbarTrue,
		singleSelect : false,
		idField : 'numberCode',
		height:'auto',
		columns:[[
		    {field:'checkboxTemp',checkbox:true},
		    {field:'numberCode',title:'编号',halign:'center'},
		    {field:'nameCn',title:'名称',halign:'center'}
		]],
		queryParams : {
			nameCn : nameCn,
			numberCode : numberCode,
			possessor : possessor
		}
	});
	
	xyzgrid({
		table : 'possessorResourceFalseTable',
		title : '未选中的'+resourceTypeCn,
		url:falseListUrl,
		pageList : [5,10,15,30,50],
		pageSize : 5,
		toolbar : toolbarFalse,
		singleSelect : false,
		idField : 'numberCode',
		height:'auto',
		columns:[[
		    {field:'checkboxTemp',checkbox:true},
		    {field:'numberCode',title:'编号',halign:'center'},
		    {field:'nameCn',title:'名称',halign:'center'}
		]],
		queryParams : {
			nameCn : nameCn,
			numberCode : numberCode,
			possessor : possessor
		}
	});
}

function possessorResourceQueryTrue(){
	
	$("#possessorResourceTrueTable").datagrid('load',{
		possessor : $("#possessorResource_possessor").val(),
		numberCode : $("#allTypeNumber").val(),
		name : $("#allTypeNameCn").val()
	});
}

function possessorResourceQueryFalse(){
	
	$("#possessorResourceFalseTable").datagrid('load',{
		possessor : $("#possessorResource_possessor").val(),
		numberCode : $("#allTypeNumber").val(),
		name : $("#allTypeNameCn").val()
	});
	
}

function deletePossessorResource(){
	var possessorResources = $.map($("#possessorResourceTrueTable").datagrid("getChecked"),function(p){return p.numberCode;}).join(",");
	if(xyzIsNull(possessorResources)){
		top.$.messager.alert("提示","请先选中需要操作的对象！","info");
		return;
	}
	
	var possessor = $("#possessorResource_possessor").val();
	
	var deletePossessorResourceUrl = "";
	
	if (resourceType == "Tkview") {
		deletePossessorResourceUrl = "../PossessorWS/deletePossessorTkview.do";
	}else if (resourceType == "Ptview") {
		deletePossessorResourceUrl = "../PossessorWS/deletePossessorPtview.do";
	}else if (resourceType == "Provider") {
		deletePossessorResourceUrl = "../PossessorWS/deletePossessorProvider.do";
	}else if (resourceType == "Channel") {
		deletePossessorResourceUrl = "../PossessorWS/deletePossessorChannel.do";
	}else if (resourceType == "Distributor") {
		deletePossessorResourceUrl = "../PossessorWS/deletePossessorDistributor.do";
	}else if (resourceType == "security_position") {
		deletePossessorResourceUrl = "../PossessorWS/deletePossessorPosition.do";
	}else {
		return;
	}
	
	$.ajax({
		url : deletePossessorResourceUrl,
		type : "POST",
		data : {
			possessor : possessor,
			possessorResources : possessorResources
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#possessorResourceTrueTable").datagrid("reload");
				$("#possessorResourceFalseTable").datagrid("reload");
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

function addPossessorResource(){
	var possessorResources = $.map($("#possessorResourceFalseTable").datagrid("getChecked"),function(p){return p.numberCode;}).join(",");
	if(xyzIsNull(possessorResources)){
		top.$.messager.alert("提示","请先选中需要操作的对象！","info");
		return;
	}
	
	var possessor = $("#possessorResource_possessor").val();
	
	if (resourceType == "Tkview") {
		addPossessorResourceUrl = "../PossessorWS/addPossessorTkview.do";
	}else if (resourceType == "Ptview") {
		addPossessorResourceUrl = "../PossessorWS/addPossessorPtview.do";
	}else if (resourceType == "Provider") {
		addPossessorResourceUrl = "../PossessorWS/addPossessorProvider.do";
	}else if (resourceType == "Channel") {
		addPossessorResourceUrl = "../PossessorWS/addPossessorChannel.do";
	}else if (resourceType == "Distributor") {
		addPossessorResourceUrl = "../PossessorWS/addPossessorDistributor.do";
	}else if (resourceType == "security_position") {
		addPossessorResourceUrl = "../PossessorWS/addPossessorPosition.do";
	}else {
		return;
	}
	
	$.ajax({
		url : addPossessorResourceUrl,
		type : "POST",
		data : {
			possessor : possessor,
			possessorResources : possessorResources
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#possessorResourceTrueTable").datagrid("reload");
				$("#possessorResourceFalseTable").datagrid("reload");
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

function initPossessorResourceList(){
	possessorResourceList = [];
	$.ajax({
		url : "../PossessorWS/queryPossessorResourceList.do",
		type : "POST",
		data : {
			possessor : possessor,
			type : resourceType
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var tempList = data.content;
				possessorResourceList = $.map(tempList,function(p){return p.relate;});
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}
