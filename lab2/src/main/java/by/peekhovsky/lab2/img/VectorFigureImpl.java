package by.peekhovsky.lab2.img;

import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * @author Rastsislau Piakhouski 2019
 */
@SuppressWarnings("WeakerAccess")
@ToString
public class VectorFigureImpl implements VectorFigure {

  @Getter
  private double square;

  @Getter
  private double perimeter;

  @Getter
  private double compactness;

  @Getter
  private double elongation;

  @Getter
  @ToString.Exclude
  private int[][] figureArray;

  @ToString.Exclude
  private int imgWidth;

  @ToString.Exclude
  private int imgHeight;


  public VectorFigureImpl(double square, double perimeter, double compactness, double elongation) {
    this.square = square;
    this.perimeter = perimeter;
    this.compactness = compactness;
    this.elongation = elongation;
  }

  private VectorFigureImpl(int imgWidth, int imgHeight, int[][] figureArray) {
    this.imgWidth = imgWidth;
    this.imgHeight = imgHeight;
    this.figureArray = figureArray;
    this.square = calcSquare();
    this.perimeter = calcPerimeter();
    this.compactness = calcCompactness(this.perimeter, this.square);
    Coordinate centerOfMass = calcCenterOfMass();
    this.elongation = calcElongation(centerOfMass);
  }


  /* Calculates distance between vectors */
  @Override
  public double distance(VectorFigure vectorFigure) {
    return Math.sqrt(
        Math.pow((this.square - vectorFigure.getSquare()), 2)
            + Math.pow((this.perimeter - vectorFigure.getPerimeter()), 2)
            + Math.pow((this.compactness - vectorFigure.getCompactness()), 2)
            + Math.pow((this.elongation - vectorFigure.getElongation()), 2));
  }

  private double calcSquare() {
    int sum = 0;
    for (int y = 0; y < imgHeight; y++) {
      for (int x = 0; x < imgWidth; x++) {
        sum += figureArray[x][y];
      }
    }
    return sum;
  }

  private int calcPerimeter() {
    int sum = 0;
    for (int y = 0; y < imgHeight; y++) {
      for (int x = 0; x < imgWidth; x++) {
        if (figureArray[x][y] == 1) {
          if (x > 0 && x < imgWidth - 1 && y > 0 && y < imgHeight - 1) {
            if (figureArray[x + 1][y] == 0
                || figureArray[x][y + 1] == 0
                || figureArray[x - 1][y] == 0
                || figureArray[x][y - 1] == 0) {
              sum++;
            }
          } else {
            sum++;
          }
        }
      }
    }
    return sum;
  }

  private double calcCompactness(double perimeter, double square) {
    return Math.pow(perimeter, 2) / square;
  }

  private Coordinate calcCenterOfMass() {
    double sumX = 0;
    double sumY = 0;
    double total = 0;

    for (int y = 0; y < imgHeight; y++) {
      for (int x = 0; x < imgWidth; x++) {
        if (figureArray[x][y] == 1) {
          sumX += x;
          sumY += y;
          total++;
        }
      }
    }
    return new Coordinate((sumX / total), (sumY / total));
  }

  private double calcCentralMoment(int i, int j, Coordinate centerOfMass) {
    double centralMoment = 0;
    for (int y = 0; y < imgHeight; y++) {
      for (int x = 0; x < imgWidth; x++) {
        if (figureArray[x][y] == 1) {
          centralMoment += Math.pow((x - centerOfMass.getX()), i) * Math.pow((y - centerOfMass.getY()), j);
        }
      }
    }
    return centralMoment;
  }

  private double calcElongation(Coordinate centerOfMass) {
    double m20 = calcCentralMoment(2, 0, centerOfMass);
    double m02 = calcCentralMoment(0, 2, centerOfMass);
    double m11 = calcCentralMoment(1, 1, centerOfMass);
    double sqrt = Math.sqrt((Math.pow((m20 - m02), 2) + 4 * Math.pow(m11, 2)));
    System.out.println("figure: ");
    System.out.println("M20: " + m20 + ", MO2: " + m02 + ", M11: " + m11 + ", sqrt: " + sqrt);
    double el = (m20 + m02 + sqrt) / (m20 + m02 - sqrt);
    if (Double.isInfinite(el) || Double.isNaN(el)) {
      el = 0;
      System.out.println("00000");
    }
    System.out.println("elongation: " + el);
    return el;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VectorFigureImpl that = (VectorFigureImpl) o;
    return Double.compare(that.square, square) == 0 &&
        Double.compare(that.perimeter, perimeter) == 0 &&
        Double.compare(that.compactness, compactness) == 0 &&
        Double.compare(that.elongation, elongation) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(square, perimeter, compactness, elongation);
  }


  public static ImageFigureBuilder builder(int imgWidth, int imgHeight) {
    return new ImageFigureBuilder(imgWidth, imgHeight);
  }

  public static class ImageFigureBuilder {
    private int[][] figureArray;
    private int width;
    private int height;

    public ImageFigureBuilder(int imgWidth, int imgHeight) {
      this.figureArray = new int[imgWidth][imgHeight];
      this.height = imgHeight;
      this.width = imgWidth;

      for (int y = 0; y < imgHeight; y++) {
        for (int x = 0; x < imgWidth; x++) {
          figureArray[x][y] = 0;
        }
      }
    }

    public ImageFigureBuilder addPixel(int x, int y) {
      figureArray[x][y] = 1;
      return this;
    }

    public VectorFigureImpl build() {
      return new VectorFigureImpl(width, height, figureArray);
    }
  }
}
