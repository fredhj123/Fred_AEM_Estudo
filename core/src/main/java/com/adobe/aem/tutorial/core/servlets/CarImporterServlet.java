package com.adobe.aem.tutorial.core.servlets;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.Servlet;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/importCars")
public class CarImporterServlet extends SlingAllMethodsServlet {

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        ResourceResolver resolver = request.getResourceResolver();
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        
        String jsonPath = "/content/practice/data/cars.json";
        String parentPath = "/content/practice/us/en";
        String template = "/conf/practice/settings/wcm/templates/page-content";
        
        Resource jsonResource = resolver.getResource(jsonPath);
        if (jsonResource == null) {
            response.getWriter().write("ERRO: JSON nao encontrado");
            return;
        }

        try (InputStream is = jsonResource.adaptTo(InputStream.class);
             JsonReader reader = Json.createReader(is)) {

            JsonObject jsonObject = reader.readObject();
            JsonArray cars = jsonObject.getJsonArray("models");
            int count = 0;

            for (JsonObject car : cars.getValuesAs(JsonObject.class)) {
                String pageName = car.getString("id"); 
                String title = car.getString("brand") + " " + car.getString("model");

                if (resolver.getResource(parentPath + "/" + pageName) == null) {
                    Page newPage = pageManager.create(parentPath, pageName, template, title);
                    if (newPage != null) {
                        addCarComponent(newPage, car, resolver);
                        count++;
                    }
                }
            }
            resolver.commit();
            response.getWriter().write("SUCESSO: Foram importados " + count + " carros.");

        } catch (WCMException e) {
            response.getWriter().write("ERRO: " + e.getMessage());
        }
    }

    private void addCarComponent(Page page, JsonObject car, ResourceResolver resolver) {
        String containerPath = page.getPath() + "/jcr:content/root/container";
        Resource container = resolver.getResource(containerPath);
        
        if (container != null) {
            Map<String, Object> props = new HashMap<>();
            props.put("jcr:primaryType", "nt:unstructured");
            props.put("sling:resourceType", "practice/components/car-card");
            
            // Dados Básicos
            props.put("brand", car.getString("brand"));
            props.put("model", car.getString("model"));
            props.put("year", car.getInt("year"));
            props.put("category", car.getString("category"));
            props.put("description", car.getString("description"));
            
            // Lógica de Imagem (Captura Segura)
            String imageValue = "/content/dam/practice/default-car.jpg";
            if (car.containsKey("image")) {
                imageValue = car.getString("image");
            } else if (car.containsKey("imagePath")) {
                imageValue = car.getString("imagePath");
            }
            props.put("imagePath", imageValue);

            // Preço
            if (car.containsKey("price")) {
                props.put("priceBase", car.getJsonObject("price").getInt("base"));
            }

            // Especificações Técnicas
            if (car.containsKey("specifications")) {
                JsonObject specs = car.getJsonObject("specifications");
                
                if (specs.containsKey("engine")) {
                    JsonObject eng = specs.getJsonObject("engine");
                    props.put("engineType", eng.getString("type"));
                    props.put("horsepower", eng.getInt("horsepower"));
                    props.put("torque", eng.getInt("torqueNm"));
                    props.put("fuelType", eng.getString("fuelType"));
                }
                
                if (specs.containsKey("performance")) {
                    JsonObject perf = specs.getJsonObject("performance");
                    props.put("topSpeed", perf.getInt("topSpeedKmh"));
                    props.put("acceleration", perf.getJsonNumber("acceleration0To100").doubleValue());
                    props.put("driveType", perf.getString("driveType"));
                }

                if (specs.containsKey("transmission")) {
                    props.put("transType", specs.getJsonObject("transmission").getString("type"));
                    props.put("transGears", specs.getJsonObject("transmission").getInt("gears"));
                }
            }

            // Listas (Highlights, Interior, Segurança)
            props.put("highlights", getJsonArrayAsArray(car, "highlights"));
            props.put("interiorFeatures", getJsonArrayAsArray(car, "interiorFeatures"));
            props.put("safetyFeatures", getJsonArrayAsArray(car, "safetyFeatures"));
            
            try {
                resolver.create(container, "car_card", props);
            } catch (Exception e) {
                // Erro silenciado para o JCR
            }
        }
    }

    private String[] getJsonArrayAsArray(JsonObject car, String key) {
        if (car.containsKey(key)) {
            JsonArray jsonArray = car.getJsonArray(key);
            String[] array = new String[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                array[i] = jsonArray.getString(i);
            }
            return array;
        }
        return new String[0];
    }
}