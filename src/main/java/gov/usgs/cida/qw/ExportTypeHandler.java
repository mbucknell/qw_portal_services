package gov.usgs.cida.qw;


import gov.usgs.cida.qw.Export;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;


/**
 * @author drsteini
 *
 */
public class ExportTypeHandler implements TypeHandler<Export> {

    /** 
     * {@inheritDoc}
     * @see org.apache.ibatis.type.TypeHandler#setParameter(java.sql.PreparedStatement, int, java.lang.Object, org.apache.ibatis.type.JdbcType)
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, Export parameter, JdbcType jdbcType) throws SQLException {
    	throw new UnsupportedOperationException("Not currently needed for this application so not implemented");
    }

    /** 
     * {@inheritDoc}
     * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.ResultSet, int)
     */
    @Override
    public Export getResult(ResultSet rs, int columnIndex) throws SQLException {
    	throw new UnsupportedOperationException("Not currently needed for this application so not implemented");
    }

    /** 
     * {@inheritDoc}
     * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.CallableStatement, int)
     */
    @Override
    public Export getResult(CallableStatement cs, int columnIndex) throws SQLException {
    	throw new UnsupportedOperationException("Not currently needed for this application so not implemented");
    }

    /** 
     * {@inheritDoc}
     * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.ResultSet, java.lang.String)
     */
    @Override
    public Export getResult(ResultSet rs, String columnName) throws SQLException {
        Export export = new Export();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            export.put(rs.getMetaData().getColumnName(i), nullCheckValue(rs.getObject(i)));
        }

        return export;
    }

    /** 
     * We convert nulls to an empty string to avoid having the text "null" in the output of the Export
     * @param value
     * @return the value or an empty string.
     */
    private Object nullCheckValue(Object value) {
        return null == value ? "" : value;
    }

}
