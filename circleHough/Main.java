package circleHough;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import circleHough.filters.GaussianBlur;
import circleHough.filters.Sobel;
import circleHough.houghTransform.Circle;
import circleHough.houghTransform.CircleDetector;

public class Main {

	public static void main(String[] args) throws Exception {

		/* Default Variables/Arguments */
		final String path = "./results/";
		int sobelThreshold = 150;
		int minRadius = 10;
		int maxRadius = 100;
		int circleThreshold = 1;

		/* Checks argument and result folder */
		if (args.length < 1 || args.length > 5) {
			System.out.println("Usage: Main imagePath [sobelThreshold] [minRadius] [maxRadius] [circlesThreshold]");
			System.exit(1);
		}

		if (args.length > 2) {

			try {
				switch (args.length) {
				case 5:
					circleThreshold = Integer.parseInt(args[4]);
				case 4:
					maxRadius = Integer.parseInt(args[3]);
				case 3:
					minRadius = Integer.parseInt(args[2]);
				case 2:
					sobelThreshold = Integer.parseInt(args[1]);
				}
			} catch (NumberFormatException e) {
				System.out.println("Specified arguments must be integers");
				System.exit(1);
			}
		}

		if (maxRadius - minRadius <= 0) {
			System.out.println("Minimum and maximum radius are not valid");
			System.exit(1);
		}

		File resFolder = new File(path);
		if (!(resFolder.exists() && resFolder.isDirectory())) {
			try {
				resFolder.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		ArrayList<BufferedImage> images = new ArrayList<>();

		/* Reads the original image */
		File origin = new File(args[0]);

		try {
			images.add(ImageIO.read(origin));
		} catch (IOException e) {
			System.out.println("Image not found");
			System.exit(1);
		}

		/* Converts image into gray-scale */
		BufferedImage gray = Utils.toGray(images.get(0));
		images.add(gray);
		Utils.writeImage(gray, path + "gray.png");

		/* Gaussian Blur */
		BufferedImage blurred = GaussianBlur.blur(gray);
		for (int i = 0; i < 2; i++) {
			blurred = GaussianBlur.blur(blurred);
		}
		images.add(blurred);
		Utils.writeImage(blurred, path + "blur.png");

		/* Sobel edge detection */
		Sobel sob = new Sobel(blurred);

		BufferedImage imgSobel = sob.getSobelImage();
		BufferedImage imgSobelThreshold = sob.thresholdImg(sobelThreshold);

		images.add(imgSobel);
		images.add(imgSobelThreshold);
		Utils.writeImage(imgSobel, path + "sobel.png");
		Utils.writeImage(imgSobelThreshold, path + "sobelThreshold.png");

		/* Circle detection */
		BufferedImage circlesImg = new BufferedImage(gray.getWidth(), gray.getHeight(), BufferedImage.TYPE_INT_ARGB);

		CircleDetector circleDetector = new CircleDetector(sobelThreshold, minRadius, maxRadius);
		List<Circle> detectedCircles = circleDetector.circleDetection(imgSobel, sob.getSobelValues());
		Collections.sort(detectedCircles, Collections.reverseOrder());

		circlesImg.createGraphics().drawImage(images.get(0), 0, 0, null);
		Graphics2D graph = circlesImg.createGraphics();
		graph.setColor(Color.RED);

		if(circleThreshold > detectedCircles.size()) {
			circleThreshold = detectedCircles.size();
		}

		for (int i = 0; i < circleThreshold; i++) {
			Circle circleToDraw = detectedCircles.get(i);
			double x = circleToDraw.getX() - circleToDraw.getR() * Math.cos(0 * Math.PI / 180);
			double y = circleToDraw.getY() - circleToDraw.getR() * Math.sin(90 * Math.PI / 180);
			graph.drawOval((int) x, (int) y, 2 * circleToDraw.getR(), 2 * circleToDraw.getR());
		}

		images.add(circlesImg);
		Utils.writeImage(circlesImg, path + "detectedCircles.png");

		View gui = new View(images, path);
		gui.setVisible(true);
	}
}
