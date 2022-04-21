package app.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Chip extends Circle implements Collidable {

	private String[] chipNames = {"Plastic","Wooden","Copper","Marble","Feather","Stone","Steerer","Giant"};
	private int[] multiplier = {1,2,3,4,4,6,5,8};
	private int[] cost = {10,40,175,750,650,2250,2400,5000};
	private static int[] unlockLevel = {1,1,1,1,5,5,10,12};
	private double[] radius = {16,16,16,16,16,16,16,22};
	private double[] acc = {0.2,0.2,0.2,0.2,0.1,0.3,0.2,0.15};
	private static boolean[] unlocked = {true,true,true,true,false,false,false,false};
	private String[] fill = {"Plastic.jpg","Wooden.jpg", "Copper.jpg","Marble.jpg","Feather.jpg","Stone.jpg","Steerer.jpg","Plastic.jpg"};
	
	private int type;
	private boolean chipReleased;
	private boolean chipDragged;
	private boolean reachedBottom;
	private Timeline tl;
	/**
	 * Physical quantities
	 */
	private double vX;
	private double vY;
	private double aY;
	private double direction;
	
	public Chip(int type){
		chipDragged=false;
		chipReleased=false;
		reachedBottom=false;
		this.type=type;
		setRadius(radius[type]);
		setFill(new ImagePattern(new Image("chips/"+fill[type])));
		vX=0;
		vY=0;
		aY=0;
	}
	public boolean isChipReleased() {
		return chipReleased;
	}
	public void setChipReleased(boolean chipReleased) {
		this.chipReleased = chipReleased;
	}
	
	public void release(){
		chipReleased=true;
		aY=acc[type];
		direction=Math.PI;
		tl = new Timeline(new KeyFrame(Duration.millis(30), e -> move()));
		tl.setCycleCount(Timeline.INDEFINITE);
		tl.play();
		if(type==6){
			this.requestFocus();
			this.setOnKeyPressed(e->{
				if(Math.abs(vX)<8){
					if (e.getCode().equals(KeyCode.LEFT)){
						vX-=0.2;
					}
					else if(e.getCode().equals(KeyCode.RIGHT)){
						vX+=0.2;
					}
				}
		    });
		}
	}
	private void move(){
		vY+=aY;
		setCenterY(this.getCenterY()+vY);
		setCenterX(this.getCenterX()+vX);
	}
	public int getCost(){
		return cost[type];
	}
	public int getMultiplier(){
		return multiplier[type];
	}
	public String getTypeName(){
		return chipNames[type];
	}
	public int getType(){
		return type;
	}
	public void setType(int type){
		this.type = type;
	}
	public static void unlockByLevel(int level){
		for(int i=0;i<unlockLevel.length;++i){
			if(unlockLevel[i]==level){
				unlock(i);
			}
		}
	}
	//to unlock a new chip
	public static void unlock(int type){
		unlocked[type]=true;
	}
	public boolean isUnlocked(){
		return unlocked[type];
	}
	@Override
	public void collide(Double angle){
		tl.pause();
		if(vY>0){
			direction = Math.PI+Math.atan(-vX/vY);
		}
		else{
			direction = -Math.atan(vX/vY);
		}
		//algorithm to calculate new direction
		//not 100% accurate because i suck at trigonometry
		//but close enough
		direction = 2*angle+direction-Math.PI;
		double uX = vX;
		vX = Math.sqrt(vX*vX+vY*vY)*Math.sin(direction);
		//prevents ball from bouncing up too much
		vY = -0.7*Math.sqrt(uX*uX+vY*vY)*Math.cos(direction);
		if(Math.abs(vX)<0.3&&Math.abs(angle)<0.1){
			if(vX<0)
				vX-=0.05;//in case it lands in middle of pin
			else
				vX+=0.05;
		}
		tl.play();
	}
	@Override
	public void collideFlatH() {
		if(vX<0.5&&vX>=0){
			vX+=0.3;//prevents ball from bouncing in same spot too long
		}
		if(vX>-0.5&&vX<0){
			vX-=0.3;
		}
		vY*=-0.7;
		//0.7 to prevent bouncing up too high, simulate loss of energy 
		
	}
	@Override
	public void collideFlatV() {
		vX*=-0.7;
		//loss of energy
	}
	public double getVelocityX(){
		return vX;
	}
	public double getVelocityY(){
		return vY;
	}
	public boolean isReachedBottom() {
		return reachedBottom;
	}
	public void setReachedBottom(boolean reachedBottom) {
		this.reachedBottom = reachedBottom;
		tl.stop();
		this.setOpacity(0.5);
	}
	public boolean isChipDragged() {
		return chipDragged;
	}
	public void setChipDragged(boolean chipDragged) {
		this.chipDragged = chipDragged;
	}
}
