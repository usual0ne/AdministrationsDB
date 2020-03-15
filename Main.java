package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Scanner;

public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter login default (root): ");
        String login = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine().trim();

        DatabaseHandler.getDbConnection(login, password);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
        try
        {
            DatabaseHandler.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
