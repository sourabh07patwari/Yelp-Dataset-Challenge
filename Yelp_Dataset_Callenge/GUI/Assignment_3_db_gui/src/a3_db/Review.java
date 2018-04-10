/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a3_db;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Anmol
 */
public class Review {
    private final SimpleStringProperty date;
    private final SimpleStringProperty stars;
    private final SimpleStringProperty text;
    private final SimpleStringProperty userid;
    private final SimpleStringProperty usefulVotes;

    public Review(String date, String stars, String text, String userid, String usefulVotes) {
        this.date           = new SimpleStringProperty(date);
        this.stars          = new SimpleStringProperty(stars);
        this.text           = new SimpleStringProperty(text);
        this.userid         = new SimpleStringProperty(userid);
        this.usefulVotes    = new SimpleStringProperty(usefulVotes);
    }
    
    public String getDate() {
        return date.get();
    }

    public String getStars() {
        return stars.get();
    }

    public String getText() {
        return text.get();
    }

    public String getUserid() {
        return userid.get();
    }

    public String getUsefulVotes() {
        return usefulVotes.get();
    }
    
    
}
