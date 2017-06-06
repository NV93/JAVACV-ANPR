package javacv.bbd.filters;

import org.bytedeco.javacpp.opencv_core.*;

import static org.bytedeco.javacpp.opencv_core.BORDER_DEFAULT;
import static org.bytedeco.javacpp.opencv_core.CV_16S;
import static org.bytedeco.javacpp.opencv_core.absdiff;
import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_highgui.resizeWindow;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * Created by nevyt on 5/18/2017.
 */
public class MorphFilters {



    public static Mat rgbToGray(Mat image){
        Mat imageOut = new Mat();
        cvtColor(image,imageOut,CV_BGR2GRAY);
//        imshow("GRAY",imageOut);
//        waitKey(-1);
        return imageOut;
    }

    public static Mat gaussian(Mat image){
        Mat imageOut = new Mat();
        GaussianBlur(image,imageOut,new Size(15,15),0);
        return imageOut;
    }

    public static Mat erosion(Mat image){
        int erosionSize = 3;
        Mat element = getStructuringElement( MORPH_RECT,
                new Size( 2*erosionSize + 1, 2*erosionSize+1 ),new Point(erosionSize,erosionSize));
        Mat imageOut = new Mat();
        erode(image,imageOut,element);
        return imageOut;
    }

    public static Mat dilation(Mat image){
        int dilationSize = 3;
        Mat element = getStructuringElement( MORPH_RECT,
                new Size( 2*dilationSize + 1, 2*dilationSize+1 ),new Point(dilationSize,dilationSize));
        Mat imageOut = new Mat();
        dilate(image,imageOut,element);
        return imageOut;
    }

    public static Mat blackHat(Mat image){
        int morphSize = 3;
        Mat imageOut = new Mat();
        Mat element = getStructuringElement(MORPH_ELLIPSE, new Size(2*morphSize + 1, 2*morphSize +1));
        morphologyEx(image,imageOut,MORPH_BLACKHAT,element);
        return imageOut;
    }

    public static Mat thresholdOtsu(Mat image){
        Mat imageOut = new Mat();
        threshold(image,imageOut,74,255,CV_THRESH_BINARY);

        return imageOut;
    }

    public static Mat sobelH(Mat image){

        Mat imageOut = new Mat();
        Sobel(image, imageOut, -1,0 , 1); // 0 1
        return imageOut;
    }

    public static Mat sobelV(Mat image){

        Mat imageOut = new Mat();
        Sobel(image, imageOut, -3, 1, 0); // 1 0
        return imageOut;
    }

    public static Mat canny(Mat image, int size){
        Mat imageOut = new Mat();
        Canny(image,imageOut,size,size*3,3,true);
        return imageOut;
    }

    public static Mat flood(Mat image){

        floodFill(image,new Point(0,0),new Scalar(255));
        return image;
    }

    public static Mat closing(Mat image,int morphSize){
        Mat imageOut = new Mat();
        Mat element = getStructuringElement(MORPH_RECT,new Size(2*morphSize + 1, 2*morphSize +1), new Point(morphSize,morphSize));
        morphologyEx(image,imageOut,MORPH_CLOSE,element);
        return imageOut;
    }

    public static Mat opening(Mat image,int morphSize){
        Mat imageOut = new Mat();
        Mat element = getStructuringElement(MORPH_ELLIPSE,new Size(2*morphSize + 1, 2*morphSize +1));
        morphologyEx(image,imageOut,MORPH_OPEN,element);
        //resizeWindow("Opening",1920,1080);
        //imshow("Opening",imageOut);
        //waitKey(-1);
        return imageOut;
    }
    public static Mat subtract(Mat image1, Mat image2){
        Mat imageOut1 = new Mat();
        Mat imageOut2 = new Mat();
        Mat imageOut = new Mat();
        absdiff(imageOut2,imageOut1,imageOut);;
        return imageOut;


    }

    public static Mat topHat(Mat image){
        int morphSize = 24;
        Mat imageOut = new Mat();
        Mat element = getStructuringElement(MORPH_ELLIPSE,new Size(2*morphSize + 1, 2*morphSize +1));
        morphologyEx(image,imageOut,MORPH_TOPHAT,element);
        return imageOut;
    }

    public static Mat adaptiveThresh(Mat image) {
        Mat imageOut = new Mat();
        adaptiveThreshold(image,imageOut,255,ADAPTIVE_THRESH_MEAN_C,CV_THRESH_BINARY_INV,59,1);
        return imageOut;
    }




}
