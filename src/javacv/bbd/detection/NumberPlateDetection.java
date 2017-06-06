package javacv.bbd.detection;

import javacv.bbd.filters.MorphFilters;
import javacv.bbd.main.Utils;
import javacv.bbd.processing.PreProcessing;
import org.bytedeco.javacpp.opencv_core.*;

import java.util.ArrayList;

import static org.bytedeco.javacpp.opencv_core.CV_8UC3;
import static org.bytedeco.javacpp.opencv_core.hconcat;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgproc.boundingRect;
import static org.bytedeco.javacpp.opencv_imgproc.drawContours;

/**
 * Created by nevyt on 5/24/2017.
 */
public class NumberPlateDetection {



    public static MatVector getContours(Mat image) {
        System.out.println("GETTING CONTOURS ...");
        MorphFilters morphFilters = new MorphFilters();
        MatVector contours = new MatVector();
        Mat hierarchy = new Mat();
        Mat cannyEdges = PreProcessing.plateEdgeDetection(image);
        findContours(cannyEdges, contours, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE); // find all contours and store them into contours variable
        System.out.println("Contour size:" + contours.size());
//        Utils.showImage(cannyEdges, "CANNY");
        return contours;

    }

    public static ArrayList<Rect> generatePlateMasks(Mat image) {
        // Create a black mask
        System.out.println("GENERATING PLATE MASKS ...");
        MatVector contours = getContours(image);
        Mat canny = PreProcessing.plateEdgeDetection(image);
        Mat drawing = new Mat();
        drawing.put(Mat.zeros(canny.size(), CV_8UC3));                  // empty mask


        // FINDING AND DRAWING A RECTANGLE OF PROVIDED AREA SIZE                                                                                        ________________________________________
        Scalar color = new Scalar(255, 255);                     // border color
        MatVector contourVector = new MatVector(contours);              // saugomi konturu vektoriai
        ArrayList<Rect> listOfMasks = new ArrayList<>();                // mask'ai busimos numeriu (ar pagal plota panasios) vietos
        Rect rect = null;                                               // mask - tai plotas kuri noresime isgauti is esamos foto
        for (int i = 0; i < contours.size(); i++) {                     // ieskome visu konturu kurie atitinka salyga if'e
            double contourAreaSize = contourArea(contours.get(i));

            if (contourAreaSize >= 10000) {                             // salyga filtravimui (apytikslus numeriu plotas)

//                System.out.println("CONTOUR " + i + " FOR NUMBERPLATE FITS");
//                Rect bound = boundingRect(contours.get(i));
//                drawContours(
//                        image, contourVector, i, color, 10, 8, new Mat(), CV_FILLED, new opencv_core.Point()
//                );                                          // breziame filtra praejusius konturus
                rect = boundingRect(contours.get(i));                   // gautus konturus apibreziame kaip mask
                rectangle(image,rect.tl(),rect.br(),new Scalar(0,255));
//                Utils.showImage(image, "THE ONE " + i);
                System.out.println("THE AREA OF " + i + " IS: " + rect.area());
                listOfMasks.add(rect);                                  // sumetame mask'us i lista
            }

        }
        System.out.println("THE SIZE OF MASKS: " + listOfMasks.size());
        return listOfMasks;
    }

    public static Rect filterMasks(Mat image) {
        System.out.println("FILTERING MASKS ...");
        ArrayList<Rect> listOfMasks = generatePlateMasks(image);
        Rect sampleMask = null;
        int i = 1;
        for (Rect rect :
                listOfMasks) {
            System.out.println("RECT NR." + i + "height: " + rect.height());
            System.out.println("RECT NR." + i + "width: " + rect.width());
            i++;
            if (rect.height() > 110 && rect.height() < 400 && rect.width() > 350 || rect.width() < 1100) {
                return rect;
            }
            // height ribos 200-250 ~
            // width ribos 700-1100~
        }
        System.out.println("No suitable mask found");
        return sampleMask;
    }

    public static Mat extractPlate(Mat image) {
        System.out.println("EXTRACTING NUMBERPLATE ...");
        Rect mask = filterMasks(image);
        Mat imageCopy = new Mat();
        image.copyTo(imageCopy);
        Mat cutImage = new Mat(imageCopy, mask);                            // iskirptas paveiksliukas
        Utils.showImage(cutImage, "extracted number plate ");
        return cutImage;
    }

