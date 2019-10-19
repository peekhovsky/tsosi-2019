package by.peekhovsky.lab2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(ImageProcessor.class);

    private static final String IMG_PATH = "/img_1.jpg";

    public static void main(String[] args) {
        ImageProcessor imageProcessor = new ImageProcessor();
        imageProcessor.readImage(IMG_PATH);
    }

}
