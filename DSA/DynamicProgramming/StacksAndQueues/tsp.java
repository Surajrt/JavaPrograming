import java.io.*;
import java.util.*;

public class AntColonyTSP {
    private static int numberOfCities;
    private static double[][] distances;
    private static double[][] pheromones;
    private static final double ALPHA = 1.0;  // Influence of pheromone
    private static final double BETA = 2.0;   // Influence of distance
    private static final double RHO = 0.5;    // Pheromone evaporation rate
    private static final double Q = 100;      // Pheromone deposition constant
    private static final int NUM_ANTS = 50;   // Number of ants
    private static final int NUM_ITERATIONS = 1000; // Number of iterations
    private static final double INITIAL_PHEROMONE = 1.0;

    private static Random random = new Random();

    public static void main(String[] args) throws IOException {
        // Load TSP data from file
        List<City> cities = readTSPFile("tsp_file.tsp");
        numberOfCities = cities.size();
        distances = new double[numberOfCities][numberOfCities];

        // Compute distances between cities
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {
                if (i != j) {
                    distances[i][j] = cities.get(i).distanceTo(cities.get(j));
                } else {
                    distances[i][j] = 0;
                }
            }
        }

        // Initialize pheromone levels
        pheromones = new double[numberOfCities][numberOfCities];
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {
                pheromones[i][j] = INITIAL_PHEROMONE;
            }
        }

        // Start the ACO algorithm
        List<Integer> bestTour = null;
        double bestTourLength = Double.MAX_VALUE;

        for (int iteration = 0; iteration < NUM_ITERATIONS; iteration++) {
            List<List<Integer>> allAntTours = new ArrayList<>();
            List<Double> allAntTourLengths = new ArrayList<>();

            // Each ant constructs a tour
            for (int ant = 0; ant < NUM_ANTS; ant++) {
                List<Integer> tour = constructTour();
                double tourLength = calculateTourLength(tour);
                allAntTours.add(tour);
                allAntTourLengths.add(tourLength);
            }

            // Find the best tour for this iteration
            for (int ant = 0; ant < NUM_ANTS; ant++) {
                double tourLength = allAntTourLengths.get(ant);
                if (tourLength < bestTourLength) {
                    bestTourLength = tourLength;
                    bestTour = allAntTours.get(ant);
                }
            }

            // Update pheromones based on the ants' tours
            updatePheromones(allAntTours, allAntTourLengths);

            System.out.println("Iteration " + iteration + " Best Length: " + bestTourLength);
        }

        // Output the best tour found
        System.out.println("Best tour: " + bestTour);
        System.out.println("Best tour length: " + bestTourLength);
    }

    // Reads TSP file (TSPLIB format)
    private static List<City> readTSPFile(String filename) throws IOException {
        List<City> cities = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        boolean readingCoordinates = false;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("NODE_COORD_SECTION")) {
                readingCoordinates = true;
                continue;
            }
            if (readingCoordinates) {
                if (line.startsWith("EOF")) break;
                String[] parts = line.split("\\s+");
                int id = Integer.parseInt(parts[0]);
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                cities.add(new City(id, x, y));
            }
        }
        reader.close();
        return cities;
    }

    // Represents a city with coordinates
    static class City {
        int id;
        double x, y;

        City(int id, double x, double y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        // Euclidean distance to another city
        double distanceTo(City other) {
            return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
        }
    }

    // Construct a tour for an ant
    private static List<Integer> constructTour() {
        List<Integer> tour = new ArrayList<>();
        Set<Integer> visitedCities = new HashSet<>();
        int currentCity = random.nextInt(numberOfCities);
        tour.add(currentCity);
        visitedCities.add(currentCity);

        while (tour.size() < numberOfCities) {
            int nextCity = selectNextCity(currentCity, visitedCities);
            tour.add(nextCity);
            visitedCities.add(nextCity);
            currentCity = nextCity;
        }
        return tour;
    }

    // Select the next city using a probability function based on pheromone levels and distance
    private static int selectNextCity(int currentCity, Set<Integer> visitedCities) {
        double[] probabilities = new double[numberOfCities];
        double sum = 0.0;

        for (int i = 0; i < numberOfCities; i++) {
            if (!visitedCities.contains(i)) {
                probabilities[i] = Math.pow(pheromones[currentCity][i], ALPHA) *
                                   Math.pow(1.0 / distances[currentCity][i], BETA);
                sum += probabilities[i];
            } else {
                probabilities[i] = 0;
            }
        }

        // Normalize probabilities
        for (int i = 0; i < numberOfCities; i++) {
            probabilities[i] /= sum;
        }

        // Select the next city based on the probabilities
        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < numberOfCities; i++) {
            cumulativeProbability += probabilities[i];
            if (rand <= cumulativeProbability) {
                return i;
            }
        }

        return -1; // This should never happen if probabilities are correctly normalized
    }

    // Calculate the length of a tour
    private static double calculateTourLength(List<Integer> tour) {
        double length = 0.0;
        for (int i = 0; i < tour.size() - 1; i++) {
            length += distances[tour.get(i)][tour.get(i + 1)];
        }
        length += distances[tour.get(tour.size() - 1)][tour.get(0)]; // Return to the starting city
        return length;
    }

    // Update pheromones based on the tours constructed by ants
    private static void updatePheromones(List<List<Integer>> allAntTours, List<Double> allAntTourLengths) {
        // Evaporate pheromones
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {
                pheromones[i][j] *= (1 - RHO);
            }
        }

        // Add pheromones based on the ant tours
        for (int ant = 0; ant < NUM_ANTS; ant++) {
            List<Integer> tour = allAntTours.get(ant);
            double tourLength = allAntTourLengths.get(ant);
            double pheromoneDeposit = Q / tourLength;

            for (int i = 0; i < tour.size() - 1; i++) {
                pheromones[tour.get(i)][tour.get(i + 1)] += pheromoneDeposit;
                pheromones[tour.get(i + 1)][tour.get(i)] += pheromoneDeposit; // Symmetric
            }
            pheromones[tour.get(tour.size() - 1)][tour.get(0)] += pheromoneDeposit; // Return to start
        }
    }
}
