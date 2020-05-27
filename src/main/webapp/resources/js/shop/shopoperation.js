/**
 *
 */
$(function(){
    var shopId = getQueryString('shopId');
    var isEdit = shopId ? true : false;
    var initUrl = '/O2OShop/shopadmin/getshopinitinfo';//获取店铺的初始信息,把下拉菜单进行填充
    var registerShopUrl = '/O2OShop/shopadmin/registershop';//注册店铺
    var shopInfoUrl = '/O2OShop/shopadmin/getshopbyid?shopId=' + shopId; // 获取指定id的店铺信息
    var editShopUrl = '/O2OShop/shopadmin/modifyshop';
    if (!isEdit){
        // 如果不是修改，是需要注册
        getShopInitInfo();
    } else {
        // 修改店铺
        getShopInfo(shopId);
    }

    // 定义方法getShopInfo:获取当前店铺信息
    function getShopInfo(shopId) {
        $.getJSON(shopInfoUrl, function (data) {
            if (data.success){
                var shop = data.shop;
                $('#shop-name').val(shop.shopName);
                $('#shop-addr').val(shop.shopAddr);
                $('#shop-phone').val(shop.phone);
                $('#shop-desc').val(shop.shopDesc);
                // 分类不能修改，直接显示
                var shopCategory = '<option data-id="'
                    + shop.shopCategory.shopCategoryId + '" selected>'
                    + shop.shopCategory.shopCategoryName + '</option>';
                var tempAreaHtml = '';
                // 区域可以修改
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">'
                        + item.areaName + '</option>';
                });
                $('#shop-category').html(shopCategory);
                $('#shop-category').attr('disabled', 'disabled');
                $('#shop-area').html(tempAreaHtml);
                $('#shop-area option[data-id="'+ shop.area.areaId+'"]').attr('selected', 'selected');
            }
        });
    }

    // 定义方法getShopInitInfo：获取商铺分类、区域的列表信息
    function getShopInitInfo(){
    	// alert(initUrl);//调试弹窗，证明js文件被加载
        $.getJSON(initUrl,function(data){ //访问的URL，回调方法
            if(data.success){ //true
                 // alert(initUrl);
                var tempHtml = '';//存放店铺类别列表
                var tempAreaHtml = '';//存放区域列表
                data.shopCategoryList.map(function(item,index){
                    //用map遍历店铺类别列表，生成如<option data-id="1">盖浇饭</option>的列表
                    tempHtml += '<option data-id="' + item.shopCategoryId + '">' +item.shopCategoryName + "</option>";
                });
                data.areaList.map(function(item,index){
                    //用map遍历区域列表，生成如<option data-id="1">东苑</option>的列表
                    tempAreaHtml += '<option data-id="' + item.areaId + '">' + item.areaName + '</option>';
                });
                //完成遍历之后，将获取到的信息，塞进前台定义好的id里面 #是css的用法，代表id
                $('#shop-category').html(tempHtml);
                $('#shop-area').html(tempAreaHtml);
            }
        });
    }

    //第二个方法：点击提交，获取到表单信息，通过ajax转发到后台
    $('#submit').click(function(){//点击id为submit的空间，调用方法
        var shop={};//json对象
        // 如果是修改店铺需要传入shopId
        if (isEdit){
            shop.shopId = shopId;
        }
        //获取控件的信息
        shop.shopName = $('#shop-name').val();
        shop.shopAddr = $('#shop-addr').val();
        shop.phone = $('#shop-phone').val();
        shop.shopDesc = $('#shop-desc').val();
        shop.shopCategory = {
            shopCategoryId:$('#shop-category').find('option').not(function(){//返回被选中的
                return !this.selected;
            }).data('id')
        };
        shop.area = {
            areaId:$('#shop-area').find('option').not(function(){
                return !this.selected;
            }).data('id')
        };
        var shopImg = $('#shop-img')[0].files[0];
        var formData = new FormData();//定义表单，用于接收
        formData.append('shopImg',shopImg);
        formData.append('shopStr',JSON.stringify(shop));//将json转换成字符流
        // 传验证码到后台
        var verifyCodeActual = $('#j-kaptcha').val();
        if(!verifyCodeActual){
            $.toast('请输入验证码！');
            return;
        }
        formData.append('verifyCodeActual',verifyCodeActual);
        $.ajax({
            url: (isEdit?editShopUrl:registerShopUrl),
            type:'POST',
            data:formData,
            contentType:false,//既要传文件，又要传文字，设为false
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    $.toast("提交成功！");
                }else{
                    $.toast("提交失败："+data.errMsg);
                }
                // 点击提交之后不管成不成功都更换验证码图片
                $('#kaptcha-img').click()
            }
        });
    });
});
