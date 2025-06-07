package application;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import java.util.*;

public class GameController {
    private GridPane grid;
    private AnimalCard firstSelected;
    private AnimalCard secondSelected;
    private boolean canClick = true;
    private int pairsFound = 0;
    private int maxIntentos = 10; /// VARIABLES NUEVOS PARA MAXIMO NUMERO DE INTENTOS
    private int clicIntentos= 0;  /// VARIABLES CLIC CONTADOR
    private Label LabelMaxInetos;

    private final String[] animalNames = {
        "jaguar", "llama", "condor", "anaconda"
    };

    public StackPane createContent() {
        StackPane root = new StackPane();
        /// CREACION DE TABLERO
        grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        /// /MOSTRAR MAXIMO DE INTETOS
        LabelMaxInetos = new Label("Intentos : 0 /" +  maxIntentos);
        LabelMaxInetos.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        /// MAZCLADOR DE CARTAS
        List<String> images = new ArrayList<>();
        for (String name : animalNames) {
            images.add(name);
            images.add(name);
        }

        Collections.shuffle(images);

        int rows = 2;
        int cols = images.size() / 2;

        for (int i = 0; i < images.size(); i++) {
            AnimalCard card = new AnimalCard(images.get(i), this);
            grid.add(card, i % cols, i / cols);
        }

        /// 🔁 Aquí está la parte que falta en tu código original:
        VBox layout = new VBox(10); // espacio entre elementos
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(LabelMaxInetos, grid); // agrega el Label y el grid
        root.getChildren().add(layout); // agrega el VBox al StackPane

        ///root.getChildren().add(grid);
        return root;
    }

    public void cardClicked(AnimalCard card) {
        if (!canClick || card.isMatched() || card == firstSelected) return;

        clicIntentos++; // Cada clic cuenta como intento
        LabelMaxInetos.setText("Intentos : " +  clicIntentos + " / " + maxIntentos);
        if (clicIntentos >= maxIntentos) {
            showGameOverMessage();
            return;
        }

        card.flip();
        playSound("flip.mp3");

        if (firstSelected == null) {
            firstSelected = card;
        } else {
            secondSelected = card;
            canClick = false;

            if (firstSelected.getAnimal().equals(secondSelected.getAnimal())) {
                firstSelected.setMatched(true);
                secondSelected.setMatched(true);
                playSound("match.mp3");
                showAnimalInfo(firstSelected.getAnimal());

                pairsFound++;
                resetTurn();

                if (pairsFound == animalNames.length) {
                    showVictoryMessage();
                }
            } else {
                playSound("mismatch.mp3");
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        javafx.application.Platform.runLater(() -> {
                            firstSelected.flip();
                            secondSelected.flip();
                            resetTurn();
                        });
                    }
                }, 1000);
            }
        }
    }

    private void resetTurn() {
        firstSelected = null;
        secondSelected = null;
        canClick = true;
    }

    private void playSound(String file) {
        AudioClip sound = new AudioClip(getClass().getResource("/sounds/" + file).toExternalForm());
        sound.play();
    }

    private void showAnimalInfo(String animal) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("¡Aprende sobre el " + animal + "!");
        alert.setContentText(getAnimalFact(animal));
        alert.show();
    }

    private void showVictoryMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Felicidades!");
        alert.setHeaderText("Has descubierto toda la fauna.");
        alert.setContentText("Gracias por jugar y aprender sobre la biodiversidad de Latinoamérica.");
        alert.show();
    }

    private String getAnimalFact(String name) {
        switch (name) {
            case "jaguar": return "El jaguar es el felino más grande de América y un excelente nadador.";
            case "llama": return "La llama es un animal domesticado típico de los Andes, usado como animal de carga.";
            case "condor": return "El cóndor andino es una de las aves voladoras más grandes del mundo.";
            case "anaconda": return "La anaconda es una serpiente gigante que habita en ríos sudamericanos.";
            default: return "Animal fascinante de la fauna latinoamericana.";
        }
    }

    ///  NUEVOS METODOS JD
    private void showGameOverMessage(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText("¡Juego Terminado!");
        alert.setContentText("Inténtalo de nuevo para descubrir toda la fauna.");
        alert.show();

        alert.setOnCloseRequest(e -> ReinicioJuego());
        alert.show();

        canClick = false;
    }

    private  void ReinicioJuego(){
        firstSelected = null;
        secondSelected = null;
        pairsFound = 0;
        clicIntentos = 0;
        canClick = true;

        LabelMaxInetos.setText("Intentos : 0 /" +  maxIntentos);

        grid.getChildren().clear();

        List<String> images = new ArrayList<>();
        for (String name : animalNames) {
            images.add(name);
            images.add(name);
        }

        Collections.shuffle(images);

        int rows = 2;
        int cols = images.size() / 2;

        for (int i = 0; i < images.size(); i++) {
            AnimalCard card = new AnimalCard(images.get(i), this);
            grid.add(card, i % cols, i / cols);
        }

    }
}