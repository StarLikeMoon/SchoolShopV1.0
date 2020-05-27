package cn.chm.o2o.util;

/**
 * 处理前端页码和数据库行转换的工具类
 */
public class PageCalculator {

    /**
     * 将前端的页数转换为去数据库取数据的行数
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static int calulateRowIndex(int pageIndex, int pageSize) {
        return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
    }

}
