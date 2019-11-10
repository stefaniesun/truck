$(document).ready(function() {
	xyzCombobox({
		combobox:'cruise',
		url : '../ListWS/getCruiseList.do',
		mode: 'remote',
		icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	xyzTextbox("nameCn");
	
	$("#type").combobox({
		icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	$("#isOpen").combobox({
		icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).combobox('clear');
			}
		}]
	});
	
	initTable();
	initTabs();
	
	$("#cabinQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = [];
	if (xyzControlButton("buttonCode_x20161207195602")) {
		toolbar[toolbar.length] = {
			text : '单个新增',
			border : '1px solid #bbb',
			iconCls : 'icon-add',
			handler : function() {
				addCabinButton();
			}
		};
	}
	if (xyzControlButton("buttonCode_x20161207195603")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length] = {
				text : '完整新增',
				border : '1px solid #bbb',
				iconCls : 'icon-add',
				handler : function() {
					addQuickCabinButton();
				}
		};
	}
	if (xyzControlButton("buttonCode_x20161207195603")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length] = {
			text : '编辑',
			border : '1px solid #bbb',
			iconCls : 'icon-edit',
			handler : function() {
				editCabinButton();
			}
		};
	}
	if (xyzControlButton("buttonCode_x20161207195604")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length] = {
			text : '删除',
			border : '1px solid #bbb',
			iconCls : 'icon-remove',
			handler : function() {
				deleteCabinButton();
			}
		};
	}

	xyzgrid({
		table : 'cabinManagerTable',
		title : "舱型列表",
		url : '../CabinWS/queryCabinList.do',
		toolbar : toolbar,
		singleSelect : false,
		idField : 'numberCode',
		columns : [[
             {field:'checkboxTemp',checkbox:true},
             {field:'numberCode',title:'舱型编号',hidden:true,halign:'center'},
             {field:'nameCn',title:'舱型名称',width:130,halign:'center',
  		    	formatter:function(value ,row ,index){
 					return xyzGetDiv(value,0,130);
 				}
  		     },
             {field:'mark',title:'代码',align:'center',width:70,
	        	  formatter:function(value ,row ,index){
						return xyzGetDiv(value,0,70);
					}
		     },
 		     {field:'volume',title:'容量',align:'center',width:50,
		    	 formatter:function(value ,row ,index){
		    		 if(value == row.maxVolume){
		    			 return value;
		    		 }else if(row.maxVolume != 0){
		    			 return ''+value+'-'+row.maxVolume+'';
		    		 }
		    	 }
		     },
 		     {field:'floors',title:'所在楼层',width:100,halign:'center',
 		    	formatter:function(value ,row ,index){
					return xyzGetDiv(value,0,100);
				}
 		     },
 		     {field:'acreage',title:'面积(㎡)',align:'center',width:75,halign:'center',
 		    	formatter:function(value ,row ,index){
		    		 if(value == "null㎡"){
		    			 return "";
		    		 }
		    		 return value;
		    	 }
 		     },
 		     {field:'type',title:'舱型类型',align:'center',
 		    	formatter:function(value ,row ,index){
					if(value == 0){
						return "内舱";
					}else if(value == 1){
						return "海景";
					}else if(value == 2){
						return "阳台";
					}else if(value == 3){
						return "套房";
					}
				}
 		     },
 		    {field:'cruise',title:'邮轮编号',width:80,hidden:true,halign:'center'},
            {field:'cruiseNameCn',title:'邮轮名称',width:100,halign:'center',
        	    formatter:function(value ,row ,index){
					return xyzGetDiv(value,0,100);
				}
            },
            {field:'isOpen',title:'是否开启',halign:'center',align:'center',
            	formatter: function(value,row,index){
		    		return '<input style="width:42px;height:18px;" class="switchbutton isEnableSwitchbutton" data-options="checked:'+(value=='开'?'true':'false')+'" data-infoTableCode="'+row.numberCode+'"/>';
				}
            },
 		     {field:'upLoadImages',title:'图片',halign:'center',
	        	  formatter: function(value,row,index){
	        		  var html = "";
	        		  if(xyzControlButton("buttonCode_x20161207195605")){
	        			  html = xyzGetA("图片","upLoadImages",[row.numberCode,row.nameCn,row.images],"点我查看和上传舱型图片!","blue");
	        		  }else {
	        			  html = xyzGetA("图片","checkImages",[row.nameCn,row.images],"点我查看舱型图片!","blue");
	        		  }
	        		  return html;
	        	  }
	          },
	         {field:'images',title:'图片地址',hidden:true,halign:'center'},
	         {field:'detail',title:'详情',halign:'center',
			   formatter: function(value,row,index){
				  var html = "";
				  if(xyzControlButton("buttonCode_x20161215110607")){
					  html = xyzGetA("详情","addDetail",[row.numberCode,index],"点我查看和添加舱型详情!","blue");
				  }else{
					  html = xyzGetA("详情","checkDetail",[row.numberCode,index],"点我查看舱型详情!","blue");
				  }
				  return html;
			  }
			},
		     {field:'survey',title:'设施',width:130,halign:'center',
            	 formatter:function(value ,row ,index){
					return xyzGetDiv(value,0,130);
				 }
             },
		     {field:'addDate',title:'添加时间',hidden:true,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'alterDate',title:'修改时间',width:83,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'remark',title:'备注',width:100,halign:'center',
		    	 formatter:function(value ,row ,index){
		    		 return xyzGetDiv(value ,0 ,100);
		    	 }
            }
		]],
		onLoadSuccess : function(data){
			$('.isEnableSwitchbutton').switchbutton({ 
			      onText : '开',
			      offText : '关',
			      onChange: function(checked){ 
			    	  editIsOpen($(this).attr('data-infoTableCode'));
			      } 
			});
		}
	});
}

