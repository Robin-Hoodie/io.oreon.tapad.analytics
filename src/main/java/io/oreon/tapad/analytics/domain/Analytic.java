package io.oreon.tapad.analytics.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.io.Serializable;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class Analytic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String user;
    private Long timestamp;
    private boolean click;
    private boolean impression;

    //Used by Jackson
    private Analytic() {}

    private Analytic(String user, Long timestamp, boolean click, boolean impression) {
        this.user = user;
        this.timestamp = timestamp;
        this.click = click;
        this.impression = impression;
    }

    public String getUser() {
        return user;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Boolean isClick() {
        return click;
    }

    public Boolean isImpression() {
        return impression;
    }

    public static class Builder {

        private String user;
        private Long timestamp;
        private boolean click;
        private boolean impression;

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

        public Builder withClick(boolean click) {
            this.click = click;
            return this;
        }

        public Builder withImpression(boolean impression) {
            this.impression = impression;
            return this;
        }

        public Analytic build() {
            return new Analytic(this.user, this.timestamp, this.click, this.impression);
        }
    }
}
