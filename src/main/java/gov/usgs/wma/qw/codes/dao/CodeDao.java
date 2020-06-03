package gov.usgs.wma.qw.codes.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.usgs.wma.qw.codes.Code;
import gov.usgs.wma.qw.codes.CodeType;

@Component
public class CodeDao extends SqlSessionDaoSupport {

	@Autowired
	public CodeDao(SqlSessionFactory sqlSessionFactory) {
		setSqlSessionFactory(sqlSessionFactory);
	}

	public int getRecordCount(final CodeType codeType, final Map<String, Object> parameterMap) {
		return getSqlSession().selectOne(codeType.getCountSelectID(), parameterMap);
	}

	public List<Code> getCodes(final CodeType codeType, final Map<String, Object> parameterMap) {
		return getSqlSession().selectList(codeType.getListSelectID(), parameterMap);
	}

	public List<Code> getCodes(final CodeType codeType) {
		return getCodes(codeType, new HashMap<String, Object>());
	}

	public Code getCode(final CodeType codeType, final String codeValue) {
		return getSqlSession().selectOne(codeType.getSingleSelectID(), codeValue);
	}

}
