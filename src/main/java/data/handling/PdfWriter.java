package data.handling;

import com.thoughtworks.xstream.XStream;
import org.apache.fop.apps.*;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.List;
import java.util.logging.Logger;


public class PdfWriter {
    private static final Logger LOGGER = Logger.getLogger(PdfWriter.class.getName());
    private final String prefix;
    private final String folderPath;

    public PdfWriter(String prefix, String folderPath) {
        this.prefix = prefix;
        this.folderPath = folderPath;
    }


    public void print(StreamSource xmlSource, int pageNumber) throws Exception {
        File xsltFile = new File("template.xsl");
        //StreamSource xmlSource = new StreamSource(new File("users.xml"));
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        Fop fop = createfopFactory(fopFactory, foUserAgent, new FileOutputStream(folderPath + File.separator + prefix + pageNumber + ".pdf"));
        Transformer transformer = setUpXSLT(xsltFile);
        Result res = new SAXResult(fop.getDefaultHandler());
        generatePDF(xmlSource, transformer, res);
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

    public void print(List<List<String>> rows, int pageNumber) {
        XStream xStream = new XStream();
        xStream.alias("row", String.class);
        xStream.alias("data", Data.class);
        xStream.addImplicitCollection(Data.class, "stringList");
        Data data = new Data(rows.get(0));         //FIXME : Yyeruva : send each and every row, Create a new Thread?
        OutputStream streamSource = new ByteArrayOutputStream();
        xStream.toXML(data, streamSource);
        try {
            print(new StreamSource(new ByteArrayInputStream(((ByteArrayOutputStream) streamSource).toByteArray())), pageNumber);  //Invoke print
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
