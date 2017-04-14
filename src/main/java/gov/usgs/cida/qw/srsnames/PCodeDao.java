package gov.usgs.cida.qw.srsnames;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;

public class PCodeDao extends SqlSessionDaoSupport {

	/** 
	 * Gets the a sorted list of pcodes from the database.
	 * @return the list of sorted pcodes.
	 */
	public List<LinkedHashMap<String, Object>> getRows() {
		return getSqlSession().selectList("srsnames.get");
	}

	/** 
	 * Gets the latest update date from all of the rows in the pcode tables.
	 * @return the most recent pcode data modification date.
	 */
	public Date getLastModified() {
		return getSqlSession().selectOne("srsnames.getLastModifiedDate");
	}

}
