var MobileIndexPage={MobileIndexTop:function(t){str='<div class="swiper-wrapper">',t&&t.length&&0<t.length&&$.each(t,function(t,a){str+='<div class="swiper-slide">',-1==a.href.indexOf("mobile/weppersonalvipbuy")?str+='<a rel="external" href="'+a.href+'" class="mfarr">':str+='<a rel="external" href="'+a.href+'?type=mindexBanner" data-track="wap棣栭〉-banner" class="mfarr">',str+='<img src="'+a.url+'" />',str+="</a></div>"}),str+="</div>",$("#mfSwiper").html(str);new Swiper(".swiper-container",{pagination:".mfTurn",centeredSlides:!0,autoplayDisableOnInteraction:!1})},MobileIndxFloat:function(t){}};$(function(){if("xizang"==STATIONPINYIN)new Swiper(".swiper-container",{pagination:".mfTurn",centeredSlides:!0,autoplayDisableOnInteraction:!1});CommonFontSize(),$(".mfNav li:first a").on("click",function(){window._hmt&&window._hmt.push(["_trackEvent","wap鐢佃剳鐗�","click"])});var e=$(".mFourcar .mfTitle p").width(),i=$(".mFourcar .mfTitle p").eq(0).find("span").width(),n=(e-i)/2;$("#fCar").css({width:i,left:n});$(".mFourcar .mfTitle p").tabChange({addClass:"select",targetClass:".mfCarlist",tabCallback:function(t){var a=t;switch(i=$(".mFourcar .mfTitle p").eq(a).find("span").width(),n=(e-i)/2,n+=t*e,$("#fCar").css({width:i,left:n}),a){case 0:var o="/car/mcommendzq.htm";break;case 1:o="/"+STATIONPINYIN+"/soa1";break;case 2:o="/"+STATIONPINYIN+"/soa1d9";break;case 3:o="/"+STATIONPINYIN+"/soa6"}$("#mfMorecar").off().on("click",function(){$.attributeLink(o,"")})}}),$("#mfMorecar").off().on("click",function(){var t="/"+STATIONPINYIN+"/soa1";""!=SPURL&&(t="/"+STATIONPINYIN+SPURL),$.attributeLink(t,"")});$(".cbloBox a").blockFly({targetId:".coolstyle",flyType:3,ifShadow:!1,beginLocation:"100%",targetLocation:"0%",closebuttonId:"#cstArrow",flyCallback:function(){var a=$(this).attr("data-pid"),n=$(this).data("dirname");$.post("/tools/getCarSerialType.json",{pid:a},function(t){if(t){$("#csList").html("");var i='<a data-dirname="'+n+'" data-pid="'+a+'">涓嶉檺</a>';$.each(t.first,function(t,a){for(var o in a){i+="<span>"+o+"</span>";for(var e=0;e<a[o].length;e++)i+='<a data-sid="'+a[o][e].id+'"data-dirname="/'+n+"/"+a[o][e].pinyin+'">'+a[o][e].title+'<b class="wtselect"><i></i></b></a>'}}),$("#csList").append(i).find("a").off().on("click",o)}})}});var o=function(){if($(this).attr("data-sid"))$(this).find(".wtselect").hasClass("wtselectOn")?$(this).find(".wtselect").removeClass("wtselectOn"):"none"!=$(this).find(".wtselect i").css("display")&&$(this).find(".wtselect").addClass("wtselectOn"),4<$(this).parent().find(".wtselectOn").length?$(this).parent().find(".wtselect:not(.wtselectOn) i").hide():$(this).parent().find("i").show();else{var t=$(this).data("dirname"),a=$(this).data("pid"),o="/"+STATIONPINYIN+"/"+t+"/soa1t"+a;$.attributeLink(o,"")}};$("#cstAffirm").off().on("click",function(){var t="",a="",o="",e=$("#csList .wtselectOn").length,i=$("#csList .wtselectOn").parent();0<e?(1<e?(a=$(i[0]).data("dirname").split("/")[1],i.each(function(){t+="t"+$(this).attr("data-sid")})):(a=i.data("dirname").slice(1),t="t"+i.data("sid")),o="/"+STATIONPINYIN+"/"+a+"/soa1"+t,$.attributeLink(o,"")):$.alertMessage.call($(this),"璇烽€夋嫨杞︾郴")}),$("#mhtMore").blockFly({targetId:".coolbrandselect",flyType:2,ifShadow:!1,beginLocation:"100%",targetLocation:"0%",closebuttonId:"#coolArrow",closeCallback:a,nextCallback:s});var t=function(){$(".cbList").removeClass("cblBoxon"),$(this).addClass("cblBoxon")};function a(){$("body").removeClass("notmove"),$("body").css({overflow:"auto",height:"auto"})}function s(){$("body").addClass("notmove"),$("body").css({overflow:"hidden",height:$(window).height()})}if($(".cbList").each(function(){$(this).off().on("click",t)}),$("#mfGotop").hxGototop({ifSH:!0,showDis:300,showID:"#mfGotop"}),$("#mtMore").blockFly({targetId:".mMoretool",flyType:2,ifShadow:!1,beginLocation:"100%",targetLocation:"0%",closebuttonId:"#mmtBack",closeCallback:a,preventDiv:"body",nextCallback:s}),$("#changeStation,#moreCity").blockFly({targetId:".mzarea",flyType:2,ifShadow:!1,beginLocation:"100%",targetLocation:"0%",closebuttonId:"#areaArrow",closeCallback:a,flyCallback:s,nextCallback:null}),$(".mzaProvince .mbbaBrand").blockFly({targetId:".mzcity",flyType:2,ifShadow:!1,beginLocation:"100%",targetLocation:"0%",closebuttonId:"#cityArrow",nextCallback:function(){var t=$(this).children("span"),i=$.trim(t.html()),n=$.trim($(this).data("dirname"));$.post("/wap/getCityBySF.json",{areaCode:i},function(t){var a='<a class="mzcbaL" href="/'+n+'">涓嶉檺</p>',o=t.city;for(var e in o)a+='<a class="mzcbaL" href="/'+o[e].citypinyin+'">'+e+"</p>";$(".mzcity .mzaTop span").html(i),$("#mzcbAll").html(a)})}}),$("#mlogin").waplogin({loginState:loginstate,redirectUrl:"/wap/personcenter.htm"}),$(".mmhRule").waplogin({loginState:loginstate,redirectUrl:"/wzcx"}),$(".mmhAssess").waplogin({loginState:loginstate,redirectUrl:"/tools/carassess.htm"}),$(".mmhInsurance").waplogin({loginState:loginstate,redirectUrl:"/tools/safecount.htm"}),$(".mmhLoan").waplogin({loginState:loginstate,redirectUrl:"/wap/addwillloan.htm"}),$(".mmhData").waplogin({loginState:loginstate,redirectUrl:"/hxdata"}),$(".mpTablepf,.mmSellCar").waplogin({loginState:loginstate,redirectUrl:"/wap/index.htm"}),$("body").delegate(".msg-bottom .toDowload","click",function(){window._hmt&&window._hmt.push(["_trackEvent","wap棣栭〉app涓嬭浇","click"])}),$("[data-track]").on("click",function(){var t=$(this).data("track");window._hmt&&window._hmt.push(["_trackEvent",t,"click"])}),$("#mfDownload,.toDowload,.appDownloae").click(function(){$.hxWaptoapp()}),!loginstate){var l,r;$(".pfclick").click(function(){return l=$(this).find("a").attr("href"),!1}),$(".pfclick").waplogin({loginState:loginstate,checkedCallback:function(){var t="";-1!=l.indexOf("tradedetails")&&(t="wap棣栭〉-鎵瑰彂鎸夐挳-鐧诲綍"),window._hmt&&window._hmt.push(["_trackEvent",t,"click"]),window.location.href=l}}),$(".viplist a").click(function(){return-1!=(r=$(this).attr("href")).indexOf("from=groupmessage")}),$(".viplist a").waplogin({loginState:loginstate,beforeCallback:function(){return-1==r.indexOf("from=groupmessage")},checkedCallback:function(){var t="";-1!=r.indexOf("entry=pfshouye")?t="wap棣栭〉-鎵瑰彂杞︾櫥褰�":-1!=r.indexOf("newReleaseCarList")?t="wap棣栭〉-鏂板彂甯冪櫥褰�":-1!=r.indexOf("qualitycarlist")&&(t="wap棣栭〉-浣庝环杞︾櫥褰�"),window._hmt&&window._hmt.push(["_trackEvent",t,"click"]),window.location.href=r}})}function d(t){for(var a=0,o=0;o<t.length;o++){null!=t.charAt(o).match(/[^\x00-\xff]/gi)?a+=2:a+=1}return a}function c(t){$(".magic_box").attr("class","magic_box "+t)}$(".showfollow").waplogin({loginState:loginstate,redirectUrl:"/wap/ShowRE.htm"}),$(".mtpinggu").waplogin({loginState:loginstate,redirectUrl:"/tools/carassess.htm"}),$(".mtweizhang").waplogin({loginState:loginstate,redirectUrl:"/wzcx"}),$(".mtbaoxian").waplogin({loginState:loginstate,redirectUrl:"/tools/safecount.htm"}),function(t,a){for(var a=a||1,o=t,e=0;e<o.length;e++){var i=$(o[e]),n=d(i.text()),s=i.text().length,l=i.width(),r=i.css("font-size");r=r.substr(0,2);var c=Math.floor(l/r*s/n);if(1.5*c*a-1<n){var h=i.text().substr(0,Math.floor(1.5*c*a-1));i.text(h+"...")}}}($(".gass_title_txt"),2.1),c("show_star"),$(document).scroll(function(){var t=$("#mHot").offset().top,a=$(".mFourcar").offset().top,o=$(window).height(),e=$(this).scrollTop(),i=e+o-40;t<i&&i<a&&!$(".mag_right").hasClass("not")&&!$(".mag_right").hasClass("snot")&&(c("show_right"),window._hmt&&window._hmt.push(["_trackEvent","wap棣栭〉鎺ㄨ崘妯″潡灞曞紑","click"])),a<i?$(".magic_box").hasClass("show_right")&&!$(".mag_right").hasClass("snot")?c("show_logo"):$(".magic_box").hasClass("show_logo")||c("show_top"):$(".magic_box").hasClass("show_right")||$(".magic_box").hasClass("show_logo")||c("show_star"),i<t&&(!$(".magic_box").hasClass("show_right")&&!$(".magic_box").hasClass("show_logo")||$(".mag_right").hasClass("snot")||c("show_logo"))}),$(".mag_new")[0].addEventListener(function(){var t,a=document.createElement("fakeelement"),o={transition:"transitionend",OTransition:"oTransitionEnd",MozTransition:"transitionend",WebkitTransition:"webkitTransitionEnd",MsTransition:"msTransitionEnd"};for(t in o)if(void 0!==a.style[t])return o[t]}(),function(){$(".magic_box").removeClass("show_logo"),$(document).scrollTop($(document).scrollTop()+1)}),$(".mag_top").click(function(){$(".mag_right").addClass("snot"),$("html,body").animate({scrollTop:0},200),setTimeout(function(){$(".mag_right").removeClass("snot")},200)}),$(".mag_star,.mag_new").click(function(){$(".mag_right").addClass("snot");var t=$(".mFourcar").offset().top-$("#mlTop").height();$(".mFourcar .mfTitle p").eq(1).click(),$("html,body").animate({scrollTop:t},200),setTimeout(function(){$(".mag_right").removeClass("snot")},200)}),$(".mag_right").click(function(){c("show_logo"),$(".mag_right").addClass("not")}),window.frames.length!=parent.frames.length||(location.href="#mlTop"),$("body").delegate(".mstBack","click",function(){$(".mindex").removeClass("zIndex4"),$("body").hasClass("showbot")||$(".msg-bottom").hide()}),$(".msg-bottom .closes").click(function(){$("body").removeClass("showbot")}),$("#mltSearch").off().click(function(){location.href="/help/hxss.htm"}),$(document).scroll(function(){var t=$(this).scrollTop(),a=$(".mindex").offset().top-$("#mlTop").height()+5;if(a<t){$("body").addClass("nmlTop");var o=$("#mltSearch").attr("data-hot");$("#mltSearch span").text(o),$("#mstiInput").val(o)}else $("body").removeClass("nmlTop"),$("#mltSearch span").text("鎴戣鎵捐溅")}),$.post("/car/historyKeyAndHotKey.json",{keytype:"0"},function(t){t&&(0<t.historyCookieKeys.length?$("#mltSearch").attr("data-hot",t.historyCookieKeys[0]):$.post("/search/gethotcar.json",{},function(t){t&&$("#mltSearch").attr("data-hot",t.hotlist[0].title)}))}),document.addEventListener("touchmove",function(t){$("body").hasClass("notmove")&&t.preventDefault()},!1)});