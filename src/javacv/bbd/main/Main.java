package javacv.bbd.main;

import javacv.bbd.detection.NumberPlateDetection;
import javacv.bbd.filters.MorphFilters;
import javacv.bbd.processing.PreProcessing;
import org.bytedeco.javacpp.opencv_core.*;

import static org.bytedeco.javacpp.opencv_core.bitwise_not;
import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;
import static org.bytedeco.javacpp.opencv_imgproc.blur;
import static org.bytedeco.javacpp.opencv_imgproc.equalizeHist;
import static org.bytedeco.javacpp.opencv_imgproc.invertAffineTransform;

/**
 * Created by nevyt on 5/18/2017.
 */
public class Main {

    public static void main(String[] args) {
        MorphFilters morphFilters = new MorphFilters();
        String testImagePath = "C:\\Users\\nevyt\\Desktop\\BBD project JAVA\\JAVA ANPR BBD\\Resources\\Input\\IMG_20170505_134514.jpg";
        String testImagePath2 = "C:\\Users\\nevyt\\Desktop\\BBD project JAVA\\JAVA ANPR BBD\\Resources\\Input\\IMG_20170505_134455.jpg";
        String testImagePath3 = "C:\\Users\\nevyt\\Desktop\\BBD project JAVA\\JAVA ANPR BBD\\Resources\\Input\\IMG_20170505_134505.jpg";
        String testImagePath4 = "C:\\Users\\nevyt\\Desktop\\BBD project JAVA\\JAVA ANPR BBD\\Resources\\Input\\IMG_20170505_134715.jpg";
        String testImagePath5 = "C:\\Users\\nevyt\\Desktop\\BBD project JAVA\\JAVA ANPR BBD\\Resources\\Input\\mazda.jpg";
        Mat testImage = imread(testImagePath);
        Mat testImage2 = imread(testImagePath2);
        Mat testImage3 = imread(testImagePath3);
        Mat testImage4 = imread(testImagePath4);
        Mat testImage5 = imread(testImagePath5);

//        PreProcessing.algorithm1(testImage3);
//
//        PreProcessing.algorithm2(testImage3);
//
//        PreProcessing.algorithm3(testImage3);

//        NumberPlateDetection.findNumberPlateContours(testImage5);
        NumberPlateDetection.extractPlate(NumberPlateDetection.findNumberPlateContours(testImage2));








    }
}
