module confbuddy.web {
    requires polly.domain;
    requires java.logging;
    requires java.net.http;
    requires jakarta.cdi;
    requires jakarta.inject;
    requires jakarta.mvc;
    requires jakarta.validation;
    requires jakarta.ws.rs;
    requires mvc.toolbox.core;

    // Required because of https://github.com/eclipse-ee4j/krazo/issues/364 resp.
    // https://github.com/eclipse-ee4j/krazo/pull/365.
    requires krazo.core;
    requires jakarta.servlet;
    requires jakarta.annotation;
}
