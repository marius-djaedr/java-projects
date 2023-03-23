package com.me.util.utils;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class GuiUtils {
	private static final String BASE_PATH = "src/main/resources/icons";

	/**
	 * 
	 * @param path the path within src/main/resources/icons/
	 * @return
	 */
	public static ImageIcon createImageIcon(final String path, final int height) {

		URL imgURL;
		try {
			imgURL = new File(BASE_PATH, path).toURI().toURL();
		} catch(final MalformedURLException e) {
			throw new IllegalArgumentException("Bad file: " + path, e);
		}
		final ImageIcon initialIcon = new ImageIcon(imgURL);
		final Image newImage = initialIcon.getImage().getScaledInstance(-1, height, Image.SCALE_SMOOTH);
		return new ImageIcon(newImage);
	}

	public static void centerFrameInWindow(final JFrame frame) {
		final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		final int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		final int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}
}
