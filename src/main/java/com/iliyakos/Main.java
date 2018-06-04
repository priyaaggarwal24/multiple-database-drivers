package com.iliyakos;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
            System.out.println("Starting datasource creation with configured driver as MSSQL driver class");
            final DataSource dataSource = createDataSource(MSSQL_CONNECTION_STRING,
                    MSSQL_CONNECTION_USERNAME,
                    MSSQL_CONNECTION_PASSWORD,
                    MSSQL_DRIVER_CLASS);
            System.out.println("Finished creating datasource  with configured driver MSSQL driver class");
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.queryForList("Select 1");
            System.out.println("MSSQL driver loaded, ran a DB query");
        });

        executorService.submit(() -> {
            System.out.println("Starting datasource creation with configured driver JTDS driver class");
            final DataSource dataSource = createDataSource(JTDS_CONNECTION_STRING,
                    JTDS_CONNECTION_USERNAME,
                    JTDS_CONNECTION_PASSWORD,
                    JTDS_DRIVER_CLASS);
            System.out.println("Finished creating datasource with configured driver JTDS driver class");
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.queryForList("Select 1");
            System.out.println("JTDS driver loaded, ran a DB query");
        });

        executorService.shutdown();
    }

    private static DataSource createDataSource(String jdbcUrl, String userName, String password, String driverClass) {
        final PoolConfiguration poolConfiguration = new PoolProperties();

        poolConfiguration.setUrl(jdbcUrl);
        poolConfiguration.setUsername(userName);
        poolConfiguration.setPassword(password);
        poolConfiguration.setDriverClassName(driverClass);

        return new DataSource(poolConfiguration);
    }

    static {
        JTDS_CONNECTION_STRING = System.getProperty("JTDS_CONNECTION_STRING");
        JTDS_CONNECTION_USERNAME = System.getProperty("JTDS_CONNECTION_USERNAME");
        JTDS_CONNECTION_PASSWORD = System.getProperty("JTDS_CONNECTION_PASSWORD");

        MSSQL_CONNECTION_STRING = System.getProperty("MSSQL_CONNECTION_STRING");
        MSSQL_CONNECTION_USERNAME = System.getProperty("MSSQL_CONNECTION_USERNAME");
        MSSQL_CONNECTION_PASSWORD = System.getProperty("MSSQL_CONNECTION_PASSWORD");
    }

    private static final String JTDS_DRIVER_CLASS = "net.sourceforge.jtds.jdbc.Driver";
    private static final String JTDS_CONNECTION_STRING;
    private static final String JTDS_CONNECTION_USERNAME;
    private static final String JTDS_CONNECTION_PASSWORD;

    private static final String MSSQL_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String MSSQL_CONNECTION_STRING;
    private static final String MSSQL_CONNECTION_USERNAME;
    private static final String MSSQL_CONNECTION_PASSWORD;

}
