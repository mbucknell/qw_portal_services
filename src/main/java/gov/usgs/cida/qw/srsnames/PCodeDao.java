package gov.usgs.cida.qw.srsnames;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PCodeDao extends SqlSessionDaoSupport {

	private static final String NAME_SPACE = "srsnames";
	private static final String GET_ROWS_QUERY = "get";
	private static final String GET_LAST_MODIFIED_QUERY = "getLastModifiedDate";

	@Autowired
	public PCodeDao(SqlSessionFactory sqlSessionFactory) {
		setSqlSessionFactory(sqlSessionFactory);
	}

	/** 
	 * Gets the a sorted list of pcodes from the database.
	 * @return the list of sorted pcodes.
	 */
	public List<LinkedHashMap<String, Object>> getRows() {
		List<LinkedHashMap<String, Object>> data = getSqlSession().selectList(String.join(".", NAME_SPACE, GET_ROWS_QUERY));
		//With the magic of Java 8 lambda, We convert nulls to an empty string to avoid having the text "null" in the output.
		data.forEach((map) -> map.forEach((key, value) -> map.compute(key, (k, v) -> (v == null) ? "" : v)));
		return data;
	}

	/** 
	 * Gets the latest update date from all of the rows in the pcode tables.
	 * @return the most recent pcode data modification date.
	 */
	public Date getLastModified() {
		return getSqlSession().selectOne(String.join(".", NAME_SPACE, GET_LAST_MODIFIED_QUERY));
	}

}
