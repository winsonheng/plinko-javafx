package app;
	
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import app.model.Chip;
import app.model.Pin;
import app.model.SimpleSplashScreen;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	private int[] pinX = {15,75,135,195,255,315,375,435,495,555,45,105,165,225,285,345,405,465,525,15,75,135,195,255,315,375,435,495,555,45,105,165,225,285,345,405,465,525,15,75,135,195,255,315,375,435,495,555,45,105,165,225,285,345,405,465,525,15,75,135,195,255,315,375,435,495,555,45,105,165,225,285,345,405,465,525,15,75,135,195,255,315,375,435,495,555};
	private int[] pinY = {125,125,125,125,125,125,125,125,125,125,177,177,177,177,177,177,177,177,177,229,229,229,229,229,229,229,229,229,229,281,281,281,281,281,281,281,281,281,333,333,333,333,333,333,333,333,333,333,385,385,385,385,385,385,385,385,385,437,437,437,437,437,437,437,437,437,437,489,489,489,489,489,489,489,489,489,541,541,541,541,541,541,541,541,541,541};
	private AnchorPane root;
	private Text balance;
	private Label remaining;
	private ArrayList<Label> names = new ArrayList<Label>();
	private ArrayList<Text> infos = new ArrayList<Text>();
	private Text winnings;
	private Text profit;
	private Label level;
	private int chipsToHitBottom;
	private int lScore;
	private int score;
	private int numOfChipsDropping;
	private boolean gameover;
	private Locale locale = new Locale("en", "SG");
	private ResourceBundle rb = ResourceBundle.getBundle("properties/Dictionary",locale);
	private Stage stage;
	private Shape intersect;
	private MediaPlayer mainPlayer;
	private Rectangle r;//right rectangle with the data
	private Rectangle bottomPad;//invisible line to check that chip has entered slot
	private Button saq;
	private ArrayList<Rectangle> dividers;
	private ArrayList<StackPane> slots;
	private final int[] chipX={80,194,308,422};
	private final int chipY=46;
	private int[] chipType = {0,1,2,3};
	private int[] highscores= new int[5];
	private String[] hsNames=new String[5];
	private BackgroundImage myBI;
	private Random ran = new Random();
	private Color[] colors = {Color.GOLD,Color.CYAN,Color.RED,Color.VIOLET,Color.BLUE,Color.ORANGE,Color.LIME};
	private final static int MAX_BALANCE = 99999999;
	
			 @Override public void start(Stage stage){
				 SimpleSplashScreen sss = new SimpleSplashScreen(new Image("MainImages/Splash.jpg"),382.5,232.5,400,500,"-fx-padding: 10; " +
						 "-fx-background-color: cornsilk; " +
						 "-fx-border-width:5; " +
						 "-fx-border-color: " +
						 "linear-gradient(" +
						 "to bottom, " +
						"chocolate, " +
						 "derive(chocolate, 50%)" +
						 ");","Initalizing download. . .","Initalizing download. . .",
						 FXCollections.observableArrayList(
								 "Reading From Files","Loading Images","Loading Music",
						 "Downloading Chips", "Downloading Buttons", "Downloading Pins", "Preparing Bank",
						 "Initializing UI", "Almost There", "Starting Game"
						 ),"Download Complete",1.2,stage);
				 sss.setTimeDelay(700);
				 sss.setFinalDelay(1000);
				 sss.show();
				 sss.getClosed().addListener((o, oldState, newState)->{
					 if(newState){
						 showMainStage();
					 }
				 });
			 }
	/**
	@Override
	public void init(){
		ImageView splash = new ImageView(new Image(SPLASH_IMAGE));
		splash.setFitHeight(232.5);
		splash.setFitWidth(382.5);
				 loadProgress = new ProgressBar();
				 loadProgress.setPrefWidth(SPLASH_WIDTH - 20);
				 progressText = new Label("Initializing download . . .");
				 splashLayout = new VBox();
				 splashLayout.getChildren().addAll(splash, loadProgress, progressText);
				 progressText.setAlignment(Pos.CENTER);
				 splashLayout.setStyle(
				 "-fx-padding: 10; " +
				 "-fx-background-color: cornsilk; " +
				 "-fx-border-width:5; " +
				 "-fx-border-color: " +
				 "linear-gradient(" +
				 "to bottom, " +
				"chocolate, " +
				 "derive(chocolate, 50%)" +
				 ");"
				 );
				 splashLayout.setEffect(new DropShadow());
	}
	@Override
	public void start(final Stage initStage) {
		final Task<ObservableList<String>> loadingTask = new Task<ObservableList<String>>() {
			 @Override
			 protected ObservableList<String> call() throws InterruptedException {
			 ObservableList<String> downloaded =
			 FXCollections.<String>observableArrayList();
			 ObservableList<String> loadStrings =
			 FXCollections.observableArrayList(
					 "Reading From Files","Loading Images","Loading Music",
			 "Downloading Chips", "Downloading Buttons", "Downloading Pins", "Preparing Bank",
			 "Initializing UI", "Almost There", "Starting Game"
			 );
			 updateMessage("Initalizing download. . .");
			 for (int i = 0; i < loadStrings.size(); i++) {
			 Thread.sleep(700);
			updateProgress(i + 1, loadStrings.size());
			 String next= loadStrings.get(i);
			 downloaded.add(next);
			 updateMessage(next+" . . .");
			 }
			 Thread.sleep(1000);
			 updateMessage("Download Complete");
			 return downloaded;
			 }
			 };
			 showSplash(
			 initStage,
			 loadingTask,
			 () -> showMainStage(loadingTask.valueProperty())
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
						 FadeTransition(Duration.seconds(1.2), splashLayout);
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
			 initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH /2);
			 initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT /2);
			 initStage.show();
			 }
			 public interface InitCompletionHandler {
				 public void complete();
			 }
	 */
			 public void showMainStage(){
					try{
						stage = new Stage();
						root = new AnchorPane();
						Scanner in;
						try {
							in = new Scanner(new FileInputStream("data/Highscores.txt"));
							String next="";
							int i=0;
							in.useDelimiter(",");
							while(in.hasNext()){
								next=in.next();
								highscores[i]=Integer.parseInt(next.substring(next.lastIndexOf(" ")+1));
								hsNames[i]=next.substring(0,next.lastIndexOf(" "));
								++i;
							}
							in.close();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						startScreen();
						Scene scene = new Scene(root,777,700);
						stage.setScene(scene);
						stage.setTitle(rb.getString("title"));
						stage.getIcons().add(new Image("MainImages/Icon.jpg"));
						stage.show();
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
		 
	public void startScreen(){
		stage.setTitle(rb.getString("title"));
		root.getChildren().clear();
		Chip.resetUnlock();
		Chip.resetCost();
		myBI= new BackgroundImage(new Image("MainImages/Background.jpg",700,777,false,true),
		        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
		          BackgroundSize.DEFAULT);
		root.setBackground(new Background(myBI));
		if(mainPlayer!=null){
			mainPlayer.stop();
		}
		Rectangle high = new Rectangle(275,290,227,70);
		high.setArcHeight(70);
		high.setArcWidth(70);
		high.setFill(Color.GOLDENROD);
		high.setOnMouseClicked(e->showHighScores(false));
		Text highscores= new Text(rb.getString("highscores"));
		highscores.setOnMouseClicked(e->showHighScores(false));
		highscores.setLayoutX(298);
		highscores.setLayoutY(338);
		highscores.setStyle("-fx-font: 35 arial");
		highscores.setFill(Color.WHITE);
		Rectangle resume = new Rectangle(275,200,227,70);
		resume.setArcHeight(70);
		resume.setArcWidth(70);
		resume.setFill(Color.GOLDENROD);
		resume.setOnMouseClicked(e->resumeGame());
		Text res = new Text(rb.getString("resume"));
		res.setOnMouseClicked(e->resumeGame());
		res.setLayoutX(298);
		res.setLayoutY(248);
		res.setStyle("-fx-font: 35 arial");
		res.setFill(Color.WHITE);
		Scanner sc;
		try {
			sc = new Scanner(new FileInputStream("data/Data.txt"));
			if(sc.nextLine().equals("false")){
				resume.setDisable(true);
				resume.setOpacity(0.5);
				res.setDisable(true);
				res.setOpacity(0.5);
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Rectangle start = new Rectangle(275,110,227,70);
		start.setArcHeight(70);
		start.setArcWidth(70);
		start.setFill(Color.GOLDENROD);
		start.setOnMouseClicked(e->startGame());
		Text startGame = new Text(rb.getString("startGame"));
		startGame.setOnMouseClicked(e->startGame());
		startGame.setLayoutX(298);
		startGame.setLayoutY(158);
		startGame.setStyle("-fx-font: 35 arial;");
		startGame.setFill(Color.WHITE);
		Rectangle help = new Rectangle(275,380,227,70);
		help.setArcHeight(70);
		help.setArcWidth(70);
		help.setFill(Color.GOLDENROD);
		help.setOnMouseClicked(e->showHelp());
		Text helpText = new Text(rb.getString("Help"));
		helpText.setOnMouseClicked(e->showHelp());
		helpText.setLayoutX(333);
		helpText.setLayoutY(428);
		helpText.setStyle("-fx-font: 35 arial;");
		helpText.setFill(Color.WHITE);
		Rectangle quit = new Rectangle(275,470,227,70);
		quit.setArcHeight(70);
		quit.setArcWidth(70);
		quit.setFill(Color.GOLDENROD);
		quit.setOnMouseClicked(e->System.exit(0));
		Text quitGame = new Text(rb.getString("quit"));
		quitGame.setOnMouseClicked(e->System.exit(0));
		quitGame.setLayoutX(353);
		quitGame.setLayoutY(518);
		quitGame.setStyle("-fx-font: 35 arial;");
		quitGame.setFill(Color.WHITE);
		Text select = new Text(rb.getString("selectLanguage"));
		select.setFill(Color.WHITE);
		select.setLayoutX(275);
		select.setLayoutY(585);
		select.setStyle("-fx-font: 30 arial;");
		Circle sg = new Circle(285,645,35);
		sg.setFill(new ImagePattern(new Image("flags/SGFlag.jpg")));
		sg.setOnMouseClicked(e->{
			if(!locale.getCountry().equals("SG")){
				locale = new Locale("en","SG");
				rb = ResourceBundle.getBundle("properties/Dictionary", locale);
				startScreen();
			}
		});
		Circle cn = new Circle(385,645,35);
		cn.setFill(new ImagePattern(new Image("flags/CNFlag.jpg")));
		cn.setOnMouseClicked(e->{
			if(!locale.getCountry().equals("CN")){
				locale = new Locale("zh","CN");
				rb = ResourceBundle.getBundle("properties/Dictionary",locale);
				startScreen();
			}
		});
		Circle in = new Circle(485,645,35);
		in.setFill(new ImagePattern(new Image("flags/INFlag.jpg")));
		in.setOnMouseClicked(e->{
			if(!locale.getCountry().equals("IN")){
				locale = new Locale("ta","IN");
				rb = ResourceBundle.getBundle("properties/Dictionary", locale);
				startScreen();
			}
		});
		root.getChildren().addAll(resume,res,start,startGame,high,highscores,help,helpText,quit,quitGame,select,sg,cn,in);
	}
	public void showHelp(){
		root.setBackground(null);
		root.getChildren().clear();
		ImageView image = new ImageView(new Image("Help/Help.jpg"));
		image.setLayoutX(105);
		image.setLayoutY(25);
		Circle drag = new Circle(16);
		drag.setFill(new ImagePattern(new Image("Help/DragChip.jpg")));
		Path path = new Path();
		path.getElements().add(new MoveTo(184,77));
		path.getElements().add(new CubicCurveTo(250,130,184,77,250,130));
		PathTransition pt = new PathTransition();
		pt.setDuration(Duration.millis(1500));
		pt.setNode(drag);
		pt.setPath(path);
		pt.setCycleCount(Timeline.INDEFINITE);
		pt.play();
		Chip target = new Chip(0);
		target.setLayoutX(250);
		target.setLayoutY(130);
		target.setOpacity(0.4);
		Label text = new Label(rb.getString("helpText"));
		text.setMaxWidth(575);
		text.setLayoutX(105);
		text.setLayoutY(200);
		text.setStyle("-fx-font: 24 Arial");
		text.setTextFill(Color.GREEN);
		Rectangle back = new Rectangle(275,570,227,70);
		back.setArcHeight(70);
		back.setArcWidth(70);
		back.setFill(Color.GOLDENROD);
		back.setOnMouseClicked(e->startScreen());
		Text goback = new Text(rb.getString("back"));
		goback.setOnMouseClicked(e->startScreen());
		goback.setLayoutX(327);
		goback.setLayoutY(618);
		goback.setStyle("-fx-font: 35 arial;");
		goback.setFill(Color.WHITE);
		root.getChildren().addAll(image,target,drag,text,back,goback);
	}
	public void resumeGame(){
		startGame();
		Scanner sc;
		try{
			sc = new Scanner(new FileInputStream("data/Data.txt"));
			sc.nextLine();
			int lvl=Integer.parseInt(sc.nextLine());
			for(int i=2;i<=lvl;++i){
				Chip.increaseCost();
				Chip.unlockByLevel(i);
			}
			for(int i=0;i<infos.size();++i){
				Chip chip= new Chip(i);
				infos.get(i).setText("   ×"+chip.getMultiplier()+"\n "+rb.getString("$")+chip.getCost());
			}
			level.setText(rb.getString("level")+": "+lvl);
			balance.setText(sc.nextLine());
			int j = Integer.parseInt(sc.nextLine());
			chipsToHitBottom=j;
			remaining.setText(rb.getString("chipsLeft")+": "+j);
			winnings.setText(rb.getString("winnings")+":\n"+rb.getString("$")+sc.nextLine());
			profit.setText(rb.getString("profit")+":\n"+rb.getString("$")+sc.nextLine());
			score = Integer.parseInt(sc.nextLine());
			lScore = Integer.parseInt(sc.nextLine());
			setSlotValues();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	public void startGame(){//coding for all the UI
		numOfChipsDropping=0;
		gameover=false;
		root.getChildren().clear();
		root.setBackground(null);
		names.clear();
		infos.clear();
		chipsToHitBottom=5;
		score=0;
		lScore=0;
		for(int i=0;i<4;++i){
			chipType[i]=i;
		}
		for(int i =0;i<pinX.length; ++i){
			root.getChildren().add(new Circle(6));
			root.getChildren().get(i).setVisible(false);
			root.getChildren().get(i).setLayoutX(pinX[i]);
			root.getChildren().get(i).setLayoutY(pinY[i]);
		}
		for(int i =0;i<pinX.length; ++i){
			root.getChildren().add(new Pin());
			root.getChildren().get(i+86).setLayoutX(pinX[i]);
			root.getChildren().get(i+86).setLayoutY(pinY[i]);
		}
		Media m = new Media("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/music/"+rb.getString("music")+".mp3");
			mainPlayer = new MediaPlayer(m);
			mainPlayer.setCycleCount(Timeline.INDEFINITE);
			mainPlayer.play();
        MediaView mv = new MediaView(mainPlayer);
        root.getChildren().add(mv);
		bottomPad = new Rectangle(0,700,777,20);
		bottomPad.setFill(Color.web("#20d326"));
		r = new Rectangle(574,7,197,690);
		r.setFill(Color.web("#20d326"));
		r.setStroke(Color.web("#20d326"));
		r.setStrokeWidth(10);
		Rectangle bankSign = new Rectangle(165,35);
		bankSign.setFill(Color.BLACK);
		bankSign.setStroke(Color.SILVER);
		bankSign.setStrokeWidth(3);
		Text text = new Text(rb.getString("bank"));
		text.setFill(Color.WHITE);
		text.setStyle("-fx-font: 24 arial;");
		StackPane pane = new StackPane();
		pane.setLayoutX(589);
		pane.setLayoutY(107);
		pane.getChildren().addAll(bankSign,text);
		Rectangle link1 = new Rectangle(630,145,15,50);
		link1.setFill(Color.BLACK);
		link1.setSmooth(false);
		Rectangle link2 = new Rectangle(700,145,15,50);
		link2.setFill(Color.BLACK);
		link2.setSmooth(false);
		Rectangle bank = new Rectangle(589,195,165,150);
		bank.setFill(Color.DIMGRAY);
		bank.setStroke(Color.SILVER);
		bank.setStrokeWidth(3);
		Text dollarSign = new Text(rb.getString("$"));
		dollarSign.setStyle("-fx-font: 30 input;"+"-fx-font-style: italic bold");
		dollarSign.setLayoutX(600);
		dollarSign.setLayoutY(229);
		dollarSign.setFill(Color.GREEN);
		balance = new Text("100");
		balance.setStyle("-fx-font: 25 input;");
		Rectangle balanceBox = new Rectangle(149,30);
		balanceBox.setFill(Color.WHITE);
		StackPane pane2 = new StackPane();
		pane2.setLayoutX(597);
		pane2.setLayoutY(202);
		pane2.getChildren().addAll(balanceBox,balance);
		Circle c = new Circle(7);
		c.setLayoutX(630);
		c.setLayoutY(290);
		c.setFill(Color.BLACK);
		Rectangle handle1 = new Rectangle(627,260,6,30);
		handle1.setArcHeight(50);
		handle1.setArcWidth(50);
		Rectangle handle2 = new Rectangle(615,283,6,30);
		handle2.setArcHeight(50);
		handle2.setArcWidth(50);
		handle2.setRotate(240);
		Rectangle handle3 = new Rectangle(641,283,6,30);
		handle3.setArcHeight(50);
		handle3.setArcWidth(50);
		handle3.setRotate(120);
		Polygon buttonLeft = new Polygon();
		buttonLeft.getPoints().addAll(new Double[]{
				15.0, 40.0,
				40.0, 25.0,
				40.0, 55.0 }
				);
		buttonLeft.setFill(Color.RED);
		buttonLeft.setStroke(Color.RED);
		buttonLeft.setStrokeWidth(14);
		buttonLeft.setStrokeLineJoin(StrokeLineJoin.ROUND);
		buttonLeft.setOnMouseClicked(event -> {
			if(chipType[0]!=0){
			for(int i=0;i<root.getChildren().size();++i){
				if(root.getChildren().get(i) instanceof Chip&&!((Chip)root.getChildren().get(i)).isChipReleased()){
					root.getChildren().remove(i);
					--i;
				}
			}
			for(int j=0;j<4;++j){
				--chipType[j];
			}
			for(int j=0;j<4;++j){
				Chip chip = new Chip(chipType[j]);
				chip.setLayoutX(chipX[j]);
				chip.setLayoutY(chipY);
				if(!chip.isUnlocked()){
					chip.setOpacity(0.5);
				}
				root.getChildren().add(chip);
				names.get(j).setText(rb.getString(chip.getTypeName()));
				infos.get(j).setText("   ×"+chip.getMultiplier()+"\n "+rb.getString("$")+chip.getCost());
				Wrapper<Point2D> mouseLocation = new Wrapper<>();
				setUpDragging(chip,mouseLocation);
			}
			}
		});
		Polygon buttonRight = new Polygon();
		buttonRight.getPoints().addAll(new Double[]{
			            					550.0, 40.0,
			            					525.0, 25.0,
			            					525.0, 55.0 }
		);
		buttonRight.setFill(Color.RED);
		buttonRight.setStroke(Color.RED);
		buttonRight.setStrokeWidth(14);
		buttonRight.setStrokeLineJoin(StrokeLineJoin.ROUND);
		buttonRight.setOnMouseClicked(event -> {
			if(chipType[3]<7){
			for(int i=0;i<root.getChildren().size();++i){
				if(root.getChildren().get(i) instanceof Chip&&!((Chip)root.getChildren().get(i)).isChipReleased()){
					root.getChildren().remove(i);
					--i;
				}
			}
			for(int j=0;j<4;++j){
				++chipType[j];
			}
			for(int j=0;j<4;++j){
				Chip chip = new Chip(chipType[j]);
				chip.setLayoutX(chipX[j]);
				chip.setLayoutY(chipY);
				if(!chip.isUnlocked()){
					chip.setOpacity(0.5);
				}
				root.getChildren().add(chip);
				names.get(j).setText(rb.getString(chip.getTypeName()));
				infos.get(j).setText("   ×"+chip.getMultiplier()+"\n "+rb.getString("$")+chip.getCost());
				Wrapper<Point2D> mouseLocation = new Wrapper<>();
				setUpDragging(chip,mouseLocation);
			}
			}
		});
		Wrapper<Point2D> mouseLocation = new Wrapper<>();
		for(int i=0;i<4;++i){
			Rectangle rect = new Rectangle(55+i*114,2,114,70);
			rect.setFill(Color.GREENYELLOW);
			rect.setStroke(Color.RED);
			rect.setStrokeWidth(5);
			root.getChildren().add(rect);
			Chip chip = new Chip(i);
			Label name = new Label(rb.getString(chip.getTypeName()));
			name.setStyle("-fx-font: 20 Arial");
			name.setLayoutX(79+i*114);
			name.setLayoutY(5);
			name.setMinSize(100, 20);
			name.setTextAlignment(TextAlignment.CENTER);
			names.add(name);
			Text info = new Text("   ×"+chip.getMultiplier()+"\n "+rb.getString("$")+chip.getCost());
			info.setLayoutX(100+i*114);
			info.setLayoutY(42);
			info.setStyle("-fx-font: 18 Arial");
			infos.add(info);
			root.getChildren().addAll(name,info);
		}
		remaining = new Label(rb.getString("chipsLeft")+": 5");
		remaining.setStyle("-fx-font: 25 Arial;"+"-fx-font-style: italic");
		remaining.setLayoutX(590);
		remaining.setLayoutY(375);
		winnings = new Text(rb.getString("winnings")+":\n"+rb.getString("$")+"0");
		winnings.setStyle("-fx-font: 25 Arial;"+"-fx-font-style: italic");
		winnings.setLayoutX(590);
		winnings.setLayoutY(450);
		profit = new Text(rb.getString("profit")+":\n"+rb.getString("$")+"0");
		profit.setStyle("-fx-font: 25 Arial;"+"-fx-font-style: italic");
		profit.setLayoutX(590);
		profit.setLayoutY(525);
		level = new Label(rb.getString("level")+": 1");
		level.setStyle("-fx-font: 40 Arial;"+"-fx-font-style: italic bold");
		level.setLayoutX(585);
		level.setLayoutY(35);
		slots = new ArrayList<StackPane>();
		dividers= new ArrayList<Rectangle>();
		for(int i=0; i<9;++i){
			Rectangle divider = new Rectangle(11+60*i,600,8,100);
			divider.setFill(Color.BLUE);
			dividers.add(divider);
			Rectangle slot = new Rectangle(60,100);
			slot.setFill(Color.WHITE);
			StackPane s = new StackPane();
			Text value = new Text("");
			value.setStyle("-fx-font: 20 Arial");
			value.setFill(Color.BLACK);
			s.setLayoutX(15+60*i);
			s.setLayoutY(600);
			value.setRotate(270);
			slots.add(s);
			s.getChildren().addAll(slot,value);
			root.getChildren().add(s);
			root.getChildren().add(divider);
		}
		Rectangle divider = new Rectangle(551,600,8,100);
		divider.setFill(Color.BLUE);
		dividers.add(divider);
		setSlotValues();
		saq = new Button(rb.getString("saveAndQuit"));
		saq.setLayoutX(590);
		saq.setLayoutY(600);
		saq.setStyle("-fx-font: 25 Arial");
		saq.setOnAction(e->{
			saveProgress(true);
			startScreen();
			});
		root.getChildren().addAll(r,bottomPad,pane,link1,link2,bank,pane2,dollarSign,c,handle1,handle2,handle3,buttonLeft,buttonRight,remaining,winnings,profit,level,divider,saq);
		for(int i =0; i<4;++i){
			Chip chip = new Chip(i);
			chip.setLayoutX(chipX[i]);
			chip.setLayoutY(chipY);
			if(!chip.isUnlocked()){
				chip.setOpacity(0.5);
			}
			root.getChildren().add(chip);
			setUpDragging(chip, mouseLocation);
			
		}
	}
	
	public void saveProgress(boolean canResume){//canResume determines if there is a resumable game
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream("data/Data.txt"));
			out.print(canResume+"\n"+level.getText().substring(level.getText().indexOf(" ")+1)
					+"\n"+balance.getText()+"\n"
					+remaining.getText().substring(remaining.getText().indexOf(":")+2)+"\n"
					+winnings.getText().substring(winnings.getText().indexOf(rb.getString("$"))+1)+"\n"
					+profit.getText().substring(profit.getText().indexOf(rb.getString("$"))+1)+"\n"
					+score+"\n"
					+lScore);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void saveHighscores(){
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream("data/Highscores.txt"));
			out.print(hsNames[0]+" "+highscores[0]);
			for(int i=1;i<5;++i){
				out.print(","+hsNames[i]+" "+highscores[i]);
			}
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void showHighScores(boolean gameover){
		if(score>=highscores[0]){
			for(int i=4;i>0;--i){
				highscores[i]=highscores[i-1];
				hsNames[i]=hsNames[i-1];
			}
			highscores[0]=score;
		}
		else if(score<highscores[4]){
			
		}
		else{
			for(int i=0;i<4;++i){
				if(score<=highscores[i]&&score>=highscores[i+1]){
					for(int j=4;j>i+1;--j){
						highscores[j]=highscores[j-1];
						hsNames[j]=hsNames[j-1];
					}
					highscores[i+1]=score;
					break;
				}
			}
		}
		boolean highest=false;
		Rectangle board = new Rectangle(50,182,470,302);
		if(gameover)
			board.setFill(Color.GOLDENROD);
		else
			board.setFill(Color.GREEN);
		root.getChildren().add(board);
		Rectangle next = new Rectangle(192.5,405,185,55);
		next.setArcHeight(40);
		next.setArcWidth(40);
		next.setFill(Color.YELLOW);
		Text finish=new Text();
		if(gameover)
			finish.setText(rb.getString("finish"));
		else
			finish.setText("  "+rb.getString("back"));
		finish.setLayoutX(212.5);
		finish.setLayoutY(440);
		finish.setStyle("-fx-font: 30 Arial");
		finish.setFill(Color.GREEN);
		root.getChildren().addAll(next,finish);
		next.setOnMouseClicked(e->{
			saveHighscores();
			startScreen();
			});
		finish.setOnMouseClicked(e->{
			saveHighscores();
			startScreen();
			});
		for(int i=0;i<5;++i){
			Text text = new Text(hsNames[i]);
			text.setLayoutX(80);
			text.setLayoutY(230+i*40);
			text.setStyle("-fx-font: 22 Arial");
			text.setFill(Color.WHITE);
			Text sc = new Text(highscores[i]+"");
			sc.setLayoutX(345);
			sc.setLayoutY(230+i*40);
			sc.setStyle("-fx-font: 22 Arial");
			sc.setFill(Color.WHITE);
			if(gameover){
			if(score==Integer.parseInt(sc.getText())&&!highest){
				highest=true;
				next.setDisable(true);
				finish.setDisable(true);
				next.setOpacity(0.35);
				finish.setOpacity(0.35);
				sc.setFill(Color.GREEN);
				text.setFill(Color.GREEN);
				text.setText("");
				TextField name = new TextField();
				name.setLayoutX(80);
				name.setLayoutY(200+i*40);
				name.setPrefSize(225, 40);
				name.setStyle("-fx-font: 25 Arial");
				name.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%);");
				name.setPromptText("Enter name");
				name.setFont(Font.font(20));
				name.setOnKeyPressed(e->{
					if(e.getCode().equals(KeyCode.ENTER)&&!name.getText().equals("")){
						if(name.getText().length()<13){
							text.setText(name.getText());
							hsNames[(int)(text.getLayoutY()-230)/40]=name.getText();
							root.getChildren().remove(name);
							next.setDisable(false);
							finish.setDisable(false);
							next.setOpacity(1);
							finish.setOpacity(1);
						}
						else{
							name.clear();
							name.setPromptText("1-12 CHARS ONLY!!!");
						}
					}
				});
				root.getChildren().add(name);
			}
			}
			root.getChildren().addAll(text,sc);
		}
	}
	
	public void levelComplete(){
		saq.setDisable(true);
		score+=lScore;
		Rectangle board = new Rectangle(50,182,470,302);
		board.setFill(Color.GREEN);
		Label grats= new Label(rb.getString("greatJob"+new Random().nextInt(3))+"!");
		grats.setLayoutX(80);
		grats.setLayoutY(200);
		grats.setStyle("-fx-font: 50 Arial");
		grats.setTextFill(Color.GOLDENROD);
		Label levelScore = new Label(rb.getString("levelScore")+": "+rb.getString("$")+lScore);
		levelScore.setLayoutX(80);
		levelScore.setLayoutY(285);
		levelScore.setStyle("-fx-font: 35 Arial");
		levelScore.setTextFill(Color.GOLDENROD);
		Label totalScore = new Label(rb.getString("totalScore")+": "+rb.getString("$")+score);
		totalScore.setLayoutX(80);
		totalScore.setLayoutY(335);
		totalScore.setStyle("-fx-font: 35 Arial");
		totalScore.setTextFill(Color.GOLDENROD);
		root.getChildren().addAll(board,grats,levelScore,totalScore);
		if(Integer.parseInt(level.getText().substring(level.getText().indexOf(" ")+1))==26){
			Rectangle next = new Rectangle(192.5,395,185,55);
			next.setArcHeight(40);
			next.setArcWidth(40);
			next.setFill(Color.GOLDENROD);
			Text finish = new Text(rb.getString("finish"));
			finish.setLayoutX(212.5);
			finish.setLayoutY(430);
			finish.setStyle("-fx-font: 30 Arial");
			finish.setFill(Color.GREEN);
			root.getChildren().addAll(next,finish);
			next.setOnMouseClicked(e->{
				saveProgress(false);
				saveHighscores();
				showHighScores(true);
				});
			finish.setOnMouseClicked(e->{
				saveProgress(false);
				saveHighscores();
				showHighScores(true);
				});
		}
		else{
			Rectangle next = new Rectangle(80,395,185,55);
			next.setArcHeight(40);
			next.setArcWidth(40);
			next.setFill(Color.GOLDENROD);
			Rectangle exit = new Rectangle(305,395,185,55);
			exit.setArcHeight(40);
			exit.setArcWidth(40);
			exit.setFill(Color.GOLDENROD);
			Text nextLevel = new Text(rb.getString("continue"));
			nextLevel.setLayoutX(100);
			nextLevel.setLayoutY(430);
			nextLevel.setStyle("-fx-font: 30 Arial");
			nextLevel.setFill(Color.GREEN);
			Text quit = new Text(rb.getString("quit"));
			quit.setLayoutX(365);
			quit.setLayoutY(430);
			quit.setStyle("-fx-font: 30 Arial");
			quit.setFill(Color.WHITE);
			next.setOnMouseClicked(e->nextLevel());
			nextLevel.setOnMouseClicked(e->nextLevel());
			quit.setOnMouseClicked(e->{nextLevel();saveProgress(true);startScreen();});
			exit.setOnMouseClicked(e->{nextLevel();saveProgress(true);startScreen();});
			root.getChildren().addAll(next,exit,nextLevel,quit);
		}
	}
	public void nextLevel(){
		saq.setDisable(false);
		Chip.increaseCost();
		for(int i=0;i<infos.size();++i){
			Chip chip= new Chip(chipType[i]);
			infos.get(i).setText("   ×"+chip.getMultiplier()+"\n "+rb.getString("$")+chip.getCost());
		}
		lScore=0;
		chipsToHitBottom=5;
		winnings.setText(rb.getString("winnings")+":\n"+rb.getString("$")+"0");
		profit.setText(rb.getString("profit")+":\n"+rb.getString("$")+"0");
		level.setText(rb.getString("level")+": "+(Integer.parseInt(level.getText().substring(level.getText().indexOf(" ")+1))+1));
		for(int i=0;i<8;++i){
			root.getChildren().remove(root.getChildren().size()-1);
		}
		Chip.unlockByLevel(Integer.parseInt(level.getText().substring(level.getText().indexOf(" ")+1)));
		for(int i=0;i<root.getChildren().size();++i){
			if(root.getChildren().get(i) instanceof Chip&&((Chip) root.getChildren().get(i)).isReachedBottom()){
				root.getChildren().remove(i);
				--i;
			}
			if(root.getChildren().get(i) instanceof Chip&&((Chip)root.getChildren().get(i)).isUnlocked()){
				root.getChildren().get(i).setOpacity(1);
			}
		}
		setSlotValues();
		chipsToHitBottom=5;
		remaining.setText(rb.getString("chipsLeft")+": 5");
	}
	
	public void gameover(){
		gameover=true;
		saq.setDisable(true);
		mainPlayer.stop();
		score+=lScore;
		for(int i=0;i<root.getChildren().size();++i){
			if(root.getChildren().get(i) instanceof Chip&&((Chip)root.getChildren().get(i)).isChipReleased()){
				root.getChildren().remove(i);
				--i;
			}
		}
		Rectangle board = new Rectangle(50,182,470,302);
		board.setFill(Color.RED);
		Label grats= new Label(rb.getString("gameover")+"!");
		grats.setLayoutX(80);
		grats.setLayoutY(200);
		grats.setStyle("-fx-font: 50 Arial");
		grats.setTextFill(Color.GOLDENROD);
		Label levelScore = new Label(rb.getString("levelScore")+": "+rb.getString("$")+lScore);
		levelScore.setLayoutX(80);
		levelScore.setLayoutY(285);
		levelScore.setStyle("-fx-font: 35 Arial");
		levelScore.setTextFill(Color.GOLDENROD);
		Label totalScore = new Label(rb.getString("totalScore")+": "+rb.getString("$")+score);
		totalScore.setLayoutX(80);
		totalScore.setLayoutY(335);
		totalScore.setStyle("-fx-font: 35 Arial");
		totalScore.setTextFill(Color.GOLDENROD);
		Rectangle next = new Rectangle(192.5,395,185,55);
		next.setArcHeight(40);
		next.setArcWidth(40);
		next.setFill(Color.GOLDENROD);
		Text finish = new Text(rb.getString("finish"));
		finish.setLayoutX(212.5);
		finish.setLayoutY(430);
		finish.setStyle("-fx-font: 30 Arial");
		finish.setFill(Color.GREEN);
		next.setOnMouseClicked(e->{
			saveProgress(false);
			saveHighscores();
			showHighScores(true);
		});
		finish.setOnMouseClicked(e->{
			saveProgress(false);
			saveHighscores();
			showHighScores(true);
			});
		root.getChildren().addAll(board,grats,levelScore,totalScore,next,finish);
		Media m = new Media("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/music/Bust.mp3");
        MediaPlayer player = new MediaPlayer(m);
        player.play();
        root.getChildren().add(new MediaView(player));
	}
	
	public void setSlotValues(){
		try {
			Scanner s = new Scanner(new FileInputStream("data/Slots.txt"));
			String line="";
			String[] values = new String[9];
			int[] chosen = {1,1,1,1,1,1,1,1,1};
			Random r = new Random();
			int i=Integer.parseInt(level.getText().substring(level.getText().indexOf(" ")+1));
			while(i>0){
				line=s.nextLine();
				--i;
			}
			s.close();
			Scanner lsc = new Scanner(line);
			lsc.useDelimiter(",");
			while(lsc.hasNext()){
				values[i]=lsc.next();
				++i;
			}
			lsc.close();
			String biggest = values[0];
			String str;
			int ran;
			for(int j=0;i>0;++j){
				ran = r.nextInt(9);
				if(chosen[ran]==0){
					--j;
					continue;
				}
				chosen[ran]=0;
				str=values[ran];
				((Text) slots.get(j).getChildren().get(1)).setFill(Color.BLACK);
				((Rectangle) slots.get(j).getChildren().get(0)).setFill(Color.WHITE);
				((Text) slots.get(j).getChildren().get(1)).setText(str);
				--i;
				if(str.equals("BUST")){
					((Text) slots.get(j).getChildren().get(1)).setText(rb.getString(str));
					((Text) slots.get(j).getChildren().get(1)).setFill(Color.WHITE);
					((Rectangle) slots.get(j).getChildren().get(0)).setFill(Color.BLACK);
				}
				if(str.startsWith("-")){
					((Text) slots.get(j).getChildren().get(1)).setFill(Color.WHITE);
					((Rectangle) slots.get(j).getChildren().get(0)).setFill(Color.RED);
				}
				if(str.equals(biggest)){
					((Text) slots.get(j).getChildren().get(1)).setFill(Color.GOLD);
					((Rectangle) slots.get(j).getChildren().get(0)).setFill(Color.GREEN);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void setUpDragging(Chip circle, Wrapper<Point2D> mouseLocation) {
		circle.setOnMouseDragged(event -> {
			if(/**chipDropping!=null&&(!chipDropping.isReachedBottom()||*/chipsToHitBottom==0||!circle.isChipDragged()){
				return;
			}
            if (!circle.isChipReleased()&&mouseLocation.value != null&&circle.isUnlocked()&&(Integer.parseInt(balance.getText())>=circle.getCost()||circle.isChipDragged())){//&&mouseLocation.value.getX()>circle.getRadius()&&mouseLocation.value.getX()<550&&mouseLocation.value.getY()<90) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                
                double newX = circle.getLayoutX() + deltaX ;
                circle.setLayoutX(newX);
                double newY = circle.getLayoutY() + deltaY ;
                circle.setLayoutY(newY);
            }
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
        });
		circle.setOnDragDetected(event -> {
		if(/**(chipDropping==null||chipDropping.isReachedBottom())&&(chipsToHitBottom>0&&*/circle.isUnlocked()&&Integer.parseInt(remaining.getText().substring(remaining.getText().lastIndexOf(" ")+1))>0&&!circle.isChipReleased()&&Integer.parseInt(balance.getText())>=circle.getCost()&&numOfChipsDropping<3){
			circle.setChipDragged(true);
			Chip chip = new Chip(circle.getType());
			for(int i=0;i<4;++i){
				if(circle.getType()==chipType[i]){
					chip.setLayoutX(chipX[i]);
					chip.setLayoutY(chipY);
			
				}
			}
			root.getChildren().add(chip);
			balance.setText((Integer.parseInt(balance.getText())-circle.getCost())+"");
			remaining.setText(rb.getString("chipsLeft")+": " +(Integer.parseInt(remaining.getText().substring(remaining.getText().lastIndexOf(" ")+1))-1));
			setUpDragging(chip,mouseLocation);
			circle.getParent().setCursor(Cursor.CLOSED_HAND);
			mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
		}else{
			//do nothing
		}
	    });

	    circle.setOnMouseReleased(event -> {
	    	/**
	    	if(chipDropping!=null){
	    		if(!chipDropping.isReachedBottom())
	    			return;
	    	}
	    	*/
	    if(circle.isChipDragged()==false||circle.isChipReleased()){
	    	circle.getParent().setCursor(Cursor.DEFAULT);
		    mouseLocation.value = null ;
		    return;
	    }
	    
	    if(circle.getLayoutX()<circle.getRadius()){
	    	circle.setLayoutX(circle.getRadius());
	    }
	    if(circle.getLayoutX()>550){
	    	circle.setLayoutX(550);
	    }
	    if(circle.getLayoutY()<circle.getRadius()){
	    	circle.setLayoutY(circle.getRadius());
	    }
	    if(circle.getLayoutY()>90){
	    	circle.setLayoutY(90);
	    }
	    ++numOfChipsDropping;
	    circle.setChipReleased(true);
	    saq.setDisable(true);
	    circle.getParent().setCursor(Cursor.DEFAULT);
	    mouseLocation.value = null ;
	    Platform.runLater(new Runnable(){
	    	@Override public void run(){
	    		circle.release();
	    	}
	    });
	 
	    Task<Void> task = new Task<Void>(){
	    	@Override
			protected Void call(){
	    		for(int j=0; j<99999;++j){
	    			if(stage.isShowing()&&circle.isReachedBottom()==false){
	    				for(int i=0; i<86;++i){
	    		    		intersect = Shape.intersect(circle, (Shape) root.getChildren().get(i));
	    		    		if (intersect.getBoundsInLocal().getWidth() != -1) {
	    		    			((Pin)root.getChildren().get(i+86)).setStroke(colors[ran.nextInt(6)]);
	    		    			double sine = circle.getLayoutX()+circle.getCenterX()-root.getChildren().get(i).getLayoutX();
	    		    			if(Math.abs(sine)>circle.getRadius()+6){
	    		    				if(sine<0)
	    		    					sine=-circle.getRadius()-6;
	    		    				else
	    		    					sine=circle.getRadius()+6;
	    		    			}
	    		    			circle.collide(Math.asin(sine/(circle.getRadius()+6)));
	    		    			j=0;
	    		    			try {//prevents collision with same pin over and over again
									Thread.sleep(250);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
	    		    			((Pin)root.getChildren().get(i+86)).setStroke(Color.BLACK);
	    		    			}
	    		    	}
	    			}
	    			else{
	    				break;
	    			}
	    			if(j==99998){
	    				j=0;
	    			}
	    		}
	    		return  null;
	    	}
	    };
	    Task<Void> task2 = new Task<Void>(){
	    	@Override protected Void call(){
	    		Shape intersect1;
	    		Shape intersect2;
		    		for(int j=0; j<999;++j){
		    			intersect1 = Shape.intersect(circle, bottomPad);
		    			if(!stage.isShowing()||intersect1.getBoundsInLocal().getWidth()!=-1||circle.getCenterY()+circle.getLayoutY()>700){
		    				--chipsToHitBottom;
	    					--numOfChipsDropping;
		    				circle.setReachedBottom(true);
		    				if(((Text) slots.get((int)(circle.getLayoutX()+circle.getCenterX()-19)/60).getChildren().get(1)).getText().equals(rb.getString("BUST"))){
		    					balance.setText("0");
		    					if(!gameover){
		    						Platform.runLater(new Runnable(){
		    							@Override public void run(){
		    								gameover();
		    							}
		    						});
		    					}
		    					task.cancel();
		    					this.cancel();
		    					break;
		    				}
		    				else{
		    					int win = Integer.parseInt(((Text) slots.get((int)(circle.getLayoutX()+circle.getCenterX()-19)/60).getChildren().get(1)).getText())*circle.getMultiplier();
		    					if(((Rectangle)slots.get((int)(circle.getLayoutX()+circle.getCenterX()-19)/60).getChildren().get(0)).getFill().equals(Color.GREEN)){
		    						Media m = new Media("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/music/Jackpot.mp3");
		    				        MediaPlayer player = new MediaPlayer(m);
		    				        player.play();
		    					}
		    					winnings.setText(rb.getString("winnings")+":\n"+rb.getString("$")+(Integer.parseInt(winnings.getText().substring(winnings.getText().indexOf(rb.getString("$"))+1))+win));
		    					profit.setText(rb.getString("profit")+":\n"+rb.getString("$")+(Integer.parseInt(profit.getText().substring(profit.getText().indexOf(rb.getString("$"))+1))+win-circle.getCost()));
		    					balance.setText(Integer.parseInt(balance.getText())+win+"");
		    					if(!gameover&&Integer.parseInt(balance.getText())<Chip.getLowestCost()&&numOfChipsDropping==0){
		    						Platform.runLater(new Runnable(){
		    							@Override public void run(){
		    								gameover();
		    							}
		    						});
		    						task.cancel();
			    					this.cancel();
			    					break;
		    					}
		    					else if(Integer.parseInt(balance.getText())>MAX_BALANCE){
		    						balance.setText(MAX_BALANCE+"");
		    					}
		    					else{
		    						if(win>0){
		    							lScore+=win;
		    							Media m = new Media("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/music/Win"+new Random().nextInt(3)+".mp3");
		    				        	MediaPlayer player = new MediaPlayer(m);
		    				        	player.play();
		    						}
		    					}
		    				}
		    					if(chipsToHitBottom==0){
		    						Platform.runLater(new Runnable(){
		    							@Override public void run(){
		    								levelComplete();
		    							}
		    						});
		    					}
		    					if(numOfChipsDropping==0){
		    						saq.setDisable(false);
		    					}
		    					task.cancel();
		    					this.cancel();
		    					break;
		    			}
		    			if(circle.isReachedBottom()==false){
		    				for(int i=0;i<10;++i){
		    	    			intersect2 = Shape.intersect(circle, dividers.get(i));
		    	    			if(intersect2.getBoundsInLocal().getWidth()!=-1){ 
		    	    				if(circle.getCenterY()+circle.getLayoutY()+circle.getRadius()/2<=dividers.get(i).getY()){
		    	    					circle.collideFlatH();
		    	    				}
		    	    				else{
		    	    					circle.collideFlatV();
		    	    				}
		    	    				try {
			    						//prevents infinite collision with walls
			    						j=0;
										Thread.sleep(200);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
		    	    			}
		    				}
		    				if(circle.getLayoutX()+circle.getCenterX()-circle.getRadius()<=5){
		    					circle.setCenterX(5-circle.getLayoutX()+circle.getRadius());
		    					circle.collideFlatV();
		    					try {
		    						//prevents infinite collision with walls
		    						j=0;
									Thread.sleep(200);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		    				}
		    				else if(circle.getLayoutX()+circle.getCenterX()>=550){
		    					circle.setCenterX(550-circle.getLayoutX());
		    					circle.collideFlatV();
		    					try {
		    						//prevents infinite collision with walls
		    						j=0;
									Thread.sleep(200);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		    				}
		    				if(j==998){
		    					j=0;
		    				}
		    			}
		    		}
	    		return null;
	    	}
	    };
	    new Thread(task).start();
	    new Thread(task2).start();
		});
		}
	 static class Wrapper<T> { T value ; }
}
