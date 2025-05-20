package com.hd10.floyd;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Floyd algorithm for shortest paths
 */
public class FloydAlgorithm {
    private static final int infinity = Integer.MAX_VALUE / 2;
    private int[][] distance;
    private int[][] next;

    /**
     * Runs Floyd algorithm on the provided adjacency matrix
     * After calling this, getDistance and getPathIndices will reflect shortest paths
     */
    public void compute(int[][] matrix) {
        int n = matrix.length;
        // Copy matrix into distance matrix (so as to not modify the original)
        distance = new int[n][n];
        next = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distance[i][j] = matrix[i][j];
                if (i != j && matrix[i][j] < infinity) {
                    next[i][j] = j;
                } else {
                    next[i][j] = -1;
                }
            }
            // Keep distance to same node 0 
            distance[i][i] = 0;
            next[i][i] = i;
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int viaK = distance[i][k] + distance[k][j];
                    if (viaK < distance[i][j]) {
                        distance[i][j] = viaK;
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
    }

    /**
     * Returns the computed shortest path distance between i and j.
     */
    public int getDistance(int i, int j) {
        return distance[i][j];
    }

    /**
     * Reconstructs the path of indices from i to j 
     */
    public List<Integer> getPathIndices(int i, int j) {
        List<Integer> path = new ArrayList<>();
        if (next[i][j] == -1) {
            return path;
        }
        int at = i;
        while (at != j) {
            path.add(at);
            at = next[at][j];
        }
        path.add(j);
        return path;
    }
}
