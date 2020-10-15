package by.peekhovsky.tsosi.lab1.filter;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rastsislau Piakhouski 2019
 */
public class MaxFilter implements Filter {

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
        List<Integer> pix = getBehindPixels(image, x, y);
        newImage.setRGB(x, y, processPixel(pix));
      }
    }

    return newImage;
  }

  private int processPixel(List<Integer> pixels) {
    int max = pixels.stream().max((pixel1, pixel2) -> {
      int medium1 = ((int) ((pixel1 >> 16 & 0xff) * RED_FACTOR)
              + (int) ((pixel1 >> 8 & 0xff) * GREEN_FACTOR)
              + (int) ((pixel1 & 0xff) * BLUE_FACTOR));
      int medium2 = ((int) ((pixel2 >> 16 & 0xff) * RED_FACTOR)
              + (int) ((pixel2 >> 8 & 0xff) * GREEN_FACTOR)
              + (int) ((pixel2 & 0xff) * BLUE_FACTOR));
      return Integer.compare(medium1, medium2);
    }).orElse(0);

    return new Color(max).getRGB();
  }

  private List<Integer> getBehindPixels(BufferedImage image, int x, int y) {
    int width = image.getWidth();
    int height = image.getHeight();
    List<Integer> pixels = new ArrayList<>();

    if (x > 0) {
      pixels.add(image.getRGB(x - 1, y));
    }
    if (y > 0) {
      pixels.add(image.getRGB(x, y - 1));
    }
    if (y > 0 && x > 0) {
      pixels.add(image.getRGB(x - 1, y - 1));
    }
    if (x < width - 1) {
      pixels.add(image.getRGB(x + 1, y));
    }
    if (y < height - 1) {
      pixels.add(image.getRGB(x, y + 1));
    }
    if (x < width - 1 && y < height - 1) {
      pixels.add(image.getRGB(x + 1, y + 1));
    }
    if (x > 0 && y < height - 1) {
      pixels.add(image.getRGB(x - 1, y + 1));
    }
    if (x < width - 1 && y > 0) {
      pixels.add(image.getRGB(x + 1, y - 1));
    }
    pixels.add(image.getRGB(x, y));

    return pixels;
  }
}
