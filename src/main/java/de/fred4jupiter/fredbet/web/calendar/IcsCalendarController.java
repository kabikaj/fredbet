package de.fred4jupiter.fredbet.web.calendar;

import de.fred4jupiter.fredbet.service.calendar.IcsCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/ics")
public class IcsCalendarController {

    private static final String CONTENT_TYPE = "text/calendar";

    @Autowired
    private IcsCalendarService icsCalendarService;

    @RequestMapping(value = "/{matchId}", method = RequestMethod.GET, produces = CONTENT_TYPE)
    public ResponseEntity<byte[]> downloadIcsCalendarFile(@PathVariable("matchId") Long matchId) {
        final String fileName = "Match.ics";
        byte[] templateFile = icsCalendarService.createCalendarEventFromMatch(matchId, LocaleContextHolder.getLocale());

        if (templateFile == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().header("Content-Type", CONTENT_TYPE)
                .header("Content-Disposition", "inline; filename=\"" + fileName + "\"").body(templateFile);
    }

}
