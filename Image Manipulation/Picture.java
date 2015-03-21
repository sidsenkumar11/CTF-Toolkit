/*************************************************************************
 *  Compilation:  javac Picture.java
 *  Execution:    java Picture imagename
 *
 *  Data type for manipulating individual pixels of an image. The original
 *  image can be read from a file in jpg, gif, or png format, or the
 *  user can create a blank image of a given size. Includes methods for
 *  displaying the image in a window on the screen or saving to a file.
 *
 *  % java Picture mandrill.jpg
 *
 *  Remarks
 *  -------
 *   - pixel (x, y) is column x and row y, where (0, 0) is upper left
 *
 *   - see also GrayPicture.java for a grayscale version
 *
 *************************************************************************/

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * This class provides methods for manipulating individual pixels of an image.
 * The original image can be read from a file in JPEG, GIF, or PNG format, or
 * the user can create a blank image of a given size. This class includes
 * methods for displaying the image in a window on the screen or saving to a
 * file.
 * <p>
 * By default, pixel (x, y) is column x, row y, where (0, 0) is upper left. The
 * method setOriginLowerLeft() change the origin to the lower left.
 * <p>
 * For additional documentation, see <a
 * href="http://introcs.cs.princeton.edu/31datatype">Section 3.1</a> of
 * <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by
 * Robert Sedgewick and Kevin Wayne.
 */
public final class Picture implements ActionListener {
	private BufferedImage image; // the rasterized image
	private JFrame frame; // on-screen view
	private String filename; // name of file
	private boolean isOriginUpperLeft = true; // location of origin
	private final int width, height; // width and height

	/**
	 * Create a blank w-by-h picture, where each pixel is black.
	 */
	public Picture(int w, int h) {
		width = w;
		height = h;
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		// set to TYPE_INT_ARGB to support transparency
		filename = w + "-by-" + h;
	}
	
	/**
	 * Copy constructor.
	 */
	public Picture(Picture pic) {
		width = pic.width();
		height = pic.height();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		filename = pic.filename;
		for (int i = 0; i < width(); i++)
			for (int j = 0; j < height(); j++)
				image.setRGB(i, j, pic.get(i, j).getRGB());
	}

	/**
	 * Create a picture by reading in a .png, .gif, or .jpg from the given
	 * filename or URL name.
	 */
	public Picture(String filename) {
		this.filename = filename;
		try {
			// try to read from file in working directory
			File file = new File(filename);
			if (file.isFile()) {
				image = ImageIO.read(file);
			}

			// now try to read from file in same directory as this .class file
			else {
				URL url = getClass().getResource(filename);
				if (url == null) {
					url = new URL(filename);
				}
				image = ImageIO.read(url);
			}
			width = image.getWidth(null);
			height = image.getHeight(null);
		} catch (IOException e) {
			// e.printStackTrace();
			throw new RuntimeException("Could not open file: " + filename);
		}
	}

