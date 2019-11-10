/**
 * Created by Administrator on 2017/10/20.
 */
import bus from './component'
import axios from 'axios'
import Router from '../../router';
let qs = require('querystring');
axios.defaults.timeout = 5000;
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';

// 请求拦截（配置发送请求的信息）
axios.interceptors.request.use((config) => {
  // 处理请求之前的配置
  let urlObj = config.url.split('api/');
  let api;
  if(config.url.indexOf('api/')>-1){
    urlObj = config.url.split('api/')[1];
    api = 'api/';
  }
  urlObj = JSON.parse(urlObj);
  let url;
  if(api){
    url = api + urlObj.url;
  }else {
    url = urlObj.url;
  }
  let isApiKey = urlObj.isApiKey;
  let isLoading = urlObj.isLoading;
  let userType = sessionStorage.userType;
  if(isApiKey && userType === 'true'){
    let apiKey = sessionStorage.apikey;
    if(!config.data) config.data = new Object();
    config.data.apikey = apiKey;
  }else {
    if(urlObj.freeUrl){
      if(api){
        url = api + urlObj.freeUrl;
      }else {
        url = urlObj.freeUrl;
      }
      let possessor = sessionStorage.possessor;
      if(!config.data) config.data = new Object();
      config.data.possessor = possessor;
    }
  }
  config.url = url;
  if(isLoading) bus.Loading.show();

  if(config.method  === 'post'){
    config.data = qs.stringify(config.data);
  }
  return config;
},(error) =>{
  // 请求失败的处理
  bus.Loading.hide();
  bus.dialog.showDialog('请求失败');
  return Promise.reject(error);
});

// 响应拦截（配置请求回来的信息）
axios.interceptors.response.use((response) => {
  // 处理响应数据
  bus.Loading.hide();
  let data = response.data;
  if(data.status === 1){
    return response.data;
  }else if(data.status === 0) {
    data.msg && bus.dialog.showDialog(data.msg);
    return response.data;
  }else {
    bus.dialog.showDialog('后台错误,请联系系统管理员');
  }
}, function (error){
  // 处理响应失败
  bus.Loading.hide();
  if(error.response){
    switch (error.response.status) {
      case 400:
        error.message = '请求错误';
        break;
      case 401:
        error.message = '未授权，请登录';
        break;
      case 403:
        error.message = '拒绝访问';
        break;
      case 404:
        error.message = `请求地址出错: ${error.response.config.url}`;
        Router.push('/noPage');
        break;
      case 408:
        error.message = '请求超时'
        break;
      case 500:
        error.message = '服务器内部错误'
        break;
      case 501:
        error.message = '服务未实现'
        break;
      case 502:
        error.message = '网关错误'
        break;
      case 503:
        error.message = '服务不可用'
        break;
      case 504:
        error.message = '网关超时'
        break;
      case 505:
        error.message = 'HTTP版本不受支持'
        break;
      default:
        break;
    }
  }
  bus.dialog.showDialog(error.message);
  return Promise.reject(error);
});
