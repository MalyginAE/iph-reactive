package com.hse.iphreactive.openCV;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Slf4j
public class ImageAnalyzeUtil {
    private static final Path basePath = Paths.get("C:\\Users\\Public\\temp\\");//to config
    public static final Scalar COLOR_BLACK = colorRGB(0, 0, 0);
    public static final Scalar COLOR_WHITE = colorRGB(255, 255, 255);
    public static final Scalar COLOR_RED = colorRGB(255, 0, 0);
    public static final Scalar COLOR_BLUE = colorRGB(0, 0, 255);
    public static final Scalar COLOR_GREEN = colorRGB(0, 128, 0);
    public static final Scalar COLOR_YELLOW = colorRGB(255, 255, 0);
    public static final Scalar COLOR_GRAY = colorRGB(128, 128, 128);



    public static Scalar colorRGB(double red, double green, double blue) {
        return new Scalar(blue, green, red);
    }
    /////////////////////////////Загрузка фото и классификатора////////////////////////////////////////


    public static byte[] getAnalyzingImage(String uri) throws IOException {
       // bytes = Files.readAllBytes(new File(uri).toPath());
        Mat img = Imgcodecs.imread(uri);
        if (img.empty()) {
            log.error("Не удалось загрузить изображение");

            return new byte[0];
        }
        String path = basePath + "\\classifier\\haarcascade_fullbody.xml";
        CascadeClassifier fullbody_detector = new CascadeClassifier();
        if (!fullbody_detector.load(path)) {
            System.out.println("Не удалось загрузить классификатор " + path);
            return new byte[0];
        }
        /////////////////////////////Преобразование фото в черно-белое////////////////////////////////////////
        Mat img2 = new Mat();
        Imgproc.cvtColor(img, img2, Imgproc.COLOR_BGR2GRAY);
        Mat img3 = new Mat();
        Imgproc.GaussianBlur(img2, img3, new Size(3, 3), 0);
        Mat img4 = new Mat();
        Imgproc.adaptiveThreshold(img3, img4, 255,
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                Imgproc.THRESH_BINARY_INV, 25, 1);
        /////////////////////////////Создание контура////////////////////////////////////////
        Imgproc.Canny(img4, img4, 150, 255);
        Imgproc.GaussianBlur(img4, img4, new Size(3, 3), 0);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                new Size(3, 3));
        Imgproc.morphologyEx(img4, img4, Imgproc.MORPH_CLOSE, kernel);

        Mat hierarchy = new Mat();
        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(img4, contours, hierarchy,
                Imgproc.RETR_EXTERNAL,
                Imgproc.CHAIN_APPROX_NONE);

        Mat img5 = new Mat(new Size(img4.width(), img4.height()), CvType.CV_8UC3, new Scalar(254, 188, 4));
        for (int i = 0; i < contours.size(); i++){
            double cont_area = Imgproc.contourArea(contours.get(i));
            if (cont_area > 1000) {
                Imgproc.drawContours(img5, contours, i, new Scalar(130, 0, 15), 6);
            }
        }
        ///////////////////////////////Нахождение роста//////////////////////////////////////

//        MatOfRect fullbody = new MatOfRect();
//        fullbody_detector.detectMultiScale(img5, fullbody);
//        for (Rect r : fullbody.toList()) {
//            Imgproc.rectangle(img5, new Point(r.x, r.y),
//                    new Point(r.x + r.width, r.y + r.height),
//                    CvUtils.COLOR_BLACK, 2);
//        }
        Rect item = new Rect(1, 1, 1,1);
        Rect body = new Rect(1, 1, 1,1);
        for (MatOfPoint contour : contours) {
            Rect r = Imgproc.boundingRect(contour);
            if (r.height > body.height){
                item = body;
                body = r;
            }
        }
        Imgproc.rectangle(img5, new Point(item.x, item.y),
                new Point(item.x + item.width - 1, item.y + item.height - 1),
                COLOR_WHITE);
        Imgproc.rectangle(img5, new Point(body.x, body.y),
                new Point(body.x + body.width - 1, body.y + body.height - 1),
                COLOR_RED);
        BufferedWriter writer = null;
        try {
            String height= String.valueOf(body.height * 103 / item.height);
            log.info("height: {}",height);
            writer = new BufferedWriter(new FileWriter("height.txt"));
            writer.write( height);
            writer.close();
        } catch (IOException e) {
            System.out.println("Не удалось сохранить рост");
        }

        boolean st = Imgcodecs.imwrite(basePath+"\\analyze\\"+Path.of(uri).getFileName().toString(), img5);
        if (!st) {
            System.out.println("Не удалось сохранить изображение");
        }

        return Files.readAllBytes(Path.of(basePath+"\\analyze\\"+Path.of(uri).getFileName().toString()));
    }
}