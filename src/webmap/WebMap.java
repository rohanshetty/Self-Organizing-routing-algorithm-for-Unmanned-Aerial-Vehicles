package webmap;

import Business.Drone;
import Geocoder.newGeocode;
import Graph.Algorithm;
import Graph.Link;
import Graph.Stack;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javax.swing.Timer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;



/**
 * @author Rohaadd
 */
public class WebMap extends Application {
    
    private static final String URL = "http://maps.googleapis.com/maps/api/geocode/json"; 
    private Timeline locationUpdateTimeline;
//    Timer timer = new Timer();
    ArrayList<Drone> droneList;
    double coList[][];
    int i;
    WebView web;
    
    
    // All textfieldsf

    ComboBox focusCombo = new ComboBox();
    final ComboBox comboBox = new ComboBox();
    TextField initialLocationLabel = new TextField();
    TextField initialLongitude = new TextField();
    TextField finalLocationLabel = new TextField();
    TextField finalLongitude = new TextField();
    TextField initialAltitude = new TextField();
    TextField initialSpeed = new TextField();
    TextField currentLat = new TextField();
    TextField currentLng = new TextField();
    TextField currentSpeed = new TextField();
    TextField currentAltitude = new TextField();
    TextField currentBattery = new TextField();
    TextField setSpeedTo = new TextField();
    TextField setAltitudeTo = new TextField();
    TextField windSpeed = new TextField();
    TextField temperature = new TextField();
    TextField turbulence = new TextField();
    TextField rain = new TextField();
    TextField icing = new TextField();

    int x = 0;
    public WebMap() {
        i=0;
        
    droneList = new ArrayList<Drone>();
    coList = new double[][]{
    {42.32599290979131 , -71.1116439950517},
{42.44 , -71.11164400130363},
{42.66 , -71.33},
{42.33599290979131 , -71.1116440002685},
{42.32599290979131 , -71.11164399979008},
{42.33599290979131 , -71.1116440000553},
{42.32599290979131 , -71.11164399995675},
{42.33599290979131 , -71.1116440000114},
{42.32599290979131 , -71.11164399999109},
{42.33599290979131 , -71.11164400000234},
{42.32599290979131 , -71.11164399999817},
{42.33599290979131 , -71.11164400000048},
{42.32599290979131 , -71.11164399999961},
{42.33599290979131 , -71.1116440000001},
{42.32599290979131 , -71.11164399999993},
{42.33599290979131 , -71.11164400000001},
{42.32599290979131 , -71.11164399999998},
{42.33599290979131 , -71.111644},
{42.32599290979131 , -71.111644},
{42.32907406727151 , -71.09219033729244},
{42.330966459305785 , -71.10200964747129},
{42.33218987524781 , -71.11193452799615}      
    };
    }
    
    
    
