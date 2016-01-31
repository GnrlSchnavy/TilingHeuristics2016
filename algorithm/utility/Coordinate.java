package algorithm.utility;

public class Coordinate {
	private int x, y;
	public Coordinate(int x, int y){
		this.x = x;
		this.y = y;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}

	public boolean equals(Object obj){
		if (obj instanceof Coordinate) {
			return x == ((Coordinate) obj).getX() && y == ((Coordinate) obj).getY();
		}
		return super.equals(obj);
	}

	public String toString() {
		return x + ", " + y;
	}
}
