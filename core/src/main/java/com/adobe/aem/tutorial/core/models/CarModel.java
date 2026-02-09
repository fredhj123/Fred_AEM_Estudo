package com.adobe.aem.tutorial.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import java.util.List; // Importação obrigatória para List

@Model(adaptables = Resource.class,
       adapters = CarModel.class, // Verifique se aqui não tem um "s" sobrando
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CarModel {

    @ValueMapValue
    private String brand;

    @ValueMapValue
    private String model;

    @ValueMapValue
    private Integer year;

    @ValueMapValue
    private String category;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private List<String> highlights;

    @ValueMapValue
    private List<String> interiorFeatures;

    @ValueMapValue
    private List<String> safetyFeatures;

    @ValueMapValue
    private String imagePath;
    
    @ValueMapValue
    private String badge;

    @ChildResource
    private Resource price;

    @ChildResource
    private Resource specifications;

    // --- Getters ---

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public Integer getYear() {
        return year;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getHighlights() {
        return highlights;
    }

    public List<String> getInteriorFeatures() {
        return interiorFeatures;
    }

    public List<String> getSafetyFeatures() {
        return safetyFeatures;
    }

    public Resource getPrice() {
        return price;
    }

    public Resource getSpecifications() {
        return specifications;
    }

    public String getImagePath() {
        return imagePath; 
    }

    public String getBadge() {
        return badge;
    }
}
