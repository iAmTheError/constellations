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

            while(reader.hasNext()) {
                int event = reader.next();

                if(event == XMLStreamConstants.START_ELEMENT){
                    String name  = reader.getLocalName();
                    
                    if("FIELD".equals(name)){
                        String fieldname  = reader.getAttributeValue(null,"name");
                        fields.add(fieldname);
                    }

                    if("TABLEDATA".equals(name)){
                        inTableData = true;
                    }
                    
                    if(inTableData && "TR".equals(name)){
                        currentRow = new ArrayList<>();
                    }
                    
                    if(inTableData && "TD".equals(name)){
                        inTD = true;
                    }
                }
                if(event == XMLStreamConstants.CHARACTERS && inTableData && inTD){
                    String text = reader.getText().trim();
                    if(currentRow != null && !text.isEmpty()){
                        currentRow.add(text);
                    }
                }
                if(event == XMLStreamConstants.END_ELEMENT){
                    String name = reader.getLocalName();

                    if("TD".equals(name)){
                        inTD = false;
                    }

                    if(inTableData && "TR".equals(name)) {
                        if(currentRow != null && !fields.isEmpty() && currentRow.size() == fields.size()){
                            StarRow row = mapRow(fields,currentRow);
                            if(row !=null) rows.add(row);
                        }
                        currentRow = null;
                    }
                }
    }
    return rows;
}

private StarRow mapRow(List<String> fields, List<String> values){
    Long sourceId = null;
    Double ra = null,dec = null, gmag = null;

    for(int i=0; i<fields.size();i++){
        String f = fields.get(i);
        String v = values.get(i);

        if(v==null || v.isBlank()) continue;
        switch(f){
            case "source_id" -> sourceId = Long.parseLong(v);
            case "ra" -> ra = Double.parseDouble(v);
            case "dec" -> dec = Double.parseDouble(v);
            case "phot_g_mean_mag" -> gmag = Double.parseDouble(v);
            default -> {}
        }
    }
    if(sourceId == null || ra == null || dec == null || gmag == null){
        return null;
    }
return new StarRow(sourceId,ra,dec,gmag);
    }
}