	/**
	 * Create a picture by reading in a .png, .gif, or .jpg from a File.
	 */
	public Picture(File file) {
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not open file: " + file);
		}
		if (image == null) {
			throw new RuntimeException("Invalid image file: " + file);
		}
		width = image.getWidth(null);
		height = image.getHeight(null);
		filename = file.getName();
	}

	/**
	 * Return a JLabel containing this Picture, for embedding in a JPanel,
	 * JFrame or other GUI widget.
	 */
	public JLabel getJLabel() {
		if (image == null) {
			return null;
		} // no image available
		ImageIcon icon = new ImageIcon(image);
		return new JLabel(icon);
	}

	/**
	 * Set the origin to be the upper left pixel.
	 */
	public void setOriginUpperLeft() {
		isOriginUpperLeft = true;
	}

	/**
	 * Set the origin to be the lower left pixel.
	 */
	public void setOriginLowerLeft() {
		isOriginUpperLeft = false;
	}

	/**
	 * Display the picture in a window on the screen.
	 */
	public void show() {

		// create the GUI for viewing the image if needed
		if (frame == null) {
			frame = new JFrame();

			JMenuBar menuBar = new JMenuBar();
			JMenu menu = new JMenu("File");
			menuBar.add(menu);
			JMenuItem menuItem1 = new JMenuItem(" Save...   ");
			menuItem1.addActionListener(this);
			menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
					Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			menu.add(menuItem1);
			frame.setJMenuBar(menuBar);

			frame.setContentPane(getJLabel());
			// f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setTitle(filename);
			frame.setResizable(false);
			frame.pack();
			frame.setVisible(true);
		}

		// draw
		frame.repaint();
	}

	/**
	 * Return the height of the picture in pixels.
	 */
	public int height() {
		return height;
	}

	/**
	 * Return the width of the picture in pixels.
	 */
	public int width() {
		return width;
	}

	/**
	 * Return the color of pixel (i, j).
	 */
	public Color get(int i, int j) {
		if (isOriginUpperLeft)
			return new Color(image.getRGB(i, j));
		else
			return new Color(image.getRGB(i, height - j - 1));
	}
	
	/**
	 * Set the color of pixel (i, j) to c.
	 */
	public void set(int i, int j, Color c) {
		if (c == null) {
			throw new RuntimeException("can't set Color to null");
		}
		if (isOriginUpperLeft)
			image.setRGB(i, j, c.getRGB());
		else
			image.setRGB(i, height - j - 1, c.getRGB());
	}

	/**
	 * Is this Picture equal to obj?
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (obj.getClass() != this.getClass())
			return false;
		Picture that = (Picture) obj;
		if (this.width() != that.width())
			return false;
		if (this.height() != that.height())
			return false;
		for (int x = 0; x < width(); x++)
			for (int y = 0; y < height(); y++)
				if (!this.get(x, y).equals(that.get(x, y)))
					return false;
		return true;
	}

	/**
	 * Save the picture to a file in a standard image format. The filetype must
	 * be .png or .jpg.
	 */
	public void save(String name) {
		save(new File(name));
	}

	/**
	 * Save the picture to a file in a standard image format.
	 */
	public void save(File file) {
		this.filename = file.getName();
		if (frame != null) {
			frame.setTitle(filename);
		}
		String suffix = filename.substring(filename.lastIndexOf('.') + 1);
		suffix = suffix.toLowerCase();
		if (suffix.equals("jpg") || suffix.equals("png")) {
			try {
				ImageIO.write(image, suffix, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Error: filename must end in .jpg or .png");
		}
	}
	
	/**
	 * Opens a save dialog box when the user selects "Save As" from the menu.
	 */
	public void actionPerformed(ActionEvent e) {
		FileDialog chooser = new FileDialog(frame,
				"Use a .png or .jpg extension", FileDialog.SAVE);
		chooser.setVisible(true);
	
		if (chooser.getFile() != null) {
			save(chooser.getDirectory() + File.separator + chooser.getFile());
		}
	}

	void randomize() {
		int newRed = 0;
		int newGreen = 0;
		int newBlue = 0;
		for (int i = 0; i < width(); i++) {
			for (int j = 0; j < height(); j++) {
				newRed = (int) (Math.random() * 255);
				newGreen = (int) (Math.random() * 255);
				newBlue = (int) (Math.random() * 255);
				image.setRGB(i, j,
						new Color(newRed, newGreen, newBlue).getRGB());
			}
		}
	}

	void invertPic() {
		int newRed = 0;
		int newGreen = 0;
		int newBlue = 0;

		for (int i = 0; i < width(); i++) {
			for (int j = 0; j < height(); j++) {
				newRed = 255 - get(i, j).getRed();
				newGreen = 255 - get(i, j).getGreen();
				newBlue = 255 - get(i, j).getBlue();
				image.setRGB(i, j,
						new Color(newRed, newGreen, newBlue).getRGB());
			}
		}
	}

	void mirrorPic() {
		/*
		int newRed = 0;
		int newGreen = 0;
		int newBlue = 0;

		for (int height = 0; height <= height(); height++) {
			for (int width = 0; width < width() / 2; width++) {
				newRed = get(width, height).getRed();
				newGreen = get(width, height).getGreen();
				newBlue = get(width, height).getBlue();
				image.setRGB(width() - width - 1, height, new Color(newRed,
						newGreen, newBlue).getRGB());
			}
		}
*/
		for (int i = 0; i < width(); i++) {
			for (int j = 0; j < height(); j++)
				image.setRGB(width() - i - 1, j, get(i, j).getRGB());
		}
	}
	
	void changeNumColors()
	{
		int newRed = 0;
		int newGreen = 0;
		int newBlue = 0;

		for (int i = 0; i < width(); i++) {
			for (int j = 0; j < height(); j++) {
				newRed = get(i,j).getRed()/64 * 64;
				newGreen =get(i,j).getGreen()/64 * 64;
				newBlue = get(i,j).getBlue()/64 * 64;
				image.setRGB(i, j,
						new Color(newRed, newGreen, newBlue).getRGB());
			}
		}
	}

	void grayScale()
	{
		int r = 0;
		int g = 0;
		int b = 0;
		double x = 0;

		for (int i = 0; i < width(); i++) {
			for (int j = 0; j < height(); j++) {
				r = get(i, j).getRed();
				g = get(i,j).getGreen();
				b = get(i,j).getBlue();
				x = .299 * r + .587 * g + .114 * b;

				int y = (int)(Math.round(x));
				Color gray = new Color(y, y, y);
				image.setRGB(i, j,
						gray.getRGB());
			}
		}
	}
	
	void merge(Picture other)
	{
		if (other.width() == width() && other.height() == height())
		{
			int thisR = 0;
			int thisG = 0;
			int thisB = 0;
			int otherR = 0;
			int otherG = 0;
			int otherB = 0;
			int newRed = 0;
			int newGreen = 0;
			int newBlue = 0;
			
			for (int i = 0; i < width(); i++)
				for(int j = 0; j < height(); j++){
					thisR = get(i, j).getRed();
					thisG = get(i,j).getGreen();
					thisB = get(i,j).getBlue();
					otherR = other.get(i, j).getRed();
					otherG = other.get(i,j).getGreen();
					otherB = other.get(i,j).getBlue();
					newRed = (thisR + otherR) / 2;
					newGreen = (thisG + otherG) / 2;
					newBlue = (thisB + otherB) / 2;
					
					image.setRGB(i, j, new Color(newRed, newGreen, newBlue).getRGB());
				}
		}
		else
		{
			System.out.println("Not compatible. Dimensions of both photos must be the same.");
		}
	}

	public void changeColors() {
		int red = -1;
		int green = -1;
		int blue = -1;
		for (int i = 0; i < width(); i++) {
			for (int j = 0; j < height(); j++) {
				red = get(i, j).getRed();
				green = get(i, j).getGreen();
				blue = get(i, j).getBlue();

				if (red == 0) {
					red = 255;
					green = 255;
					blue = 255;
					set(i,j, new Color(red,green,blue));
				} else {
					red = 0;
					green = 0;
					blue = 0;
					set(i, j, new Color(red, green, blue));
				}
			}
		}
	}

	public void revealNonWhiteAndBlackPixels() {
			int red = 0;
			int green = 0;
			int blue = 0;
			
			for (int i = 0; i < width(); i++) {
				for(int j = 0; j < height(); j++){
					red = get(i, j).getRed();
					blue = get(i, j).getBlue();
					green = get(i, j).getGreen();

					if (red != 0 && green != 0 && blue != 0 || red != 255 && green != 255 && blue != 255) {
						image.setRGB(i, j, new Color(255,255,255).getRGB());
					} else {
						image.setRGB(i, j, new Color(0, 0, 0).getRGB());
					}
				}
		}
	}

	public ArrayList<Integer> barCount() {
		ArrayList<Integer> bars = new ArrayList<Integer>();
		int previous = get(50, 0).getRed();
		int count = 1;
		int lowest = 100;
		for (int j = 1; j < height(); j++) {
			if (get(50, j).getRed() == 0) {
				if (previous == 0) {
					count++;
				} else {
					if (count != 0) {
						//bars.add(count);
					}
					if (count < lowest) {
						lowest = count;
					}
					count = 0;
					previous = get(50, j).getRed();
				}
			} else {
				if (previous != 0) {
					count++;
				} else {
					if (count != 0) {
						bars.add(count);
					}
					if (count < lowest) {
						lowest = count;
					}
					count = 0;
					previous = get(50, j).getRed();
				}
			}
		}
		bars.add(count);
		return bars;
	}

	/**
	 * Prints all RGB values of all pixels
	 */
	public void printPixels() {
		for (int i = 0; i < width(); i++) {
			System.out.print("(");
			for (int j = 0; j < height(); j++) {
				System.out.print(get(i,j).getRed() + ", ");
			}
			System.out.println(")");
		}
	}

	/**
	 * Test client. Reads a picture specified by the command-line argument, and
	 * shows it in a window on the screen.
	 */
	public static void main(String[] args) {
		String fileName = "";
		Picture pic = new Picture(fileName);
		
	}
}