    @Override public void start(final Stage stage) {
        // create web engine and view
//        final WebEngine webEngine = new WebEngine(getClass().getResource("googlemap.html").toString());
//        final WebView webView = new WebView(webEngine);
        
        final WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("googlemap.html").toString());
        // create map type buttons
        webView.setPrefSize(200,200);
        webView.setPrefHeight(500);
        webView.setPrefWidth(500);
//        webView.resize(50, 50);
        this.web = webView;
        
        
        System.out.println("Size"+webView.getWidth());
        final ToggleGroup mapTypeGroup = new ToggleGroup();
        final ToggleButton road = new ToggleButton("Road");
        road.setSelected(true);
        road.setToggleGroup(mapTypeGroup);
        final ToggleButton satellite = new ToggleButton("Satellite");
        satellite.setToggleGroup(mapTypeGroup);
        final ToggleButton hybrid = new ToggleButton("Hybrid");
        hybrid.setToggleGroup(mapTypeGroup);
        final ToggleButton terrain = new ToggleButton("Terrain");
        terrain.setToggleGroup(mapTypeGroup);
        mapTypeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle toggle1) {
                if (road.isSelected()) {
                    webEngine.executeScript("document.setMapTypeRoad()");
                } else if (satellite.isSelected()) {
                    webEngine.executeScript("document.setMapTypeSatellite()");
                } else if (hybrid.isSelected()) {
                    webEngine.executeScript("document.setMapTypeHybrid()");
                } else if (terrain.isSelected()) {
                    webEngine.executeScript("document.setMapTypeTerrain()");
                }
            }
        });
        // add map source toggles
        ToggleGroup mapSourceGroup = new ToggleGroup();
        final ToggleButton google = new ToggleButton("Google");
        google.setSelected(true);
        google.setToggleGroup(mapSourceGroup);
        final ToggleButton yahoo = new ToggleButton("Yahoo");
        yahoo.setToggleGroup(mapSourceGroup);
        final ToggleButton bing = new ToggleButton("Bing");
        bing.setToggleGroup(mapSourceGroup);
        // listen to selected source
        mapSourceGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle toggle1) {
                terrain.setDisable(true);
                if (google.isSelected()) {
                    terrain.setDisable(false);
                    webEngine.load(getClass().getResource("googlemap.html").toString());
                } else if (yahoo.isSelected()) {
                    webEngine.load(getClass().getResource("bingmap.html").toString());
                } else if (bing.isSelected()) {
                    webEngine.load(getClass().getResource("yahoomap.html").toString());
                }
                mapTypeGroup.selectToggle(road);
            }
        });
        // add search
        final TextField searchField = new TextField("95054");
        final TextField searchFiel = new TextField("55555555555");

        // tabbed pane java fx
        
        TabPane tabPane = new TabPane();
        tabPane.setMinHeight(500);
        tabPane.setMinWidth(400);
        
        Tab tab1 = new Tab();
        tab1.setText("new tab 1");
        tab1.setContent(new Rectangle(200,200, Color.BROWN));
        Tab tab2 = new Tab();
        tab2.setText("new tab 2");
        tab2.setContent(new Rectangle(200,200, Color.LIGHTGREEN));

        tabPane.getTabs().add(tab2);
        tabPane.getTabs().add(tab1);
 
  
        Button zoomIn = new Button("Zoom In");
        zoomIn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) { webEngine.executeScript("document.zoomIn()"); }
        });
        Button zoomOut = new Button("Zoom Out");
        zoomOut.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) { webEngine.executeScript("document.zoomOut()"); }
        });
        // test for coordinate updation
        final double la = 37.763145;
        final double lo = -122.497231;
        
        GridPane imageDial = new GridPane();
        
             final Popup loadingPopup = new Popup(); 
        loadingPopup.setX(600); loadingPopup.setY(300);
        
        comboBox.setPrefWidth(480);
        focusCombo.setPrefWidth(400);
        

        //Tiled Pane
        TitledPane setupGridTitlePane = new TitledPane();
        GridPane setupGrid = new GridPane();
        setupGrid.setVgap(4);
        setupGrid.setPadding(new Insets(5, 5, 5, 5));

        setupGrid.add(new Label("INITIAL LOCATION: "), 0, 0);
        setupGrid.add(initialLocationLabel, 1, 0);
        initialLocationLabel.setPrefWidth(250);
        initialLocationLabel.setText("Northeastern University, Boston, MA");
        setupGrid.add(new Label("FINAL LOCATION: "), 0, 2);
        setupGrid.add(finalLocationLabel, 1, 2);
        finalLocationLabel.setPrefWidth(250);
        finalLocationLabel.setText("1 Smith Street, Boston, MA");
        
        setupGrid.add(new Label("DESIRED ALTITUDE: "), 0, 4);
        setupGrid.add(initialAltitude, 1, 4);        
//        initialAltitude.setText("100");
        setupGrid.add(new Label("DESIRED SPEED: "), 0, 5);
        setupGrid.add(initialSpeed, 1, 5);        
        
