package it.mulders.polly.web;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.mvc.MvcContext;
import org.eclipse.krazo.MvcContextImpl;


@ApplicationScoped
@Default
public class MvcToolkitHelper {

  @Produces
  @RequestScoped
  @Default
  public MvcContext produceMvcContext() {
    return new MvcContextImpl();
  }

}
