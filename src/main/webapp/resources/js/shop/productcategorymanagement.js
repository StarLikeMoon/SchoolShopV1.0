$(function () {
    var shopId = getQueryString('shopId');
    var listUrl = '/O2OShop/shopadmin/getproductcategorylist';
    var addUrl = '/O2OShop/shopadmin/addproductcategorys';
    var deleteUrl = '/O2OShop/shopadmin/removeproductcategory';
    getList();

    // 获取店铺商品类别列表
    function getList() {
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                var dataList = data.data;
                $('.category-wrap').html('');
                var tempHtml = '';
                dataList.map(function (item, index) {
                    tempHtml += ''
                        + '<div class="row row-product-category now">'
                        + '<div class="col-33 product-category-name">'
                        + item.productCategoryName
                        + '</div>'
                        + '<div class="col-33">'
                        + item.priority
                        + '</div>'
                        + '<div class="col-33"><a href="#" class="button delete" data-id="'
                        + item.productCategoryId
                        + '">删除</a></div>' + '</div>';
                });
                $('.category-wrap').append(tempHtml);
            }
            $('#goback').attr('href', '/O2OShop/shopadmin/shopmanagement?shopId=' + shopId);
        });
    }

    // 点击新增按钮会创建一个新的空白行
    $('#new').click(
        function () {
            var tempHtml = '<div class="row row-product-category temp">'
                + '<div class="col-33"><input class="category-input product-category-name" type="text" placeholder="分类名"></div>'
                + '<div class="col-33"><input class="category-input priority" type="number" placeholder="优先级"></div>'
                + '<div class="col-33"><a href="#" class="button delete">删除</a></div>'
                + '</div>';
            $('.category-wrap').append(tempHtml);
        });

    // 点击新增提交待添加的商品类别给后台
    $('#submit').click(function () {
        var tempArr = $('.temp');
        var productCategoryList = [];
        tempArr.map(function (index, item) {
            var tempObj = {};
            tempObj.productCategoryName = $(item).find('.product-category-name').val();
            tempObj.priority = $(item).find('.priority').val();
            if (tempObj.productCategoryName && tempObj.priority) {
                productCategoryList.push(tempObj);
            }
        });
        $.ajax({
            url: addUrl,
            type: 'POST',
            data: JSON.stringify(productCategoryList),
            contentType: 'application/json',
            success:function (data) {
                if (data.success) {
                    getList();
                    $ .toast('提交成功！');
                    alert("提交成功！");
                } else {
                    $ .toast('提交失败！请重试');
                }
            }
        });
    });

    $('.category-wrap').on('click', '.row-product-category.now .delete',
        function (e) {
            var target = e.currentTarget;
            $.confirm('确定么?', function () {
                $.ajax({
                    url: deleteUrl,
                    type: 'POST',
                    data: {
                        productCategoryId: target.dataset.id,
                        shopId: shopId
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.success) {
                            $.toast('删除成功！');
                            getList();
                        } else {
                            $.toast('删除失败！');
                        }
                    }
                });
            });
        });

    $('.category-wrap').on('click', '.row-product-category.temp .delete',
        function (e) {
            console.log($(this).parent().parent());
            $(this).parent().parent().remove();

        });
});