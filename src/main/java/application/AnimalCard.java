package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class AnimalCard extends StackPane {
    private final String animal;
    private final ImageView front;
    private final ImageView back;
    private boolean flipped = false;
    private boolean matched = false;

    public AnimalCard(String animal, GameController controller) {
        this.animal = animal;

        front = new ImageView(new Image(getClass().getResourceAsStream("/images/" + animal + ".png")));
        front.setFitWidth(100);
        front.setFitHeight(100);

        back = new ImageView(new Image(getClass().getResourceAsStream("/images/back.png")));
        back.setFitWidth(100);
        back.setFitHeight(100);

        getChildren().add(back);

        setOnMouseClicked(e -> {
            if (!flipped && !matched) {
                controller.cardClicked(this);
            }
        });
    }

    public void flip() {
        getChildren().clear();
        flipped = !flipped;
        getChildren().add(flipped ? front : back);
    }

    public String getAnimal() {
        return animal;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }
}