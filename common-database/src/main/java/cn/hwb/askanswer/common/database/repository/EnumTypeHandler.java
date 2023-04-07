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
 * 将枚举转为int存储到数据库
 * @author hwb
 */
@Slf4j
public class EnumTypeHandler<T extends BaseEnum> extends BaseTypeHandler<BaseEnum> {
    /**
     * 枚举的类型
     */
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

    /**
     * 通过 int code 获取枚举对象，见 {@link BaseEnum#fromCode(Class, int)}
     * @param columnValue
     * @return
     */
    private T getBaseEnum(int columnValue) {
        return BaseEnum.fromCode(classType, columnValue);
    }
}
