package javacv.bbd.detection;

import javacv.bbd.filters.MorphFilters;
import javacv.bbd.main.Utils;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;

import static org.bytedeco.javacpp.opencv_core.CV_8UC3;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgproc.boundingRect;
import static org.bytedeco.javacpp.opencv_imgproc.drawContours;

/**
 * Created by nevyt on 5/24/2017.
 */
public class NumberPlateDetection {

    public static Mat findNumberPlateContours(Mat image) {
        MorphFilters morphFilters = new MorphFilters();
        MatVector contours = new MatVector();
        Mat hierarchy = new Mat();
        Mat gray = morphFilters.rgbToGray(image);
        Mat blur = morphFilters.gaussian(gray);
        Mat canny = morphFilters.canny(blur, 24);
        findContours(canny, contours, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);
        System.out.println("Contour size:" + contours.size());

        // Create a black mask
        Mat drawing = new Mat();
        drawing.put(Mat.zeros(canny.size(), CV_8UC3));

        // FINDING AND DRAWING A RECTANGLE OF PROVIDED AREA SIZE
        Scalar color = new Scalar(255, 255);
        MatVector contourVector = new MatVector(contours);
        for (int i = 0; i < contours.size(); i++) {
            double contourAreaSize = contourArea(contours.get(i));
            if(contourAreaSize>=8000){
//                Rect bound = boundingRect(contours.get(i));
                drawContours(gray,contourVector,i,color,10,8,new Mat(),CV_FILLED,new opencv_core.Point());
                Utils.showImage(gray,"THE ONE");
            }

        }
        return image;
    }

    public static void extractPlate(Mat image){
        Mat crop = new Mat(image.rows(),image.cols(),CV_8UC3);
        Mat color = new Mat(new Scalar(255,255));
        Mat mask = new Mat();
        mask.put(Mat.zeros(image.size(), CV_8UC3));
        crop.setTo(color);
        image.copyTo(crop,mask);
        Utils.showImage(image,"ORIGI");
        Utils.showImage(mask,"MASK");
        Utils.showImage(crop,"CROP");
    }
}
