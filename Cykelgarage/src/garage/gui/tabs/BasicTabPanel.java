package garage.gui.tabs;

import garage.controller.BicycleGarageManager;

import javax.swing.*;

@SuppressWarnings("serial")
public abstract class BasicTabPanel extends JPanel {
	protected BicycleGarageManager bicycleGarageManager;
	
	public BasicTabPanel(BicycleGarageManager bicycleGarageManager) {
		this.bicycleGarageManager = bicycleGarageManager;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void notifyTabChange() {
		
	}

}
