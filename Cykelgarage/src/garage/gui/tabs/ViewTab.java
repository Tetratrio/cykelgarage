package garage.gui.tabs;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import garage.controller.BicycleGarageManager;

@SuppressWarnings("serial")
public class ViewTab extends BasicTabPanel implements ListSelectionListener {
	private final static String LEFT_LIST_TITLE = "Användarnamn";
	private final static String RIGHT_LIST_TITLE = "Cyklar";
	private DefaultListModel<String> userList;
	private JList<String> graphicalUserList;
	
	private DefaultListModel<String> bikeList;

	public ViewTab(BicycleGarageManager bicycleGarageManager) {
		super(bicycleGarageManager);
		this.setLayout(new GridLayout(1, 2));
		
		JPanel leftListPanel = new JPanel();
		leftListPanel.setLayout(new BorderLayout());
		userList = new DefaultListModel<String>();
		graphicalUserList = new JList<String>(userList);
		graphicalUserList.addListSelectionListener(this);
		leftListPanel.add(graphicalUserList, BorderLayout.CENTER);
		leftListPanel.setBorder(new TitledBorder(LEFT_LIST_TITLE));
		
		JPanel rightListPanel = new JPanel();
		rightListPanel.setLayout(new BorderLayout());
		bikeList = new DefaultListModel<String>();
		JList<String> graphicalBikeList = new JList<String>(bikeList);
		graphicalBikeList.setSelectionModel(new DisableItemSelectionModel());
		rightListPanel.add(graphicalBikeList, BorderLayout.CENTER);
		rightListPanel.setBorder(new TitledBorder(RIGHT_LIST_TITLE));

		
		this.add(leftListPanel);
		this.add(rightListPanel);
	}
	
	@Override
	public void notifyTabChange() {
		userList.removeAllElements();
		List<String> usernameList = bicycleGarageManager.getUsers();
		for (String username : usernameList) {
			userList.addElement(username);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		bikeList.removeAllElements();
		int selectedIndex = graphicalUserList.getSelectedIndex();
		if (selectedIndex != -1) {
			List<String> bikeIDList = bicycleGarageManager.getBikes(userList.get(selectedIndex));
			for (String bikeID : bikeIDList) {
				bikeList.addElement(bikeID);
			}
		}
	}
}
