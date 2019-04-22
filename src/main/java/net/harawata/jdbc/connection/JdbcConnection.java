/**
 * Copyright (c) 2019 Iwao AVE!
 * Licensed under the MIT license.
 * See LICENSE file in the project root for details.
 */
package net.harawata.jdbc.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import net.harawata.appdirs.AppDirsFactory;

public class JdbcConnection {

  private static final String CONFIG_DIR_KEY = "net.harawata.jdbc.config.dir";
  private final String db;
  private final File dir;

  public JdbcConnection(String db) {
    this(db, null);
  }

  public JdbcConnection(String db, File dir) {
    this.db = db;
    if (dir != null) {
      this.dir = dir;
    } else {
      String path = System.getProperty(CONFIG_DIR_KEY);
      if (path == null) {
        path = System.getenv(CONFIG_DIR_KEY);
      }
      if (path == null) {
        path = AppDirsFactory.getInstance().getUserConfigDir("net.harawata.jdbc.JdbcConnection", "1.0.0", null);
      }
      this.dir = new File(path);
    }
  }

  public Connection getConnection() throws ReflectiveOperationException, SQLException, IOException {
    return getConnection(new Properties());
  }

  public Connection getConnection(Properties connectionProperties)
      throws ReflectiveOperationException, SQLException, IOException {
    if (!dir.exists()) {
      throw new IllegalStateException(
          "Config directory '" + dir + "' does not exist. To use another directory, set the path to '" + CONFIG_DIR_KEY
              + "' system property or environment variable.");
    }
    Properties prop = loadDbConfig();
    String driver = prop.getProperty("driver");
    String url = prop.getProperty("url");
    String username = prop.getProperty("username");
    String password = prop.getProperty("password");
    Properties info = new Properties();
    info.put("user", username);
    info.put("password", password);
    info.putAll(connectionProperties);
    Connection con = ((Driver) ClassLoader.getSystemClassLoader().loadClass(driver).getDeclaredConstructor()
        .newInstance()).connect(url, info);
    printVersions(con);
    return con;
  }

  protected void printVersions(Connection con) throws SQLException {
    DatabaseMetaData dbmd = con.getMetaData();
    System.out.println(">>> DB version : " + dbmd.getDatabaseProductName() + " " + dbmd.getDatabaseProductVersion());
    System.out.println(">>> Driver version : " + dbmd.getDriverVersion());
  }

  protected Properties loadDbConfig() throws IOException {
    Properties prop = new Properties();
    try (FileInputStream fileInputStream = new FileInputStream(new File(dir, db + ".properties"))) {
      prop.load(fileInputStream);
    } catch (FileNotFoundException e) {
      throw new MissingDbConfigException(
          "Config file '" + db + ".properties' does not exist in the directory '" + dir + "'. ");
    }
    return prop;
  }

}
