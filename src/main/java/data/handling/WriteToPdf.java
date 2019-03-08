package data.handling;

import org.apache.fop.apps.*;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;


public class WriteToPdf implements Callable<String> {
    @Override
    public String call() throws Exception {
        File xsltFile = new File("template.xsl");
        StreamSource xmlSource = new StreamSource(new File("users.xml"));
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        Fop fop = createfopFactory(fopFactory, foUserAgent, new FileOutputStream("users.pdf"));
        Transformer transformer = setUpXSLT(xsltFile);
        Result res = new SAXResult(fop.getDefaultHandler());
        generatePDF(xmlSource, transformer, res);
        return "";
    }

    private static Fop createfopFactory(FopFactory fopFactory, FOUserAgent foUserAgent, OutputStream out) throws FOPException {
        return fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
    }

    private static Transformer setUpXSLT(File xsltFile) throws TransformerConfigurationException {
        TransformerFactory factory = TransformerFactory.newInstance();
        return factory.newTransformer(new StreamSource(xsltFile));
    }

    private static void generatePDF(StreamSource xmlSource, Transformer transformer, Result res) throws TransformerException {
        transformer.transform(xmlSource, res);
    }

}
