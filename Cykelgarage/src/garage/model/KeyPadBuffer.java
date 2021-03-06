package garage.model;

import java.util.Arrays;

import garage.hardware.interfaces.*;
import garage.logging.LogAccess;

public class KeyPadBuffer {
	
	private static int WAITING_TIME = 4;
	
	private static int RED_LED_LIGHT_TIME = 1;
	
	private KeyPadBufferListener manager;
	
	private TimerThread timer = null;
	
	PinCodeTerminal pinCodeTerminal;
	
	private int bufferSize;
	private char[] buffer;
	private int size;
	private int expectedInput;
	
	/**
	 * Create a new KeyPadBuffer object which saves input
	 * until the expected amount of input is recieved, it will
	 * pass that input to the manager.
	 * @param bufferSize Original size of the buffer.
	 * @param pinCodeTerminal PIN-codeterminal used for lighting LEDs.
	 * @param manager Manager that recieves the buffer when its full.
	 */
	public KeyPadBuffer(int bufferSize, PinCodeTerminal pinCodeTerminal, KeyPadBufferListener manager) {
		this.manager = manager;
		this.pinCodeTerminal = pinCodeTerminal;
		this.bufferSize = bufferSize;
		buffer = new char[bufferSize];
		expectedInput = bufferSize;
		size = 0;
	}
	
	/**
	 * Set the expected number of chars for the buffer.
	 * @param n Number of chars to expect. 0 <= m < BUFFER_SIZE
	 */
	public void setExpectedInput(int n) {
		if (n < 0) {
			throw new IllegalArgumentException(
					"Cannot set expected input to a negative value");
		}
		if (n > bufferSize) {
			throw new IllegalArgumentException(
					"Cannot set expect input to a number larger than BUFFER_SIZE");
		}
		expectedInput = n;
		newTimer();
	}
	
	/**
	 * Recieve a new char and add it to the buffer.
	 * @param c Character to recieve.
	 */
	public void recieveChar(char c) {
		buffer[size++] = c;
		if (size >= expectedInput) {
			char[] buf = Arrays.copyOf(buffer, size);
			String bufVal = String.valueOf(buf);
			newBuffer();
			timer = null;
			manager.recieveBuffer(bufVal);
		} else {
			newTimer();
		}
	}
	
	private void newTimer() {
		timer = new TimerThread(WAITING_TIME, this);
	}
	
	private void newBuffer() {
		buffer = new char[bufferSize];
		expectedInput = bufferSize;
		size = 0;
	}
	
	
	private void timerFinnished(TimerThread caller) {
		if (timer == null || ! timer.equals(caller)) {
			return;
		}
		newBuffer();
		pinCodeTerminal.lightLED(PinCodeTerminal.RED_LED, RED_LED_LIGHT_TIME);
	}
	
	private class TimerThread extends Thread implements Runnable {
		private KeyPadBuffer manager;
		private long time;
		private TimerThread(int seconds, KeyPadBuffer manager) {
			this.manager = manager;
			time = seconds * 1000l;
			this.start();
		}
		
		/**
		 * This method starts a timer by putting the new thread
		 * to sleep for set amount of time. When the set time
		 * has passed it notifies its manager.
		 */
		public void run() {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				LogAccess.error().log("TimerThread: sleep interrupted.");
				LogAccess.error().log("TimerThread: " + e.getMessage());
			}
			manager.timerFinnished(this);
		}
	}
}
