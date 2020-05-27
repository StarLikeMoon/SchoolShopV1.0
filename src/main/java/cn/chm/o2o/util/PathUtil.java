package cn.chm.o2o.util;

/**
 * 用来提供路径的工具类
 */
public class PathUtil {
    // 系统的路径格式，分隔符格式
    private static String separator = System.getProperty("file.separator");

    /**
     * 返回项目图片根目录，根据系统自动返回
     * @return
     */
    public static String getImgBasePath() {
        // 获取操作系统
        String os = System.getProperty("os.name");
        String basePath = "";
        if (os.toLowerCase().startsWith("win")) {
            basePath = "F:/Envs/projectdev/image";
        } else {
            basePath = "/User/chm/image";
        }
        basePath = basePath.replace("/", separator);
        return basePath;
    }

    /**
     * 获取店铺图片的子路径
     * @param shopId
     */
    public static String getShopImagePath(long shopId) {
        String imagePath = "/upload/item/shop/" + shopId + "/";
        return imagePath.replace("/", separator);
    }

    /**
     * 获取店铺分类的子路径
     * @return
     */
    public static String getShopCategoryImagePath() {
        String imagePath = "upload/item/shopcategory/";
        return imagePath.replace("/", separator);
    }

    /**
     * 获取个人信息的子路径
     * @return
     */
    public static String getPersonInfoImagePath() {
        String imagePath = "upload/item/personinfo/";
        return imagePath.replace("/", separator);
    }
}
