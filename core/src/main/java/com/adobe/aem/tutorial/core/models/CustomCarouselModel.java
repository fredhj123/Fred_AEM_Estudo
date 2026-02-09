package com.adobe.aem.tutorial.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class)
public class CustomCarouselModel {

    @ChildResource
    private Resource slides;

    private List<SlideItem> slidesList = new ArrayList<>();

    @PostConstruct
    protected void init() {
        if (slides != null) {
            int count = 0;
            for (Resource slide : slides.getChildren()) {
                if (count >= 3) break;

                ValueMap vm = slide.getValueMap();
                String imgPath = vm.get("imagePath", String.class);

                // Tratamento da imagem
                if (imgPath != null && !imgPath.startsWith("/")) {
                    imgPath = "/" + imgPath;
                }

                // Mapeamento exato para o HTL
                slidesList.add(new SlideItem(
                    vm.get("title", String.class),
                    vm.get("text", String.class),      // No HTL será .subtitle
                    imgPath,
                    vm.get("btnLabel", String.class),  // No HTL será .buttonLabel
                    vm.get("btnLink", String.class)    // No HTL será .pagePath
                ));
                
                count++;
            }
        }
    }

    // O HTL chama ${customCarousel.slides}, então o método DEVE ser getSlides
    public List<SlideItem> getSlides() {
        return slidesList;
    }

    public boolean isEmpty() {
        return slidesList.isEmpty();
    }

    /**
     * Classe interna ajustada para os nomes do HTML
     */
    public static class SlideItem {
        private final String title;
        private final String subtitle;
        private final String imagePath;
        private final String buttonLabel;
        private final String pagePath;

        public SlideItem(String title, String subtitle, String imagePath, String buttonLabel, String pagePath) {
            this.title = title;
            this.subtitle = subtitle;
            this.imagePath = imagePath;
            this.buttonLabel = buttonLabel;
            this.pagePath = pagePath;
        }

        public String getTitle() { return title; }
        
        // No Java pegamos o valor de "text", mas entregamos como "subtitle"
        public String getSubtitle() { return subtitle; }
        
        public String getImagePath() { return imagePath; }
        
        public String getButtonLabel() { return buttonLabel; }
        
        public String getPagePath() { return pagePath; }
    }
}