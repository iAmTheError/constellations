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
            StringBuilder tdBuffer = null;

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
                        tdBuffer = new StringBuilder();
                    }
                }
                if(event == XMLStreamConstants.CHARACTERS && inTableData && tdBuffer != null){
                    tdBuffer.append(reader.getText());
                }
                if(event == XMLStreamConstants.END_ELEMENT){
                    String name = reader.getLocalName();

                    if(inTableData &&"TD".equals(name)){
                        String cell = (tdBuffer ==null)? "": tdBuffer.toString().trim();
                        if(currentRow != null) currentRow.add(cell);
                        tdBuffer = null;
                    }

                    if(inTableData && "TR".equals(name)) {
                        if(currentRow != null && !fields.isEmpty() && currentRow.size() == fields.size()){
                            StarRow row = mapRow(fields,currentRow);
                            if(row !=null) rows.add(row);
                        }
                        currentRow = null;
                    }

                    if("TABLEDATA".equals(name)){
                        inTableData = false;
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
        try{
            switch(f){
                case "source_id": sourceId = Long.parseLong(v); break;
                case "ra": ra = Double.parseDouble(v); break;
                case "dec": dec = Double.parseDouble(v); break;
                case "phot_g_mean_mag": gmag = Double.parseDouble(v); break;
                default: break;
            }
        } catch(NumberFormatException e){
            return null;
        }
    }
    if(sourceId == null || ra == null || dec == null || gmag == null){
        return null;
    }
return new StarRow(sourceId,ra,dec,gmag);
    }
}

