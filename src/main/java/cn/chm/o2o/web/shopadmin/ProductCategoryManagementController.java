package cn.chm.o2o.web.shopadmin;

import cn.chm.o2o.dto.ProductCategoryExecution;
import cn.chm.o2o.dto.ProductExecution;
import cn.chm.o2o.dto.Result;
import cn.chm.o2o.entity.Product;
import cn.chm.o2o.entity.ProductCategory;
import cn.chm.o2o.entity.Shop;
import cn.chm.o2o.enums.ProductCategoryStateEnum;
import cn.chm.o2o.service.ProductCategoryService;
import cn.chm.o2o.service.ProductService;
import cn.chm.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品类别的管理
 */
@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 获取商铺的商品分类列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
    @ResponseBody
    private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {
        // 从session中获取到店铺id
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        List<ProductCategory> productCategoryList = null;
        if (currentShop != null && currentShop.getShopId() > 0) {
            // 能正常拿到shopId
            productCategoryList = productCategoryService.getProductCategoryList(currentShop.getShopId());
            return new Result<List<ProductCategory>>(true, productCategoryList);
        } else {
            // 拿不到shopId
            ProductCategoryStateEnum productCategoryStateEnum = ProductCategoryStateEnum.INNER_ERROR;
            return new Result<List<ProductCategory>>(false, productCategoryStateEnum.getStateInfo(),
                    productCategoryStateEnum.getState());
        }
    }

    /**
     * 批量添加商品分类
     *
     * @param productCategoryList
     * @param request
     * @return
     */
    @RequestMapping(value = "/addproductcategorys", method = RequestMethod.POST)
    private Map<String, Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,
                                                    HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //从session中获取到shop的信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 空值判断
        if (productCategoryList != null && productCategoryList.size() > 0) {
            // 把shopId赋值进去
            for (ProductCategory productCategory : productCategoryList) {
                productCategory.setShopId(currentShop.getShopId());
                productCategory.setCreateTime(new Date());
            }
            try {
                ProductCategoryExecution productCategoryExecution
                        = productCategoryService.batchAddProductCategory(productCategoryList);
                if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    // 添加成功
                    modelMap.put("success", true);
                } else {
                    // 添加失败
                    modelMap.put("success", false);
                    modelMap.put("errMsg", productCategoryExecution.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            // 传的值不合法
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入至少一个商品类别");
        }
        return modelMap;
    }

    /**
     * 删除指定商品的分类信息
     *
     * @param productCategoryId
     * @param request
     * @return
     */
    @RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> removeProductCategory(Long productCategoryId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (productCategoryId != null && productCategoryId > 0) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                ProductCategoryExecution productCategoryExecution
                        = productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
                if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", productCategoryExecution.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请选择要删除的商品");
        }
        return modelMap;
    }


}
