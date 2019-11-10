$(document).ready(function(){
	xyzTextbox("nameCn");
	xyzTextbox("mark");
	
	xyzCombobox({
		combobox:'company',
		url : '../ListWS/getCompanyList.do',
		mode: 'remote',
		lazy : false
	});
	
	initTable();
	
	$("#cruiseQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = []; 
	if(xyzControlButton("buttonCode_x20161207110502")){
		toolbar[toolbar.length]={
				text: '新增',  
				border:'1px solid #bbb',  
				iconCls: 'icon-add', 
				handler: function(){
					addCruiseButton();
				}
		};
	}
	
	if(xyzControlButton("buttonCode_x20161207110503")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
			text: '编辑',  
			border:'1px solid #bbb',  
			iconCls: 'icon-edit', 
			handler: function(){
				editCruiseButton();
			}
		};
	}	
	
	if(xyzControlButton("buttonCode_x20161207110504")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
				text: '删除',  
				border:'1px solid #bbb',  
				iconCls: 'icon-remove', 
				handler: function(){
					deleteCruiseButton();
				}
		};
	}
	
	xyzgrid({
		table : 'cruiseManagerTable',
		title : "邮轮档案列表",
		url : '../CruiseWS/queryCruiseList.do',
		toolbar : toolbar,
		singleSelect : false, 
		idField : 'numberCode',
		frozenColumns : [[
			{field:'checkboxTemp',checkbox:true},
			{field:'nameCn',title:'邮轮名称',halign:'center',
			  formatter:function(value ,row ,index){
				return xyzGetDiv(value, 0, 50);
			  }
			},
			{field:'nameEn',title:'英文名',halign:'center',
			   formatter:function(value ,row ,index){
				 return xyzGetDiv(value, 0, 40);
			   }
			},
			{field:'mark',title:'代码',align:'center'},
			{field:'company',title:'邮轮公司编号',hidden:true,align:'center'},
			{field:'companyNameCn',title:'邮轮公司',halign:'center',
			    formatter:function(value ,row ,index){
				   return xyzGetDiv(value, 0, 50);
			    }
		    },
		    {field:'images',title:'主图',align:'center',width:80,
				formatter:function(value, row, index){
					 var content = xyzIsNull(value)?"上传图片":"<img src='"+value+"' width='60' height='30'/>";
					 var html = "";
	        		  if(xyzControlButton("buttonCode_x20161207110505")){
	        			  html = xyzGetA(content,"upLoadImages",[row.numberCode,row.nameCn,row.images],"点我查看和上传邮轮图片!","blue");
	        		  }else {
	        			  html = xyzGetA(content,"checkImages",[row.nameCn,row.images],"点我查看邮轮图片!","blue");
	        		  }
	        		  return html;
				}
			},
		    
			{field:'weixinSmallImg',title:'详情图片',align:'center',
			  formatter: function(value,row,index){
				  var html = "";
				  var count ="";
					if(!xyzIsNull(value)){
						count = value.split(",").length;
					}else{
						count = 0;
					}
				  if(xyzControlButton("buttonCode_x20161207110505")){
					  html += "&nbsp;"+xyzGetA("图片("+count+")","upLoadWeixinImg",[row.numberCode,row.nameCn,row.weixinSmallImg,row.weixinLargeImg],"点我查看和上传微信图片!","blue");
				  }else {
					  html += "&nbsp;"+xyzGetA("图片("+count+")","checkWeixinImg",[row.nameCn,row.weixinSmallImg,row.weixinLargeImg],"点我查看微信图片!","blue");
				  }
				  return html;
			  }
			},
			{field:'detail',title:'详情',align:'center',
			  formatter: function(value,row,index){
				  var html = "";
				  if(xyzControlButton("buttonCode_x20161207110506")){
					  html = xyzGetA("详情","addDetail",[row.numberCode,index],"点我查看和添加邮轮详情!","blue");
				  }else{
					  html = xyzGetA("详情","checkDetail",[row.numberCode,index],"点我查看邮轮详情!","blue");
				  }
				  return html;
			  }
			}
		]],
		columns : [[
			  {field:'wide',title:'邮轮宽度(米)',width:85,align:'center'},
			  {field:'numberCode',title:'邮轮编号',width:60,hidden:true},
			  {field:'length',title:'邮轮长度(米)',width:85,align:'center'},
			  {field:'tonnage',title:'吨位(万吨)',align:'center'},
			  {field:'floor',title:'甲板楼层(层)',width:85,align:'center'},
			  {field:'busload',title:'载客量(人)',width:80,align:'center'},
			  {field:'totalCabin',title:'船舱总数(间)',width:85,align:'center'},
			  {field:'avgSpeed',title:'平均航速(节)',width:85,align:'center'},
			  {field:'voltageSource',title:'电压电源(V)',align:'center',hidden:true},
			  {field:'laundromat',title:'自助洗衣',align:'center',hidden:true,
				formatter:function(value ,row ,index){
					if(value == 0){
						return "无";
					}
					return "有";
				}
			  },
			  {field:'library',title:'图书馆',align:'center',hidden:true,
				formatter:function(value ,row ,index){
				   if(value == 0){
					 return "无";
				   }
				   return "有";
				}
			  },
			  {field:'survey',title:'概况',hidden:true,
				 formatter:function(value ,row ,index){
					 return xyzGetDiv(value, 0, 40);
				 }
			  },
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
			  {field:'remark',title:'备注',halign:'center',
				  formatter:function(value ,row ,index){
					  return xyzGetDiv(value ,0 ,50);
				  }
			  }
		]]
	});
}

