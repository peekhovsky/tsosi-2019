package by.peekhovsky.lab2.img;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Rastsislau Piakhouski 2019
 */
public class FigureKMediansAnalyzer {

  private final Random random;

  public FigureKMediansAnalyzer(Random random) {
    this.random = random;
  }

  public void analyze(int numOfClusters, Map<Integer, VectorFigure> figures) {
    Map<VectorFigure, Map<Integer, VectorFigure>> clusters = new HashMap<>();
    clusters = setUpNewRandomCenters(numOfClusters, figures, clusters);
    clusters = addFiguresToClusters(figures, clusters);
    showClusterFigures(clusters);
  }

  private Map<VectorFigure, Map<Integer, VectorFigure>> setUpNewRandomCenters(
      int numOfClusters,
      Map<Integer, VectorFigure> figures,
      Map<VectorFigure, Map<Integer, VectorFigure>> clusters) {

    var values = figures.entrySet().toArray(new Map.Entry[0]);
    for (int i = 0; i < numOfClusters; i++) {
      var clusterCenterOfMassEntry = values[random.nextInt(values.length)];
      Map<Integer, VectorFigure> clusterVectors = new HashMap<>();
      clusterVectors.put(
          (Integer) clusterCenterOfMassEntry.getKey(),
          (VectorFigure) clusterCenterOfMassEntry.getValue());
      clusters.put((VectorFigure) clusterCenterOfMassEntry.getValue(), clusterVectors);
    }
    return clusters;
  }

  private Map<VectorFigure, Map<Integer, VectorFigure>> recalculateCenters(
      int numOfClusters,
      Map<Integer, VectorFigure> figures,
      Map<VectorFigure, Map<Integer, VectorFigure>> clusters) {


    return clusters;
  }

  private Map<VectorFigure, Map<Integer, VectorFigure>> addFiguresToClusters(
      Map<Integer, VectorFigure> figures,
      Map<VectorFigure, Map<Integer, VectorFigure>> clusterFigures) {

    figures.forEach((key, figure) -> {
      VectorFigure minClusterFigure = null;
      if (clusterFigures.isEmpty()) {
        throw new IllegalArgumentException();
      }

      double minDistance = -1;
      for (VectorFigure clusterFigure : clusterFigures.keySet()) {
        double distance = figure.distance(clusterFigure);
        if (distance < minDistance || minDistance == -1) {
          minDistance = distance;
          minClusterFigure = clusterFigure;
        }
      }
      clusterFigures.get(minClusterFigure).put(key, figure);
    });
    return clusterFigures;
  }


  private void showClusterFigures(Map<VectorFigure, Map<Integer, VectorFigure>> clusterFigures) {
    clusterFigures.forEach((cluster, figures) -> {
      System.out.println("Cluster: " + cluster);
      System.out.println("Figures: ");
      figures.forEach((key, figure) -> {
        System.out.println("Key: " + key);
        System.out.println("Figure: " + figure);
        System.out.println();
      });
    });
  }

}
