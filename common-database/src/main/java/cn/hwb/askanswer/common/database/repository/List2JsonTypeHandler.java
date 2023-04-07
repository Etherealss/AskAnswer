package cn.hwb.askanswer.common.database.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 可以用于将List自动转为 JSON 格式存在数据库中
 * 使用时直接配合MybatisPlus
 * @author hwb
 */
public class List2JsonTypeHandler extends BaseTypeHandler<List<?>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<?> objects, JdbcType jdbcType) throws SQLException {
        try {
            String s = objectMapper.writeValueAsString(objects);
            preparedStatement.setString(i, s);
        } catch (JsonProcessingException e) {
            throw new SQLException("参数转换失败，无法将列表：'" + objects.toString() + "' 转换为JSON类型");
        }
    }

    @Override
    public List<?> getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return readJson(resultSet.getString(columnName));
    }

    @Override
    public List<?> getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return readJson(resultSet.getString(columnIndex));
    }

    @Override
    public List<?> getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return readJson(callableStatement.getString(columnIndex));
    }

    private List<?> readJson(String json) throws SQLException {
        if (json == null) {
            return new ArrayList<>(0);
        }
        try {
            return objectMapper.readValue(json, ArrayList.class);
        } catch (JsonProcessingException e) {
            throw new SQLException("参数转换失败，无法将JSON数据：'" + json + "' 转换为Set数据");
        }
    }
}
