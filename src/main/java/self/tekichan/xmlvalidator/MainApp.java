package self.tekichan.xmlvalidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * MainApp is the entry point for the XML validator application.
 * <p>
 * This application validates an XML file against an optional external DTD and evaluates
 * an optional XPath expression on the XML document. It takes command-line arguments to
 * specify the XML file path, the DTD file path (optional), and the XPath expression (optional).
 * If no DTD file path is provided, the internal DTD of the XML will be used for validation.
 * The XPath expression, if provided, will be evaluated against the XML document and the results will be printed.
 * <p>
 * Usage:
 * <pre>
 * java MainApp &lt;XML file path&gt; [&lt;DTD file path&gt;] [&lt;XPath expression&gt;]
 * </pre>
 * <p>
 * Example:
 * <pre>
 * java MainApp /path/to/file.xml /path/to/file.dtd "//book/title"
 * </pre>
 * This will validate the XML file at the given path, using the DTD if provided, and print out
 * the titles of all books in the XML document that match the provided XPath expression.
 * </p>
 *
 * @author Teki Chan
 * @see XmlValidator
 */
public class MainApp {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    /**
     * The main method of the application. It validates the XML file, optionally validates it
     * against a specified DTD, and evaluates an optional XPath expression.
     * <p>
     * Command-line arguments are expected as follows:
     * <ul>
     *     <li>args[0]: The path to the XML file to validate (required).</li>
     *     <li>args[1]: The path to the DTD file for external validation (optional).</li>
     *     <li>args[2]: The XPath expression to evaluate on the XML document (optional).</li>
     * </ul>
     * </p>
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java MainApp <XML file path> [DTD file path] [XPath expression]");
            return;
        }

        String xmlFilePath = args[0];
        String dtdFilePath = (args.length > 1 && !"null".equalsIgnoreCase(args[1])) ? args[1] : null;
        String xpathExpr = args.length > 2 ? args[2] : null;

        try {
            if (dtdFilePath == null) {
                logger.info("Validation using embedded DTD");
            }
            // Validate the XML document
            Document document = XmlValidator.readAndValidateXML(xmlFilePath, dtdFilePath);
            System.out.println("XML validation successful.");

            // If XPath expression is provided, evaluate it
            if (xpathExpr != null) {
                NodeList nodes = XmlValidator.evaluateXPath(document, xpathExpr);
                System.out.println("XPath Result:");
                for (int i = 0; i < nodes.getLength(); i++) {
                    System.out.println(nodes.item(i).getTextContent());
                }
            }
        } catch (Exception e) {
            // Handle errors and log them
            System.err.println("Error: " + e.getMessage());
            logger.error("Application error", e);
        }
    }
}
