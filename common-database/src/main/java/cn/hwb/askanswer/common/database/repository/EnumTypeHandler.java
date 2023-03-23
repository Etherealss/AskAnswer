package cn.hwb.askanswer.common.database.repository;


import cn.hwb.askanswer.common.base.enums.BaseEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author wang tengkun
 * @date 2022/4/26
 */
@Slf4j
public class EnumTypeHandler<T extends BaseEnum> extends BaseTypeHandler<BaseEnum> {
    Class<T> classType;

    // 必须要有，否则启动报错NoSuchMethodException
    public EnumTypeHandler() {
    }

    public EnumTypeHandler(Class<T> classType) {
        if (classType == null) {
            throw new IllegalArgumentException("EnumTypeHandler处理的类型不能为null");
        }
        log.info("MyBatis 枚举映射初始化：{}", classType.getSimpleName());
        this.classType = classType;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BaseEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public BaseEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getBaseEnum(rs.getInt(columnName));
    }

    @Override
    public BaseEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getBaseEnum(rs.getInt(columnIndex));

    }

    @Override
    public BaseEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getBaseEnum(cs.getInt(columnIndex));
    }

    private T getBaseEnum(int columnValue) {
        return BaseEnum.fromCode(classType, columnValue);
    }
}
