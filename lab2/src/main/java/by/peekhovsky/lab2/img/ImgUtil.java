package by.peekhovsky.lab2.img;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;


/**
 * @author Rastsislau Piakhouski 2019
 */
public class ImgUtil {

  private ImgUtil() {
  }

  public static BufferedImage clone(BufferedImage img) {
    ColorModel model = img.getColorModel();
    boolean isAlpha = img.isAlphaPremultiplied();
    WritableRaster raster = img.copyData(null);
    return new BufferedImage(model, raster, isAlpha, null);
  }
}
