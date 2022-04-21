package app.model;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.stage.Screen;

public class SimpleSplashScreen extends app.model.Screen {
	
	private Image image;
	private String vBoxStyle;
	private String firstMessage;
	private ObservableList<String> loadStrings;
	private String endMessage;
	private double fadeOut;
	private Stage stage;
	private ProgressBar loadProgress;
	private Label progressText;
	private VBox splashLayout;
	private double fitWidth;
	private double fitHeight;
	private double splashWidth;
	private double splashHeight;
	private String initMessage;
	private long delay=-1;
	private final long FINAL_DELAY=100;
	private long interval=-1;
	private final long PRESET_INTERVAL=500;
	private Class<? extends Application> main;
	private BooleanProperty closed;
	
	public void setMain(Class<? extends Application> main){
		this.main=main;
	}
	
	public SimpleSplashScreen(Image image, double fitWidth,double fitHeight,double splashWidth,
						double splashHeight, String vBoxStyle,String initMessage, 
						String firstMessage,ObservableList<String> loadStrings,
						String endMessage,double fadeOut,Stage stage){
		this.image=image;
		this.fitHeight=fitHeight;
		this.fitWidth=fitWidth;
		this.splashHeight=splashHeight;
		this.splashWidth=splashWidth;
		this.vBoxStyle=vBoxStyle;
		this.initMessage=initMessage;
		this.firstMessage=firstMessage;
		this.loadStrings=loadStrings;
		this.endMessage=endMessage;
		this.fadeOut=fadeOut;
		this.stage = stage;
	}

	public void init() {
		ImageView splash = new ImageView(image);
		splash.setFitHeight(fitHeight);
		splash.setFitWidth(fitWidth);
				 loadProgress = new ProgressBar();
				 loadProgress.setPrefWidth(splashWidth - 20);
				 progressText = new Label(initMessage);
				 splashLayout = new VBox();
				 splashLayout.getChildren().addAll(splash, loadProgress, progressText);
				 progressText.setAlignment(Pos.CENTER);
				 splashLayout.setStyle(vBoxStyle);
				 splashLayout.setEffect(new DropShadow());
	}
	public void start() {
		final Task<ObservableList<String>> loadingTask = new Task<ObservableList<String>>() {
			 @Override
			 protected ObservableList<String> call() throws InterruptedException {
			 ObservableList<String> downloaded =
			 FXCollections.<String>observableArrayList();
			 updateMessage(firstMessage);
			 for (int i = 0; i < loadStrings.size(); i++) {
				 if(interval<0)
					 Thread.sleep(PRESET_INTERVAL);
				 else{
					 Thread.sleep(interval);
				 }
			updateProgress(i + 1, loadStrings.size());
			 String next= loadStrings.get(i);
			 downloaded.add(next);
			 updateMessage(next+" . . .");
			 }
			 if(delay<0){
				 Thread.sleep(FINAL_DELAY);
			 }
			 else{
				 Thread.sleep(delay);
			 }
			 updateMessage(endMessage);
			 return downloaded;
			 }
			 };
			 showSplash(
			 stage,
			 loadingTask,
			 () -> closed.setValue(true)
			 );
			 new Thread(loadingTask).start();
	}
	private void showSplash(
			 final Stage initStage,
			 Task<?> task,
			 InitCompletionHandler initCompletionHandler
			 ) {
			 progressText.textProperty().bind(task.messageProperty());
			 loadProgress.progressProperty().bind(task.progressProperty());
			 task.stateProperty().addListener((observableValue, oldState, newState) ->
			{
			 if (newState == Worker.State.SUCCEEDED) {
				 loadProgress.progressProperty().unbind();
				 loadProgress.setProgress(1);
				 initStage.toFront();
				 FadeTransition fadeSplash = new
						 FadeTransition(Duration.seconds(fadeOut), splashLayout);
				 fadeSplash.setFromValue(1.0);
				 fadeSplash.setToValue(0.0);
				 fadeSplash.setOnFinished(actionEvent -> initStage.hide());
				 fadeSplash.play();
				 initCompletionHandler.complete();
			 	}
			 });
			 Scene splashScene = new Scene(splashLayout);
			 initStage.initStyle(StageStyle.UNDECORATED);
			 final Rectangle2D bounds = Screen.getPrimary().getBounds();
			 initStage.setScene(splashScene);
			 initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - splashWidth /2);
			 initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - splashHeight /2);
			 initStage.show();
			 }
			 public interface InitCompletionHandler {
				 public void complete();
			 }
	public void setFinalDelay(long delay){
		this.delay=delay;
	}
	public void setTimeDelay(long interval){
		this.interval=interval;
	}
	public void show(){
		closed = new SimpleBooleanProperty(false);
		init();
		start();
	}
	public BooleanProperty getClosed(){
		return closed;
	}
}
