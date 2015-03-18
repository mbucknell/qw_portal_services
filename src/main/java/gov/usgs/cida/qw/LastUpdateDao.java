package gov.usgs.cida.qw;

import org.joda.time.LocalDateTime;
import org.mybatis.spring.support.SqlSessionDaoSupport;

public class LastUpdateDao extends SqlSessionDaoSupport {

    public LocalDateTime getLastEtl() {
    	return getSqlSession().selectOne("lastEtl.get");
    }

}
