package filters;

public class MinxMaxyDistanceCluster implements DistanceCluster {
	private double minx;
	private double maxy;

	public MinxMaxyDistanceCluster(final double x, final double y) {
		minx = x;
		maxy = y;
	}

	@Override
	public void add(final double x, final double y) {
		if (minx > x) {
			minx = x;
		}
		if (maxy < y) {
			maxy = y;
		}
	}

	@Override
	public double getDistance(final double x, final double y) {
		return Math.abs(x - minx) + Math.abs(y - maxy);
	}

}
