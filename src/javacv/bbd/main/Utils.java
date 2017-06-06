package javacv.bbd.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static org.bytedeco.javacpp.opencv_core.Mat;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;

/**
 * Created by nevyt on 5/24/2017.
 */
public class Utils {

    public static void showImage(Mat image, String imageName){
        Mat imageOut = new Mat();
//        resize(image,imageOut,new opencv_core.Size(1024,720));
        namedWindow(imageName,CV_WINDOW_AUTOSIZE);
        imshow(imageName,image);
        waitKey(-1);
    }

    public static void showImageList(ArrayList<Mat> imageList){
        String name = "Image ";
        int i = 0;
        for (Mat image :
                imageList) {
            namedWindow(name + i,CV_WINDOW_AUTOSIZE);
            imshow(name+i,image);
            waitKey(-1);
        }
    }

    public static void outputImagesFromList(ArrayList<Mat> imageList,String directory){
        int i = 1;
        for (Mat image :
                imageList) {
            imwrite(directory + "outputImage" + i +".jpg",image);
            i++;
        }

    }

    public static ArrayList<String> filePathsToList(String path) {

        File f = new File(path);
        ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(f.listFiles()));
        ArrayList<String> filePathList = new ArrayList<>();
        System.out.println("Loading file paths...");
        for (File file : fileList) {
            String filePath = file.getAbsolutePath();
            filePathList.add(filePath);
            //System.out.println(file.toString());
        }
        return filePathList;


    }

    public static ArrayList<Mat> filePathsToMatImageList(ArrayList<String> fileList) {
        ArrayList<Mat> matImageList = new ArrayList<>();
        System.out.println("Loading Mat images ...");
        for (String path : fileList) {
            matImageList.add(imread(path));

            System.out.println(path);
        }
        return matImageList;
    }
}
