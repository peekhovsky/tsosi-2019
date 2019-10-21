package by.peekhovsky.lab2.analyze;

import by.peekhovsky.lab2.img.VectorFigure;
import by.peekhovsky.lab2.img.VectorFigureImpl;

import java.util.*;

/**
 * @author Rastsislau Piakhouski 2019
 */
public class FigureKMediansAnalyzer {

  private final Random random;

  public FigureKMediansAnalyzer(Random random) {
    this.random = random;
  }

  public Map<VectorFigure, Map<Integer, VectorFigure>> analyze(int numOfClusters, Map<Integer, VectorFigure> figures) {
    Map<VectorFigure, Map<Integer, VectorFigure>> clusters = new HashMap<>();
    setUpNewRandomCenters(numOfClusters, figures, clusters);
    addFiguresToClusters(figures, clusters);
    showClusterFigures(clusters);

    while (true) {
      //for (int i = 0; i < 5; i++) {
      var prevClusterCenters = clusters.keySet();
      clusters = recalculateCenters(clusters);
      var newClusterCenters = clusters.keySet();
      addFiguresToClusters(figures, clusters);
      if (isClusterCenterSetsSimilar(prevClusterCenters, newClusterCenters)) {
        break;
      }
    }
    return clusters;
  }


  private Map<VectorFigure, Map<Integer, VectorFigure>> setUpNewRandomCenters(
      int numOfClusters,
      Map<Integer, VectorFigure> figures,
      Map<VectorFigure, Map<Integer, VectorFigure>> clusters) {
    var values = figures.entrySet().toArray(new Map.Entry[0]);

    for (int i = 0; i < numOfClusters; i++) {
      while (true) {
        Map.Entry<Integer, VectorFigure> clusterCenterOfMassEntry = values[random.nextInt(values.length)];
        if (clusters.get(clusterCenterOfMassEntry.getValue()) != null) {
          continue;
        }
        System.out.println("clusterCenterOfMassEntry");
        System.out.println(clusterCenterOfMassEntry);
        Map<Integer, VectorFigure> clusterVectors = new HashMap<>();
        clusters.put(clusterCenterOfMassEntry.getValue(), clusterVectors);
        break;
      }
    }
    return clusters;
  }


  /* public VectorFigure(double square, double perimeter, double compactness, double elongation, Coordinate centerOfMass) */

  private Map<VectorFigure, Map<Integer, VectorFigure>> recalculateCenters(Map<VectorFigure, Map<Integer, VectorFigure>> clusters) {
    Map<VectorFigure, Map<Integer, VectorFigure>> newClusters = new HashMap<>();
    System.out.println("\n\n");
    clusters.forEach((cluster, figures) -> {
      System.out.println("cluster:");
      System.out.println(cluster);
      System.out.println("figures: ");
      System.out.println(figures);
      Map<Integer, VectorFigure> newFigures = new HashMap<>();
      VectorFigure clusterCenter = calcCenterOfMassOfVectors(figures.values());
      // if (clusterCenter != null) {
      newClusters.put(clusterCenter, newFigures);
      //}
    });

    return newClusters;
  }


  private VectorFigure calcCenterOfMassOfVectors(Collection<VectorFigure> vectorFigures) {
    List<Double> squares = new ArrayList<>();
    List<Double> perimeters = new ArrayList<>();
    List<Double> compactnesses = new ArrayList<>();
    List<Double> elongations = new ArrayList<>();
    int size = vectorFigures.size();
    int medianIndex = (int) Math.abs(size / 2.0);

    if (vectorFigures.isEmpty()) {
      return new VectorFigureImpl(0, 0, 0, 0);
    }

    for (VectorFigure vectorFigure : vectorFigures) {
      squares.add(vectorFigure.getSquare());
      perimeters.add(vectorFigure.getPerimeter());
      compactnesses.add(vectorFigure.getCompactness());
      elongations.add(vectorFigure.getElongation());
    }

    squares.sort(Double::compareTo);
    perimeters.sort(Double::compareTo);
    compactnesses.sort(Double::compareTo);
    elongations.sort(Double::compareTo);

//    System.out.println("\ndata: ");
//    System.out.println(squares);
//    System.out.println(perimeters);
//    System.out.println(compactnesses);
//    System.out.println(elongations);

    VectorFigureImpl newCenter = new VectorFigureImpl(squares.get(medianIndex), perimeters.get(medianIndex), compactnesses.get(medianIndex), elongations.get(medianIndex));
    return newCenter;
  }


  private boolean isClusterCenterSetsSimilar(Set<VectorFigure> clusterCenters1, Set<VectorFigure> clusterCenters2) {
    VectorFigure[] clusterCentersArray1 = clusterCenters1.toArray(new VectorFigure[0]);
    VectorFigure[] clusterCentersArray2 = clusterCenters2.toArray(new VectorFigure[0]);

    if (clusterCentersArray1.length != clusterCentersArray2.length) {
      throw new IllegalArgumentException("Cluster centers lengths in not equal");
    }

    for (var i = 0; i < clusterCentersArray1.length; i++) {
      System.out.println("\nclusterCentersArray");
      System.out.println(clusterCentersArray1[i]);
      System.out.println(clusterCentersArray2[i]);
      if (!clusterCentersArray1[i].equals(clusterCentersArray2[i])) {
        return false;
      }
    }
    return true;
  }


  private Map<VectorFigure, Map<Integer, VectorFigure>> addFiguresToClusters(
      Map<Integer, VectorFigure> figures,
      final Map<VectorFigure, Map<Integer, VectorFigure>> clusters) {

    figures.forEach((key, figure) -> {
      VectorFigure minClusterFigure = null;
      if (clusters.isEmpty()) {
        throw new IllegalArgumentException();
      }

      double minDistance = -1;
      for (VectorFigure cluster : clusters.keySet()) {
        double distance = figure.distance(cluster);
        if (distance < minDistance || minDistance == -1) {
          minDistance = distance;
          minClusterFigure = cluster;
        }
      }
      clusters.get(minClusterFigure).put(key, figure);
    });

    return clusters;
  }


  private void showClusterFigures(Map<VectorFigure, Map<Integer, VectorFigure>> clusterFigures) {
    clusterFigures.forEach((cluster, figures) -> {
//      System.out.println("Cluster: " + cluster);
//      System.out.println("Figures: ");
      figures.forEach((key, figure) -> {
//        System.out.println("Key: " + key);
//        System.out.println("Figure: " + figure);
//        System.out.println();
      });
    });
  }
}
