package it.mulders.polly.web.home;

import de.chkal.mvctoolbox.core.typesafe.ViewName;
import jakarta.enterprise.context.RequestScoped;
import jakarta.mvc.Controller;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Controller
@Path("/")
@RequestScoped
public class HomeController {
    public enum Views {
        @ViewName("home.jsp")
        HOME
    }

    @GET
    @Produces("text/html; charset=UTF-8")
    public Views show() {
        return Views.HOME;
    }
}
