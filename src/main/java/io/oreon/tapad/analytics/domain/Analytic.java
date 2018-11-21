package io.oreon.tapad.analytics.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class Analytic {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String user;
    private Long timestamp;
    private Boolean click;
    private Boolean impression;

    //Used by Jackson
    private Analytic() {}

    private Analytic(String user, Long timestamp, Boolean click, Boolean impression) {
        this.user = user;
        this.timestamp = timestamp;
        this.click = click;
        this.impression = impression;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean isClick() {
        return click;
    }

    public void setClick(Boolean click) {
        this.click = click;
    }

    public Boolean isImpression() {
        return impression;
    }

    public void setImpression(Boolean impression) {
        this.impression = impression;
    }

    public static class Builder {

        private String user;
        private Long timestamp;
        private Boolean click;
        private Boolean impression;

        public static Builder anAnalytic() {
            return new Builder();
        }

        public Builder withUser(String user) {
            this.user = user;
            return this;
        }

        public Builder withTimestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder withClick() {
            this.click = true;
            return this;
        }

        public Builder withImpression() {
            this.impression = true;
            return this;
        }

        public Analytic build() {
            return new Analytic(this.user, this.timestamp, this.click, this.impression);
        }
    }
}
