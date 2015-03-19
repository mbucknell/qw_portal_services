package gov.usgs.cida.qw.summary;

import gov.usgs.cida.qw.summary.SummaryController.RowCounts;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

public class SummaryDao extends SqlSessionDaoSupport {

	public List<RowCounts> retrieveCounts(Map<String, Object> queryParams) {
		return getSqlSession().selectList("summary.discreteSampleCountBin",  queryParams);
	}

}
