/**
 * This Class outlines the Window Components
 * to be used by PassNgR One
 */
package org.ProVision.passNgerOne;

/**
 * @author drago
 *
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.print.Printable;
import java.awt.Container;

//Layout Managers
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import java.text.MessageFormat;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import net.miginfocom.swing.*;

public class WindowComponent {
	
	static JTextField fname, lname, sex, dob,
		nation, docNum, address, JTPssngrIndx;
	static JTextArea scan;
	static JPanel contentPane, pass;
	static JLabel message, Title, lblPassngrAmt;
	static JTable manifest;
	static JButton add;
	static Boolean manual = false;	
	DefaultTableModel manifestModel;
	action passAction = new action();
	
	Object[] header = {" ", "NAMES", "SEX", "DATE OF BIRTH", "NATIONALITY", "PASSPORT #", "PERMANENT ADDRESS"};
	Object[][] data = {{"","CREW","\t\t\t","\t\t\t","\t\t\t","\t\t\t","\t\t\t"},
			{"1","\t\t\t","\t\t\t","\t\t\t","\t\t\t","\t\t\t","\t\t\t"},
			{"2","\t\t\t","\t\t\t","\t\t\t","\t\t\t","\t\t\t","\t\t\t"},
			{"3","\t\t\t","\t\t\t","\t\t\t","\t\t\t","\t\t\t","\t\t\t"},
			{"4","\t\t\t","\t\t\t","\t\t\t","\t\t\t","\t\t\t","\t\t\t"},
			{" ", "PASSENGERS", "\t\t\t", "\t\t\t", "\t\t\t", "\t\t\t", "\t\t\t"}};	
	
	public JMenuBar CreatePassBar(){
		final JMenuBar menubar;
		JMenu menu;
		JMenuItem menuItem;
		JCheckBoxMenuItem cbMenuItem;
		
		menubar = new JMenuBar();
		
		menu = new JMenu("Manifest");
		menu.setMnemonic(KeyEvent.VK_M);
		menu.getAccessibleContext().setAccessibleDescription("Main Menu");
		menubar.add(menu);
		
		menuItem = new JMenuItem("Load ...");
		menuItem.setMnemonic(KeyEvent.VK_L);
		menuItem.setAccelerator(KeyStroke.getKeyStroke('L', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		action.manifestOpen mopen = passAction.new manifestOpen();
		menuItem.addActionListener(mopen);
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Save As ...");
		menuItem.setMnemonic(KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		action.manifestSave msave = passAction.new manifestSave();
		menuItem.addActionListener(msave);
		menu.add(menuItem);
		
		menu.addSeparator();
		cbMenuItem = new JCheckBoxMenuItem("Manual Entry");
		cbMenuItem.setMnemonic(KeyEvent.VK_E);
		cbMenuItem.setSelected(false);
		action.switchToManual smanual = passAction.new switchToManual();
		cbMenuItem.addActionListener(smanual);
		menu.add(cbMenuItem);
		menu.addSeparator();
		
		menuItem = new JMenuItem("Print ...");
		menuItem.setMnemonic(KeyEvent.VK_P);
		menuItem.setAccelerator(KeyStroke.getKeyStroke('P', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		action.manifestPrint mprint = passAction.new manifestPrint();  
		menuItem.addActionListener(mprint);
		menu.add(menuItem);		
				
		menuItem = new JMenuItem("Exit");
		menuItem.setMnemonic(KeyEvent.VK_X);
		menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//System.exit(0);
				JFrame f = (JFrame) menubar.getParent().getParent().getParent();
				f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
			}
		});
		menu.add(menuItem);
				
		return menubar;
	}
	
	public Container createInfoPane(){
		//Design Content Pane
		JButton remove, home, last, back, forward, btnManifest, clear;
		JPanel holder, passengerPane, viewManifest;		
		JScrollPane manifestScrollPane;
		JLabel lblTitle, lblFnam,lblLnam, lblSex, lblDOB, lblNation, lblDocNum, lblAddr, lblScan;
		DefaultTableCellRenderer cellRender;
		
		// Passenger details form		
		lblFnam = new JLabel("Forenames: ");
		lblLnam = new JLabel("Surname: ");
		lblSex = new JLabel("Sex: ");
		lblDOB = new JLabel("Date of Birth: ");
		lblDOB.setToolTipText("The date of birth format is \"DD-MM-YY\"");
		lblNation = new JLabel("Nationality: ");
		lblDocNum = new JLabel("Passport #: ");
		lblAddr = new JLabel("Permanent Address: ");
		Title = new JLabel("Passenger Information: Scan Mode");
		lblScan = new JLabel("Scan: ");
		
		fname = new JTextField(15);
		fname.setEditable(false);
		lname = new JTextField(15);
		lname.setEditable(false);
		sex = new JTextField(5);
		sex.setEditable(false);
		dob = new JTextField(8);
		dob.setToolTipText("The date of birth format is \"DD-MM-YY\"");
		dob.setEditable(false);
		nation = new JTextField(15);
		nation.setEditable(false);
		docNum = new JTextField(10);
		docNum.setEditable(false);
		address = new JTextField(30);
		address.setEditable(false);
		
		//Element to receive input from document scanner
		scan = new JTextArea(1, 30);		
		action.triggerParse tfChange = passAction.new triggerParse();
		//Parse input from document scanner
		scan.getDocument().addDocumentListener(tfChange);
		
		BevelBorder border = new BevelBorder(BevelBorder.LOWERED,Color.gray, Color.gray);
		scan.setBorder(border);
		
		JTPssngrIndx = new JTextField(2);
		JTPssngrIndx.setHorizontalAlignment(JTextField.CENTER);
		JTPssngrIndx.setText("0");
		action.jumpto jto = passAction.new jumpto();
		JTPssngrIndx.addKeyListener(jto);		
		lblPassngrAmt = new JLabel("Total Number of Passengers: 0");
		
		add = new JButton("Add");
		add.setEnabled(false);
		action.passengerAdd manAdd = passAction.new passengerAdd();
		add.addActionListener(manAdd);
		clear = new JButton("Clear");		
		action.passengerClear clrPssngr = passAction.new passengerClear();
		clear.addActionListener(clrPssngr);
		remove = new JButton("Remove");
		action.passengerRemove remPssngr = passAction.new passengerRemove();
		remove.addActionListener(remPssngr);
		
		home = new JButton("Home");
		action.manifestHome start = passAction.new manifestHome();
		home.addActionListener(start);
		last = new JButton("Last");
		action.manifestEnd end = passAction.new manifestEnd();
		last.addActionListener(end);
		back = new JButton("<");
		action.manifestPrevious prev = passAction.new manifestPrevious();
		back.addActionListener(prev);
		forward = new JButton(">");
		action.manifestNext next = passAction.new manifestNext();
		forward.addActionListener(next);
				
		btnManifest = new JButton("View Manifest");
		action.switchViewList vList = passAction.new switchViewList();
		btnManifest.addActionListener(vList);
		
		passengerPane = new JPanel(new MigLayout("fillx", "[right]rel[grow,fill]rel[right]rel[grow,fill]"));
		passengerPane.setPreferredSize(new Dimension(1000,700));
		passengerPane.setBorder(new BevelBorder(BevelBorder.RAISED));
		passengerPane.add(Title, "Span, center, gapbottom 15");
		passengerPane.add(lblFnam);
		passengerPane.add(fname);
		passengerPane.add(lblLnam);
		passengerPane.add(lname, "wrap");
		passengerPane.add(lblSex);
		passengerPane.add(sex);
		passengerPane.add(lblDOB);
		passengerPane.add(dob, "wrap");
		passengerPane.add(lblNation);
		passengerPane.add(nation);
		passengerPane.add(lblDocNum);
		passengerPane.add(docNum,"wrap");
		passengerPane.add(lblAddr,"right");
		passengerPane.add(address,"span 3, left, grow, wrap");
		passengerPane.add(new JSeparator(),"span, growx");
		passengerPane.add(lblScan,"right");
		passengerPane.add(scan,"span 3, left, grow, wrap");		
		passengerPane.add(new JSeparator(),"span, growx");
		
		passengerPane.add(JTPssngrIndx,"span 4, center, wrap");
		passengerPane.add(lblPassngrAmt,"span 4, center, wrap");
		passengerPane.add(new JSeparator(),"span, growx");
		passengerPane.add(clear,"span, shrink, gapleft 150, cell 0 11, tag left");
		passengerPane.add(add,"shrink, gapleft 210, cell 0 11, tag left");
		passengerPane.add(remove,"shrink, cell 0 11, gapleft 210, shrink, tag left, wrap");
		passengerPane.add(home,"span, cell 0 12, gapleft 120, shrink, tag left");
		passengerPane.add(back,"cell 0 12, gapleft 5, shrink, tag left");
		passengerPane.add(forward,"cell 0 12, gapleft 390, shrink, tag left");
		passengerPane.add(last, "cell 0 12, gapleft 5, shrink, tag left, wrap");
		passengerPane.add(btnManifest,"span 4, center, wrap");
		passengerPane.add(new JSeparator(),"span, growx");
				
		message = new JLabel();
		message.setForeground(Color.red);
		passengerPane.add(message,"span 4, center");		
				
		manifestModel = new DefaultTableModel(data,header);		
		manifest = new JTable(manifestModel){
			/**
			 * 
			 */
			private static final long serialVersionUID = 4265072282374459677L;

			protected JTableHeader createDefaultTableHeader(){
				return new GroupableTableHeader(columnModel);
			}
			
			@Override
			public Printable getPrintable(PrintMode printMode, MessageFormat headerFormat, MessageFormat footerFormat) {
			       return new TablePrintable(this, printMode, headerFormat, footerFormat);
			    }
		};
		//set rows of the manifest
		manifest.setRowHeight(30);
		
		//Adjust the row the passenger area of the manifest.
		manifest.setRowHeight(4, 35);		
		for (Integer i = 1; i < 7; i++){
			cellRender = (DefaultTableCellRenderer) manifest.getCellRenderer(4, i);
			cellRender.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);			
		}
		
		manifest.setPreferredScrollableViewportSize(new Dimension(1000, 700));
		
		//Group columns of the table
		TableColumnModel cm = manifest.getColumnModel();		
				
		cm.getColumn(0).setPreferredWidth(40);
		cm.getColumn(1).setPreferredWidth(250);
		cm.getColumn(2).setPreferredWidth(80);
		cm.getColumn(3).setPreferredWidth(160);
		cm.getColumn(4).setPreferredWidth(200);
		cm.getColumn(5).setPreferredWidth(150);
		cm.getColumn(6).setPreferredWidth(520);		
		
		
		ColumnGroup title = new ColumnGroup("LIST OF PASSENGERS ORDINANCE NO.155                              ANGUILLA, WEST INDIES");
		ColumnGroup NOS = new ColumnGroup("NAME OF SHIP");
		ColumnGroup depart = new ColumnGroup("DEPARTURE");
		ColumnGroup arrive = new ColumnGroup("ARRIVAL");
		ColumnGroup time = new ColumnGroup("TIME");
		ColumnGroup GB = new ColumnGroup("GB EXPRESS");
		ColumnGroup departdtl = new ColumnGroup("\t\t");
		ColumnGroup arrivedtl = new ColumnGroup("\t\t");
		ColumnGroup timedtl = new ColumnGroup("\t\t");
		
		GB.add(cm.getColumn(0));
		GB.add(cm.getColumn(1));
		
		departdtl.add(cm.getColumn(2));
		departdtl.add(cm.getColumn(3));
		
		arrivedtl.add(cm.getColumn(4));
		arrivedtl.add(cm.getColumn(5));
		
		timedtl.add(cm.getColumn(6));
		
		NOS.add(GB);
		depart.add(departdtl);
		arrive.add(arrivedtl);
		time.add(timedtl);
		
		title.add(NOS);
		title.add(depart);
		title.add(arrive);
		title.add(time);
		
		GroupableTableHeader head = (GroupableTableHeader)manifest.getTableHeader();
		head.addColumnGroup(title);
		head.setFont(new Font("Times New Roman",Font.BOLD,14));
		//End Column Grouping
				
		JScrollPane scrollTable = new JScrollPane(manifest);
		scrollTable.setPreferredSize(new Dimension(1000, 700));
		
		//View all scanned passengers on this pane
		viewManifest = new JPanel(new MigLayout("Wrap 4"));		
		//viewManifest.setPreferredSize(new Dimension(1000,700));
		lblTitle = new JLabel("ANGUILLA FERRY SHUTTLE & CHARTER SERICES LIMITED");
		lblTitle.setFont(new Font("Times New Roman",Font.BOLD,16));
		viewManifest.add(lblTitle,"span 4, center, wrap");		
				
		viewManifest.add(scrollTable,"span 4, center, wrap");
						
		btnManifest = new JButton("Back");
		viewManifest.add(btnManifest,"span 4, center, wrap");		
		action.switchMainInput vInput = passAction.new switchMainInput();
		btnManifest.addActionListener(vInput);
		
		//add viewManifest to scrollPane
		
		manifestScrollPane = new JScrollPane(viewManifest);
		manifestScrollPane.setPreferredSize(new Dimension(1000, 700));

		contentPane = new JPanel(new CardLayout());
		contentPane.add(passengerPane,0);
		contentPane.add(manifestScrollPane,1);
		
		holder = new JPanel(new GridBagLayout());
		holder.setPreferredSize(new Dimension(1000,700));		
		holder.add(contentPane);
		return holder;		
	}
}