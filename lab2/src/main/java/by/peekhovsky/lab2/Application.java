package by.peekhovsky.lab2;

import by.peekhovsky.lab2.analyze.AnalyzedImage;
import by.peekhovsky.lab2.filter.Filter;
import by.peekhovsky.lab2.filter.GrayFilter;
import by.peekhovsky.lab2.filter.MedianFilter;
import by.peekhovsky.lab2.img.ImageDrawer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;


public class Application {

  private static final String IMG_PATH = "/img_5.jpg";
  private static final int CLUSTER_NUM = 4;

  private void process() throws IOException {
    InputStream inputStream = this.getClass().getResourceAsStream(IMG_PATH);
    BufferedImage bufferedImage = ImageIO.read(inputStream);
    ImageDrawer drawer = new ImageDrawer();

    // gray filter
    Filter grayFilter = new GrayFilter();
    bufferedImage = grayFilter.filter(bufferedImage);
    drawer.drawBufferedImage(bufferedImage, "after_gray");

    // median filter
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
    drawer.drawBufferedImage(bufferedImage, "after_median");

    AnalyzedImage analyzedImage = new AnalyzedImage(bufferedImage);

    //binary
    drawer.drawBinary(analyzedImage);

    //diff colors
    drawer.drawFiguresInDiffColors(analyzedImage);

    //clusters
    drawer.drawByClusters(analyzedImage, CLUSTER_NUM);
  }

  public static void main(String[] args) throws IOException {
    new Application().process();
  }
}
