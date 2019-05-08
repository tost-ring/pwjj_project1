package app.core;

import app.core.shop.OpenDealer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class OpenRoot extends Application {
    public final String fxmlPrePath = "/fxml/";
    public final String fxmlPostPath = ".fxml";

    private Map<Object, OpenStage> stages;
    private Map<Object, OpenDealer> dealers;
    private Stage primaryStage;

    @Override
    public void init() {
        stages = new HashMap<>();
        dealers = new HashMap<>();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public OpenStage openStage(Object key){
        OpenStage openStage = stages.get(key);
        if(openStage == null) {
            openStage = key instanceof Stage ?
                    new OpenStage(key, this, (Stage) key) :
                    new OpenStage(key, this, new Stage());
            stages.put(key, openStage);
        }
        return openStage;
    }

    public boolean collectStage(Object key) {
        OpenStage openStage = stages.get(key);
        if(openStage != null) {
            if (openStage.getStage() == primaryStage){
                return exitDialog();
            }
            else openStage.getStage().close();
        }
        return true;
    }

    public URL getResource(String forPath){
        return getClass().getResource(forPath);
    }

    public URL getFxmlResource(String fxml){return getResource(fxmlPrePath + fxml + fxmlPostPath);}

    public boolean exitDialog(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konczenie pracy");
        alert.setHeaderText("Czy napewno chcesz wyjsc z programu?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            Platform.exit();
            return true;
        }
        return false;
    }

    public void popUpStage(String fxml){
        openStage(new Stage()).openScene(fxml).show();
    }

    public OpenDealer openDealer(){return openDealer(OpenRoot.class);}

    public OpenDealer openDealer(Object key){
        OpenDealer openDealer = dealers.get(key);
        if(openDealer == null) {
            openDealer = new OpenDealer(this);
            dealers.put(key, openDealer);
        }
        return openDealer;
    }

    public OpenDealer openDealer(Object key, OpenDealer openDealer){
        dealers.put(key,openDealer);
        return openDealer;
    }
}
