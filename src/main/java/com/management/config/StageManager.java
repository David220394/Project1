package com.management.config;

import com.management.entity.FxmlView;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Manages switching Scenes on the Primary Stage
 */
public class StageManager {

    private final Stage primaryStage;
    private final SpringFXMLLoader springFXMLLoader;

    public StageManager(SpringFXMLLoader springFXMLLoader, Stage stage) {
        this.springFXMLLoader = springFXMLLoader;
        this.primaryStage = stage;
    }

    public SpringFXMLLoader getSpringFXMLLoader(){return this.springFXMLLoader;}

    public void switchScene(final FxmlView view) {
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
        show(viewRootNodeHierarchy, view.getTitle());
    }
    
    public void switchPane(final FxmlView view, Pane p) {
    	Pane pane = loadPane(view.getFxmlFile());
    	p.getChildren().add(pane);
    	p.toFront();
    }

    public Parent getParentView(final FxmlView view){
        return loadViewNodeHierarchy(view.getFxmlFile());
    }

    
    public void show(final Parent rootnode, String title) {
        Scene scene = prepareScene(rootnode);

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        
        try {
            primaryStage.show();
        } catch (Exception exception) {
            logAndExit ("Unable to show scene for title" + title,  exception);
        }
    }
    
    private Scene prepareScene(Parent rootnode){
        Scene scene = primaryStage.getScene();

        if (scene == null) {
            scene = new Scene(rootnode);
        }
        scene.setRoot(rootnode);
        return scene;
    }

    /**
     * Loads the object hierarchy from a FXML document and returns to root node
     * of that hierarchy.
     *
     * @return Parent root node of the FXML document hierarchy
     */
    private Parent loadViewNodeHierarchy(String fxmlFilePath) {
        Parent rootNode = null;
        try {
            rootNode = springFXMLLoader.load(fxmlFilePath);
            //rootNode.getStylesheets().add(getClass().getResource("/static/"));
            Objects.requireNonNull(rootNode, "A Root FXML node must not be null");
        } catch (Exception exception) {
            logAndExit("Unable to load FXML view" + fxmlFilePath, exception);
        }
        return rootNode;
    }
    
    public Pane loadPane(String fxmlFilePath) {
    	Pane pane = null;
    	try {
    		pane = (Pane) springFXMLLoader.load(fxmlFilePath);
    		
    	}catch(Exception e){
    		logAndExit("Unable to load FXML view" + fxmlFilePath, e);
    	}
    	return pane;
    }
    
    
    private void logAndExit(String errorMsg, Exception exception) {
        Platform.exit();
    }

}