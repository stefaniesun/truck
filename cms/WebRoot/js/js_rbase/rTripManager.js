$(document).ready(function() {
	var params = getUrlParameters();
	airway = params.airway;
	
	initTable();
	
});

function initTable(){
	var toolbar = [];

	xyzgrid({
		table : 'tripManagerTable',
		title : "行程模版列表",
		url : '../R_TripWS/queryRTripList.do',
		toolbar : toolbar,
		singleSelect : false,
		pagination : false,
		idField : 'numberCode',
	    fit : false,
		height : 570,
		queryParams:{
			airway : airway
		},
		columns : [ [
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'路线编号',hidden:true,halign:'center'},
			{field:'airway',title:'航线编号',hidden:true,halign:'center'},
			{field:'airwayMark',title:'航线代码',halign:'center'},
			{field:'time',title:'时间',halign:'center'},
			{field:'timeType',title:'时间类型',halign:'center',
				formatter : function(value, row, index){
					//EMBARK:起航 、DOCKED:停靠、CRUISING:巡航、DEBARK:结束
					if(value == 'EMBARK'){
						return "起航";
					}else if(value == 'DOCKED'){
						return "停靠";
					}else if(value == 'CRUISING'){
						return "巡航";
					}else{
						return "结束";
					}
				}
			},
			{field:'port',title:'港口编号',hidden:true,halign:'center'},
			{field:'portMark',title:'港口代码',halign:'center'},
			{field:'portNameCn',title:'港口名称',halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value, 0, 100);
				}
			}
		]]
	});
}