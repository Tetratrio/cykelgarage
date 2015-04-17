package garage.gui.tabs;

import garage.controller.Controller;

import javax.swing.*;

@SuppressWarnings("serial")
public abstract class BasicTabPanel extends JPanel {
	protected Controller controller;
	
	public BasicTabPanel(Controller controller) {
		this.controller = controller;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void notifyTabChange() {
		
	}
	
	protected void displayMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}
}
