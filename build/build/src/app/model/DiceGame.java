package app.model;

import app.Main;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class DiceGame extends MiniGame {
	
	private int[] target = new int[3];
	private Main main;
	private Button b1 = new Button("");
	private Button b2;
	private Button b3;
	private AnchorPane root = new AnchorPane();
	private GridPane grid = new GridPane();

	public DiceGame(){
		grid.getColumnConstraints().add(new ColumnConstraints(50));
		reset();
	}
	public void reset(){
		
	}
	public void start(){
		
	}
	public void setMain(Main main){
		this.main=main;
	}
	
}
