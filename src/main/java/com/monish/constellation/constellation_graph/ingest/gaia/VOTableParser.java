package com.monish.constellation.constellation_graph.ingest.gaia;
import com.monish.constellation.constellation_graph.ingest.model.StarRow;
import javax.xml.stream.*;
import java.io.InputStream;
import java.util.*;

public class VOTableParser {
        private final XMLInputFactory factory = XMLInputFactory.newFactory();   
        
        public Iterable<StarRow> parse(InputStream in) throws XMLStreamException {
            XMLStreamReader reader = factory.createXMLStreamReader(in);

            //Reading field names, which are the columns of the table
            List<String> fields  = new ArrayList<>();
            boolean inTableData = false;

            //temporaryily converting each row to a list
            //later convert to stream
            List<StarRow> rows = new ArrayList<>();

            List<String> currentRow = null;
            boolean inTD = false;

            

        }
}
