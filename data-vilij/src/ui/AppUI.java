package ui;

import actions.AppActions;
import dataprocessors.AppData;
import hw4files.Algorithm;
import hw4files.DataSet;
import hw4files.RandomClassifier;
import hw6files.KMeansClusterer;
import hw6files.RandomCluster;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import static java.io.File.separator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.text.Text;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import static javafx.scene.paint.Color.GREEN;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JRadioButton;
import static settings.AppPropertyTypes.READONLY_TOOLTIP;
import static settings.AppPropertyTypes.SCREENSHOT_ICON;
import static settings.AppPropertyTypes.SCREENSHOT_TOOLTIP;
import vilij.components.ConfirmationDialog;
import vilij.components.Dialog;
import vilij.components.ErrorDialog;
import vilij.propertymanager.PropertyManager;
import vilij.settings.PropertyTypes;
import static vilij.settings.PropertyTypes.EXIT_TOOLTIP;
import static vilij.settings.PropertyTypes.GUI_RESOURCE_PATH;
import static vilij.settings.PropertyTypes.ICONS_RESOURCE_PATH;
import static vilij.settings.PropertyTypes.LOAD_TOOLTIP;
import static vilij.settings.PropertyTypes.NEW_TOOLTIP;
import static vilij.settings.PropertyTypes.PRINT_ICON;
import static vilij.settings.PropertyTypes.PRINT_TOOLTIP;
import static vilij.settings.PropertyTypes.SAVE_ICON;
import static vilij.settings.PropertyTypes.SAVE_TOOLTIP;
import vilij.templates.ApplicationTemplate;
import vilij.templates.UITemplate;

/**
 * This is the application's user interface implementation.
 *
 * @author Ritwik Banerjee
 */



public final class AppUI extends UITemplate {

        
    
    /** The application to which this class of actions belongs. */
    ApplicationTemplate applicationTemplate;
    Path dataFilePath;


    @SuppressWarnings("FieldCanBeLocal")
    private Button                       scrnshotButton; // toolbar button to take a screenshot of the data
    private Button                       validityButton;
    private Button                       undoreadonlyButton;
    private Button                       Classification;
    private RadioButton                  ClasAlgo;
    private RadioButton                  kClusAlgo;

    public RadioButton getkClusAlgo() {
        return kClusAlgo;
    }
    private Button                       Run;
    private TextField Maxt = new TextField();
    private TextField updatet= new TextField();
    private TextField contrunt = new TextField();
    private TextField clusterst = new TextField();
    private CheckBox checkbox = new CheckBox("Continous Run?:");
    private TextField cMaxt = new TextField();
    private TextField cupdatet= new TextField();
    private TextField ccontrunt = new TextField();
    private CheckBox ccheckbox = new CheckBox("Continous Run?:");
    
    

    public Button getClassification() {
        return Classification;
    }

    public Button getClustering() {
        return Clustering;
    }
    private Button                      Clustering;
    private RadioButton                 ClusAlgo;

    public RadioButton getClasAlgo() {
        return ClasAlgo;
    }

    public Button getRun() {
        return Run;
    }

    public RadioButton getClusAlgo() {
        return ClusAlgo;
    }

    public Button getUndoreadonlyButton() {
        return undoreadonlyButton;
    }
    public Button getScrnshotButton() {
        return scrnshotButton;
    }
    private String                       scrnshotPath;
    private LineChart<Number, Number> chart;          // the chart where data will be displayed
    private Button                       displayButton;  // workspace button to display data on the chart
    private TextArea                     textArea;       // text area for new data input
    private Text                         dataprop;
    private Button                       clasettings;
    private Button                       clusettings;
    private Button                       kclusettings;
    private Label                        AlgoType;
    private Task                         t;
    private Thread                       thread;
    private int                          count = 0;

    public int getCount() {
        return count;
    }
    private int                          MaxNum;
    private int                          UpdateNum;
    private int                          clusternum;
    
    public Thread getThread() {
        
        return thread;
    }

