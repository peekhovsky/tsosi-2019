package by.peekhovsky.lab2.img;

import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * @author Rastsislau Piakhouski 2019
 */
@SuppressWarnings("WeakerAccess")
@ToString
public class VectorFigure {

  @Getter
  private double square;

  @Getter
  private double perimeter;

  @Getter
  private double compactness;

  @Getter
  private Coordinate centerOfMass;

  @Getter
  private double elongation;

  private int imgWidth;

  private int imgHeight;

  @ToString.Exclude
  private int[][] figureArray;


  public VectorFigure(int imgWidth, int imgHeight, int[][] figureArray) {
    this.imgWidth = imgWidth;
    this.imgHeight = imgHeight;
    this.figureArray = figureArray;
    this.square = calcSquare();
    this.perimeter = calcPerimeter();
    this.compactness = calcCompactness(this.square);
    this.centerOfMass = calcCenterOfMass(this.square);
    this.elongation = calcElongation(this.centerOfMass);
  }

  public VectorFigure(double square, double perimeter, double compactness, double elongation) {
    this.square = square;
    this.perimeter = perimeter;
    this.compactness = compactness;
    this.elongation = elongation;

    this.centerOfMass = calcCenterOfMass(this.square);
  }

  public double distance(VectorFigure vectorFigure) {
    return Math.sqrt(
        Math.pow((this.square - vectorFigure.square), 2)
            + Math.pow((this.perimeter - vectorFigure.perimeter), 2)
            + Math.pow((this.compactness - vectorFigure.compactness), 2)
            + Math.pow((this.elongation - vectorFigure.elongation), 2));
  }

  public VectorFigure square() {
    return new VectorFigure(Math.pow(square, 2), Math.pow(perimeter, 2), Math.pow(compactness, 2), Math.pow(elongation, 2));
  }

  public double calcSquare() {
    int sum = 0;
    for (int y = 0; y < imgHeight; y++) {
      for (int x = 0; x < imgWidth; x++) {
        sum += figureArray[x][y];
      }
    }
    return sum;
  }

  public int calcPerimeter() {
    int sum = 0;
    for (int y = 0; y < imgHeight; y++) {
      for (int x = 0; x < imgWidth; x++) {
        if (figureArray[x][y] == 1) {
          if (x > 0 && x < imgWidth - 1 && y > 0 && y < imgHeight - 1) {
            if (figureArray[x + 1][y] == 1
                || figureArray[x][y + 1] == 1
                || figureArray[x - 1][y] == 1
                || figureArray[x][y - 1] == 1) {
              sum++;
            }
          } else {
            sum++;
          }

        }
        sum += figureArray[x][y];
      }
    }
    return sum;
  }

  public double calcCompactness(double square) {
    int per = calcPerimeter();
    return Math.pow(per, 2) / square;
  }

  public Coordinate calcCenterOfMass(double square) {
    double sumX = 0;
    double sumY = 0;

    for (int y = 0; y < imgHeight; y++) {
      for (int x = 0; x < imgWidth; x++) {
        if (figureArray[x][y] == 1) {
          sumX += x;
          sumY += y;
        }
      }
    }
    return new Coordinate((int) Math.abs(sumX / square), (int) Math.abs(sumY / square));
  }

  private double calcCentralMoment(int i, int j, Coordinate centerOfMass) {
    double centralMoment = 0;

    for (int y = 1; y <= imgHeight; y++) {
      for (int x = 1; x <= imgWidth; x++) {
        if (figureArray[x-1][y-1] == 1) {
          centralMoment += Math.pow((x - centerOfMass.getX() + 1), i) * Math.pow((y - centerOfMass.getY() + 1), j);
        }
      }
    }
    return centralMoment;
  }

  public double calcElongation(Coordinate centerOfMass) {
    double m20 = calcCentralMoment(2, 0, centerOfMass);
    double m02 = calcCentralMoment(0, 2, centerOfMass);
    double m11 = calcCentralMoment(1, 1, centerOfMass);
    double sqrt = Math.sqrt((Math.pow((m20 - m02), 2) + 4 * Math.pow(m11, 2)));
    System.out.println(this);
    System.out.println("M20: " + m20 + ", MO2: " + m02 + ", M11: " + m11 + ", sqrt: " + sqrt);
    double elongation =  (m20 + m02 + sqrt) / (m20 + m02 - sqrt);
    System.out.println("elongation: " + elongation);
    return elongation;
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

    public VectorFigure build() {
      return new VectorFigure(width, height, figureArray);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VectorFigure that = (VectorFigure) o;
    return Double.compare(that.square, square) == 0 &&
        Double.compare(that.perimeter, perimeter) == 0 &&
        Double.compare(that.compactness, compactness) == 0 &&
        Double.compare(that.elongation, elongation) == 0 &&
        imgWidth == that.imgWidth &&
        imgHeight == that.imgHeight &&
        Objects.equals(centerOfMass, that.centerOfMass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(square, perimeter, compactness, centerOfMass, elongation, imgWidth, imgHeight);
  }


}