function loadTable(){
	var company = $("#company").combobox("getValue");
	var nameCn = $("#nameCn").textbox("getValue");
	var mark = $("#mark").textbox("getValue");

	$("#cruiseManagerTable").datagrid('load', {
		mark : mark,
		nameCn : nameCn,
		company : company
	});
}

function addCruiseButton(){
	xyzdialog({
		dialog : 'dialogFormDiv_addCruiseButton',
		title : '新增邮轮档案',
	    href : '../jsp_base/addCruise.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				addCruiseSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addCruiseButton").dialog("destroy");
			}
		}],
		onLoad : function() {
			xyzCombobox({
				required:true,
				combobox:'companyForm',
				url : '../ListWS/getCompanyList.do',
				mode: 'remote',
				lazy : false
			});
			
			xyzTextbox("nameCnForm");
			xyzTextbox("markForm");
			xyzTextbox("nameEnForm");
			xyzTextbox("voltageSourceForm");
			xyzTextbox("remarkForm");
			
			$(".numberboxForm").numberbox({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});
			
		}
	});
}

function addCruiseSubmit(){
	if(!$("form").form('validate')){
		return false;
	}

	var company = $("#companyForm").combobox("getValue");
	var nameCn = $("#nameCnForm").val();
	var mark = $("#markForm").val(); 
	var remark = $("#remarkForm").val();

	var nameEn = $("#nameEnForm").val();
	var wide = $("#wideForm").numberbox("getValue");
	var length = $("#lengthForm").numberbox("getValue");
	var tonnage = $("#tonnageForm").numberbox("getValue");
	var floor = $("#floorForm").numberbox("getValue");
	var busload = $("#busloadForm").numberbox("getValue");
	var totalCabin = $("#totalCabinForm").numberbox("getValue");
	var avgSpeed = $("#avgSpeedForm").numberbox("getValue");
	var voltageSource = $("#voltageSourceForm").val();
	var laundromat = $("#laundromatForm").combobox("getValue");
	var library = $("#libraryForm").combobox("getValue");
	var survey = $("#surveyForm").val();
	
	mark = mark.toUpperCase(); 
	var markReg = /^[A-Z]{2,3}$/;
	if(!markReg.test(mark)){
		top.$.messager.alert("提示","邮轮代码为两到三位大写字母!","info");
		return false;
	}
	var nameEnReg = /^([A-za-z]+[\s]?)+[A-za-z\s]$/;
	if(!xyzIsNull(nameEn) && !nameEnReg.test(nameEn)){
		top.$.messager.alert("提示","英文名称只能输入字母、空格!","info");
		return false;
	}
	if(survey.length > 500){
		top.$.messager.alert("提示","邮轮概况不能超过500个字符!","info");
		return false;
	}
	
	$.ajax({
		url : "../CruiseWS/addCruise.do",
		type : "POST",
		data : {
			nameCn : nameCn,
			mark : mark,
			company : company,
			remark : remark,
			nameEn : nameEn,
			wide : wide,
			length : length,
			tonnage : tonnage,
			floor : floor,
			busload : busload,
			totalCabin : totalCabin,
			avgSpeed : avgSpeed,
			voltageSource : voltageSource,
			laundromat : laundromat,
			library : library,
			survey : survey
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#cruiseManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_addCruiseButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editCruiseButton(){
	var cruises = $("#cruiseManagerTable").datagrid("getChecked");
	if(cruises.length != 1){
		top.$.messager.alert("提示","请先选中单个对象!","info");
		return;
	}
	var row = cruises[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editCruiseButton',
		title : '编辑【'+ row.companyNameCn +'】【'+ row.nameCn +'】【'+ row.mark +'】',
	    href : '../jsp_base/editCruise.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				editCruiseSubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editCruiseButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzCombobox({
				required : true,
				combobox : 'companyForm',
				url : '../ListWS/getCompanyList.do',
				mode : 'remote'
			});
			$("#companyForm").combobox({
				value : row.company
			});
			
			xyzTextbox("nameCnForm");
			xyzTextbox("markForm");
			xyzTextbox("nameEnForm");
			xyzTextbox("voltageSourceForm");
			xyzTextbox("remarkForm");
			
			$("#nameCnForm").textbox('setValue',row.nameCn);
			$("#markForm").textbox('setValue',row.mark);
			$("#remarkForm").textbox('setValue',row.remark);
			$("#nameEnForm").textbox('setValue',row.nameEn);
			$("#wideForm").numberbox("setValue",row.wide);
			$("#lengthForm").numberbox("setValue",row.length);
			$("#tonnageForm").numberbox("setValue",row.tonnage);
			$("#floorForm").numberbox("setValue",row.floor);
			$("#busloadForm").numberbox("setValue",row.busload);
			$("#totalCabinForm").numberbox("setValue",row.totalCabin);
			$("#avgSpeedForm").numberbox("setValue",row.avgSpeed);
			$("#voltageSourceForm").textbox('setValue',row.voltageSource);
			$("#laundromatForm").combobox("setValue",row.laundromat);
			$("#libraryForm").combobox("setValue",row.library);
			$("#surveyForm").val(row.survey);
			
			$("#nameCnForm").textbox({
				required:true
			});
			$("#markForm").textbox({
				required:true
			});
			$(".numberboxForm").numberbox({
				required:true,
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).numberbox('clear');
					}
				}]
			});
		
			$("#voltageSourceForm").textbox({
				required:true
			});
			$("#surveyForm").validatebox({
				required:true
			});
		}
	});
}

function editCruiseSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var company = $("#companyForm").combobox("getValue");
	var nameCn = $("#nameCnForm").val();
	var mark = $("#markForm").val(); 
	var remark = $("#remarkForm").val();
	
	var nameEn = $("#nameEnForm").val();
	var wide = $("#wideForm").numberbox("getValue");
	var length = $("#lengthForm").numberbox("getValue");
	var tonnage = $("#tonnageForm").numberbox("getValue");
	var floor = $("#floorForm").numberbox("getValue");
	var busload = $("#busloadForm").numberbox("getValue");
	var totalCabin = $("#totalCabinForm").numberbox("getValue");
	var avgSpeed = $("#avgSpeedForm").numberbox("getValue");
	var voltageSource = $("#voltageSourceForm").val();
	var laundromat = $("#laundromatForm").combobox("getValue");
	var library = $("#libraryForm").combobox("getValue");
	var survey = $("#surveyForm").val();
	
	mark = mark.toUpperCase(); 
	
	var markReg = /^[A-Z]{2,3}$/;
	if(!markReg.test(mark)){
		top.$.messager.alert("提示","邮轮代码为两到三位大写字母!","info");
		return false;
	}
	var nameEnReg = /^([A-za-z]+[\s]?)+[A-za-z\s]$/;
	if(!xyzIsNull(nameEn) && !nameEnReg.test(nameEn)){
		top.$.messager.alert("提示","英文名称只能输入字母、空格!","info");
		return false;
	}
	
	$.ajax({
		url : "../CruiseWS/editCruise.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			nameCn : nameCn,
			mark : mark,
			company : company,
			remark : remark,
			nameEn : nameEn,
			wide : wide,
			length : length,
			tonnage : tonnage,
			floor : floor,
			busload : busload,
			totalCabin : totalCabin,
			avgSpeed : avgSpeed,
			voltageSource : voltageSource,
			laundromat : laundromat,
			library : library,
			survey : survey
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#cruiseManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_editCruiseButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deleteCruiseButton(){
	var cruises = $.map($("#cruiseManagerTable").datagrid("getChecked"),
		function(p){
	       return p.numberCode;
	    }
	).join(",");
	if(xyzIsNull(cruises)){
		top.$.messager.alert("提示","请先选中需要删除的对象!","info");
		return;
	}
	
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		if(r){
			$.ajax({
				url : "../CruiseWS/deleteCruise.do",
				type : "POST",
				data : {
					numberCodes : cruises
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						$("#cruiseManagerTable").datagrid("reload");
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

function upLoadImages(numberCode,nameCn,imagesUrl){
	if(xyzIsNull(numberCode)){
		top.$.messager.alert("提示","错误的参数!","info");
		return;
	}

	var htmlContent = "<div id='xyzUploadifyGridDiv'></div><br/>"
					+ "<div id='xyzUploadifyButton' style='margin-left:100px;width:200px;height:20px;'></div>";
	xyzdialog({
		dialog : 'dialogFormDiv_upLoadImages',
		title : '上传图片 【'+ nameCn +'】',
		content : htmlContent,
	    fit : false,  
	    height : 300,
	    width: 500,
	    buttons:[{
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
				params:{"derictoryCode":"cruise"},//上传时需要同时提交的参数键值对
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
		url : "../CruiseWS/addImages.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			urls : urls
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#cruiseManagerTable").datagrid("reload");
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

function upLoadWeixinImg(numberCode, nameCn, weixinSmallImg, weixinLargeImg){
	if(xyzIsNull(numberCode)){
		top.$.messager.alert("提示","错误的参数!","info");
		return;
	}
	
	var html = '<div style="margin-left:10px;">';
	html += '<p>';
	html += ' <span style="font-size:16px;">微信小图</span>';
	html += ' <div id="smallImgDiv" style="margin-left:120px;width:180px;"></div><br/>';
	html += ' <div id="smallImgButton" style="margin-left:100px;width:180px;height:20px;"></div>';
	html += '</p>';
	html += '<br/><br/>';
	html += '<p>';
	html += ' <span style="font-size:16px;">微信大图</span>';
	html += ' <div id="largeImgDiv" style="margin-left:14px;"></div><br/>';
	html += ' <div id="largeImgButton" style="margin-left:100px;width:180px;height:20px;"></div>';
	html += '</p></div>';

	xyzdialog({
		dialog : 'dialogFormDiv_upLoadWeixinImg',
		title : '上传微信端图片 【'+ nameCn +'】',
		content : html,
	    fit : false,  
	    width: 550,
	    height : 550,
	    buttons:[{
			text:'确定',
			handler:function(){
				upLoadWeixinImgSubmit(numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_upLoadWeixinImg").dialog("destroy");
			}
		}],
		onOpen : function(){
			//初始化图片容器
			xyzPicPreview.create({
				xyzPicPreview : 'smallImgDiv',
				//初始化后要立即展示的链接
				imageUrls : weixinSmallImg,
				maxCount : 1
			});
			
			xyzPicPreview.create({
				xyzPicPreview : 'largeImgDiv',
				//初始化后要立即展示的链接
				imageUrls : weixinLargeImg,
				maxCount : 4,
				moveWidth : 80
			});
			
			//上传容器
			xyzDropzone.create({
				xyzDropzone : 'smallImgButton',        //容器div，允许使用逗号分隔传入多个ID则同时初始化多个xyzDropzone
				params : {"derictoryCode":"cruise"},   //上传时需要同时提交的参数键值对
				acceptedExtName : ".jpg,.png,.jpeg,.gif,.bmp",  //允许文件类型
				maxFilesize : "1024kb",     //允许上传的单个文件大小（单位kb）
				btnText : '点击或拖拽文件至此',
				success : function(result){
					xyzPicPreview.addPic('smallImgDiv', result.content.url);
				}
			});
			xyzDropzone.create({
				xyzDropzone:'largeImgButton',                 //容器div，允许使用逗号分隔传入多个ID则同时初始化多个xyzDropzone
				params:{"derictoryCode":"cruise"},            //上传时需要同时提交的参数键值对
				acceptedExtName:".jpg,.png,.jpeg,.gif,.bmp",  //允许文件类型
				maxFilesize:"1024kb",          //允许上传的单个文件大小（单位kb）
				btnText:'点击或拖拽文件至此',
				success:function(result){
					xyzPicPreview.addPic('largeImgDiv', result.content.url);
				}
			});
		}
	});
}

function upLoadWeixinImgSubmit(numberCode){
	var smallImg = xyzPicPreview.getAllPic("smallImgDiv").join(",");
	var largeImg = xyzPicPreview.getAllPic("largeImgDiv").join(",");

	$.ajax({
		url : "../CruiseWS/addWeixinImages.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			smallImg : smallImg,
			largeImg : largeImg
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#cruiseManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_upLoadWeixinImg").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
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

function checkWeixinImg(nameCn, weixinSmallImg, weixinLargeImg){
	var htmlContent = '<div style="margin-left:10px;">';
	htmlContent += '<p>';
	htmlContent += ' <span style="font-size:16px;">微信小图</span>';
	htmlContent += ' <div id="smallImgDiv" style="margin-left:120px;width:180px;"></div>';
	htmlContent += '</p>';
	htmlContent += '<br/><br/>';
	htmlContent += '<p>';
	htmlContent += ' <span style="font-size:16px;">微信大图</span>';
	htmlContent += ' <div id="largeImgDiv"></div>';
	htmlContent += '</p></div>';
	
	xyzdialog({
		dialog : 'dialogFormDiv_checkWeixinImg',
		title : '查看微信图片 【'+nameCn+'】',
		content : htmlContent,
		fit : false,  
		width : 520,
		height : 520,
		buttons : [{
			text:'关闭',
			handler:function(){
				$("#dialogFormDiv_checkWeixinImg").dialog("destroy");
			}
		}],
		onOpen : function(){
			xyzPicPreview.create({
				xyzPicPreview : 'smallImgDiv',
				imageUrls : weixinSmallImg,  //初始化后要立即展示的链接
				maxCount : 1
			});
			
			xyzPicPreview.create({
				xyzPicPreview : 'largeImgDiv',
				imageUrls : weixinLargeImg,    //初始化后要立即展示的链接
				maxCount : 4
			});
			
		}
	});
}

function addDetail(numberCode,index) {
	$('#cruiseManagerTable').datagrid('selectRow',index);    
	var cruiseDetail = $('#cruiseManagerTable').datagrid('getSelected');    
	
	var htmlContent = "<div>";
	htmlContent += "<textarea name='ckeditor' class='value' ></textarea>";
	htmlContent += "</div>";
	xyzdialog({
		dialog : 'dialogFormDiv_addDetail',
		title : '邮轮详情',
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
			$("textarea[class='value']").val(cruiseDetail.detail);
		}
	});
}

function addDetailSubmit(numberCode) {
	var value = $("textarea[class='value']").val();
	
	$.ajax({
		url : '../CruiseWS/addDetail.do',
		type : "POST",
		data : {
			numberCode : numberCode,
			detail : value
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#cruiseManagerTable").datagrid("reload");
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
	$('#cruiseManagerTable').datagrid('selectRow',index);    
	var cruiseDetail = $('#cruiseManagerTable').datagrid('getSelected');    
	
	var htmlContent = "<div>";
	htmlContent += "<textarea name='ckeditor' class='value'></textarea>";
	htmlContent += "</div>";
	xyzdialog({
		dialog : 'dialogFormDiv_checkDetail',
		title : '邮轮详情',
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
			$("textarea[class='value']").val(cruiseDetail.detail);
		}
	});
}