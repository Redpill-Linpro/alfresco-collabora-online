package fr.jeci.collabora.alfresco;

import fr.jeci.collabora.alfresco.WopiDiscovery.DiscoveryAction;
import org.junit.Before;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class WopiDiscoveryTest {
	WopiDiscovery wopiDiscovery = null;

	@Before
	public void setUp() throws Exception {
		wopiDiscovery = new WopiDiscovery();
	}

	@Test
	public void testLoadDiscoveryXML() throws XMLStreamException, IOException {
		File discoveryFile = new File("src/test/resources/discovery_collabora_online.xml");
		FileInputStream in = new FileInputStream(discoveryFile);
		wopiDiscovery.loadDiscoveryXML(in);
		assertFalse(wopiDiscovery.getActions().isEmpty());
	}

	@Test
	public void testGetSrcURL() throws XMLStreamException, IOException {
		File discoveryFile = new File("src/test/resources/discovery_collabora_online.xml");
		FileInputStream in = new FileInputStream(discoveryFile);
		wopiDiscovery.loadDiscoveryXML(in);

		String urlsrc = wopiDiscovery.getSrcURL("application/vnd.ms-excel", "edit");

		assertEquals("http://localhost:9980/loleaflet/1430151/loleaflet.html?", urlsrc);

		urlsrc = wopiDiscovery.getSrcURL("application/pdf", "view_comment");
		assertEquals("http://localhost:9980/loleaflet/1430151/loleaflet.html?", urlsrc);
	}

	@Test
	public void testGetAction() throws XMLStreamException, IOException {
		File discoveryFile = new File("src/test/resources/discovery_collabora_online.xml");
		FileInputStream in = new FileInputStream(discoveryFile);
		wopiDiscovery.loadDiscoveryXML(in);

		List<DiscoveryAction> action = wopiDiscovery.getAction("ods");
		assertFalse(action.isEmpty());
		assertEquals("http://localhost:9980/loleaflet/1430151/loleaflet.html?", action.get(0).getUrlsrc());
		assertEquals("edit", action.get(0).getName());
		assertEquals("ods", action.get(0).getExt());

		action = wopiDiscovery.getAction("PDF");
		assertFalse(action.isEmpty());
		assertEquals("http://localhost:9980/loleaflet/1430151/loleaflet.html?", action.get(0).getUrlsrc());
		assertEquals("view_comment", action.get(0).getName());
		assertEquals("pdf", action.get(0).getExt());
	}

}
