package circleHough.houghTransform;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class CircleDetector {

	private int threshold;
	private int minRadius;
	private int maxRadius;

	public CircleDetector(int threshold, int minRadius, int maxRadius) {
		this.threshold = threshold;
		this.minRadius = minRadius;
		this.maxRadius = maxRadius;
	}

	/*
	 * Detect circles on a given image starting from sobel results
	 */
	public List<Circle> circleDetection(BufferedImage image, double[][] sobel) throws Exception {
		List<Circle> circles = new ArrayList<>();
		int[][][] accumulator = new int[image.getWidth()][image.getHeight()][maxRadius]; //3D integer space array to contain 'hits' circles

		int max = 0;
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				/*
				 * checks if the pixel is above threshold, checks if its coordinates are valid
				 * and increases accumulator
				 */
				if (sobel[x][y] > threshold) {
					for (int rad = minRadius; rad < maxRadius; rad++) {
						for (int t = 0; t <= 360; t++) {
							Integer a = (int) Math.floor(x - rad * Math.cos(t * Math.PI / 180));
							Integer b = (int) Math.floor(y - rad * Math.sin(t * Math.PI / 180));

							// checks if a or b are outside the bounds of the image, then ignore them
							if (!((0 > a || a > image.getWidth() - 1) || (0 > b || b > image.getHeight() - 1))) {
								accumulator[a][b][rad] += 1;
								if (accumulator[a][b][rad] > max) {
									max = accumulator[a][b][rad];
								}
							}
						}
					}
				}
			}
		}

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				for (int rad = minRadius; rad < maxRadius; rad++) {
					circles.add(new Circle(x, y, rad, accumulator[x][y][rad]));
				}
			}
		}
		return circles;
	}

	public int getThreshold() {
		return threshold;
	}

	public int getMinRadius() {
		return minRadius;
	}

	public int getMaxRadius() {
		return maxRadius;
	}
}
