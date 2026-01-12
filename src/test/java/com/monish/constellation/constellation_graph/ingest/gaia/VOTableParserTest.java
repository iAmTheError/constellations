package com.monish.constellation.constellation_graph.ingest.gaia;

import com.monish.constellation.constellation_graph.ingest.model.StarRow;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VOTableParserTest {
    private final VOTableParser parser = new VOTableParser();
    
    private static ByteArrayInputStream xml(String content) {
        return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    }

    private static List<StarRow> toList(Iterable<StarRow> iterable){
        List<StarRow> list  = new ArrayList<>();
        iterable.forEach(list::add);
        /*Above code is equal to 
        for(StarRow row : iterable){
            list.add(row);
        } */
        return list;
    }

    @Test
    public void testParseVOTableWtihUselessCrap() throws XMLStreamException {
        // Add test implementation here
        String votable = """
<?xml version="1.0" encoding="utf-8"?>
<!-- Produced with MAST VOTable encoder version 1.0 -->
<VOTABLE version="1.4" xmlns="http://www.ivoa.net/xml/VOTable/v1.3" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.ivoa.net/xml/VOTable/v1.3 http://www.ivoa.net/xml/VOTable/v1.3">
<RESOURCE type="results">
<INFO name="QUERY_STATUS" value="OK"/>
<INFO name="QUERY" value="SELECT source_id, ra, dec, phot_g_mean_mag
      FROM gaia_source
      WHERE phot_g_mean_mag &lt;= 6
      ORDER BY phot_g_mean_mag ASC"/>
<TABLE>
<FIELD name="source_id" datatype="long" ucd="meta.id">
<DESCRIPTION>source id</DESCRIPTION></FIELD>
<FIELD name="ra" datatype="double" unit="deg" ucd="pos.eq.ra;meta.main">
<DESCRIPTION>barycentric right ascension of the source in icrs at the reference epoch</DESCRIPTION></FIELD>
<FIELD name="dec" datatype="double" unit="deg" ucd="pos.eq.dec;meta.main">
<DESCRIPTION>barycentric declination of the source in icrs at the reference epoch</DESCRIPTION></FIELD>
<FIELD name="phot_g_mean_mag" datatype="float" unit="mag" ucd="phot.mag;stat.mean;em.opt">
<DESCRIPTION>g band mean mag</DESCRIPTION></FIELD>
<DATA>
<TABLEDATA>
<TR><TD>1576683529448755328</TD><TD>193.50817846782095</TD><TD>55.959784778923755</TD><TD>1.7316069602966309</TD></TR>
<TR><TD>6560604777055249536</TD><TD>332.05907779469464</TD><TD>-46.96161734131188</TD><TD>1.7732802629470825</TD></TR>
</TABLEDATA>
</DATA>
</TABLE>
</RESOURCE>
</VOTABLE>
""";
        List<StarRow> rows = toList(parser.parse(xml(votable)));

        assertEquals(2, rows.size());
        assertEquals(1576683529448755328L, rows.get(0).sourceId(),1e-9);
        assertEquals(193.50817846782095, rows.get(0).ra(),1e-9);
        assertEquals(55.959784778923755, rows.get(0).dec(),1e-9);
        assertEquals(1.7316069602966309, rows.get(0).gmag(),1e-9);
    }
    @Test 
    public void mapsWithFieldsSwitched() throws XMLStreamException {
                String votable = """
            <VOTABLE version="1.4">
              <RESOURCE type="results">
                <TABLE>
                  <FIELD name="ra" datatype="double"/>
                  <FIELD name="phot_g_mean_mag" datatype="double"/>
                  <FIELD name="source_id" datatype="long"/>
                  <FIELD name="dec" datatype="double"/>
                  <DATA><TABLEDATA>
                    <TR><TD>10.0</TD><TD>2.0</TD><TD>99</TD><TD>-5.0</TD></TR>
                  </TABLEDATA></DATA>
                </TABLE>
              </RESOURCE>
            </VOTABLE>
            """;
        List<StarRow> rows = toList(parser.parse(xml(votable)));
        assertEquals(1,rows.size());
        StarRow row = rows.get(0);
        assertEquals(99L, row.sourceId());
        assertEquals(10.0, row.ra(),1e-9);
        assertEquals(-5.0, row.dec(),1e-9);
        assertEquals(2.0, row.gmag(),1e-9);
    }

    @Test
    public void skipRowsWhenRequiredFieldMissing() throws XMLStreamException {
        String votable = """
            <VOTABLE version="1.4">
              <RESOURCE type="results">
                <TABLE>
                  <FIELD name="source_id" datatype="long"/>
                  <FIELD name="ra" datatype="double"/>
                  <FIELD name="dec" datatype="double"/>
                  <FIELD name="phot_g_mean_mag" datatype="double"/>
                  <DATA><TABLEDATA>
                    <TR><TD>100</TD><TD>10.0</TD><TD>-5.0</TD><TD>2.0</TD></TR>
                    <TR><TD></TD><TD>11.0</TD><TD>-6.0</TD><TD>3.0</TD></TR>
                    <TR><TD>102</TD><TD>12.0</TD><TD>-7.0</TD><TD>4.0</TD></TR>
                    <TR><TD>103</TD><TD></TD><TD>-8.0</TD><TD>5.0</TD></TR>
                  </TABLEDATA></DATA>
                </TABLE>
              </RESOURCE>
            </VOTABLE>
            """;
        List<StarRow> rows = toList(parser.parse(xml(votable)));
        assertEquals(2, rows.size());
        assertEquals(100L, rows.get(0).sourceId());
        assertEquals(102L, rows.get(1).sourceId());
    }
    @Test

    public void nullWhenFieldsLessThantotalTDs() throws XMLStreamException {
        String votable = """
            <VOTABLE version="1.4">
              <RESOURCE type="results">
                <TABLE>
                  <FIELD name="source_id" datatype="long"/>
                  <FIELD name="ra" datatype="double"/>
                  <FIELD name="phot_g_mean_mag" datatype="double"/>
                  <DATA><TABLEDATA>
                    <TR><TD>100</TD><TD>10.0</TD><TD>-5.0</TD><TD>2.0</TD></TR>
                    <TR><TD>101</TD><TD>11.0</TD><TD>-6.0</TD><TD>3.0</TD></TR>
                    <TR><TD>102</TD><TD>12.0</TD><TD>-7.0</TD><TD>4.0</TD></TR>
                  </TABLEDATA></DATA>
                </TABLE>
              </RESOURCE>
            </VOTABLE>
            """;
        List<StarRow> rows = toList(parser.parse(xml(votable)));
        assertEquals(0, rows.size());
    }

    @Test
    public void ignoresUnknownFields() throws XMLStreamException {
        String votable = """
            <VOTABLE version="1.4">
              <RESOURCE type="results">
                <TABLE>
                  <FIELD name="source_id"/>
                  <FIELD name="ra"/>
                  <FIELD name="dec"/>
                  <FIELD name="phot_g_mean_mag"/>
                  <FIELD name="some_extra_field"/>
                  <DATA><TABLEDATA>
                    <TR><TD>7</TD><TD>1</TD><TD>2</TD><TD>3</TD><TD>junk</TD></TR>
                  </TABLEDATA></DATA>
                </TABLE>
              </RESOURCE>
            </VOTABLE>
            """;
        List<StarRow> rows = toList(parser.parse(xml(votable)));
        assertEquals(1, rows.size());
        StarRow row = new StarRow(7L, 1.0, 2.0, 3.0);
        assertEquals(row, rows.get(0));
    }

    @Test
    public void throwsOnEmptyXML(){
        String votable = "";
        assertThrows(XMLStreamException.class, () -> {
            parser.parse(xml(votable));
        });
    }

    @Test
    public void nullOnWrongFormatData() throws XMLStreamException {
        String votable = """
            <VOTABLE version="1.4">
              <RESOURCE type="results">
                <TABLE>
                  <FIELD name="source_id" datatype="long"/>
                  <FIELD name="ra" datatype="double"/>
                  <FIELD name="dec" datatype="double"/>
                  <FIELD name="phot_g_mean_mag" datatype="double"/>
                  <DATA><TABLEDATA>
                    <TR><TD>not_a_number</TD><TD>10.0</TD><TD>-5.0</TD><TD>2.0</TD></TR>
                  </TABLEDATA></DATA>
                </TABLE>
              </RESOURCE>
            </VOTABLE>
            """;
        List<StarRow> rows = toList(parser.parse(xml(votable)));
        assertEquals(0, rows.size());
    }
}