//        initialSpeed.setText("200");
        Button saveDrone = new Button("SAVE DRONE");
        saveDrone.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
        // Add the code to be performed when completed
            Drone d = (Drone)comboBox.getValue();
            
            
            double i=0,i2=0;
            String initialLoc = initialLocationLabel.getText();
            String finalLoc = finalLocationLabel.getText();
            newGeocode g = new newGeocode();
            
            try {
                double[] list = g.getLatLng(initialLoc);
                i = list[0];
                i2 = list[1];
            } catch (ParseException ex) {
                Logger.getLogger(WebMap.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(WebMap.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Link initial = new Link(i, i2);
            d.setInitialCordinate(initial);
            System.out.println("latLng : "+i+","+i2);
             try {
                double[] list = g.getLatLng(finalLoc);
                i = list[0];
                i2 = list[1];
            } catch (ParseException ex) {
                Logger.getLogger(WebMap.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(WebMap.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
//            i = Double.parseDouble(finalLocationLabel.getText());
//            i2 = Double.parseDouble(finalLongitude.getText());
            Link finalC = new Link(i,i2);
            d.setFinalCordinate(finalC);
            System.out.println("latLng : "+i+","+i2);
            
            i = Double.parseDouble(initialAltitude.getText());
            d.setInitialAltitude(i);
            i = Double.parseDouble(initialSpeed.getText());
            d.setInitialspeed(i);
            System.out.println("One drone added");
        }
        });
        setupGrid.add(saveDrone, 0, 6);
        saveDrone.setPrefWidth(150);

        setupGridTitlePane.setText("INITIAL SETUP");
        setupGridTitlePane.setContent(setupGrid);
        
        

        TitledPane quickLookGridTitlePane = new TitledPane();
        GridPane quickLookGrid = new GridPane();
        quickLookGrid.setVgap(4);
        quickLookGrid.setPadding(new Insets(5, 5, 5, 5));
        quickLookGrid.add(new Label("CURRENT LATITUDE: "), 0, 0);
        quickLookGrid.add(currentLat, 1, 0);
        
        quickLookGrid.add(new Label("CURRENT LONGITUDE: "), 0, 1);
        quickLookGrid.add(currentLng, 1, 1);
        
        quickLookGrid.add(new Label("CURRENT SPEED: "), 0, 2);
        quickLookGrid.add(currentSpeed, 1, 2);
        
        quickLookGrid.add(new Label("CURRENT ALTITUDE: "), 0, 3);
        
        quickLookGrid.add(currentAltitude, 1, 3);
        quickLookGrid.add(new Label("CURRENT BATTERY: "), 0, 4);
        
        quickLookGrid.add(currentBattery, 1, 4);
        quickLookGridTitlePane.setText("QUICK LOOK");
        quickLookGridTitlePane.setContent(quickLookGrid);
        currentLat.setPrefWidth(250);
        currentLng.setPrefWidth(250);
       currentBattery.setPrefWidth(250);
        currentAltitude.setPrefWidth(250);
        currentSpeed.setPrefWidth(250);
        currentBattery.setText("<INCOMING VALUE FROM SENSORS>");
        
         Button currentStatus = new Button("FETCH STATUS");
        currentStatus.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
        // Add the code to be performed when completed
            Drone d = (Drone)comboBox.getValue();
            int noStatus = d.getDroneNumber();
            
            Double lat = (Double)webEngine.executeScript("document.getCurrentLat("+noStatus+")");   
            Double lng = (Double)webEngine.executeScript("document.getCurrentLng("+noStatus+")");   
            currentLat.setText(String.valueOf(lat));
            currentLng.setText(String.valueOf(lng));
            currentSpeed.setText(String.valueOf(d.getInitialspeed()));
            currentAltitude.setText(String.valueOf(d.getInitialAltitude()));
        }});
        
        quickLookGrid.add(currentStatus, 0, 5);
        currentStatus.setPrefWidth(150);
        

        TitledPane actionGridTitlePane = new TitledPane();
        GridPane actionGrid = new GridPane();
        actionGrid.setVgap(4);
        actionGrid.setPadding(new Insets(5, 5, 5, 5));
        actionGrid.add(new Label("SET SPEED TO : "), 0, 0);
        
        Label l = new Label("SET ALTITUDE TO : ");
        actionGrid.add(setSpeedTo, 1, 0);
        actionGrid.add(l, 0, 1);
        setSpeedTo.setText("<FOR MANIPULATING SENSORS>");
        
        l.setPrefWidth(150);
        actionGrid.add(setAltitudeTo, 1, 1);
        setAltitudeTo.setText("<FOR MANIPULATING SENSORS>");
        setAltitudeTo.setPrefWidth(250);
        setSpeedTo.setPrefWidth(250);
        
        actionGridTitlePane.setText("POST LAUNCH ACTIONS");
        actionGridTitlePane.setContent(actionGrid);
        
        TitledPane weatherGridTitlePane = new TitledPane();
        GridPane weatherGrid = new GridPane();
        weatherGrid.setVgap(4);
        weatherGrid.setPadding(new Insets(5, 5, 5, 5));
        weatherGrid.add(new Label("WIND SPEED : "), 0, 0);
        
        weatherGrid.add(windSpeed, 1, 0);
        weatherGrid.add(new Label("TEMPERATURE : "), 0, 1);