    public Task getT() {
        return t;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public void setAlgoType(Label AlgoType) {
        this.AlgoType = AlgoType;
    }
    public void setDisable(){
        saveButton.setDisable(true);
    }
    private boolean                      hasNewText;     // whether or not the text area has any new data since last display

    public LineChart<Number, Number> getChart() { return chart; }

    public AppUI(Stage primaryStage, ApplicationTemplate applicationTemplate) {
        super(primaryStage, applicationTemplate);
        
        this.applicationTemplate = applicationTemplate;
    }

    @Override
    protected void setResourcePaths(ApplicationTemplate applicationTemplate) {
        super.setResourcePaths(applicationTemplate);
        PropertyManager manager = applicationTemplate.manager;
            String iconsPath = "/" + String.join(separator,
                          manager.getPropertyValue(GUI_RESOURCE_PATH.name()),
                          manager.getPropertyValue(ICONS_RESOURCE_PATH.name()));

       scrnshotPath = String.join(separator, iconsPath, 
                              manager.getPropertyValue(SCREENSHOT_ICON.name()));
       
    }

    @Override
    protected void setToolBar(ApplicationTemplate applicationTemplate) 
    {
        // TODO for homework 1
      PropertyManager manager = applicationTemplate.manager;
      newButton = setToolbarButton(newiconPath, 
              manager.getPropertyValue(NEW_TOOLTIP.name()), true);
      saveButton = setToolbarButton(saveiconPath, 
              manager.getPropertyValue(SAVE_TOOLTIP.name()), true);
      loadButton = setToolbarButton(loadiconPath, 
              manager.getPropertyValue(LOAD_TOOLTIP.name()), false);
      printButton = setToolbarButton(printiconPath, 
                    manager.getPropertyValue(PRINT_TOOLTIP.name()), true);
      exitButton = setToolbarButton(exiticonPath, 
                    manager.getPropertyValue(EXIT_TOOLTIP.name()), false);

      scrnshotButton = setToolbarButton(scrnshotPath, 
                    manager.getPropertyValue(SCREENSHOT_TOOLTIP.name()), true);
      toolBar = new ToolBar(newButton, saveButton, loadButton, printButton, 
                            exitButton, scrnshotButton);

          
            
    }

    @Override
    protected void setToolbarHandlers(ApplicationTemplate applicationTemplate) {
        applicationTemplate.setActionComponent(new AppActions(applicationTemplate));
        newButton.setOnAction(e -> applicationTemplate.getActionComponent().handleNewRequest());
        saveButton.setOnAction(e -> applicationTemplate.getActionComponent().handleSaveRequest());
        loadButton.setOnAction(e -> applicationTemplate.getActionComponent().handleLoadRequest());
        exitButton.setOnAction(e -> applicationTemplate.getActionComponent().handleExitRequest());
        printButton.setOnAction(e -> applicationTemplate.getActionComponent().handlePrintRequest());
        //displayButton.setOnAction(e -> applicationTemplate.getActionComponent().handleDisplayRequest());
        
    }

    @Override
    public void initialize() {
        layout();
        setWorkspaceActions();
    }

    @Override
    public void clear() {
        // TODO for homework 
        try{
            ((AppData) applicationTemplate.getDataComponent()).clear();
                } catch( Exception e){
                    
                }
    }

    private void layout() {
        //scrnshotButton = setToolbarButton(newiconPath, manager.getPropertyValue(NEW_TOOLTIP.name()), true);
        primaryStage.setTitle("Data ViLiJ");
        //textArea.setDisable(true);
        
        final NumberAxis xAxis = new NumberAxis();

        xAxis.setTickLabelFill(Paint.valueOf("#e2983d"));
        

        
        final NumberAxis yAxis = new NumberAxis();        

        yAxis.setTickLabelFill(Paint.valueOf("#e2983d"));
        
        textArea = new TextArea();
        dataprop = new Text();
        AlgoType = new Label();
        Classification = new Button();
        Clustering = new Button();
        ClasAlgo = new RadioButton();
        ClusAlgo = new RadioButton();
        kClusAlgo = new RadioButton();
        clasettings = new Button("Random Classification Settings");
        clusettings = new Button("Random Clustering Settings");
        kclusettings = new Button ("K Means Clusterer Settings");
        
        displayButton = new Button("Display");
        validityButton = new Button("Finished? (Validity Check)");

        undoreadonlyButton = new Button("Undo R.O.");
        chart = new LineChart<Number,Number>( xAxis ,yAxis);
        chart.setPadding(new Insets(10));

        //chart.setBorder(new Border(20));
          
        workspace = new VBox();
        VBox graph = new VBox();
        Run = new Button("Run");
        


        workspace.getChildren().add(chart);
        workspace.getChildren().add(AlgoType);
        workspace.getChildren().add(dataprop);

        
        appPane.getChildren().add(workspace);

        chart.setStyle("-fx-stroke:black;");
        
//        Series series = new Series();
//        chart.getData().add(series);
//
//        Node line = series.getNode().lookup(".chart-series-line");
//
//        Color color = Color.RED; // or any other color
//
//        String rgb = String.format("%d, %d, %d",
//        (int) (color.getRed() * 255),
//        (int) (color.getGreen() * 255),
//        (int) (color.getBlue() * 255));
//
//        line.setStyle("-fx-stroke: rgba(" + rgb + ", 2.0);");
        
        chart.setTitle("Data Visualization");
        
        chart.setAlternativeRowFillVisible(false);
        chart.setAlternativeColumnFillVisible(false);
        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);
 
        chart.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"), 
            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(10))));
       
       Map<String, String> dlref = ((AppData) applicationTemplate.getDataComponent()).getProcessor().getDataLabels();
       Map<String, Point2D> dataref = ((AppData) applicationTemplate.getDataComponent()).getProcessor().getDataPoints();
       

       EventHandler<? super MouseEvent> onMouseEnteredSeriesListener = 
           (MouseEvent event) -> {
                ((Node)(event.getSource())).setCursor(Cursor.HAND);
            };
       EventHandler<? super MouseEvent> onMouseExitedSeriesListener = 
          (MouseEvent event) -> {
               
             ((Node)(event.getSource())).setCursor(Cursor.DEFAULT);
                };
      //for(int i=0; i <= ((AppData) applicationTemplate.getDataComponent()).getProcessor().getCounter(); i++) {
          
       chart.setOnMouseEntered(onMouseEnteredSeriesListener);
       chart.setOnMouseExited(onMouseExitedSeriesListener);
       //        }
       //make y average line

       
        // TODO for homework 1
    }

    private void setWorkspaceActions() {
       
        
        newButton.setDisable(false);
        saveButton.setDisable(false);
        
        Run.setOnAction(e -> {
            
        if( !cMaxt.getText().isEmpty() && !cupdatet.getText().isEmpty() ){
          DataSet algodata = new DataSet();
         algodata = ((AppData) applicationTemplate.getDataComponent()).getProcessor().getData();
          MaxNum = Integer.parseInt(cMaxt.getText());
           UpdateNum = Integer.parseInt(cupdatet.getText()); 
          
           RandomClassifier random = new RandomClassifier(algodata , MaxNum , UpdateNum ,true, applicationTemplate);
           if ( ccheckbox.isSelected()) {
                if( count  <= MaxNum){
               scrnshotButton.setDisable(true);
              Run.setDisable(true);
             t = new Task() {
                @Override
                protected Object call() throws Exception {
                                random.run();
                                scrnshotButton.setDisable(false);
                                Run.setDisable(true);
                                return null;
                }
            };
                
            thread = new Thread(t);
            thread.start();
                 count += UpdateNum;
                }
            } else {
                if( count <= MaxNum){
                random.nonrun();
                Service<Void> service = new Service<Void>() {                   
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            Thread.sleep(1000);//Waiting time
                            return null;
                        }
                    };
                }
            };
            scrnshotButton.disableProperty().bind(service.runningProperty()); 
            Run.disableProperty().bind(service.runningProperty());
            service.start();
            count += UpdateNum;
            System.out.println(count);
            }
                //scrnshotButton.setDisable(false);
            }
        }
            if( !Maxt.getText().isEmpty() && !updatet.getText().isEmpty() && !clusterst.getText().isEmpty()  && ClusAlgo.isSelected() ){
                workspace.getStylesheets().add(this.getClass().getResource("/gui/icons/chart.css").toExternalForm());
                DataSet  algodata = new DataSet();
                algodata = ((AppData) applicationTemplate.getDataComponent()).getProcessor().getData();
                MaxNum = Integer.parseInt(Maxt.getText());
                UpdateNum = Integer.parseInt(updatet.getText()); 
                clusternum = Integer.parseInt(clusterst.getText()); 
            //KMeansClusterer  clusteralgo = new KMeansClusterer(algodata,MaxNum,UpdateNum , clusternum, applicationTemplate);
            RandomCluster clusteralgo = new RandomCluster(algodata,MaxNum,UpdateNum, true, clusternum, applicationTemplate);
            if ( checkbox.isSelected()) {
                 if( count  <= MaxNum){
                scrnshotButton.setDisable(true);
                Run.setDisable(true);
                t = new Task() {
                @Override
                protected Object call() throws Exception {
                                clusteralgo.run();
                                scrnshotButton.setDisable(false);
                                Run.setDisable(true);
                                return null;
                }
            };
                
            thread = new Thread(t);
            thread.start();
                 count += UpdateNum;
                }
            } else {
                if( count <= MaxNum){
                clusteralgo.nonrun();
                Service<Void> service = new Service<Void>() {                   
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            Thread.sleep(1000);//Waiting time
                            return null;
                        }
                    };
               }
            };
            scrnshotButton.disableProperty().bind(service.runningProperty()); 
            Run.disableProperty().bind(service.runningProperty());
            service.start();
            count += UpdateNum;
            System.out.println(count);
            }
            }
            //thread1.start();
            }
            if( !Maxt.getText().isEmpty() && !updatet.getText().isEmpty() && !clusterst.getText().isEmpty() && kClusAlgo.isSelected()){
                workspace.getStylesheets().add(this.getClass().getResource("/gui/icons/chart.css").toExternalForm());
                DataSet  algodata = new DataSet();
                algodata = ((AppData) applicationTemplate.getDataComponent()).getProcessor().getData();
                MaxNum = Integer.parseInt(Maxt.getText());
                UpdateNum = Integer.parseInt(updatet.getText()); 
                clusternum = Integer.parseInt(clusterst.getText()); 
            KMeansClusterer  kclusteralgo = new KMeansClusterer(algodata,MaxNum,UpdateNum , clusternum, applicationTemplate);
            //RandomCluster clusteralgo = new RandomCluster(algodata,MaxNum,UpdateNum, true, clusternum, applicationTemplate);
            if ( checkbox.isSelected()) {
                 if( count  <= MaxNum){
                scrnshotButton.setDisable(true);
                Run.setDisable(true);
                t = new Task() {
                @Override
                protected Object call() throws Exception {
                                kclusteralgo.run();
                                scrnshotButton.setDisable(false);
                                Run.setDisable(true);
                                return null;
                }
            };
                
            thread = new Thread(t);
            thread.start();
                 count += UpdateNum;
                }
            } else {
                if( count <= MaxNum){
                kclusteralgo.nonrun();
                Service<Void> service = new Service<Void>() {                   
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            Thread.sleep(1000);//Waiting time
                            return null;
                        }
                    };
               }
            };
            scrnshotButton.disableProperty().bind(service.runningProperty()); 
            Run.disableProperty().bind(service.runningProperty());
            service.start();
            count += UpdateNum;
            System.out.println(count);
            }
            }
            //thread1.start();
            }
        });
        
        Classification.setOnAction(e -> {
            if( workspace.getChildren().contains(ClasAlgo) == false){
            workspace.getChildren().add(ClasAlgo);
            }
        });
        
        Clustering.setOnAction(e -> {
            if( workspace.getChildren().contains(ClusAlgo) == false && workspace.getChildren().contains(kClusAlgo) == false){
            workspace.getChildren().add(ClusAlgo);
            workspace.getChildren().add(kClusAlgo);
            }
            
        });
       
        ClasAlgo.setOnAction(e -> {
           if( workspace.getChildren().contains(clasettings) == false){
            workspace.getChildren().add(clasettings);
            }
           if( workspace.getChildren().contains(Run) == false){
            workspace.getChildren().add(Run);
            Run.setDisable(true);
            }
           
        });
        
        ClusAlgo.setOnAction(e -> {
           if( workspace.getChildren().contains(clusettings) == false){
            workspace.getChildren().add(clusettings);
            }
           if( workspace.getChildren().contains(Run) == false){
            workspace.getChildren().add(Run);
            Run.setDisable(true);
            }
           
        });
        
        kClusAlgo.setOnAction(e -> {
            if( workspace.getChildren().contains(kclusettings) == false){
            workspace.getChildren().add(kclusettings);
            }
            if( workspace.getChildren().contains(Run) == false){
            workspace.getChildren().add(Run);
            Run.setDisable(true);
            }
        });
        
        clasettings.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
 
                VBox root = new VBox();
                root.setPadding(new Insets(50,50,50,50));
 
                 // New window (Stage)
                Stage newWindow = new Stage();
                newWindow.setTitle("Algorithm Run Configuration");
 
                // Set position of second window, related to primary window.
                newWindow.setX(primaryStage.getX() + 300);
                newWindow.setY(primaryStage.getY() + 100);
                
                Label Max = new Label("Max. Iterations:");
                Label update = new Label("Update Interval:");
                Label contrun = new Label("Continous Run?");
                Label settings = new Label("Click Settings again to Run");

                root.getChildren().add(Max);
                root.getChildren().add(cMaxt);
                root.getChildren().add(update);
                root.getChildren().add(cupdatet);
                root.getChildren().add(ccheckbox);
                root.getChildren().add(settings);
                
                        
                Scene secondScene = new Scene(root, 300, 300);
                newWindow.setScene(secondScene);
                newWindow.show();
                
                if( !cMaxt.getText().isEmpty() && !cupdatet.getText().isEmpty() ){
                     Run.setDisable(false);
                } else {
                    Run.setDisable(true);
                }
            }
        });

        kclusettings.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
 
                VBox root = new VBox();
                root.setPadding(new Insets(50,50,50,50));
 
                 // New window (Stage)
                Stage newWindow = new Stage();
                newWindow.setTitle(" K Means Algorithm Run Configuration");
 
                // Set position of second window, related to primary window.
                newWindow.setX(primaryStage.getX() + 300);
                newWindow.setY(primaryStage.getY() + 100);
                
                Label Max = new Label("Max. Iterations:");
                Label update = new Label("Update Interval:");
                Label contrun = new Label("Continous Run?:");
                Label clusters = new Label("# of Clusters:");
                Label settings = new Label("Click Settings again to Run");
                
                root.getChildren().add(Max);
                root.getChildren().add(Maxt);
                root.getChildren().add(update);
                root.getChildren().add(updatet);
