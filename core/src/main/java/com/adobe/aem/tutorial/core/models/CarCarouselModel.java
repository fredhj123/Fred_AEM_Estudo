package com.adobe.aem.tutorial.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import org.apache.sling.models.annotations.injectorspecific.*;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CarCarouselModel {

    // Injeta o multifield 'slides' definido no XML do diálogo
    @ChildResource
    private Resource slides;

    private List<CarItem> items = new ArrayList<>();

    @PostConstruct
    protected void init() {
        if (slides != null) {
            int count = 0;
            for (Resource slide : slides.getChildren()) {
                // Regra de negócio: Máximo de 3 slides
                if (count >= 3) break;

                ValueMap vm = slide.getValueMap();
                String imgPath = vm.get("imagePath", String.class);

                // Normalização do caminho da imagem
                if (imgPath != null && !imgPath.startsWith("/")) {
                    imgPath = "/" + imgPath;
                }

                items.add(new CarItem(
                    vm.get("title", "A4X Motors"),
                    vm.get("text", "Conheça nossos modelos"),
                    imgPath,
                    vm.get("btnLabel", "Explorar"),
                    vm.get("btnLink", "#")
                ));
                
                count++;
            }
        }
    }

    public List<CarItem> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Classe interna para representar cada slide do carro
     */
    public static class CarItem {
        private final String title;
        private final String text;
        private final String imagePath;
        private final String btnLabel;
        private final String btnLink;

        public CarItem(String title, String text, String imagePath, String btnLabel, String btnLink) {
            this.title = title;
            this.text = text;
            this.imagePath = imagePath;
            this.btnLabel = btnLabel;
            this.btnLink = btnLink;
        }

        public String getTitle() { return title; }
        public String getText() { return text; }
        public String getImagePath() { return imagePath; }
        public String getBtnLabel() { return btnLabel; }
        public String getBtnLink() { return btnLink; }
    }
}