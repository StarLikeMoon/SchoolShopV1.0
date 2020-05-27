$(function () {
    var loginUrl = '/O2OShop/local/logincheck';
    // 登录次数，超过三次失败就开始输验证码
    var loginCount = 0;
    // 用户的类型
    var usertype = 0;

    $('#submit').click(function () {
        // 获取输入的账户信息
        var username = $('#username').val();
        var password = $('#psw').val();
        var verifyCodeActual = $('#j_captcha').val();
        // 是否需要验证码验证，默认3次以内不用
        var needVerify = false;
        // 3次及以上就需要输入验证码
        if (loginCount >= 3) {
            if (!verifyCodeActual) {
                $.toast('请输入验证码！');
                return;
            } else {
                needVerify = true;
            }
        }
        // 访问后台进行验证
        $.ajax({
            url: loginUrl,
            async: false,
            cache: false,
            type: "post",
            dataType: 'json',
            data: {
                username: username,
                password: password,
                verifyCodeActual: verifyCodeActual,
                needVerify: needVerify
            },
            success: function (data) {
                if (data.success) {
                    $.toast('登录成功！');
                    usertype = data.usertype;
                    window.location.href = '/O2OShop/frontend/index?usertype=' + usertype;
                } else {
                    $.toast('登录失败！');
                    loginCount++;
                    if (loginCount >= 3) {
                        $('#verifyPart').show();
                    }
                }
            }
        });
    });

    $('#register').click(function () {
        window.location.href = '/O2OShop/local/register';
    });
});