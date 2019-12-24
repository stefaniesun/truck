(function() {
    window.zhuge = window.zhuge || [];
    window.zhuge.methods = "_init identify track getDid getSid getKey setSuperProperty setUserProperties setWxProperties setPlatform".split(" ");
    window.zhuge.factory = function(c) {
        return function() {
            var d = Array.prototype.slice.call(arguments);
            d.unshift(c);
            window.zhuge.push(d);
            return window.zhuge
        }
    }
    ;
    for (var b = 0; b < window.zhuge.methods.length; b++) {
        var a = window.zhuge.methods[b];
        window.zhuge[a] = window.zhuge.factory(a)
    }
    window.zhuge.load = function(e, d) {
        if (!document.getElementById("zhuge-js")) {
            var f = document.createElement("script");
            var g = new Date();
            var i = g.getFullYear().toString() + g.getMonth().toString() + g.getDate().toString();
            f.type = "text/javascript";
            f.id = "zhuge-js";
            f.async = !0;
            f.src = (location.protocol == "http:" ? "http://sdk.zhugeio.com/zhuge.min.js?v=" : "https://zgsdk.zhugeio.com/zhuge.min.js?v=") + i;
            f.onerror = function() {
                window.zhuge.identify = window.zhuge.track = function(c, j, k) {
                    if (k && Object.prototype.toString.call(k) === "[object Function]") {
                        k()
                    } else {
                        if (Object.prototype.toString.call(j) === "[object Function]") {
                            j()
                        }
                    }
                }
            }
            ;
            var h = document.getElementsByTagName("script")[0];
            h.parentNode.insertBefore(f, h);
            window.zhuge._init(e, d)
        }
    }
    ;
    window.zhuge.load("a195c2b5419743c98f258995761166e4", {
        superProperty: {
            "应用名称": "诸葛io"
        },
        adTrack: false,
        autoTrack: false,
        singlePage: false
    });
    zhuge.track("Visited URLs", {
        "Current URL": window.location.href,
        "Refer URL": document.referrer ? document.referrer : "wulaiyuan"
    })
}
)();
