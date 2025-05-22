package com.hd10.floyd;
// importaciones necesarias
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList; 
import java.util.Arrays;
import java.util.List;

/**
 * Como tal Test para la clase Graph y el algoritmo de Floyd
 */
public class GraphFloydTest {
    
    private Graph graph;
    private FloydAlgorithm floyd;
    private List<Path> samplePaths;
    
    @BeforeEach
    void setUp() {
        //se crea una lista de paths de ejemplo
        // para inicializar el grafo y el algoritmo de Floyd
        samplePaths = Arrays.asList(
            new Path("AToB", "A", "B", 5, 7, 10, 15),
            new Path("BToC", "B", "C", 3, 5, 8, 12),
            new Path("AToC", "A", "C", 10, 12, 15, 20),
            new Path("CToD", "C", "D", 4, 6, 9, 13)
        );
        
        graph = new Graph(samplePaths);
        floyd = new FloydAlgorithm();
    }
    
    @Nested
    @DisplayName("Tests para clase Graph") //es mas que nada para verificar la clase Graph
    // y su constructor, métodos de acceso y actualización de pesos
    class GraphTests {
        
        
        @Test
        @DisplayName("Test getMatrix retorna matriz válida")
        void testGetMatrix() {
            int[][] matrix = graph.getMatrix();
            assertNotNull(matrix);
            assertEquals(4, matrix.length);
            assertEquals(4, matrix[0].length);
            
            // Diagonal debe ser 0
            for (int i = 0; i < matrix.length; i++) {
                assertEquals(0, matrix[i][i]);
            }
        }

        
        @Test
        @DisplayName("Test matriz construida correctamente desde paths") // Test para verificar que la matriz se construya correctamente
        void testMatrixBuiltFromPaths() {         //*(ChatGpt, 2025) */
            int[][] matrix = graph.getMatrix();
            List<String> cities = graph.getCities();
            
            // Encontrar índices de ciudades
            int indexA = cities.indexOf("A");
            int indexB = cities.indexOf("B");
            int indexC = cities.indexOf("C");
            int indexD = cities.indexOf("D");
            
            // Verificar pesos en la matriz
            assertEquals(5, matrix[indexA][indexB]);
            assertEquals(3, matrix[indexB][indexC]);
            assertEquals(10, matrix[indexA][indexC]);
            assertEquals(4, matrix[indexC][indexD]);
            
            // Verificar simetría en matriz
            assertEquals(matrix[indexA][indexB], matrix[indexB][indexA]);
            assertEquals(matrix[indexB][indexC], matrix[indexC][indexB]);
        }
    }
    
    @Nested
    @DisplayName("Tests para algoritmo de Floyd") // Tests para el algoritmo de Floyd
    // y su método compute, así como la obtención de distancias y rutas
    class FloydAlgorithmTests {
        
        @Test
        @DisplayName("Test Floyd compute no lanza excepciones")
        void testFloydCompute() {
            assertDoesNotThrow(() -> {
                floyd.compute(graph.getMatrix());
            });
        }
        
        @Test
        @DisplayName("Test Floyd calcula distancias correctas") // Test para verificar que el algoritmo de Floyd calcule las distancias correctamente
        // y que el método getDistance funcione como se espera
        void testFloydDistances() {
            floyd.compute(graph.getMatrix());
            List<String> cities = graph.getCities();
            
            int indexA = cities.indexOf("A");
            int indexB = cities.indexOf("B");
            int indexC = cities.indexOf("C");
            int indexD = cities.indexOf("D");
            
            // Distancias directas
            assertEquals(5, floyd.getDistance(indexA, indexB));
            assertEquals(3, floyd.getDistance(indexB, indexC));
            
            // Distancia A->C: directo (10) vs A->B->C (5+3=8)
            assertEquals(8, floyd.getDistance(indexA, indexC)); // Debe elegir la menor
            
            // Distancia a sí mismo
            assertEquals(0, floyd.getDistance(indexA, indexA));
            assertEquals(0, floyd.getDistance(indexB, indexB));
        }
        
        @Test
        @DisplayName("Test Floyd getPathIndixes retorna rutas correctas") // Test para verificar que el método getPathIndixes
        // devuelva las rutas correctas entre ciudades
        void testFloydPathIndixes() {
            floyd.compute(graph.getMatrix());
            List<String> cities = graph.getCities();
            
            int indexA = cities.indexOf("A");
            int indexB = cities.indexOf("B");
            int indexC = cities.indexOf("C");
            int indexD = cities.indexOf("D");
            
            // Ruta A->B (directa)
            List<Integer> pathAB = floyd.getPathIndixes(indexA, indexB);
            assertEquals(Arrays.asList(indexA, indexB), pathAB);
            
            // Ruta A->C (debe ir via B: A->B->C)
            List<Integer> pathAC = floyd.getPathIndixes(indexA, indexC);
            assertEquals(Arrays.asList(indexA, indexB, indexC), pathAC);
            
            // Ruta a sí mismo
            List<Integer> pathAA = floyd.getPathIndixes(indexA, indexA);
            assertEquals(Arrays.asList(indexA), pathAA);
        }
        

    }
    
}