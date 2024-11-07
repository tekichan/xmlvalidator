package self.tekichan.xmlvalidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * XmlValidator provides functionality to validate an XML document against an external DTD
 * and evaluate XPath expressions on the XML document.
 * <p>
 * The class includes methods for reading and validating XML files with optional DTD validation,
 * and for evaluating XPath expressions to extract specific nodes from the XML document.
 * </p>
 * @author Teki Chan
 */
public class XmlValidator {
    private static final Logger logger = LoggerFactory.getLogger(XmlValidator.class);

    /**
     * Reads and validates an XML file against an optional external DTD.
     *
     * @param xmlFilePath The path to the XML file to validate.
     * @param dtdFilePath The path to the DTD file for external validation (nullable).
     * @return A Document object representing the parsed and validated XML file.
     * @throws SAXException If a validation error occurs during XML parsing.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public static Document readAndValidateXML(String xmlFilePath, String dtdFilePath) throws SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);

        try {
            // Create a DocumentBuilder instance
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Set error handler to handle validation errors
            builder.setErrorHandler(new ErrorHandler());

            // Parse the XML file with the DTD
            Document document;
            if (dtdFilePath == null) {
                // If no DTD file is provided, parse XML with internal DTD if available
                document = builder.parse(new File(xmlFilePath));
            } else {
                StringWriter sw = transform(xmlFilePath, dtdFilePath);
                document = builder.parse(new InputSource(new StringReader(sw.toString())));
            }

            return document;  // Return parsed document after validation
        } catch (SAXParseException saxe) {
            logger.error("Validation error at {}:{}", saxe.getLineNumber(), saxe.getColumnNumber(), saxe);
            throw saxe;
        } catch (ParserConfigurationException pce) {
            logger.error("Parser configuration error", pce);
            throw new SAXException("Parser configuration error", pce);
        } catch (Exception e) {
            logger.error("Error", e);
            throw new SAXException("Error", e);
        }
    }

    /**
     * Transform Source XML DOCTYPE SYSTEM by the given DTD
     * @param srcXml    Source XML Path
     * @param newDtd    Given DTD Path
     * @return  String Writer which can provides the new content of the XML
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IOException
     * @throws SAXException
     */
    private static StringWriter transform(String srcXml, String newDtd) throws ParserConfigurationException, TransformerException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        DocumentBuilder db = factory.newDocumentBuilder();
        //parse file into DOM
        Document doc = db.parse(new File(srcXml));
        DOMSource source = new DOMSource(doc);
        //now use a transformer to add the DTD element
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, newDtd);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        return writer;
    }

    /**
     * Evaluates an XPath expression on the given XML document and returns the resulting nodes.
     *
     * @param document The XML document to evaluate the XPath expression on.
     * @param xpathExpr The XPath expression to evaluate.
     * @return A NodeList containing the nodes that match the XPath expression.
     * @throws XPathExpressionException If the XPath expression cannot be compiled or evaluated.
     */
    public static NodeList evaluateXPath(Document document, String xpathExpr) throws XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression expression = xpath.compile(xpathExpr);

        return (NodeList) expression.evaluate(document, XPathConstants.NODESET);
    }

    /**
     * Custom error handler to handle validation errors during XML parsing.
     */
    private static class ErrorHandler extends DefaultHandler {
        @Override
        public void error(SAXParseException saxe) throws SAXException {
            throw saxe;
        }

        @Override
        public void fatalError(SAXParseException saxe) throws SAXException {
            throw saxe;
        }

        @Override
        public void warning(SAXParseException saxe) throws SAXException {
            logger.warn("Warning message", saxe);
            throw saxe; // Optionally throw if you want to handle warnings as errors
        }
    }
}