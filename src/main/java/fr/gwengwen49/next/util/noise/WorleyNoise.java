package fr.gwengwen49.next.util.noise;

import net.minecraft.util.math.random.Random;

public class WorleyNoise {

    private final int numCells;
    private final Random random;

    public WorleyNoise(int numCells, long seed) {
        this.numCells = numCells;
        this.random = Random.create(seed);
    }

    private double euclideanDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double getNoise(double x, double y) {
        double minDistance1 = Double.POSITIVE_INFINITY;
        double minDistance2 = Double.POSITIVE_INFINITY;

        for (int i = 0; i < numCells; i++) {
            double cellX = random.nextDouble();
            double cellY = random.nextDouble();
            double distance = euclideanDistance(x, y, cellX, cellY);

            if (distance < minDistance1) {
                minDistance2 = minDistance1;
                minDistance1 = distance;
            } else if (distance < minDistance2) {
                minDistance2 = distance;
            }
        }

        return minDistance2 - minDistance1;
    }
}