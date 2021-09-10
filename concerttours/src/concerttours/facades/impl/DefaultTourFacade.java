package concerttours.facades.impl;
import concerttours.jalo.Concert;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import concerttours.data.ConcertSummaryData;
import concerttours.data.TourData;
import concerttours.enums.ConcertType;
import concerttours.facades.TourFacade;
import concerttours.model.ConcertModel;
import org.springframework.stereotype.Component;

@Component(value = "tourFacade")
public class DefaultTourFacade implements TourFacade
{
    private ProductService productService;
    
    @Override
    public TourData getTourDetails(final String tourId)
    {
        if (tourId == null)
        {
            throw new IllegalArgumentException("Tour id cannot be null");
        }
        final ProductModel product = productService.getProductForCode(tourId);
        if (product == null)
        {
            return null;
        }
        // Create a list of ConcertSummaryData from the matches
        final List<ConcertSummaryData> concerts = new ArrayList<>();
        if (product.getVariants() != null)
        {
            for (final VariantProductModel variant : product.getVariants())
            {
                if (variant instanceof ConcertModel)
                {
                    final ConcertModel concert = (ConcertModel) variant;
                    final ConcertSummaryData summary = new ConcertSummaryData();
                    summary.setId(concert.getCode());
                    summary.setDate(concert.getDate());
                    summary.setVenue(concert.getVenue());
                    summary.setType(concert.getConcertType() == ConcertType.OPENAIR ? "Outdoors" : "Indoors");
                    summary.setCountdown(concert.getDaysLeft());
    
                    if(concert.getVariants() != null) {
                        List<ConcertModel> variantList = new ArrayList<>();
                        
                        for (VariantProductModel variantConcert : variant.getVariants()) {
                            if(variantConcert instanceof ConcertModel) {
                                final ConcertModel cm = (ConcertModel) variantConcert;
    
                                System.out.println(cm.getCode());
                                variantList.add(cm);
                            }
                        }
                        summary.setVariants(variantList);
                    }
                    
                    concerts.add(summary);
                }
            }
        }
        // Now we can create the TourData transfer object
        final TourData tourData = new TourData();
        tourData.setId(product.getCode());
        tourData.setTourName(product.getName());
        tourData.setDescription(product.getDescription());
        tourData.setConcerts(concerts);
        return tourData;
    }
    @Autowired
    public void setProductService(final ProductService productService)
    {
        this.productService = productService;
    }
}