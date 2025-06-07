package application;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import java.util.*;

public class GameController {
    private GridPane grid;
    private AnimalCard firstSelected;
    private AnimalCard secondSelected;
    private boolean canClick = true;
    private int pairsFound = 0;

    private final String[] animalNames = {
        "jaguar", "llama", "condor", "anaconda"
    };

    public StackPane createContent() {
        StackPane root = new StackPane();
        grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

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

        root.getChildren().add(grid);
        return root;
    }

    public void cardClicked(AnimalCard card) {
        if (!canClick || card.isMatched() || card == firstSelected) return;

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
}