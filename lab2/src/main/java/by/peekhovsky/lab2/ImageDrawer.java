package by.peekhovsky.lab2;

import by.peekhovsky.lab2.analyze.AnalyzedImage;
import by.peekhovsky.lab2.analyze.FigureKMediansAnalyzer;
import by.peekhovsky.lab2.analyze.VectorFigure;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Rastsislau Piakhouski 2019
 */
@SuppressWarnings("WeakerAccess")
public class ImageDrawer {

  private static final String JPG_EXT = "jpg";

  private static final List<Color> colorList = new ArrayList<>(8);

  static {
    colorList.add(Color.blue);
    colorList.add(Color.green);
    colorList.add(Color.yellow);
    colorList.add(Color.pink);
    colorList.add(Color.red);
    colorList.add(Color.magenta);
    colorList.add(Color.cyan);
    colorList.add(Color.lightGray);
    Collections.shuffle(colorList);
  }

  private int currentColorIndex = 0;

  public void drawBufferedImage(BufferedImage bufferedImage, String name) throws IOException {
    ImageIO.write(bufferedImage, JPG_EXT, new File(name + "." + JPG_EXT));
  }

  public void drawBinaryImage(AnalyzedImage analyzedImage, String name) throws IOException {
    int width = analyzedImage.getWidth();
    int height = analyzedImage.getHeight();
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = bufferedImage.createGraphics();

    boolean[][] binaryValues = analyzedImage.getBinaryValues();

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (binaryValues[x][y]) {
          graphics2D.fillRect(x, y, 1, 1);
        }
      }
    }
    drawBufferedImage(bufferedImage, name);
  }

  public void drawFiguresInDiffColors(AnalyzedImage analyzedImage, String name) throws IOException {
    resetColors();
    int width = analyzedImage.getWidth();
    int height = analyzedImage.getHeight();
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = bufferedImage.createGraphics();

    for (Map.Entry<Integer, VectorFigure> entry : analyzedImage.getFigures().entrySet()) {
      int[][] figureArray = entry.getValue().getFigureArray();
      Color currentColor = nextColor();
      graphics2D.setColor(currentColor);
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (figureArray[x][y] == 1) {
            graphics2D.fillRect(x, y, 1, 1);
          }
        }
      }
      drawBufferedImage(bufferedImage, name);
    }
  }

  public void drawByClusters(
      AnalyzedImage analyzedImage,
      int numOfClusters,
      String name,
      double minClusterDistance,
      double distanceSubtrahend)
      throws IOException {
    resetColors();
    int width = analyzedImage.getWidth();
    int height = analyzedImage.getHeight();
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = bufferedImage.createGraphics();

    FigureKMediansAnalyzer figureKMediansAnalyzer
        = new FigureKMediansAnalyzer(new Random(), minClusterDistance, distanceSubtrahend);
    Map<VectorFigure, Map<Integer, VectorFigure>> clusters
        = figureKMediansAnalyzer.analyze(numOfClusters, analyzedImage.getFigures());

    for (Map.Entry<VectorFigure, Map<Integer, VectorFigure>> entry : clusters.entrySet()) {
      Map<Integer, VectorFigure> figures = entry.getValue();
      Color currentColor = nextColor();
      graphics2D.setColor(currentColor);

      figures.forEach((key, figure) -> {
        int[][] figureArray = analyzedImage.getFigures().get(key).getFigureArray();
        for (int y = 0; y < height; y++) {
          for (int x = 0; x < width; x++) {
            if (figureArray[x][y] == 1) {
              graphics2D.fillRect(x, y, 1, 1);
            }
          }
        }
      });

      drawBufferedImage(bufferedImage, name);
    }
  }

  private void resetColors() {
    currentColorIndex = 0;
  }

  private Color nextColor() {
    if (currentColorIndex >= colorList.size()) {
      currentColorIndex = 0;
    }
    return colorList.get(currentColorIndex++);
  }
}
