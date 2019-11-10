/**
 * Created by Administrator on 2017/10/20.
 */
function chinaDate(date){
  let year = date.split("-")[0];
  let month = parseInt(date.split("-")[1]);
  let day = parseInt(date.split("-")[2]);
  let result = year+"年"+month+"月"+day+"日";
  return result;
}
function xyzIsNull(obj){
  return !obj;
}
function xyzIsPhone(phone){
  return (/^1[0-9]{10}$/g).test(phone);
}
function xyzIsEmail(email){
  let emailReg=/^\w+([-\.]\w+)*@\w+([\.-]\w+)*\.\w{2,4}$/;
  if (emailReg.test(email)){
    return true;
  }else{
    return false;
  }
}
function xyzIsMobile(){
  let u = navigator.userAgent;
  let android = u.indexOf('Android')>-1 || u.indexOf('Linux')>-1;
  let iPhone = u.indexOf('iPhone')>-1;
  let iPad = u.indexOf('iPad')>-1;
  if(android  || iPhone || iPad){
    return true;
  }else{
    return false;
  }
}
function xyzJsonToObject(str){
  if(xyzIsNull(str)){
    return "";
  }else{
    str = str.replace(/\n/g, " ");
    str = str.replace(/\r/g, " ");
    str = str.replace(/\t/g, " ");
    return JSON.parse(str);
  }
}
function xyzHtmlDecode(str) {
  if (xyzIsNull(str)) {
    return str;
  }
  str = str.replace(/&lt;/g, "<");
  str = str.replace(/&gt;/g, ">");
  str = str.replace(/&acute;/g, "'");
  str = str.replace(/&#45;&#45;/g, "--");
  str = str.replace(/&lt;/g, "<");
  str = str.replace(/&lt;/g, "<");
  str = str.replace(/&quot;/g, '\"');
  str = str.replace(/&acute;/g, "\'");
  return str;
}
function clone(obj){
  let o;
  if(typeof obj=='object'){
    if(obj === null){
      o = null;
    }else{
      if(obj instanceof Array){
        o = [];
        for(let i in obj){
          o.push(clone(obj[i]));
        }
      }else{
        o = {};
        for(let j in obj){
          o[j] = clone(obj[j]);
        }
      }
    }
  }else{
    o = obj;
  }
  return o;
}
Date.prototype.Format = function(fmt) { // author: meizz
  let o = {
    "M+" : this.getMonth() + 1, // 月份
    "d+" : this.getDate(), // 日
    "h+" : this.getHours(), // 小时
    "m+" : this.getMinutes(), // 分
    "s+" : this.getSeconds(), // 秒
    "q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
    "S" : this.getMilliseconds()
    // 毫秒
  };
  if (/(y+)/.test(fmt)){
    fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  }
  for (let k in o){
    if (new RegExp("(" + k + ")").test(fmt)){
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    }
  }
  return fmt;
};
/**
 * 一个时间
 * @param d1
 */
function timeFn(d1) {
  let dateBegin = new Date(d1.replace(/-/g, "/"));
  let dateEnd = new Date();
  let dateDiff = dateBegin.getTime() - dateEnd.getTime();
  let dayDiff = Math.floor(Math.abs(dateDiff / (24 * 3600 * 1000)));//计算出相差天数
  let leave1=dateDiff%(24*3600*1000);
  let hours=Math.floor(Math.abs(leave1/(3600*1000)));
  //计算相差分钟数
  let leave2=leave1%(3600*1000);
  let minutes=Math.floor(Math.abs(leave2/(60*1000)));
  //计算相差秒数
  let leave3=leave2%(60*1000)
  let seconds=Math.round(leave3/1000)
  // + minutes + " 分钟" + seconds+" 秒"
  let dateStr;
  if(dayDiff){
    dateStr = dayDiff + "天";
    if(hours){
      dateStr += hours + "小时";
    }
  }else {
    dateStr = hours + "小时";
    if(minutes){
      dateStr += minutes + "分";
    }
  }
  return dateStr;
}

/**
 * 处理对象数组去重
 * @param array
 * @returns
 */
function distinctObjArray(array){
  if(array.length<=0)
    return array;
  let n = {},r=[]; //n为hash表，r为临时数组
  for(let i = 0; i < array.length; i++){
    if(array[i] instanceof Array){
      array[i] = distinctObjArray(array[i]);
    }
    let pp = array[i];
    pp = (pp instanceof Object || pp instanceof Array)?JSON.stringify(pp):pp;
    if (!n[pp]){
      n[pp] = true; //存入hash表
      r.push(array[i]); //把当前数组的当前项push到临时数组里面
    }
  }
  return r;
}
function getUrlParameters(){
  let result = {};
  let q;
  if(location.href.indexOf('?') > -1){
    if( location.href.indexOf('&isappinstalled=0')>-1){
      q = location.href.split('ptDetailToC?')[1];
    }else{
      q = location.href.split('?')[1];
    }
    let qs = q.split("&");
    if (qs) {
      for (let i = 0; i < qs.length; i++) {
        let key = qs[i].substring(0, qs[i].indexOf("="));
        result[key] = qs[i].substring(qs[i].indexOf("=") + 1);
      }
    }
  }
  return result;
}

export default {
  chinaDate,xyzIsNull,xyzIsEmail,xyzIsPhone,xyzIsMobile,xyzJsonToObject,xyzHtmlDecode,clone,distinctObjArray,getUrlParameters,timeFn
}
