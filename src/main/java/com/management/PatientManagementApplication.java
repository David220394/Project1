package com.management;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.management.config.StageManager;
import com.management.entity.FxmlView;

import javafx.scene.Parent;
import javafx.stage.Stage;

@SpringBootApplication
@ComponentScan(basePackages = "com.management")
public class PatientManagementApplication extends javafx.application.Application {

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;
    protected StageManager stageManager;

	public static void main(String[] args) {
	       launch(args);
		}

	    @Override
	    public void init() throws Exception {
	        springContext = springBootApplicationContext();

	    }

	    @Override
	    public void stop() {
	        springContext.stop();
	    }

	    @Override
	    public void start(Stage primaryStage) throws Exception {
	        stageManager = springContext.getBean(StageManager.class, primaryStage);
	        primaryStage.resizableProperty().set(false);
	        displayInitialScene();
	    }

	    protected void displayInitialScene() {
	        stageManager.switchScene(FxmlView.HOME);
	    }

	    private ConfigurableApplicationContext springBootApplicationContext() {
	        SpringApplicationBuilder builder = new SpringApplicationBuilder(PatientManagementApplication.class);
	        String[] args = getParameters().getRaw().stream().toArray(String[]::new);
	        return builder.run(args);
	    }
}
