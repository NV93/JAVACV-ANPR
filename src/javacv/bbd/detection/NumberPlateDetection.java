package javacv.bbd.detection;

import javacv.bbd.filters.MorphFilters;
import javacv.bbd.main.Utils;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;

import java.util.ArrayList;

import static org.bytedeco.javacpp.opencv_core.CV_8UC3;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgproc.boundingRect;
import static org.bytedeco.javacpp.opencv_imgproc.drawContours;

/**
 * Created by nevyt on 5/24/2017.
 */
public class NumberPlateDetection {

    public static ArrayList<Rect> findNumberPlateContours(Mat image, int areaSize) {
        MorphFilters morphFilters = new MorphFilters();
        MatVector contours = new MatVector();
        Mat hierarchy = new Mat();
        Mat gray = morphFilters.rgbToGray(image);
        Mat blur = morphFilters.gaussian(gray);
        Mat canny = morphFilters.canny(blur, 24);
        findContours(canny, contours, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);
        System.out.println("Contour size:" + contours.size());
        Utils.showImage(canny, "CANNY");
        // Create a black mask
        Mat drawing = new Mat();
        drawing.put(Mat.zeros(canny.size(), CV_8UC3));

        // FINDING AND DRAWING A RECTANGLE OF PROVIDED AREA SIZE
        Scalar color = new Scalar(255, 255);
        MatVector contourVector = new MatVector(contours);
        ArrayList<Rect> listOfMasks = new ArrayList<>();
        Rect mask = null;
        for (int i = 0; i < contours.size(); i++) {
            double contourAreaSize = contourArea(contours.get(i));

            if (contourAreaSize >= areaSize) {

                System.out.println("CONTOUR " + i + " FITS");
//                Rect bound = boundingRect(contours.get(i));
                drawContours(gray, contourVector, i, color, 10, 8, new Mat(), CV_FILLED, new opencv_core.Point());
                Utils.showImage(gray, "THE ONE");
                mask = boundingRect(contours.get(i));
                listOfMasks.add(mask);
            }

        }

//        System.out.println("X :" + bounding.x() + " Y: " + bounding.y());
//        System.out.println("Height: " + bounding.height() + " Width: " + bounding.width());
        Mat image_roi = new Mat(gray, mask);
        Utils.showImage(image_roi, "CROP?");


//        System.out.println(contours.get(510).);
        return listOfMasks;
    }

    public static void extractPlate(Mat image,Rect mask){
        MorphFilters morphFilters = new MorphFilters();
        Mat croppedImage = new Mat(image,mask);
        Mat detectedEdges = morphFilters.canny(croppedImage, 24);
        findNumberPlateContours(detectedEdges,2100);

    }

}
