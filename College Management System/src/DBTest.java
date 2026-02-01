import java.sql.*;

public class DBTest {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/college_ms",
                    "root",
                    "sandesh"
            );

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM admin WHERE userName='admin' AND password='admin'"
            );

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("LOGIN OK");
            } else {
                System.out.println("LOGIN FAILED");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
