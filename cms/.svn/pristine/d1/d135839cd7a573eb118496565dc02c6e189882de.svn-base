/***
 * 移动端图片上传组件
 * （强烈建议配合weui.js使用）
 * xyzUploader.js
 * v 0.0.9
 * 20170106
 * 使用说明：http://uploadtoken.maytek.cn/demoAPI.html
 */
(function () {
    /**上传控件全局对象**/
    var xyzUploader = {};
    xyzUploader.version = 'v0.0.9';
    xyzUploader.versionTime = '20170106';
    var _fn = {};//保存回调函数对象
    var _hasWeui = window.weui ? true : false;//是否存在weui引用
    var _conf = {
        url: 'http://up.qiniu.com',//文件上传服务地址
        accept: ['image/png', 'image/jpg', 'image/jpeg'],//允许接受的文件类型
        size: 5 * 1000 * 1000,//单文件尺寸，单位B
        count: 1000,//本控件最多允许上传的文件个数
        params: {},//ajax附加参数
        timeout: 60 * 1000,//单文件上传超时设置
        validFail: function (result) {//文件限制验证失败
            if (result && result.msg) {
                if (_hasWeui) {
                    weui.toast(result.msg);
                }
            }
        },
        progress: function (result, event) {//文件上传进度
            if (result && result.progress) {
                if (_hasWeui) {
                    weui.loading(-1, '已上传' + result.progress + '%');
                }
            }
        },
        success: function (result, config) {//上传成功
        },
        removeImage: function (uploaderId) {//删除图片事件通知
        },
        error: function (statusText, result, config) {//上传ajax出错
            if (_hasWeui) {
                weui.toast(statusText);
            }
        },
        tokenUrl: 'http://uploadtoken.maytek.cn/qt2',
        bucket: '',
        token: '',
    };
    var errCodeMsg = {
    		'code400' : '报文构造不正确或者没有完整发送。',
    		'code401' : '上传凭证无效。',
    		'code403' : '上传文件格式不正确。',
    		'code413' : '上传文件过大。',
    		'code579' : '回调业务服务器失败。',
    		'code599' : '服务端操作失败。',
    		'code614' : '目标资源已存在。'
    };

    //全局配置函数
    xyzUploader.config = function (config) {
        for (var key in _conf) {
            _conf[key] = (typeof config[key]) === (typeof _conf[key]) ? config[key] : _conf[key];
        }
    };

    /**
     *
     * @param el div载体元素
     * @param config 配置项
     * @param initCallback 初始化成功后回调 initCallback(uploader-id)
     */
    xyzUploader.init = function (el, config, initCallback) {
        //参数验证
        el = validEl(el);
        var elConfig = el.getAttribute('data-uploader-config');
        if (!config) {
            config = {};
        }
        if (JSON.stringify(config) == '{}' && elConfig) {
            config = JSON.parse(elConfig);
        }
        //强制限定qiniu上传
        config.url = 'http://up.qiniu.com';

        //注册上传队列管理id
        var id = el.getAttribute('data-uploader-id');
        if (!id || !(id in _fn)) {//验证是否重复初始化同一个div载体
            id = getQueueId();
        }
        //准备配置信息
        var readyConfig = getConfig(id, config);
        //注册回调函数
        registerCallback(id, config);
        //构建html
        el.innerHTML = '';
        el.className = el.className.indexOf('xyzDropzone-uploader-main') < 0 ? ('xyzDropzone-uploader-main ' + el.className) : el.className;
        el.setAttribute('data-uploader-id', id);

        //图片展示内层主div
        var div1 = document.createElement('DIV');
        div1.className = 'uploader-bd';
        //图片展示UL层
        var ul = document.createElement('UL');
        ul.className = 'xyzDropzone-uploader-list';
        ul.setAttribute('data-uploader-id', id);
        ul.setAttribute('data-upload', JSON.stringify(readyConfig));
        //上传按钮外层div
        var divInputFile = document.createElement('DIV');
        divInputFile.className = 'xyzDropzone-uploader-form';
        //上传按钮
        var inputFile = document.createElement('INPUT');
        inputFile.className = "upload-input";
        inputFile.setAttribute('type', 'file');
        //inputFile.setAttribute('multiple', true);
        inputFile.setAttribute('accept', readyConfig.accept.join(','));
        inputFile.setAttribute('data-uploader-id', id);
        inputFile.setAttribute('bucket', readyConfig.bucket);
        inputFile.setAttribute('data-upload', JSON.stringify(readyConfig));
        //appendChild
        divInputFile.appendChild(inputFile);
        div1.appendChild(ul);
        div1.appendChild(divInputFile);
        el.appendChild(div1);
        var imgs = el.getAttribute('data-images');
        if (imgs) {
            imgs = imgs.split(',');
        } else {
            imgs = [];
        }
        xyzUploader.show(el, imgs);
        if ((typeof initCallback) == 'function') {
            initCallback(id);
        }
        if (readyConfig.url.indexOf('qiniu.com') > -1) {
            uploadToken(id);
        }
    };

    xyzUploader.show = function (el, images) {
        el = validEl(el);
        if ((typeof images != 'string') && !(images instanceof Array)) {
            throw new Error('images只允许单个url字符串或多个url字符串数组');
        }
        if (typeof images == 'string') {
            images = [images];
        }
        var id = el.getAttribute('data-uploader-id');
        var ul = document.querySelector('ul[data-uploader-id="' + id + '"]');
        for (var i = 0; i < images.length; i++) {
            if (images[i].length < 5) {
                continue;
            }
            var tempUrl = images[i];
            if (tempUrl.indexOf('file.duanyi.com.cn') <= -1) {//七牛处理
                tempUrl = images[i] + '-small';
            } else {
                //将showUrl变成小图的url
                var urlStart = images[i].substring(0, images[i].lastIndexOf('/'));
                var urlEnd = images[i].substring(images[i].lastIndexOf('/') + 1, images[i].length);
                tempUrl = urlStart + "/small_" + urlEnd;
            }

            //创建图片容器
            var li = document.createElement('LI');
            li.className = 'xyzDropzone-uploader-item xyzDropzone-uploader-struts';
            var img = document.createElement('IMG');
            img.src = tempUrl;
            img.alt = images[i];
            li.appendChild(img);
            var div = document.createElement('DIV');
            div.className = 'xyzDropzone-uploader-struts-content';
            //注意：这里只能把点击事件以innerHTML插入节点，动态绑定和事件代理在iOS设备上无法响应I标签点击，原因不明/(ㄒoㄒ)/~~
            div.innerHTML = '<i class="icon-wram" data-uploader-id="' + id + '" onclick="xyzUploader.removeItem(this)"></i>';
            li.appendChild(div);

            ul.appendChild(li);
        }
        showUploaderInput(id);
    };

    xyzUploader.getUrls = function (el) {
        el = validEl(el);
        var id = el.getAttribute('data-uploader-id');
        var imgs = document.querySelectorAll('ul[data-uploader-id="' + id + '"] img');
        var urls = new Array();
        for (var i = 0; imgs && i < imgs.length; i++) {
            urls.push(imgs[i].getAttribute('alt'));
        }
        return urls;
    };

    function validEl(el) {
        if ((typeof el) === 'string') {
            if (el.length < 2 || el.indexOf('#') != 0) {
                throw new Error('el字符串参数需要使用#打头并指向一个元素id');
            }
            el = document.getElementById(el.substring(1, el.length));
        } else if (window.jQuery && el instanceof jQuery) {
            el = el[0];
        } else if (el instanceof Element) {
            ;
        } else {
            throw new Error('el参数：' + el + '不符合要求');
        }
        if (!(el instanceof Element) || el.nodeName != 'DIV') {
            throw new Error('el无法指向一个DIV元素');
        }
        return el;
    }

    function getConfig(id, conf) {
        var config = {};
        var p = '|accept|size|count|params|timeout|url|bucket|';
        for (var key in _conf) {
            if (p.indexOf(key) < 0) {
                continue;
            }
            config[key] = (typeof _conf[key]) === (typeof conf[key]) ? conf[key] : _conf[key];
        }

        config.id = id;
        return config;
    }

    function registerCallback(id, conf) {
        //回调函数
        var callback = {};
        var p = '|success|progress|validFail|removeImage|error|';
        for (var key in _conf) {
            if (p.indexOf(key) < 0) {
                continue;
            }
            callback[key] = (typeof _conf[key]) === (typeof conf[key]) ? conf[key] : _conf[key];
        }

        //将所有回调函数注册到uploader函数管理中
        _fn[id] = callback;
    }

    function getQueueId() {
        var index = 0;
        for (var j in _fn) {
            index++;
        }
        index++;
        return 'uploader' + index + 'q';
    }

    function uploadSuccess(result, config) {
        if (result) {
            if (result.status == 1) {
                var el = document.querySelector('div[data-uploader-id="' + config.id + '"]');
                xyzUploader.show(el, result.content.url);
                if (_hasWeui) {
                    weui.hideLoading();
                }
                if (_fn[config.id] && (typeof _fn[config.id].success == 'function')) {
                    _fn[config.id].success(result, config);
                }
            } else {
                if (_hasWeui) {
                    weui.toast(result.msg);
                    weui.hideLoading();
                }
            }
        }
    };

    /**
     *使用事件代理，监听input type=file选中文件的事件
     **/
    window.addEventListener('change', function (e) {
        var isUpload = ('INPUT' === e.target.nodeName
        && e.target.className
        && e.target.className.indexOf('upload-input') > -1
        && e.target.getAttribute('type') === 'file'
        && e.target.getAttribute('data-upload')
        && e.target.getAttribute('data-uploader-id'));
        if (!isUpload) {
            return;
        }
        e.stopPropagation();
        var inputFile = e.target;
        var id = e.target.getAttribute('data-uploader-id');
        var config = {};//配置信息
        try {
            config = JSON.parse(e.target.getAttribute('data-upload'));
        } catch (e) {
            if (_hasWeui) {
                weui.toast('上传组件配置信息不可用');
            }
        }
        //预定义返回结果
        var result = {
            msg: '',
            config: config
        };
        //检查已上传的文件数量
        var li = document.querySelectorAll('ul[data-uploader-id="' + id + '"] li');
        if (li && li.length >= config.count) {
            inputFile.value = '';
            result.msg = '已达最大上传数量限制' + (config.count) + '个';
            return _fn[id].validFail(result);
        }
        for (var i = 0; i < inputFile.files.length; i++) {
            var fileItem = inputFile.files[i];
            //检查文件大小
            if (fileItem.size > config.size) {
                //此处插入自定义钩子
                inputFile.value = '';

                //大于 1024 kb 把单位显示为 mb
                if (config.size / 1000 > 1024) {
                    result.msg = '单文件最大允许 ' + Math.floor(config.size / 1000 / 1000) + ' MB';
                } else {
                    result.msg = '单文件最大允许 ' + (config.size / 1000) + ' KB';
                }

                _fn[id].validFail(result);
                return;
            }
            //检查图片文件格式是否正确
            var acceptFlag = false;
            for (var a = 0; a < config.accept.length; a++) {
                if (config.accept[a] == fileItem.type) {
                    acceptFlag = true;
                    break;
                }
            }
            if (!acceptFlag) {
                //此处插入自定义钩子
                inputFile.value = '';
                result.msg = '这里只允许上传' + config.accept.join(',') + '格式的文件';
                _fn[id].validFail(result);
                return;
            }

            //检查xhr支持
            if (!window.XMLHttpRequest) {
                if (_hasWeui) {
                    inputFile.value = '';
                    weui.toast('您的浏览器不支持异步文件上传');
                }
                return;
            }

            //构建xhr上传表单参数
            var form = new FormData();
            if (config.url.indexOf('qiniu.com') > -1) {//七牛
                form.append('file', fileItem);
                form.append('token', inputFile.getAttribute('token'));
                if(config.params['derictoryCode']){
                	form.append('x:folder', config.params['derictoryCode']);
                }else{
                	form.append('x:folder', 'default');
                }
            } else {
                form.append('fileData', fileItem);
            }
            for (var p in config.params) {
                form.append(p, config.params[p]);
            }
            //构建xhr对象
            var xhr = new XMLHttpRequest();
            xhr.open("POST", config.url, true);
            xhr.setRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
            //上传进度回调
            xhr.upload.onprogress = function (event) {
                result.total = event.total;
                result.loaded = event.loaded;
                result.progress = parseInt(event.loaded / event.total * 100);
                _fn[id].progress(result, event);
            };
            if (xhr.ontimeout) {
                xhr.ontimeout = function (event) {
                    if (_hasWeui) {
                        inputFile.value = '';
                        weui.toast('上传已超时');
                    }
                };
            }
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4) {
                    if (xhr.status == 200) {
                        var resultData = xhr.responseText;
                        try {
                            resultData = eval('(' + xhr.responseText + ')');
                        } catch (e) {
                            resultData = {
                                status: 0,
                                msg: xhr.responseText
                            };
                        }
                        inputFile.value = '';
                        if (resultData.content && resultData.content.suffix) {//qiniu处理
                            resultData.content.suffix = resultData.content.suffix.replace('.', '');
                        }
                        uploadSuccess(resultData, result.config);
                    } else {
                        if (_hasWeui) {
                            weui.hideLoading();
                        }
                        var resultData = {
                                status: 0,
                                msg: xhr.statusText
                            };
                        if(errCodeMsg['code'+xhr.status]){
                        	resultData.msg = errCodeMsg['code'+xhr.status];
                        	uploadSuccess(resultData, result.config);
                        }else{
                        	_fn[id].error(xhr.statusText, xhr, result.config);
                        } 
                    }
                }
            };
            xhr.send(form);//发送请求
        }
    });
    window.addEventListener('click', function (e) {
        var button = ('INPUT' === e.target.nodeName
        && e.target.className
        && e.target.className.indexOf('upload-input') > -1
        && e.target.getAttribute('type') === 'button'
        && e.target.getAttribute('data-uploader-id'));
        if (button) {
            editToggle(e.target);
        }
    });

    function editToggle(button) {
        var id = button.getAttribute('data-uploader-id');
        var parentDiv = button.parentNode;
        //找出对应的图片容器
        var li = document.querySelectorAll('ul[data-uploader-id="' + id + '"] li');
        //按钮外层样式切换
        var flag = parentDiv.className.indexOf('confirm') > -1;
        for (var i = 0; i < li.length; i++) {
            if (flag) {
                if (li[i].childNodes.length >= 2) {
                    li[i].removeChild(li[i].childNodes[li[i].childNodes.length - 1]);
                }
                li[i].className = li[i].className.replace(' xyzDropzone-uploader-struts', '');
            } else {
                var div = document.createElement('DIV');
                div.className = 'xyzDropzone-uploader-struts-content';
                //注意：这里只能把点击事件以innerHTML插入节点，动态绑定和事件代理在iOS设备上无法响应I标签点击，原因不明/(ㄒoㄒ)/~~
                div.innerHTML = '<i class="icon-wram" data-uploader-id="' + id + '" onclick="xyzUploader.removeItem(this)"></i>';
                li[i].appendChild(div);
                li[i].className = li[i].className + ' xyzDropzone-uploader-struts';
            }
        }
        if (flag) {
            parentDiv.className = parentDiv.className.replace('confirm', 'minus');
        } else {
            parentDiv.className = parentDiv.className.replace('minus', 'confirm');
        }
    }

    function removeItem(icon) {
        var target = icon;
        var id = target.getAttribute('data-uploader-id');
        for (; target && target != document; target = target.parentNode) {
            if (target.nodeName == 'LI' && target.className.indexOf('xyzDropzone-uploader-item') > -1) {
                target.parentNode.removeChild(target);
                if (id) {
                    showUploaderInput(id);
                    _fn[id].removeImage(id);
                }
            }
        }
    }

    xyzUploader.removeItem = function (target) {
        if (_hasWeui) {
            weui.confirm('删除此项？', function () {
                removeItem(target);
            });
        } else {
            if (confirm('删除此项？')) {
                removeItem(target);
            }
        }
    };

    function showUploaderInput(id) {
        var li = document.querySelectorAll('ul[data-uploader-id="' + id + '"] li');
        var uploader = document.querySelector('input[data-uploader-id="' + id + '"]');
        if (li && uploader) {
            var config = uploader.getAttribute('data-upload');
            if (config) {
                config = JSON.parse(config);
                uploader.parentNode.style.display = (li.length >= config.count) ? 'none' : '';
            }
        }
    }

    function uploadToken(uploaderId) {
        var uploader = document.querySelector('input[data-uploader-id="' + uploaderId + '"]');
        var xhr = new XMLHttpRequest();
        if (!uploader || !xhr) {
            return;
        }
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4) {
                if (xhr.status == 200) {
                    uploader.setAttribute('token', xhr.responseText);
                } else {
                    uploader.setAttribute('token', '');
                }
            }
        };
        var config = uploader.getAttribute('data-upload');
        var accept = uploader.getAttribute('accept');
        var bucket = uploader.getAttribute('bucket');
        try{
        	config = JSON.parse(config);
        }catch(e){
        	config = undefined;
        }
        var paramArray = new Array();
        if (bucket) {
            paramArray[paramArray.length] = (encodeURIComponent('bucket') + '=' + encodeURIComponent(bucket));
        }
        if(config){
        	paramArray[paramArray.length] = (encodeURIComponent('fsizeLimit') + '=' + encodeURIComponent(config.size));
        }
        if(accept){
        	accept = accept.replace(/,/g ,';');
        	paramArray[paramArray.length] = (encodeURIComponent('mimeLimit') + '=' + encodeURIComponent(accept));
        }
        var param = paramArray.join('&').replace('%20', '+');
        xhr.open("POST", _conf['tokenUrl'], true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        xhr.setRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        xhr.send(param);
    };

    window.xyzUploader = xyzUploader;
})();