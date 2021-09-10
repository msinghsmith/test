package concerttours.service.impl;

import concerttours.daos.BandDAO;
import concerttours.model.BandModel;
import concerttours.service.BandService;
import de.hybris.platform.impex.jalo.imp.AmbiguousItemException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class DefaultBandService implements BandService {
    
    private BandDAO bandDAO;
    
    @Override
    public List<BandModel> getBands() {
        return bandDAO.findBands();
    }
    
    @Override
    public BandModel getBandForCode(final String code) {
        final List<BandModel> bands = bandDAO.findBandsByCode(code);
        
        if(bands.isEmpty()) {
            throw new UnknownIdentifierException("Band with code " + code + " not found");
        }
        else if(bands.size() > 1) {
            throw new AmbiguousIdentifierException("More than one bands found with code: " + code);
        }
        return bands.get(0);
    }
    
    public BandDAO getBandDAO() {
        return bandDAO;
    }
    
    @Required
    public void setBandDAO(final BandDAO bandDAO) {
        this.bandDAO = bandDAO;
    }
}
