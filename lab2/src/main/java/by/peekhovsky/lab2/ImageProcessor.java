package by.peekhovsky.lab2;

import by.peekhovsky.lab2.img.AnalyzedImage;
import by.peekhovsky.lab2.img.FigureKMediansAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

public class ImageProcessor {

  private static final Logger log = LoggerFactory.getLogger(ImageProcessor.class);

  private static final String RESOURCE_PATH = "src/main/resources/";

  public void write(BufferedImage img, String path) throws IOException {
    OutputStream output = new FileOutputStream(path);


    ImageIO.write(img, "jpg", output);
    output.close();
  }

  public void readImage(String imagePath) {
    try {
      InputStream inputStream = this.getClass().getResourceAsStream(imagePath);
      BufferedImage bufferedImage = ImageIO.read(inputStream);
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

      ImageIO.write(bufferedImage, "jpg", outputStream);
      byte[] data = outputStream.toByteArray();

      ImageProcessor imageProcessor = new ImageProcessor();
      //BufferedImage newImage = imageProcessor.processImage(new BlackAndWhitePixelOperation(), bufferedImage);

      AnalyzedImage analyzedImage = new AnalyzedImage(bufferedImage);
      FigureKMediansAnalyzer figureKMediansAnalyzer = new FigureKMediansAnalyzer(new Random());
      figureKMediansAnalyzer.analyze(2, analyzedImage.getFigures());


    } catch (IOException e) {
      log.info(e.getMessage());
    }
  }
}
