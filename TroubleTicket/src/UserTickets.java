//This program will simulate a trouble ticket system
//ITMD 411 FALL 2021
//Made by Grace Sopha
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class UserTickets extends JFrame implements ActionListener {

	// class level member objects
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfUser = null;
	
	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuUser = new JMenu("User");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;
	private JLabel welcomeLabel;

	//create view for regular user account
		public UserTickets(Boolean isUser) {
			chkIfUser = isUser;
			createMenu();
			prepareGUI();
		}
		
	//user menu
	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);	

		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// initialize second sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Tickets");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);

		 /*
		  * continue implementing any other desired sub menu items (like 
		  * for update and delete sub menus for example) with similar 
		  * syntax & logic as shown above*
		 */
	}
	
	//user gui
	private void prepareGUI() {
		
		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		bar.add(mnuUser);
		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);

		addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		setSize(400, 200);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		
		welcomeLabel = new JLabel("Welcome to the IIT Help Desk!");
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		getContentPane().add(welcomeLabel, BorderLayout.CENTER);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// implement actions for sub menu items
		//exit button
		if (e.getSource() == mnuItemExit) {
			System.exit(0);
		} 
		//open a new ticket button
		else if (e.getSource() == mnuItemOpenTicket) {
			//get rid of welcome label
			welcomeLabel.setText(" ");

			try {
				//set up timestamp
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());  
		        //returns a LocalDateTime object which represents the same date-time value as this Timestamp  
		        String str=timestamp.toString();  

				// get ticket information
				String ticketName = JOptionPane.showInputDialog(null, "Enter your name");
				String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");
				String startDate = str.toString();
				String endDate = "";
				
				//do not allow blank entries
				if (ticketName.isEmpty() && ticketDesc.isEmpty()) {
					//notify user 
					System.out.println("Ticket cannot be created!");
					JOptionPane.showMessageDialog(null, "Ticket cannot be created! Please make sure all fields are filled in.");
				}
				//allow entries that are filled in to be entered to database
				else {
					// insert ticket information to database
					int id = dao.insertRecords(ticketName, ticketDesc, startDate, endDate);

					// display results if successful or not to console / dialog box
					if (id != 0) {
						System.out.println("Ticket ID : " + id + " created successfully!");
						JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
					} 
				}
			} catch (HeadlessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("Ticket cannot be created!");
				System.out.println("Ticket cannot be created! Please make sure all fields are filled in.");
			}
		}
		// view ticket button
		else if (e.getSource() == mnuItemViewTicket) {
			//get rid of welcome label
			welcomeLabel.setText(" ");
			
			// retrieve all tickets details for viewing in JTable
			try {
				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
				jt.setBounds(30, 50, 300, 400);
				JScrollPane sp = new JScrollPane(jt);
				getContentPane().add(sp);
				setVisible(true); // refreshes or repaints frame on screen

			} 
			catch (SQLException e1) {
				e1.printStackTrace();
				System.out.println("Tickets cannot be viewed!");
			} 
			}
	}
}
			