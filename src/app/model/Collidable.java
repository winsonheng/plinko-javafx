package app.model;

/**Interface for objects which can interact with
 * other objects in the form of collisions
 * Applies kinematics and physical quantities
 */
public abstract interface Collidable {
	//collision with any object at an angle
	void collide(Double angle);
	//collision with horizontal flat objects
	void collideFlatH();
	//collision with vertical flat objects
	void collideFlatV();
}
