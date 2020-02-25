"use strict";
var query="";
function _toConsumableArray(t) {
    if (Array.isArray(t)) {
        for (var a = 0, e = Array(t.length); a < t.length; a++)
            e[a] = t[a];
        return e
    }
    return Array.from(t)
}
function ksort(t) {
    for (var a = [], e = [], i = ["c", "b", "s"], n = 0; n < t.length; n++) {
        var l = t[n]
          , s = i.indexOf(l[0]);
        s > -1 && !/se(.+?)se/.test(l) ? a[s] = l : e.push(l)
    }
    return [].concat(a, _toConsumableArray(e.sort()))
}
function initKey(t) {
    var a = window.keys.split("_")
      , e = a.map(function(a, e) {
        return a[0] !== t[0] || /se(.+?)se/.test(a) ? a : t
    })
      , i = [].concat(_toConsumableArray(e));
    return "c" === t[0] && (i = i.filter(function(t) {
        return !/b\d+/.test(t)
    }),
    i = i.filter(function(t) {
        return !/t\d+/.test(t)
    }),
    i = i.filter(function(t) {
        return !/s\d+/.test(t)
    })),
    "t" === t[0] && (i = i.filter(function(t) {
        return !/b\d+/.test(t)
    }),
    i = i.filter(function(t) {
        return !/s\d+/.test(t)
    })),
    "b" === t[0] && (i = i.filter(function(t) {
        return !/s\d+/.test(t)
    })),
    i.indexOf(t) === -1 && i.push(t),
    i = i.filter(function(t) {
        return t
    }),
    i = ksort(i),
    i.join("_")
}
function tipBox(t, a) {
    return '<a href="/' + initKey(t) + '/" class="hot-tips clear-city icon-handle-tips clicks" style="display: flex; display: -ms-flexbox; white-space: nowrap; float: left;">\n\t\t\t\t<div>' + a + "</div>\n\t\t\t</a>"
}
function listClick() {
    $(".initKey-click").unbind("click").on("click", function(t) {
        var a = $(this)
          , e = a.attr("data-clickType")
          , i = a.attr("data-value");
        
        var label="";
        
        console.log("i=="+i);
        
        var type="";
        if(i=="sortNew"||i=="sortMinPrice"||i=="sortMaxPrice"){
        	type="sort";
        }else if(i=="QYC"||i=="ZHC"||i=="ZXC"||i=="OTHER"){
        	type="cartype";
        }else if(i.indexOf("_")>0){
        	type="price";
        }else{
        	type="logo";
        }
        
        if(i=="sortNew"){
        	label="最新上架";
        }else  if(i=="sortMinPrice"){
        	label="价格最低";
        }else  if(i=="sortMaxPrice"){
        	label="价格最高";
        }
        
        if(i=="解放"||i=="东风"||i=="重汽"||i=="上汽红岩"||i=="陕汽"||i=="大运"||i=="福田"){
        	label=i;
        }
        
        if(i=="QYC"){
        	label="牵引车";
        }else  if(i=="ZHC"){
        	label="载货车";
        }else  if(i=="ZXC"){
        	label="自卸车";
        }else  if(i=="OTHER"){
        	label="其他";
        }
        
        if(i=="0_5"){
        	label="5W以下";
        }else  if(i=="5_10"){
        	label="5W-10W";
        }else  if(i=="10_15"){
        	label="10W-15W";
        }else  if(i=="15_20"){
        	label="15W-20W";
        }else  if(i=="20_500"){
        	label="20W以上";
        }
        
        var queryHtml='';
		 if($(".c-handle-tips-con").length>0){
			if(type=="sort"){
				if($("#sort").length>0){
					$("#sort").html(label);
					$("#sort").attr("value",i);
				}else{
					$(".icon-handle-tips:last").after("<a id='sort' class='icon-handle-tips' value='"+i+"'>"+label+"</a>");
				}
			}else if(type=="cartype"){
				if($("#cartype").length>0){
					$("#cartype").html(label);
					$("#cartype").attr("value",i);
				}else{
					$(".icon-handle-tips:last").after("<a id='cartype' class='icon-handle-tips' value='"+i+"'>"+label+"</a>");
				}
			}else if(type=="price"){
				if($("#price").length>0){
					$("#price").html(label);
					$("#price").attr("value",i);
				}else{
					$(".icon-handle-tips:last").after("<a id='price' class='icon-handle-tips' value='"+i+"'>"+label+"</a>");
				}
			}else if(type=="logo"){
				if($("#logo").length>0){
					$("#logo").html(label);
					$("#logo").attr("value",i);
				}else{
					$(".icon-handle-tips:last").after("<a id='price' class='icon-handle-tips' value='"+i+"'>"+label+"</a>");
				}
			}
		 }else{
			 queryHtml+='<div class="c-handle-tips-con">';
			 queryHtml+='<div class="c-flex c-box-flex scroll">';
			 queryHtml+='<div style="width: 1000%;">';
			 queryHtml+='<a class="icon-handle-tips" id="'+type+'" value="'+i+'">'+label+'</a>';
			 queryHtml+='</div>';
			 queryHtml+='<a href="/query.html" class="init-btn br-l" style="float:right">重置</a></div></div>';
			 $(".c-list-nav-con").after(queryHtml);
		 }
		
		 
		 $(".c-fixed").removeClass("active");
		 $(".c-right-handle").removeClass("active");
        
        
		 query="";
		 $(".icon-handle-tips").each(function(){
			 query=query+$(this).attr("value")+"-";
		 });
		 
		 page=1;
		 
        $.ajax({
			url : "/WebWS/getSearchData.xyz",
			type : "POST",
			data : {
				page : 1,
				rows:pageSize,
				query:query
			},
			async : false,
			dataType : "json",
			success : function(data) {
				
				if (data.status == 1) {
					countPage=parseInt((data.content.total-1)/pageSize)+1;
					
					console.log("query  countPage=="+countPage);
					
					var truckList=data.content.rows;
					var html='';
					 for(var i=0;i<truckList.length;i++){
						var dataObj=truckList[i];
						var images=JSON.parse(dataObj.images);
						
						html+='<a class="c-list-cell c-flex c-truck-list" href="detail.html?numberCode='+dataObj.numberCode+'">';
						html+='<div class="photo">';
						html+='<img class="truck-photo-load" src="'+images[0]+'" data-src="'+images[0]+'">';
						html+='</div>';
						html+='<div class="c-box-flex content">';
						html+=' <div class="title">';
						var type="其他";
						if(dataObj.truckType=="QYC"){
							type="牵引车";
						}else if(dataObj.truckType=="ZHC"){
							type="载货车";
						}else if(dataObj.truckType=="ZXC"){
							type="自卸车";
						}
						html+='<div class="text">'+type+' '+dataObj.truckLogo+' </div>';
						html+='</div>';
						html+='<div class="info">'+dataObj.cardDate.substring(0,7)+'月/'+dataObj.mile+'万公里/'+dataObj.year+'年</div>';
						html+='<div class="c-align-center">';
						html+='<div class="price">'+dataObj.price+'万</div>';
						html+='</div>';
						html+='</div>';
						html+='</a>	';
					 }
					 $('html,body').animate({scrollTop: '0px'}, 200);
					 $(".c-buy-car-list").html(html);
					 
					 
				} else {
					alert(data.msg);
				}
				
				
			}
		});
        
       
        
        
        $(".icon-handle-tips").click(function(){
        	$(this).remove();
        	if($(".icon-handle-tips").length==0){
        		window.location.replace("query.html");
        	}
        	
        	
        	
        	var query="";
   		 $(".icon-handle-tips").each(function(){
   			 query=query+$(this).attr("value")+"-";
   		 });
   		 
           $.ajax({
   			url : "/WebWS/getSearchData.xyz",
   			type : "POST",
   			data : {
   				page : 1,
   				rows:pageSize,
   				query:query
   			},
   			async : false,
   			dataType : "json",
   			success : function(data) {
   				
   				if (data.status == 1) {
   					countPage=parseInt((data.content.total-1)/pageSize)+1;
   					
   					
   					console.log("lyddd==="+countPage);
   					
   					var truckList=data.content.rows;
   					var html='';
   					 for(var i=0;i<truckList.length;i++){
   						var dataObj=truckList[i];
   						var images=JSON.parse(dataObj.images);
   						
   						html+='<a class="c-list-cell c-flex c-truck-list" href="detail.html?numberCode='+dataObj.numberCode+'">';
   						html+='<div class="photo">';
   						html+='<img class="truck-photo-load" src="'+images[0]+'" data-src="'+images[0]+'">';
   						html+='</div>';
   						html+='<div class="c-box-flex content">';
   						html+=' <div class="title">';
   						var type="其他";
   						if(dataObj.truckType=="QYC"){
   							type="牵引车";
   						}else if(dataObj.truckType=="ZHC"){
   							type="载货车";
   						}else if(dataObj.truckType=="ZXC"){
   							type="自卸车";
   						}
   						html+='<div class="text">'+type+' '+dataObj.truckLogo+' </div>';
   						html+='</div>';
   						html+='<div class="info">'+dataObj.cardDate.substring(0,7)+'月/'+dataObj.mile+'万公里/'+dataObj.year+'年</div>';
   						html+='<div class="c-align-center">';
   						html+='<div class="price">'+dataObj.price+'万</div>';
   						html+='</div>';
   						html+='</div>';
   						html+='</a>	';
   					 }
   					 $(".c-buy-car-list").html(html);
   					 
   					 
   				} else {
   					alert(data.msg);
   				}
   				
   				
   			}
   		});
        	
        	
        	
        });
        
        return;
        
        if ("c100001" === i)
            return window.keys = initKey(i),
            window.provincePinyin ? window.location.href = "/" + window.provincePinyin + "/" + encodeURIComponent(window.keys) + "/" : window.location.href = "/" + encodeURIComponent(window.keys) + "/",
            !1;
        var n = "";
        if (e !== clickType) {
            switch (window.keys = initKey(i),
            e) {
            case "cat":
                n = "brand",
                ajaxId.subId = i.replace("c", ""),
                "series" === clickType && ajaxId.brandId ? (window.keys = initKey("b" + ajaxId.brandId),
                n = "series") : n = "brand",
                4 == ajaxId.subId && (n = "special_cat");
                break;
            case "special_cat":
                ajaxId.subId = i.replace("t", ""),
                n = "brand";
                break;
            case "brand":
                ajaxId.brandId = i.replace("b", ""),
                n = "series"
            }
            return _ajax(n)
        }
        var l = ("" + a.attr("data-href")).replace("/", "");
        window.provincePinyin ? window.location.href = "/" + window.provincePinyin + "/" + encodeURIComponent(l) + "/" : window.location.href = "/" + encodeURIComponent(l) + "/"
    })
}
function selectList(t, a, e) {
    if (!a.value)
        return "";
    var i = "";
    return e && (i += '<div data-index="' + e + '" class="c-small-title-con">' + e + "</div>"),
    i += '<div class="c-list-cell c-flex initKey-click" data-clickType="' + t + '" data-value="' + a.value + '" data-href="' + initKey(a.value) + '">' + a.name + "</div>"
}
function addHtml(t, a) {
    for (var e = selectData[t], i = e.data, n = e.title, l = "", s = "", o = 0; o < i.length; o++) {
        var r = i[o];
        r.letter !== s ? (s = r.letter,
        l += selectList(t, r, s)) : l += selectList(t, r)
    }
    l || (l = '<div data-text="当前条件下没有' + (n || "").replace("选择", "") + '" class="no-truck-con c-flex-center icon-wushuju"></div>'),
    $title.text(n),
    $cells.html(l);
    var c = "";
    if (a) {
        for (var d = 0; d < a.length; d++)
            c += '<div class="list">' + a[d] + "</div>";
        $lettersList.addClass("show").html(c)
    } else
        $lettersList.removeClass("show").html("");
    setTimeout(function() {
        listClick()
    }, 0)
}
var ajaxId = {
    subId: "",
    brandId: ""
}
  , city = document.querySelector(".city .text")
  , clickType = ""
  , oldKeys = window.keys
  , special_cat_id = "";
