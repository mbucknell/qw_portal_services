package gov.usgs.cida.qw.summary;

import gov.usgs.cida.qw.summary.SummaryController.RowCounts;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SummaryDao extends SqlSessionDaoSupport {

	private static final String NAME_SPACE = "summary";
	private static final String BIN_QUERY = "discreteSampleCountBin";

	@Autowired
	public SummaryDao(SqlSessionFactory sqlSessionFactory) {
		setSqlSessionFactory(sqlSessionFactory);
	}

	public List<RowCounts> retrieveCounts(Map<String, Object> queryParams) {
		return getSqlSession().selectList(String.join(".", NAME_SPACE, BIN_QUERY),  queryParams);
	}

}
