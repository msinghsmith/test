package concerttours.facades.impl;

import concerttours.data.BandData;
import concerttours.data.TourSummaryData;
import concerttours.enums.MusicType;
import concerttours.facades.BandFacade;
import concerttours.model.BandModel;
import concerttours.service.BandService;
import de.hybris.platform.catalog.job.diff.impl.ProductApprovalStatusDiffFinder;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.media.MediaService;
import groovy.util.NodeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component(value = "bandFacade")
public class DefaultBandFacade implements BandFacade {
    
    private BandService bandService;
    private MediaService mediaService;
    @Override
    public BandData getBand(final String name) {
        
        if(name == null) {
            throw new IllegalArgumentException("Band cannot be null");
        }
        
        final BandModel bandModel = bandService.getBandForCode(name);
        
        if(bandModel == null) {
            return null;
        }
    
        List<String> genres = new ArrayList<>();
        final Collection<MusicType> musicTypes = bandModel.getTypes();
        if(musicTypes != null ) {
            for (MusicType musicType: musicTypes) {
                genres.add(musicType.getCode());
            }
        }
        
        List<TourSummaryData> tourSummaryDataList = new ArrayList<>();
        final Set<ProductModel> tours = bandModel.getTours();
        for (ProductModel tour:tours) {
            TourSummaryData tourSummaryData = new TourSummaryData();
            tourSummaryData.setId(tour.getCode());
            tourSummaryData.setTourName(tour.getName());
            tourSummaryData.setNumberOfConcerts(String.valueOf(tour.getVariants().size()));
            
            tourSummaryDataList.add(tourSummaryData);
        }
    
        final MediaFormatModel format = mediaService.getFormat("bandDetail");
        final BandData bandData = new BandData();
        bandData.setAlbumsSold(bandModel.getAlbumSales());
        bandData.setDescription(bandModel.getHistory());
        bandData.setId(bandModel.getCode());
        bandData.setName(bandModel.getName());
        bandData.setGenres(genres);
        bandData.setTours(tourSummaryDataList);
        bandData.setImageURL(getImageURL(bandModel, format));
        
        return bandData;
        
        
    }
    
    private String getImageURL(final BandModel bandModel, final MediaFormatModel format) {
    
        final MediaContainerModel mediaContainer = bandModel.getImage();
        
        if(mediaContainer != null)
            return mediaService.getMediaByFormat(mediaContainer, format).getDownloadURL();
        
        return null;
    }
    
    @Override
    public List<BandData> getBands() {
        final List<BandModel> bandmodels = bandService.getBands();
        final List<BandData> bandDataList = new ArrayList<>();
        final MediaFormatModel format = mediaService.getFormat("bandList");
        
        for (BandModel bandmodel:bandmodels) {
            BandData  bandData = new BandData();
            bandData.setId(bandmodel.getCode());
            bandData.setName(bandmodel.getName());
            bandData.setDescription(bandmodel.getHistory());
            bandData.setAlbumsSold(bandmodel.getAlbumSales());
            bandData.setImageURL(getImageURL(bandmodel, format));
            bandDataList.add(bandData);
        }
        
        return bandDataList;
    }
    
    public BandService getBandService() {
        return bandService;
    }
    
    @Autowired
    public void setBandService(final BandService bandService) {
        this.bandService = bandService;
    }
    
    public MediaService getMediaService() {
        return mediaService;
    }
    @Autowired
    public void setMediaService(final MediaService mediaService) {
        this.mediaService = mediaService;
    }
}