//        windSpeed.setText("10");
        windSpeed.setPrefWidth(250);
        
        weatherGrid.add(temperature, 1, 1);
        weatherGrid.add(new Label("TURBULENCE : "), 0, 2);
//        temperature.setText("20");
        temperature.setPrefWidth(250);
        weatherGrid.add(turbulence, 1, 2);
        weatherGrid.add(new Label("RAIN : "), 0, 3);
//        turbulence.setText("300");
        turbulence.setPrefWidth(250);
        weatherGrid.add(rain, 1, 3);
//        rain.setText("30");
        rain.setPrefWidth(250);
        weatherGrid.add(new Label("ICING : "), 0, 4);
//        icing.setText("10");
        icing.setPrefWidth(250);
        weatherGrid.add(icing, 1, 4);
        Button launchDrone = new Button("LAUNCH DRONE");
        launchDrone.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
        // Add the code to be performed when completed
            
            
            Drone d = (Drone)comboBox.getValue();
            double i = Double.parseDouble(windSpeed.getText());
            d.setWindSpeed(i);
            i = Double.parseDouble(temperature.getText());
            d.setTemperature(i);
            i = Double.parseDouble(turbulence.getText());
            d.setTurbulence(i);
            i = Double.parseDouble(rain.getText());
            d.setRain(i);
            i = Double.parseDouble(icing.getText());
            d.setIcing(i);
            System.out.println("The drone is launched ");
            
//            webEngine.executeScript("document.beginMovement("+x+")");
            
            System.out.println("Movement Exec ends");
            // code for implementing the algorithm--
            Algorithm algo = new Algorithm();
            try {
                algo.implement(d.getInitialCordinate().getLatitude(),d.getInitialCordinate().getLongitude()
                        ,d.getFinalCordinate().getLatitude(),d.getFinalCordinate().getLongitude(),d);
            } catch (IOException ex) {
                Logger.getLogger(WebMap.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(WebMap.class.getName()).log(Level.SEVERE, null, ex);
            }
            Stack stack = algo.getStack();
//            System.out.println(stack);
            JSONObject obj = createAndReturnJSON(stack);
            System.out.println(obj);
            webEngine.executeScript("document.initPositions("+x+","+obj+")");
            
            int sp = getSpeed(d.getInitialspeed());
            
            webEngine.executeScript("document.beginMovement("+x+","+sp+")");
            x++;
//            webEngine.executeScript("document.beginMovement("+x+")");
            
//            moveDrone(stack,webEngine);
        }
        });
        
        
