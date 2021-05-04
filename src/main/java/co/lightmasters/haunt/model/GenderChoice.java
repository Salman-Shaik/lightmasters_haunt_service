package co.lightmasters.haunt.model;

public enum GenderChoice {
    MALE("Male"), FEMALE("Female"), BOTH("Both");

    private final String genderChoice;

    GenderChoice(String genderChoice) {
        this.genderChoice = genderChoice;
    }

    public String toString() {
        return genderChoice;
    }

    public static boolean isBoth(String genderChoice) {
        return genderChoice.equals(GenderChoice.BOTH.toString());
    }
}
