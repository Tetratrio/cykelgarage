package garage.gui;

import garage.controller.BicycleGarageManager;
import garage.gui.tabs.*;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

@SuppressWarnings("serial")
public class BicycleGarageGUI extends JFrame implements ChangeListener {
	private final static String MANIPULATION_TAB_TITLE = "Kontroll";
	private final static String MANIPULATION_TAB_DESCRIPTION = "Kontroll";
	private final static String VIEW_TAB_TITLE = "Visa";
	private final static String VIEW_TAB_DESCRIPTION = "Visa";
	private final static String SEARCH_TAB_TITLE = "Sök";
	private final static String SEARCH_TAB_DESCRIPTION = "Sök";
	
	private ArrayList<BasicTabPanel> tabs;
	
	/**
	 * Create a new GUI object for the bicyclegarage.
	 * @param bicycleGarageManager BicycleGarageManager object to be attached
	 * to this gui. The GUI calls this object after user input.
	 */
	public BicycleGarageGUI(BicycleGarageManager bicycleGarageManager) {
		tabs = new ArrayList<BasicTabPanel>();
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addChangeListener(this);
		BasicTabPanel basicPanel;
		
		basicPanel = new ManipulationTab(bicycleGarageManager);
		tabbedPane.addTab(MANIPULATION_TAB_TITLE, null, basicPanel, MANIPULATION_TAB_DESCRIPTION);
		tabs.add(basicPanel);
		
		basicPanel = new ViewTab(bicycleGarageManager);
		tabbedPane.addTab(VIEW_TAB_TITLE, null, basicPanel, VIEW_TAB_DESCRIPTION);
		tabs.add(basicPanel);
		
		basicPanel = new SearchTab(bicycleGarageManager);
		tabbedPane.addTab(SEARCH_TAB_TITLE, null, basicPanel, SEARCH_TAB_DESCRIPTION);
		tabs.add(basicPanel);
		
        tabbedPane.setSelectedIndex(0);
        tabbedPane.addChangeListener(this);

        this.setLayout(new BorderLayout());
        this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 450);
        this.setVisible(true);
	}
	
	private void notifyTabs() {
		for (BasicTabPanel tab : tabs) {
			tab.notifyTabChange();
		}
	}
	
	/**
	 * Called when a new tab is chosen in the GUI.
	 * Notifies all tabs of the tab change.
	 */
	@Override
	public void stateChanged(ChangeEvent ce) {
		notifyTabs();
	}
	
	/**
	 * Create a gui window asking the user for the maximum capacity
	 * of the garage.
	 * @return Value entered by user. Default value if operation failed.
	 */
	public static int getMaxBikes() {
		String respons = "";
		respons = JOptionPane.showInputDialog("Ange max antal cyklar för garaget");
		try {
			int val = Integer.parseInt(respons);
			if (val > 0) return val;
			return BicycleGarageManager.DEFAULT_MAX_BIKES;
		} catch (NumberFormatException e) {
			return BicycleGarageManager.DEFAULT_MAX_BIKES;
		}
	}
	
	public static void showMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}
}
