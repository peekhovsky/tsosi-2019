package by.peekhovsky.lab2.analyze;

import by.peekhovsky.lab2.img.ColorfulPixel;
import by.peekhovsky.lab2.img.VectorFigure;
import by.peekhovsky.lab2.img.VectorFigureImpl;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Rastsislau Piakhouski 2019
 */
public class AnalyzedImage {

  private static final Logger log = LoggerFactory.getLogger(AnalyzedImage.class);

  private static final double RED_FACTOR = 0.3;
  private static final double GREEN_FACTOR = 0.59;
  private static final double BLUE_FACTOR = 0.11;

  @Getter
  private boolean[][] binaryValues;

  @Getter
  private int[][] labelValues;

  @Getter
  private ColorfulPixel[][] rgbValues;

  /**
   * Key - number of the figure.
   * Value - figure object
   */
  @Getter
  private Map<Integer, VectorFigure> figures = new HashMap<>();

  /**
   * Img width.
   */
  @Getter
  private final int width;

  /**
   * Img height.
   */
  @Getter
  private final int height;


  public AnalyzedImage(BufferedImage bufferedImage) {
    this.width = bufferedImage.getWidth();
    this.height = bufferedImage.getHeight();
    labelValues = new int[width][height];
    initImage(bufferedImage);
  }

  private void initImage(BufferedImage bufferedImage) {
    initRgbValues(bufferedImage);
    initBinaryValues(this.rgbValues);
    initLabels();
    initFigures();
    //showRgb();
    showBinary();
    showLabels();
  }

  private void initRgbValues(BufferedImage bufferedImage) {
    rgbValues = new ColorfulPixel[width][height];

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int rgb = bufferedImage.getRGB(x, y);
        int r = (int) ((rgb >> 16 & 0xff) * RED_FACTOR);
        int g = (int) ((rgb >> 8 & 0xff) * GREEN_FACTOR);
        int b = (int) ((rgb & 0xff) * BLUE_FACTOR);
        rgbValues[x][y] = new ColorfulPixel(r, g, b);
      }
    }
  }

  private void initBinaryValues(ColorfulPixel[][] rgbValues) {
    int medium = 0;
    int counter = 0;

    binaryValues = new boolean[width][height];

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        ColorfulPixel rgb = rgbValues[x][y];
        medium += rgb.getR() + rgb.getG() + rgb.getB();
        counter++;
      }
    }
    medium /= counter;

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        ColorfulPixel rgb = rgbValues[x][y];
        int gray = rgb.getR() + rgb.getG() + rgb.getB();
        binaryValues[x][y] = gray > medium * 2;
      }
    }
  }

  private void initLabels() {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        labelValues[x][y] = 0;
      }
    }
    int l = 0;
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        fillByLabels(x, y, l++);
      }
    }
  }

  private void fillByLabels(int x, int y, int l) {
    if (labelValues[x][y] == 0 && binaryValues[x][y]) {
      labelValues[x][y] = l;
      if (x > 0) {
        fillByLabels(x - 1, y, l);
      }
      if (x < width - 1) {
        fillByLabels(x + 1, y, l);
      }
      if (y > 0) {
        fillByLabels(x, y - 1, l);
      }
      if (y < height - 1) {
        fillByLabels(x, y + 1, l);
      }
    }
  }

  private void initFigures() {
    Map<Integer, VectorFigureImpl.ImageFigureBuilder> figureBuilders = new HashMap<>();
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (labelValues[x][y] != 0) {
          VectorFigureImpl.ImageFigureBuilder imageFigureBuilder = figureBuilders.get(labelValues[x][y]);
          if (imageFigureBuilder == null) {
            imageFigureBuilder = VectorFigureImpl.builder(width, height);
            figureBuilders.put(labelValues[x][y], imageFigureBuilder);
          }
          imageFigureBuilder.addPixel(x, y);
        }
      }
    }

    figureBuilders.forEach((key, figureBuilder) -> {
      VectorFigure vectorFigure = figureBuilder.build();
      if (vectorFigure.getElongation() != 0) {
        figures.put(key, vectorFigure);
      }
    });


    figures.forEach((key, val) -> {
      System.out.println("\nFigure: " + key);
      System.out.println("Square: " + val.getSquare());
      System.out.println("Perimeter: " + val.getPerimeter());
      System.out.println("Compactness: " + val.getCompactness());
      System.out.println("Elongation: " + val.getElongation());
    });
  }

  public boolean getPixel(int x, int y) {
    if (x > width || y > height) {
      throw new RuntimeException();
    }
    return binaryValues[x][y];
  }


  //TODO delete
  private void showRgb() {
    System.out.println();
    log.info("RGB: ");

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (x % 5 == 0 && y % 5 == 0) {
          System.out.print(rgbValues[x][y].getR() + " " + rgbValues[x][y].getG() + " " + rgbValues[x][y].getB() + "\n");
        }
      }
      if (y % 5 == 0) {
        System.out.println();
      }
    }
  }

  //TODO delete
  private void showBinary() {
    System.out.println();
    log.info("BINARY: ");
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (x % 5 == 0 && y % 5 == 0) {
          System.out.print((binaryValues[x][y] ? "1" : "0") + " ");
        }
      }
      if (y % 5 == 0) {
        System.out.println();
      }
    }
  }

  //TODO delete
  private void showLabels() {
    System.out.println();
    log.info("LABELS: ");
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (x % 5 == 0 && y % 5 == 0) {
          System.out.print(labelValues[x][y] + " ");
        }
      }
      if (y % 5 == 0) {
        System.out.println();
      }
    }
  }
}