    public static ArrayList<Rect> getSymbolContours(Mat image){
//        Mat numberPlate = extractPlate(image);
        MatVector contours = new MatVector();
        Mat hierarchy = new Mat();
        Mat cannyEdges = PreProcessing.characterEdgeDetection(image);
        Utils.showImage(cannyEdges,"CANN OF PLATE");
        ArrayList<Rect> listOfMasks = new ArrayList<>();
        Rect rect = null;

        findContours(cannyEdges, contours, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE); // find all contours and store them into contours variable
        System.out.println("Plate contour size:" + contours.size());
        for(int i=0; i<contours.size();i++){
            System.out.println("Contour " + i + " area is: " + contourArea(contours.get(i)));
            if(contourArea(contours.get(i)) >=1650 && contourArea(contours.get(i)) <22000){
                rect = boundingRect(contours.get(i));
                System.out.println("RECT " + i + " WIDTH :" + rect.width() + " AND HEIGHT " + rect.height()); // 80 width ir hieght 120
                if(rect.width()>=51 && rect.height()>=82){
                    rectangle(image,rect.tl(),rect.br(), new Scalar(0,255));
                    listOfMasks.add(rect);
                }
//
            }
        }

        return listOfMasks;
    }
    public static ArrayList<Mat> extractCharacters(Mat image){
//        Mat plateImage = extractPlate(image);
        ArrayList<Rect> listOfMasks = getSymbolContours(image);
        Utils.showImage(image,"EXTRACTED PLATE IMAGE");
        Mat imageCopy = new Mat();
        ArrayList<Mat> characterImageList = new ArrayList<>();
        image.copyTo(imageCopy);
        int i = 0;
        for (Rect mask : listOfMasks) {
            Mat cutCharacter = new Mat(image,mask);
            characterImageList.add(i,cutCharacter);
            i++;
        }
        Utils.showImageList(characterImageList);
        System.out.println(characterImageList.size());
        return characterImageList;
    }





//    public static MatVector findSymbolContours(Mat image) {
//        System.out.println("FINDING SYMBOL CONTOURS ...");
//        Mat cutImage = extractPlate(image);
//        MatVector plateContours = getContours(cutImage);
//        System.out.println("THE SIZE: " + plateContours.size());
//        for (int i = 0; i < plateContours.size(); i++) {
//            System.out.println("The area of contour " + i + " is: " + contourArea(plateContours.get(i)));
//        }
//        return plateContours;
//    }
//
//    public static ArrayList<Rect> getCharacterMasks(Mat image) {
//        System.out.println("GETTING CHARACTER MASKS ...");
//        Mat plateImage = extractPlate(image);
//        MatVector contours = findSymbolContours(image);
//
//        // FINDING AND DRAWING A RECTANGLE OF PROVIDED AREA SIZE                                                                                        ________________________________________
//        Scalar color = new Scalar(255, 255);                     // border color
//        MatVector contourVector = new MatVector(contours);              // saugomi konturu vektoriai
//        ArrayList<Rect> listOfMasks = new ArrayList<>();                // mask'ai busimos numeriu (ar pagal plota panasios) vietos
//        Rect mask = null;                                               // mask - tai plotas kuri noresime isgauti is esamos foto
//        for (int i = 0; i < contours.size(); i++) {                     // ieskome visu konturu kurie atitinka salyga if'e
//            double contourAreaSize = contourArea(contours.get(i));
//            System.out.println("CHAR CONTOUR " + i + " AREA: " +contourAreaSize);
//            if (contourAreaSize > 200 && contourAreaSize <= 15000) {                             // salyga filtravimui (apytikslus numeriu plotas)
//                Rect bound = boundingRect(contours.get(i));
//                rectangle(image, bound.tl(),bound.br(),new Scalar(0,255));                                         // breziame filtra praejusius konturus
//                Utils.showImage(image, "THE ONE " + i);
//                                // gautus konturus apibreziame kaip mask
//
//                listOfMasks.add(bound);                                  // sumetame mask'us i lista
//            }
//
//
//
//        }
//        System.out.println("THE SIZE OF MASKS: " + listOfMasks.size());
//
//        return listOfMasks;
//    }
//
//    public static void extractCharacters(Mat image) {
//        System.out.println("EXTRACTING CHARACTERS ...");
//        ArrayList<Mat> listOfCharacterImages = new ArrayList<>();
//        ArrayList<Rect> listOfMasks = getCharacterMasks(image);
//        Mat plateImage = extractPlate(image);
//        int i = 0;
//        Mat imageCopy = new Mat();
//        image.copyTo(imageCopy);
//
//        Mat drawing = new Mat();
//        drawing.put(Mat.zeros(image.size(), CV_8UC3));                  // empty mask
//        for (Rect mask :
//                listOfMasks) {
//            Mat cutImage = new Mat(plateImage, listOfMasks.get(i));
//            i++;
//            Utils.showImage(cutImage, "CUT:" + i);
//        }
//
//
//    }
}
