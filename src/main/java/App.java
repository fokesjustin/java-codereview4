import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("bands", Band.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
     
    get("/bands/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/band_form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/venues/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/venue_form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/band/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String bandName = request.queryParams("name");
      String genre = request.queryParams("genre");
      String homeTown = request.queryParams("homeTown");
      Band newBand = new Band(bandName, genre, homeTown);
      newBand.save();
      model.put("band", newBand);
      model.put("bands", Band.all());
      response.redirect("/band/" + newBand.getId());
      return null;
      });

    post("/venue/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String venueName = request.queryParams("name");
      String phone = request.queryParams("phone");
      String location = request.queryParams("location");
      Venue newVenue = new Venue(venueName, phone, location);
      newVenue.save();
      model.put("venue", newVenue);
      model.put("venues", Venue.all());
      response.redirect("/venue/" + newVenue.getId());
      return null;
      });

    get("/bands", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/bands.vtl");
      model.put("bands", Band.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/venues", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/venues.vtl");
      model.put("venues", Venue.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/band/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Band newBand = Band.find(Integer.parseInt(request.params(":id")));
      model.put("thisBand", newBand);
      model.put("bands", Band.all());
      model.put("venues", Venue.all());
      model.put("template", "templates/band.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/venue/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Venue newVenue = Venue.find(Integer.parseInt(request.params(":id")));
      model.put("thisVenue", newVenue);
      model.put("bands", Band.all());
      model.put("venues", Venue.all());
      model.put("template", "templates/venue.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/band/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Band newBand = Band.find(Integer.parseInt(request.params(":id")));
      Venue thisVenue = Venue.find(Integer.parseInt(request.queryParams("addedVenue")));
      newBand.addVenue(thisVenue);
      model.put("thisBand", newBand.getName());
      model.put("bands", Band.all());
      model.put("venues", Venue.all());
   	  response.redirect("/band/" + newBand.getId());
      return null;
      });

    post("/venue/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Venue newVenue = Venue.find(Integer.parseInt(request.params(":id")));
      Band thisBand = Band.find(Integer.parseInt(request.queryParams("addedBand")));
      newVenue.addBand(thisBand);
      model.put("thisVenue", newVenue.getName());
      model.put("bands", Band.all());
      model.put("venues", Venue.all());
      response.redirect("/venue/" + newVenue.getId());
      return null;
      });

    post("/band/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Band newBand = Band.find(Integer.parseInt(request.params(":id")));
      String newName = request.queryParams("newName");
      String newGenre = request.queryParams("newGenre");
      String newHomeTown = request.queryParams("newHomeTown");
      newBand.update(newName, newGenre, newHomeTown);
      model.put("thisBand", newBand);
      model.put("bands", Band.all());
      model.put("venues", Venue.all());
      response.redirect("/band/" + newBand.getId());
      return null;
      });

     get("/band/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Band newBand = Band.find(Integer.parseInt(request.params(":id")));
      newBand.delete();
      response.redirect("/bands");
      model.put("template", "templates/bands.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
