package circleHough;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Utils {
	private Utils() {}

	/*
	 * Converts a given image into a gray-scale image
	 */
	public static BufferedImage toGray(BufferedImage img) {
		BufferedImage grayImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D graphics = grayImage.createGraphics();
		try {
			graphics.setComposite(AlphaComposite.Src);
			graphics.drawImage(img, 0, 0, null);
		} finally {
			graphics.dispose();
		}
		return grayImage;
	}

	/*
	 * Writes on disk the given image
	 */
	public static void writeImage(BufferedImage image, String path) throws IOException {
		File output = new File(path);
		ImageIO.write(image, "png", output);
	}

	/*
	 * Rescale pixel-by-pixel the data of a factor equal to scaleFact
	 */
	public static BufferedImage rescaleData(float scaleFact, BufferedImage image) {
		RescaleOp rOp = new RescaleOp(scaleFact, 0, null);
		image = rOp.filter(image, image);
		return image;
	}
}
