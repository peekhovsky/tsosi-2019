package by.peekhovsky.tsosi.lab1.filter;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rastsislau Piakhouski 2019
 */
public class HarmonicMeanFilter implements Filter {

  private static final double RED_FACTOR = 0.3;
  private static final double GREEN_FACTOR = 0.59;
  private static final double BLUE_FACTOR = 0.11;

  @Override
  @SuppressWarnings("Duplicates")
  public BufferedImage filter(BufferedImage image, Integer value) {
    int height = image.getHeight();
    int width = image.getWidth();

    BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = newImage.createGraphics();
    g.drawImage(image, 0, 0, null);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        List<Double> pix = getBehindPixels(image, x, y);
        newImage.setRGB(x, y, processPixel(pix));
      }
    }

    return newImage;
  }

  private int processPixel(List<Double> pixels) {
    double mediumRed = (pixels.size() / pixels.stream()
      .reduce(0.00, (acc, rgb) -> acc + (Math.pow(new Color((int) Math.round(rgb)).getRed(), -1))));
    double mediumGreen = (pixels.size() / pixels.stream()
      .reduce(0.00, (acc, rgb) -> acc + (Math.pow(new Color((int) Math.round(rgb)).getGreen(), -1))));
    double mediumBlue = (pixels.size() / pixels.stream()
      .reduce(0.00, (acc, rgb) -> acc + (Math.pow(new Color((int) Math.round(rgb)).getBlue(), -1))));

    return new Color(
      roundPixel(mediumRed),
      roundPixel(mediumGreen),
      roundPixel(mediumBlue)
    ).getRGB();
  }

  private int roundPixel(double pixel) {
    long rounded = Math.round(pixel);
    if (rounded > 255) {
      return 255;
    }
    if (rounded < 0) {
      return 0;
    }
    return (int) rounded;
  }

  private List<Double> getBehindPixels(BufferedImage image, int x, int y) {
    int width = image.getWidth();
    int height = image.getHeight();
    List<Double> pixels = new ArrayList<>();

    if (x > 0) {
      pixels.add((double) image.getRGB(x - 1, y));
    }
    if (y > 0) {
      pixels.add((double) image.getRGB(x, y - 1));
    }
    if (y > 0 && x > 0) {
      pixels.add((double) image.getRGB(x - 1, y - 1));
    }
    if (x < width - 1) {
      pixels.add((double) image.getRGB(x + 1, y));
    }
    if (y < height - 1) {
      pixels.add((double) image.getRGB(x, y + 1));
    }
    if (x < width - 1 && y < height - 1) {
      pixels.add((double) image.getRGB(x + 1, y + 1));
    }
    if (x > 0 && y < height - 1) {
      pixels.add((double) image.getRGB(x - 1, y + 1));
    }
    if (x < width - 1 && y > 0) {
      pixels.add((double) image.getRGB(x + 1, y - 1));
    }
    pixels.add((double) image.getRGB(x, y));

    return pixels;
  }
}
