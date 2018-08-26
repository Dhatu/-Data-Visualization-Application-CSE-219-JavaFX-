package dataprocessors;

import ui.AppUI;
import vilij.components.DataComponent;
import vilij.templates.ApplicationTemplate;

import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the concrete application-specific implementation of the data component defined by the Vilij framework.
 *
 * @author Ritwik Banerjee
 * @see DataComponent
 */
public class AppData implements DataComponent {

    private TSDProcessor        processor;

    public TSDProcessor getProcessor() {
        return processor;
    }
    private ApplicationTemplate applicationTemplate;

    public AppData(ApplicationTemplate applicationTemplate) {
        this.processor = new TSDProcessor();
        this.applicationTemplate = applicationTemplate;
    }

    @Override
    public void loadData(Path dataFilePath) {
        // TODO: NOT A PART OF HW 1
    }

    public void loadData(String dataString) throws Exception {
        processor.processString(dataString);
        ((AppData) applicationTemplate.getDataComponent()).displayData();
        
    }
    public void loadData2(String dataString) throws Exception {
        processor.processString(dataString);
        
    }
    public void cloadData(String dataString) throws Exception {
        processor.processString(dataString);
        ((AppData) applicationTemplate.getDataComponent()).cdisplayData();

    }

    @Override
    public void saveData(Path dataFilePath) {
        // TODO: NOT A PART OF HW 1
    }

    @Override
    public void clear() {
        processor.clear(((AppUI) applicationTemplate.getUIComponent()).getChart());
        
    }

    public void displayData() {
        processor.toChartData(((AppUI) applicationTemplate.getUIComponent()).getChart());
    }
    public void cdisplayData() {
        processor.tocChartData(((AppUI) applicationTemplate.getUIComponent()).getChart());
    }
    
}
