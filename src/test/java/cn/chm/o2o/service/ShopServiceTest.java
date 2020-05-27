package cn.chm.o2o.service;

import cn.chm.o2o.BaseTest;
import cn.chm.o2o.dto.ShopExecution;
import cn.chm.o2o.entity.Area;
import cn.chm.o2o.entity.PersonInfo;
import cn.chm.o2o.entity.Shop;
import cn.chm.o2o.entity.ShopCategory;
import cn.chm.o2o.enums.ShopStateEnum;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopServiceTest extends BaseTest {
    @Autowired
    private ShopService shopService;

    @Test
    @Ignore
    public void testAddShop() throws IOException {
        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(1L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试service层添加店铺");
        shop.setShopDesc("test3");
        shop.setShopAddr("test3");
        shop.setPhone("15555555");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");
    }

    @Test
    @Ignore
    public void testModifyShop() {
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopName("测试修改店铺");
        ShopExecution shopExecution = shopService.modifyShop(shop, null);
        System.out.println(shopExecution.getState());
    }

    @Test
    public void testQueryShopList() {
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        shopCondition.setOwner(owner);
        ShopExecution shopList = shopService.getShopList(shopCondition, 1, 2);
        System.out.println(shopList.getShopList().size());
        System.out.println(shopList.getCount());
    }

}
