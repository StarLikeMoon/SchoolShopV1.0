// 刷新验证码
function changeVerifyCode(img) {
    img.src = "../Kaptcha?" + Math.floor(Math.random() * 100);
}

// 根据url传过来的key，获取其中的值
function getQueryString(name) {

    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null){
        return decodeURIComponent(r[2]);
    }
    return '';

}

// 格式化时间
function formateDate(date) {
    var date = new Date(date);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    var d = date.getDate();
    var h = date.getHours();
    var mi = date.getMinutes();
    m = m > 9 ? m : '0' + m;
    // alert("y:"+y+"m:"+m+"d:"+d+"h:"+h+"mi:"+mi);
    return y + '-' + m + '-' + d + ' ' + h + ':' + mi;
}


// // 格式化日期
// Date.prototype.Format = function(fmt) {
//     var o = {
//         "M+" : this.getMonth() + 1, // 月份
//         "d+" : this.getDate(), // 日
//         "h+" : this.getHours(), // 小时
//         "m+" : this.getMinutes(), // 分
//         "s+" : this.getSeconds(), // 秒
//         "q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
//         "S" : this.getMilliseconds()
//         // 毫秒
//     };
//     if (/(y+)/.test(fmt))
//         fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
//             .substr(4 - RegExp.$1.length));
//     for ( var k in o)
//         if (new RegExp("(" + k + ")").test(fmt))
//             fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
//                 : (("00" + o[k]).substr(("" + o[k]).length)));
//     return fmt;
// }