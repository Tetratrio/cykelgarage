package garage.gui.tabs;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import garage.controller.BicycleGarageManager;
import garage.gui.BicycleGarageGUI;
import garage.logging.LogAccess;

@SuppressWarnings("serial")
public class SearchTab extends BasicTabPanel implements ActionListener {
	private final static String SEARCH_OPTIONS_TITLE = "Sök efter";
	private final static String SEARCH_TEXTFIELD_TITLE = "Sökterm";
	private final static String SEARCH_BUTTON_TITLE = " ";
	private final static String[] SEARCH_FOR_ITEMS = {"Användarnamn", "Cykel ID"};
	private final static String SEARCH_BUTTON_LABEL = "Sök";
	private final static String SEARCH_RESULT_TITLE = "Sökresultat";
	
	private DefaultListModel<String> searchResultList;
	private JComboBox<String> searchMenu;
	private JTextField searchTextField;
	
	public SearchTab(BicycleGarageManager bicycleGarageManager) {
		super(bicycleGarageManager);
		this.setLayout(new BorderLayout());
		
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
		
		JPanel searchOptionsPanel = new JPanel();
		searchOptionsPanel.setLayout(new BoxLayout(searchOptionsPanel, BoxLayout.Y_AXIS));
		searchOptionsPanel.add(new JLabel(SEARCH_OPTIONS_TITLE));
		searchMenu = new JComboBox<String>(SEARCH_FOR_ITEMS);
		searchOptionsPanel.add(searchMenu);
		
		JPanel searchTextPanel = new JPanel();
		searchTextPanel.setLayout(new BoxLayout(searchTextPanel, BoxLayout.Y_AXIS));
		searchTextPanel.add(new JLabel(SEARCH_TEXTFIELD_TITLE));
		searchTextField = new JTextField();
		searchTextPanel.add(searchTextField);
		
		JPanel searchButtonPanel = new JPanel();
		searchButtonPanel.setLayout(new BoxLayout(searchButtonPanel, BoxLayout.Y_AXIS));
		searchButtonPanel.add(new JLabel(SEARCH_BUTTON_TITLE));
		JButton searchButton = new JButton(SEARCH_BUTTON_LABEL);
		searchButton.addActionListener(this);
		searchButtonPanel.add(searchButton);
		
		searchPanel.add(searchOptionsPanel);
		searchPanel.add(searchTextPanel);
		searchPanel.add(searchButtonPanel);
		
		JPanel searchResultPanel = new JPanel();
		searchResultPanel.setLayout(new BorderLayout());
		searchResultList = new DefaultListModel<String>();
		JList<String> graphicalSearchResultList = new JList<String>(searchResultList);
		graphicalSearchResultList.setSelectionModel(new DisableItemSelectionModel());
		searchResultPanel.add(graphicalSearchResultList, BorderLayout.CENTER);
		searchResultPanel.setBorder(new TitledBorder(SEARCH_RESULT_TITLE));
		
		this.add(searchPanel, BorderLayout.NORTH);
		this.add(searchResultPanel, BorderLayout.CENTER);
	}
	
	@Override
	public void notifyTabChange() {
		searchTextField.setText("");
		searchResultList.removeAllElements();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String searchFor = searchTextField.getText();
		switch (searchMenu.getSelectedIndex()) {
			case 0:
				fillSearchResults(bicycleGarageManager.searchUsername(searchFor));
				break;
			case 1:
				fillSearchResults(bicycleGarageManager.searchBikeID(searchFor));
				break;
			default:
				BicycleGarageGUI.showMessage("GUI Error: Selected search menu option should not exist.");
				LogAccess.error().log("GUI Error: Selected search menu option should not exist.");
				break;
		}
	}
	
	private void fillSearchResults(List<String> result) {
		searchResultList.removeAllElements();
		for (String line : result) {
			searchResultList.addElement(line);
		}
	}

}
