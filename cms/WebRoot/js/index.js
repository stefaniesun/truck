"use strict";
function jump() {
    var t = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : ""
      , i = [];
    $.each(activeArr, function(t, e) {
        e !== initArr[t] && i.push(e)
    });
    var e = i.join("_")
      , n = e.indexOf("_p") !== -1 ? e.match(/[^p]*p/)[0] + (e.match(/[^p]*p(.*)/) || ["", ""])[1].replace(/p/g, "") : e;
    $sq.noHash(function() {
        var i = [];
        !!t && i.push(t),
        !!n && i.push(n);
        var e = i.join("/") ? "/" + i.join("/") + "/" : initUrl ? "/list/" : "/";
        window.dealerList && (e = window.location.href),
        window.location.href = e
    }, 0)
}
function setCookie(t, i, e) {
    e = 864e5,
    t || (e = 36e5),
    $sq.isApp() && $sq.loading("show");
    var n = new Date;
    n.setTime(n.getTime() + e),
    document.cookie = escape(cookieName) + "=" + escape(t) + ";expires=" + n.toGMTString() + ";path=/;domain=.360che.com",
    initUrl ? jump(i) : $sq.noHash(function() {
        window.location.reload()
    })
}
function unsearchProvince() {
    var t = 36e5
      , i = new Date;
    i.setTime(i.getTime() + t),
    document.cookie = escape(cookieName) + "=0;expires=" + i.toGMTString() + ";path=/;domain=.360che.com",
    jump()
}
function appendCity(t) {
    return '<div class="c-list-cell" onclick="setCookie(' + t.cityid + ", '" + t.citypinyin + "')\">" + t.city + "</div>"
}
var cities = window.cities ? window.cities : {
    indexNav: [],
    location: []
};
document.getElementById("indexTouch") && MyTouch("#indexTouch", {
    list: ".num",
    automatic: !0,
    speed: 5e3,
    loop: !0
}),
$('.c-switch-con[data-type="index"] .c-box-flex').each(function(t, i) {
    $(this).on("click", function() {
        $(this).addClass("active").siblings().removeClass("active"),
        $(".c-index-more-list").eq(t).show().siblings(".c-index-more-list").hide()
    })
}).eq(0).click();
var $seoListMore = $(".c-seo-list-more")
  , $seoSwitchContent = $(".c-seo-switch-content .c-box-flex")
  , $seoListMoreIndex = 0;
$seoSwitchContent.each(function(t, i) {
    $(this).on("click", function() {
        $seoListMoreIndex = t,
        $seoListMore.removeClass("active"),
        $(".c-seo-switch-list").removeClass("active"),
        $(this).addClass("active").siblings().removeClass("active");
        var i = $(".c-seo-switch-list-content .c-seo-switch-list").eq(t);
        i.show().siblings(".c-seo-switch-list").hide(),
        (0 === t || 1 === t) && i.find(".list").length > 6 || 2 === t && i.find(".list").length > 9 ? $seoListMore.show() : $seoListMore.hide()
    })
}).eq(0).click(),
$seoListMore.on("click", function() {
    $(this).hasClass("active") ? ($(this).removeClass("active"),
    $(".c-seo-switch-list").removeClass("active")) : ($(".c-seo-switch-list").eq($seoListMoreIndex).addClass("active"),
    $(this).addClass("active"))
});
var cookieName = "tao_privite_prov_no"
  , dataCity = $("div.c-right-city")
  , activeArr = []
  , initArr = ["c0", "b0", "p0", "p0", "p0", "o0"]
  , hotCityElement = ""
  , lettersElement = '<div class="list">热</div>';
$.each(cities.location[0] || [], function(t, i) {
    hotCityElement += '<div class="hot-city" onclick="setCookie(' + i.cityid + ", '" + i.citypinyin + "')\">" + i.city + "</div>"
}),
$(".hot-city-con").append(hotCityElement);
for (var nowCityId = $sq.getCookie("tao_privite_prov_no"), cityElement = "", i = 1; i < cities.location.length; i++)
    lettersElement += '<div class="list">' + cities.indexNav[i] + "</div>",
    cityElement += '<div class="c-small-title-con">' + cities.indexNav[i] + '</div>\n\t\t\t\t\t<div class="c-list-cells">',
    $.each(cities.location[i], function(t, i) {
        var e = i.cityid == nowCityId ? "icon-active-right" : "";
        cityElement += '<div class="c-list-cell ' + e + ' c-box-flex" onclick="setCookie(' + i.cityid + ", '" + i.citypinyin + "')\">" + i.city + "</div>"
    }),
    cityElement += "</div>";
$(".c-city-con").append(cityElement);
var $cityLettersList = dataCity.next(".c-letters-list");
$cityLettersList.append(lettersElement);
var $rightCityPage = dataCity.find(".c-page")
  , $smallTitle = $('div[data-hash="city"] .c-small-title-con')
  , $handle = $(".c-toast-con")
  , pageArr = []
  , rightHeight = dataCity.find(".c-header-right").height()
  , timer = null;
