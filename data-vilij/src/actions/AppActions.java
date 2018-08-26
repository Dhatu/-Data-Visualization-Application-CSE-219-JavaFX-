package actions;

import hw4files.DataSet;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import vilij.components.ActionComponent;
import vilij.templates.ApplicationTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import ui.AppUI;
import vilij.components.Dialog;
import vilij.components.ErrorDialog;

/**
 * This is the concrete implementation of the action handlers required by the application.
 *
 * @author Ritwik Banerjee
 */
public final class AppActions implements ActionComponent {
        

    /** The application to which this class of actions belongs. */
    private ApplicationTemplate applicationTemplate;

    /** Path to the data file currently active. */
    Path dataFilePath;
    private Window dialogStage;    
    public void start(Window dialogStage) {
    this.dialogStage = dialogStage;
    }
    public AppActions(ApplicationTemplate applicationTemplate) {
        this.applicationTemplate = applicationTemplate;
    }

    @Override
    public void handleNewRequest() {
        // TODO for homework 1
    }

    @Override
    public void handleSaveRequest() {
       
//        int forcounter = 0;
//         int dupcounter = 0;
//        String[] dupstring = ((AppUI) applicationTemplate.getUIComponent()).getTextArea().getText().split("\n");
//         for (int i = 0; i < dupstring.length-1; i++)
//        {
//            for (int j = i+1; j < dupstring.length; j++)
//            {
//                if( (dupstring[i].equals(dupstring[j])) && (i != j) )
//                {
//                    ((ErrorDialog) applicationTemplate.getDialog(Dialog.DialogType.ERROR)).init(applicationTemplate.getUIComponent().getPrimaryWindow());
//                    ((ErrorDialog) applicationTemplate.getDialog(Dialog.DialogType.ERROR)).show("ERROR Message", "DUPLICATE instance at" + dupcounter + "th Line)";
//                }
//            }
//        }
        


            if ( dataFilePath == null){
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TSD files (*.tsd)", "*.tsd");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showSaveDialog(applicationTemplate.getUIComponent().getPrimaryWindow());
                
                dataFilePath = file.toPath();
                
            }
            
            try (BufferedWriter writer = Files.newBufferedWriter(dataFilePath)) {
                writer.write(((AppUI) applicationTemplate.getUIComponent()).getTextArea().getText());
                
            } catch (IOException t) {
                System.out.println("savefile exception");
            }
            ((AppUI) applicationTemplate.getUIComponent()).setDisable();
        
                   //  fileChooser.showSaveDialog()
    }

    @Override
    public void handleLoadRequest() {
        // TODO: NOT A PART OF HW 1
        ((AppUI) applicationTemplate.getUIComponent()).setDisable();
        ((AppUI) applicationTemplate.getUIComponent()).getTextArea().setDisable(false);
        ((AppUI) applicationTemplate.getUIComponent()).getDisplayButton().setDisable(false);
        ((AppUI) applicationTemplate.getUIComponent()).getUndoreadonlyButton().setDisable(false);

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(applicationTemplate.getUIComponent().getPrimaryWindow());
        
        String filename = file.getAbsolutePath();
        
        BufferedReader reader; 
        try{
            reader = new BufferedReader(new FileReader(file));
       
        int counter = 0;
        int labelcounter = 0;

        ArrayList<String> labellist = new ArrayList<String>();
        String sCurrentLine ="";
        String whole = "";
			while ((sCurrentLine = reader.readLine()) != null && counter < 10) {
				whole = ((AppUI) applicationTemplate.getUIComponent()).getTextArea().getText().concat(sCurrentLine + "\n" );
                                ((AppUI) applicationTemplate.getUIComponent()).getTextArea().setText(whole);
                                String[] arrayref = sCurrentLine.split("\t");
                                    
                                
                                 boolean exists = false;
                                 for (int i = 0; i < labellist.size(); i++) {                                   
                                    if(arrayref[1].equals(labellist.get(i))){
                                       exists = true;
                                       break;
                                        } 
                                    }
                                 //labellist.add(arrayref[1]);
                                 if(exists == false){
                                     labellist.add(arrayref[1]);
                                     labelcounter++;
                                 }
                                 arrayref = new String[arrayref.length];
                                
                                counter++;
                        }

                        while ((sCurrentLine = reader.readLine()) != null){
                            counter++;
                        }

          if (counter >= 10){
             (applicationTemplate.getDialog(Dialog.DialogType.CONFIRMATION)).init(applicationTemplate.getUIComponent().getPrimaryWindow());
             (applicationTemplate.getDialog(Dialog.DialogType.CONFIRMATION)).show("Message", "Loaded data consists of " + counter +
                        " lines. Showing only the first 10 in the text area." );
                          
            }
          
          ((AppUI) applicationTemplate.getUIComponent()).getDataprop().setText( counter + " instances with " + labelcounter + " loaded from the " + filename + ". The labels are: " + labellist.toString() );
//          Label label = new Label("Which Algorithm Type?");
//          ((AppUI) applicationTemplate.getUIComponent()).setAlgoType(label);         

          if(labellist.size() == 2 && labellist.get(0) != null && labellist.get(1) != null) {
          ((AppUI) applicationTemplate.getUIComponent()).getClassification().setDisable(false);
          ((AppUI) applicationTemplate.getUIComponent()).getClassification().setText("Random Classification");
          ((AppUI) applicationTemplate.getUIComponent()).getClasAlgo().setDisable(false);
          ((AppUI) applicationTemplate.getUIComponent()).getClasAlgo().setText("Algorithm 1");
          }
          ((AppUI) applicationTemplate.getUIComponent()).getClustering().setDisable(false);
          ((AppUI) applicationTemplate.getUIComponent()).getClustering().setText("Random Clustering");
          ((AppUI) applicationTemplate.getUIComponent()).getClusAlgo().setDisable(false);
          ((AppUI) applicationTemplate.getUIComponent()).getClusAlgo().setText("Algorithm 1");
          
        }catch (Exception r){
            Logger.getLogger(AppActions.class.getName()).log(Level.SEVERE, null, r);
           ((ErrorDialog) applicationTemplate.getDialog(Dialog.DialogType.ERROR)).show("ERROR Message", "INVALID INPUT");
           ((AppUI) applicationTemplate.getUIComponent()).getTextArea().clear();
        }
        
    }

