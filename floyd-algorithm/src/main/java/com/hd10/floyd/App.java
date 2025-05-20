package com.hd10.floyd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final String DATA = "origin/main/reorigins/logistica.txt";

    public static void main(String[] args) {
        try {
            List<Path> paths = FileParser.parse(DATA);
            Graph graph = new Graph(paths);
            FloydAlgorithm floyd = new FloydAlgorithm();

            Scanner scanner = new Scanner(System.in);
            boolean exit = false;
            while (!exit) {
                printMenu();
                int choice = readInt(scanner);
                switch (choice) {
                    case 1:
                        shortestPath(scanner, graph, floyd);
                        break;
                    case 2:
                        findCenter(scanner, graph, floyd);
                        break;
                    case 3:
                        addObstruction(scanner, graph, floyd);
                        break;
                    case 4:
                        System.out.println("Exiting");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.err.println("Error reading data file: " + e.getMessage());
        }
    }

    private static void printMenu() {
        System.out.println("\nWelcome to the Floyd's Algorithm Program");
        System.out.println("1. Shortest path between two cities");
        System.out.println("2. Center of the graph");
        System.out.println("3. Modify graph (add/change obstructions)");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");
    }

    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("\nEnter a number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void shortestPath(Scanner scanner, Graph graph, FloydAlgorithm floyd) {
        // List available cities with indices
        List<String> cities = graph.getCities();
        System.out.println("Available cities:");
        for (int idx = 0; idx < cities.size(); idx++) {
            System.out.printf("%d: %s\n", idx, cities.get(idx));
        }
        System.out.print("Enter origin city index: ");
        int origin = readInt(scanner);
        System.out.print("Enter destination city index: ");
        int destination = readInt(scanner);

        // Compute shortest paths on current graph
        floyd.compute(graph.getMatrix());
        List<Integer> pathIndices = floyd.getPathIndices(origin, destination);
        if (pathIndices.isEmpty()) {
            System.out.println("No path found from " + cities.get(origin) + " to " + cities.get(destination));
            return;
        }
        // Map indices to city names
        List<String> pathCities = new ArrayList<>();
        for (int i : pathIndices) {
            pathCities.add(cities.get(i));
        }
        int distance = floyd.getDistance(origin, destination);
        System.out.println("Shortest path: " + String.join(" -> ", pathCities));
        System.out.println("Total distance: " + distance);
    }

    private static void findCenter(Scanner scanner, Graph graph, FloydAlgorithm floyd) {
        // TODO: implement center-finding logic
    }

    private static void addObstruction(Scanner scanner, Graph graph, FloydAlgorithm floyd) {
        // TODO: implement obstruction modification loop
    }
}