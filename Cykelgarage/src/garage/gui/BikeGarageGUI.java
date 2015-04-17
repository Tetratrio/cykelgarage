package garage.gui;

import garage.controller.Controller;
import garage.gui.tabs.BasicTabPanel;

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

@SuppressWarnings("serial")
public class BikeGarageGUI extends JFrame implements ChangeListener {

	private ArrayList<BasicTabPanel> tabs;
	
	public BikeGarageGUI(Controller controller) {
		
	}
	
	private void notifyTabs() {
		for (BasicTabPanel tab : tabs) {
			tab.notifyTabChange();
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent ce) {
		notifyTabs();
	}
}