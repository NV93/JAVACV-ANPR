package javacv.bbd.main;

import org.bytedeco.javacpp.opencv_core;

import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

/**
 * Created by nevyt on 5/24/2017.
 */
public class Utils {

    public static void showImage(opencv_core.Mat image, String imageName){
        opencv_core.Mat imageOut = new opencv_core.Mat();
        resize(image,imageOut,new opencv_core.Size(1024,720));
        namedWindow(imageName,CV_WINDOW_AUTOSIZE);
        imshow(imageName,imageOut);
        waitKey(-1);
    }
}
