package circleHough;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class Utils {

	/*
	 * Displays an image given its path
	 */
	public static void displayImage(String imagePath, String title) throws IOException {
		File file = new File(imagePath);
		BufferedImage image = ImageIO.read(file);

		JFrame f = new JFrame(title);
		ImageIcon pic = new ImageIcon(imagePath);
		JLabel label;
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		panel.setBackground(Color.WHITE);
		label = new JLabel("", pic, SwingConstants.CENTER);
		panel.add(label);
		f.setVisible(true);
		f.setResizable(false);
		f.add(panel);
		f.pack();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	/*
	 * Changes the brightness of an image by the given factor
	 */
	public static BufferedImage editBrightness(float brightenFactor, BufferedImage image) {
		RescaleOp rOp = new RescaleOp(brightenFactor, 0, null);
		image = rOp.filter(image, image);
		return image;
	}

	/*
	 * Maps the value between start1 and end1 to a value between start2 and end2
	 */
	public static double map(double value, double start1, double end1, double start2, double end2) {
		double ratio = (end2 - start2) / (end1 - start1);
		return ratio * (value - start1) + start2;
	}

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

	private Utils() {
	}
}
