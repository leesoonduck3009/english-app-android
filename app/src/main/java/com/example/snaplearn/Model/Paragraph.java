package com.example.snaplearn.Model;

import java.util.List;

public class Paragraph {
    String paragraphId;
    String paragraphTittle;
    String paragraphUrl;
    List<Keyword> keywordInParagraph;
    String userId;
    private Paragraph(Builder builder) {
        this.paragraphId = builder.paragraphId;
        this.paragraphTittle = builder.paragraphTittle;
        this.paragraphUrl = builder.paragraphUrl;
        this.keywordInParagraph = builder.keywordInParagraph;
        this.userId = builder.userId;
    }
    public Paragraph(){}
    public String getParagraphId() {
        return paragraphId;
    }

    public void setParagraphId(String paragraphId) {
        this.paragraphId = paragraphId;
    }

    public String getParagraphTittle() {
        return paragraphTittle;
    }

    public void setParagraphTittle(String paragraphTittle) {
        this.paragraphTittle = paragraphTittle;
    }

    public List<Keyword> getKeywordInParagraph() {
        return keywordInParagraph;
    }

    public void setKeywordInParagraph(List<Keyword> keywordInParagraph) {
        this.keywordInParagraph = keywordInParagraph;
    }

    public String getParagraphUrl() {
        return paragraphUrl;
    }

    public void setParagraphUrl(String paragraphUrl) {
        this.paragraphUrl = paragraphUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public static class Builder {
        private String paragraphId;
        private String paragraphTittle;
        private String paragraphUrl;
        private List<Keyword> keywordInParagraph;
        private String userId;

        public Builder() {
        }

        public Builder setParagraphId(String paragraphId) {
            this.paragraphId = paragraphId;
            return this;
        }

        public Builder setParagraphTittle(String paragraphTittle) {
            this.paragraphTittle = paragraphTittle;
            return this;
        }

        public Builder setParagraphUrl(String paragraphUrl) {
            this.paragraphUrl = paragraphUrl;
            return this;
        }

        public Builder setKeywordInParagraph(List<Keyword> keywordInParagraph) {
            this.keywordInParagraph = keywordInParagraph;
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Paragraph build() {
            return new Paragraph(this);
        }
    }

}
