"use strict";
function _classCallCheck(t, e) {
    if (!(t instanceof e))
        throw new TypeError("Cannot call a class as a function")
}
function isStorageSupported() {
    var t = "test"
      , e = window.localStorage;
    try {
        return e.setItem(t, "testValue"),
        e.removeItem(t),
        window.isStorageSupportedFlag = !0,
        !0
    } catch (n) {
        return window.isStorageSupportedFlag = !1,
        !1
    }
}
var _typeof = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function(t) {
    return typeof t
}
: function(t) {
    return t && "function" == typeof Symbol && t.constructor === Symbol && t !== Symbol.prototype ? "symbol" : typeof t
}
  , _createClass = function() {
    function t(t, e) {
        for (var n = 0; n < e.length; n++) {
            var i = e[n];
            i.enumerable = i.enumerable || !1,
            i.configurable = !0,
            "value"in i && (i.writable = !0),
            Object.defineProperty(t, i.key, i)
        }
    }
    return function(e, n, i) {
        return n && t(e.prototype, n),
        i && t(e, i),
        e
    }
}();
!function t(e) {
    var n = document.documentElement
      , i = n.clientWidth;
    n.setAttribute("style", "font-size: " + i / e + "px !important"),
    window.onresize = function() {
        t(e)
    }
}(7.5);
var DeleteScroll = function() {
    function t(e) {
        var n = this;
        _classCallCheck(this, t),
        this.scroll = e,
        this.endX = this.startX = this.endY = this.startY = this.scrollY = null,
        this.init(),
        document.addEventListener("click", function() {
            n.scroll.forEach(function(t, e, n) {
                t.classList.contains("active") && t.classList.remove("active")
            })
        })
    }
    return _createClass(t, [{
        key: "init",
        value: function() {
            var t = this;
            this.scroll.forEach(function(e, n, i) {
                e.$this = t,
                e.classList.remove("active"),
                e.removeEventListener("touchstart", t.start),
                e.removeEventListener("touchmove", t.move),
                e.addEventListener("touchstart", t.start),
                e.addEventListener("touchmove", t.move)
            })
        }
    }, {
        key: "start",
        value: function(t) {
            var e = this.$this;
            e.endX = e.startX = t.targetTouches[0].pageX,
            e.endY = e.startY = t.targetTouches[0].pageY,
            e.scrollY = void 0,
            e.scroll.forEach(function(t, e, n) {
                t.classList.remove("active")
            })
        }
    }, {
        key: "move",
        value: function(t) {
            var e = this.$this;
            e.endX = t.targetTouches[0].pageX,
            e.endY = t.targetTouches[0].pageY,
            "undefined" == typeof e.scrollY && (e.scrollY = !!(e.scrollY || Math.abs(e.endX - e.startX) < Math.abs(e.endY - e.startY))),
            e.scrollY || (t.preventDefault(),
            e.startX - e.endX > 30 && this.classList.add("active"),
            e.endX - e.startX > 30 && this.classList.remove("active"))
        }
    }]),
    t
}();
!function() {
    var t = isStorageSupported()
      , e = window.sessionStorage
      , n = window.apiUrl
      , i = window.accessToken
      , s = {
        browser: {
            versions: function() {
                var t = window.navigator.userAgent;
                return {
                    trident: t.indexOf("Trident") > -1,
                    presto: t.indexOf("Presto") > -1,
                    webKit: t.indexOf("AppleWebKit") > -1,
                    gecko: t.indexOf("Gecko") > -1 && t.indexOf("KHTML") == -1,
                    mobile: !!t.match(/AppleWebKit.*Mobile.*/),
                    ios: !!t.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/),
                    android: t.indexOf("Android") > -1 || t.indexOf("Linux") > -1,
                    iPhone: t.indexOf("iPhone") > -1 || t.indexOf("Mac") > -1,
                    iPad: t.indexOf("iPad") > -1,
                    webApp: t.indexOf("Safari") == -1,
                    weixin: !("micromessenger" != t.toLowerCase().match(/MicroMessenger/i))
                }
            }()
        },
        isFn: function(t) {
            return "function" == typeof t
        },
        storageSet: function(n, i) {
            return t && e.setItem(n, "string" != typeof i ? JSON.stringify(i) : i)
        },
        storageGet: function(n) {
            var i = t && e.getItem(n);
            return !!i && i
        },
        storageRemove: function(n) {
            return t && e.removeItem(n)
        },
        deleteScroll: function(t) {
            return new DeleteScroll(t)
        },
        bigSlider: function(t, e) {
            $(t).on("click", function(t) {
                if ("IMG" == t.target.nodeName) {
                    var n = $(t.target).attr("data-img-arr")
                      , i = 0
                      , s = '<ul class="list">';
                    $('img[data-img-arr="' + n + '"]').each(function(e, n) {
                        var a = $(this).attr("src");
                        $(t.target).get(0) == $(this).get(0) && (i = e),
                        s += '<li><img src="' + a + '"/></li>'
                    }),
                    s += '</ul><ul class="num"></ul>',
                    $("body").append('<div class="big-slider" id="bigSlider"></div>');
                    var a = $("#bigSlider");
                    a.on("click", function() {
                        a.remove()
                    }).append(s),
                    e && e();
                    var c = MyTouch("#bigSlider", {
                        list: ".num",
                        automatic: !1,
                        loop: !1
                    });
                    c.fnNum(),
                    c.index = i,
                    c.oDiv.style.transitionDuration = "0ms",
                    c.fnActive(1),
                    a.addClass("active")
                }
            })
        },
        trimText: function(t) {
            return "string" != typeof t && t instanceof Object && (t instanceof Array ? (t = t[0],
            t instanceof Object && $.each(t, function(e, n) {
                t = n
            })) : $.each(t, function(e, n) {
                t = n
            })),
            t
        },
        handleTimer: null,
        handle: function a(t, e) {
            t = $.trim(s.trimText(t));
            var n = "c-handle-con"
              , a = $("." + n);
            a.length <= 0 && ($("body").append('<div class="' + n + '"></div>'),
            a = $("." + n)),
            a.text(t),
            a.addClass("active"),
            clearTimeout(s.handleTimer),
            s.handleTimer = setTimeout(function() {
                a.removeClass("active")
            }, e || 1500)
        },
        timeout: function(t) {
            var e = this;
            this.timer = null,
            this.set = function() {
                e.timer = setTimeout(function() {
                    s.isFn(t.callback) && t.callback()
                }, t.time || 10)
            }
            ,
            this.clear = function() {
                clearTimeout(e.timer)
            }
            ,
            t.init(function() {
                e.clear(),
                e.set()
            })
        },
        initPadding: function() {
            var t = $('body > [class*="c-header-"]').outerHeight(!0) - 1 || 0
              , e = $('[class*="c-footer-"]').outerHeight(!0) - 1 || 0;
            $("body").css({
                "padding-top": t,
                "padding-bottom": e
            })
        },
        loading: function(t) {
            var e = arguments.length > 1 && void 0 !== arguments[1] ? arguments[1] : "加载中"
              , n = '<div class="c-fixed-load append-loading">\n\t\t\t\t\t\t\t\t<div class="c-loading-content">\n\t\t\t\t\t\t\t\t\t<div class="c-box-loading"></div>\n\t\t\t\t\t\t\t\t\t<div class="text">' + e + "</div>\n\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t</div>"
              , i = $(".append-loading");
            i.length <= 0 ? ($("body").append(n),
            i = $(".append-loading")) : i.find(".text").text(e),
            "show" == t ? i.addClass("active") : i.removeClass("active")
        },
        ajax: function(t) {
            $.ajax({
                url: t.url,
                type: t.type,
                dataType: t.dataType || "json",
                data: t.data
            }).done(function(e) {
                s.isFn(t.success) && t.success(e)
            }).fail(function(e) {
                s.isFn(t.error) && t.error(e),
                s.handle("数据请求出错, 请检查网络是否正常")
            }).always(function() {
                s.loading("hide"),
                s.isFn(t.always) && t.always()
            })
        },
        scrollBottom: function() {
            var t = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : 0
              , e = $("body").outerHeight(!0) + 2 * t;
            return e - $(document).scrollTop() - 100 <= $(window).height()
        },
        formData: function(t, e) {
            var n = $(t).serializeArray()
              , i = {}
              , a = !0;
            return $.each(n, function(t, n) {
                var c = $('[name="' + this.name + '"]').attr("data-error");
                if ("" == $.trim(this.value) && c)
                    return s.isFn(e) ? e() : s.handle(c),
                    a = !1;
                var o = $('[name="' + this.name + '"]').attr("data-number");
                return "" != $.trim(this.value) && !/^\d+(\.\d+)?$/.test($.trim(this.value)) && o ? (s.isFn(e) ? e() : s.handle(o + "必须为数字，且不能为负数"),
                a = !1) : void (i[this.name] = this.value)
            }),
            !!a && i
        },
        sendCode: function(t) {
            $(t.btn).on("click", function() {
                var e = t.time || 60
                  , n = null
                  , i = $(this)
                  , a = $(t.mobile);
                return "" == $.trim(a.val()) ? (s.handle("手机号码不能为空"),
                !1) : !i.hasClass("active") && (i.addClass("active"),
                void s.ajax({
                    url: s.api.captcha,
                    type: "POST",
                    data: {
                        mobile: a.val()
                    },
                    success: function(t) {
                        0 == t.status ? (s.api.captcha.indexOf("z.taoshop") === -1 && s.api.captcha.indexOf("test") === -1 && s.api.captcha.indexOf("yufabu") === -1 || (s.handle("为您献上验证码，辛苦了", 1e3),
                        $('input[name="captcha"]').val(t.data)),
                        i.text(e + "秒后可重新获取"),
                        n = setInterval(function() {
                            e > 1 ? (e--,
                            i.text(e + "秒后可重新获取")) : (clearInterval(n),
                            i.removeClass("active").text("获取验证码"))
                        }, 1e3)) : (s.handle(t.data),
                        i.removeClass("active").text("获取验证码"))
                    },
                    error: function() {
                        s.handle("短信发生失败"),
                        i.removeClass("active").text("获取验证码")
                    }
                }))
            })
        },
        conFirm: function(t) {
            function e() {
                r.classList.add("active"),
                o.classList.add("active")
            }
            function n() {
                r.classList.remove("active"),
                o.classList.remove("active"),
                t.obj || setTimeout(function() {
                    r.remove(),
                    o.remove()
                }, 300)
            }
            var i = arguments.length > 1 && void 0 !== arguments[1] ? arguments[1] : "取消"
              , a = arguments.length > 2 && void 0 !== arguments[2] ? arguments[2] : "确定"
              , c = '<div class="content br-b">\n\t\t\t\t\t\t\t\t<p class="big">' + t.text + '</p></p>\n\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t<div class="btn-con c-flex">\n\t\t\t\t\t\t\t\t' + ("no" !== t.left ? '<div class="c-box-flex no c-confirm-close br-r">' + i + "</div>" : "") + "\n\t\t\t\t\t\t\t\t" + ("no" !== t.right ? '<div class="c-box-flex yes c-confirm-close">' + a + "</div>" : "") + "\n\t\t\t\t\t\t\t</div>"
              , o = document.createElement("div");
            o.className = "c-confirm-con c-center-con",
            o.innerHTML = c;
            var r = document.createElement("div");
            r.className = "c-fixed-confirm c-confirm-close",
            t.obj ? $(t.obj).on("click", e) : e(),
            $("body").append(r).append(o),
            s.isFn(t.callback) && $(".btn-con .yes").off("click", t.callback).on("click", t.callback),
            $(".c-confirm-close").on("click", n)
        },
        getQueryString: function(t) {
            var e = new RegExp("(^|&)" + t + "=([^&]*)(&|$)")
              , n = window.location.search.substr(1).match(e)
              , i = null != n ? decodeURI(n[2]) : null;
            return i
        },
        getCookie: function(t) {
            var e = void 0
              , n = new RegExp("(^| )" + t + "=([^;]*)(;|$)");
            return (e = document.cookie.match(n)) ? decodeURI(e[2]) : null
        },
        deepCopy: function(t) {
            if ("object" != ("undefined" == typeof t ? "undefined" : _typeof(t)))
                return t;
            var e = {};
            for (var n in t)
                e[n] = s.deepCopy(t[n]);
            return e
        },
        api: {
            weexMember: n + "weex/member?access-token=" + i,
            edit: {
                sellerProfile: n + "seller/profile?access-token=" + i,
                seller: n + "seller?access-token=" + i,
                intermediary: n + "intermediary?access-token=" + i,
                sellerUpgrade: n + "seller/upgrade?access-token=" + i,
                storeAuth: n.replace("v1", "rn") + "member/store-auth?access-token=" + i
            },
            engineBrand: n + "product/engine-brand",
            truckUrl: n + "forward?api=https%3a%2f%2fproduct.360che.com%2findex.php&r=api/",
            truckPublish: n + "truck?access-token=" + i,
            userCenter: {
                messageDelete: n + "member/delnotif?access-token=" + i,
                messageDeleteAll: n + "member/delnotifs?access-token=" + i,
                message: n + "member/notification?access-token=" + i,
                favoriteDelete: n + "favorite?access-token=" + i,
                favoriteDeleteAll: n + "favorite?access-token=" + i,
                favorite: n + "favorite?access-token=" + i,
                historyDelete: n + "member/historyd?access-token=" + i,
                historyDeleteAll: n + "member/historyds?access-token=" + i,
                history: n + "member/history?access-token=" + i
            },
            sellerSellers: n + "seller/sellers?access-token=" + i,
            memberReduceNotification: n + "member/reduce-notification?access-token=" + i,
            basePrice: n + "truck/base-price?access-token=" + i,
            floorPrice: n.replace("v1", "st") + "dealers/floor-price?access-token=" + i,
            favorite: n + "favorite?access-token=" + i,
            compared: n + "compared?access-token=" + i,
            annualFee: n + "annual-fee?access-token=" + i,
            shop: {
                editShop: n + "shop?access-token=" + i,
                sellerTruck: n + "seller-truck/seller-truck",
                dealersOnsale: n.replace("v1", "st") + "dealers/onsale",
                tradeCase: n + "trade-case?access-token=" + i
            },
            upload: {
                upload: n + "upload?access-token=" + i,
                "private": n + "upload/private?access-token=" + i
            },
            truck: {
                myTruck: n + "truck/my-truck?access-token=" + i,
                "delete": n + "truck/index?access-token=" + i,
                onSale: n + "truck/on-sale?access-token=" + i,
                inSale: n + "truck/on-sale?access-token=" + i,
                refresh: n + "truck/refresh?access-token=" + i,
                onShelves: n + "truck/on-shelves?access-token=" + i,
                offShelves: n + "truck/off-shelves?access-token=" + i
            },
            permission: n + "truck/publish-permission?access-token=" + i,
            captcha: n + "sms/captcha?access-token=" + i,
            demands: n + "assure/demands?access-token=" + i,
            handleDemands: n + "assure/demand",
            demandCreate: n + "demand/create",
            responseSellers: n + "assure/response-sellers?access-token=" + i,
            rsubscribeSellers: n + "assure/subscribe-sellers?access-token=" + i,
            response: n + "assure/response?access-token=" + i,
            assureList: n + "assure/assure-list?access-token=" + i,
            demandSellers: n + "seller/demand-sellers?access-token=" + i,
            assureRefund: n + "assure/refund?access-token=" + i,
            assureAskForSale: n + "assure/ask-for-sale?access-token=" + i
        },
        truckCmp: function(t, e) {
            var n = e.nohref ? "div" : "a";
            return "<" + n + ' class="c-list-cell c-flex c-truck-list ' + (e && e["class"]) + '" href="/m' + t.id + '_index.html">\n\t\t\t\t\t\t<div class="photo">\n\t\t\t\t\t\t\t<img class="truck-photo-load" src="/m/project/dist/images/error_img.jpg" data-src="' + t.cover + '" alt="' + t.mainTitle + '图片">\n\t\t\t\t\t\t</div>\n\t\t\t\t\t\t<div class="c-box-flex content">\n\t\t\t\t\t\t\t<div class="title">\n\t\t\t\t\t\t\t\t' + (e.hasTip ? "<i>精</i>" : "") + '\n\t\t\t\t\t\t\t\t<div class="text">' + t.mainTitle + "</div>\n\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t" + (e.noinfo ? "" : '<div class="info">' + (e.create_at ? "发布于" + t.create_at : "" + t.secondTitle) + "</div>") + "\n\t\t\t\t\t\t\t" + (e.other ? '<div class="info">发布时间 ' + t.create_at + "</div>" : "") + "\n\t\t\t\t\t\t\t" + (e.other ? '<div class="info">售卖时间 ' + t.closing_time + "</div>" : "") + "\n\t\t\t\t\t\t\t" + (e.noprice ? "" : '<div class="c-align-center">\n\t\t\t\t\t\t\t\t\t\t\t\t<div class="price">' + t.price + "万</div>\n\t\t\t\t\t\t\t\t\t\t\t\t" + (e["delete"] ? '<div class="c-box-flex c-flex">\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class="c-box-flex"></div>\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class="delete icon-delete" data-id="' + t.id + '"></div>\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>' : "") + "\n\t\t\t\t\t\t\t\t\t\t\t</div>") + "\n\t\t\t\t\t\t</div>\n\t\t\t\t\t</" + n + ">"
        },
        noTruck: function() {
            var t = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : "没有任何车辆信息";
            return '<div class="no-truck-con c-flex-center icon-wushuju" data-text="' + t + '"></div>'
        },
        isApp: function() {
            var t = navigator.userAgent
              , e = t.indexOf("360CHE") > -1;
            return e
        },
        shareTemp: function() {
            return '<div class="c-fixed-share"></div>\n\t\t\t\t\t<div class="c-share-container c-share-icon claused bdsharebuttonbox">\n\t\t\t\t\t\t<div class="title">分享至</div>\n\t\t\t\t\t\t<div class="c-c-page">\n\t\t\t\t\t\t\t<div class="c-share-con c-align-center">\n\t\t\t\t\t\t\t\t<div class="c-share-box c-box-flex c-justify-center">\n\t\t\t\t\t\t\t\t\t<a class="bds_tsina baidu-share" data-cmd="tsina" title="分享到新浪微博"></a>\n\t\t\t\t\t\t\t\t\t<div class="icon-con c1">\n\t\t\t\t\t\t\t\t\t\t<span>\n\t\t\t\t\t\t\t\t\t\t\t<i class="iconfont icon-xinlangweibo"></i>\n\t\t\t\t\t\t\t\t\t\t</span>\n\t\t\t\t\t\t\t\t\t\t<p>新浪微博</p>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t<div class="c-share-box c-box-flex c-justify-center">\n\t\t\t\t\t\t\t\t\t<a class="bds_qzone baidu-share" data-cmd="qzone" title="分享到QQ空间"></a>\n\t\t\t\t\t\t\t\t\t<div class="icon-con c2">\n\t\t\t\t\t\t\t\t\t\t<span>\n\t\t\t\t\t\t\t\t\t\t\t<i class="iconfont icon-iconfontkongjian"></i>\n\t\t\t\t\t\t\t\t\t\t</span>\n\t\t\t\t\t\t\t\t\t\t<p>QQ空间</p>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t<div class="c-box-flex c-justify-center more-icon-btn" title="更多">\n\t\t\t\t\t\t\t\t\t<div class="icon-con c6">\n\t\t\t\t\t\t\t\t\t\t<span>\n\t\t\t\t\t\t\t\t\t\t\t<div class="more-con">\n\t\t\t\t\t\t\t\t\t\t\t\t<i class="more-icon"></i>\n\t\t\t\t\t\t\t\t\t\t\t\t<i class="more-icon"></i>\n\t\t\t\t\t\t\t\t\t\t\t\t<i class="more-icon"></i>\n\t\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t</span>\n\t\t\t\t\t\t\t\t\t\t<p>更多</p>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t</div>\n\t\t\t\t\t\t<div class="closed" id="closeDlause">取消分享</div>\n\t\t\t\t\t</div>\n\t\t\t\t\t<div id="wxShare"><img src="https://tao.m.360che.com/m/images/wechat-tips.png" alt="微信分享" /></div>'
        },
        noHash: function(t) {
            var e = arguments.length > 1 && void 0 !== arguments[1] ? arguments[1] : 100;
            s.isApp() && s.storageSet("no-hash", "true"),
            setTimeout(function() {
                history.go(-1)
            }, e),
            setTimeout(function() {
                t && t()
            }, 200)
        },
        photoLoad: function() {
            $(".truck-photo-load").each(function(t, e) {
                var n = $(this)
                  , i = new Image
                  , s = n.attr("data-src");
                i.src = s,
                i.onload = function() {
                    n.attr("src", s).removeClass("truck-photo-load")
                }
            })
        },
        downLoadApp: function(t) {
            return '<div class="truckhomeapp-box" id="thapp" style="' + t + '">\n\t\t\t\t\t\t<i id="thappclose"></i>\n\t\t\t\t\t\t<a href="#tao360che://tao.360che.com/app?open=home" id="thappdown" class="truckhomeapp-download">\n\t\t\t\t\t\t\t<img src="https://s.kcimg.cn/ershouche/rn/image/300.jpg?new">\n\t\t\t\t\t\t\t<div class="content">\n\t\t\t\t\t\t\t\t<div class="title">卡家好车APP</div>\n\t\t\t\t\t\t\t\t<div class="text">卡车之家旗下服务平台</div>\n\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t<div class="open">立即打开</div>\n\t\t\t\t\t\t</a>\n\t\t\t\t\t</div>'
        }
    };
    window.$sq = s
}(),
$(document).ready(function() {
    function t(t) {
        var e = location.hash.replace("#", "");
        $sq.storageGet("no-hash") ? $sq.storageRemove("no-hash") : $("div[data-hash], .c-fixed[data-hash]").removeClass("active"),
        $("input").blur(),
        e && $('div[data-hash="' + e + '"], .c-fixed[data-hash]').addClass("active")
    }
    function e(t, e, n) {
        window.WebViewJavascriptBridge.callHandler(t, e, n)
    }
    function n(t) {
        window.WebViewJavascriptBridge ? t(WebViewJavascriptBridge) : document.addEventListener("WebViewJavascriptBridgeReady", function() {
            t(WebViewJavascriptBridge)
        }, !1)
    }
    $sq.isApp() ? ($("div[header]").height(0).hide(),
    $sq.initPadding()) : ($("div[header]").css("opacity", 1),
    $sq.initPadding()),
    $('[class*="c-header-"] .c-return').on("click", function() {
        $(this).attr("data-href") ? location.href = $(this).attr("data-href") : history.go(-1)
    });
    var i = $(".c-sticky");
    i.css("top", parseInt($(".c-header-con").outerHeight(!0)) - 1 || 0),
    $sq.photoLoad(),
    $("img").on("click", function(t) {
        t.preventDefault()
    }),
    $("a img").unbind("click");
    var s = $("html");
    $(document).on("touchmove", function(t) {
        s.hasClass("no-scroll") && t.preventDefault()
    });
    var a = $("form")
      , c = null;
    a.find('input[type="text"], input[type="tel"], input[type="number"], input[type="password"], input[type="email"], input[type="search"]').on("focus", function() {
        clearTimeout(c),
        s.addClass("no-scroll")
    }).on("blur", function() {
        c = setTimeout(function() {
            s.removeClass("no-scroll")
        }, 100)
    }).on("input", function() {
        var t = $(this).parents("form").find(".icon-right-close");
        "" == $(this).val() ? t.hide() : t.show()
    }),
    a.on("submit", function(t) {
        t.preventDefault()
    }),
    a.find(".icon-right-close").on("click", function() {
        $(this).hide().parents("form").find("input").val("").focus()
    });
    var o = $(".c-go-top");
    o.on("click", function() {
        $("html, body").scrollTop(0)
    }),
    $(document).on("scroll", function() {
        $(document).scrollTop() > 200 ? o.addClass("active") : o.removeClass("active")
    }),
    $(window).on("hashchange", t),
    t(),
    $(".c-fixed[data-hash]").on("click", function() {
        history.go(-1)
    }),
    $sq.isApp() && n(function(t) {
        e("onChangeWebTitle", {
            changeWebTitle: document.title.length > 10 ? document.title.substring(0, 10) + "..." : document.title
        })
    })
});
