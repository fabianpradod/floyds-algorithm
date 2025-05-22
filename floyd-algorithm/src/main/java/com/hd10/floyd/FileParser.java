package com.hd10.floyd;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    // Existing method (keep for compatibility)
    public static List<Path> parse(String filename) throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            return parse(fis);
        }
    }

    // New method that accepts an InputStream
    public static List<Path> parse(InputStream inputStream) throws IOException {
        List<Path> paths = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] path = line.split("\\s+");
                if (path.length != 6) { /*expecting 6 fields */
                    throw new IllegalArgumentException(
                        "Expected 6 fields, got " + path.length + ": " + line);
                }
                String city1 = path[0];
                String city2 = path[1];
                int timeNormal = Integer.parseInt(path[2]);
                int timeRain = Integer.parseInt(path[3]);
                int timeSnow = Integer.parseInt(path[4]);
                int timeStorm = Integer.parseInt(path[5]);

                String name = city1 + "To" + city2;
                paths.add(new Path(name, city1, city2, timeNormal, timeRain, timeSnow, timeStorm));
            }
        }

        return paths;
    }
}