window.keys.split("_").map(function(t, a) {
    "c" === t[0] && (ajaxId.subId = t.replace("c", "")),
    "b" === t[0] && (ajaxId.brandId = t.replace("b", "")),
    "t" === t[0] && (special_cat_id = t.replace("t", ""))
}),
4 == ajaxId.subId && ajaxId.brandId && (ajaxId.subId = special_cat_id);
var selectData = {
    order: {
        title: "选择排序",
        data: [{
            name: "最新上架",
            value: "sortNew"
        }, {
            name: "价格最低",
            value: "sortMinPrice"
        }, {
            name: "价格最高",
            value: "sortMaxPrice"
        }]
    },
    cat: {
        title: "选择车型",
        data: [{
            name: "牵引车",
            value: "QYC"
        }, {
            name: "载货车",
            value: "ZHC"
        }, {
            name: "自卸车",
            value: "ZXC"
        }, {
            name: "其他",
            value: "OTHER"
        }]
    },
    logo: {
        title: "选择品牌",
        data: [{
            name: "解放",
            value: "解放"
        }, {
            name: "东风",
            value: "东风"
        }, {
            name: "重汽",
            value: "重汽"
        }, {
            name: "上汽红岩",
            value: "上汽红岩"
        }, {
            name: "陕汽",
            value: "陕汽"
        }, {
            name: "大运",
            value: "大运"
        }, {
            name: "福田",
            value: "福田"
        }]
    },
    price: {
        title: "选择价格",
        data: [{
            name: "5W以下",
            value: "0_5"
        }, {
            name: "5W-10W",
            value: "5_10"
        }, {
            name: "10W-15W",
            value: "10_15"
        }, {
            name: "15W-20W",
            value: "15_20"
        }, {
            name: "20W以上",
            value: "20_500"
        }]
    },
    brand: {
        title: "选择品牌",
        data: []
    },
    special_cat: {
        title: "选择专用车",
        data: []
    },
    series: {
        title: "选择车系",
        data: []
    }
};
$("head").append("<style>.hot-tips:after{display:none;}</style>");
var _ajax = function(t) {
    return !!t && (selectData[t].data.length ? (addHtml(t),
    !1) : void ("brand" === t || "special_cat" === t ? 4 != ajaxId.subId || selectData.special_cat.data.length ? ($sq.loading("show"),
    $sq.ajax({
        url: $sq.api.truckUrl + "getproductdataforershouche&subId=" + ajaxId.subId,
        dataType: "jsonp",
        success: function(a) {
            if ($sq.loading("hide"),
            "ok" === a.info) {
                var e = []
                  , i = []
                  , n = a.data.list || {};
                for (var l in n) {
                    i.push(l);
                    for (var s in n[l])
                        e.push({
                            value: "b" + n[l][s].F_BrandId,
                            name: n[l][s].F_BrandName,
                            logo: n[l][s].F_BrandLogo,
                            next: !0,
                            letter: l
                        })
                }
                selectData[t] = {
                    title: selectData[t].title,
                    data: e
                },
                addHtml(t, i)
            } else
                $sq.handle(a.data)
        },
        error: function() {
            $sq.loading("hide")
        }
    })) : ($sq.loading("show"),
    $sq.ajax({
        url: $sq.api.truckUrl + "getsubcateforcate&cateId=" + ajaxId.subId,
        dataType: "jsonp",
        success: function(a) {
            if ($sq.loading("hide"),
            "ok" === a.info) {
                for (var e = [], i = a.data, n = 0; n < i.length; n++)
                    e.push({
                        value: "t" + i[n].F_SubCategoryId,
                        name: i[n].F_SubCategoryName,
                        next: !0
                    });
                selectData[t] = {
                    title: selectData[t].title,
                    data: e
                },
                selectData.brand = {
                    title: selectData.brand.title,
                    data: []
                },
                addHtml(t)
            } else
                $sq.handle(a.data)
        },
        error: function() {
            $sq.loading("hide")
        }
    })) : "series" === t && ($sq.loading("show"),
    $sq.ajax({
        url: $sq.api.truckUrl + "getproductdataforershouche&subId=" + ajaxId.subId + "&brandId=" + ajaxId.brandId,
        dataType: "jsonp",
        success: function(a) {
            if ($sq.loading("hide"),
            "ok" === a.info) {
                for (var e = [], i = a.data.list || {}, n = 0; n < i.length; n++)
                    e.push({
                        value: "s" + i[n].F_SeriesId,
                        name: i[n].F_SeriesName + " " + i[n].F_SubCategoryName,
                        F_SubCategoryId: i[n].F_SubCategoryId
                    });
                selectData[t] = {
                    title: selectData[t].title,
                    data: e
                },
                addHtml(t)
            } else
                $sq.handle(a.data)
        },
        error: function() {
            $sq.loading("hide")
        }
    }))))
}
  , $cells = $('.c-list-cells[type="keys"]')
  , $title = $('.c-title[type="keys"]')
  , $lettersList = $(".c-letters-list")
  , $rightScroll = $('.c-page[data-type="right-scroll"]');
