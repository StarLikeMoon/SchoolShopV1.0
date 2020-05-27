package cn.chm.o2o.web.shopadmin;

import cn.chm.o2o.dao.PersonInfoDao;
import cn.chm.o2o.dto.ShopExecution;
import cn.chm.o2o.entity.Area;
import cn.chm.o2o.entity.PersonInfo;
import cn.chm.o2o.entity.Shop;
import cn.chm.o2o.entity.ShopCategory;
import cn.chm.o2o.enums.ShopStateEnum;
import cn.chm.o2o.service.AreaService;
import cn.chm.o2o.service.ShopCategoryService;
import cn.chm.o2o.service.ShopService;
import cn.chm.o2o.util.CodeUtil;
import cn.chm.o2o.util.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 商家店铺管理的控制层
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementContorller {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private PersonInfoDao personInfoDao;

    /**
     * 管理商家商铺的session的控制层
     * +
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getshopmanagementinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 获取到商家点击的哪个店铺id
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId <= 0) {
            // 如果前端没有传，就尝试直接从session中获取
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            if (currentShopObj == null) {
                // 如果确实拿不到，就重定向回商铺列表页
                modelMap.put("redirect", true);
                modelMap.put("url", "O2OShop/shopadmin/getshoplist");
            } else {
                // 拿到了就将shopId返回
                Shop currentShop = (Shop) currentShopObj;
                modelMap.put("redirect", false);
                modelMap.put("shopId", currentShop.getShopId());
            }
        } else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop", currentShop);
            modelMap.put("redirect", false);
        }

        return modelMap;
    }

    /**
     * 根据商家的信息，获取商家的商铺列表，并传给前端
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        PersonInfo user = null;
        // 通过session获取商家的个人信息
        user = (PersonInfo) request.getSession().getAttribute("user");
        Shop shopCondition = new Shop();
        if (user != null && user.getUserId() != null) {
            try {
                shopCondition.setOwner(user);
                ShopExecution shopExecution = shopService.getShopList(shopCondition, 0, 100);
                modelMap.put("shoplist", shopExecution.getShopList());
                // 将当前商家的店铺列表存到session中
                request.getSession().setAttribute("shopList", shopExecution.getShopList());
                modelMap.put("user", user);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请先登录");
            return modelMap;
        }
    }

    /**
     * 获取商铺的区域信息和分类信息，并传给前端
     *
     * @return
     */
    @RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList = null;
        List<Area> areaList = null;
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaLsit();
            modelMap.put("success", true);
            modelMap.put("areaList", areaList);
            modelMap.put("shopCategoryList", shopCategoryList);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    /**
     * 注册店铺
     *
     * @param request 前端传回来的HttpServletRequest信息
     * @return 状态信息
     */
    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 核对验证码是否相同
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        // 1.接收并转化相应的参数，包括店铺信息以及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        // jackson-databind提供的类，可以将json对象包装成实体类
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            // 如果失败的话就将失败信息返回给前台
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        //Spring自带的处理上传图片的类
        CommonsMultipartResolver commonsMultipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest =
                    (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            // 如果没有图片就前端报错
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }
        // 2.注册店铺
        if (shop != null && shopImg != null) {
            // 从Session获取掌柜身份信息
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(owner);
            ShopExecution shopExecution = shopService.addShop(shop, shopImg);
            if (shopExecution.getState() == ShopStateEnum.CHECK.getState()) {
                modelMap.put("success", true);
                // 更新当前用户可以操作的店铺列表
                @SuppressWarnings("unchecked")
                List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                if (shopList == null || shopList.size() == 0) {
                    // 第一次创建店铺
                    shopList = new ArrayList<>();
                }
                shopList.add(shopExecution.getShop());
                request.getSession().setAttribute("shopList", shopList);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", shopExecution.getStateInfo());
            }
            return modelMap;
        } else {
            // 如果有空就前端报错
            modelMap.put("success", false);
            modelMap.put("errMsg", "店铺信息不完全");
            return modelMap;
        }
    }

    /**
     * 根据前端传回的shopId查到店铺所有信息返回给前端
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1) {
            try {
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaLsit();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            // shopId <= -1 获取失败
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }

    /**
     * 修改店铺
     *
     * @param request 前端传回来的HttpServletRequest信息
     * @return 状态信息
     */
    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 核对验证码是否相同
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        // 1.接收并转化相应的参数，包括店铺信息以及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        // jackson-databind提供的类，可以将json对象包装成实体类
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            // 如果失败的话就将失败信息返回给前台
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        MultipartHttpServletRequest multipartHttpServletRequest = null;
        //Spring自带的处理上传图片的类
        CommonsMultipartResolver commonsMultipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }
        // 2.修改店铺信息
        if (shop != null && shop.getShopId() != null) {
            ShopExecution shopExecution;
            if (shopImg == null) {
                shopExecution = shopService.modifyShop(shop, null);
            } else {
                shopExecution = shopService.modifyShop(shop, shopImg);
            }
            if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", shopExecution.getStateInfo());
            }
            return modelMap;
        } else {
            // 如果有空就前端报错
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
    }

}
