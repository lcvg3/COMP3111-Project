package ui.comp3111;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import core.comp3111.DataColumn;
import core.comp3111.DataTable;
import core.comp3111.DataType;
import core.comp3111.SampleDataGenerator;
import core.comp3111.DataGenerator;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The Main class of this GUI application
 * 
 * @author TEAM
 *
 */
public class Main extends Application {

	// Attribute: DataTable
	// In this sample application, a single data table is provided
	// You need to extend it to handle multiple data tables
	// Hint: Use java.util.List interface and its implementation classes (e.g.
	// java.util.ArrayList)

	// Attributes: Scene and Stage
	private static final int SCENE_NUM = 10;
	private static final int SCENE_MAIN_SCREEN = 0;
	private static final int SCENE_LINE_CHART = 1;
	private static final int SCENE_IMPORT_ENV = 2;
	private static final int SCENE_IMPORT_DATA = 3;
	private static final int SCENE_CREATE_CH = 4;
	private static final int SCENE_INFO = 5;
	private static final int SCENE_LINE = 6;
	private static final int SCENE_PIE = 7;
	private static final int SCENE_ANIMATED = 8;
	private static final String[] SCENE_TITLES = { "COMP3111 Chart - [Team Name]", "Sample Line Chart Screen" };
	private Stage stage = null;
	private Scene[] scenes = null;
	private static enum Chart {LINE, PIE, ANIMATED};
	private static Chart enumC;
	//private ArrayList<String> dataSets = new ArrayList<String>; //datapaths to data
	//private ArrayList<charts> charts = new ArrayList<charts>; //all charts for an environment

	// To keep this application more structural,
	// The following UI components are used to keep references after invoking
	// createScene()

	// Screen 1: paneMainScreen
	private Button btImportEnv, btImportDataset, btSampleLineChart, btCreateCharts, submitCh;
	private Label lbSampleDataTable, lbMainScreenTitle, missingData;
	private ListView<String> dList = new ListView<String>();
	private ObservableList<String> datas = FXCollections.observableArrayList("","","");
	private ListView<String> cList = new ListView<String>();
	private ObservableList<String> charts = FXCollections.observableArrayList("","","");
	private ArrayList<String> datasets = new ArrayList<String>();
	private Map<String, Object> dataTables = new HashMap<String, Object>();
	
	// Screen 2: paneSampleLineChartScreen
	private LineChart<Number, Number> lineChart = null;
	private PieChart pieChart = null;
	private NumberAxis xAxis = null;
	private NumberAxis yAxis = null;
	private Button btLineChartBackMain = null;

	// Screen 3: createCharts
	private Button pieChartb, lineChartb, animatedb;
	private Label chartTypes;
	private TextField colLabels, colNum, rowStart, rowEnd, title, x, y;
	/**
	 * create all scenes in this application
	 */
	private void initScenes() {
		scenes = new Scene[SCENE_NUM];
		scenes[SCENE_MAIN_SCREEN] = new Scene(paneMainScreen(), 400, 500);
		scenes[SCENE_LINE_CHART] = new Scene(paneLineChartScreen(), 800, 600);
		scenes[SCENE_IMPORT_ENV] = new Scene(paneImportEnvironment(), 400, 500);
		scenes[SCENE_IMPORT_DATA] = new Scene(paneImportDataset(), 400, 500);
		scenes[SCENE_CREATE_CH] = new Scene(paneChooseChart(), 400, 500);
		scenes[SCENE_INFO] = new Scene(paneInfoChart(), 400, 500);
		for (Scene s : scenes) {
			if (s != null)
				// Assumption: all scenes share the same stylesheet
				s.getStylesheets().add("Main.css");
		}
	}

	/**
	 * This method will be invoked after createScenes(). In this stage, all UI
	 * components will be created with a non-NULL references for the UI components
	 * that requires interaction (e.g. button click, or others).
	 */
	private void initEventHandlers() {
		initMainScreenHandlers();
		initLineChartScreenHandlers();
		initCreateCharts();
	}

	/**
	 * Initialize event handlers of the line chart screen
	 */
	private void initLineChartScreenHandlers() {

		// click handler
		btLineChartBackMain.setOnAction(e -> {
			putSceneOnStage(SCENE_MAIN_SCREEN);
		});
	}

