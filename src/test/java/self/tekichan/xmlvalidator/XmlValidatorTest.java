package self.tekichan.xmlvalidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class XmlValidatorTest {

    private static final String VALID_XML_PATH = XmlValidatorTest.class.getClassLoader().getResource("valid.xml").getPath();
    private static final String VALID_XML_2_PATH = XmlValidatorTest.class.getClassLoader().getResource("valid2.xml").getPath();
    private static final String VALID_XML_NO_DTD_PATH = XmlValidatorTest.class.getClassLoader().getResource("valid_nodtd.xml").getPath();
    private static final String INVALID_XML_PATH = XmlValidatorTest.class.getClassLoader().getResource("invalid.xml").getPath();
    private static final String DTD_PATH = XmlValidatorTest.class.getClassLoader().getResource("test.dtd").getPath();

    private Document validDocument;

    @BeforeEach
    void setUp() throws IOException, SAXException {
        validDocument = XmlValidator.readAndValidateXML(VALID_XML_PATH, null);
    }

    @Test
    void testReadAndValidateXMLWithValidFile() {
        assertNotNull(validDocument, "Document should not be null for a valid XML file.");
    }

    @Test
    void testReadAndValidateXMLWithInvalidFile() {
        assertThrows(SAXException.class, () -> {
            XmlValidator.readAndValidateXML(INVALID_XML_PATH, null);
        }, "SAXException should be thrown for an invalid XML file.");
    }

    @Test
    void testReadAndValidateXMLWithExternalDTD() throws IOException, SAXException {
        Document documentWithDTD = XmlValidator.readAndValidateXML(VALID_XML_PATH, DTD_PATH);
        assertNotNull(documentWithDTD, "Document should not be null when validated with an external DTD.");

        Document documentWithFileDTD = XmlValidator.readAndValidateXML(VALID_XML_2_PATH, DTD_PATH);
        assertNotNull(documentWithFileDTD, "Document should not be null when validated with an external DTD.");

        Document documentWithNoDTD = XmlValidator.readAndValidateXML(VALID_XML_NO_DTD_PATH, DTD_PATH);
        assertNotNull(documentWithNoDTD, "Document should not be null when validated with an external DTD.");
    }

    @Test
    void testEvaluateXPathValidExpression() throws IOException, SAXException, XPathExpressionException {
        Document document = XmlValidator.readAndValidateXML(VALID_XML_PATH, null);
        NodeList nodeList = XmlValidator.evaluateXPath(document, "//bookstore/book/title");

        assertNotNull(nodeList, "NodeList should not be null for a valid XPath expression.");
        assertTrue(nodeList.getLength() > 0, "NodeList should contain elements for the given XPath.");
    }

    @Test
    void testEvaluateXPathInvalidExpression() throws XPathExpressionException {
        XPathExpressionException exception = assertThrows(XPathExpressionException.class, () -> {
            XmlValidator.evaluateXPath(validDocument, "invalid\\xpath");
        });
        assertTrue(exception.getMessage().contains("illegal"), "Exception should mention the invalid XPath expression.");
    }
}
