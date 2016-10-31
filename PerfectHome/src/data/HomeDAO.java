package data;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import entities.Home;
import entities.HomeUser;
import entities.User;

@Transactional
public class HomeDAO {

	@PersistenceContext
	private EntityManager em;

	// Get All Homes
	public List<Home> index() {
		String query = "Select h from Home h";
		List<Home> homes = em.createQuery(query, Home.class).getResultList();

		return homes;
	}

	// Get Home By ID
	public Home show(int id) {
		return em.find(Home.class, id);
	}

	// Add New Home
	public Home create(Home home) {

		em.persist(home);
		em.flush();

		return home;
	}

	// Add New Home and HomeUser
	public User createHomeToPass(Home home, int id) {
		// Create New Home Home
		em.persist(home);
		em.flush();
		
		Home managedHome = em.find(Home.class, home.getId());
		User user = em.find(User.class, id);
		
		HomeUser hu = new HomeUser();
		
		hu.setHome(managedHome);
		hu.setUser(user);
		
		em.persist(user);		
		em.persist(hu);

		return user;
	}

	// Delete Home
	public void destroy(int id) {
		Home deleteHome = em.find(Home.class, id);

		em.remove(deleteHome);
	}

	public ZillowDTO zillowFirstCall(String zillowUrl) throws Exception {
		URL url = new URL(zillowUrl);
		URLConnection conn = url.openConnection();

		Document doc = parseXML(conn.getInputStream());
		NodeList descNode = doc.getElementsByTagName("result");

		ZillowDTO zillow = new ZillowDTO();

		for (int i = 0; i < descNode.getLength(); i++) {
			// Zillow ID
			if (descNode.item(i).getFirstChild().toString().contains("zpid")) {
				zillow.setZillowId(Integer.parseInt(descNode.item(i).getFirstChild().getTextContent()));
			}
			// Street Address
			if (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild().toString()
					.contains("street")) {

				zillow.setStreet(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild()
						.getTextContent());
			}
			// Zip Code
			if (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild().getNextSibling()
					.toString().contains("zipcode")) {

				zillow.setZip(Integer.parseInt((descNode.item(i).getFirstChild().getNextSibling().getNextSibling()
						.getFirstChild().getNextSibling().getTextContent())));
			}
			// City
			if ((descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild().getNextSibling()
					.getNextSibling().toString().contains("city"))) {

				zillow.setCity(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild()
						.getNextSibling().getNextSibling().getTextContent());
			}
			// State
			if (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild().getNextSibling()
					.getNextSibling().getNextSibling().toString().contains("state")) {

				zillow.setState(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild()
						.getNextSibling().getNextSibling().getNextSibling().getTextContent());
			}

			// Use Code
			if (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
					.toString().contains("useCode")) {

				zillow.setUseCode(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
						.getNextSibling().getTextContent());
			}

			// Year Built
			if (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
					.getNextSibling().getNextSibling().getNextSibling().toString().contains("yearBuilt")) {

				zillow.setYearBuilt(Integer
						.parseInt(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
								.getNextSibling().getNextSibling().getNextSibling().getNextSibling().getTextContent()));
			}

			// Total Lot Square Foot
			if (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
					.getNextSibling().getNextSibling().getNextSibling().getNextSibling().toString()
					.contains("lotSizeSqFt")) {

				zillow.setTotalSqFoot(Integer.parseInt(descNode.item(i).getFirstChild().getNextSibling()
						.getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
						.getNextSibling().getNextSibling().getTextContent()));
			}

			// Finished Square Foot
			if (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
					.getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling().toString()
					.contains("finishedSqFt")) {

				zillow.setFinishedSqFoot(Integer.parseInt(descNode.item(i).getFirstChild().getNextSibling()
						.getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
						.getNextSibling().getNextSibling().getNextSibling().getTextContent()));
			}

			// Bathrooms
			if (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
					.getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
					.getNextSibling().toString().contains("bathroom")) {

				zillow.setBathrooms(Double.parseDouble(descNode.item(i).getFirstChild().getNextSibling()
						.getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
						.getNextSibling().getNextSibling().getNextSibling().getNextSibling().getTextContent()));
			}

			// Bedrooms
			if (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
					.getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
					.getNextSibling().getNextSibling().toString().contains("bedroom")) {

				zillow.setBedrooms(Integer.parseInt(descNode.item(i).getFirstChild().getNextSibling().getNextSibling()
						.getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
						.getNextSibling().getNextSibling().getNextSibling().getNextSibling().getTextContent()));
			}
		}

		System.out.println(zillow);

		zillow = zillowSecondCall(zillow);

		return zillow;
	}

