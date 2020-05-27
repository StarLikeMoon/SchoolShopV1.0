package cn.chm.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 店铺管理页面的转发路由
 */
@Controller
@RequestMapping(value = "/shopadmin", method = {RequestMethod.GET})
public class ShopAdminController {

    /**
     * 转发到店铺添加/编辑页面
     * @return
     */
    @RequestMapping(value = "/shopoperation")
    public String shopOperation(){
        return "/shop/shopoperation";
    }

    /**
     * 转发到店铺列表页面
     * @return
     */
    @RequestMapping(value = "/shoplist")
    public String shopList(){
        return "/shop/shoplist";
    }

    /**
     * 转发到店铺管理页面
     * @return
     */
    @RequestMapping(value = "/shopmanagement")
    public String shopManagement(){
        return "/shop/shopmanagement";
    }

    /**
     * 转发到商品分类管理页面
     * @return
     */
    @RequestMapping(value = "/productcategorymanagement", method = RequestMethod.GET)
    public String productCategoryManagement(){
        return "/shop/productcategorymanagement";
    }

    /**
     * 转发到商品添加/编辑页面
     * @return
     */
    @RequestMapping(value = "/productoperation")
    public String productOperation(){
        return "/shop/productoperation";
    }

    /**
     * 转发到商家的商品管理页面
     * @return
     */
    @RequestMapping(value = "/productmanagement")
    public String productManagement(){
        return "/shop/productmanagement";
    }

}
