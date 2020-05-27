package cn.chm.o2o.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 用来处理图片的工具类
 */
public class ImageUtil {

    // 获取classpath的绝对值路径
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    // 用来生成图片名的日期格式
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();
    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 用来处理商店主图缩略图(thumbnail)
     *
     * @param thumbnail  用户上传的图片流
     * @param targetAddr 要存储的路径
     * @return
     */
    public static String generateThumbnail(CommonsMultipartFile thumbnail, String targetAddr) {
        // 由于用户上传的图片名字不一致，这里统一成系统自动生成的不重名文件名
        String realFileName = getRandomFileName();
        // 获取用户上传文件的扩展名
        String extension = getFileExtension(thumbnail);
        // 创建商店图片存储目录
        makeDirPath(targetAddr);
        // 图片的相对路径
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is: " + relativeAddr);
        // 新的文件的绝对路径
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is: " + PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("basePath is：" + basePath);
        // 创建缩略图
        try {
            Thumbnails.of(thumbnail.getInputStream()).size(200, 200).watermark(Positions.BOTTOM_RIGHT,
                    ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
                    .outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 批量处理储存图片，返回地址列表
     *
     * @param imgs
     * @param targetAddr
     * @return
     */
    public static List<String> generateNormalImgs(List<CommonsMultipartFile> imgs, String targetAddr) {
        int count = 0;
        List<String> relativeAddrList = new ArrayList<>();
        if (imgs != null && imgs.size() > 0) {
            makeDirPath(targetAddr);
            for (CommonsMultipartFile img : imgs) {
                // 由于用户上传的图片名字不一致，这里统一成系统自动生成的不重名文件名
                String realFileName = getRandomFileName();
                // 获取用户上传文件的扩展名
                String extension = getFileExtension(img);
                // 图片的相对路径
                String relativeAddr = targetAddr + realFileName + count + extension;
                logger.debug("current relativeAddr is: " + relativeAddr);
                // 新的文件的绝对路径
                File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
                count++;
                logger.debug("current complete addr is: " + PathUtil.getImgBasePath() + relativeAddr);
                logger.debug("basePath is：" + basePath);
                // 创建缩略图
                try {
                    Thumbnails.of(img.getInputStream()).size(600, 300).outputQuality(0.5f).toFile(dest);
                } catch (IOException e) {
                    throw new RuntimeException("图片创建失败" + e.toString());
                }
                relativeAddrList.add(relativeAddr);
            }
        }
        return relativeAddrList;
    }

    /**
     * 处理缩略图
     *
     * @param thumbnail
     * @param dest
     * @return
     */
    public static String generateNormalImg(CommonsMultipartFile thumbnail, String targetAddr) {
        String realFileName = getRandomFileName();
        String extension = getFileExtension(thumbnail);
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnail.getInputStream()).size(600, 300).outputQuality(0.5f).toFile(dest);
        } catch (IOException e) {
            throw new RuntimeException("创建缩略图失败：" + e.toString());
        }
        return relativeAddr;
    }


    /**
     * 创建目标路径上所涉及到的目录
     *
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {

        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        // 判断路径是否存在
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    /**
     * 获取输入文件流的扩展名.jpg .png
     *
     * @param thumbnail
     * @param cFile
     * @return
     */
    private static String getFileExtension(CommonsMultipartFile cFile) {
        String originalFileName = cFile.getOriginalFilename();
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    /**
     * 随机生成一个文件名字，要求不能重复生成
     * 这里使用当前年月日小时分钟秒钟+五位随机数的形式来生成
     *
     * @return 文件名
     */
    private static String getRandomFileName() {
        // 获取随机五位数
        int rannum = r.nextInt(89999) + 10000;
        // 获取当前时间字符串
        String nowTimeStr = simpleDateFormat.format(new Date());
        return nowTimeStr + rannum;
    }

    /**
     * 删除文件或者目录
     *
     * @param storePath 如果storePath是文件目录则删除该文件，是目录路径就删除目录下所有文件
     */
    public static void deleteFileOrPath(String storePath) {
        File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
        if (fileOrPath.exists()) {
            // 1.目录
            if (fileOrPath.isDirectory()) {
                File[] files = fileOrPath.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            // 2.文件 （如果是目录这一步把文件夹也会删掉）
            fileOrPath.delete();
        }
    }

}