//        Button getSomethingBtn = new Button("Get Something");
//        getSomethingBtn.setOnAction(new EventHandler<ActionEvent>() {
//        @Override public void handle(ActionEvent e) {
//        // Add the code to be performed when completed
////             JSONObject obzz 
////            
//           Double i = (Double)webEngine.executeScript("document.getSomething("+0+","+10+")");   
//            System.out.println("recieved integer value : "+ i);
//        }
//        });
//        
//        Button doSomethingBtn = new Button("Do Something");
//        doSomethingBtn.setOnAction(new EventHandler<ActionEvent>() {
//        @Override public void handle(ActionEvent e) {
//       
////            webEngine.executeScript("document.beginMovement("+x+")");
////            x++;
////            webEngine.executeScript("document.mapHeading()");
////        webEngine.executeScript("document.doSomething()");   
//        }
//        });
        
        TitledPane ChangeFocusTitlePane = new TitledPane();
        GridPane focusGrid = new GridPane();
        focusGrid.setVgap(4);
        focusGrid.setPadding(new Insets(5, 5, 5, 5));
        
        
        focusCombo.getItems().add("ALL DRONES");
	focusGrid.add(new Label("SELECT VIEW MODEL :"),0,0);
        focusGrid.add(focusCombo, 0, 1);
        
        Button changeFocusDroneBtn = new Button("SET MAP FOCUS");
        changeFocusDroneBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
        // Add the code to be performed when completed
            
            if(focusCombo.getValue().equals("ALL DRONES")){
                webEngine.executeScript("document.setDroneFocusNo(99999)");
                return;
            }
            Drone d = (Drone)focusCombo.getValue();
            int fNo = d.getDroneNumber();
            webEngine.executeScript("document.setDroneFocusNo("+fNo+")");
            // get the number -- x and set on javascript
        }
        });
        
        changeFocusDroneBtn.setPrefWidth(150);
        focusGrid.add(changeFocusDroneBtn, 0, 2);
        changeFocusDroneBtn.setPrefWidth(400);
        ChangeFocusTitlePane.setText("CHANGE FOCUS");
        ChangeFocusTitlePane.setContent(focusGrid);
        
        
        
        
        weatherGrid.add(launchDrone, 0, 5);
        launchDrone.setPrefWidth(150);
//        weatherGrid.add(getSomethingBtn, 2, 5);
//        weatherGrid.add(doSomethingBtn, 2, 6);
        weatherGridTitlePane.setText("DRONE THRESHOLD");
        weatherGridTitlePane.setContent(weatherGrid);
        
        Label droneName = new Label("DRONE NAME");
        droneName.setPrefWidth(450);
        
        GridPane gridLeft = new GridPane();
        gridLeft.setVgap(4);
        gridLeft.setPadding(new Insets(5, 0, 0, 15));
        gridLeft.addRow(0,droneName);
        gridLeft.addRow(1,comboBox);
        gridLeft.addRow(2,setupGridTitlePane);
        gridLeft.addRow(3,weatherGridTitlePane);
        gridLeft.addRow(4,quickLookGridTitlePane);
        gridLeft.addRow(5,ChangeFocusTitlePane);
        gridLeft.addRow(6,actionGridTitlePane);
        
        
//        quickLookGridTitlePane.setExpanded(false);
//        actionGridTitlePane.setExpanded(false);
//        quickLookGridTitlePane.setCollapsible(false);
//        actionGridTitlePane.setCollapsible(false);
//      
        
        //Tiled Pane
        // create toolbar
        ToolBar toolBar = new ToolBar();
        toolBar.getStyleClass().add("map-toolbar");
        toolBar.getItems().addAll(
                createSpacer(),
                road, satellite, hybrid, terrain,
                createSpacer(),
                google, yahoo, bing,
                createSpacer(),
                new Label("Location:"), zoomIn, zoomOut);
//                new Label("Location:"), searchBox, zoomIn, zoomOut);
        toolBar.setPrefWidth(2000);
