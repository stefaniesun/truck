$(document).ready(function() {

    initTable();

    xyzTextbox("numberCode");
    xyzTextbox("name");
    
    $("#distributorTagQueryButton").click(function(){
		loadTable();
	});
    
});

function initTable() {
    var toolbar = [];
    
    if (xyzControlButton("buttonCode_y20171108152102")) {
    	toolbar[toolbar.length] = '-';
    	toolbar[toolbar.length] = {
			text:'新增',
			iconCls:'icon-add',
			handler:function() {
				addDistributorTagButton();
			}
		};
	}
    
    if (xyzControlButton('buttonCode_y20171108152103')) {
        toolbar[toolbar.length] = {
            text:'编辑',
            border:'1px solid #bbb',
            iconCls:'icon-edit',
            handler:function() {
            	editDistributorTagButton();
            }
        };
        toolbar[toolbar.length] = '-';
    }
    
    if (xyzControlButton('buttonCode_y20171108152104')) {
    	toolbar[toolbar.length] = {
    			text:'删除',
    			border:'1px solid #bbb',
    			iconCls:'icon-remove',
    			handler:function() {
    				deleteDistributorTagButton();
    			}
    	};
    	toolbar[toolbar.length] = '-';
    }
    


    xyzgrid({
        table : 'distributorTagTable',
        title : '分销商列表',
        url : '../DistributorTagWS/queryDistributorTagList.do',
        pageList : [5, 10, 15, 30, 50],
        pageSize : 15,
        toolbar : toolbar,
        singleSelect : true,
        idField : 'username',
        columns : [[
            {field:'checkboxTemp',checkbox:true,hidden:true},
            {field:'numberCode',title:'编号',width:80,halign:'center',hidden:true,
            	formatter:function(value, row, index){
            		return xyzGetDiv(value,0,80);
            	}
            },
            {field:'name',title:'分销等级名称',width:80,halign:'center',
            	formatter:function(value, row, index){
            		return xyzGetDiv(value,0,80);
            	}
            },
            {field:'strategyJson',title:'操作',halign:'center',
				formatter:function(value ,row ,index){
					var html = "";
					if(xyzControlButton("buttonCode_y20171108152105")){
						html +=xyzGetA("减免价格设置","editStrategyJson",[row.numberCode,row.name],"","");
					}else{
						html += " ";
					}
					return html;
		    	 }
			},
            {field:'addDate',title:'添加时间',hidden:true,halign:'center'},
            {field:'alertDate',title:'修改时间',width:85,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
            {field:'remark',title:'备注',width:70,halign:'center'}
        ]]
    });
}

function loadTable() {
    
	var numberCode = $("#numberCode").val();
	var name = $("#name").val();
	
    $("#distributorTagTable").datagrid("load", {
    	numberCode : numberCode, 
    	name : name
    });
}

function addDistributorTagButton(){
	
	xyzdialog({
		dialog : 'dialogFormDiv_addDistributorTagButton',
		title : '新增分销商',
	    href : '../jsp_distributor/addDistributorTag.html',
	    fit : false,  
	    width: 550,
	    height : 400,
	    buttons:[{
			text:'确定',
			handler:function(){
				addDistributorTagSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addDistributorTagButton").dialog("destroy");
			}
		}],
		onLoad : function() {
			xyzTextbox("nameForm");
		}
	});
}

function addDistributorTagSubmit(){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var name = $("#nameForm").val();
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../DistributorTagWS/addDistributorTag.do",
		type : "POST",
		data : {
			name : name,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#distributorTagTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_addDistributorTagButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editDistributorTagButton(){
	
	var distributorTag = $("#distributorTagTable").datagrid("getChecked")[0];
	if (xyzIsNull(distributorTag)) {
		top.$.messager.alert("提示", "请选择单个对象!", "info");
		return;
	}
	
	xyzdialog({
		dialog : 'dialogFormDiv_editDistributorTagButton',
		title : '编辑分销商',
	    href : '../jsp_distributor/editDistributorTag.html',
	    fit : false,  
	    width: 500,
	    height : 600,
	    buttons:[{
			text:'确定',
			handler:function(){
				editDistributorTagSubmit(distributorTag.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editDistributorTagButton").dialog("destroy");
			}
		}],
		onLoad : function() {
			xyzTextbox("nameForm");
			
			
			$("#nameForm").textbox("setValue",distributorTag.name); 
			$("#remarkForm").val(distributorTag.remark);

		}
	});
}

function editDistributorTagSubmit(numberCode){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var name = $("#nameForm").val();
	var remark = $("#remarkForm").val();

	$.ajax({
		url : "../DistributorTagWS/editDistributorTag.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			name : name,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#distributorTagTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_editDistributorTagButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deleteDistributorTagButton(){
	
	var distributorTag = $("#distributorTagTable").datagrid("getChecked")[0];
	if (xyzIsNull(distributorTag)) {
		top.$.messager.alert("提示", "请选择单个对象!", "info");
		return;
	}
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		 if(r){
			  $.ajax({
				url : "../DistributorTagWS/deleteDistributorTag.do",
				type : "POST",
				data : {
					numberCodes : distributorTag.numberCode
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.status == 1) {
						$("#distributorTagTable").datagrid("reload");
						top.$.messager.alert("提示", "操作成功!", "info");
						$("#dialogFormDiv_editDistributorTagButton").dialog("destroy");
					} else {
						top.$.messager.alert("警告", data.msg, "warning");
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
				}
			});
		 }
	});
}
function editStrategyJson(numberCode,nameCn) {
	$.ajax({
		url : '../DistributorTagWS/queryDistributorTagListByNumberCode.do',
		type : "POST",
		data : {
			numberCode : numberCode
		},
		dataType : 'json',
		async : false,
		success : function(data) {
			if (data.status == 1) {
				var dataJson  = eval(data.content.strategyJson) ;
				var content = "<div style='margin-top:30px'>";
				content += "<table align='center'  id='info' style='margin-top:10px;' cellspacing='10'>";
					content += "<tr>";
						content += "<td></td>"; 
						content += "<td></td>"; 
						content += "<td></td>"; 
						content += "<td></td>"; 
						content += "<td></td>"; 
						content += "<td></td>"; 
						content += "<td align='right'><img  onclick='addPriceStrategyHtml()' alt='点我添加加价区间' src='../image/other/addPage.gif' title='点我添加加价区间' style='cursor: pointer;' ></td>"; 
						content += "<td></td>"; 
					content += "</tr>";
				if(xyzIsNull(dataJson)){
					content += "<tr id='priceStrategy_0' class = 'priceStrategyClass' >";
						content += "<td>价格区间 <input type='text'  id='minPrice_0'  style='width:80px'></td>"; 
						content += "<td>──</td>"; 
						content += "<td><input type='text'  id='maxPrice_0'  style='width:80px'></td>"; 
						content += "<td>减免</td>"; 
						content += "<td><input type='text'  id='price_0'  style='width:80px'>元</td>"; 
						content += "<td>返利</td>"; 
						content += "<td><input type='text'  id='rebate_0'  style='width:80px'></td>"; 
						content +='<td><img onclick ="deletePriceStrategyHtml(0)" alt="点我删除 区间!" src="../image/other/delete.png" title="点我删除 区间!" style="cursor: pointer;"></td>';
					content += "</tr>";
				}
				for(var i in dataJson){  
					content += "<tr id='priceStrategy_"+i+"' class = 'priceStrategyClass' >";
						content += "<td>价格区间 <input type='text'  id='minPrice_"+i+"' value='"+dataJson[i].minPrice+"' style='width:80px'></td>"; 
						content += "<td>──</td>"; 
						content += "<td><input type='text'  id='maxPrice_"+i+"' value='"+dataJson[i].maxPrice+"' style='width:80px'></td>"; 
						content += "<td>减免</td>"; 
						content += "<td><input type='text'  id='price_"+i+"' value='"+dataJson[i].price+"' style='width:80px'>元</td>"; 
						content += "<td>返利</td>"; 
						content += "<td><input type='text'  id='rebate_"+i+"' value='"+dataJson[i].rebate+"' style='width:80px'></td>"; 
						content +='<td><img onclick ="deletePriceStrategyHtml(\''+i+'\')" alt="点我删除 区间!" src="../image/other/delete.png" title="点我删除 区间!" style="cursor: pointer;"></td>';
					content += "</tr>";
				}
				content += "</table>";
				content += "</div>";
				xyzdialog({
					dialog : 'dialogDiv_editpriceStrategy',
					title : nameCn+' 加价区间',
					content : content,
					width : 700,
					height : 600,
					fit : false,
					buttons:[{
							text:'确定',
							handler:function(){
								editStrategyJsonSubmit(numberCode);
							}
						},{
							text:'取消', 
							handler:function(){
								$("#dialogDiv_editpriceStrategy").dialog("destroy");
							}
					}],
					onOpen : function(){
						$(".priceStrategyClass").each(function(i,n){
							$('#minPrice_'+i).numberbox({ 
								required:true,
								min:0,    
								max:20000,
							}); 
							$('#maxPrice_'+i).numberbox({ 
								required:true,
								min:0,    
								max:20000,
							}); 
							$('#price_'+i).numberbox({ 
								min:0,    
								max:20000,
							}); 
							xyzTextbox("rebate_"+i);
							
						});
					}
				});
			} 
		},
	});
}

function addPriceStrategyHtml(){ 
	var tempId = $($(".priceStrategyClass:last")).attr('id');
	tempLength = tempId.split('_')[1];
	tempLength = parseInt(tempLength)+1;
	var  addPriceStrategyHtml = '';
	addPriceStrategyHtml+='<tr  id = "priceStrategy_'+tempLength+'" class = "priceStrategyClass">';
	addPriceStrategyHtml += "<td>价格区间 <input type='text'   id='minPrice_"+tempLength+"'  style='width:80px'></td>"; 
	addPriceStrategyHtml += "<td>──</td>"; 
	addPriceStrategyHtml += "<td><input type='text'  id='maxPrice_"+tempLength+"'  style='width:80px'></td>"; 
	addPriceStrategyHtml += "<td>减免</td>";
	addPriceStrategyHtml += "<td><input type='text'   id='price_"+tempLength+"'  style='width:80px'>元</td>"; 
	addPriceStrategyHtml += "<td>返利</td>"; 
	addPriceStrategyHtml += "<td><input type='text'  id='rebate_"+tempLength+"'  style='width:80px'></td>"; 
	addPriceStrategyHtml +='<td><img onclick ="deletePriceStrategyHtml(\''+tempLength+'\')" alt="点我删除 区间!" src="../image/other/delete.png" title="点我删除渠道和宝贝!" style="cursor: pointer;"></td>';
	addPriceStrategyHtml +='</tr>';
	$("tr[id^=priceStrategy_]:last").after(addPriceStrategyHtml);
	
	$('#minPrice_'+tempLength).numberbox({ 
		required:true,
		min:0,    
		max:20000,
	});  
	$('#maxPrice_'+tempLength).numberbox({ 
		required:true,
		min:0,    
		max:20000,
	});  
	$('#price_'+tempLength).numberbox({ 
		min:0,    
		max:20000,
	});  
	xyzTextbox("rebate_"+tempLength);
}

function deletePriceStrategyHtml(id){
	
	$("#priceStrategy_"+id).remove();
}
function editStrategyJsonSubmit(numberCode){
	if(!$("#info").form('validate')){
		return false;
	}
	
	var minNum = 0;//最小值
	var maxNum = 0;//最大值
	var min = 0;//用于错误时返回的区间最小
	var max = 0;//用于错误时返回的区间最大
	var required = true;//必填状态
	var sequenceState = true;//区间大小比较状态
	var minState = true;//从小到大 排列时的状态
	var maxState = true;//从大到小排列的状态
	var ptrategyJson = [];
	$(".priceStrategyClass").each(function(i,n){
		var minPrice = $(this).find("input[id^='minPrice_']").numberbox('getValue');
		var maxPrice = $(this).find("input[id^='maxPrice_']").numberbox('getValue');
		var price = $(this).find("input[id^='price_']").numberbox('getValue');
		var rebate = $(this).find("input[id^='rebate_']").val();
		
		if(xyzIsNull(price)&&xyzIsNull(rebate)){
			required = false;
		}
		if(parseInt(minPrice)>parseInt(maxPrice)){
			sequenceState = false;
			min = minPrice;
			max = maxPrice;
		}
		if(i==0){
			maxNum = maxPrice; //区间的最大值
			minNum = minPrice;
		}else{
			//区间从小到大
			if(parseInt(maxNum)>=parseInt(minPrice) ||parseInt(maxNum)>=parseInt(maxPrice)){
				minState=false;
				min =minPrice;
			}else{
				maxNum = maxPrice; 
			}
			//区间从大到小
			if(parseInt(minNum)>=parseInt(minPrice)||parseInt(minNum)>=parseInt(maxPrice)){
				minState=true;
				if(parseInt(minNum)<=parseInt(maxPrice)){
					maxState = false;
					max = maxPrice;
				}else {
					minNum = minPrice;
				}
			}
		}
		ptrategyJson.push({"minPrice":minPrice,maxPrice:maxPrice,"price":price,"rebate":rebate});
		
	 });
	if(required==false){
		top.$.messager.alert("提示","减免和返利必填一种，添加失败!","info");
		return ;
	}else if(sequenceState==false){
		top.$.messager.alert("提示",min+"和"+max+"请按从小到大的顺序输入，添加失败!","info");
		return ;
	}else if(minState==false){
		top.$.messager.alert("提示",min+"值存在区间内，添加失败!","info");
		return ;
	}else if(maxState==false){
		top.$.messager.alert("提示",max+"值存在区间内，添加失败!","info");
		return ;
	}
	$.ajax({
		url : "../DistributorTagWS/editStrategyJsonByNumberCode.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			ptrategyJson : JSON.stringify(ptrategyJson)
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#channelManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogDiv_editpriceStrategy").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}



