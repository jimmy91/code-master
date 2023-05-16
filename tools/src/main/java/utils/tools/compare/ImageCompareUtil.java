package utils.tools.compare;

import org.imgscalr.Scalr;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Feature2D;
import org.opencv.features2d.ORB;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jimmy
 */
public class ImageCompareUtil {

    static {
        URL url = ClassLoader.getSystemResource("lib/opencv_java460.dll");
        System.load(url.getPath());
    }

    /**
     * 测试openCv 安装是否成功
     */
    public static void openImShow(){
        Mat imread = Imgcodecs.imread("C:\\Users\\PLXQ\\Desktop\\product\\G07\\1 (1).jpg");
        HighGui.imshow("opencv测试", imread);

        HighGui.waitKey();
        HighGui.destroyAllWindows();

    }


    public static void main(String[] args) throws IOException {

        //openImShow();

        String path1;
        String path2;


        path1 = "C:\\Users\\PLXQ\\Desktop\\product\\G07\\1 (6).jpg";
        path2 = "C:\\Users\\PLXQ\\Desktop\\product\\G07\\1 (3).jpg";
        //compareImages(path1, path2);
        //compareImages02(path1, path2);

        path1 = "C:\\Users\\PLXQ\\Desktop\\product\\G07\\1 (3).jpg";
        path2 = "C:\\Users\\PLXQ\\Desktop\\product\\G07\\1 (3).jpg";
        compareImages(path1, path2);
        compareImages02(path1, path2);

/*
        path1 = "C:\Users\PLXQ\Desktop\product\G07\\1 (2).jpg";
        path2 = "C:\Users\PLXQ\Desktop\product\G07\\1 (5).jpg";
        compareImages02(path1, path2);


        path1 = "C:\Users\PLXQ\Desktop\product\G07\\1 (2).jpg";
        path2 = "C:\Users\PLXQ\Desktop\product\G07\\1 (6).jpg";
        compareImages02(path1, path2);



        path1 = "C:\Users\PLXQ\Desktop\product\G07\\1 (3).jpg";
        path2 = "C:\Users\PLXQ\Desktop\product\G07\\1 (7).jpg";
        compareImages02(path1, path2);


        path1 = "C:\Users\PLXQ\Desktop\product\G07\\1 (4).jpg";
        path2 = "C:\Users\PLXQ\Desktop\product\G07\\1 (8).jpg";
        compareImages02(path1, path2);
*/



        // compareImages(path1, path2);

    }
    private static double compareImages(String path1, String path2) throws IOException {

        BufferedImage img1 = ImageIO.read(new File(path1));
        BufferedImage img2 = ImageIO.read(new File(path2));
        int targetSize = 200;
        img1 = Scalr.resize(img1, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, targetSize, targetSize);
        img2 = Scalr.resize(img2, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, targetSize, targetSize);

        // 将图片转换为灰度图像
        img1 = Scalr.apply(img1, Scalr.OP_GRAYSCALE);
        img2 = Scalr.apply(img2, Scalr.OP_GRAYSCALE);


        double score = 0.0;
        double totalPixelCount = img1.getWidth() * img1.getHeight();
        for (int y = 0; y < img1.getHeight(); y++) {
            for (int x = 0; x < img1.getWidth(); x++) {
                int p1 = img1.getRGB(x, y);
                int p2 = img2.getRGB(x, y);
                int r1 = (p1 >> 16) & 0xff;
                int g1 = (p1 >> 8) & 0xff;
                int b1 = p1 & 0xff;
                int r2 = (p2 >> 16) & 0xff;
                int g2 = (p2 >> 8) & 0xff;
                int b2 = p2 & 0xff;
                double distance = Math.sqrt(Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2));
                score += (1 - (distance / 441.67));
            }
        }
        double similarity = (score / totalPixelCount) * 100;
        System.out.println("图片相似度：" + similarity);
        return similarity;
    }



    // --------------------
    static int threshold  = 50;
    private static void compareImages02(String path1, String path2) {
        // 1.读取需要比对的两张图片，并将其转化为灰度图
        Mat img1 = Imgcodecs.imread(path1);
        Mat img2 = Imgcodecs.imread(path2);

        int targetWidth = 800;  // 目标宽度
        int targetHeight = 800; // 目标高度
        // 调整图像大小
        Imgproc.resize(img1, img1, new Size(targetWidth, targetHeight));
        Imgproc.resize(img2, img2, new Size(targetWidth, targetHeight));

        // 颜色设置成灰白色
        Imgproc.cvtColor(img1, img1, Imgproc.COLOR_RGB2GRAY);
        Imgproc.cvtColor(img2, img2, Imgproc.COLOR_RGB2GRAY);

        // 创建 ORB 特征提取器
        Feature2D orb = ORB.create();

        // 2.使用ORB算法提取图片的关键点及特征描述子
        // 检测关键点和计算描述子
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        Mat descriptors1 = new Mat();
        Mat descriptors2 = new Mat();
        orb.detectAndCompute(img1, new Mat(), keypoints1, descriptors1);
        orb.detectAndCompute(img2, new Mat(), keypoints2, descriptors2);

        // 3. 使用BFMatcher算法进行特征匹配
        List<MatOfDMatch> matchesList = new LinkedList<>();
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
        // 使用 knnMatch 匹配方式，可以根据最近邻匹配的距离进行筛选，以获取更精准的匹配结果
        matcher.knnMatch(descriptors1, descriptors2, matchesList, 2);


        // 应用比值测试，筛选最佳匹配
        float ratioThreshold = 0.5f;
        List<MatOfDMatch> goodMatches = new ArrayList<>();
        for (MatOfDMatch knnMatch : matchesList) {
            if (knnMatch.rows() > 1) {
                float distances = knnMatch.toArray()[0].distance;
                if (distances <= ratioThreshold * distances) {
                    goodMatches.add(knnMatch);
                }
            }
        }

        // 计算相似度
        double similarity = (double) goodMatches.size() / keypoints1.rows();

        // 判断是否为同一款产品

        System.out.println("图片特征相似度："+similarity * 100);


        // 4. 根据匹配结果进行相似度计算
        LinkedList<DMatch> goodMatchesList = new LinkedList<>();
        for (int i = 0; i < matchesList.size(); i++) {
            MatOfDMatch matofDMatch = matchesList.get(i);
            DMatch[] dmatcharray = matofDMatch.toArray();
            DMatch m1 = dmatcharray[0];
            DMatch m2 = dmatcharray[1];
            //System.out.println("距离比值：" + (m1.distance / m2.distance));
            if (m1.distance < 2 * ratioThreshold * m2.distance) {
                goodMatchesList.addLast(m1);
            }
        }

        // 计算相似度
        double similarity1 = (double)goodMatchesList.size() / (double) keypoints1.rows();

        System.out.println("图片特征相似度："+similarity1 * 100);

    }

}