package com.hd10.floyd;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final String DATA = "logistica.txt";

    public static void main(String[] args) {
        try {
            InputStream inputStream = App.class.getClassLoader().getResourceAsStream(DATA);
            if (inputStream == null) {
                System.err.println("Cannot find resource: " + DATA);
                return;
            }
            
            List<Path> paths = FileParser.parse(inputStream);  /*initializing all the things to use from the  other clases */
            Graph graph = new Graph(paths);
            FloydAlgorithm floyd = new FloydAlgorithm();

            Scanner scanner = new Scanner(System.in);
            boolean exit = false; // Main loop
            while (!exit) {
                printMenu();
                int choice = readInt(scanner);
                if (choice == -1) {
                    System.out.println("\nInvalid option. Please try again.");
                    continue;
                }
                
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
                        System.out.println("\nThank you for using Floyds Algorithm!\n");
                        exit = true;
                        break;
                    default:
                        System.out.println("\nInvalid option. Please try again.");
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.err.println("Error reading data file: " + e.getMessage());
        }
    }

    private static void printMenu() { // Display the main menu
        System.out.println("\n=====================================");
        System.out.println("\nWelcome to the Floyd's Algorithm Program\n");
        System.out.println("1. Shortest path between two cities");
        System.out.println("2. Center of the graph");
        System.out.println("3. Modify graph (add/change obstructions)");
        System.out.println("4. Exit");
        System.out.print("\nChoose an option: ");   
    }

    private static int readInt(Scanner scanner) { // Read an integer from the scanner
        try {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                scanner.next(); 
                return -1;
            }
        } catch (InputMismatchException e) {
            scanner.next(); 
            return -1;
        }
    }

    private static void shortestPath(Scanner scanner, Graph graph, FloydAlgorithm floyd) {
        // List available cities with Indixes
        List<String> cities = graph.getCities();
        System.out.println("\nAvailable cities:");
        for (int idx = 0; idx < cities.size(); idx++) {
            System.out.printf("%d: %s\n", idx, cities.get(idx));
        }
        System.out.print("\nEnter origin city index: ");
        int origin = readInt(scanner);
        if (origin < 0 || origin >= cities.size()) {
            System.out.println("\nInvalid city index");
            return;
        }
        
        System.out.print("Enter destination city index: ");
        int destination = readInt(scanner);
        if (destination < 0 || destination >= cities.size()) {
            System.out.println("\nInvalid city index");
            return;
        }

        // Compute shortest paths on current graph
        floyd.compute(graph.getMatrix());
        List<Integer> pathIndixes = floyd.getPathIndixes(origin, destination);
        if (pathIndixes.isEmpty()) {
            System.out.println("\nNo path found from " + cities.get(origin) + " to " + cities.get(destination));
            return;
        }
        // Map Indixes to city names
        List<String> pathCities = new ArrayList<>();
        for (int i : pathIndixes) {
            pathCities.add(cities.get(i));
        }
        int distance = floyd.getDistance(origin, destination);
        System.out.println("\nShortest path: " + String.join(" -> ", pathCities));
        System.out.println("Total distance: " + distance);
    }

    // Find the center of the graph
    // The center is the city with the minimum eccentricity (maximum distance to any other city)
    // Eccentricity is the maximum distance from a city to any other city
    /*Osea como tal primero se va a calcular la excentricidad de cada ciudad y con base en eso determina el centro(ciudad con la menor excentricidad) */
   private static void findCenter(Scanner scanner, Graph graph, FloydAlgorithm floyd) {
    List<String> cities = graph.getCities();
    int n = cities.size();
    
    if (n == 0) {
        System.out.println("\nNo cities available in the graph.");
        return;
    }
    
    // Ejecutar el algoritmo de Floyd para obtener todas las distancias más cortas
    floyd.compute(graph.getMatrix());
    
    int centerIndex = -1;
    int minMaxDistance = Integer.MAX_VALUE;
    
    System.out.println("\nCalculating center of the graph...");
    System.out.println("\nEccentricity for each city:");
    System.out.println("----------------------------------------");
    
    // Para cada ciudad, encontrar su excentricidad (distancia máxima a cualquier otra ciudad)
    for (int i = 0; i < n; i++) {
        int maxDistanceFromCity = 0;
        
        // Encontrar la distancia máxima desde la ciudad i hacia cualquier otra ciudad
        for (int j = 0; j < n; j++) {
            if (i != j) {
                int distance = floyd.getDistance(i, j);
                if (distance < Integer.MAX_VALUE / 2 && distance > maxDistanceFromCity) {
                    maxDistanceFromCity = distance;
                }
            }
        }
        
        
        // Si esta ciudad tiene menor excentricidad, es mejor candidata para centro
        if (maxDistanceFromCity < minMaxDistance) {
            minMaxDistance = maxDistanceFromCity;
            centerIndex = i;
        }
    }
    
    if (centerIndex != -1) {
        System.out.println("----------------------------------------");
        System.out.println("\nCenter of the graph: " + cities.get(centerIndex));
        System.out.println("Eccentricity (maximum distance to any other city): " + minMaxDistance);
        
        // Mostrar las distancias desde el centro hacia todas las demás ciudades
        System.out.println("\nDistances from center to all other cities:");
        System.out.println("------------------------------------------");
        for (int j = 0; j < n; j++) {
            if (centerIndex != j) {
                int distance = floyd.getDistance(centerIndex, j);
                if (distance < Integer.MAX_VALUE / 2) {
                    System.out.printf("%-15s -> %-15s: %d\n", 
                        cities.get(centerIndex), cities.get(j), distance);
                } else {
                    System.out.printf("%-15s -> %-15s: No path available\n", 
                        cities.get(centerIndex), cities.get(j));
                }
            }
        }
        
        // Mostrar rutas específicas desde el centro
        System.out.println("\nShortest paths from center to all cities:");
        System.out.println("-----------------------------------------");
        for (int j = 0; j < n; j++) {
            if (centerIndex != j) {
                List<Integer> pathIndices = floyd.getPathIndixes(centerIndex, j);
                if (!pathIndices.isEmpty()) {
                    List<String> pathCities = new ArrayList<>();
                    for (int idx : pathIndices) {
                        pathCities.add(cities.get(idx));
                    }
                    System.out.printf("To %-15s: %s (Distance: %d)\n", 
                        cities.get(j), String.join(" -> ", pathCities), 
                        floyd.getDistance(centerIndex, j));
                } else {
                    System.out.printf("To %-15s: No path available\n", cities.get(j));
                }
            }
        }
    } else {
        System.out.println("\nCould not determine the center of the graph.");
    }
}

    private static void addObstruction(Scanner scanner, Graph graph, FloydAlgorithm floyd) {
        List<Path> existingPaths = graph.getOriginalPaths();

        // Display all available paths
        System.out.println("\nAvailable paths:");
        for (int i = 0; i < existingPaths.size(); i++) {
            Path path = existingPaths.get(i);
            System.out.printf("%d: %s - %s (Normal: %d, Rain: %d, Snow: %d, Storm: %d)\n", 
                i, path.getCity1(), path.getCity2(), 
                path.getNormalTime(), path.getRainTime(), 
                path.getSnowTime(), path.getStormTime());
        }
        
        System.out.print("\nSelect path number to modify: ");
        int pathIndex = readInt(scanner);
        
        if (pathIndex < 0 || pathIndex >= existingPaths.size()) {
            System.out.println("\nInvalid path selection");
            return;
        }
        
        Path selectedPath = existingPaths.get(pathIndex);
        String city1 = selectedPath.getCity1();
        String city2 = selectedPath.getCity2();
        
        System.out.println("\nSelected path: " + city1 + " - " + city2);
        System.out.println("Select weather condition:");
        System.out.println("1. Normal (" + selectedPath.getNormalTime() + ")");
        System.out.println("2. Rain (" + selectedPath.getRainTime() + ")");
        System.out.println("3. Snow (" + selectedPath.getSnowTime() + ")");
        System.out.println("4. Storm (" + selectedPath.getStormTime() + ")");
        System.out.println("5. Block route (infinity)\n");
        System.out.print("Enter choice: ");
        int weatherChoice = readInt(scanner);
        
        if (weatherChoice < 1 || weatherChoice > 5) {
            System.out.println("\nInvalid choice");
            return;
        }
        
        int newTime;
        switch (weatherChoice) {
            case 1:
                newTime = selectedPath.getNormalTime();
                break;
            case 2:
                newTime = selectedPath.getRainTime();
                break;
            case 3:
                newTime = selectedPath.getSnowTime();
                break;
            case 4:
                newTime = selectedPath.getStormTime();
                break;
            case 5:
                newTime = Integer.MAX_VALUE / 2; // Block route
                break;
            default:
                System.out.println("\nInvalid choice");
                return;
        }
        
        // Update the path with new time
        graph.updatePath(city1, city2, newTime);
        System.out.println("\nPath updated: " + city1 + " - " + city2 + " with new time: " + 
                          (newTime == Integer.MAX_VALUE/2 ? "BLOCKED" : newTime));
    }
}