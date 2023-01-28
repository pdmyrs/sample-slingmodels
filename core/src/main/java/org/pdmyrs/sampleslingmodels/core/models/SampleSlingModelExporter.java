package org.pdmyrs.sampleslingmodels.core.models;

import java.util.Optional;

/**
 * 
 * This class was copied from ACS Samples:
 * https://github.com/Adobe-Consulting-Services/acs-aem-samples/blob/909c4e100c2dc070b5fb2997777e305cbe2a5b9b/core/src/main/java/com/adobe/acs/samples/models/SampleSlingModelExporter.java
 * And modified.
 * 
 * Without the export annotations the .model.json still works but this model is never called.
 * 
 * Excellent documentation: http://sling.apache.org/documentation/bundles/models.html
 *
 *
 * ************************************************************************
 *
 *  The "adaptables" for a Sling Model is key element.
 *
 *  All the injected fields are looked up via a set of Injectors
 *  > http://sling.apache.org/documentation/bundles/models.html#available-injectors
 *  > Ensure you are using the latest Sling Model API and Impl bundles for access
 *    to the latest and greatest Injectors
 * 
 * 
 * 
 * 
 */

import javax.annotation.PostConstruct;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;

@Model(
        // This must adapt from a SlingHttpServletRequest, since this is invoked directly via a request, and not via a resource.
        // It can specify Resource.class as a second adaptable as needed
        adaptables = { SlingHttpServletRequest.class },
        // The resourceType is required if you want Sling to "naturally" expose this model as the exporter for a Resource.
        resourceType = "sample-slingmodels/components/sample-slingmodel",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
// name = the registered name of the exporter
// extensions = the extensions this exporter is registered to
// selector = defaults to "model", can override as needed; This is helpful if a single resource needs 2 different JSON renditions
@Exporter(name = "jackson", extensions = "json", options = {
    /**
     * Jackson options:
     * - Mapper Features: http://static.javadoc.io/com.fasterxml.jackson.core/jackson-databind/2.8.5/com/fasterxml/jackson/databind/MapperFeature.html
     * - Serialization Features: http://static.javadoc.io/com.fasterxml.jackson.core/jackson-databind/2.8.5/com/fasterxml/jackson/databind/SerializationFeature.html
     */
    @ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
    @ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value="false")
})
public class SampleSlingModelExporter {

    @Self
    private SlingHttpServletRequest request;

    @Self
    private Resource resource;


    @SlingObject
    private Resource currentResource;
    @SlingObject
    private ResourceResolver resourceResolver;

    private String message;

    @PostConstruct
    protected void init() {
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        String currentPagePath = Optional.ofNullable(pageManager)
                .map(pm -> pm.getContainingPage(resource))
                .map(Page::getPath).orElse("");

        message = "Sling Model Exporter says : \n"
            + "Current page is:  " + currentPagePath + "\n";
    }


    public String getMessage() {
        return message;
    }



   @ValueMapValue
   private String description;


    public String getDescription() {
        // return description;
        return "hello from my own exporter";
    }

    

}
