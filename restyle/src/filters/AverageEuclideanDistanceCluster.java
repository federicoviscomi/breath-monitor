package filters;

public class AverageEuclideanDistanceCluster implements DistanceCluster {

	double averageClusterValueX;
	double averageClusterValueY;

	public AverageEuclideanDistanceCluster(final double x, final double y) {
		averageClusterValueX = x;
		averageClusterValueY = y;
	}

	@Override
	public void add(final double x, final double y) {
		averageClusterValueX = (averageClusterValueX + x) / 2;
		averageClusterValueY = (averageClusterValueY + y) / 2;
	}

	@Override
	public double getDistance(final double x, final double y) {
		return Math.sqrt(Math.pow(x - averageClusterValueX, 2)
				+ Math.pow(y - averageClusterValueY, 2));
	}

}
