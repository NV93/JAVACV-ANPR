package javacv.bbd.detection;

import javacv.bbd.filters.MorphFilters;
import javacv.bbd.main.Utils;
import javacv.bbd.processing.PreProcessing;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.bytedeco.javacpp.opencv_core;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by nevyt on 6/2/2017.
 */
public class CharacterDetection {

   public static void charRecognition() throws TesseractException {

       MorphFilters morphFilters = new MorphFilters();


       Tesseract instance = new Tesseract();
       instance.setDatapath("C:\\Users\\nevyt\\Desktop\\BBD project JAVA\\Tess4J\\tessdata");

       String inputPath = "C:\\Users\\nevyt\\Desktop\\BBD project JAVA\\BBD JAVACV\\resources\\InputImages\\";
       String outputPath = "C:\\Users\\nevyt\\Desktop\\BBD project JAVA\\BBD JAVACV\\resources\\Outputimages\\";
       ArrayList<String> filePathList = Utils.filePathsToList(inputPath);
       ArrayList<opencv_core.Mat> matImagesList = Utils.filePathsToMatImageList(filePathList);
       ArrayList<opencv_core.Mat> extractedPlateImageList = new ArrayList<>();
       for (opencv_core.Mat image :
               matImagesList) {
           extractedPlateImageList.add(PreProcessing.toThreshold(NumberPlateDetection.extractPlate(image)));
       }

       Utils.outputImagesFromList(extractedPlateImageList,outputPath);
       ArrayList<String> outputFilePathList = Utils.filePathsToList(outputPath);
       ArrayList<String> outputResultStringList = new ArrayList<>();

       for (String file :
               outputFilePathList) {
           File imageFile = new File(file);
           String result = instance.doOCR(imageFile);
           outputResultStringList.add(result);
       }
       for (String outputResult :
               outputResultStringList ) {
           System.out.println("THE NUMBER PLATE IS: " + outputResult);

       }
   }
}
