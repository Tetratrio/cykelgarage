package garage.gui.tabs;

import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import garage.controller.BicycleGarageManager;
import garage.gui.BicycleGarageGUI;

@SuppressWarnings("serial")
public class ManipulationTab extends BasicTabPanel implements ActionListener {
    private final static String REG_USR_TITLE = "Registrera ny användare";
    private final static String REG_USR_USRNAME_TITLE = "Användarnamn";
    private final static String REG_USR_PASSWORD_TITLE = "Lösenord";
    private final static String REG_USR_BUTTON_TITLE = " ";
    private final static String REG_USR_BUTTON_LABEL = "Registrera";
    private final static String REG_USR_EMPTY_SPACE = "   ";


    private final static String UNREG_USR_TITLE = "Avregistrera befintlig användare";
    private final static String UNREG_USR_USRNAME_TITLE = "Användarnamn";
    private final static String UNREG_USR_BUTTON_TITLE = " ";
    private final static String UNREG_USR_BUTTON_LABEL = "Avregistrera";
    private final static String UNREG_USR_EMPTY_SPACE = "";

    private final static String ADD_BIKE_TITLE = "Anslut ny cykel";
    private final static String ADD_BIKE_USRNAME_TITLE = "Användarnamn";
    private final static String ADD_BIKE_BUTTON_TITLE = " ";
    private final static String ADD_BIKE_BUTTON_LABEL = "Anslut";
    private final static String ADD_BIKE_EMPTY_SPACE = "         ";

    private final static String REM_BIKE_TITLE = "Ta bort befintlig cykel";
    private final static String REM_BIKE_BIKEID_TITLE = "Cykel ID";
    private final static String REM_BIKE_BUTTON_TITLE = " ";
    private final static String REM_BIKE_BUTTON_LABEL = "Ta bort";
    private final static String REM_BIKE_EMPTY_SPACE = "        ";

    private JTextField regUsrUsrnameTextField;
    private JTextField regUsrPasswordTextField;
    private JTextField unregUsrUsrnameTextField;
    private JTextField addBikeUsrnameTextField;
    private JTextField remBikeBikeIDTextField;

