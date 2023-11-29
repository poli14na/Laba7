package com.example;

import connectionModule.ConnectionModule;
import enums.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ProfileController {

    @FXML
    public void initialize(){
        if(Client.connectedUser != null) {
            fullnameInput.setText(Client.connectedUser.getFullName());
            phoneInput.setText(Client.connectedUser.getPhone());
        }
        else{
            fullnameInput.setText(Client.connectedMaster.getFullName());
            phoneInput.setText(String.valueOf(Client.connectedMaster.getExperience()));
            phoneInput.setPromptText("Стаж");
        }

    }
    @FXML
    private TextField fullnameInput;

    @FXML
    private TextField phoneInput;

    @FXML
    void onCancel(ActionEvent event) {
        if(Client.userType == UserType.ADMIN)
            Client.changingWindowUtility.showWindow(Client.changingWindowUtility.userView, Client.changingWindowUtility.userW, Client.changingWindowUtility.userH, "Управление пользователями");
        else
            Client.changingWindowUtility.showWindow(Client.changingWindowUtility.typesView, Client.changingWindowUtility.typesW, Client.changingWindowUtility.typesH, "Предоставляемые услуги");
    }

    @FXML
    void onOK(ActionEvent event) {
        String fullname = fullnameInput.getText();
        String telephone = phoneInput.getText();
        if(fullname.isEmpty() || telephone.isEmpty()){
            AlertManager.showErrorAlert("Ошибка!", "Введите все поля");
            return;
        }

        int exp = -1;

        try{
            exp = Integer.valueOf(telephone);
            if(exp<0)
                throw new NumberFormatException();
        }
        catch (NumberFormatException e){
            AlertManager.showErrorAlert("Ошибка", "В поле должно быть число");
            return;
        }

        try {
            if(Client.connectedMaster == null){
                Client.connectedUser.setFullName(fullname);
                Client.connectedUser.setPhone(telephone);
                ConnectionModule.editCurrentProfile(UserType.USER ,Client.connectedUser);
            }
            else{
                Client.connectedMaster.setFullName(fullname);
                Client.connectedMaster.setExperience(exp);
                ConnectionModule.editCurrentProfile(UserType.MASTER ,Client.connectedMaster);
            }

            if(Client.userType == UserType.ADMIN)
                Client.changingWindowUtility.showWindow(Client.changingWindowUtility.userView, Client.changingWindowUtility.userW, Client.changingWindowUtility.userH, "Управление пользователями");
            else
                Client.changingWindowUtility.showWindow(Client.changingWindowUtility.typesView, Client.changingWindowUtility.typesW, Client.changingWindowUtility.typesH, "Предоставляемые услуги");
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка!", "");
        }
    }

}
