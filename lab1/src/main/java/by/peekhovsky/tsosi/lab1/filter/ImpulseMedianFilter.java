package by.peekhovsky.tsosi.lab1.filter;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Rastsislau Piakhouski 2019
 */
public class ImpulseMedianFilter implements Filter {

  private static final double RED_FACTOR = 0.3;
  private static final double GREEN_FACTOR = 0.59;
  private static final double BLUE_FACTOR = 0.11;

  @Override
  public BufferedImage filter(BufferedImage image) {
    int height = image.getHeight();
    int width = image.getWidth();

    BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = newImage.createGraphics();
    g.drawImage(image, 0, 0, null);

    for (int y = 1; y < height - 1; y+=10) {
      for (int x = 1; x < width - 1; x+=10) {
        List<Integer> pix = new ArrayList<>(9);
        pix.add(image.getRGB(x - 1, y - 1));
        pix.add(image.getRGB(x - 1, y));
        pix.add(image.getRGB(x - 1, y + 1));
        pix.add(image.getRGB(x, y - 1));
        pix.add(image.getRGB(x, y));
        pix.add(image.getRGB(x, y + 1));
        pix.add(image.getRGB(x + 1, y - 1));
        pix.add(image.getRGB(x + 1, y));
        pix.add(image.getRGB(x + 1, y + 1));

        Integer impulsePixel = image.getRGB(x,y);

        pix.sort((rgb1, rgb2) -> {
          int medium1 = (int) ((rgb1 >> 16 & 0xff) * RED_FACTOR)
              + (int) ((rgb1 >> 8 & 0xff) * GREEN_FACTOR)
              + (int) ((rgb1 & 0xff) * BLUE_FACTOR);
          int medium2 = (int) ((rgb2 >> 16 & 0xff) * RED_FACTOR)
              + (int) ((rgb2 >> 8 & 0xff) * GREEN_FACTOR)
              + (int) ((rgb2 & 0xff) * BLUE_FACTOR);
          return Integer.compare(medium1, medium2);
        });

        if (pix.get(8).equals(impulsePixel)) {
          newImage.setRGB(x, y, Color.WHITE.getRGB());
        } else {
          //newImage.setRGB(x, y, pix.get(4));
        }
      }
    }
    return newImage;
  }
}
