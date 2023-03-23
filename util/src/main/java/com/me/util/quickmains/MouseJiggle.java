package com.me.util.quickmains;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.Random;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

public class MouseJiggle {
	//	private static final int MIN_X = -1670;
	private static final int MIN_X = 40;
	private static final int MIN_Y = 40;
	private static final int MAX_X = 3590;
	private static final int MAX_Y = 1070;

	private static int x = 0, y = 0;
	private static final Random rand = new Random();

	public static void main(final String[] args) throws InterruptedException, AWTException {
		try {
			GlobalScreen.registerNativeHook();
		} catch(final NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}

		// Construct the example object.
		final GlobalMouseListenerExample example = new GlobalMouseListenerExample();

		// Add the appropriate listeners.
		GlobalScreen.addNativeMouseListener(example);
		GlobalScreen.addNativeMouseMotionListener(example);

		final Robot robot = new Robot();
		while(true) {
			Thread.sleep(100);
			robot.mouseMove(calcNext(x, MIN_X, MAX_X), calcNext(y, MIN_Y, MAX_Y));
		}
	}

	private static int calcNext(final int current, final int min, final int max) {

		final int next = current + rand.nextInt(51) - 25;

		if(next < min) {
			return min;
		}
		if(next > max) {
			return max;
		}
		return next;
	}

	private static class GlobalMouseListenerExample implements NativeMouseInputListener {
		@Override
		public void nativeMouseClicked(final NativeMouseEvent e) {
			System.out.println("Mouse Clicked: " + e.getClickCount() + " on button " + e.getButton());

		}

		@Override
		public void nativeMousePressed(final NativeMouseEvent e) {
			System.out.println("Mouse Pressed: " + e.getButton());

			if(2 == e.getButton()) {
				System.exit(0);
			}
		}

		@Override
		public void nativeMouseReleased(final NativeMouseEvent e) {
			System.out.println("Mouse Released: " + e.getButton());
		}

		@Override
		public void nativeMouseMoved(final NativeMouseEvent e) {
			System.out.println("Mouse Moved: " + e.getX() + ", " + e.getY());
			x = e.getX();
			y = e.getY();
		}

		@Override
		public void nativeMouseDragged(final NativeMouseEvent e) {
			System.out.println("Mouse Dragged: " + e.getX() + ", " + e.getY());
		}

	}
}