$cityLettersList.find(".list").each(function(t, i) {
    pageArr.push($smallTitle.eq(t).offset().top - rightHeight),
    $(this).on("click", function() {
        var i = pageArr[t];
        $handle.text($(this).text()).addClass("active"),
        clearTimeout(timer),
        timer = setTimeout(function() {
            $handle.removeClass("active")
        }, 2e3),
        $rightCityPage.scrollTop(i)
    })
});
var $lettersList = $(".c-letters-list")
  , listHeight = $lettersList.find(".list:eq(0)").height();
$lettersList.on("touchmove", function(t) {
    t.preventDefault();
    var i = $(this).get(0)
      , e = i.getBoundingClientRect()
      , n = t.originalEvent.changedTouches[0].pageY - e.top - $(document).scrollTop();
    n > 0 && n < e.height && $(this).find(".list").eq(Math.round(n / listHeight)).click()
});
var $cityForm = $('form[data-role="cityForm"]')
  , $citySearch = $(".city-search-con");
$citySearch.css({
    height: $("html").height() - rightHeight
}),
$('input[name="city"]').on("input", function() {
    var t = ""
      , i = $(this).val();
    if (i.match(/[a-z]/g) && (i = i.match(/[a-z]/g).join("")),
    /^[a-z]+$/.test(i)) {
        i = i.toLowerCase();
        var e = 999999
          , n = (i[0] || "").toUpperCase();
        $.each(cities.indexNav, function(t, i) {
            n == i && (e = t)
        });
        var a = cities.location[e] || [];
        $.each(a, function(e, n) {
            if (n.shortPinyin.indexOf(i) !== -1)
                t += appendCity(n);
            else {
                for (var a = 0, c = 0; c < n.citypinyin.length; c++)
                    n.citypinyin[c] == i[c] && a++;
                a === i.length && (t += appendCity(n))
            }
        })
    } else
        i = i || "全国",
        $.each(cities.location, function(e, n) {
            $.each(n, function(e, n) {
                n.city.indexOf(i) !== -1 && (t += appendCity(n))
            })
        });
    t ? $citySearch.show() : $citySearch.hide(),
    $citySearch.html(t)
});
var $searchForm = $('form[data-role="searchForm"]');
$searchForm.on("submit", function() {
    var t = $(this).find("input").val();
    $sq.noHash(function() {
        if (window.page && "dealer" === window.page) {
            var i = $sq.getQueryString("role");
            return window.location.href = "/dealer/list?role=" + i + "&order_by=like&name=" + encodeURI(t),
            !1
        }
        var e = window.keys ? window.keys.split("_") : [];
        e = e.filter(function(t) {
            return !/se(.+?)se/.test(t)
        }),
        e = e.filter(function(t) {
            return t
        }),
        e.push(encodeURIComponent("se" + t + "se")),
        window.provincePinyin ? window.location.href = "/" + window.provincePinyin + "/" + e.join("_") : window.location.href = "/" + e.join("_")
    })
}),
$(".icon-active-right[data-value][data-index], .more-handle-con .active").each(function() {
    var t = $(this).attr("data-index") - 1
      , i = $(this).attr("data-value");
    activeArr[t] = i
});
var touch = null;
$(document).on("touchstart", function(t) {
    touch = !0
}).on("touchmove", function(t) {
    touch = !1
}),
$("div[data-value][data-index]").on("touchend", function() {
    if (!touch)
        return !1;
    $sq.isApp() && $sq.loading("show");
    var t = $(this).attr("data-index") - 1
      , i = $(this).attr("data-value");
    activeArr[t] = i,
    jump(provincePinyin)
});
for (var dataBrand = $(".c-right-brand"), $brandLettersList = dataBrand.next(".c-letters-list"), $brandCityPage = dataBrand.find(".c-page"), $brandSmallTitle = $('div[data-hash="brand"] .c-page').children(".c-small-title-con"), brandPageArr = [], brandRightHeight = dataBrand.find(".c-header-right").height(), bramdLettersElement = "", _i2 = 0; _i2 < $brandSmallTitle.length; _i2++)
    bramdLettersElement += '<div class="list">' + $brandSmallTitle.eq(_i2).text()[0] + "</div>";
$brandLettersList.append(bramdLettersElement),
$brandLettersList.find(".list").each(function(t, i) {
    brandPageArr.push($brandSmallTitle.eq(t).offset().top - brandRightHeight),
    $(this).on("click", function() {
        var i = brandPageArr[t];
        $handle.text($(this).text()).addClass("active"),
        clearTimeout(timer),
        timer = setTimeout(function() {
            $handle.removeClass("active")
        }, 2e3),
        $brandCityPage.scrollTop(i)
    })
});
var hotBrandHeight = $(".hot-brand-con").outerHeight(!0);
$('[data-hash="brand"] .c-page').on("scroll", function() {
    var t = $(this).scrollTop();
    t > hotBrandHeight ? $brandLettersList.addClass("active") : $brandLettersList.removeClass("active")
}),
$('[data-role="cityForm"] .icon-right-close').on("click", function() {
    $(".city-search-con").hide()
}).css("padding-left", "20px");
var headHeight = $(".c-index-search-con").outerHeight(!0) + $(".c-index-search-con").next("a").outerHeight(!0)
  , $indexSticky = $(".c-sticky");
$indexSticky.length > 0 && $(window).on("scroll", function() {
    $indexSticky.offset().top > headHeight + 1 ? $indexSticky.addClass("br-b bgf") : $indexSticky.removeClass("br-b bgf")
});
