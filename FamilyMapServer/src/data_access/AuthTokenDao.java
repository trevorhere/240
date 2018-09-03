package data_access;

import database.Database;
import model.AuthToken;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Methods to mutate Authorization Tokens in Database
 */

public class AuthTokenDao {
    public static Database db;

    public AuthTokenDao(Database db) {
        this.db = db;
    }


    /**
     * This function inserts an Authtoken into the database
     * @param token
     */
    public void storeAuthToken(AuthToken token)
    {
            String sql = "insert into authTokens (userName, authToken, personID ) values (?,?,?)";
            PreparedStatement stmt = null;
            try {

                stmt = db.conn.prepareStatement(sql);
                stmt.setString(1, token.username);
                stmt.setString(2, token.authToken);
                stmt.setString(3, token.personID);


                if (stmt.executeUpdate() != 1)
                {
                    throw new SQLException();
                }

                if (stmt != null)
                {
                    stmt.close();

                }
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }

    }

    /**
     * This function checks to make sure an authToken exists base on an authToken string
     * @param authToken
     * @return
     */
    public boolean authenticateToken( String authToken)
    {
        AuthToken token = new AuthToken();
        ResultSet resultSet = null;
        Statement stmt = null;
        boolean result = false;

        try {
            stmt = db.conn.createStatement();
            resultSet = stmt.executeQuery("select * from authTokens where authTokens.authToken = '" + authToken + "'");

            while (resultSet.next()) {
                token.setUsername(resultSet.getString(1));
                token.setAuthToken(resultSet.getString(2));
                token.setPersonID(resultSet.getString(3));
            }


            if (stmt != null)
            {
                    stmt.close();
            }
            if (resultSet != null)
            {
                    resultSet.close();
            }
        }
        catch (SQLException e)
        {
            e.getErrorCode();
        }

            if(token.personID != null && token.username != null && token.authToken != null)
            {
                result = true;
            }

        return result;

    }


}
