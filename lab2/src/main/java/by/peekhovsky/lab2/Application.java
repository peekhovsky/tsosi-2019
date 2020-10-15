package by.peekhovsky.lab2;

import by.peekhovsky.lab2.analyze.AnalyzedImage;
import by.peekhovsky.lab2.filter.DilationFilter;
import by.peekhovsky.lab2.filter.ErosionFilter;
import by.peekhovsky.lab2.filter.Filter;
import by.peekhovsky.lab2.filter.GrayFilter;
import by.peekhovsky.lab2.filter.MedianFilter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;


public class Application {

  private static final String IMG_PATH = "/img_5.jpg";
  private static final int CLUSTER_NUM = 4;
  private static final double MIN_CLUSTER_DISTANCE = 1500;
  private static final double DISTANCE_SUBTRAHEND = 50;


  private void process() throws IOException {
    InputStream inputStream = this.getClass().getResourceAsStream(IMG_PATH);
    BufferedImage bufferedImage = ImageIO.read(inputStream);
    ImageDrawer drawer = new ImageDrawer();

    // gray filter
    Filter grayFilter = new GrayFilter();
    bufferedImage = grayFilter.filter(bufferedImage);
    drawer.drawBufferedImage(bufferedImage, "1_after_gray");

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

    drawer.drawBufferedImage(bufferedImage, "2.1_after_median");


    Filter erosionFilter = new ErosionFilter();
    bufferedImage = erosionFilter.filter(bufferedImage);
    //bufferedImage = erosionFilter.filter(bufferedImage);
    drawer.drawBufferedImage(bufferedImage, "2.2_after_erosion");

    Filter dilationFilter = new DilationFilter();
    bufferedImage = dilationFilter.filter(bufferedImage);
    bufferedImage = dilationFilter.filter(bufferedImage);
    drawer.drawBufferedImage(bufferedImage, "2.3_after_dilation");

    AnalyzedImage analyzedImage = new AnalyzedImage(bufferedImage);

    //binary
    drawer.drawBinaryImage(analyzedImage, "3_binary");

    //diff colors
    drawer.drawFiguresInDiffColors(analyzedImage, "4_figures");

    //clusters
    drawer.drawByClusters(analyzedImage, CLUSTER_NUM, "5_clusters", MIN_CLUSTER_DISTANCE, DISTANCE_SUBTRAHEND);
  }

  public static void main(String[] args) throws IOException {
    new Application().process();
  }
}
