package ui;

import actions.AppActions;
import dataprocessors.AppData;
import java.io.File;
import static java.io.File.separator;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import static settings.AppPropertyTypes.SCREENSHOT_ICON;
import static settings.AppPropertyTypes.SCREENSHOT_TOOLTIP;
import vilij.components.ConfirmationDialog;
import vilij.components.Dialog;
import vilij.components.ErrorDialog;
import vilij.propertymanager.PropertyManager;
import static vilij.settings.PropertyTypes.EXIT_TOOLTIP;
import static vilij.settings.PropertyTypes.GUI_RESOURCE_PATH;
import static vilij.settings.PropertyTypes.ICONS_RESOURCE_PATH;
import static vilij.settings.PropertyTypes.LOAD_TOOLTIP;
import static vilij.settings.PropertyTypes.NEW_TOOLTIP;
import static vilij.settings.PropertyTypes.PRINT_ICON;
import static vilij.settings.PropertyTypes.PRINT_TOOLTIP;
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

    @SuppressWarnings("FieldCanBeLocal")
    private Button                       scrnshotButton; // toolbar button to take a screenshot of the data
    private String                       scrnshotPath;
    private ScatterChart<Number, Number> chart;          // the chart where data will be displayed
    private Button                       displayButton;  // workspace button to display data on the chart
    private TextArea                     textArea;       // text area for new data input
    private boolean                      hasNewText;     // whether or not the text area has any new data since last display

    public ScatterChart<Number, Number> getChart() { return chart; }

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
        textArea = new TextArea();
        appPane.getChildren().add(textArea);
        displayButton = new Button("Display");
        chart = new ScatterChart<Number,Number>(new NumberAxis(),new NumberAxis());
        workspace = new VBox();
        workspace.getChildren().add(textArea);
        workspace.getChildren().add(displayButton);
        workspace.getChildren().add(chart);
        appPane.getChildren().add(workspace);
        // TODO for homework 1
    }

    private void setWorkspaceActions() {
        displayButton.setOnAction(e -> {   
            try{
            ((AppData) applicationTemplate.getDataComponent()).loadData(textArea.getText());
            }catch(Exception ex){
           ((ErrorDialog) applicationTemplate.getDialog(Dialog.DialogType.ERROR)).init(primaryStage);
           ((ErrorDialog) applicationTemplate.getDialog(Dialog.DialogType.ERROR)).show("ERROR Message", "INVALID INPUT");
            }
        
        });
       
       // displayButton.setOnAction(e -> ((AppData) .processor.processString()));
        //
        
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!textArea.getText().equals("")){
                    newButton.setDisable(false);
                    saveButton.setDisable(false);}
                else {
                    newButton.setDisable(true);
                    saveButton.setDisable(true);
                }
            }
        });
        
        

        newButton.setOnAction(e -> {
            
            
          if(!textArea.getText().equals("")){   
            applicationTemplate.getDialog(Dialog.DialogType.CONFIRMATION);
            //applicationTemplate.getDialog(Dialog.DialogType.CONFIRMATION).init(primaryStage);
            applicationTemplate.getDialog(Dialog.DialogType.CONFIRMATION).show(
                    "Save Current Work", "Would you like to save current work?");
          }
            if(((ConfirmationDialog) applicationTemplate.getDialog(Dialog.DialogType.CONFIRMATION)).getSelectedOption().toString().equals("YES")){
              FileChooser fileChooser = new FileChooser();
  
              FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Tab-Separated Data (.*.tsd)", "*.tsd");
              fileChooser.getExtensionFilters().add(extFilter);
              
              File file = fileChooser.showSaveDialog(primaryStage);
              
            }
            if(((ConfirmationDialog) applicationTemplate.getDialog(Dialog.DialogType.CONFIRMATION)).getSelectedOption().toString().equals("NO")){
                 newButton.setDisable(true);
                 saveButton.setDisable(true);
                 textArea.clear();
                 clear();
            }
        }); 
        
        

                

        
        
        
        
        
        
        
    }
}
