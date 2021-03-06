package javacv.bbd.processing;

import javacv.bbd.filters.MorphFilters;
import javacv.bbd.main.Utils;
import org.bytedeco.javacpp.opencv_core.*;

import static org.bytedeco.javacpp.opencv_core.bitwise_not;
import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * Created by nevyt on 5/24/2017.
 */
public class PreProcessing {

    public static Mat toThreshold(Mat image){
        MorphFilters morphFilters = new MorphFilters();
        Mat gray = morphFilters.rgbToGray(image);
        Mat blur = morphFilters.gaussian(gray);
        Mat thresh = morphFilters.thresholdOtsu(blur);
        Mat open = morphFilters.opening(thresh,5);


        return open;
    }

    public static Mat plateEdgeDetection(Mat image){
        MorphFilters morphFilters = new MorphFilters();
        Mat gray = morphFilters.rgbToGray(image);
        Mat blur = morphFilters.gaussian(gray);
        Mat canny = morphFilters.canny(blur, 25);

        return canny;
    }

    public static Mat characterEdgeDetection(Mat image){
        MorphFilters morphFilters = new MorphFilters();
        Mat gray = morphFilters.rgbToGray(image);
        Mat blur = morphFilters.gaussian(gray);
        Mat canny = morphFilters.canny(blur, 11);

        return canny;
    }


    public static Mat algorithm1(Mat image) {
        MorphFilters morphFilters = new MorphFilters();
        Mat grayscaleImage = morphFilters.rgbToGray(image);
        Mat blurredImage = morphFilters.gaussian(grayscaleImage);
        Mat whiteTopHat = morphFilters.topHat(grayscaleImage);
        Mat blackTopHat = morphFilters.blackHat(blurredImage);
        Mat adaptiveThresh = morphFilters.adaptiveThresh(blackTopHat);
//        Utils.showImage(adaptiveThresh,"ADAPTUOTAS");
//        morphFilters.showImage(morphFilters.closing(adaptiveThresh,3), "CLOSED");
//        Mat opened = morphFilters.opening(adaptiveThresh,5);
//        morphFilters.showImage(opened, "OPENED");
        Mat invertedOut = new Mat();
        bitwise_not(adaptiveThresh, invertedOut);
        Utils.showImage(invertedOut, "INVERTS");

//        Mat sobelH = morphFilters.sobelH(adaptiveThresh);
//        Utils.showImage(sobelH,"A1 sobel");
//        Mat sobelInv = morphFilters.sobelH(invertedOut);
//        Utils.showImage(sobelInv,"A1 Sobel INV");
        return invertedOut;

    }

    public static void algorithm2(Mat image) {
        MorphFilters morphFilters = new MorphFilters();
        Mat gray = morphFilters.rgbToGray(image);
        Mat blur = morphFilters.gaussian(gray);
        Mat dilation = morphFilters.dilation(blur);
        Mat thresh = morphFilters.adaptiveThresh(dilation);
        bitwise_not(thresh, thresh);
        Utils.showImage(thresh, "A2 THRESH");

    }

    public static void algorithm3(Mat image) {
//        MatOfInt4 lines = new MatOfInt4();
        MatVector contours = new MatVector();
//        MatOfPoint2f points = new MatOfPoint2f();

        MorphFilters morphFilters = new MorphFilters();
        Mat gray = morphFilters.rgbToGray(image);
        Mat blur = morphFilters.gaussian(gray);
        Mat sobelV = morphFilters.sobelV(blur);
        Mat adaptiveThresh = morphFilters.adaptiveThresh(sobelV);
        Mat open = morphFilters.opening(adaptiveThresh, 7);
        Mat close = morphFilters.closing(open, 9);
        Utils.showImage(close, "A3 CLOSE");
        findContours(close, contours, new Mat(), CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, new Point(1, 1));
//        RotatedRect who = minAreaRect(close);


    }
    public static void doErosion(Mat image){
        Mat gray = MorphFilters.rgbToGray(image);
        Mat erosion = MorphFilters.erosion(gray);
        Mat thresh = MorphFilters.adaptiveThresh(erosion);
        imwrite("erosion.jpg",thresh);
    }

    public static void doDilation(Mat image){
        Mat gray = MorphFilters.rgbToGray(image);
        Mat erosion = MorphFilters.dilation(gray);
        Mat thresh = MorphFilters.adaptiveThresh(erosion);
        imwrite("dilate.jpg",thresh);
    }

    public static void doOpen(Mat image){
        Mat gray = MorphFilters.rgbToGray(image);
        Mat thresh = MorphFilters.thresholdOtsu(gray);
        Mat open = MorphFilters.opening(thresh,5);
        imwrite("Opened.jpg",open);

    }

    public static void doClose(Mat image){
        Mat gray = MorphFilters.rgbToGray(image);
        Mat thresh = MorphFilters.thresholdOtsu(gray);
        Mat close = MorphFilters.closing(thresh,5);
        imwrite("Closed.jpg",close);

    }
}