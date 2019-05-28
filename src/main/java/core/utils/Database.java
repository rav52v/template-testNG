package main.java.core.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

import static main.java.core.ConfigService.getConfigService;

public class Database {
  private static Database instance;

  private Connection con;
  private Logger log;


  private Database() {
    log = LogManager.getLogger(this);
    try {
      con = DriverManager.getConnection(
              getConfigService().getStringProperty("Sql.conUrl"),
              getConfigService().getStringProperty("Sql.login"),
              getConfigService().getStringProperty("Sql.password"));
      con.setAutoCommit(true);
      log.info("Connected to database {" + getConfigService().getStringProperty("Sql.conUrl") + "}");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static Database getInstance() {
    if (instance == null)
      instance = new Database();
    return instance;
  }

  /**
   * @param query full query to execute
   */
  public void executeQuery(String query) {
    try {
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      log.error(e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * @param table  name of table
   * @param column name of column
   * @param value  value to check (implemented for Integer or String)
   * @return if record occurs
   */
  public boolean checkIfRecordExist(String table, String column, Object value) {
    String query = "SELECT " + column + " FROM " + table + " WHERE " + column + " = ?";
    ResultSet rs;
    try {
      PreparedStatement pstmt = con.prepareStatement(query);

      if (value instanceof String)
        pstmt.setString(1, (String) value);
      else if (value instanceof Integer)
        pstmt.setInt(1, (Integer) value);

      rs = pstmt.executeQuery();
      return rs.next();
    } catch (SQLException e) {
      log.error(e.getMessage());
      e.printStackTrace();
    }
    return false;
  }

  /**
   * @param table          name of table
   * @param columnWhere    column type 'WHERE'
   * @param valueWhere     record from columnWhere (implemented for Integer or String)
   * @param columnToUpdate column containing record to update
   * @param newValue       new value (implemented for Integer or String)
   */
  public void updateRecord(String table, String columnWhere, Object valueWhere, String columnToUpdate, Object newValue) {
    String query = "UPDATE " + table + " SET " + columnToUpdate + " = ? WHERE " + columnWhere + " = ?";
    try {
      PreparedStatement pstmt = con.prepareStatement(query);

      if (newValue instanceof String)
        pstmt.setString(1, (String) newValue);
      else if (newValue instanceof Integer)
        pstmt.setInt(1, (Integer) newValue);

      if (valueWhere instanceof String)
        pstmt.setString(2, (String) valueWhere);
      else if (valueWhere instanceof Integer)
        pstmt.setInt(2, (Integer) valueWhere);

      pstmt.executeUpdate();
    } catch (SQLException e) {
      log.error(e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * @param table   name of table
   * @param columns columns names
   * @param values  values to insert
   */
  public void insertRecords(String table, String[] columns, Object[] values) {
    if (columns.length != values.length) {
      log.error("Columns length {" + columns.length + "} is not the same as number of values {"
              + values.length + "}");
      throw new RuntimeException("Columns length {" + columns.length + "} is not the same as number of values {"
              + values.length + "}");
    }

    String query = String.format("INSERT into %s (%s) VALUES (%s?)",
            table, String.join(", ", columns), "?, ".repeat(columns.length - 1));

    PreparedStatement pstmt;
    try {
      pstmt = con.prepareStatement(query);
      for (int i = 0; i < values.length; i++) {
        if (values[i] instanceof String)
          pstmt.setString(i + 1, (String) values[i]);
        else if (values[i] instanceof Integer)
          pstmt.setInt(i + 1, (Integer) values[i]);
      }
      pstmt.executeUpdate();
    } catch (SQLException e) {
      log.error(e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * @param table            name of table
   * @param columnWhere      column type 'WHERE'
   * @param valueWhere       record from columnWhere
   * @param columnWithResult column with result
   * @return String value given from result set
   */
  public String getString(String table, String columnWhere, Object valueWhere, String columnWithResult) {
    String query = "SELECT * FROM " + table + " WHERE " + columnWhere + " = ?";
    ResultSet rs;
    try {
      PreparedStatement pstmt = con.prepareStatement(query);

      if (valueWhere instanceof String)
        pstmt.setString(1, (String) valueWhere);
      else if (valueWhere instanceof Integer)
        pstmt.setInt(1, (Integer) valueWhere);

      rs = pstmt.executeQuery();
      if (rs.next()) return rs.getString(columnWithResult);
    } catch (SQLException e) {
      log.error(e.getMessage());
      e.printStackTrace();
    }
    return "";
  }

  /**
   * @param table            name of table
   * @param columnWhere      column type 'WHERE'
   * @param valueWhere       record from columnWhere
   * @param columnWithResult column with result
   * @return Integer value given from result set
   */
  public Integer getInt(String table, String columnWhere, Object valueWhere, String columnWithResult) {
    String query = "SELECT * FROM " + table + " WHERE " + columnWhere + " = ?";
    ResultSet rs;
    try {
      PreparedStatement pstmt = con.prepareStatement(query);

      if (valueWhere instanceof String)
        pstmt.setString(1, (String) valueWhere);
      else if (valueWhere instanceof Integer)
        pstmt.setInt(1, (Integer) valueWhere);

      rs = pstmt.executeQuery();
      if (rs.next()) return rs.getInt(columnWithResult);
    } catch (SQLException e) {
      log.error(e.getMessage());
      e.printStackTrace();
    }
    return 0;
  }
}
