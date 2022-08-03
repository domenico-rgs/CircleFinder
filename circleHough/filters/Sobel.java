package circleHough.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Sobel {

	private BufferedImage gray;
	private int[][] sobelX;
	private int[][] sobelY;
	private double[][] sobel;

	public Sobel(BufferedImage gray) {
		this.gray = gray;
		sobelX = new int[gray.getWidth()][gray.getHeight()];
		sobelY = new int[gray.getWidth()][gray.getHeight()];
		sobel = new double[gray.getWidth()][gray.getHeight()];

		computeSobel();
	}

	/*
	 * Generates the horizontal Sobel edge
	 */
	private void calcSobelX() {
		int[][] kernel = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };

		for (int i = 0; i < gray.getWidth(); i++) {
			for (int j = 0; j < gray.getHeight(); j++) {
				sobelX[i][j] = performConvolution(i, j, kernel);
			}
		}
	}

	/*
	 * Generates the vertical Sobel edge
	 */
	private void calcSobelY() {
		int[][] kernel = { { 1, 2, 1 }, { 0, 0, 0 }, { -1, -2, -1 } };

		for (int i = 0; i < gray.getWidth(); i++) {
			for (int j = 0; j < gray.getHeight(); j++) {
				sobelY[i][j] = performConvolution(i, j, kernel);
			}
		}
	}

	/*
	 * Combine the horizontal Sobel and vertical Sobel obtaining the module
	 */
	private void computeSobel() {
		calcSobelX();
		calcSobelY();
		for (int i = 0; i < gray.getWidth(); i++) {
			for (int j = 0; j < gray.getHeight(); j++) {
				sobel[i][j] = Math.sqrt(Math.pow(sobelX[i][j], 2) + Math.pow(sobelY[i][j], 2));
			}
		}
	}

	/*
	 * Generates Sobel image
	 */
	public BufferedImage getSobelImage() {
		BufferedImage imgSobel = new BufferedImage(gray.getWidth(), gray.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		// Max value of sobel array
		double maxSobel = 0;
		for (int i = 0; i < gray.getWidth(); i++) {
			for (int j = 0; j < gray.getHeight(); j++) {
				if (sobel[i][j] > maxSobel) {
					maxSobel = sobel[i][j];
				}
			}
		}

		for (int i = 0; i < gray.getWidth(); i++) {
			for (int j = 0; j < gray.getHeight(); j++) {
				int rgb = new Color((int) map(sobel[i][j], 0, maxSobel, 0, 255),
						(int) map(sobel[i][j], 0, maxSobel, 0, 255),
						(int) map(sobel[i][j], 0, maxSobel, 0, 255), 255).getRGB();
				imgSobel.setRGB(i, j, rgb);
			}
		}

		//imgSobel = Utils.rescaleData(20.0f, imgSobel); // to see better the details (similar to dilation), no changes in the following steps of HT
		return imgSobel;
	}

	/*
	 * Generates the image with only pixels above the threshold
	 */
	public BufferedImage thresholdImg(int threshold) {
		BufferedImage thresholdImg = new BufferedImage(gray.getWidth(), gray.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Color c = new Color(255, 0, 0);

		for (int i = 0; i < gray.getWidth(); i++) {
			for (int j = 0; j < gray.getHeight(); j++) {
				if (sobel[i][j] > threshold) {
					thresholdImg.setRGB(i, j, c.getRGB());
				}
			}
		}
		return thresholdImg;
	}

	/*
	 * Checks all the edge cases to ensure no errors and calculates the convolution for any pixel/kernel
	 */
	private int performConvolution(int x, int y, int[][] kernel) {
		int res = 0;

		if (x == 0 && y == 0) {
			for (int i = 0; i <= 1; i++) {
				for (int j = 0; j <= 1; j++) {
					res += new Color(gray.getRGB(x + i, y + j)).getRed() * kernel[j + 1][i + 1];
				}
			}
		} else if (x == 0 && y == gray.getHeight() - 1) {
			for (int i = 0; i <= 1; i++) {
				for (int j = -1; j <= 0; j++) {
					res += new Color(gray.getRGB(x + i, y + j)).getRed() * kernel[j + 1][i + 1];
				}
			}
		} else if (x == 0 && y != 0) {
			for (int i = 0; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					res += new Color(gray.getRGB(x + i, y + j)).getRed() * kernel[j + 1][i + 1];
				}
			}
		} else if (x == gray.getWidth() - 1 && y == 0) {
			for (int i = -1; i <= 0; i++) {
				for (int j = 0; j <= 1; j++) {
					res += new Color(gray.getRGB(x + i, y + j)).getRed() * kernel[j + 1][i + 1];
				}
			}
		} else if (x != 0 && y == 0) {
			for (int i = -1; i <= 1; i++) {
				for (int j = 0; j <= 1; j++) {
					res += new Color(gray.getRGB(x + i, y + j)).getRed() * kernel[j + 1][i + 1];
				}
			}
		} else if (x == gray.getWidth() - 1 && y == gray.getHeight() - 1) {
			for (int i = -1; i <= 0; i++) {
				for (int j = -1; j <= 0; j++) {
					res += new Color(gray.getRGB(x + i, y + j)).getRed() * kernel[j + 1][i + 1];
				}
			}
		} else if (x == gray.getWidth() - 1 && y != gray.getHeight() - 1) {
			for (int i = -1; i <= 0; i++) {
				for (int j = -1; j <= 1; j++) {
					res += new Color(gray.getRGB(x + i, y + j)).getRed() * kernel[j + 1][i + 1];
				}
			}
		} else if (x != gray.getWidth() - 1 && y == gray.getHeight() - 1) {
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 0; j++) {
					res += new Color(gray.getRGB(x + i, y + j)).getRed() * kernel[j + 1][i + 1];
				}
			}
		} else {
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					res += new Color(gray.getRGB(x + i, y + j)).getRed() * kernel[j + 1][i + 1];
				}
			}
		}

		return res;
	}

	/*
	 * Maps the value between start1 and end1 to a value between start2 and end2.
	 * Useful to map pixels to a gray-scale value between 0 and 255 from a value between and the max value of sobel array
	 */
	private static double map(double value, double start1, double end1, double start2, double end2) {
		double ratio = (end2 - start2) / (end1 - start1);
		return ratio * (value - start1) + start2;
	}

	public double[][] getSobelValues() {
		return sobel;
	}

	public int[][] getSobelXValues() {
		return sobelX;
	}

	public int[][] getSobelYValues() {
		return sobelY;
	}
}
