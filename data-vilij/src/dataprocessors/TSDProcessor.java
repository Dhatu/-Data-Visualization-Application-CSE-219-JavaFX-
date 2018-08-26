package dataprocessors;

import hw4files.DataSet;
import javafx.geometry.Point2D;
import javafx.scene.chart.XYChart;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

/**
 * The data files used by this data visualization applications follow a tab-separated format, where each data point is
 * named, labeled, and has a specific location in the 2-dimensional X-Y plane. This class handles the parsing and
 * processing of such data. It also handles exporting the data to a 2-D plot.
 * <p>
 * A sample file in this format has been provided in the application's <code>resources/data</code> folder.
 *
 * @author Ritwik Banerjee
 * @see XYChart
 */
public final class TSDProcessor {

    public static class InvalidDataNameException extends Exception {

        private static final String NAME_ERROR_MSG = "All data instance names must start with the @ character.";

        public InvalidDataNameException(String name) {
            super(String.format("Invalid name '%s'." + NAME_ERROR_MSG, name));
        }
    }

    private Map<String, String>  dataLabels;
    private Map<String, String> cdataLabels;

    public void setCdataLabels(Map<String, String> cdataLabels) {
        this.cdataLabels = cdataLabels;
    }
    private DataSet data = new DataSet();
    private ArrayList<String> ClasInsta = new ArrayList<String>();

    public ArrayList<String> getClasInsta() {
        return ClasInsta;
    }

    public DataSet getData() {
        return data;
    }
    private int counter;

    public int getCounter() {
        return counter;
    }

    public Map<String, String> getDataLabels() {
        return dataLabels;
    }
     public Map<String, String> getcDataLabels() {
        return cdataLabels;
    }


    public Map<String, Point2D> getDataPoints() {
        return dataPoints;
    }
    private Map<String, Point2D> dataPoints;

    public TSDProcessor() {
        dataLabels = new HashMap<>();
        dataPoints = new HashMap<>();
    }

    /**
     * Processes the data and populated two {@link Map} objects with the data.
     *
     * @param tsdString the input data provided as a single {@link String}
     * @throws Exception if the input string does not follow the <code>.tsd</code> data format
     */
    public void processString(String tsdString) throws Exception {
        AtomicBoolean hadAnError   = new AtomicBoolean(false);
        StringBuilder errorMessage = new StringBuilder();
        Stream.of(tsdString.split("\n"))
              .map(line -> Arrays.asList(line.split("\t")))
              .forEach(list -> {
                  try {
                      String   name  = checkedname(list.get(0));
                      String   label = list.get(1);
                      String[] pair  = list.get(2).split(",");
                      Point2D  point = new Point2D(Double.parseDouble(pair[0]), Double.parseDouble(pair[1]));
                      dataLabels.put(name, label);
                      dataPoints.put(name, point);
                      ClasInsta.add(name);
              data.setLabels(dataLabels);
              data.setLocations(dataPoints);
                  } catch (Exception e) {
                      errorMessage.setLength(0);
                      errorMessage.append(e.getClass().getSimpleName()).append(": ").append(e.getMessage());
                      hadAnError.set(true);
                  }

              });
//        if (errorMessage.length() > 0)
//            throw new Exception(errorMessage.toString());
    }

    /**
     * Exports the data to the specified 2-D chart.
     *
     * @param chart the specified chart
     */
    void toChartData(XYChart<Number, Number> chart) {
        Set<String> labels = new HashSet<>(dataLabels.values());
        for (String label : labels) {
            counter++;
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(label);
            dataLabels.entrySet().stream().filter(entry -> entry.getValue().equals(label)).forEach(entry -> {
                Point2D point = dataPoints.get(entry.getKey());
                series.getData().add(new XYChart.Data<>(point.getX(), point.getY()));
            });
            chart.getData().add(series);
            
//            EventHandler<? super MouseEvent> onMouseEnteredSeriesListener = 
//            (MouseEvent event) -> {
//                ((Node)(event.getSource())).setCursor(Cursor.HAND);
//                
//            };
//            EventHandler<? super MouseEvent> onMouseExitedSeriesListener = 
//            (MouseEvent event) -> {
//                
//             ((Node)(event.getSource())).setCursor(Cursor.DEFAULT);
//                };
//             series.getNode().setOnMouseEntered(onMouseEnteredSeriesListener);
//             series.getNode().setOnMouseExited(onMouseExitedSeriesListener);
        }
        

       
//       double yavgtotal = 0;
//       double counter = 0;
//       for (Point2D value : dataPoints.values()) {
//             yavgtotal = yavgtotal + value.getY(); 
//             counter++;
//            }
//       double yavg = yavgtotal/counter;
//       Line yavgLine = new Line();
//       XYChart.Series series1 = new XYChart.Series();
//       XYChart.Series series2 = new XYChart.Series();
//       series1.getData().add(new XYChart.Data(-1.50, yavg));
//       series2.getData().add(new XYChart.Data(2, yavg));
//       chart.getData().addAll(series1, series2);
               
    }
        void tocChartData(XYChart<Number, Number> chart) {
        Set<String> labels = new HashSet<>(cdataLabels.values());
        for (String label : labels) {
            counter++;
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(label);
            cdataLabels.entrySet().stream().filter(entry -> entry.getValue().equals(label)).forEach(entry -> {
                Point2D point = dataPoints.get(entry.getKey());
                series.getData().add(new XYChart.Data<>(point.getX(), point.getY()));
            });
            chart.getData().add(series);
            
        }             
    }
    
    void clear(XYChart<Number, Number> chart) {
        dataPoints.clear();
        dataLabels.clear();
        chart.getData().clear();
    }
    public void clear2(XYChart<Number, Number> chart) {
        chart.getData().clear();
    }

    private String checkedname(String name) throws InvalidDataNameException {
        if (!name.startsWith("@"))
            throw new InvalidDataNameException(name);
        return name;
    }
}
