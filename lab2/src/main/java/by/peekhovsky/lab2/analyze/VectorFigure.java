package by.peekhovsky.lab2.analyze;

/**
 * @author Rastsislau Piakhouski 2019
 */
public interface VectorFigure {
  double getSquare();

  double getPerimeter();

  double getCompactness();

  double getElongation();

  int[][] getFigureArray();

  double distance(VectorFigure vectorFigure);
}
