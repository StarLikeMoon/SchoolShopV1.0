$(function () {
    var productId = getQueryString('productId');
    // 获取当前商品信息
    var infoUrl = '/O2OShop/shopadmin/getproductbyid?productId=' + productId;
    // 获取当前店铺设定的商品类别列表
    var categoryUrl = '/O2OShop/shopadmin/getproductcategorylist';
    // 编辑商品的url
    var productPostUrl = '/O2OShop/shopadmin/modifyproduct';
    if (productId) {
        // 若有商品id，则进入编辑
        getInfo(productId);
    } else {
        // 没有就进入添加，先获取所有商品类别
        getCategory();
        productPostUrl = '/O2OShop/shopadmin/addproduct';
    }

    // 获取商品的信息
    function getInfo(productId) {
        $.getJSON(infoUrl, function (data) {
            if (data.success) {
                // 从返回的json中获取product对象信息，并赋值给表单
                var product = data.product;
                $('#product-name').val(product.productName);
                $('#product-desc').val(product.productDesc);
                $('#priority').val(product.priority);
                $('#normal-price').val(product.normalPrice);
                $('#promotion-price').val(product.promotionPrice);

                var optionHtml = '';
                var optionArr = data.productCategoryList;
                var optionSelected = product.productCategory.productCategoryId;
                optionArr.map(function (item, index) {
                        var isSelect = optionSelected === item.productCategoryId ? 'selected'
                            : '';
                        optionHtml += '<option data-value="'
                            + item.productCategoryId
                            + '"'
                            + isSelect
                            + '>'
                            + item.productCategoryName
                            + '</option>';
                    });
                $('#product-category').html(optionHtml);
            }
        });
    }

    // 如果是新增商品，就只需要获取该店铺下的商品分类，填充下拉栏即可
    function getCategory() {
        $.getJSON(categoryUrl, function (data) {
            if (data.success) {
                var productCategoryList = data.data;
                var optionHtml = '';
                productCategoryList.map(function (item, index) {
                    optionHtml += '<option data-value="'
                        + item.productCategoryId + '">'
                        + item.productCategoryName + '</option>';
                });
                $('#product-category').html(optionHtml);
            }
        });
    }

    // 每上传一张详情图，就新增一个上传按钮
    $('.detail-img-div').on('change', '.detail-img:last-child', function () {
        if ($('.detail-img').length < 6) {
            $('#detail-img').append('<input type="file" class="detail-img">');
        }
    });

    // 提交数据给后台
    $('#submit').click(
        function () {
            var product = {};
            product.productName = $('#product-name').val();
            product.productDesc = $('#product-desc').val();
            product.priority = $('#priority').val();
            product.normalPrice = $('#normal-price').val();
            product.promotionPrice = $('#promotion-price').val();
            product.productCategory = {
                productCategoryId: $('#product-category').find('option').not(
                    function () {
                        return !this.selected;
                    }).data('value')
            };
            product.productId = productId;
            // 获取缩略图
            var thumbnail = $('#small-img')[0].files[0];
            console.log(thumbnail);
            var formData = new FormData();
            formData.append('thumbnail', thumbnail);
            // 遍历商品详情空间，获取文件流
            $('.detail-img').map(
                function (index, item) {
                    if ($('.detail-img')[index].files.length > 0) {
                        formData.append('productImg' + index,
                            $('.detail-img')[index].files[0]);
                    }
                });
            formData.append('productStr', JSON.stringify(product));
            var verifyCodeActual = $('#j_captcha').val();
            if (!verifyCodeActual) {
                $.toast('请输入验证码！');
                return;
            }
            formData.append("verifyCodeActual", verifyCodeActual);
            $.ajax({
                url: productPostUrl,
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                cache: false,
                success: function (data) {
                    if (data.success) {
                        $.toast('提交成功！');
                        $('#captcha_img').click();
                    } else {
                        $.toast('提交失败！');
                        $('#captcha_img').click();
                    }
                }
            });
        });

});