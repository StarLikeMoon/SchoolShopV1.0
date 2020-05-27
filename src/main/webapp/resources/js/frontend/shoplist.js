$(function () {
    var loading = false;
    // 分页允许返回的最大条数，超过就禁止访问后台
    var maxItems = 999;
    // 一页返回的最大条数
    var pageSize = 10;
    // 加载商铺列表
    var listUrl = '/O2OShop/frontend/listshops';
    // 加载类别列表以及区域列表
    var searchDivUrl = '/O2OShop/frontend/listshopspageinfo';
    // 页码
    var pageNum = 1;
    // 在地址栏里尝试获取parentId
    var parentId = getQueryString('parentId');
    var areaId = '';
    var shopCategoryId = '';
    var shopName = '';
    // 渲染出店铺类别列表
    getSearchDivData();
    // 预先加载10条店铺信息
    addItems(pageSize, pageNum);

    function getSearchDivData() {
        // 如果传入了parentId，说明是二级分类，就重组url，传parentId给后台
        var url = searchDivUrl + '?' + 'parentId=' + parentId;
        $.getJSON(url, function (data) {
            if (data.success) {
                // 获取后台传回的shopCategoryList
                var shopCategoryList = data.shopCategoryList;
                var html = '';
                html += '<a href="#" class="button" data-category-id=""> 全部类别  </a>';
                // 遍历shopCategoryList，拼接出标签
                shopCategoryList.map(function (item, index) {
                    html += '<a href="#" class="button" data-category-id='
                        + item.shopCategoryId
                        + '>'
                        + item.shopCategoryName
                        + '</a>';
                });
                // 将标签嵌入前台组件
                $('#shoplist-search-div').html(html);
                var selectOptions = '<option value="">全部区域</option>';
                var areaList = data.areaList;
                areaList.map(function (item, index) {
                    selectOptions += '<option value="'
                        + item.areaId + '">'
                        + item.areaName + '</option>';
                });
                $('#area-search').html(selectOptions);
            }
        });
    }

    // 获取根据筛选条件分页展示的店铺列表信息
    function addItems(pageSize, pageIndex) {
        // 生成新条目的HTML
        var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
            + pageSize + '&parentId=' + parentId + '&areaId=' + areaId
            + '&shopCategoryId=' + shopCategoryId + '&shopName=' + shopName;
        // 加载符，防止多次访问后台重复加载
        loading = true;
        $.getJSON(url, function (data) {
            if (data.success) {
                // 获取当前条件下店铺总数
                maxItems = data.count;
                var html = '';
                // 遍历集合，拼接出店铺标签
                data.shopList.map(function (item, index) {
                    html += '' + '<div class="card" data-shop-id="'
                        + item.shopId + '">' + '<div class="card-header">'
                        + item.shopName + '</div>'
                        + '<div class="card-content">'
                        + '<div class="list-block media-list">' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">' + '<img src="'
                        + item.shopImg + '" width="44">' + '</div>'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + item.shopDesc
                        + '</div>' + '</div>' + '</li>' + '</ul>'
                        + '</div>' + '</div>' + '<div class="card-footer">'
                        + '<p class="color-gray">'
                        + formateDate(item.lastEditTime)
                        + '更新</p>' + '<span>点击查看</span>' + '</div>'
                        + '</div>';
                });
                $('.list-div').append(html);
                // 已经加载的数量
                var total = $('.list-div .card').length;
                // 如果加载数量达到店铺最大数量，就停止加载
                if (total >= maxItems) {
                    // 隐藏加载提示符
                    $('.infinite-scroll-preloader').hide();
                } else {
                    $('.infinite-scroll-preloader').show();
                }
                pageNum += 1;
                loading = false;
                // 刷新页面显示新加载的店铺
                $.refreshScroller();
            }
        });
    }

    // 下滑屏幕自动进行分页搜索
    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading)
            return;
        addItems(pageSize, pageNum);
    });

    // 点击店铺的卡片进入店铺详情页
    $('.shop-list').on('click', '.card', function (e) {
        var shopId = e.currentTarget.dataset.shopId;
        window.location.href = '/O2OShop/frontend/shopdetail?shopId=' + shopId;
    });

    // 选择新的店铺类别之后，重置页码，清空列表，重新查询加载
    $('#shoplist-search-div').on(
        'click',
        '.button',
        function (e) {
            if (parentId) {// 如果传递过来的是一个父类下的子类
                shopCategoryId = e.target.dataset.categoryId;
                //如果之前已经选择了别的分类卡，现在移除效果，重新更改成新的
                if ($(e.target).hasClass('button-fill')) {
                    $(e.target).removeClass('button-fill');
                    shopCategoryId = '';
                } else {
                    $(e.target).addClass('button-fill').siblings()
                        .removeClass('button-fill');
                }
                // 清空列表重新查询
                $('.list-div').empty();
                // 重置页码
                pageNum = 1;
                addItems(pageSize, pageNum);
            } else {// 如果传递过来的父类为空，则按照父类查询
                parentId = e.target.dataset.categoryId;
                if ($(e.target).hasClass('button-fill')) {
                    $(e.target).removeClass('button-fill');
                    parentId = '';
                } else {
                    $(e.target).addClass('button-fill').siblings()
                        .removeClass('button-fill');
                }
                $('.list-div').empty();
                pageNum = 1;
                addItems(pageSize, pageNum);
                parentId = '';
            }

        });

    // 搜索栏发生变化，就清空重置，重新查询加载
    $('#search').on('change', function (e) {
        shopName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });

    // 区域信息发生变化，就清空重置，重新查询加载
    $('#area-search').on('change', function () {
        areaId = $('#area-search').val();
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });

    // 打开侧边栏
    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });

    $.init();
});
