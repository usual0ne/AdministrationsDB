package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DatabaseHandler {

    private static Connection dbConnection;
    private static ResultSet rs;
    private PreparedStatement pstmt;

    public static Connection getDbConnection(String login, String password) throws SQLException
    {
        dbConnection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/railroads?serverTimezone=UTC", login, password);
        System.out.println("Connected to the database.");
        return dbConnection;
    }

    public static void close() throws SQLException
    {
        dbConnection.close();
        System.out.println("Disconnected from the database.");
    }

    public static ResultSet fillTable() throws SQLException
    {
        PreparedStatement stmt =
                dbConnection.prepareStatement("SELECT * FROM administrations");
        rs = stmt.executeQuery();
        return rs;
    }

    public static void addQuery(String code, String fullname, String shortname)
    {
        try
        {
            PreparedStatement addStmt =
                    dbConnection.prepareStatement("INSERT INTO administrations (`Код_ЖА`, `Полное_наименование_ЖА`, " +
                            "`Аббревиатура_ЖА`) VALUES (?, ?, ?)");
            addStmt.setString(1, code);
            addStmt.setString(2, fullname);
            addStmt.setString(3, shortname);
            addStmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    public static  void editQuery(String code, String fullname, String shortname)
    {
        try
        {
            PreparedStatement editStmt =
                    dbConnection.prepareStatement("UPDATE administrations SET Полное_наименование_ЖА= ?, " +
                            "Аббревиатура_ЖА= ? WHERE Код_ЖА = ?");
            editStmt.setString(1, fullname);
            editStmt.setString(2, shortname);
            editStmt.setString(3, code);

            editStmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static  void deleteQuery(String code)
    {
        try
        {
            PreparedStatement deleteStmt =
                    dbConnection.prepareStatement("DELETE FROM administrations WHERE Код_ЖА = " + code);

            deleteStmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static ObservableList<TableContent> updateValuesQuery()
    {
        ObservableList list = FXCollections.observableArrayList();

        try
        {
            PreparedStatement updateStmt =
                    dbConnection.prepareStatement("SELECT a.Код_ЖА, a.Полное_наименование_ЖА, a.Аббревиатура_ЖА FROM administrations AS a");
            rs = updateStmt.executeQuery();

            while(rs.next())
            {
                list.add(new TableContent(rs.getString("Код_ЖА"),
                        rs.getString("Полное_наименование_ЖА"), rs.getString("Аббревиатура_ЖА")));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return list;
    }
}