package config;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisUtil {

    private static final SqlSessionFactory sqlSessionFactory;

    static {
        try {

            Reader reader = Resources.getResourceAsReader("mybatis-config.xml");

            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

        } catch (Exception e) {

            throw new RuntimeException("Failed to initialize MyBatis", e);
        }
    }

    private MyBatisUtil() {
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
