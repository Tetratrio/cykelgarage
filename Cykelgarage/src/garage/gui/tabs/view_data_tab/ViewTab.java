package garage.gui.tabs.view_data_tab;

import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import garage.controller.Controller;
import garage.gui.tabs.BasicTabPanel;
import garage.gui.tabs.common.DisableItemSelectionModel;

@SuppressWarnings("serial")
public class ViewTab extends BasicTabPanel implements ListSelectionListener {
	private final static String LEFT_LIST_TITLE = "Användarnamn";
	private final static String RIGHT_LIST_TITLE = "Cyklar";
	private DefaultListModel<String> userList;
	private JList<String> graphicalUserList;
	
	private DefaultListModel<String> bikeList;

	public ViewTab(Controller controller) {
		super(controller);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JPanel leftListPanel = new JPanel();
		leftListPanel.setLayout(new BoxLayout(leftListPanel, BoxLayout.Y_AXIS));
		leftListPanel.add(new JLabel(LEFT_LIST_TITLE));
		userList = new DefaultListModel<String>();
		graphicalUserList = new JList<String>(userList);
		graphicalUserList.addListSelectionListener(this);
		leftListPanel.add(graphicalUserList);
		
		JPanel rightListPanel = new JPanel();
		rightListPanel.setLayout(new BoxLayout(rightListPanel, BoxLayout.Y_AXIS));
		leftListPanel.add(new JLabel(RIGHT_LIST_TITLE));
		bikeList = new DefaultListModel<String>();
		JList<String> graphicalBikeList = new JList<String>(bikeList);
		graphicalBikeList.setSelectionModel(new DisableItemSelectionModel());
		rightListPanel.add(graphicalBikeList);
		
		this.add(leftListPanel);
		this.add(rightListPanel);
	}
	
	@Override
	public void notifyTabChange() {
		userList.removeAllElements();
		List<String> usernameList = controller.getUsers();
		for (String username : usernameList) {
			userList.addElement(username);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		bikeList.removeAllElements();
		int selectedIndex = graphicalUserList.getSelectedIndex();
		if (selectedIndex != -1) {
			List<String> bikeIDList = controller.getBikes(userList.get(selectedIndex));
			for (String bikeID : bikeIDList) {
				bikeList.addElement(bikeID);
			}
		}
	}
}