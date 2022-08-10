package entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utility.XMLUtils;

public class Distributore {

	private int id;
	private String marca;
	private String modello;
	private int numMaxProdotti;
	private int codice_qrt;
	private String indirizzo;
	private ArrayList<Disponibilita> disponibilita;
	private ArrayList<Erogazione> erogazioni;

	public Distributore() {};
	
	public Distributore() {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("distributore.properties");
			prop.load(input);
			this.id=Integer.parseInt(prop.getProperty("id").toString());
			this.marca=prop.getProperty("marca");
			this.modello=prop.getProperty("modello");
			this.numMaxProdotti=Integer.parseInt(prop.get("num_max").toString());

		} catch(IOException e) {
			e.printStackTrace();
		}

		Document doc = XMLUtils.openXML("Distributore.xml");
		NodeList list = doc.getElementsByTagName("prodotto");
		this.disponibilita=new ArrayList<Disponibilita>();
		for (int i=0; i<list.getLength(); i++) {
			Disponibilita disp = new Disponibilita();
			Node current = list.item(i);
			if (current.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) list.item(i);
				disp.setProdotto(new Prodotto(Integer.parseInt(current.getAttributes().getNamedItem("id").getNodeValue())));
				disp.setQuantita(Integer.parseInt(e.getElementsByTagName("quantita").item(0).getTextContent()));
				this.disponibilita.add(disp);
			}
		}
		this.erogazioni=new ArrayList<Erogazione>();
		updateErogazioni();

	}
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

	public int getNumMaxProdotti() {
		return numMaxProdotti;
	}

	public void setNumMaxProdotti(int numMaxProdotti) {
		this.numMaxProdotti = numMaxProdotti;
	}
	
	public ArrayList<Erogazione> getErogazioni() {
		return this.erogazioni;
	}

	public ArrayList<Disponibilita> getDisponibilita() {
		return this.disponibilita;
	}

	public void setDisponibilita(ArrayList<Disponibilita> disp) {
		this.disponibilita = disp;
	}

	public void setErogazioni(ArrayList<Erogazione> erog) {
		this.erogazioni = erog;
	}
	
	public int getCodice_qrt() {
		return codice_qrt;
	}

	public void setCodice_qrt(int codice_qrt) {
		this.codice_qrt = codice_qrt;
	}
	
	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	
	public void updateErogazioni() {
		
		Document doc = XMLUtils.openXML("Erogazioni.xml");
		NodeList list = doc.getElementsByTagName("erogazione");
		for (int i=0; i<list.getLength(); i++) {
			Node current = list.item(i);
			if (current.getNodeType() == Node.ELEMENT_NODE) {
				Erogazione erog = new Erogazione();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Element e = (Element) list.item(i);
				Prodotto p = new Prodotto(Integer.parseInt(e.getElementsByTagName("id").item(0).getTextContent()));
				try {
					erog.setData(sdf.parse(e.getElementsByTagName("ora").item(0).getTextContent()));
					erog.setProdotto(p);
				} catch (DOMException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.erogazioni.add(erog);
			}
		}
	}
	
	public void updateDisponibilita() {
		
		this.disponibilita.clear();
		Document doc = XMLUtils.openXML("Distributore.xml");
		NodeList list = doc.getElementsByTagName("prodotto");
		for (int i=0; i<list.getLength(); i++) {
			Node current = list.item(i);
			if (current.getNodeType() == Node.ELEMENT_NODE) {
				Disponibilita disp = new Disponibilita();
				Element e = (Element) list.item(i);
				Prodotto p = new Prodotto(Integer.parseInt(e.getAttribute("id").toString()));
				try {
					disp.setProdotto(p);
					disp.setQuantita(Integer.parseInt(e.getElementsByTagName("quantita").item(0).getTextContent()));
				} catch (DOMException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.disponibilita.add(disp);
			}
		}
	}
	
	public void cleanErogazioni() {
		File fileToDelete = new File("Erogazioni.xml");
		this.erogazioni.clear();
		fileToDelete.delete();
	}

}
