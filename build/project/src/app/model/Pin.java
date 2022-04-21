package app.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Pin extends Circle {
	private final int radius=4;
	
	public Pin(){
		setRadius(radius);
		setFill(null);
		setStroke(Color.BLACK);
	}

}
