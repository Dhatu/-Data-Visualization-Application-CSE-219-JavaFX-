package hw6files;
import dataprocessors.AppData;
import hw4files.DataSet;
import hw4files.RandomClassifier;
import javafx.geometry.Point2D;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.application.Platform;
import ui.AppUI;
import vilij.templates.ApplicationTemplate;


/**
 *
 * @author sudhi
 */
public class KMeansClusterer extends Clusterer{

    private DataSet       dataset;

    public DataSet getDataset() {
        return dataset;
    }
    private List<Point2D> centroids;

    private final int           maxIterations;
    private final int           updateInterval;
    private final AtomicBoolean tocontinue;
    private ApplicationTemplate app = new ApplicationTemplate();
    ReentrantLock Lock = new ReentrantLock();




    public KMeansClusterer(DataSet dataset, int maxIterations, int updateInterval, int numberOfClusters, ApplicationTemplate app) {
        super(numberOfClusters);
        this.dataset = dataset;
        this.maxIterations = maxIterations;
        this.updateInterval = updateInterval;
        this.tocontinue = new AtomicBoolean(false);
        this.app = app;
    }



    @Override
    public int getMaxIterations() { return maxIterations; }

    @Override
    public int getUpdateInterval() { return updateInterval; }

    @Override
    public boolean tocontinue() { return tocontinue.get(); }

    public void nonrun() {
        initializeCentroids();
        int iteration = 0;
        String finalstring = "";
        while (iteration++ < maxIterations & tocontinue.get()) {
            assignLabels();
            recomputeCentroids();
            
        List<String> instanceNames = new ArrayList<>(dataset.getLabels().keySet());
        for (int n=0; n < instanceNames.size(); n++){
        finalstring = finalstring + instanceNames.get(n) + "\t" + dataset.getLabels().get(instanceNames.get(n)) + "\t" + dataset.getLocations().get(instanceNames.get(n)).getX()
                + "," + dataset.getLocations().get(instanceNames.get(n)).getX() + "\n";
             }
        System.out.println(finalstring);
        if(((AppUI) app.getUIComponent()).getCount() % updateInterval == 0){
            try {
                ((AppData) app.getDataComponent()).loadData(finalstring);
            } catch (Exception ex) {
                Logger.getLogger(KMeansClusterer.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        }
    }
    @Override
    public void run() {
        try {
        initializeCentroids();
        int iteration = 0;
        
        while (iteration++ < maxIterations & tocontinue.get()) {
            Lock.lock();
            try {
            System.out.println("Test");   
            assignLabels();
            recomputeCentroids();
           
         
        
         Platform.runLater(() -> {
            try {
                String finalstring = "";
                List<String> instanceNames = new ArrayList<>(dataset.getLabels().keySet());
                for (int n=0; n < instanceNames.size(); n++){
                    finalstring = finalstring + instanceNames.get(n) + "\t" + dataset.getLabels().get(instanceNames.get(n)) + "\t" + dataset.getLocations().get(instanceNames.get(n)).getX()
                    + "," + dataset.getLocations().get(instanceNames.get(n)).getY() + "\n";
                }
                System.out.println(finalstring);
                ((AppData) app.getDataComponent()).loadData(finalstring);
            } catch (Exception ex) {
                Logger.getLogger(RandomClassifier.class.getName()).log(Level.SEVERE, null, ex);
            }                        
             });  
          
            } finally {
                if(Lock.isHeldByCurrentThread())
                    Lock.unlock();
            }
           Thread.sleep(2000);
        }
        } catch (InterruptedException x){
            Thread.currentThread().interrupt();
        }
        
    }
    private void initializeCentroids() {
        Set<String>  chosen        = new HashSet<>();
        List<String> instanceNames = new ArrayList<>(dataset.getLabels().keySet());
        Random       r             = new Random();
        while (chosen.size() < numberOfClusters) {
            int i = r.nextInt(instanceNames.size());
            while (chosen.contains(instanceNames.get(i)))
                ++i;
            chosen.add(instanceNames.get(i));
        }
        centroids = chosen.stream().map(name -> dataset.getLocations().get(name)).collect(Collectors.toList());
        tocontinue.set(true);
    }

    private void assignLabels() {
        dataset.getLocations().forEach((instanceName, location) -> {
            double minDistance      = Double.MAX_VALUE;
            int    minDistanceIndex = -1;
            for (int i = 0; i < centroids.size(); i++) {
                double distance = computeDistance(centroids.get(i), location);
                if (distance < minDistance) {
                    minDistance = distance;
                    minDistanceIndex = i;
                }
            }
            dataset.getLabels().put(instanceName, Integer.toString(minDistanceIndex));
        });
    }

    private void recomputeCentroids() {
        tocontinue.set(false);
        IntStream.range(0, numberOfClusters).forEach(i -> {
            AtomicInteger clusterSize = new AtomicInteger();
            Point2D sum = dataset.getLabels()
                                 .entrySet()
                                 .stream()
                                 .filter(entry -> i == Integer.parseInt(entry.getValue()))
                                 .map(entry -> dataset.getLocations().get(entry.getKey()))
                                 .reduce(new Point2D(0, 0), (p, q) -> {
                                     clusterSize.incrementAndGet();
                                     return new Point2D(p.getX() + q.getX(), p.getY() + q.getY());
                                 });
            Point2D newCentroid = new Point2D(sum.getX() / clusterSize.get(), sum.getY() / clusterSize.get());
            if (!newCentroid.equals(centroids.get(i))) {
                centroids.set(i, newCentroid);
                tocontinue.set(true);
            }
        });
    }

    private static double computeDistance(Point2D p, Point2D q) {
        return Math.sqrt(Math.pow(p.getX() - q.getX(), 2) + Math.pow(p.getY() - q.getY(), 2));
    }
    
}