	/**
	 * Populate sample data table values to the chart view
	 */
	private void populateDataTableValues(String chosenData, String title, String x, String y, String colLabels, String colNum, String rowStart, String rowEnd) {

		// Get 2 columns
		enumC = Chart.LINE;
		DataTable chosen = (DataTable)(dataTables.get(chosenData));

		DataColumn xCol = chosen.getCol(colLabels);
		DataColumn yCol = chosen.getCol(colNum);

		// Ensure both columns exist and the type is number
		if (xCol != null && yCol != null && xCol.getTypeName().equals(DataType.TYPE_NUMBER)
				&& yCol.getTypeName().equals(DataType.TYPE_NUMBER)) {
			
			switch (enumC) {
			
				case LINE:
					lineChart.setTitle(title);
					break;
				
				case PIE:
					pieChart.setTitle(title);
					break;
				
				case ANIMATED:
					break;
			}
			xAxis.setLabel(x);
			yAxis.setLabel(y);

			// defining a series
			XYChart.Series series = new XYChart.Series();

			series.setName(title);

			// populating the series with data
			// As we have checked the type, it is safe to downcast to Number[]
			Number[] xValues = (Number[]) xCol.getData();
			Number[] yValues = (Number[]) yCol.getData();

			// In DataTable structure, both length must be the same
			int len = xValues.length;

			for (int i = 0; i < len; i++) {
				series.getData().add(new XYChart.Data(xValues[i], yValues[i]));
			}

			switch (enumC) {
				case LINE:
					lineChart.getData().clear();
					lineChart.getData().add(series);
					break;
				case PIE: 
					lineChart.getData().clear();
					lineChart.getData().add(series);
					break;
				case ANIMATED:
					lineChart.getData().clear();
					lineChart.getData().add(series);
			}
		}

	}
	/**
	 * Initialize event handlers of create charts screen
	 */
	private void initCreateCharts() {
		pieChartb.setOnAction( e -> {
			enumC = Chart.PIE;
			putSceneOnStage(SCENE_INFO);
		});
		
		lineChartb.setOnAction( e -> {
			enumC = Chart.LINE;
			putSceneOnStage(SCENE_INFO);
		});
		
		animatedb.setOnAction( e -> {
			enumC = Chart.ANIMATED;
			putSceneOnStage(SCENE_INFO);
		});
		
		submitCh.setOnAction( e -> {
			if (title.getText() != null && x.getText() != null && y.getText() != null
					&& colLabels.getText() != null && rowStart.getText() != null &&
					rowEnd.getText() != null && colNum.getText() != null) {
				populateDataTableValues(title.getText(), title.getText(), x.getText(), y.getText(),
						colLabels.getText(), colNum.getText(), rowStart.getText(), rowEnd.getText());
			} else {
				//missing data
				missingData.setText("Please fill in all fields");
			}
			switch (enumC) {
				case LINE: 
					putSceneOnStage(SCENE_LINE);
					break;
				case PIE:
					putSceneOnStage(SCENE_PIE);
					break;
				case ANIMATED:
					putSceneOnStage(SCENE_ANIMATED);
					break;
			}
		});
	}
	
	/**
	 * Initialize event handlers of the main screen
	 */
	private void initMainScreenHandlers() {

		// click handler
		btImportDataset.setOnAction(e -> {
			putSceneOnStage(SCENE_IMPORT_DATA);

		});

		// click handler
		btImportEnv.setOnAction(e -> {
			putSceneOnStage(SCENE_IMPORT_ENV);
		});

		// click handler
		btSampleLineChart.setOnAction(e -> {
			putSceneOnStage(SCENE_LINE_CHART);
		});
		
		btCreateCharts.setOnAction(e -> {
			putSceneOnStage(SCENE_CREATE_CH);
		});
		

	}

