$(function () {
    var shopId = getQueryString('shopId');
    var listUrl = '/O2OShop/shopadmin/getproductlistbyshop?pageIndex=1&pageSize=9999';
    var deleteUrl = '/O2OShop/shopadmin/modifyproduct';

    function getList() {
        // 从后台获取此店铺的商品列表
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                var productList = data.productList;
                var tempHtml = '';
                productList.map(function (item, index) {
                    var textOp = "下架";
                    var contraryStatus = 0;
                    if (item.enableStatus == 0) {
                        textOp = "上架";
                        contraryStatus = 1;
                    } else {
                        contraryStatus = 0;
                    }
                    tempHtml += '' + '<div class="row row-product">'
                        + '<div class="col-33">'
                        + item.productName
                        + '</div>'
                        + '<div class="col-20">'
                        + item.priority
                        + '</div>'
                        + '<div class="col-40">'
                        + '<a href="#" class="edit" data-id="'
                        + item.productId
                        + '" data-status="'
                        + item.enableStatus
                        + '">编辑</a>'
                        + '<a href="#" class="delete" data-id="'
                        + item.productId
                        + '" data-status="'
                        + contraryStatus
                        + '">'
                        + textOp
                        + '</a>'
                        + '<a href="#" class="preview" data-id="'
                        + item.productId
                        + '" data-status="'
                        + item.enableStatus
                        + '">预览</a>'
                        + '</div>'
                        + '</div>';
                });
                $('.product-wrap').html(tempHtml);
                $('#goback').attr('href', '/O2OShop/shopadmin/shopmanagement?shopId=' + shopId);
            }
        });
    }

    getList();

    function deleteItem(id, enableStatus) {
        var product = {};
        product.productId = id;
        product.enableStatus = enableStatus;
        $.confirm('确定么?', function () {
            $.ajax({
                url: deleteUrl,
                type: 'POST',
                data: {
                    productStr: JSON.stringify(product),
                    statusChange: true
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        $.toast('操作成功！');
                        getList();
                    } else {
                        $.toast('操作失败！');
                    }
                }
            });
        });
    }

    // 将class为product-wrap的a标签绑定上点击事件，比如编辑，上下架
    $('.product-wrap').on('click', 'a',
        function (e) {
            var target = $(e.currentTarget);
            if (target.hasClass('edit')) {
                // 如果是点击了edit
                window.location.href = '/O2OShop/shopadmin/productoperation?productId='
                    + e.currentTarget.dataset.id;
            } else if (target.hasClass('delete')) {
                deleteItem(e.currentTarget.dataset.id,
                    e.currentTarget.dataset.status);
            } else if (target.hasClass('preview')) {
                window.location.href = '/O2OShop/shopadmin/productoperation?productId='
                    + e.currentTarget.dataset.id;
            }
        });

    $('#new').click(function () {
        window.location.href = '/O2OShop/shopadmin/productoperation';
    });
});