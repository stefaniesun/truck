$(document).ready(function() {
	
	initTable();
	
});

function initTable(){
	var toolbar = [];
	if (xyzControlButton("buttonCode_x20170309120002")) {
		toolbar[toolbar.length] = {
			text : '新增',
			border : '1px solid #bbb',
			iconCls : 'icon-add',
			handler : function() {
				addPriceStrategyButton();
			}
		};
	}
	if (xyzControlButton("buttonCode_x20170309120003")) {
		toolbar[toolbar.length] = {
			text : '编辑',
			border : '1px solid #bbb',
			iconCls : 'icon-edit',
			handler : function() {
				editPriceStrategyButton();
			}
		};
	}
	if (xyzControlButton("buttonCode_x20170309120004")) {
		toolbar[toolbar.length] = {
			text : '删除',
			border : '1px solid #bbb',
			iconCls : 'icon-remove',
			handler : function() {
				deletePriceStrategyButton();
			}
		};
	}

	xyzgrid({
		table : 'priceStrategyManagerTable',
		title : "加价区间列表",
		url : '../PriceStrategyWS/queryPriceStrategyList.do',
		toolbar : toolbar,
		singleSelect : true,
		fit : true,
		idField : 'numberCode',
		columns : [ [
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'编号',align:'center',hidden:true},
			{field:'minPrice',title:'区间最小值(单位:元)',align:'center'},
			{field:'maxPrice',title:'区间最大值(单位:元)',align:'center'},
			{field:'priceMarkup',title:'加价值(单位:元)',align:'center'},
			{field:'addDate',title:'添加时间',hidden:true,align:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'alterDate',title:'修改时间',align:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			 {field:'remark',title:'备注',
				formatter:function(value ,row ,index){
		    		 return xyzGetDiv(value ,0 ,200);
		    	 }
			},
		] ]
	});
}

function addPriceStrategyButton(){
	xyzdialog({
		dialog : 'dialogFormDiv_addPriceStrategyButton',
		title : '新增价格区间',
	    href : '../jsp_resources/addPriceStrategy.html',
	    fit : false,  
	    pageSize : 5,
	    width: 450,
	    height : 450,
	    buttons:[{
			text:'确定',
			handler:function(){
				addPriceStrategySubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addPriceStrategyButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("remarkForm");
			
			$(".priceInup").numberbox({
				required:true,
				min:0,    
			    precision:2,
			    icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).textbox('clear');
					}
				}]
			});
			
		}
	});
}

function addPriceStrategySubmit(){
	if(!$("form").form('validate')){
		return false;
	}

	var minPrice = $("#minPriceForm").numberbox("getValue");
	var maxPrice = $("#maxPriceForm").numberbox("getValue");
	var priceMarkup = $("#priceMarkupForm").numberbox("getValue");
	var remark = $("#remarkForm").textbox("getValue");
	
	$.ajax({
		url : "../PriceStrategyWS/addPriceStrategy.do",
		type : "POST",
		data : {
			minPrice : minPrice,
			maxPrice : maxPrice,
			priceMarkup : priceMarkup,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#priceStrategyManagerTable").datagrid("reload");
				$("#dialogFormDiv_addPriceStrategyButton").dialog("destroy");
				
				top.$.messager.alert("提示","操作成功!","info");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editPriceStrategyButton(){
	var priceStrategy = $("#priceStrategyManagerTable").datagrid("getChecked");
	if(priceStrategy.length != 1){
		top.$.messager.alert("提示","请先选中单个对象!","info");
		return;
	}
	
	var row = priceStrategy[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editPriceStrategyButton',
		title : '编辑价格区间【'+ row.minPrice +'】~【'+ row.maxPrice +'】',
	    href : '../jsp_resources/editPriceStrategy.html',
	    fit : false,  
	    width: 450,
	    height : 450,
	    buttons:[{
			text:'确定',
			handler:function(){
				editPriceStrategySubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editPriceStrategyButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			$(".priceInup").numberbox({
				required:true,
				min:0,    
			    precision:2,
			    icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).textbox('clear');
					}
				}]
			});
			
			$("#minPriceForm").numberbox("setValue", row.minPrice);
			$("#maxPriceForm").numberbox("setValue", row.maxPrice);
			$("#priceMarkupForm").numberbox("setValue", row.priceMarkup);
			$("#remarkForm").val(row.remark);
			
			xyzTextbox("remarkForm");
		}
	});
}

function editPriceStrategySubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var minPrice = $("#minPriceForm").numberbox("getValue");
	var maxPrice = $("#maxPriceForm").numberbox("getValue");
	var priceMarkup = $("#priceMarkupForm").numberbox("getValue");
	var remark = $("#remarkForm").textbox("getValue");
	
	$.ajax({
		url : "../PriceStrategyWS/editPriceStrategy.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			minPrice : minPrice,
			maxPrice : maxPrice,
			priceMarkup : priceMarkup,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#priceStrategyManagerTable").datagrid("reload");
				$("#dialogFormDiv_editPriceStrategyButton").dialog("destroy");
				
				top.$.messager.alert("提示","操作成功!","info");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deletePriceStrategyButton(){
	var numberCodes = $.map($("#priceStrategyManagerTable").datagrid("getChecked"),
		function(p){
			return p.numberCode;
		}
	).join(",");
	
	if(xyzIsNull(numberCodes)){
		top.$.messager.alert("提示","请先勾选需要删除的对象！","info");
		return;
	}
	
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		if(r){
		  $.ajax({
			url : "../PriceStrategyWS/deletePriceStrategy.do",
			type : "POST",
			data : {
				numberCodes : numberCodes
			},
			async : false,
			dataType : "json",
			success : function(data) {
				if(data.status==1){
					$("#priceStrategyManagerTable").datagrid("reload");
					top.$.messager.alert("提示","操作成功!","info");
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