package kr.xit.core.support.mybatis;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * <pre>
 * description : Mybatis Object Handler
 * packageName : kr.xit.core.support.mybatis
 * fileName    : ObjectTypeHandler
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 *
 * @see BaseTypeHandler
 */
public class ObjectTypeHandler extends BaseTypeHandler<Object> {
    private static final Log log = LogFactory.getLog(ObjectTypeHandler.class);

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object object = rs.getObject(columnName);

		if (object instanceof Clob clob) {
            try {
                int size = (int) clob.length();
                return clob.getSubString(1, size);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else if (object instanceof java.sql.Date) {
            Timestamp sqlTimestamp = rs.getTimestamp(columnName);
            if (sqlTimestamp != null) {
                return new Date(sqlTimestamp.getTime());
            }
        }
        return object;
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        Object object = rs.getObject(columnIndex);

        if (object instanceof Clob clob) {

            try {
                int size = (int) clob.length();
                return clob.getSubString(1, size);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else if (object instanceof java.sql.Date) {
            Timestamp sqlTimestamp = rs.getTimestamp(columnIndex);
            if (sqlTimestamp != null) {
                return new Date(sqlTimestamp.getTime());
            }
        }
        return object;
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return cs.getObject(columnIndex);
    }
}
