$(document).ready(function() {
	var params = getUrlParameters();
	shipment = params.shipment;
	airwayMark = params.airwayMark;
	
	initTable();
	
});

function initTable(){
	var toolbar = [];
	var title = "行程路线列表【"+airwayMark+"】";

	xyzgrid({
		table : 'voyageManagerTable',
		title : title,
		url : '../R_VoyageWS/queryRVoyageList.do',
		toolbar : toolbar,
		idField : 'numberCode',
		queryParams : {
			shipment : shipment
		},
		singleSelect : false ,
		height : 560,
		columns : [ [
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'行程编号',hidden:true},
			{field:'shipmentMark',title:'航期代码',halign:'center'},
			{field:'time',title:'时间',align:'center'},
			{field:'timeType',title:'时间类型',halign:'center',
				formatter : function(value, row, index){
					//EMBARK:起航、DOCKED:停靠、CRUISING:巡航、DEBARK:结束
					if(value == 'EMBARK'){
						return "起航";
					}else if(value == 'DOCKED'){
						return "停靠";
					}else if(value == 'CRUISING'){
						return "巡航";
					}else{
						return "结束 ";
					}
				}
			},
			{field:'port',title:'港口编号',hidden:true},
			{field:'portMark',title:'港口代码',halign:'center'},
			{field:'portNameCn',title:'港口名称',halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value, 0, 70);
				}
			},
			{field:'arrivalTime',title:'到达时间',align:'center'},
			{field:'leaveTime',title:'出发时间',align:'center'}
		]]
	});

}