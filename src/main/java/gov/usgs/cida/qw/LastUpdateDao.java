package gov.usgs.cida.qw;

import java.time.LocalDateTime;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LastUpdateDao extends SqlSessionDaoSupport {

	private static final String NAME_SPACE = "lastEtl";
	private static final String GET_QUERY = "get";

	@Autowired
	public LastUpdateDao(SqlSessionFactory sqlSessionFactory) {
		setSqlSessionFactory(sqlSessionFactory);
	}

	public LocalDateTime getLastEtl() {
		return getSqlSession().selectOne(String.join(".", NAME_SPACE, GET_QUERY));
	}

}
