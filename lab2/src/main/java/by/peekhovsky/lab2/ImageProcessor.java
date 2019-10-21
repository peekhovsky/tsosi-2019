package by.peekhovsky.lab2;

import by.peekhovsky.lab2.analyze.AnalyzedImage;
import by.peekhovsky.lab2.filter.Filter;
import by.peekhovsky.lab2.filter.MedianFilter;
import by.peekhovsky.lab2.img.AnalyzedImageDrawer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageProcessor {

  private static final Logger log = LoggerFactory.getLogger(ImageProcessor.class);

  public void process(String imagePath) {
    try {
      InputStream inputStream = this.getClass().getResourceAsStream(imagePath);
      BufferedImage bufferedImage = ImageIO.read(inputStream);
      AnalyzedImageDrawer analyzedImageDrawer = new AnalyzedImageDrawer();

      Filter medianFilter = new MedianFilter();
      bufferedImage = medianFilter.filter(bufferedImage);
      bufferedImage = medianFilter.filter(bufferedImage);
      bufferedImage = medianFilter.filter(bufferedImage);
      bufferedImage = medianFilter.filter(bufferedImage);
      bufferedImage = medianFilter.filter(bufferedImage);
      bufferedImage = medianFilter.filter(bufferedImage);
      bufferedImage = medianFilter.filter(bufferedImage);
      bufferedImage = medianFilter.filter(bufferedImage);
      bufferedImage = medianFilter.filter(bufferedImage);
      bufferedImage = medianFilter.filter(bufferedImage);

      analyzedImageDrawer.drawBufferedImage(bufferedImage, "after_median");

      AnalyzedImage analyzedImage = new AnalyzedImage(bufferedImage);
      analyzedImageDrawer.drawBinary(analyzedImage);
      analyzedImageDrawer.drawFiguresInDiffColors(analyzedImage);
      analyzedImageDrawer.drawByClusters(analyzedImage, 3);

    } catch (IOException e) {
      log.info(e.getMessage());
    }
  }
}