//                root.getChildren().add(contrun);
//                root.getChildren().add(contrunt);
                root.getChildren().add(clusters);
                root.getChildren().add(clusterst);
                root.getChildren().add(checkbox);
                root.getChildren().add(settings);

                        
                Scene secondScene = new Scene(root, 300, 300);
                newWindow.setScene(secondScene);
                newWindow.show();
            
            if( !Maxt.getText().isEmpty() && !updatet.getText().isEmpty() && !clusterst.getText().isEmpty() ){
                     Run.setDisable(false);
                } else {
                    Run.setDisable(true);
                } 
                
            }
        });
        
        
        
        
        
        clusettings.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
 
                VBox root = new VBox();
                root.setPadding(new Insets(50,50,50,50));
 
                 // New window (Stage)
                Stage newWindow = new Stage();
                newWindow.setTitle("Random Algorithm Run Configuration");
 
                // Set position of second window, related to primary window.
                newWindow.setX(primaryStage.getX() + 300);
                newWindow.setY(primaryStage.getY() + 100);
                
                Label Max = new Label("Max. Iterations:");
                
                Label update = new Label("Update Interval:");
                Label contrun = new Label("Continous Run?:");
                Label clusters = new Label("# of Clusters:");
                Label settings = new Label("Click Settings again to Run");

                root.getChildren().add(Max);
                root.getChildren().add(Maxt);
                root.getChildren().add(update);
                root.getChildren().add(updatet);
                root.getChildren().add(clusters);
                root.getChildren().add(clusterst);
                root.getChildren().add(checkbox);
                root.getChildren().add(settings);

                
                        
                Scene secondScene = new Scene(root, 300, 300);
                newWindow.setScene(secondScene);
                newWindow.show();
            
             if( !Maxt.getText().isEmpty() && !updatet.getText().isEmpty() && !clusterst.getText().isEmpty() ){
                     Run.setDisable(false);
                } else {
                    Run.setDisable(true);
                }
               
            }
        });
         
        validityButton.setOnAction(e -> {
        try{
            ((AppData) applicationTemplate.getDataComponent()).loadData2(textArea.getText());    
            
            textArea.setEditable(false);
            textArea.setMouseTransparent(true);
            textArea.setFocusTraversable(false);
            textArea.setOpacity(.5);
            
        int counter = 0;
        int labelcounter = 0;
        ArrayList<String> labellist = new ArrayList<String>();
        String[] whole = ((AppUI) applicationTemplate.getUIComponent()).getTextArea().getText().split("\n");
        int j = 0;
			while (whole.length > counter) {                                  
                                String[] arrayref = whole[j].split("\t");
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
                                j++;
                        }   

          ((AppUI) applicationTemplate.getUIComponent()).getDataprop().setText( counter + " instances with " + labelcounter + " in the inputted data. The labels are: " + labellist.toString() );
//           Label label = new Label("Which Algorithm Type?");
//          ((AppUI) applicationTemplate.getUIComponent()).setAlgoType(label);
            if(labellist.size() == 2 && labellist.get(0) != null && labellist.get(1) != null) {
            if( workspace.getChildren().contains(Classification) == false){
            workspace.getChildren().add(Classification);
            }
            Classification.setText(" Random Classification");
            if( workspace.getChildren().contains(ClasAlgo) == false){
            workspace.getChildren().add(ClasAlgo);
            }
            ClasAlgo.setText("Algorithm 1");
            
            }
            if( workspace.getChildren().contains(Clustering) == false){
            workspace.getChildren().add(Clustering);
            }
            Clustering.setText("Clustering");
            if( workspace.getChildren().contains(ClusAlgo) == false && workspace.getChildren().contains(kClusAlgo) == false){
            workspace.getChildren().add(ClusAlgo);
            workspace.getChildren().add(kClusAlgo);
            }           
            ClusAlgo.setText("Random Clustering");
            kClusAlgo.setText("K Means Cluster");
            
        
        }catch(Exception ex){
           ((ErrorDialog) applicationTemplate.getDialog(Dialog.DialogType.ERROR)).init(primaryStage);
           ((ErrorDialog) applicationTemplate.getDialog(Dialog.DialogType.ERROR)).show("ERROR Message", "INVALID INPUT");
           clear();
           
            }
        });
        
        undoreadonlyButton.setOnAction(e -> {    
            textArea.setEditable(true);
            textArea.setMouseTransparent(false);
            textArea.setFocusTraversable(true);
            textArea.setOpacity(1.0);
        });
        
        displayButton.setOnAction(e -> {   
            try{
            ((AppData) applicationTemplate.getDataComponent()).loadData(textArea.getText());
                scrnshotButton.setDisable(false);
                textArea.setEditable(false);
                textArea.setMouseTransparent(true);
                textArea.setFocusTraversable(false);
                textArea.setOpacity(.5);

            
            }catch(Exception ex){
           ((ErrorDialog) applicationTemplate.getDialog(Dialog.DialogType.ERROR)).init(primaryStage);
           ((ErrorDialog) applicationTemplate.getDialog(Dialog.DialogType.ERROR)).show("ERROR Message", "INVALID INPUT");
           
            }
        
        });
               
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!textArea.getText().equals("")){
                   saveButton.setDisable(false);
                } else {
                        saveButton.setDisable(true);
                        }
            }
        });
        
        

        newButton.setOnAction(e -> {   
            
                //textArea.setDisable(false);
                if( workspace.getChildren().contains(validityButton) == false){
                    workspace.getChildren().add(validityButton);
                }
                if( workspace.getChildren().contains(textArea) == false){
                    workspace.getChildren().add(textArea);
                }
                if( workspace.getChildren().contains(displayButton) == false){
                    workspace.getChildren().add(displayButton);
                }
                if( workspace.getChildren().contains(undoreadonlyButton) == false){
                    workspace.getChildren().add(undoreadonlyButton);
                }
                if(!textArea.getText().equals("")){
                    applicationTemplate.getDialog(Dialog.DialogType.CONFIRMATION);
                    //applicationTemplate.getDialog(Dialog.DialogType.CONFIRMATION).init(primaryStage);
                    applicationTemplate.getDialog(Dialog.DialogType.CONFIRMATION).show(
                            "Save Current Work", "Would you like to save current work?");
                
                if (((ConfirmationDialog) applicationTemplate.getDialog(Dialog.DialogType.CONFIRMATION)).getSelectedOption().toString().equals("YES")) {
                    FileChooser fileChooser = new FileChooser();
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Tab-Separated Data (.*.tsd)", "*.tsd");
                    fileChooser.getExtensionFilters().add(extFilter);
                    File file = fileChooser.showSaveDialog(primaryStage);
                    if (file != null) {
                        String filePath = file.getPath();
                        try (final BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
                            writer.write(textArea.getText());
                        } catch (IOException t1) {
                            System.out.println("savefile exception");
                        }
                    }
                    textArea.clear();
                    clear();
                    saveButton.setDisable(true);
                    scrnshotButton.setDisable(true);
                    
                    workspace.getChildren().remove(Run);
                    workspace.getChildren().remove(clasettings);
                    workspace.getChildren().remove(clusettings);
                    workspace.getChildren().remove(kclusettings);
                    workspace.getChildren().remove(Classification);
                    workspace.getChildren().remove(Clustering);
                    workspace.getChildren().remove(kClusAlgo);
                    workspace.getChildren().remove(ClusAlgo);
                    workspace.getChildren().remove(ClasAlgo);
                    
                }
                if(((ConfirmationDialog) applicationTemplate.getDialog(Dialog.DialogType.CONFIRMATION)).getSelectedOption().toString().equals("NO")){
                    newButton.setDisable(false);
                    saveButton.setDisable(false);
                    scrnshotButton.setDisable(true);
                    textArea.clear();
                    clear();
                    
                    workspace.getChildren().remove(Run);
                    workspace.getChildren().remove(clasettings);
                    workspace.getChildren().remove(clusettings);
                    workspace.getChildren().remove(kclusettings);
                    workspace.getChildren().remove(Classification);
                    workspace.getChildren().remove(Clustering);
                    workspace.getChildren().remove(kClusAlgo);
                    workspace.getChildren().remove(ClusAlgo);
                    workspace.getChildren().remove(ClasAlgo);
                    
                }
                }
            
        }); 
        
        

                

        
        
        
        
        
        
        
    }

    public TextField getcMaxt() {
        return cMaxt;
    }
    public TextField getMaxt() {
        return Maxt;
    }
    public TextField getCupdatet() {
        return cupdatet;
    }

    public CheckBox getCcheckbox() {
        return ccheckbox;
        
    }

    public Text getDataprop() {
        return dataprop;
    }

    public Button getDisplayButton() {
        return displayButton;
    }
}
