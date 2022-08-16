package br.com.sidney.alura_challenge_backend.enums;

public enum Category {
    FOOD("Food"),
    HEALTH("Health"),
    DWELLING_HOUSE("Dwelling house"),
    TRANSPORT("Transport"),
    EDUCATION("Education"),
    UNFORESEEN("Unforeseen"),
    LEISURE("Leisure"),
    OTHERS("Others");

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
