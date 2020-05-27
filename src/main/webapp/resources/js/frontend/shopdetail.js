$(function () {
    var loading = false;
    var maxItems = 20;
    var pageSize = 10;

    var listUrl = '/O2OShop/frontend/listproductsbyshop';

    var pageNum = 1;
    var shopId = getQueryString('shopId');
    var productCategoryId = '';
    var productName = '';

    var searchDivUrl = '/O2OShop/frontend/listshopdetailpageinfo?shopId='
        + shopId;

    getSearchDivData();

    // 填充店铺信息
    function getSearchDivData() {
        var url = searchDivUrl;
        $
            .getJSON(
                url,
                function (data) {
                    if (data.success) {
                        var shop = data.shop;
                        $('#shop-cover-pic').attr('src', shop.shopImg);
                        $('#shop-update-time').html(formateDate(shop.lastEditTime));
                        $('#shop-name').html(shop.shopName);
                        $('#shop-desc').html(shop.shopDesc);
                        $('#shop-addr').html(shop.shopAddr);
                        $('#shop-phone').html(shop.phone);

                        var productCategoryList = data.productCategoryList;
                        var html = '';
                        productCategoryList
                            .map(function (item, index) {
                                html += '<a href="#" class="button" data-product-search-id='
                                    + item.productCategoryId
                                    + '>'
                                    + item.productCategoryName
                                    + '</a>';
                            });
                        $('#shopdetail-button-div').html(html);
                    }
                });
    }


    // 生成店铺中的商品列表
    function addItems(pageSize, pageIndex) {
        // 生成新条目的HTML
        var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
            + pageSize + '&productCategoryId=' + productCategoryId
            + '&productName=' + productName + '&shopId=' + shopId;
        loading = true;
        $.getJSON(url, function (data) {
            if (data.success) {
                maxItems = data.count;
                var html = '';
                data.productList.map(function (item, index) {
                    html += '' + '<div class="card" data-product-id='
                        + item.productId + '>'
                        + '<div class="card-header">' + item.productName
                        + '</div>' + '<div class="card-content">'
                        + '<div class="list-block media-list">' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">' + '<img src="'
                        + item.imgAddr + '" width="44">' + '</div>'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + item.productDesc
                        + '</div>' + '</div>' + '</li>' + '</ul>'
                        + '</div>' + '</div>' + '<div class="card-footer">'
                        + '<p class="color-gray">'
                        + formateDate(item.lastEditTime)
                        + '更新</p>' + '<span>点击查看</span>' + '</div>'
                        + '</div>';
                });
                $('.list-div').append(html);
                var total = $('.list-div .card').length;
                if (total >= maxItems) {
                    // 隐藏加载提示符
                    $('.infinite-scroll-preloader').hide();
                } else {
                    $('.infinite-scroll-preloader').show();
                }
                pageNum += 1;
                loading = false;
                $.refreshScroller();
            }
        });
    }

    addItems(pageSize, pageNum);

    // 下滑屏幕自动加载
    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading)
            return;
        addItems(pageSize, pageNum);
    });

    // 重新筛选商品
    $('#shopdetail-button-div').on(
        'click',
        '.button',
        function (e) {
            productCategoryId = e.target.dataset.productSearchId;
            if (productCategoryId) {
                if ($(e.target).hasClass('button-fill')) {
                    $(e.target).removeClass('button-fill');
                    productCategoryId = '';
                } else {
                    $(e.target).addClass('button-fill').siblings()
                        .removeClass('button-fill');
                }
                $('.list-div').empty();
                pageNum = 1;
                addItems(pageSize, pageNum);
            }
        });

    // 点击商品进入商品详情
    $('.list-div')
        .on(
            'click',
            '.card',
            function (e) {
                var productId = e.currentTarget.dataset.productId;
                window.location.href = '/O2OShop/frontend/productdetail?productId='
                    + productId;
            });

    // 搜索时重新加载
    $('#search').on('change', function (e) {
        productName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });

    // 点击我，打开侧边栏
    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });
    $.init();
});