//
        
        
         
        
        //**************
        
        //**************
        final Popup popup = new Popup(); popup.setX(600); popup.setY(300);
        GridPane droneNameDial = new GridPane();
        droneNameDial.setVgap(4);
        droneNameDial.setPadding(new Insets(5, 5, 5, 5));
        droneNameDial.add(new Label("DRONE NAME : "), 0, 0);
        final TextField dName = new TextField();
        droneNameDial.add(dName, 1, 0);
        Button add = new Button("ADD");;
        droneNameDial.add(add,1,2);
        add.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
        // Add the code to be performed when completed
            Drone d = new Drone();
            String s = dName.getText();
            d.setDroneName(s);
            droneList.add(d);
            refreshCombo(comboBox,focusCombo,d);
            popup.hide();
        }
        });
        popup.getContent().addAll(droneNameDial);
//
        Button newDroneBtn = new Button("ADD NEW DRONE");
        newDroneBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
        // Add the code to be performed when completed
            dName.setText("");
            popup.show(stage);
        }
        });
        
        ToolBar titleBar = new ToolBar();
        titleBar.getStyleClass().add("map-toolbar");
        titleBar.getItems().addAll(newDroneBtn);
        titleBar.setPrefWidth(2000);
        
        //
        GridPane gridTop = new GridPane();
        gridTop.setVgap(4);
        gridTop.setPadding(new Insets(5, 0, 0, 15));
        gridTop.add(titleBar, 0, 0);
        gridTop.add(toolBar, 0, 1);
        gridTop.setPrefWidth(2000);

//gridTop.addRow(0, toolBar);
//gridTop.addRow(1, toolBar);
        // create root
        BorderPane root = new BorderPane();
        root.getStyleClass().add("map");
        webView.setMaxHeight(600);
        webView.setMaxWidth(1300);
        // use set max height for setting the size
        webView.setPrefSize(200,200);
        webView.setPrefHeight(500);
        webView.setPrefWidth(500);
        
        root.setTop(gridTop);
        root.setLeft(gridLeft);
        root.setCenter(webView);
        
        
        root.setPrefHeight(200);
        root.setPrefWidth(500);
        root.setPrefSize(600,300);
//        root.setLayoutX(600);
//        root.setLayoutY(300);

        
        // create scene
        stage.setTitle("Web Map");
        Scene scene = new Scene(root,1000,700, Color.web("#666970"));
        stage.setScene(scene);
        scene.getStylesheets().add("/webmap/WebMap.css");
        // show stage
       
        stage.show();
