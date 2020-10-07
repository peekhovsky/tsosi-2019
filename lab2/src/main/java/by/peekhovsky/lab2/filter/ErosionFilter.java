package by.peekhovsky.lab2.filter;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rastsislau Piakhouski 2019
 */
public class ErosionFilter implements Filter {

  @Override
  @SuppressWarnings("Duplicates")
  public BufferedImage filter(BufferedImage image) {
    int height = image.getHeight();
    int width = image.getWidth();

    BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = newImage.createGraphics();
    g.drawImage(image, 0, 0, null);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        List<Integer> pix = new ArrayList<>(9);

        if (x > 0 && y > 0) {
          pix.add(image.getRGB(x - 1, y - 1));
        }
        if (x > 0) {
          pix.add(image.getRGB(x - 1, y));
        }
        if (x > 0 && y < height - 1) {
          pix.add(image.getRGB(x - 1, y + 1));
        }
        if (y > 0) {
          pix.add(image.getRGB(x, y - 1));
        }
        pix.add(image.getRGB(x, y));
        if (y < height - 1) {
          pix.add(image.getRGB(x, y + 1));
        }
        if (x < width - 1 && y > 0) {
          pix.add(image.getRGB(x + 1, y - 1));
        }
        if (x < width - 1) {
          pix.add(image.getRGB(x + 1, y));
        }
        if (x < width - 1 && y < height - 1) {
          pix.add(image.getRGB(x + 1, y + 1));
        }

        pix.sort(Double::compare);
        newImage.setRGB(x, y, pix.get(0));
      }
    }
    return newImage;
  }
}
