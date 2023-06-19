package Prova;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class SortingAlgorithms {
    private static int comparisons = 0;
    private static int movements = 0;

    public static void main(String[] args) {
        int[] sizes = {1000, 10000, 100000};
        
        for (int size : sizes) {
            int[][] randomArrays = generateRandomArrays(size, 10);
            int[][] almostSortedArrays = generateAlmostSortedArrays(size, 10);
            int[][] reversedArrays = generateReversedArrays(size, 10);
            
            System.out.println("-------------------------------------------------------------------");
            System.out.println("Tamanho do vetor: " + size);
            System.out.println("-------------------------------------------------------------------");
            
            runTests(randomArrays, "Vetor Aleatório", size);
            runTests(almostSortedArrays, "Vetor Quase Ordenado", size);
            runTests(reversedArrays, "Vetor Inversamente Ordenado", size);
            
            System.out.println();
        }
    }

    private static void runTests(int[][] arrays, String arrayType, int size) {
        System.out.println("Tipo de Vetor: " + arrayType);
        System.out.println();
        System.out.println("Método\t\tTempo(ms)\tComparacoes\tMovimentacoes");
        System.out.println("--------------------------------------------------------");
        
        for (int i = 0; i < arrays.length; i++) {
            int[] arr1 = Arrays.copyOf(arrays[i], arrays[i].length);
            int[] arr2 = Arrays.copyOf(arrays[i], arrays[i].length);
            
            comparisons = 0;
            movements = 0;
            long startTime = System.nanoTime();
            heapSort(arr1);
            long endTime = System.nanoTime();
            long heapSortTime = endTime - startTime;
            long heapSortTimeMs = heapSortTime / 1000000; // Tempo em milissegundos
            String heapSortFileBefore = "heapSort_before_" + size + "_" + i + ".txt";
            String heapSortFileAfter = "heapSort_after_" + size + "_" + i + ".txt";
            saveArrayToFile(heapSortFileBefore, arrays[i]);
            saveArrayToFile(heapSortFileAfter, arr1);
            
            comparisons = 0;
            movements = 0;
            startTime = System.nanoTime();
            mergeSort(arr2);
            endTime = System.nanoTime();
            long mergeSortTime = endTime - startTime;
            long mergeSortTimeMs = mergeSortTime / 1000000; // Tempo em milissegundos
            String mergeSortFileBefore = "mergeSort_before_" + size + "_" + i + ".txt";
            String mergeSortFileAfter = "mergeSort_after_" + size + "_" + i + ".txt";
            saveArrayToFile(mergeSortFileBefore, arrays[i]);
            saveArrayToFile(mergeSortFileAfter, arr2);
            
            System.out.printf("HeapSort\t%d\t\t%d\t\t%d%n", heapSortTimeMs, comparisons, movements);
            System.out.printf("MergeSort\t%d\t\t%d\t\t%d%n", mergeSortTimeMs, comparisons, movements);
        }
        
        System.out.println("--------------------------------------------------------");
        System.out.println();
    }

    private static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        
        comparisons++;
        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }
        
        comparisons++;
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }
        
        if (largest != i) {
            swap(arr, i, largest);
            movements++;
            
            heapify(arr, n, largest);
        }
    }

    public static void heapSort(int[] arr) {
        int n = arr.length;
        
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }
        
        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i);
            movements++;
            
            heapify(arr, i, 0);
        }
    }

    public static void mergeSort(int[] arr) {
        int n = arr.length;
        
        if (n < 2) {
            return;
        }
        
        int mid = n / 2;
        int[] left = new int[mid];
        int[] right = new int[n - mid];
        
        System.arraycopy(arr, 0, left, 0, mid);
        System.arraycopy(arr, mid, right, 0, n - mid);
        
        mergeSort(left);
        mergeSort(right);
        
        merge(arr, left, right);
    }

    private static void merge(int[] arr, int[] left, int[] right) {
        int n1 = left.length;
        int n2 = right.length;
        int i = 0, j = 0, k = 0;
        
        while (i < n1 && j < n2) {
            comparisons++;
            if (left[i] <= right[j]) {
                arr[k] = left[i];
                movements++;
                
                i++;
            } else {
                arr[k] = right[j];
                movements++;
                
                j++;
            }
            
            k++;
        }
        
        while (i < n1) {
            arr[k] = left[i];
            movements++;
            
            i++;
            k++;
        }
        
        while (j < n2) {
            arr[k] = right[j];
            movements++;
            
            j++;
            k++;
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    private static int[][] generateRandomArrays(int size, int repetitions) {
        int[][] arrays = new int[repetitions][size];
        
        Random random = new Random();
        
        for (int i = 0; i < repetitions; i++) {
            for (int j = 0; j < size; j++) {
                arrays[i][j] = random.nextInt();
            }
        }
        
        return arrays;
    }
    
    private static int[][] generateAlmostSortedArrays(int size, int repetitions) {
        int[][] arrays = new int[repetitions][size];
        
        Random random = new Random();
        
        for (int i = 0; i < repetitions; i++) {
            for (int j = 0; j < size; j++) {
                arrays[i][j] = j;
            }
            
            for (int j = 0; j < size / 10; j++) {
                int index1 = random.nextInt(size);
                int index2 = random.nextInt(size);
                swap(arrays[i], index1, index2);
            }
        }
        
        return arrays;
    }
    
    private static int[][] generateReversedArrays(int size, int repetitions) {
        int[][] arrays = new int[repetitions][size];
        
        for (int i = 0; i < repetitions; i++) {
            for (int j = 0; j < size; j++) {
                arrays[i][j] = size - j;
            }
        }
        
        return arrays;
    }
    
    private static void saveArrayToFile(String fileName, int[] arr) {
        try {
            FileWriter writer = new FileWriter(fileName);
            
            for (int i = 0; i < arr.length; i++) {
                writer.write(i + ": " + arr[i] + "\n");
            }
            
            writer.close();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao salvar o vetor em um arquivo.");
            e.printStackTrace();
        }
    }
}