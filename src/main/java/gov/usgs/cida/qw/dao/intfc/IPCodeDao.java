package gov.usgs.cida.qw.dao.intfc;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public interface IPCodeDao {

    /** 
     * Gets the a sorted list of pcodes from the database.
     * @return the list of sorted pcodes.
     */
    List<LinkedHashMap<String, Object>> getRows();

    /** 
     * Gets the latest update date from all of the rows in the pcode tables.
     * @return the most recent pcode data modification date.
     */
    Date getLastModified();

}