	/**
	 * Create the line chart screen and layout its UI components
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneLineChartScreen() {

		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		lineChart = new LineChart<Number, Number>(xAxis, yAxis);

		btLineChartBackMain = new Button("Back");

		xAxis.setLabel("undefined");
		yAxis.setLabel("undefined");
		lineChart.setTitle("undefined");

		// Layout the UI components
		VBox container = new VBox(20);
		container.getChildren().addAll(lineChart, btLineChartBackMain);
		container.setAlignment(Pos.CENTER);

		BorderPane pane = new BorderPane();
		pane.setCenter(container);

		// Apply CSS to style the GUI components
		pane.getStyleClass().add("screen-background");

		return pane;
	}

	private Pane panePieChartScreen() {
		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		pieChart = new PieChart();
		
		btLineChartBackMain = new Button("Back");
		
		xAxis.setLabel("undefined");
		yAxis.setLabel("undefined");
		pieChart.setTitle("undefined");
		
		VBox container = new VBox(20);
		container.getChildren().addAll(pieChart, btLineChartBackMain);
		container.setAlignment(Pos.CENTER);
		
		BorderPane pane = new BorderPane();
		pane.setCenter(container);
		
		pane.getStyleClass().add("screen-background");
		
		return pane;
		
	}
	
	private Pane paneAnimatedChartScreen() {
		return panePieChartScreen();
		
	}
	/**
	 * Creates the main screen and layout its UI components
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneMainScreen() {

		lbMainScreenTitle = new Label("COMP3111 Chart");
		btImportEnv = new Button("Import Environment");
		btImportDataset = new Button("Import Dataset");
	    btCreateCharts = new Button("Create a Chart");
		btSampleLineChart = new Button("Sample Line Chart");
		lbSampleDataTable = new Label("DataTable: empty");
		
		dList.setItems(datas);
		cList.setItems(charts);
		dList.prefWidth(150);
		cList.prefWidth(150);
		dList.prefHeight(200);
		dList.prefHeight(200);

		// Layout the UI components

		HBox hc = new HBox(20);
		hc.setAlignment(Pos.CENTER);
		hc.getChildren().addAll(btImportEnv, btImportDataset, btCreateCharts);

		VBox container = new VBox(20);
		container.getChildren().addAll(lbMainScreenTitle, hc, lbSampleDataTable, new Separator(), btSampleLineChart, dList, cList);
		container.setAlignment(Pos.CENTER);

		BorderPane pane = new BorderPane();
		pane.setCenter(container);

		// Apply style to the GUI components
		btSampleLineChart.getStyleClass().add("menu-button");
		lbMainScreenTitle.getStyleClass().add("menu-title");
		pane.getStyleClass().add("screen-background");

		return pane;
	}
	
	private Pane paneImportDataset() { //new window to import dataset
		BorderPane pane = new BorderPane();
		return pane;
	}
	
	private Pane paneImportEnvironment() { //new window to import environment
		BorderPane pane = new BorderPane();
		return pane;
	}
	
	private Pane paneChooseChart() {
		BorderPane pane = new BorderPane();
		chartTypes = new Label("Create a Chart");
		lineChartb = new Button("Line Chart");
		pieChartb = new Button("Pie Chart");
		animatedb = new Button("Animated Chart");
		HBox hc = new HBox(20);
		hc.setAlignment(Pos.CENTER);
		hc.getChildren().addAll(lineChartb, pieChartb, animatedb);
		pane.setCenter(hc);
		pane.getStyleClass().add("screen-background");
		
		return pane;
	}
	
	private Pane paneInfoChart() {	
		BorderPane pane = new BorderPane();
		GridPane grid = new GridPane();
		colLabels = new TextField();
		colNum = new TextField();
		rowStart = new TextField();
		rowEnd = new TextField();
		title = new TextField();
		x = new TextField();
		y = new TextField();
		colLabels.setPromptText("Choose labels column");
		colNum.setPromptText("Choose column with data");
		rowStart.setPromptText("Choose starting row");
		rowEnd.setPromptText("Choose ending row");
		title.setPromptText("Chart Title");
		x.setPromptText("Choose x axis label");
		y.setPromptText("Choose y axis label");
		grid.getChildren().addAll(title, colLabels, colNum, rowStart, rowEnd, x, y);
		submitCh = new Button("Generate Chart");
		grid.getChildren().add(submitCh);
		pane.setBottom(grid);
		
		return pane;
	}

	/**
	 * This method is used to pick anyone of the scene on the stage. It handles the
	 * hide and show order. In this application, only one active scene should be
	 * displayed on stage.
	 * 
	 * @param sceneID
	 *            - The sceneID defined above (see SCENE_XXX)
	 */
	private void putSceneOnStage(int sceneID) {

		// ensure the sceneID is valid
		if (sceneID < 0 || sceneID >= SCENE_NUM)
			return;

		stage.hide();
		stage.setTitle(SCENE_TITLES[sceneID]);
		stage.setScene(scenes[sceneID]);
		stage.setResizable(true);
		stage.show();
	}

	/**
	 * All JavaFx GUI application needs to override the start method You can treat
	 * it as the main method (i.e. the entry point) of the GUI application
	 */
	@Override
	public void start(Stage primaryStage) {
		try {

			stage = primaryStage; // keep a stage reference as an attribute
			initScenes(); // initialize the scenes
			initEventHandlers(); // link up the event handlers
			putSceneOnStage(SCENE_MAIN_SCREEN); // show the main screen

		} catch (Exception e) {

			e.printStackTrace(); // exception handling: print the error message on the console
		}
	}

	/**
	 * main method - only use if running via command line
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
