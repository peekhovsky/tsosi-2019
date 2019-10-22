package by.peekhovsky.lab2.img;

import by.peekhovsky.lab2.analyze.AnalyzedImage;
import by.peekhovsky.lab2.analyze.FigureKMediansAnalyzer;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Rastsislau Piakhouski 2019
 */
public class ImageDrawer {

  private static final List<Color> colorList = new ArrayList<>();

  private int currentColorIndex = 0;

  static {
    colorList.add(Color.blue);
    colorList.add(Color.green);
    colorList.add(Color.gray);
    colorList.add(Color.yellow);
    colorList.add(Color.pink);
    colorList.add(Color.red);
    colorList.add(Color.magenta);
    colorList.add(Color.cyan);
    colorList.add(Color.lightGray);
  }

  public void drawBufferedImage(BufferedImage image, String name) throws IOException {
    ImageIO.write(image, "jpg", new File(name + ".jpg"));
  }

  public void drawBinary(AnalyzedImage analyzedImage) throws IOException {
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
    ImageIO.write(bufferedImage, "jpg", new File("binary.jpg"));
  }

  public void drawFiguresInDiffColors(AnalyzedImage analyzedImage) throws IOException {
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
      ImageIO.write(bufferedImage, "jpg", new File("figures.jpg"));
    }
  }

  public void drawByClusters(AnalyzedImage analyzedImage, int numOfClusters) throws IOException {
    resetColors();
    int width = analyzedImage.getWidth();
    int height = analyzedImage.getHeight();
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = bufferedImage.createGraphics();

    FigureKMediansAnalyzer figureKMediansAnalyzer = new FigureKMediansAnalyzer(new Random());
    Map<VectorFigure, Map<Integer, VectorFigure>> clusters = figureKMediansAnalyzer.analyze(numOfClusters, analyzedImage.getFigures());
    System.out.println("B");
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

      ImageIO.write(bufferedImage, "jpg", new File("clusters.jpg"));
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
