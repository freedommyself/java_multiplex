package org.java.multiplex.common.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * Java生成二维码的方式有如下几种：
 * 1：使用SwetakeQRCode在Java项目中生成二维码
 * 2：使用BarCode4j生成条形码和二维码
 * 3：zxing
 * 4：google chart api就有实现二维码的方法
 * 5：JS生成二维码
 *
 * @author wangpeng
 * @version 1.0
 * @description zxing 二维码生成与解析
 * @create_time 2020/7/17 0017 13:42:06
 */
public class QRCodeUtil {

    /**
     * CHARSET      :编码方式
     * FORMAT_NAME  :图片后缀
     * QR_CODE_SIZE :二维码尺寸
     * WIDTH        :LOGO宽度
     * HEIGHT       :LOGO高度
     */
    private static final String CHARSET = "UTF-8";

    private static final String FORMAT_NAME = "JPG";

    private static final int QR_CODE_SIZE = 300;

    private static final int WIDTH = 60;

    private static final int HEIGHT = 60;

    /**
     * 创建二维码BufferedImage对象
     *
     * @param content      存放在二维码中的内容
     * @param imgPath      嵌入二维码的图片路径
     * @param needCompress 图片是否压缩
     * @return 生成的二维码BufferedImage
     * @throws WriterException MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QR_CODE_SIZE,
     *                         QR_CODE_SIZE,hints);
     * @throws IOException     QRCodeUtil.insertImage(image, imgPath, needCompress);
     */
    private static BufferedImage createImage(String content, String imgPath, boolean needCompress)
            throws WriterException, IOException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE,
                hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (imgPath == null || "".equals(imgPath)) {
            return image;
        }
        QRCodeUtil.insertImage(image, imgPath, needCompress);
        return image;
    }

    /**
     * 插入图片到二维码
     * 压缩LOGO
     * 绘制缩小后的图
     * 插入LOGO
     *
     * @param source       二维码BufferedImage对象
     * @param imgPath      嵌入二维码的图片路径
     * @param needCompress 图片是否压缩
     * @throws IOException ImageIO.read(new File(imgPath))
     */
    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws IOException {
        File file = new File(imgPath);
        if (!file.exists()) {
            throw new RuntimeException("" + imgPath + "   该文件不存在！");
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) {
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            src = image;
        }
        Graphics2D graph = source.createGraphics();
        int x = (QR_CODE_SIZE - width) / 2;
        int y = (QR_CODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 生成嵌入图片的二维码
     *
     * @param content      存放在二维码中的内容
     * @param imgPath      嵌入二维码的图片路径
     * @param destPath     生成的二维码的路径及名称
     * @param needCompress 图片是否压缩
     * @throws IOException     QRCodeUtil.createImage(content, imgPath, needCompress)
     *                         &ImageIO.write(image, FORMAT_NAME, new File(destPath))
     * @throws WriterException QRCodeUtil.createImage(content, imgPath, needCompress)
     */
    public static void encode(String content, String imgPath, String destPath, boolean needCompress) throws IOException,
            WriterException {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        mkdirs(destPath);
        // String file = new Random().nextInt(99999999)+".jpg";
        // ImageIO.write(image, FORMAT_NAME, new File(destPath+"/"+file));
        ImageIO.write(image, FORMAT_NAME, new File(destPath));
    }

    /**
     * 生成不含图片的纯二维码
     *
     * @param content  存放在二维码中的内容
     * @param destPath 生成的二维码的路径及名称
     * @throws IOException,WriterException QRCodeUtil.encode(content, null, destPath, false)
     */
    public static void encode(String content, String destPath) throws IOException, WriterException {
        QRCodeUtil.encode(content, null, destPath, false);
    }

    /**
     * 创建文件夹
     * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
     *
     * @param destPath 生成的二维码的路径及名称
     */
    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    /**
     * 生成嵌入图片的二维码
     *
     * @param content      存放在二维码中的内容
     * @param imgPath      嵌入二维码的图片路径
     * @param output       生成的二维码的存放路径的流
     * @param needCompress 图片是否压缩
     * @throws IOException     QRCodeUtil.createImage(content, imgPath, needCompress)
     *                         &ImageIO.write(image, FORMAT_NAME, output)
     * @throws WriterException QRCodeUtil.createImage(content, imgPath, needCompress)
     */
    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress)
            throws IOException, WriterException {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * 生成不含图片的纯二维码
     *
     * @param content 存放在二维码中的内容
     * @param output  生成的二维码的存放路径的流
     * @throws IOException,WriterException QRCodeUtil.encode(content, null, destPath, false)
     */
    public static void encode(String content, OutputStream output) throws IOException, WriterException {
        QRCodeUtil.encode(content, null, output, false);
    }

    /**
     * 解析二维码
     *
     * @param file 二维码文件
     * @return 解码后的内容
     * @throws IOException       ImageIO.read(file)
     * @throws NotFoundException MultiFormatReader().decode(bitmap, hints);
     */
    public static String decode(File file) throws IOException, NotFoundException {
        BufferedImage image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        Result result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }

    /**
     * 解析二维码
     *
     * @param path 二维码存放路径
     * @return 解码后的内容
     * @throws IOException       ImageIO.read(file)
     * @throws NotFoundException MultiFormatReader().decode(bitmap, hints);
     */
    public static String decode(String path) throws IOException, NotFoundException {
        return QRCodeUtil.decode(new File(path));
    }

}
