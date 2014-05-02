package gov.usgs.cida.qw.dao;

import gov.usgs.cida.qw.dao.intfc.IPCodeDao;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * @author drsteini
 *
 */
public class PCodeDao extends SqlSessionDaoSupport implements IPCodeDao {

    /** 
     * {@inheritDoc}
     * @see gov.usgs.cida.qw.dao.intfc.IPCodeDao#getRows()
     */
    public List<LinkedHashMap<String, Object>> getRows() {
        return getSqlSession().selectList("getOrderedPCodes");
    }

    /** 
     * {@inheritDoc}
     * @see gov.usgs.cida.qw.dao.intfc.IPCodeDao#getLastModified()
     */
    public Date getLastModified() {
        return getSqlSession().selectOne("getPCodesLastModifiedDate");
    }

}
