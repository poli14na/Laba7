package com.example;

import connectionModule.ConnectionModule;
import entities.Master;
import entities.Purpose;
import entities.User;
import enums.UserType;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class Client extends Application {

    public static ChangingWindowUtility changingWindowUtility;
    public  static UserType userType;
    public  static User connectedUser;
    public  static Master connectedMaster;

    public static Purpose purpose;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(false);
        try {
            ConnectionModule.connectToServer();
            //var userType = ConnectionModule.singUp("admin", "admin");
            // В контексте авторизации доступны singUp, Register, checkIfLoginExist после этого все кроме них
            changingWindowUtility = new ChangingWindowUtility(stage);
            changingWindowUtility.showWindow(changingWindowUtility.authorizationView, changingWindowUtility.authorizationW, changingWindowUtility.authorizationH, "Авторизация");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
