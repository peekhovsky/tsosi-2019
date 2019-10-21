package by.peekhovsky.lab2;

import by.peekhovsky.lab2.analyze.AnalyzedImage;
import by.peekhovsky.lab2.filter.Filter;
import by.peekhovsky.lab2.filter.MedianFilter;
import by.peekhovsky.lab2.img.AnalyzedImageDrawer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Application {

  private static final String IMG_PATH = "/img_1.jpg";
  private static final int CLUSTER_NUM = 2;


  public void process() throws IOException {
    InputStream inputStream = this.getClass().getResourceAsStream(IMG_PATH);
    BufferedImage bufferedImage = ImageIO.read(inputStream);
    AnalyzedImageDrawer analyzedImageDrawer = new AnalyzedImageDrawer();

    //median filter
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

    //binary
    analyzedImageDrawer.drawBinary(analyzedImage);

    //diff colors
    analyzedImageDrawer.drawFiguresInDiffColors(analyzedImage);

    //clusters
    analyzedImageDrawer.drawByClusters(analyzedImage, CLUSTER_NUM);
  }

  public static void main(String[] args) throws IOException {
    new Application().process();
  }
}
