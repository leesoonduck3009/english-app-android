package com.example.snaplearn.Model;

public class Keyword {
    private String keyWordId;
    private String keyWordText;
    private String engMeaning;
    private String vietMeaning;
    private String engSentence;
    private String vietSentence;
    private String userId;
    public static final String COLLECTION_NAME = "keyword";
    public static final String KEY_WORD_ID_FIELD = "keyWordId";
    public static final String KEY_WORD_TEXT_FIELD = "keyWordText";
    public static final String ENG_MEANING_FIELD = "engMeaning";
    public static final String VIET_MEANING_FIELD = "vietMeaning";
    public static final String ENG_SENTENCE_FIELD = "engSentence";
    public static final String VIET_SENTENCE_FIELD = "vietSentence";
    public static final String USER_ID_FIELD = "userId";
    public Keyword(){

    }
    private Keyword(Builder builder) {
        this.keyWordId = builder.keyWordId;
        this.keyWordText = builder.keyWordText;
        this.engMeaning = builder.engMeaning;
        this.vietMeaning = builder.vietMeaning;
        this.engSentence = builder.engSentence;
        this.vietSentence = builder.vietSentence;
        this.userId = builder.userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKeyWordId() {
        return keyWordId;
    }

    public void setKeyWordId(String keyWordId) {
        this.keyWordId = keyWordId;
    }

    public String getKeyWordText() {
        return keyWordText;
    }

    public void setKeyWordText(String keyWordText) {
        this.keyWordText = keyWordText;
    }

    public String getEngMeaning() {
        return engMeaning;
    }

    public void setEngMeaning(String engMeaning) {
        this.engMeaning = engMeaning;
    }

    public String getVietMeaning() {
        return vietMeaning;
    }

    public void setVietMeaning(String vietMeaning) {
        this.vietMeaning = vietMeaning;
    }

    public String getEngSentence() {
        return engSentence;
    }

    public void setEngSentence(String engSentence) {
        this.engSentence = engSentence;
    }

    public String getVietSentence() {
        return vietSentence;
    }

    public void setVietSentence(String vietSentence) {
        this.vietSentence = vietSentence;
    }


    public static class Builder {
        private String keyWordId;
        private String keyWordText;
        private String engMeaning;
        private String vietMeaning;
        private String engSentence;
        private String vietSentence;
        private String userId;
        public Builder() {
        }
        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setKeyWordId(String keyWordId) {
            this.keyWordId = keyWordId;
            return this;
        }

        public Builder setKeyWordText(String keyWordText) {
            this.keyWordText = keyWordText;
            return this;
        }

        public Builder setEngMeaning(String engMeaning) {
            this.engMeaning = engMeaning;
            return this;
        }

        public Builder setVietMeaning(String vietMeaning) {
            this.vietMeaning = vietMeaning;
            return this;
        }

        public Builder setEngSentence(String engSentence) {
            this.engSentence = engSentence;
            return this;
        }

        public Builder setVietSentence(String vietSentence) {
            this.vietSentence = vietSentence;
            return this;
        }

        public Keyword build() {
            return new Keyword(this);
        }
    }
}
