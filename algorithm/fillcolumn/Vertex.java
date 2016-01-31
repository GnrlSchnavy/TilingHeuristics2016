package algorithm.fillcolumn;

public class Vertex{
	private double x, y;
	public Vertex(double x, double y){
		this.x = x;
		this.y = y;
	}
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}

	public boolean equals(Object obj){
		if (obj instanceof Vertex) {
			return x == ((Vertex) obj).getX() && y == ((Vertex) obj).getY();
		}
		return super.equals(obj);
	}

	public String toString() {
		return x + ", " + y;
	}
}
