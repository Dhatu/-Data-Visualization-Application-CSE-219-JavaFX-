/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw6files;

import dataprocessors.AppData;
import dataprocessors.TSDProcessor;
import hw4files.DataSet;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import ui.AppUI;
import vilij.templates.ApplicationTemplate;

/**
 *
 * @author sudhi
 */
public class RandomCluster extends Clusterer{
    private static final Random RAND = new Random();

    @SuppressWarnings("FieldCanBeLocal")
    // this mock classifier doesn't actually use the data, but a real classifier will
    private DataSet dataset;

    private final int maxIterations;
    private final int updateInterval;
    private final int clusternum;
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

    public RandomCluster(DataSet dataset,
                            int maxIterations,
                            int updateInterval,
                            boolean tocontinue,
                            int clusternum,
                            ApplicationTemplate app) {
        super(clusternum);
        this.dataset = dataset;
        this.maxIterations = maxIterations;
        this.updateInterval = updateInterval;
        this.clusternum = clusternum;
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
            processor.setCdataLabels(processor.getDataLabels());
            Random generator = new Random();

            ArrayList<String> clusterlabel = new ArrayList<String>();
            for ( int j=1; j <= clusternum; j++){
                String label = "label" + j;
                clusterlabel.add(label);
            }
            for ( int r = 0; r < processor.getClasInsta().size(); r++){
            String name = processor.getClasInsta().get(r);
            int rnd = generator.nextInt(clusterlabel.size());
            processor.getcDataLabels().replace(name, clusterlabel.get(rnd));
            }
            
            
            
            if(i % updateInterval == 0){
             Platform.runLater(() -> {
            try {
                processor.clear2(((AppUI) app.getUIComponent()).getChart());
                ((AppData) app.getDataComponent()).cdisplayData();
                 
            } catch (Exception ex) {
                Logger.getLogger(RandomCluster.class.getName()).log(Level.SEVERE, null, ex);
            }
                        
             });
            }
            } finally {
                if(Lock.isHeldByCurrentThread())
                    Lock.unlock();
            }

            // everything below is just for internal viewing of how the output is changing
            // in the final project, such changes will be dynamically visible in the UI
                    Thread.sleep(1000);
        } 
        } catch (InterruptedException x){
            Thread.currentThread().interrupt();
        }

    }
    

    public void nonrun() {
        TSDProcessor processor = ((AppData) app.getDataComponent()).getProcessor();
            processor.setCdataLabels(processor.getDataLabels());
            Random generator = new Random();

            ArrayList<String> clusterlabel = new ArrayList<String>();
            for ( int j=1; j <= clusternum; j++){
                String label = "label" + j;
                clusterlabel.add(label);
            }
            for ( int r = 0; r < processor.getClasInsta().size(); r++){
            String name = processor.getClasInsta().get(r);
            int rnd = generator.nextInt(clusterlabel.size());
            processor.getcDataLabels().replace(name, clusterlabel.get(rnd));
            }
            
            processor.clear2(((AppUI) app.getUIComponent()).getChart());
            if(((AppUI) app.getUIComponent()).getCount() % updateInterval == 0){
                         
            try {
                
                ((AppData) app.getDataComponent()).cdisplayData();

            } catch (Exception ex) {
                Logger.getLogger(RandomCluster.class.getName()).log(Level.SEVERE, null, ex);
            }
            }

            // everything below is just for internal viewing of how the output is changing
            // in the final project, such changes will be dynamically visible in the UI
            
                  
         

    }

    // for internal viewing only



}