    @Override
    public void handleExitRequest() {
        
          if ( dataFilePath == null){
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TSD files (*.tsd)", "*.tsd");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showSaveDialog(applicationTemplate.getUIComponent().getPrimaryWindow());
                
                dataFilePath = file.toPath();
                
            }
          if ( ((AppUI) applicationTemplate.getUIComponent()).getThread().isAlive()){
           ((ErrorDialog) applicationTemplate.getDialog(Dialog.DialogType.ERROR)).init(applicationTemplate.getUIComponent().getPrimaryWindow());
           ((ErrorDialog) applicationTemplate.getDialog(Dialog.DialogType.ERROR)).show("ERROR Message", " During Algorithm Plotting");
            
          }
          
        System.exit(0);
        // TODO for homework 1
    }

    @Override
    public void handlePrintRequest() {
        // TODO: NOT A PART OF HW 1
    }

    public void handleScreenshotRequest() throws IOException {
          WritableImage image = ((AppUI) applicationTemplate.getUIComponent()).getChart().snapshot(new SnapshotParameters(), null);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
    // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
        "PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
    // Show save file dialog
        File file = fileChooser.showSaveDialog(this.dialogStage);
        if (file != null) {
    // Make sure it has the correct extension
        if (!file.getPath().endsWith(".png")) {
        file = new File(file.getPath() + ".png");
        }
        try {
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        }   catch (IOException e) {
// TODO: handle exception here
}
}
//File file = new File("chart.png");
}
        
//        FileChooser saveAsChooser = new FileChooser();
//        saveAsChooser.setTitle("Save Graph As...");
//        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("PNG files *.png", "*.png");
//        saveAsChooser.getExtensionFilters().add(filter);
//        File file = new File("chart.png");    
//        WritableImage graphImage = ((AppUI) applicationTemplate.getUIComponent()).getChart().snapshot(new SnapshotParameters(), null);
//         if (file != null) {
//             try {
//            ImageIO.write(SwingFXUtils.fromFXImage(graphImage, null), "png", file);
//            } catch (IOException e) {
//                e.printStackTrace(); }


        
        
        
        //WritableImage image = ((AppUI) applicationTemplate.getUIComponent()).getChart().snapshot(new SnapshotParameters(), null);

    // TODO: probably use a file chooser here
//        File file = new File("chart.png");
//
//        try {
//            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//        } catch (IOException e) { }
        // TODO: handle exception here
          // TODO: NOT A PART OF HW 1
    

    /**
     * This helper method verifies that the user really wants to save their unsaved work, which they might not want to
     * do. The user will be presented with three options:
     * <ol>
     * <li><code>yes</code>, indicating that the user wants to save the work and continue with the action,</li>
     * <li><code>no</code>, indicating that the user wants to continue with the action without saving the work, and</li>
     * <li><code>cancel</code>, to indicate that the user does not want to continue with the action, but also does not
     * want to save the work at this point.</li>
     * </ol>
     *
     * @return <code>false</code> if the user presses the <i>cancel</i>, and <code>true</code> otherwise.
     */
    private boolean promptToSave() throws IOException {
        FileChooser fileChooser = new FileChooser();
  
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Tab-Separated Data (.*.tsd)", "*.tsd");
        fileChooser.getExtensionFilters().add(extFilter);
              
        //File file = fileChooser.showSaveDialog(primaryStage);
        // TODO for homework 1
        // TODO remove the placeholder line below after you have implemented this method
        return false;
    }
}