//        stage.setVisible(true);
    }
    
    
    
    public synchronized void moveDrone(Stack s,final WebEngine webEngine){
        long startTime;
        webEngine.executeScript("document.setMarkerAt("+coList[i][0]+","+coList[i][1]+")");
        startTime = System.currentTimeMillis();
        System.out.println("method exec start");
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                webEngine.executeScript("document.setMarkerAt("+coList[i][0]+","+coList[i][1]+")");
//                webEngine.executeScript("document.setMarkerAt()");
                i++;
                System.out.println(i);
                
                if(i==coList.length-2){
                    ((Timer)(e.getSource())).stop();
                }
                
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
            
            timer.start();
        
            for(int z= 0;z<20;z++){
                System.out.println("System Out : "+z);
                  long sysTime = System.currentTimeMillis();
                final long end = sysTime + 1*1000;
                while(System.currentTimeMillis()<end){ 
                }
            }
        
//        Link current=s.getBottom();
//        webEngine.executeScript("document.setMarkerInitialPositionAt()");
      
        
//        webEngine.executeScript("document.setMarkerAt()");
        
//              long sysTime = System.currentTimeMillis();
//                final long end = sysTime + 1*1000;
//                while(System.currentTimeMillis()<end){ 
//                }
        
        
//        webEngine.executeScript("document.setMarkerInitialPositionAt(42,-71)");
//        int i=0;
//            while(i<9){
////            System.out.print("new google.maps.LatLng");
//            double lat = coList[i][0];
//            double lon = coList[i][1];
//            webEngine.executeScript("document.setMarkerAt("+lat+","+lon+")");
//            
//            long sysTime = System.currentTimeMillis();
//                final long end = sysTime + 1*1000;
//                while(System.currentTimeMillis()<end){ 
//                }
////            System.out.print(current+",");
////            System.out.println("");
////            current = current.previous; 
//                i++;
//        }
//
//        
        
//        while(current!=null){
////            System.out.print("new google.maps.LatLng");
//            double lat = current.getLatitude();
//            double lon = current.getLongitude();
//            webEngine.executeScript("document.setMarkerAt("+lat+","+lon+")");
//            
//            long sysTime = System.currentTimeMillis();
//                final long end = sysTime + 2*1000;
//                while(System.currentTimeMillis()<end){ 
//                }
//            System.out.print(current+",");
//            System.out.println("");
//            current = current.previous; 
//        }
    }
    
    public int getSpeed(double speed){
        
        if(speed >=10.0 && speed<20.0){
        return 38;    
        }
        if(speed >=20.0 && speed<30.0){
        return 32;    
        }
        if(speed >=30.0 && speed<40.0){
        return 27;    
        }
        if(speed >=40.0 && speed<50.0){
        return 23;    
        }
        if(speed >=50.0 && speed<60.0){
        return 18;    
        }
        if(speed >=60.0 && speed<70.0){
        return 12;    
        }
        if(speed >=70.0 && speed<80.0){
        return 5;    
        }
        else{
            return 20;
        }
        
    }
    
    
    
    
    public JSONObject createAndReturnJSON(Stack stack){
        
         JSONArray arr = new JSONArray();
         JSONObject obj = new JSONObject();
         JSONObject innerObj;
         
            Link current = stack.getBottom();
            while(current!=null){
            double lat = current.getLatitude();
            double lon = current.getLongitude();
            innerObj = new JSONObject();
            innerObj.put("lat",lat);
            innerObj.put("lon",lon);
//            String s = "new google.maps.LatLng("+current.getLatitude()+","+current.getLongitude()+")";
            arr.add(innerObj);
            current = current.previous; 
            }
         obj.put("arr", arr);
         return obj;
    }
    
    
    public void refreshCombo(ComboBox comboBox,ComboBox focusBox,Drone d){
        
        comboBox.getItems().removeAll();
        System.out.println("Accessing Combo Refresh");
        comboBox.getItems().add(d);
        focusBox.getItems().removeAll();
        System.out.println("Accessing FocusBox Refresh");
        focusBox.getItems().add(d);
    }
    
   
    
    public void refreshLeftPanels(){
        Drone d = (Drone)comboBox.getValue();
        initialLocationLabel.setText(String.valueOf(d.getInitialCordinate())) ;
        finalLocationLabel.setText(String.valueOf(d.getFinalCordinate())) ;
        initialAltitude.setText(String.valueOf(d.getInitialAltitude())) ;
        initialSpeed.setText(String.valueOf(d.getInitialspeed())) ;
        currentLat.setText(String.valueOf(d.getCurrentCordinate())) ;
        currentSpeed.setText(String.valueOf(d.getCurrentSpeed())) ;
        currentAltitude.setText(String.valueOf(d.getCurrentAltitude())) ;
        currentBattery.setText(String.valueOf(d.getCurrentBattery())) ;
        windSpeed.setText(String.valueOf(d.getWindSpeed())) ;
        temperature.setText(String.valueOf(d.getTemperature())) ;
        turbulence.setText(String.valueOf(d.getTurbulence())) ;
        rain.setText(String.valueOf(d.getRain())) ;
        icing.setText(String.valueOf(d.getIcing())) ;
    }

    public WebView getWeb() {
        return web;
    }

    
    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
    
    static { // use system proxy settings when standalone application    
        System.setProperty("java.net.useSystemProxies", "true");
    }
    
    public static void main(String[] args){
        
        Application.launch(args);
    }
}