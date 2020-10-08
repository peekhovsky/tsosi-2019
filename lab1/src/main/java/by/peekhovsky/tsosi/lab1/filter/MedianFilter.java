package by.peekhovsky.tsosi.lab1.filter;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rastsislau Piakhouski 2019
 */
public class MedianFilter implements Filter {

  private static final double RED_FACTOR = 0.3;
  private static final double GREEN_FACTOR = 0.59;
  private static final double BLUE_FACTOR = 0.11;

  @Override
  @SuppressWarnings("Duplicates")
  public BufferedImage filter(BufferedImage image, Integer value) {
    int height = image.getHeight();
    int width = image.getWidth();
    int maskSize = value;

    BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = newImage.createGraphics();
    g.drawImage(image, 0, 0, null);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        List<Integer> pix = new ArrayList<>(9);
        if (x >= maskSize && y >= maskSize) {
          pix.add(image.getRGB(x - 1, y - 1));
        }
        if (x >= maskSize) {
          pix.add(image.getRGB(x - 1, y));
        }
        if (x >= maskSize && y < height - maskSize) {
          pix.add(image.getRGB(x - 1, y + 1));
        }
        if (y >= maskSize) {
          pix.add(image.getRGB(x, y - 1));
        }
        pix.add(image.getRGB(x, y));
        if (y < height - maskSize) {
          pix.add(image.getRGB(x, y + 1));
        }
        if (x < width - maskSize && y >= maskSize) {
          pix.add(image.getRGB(x + 1, y - 1));
        }
        if (x < width - maskSize) {
          pix.add(image.getRGB(x + 1, y));
        }
        if (x < width - maskSize && y < height - maskSize) {
          pix.add(image.getRGB(x + 1, y + 1));
        }

        pix.sort((rgb1, rgb2) -> {
          int medium1 = (int) ((rgb1 >> 16 & 0xff) * RED_FACTOR)
              + (int) ((rgb1 >> 8 & 0xff) * GREEN_FACTOR)
              + (int) ((rgb1 & 0xff) * BLUE_FACTOR);
          int medium2 = (int) ((rgb2 >> 16 & 0xff) * RED_FACTOR)
              + (int) ((rgb2 >> 8 & 0xff) * GREEN_FACTOR)
              + (int) ((rgb2 & 0xff) * BLUE_FACTOR);
          return Integer.compare(medium1, medium2);
        });
        newImage.setRGB(x, y, pix.get(pix.size() / 2));
      }
    }
    return newImage;
  }
}