	public ZillowDTO zillowSecondCall(ZillowDTO zillow) throws Exception {
		URL url = new URL(
				"http://www.zillow.com/webservice/GetUpdatedPropertyDetails.htm?zws-id=X1-ZWz1fhzcpm7ymj_4zss3&zpid="
						+ zillow.getZillowId());
		System.out.println(url);
		URLConnection conn = url.openConnection();

		Document doc = parseXML(conn.getInputStream());

		// Set Address if Jumping to Second Call
		NodeList addressNode = doc.getElementsByTagName("address");

		for (int i = 0; i < addressNode.getLength(); i++) {

			if (addressNode.item(i).getFirstChild().toString().contains("street") && zillow.getStreet() == null) {
				System.out.println("in set street");
				zillow.setStreet(addressNode.item(i).getFirstChild().getTextContent());
			}

			if (addressNode.item(i).getFirstChild().getNextSibling().toString().contains("zipcode")
					&& zillow.getZip() == 0) {
				System.out.println("in set zip");
				zillow.setZip(Integer.parseInt(addressNode.item(i).getFirstChild().getNextSibling().getTextContent()));
			}

			if (addressNode.item(i).getFirstChild().getNextSibling().getNextSibling().toString().contains("city")
					&& zillow.getCity() == null) {
				System.out.println("in set city");
				zillow.setCity(addressNode.item(i).getFirstChild().getNextSibling().getNextSibling().getTextContent());
			}

			if (addressNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().toString()
					.contains("state") && zillow.getState() == null) {
				System.out.println("in set state");
				zillow.setState(addressNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
						.getTextContent());
			}

		}

		NodeList imageNode = doc.getElementsByTagName("images");

		for (int i = 0; i < imageNode.getLength(); i++) {
			// Image URL
			if (imageNode.item(i).getFirstChild().getNextSibling().getFirstChild().toString().contains("url")) {
				zillow.setImageUrl(imageNode.item(i).getFirstChild().getNextSibling().getFirstChild().getTextContent());
			}
		}

		NodeList editedFactsNode = doc.getElementsByTagName("editedFacts");

		for (int i = 0; i < editedFactsNode.getLength(); i++) {
			// Use Code
			if (editedFactsNode.item(i).getFirstChild().toString().contains("useCode") && zillow.getUseCode() == null) {
				zillow.setUseCode(editedFactsNode.item(i).getFirstChild().getTextContent());
			}
			// Bedrooms
			if (editedFactsNode.item(i).getFirstChild().getNextSibling().toString().contains("bedroom")
					&& zillow.getBedrooms() == 0) {
				zillow.setBedrooms(
						Integer.parseInt(editedFactsNode.item(i).getFirstChild().getNextSibling().getTextContent()));
			}
			// Bathrooms
			if (editedFactsNode.item(i).getFirstChild().getNextSibling().getNextSibling().toString()
					.contains("bathroom") && zillow.getBathrooms() == 0.0) {
				zillow.setBathrooms(Double.parseDouble(
						editedFactsNode.item(i).getFirstChild().getNextSibling().getNextSibling().getTextContent()));
			}
			// Finished Sq Foot
			if (editedFactsNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().toString()
					.contains("finishedSqFt") && zillow.getFinishedSqFoot() == 0) {
				zillow.setFinishedSqFoot(Integer.parseInt(editedFactsNode.item(i).getFirstChild().getNextSibling()
						.getNextSibling().getNextSibling().getTextContent()));
			}
			// Total Sq Foot
			if (editedFactsNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
					.getNextSibling().toString().contains("lotSizeSqFt") && zillow.getTotalSqFoot() == 0) {
				zillow.setTotalSqFoot(Integer.parseInt(editedFactsNode.item(i).getFirstChild().getNextSibling()
						.getNextSibling().getNextSibling().getNextSibling().getTextContent()));
			}
			// Year Built
			if (editedFactsNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
					.getNextSibling().getNextSibling().toString().contains("yearBuilt") && zillow.getYearBuilt() == 0) {
				zillow.setYearBuilt(Integer.parseInt(editedFactsNode.item(i).getFirstChild().getNextSibling()
						.getNextSibling().getNextSibling().getNextSibling().getNextSibling().getTextContent()));
			}
			// Number of Floors
			if (editedFactsNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
					.getNextSibling().getNextSibling().getNextSibling().getNextSibling().toString()
					.contains("numFloors")) {
				zillow.setFloors(Integer.parseInt(
						editedFactsNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
								.getNextSibling().getNextSibling().getNextSibling().getNextSibling().getTextContent()));
			}
			// Finished Basement
			if (editedFactsNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
					.getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling().toString()
					.contains("basement")) {
				zillow.setFinishedBasement(editedFactsNode.item(i).getFirstChild().getNextSibling().getNextSibling()
						.getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
						.getNextSibling().getTextContent());
			}

		}

		System.out.println("Zillow Object Printout in Second Call");
		System.out.println(zillow);

		return zillow;
	}

