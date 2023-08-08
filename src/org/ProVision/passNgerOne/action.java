package org.ProVision.passNgerOne;

import java.awt.Color;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PrinterException;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.*;
import org.w3c.dom.*;
import java.io.*;

public class action { 
	passenger traveler;
	Integer sIndx;
	Integer manifestIndex;	
	static ArrayList<passenger> manifestList = new ArrayList<passenger>();	
	
	final String recordStart = "START", recordEnd = "END";
	final String catLname = "SURNAME:", catFname = "FORENAMES:", catDOB = "DATE OF BIRTH:", catSex = "SEX:" ;
	final String catDnum = "DOC. NUMBER:", catNat = "NATIONALITY:", catStreet = "ADDRESS STREET:";
	final String catCity = "ADDRESS CITY:", catState = "ADDRESS STATE:", catPCode = "ADDRESS POSTAL CODE:"; 
	final String catCountry = "ADDRESS COUNTRY:", recordClose = "\n";
	static final Integer manifestStart = 5;
	
	public action() {
		
		sIndx = 0;	
	}
	
	public class triggerParse implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent e) {
			//Process string scanned into the box
			WindowComponent.message.setText(null);			
			checkScan(WindowComponent.scan.getText());
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			
			
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public void scanBoxChange(){
		Runnable TFChange = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				WindowComponent.scan.setBackground(Color.green);
				WindowComponent.scan.setText(null);
			}
		};
		
		javax.swing.SwingUtilities.invokeLater(TFChange);
	}
	
	public void scanBoxClear(){
		Runnable TFChange = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				WindowComponent.scan.setBackground(Color.white);				
			}
		};
		
		javax.swing.SwingUtilities.invokeLater(TFChange);
	}
	
	public void checkScan(String scanString){
		if (scanString.toUpperCase().indexOf(recordStart) > -1){
			if(scanString.toUpperCase().indexOf(recordEnd) > scanString.toUpperCase().indexOf(recordStart)){
				traveler = new passenger();
				String catDtl;
				
				scanBoxChange();
				
				//Set index search point
				sIndx = scanString.toUpperCase().indexOf(recordStart) + recordStart.length();
				
				
				//Locate the Surname and extract Values
				catDtl = searchScan(catLname, scanString.substring(sIndx));				
				traveler.setLname(catDtl.trim());
				WindowComponent.lname.setText(catDtl.trim());
				
				//Locate the Forenames and extract Values
				catDtl = searchScan(catFname, scanString.substring(sIndx));
				traveler.setFname(catDtl.trim());
				WindowComponent.fname.setText(catDtl.trim());
				
				//Locate Date of Birth and extract Values
				catDtl = searchScan(catDOB, scanString.substring(sIndx));
				traveler.setDOB(catDtl.trim());
				WindowComponent.dob.setText(catDtl.trim());
				
				//Locate gender and extract Values
				catDtl = searchScan(catSex, scanString.substring(sIndx));
				traveler.setSex(catDtl.trim());
				WindowComponent.sex.setText(catDtl.trim());
				
				//Locate document number and extract Values
				catDtl = searchScan(catDnum, scanString.substring(sIndx));
				traveler.setDocNum(catDtl.trim());
				WindowComponent.docNum.setText(catDtl.trim());
				
				//Locate Nationality and extract Values
				catDtl = searchScan(catNat, scanString.substring(sIndx));
				traveler.setNat(catDtl.trim());
				WindowComponent.nation.setText(catDtl.trim());
				
				//Locate address and extract Values
				String address;
				catDtl = searchScan(catStreet, scanString.substring(sIndx));
				traveler.setAddStr(catDtl.trim());				
				address = catDtl.trim();
						
				catDtl = searchScan(catCity, scanString.substring(sIndx));
				traveler.setAddCity(catDtl.trim());
				address = address.concat(compileAddress(catDtl.trim()));
								
				catDtl = searchScan(catState, scanString.substring(sIndx));
				traveler.setAddStat(catDtl.trim());
				address = address.concat(compileAddress(catDtl.trim()));
								
				catDtl = searchScan(catPCode, scanString.substring(sIndx));
				traveler.setAddPost(catDtl.trim());
				address = address.concat(compileAddress(catDtl.trim()));
				
				catDtl = searchScan(catCountry, scanString.substring(sIndx));
				traveler.setAddCountry(catDtl.trim());
				address = address.concat(compileAddress(catDtl.trim()));
				WindowComponent.address.setText(address.trim());
				
				manifestList.add(traveler);
				manifestIndex = manifestList.size() - 1;
				scanBoxClear();
				WindowComponent.message.setForeground(Color.black);
				WindowComponent.message.setText("Passenger Added.");
				Integer amt = (Integer) manifestList.size();
				WindowComponent.JTPssngrIndx.setText(amt.toString());
				WindowComponent.lblPassngrAmt.setText("Total Number of Passengers: " + amt.toString());
			}else{
				WindowComponent.message.setForeground(Color.red);
				WindowComponent.message.setText("Incorrect Scan Text Format.");
			}
		}
	}
	
	public String searchScan(String target, String baseString){
		String catVal = " ";
		Integer findIndx = baseString.toUpperCase().indexOf(target);
		
		if (findIndx < 0){
			return catVal;
		}else{
			findIndx = findIndx + target.length();
			catVal = baseString.substring(findIndx, baseString.indexOf(recordClose, findIndx));
			return catVal;
		}
	}
	
	public void genReport(ArrayList<passenger> p){
		Integer cnt = 1;
		DefaultTableModel model = (DefaultTableModel) WindowComponent.manifest.getModel();
		for(passenger trav: p){
			model.addRow(new Object[]{cnt.toString(),trav.getFname() + " " + trav.getLname(),trav.getSex(),
					trav.getDOB(),trav.getNat(),trav.getDocNum(),trav.getAddStr() + " " +
			trav.getAddCity() + " " + trav.getAddStat() + " " + trav.getAddPost() + " " + trav.getAddCountry()});
			WindowComponent.manifest.setRowHeight(model.getRowCount() -1 , 25);			
			cnt++;
		}
	}
	
	public String compileAddress(String part){
		if (part.length() < 1 ){
			return "";
		}else{
			part = " " + part;
			return part;	
		}
	}	
	
	public void loadManifest(File f, ArrayList<passenger> p){		
		try {
			ArrayList<passenger> plist = new ArrayList<passenger>();
			DocumentBuilderFactory mFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder mBuilder = mFactory.newDocumentBuilder();
			//Attempt to parse the manifest document
			Document mDoc = mBuilder.parse(f);
			//normalise the node represented in the file
			mDoc.getDocumentElement().normalize();			
			NodeList nlist = mDoc.getElementsByTagName("Passenger");
			
			if (nlist.getLength() > 0){
				for (int cnt = 0; cnt < nlist.getLength(); cnt++){
					Node mNode = nlist.item(cnt);
					
					if (mNode.getNodeType() == Node.ELEMENT_NODE){
						passenger trav = new passenger();
						Element passngr = (Element) mNode;
						trav.setLname(passngr.getElementsByTagName("Surname").item(0).getTextContent());
						trav.setFname(passngr.getElementsByTagName("Forenames").item(0).getTextContent());
						trav.setDOB(passngr.getElementsByTagName("DateOfBirth").item(0).getTextContent());
						trav.setSex(passngr.getElementsByTagName("Sex").item(0).getTextContent());
						trav.setDocNum(passngr.getElementsByTagName("DocNumber").item(0).getTextContent());
						trav.setNat(passngr.getElementsByTagName("Nationality").item(0).getTextContent());
						trav.setAddStr(passngr.getElementsByTagName("Street").item(0).getTextContent());
						trav.setAddCity(passngr.getElementsByTagName("City").item(0).getTextContent());
						trav.setAddStat(passngr.getElementsByTagName("State").item(0).getTextContent());
						trav.setAddPost(passngr.getElementsByTagName("PostalCode").item(0).getTextContent());
						trav.setAddCountry(passngr.getElementsByTagName("Country").item(0).getTextContent());
						plist.add(trav);
					}else{
						JOptionPane.showMessageDialog(WindowComponent.contentPane, "Incorrect XML file format.  Incorrect XML elements.","PassNgR 1: Manifest Load Error",JOptionPane.ERROR_MESSAGE);
					}
				}
			}else{
				JOptionPane.showMessageDialog(WindowComponent.contentPane, "Incorrect XML file format.  No passengers found.","PassNgR 1: Manifest Load Error",JOptionPane.ERROR_MESSAGE);
			}
			
			if(plist.isEmpty()){
				JOptionPane.showMessageDialog(WindowComponent.contentPane, "Unable to load passenger particulars to the manifest","PassNgR 1: Manifest Load Error",JOptionPane.ERROR_MESSAGE);
			}else{
				manifestList.clear();
				for(int i = 0; i < plist.size(); i++){
					manifestList.add(plist.get(i));					
				}				
				plist.clear();
				manifestIndex = 0;
				int modelIndx;				
				modelIndx = (int) manifestIndex;
				passenger trv = manifestList.get(modelIndx);
				WindowComponent.lname.setText(trv.getLname().trim());
				WindowComponent.fname.setText(trv.getFname().trim());
				WindowComponent.sex.setText(trv.getSex().trim());
				WindowComponent.dob.setText(trv.getDOB().trim());
				WindowComponent.nation.setText(trv.getNat().trim());
				WindowComponent.docNum.setText(trv.getDocNum().trim());
				String address = compileAddress(trv.getAddStr().trim()); 
				address = address.concat(compileAddress(trv.getAddCity().trim()));
				address = address.concat(compileAddress(trv.getAddStat().trim()));
				address = address.concat(compileAddress(trv.getAddPost().trim()));
				address = address.concat(compileAddress(trv.getAddCountry()));
				WindowComponent.address.setText(address.trim());
				Integer amt = (Integer) manifestList.size();
				WindowComponent.JTPssngrIndx.setText("1");				
				WindowComponent.lblPassngrAmt.setText("Total Number of Passengers: " + amt.toString());
			}
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(WindowComponent.contentPane, "Unable to load the manifest due to: \n" + e.getMessage(),"PassNgR 1: Manifest Load Error",JOptionPane.ERROR_MESSAGE);
			//e.printStackTrace();			
		}
		
	}
	
	public void saveManifest(String filename, ArrayList<passenger> p){
		if (p.isEmpty()){
			WindowComponent.message.setForeground(Color.red);
			WindowComponent.message.setText("There are no passengers loaded");
		}else{				
			try {
				DocumentBuilderFactory manifestFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder manifestBuilder = manifestFactory.newDocumentBuilder();
				Document manifestDocument = manifestBuilder.newDocument();
				
				//add elements of the manifest Document
				Element rootElement = manifestDocument.createElement("Manifest");
				manifestDocument.appendChild(rootElement);
				
				for (passenger trav: p){
					//add child element passenger
					Element passngr = manifestDocument.createElement("Passenger");
					//add passenger element to the document
					rootElement.appendChild(passngr);
					
					//add Last Name element to the Passenger element
					Element pssngrElement = manifestDocument.createElement("Surname");
					pssngrElement.appendChild(manifestDocument.createTextNode(trav.getLname()));
					passngr.appendChild(pssngrElement);
					//add Forenames element to the Passenger element
					pssngrElement = manifestDocument.createElement("Forenames");
					pssngrElement.appendChild(manifestDocument.createTextNode(trav.getFname()));
					passngr.appendChild(pssngrElement);
					//add Date of Birth element to the Passenger element
					pssngrElement = manifestDocument.createElement("DateOfBirth");
					pssngrElement.appendChild(manifestDocument.createTextNode(trav.getDOB()));
					passngr.appendChild(pssngrElement);
					//add gender element to the Passenger element
					pssngrElement = manifestDocument.createElement("Sex");
					pssngrElement.appendChild(manifestDocument.createTextNode(trav.getSex()));
					passngr.appendChild(pssngrElement);
					//add Document Number element to the Passenger element
					pssngrElement = manifestDocument.createElement("DocNumber");
					pssngrElement.appendChild(manifestDocument.createTextNode(trav.getDocNum()));
					passngr.appendChild(pssngrElement);
					//add Nationality element to the Passenger element
					pssngrElement = manifestDocument.createElement("Nationality");
					pssngrElement.appendChild(manifestDocument.createTextNode(trav.getNat()));
					passngr.appendChild(pssngrElement);
					//add Street element to the Passenger element
					pssngrElement = manifestDocument.createElement("Street");
					pssngrElement.appendChild(manifestDocument.createTextNode(trav.getAddStr()));
					passngr.appendChild(pssngrElement);
					//add City element to the Passenger element
					pssngrElement = manifestDocument.createElement("City");
					pssngrElement.appendChild(manifestDocument.createTextNode(trav.getAddCity()));
					passngr.appendChild(pssngrElement);
					//add State element to the Passenger element
					pssngrElement = manifestDocument.createElement("State");
					pssngrElement.appendChild(manifestDocument.createTextNode(trav.getAddStat()));
					passngr.appendChild(pssngrElement);
					//add Postal Code element to the Passenger element
					pssngrElement = manifestDocument.createElement("PostalCode");
					pssngrElement.appendChild(manifestDocument.createTextNode(trav.getAddPost()));
					passngr.appendChild(pssngrElement);
					//add Country element to the Passenger element
					pssngrElement = manifestDocument.createElement("Country");
					pssngrElement.appendChild(manifestDocument.createTextNode(trav.getAddCountry()));
					passngr.appendChild(pssngrElement);
					
				}
				
				//Writing the content to the manifest file
				TransformerFactory tFactory = TransformerFactory.newInstance();
				Transformer tform = tFactory.newTransformer();
				DOMSource source = new DOMSource(manifestDocument);
				StreamResult result = new StreamResult(new File(filename));
				tform.setOutputProperty(OutputKeys.INDENT, "yes");
				tform.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
				tform.transform(source, result);
				
				
			} catch (ParserConfigurationException | TransformerException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(WindowComponent.contentPane, "Unable to save the manifest XML document, due to: \n" + e.getMessage(), "PassNgR 1: Save Error", JOptionPane.ERROR_MESSAGE);
				//e.printStackTrace();
			}
		}
	}
	
	public class switchViewList implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub						
			if (manifestList.isEmpty()){
				WindowComponent.message.setText("There are no passengers loaded");				
			}else{
				genReport(manifestList);
				WindowComponent.message.setText(null);
				CardLayout clp = (CardLayout) WindowComponent.contentPane.getLayout();
				clp.next(WindowComponent.contentPane);				
			}			
		}
	}
	
	public class switchMainInput implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			DefaultTableModel model = (DefaultTableModel) WindowComponent.manifest.getModel();
			
			for(Integer rowCnt = model.getRowCount() - 1; rowCnt > manifestStart; rowCnt--){
				model.removeRow(rowCnt);
			}
			CardLayout clp = (CardLayout) WindowComponent.contentPane.getLayout();
			clp.previous(WindowComponent.contentPane);			
		}
	}
	
	public class manifestPrint implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub			
			WindowComponent.message.setText(null);
			if (manifestList.isEmpty()){
				WindowComponent.message.setForeground(Color.red);
				WindowComponent.message.setText("Unable to print as there are no passengers loaded");
			}else{
				Date today = new Date();
				String mydate = DateFormat.getDateInstance(DateFormat.LONG).format(today);				
				genReport(manifestList);
				MessageFormat manifestHeader = new MessageFormat("ANGUILLA FERRY SHUTTLE \n& CHARTER SERICES LIMITED");
				MessageFormat manifestFooter = new MessageFormat("I hereby declare that the above list is complete, and that the above" +
						" particulars are to the best of my knowledge, information and belief,\ntrue and correct in every particulars. \n Date: " +
						mydate + ".\tMaster: ________________________\n- {0} -");
				PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
				attributes.add(MediaSizeName.NA_LETTER);
				attributes.add(OrientationRequested.PORTRAIT);
				attributes.add(new MediaPrintableArea(0.2f, 0.2f, 8.0f, 10.2f, MediaSize.INCH));
				//WindowComponent.manifest.getpr
				try {
					WindowComponent.manifest.print(JTable.PrintMode.FIT_WIDTH, manifestHeader,manifestFooter,true,attributes,true);
				} catch (PrinterException | SecurityException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(WindowComponent.contentPane,  e1.getMessage(), "PassNgR 1: Print Error", JOptionPane.ERROR_MESSAGE);					
				}
			}
		}
		
	}
	
	public class manifestNext implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e){			
			WindowComponent.message.setText(null);
			
			if (manifestList.isEmpty()){
				String message = "<html>There are <b>no</b> passengers loaded in this manifest.</html>";
				WindowComponent.message.setForeground(Color.black);
				WindowComponent.message.setText(message);				
			}else if (manifestIndex < manifestList.size() - 1){
				int modelIndx;
				manifestIndex++;
				modelIndx = (int) manifestIndex;
				passenger p = manifestList.get(modelIndx);
				WindowComponent.lname.setText(p.getLname().trim());
				WindowComponent.fname.setText(p.getFname().trim());
				WindowComponent.sex.setText(p.getSex().trim());
				WindowComponent.dob.setText(p.getDOB().trim());
				WindowComponent.nation.setText(p.getNat().trim());
				WindowComponent.docNum.setText(p.getDocNum().trim());
				String address = compileAddress(p.getAddStr().trim());
				address = address.concat(compileAddress(p.getAddCity().trim()));
				address = address.concat(compileAddress(p.getAddStat().trim()));
				address = address.concat(compileAddress(p.getAddPost().trim()));
				address = address.concat(compileAddress(p.getAddCountry()));
				WindowComponent.address.setText(address.trim());
				Integer amt = manifestIndex + 1;
				WindowComponent.JTPssngrIndx.setText(amt.toString());
				amt = (Integer) manifestList.size();
				WindowComponent.lblPassngrAmt.setText("Total Number of Passengers: " + amt.toString());
				
			}else{
				String message = "<html>You are at the <b>end</b> of the passenger manifest</html>";
				Integer amt = (Integer) manifestList.size()+ 1;
				WindowComponent.JTPssngrIndx.setText(amt.toString());
				WindowComponent.message.setForeground(Color.black);
				WindowComponent.message.setText(message);
				manifestIndex = manifestList.size();
				WindowComponent.fname.setText(null);
				WindowComponent.lname.setText(null);
				WindowComponent.sex.setText(null);
				WindowComponent.dob.setText(null);
				WindowComponent.docNum.setText(null);
				WindowComponent.nation.setText(null);
				WindowComponent.address.setText(null);
			}						
		}
	}
	
	public class manifestPrevious implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e){
			WindowComponent.message.setText(null);
			
			if (manifestList.isEmpty()){
				String message = "<html>There are <b>no</b> passengers loaded in this manifest.</html>";
				WindowComponent.message.setForeground(Color.black);
				WindowComponent.message.setText(message);
			}else if (manifestIndex > 0){
				int modelIndx;
				manifestIndex--;
				modelIndx = (int) manifestIndex;
				passenger p = manifestList.get(modelIndx);
				WindowComponent.lname.setText(p.getLname().trim());
				WindowComponent.fname.setText(p.getFname().trim());
				WindowComponent.sex.setText(p.getSex().trim());
				WindowComponent.dob.setText(p.getDOB().trim());
				WindowComponent.nation.setText(p.getNat().trim());
				WindowComponent.docNum.setText(p.getDocNum().trim());
				String address = compileAddress(p.getAddStr().trim()); 
				address = address.concat(compileAddress(p.getAddCity().trim()));
				address = address.concat(compileAddress(p.getAddStat().trim()));
				address = address.concat(compileAddress(p.getAddPost().trim()));
				address = address.concat(compileAddress(p.getAddCountry()));
				WindowComponent.address.setText(address.trim());
				Integer amt = manifestIndex + 1;
				WindowComponent.JTPssngrIndx.setText(amt.toString());
				amt = (Integer) manifestList.size();
				WindowComponent.lblPassngrAmt.setText("Total Number of Passengers: " + amt.toString());
			}else{
				String message = "<html>You are at the <b>first</b> passenger of the manifest</html>";
				WindowComponent.message.setForeground(Color.black);
				WindowComponent.message.setText(message);
			}			
		}
	}
	
	public class manifestHome implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e){
			WindowComponent.message.setText(null);
			
			if (manifestList.isEmpty()){
				String message = "<html>There are <b>no</b> passengers loaded in this manifest.</html>";
				WindowComponent.message.setForeground(Color.black);
				WindowComponent.message.setText(message);
			}else{
				int modelIndx;
				manifestIndex = 0;
				modelIndx = (int) manifestIndex;
				passenger p = manifestList.get(modelIndx);
				WindowComponent.lname.setText(p.getLname().trim());
				WindowComponent.fname.setText(p.getFname().trim());
				WindowComponent.sex.setText(p.getSex().trim());
				WindowComponent.dob.setText(p.getDOB().trim());
				WindowComponent.nation.setText(p.getNat().trim());
				WindowComponent.docNum.setText(p.getDocNum().trim());
				String address = compileAddress(p.getAddStr().trim()); 
				address = address.concat(compileAddress(p.getAddCity().trim()));
				address = address.concat(compileAddress(p.getAddStat().trim()));
				address = address.concat(compileAddress(p.getAddPost().trim()));
				address = address.concat(compileAddress(p.getAddCountry()));
				WindowComponent.address.setText(address.trim());
				Integer amt = (Integer) manifestList.size();
				WindowComponent.JTPssngrIndx.setText("1");				
				WindowComponent.lblPassngrAmt.setText("Total Number of Passengers: " + amt.toString());
			}			
		}
	}
	
	public class manifestEnd implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e){
			WindowComponent.message.setText(null);
			
			if (manifestList.isEmpty()){
				String message = "<html>There are <b>no</b> passengers loaded in this manifest.</html>";
				WindowComponent.message.setForeground(Color.black);
				WindowComponent.message.setText(message);
			}else{
				int modelIndx;
				manifestIndex = manifestList.size() - 1;
				modelIndx = (int) manifestIndex;
				passenger p = manifestList.get(modelIndx);
				WindowComponent.lname.setText(p.getLname().trim());
				WindowComponent.fname.setText(p.getFname().trim());
				WindowComponent.sex.setText(p.getSex().trim());
				WindowComponent.dob.setText(p.getDOB().trim());
				WindowComponent.nation.setText(p.getNat().trim());
				WindowComponent.docNum.setText(p.getDocNum().trim());
				String address = compileAddress(p.getAddStr().trim()); 
				address = address.concat(compileAddress(p.getAddCity().trim()));
				address = address.concat(compileAddress(p.getAddStat().trim()));
				address = address.concat(compileAddress(p.getAddPost().trim()));
				address = address.concat(compileAddress(p.getAddCountry()));
				WindowComponent.address.setText(address.trim());
				Integer amt = (Integer) manifestList.size();
				WindowComponent.JTPssngrIndx.setText(amt.toString());
				WindowComponent.lblPassngrAmt.setText("Total Number of Passengers: " + amt.toString());
			}			
		}
	}
	
	public class passengerRemove implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e){
			WindowComponent.message.setText(null);
			Integer indx = 0;
			try{
				indx = Integer.parseInt(WindowComponent.JTPssngrIndx.getText());
			}catch(NumberFormatException nfe){
				JOptionPane.showMessageDialog(WindowComponent.contentPane, "Only an integer should be entered in this field.", "PassNgR 1: Error", JOptionPane.ERROR_MESSAGE);
			}
			if (manifestList.isEmpty()){
				String message = "<html>There are <b>no</b> passengers loaded in this manifest.</html>";
				WindowComponent.message.setForeground(Color.black);
				WindowComponent.message.setText(message);
			}else if(indx > manifestIndex){
				WindowComponent.message.setForeground(Color.red);				
				WindowComponent.message.setText("Please select a passenger to remove.");
			}else{
				int modelIndx;				
				modelIndx = (int) manifestIndex;
				manifestList.remove(modelIndx);
				
				if (manifestList.isEmpty()){
					WindowComponent.lname.setText(null);
					WindowComponent.fname.setText(null);
					WindowComponent.sex.setText(null);
					WindowComponent.dob.setText(null);
					WindowComponent.nation.setText(null);
					WindowComponent.docNum.setText(null);
					WindowComponent.address.setText(null);
					Integer amt = (Integer) manifestList.size();
					WindowComponent.JTPssngrIndx.setText(amt.toString());
					WindowComponent.lblPassngrAmt.setText("Total Number of Passengers: " + amt.toString());
				}else{					
					manifestIndex--;
					modelIndx = (int) manifestIndex;
					if (modelIndx < 0){
						manifestIndex = 0;
						modelIndx = (int) manifestIndex;
						passenger p = manifestList.get(modelIndx);
						WindowComponent.lname.setText(p.getLname().trim());
						WindowComponent.fname.setText(p.getFname().trim());
						WindowComponent.sex.setText(p.getSex().trim());
						WindowComponent.dob.setText(p.getDOB().trim());
						WindowComponent.nation.setText(p.getNat().trim());
						WindowComponent.docNum.setText(p.getDocNum().trim());
						String address = compileAddress(p.getAddStr().trim()); 
						address = address.concat(compileAddress(p.getAddCity().trim()));
						address = address.concat(compileAddress(p.getAddStat().trim()));
						address = address.concat(compileAddress(p.getAddPost().trim()));
						address = address.concat(compileAddress(p.getAddCountry()));
						WindowComponent.address.setText(address.trim());
						Integer amt = (Integer) manifestList.size();
						WindowComponent.JTPssngrIndx.setText("1");
						WindowComponent.lblPassngrAmt.setText("Total Number of Passengers: " + amt.toString());
					}else{					
						passenger p = manifestList.get(modelIndx);
						WindowComponent.lname.setText(p.getLname().trim());
						WindowComponent.fname.setText(p.getFname().trim());
						WindowComponent.sex.setText(p.getSex().trim());
						WindowComponent.dob.setText(p.getDOB().trim());
						WindowComponent.nation.setText(p.getNat().trim());
						WindowComponent.docNum.setText(p.getDocNum().trim());
						String address = compileAddress(p.getAddStr().trim()); 
						address = address.concat(compileAddress(p.getAddCity().trim()));
						address = address.concat(compileAddress(p.getAddStat().trim()));
						address = address.concat(compileAddress(p.getAddPost().trim()));
						address = address.concat(compileAddress(p.getAddCountry()));
						WindowComponent.address.setText(address.trim());
						Integer amt = manifestIndex + 1;
						WindowComponent.JTPssngrIndx.setText(amt.toString());
						amt = (Integer) manifestList.size();
						WindowComponent.lblPassngrAmt.setText("Total Number of Passengers: " + amt.toString());
					}
				}
			}		
		}
	}
	
	public class passengerClear implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			WindowComponent.message.setForeground(Color.black);
			WindowComponent.message.setText(null);
			int result = JOptionPane.showConfirmDialog(WindowComponent.contentPane, "This will clear all passengers from the passenger manifest.\n Do you wish to continue?", "PassNgR 1: Clear Manifest", 
					JOptionPane.YES_NO_OPTION);
			switch(result){
			case JOptionPane.YES_OPTION :
				manifestList.clear();
				WindowComponent.lname.setText(null);
				WindowComponent.fname.setText(null);
				WindowComponent.sex.setText(null);
				WindowComponent.dob.setText(null);
				WindowComponent.nation.setText(null);
				WindowComponent.docNum.setText(null);
				WindowComponent.address.setText(null);
				manifestIndex = -1;
				WindowComponent.JTPssngrIndx.setText("0");
				WindowComponent.lblPassngrAmt.setText("Total Number of Passengers: 0");
				break;
			default : break;
			}
		}
		
	}
	
	public class manifestOpen implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			WindowComponent.message.setText(null);
			JFileChooser mfile = new JFileChooser();
			int oDiag = mfile.showOpenDialog(WindowComponent.contentPane);
			
			if (oDiag == JFileChooser.APPROVE_OPTION && manifestList.isEmpty()){				
				File f = mfile.getSelectedFile();
				loadManifest(f, manifestList);					
			}else if (oDiag == JFileChooser.APPROVE_OPTION && !manifestList.isEmpty()){
				int result = JOptionPane.showConfirmDialog(WindowComponent.contentPane, "Passengers are loaded in this manifest.\n By continuing you will lose all passengers currently loaded.\n Do You wish to continue?", "PassNgR 1: Passengers Loaded", 
							JOptionPane.YES_NO_OPTION);
				switch(result){
					case JOptionPane.YES_OPTION :
						File f = mfile.getSelectedFile();
						loadManifest(f, manifestList);
						break;
					default : break;
				}
			}
		}
	}
	
	public class manifestSave implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			WindowComponent.message.setText(null);
			JFileChooser mfile = new JFileChooser(){
				/**
				 * 
				 */
				private static final long serialVersionUID = -3193482903992647661L;
				@Override
				public void approveSelection(){
					File f = getSelectedFile();
					if(f.exists() && getDialogType() == SAVE_DIALOG){
						int result = JOptionPane.showConfirmDialog(WindowComponent.contentPane, "The file exists. Do you want to overwrite?", "PassNgR 1: Existing File", 
								JOptionPane.YES_NO_CANCEL_OPTION);
						switch(result){
							case JOptionPane.YES_OPTION :
								super.approveSelection();
								return;
							case JOptionPane.NO_OPTION :
								return;
							case JOptionPane.CLOSED_OPTION :
								return;
							case JOptionPane.CANCEL_OPTION :
								cancelSelection();
								return;
						}
					}
					super.approveSelection();
				}
				
			};			
			int sDiag = mfile.showSaveDialog(WindowComponent.contentPane);			
			if(sDiag == JFileChooser.APPROVE_OPTION){
				String mFileName = mfile.getSelectedFile().getPath();				
				saveManifest(mFileName, manifestList);
			}
		}		
	}
	
	public class switchToManual implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			WindowComponent.message.setText(null);
			JCheckBoxMenuItem cb = (JCheckBoxMenuItem) e.getSource();
			WindowComponent.manual = cb.isSelected();
			
			if(WindowComponent.manual){
				WindowComponent.Title.setText("Passenger Information: Manual Mode");
				
			}else{
				WindowComponent.Title.setText("Passenger Information: Scan Mode");
			}
			
			WindowComponent.fname.setText(null);
			WindowComponent.lname.setText(null);
			WindowComponent.sex.setText(null);
			WindowComponent.dob.setText(null);
			WindowComponent.docNum.setText(null);
			WindowComponent.nation.setText(null);
			WindowComponent.address.setText(null);
			
			WindowComponent.scan.setEditable((WindowComponent.manual ? false : true));
			WindowComponent.fname.setEditable(WindowComponent.manual);
			WindowComponent.lname.setEditable(WindowComponent.manual);
			WindowComponent.sex.setEditable(WindowComponent.manual);
			WindowComponent.dob.setEditable(WindowComponent.manual);
			WindowComponent.nation.setEditable(WindowComponent.manual);
			WindowComponent.docNum.setEditable(WindowComponent.manual);
			WindowComponent.address.setEditable(WindowComponent.manual);
			WindowComponent.add.setEnabled(WindowComponent.manual);
			Integer amt = (Integer) ( manifestList.isEmpty() ? manifestList.size() : manifestList.size() + 1 );
			WindowComponent.JTPssngrIndx.setText(amt.toString());
			
		}
		
	}
	
	public class passengerAdd implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			WindowComponent.message.setText(null);
			Boolean err = false;
			String errText = "<html>";
			String input = WindowComponent.lname.getText();			
			if(input.length() < 1){
				err = true;
				errText = errText.concat("Passenger's last name is missing.<br>");
			}
			
			input = WindowComponent.fname.getText();
			if(input.length() < 1){
				err = true;
				errText = errText.concat("Passenger's  Forename(s) is\\are missing.<br>");
			}
			
			input = WindowComponent.dob.getText();
			if(input.length() < 1){
				err = true;
				errText = errText.concat("Passenger's Date of Birth is missing.<br>");
			}
			
			input = WindowComponent.docNum.getText();
			if(input.length() < 1){
				err = true;
				errText = errText.concat("Passenger's ID Document's number is missing.<br>");
			}
			
			errText = errText.concat("</html>");
			
			if(err){
				WindowComponent.message.setForeground(Color.red);
				WindowComponent.message.setText(errText);
			}else{
				passenger trav = new passenger();
				trav.setFname(WindowComponent.fname.getText().trim());
				trav.setLname(WindowComponent.lname.getText().trim());
				trav.setSex(WindowComponent.sex.getText().trim());
				trav.setDOB(WindowComponent.dob.getText().trim());
				trav.setDocNum(WindowComponent.docNum.getText().trim());
				trav.setNat(WindowComponent.nation.getText().trim());
				trav.setAddStr(WindowComponent.address.getText().trim());
				manifestList.add(trav);
				manifestIndex = manifestList.size();
				WindowComponent.fname.setText(null);
				WindowComponent.lname.setText(null);
				WindowComponent.sex.setText(null);
				WindowComponent.dob.setText(null);
				WindowComponent.docNum.setText(null);
				WindowComponent.nation.setText(null);
				WindowComponent.address.setText(null);
				WindowComponent.message.setForeground(Color.black);
				Integer amt = (Integer) manifestList.size() + 1;
				WindowComponent.JTPssngrIndx.setText(amt.toString());
				amt--;
				WindowComponent.lblPassngrAmt.setText("Total Number of Passengers: " + amt.toString());
				WindowComponent.message.setText("Passenger Added.");
			}
		}
		
	}
	
	public class jumpto implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == KeyEvent.VK_ENTER){
				JTextField goField = (JTextField) e.getSource();
				try{
					Integer indx = Integer.parseInt( goField.getText());
					indx--;
					if(indx > -1 && indx < manifestList.size()){
						WindowComponent.fname.setText(manifestList.get(indx).getFname().trim());
						WindowComponent.lname.setText(manifestList.get(indx).getLname().trim());
						WindowComponent.sex.setText(manifestList.get(indx).getSex().trim());
						WindowComponent.dob.setText(manifestList.get(indx).getDOB().trim());
						WindowComponent.nation.setText(manifestList.get(indx).getNat().trim());
						WindowComponent.docNum.setText(manifestList.get(indx).getDocNum().trim());
						String address = compileAddress(manifestList.get(indx).getAddStr().trim()); 
						address = address.concat(compileAddress(manifestList.get(indx).getAddCity().trim()));
						address = address.concat(compileAddress(manifestList.get(indx).getAddStat().trim()));
						address = address.concat(compileAddress(manifestList.get(indx).getAddPost().trim()));
						address = address.concat(compileAddress(manifestList.get(indx).getAddCountry()));
						WindowComponent.address.setText(address.trim());
					}else if(indx == manifestList.size()){
						WindowComponent.fname.setText(null);
						WindowComponent.lname.setText(null);
						WindowComponent.sex.setText(null);
						WindowComponent.dob.setText(null);
						WindowComponent.nation.setText(null);
						WindowComponent.docNum.setText(null);
						WindowComponent.address.setText(null);
					}else{
						JOptionPane.showMessageDialog(WindowComponent.contentPane, "Number is out of range", "PassNgR 1: Information", JOptionPane.INFORMATION_MESSAGE);
					}
				}catch(NumberFormatException ex){
					JOptionPane.showMessageDialog(WindowComponent.contentPane, "Only an integer should be entered in this field.", "PassNgR 1: Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}		
		
	}
}