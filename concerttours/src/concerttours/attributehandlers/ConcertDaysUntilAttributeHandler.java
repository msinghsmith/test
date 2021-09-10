package concerttours.attributehandlers;

import concerttours.model.ConcertModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;


public class ConcertDaysUntilAttributeHandler extends AbstractDynamicAttributeHandler<Long, ConcertModel>{
    
    @Override
    public Long get(ConcertModel model) {
        if(model.getDate() != null) {
            final Date date = model.getDate();
            final ZonedDateTime concertDate = model.getDate().toInstant().atZone(ZoneId.systemDefault());
            final ZonedDateTime now = ZonedDateTime.now();
            
            if(concertDate.isBefore(now)){
                return Long.valueOf(0L);
            }
    
            return Duration.between(concertDate, now).toDays();
        }
        
        return null;
    
    }
}
