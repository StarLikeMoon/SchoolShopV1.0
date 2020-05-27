package cn.chm.o2o.dao;

import cn.chm.o2o.BaseTest;
import cn.chm.o2o.entity.HeadLine;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class HeadLineDaoTest extends BaseTest {

    @Autowired
    private HeadLineDao headLineDao;

    @Test
    public void testQueryHeadLine() {
        HeadLine headLine = new HeadLine();
        headLine.setEnableStatus(1);
        List<HeadLine> headLines = headLineDao.queryHeadLine(headLine);
        System.out.println(headLines.size());
    }

}
