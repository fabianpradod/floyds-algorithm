package com.hd10.floyd;

import java.util.*;

/**
 * Represents an undirected graph of cities with travel times
 * Uses an adjacency matrix built from a list of Path objects
 */
public class Graph {
    private final Map<String, Integer> cityIndex = new LinkedHashMap<>();   // City names with their index in order of appearance
    private final List<String> cities = new ArrayList<>();                  // List of city names in order of appearance
    private final List<Path> originalPaths;                                 // Original paths with normalTime
    private int[][] matrix;                                                 // Adjacency matrix of travel times
    private static final int infinity = Integer.MAX_VALUE / 2;              // Infinity (unreachable path)

    /**
     * Constructs a Graph from a list of Path entries, using normalTime as the initial weights
     */
    public Graph(List<Path> paths) {
        this.originalPaths = new ArrayList<>(paths);

        for (Path p : paths) {
            if (!cityIndex.containsKey(p.getCity1())) {
                cityIndex.put(p.getCity1(), cities.size());
                cities.add(p.getCity1());
            }
            if (!cityIndex.containsKey(p.getCity2())) {
                cityIndex.put(p.getCity2(), cities.size());
                cities.add(p.getCity2());
            }
        }
        buildMatrix();
    }

    /**
     * Builds the adjacency matrix based on originalPaths and using normalTime
     */
    private void buildMatrix() {
        int n = cities.size();
        matrix = new int[n][n];
        // initialize with infinity and 0 on diagonal
        for (int i = 0; i < n; i++) {
            Arrays.fill(matrix[i], infinity);
            matrix[i][i] = 0;
        }
        // Populate known paths between cities with normalTime
        for (Path p : originalPaths) {
            int i = cityIndex.get(p.getCity1());
            int j = cityIndex.get(p.getCity2());
            matrix[i][j] = p.getNormalTime();
            matrix[j][i] = p.getNormalTime();
        }
    }

    public int[][] getMatrix() {
        return matrix;
    }

    /**
     * Returns an unmodifiable list of city names in index order.
     */
    public List<String> getCities() {
        return Collections.unmodifiableList(cities);
    }
    
    /**
     * Returns the original paths for reference
     */
    public List<Path> getOriginalPaths() {
        return Collections.unmodifiableList(originalPaths);
    }

    /**
     * Updates the travel time between two cities according to obstruction
     * Use infinity to block a path
     */
    public void updatePath(String city1, String city2, int time) {
        Integer i = cityIndex.get(city1);
        Integer j = cityIndex.get(city2);
        matrix[i][j] = time;
        matrix[j][i] = time;
    }

    /**
     * Retrieves the current weight for the path between two cities
     */
    public int getWeight(String city1, String city2) {
        Integer i = cityIndex.get(city1);
        Integer j = cityIndex.get(city2);
        return matrix[i][j];
    }
}