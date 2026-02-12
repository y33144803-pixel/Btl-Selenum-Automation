package org.example.pages;


public enum MainMenu {
    RIGHTS("מיצוי זכויות"),
    BENEFITS("קצבאות והטבות"),
    INSURANCE("דמי ביטוח"),
    CONTACT("יצירת קשר"),
    BRANCHES("סניפים"),
    PAYMENTS("תשלומים"),
    PERSONAL_SERVICE("שירות אישי");

    private final String text;

    MainMenu(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}