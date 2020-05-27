package cn.chm.o2o.web.frontend;

import cn.chm.o2o.entity.HeadLine;
import cn.chm.o2o.entity.PersonInfo;
import cn.chm.o2o.entity.ShopCategory;
import cn.chm.o2o.enums.HeadLineStateEnum;
import cn.chm.o2o.enums.ShopCategoryStateEnum;
import cn.chm.o2o.service.HeadLineService;
import cn.chm.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 响应主页请求的controller
 */
@Controller
@RequestMapping("/frontend")
public class MainPageController {

    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private HeadLineService headLineService;

    /**
     * 初始化前端的主页信息，包括获取一级店铺类别列表和头条列表
     * @return
     */
    @RequestMapping(value = "/listmainpageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listMainPageInfo() {
        Map<String, Object> modelMap = new HashMap<>();
        // 接收一级店铺列表
        List<ShopCategory> shopCategoryList = new ArrayList<>();
        try {
            // 接收一级店铺列表
            shopCategoryList = shopCategoryService.getShopCategoryList(null);
            modelMap.put("shopCategoryList", shopCategoryList);
        } catch (Exception e) {
            e.printStackTrace();
            // 失败了就返回一个ShopCategoryStateEnum失败的构造
            ShopCategoryStateEnum shopCategoryStateEnum = ShopCategoryStateEnum.INNER_ERROR;
            modelMap.put("success", false);
            modelMap.put("errMsg", shopCategoryStateEnum.getStateInfo());
            return modelMap;
        }
        // 接收头条列表
        List<HeadLine> headLineList = new ArrayList<>();
        try {
            // 接收头条列表
            HeadLine headLineCondition = new HeadLine();
            // 只接收可用的头条，0不可用 1可用
            headLineCondition.setEnableStatus(1);
            headLineList = headLineService.getHeadLineList(headLineCondition);
            modelMap.put("headLineList", headLineList);
        } catch (Exception e) {
            e.printStackTrace();
            HeadLineStateEnum headLineStateEnum = HeadLineStateEnum.INNER_ERROR;
            modelMap.put("success", false);
            modelMap.put("errMsg", headLineStateEnum.getStateInfo());
            return modelMap;
        }
        modelMap.put("success", true);
        return modelMap;
    }

}