function editIsOpen(numberCode){
	xyzAjax({
		url : "../CabinWS/editIsOpen.do",
		data : {
			numberCode : numberCode
		},
		success : function(data) {
			if(data.status !=1){
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
}

function loadTable(){
	var tab = $('#cabinManagerTabs').tabs('getSelected');
	var cruise = tab.attr("id"); 
	
	var type = $("#type").combobox("getValue");
	var isOpen = $("#isOpen").combobox("getValue");
	var nameCn = $("#nameCn").val();
	
	$("#cabinManagerTable").datagrid('load',{
		cruise : cruise,
		nameCn : nameCn,
		type : type,
		isOpen : isOpen
	});
}

function initTabs(){
	
	xyzAjax({
		url : "../CabinWS/queryCabinGroupCruise.do",
		type : "POST",
		async : false,
		dataType : "json",
		data : {},
		success : function(data) {
			if(data.status==1){
				cruiseList = data.content;
				for(var t = 0; t < cruiseList.length; t++){
					var cruiseObj = cruiseList[t];
					var cruise = cruiseObj[0];
					var cruiseNameCn = cruiseObj[1];
					addTabs(cruiseNameCn+"("+ cruiseObj[2] +")" , cruise);
				}
				$("#cabinManagerTabs").tabs({
					onSelect : function(title, index){
						var tab = $('#cabinManagerTabs').tabs('getSelected');
						var cruise = tab.attr("id"); 
						loadTable(cruise);
				    }
				});
				$('#cabinManagerTabs').tabs({
					select : 0
				});
			}
		}
	});
}

function addTabs(title, cruise){
	
	$("#cabinManagerTabs").tabs('add',{
	    title : title,    
	    id : cruise,
	    content : '',
	    closable : false
	});
	
}

function addCabinButton(){
	xyzdialog({
		dialog : 'dialogFormDiv_addCabinButton',
		title : '单个新增舱型',
	    href : '../jsp_base/addCabin.html',
	    fit : false,  
	    width: 450,
	    height : 450,
	    buttons:[{
			text:'确定',
			handler:function(){
				addCabinSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addCabinButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzCombobox({
				required : true,
				combobox : 'cruiseForm',
				url : '../ListWS/getCruiseList.do',
				mode : 'remote',
				lazy : false
			});
			xyzTextbox("nameCnForm");
			xyzTextbox("markForm");
			xyzTextbox("floorsForm");
			xyzTextbox("acreageForm");
			
			$(".numberboxForm").numberbox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});
			
			$('#isEnableSwitchbutton').switchbutton({ 
			      onText : '开',
			      offText : '关',
			      onChange: function(checked){ 
			    	  if(checked){
			    		  $("#isOpenForm").val("开");
			    	  }else{
			    		  $("#isOpenForm").val("关");
			    	  }
			      } 
			});
			
		}
	});
}

function addCabinSubmit(){
	if(!$("form").form('validate')){
		return false;
	}
	
	var cruise = $("#cruiseForm").combobox('getValue');
	var nameCn = $("#nameCnForm").val();
	var volume = $("#volumeForm").numberbox("getValue");
	var maxVolume = xyzIsNull($("#maxVolumeForm").numberbox("getValue"))==true ? volume:$("#maxVolumeForm").numberbox("getValue");
	maxVolume = maxVolume < volume?volume : maxVolume;
	var type = $("#typeForm").combobox("getValue");
	var isOpen = $("#isOpenForm").val();
	var mark = $("#markForm").val();
	var floors = $("#floorsForm").val();
	var acreage = $("#acreageForm").val();
	var survey = $("#surveyForm").val();
	var remark = $("#remarkForm").val();

	$.ajax({
		url : "../CabinWS/addCabin.do",
		type : "POST",
		data : {
			cruise : cruise,
			nameCn : nameCn,
			mark : mark, 
			volume : volume,
			maxVolume : maxVolume,
			type : type,
			remark : remark,
			floors : floors,
			acreage : acreage,
			survey : survey,
			isOpen : isOpen
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#cabinManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				window.location.reload();
				$("#dialogFormDiv_addCabinButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editCabinButton(){
	var cabins = $("#cabinManagerTable").datagrid("getChecked");
	if(cabins.length != 1){
		top.$.messager.alert("提示","请先选中单个对象!","info");
		return;
	}
	var row = cabins[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editCabinButton',
		title : '编辑【'+ row.cruiseNameCn +'】【'+ row.nameCn +'】',
	    href : '../jsp_base/editCabin.html',
	    fit : false,  
	    width: 450,
	    height : 450,
	    buttons : [{
			text : '确定',
			handler:function(){
				editCabinSubmit(row.numberCode);
			}
		},{
			text : '取消',
			handler:function(){
				$("#dialogFormDiv_editCabinButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzCombobox({
				required:true,
				combobox:'cruiseForm',
				url : '../ListWS/getCruiseList.do',
				mode: 'remote',
				lazy:false
			});
			
			xyzTextbox("nameCnForm");
			xyzTextbox("markForm");
			xyzTextbox("floorsForm");
			
			$("#cruiseForm").combobox('select',row.cruise);
			$("#nameCnForm").textbox('setValue',row.nameCn);
			$("#markForm").textbox('setValue',row.mark);
			$("#volumeForm").numberbox('setValue',row.volume);
			$("#maxVolumeForm").numberbox('setValue',row.maxVolume);
			$("#typeForm").combobox({
				value : row.type
			});
			$("#remarkForm").val(row.remark);
			$("#floorsForm").textbox('setValue',row.floors);
			$("#acreageForm").val(row.acreage);
			$("#surveyForm").val(row.survey);
			
			$(".numberboxForm").numberbox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});
			
			$("#nameCnForm").textbox({
				required:true
			});
			$("#volumeForm").numberbox({
				required:true
			});
			$("#typeForm").combobox({
				required:true
			});
			
			if(row.isOpen == '开'){
				$('#isEnableSwitchbutton').switchbutton({ 
					checked : true
				});
			}else{
				$('#isEnableSwitchbutton').switchbutton({ 
					checked : false
				});
			}
			
			$("#isOpenForm").val(row.isOpen);
			
			$('#isEnableSwitchbutton').switchbutton({ 
			      onText : '开',
			      offText : '关',
			      onChange: function(checked){ 
			    	  if(checked){
			    		  $("#isOpenForm").val("开");
			    	  }else{
			    		  $("#isOpenForm").val("关");
			    	  }
			      } 
			});
			
		}
	});
}

function editCabinSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var cruise = $("#cruiseForm").combobox('getValue');
	var nameCn = $("#nameCnForm").textbox("getValue");
	var mark = $("#markForm").textbox("getValue");
	var volume = $("#volumeForm").numberbox("getValue");
	var maxVolume = xyzIsNull($("#maxVolumeForm").numberbox("getValue"))==true?volume:$("#maxVolumeForm").numberbox("getValue");
	maxVolume = maxVolume < volume?volume : maxVolume;
	var type = $("#typeForm").combobox("getValue");
	var remark = $("#remarkForm").val();
	var floors = $("#floorsForm").textbox("getValue");
	var acreage = $("#acreageForm").val();
	var survey = $("#surveyForm").val();
	var isOpen = $("#isOpenForm").val();
	
	$.ajax({
		url : "../CabinWS/editCabin.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			cruise : cruise,
			nameCn : nameCn,
			mark : mark,
			volume : volume,
			maxVolume : maxVolume,
			type : type,
			remark : remark,
			floors : floors,
			acreage : acreage,
			survey : survey,
			isOpen : isOpen
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#cabinManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_editCabinButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deleteCabinButton(){
	var cabins = $.map($("#cabinManagerTable").datagrid("getChecked"),
			function(p){
	       return p.numberCode;
	    }
	).join(",");
	
	if(xyzIsNull(cabins)){
		top.$.messager.alert("提示","请先选中需要删除的对象!","info");
		return;
	}
	
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		if(r){
			$.ajax({
				url : "../CabinWS/deleteCabin.do",
				type : "POST",
				data : {
					numberCodes : cabins
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						$("#cabinManagerTable").datagrid("reload");
						top.$.messager.alert("提示","操作成功!","info");
						window.location.reload();
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

function upLoadImages(numberCode,nameCn,imagesUrl){
	if(xyzIsNull(numberCode)){
		top.$.messager.alert("提示","错误的参数!","info");
		return;
	}

	var htmlContent = "<div id='xyzUploadifyGridDiv'></div><br/>"
					+ "<div id='xyzUploadifyButton' style='margin-left:100px;width:200px;height:20px;'></div>";
	xyzdialog({
		dialog : 'dialogFormDiv_upLoadImages',
		title : '上传图片 【'+nameCn+'】',
		content : htmlContent,
	    fit : false,  
	    height : 300,
	    width : 500,
	    buttons : [{
			text:'确定',
			handler:function(){
				upLoadImagesSubmit(numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_upLoadImages").dialog("destroy");
			}
		}],
		onOpen : function(){
			//初始化图片容器
			xyzPicPreview.create({
				xyzPicPreview:'xyzUploadifyGridDiv',
				//初始化后要立即展示的链接
				imageUrls:imagesUrl,
				maxCount:5
			});
			//上传容器
			xyzDropzone.create({
				xyzDropzone:'xyzUploadifyButton',//容器div，允许使用逗号分隔传入多个ID则同时初始化多个xyzDropzone
				params:{"derictoryCode":"cabin"},//上传时需要同时提交的参数键值对
				acceptedExtName:".jpg,.png,.jpeg,.gif,.bmp",//允许文件类型
				maxFilesize:"1024kb",//允许上传的单个文件大小（单位kb）
				btnText:'点击或拖拽文件至此',
				success:function(result){
					xyzPicPreview.addPic('xyzUploadifyGridDiv',result.content.url);
				}
			});
		}
	});
}

function upLoadImagesSubmit(numberCode){
	var urls = xyzPicPreview.getAllPic("xyzUploadifyGridDiv").join(",");

	$.ajax({
		url : "../CabinWS/addImages.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			urls : urls
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#cabinManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_upLoadImages").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function addQuickCabinButton(){
	
	xyzdialog({
		dialog : 'dialogFormDiv_addQuickCabinButton',
		title : '完整新增舱型',
	    href : '../jsp_base/addQuickCabin.html',
	    buttons:[{
			text:'新增',
			handler:function(){
				addQuickCabinSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addQuickCabinButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzCombobox({
				required:true,
				combobox:'cruiseForm',
				url : '../ListWS/getCruiseList.do',
				mode: 'remote',
				lazy : false
			});
			
			$("#minVolumeForm").numberbox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});
			$("#maxVolumeForm").numberbox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});
			$("#addCountForm").numberbox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});
			
			$("#addBtn").click(function(){
				var type = $("#typeForm").combobox("getValue");
				var typeText = $("#typeForm").combobox("getText");
				if(xyzIsNull(type)){
					top.$.messager.alert("提示","请选择类型!","info");
					return false;
				}
				var minVolume = $("#minVolumeForm").numberbox("getValue");
				var maxVolume = $("#maxVolumeForm").numberbox("getValue");
				var count = $("#addCountForm").numberbox("getValue");
				count = (xyzIsNull(count)?0:count) / 1;
				var trCount = 0;
				$.map($("tr[id^='"+ type +"Tr_']"),
					function(p){
						return ++trCount;
					}
				);
				if((trCount+count) > 100){
					top.$.messager.alert("提示",typeText+"总数量不能超过100,最多还能添加"+(100-trCount)+"条数据!","info");
					return false;
				}
				if(maxVolume < minVolume){
					top.$.messager.alert("提示", "要新增的条目容量最大值不能小于最小值!", "info");
					$("#minVolumeForm").numberbox("setValue","1");
					$("#maxVolumeForm").numberbox("setValue","");
				}else{
					addCabinHtml(type, typeText, minVolume, maxVolume, count);
				}
				
			});
			
			/*默认加载内舱房*/
			/*addCabinHtml("inside","内舱房",1,1,1);
			$(".seaviewTr").hide();
			$(".balconyTr").hide();
			$(".suiteTr").hide();*/
			
			/*默认加载4种类型*/
			addCabinHtml("inside","内舱房",1,1,1);
			addCabinHtml("seaview","海景房",1,1,1);
			addCabinHtml("balcony","阳台房",1,1,1);
			addCabinHtml("suite","套房",1,1,1);
			
		}
	});
	
}

function addCabinHtml(type, typeText, minVolume, maxVolume, count){
	for(var t = 0; t < count; t++){
		var length = $("tr[id^='"+type+"Tr_']").length;
		var index = 0;
		if(length > 0){
			index = parseInt( $("tr[id^='"+type+"Tr_']").last().prop('id').split('_')[1] );
			parseInt(index);
			index += 1;
		}else{
			$("."+ type +"Tr").show();
		}
		
		var html = '';
			html += '<tr id="'+ type +'Tr_'+ index +'">';
			html += '<td> <span id="'+ type +'Span_'+ index +'" >'+ typeText +'</span></td>';
			html += '<td>';
			html += ' <input id="'+ type +'_nameCnForm_'+ index +'" style="width:100px;"/>';
			html += '</td>';
			html += '<td>';
			html += ' <input id="'+ type +'_minVolumeForm_'+ index +'" style="width:50px;" value="1"/>';
			html += ' -<input id="'+ type +'_maxVolumeForm_'+ index +'" style="width:50px;"/>';
			html += '</td>';
			html += '<td>';
			html += ' <input id="'+ type +'_markForm_'+ index +'" style="width:80px;"/>';
			html += '</td>';
			html += '<td>';
			html += ' <input style="width:50px;height:18px;" class="switchbutton isEnableSwitchbutton" ';
			html += '  id="'+ type +'_radio_'+ index +'" data-options="checked:\'开\'==\'开\'?\'true\':\'false\'"/>';
			html += ' <input type="hidden" id="'+ type +'_isOpenForm_'+ index +'" value="开"/>';
			html += '</td>';
			html += '<td>';
			html += ' <input id="'+ type +'_floorsForm_'+ index +'" style="width:100px;"/>';
			html += '</td>';
			html += '<td>';
			html += ' <input id="'+ type +'_acreageForm_'+ index +'" style="width:70px;"/>';
			html += '</td>';
			html += '<td>';
			html += ' <input id="'+ type +'_surveyForm_'+ index +'" style="width:100px;"/>';
			html += '</td>';
			html += '<td>';
			html += ' <input id="'+ type +'_remarkForm_'+ index +'" style="width:100px;"/>';
			html += '</td>';
			html += '<td>';
			html += ' <img id="'+ type +'_delete_'+ index +'" alt="点我删除舱型"';
			html += ' src="../image/other/delete.png" title="点我删除舱型" style="cursor:pointer;">';
			html += '</td>';
			html += '</tr>';
		
			$("#"+type+"Table").append(html);
			if(index > 0){
				$("#"+ type +"Span_"+index).hide();
			}else{
				$("#"+ type +"Span_"+index).show();
			}
			
			$("#"+type +"_radio_"+ index).switchbutton({ 
			      onText : '开',
			      offText : '关',
			      onChange: function(checked){ 
			    	  if(checked){
			    		  $("#"+type+"_isOpenForm_"+index).val("开");
			    	  }else{
			    		  $("#"+type+"_isOpenForm_"+index).val("关");
			    	  }
			      } 
			});
			
			cabinHtmlEvent(type , index , maxVolume);
	}
	
}

function cabinHtmlEvent(type , index , maxVolume){
	xyzTextbox(type+'_nameCnForm_'+index);
	xyzTextbox(type+'_markForm_'+index);
	xyzTextbox(type+'_surveyForm_'+index);
	xyzTextbox(type+'_floorsForm_'+index);
	xyzTextbox(type+'_remarkForm_'+index);
	xyzTextbox(type+'_acreageForm_'+index);
	
	$('#'+ type +'_nameCnForm_'+index).textbox({    
		required:true
	});
	
	$('#'+ type +'_minVolumeForm_'+index).numberbox({    
		required:true,
		min:1,    
	    max:20,
	    precision:0,
	    validType:length[1,2],
	    icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).numberbox('clear');
			}
		}]
	});  

	$('#'+ type +'_maxVolumeForm_'+index).numberbox({    
	    min:1,    
	    max:20,
	    precision:0,
	    validType:length[1,2],
	    icons: [{
			iconCls:'icon-clear',
			handler: function(e){
				$(e.data.target).numberbox('clear');
			}
		}]
	}); 
	
	if(maxVolume > 0){
		$('#'+ type +'_maxVolumeForm_'+index).numberbox({    
		    value : maxVolume,
		}); 
	}else{
		$('#'+ type +'_maxVolumeForm_'+index).numberbox({    
		    value : 1,
		}); 
	}
	
	$("#"+ type +"_delete_"+index).click(function(){
		deleteCabinHtml(type , index);
	});
	
}

function deleteCabinHtml(type , index){
	
	var length = $("tr[id^='"+type+"Tr_']").length;
	
	var firstIndex = $("tr[id^='"+type+"Tr_']").first().prop('id').split('_')[1];
	if(index == firstIndex && length == 1){
		$("#"+ type +"Table ."+ type +"Tr").hide();
	}else if(index == firstIndex && length > 1){
		$("span[id^='"+ type +"Span_']").eq(1).show();
	}
	
	$("#"+ type +"Tr_"+index).remove();
	
}

function addQuickCabinSubmit(){
	if(!$("form").form('validate')){
		return false;
	}
	
	var cruise = $("#cruiseForm").combobox('getValue');
	var cabinJson = [];
	
	var countTr = 0;
	/*内舱*/
	var insideLength = $("tr[id^='insideTr_']").length;
	countTr = countTr + insideLength;
	if(insideLength > 0){
		$("tr[id^='insideTr_']").each(function(){
			var index = $(this).prop('id').split('_')[1];
			
			var type = 0;
			var name = $("#inside_nameCnForm_"+index).textbox("getValue");
			var minVolume = $("#inside_minVolumeForm_"+index).numberbox('getValue');
			var maxVolume = $("#inside_maxVolumeForm_"+index).numberbox('getValue');
			var mark = $("#inside_markForm_"+index).textbox("getValue");
			var floors = $("#inside_floorsForm_"+index).numberbox("getValue");
			var acreage = $("#inside_acreageForm_"+index).val();
			var survey = $("#inside_surveyForm_"+index).textbox("getValue");
			var remark = $("#inside_remarkForm_"+index).textbox("getValue");
			var isOpen = $("#inside_isOpenForm_"+index).val();
			
			var tempJson = {
				type : type,
				name : name,
				minVolume : minVolume,
				maxVolume : maxVolume,
				mark : mark,
				floors : floors,
				acreage : acreage,
				survey : survey,
				isOpen : isOpen,
				remark : remark
			};
				
			cabinJson[cabinJson.length] = tempJson;
		});
	}
	
	/*海景*/
	var seaviewLength = $("tr[id^='seaviewTr_']").length;
	countTr = countTr + seaviewLength;
	if(seaviewLength > 0){
		$("tr[id^='seaviewTr_']").each(function(){
			var index = $(this).prop('id').split('_')[1];
			
			var type = 1;
			var name = $("#seaview_nameCnForm_"+index).textbox("getValue");
			var minVolume = $("#seaview_minVolumeForm_"+index).numberbox('getValue');
			var maxVolume = $("#seaview_maxVolumeForm_"+index).numberbox('getValue');
			var mark = $("#seaview_markForm_"+index).textbox("getValue");
			var floors = $("#seaview_floorsForm_"+index).numberbox("getValue");
			var acreage = $("#seaview_acreageForm_"+index).val();
			var survey = $("#seaview_surveyForm_"+index).textbox("getValue");
			var remark = $("#seaview_remarkForm_"+index).textbox("getValue");
			var isOpen = $("#seaview_isOpenForm_"+index).val();
			
			var tempJson = {
				type : type,
				name : name,
				minVolume : minVolume,
				maxVolume : maxVolume,
				mark : mark,
				floors : floors,
				acreage : acreage,
				survey : survey,
				isOpen : isOpen,
				remark : remark
			};
				
			cabinJson[cabinJson.length] = tempJson;
		});
	}
	
	/*阳台*/
	var balconyLength = $("tr[id^='balconyTr_']").length;
	countTr = countTr + balconyLength;
	if(balconyLength > 0){
		$("tr[id^='balconyTr_']").each(function(){
			var index = $(this).prop('id').split('_')[1];
			
			var type = 2;
			var name = $("#balcony_nameCnForm_"+index).textbox("getValue");
			var minVolume = $("#balcony_minVolumeForm_"+index).numberbox('getValue');
			var maxVolume = $("#balcony_maxVolumeForm_"+index).numberbox('getValue');
			var mark = $("#balcony_markForm_"+index).textbox("getValue");
			var floors = $("#balcony_floorsForm_"+index).numberbox("getValue");
			var acreage = $("#balcony_acreageForm_"+index).val();
			var survey = $("#balcony_surveyForm_"+index).textbox("getValue");
			var remark = $("#balcony_remarkForm_"+index).textbox("getValue");
			var isOpen = $("#balcony_isOpenForm_"+index).val();
			
			var tempJson = {
				type : type,
				name : name,
				minVolume : minVolume,
				maxVolume : maxVolume,
				mark : mark,
				floors : floors,
				acreage : acreage,
				survey : survey,
				isOpen : isOpen,
				remark : remark
			};
				
			cabinJson[cabinJson.length] = tempJson;
		});
	}
	
	/*套房*/
	var suiteLength = $("tr[id^='suiteTr_']").length;
	countTr = countTr + suiteLength;
	if(suiteLength > 0){
		$("tr[id^='suiteTr_']").each(function(){
			var index = $(this).prop('id').split('_')[1];
			
			var type = 3;
			var name = $("#suite_nameCnForm_"+index).textbox("getValue");
			var minVolume = $("#suite_minVolumeForm_"+index).numberbox('getValue');
			var maxVolume = $("#suite_maxVolumeForm_"+index).numberbox('getValue');
			var mark = $("#suite_markForm_"+index).textbox("getValue");
			var floors = $("#suite_floorsForm_"+index).numberbox("getValue");
			var acreage = $("#suite_acreageForm_"+index).val();
			var survey = $("#suite_surveyForm_"+index).textbox("getValue");
			var remark = $("#suite_remarkForm_"+index).textbox("getValue");
			var isOpen = $("#suite_isOpenForm_"+index).val();
			
			var tempJson = {
				type : type,
				name : name,
				minVolume : minVolume,
				maxVolume : maxVolume,
				mark : mark,
				floors : floors,
				acreage : acreage,
				survey : survey,
				isOpen : isOpen,
				remark : remark
			};
				
			cabinJson[cabinJson.length] = tempJson;
		});
	}
	
	cabinJson = JSON.stringify(cabinJson);
	
	if(countTr < 1){
		top.$.messager.alert("提示", "至少有一条数据!", "info");
		return false;
	}
	
	$.ajax({
		url : "../CabinWS/addQuickCabin.do",
		type : "POST",
		data : {
			cruise : cruise,
			cabinJosnStr : cabinJson
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#cabinManagerTable").datagrid("reload");
				$("#dialogFormDiv_addQuickCabinButton").dialog("destroy");
				
				top.$.messager.alert("提示", "操作成功!", "info");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function checkImages(nameCn,imagesUrl){
	
	var htmlContent = "<div id='xyzUploadifyGridDiv'></div><br/>"
		+ "<div id='xyzUploadifyButton' style='margin-left:100px;width:200px;height:20px;'></div>";
	
	xyzdialog({
		dialog : 'dialogFormDiv_checkImages',
		title : '查看图片 【'+nameCn+'】',
		content : htmlContent,
		fit : false,  
		height : 300,
		width: 500,
		buttons:[{
			text:'关闭',
			handler:function(){
				$("#dialogFormDiv_checkImages").dialog("destroy");
			}
		}],
		onOpen : function(){
			xyzPicPreview.create({
				xyzPicPreview:'xyzUploadifyGridDiv',
				//初始化后要立即展示的链接
				imageUrls:imagesUrl,
				maxCount:5
			});
		}
	});
}

function addDetail(numberCode,index) {
	$('#cabinManagerTable').datagrid('selectRow',index);    
	var cabinDetail = $('#cabinManagerTable').datagrid('getSelected');    
	
	var htmlContent = "<div>";
	htmlContent += "<textarea name='ckeditor' class='value'></textarea>";
	htmlContent += "</div>";
	xyzdialog({
		dialog : 'dialogFormDiv_addDetail',
		title : '舱型详情',
		content : htmlContent,
		fit : false,
		width : 804,
		height : 500,
		buttons : [ {
			text : '确定',
			handler : function() {
				addDetailSubmit(numberCode);
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_addDetail").dialog("destroy");
			}
		} ],
		onOpen : function() {
			$("textarea[name='ckeditor']").ckeditor({
				width : 780,
				height : 265,
				toolbar : 'Basic'
			});
			$("textarea[class='value']").val(cabinDetail.detail);
		}
	});
}

function addDetailSubmit(numberCode) {
	var value = $("textarea[class='value']").val();
	
	$.ajax({
		url : '../CabinWS/addDetail.do',
		type : "POST",
		data : {
			numberCode : numberCode,
			detail : value
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#cabinManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_addDetail").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function checkDetail(numberCode,index){
	$('#cabinManagerTable').datagrid('selectRow',index);    
	var cabinDetail = $('#cabinManagerTable').datagrid('getSelected');    
	
	var htmlContent = "<div>";
	htmlContent += "<textarea name='ckeditor' class='value'></textarea>";
	htmlContent += "</div>";
	xyzdialog({
		dialog : 'dialogFormDiv_checkDetail',
		title : '舱型详情',
		content : htmlContent,
		fit : false,
		width : 804,
		height : 500,
		buttons : [ {
			text : '关闭',
			handler : function() {
				$("#dialogFormDiv_checkDetail").dialog("destroy");
			}
		} ],
		onOpen : function() {
			$("textarea[name='ckeditor']").ckeditor({
				width : 780,
				height : 265,
				readOnly:true
			});
			$("textarea[class='value']").val(cabinDetail.detail);
		}
	});
}