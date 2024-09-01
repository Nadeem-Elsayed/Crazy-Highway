package elsayed;

public interface Tick {
	/*
	 * you may ask yourself,
	 * what is a tick?
	 * it is a small bug that loves dogs
	 */

	public void tick (double elapsedTime);
	/*required by game timer
	*since the game timer uses a tick, it requires a handle method with the same parameters
	*the game timer does not care what the method is, it just needs a method with the same parameters
	*that is why we can make a game timer by using a lambda expression and directing it to a...
	*method that has those parameters, which is why we can have the game timer run the update...
	*method in the Crazy HighWay class
	*/
	
}
