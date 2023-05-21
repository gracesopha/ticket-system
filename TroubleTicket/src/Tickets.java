//This program will simulate a trouble ticket system
//ITMD 411 FALL 2021
//Made by Grace Sopha
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.HeadlessException;

import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

	// class level member objects
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfAdmin = null;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdateTicket;
	JMenuItem mnuItemDeleteTicket;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemCloseTicket;
	JMenuItem mnuItemViewTicket;
	JMenuItem mnuItemFindTicket;
	private JLabel welcomeLabel;
	
	//create view for admin account
	public Tickets(Boolean isAdmin) {
		chkIfAdmin = isAdmin;
		createMenu();
		prepareGUI();
	}

	//admin menu
	private void createMenu() {
		/* Initialize sub menu items **************************************/
		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

		// initialize first sub menu items for Admin main menu
		mnuItemUpdateTicket = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemUpdateTicket);

		// initialize second sub menu items for Admin main menu
		mnuItemDeleteTicket = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDeleteTicket);

		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);
		
		//add to admin sub menu
		//let the admin close ticket
		mnuItemCloseTicket = new JMenuItem("Close Ticket");
		// add to Ticket Main menu item
		mnuAdmin.add(mnuItemCloseTicket);
	
		// initialize second sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Tickets");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);
		
		// initialize second sub menu item for Tickets main menu
		mnuItemFindTicket = new JMenuItem("Find Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemFindTicket);
		
		
		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemUpdateTicket.addActionListener(this);
		mnuItemDeleteTicket.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemCloseTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);
		mnuItemFindTicket.addActionListener(this);

		 /*
		  * continue implementing any other desired sub menu items (like 
		  * for update and delete sub menus for example) with similar 
		  * syntax & logic as shown above*
		 */
	}
	
	//admin gui
	private void prepareGUI() {
		
		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		bar.add(mnuAdmin);
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
		
		welcomeLabel = new JLabel("Welcome, Admin!");
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
		
		//find ticket button
		else if (e.getSource() == mnuItemFindTicket) {
			//get rid of welcome label
			welcomeLabel.setText(" ");
			
			// retrieve certain ticket details for viewing in JTable
			try {
				String ticketID = JOptionPane.showInputDialog(null, "Please enter the ticket id to find: ");
				int id = Integer.parseInt(ticketID);
				
				//show results of database with j table
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.findRecords(id)));
				jt.setBounds(30, 50, 300, 400);
				JScrollPane sp = new JScrollPane(jt);
				getContentPane().add(sp);
				setVisible(true); // refreshes or repaints frame on screen
				// display results if successful or not to console / dialog box
				if (id != 0) {
					System.out.println("Ticket ID : " + id + " found successfully!");
					JOptionPane.showMessageDialog(null, "Ticket id: " + id + " found");
				} 
				
			}
			catch (SQLException e1) {
				e1.printStackTrace();
				System.out.println("Tickets cannot be found!");
			} 
			}
		
			//update ticket button
			else if (e.getSource() == mnuItemUpdateTicket) {
				//get rid of welcome label
				welcomeLabel.setText(" ");
				
				// retrieve all tickets details for viewing in JTable
				try {
					//enter the ticket id for the ticket to update
					String ticketID = JOptionPane.showInputDialog(null, "Please enter the ticket id to update: ");
					String newticketDesc = JOptionPane.showInputDialog(null, "Please enter the new description: ");
					int id = Integer.parseInt(ticketID);
					//confirm update
					int ans = JOptionPane.showConfirmDialog(null, "Update ticket id: " + id + " with new description?", "WARNING", JOptionPane.YES_NO_OPTION);
					//choose yes or no option			
					if (ans == JOptionPane.YES_OPTION) {
						//update database
						int update = dao.updateRecords(newticketDesc, id);
						//notify the update has been completed
						System.out.println("Ticket ID : " + id + " updates successfully!");
						JOptionPane.showMessageDialog(null, "Ticket id: " + id + " updated");
						//it can't be 0
						if (id == 0) {
							JOptionPane.showMessageDialog(null, "The ticket cannot be updated");
						}
					}	
					else if (ans == JOptionPane.NO_OPTION) {
						System.out.println("Ticket ID : " + id + " was not updated");
						JOptionPane.showMessageDialog(null, "Ticket id: " + id + " was not updated");
					}					
					
				} catch (Exception e1) {
					e1.printStackTrace();
					System.out.println("The ticket cannot be updated");
				}			
			
		}
			// delete ticket button
			else if (e.getSource() == mnuItemDeleteTicket) {
				//get rid of welcome label
				welcomeLabel.setText(" ");
				
				//choose a ticket to delete
				try {
					String ticketID = JOptionPane.showInputDialog(null, "Please enter the ticket id to delete: ");
					int id = Integer.parseInt(ticketID);
					
					// confirm that user wants to delete ticket	
					int ans = JOptionPane.showConfirmDialog(null, "Delete ticket id: " + id + "?", "WARNING", JOptionPane.YES_NO_OPTION);
					//choose yes or no option			
					if (ans == JOptionPane.YES_OPTION) {
						//update database
						int delete = dao.deleteRecords(id);
						//notify the update has been completed
						System.out.println("Ticket ID : " + id + " deleted successfully!");
						JOptionPane.showMessageDialog(null, "Ticket id: " + id + " deleted");
					//it can't be 0
						if (id == 0) {
							JOptionPane.showMessageDialog(null, "The ticket cannot be deleted");
									 }
					}
					else if (ans == JOptionPane.NO_OPTION) {
						System.out.println("Ticket ID : " + id + " was not deleted");
						JOptionPane.showMessageDialog(null, "Ticket id: " + id + " was not deleted");
					}
					
				} catch (Exception e1) {
					e1.printStackTrace();
					System.out.println("The ticket cannot be deleted");
				}			
			
		}
			// close ticket button
			else if (e.getSource() == mnuItemCloseTicket) {
				//get rid of welcome label
				welcomeLabel.setText(" ");

				try {
					//set up timestamp
					//enter ticket to close
					String ticketID = JOptionPane.showInputDialog(null, "Please enter the ticket id to close: ");
					int id = Integer.parseInt(ticketID);
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());  
			        //returns a LocalDateTime object which represents the same date-time value as this Timestamp  
			        String str=timestamp.toString();  

					// get ticket information
					String endDate = str.toString();
	
					// confirm that user wants to delete ticket	
					int ans = JOptionPane.showConfirmDialog(null, "Close ticket id: " + id + "?", "WARNING", JOptionPane.YES_NO_OPTION);
					//choose yes or no option			
					if (ans == JOptionPane.YES_OPTION) {
						// insert ticket information to database
						String close = dao.closeTicket(endDate, id);
						//notify the update has been completed
						System.out.println("Ticket ID : " + id + " closed successfully!");
						JOptionPane.showMessageDialog(null, "Ticket id: " + id + " closed");
						//it can't be 0
						if (id == 0) {
							JOptionPane.showMessageDialog(null, "The ticket cannot be closed");
						}
					}
					else if (ans == JOptionPane.NO_OPTION) {
						System.out.println("Ticket ID : " + id + " was not closed");
						JOptionPane.showMessageDialog(null, "Ticket id: " + id + " was not closed");
						}						
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("Ticket cannot be created!");
				}
		}
		/*
		 * continue implementing any other desired sub menu items (like for update and
		 * delete sub menus for example) with similar syntax & logic as shown above
		 */
	}
}

