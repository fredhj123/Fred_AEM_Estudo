
package com.adobe.aem.tutorial.core.models;



import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.*;


import javax.inject.Inject;
import java.util.List;

import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;


@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class CustomCarouselModel {

    @ValueMapValue(name=PROPERTY_RESOURCE_TYPE, injectionStrategy=InjectionStrategy.OPTIONAL)
    @Default(values="No resourceType")
    protected String resourceType;

    @SlingObject
    SlingHttpServletRequest slingRequest;

    @ChildResource(name="slides")
    public List<CustomCarouselSlidesModel> slides;

    public List<CustomCarouselSlidesModel> getSlides() {
        return slides;
    }

    public void setSlides(List<CustomCarouselSlidesModel> slides) {
        this.slides = slides;
    }
}
