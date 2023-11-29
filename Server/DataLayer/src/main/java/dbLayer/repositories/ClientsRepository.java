package dbLayer.repositories;

import entities.User;
import entities.Status;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientsRepository {
    private final Connection dbConnection;

    public ClientsRepository(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    private User convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        if (!resultSet.next()) return new User();
        return convertResultSetToObj(resultSet);
    }

    private User convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var obj = new User();
        obj.setId(resultSet.getInt("id"));
        obj.setLogin(resultSet.getString("login"));
        obj.setPassword(resultSet.getString("password"));
        obj.setFullName(resultSet.getString("fullName"));
        obj.setPhone(resultSet.getString("phone"));
        switch (resultSet.getInt("status")){
            case 0 -> {
                obj.setStatus(Status.BANNED);
            }
            case 1 -> {
                obj.setStatus(Status.NOT_BANNED);
            }
        }
        return obj;
    }

    private List<User> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<User>();
        resultSet.beforeFirst();
        while (resultSet.next()) {

            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    private int getMaxId() throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT MAX(id) from clients;");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    public int create(User obj) throws SQLException {

        var insertStatement = dbConnection.prepareStatement(
                "INSERT INTO clients (login, password, fullName, phone, status) " +
                        "values (?, ?, ?, ?, ?)");

        insertStatement.setString(1, obj.getLogin());
        insertStatement.setString(2, obj.getPassword());
        insertStatement.setString(3, obj.getFullName());
        insertStatement.setString(4, obj.getPhone());
        insertStatement.setInt(5, obj.getStatus().ordinal());
        insertStatement.executeUpdate();
        return getMaxId();
    }

    public void update(User obj) throws SQLException {

        var updateStatement = dbConnection.prepareStatement(
                "UPDATE clients SET login=?, password=?, fullName=?, phone=?, status=? where id = ?");
        updateStatement.setString(1, obj.getLogin());
        updateStatement.setString(2, obj.getPassword());
        updateStatement.setString(3, obj.getFullName());
        updateStatement.setString(4, obj.getPhone());
        updateStatement.setInt(5, obj.getStatus().ordinal());
        updateStatement.setInt(6, obj.getId());
        updateStatement.executeUpdate();
    }

    public void delete(int id) throws SQLException {

        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from clients where id=?");
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
    }

    public User getById(int id) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM clients where id = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, id);
        statement.executeQuery();
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    public User get(String login, String password) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM clients where login = ? AND password = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setString(1, login);
        statement.setString(2, password);
        statement.executeQuery();
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    public User get(String login) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM clients where login = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setString(1, login);
        statement.executeQuery();
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    public List<User> getAll() throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM clients;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeQuery();
        return convertResultSetToList(statement.getResultSet());
    }


}
