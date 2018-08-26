
package hw4files;

import dataprocessors.AppData;
import dataprocessors.TSDProcessor;
import hw4files.DataSet;
import hw4files.Classifier;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import ui.AppUI;
import vilij.templates.ApplicationTemplate;

/**
 * @author Ritwik Banerjee
 */
public class RandomClassifier extends Classifier {

    private static final Random RAND = new Random();

    @SuppressWarnings("FieldCanBeLocal")
    // this mock classifier doesn't actually use the data, but a real classifier will
    private DataSet dataset;

    private final int maxIterations;
    private final int updateInterval;
    private ApplicationTemplate app = new ApplicationTemplate();
   
    ReentrantLock Lock = new ReentrantLock();
    // currently, this value does not change after instantiation
    private final AtomicBoolean tocontinue;

    @Override
    public int getMaxIterations() {
        return maxIterations;
    }

    @Override
    public int getUpdateInterval() {
        return updateInterval;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean tocontinue() {
        return tocontinue.get();
    }

    public RandomClassifier(DataSet dataset,
                            int maxIterations,
                            int updateInterval,
                            boolean tocontinue,
                            ApplicationTemplate app) {
        this.dataset = dataset;
        this.maxIterations = maxIterations;
        this.updateInterval = updateInterval;
        this.tocontinue = new AtomicBoolean(tocontinue);
        this.app = app;
    }

    @Override
    public void run() {
        
        try {
        for (int i = 1; i <= maxIterations && tocontinue(); i++) {
            Lock.lock();
            try {
            TSDProcessor processor = ((AppData) app.getDataComponent()).getProcessor();
            int xCoefficient =  new Long(-1 * Math.round((2 * RAND.nextDouble() - 1) * 10)).intValue();
            int yCoefficient = 10;
            int constant     = RAND.nextInt(11);
            
            // this is the real output of the classifier
             output = Arrays.asList(xCoefficient, yCoefficient, constant);
             xCoefficient = xCoefficient * -1;

             int y1;

            
          //  y1 = (int) ((( dataset.getLocations().get(((AppData) app.getDataComponent()).getProcessor().getClasInsta().get(0)).getX() * xCoefficient ) - constant )/ yCoefficient);
          int x1 = (int) processor.getDataPoints().get(processor.getClasInsta().get(0)).getX();
          y1 = ((x1*xCoefficient)-constant)/yCoefficient;
          int x2 = (int) processor.getDataPoints().get(processor.getClasInsta().get(1)).getX();
          int y2 = ((x2*xCoefficient)-constant)/yCoefficient;
             
             //
             String getLabels1 = processor.getDataLabels().get(processor.getClasInsta().get(0));
             String Xpoints1 = Integer.toString(x1);
             String Ypoints1 = Integer.toString(y1);
             
             //
             String getLabel2 = processor.getDataLabels().get(processor.getClasInsta().get(1));
             String Xpoint2 = Integer.toString(x2);
             String Ypoints2 = Integer.toString(y2);
             
             String finalstring = "@aewsth" + "\t" + "random" + "\t" + Xpoints1 + "," + Ypoints1 + "\n" + "@ftyhj" + "\t" + "random" + "\t" + Xpoint2 + "," + Ypoints2;
            

            if(i % updateInterval == 0){
             Platform.runLater(() -> {
            try {
                processor.clear2(((AppUI) app.getUIComponent()).getChart());
                ((AppData) app.getDataComponent()).loadData(finalstring);
                 
            } catch (Exception ex) {
                Logger.getLogger(RandomClassifier.class.getName()).log(Level.SEVERE, null, ex);
            }
                        
             });
            } 

            } finally {
                if(Lock.isHeldByCurrentThread())
                    Lock.unlock();
            }

            // everything below is just for internal viewing of how the output is changing
            // in the final project, such changes will be dynamically visible in the UI
            if (i % updateInterval == 0) {
                System.out.printf("Iteration number %d: ", i); //
                flush();
            }
            if (i > maxIterations * .6 && RAND.nextDouble() < 0.05) {
                System.out.printf("Iteration number %d: ", i);
                flush();
                break;
            }
                    Thread.sleep(2000);
        } 
        } catch (InterruptedException x){
            Thread.currentThread().interrupt();
        }

    }
    

    public void nonrun() {
        
            
            TSDProcessor processor = ((AppData) app.getDataComponent()).getProcessor();
            int xCoefficient =  new Long(-1 * Math.round((2 * RAND.nextDouble() - 1) * 10)).intValue();
            int yCoefficient = 10;
            int constant     = RAND.nextInt(11);
            
            // this is the real output of the classifier
             output = Arrays.asList(xCoefficient, yCoefficient, constant);
             xCoefficient = xCoefficient * -1;

             int y1;

            
          //  y1 = (int) ((( dataset.getLocations().get(((AppData) app.getDataComponent()).getProcessor().getClasInsta().get(0)).getX() * xCoefficient ) - constant )/ yCoefficient);
          int x1 = (int) processor.getDataPoints().get(processor.getClasInsta().get(0)).getX();
          y1 = ((x1*xCoefficient)-constant)/yCoefficient;
          int x2 = (int) processor.getDataPoints().get(processor.getClasInsta().get(1)).getX();
          int y2 = ((x2*xCoefficient)-constant)/yCoefficient;
             
             //
             String getLabels1 = processor.getDataLabels().get(processor.getClasInsta().get(0));
             String Xpoints1 = Integer.toString(x1);
             String Ypoints1 = Integer.toString(y1);
             
             //
             String getLabel2 = processor.getDataLabels().get(processor.getClasInsta().get(1));
             String Xpoint2 = Integer.toString(x2);
             String Ypoints2 = Integer.toString(y2);
             
             String finalstring = "@aewsth" + "\t" + "random" + "\t" + Xpoints1 + "," + Ypoints1 + "\n" + "@ftyhj" + "\t" + "random" + "\t" + Xpoint2 + "," + Ypoints2;
             processor.clear2(((AppUI) app.getUIComponent()).getChart());
             
             
            if(((AppUI) app.getUIComponent()).getCount() % updateInterval == 0){
            try {
                ((AppData) app.getDataComponent()).loadData(finalstring);
            } catch (Exception ex) {
                Logger.getLogger(RandomClassifier.class.getName()).log(Level.SEVERE, null, ex);
            }
            }

            // everything below is just for internal viewing of how the output is changing
            // in the final project, such changes will be dynamically visible in the UI
            if (((AppUI) app.getUIComponent()).getCount() % updateInterval == 0) {
                System.out.printf("Iteration number %d: ", ((AppUI) app.getUIComponent()).getCount()); //
                flush();
            }
            if (((AppUI) app.getUIComponent()).getCount() > maxIterations * .6 && RAND.nextDouble() < 0.05) {
                System.out.printf("Iteration number %d: ", ((AppUI) app.getUIComponent()).getCount());
                flush();
            }
                  
         

    }

    // for internal viewing only
    protected void flush() {
        System.out.printf("%d\t%d\t%d%n", output.get(0), output.get(1), output.get(2));
    }

    public static void main(String... args) throws IOException {
        DataSet          dataset    = DataSet.fromTSDFile(Paths.get("/path/to/some-data.tsd"));
    }
}
