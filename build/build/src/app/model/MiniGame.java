package app.model;

public abstract class MiniGame {

	public int chips;
	public abstract void start();
	public abstract void reset();
	public abstract void end();
	public int getChips(){
		return chips;
	}
}