$lettersList.on("touchmove", function(t) {
    t.preventDefault();
    var a = $(this).get(0)
      , e = a.getBoundingClientRect()
      , i = t.originalEvent.changedTouches[0].pageY - e.top - $(document).scrollTop();
    if (i > 0 && i < e.height) {
        var n = Math.round(i / 18)
          , l = $(this).find(".list").eq(n).text()
          , s = 0;
        if (l) {
            var o = $rightScroll.find('.c-small-title-con[data-index="' + l + '"]').prevAll();
            o.each(function(t, a) {
                s += $(a).outerHeight(!0)
            }),
            $rightScroll.scrollTop(s)
        }
    }
}),
$(".c-list-nav-con div.icon-list-nav").on("click", function() {
    var t = $(this).attr("data-type");
    
    return clickType = t,
    "brand" !== t && "series" !== t || "100001" != ajaxId.subId ? ("order" === t && city && "全国" === city.innerText && (selectData.order.data.length = 3),
    $(".c-fixed[type], .c-right-handle[type]").addClass("active"),
    "cat" === t || "brand" === t || "series" === t ? ("brand" === t && !ajaxId.subId || "series" === t && !ajaxId.subId ? t = "cat" : "brand" === t && 4 == ajaxId.subId ? t = "special_cat" : "series" !== t || ajaxId.brandId || (t = "brand",
    4 == ajaxId.subId && (t = "special_cat")),
    _ajax(t)) : void addHtml(t)) : ($sq.handle("挂车无需选择"),
    !1)
}),
$(".c-fixed[type], .c-return-noclick").on("click", function() {
	
	console.log("t==22222222222=");
	
    $(".c-fixed[type], .c-right-handle[type]").removeClass("active"),
    ajaxId = {
        subId: "",
        brandId: ""
    },
    clickType = "",
    window.keys = oldKeys,
    window.keys.split("_").map(function(t, a) {
        "c" === t[0] && (ajaxId.subId = t.replace("c", "")),
        "b" === t[0] && (ajaxId.brandId = t.replace("b", ""))
    }),
    $lettersList.removeClass("show").html("")
});
var listCityId = $sq.getCookie("tao_privite_prov_no")
  , list = {
    loading: $(".c-d-loading-cell"),
    max: 40,
    time: 100,
    ajaxIng: !1,
    page: 2,
    morePage: 1,
    moreCityisOk: !1,
    filterCity: [],
    callback: function() {
        $sq.scrollBottom() && (this.loading.find(".c-d-loading").removeClass("active"),
        this.loadmore())
    },
    loadmore: function() {
        var t = this;
        console.log("page==="+page);
        console.log("countPage==="+countPage);
        console.log("query==="+query);
        
        if(page==1){
        	page++;
        }
        
        if(page==(countPage+1)){
        	 $("#loading>i").css("display","none");
             $("#loading>span").html("到底啦~").css("color","#BDBDBD");
        	return;
        }
        return t.moreCityisOk ? (t.moreCity(),
        !1) : !t.ajaxIng && (t.ajaxIng = !0,
        void $sq.ajax({
            url: "/WebWS/getSearchData.xyz",
            type: "GET",
            data: {
            	page : page,
   				rows:pageSize,
   				query:query
            },
            success: function(data) {
            	
            	
            	if (data.status == 1) {
					countPage=parseInt((data.content.total-1)/pageSize)+1;
					var truckList=data.content.rows;
					var html='';
					 for(var i=0;i<truckList.length;i++){
						var dataObj=truckList[i];
						var images=JSON.parse(dataObj.images);
						
						html+='<a class="c-list-cell c-flex c-truck-list" href="detail.html?numberCode='+dataObj.numberCode+'">';
						html+='<div class="photo">';
						html+='<img class="truck-photo-load" src="'+images[0]+'" data-src="'+images[0]+'">';
						html+='</div>';
						html+='<div class="c-box-flex content">';
						html+=' <div class="title">';
						var type="其他";
						if(dataObj.truckType=="QYC"){
							type="牵引车";
						}else if(dataObj.truckType=="ZHC"){
							type="载货车";
						}else if(dataObj.truckType=="ZXC"){
							type="自卸车";
						}
						html+='<div class="text">'+type+' '+dataObj.truckLogo+' </div>';
						html+='</div>';
						html+='<div class="info">'+dataObj.cardDate.substring(0,7)+'月/'+dataObj.mile+'万公里/'+dataObj.year+'年</div>';
						html+='<div class="c-align-center">';
						html+='<div class="price">'+dataObj.price+'万</div>';
						html+='</div>';
						html+='</div>';
						html+='</a>	';
					 }
					 
					 $(".c-truck-list:last").after(html);
					 page++;
					 
					 
				} else {
					alert(data.msg);
				}
            	
            	
            },
            always: function() {
                t.ajaxIng = !1
            }
        }))
    },
    init: function(t) {
        var a = this;
        $(window).on("scroll", t);
        var e = $sq.storageGet("list-list");
        if (e) {
            var i = JSON.parse(e)
              , n = i.list
              , l = i.page
              , s = i.position
              , o = i.morePage
              , r = i.filterCity
              , c = i.moreCityisOk
              , d = i.popular;
            $(".c-buy-car-list").html(n),
            a.page = l,
            a.morePage = o,
            a.filterCity = r,
            a.moreCityisOk = c,
            a.popular = d,
            $(document).scrollTop(s),
            a.loading = $(".c-d-loading-cell"),
            $sq.storageRemove("list-list")
        }
        $(".c-buy-car-list a.c-truck-list").length < a.max && (a.loading.find(".c-d-loading").addClass("no-more"),
        a.moreCity()),
        $(document).on("click", "a.c-truck-list, a.to-react-page", function(t) {
            t.preventDefault();
            var e = $(this).attr("href")
              , i = $(".c-buy-car-list").html();
            $sq.storageSet("list-list", {
                list: i,
                page: a.page,
                morePage: a.morePage,
                filterCity: a.filterCity,
                moreCityisOk: a.moreCityisOk,
                popular: a.popular,
                position: $(document).scrollTop()
            }),
            window.location.href = e
        }),
        $(".clear-city.clicks").length && $(".clear-city").unbind("click").on("click", function() {
            var t = $(this).attr("data-city-id");
            $(this).remove(),
            a.filterCity.push(t),
            a.morePage = 1,
            a.moreCity(function() {
                $(".c-truck-list.more-city").remove()
            })
        })
    },
    template: function(t) {
        return $sq.truckCmp(t, {
            info: !0,
            notips: !(t.groups.indexOf(2) !== -1),
            "class": "more-city"
        })
    },
    moreCity: function(t) {
       /* var a = this;
        a.ajaxIng = !1,
        $(".c-d-loading").removeClass("no-more"),
        $sq.ajax({
            url: apiUrl + "site/periphery-list",
            type: "GET",
            data: {
                page: a.morePage,
                city: $sq.getCookie("tao_privite_prov_no"),
                filterCity: a.filterCity.join(",")
            },
            success: function(e) {
                var i = ""
                  , n = a.loading;
                e = {
                    status: e.status,
                    data: e.data.data || [],
                    city: e.data.city || []
                },
                0 == e.status ? (!a.moreCityisOk && e.city.length && (i += '<div class="c-handle-tips-con c-more-city-handle-con" style="display: block; background-color: #f5f5f5;">\n\t\t\t\t\t\t<div style="color: #666; margin-bottom: .15rem; margin-top: .12rem;">周边城市车源</div>\n\t\t\t\t\t\t<div class="c-flex c-box-flex" style="overflow: hidden; overflow-x: auto; display: block;"><div style="width: 10000%;">',
                $.each(e.city, function(t, a) {
                    .26 * a.city.length;
                    i += '<div data-city-id="' + a.city_no + '" class="clear-city icon-handle-tips" style="display: flex; display: -ms-flexbox; white-space: nowrap; float: left;">\n\t\t\t\t\t\t\t\t<div>' + a.city + "</div>\n\t\t\t\t\t\t\t</div>"
                }),
                i += "</div></div></div>",
                a.moreCityisOk = !0),
                t && t(),
                e.data.length || $(".c-truck-list.more-city").length || $(".c-more-city-handle-con").remove(),
                e.data.length ? ($.each(e.data, function(t, e) {
                    i += a.template(e)
                }),
                n.before(i).find(".c-d-loading").addClass(e.data.length < a.max ? "no-more" : "active"),
                a.morePage++,
                n.find(".c-d-loading").hasClass("no-more") && setTimeout(function() {
                    a.ajaxIng = !0
                }, 100)) : (n.before(i).find(".c-d-loading").addClass("no-more"),
                setTimeout(function() {
                    a.ajaxIng = !0
                }, 100)),
                $(".clear-city").hasClass("clicks") || $(".clear-city").addClass("clicks").on("click", function() {
                    var t = $(this).attr("data-city-id");
                    $(this).remove(),
                    a.filterCity.push(t),
                    a.morePage = 1,
                    a.moreCity(function() {
                        $(".c-truck-list.more-city").remove()
                    })
                })) : $sq.handle(e.data),
                $sq.photoLoad()
            },
            always: function() {
                a.ajaxIng = !1
            }
        })*/
    }
};
$sq.timeout(list);