	// public ZillowDTO zillow(String zillowUrl) throws Exception {
	// URL url = new URL(zillowUrl);
	// System.out.println("In DAO before xml " + url);
	// URLConnection conn = url.openConnection();
	//
	// Document doc = parseXML(conn.getInputStream());
	// NodeList descNode = doc.getElementsByTagName("result");
	//
	// ZillowDTO zillow = new ZillowDTO();
	//
	// for (int i = 0; i < descNode.getLength(); i++) {
	// System.out.println("*************************");
	// // Zillow ID
	// System.out.println(descNode.item(i).getFirstChild());
	// if (descNode.item(i).getFirstChild().toString().contains("zpid")) {
	// System.out.println(descNode.item(i).getFirstChild().getTextContent());
	// zillow.setZillowId(Integer.parseInt(descNode.item(i).getFirstChild().getTextContent()));
	// }
	//
	// // Street Address
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild());
	// if
	// (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild().toString()
	// .contains("street")) {
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild()
	// .getTextContent());
	// zillow.setStreet(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild()
	// .getTextContent());
	// }
	//
	// // Zip Code
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild()
	// .getNextSibling());
	// if
	// (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild().getNextSibling()
	// .toString().contains("zipcode")) {
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild()
	// .getNextSibling().getTextContent());
	// zillow.setZip(Integer.parseInt((descNode.item(i).getFirstChild().getNextSibling().getNextSibling()
	// .getFirstChild().getNextSibling().getTextContent())));
	// }
	//
	// // City
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild()
	// .getNextSibling().getNextSibling());
	// if
	// ((descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild().getNextSibling()
	// .getNextSibling().toString().contains("city"))) {
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild()
	// .getNextSibling().getNextSibling().getTextContent());
	// zillow.setCity(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild()
	// .getNextSibling().getNextSibling().getTextContent());
	// }
	//
	// // State
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild()
	// .getNextSibling().getNextSibling().getNextSibling());
	// if
	// (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild().getNextSibling()
	// .getNextSibling().getNextSibling().toString().contains("state")) {
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild()
	// .getNextSibling().getNextSibling().getNextSibling().getTextContent());
	// zillow.setState(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getFirstChild()
	// .getNextSibling().getNextSibling().getNextSibling().getTextContent());
	// }
	//
	// // Use Code
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling());
	// if
	// (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .toString().contains("useCode")) {
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getTextContent());
	// zillow.setUseCode(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getTextContent());
	// }
	//
	// // Year Built
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling());
	// if
	// (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().toString().contains("yearBuilt"))
	// {
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getTextContent());
	// zillow.setYearBuilt(Integer
	// .parseInt(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getTextContent()));
	// }
	//
	// // Total Lot Square Foot
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling());
	// if
	// (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().toString()
	// .contains("lotSizeSqFt")) {
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getTextContent());
	// zillow.setTotalSqFoot(Integer.parseInt(descNode.item(i).getFirstChild().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getTextContent()));
	// }
	//
	// // Finished Square Foot
	// System.out.println(
	// descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling());
	// if
	// (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling().toString()
	// .contains("finishedSqFt")) {
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getTextContent());
	// zillow.setFinishedSqFoot(Integer.parseInt(descNode.item(i).getFirstChild().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getTextContent()));
	// }
	//
	// // Bathrooms
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling());
	// if
	// (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().toString().contains("bathroom")) {
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getTextContent());
	// zillow.setBathrooms(Double.parseDouble(descNode.item(i).getFirstChild().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getTextContent()));
	// }
	//
	// // Bedrooms
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling());
	// if
	// (descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().toString().contains("bedroom")) {
	// System.out.println(descNode.item(i).getFirstChild().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getTextContent());
	// zillow.setBedrooms(Integer.parseInt(descNode.item(i).getFirstChild().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling()
	// .getNextSibling().getNextSibling().getNextSibling().getNextSibling().getTextContent()));
	// }
	// System.out.println("*************************");
	// }
	//
	// System.out.println(zillow);
	//
	// return zillow;
	//
	// }

	public Document parseXML(InputStream s) throws Exception {
		DocumentBuilderFactory dbf = null;
		DocumentBuilder db = null;
		Document doc = null;

		dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();

		doc = db.parse(s);
		System.out.println("DOC");
		System.out.println(doc);

		return doc;
	}
}
