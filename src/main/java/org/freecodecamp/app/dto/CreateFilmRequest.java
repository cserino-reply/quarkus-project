package org.freecodecamp.app.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class CreateFilmRequest {

    @NotBlank
    private String title;

    @Size(max = 1000)
    private String description;

    @NotNull
    @Positive
    private Short languageId;

    private Short originalLanguageId;

    @NotNull
    @Positive
    private Short rentalDuration;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private Float rentalRate;

    @NotNull
    @Positive
    private Short length;

    private BigDecimal replacementCost;

    @Pattern(regexp = "^(G|PG|PG-13|R|NC-17)$", message = "Invalid rating")
    private String rating;

    private String specialFeatures;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Short getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Short languageId) {
        this.languageId = languageId;
    }

    public Short getOriginalLanguageId() {
        return originalLanguageId;
    }

    public void setOriginalLanguageId(Short originalLanguageId) {
        this.originalLanguageId = originalLanguageId;
    }

    public Short getRentalDuration() {
        return rentalDuration;
    }

    public void setRentalDuration(Short rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    public Float getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(Float rentalRate) {
        this.rentalRate = rentalRate;
    }

    public Short getLength() {
        return length;
    }

    public void setLength(Short length) {
        this.length = length;
    }

    public BigDecimal getReplacementCost() {
        return replacementCost;
    }

    public void setReplacementCost(BigDecimal replacementCost) {
        this.replacementCost = replacementCost;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }
}