	public ManipulationTab(BicycleGarageManager bicycleGarageManager) {
		super(bicycleGarageManager);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel p;
        JButton b;
        TitledBorder t;
		
		JPanel registerUserPanel = new JPanel();
		registerUserPanel.setLayout(new BoxLayout(registerUserPanel, BoxLayout.X_AXIS));
		p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel(REG_USR_USRNAME_TITLE));
        regUsrUsrnameTextField = new JTextField();
        p.add(regUsrUsrnameTextField);
        p.add(new JLabel(REG_USR_PASSWORD_TITLE));
        regUsrPasswordTextField = new JTextField();
        p.add(regUsrPasswordTextField);
        registerUserPanel.add(p);
        p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(new JLabel(REG_USR_EMPTY_SPACE));
		registerUserPanel.add(p);
		p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel(REG_USR_BUTTON_TITLE));
        b = new JButton(REG_USR_BUTTON_LABEL);
        b.addActionListener(this);
        p.add(b);
        registerUserPanel.add(p);
        t = new TitledBorder(REG_USR_TITLE);
        registerUserPanel.setBorder(t);

		JPanel removeUserPanel = new JPanel();
		removeUserPanel.setLayout(new BoxLayout(removeUserPanel, BoxLayout.X_AXIS));
        
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel(UNREG_USR_USRNAME_TITLE));
        unregUsrUsrnameTextField = new JTextField();
        p.add(unregUsrUsrnameTextField);
        removeUserPanel.add(p);
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel(UNREG_USR_EMPTY_SPACE));
        removeUserPanel.add(p);
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel(UNREG_USR_BUTTON_TITLE));
        b = new JButton(UNREG_USR_BUTTON_LABEL);
        b.addActionListener(this);
        p.add(b);
        removeUserPanel.add(p);
        t = new TitledBorder(UNREG_USR_TITLE);
        removeUserPanel.setBorder(t);
		
		JPanel connectBikePanel = new JPanel();
		connectBikePanel.setLayout(new BoxLayout(connectBikePanel, BoxLayout.X_AXIS));
       
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel(ADD_BIKE_USRNAME_TITLE));
        addBikeUsrnameTextField = new JTextField();
        p.add(addBikeUsrnameTextField);
        connectBikePanel.add(p);
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel(ADD_BIKE_EMPTY_SPACE));
        connectBikePanel.add(p);
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel(ADD_BIKE_BUTTON_TITLE));
        b = new JButton(ADD_BIKE_BUTTON_LABEL);
        b.addActionListener(this);
        p.add(b);
        connectBikePanel.add(p);
        t = new TitledBorder(ADD_BIKE_TITLE);
        connectBikePanel.setBorder(t);

		JPanel removeBikePanel = new JPanel();
		removeBikePanel.setLayout(new BoxLayout(removeBikePanel, BoxLayout.X_AXIS));
        
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel(REM_BIKE_BIKEID_TITLE));
        remBikeBikeIDTextField = new JTextField();
        p.add(remBikeBikeIDTextField);
        removeBikePanel.add(p);
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel(REM_BIKE_EMPTY_SPACE));
        removeBikePanel.add(p);
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel(REM_BIKE_BUTTON_TITLE));
        b = new JButton(REM_BIKE_BUTTON_LABEL);
        b.addActionListener(this);
        p.add(b);
        removeBikePanel.add(p);
        t = new TitledBorder(REM_BIKE_TITLE);
        removeBikePanel.setBorder(t);

        JPanel marginPanel;
        
        this.add(Box.createRigidArea(new Dimension(0,10)));
        marginPanel = new JPanel();
        marginPanel.setLayout(new BoxLayout(marginPanel, BoxLayout.X_AXIS));
        marginPanel.add(Box.createRigidArea(new Dimension(10,0)));
        marginPanel.add(registerUserPanel);
        marginPanel.add(Box.createRigidArea(new Dimension(10,0)));
        this.add(marginPanel);
        
        this.add(Box.createRigidArea(new Dimension(0,10)));
        marginPanel = new JPanel();
        marginPanel.setLayout(new BoxLayout(marginPanel, BoxLayout.X_AXIS));
        marginPanel.add(Box.createRigidArea(new Dimension(10,0)));
        marginPanel.add(removeUserPanel);
        marginPanel.add(Box.createRigidArea(new Dimension(10,0)));
        this.add(marginPanel);
        
        this.add(Box.createRigidArea(new Dimension(0,10)));
        marginPanel = new JPanel();
        marginPanel.setLayout(new BoxLayout(marginPanel, BoxLayout.X_AXIS));
        marginPanel.add(Box.createRigidArea(new Dimension(10,0)));
        marginPanel.add(connectBikePanel);
        marginPanel.add(Box.createRigidArea(new Dimension(10,0)));
        this.add(marginPanel);
        
        this.add(Box.createRigidArea(new Dimension(0,10)));
        marginPanel = new JPanel();
        marginPanel.setLayout(new BoxLayout(marginPanel, BoxLayout.X_AXIS));
        marginPanel.add(Box.createRigidArea(new Dimension(10,0)));
        marginPanel.add(removeBikePanel);
        marginPanel.add(Box.createRigidArea(new Dimension(10,0)));
        this.add(marginPanel);
        this.add(Box.createRigidArea(new Dimension(0,10)));
	}

    @Override
    public void notifyTabChange() {
        clearTextFields();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		String buttonLabel = e.getActionCommand();
		
		if (buttonLabel.equals(REG_USR_BUTTON_LABEL)) {
			newUser();
		} else if (buttonLabel.equals(UNREG_USR_BUTTON_LABEL)) {
			removeUser();
		} else if (buttonLabel.equals(ADD_BIKE_BUTTON_LABEL)) {
			addBike();
		} else {
			removeBike();
		}
	}
	
	private void clearTextFields() {
		regUsrUsrnameTextField.setText("");
        regUsrPasswordTextField.setText("");
        addBikeUsrnameTextField.setText("");
        remBikeBikeIDTextField.setText("");
	}
	
	private void newUser() {
		String username = regUsrUsrnameTextField.getText();
		String password = regUsrPasswordTextField.getText();
		
		if (username.length() != 10 || !username.matches("[0-9]+")) {
			BicycleGarageGUI.showMessage("Ett användarnamn måste bestå av 10 siffror");
			return;
		}
		
		if (password.length() != 4 || !password.matches("[0-9]+")) {
			BicycleGarageGUI.showMessage("Ett lösenord måste bestå av 4 siffror");
			return;
		}
		
		boolean success = bicycleGarageManager.newUser(username, password);
		if (success) {
			BicycleGarageGUI.showMessage("Ny användare " + regUsrUsrnameTextField.getText() + " registrerad");
		} else {
			BicycleGarageGUI.showMessage("Misslyckades med registrering av ny användare " + regUsrUsrnameTextField.getText());
		}
		clearTextFields();
	}
	
	private void removeUser() {
		boolean success = bicycleGarageManager.removeUser(unregUsrUsrnameTextField.getText());
		if (success) {
			BicycleGarageGUI.showMessage("Användare " + unregUsrUsrnameTextField.getText() + " borttagen");
		}
		else {
			BicycleGarageGUI.showMessage("Misslyckades med att ta bort användare " + unregUsrUsrnameTextField.getText());
		}
		clearTextFields();
	}
	
	private void addBike() {
		boolean success = bicycleGarageManager.connectNewBike(addBikeUsrnameTextField.getText());
		if (success) {
			BicycleGarageGUI.showMessage("Ny cykel ansluten till cykelgaraget");
			BicycleGarageGUI.showMessage("Skriver ut ny streckkod...");
		} else {
			BicycleGarageGUI.showMessage("Misslyckades att ansluta en ny cykel till cykelgaraget");
		}
		clearTextFields();
	}
	
	private void removeBike() {
		boolean success = bicycleGarageManager.removeBike(remBikeBikeIDTextField.getText());
		if (success) {
			BicycleGarageGUI.showMessage("Tog bort cykel " + remBikeBikeIDTextField.getText());
		} else {
			BicycleGarageGUI.showMessage("Misslyckades att ta bort cykel " + remBikeBikeIDTextField.getText());
		}
		clearTextFields();
	}
}
