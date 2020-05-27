$(function() {
    // 获取首页信息
    var url = '/O2OShop/frontend/listmainpageinfo';
    $.getJSON(url, function (data) {
        if (data.success) {
            // 获取头条列表
            var headLineList = data.headLineList;
            var swiperHtml = '';
            headLineList.map(function (item, index) {
                swiperHtml += ''
                    + '<div class="swiper-slide img-wrap">'
                    +      '<img class="banner-img" src="'+ item.lineImg +'" alt="'+ item.lineName +'">'
                    + '</div>';
            });
            $('.swiper-wrapper').html(swiperHtml);
            // 设定轮播图3s滚动一次
            $(".swiper-container").swiper({
                autoplay: 3000,
                // 用户操作轮播图时，是否自动停止滚动
                autoplayDisableOnInteraction: false
            });
            // 大类列表
            var shopCategoryList = data.shopCategoryList;
            var categoryHtml = '';
            shopCategoryList.map(function (item, index) {
                categoryHtml += ''
                    +  '<div class="col-50 shop-classify" data-category='+ item.shopCategoryId +'>'
                    +      '<div class="word">'
                    +          '<p class="shop-title">'+ item.shopCategoryName +'</p>'
                    +          '<p class="shop-desc">'+ item.shopCategoryDesc +'</p>'
                    +      '</div>'
                    +      '<div class="shop-classify-img-warp">'
                    +          '<img class="shop-img" src="'+ item.shopCategoryImg +'">'
                    +      '</div>'
                    +  '</div>';
            });
            user = data.user;
            $('.row').html(categoryHtml);
        }
    });

    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });

    $('.row').on('click', '.shop-classify', function (e) {
        var shopCategoryId = e.currentTarget.dataset.category;
        var newUrl = '/O2OShop/frontend/shoplist?parentId=' + shopCategoryId;
        window.location.href = newUrl;
    });


});
