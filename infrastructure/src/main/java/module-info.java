module polly.infrastructure {
    requires polly.domain;
    requires java.logging;
    requires jakarta.annotation;
    requires jakarta.cdi;
    requires jakarta.inject;
    requires jakarta.persistence;
    requires jakarta.transaction;
    requires flyway.core;
    requires org.mapstruct;
}
