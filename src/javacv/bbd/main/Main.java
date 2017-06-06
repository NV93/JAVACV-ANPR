package javacv.bbd.main;

import javacv.bbd.detection.CharacterDetection;
import javacv.bbd.detection.NumberPlateDetection;
import javacv.bbd.filters.MorphFilters;
import javacv.bbd.processing.PreProcessing;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.io.File;
import java.util.ArrayList;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

/**
 * Created by nevyt on 5/18/2017.
 */
public class Main {

    public static void main(String[] args) throws TesseractException {

        CharacterDetection characterDetection = new CharacterDetection();
        characterDetection.charRecognition();



    }
}
