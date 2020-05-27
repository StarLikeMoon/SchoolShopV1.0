$(function () {
    getlist();
    // 用来加载店铺信息
    function getlist(e) {
        $.ajax({
            url: "/O2OShop/shopadmin/getshoplist",
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    handleList(data.shoplist);
                    handleUser(data.user);
                } else {
                    alert("请先登录");
                    window.location.href="/O2OShop/local/login";
                    return false;
                }

            },
        });
    }

    // 填充用户名
    function handleUser(data) {
        $('#user-name').text(data.name);
    }

    // 填充商铺列表
    function handleList(data) {
        var html = '';
        data.map(function (item, index) {
            html += '<div class = "row row-shop"><div class="col-40">'
                + item.shopName + '</div><div class="col-40">'
                + shopStatus(item.enableStatus)
                + '</div><div class="col-20">'
                + goShop(item.enableStatus, item.shopId) + '</div></div>';
        });
        $('.shop-wrap').html(html);
    }

    // 填充店铺状态
    function shopStatus(status) {
        if (status == 0) {
            return '审核中';
        } else if (status == -1) {
            return '店铺非法状态';
        } else {
            return '审核通过，正常营业中';
        }
    }

    // 生成链接，点击之后可以进入到对应的商铺管理页面
    function goShop(status, id) {
        if (status == 1){
            return '<a href="/O2OShop/shopadmin/shopmanagement?shopId=' + id + '">进入</a>';
        } else {
            return '';
        }
    }
});