package self.tekichan.xmlvalidator;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainAppTest {

    private static final String VALID_XML_PATH = MainAppTest.class.getClassLoader().getResource("valid.xml").getPath();
    private static final String INVALID_XML_PATH = MainAppTest.class.getClassLoader().getResource("invalid.xml").getPath();
    private static final String DTD_PATH = MainAppTest.class.getClassLoader().getResource("test.dtd").getPath();

    @Test
    void testMainWithValidXmlAndDtd() throws Exception {
        String output = SystemLambda.tapSystemOut(() -> {
            MainApp.main(new String[]{VALID_XML_PATH, DTD_PATH});
        });

        assertTrue(output.contains("XML validation successful."), "Expected XML validation success message.");
    }

    @Test
    void testMainWithValidXmlDtdAndXPath() throws Exception {
        String output = SystemLambda.tapSystemOut(() -> {
            MainApp.main(new String[]{VALID_XML_PATH, DTD_PATH, "//bookstore/book/title"});
        });

        assertTrue(output.contains("XML validation successful."), "Expected XML validation success message.");
        assertTrue(output.contains("XPath Result:"), "Expected XPath result message.");
    }

    @Test
    void testMainWithInvalidXml() throws Exception {
        String output = SystemLambda.tapSystemErr(() -> {
            MainApp.main(new String[]{INVALID_XML_PATH});
        });

        assertTrue(output.contains("Error"), "Expected an error message for invalid XML.");
    }

    @Test
    void testMainWithMissingArguments() throws Exception {
        String output = SystemLambda.tapSystemErr(() -> {
            MainApp.main(new String[]{});
        });

        assertTrue(output.contains("Usage:"), "Expected usage message when no arguments are provided.");
    }
}
