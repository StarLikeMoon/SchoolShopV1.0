$(function () {
    var shopId = getQueryString('shopId');
    var shopInfoUrl = '/O2OShop/shopadmin/getshopmanagementinfo?shopId=' + shopId;
    $.getJSON(shopInfoUrl, function (data) {
        if (data.redirect) {
            window.location.href = data.url;
        } else {
            if (data.shopId != undefined && data.shopId != null) {
                shopId = data.shopId;
            }
            $('#shopInfo').attr('href', '/O2OShop/shopadmin/shopoperation?shopId=' + shopId);
            $('#productCate').attr('href', '/O2OShop/shopadmin/productcategorymanagement?shopId=' + shopId);
            $('#productmanagement').attr('href', '/O2OShop/shopadmin/productmanagement?shopId=' + shopId);
        }
    });
});