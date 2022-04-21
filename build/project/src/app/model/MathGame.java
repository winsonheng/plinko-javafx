package app.model;

import java.util.Random;

import app.Main;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class MathGame extends MiniGame{

	private Main main;
	private AnchorPane root=new AnchorPane();
	private Label openingText = new Label("Calculate as fast as you can!");
	private GridPane pane = new GridPane();
	private Button start = new Button("Start");
	private TextField answer = new TextField();
	private Label n1 = new Label();
	private Label n2 = new Label();
	private Label operation = new Label();
	private Label equals = new Label();
	private Label xequals = new Label();
	private TextField xanswer = new TextField();
	private String[] operations = {"+","-","X","÷"}; 
	private Label chipCount = new Label();
	private Random r = new Random();
	
	public MathGame(){
		start.setPrefHeight(20);
		start.setStyle("-fx-font: 24 arial;");
		pane.getColumnConstraints().add(new ColumnConstraints(100));
		pane.getColumnConstraints().add(new ColumnConstraints(50));
		pane.getColumnConstraints().add(new ColumnConstraints(100));
		pane.getColumnConstraints().add(new ColumnConstraints(30));
		pane.getColumnConstraints().add(new ColumnConstraints(70));
		pane.getRowConstraints().add(new RowConstraints(100));
		pane.getRowConstraints().add(new RowConstraints(100));
		pane.getRowConstraints().add(new RowConstraints(100));
		pane.add(n1, 0, 0);
		pane.add(operation, 1,0);
		pane.add(n2, 2,0);
		pane.add(equals, 3, 0);
		pane.add(answer, 4, 0);
		pane.add(xequals, 0, 1);
		pane.add(xanswer, 1,1);
		pane.add(chipCount, 0, 2);
		
		reset();
	}
	public void reset(){
		chips=0;
		root.getChildren().add(openingText);
		root.getChildren().add(start);
		openingText.setLayoutX(100);
		openingText.setLayoutY(75);
		start.setLayoutX(150);
		start.setLayoutY(150);
		
	}
	public void start(){
		
		
	}
}
