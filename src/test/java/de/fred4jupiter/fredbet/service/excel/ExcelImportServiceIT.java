package de.fred4jupiter.fredbet.service.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.fred4jupiter.fredbet.AbstractIntegrationTest;
import de.fred4jupiter.fredbet.domain.Country;
import de.fred4jupiter.fredbet.domain.Group;
import de.fred4jupiter.fredbet.domain.Match;
import de.fred4jupiter.fredbet.util.DateUtils;

public class ExcelImportServiceIT extends AbstractIntegrationTest {

	@Autowired
	private ExcelImportService excelImportService;

	@Test
	public void readExcelFileWithMatchesToImport() {
		List<Match> matches = excelImportService.importFromExcel(new File("src/test/resources/excelimport/MatchesImportTemplate.xlsx"));
		assertNotNull(matches);
		assertFalse(matches.isEmpty());
		assertEquals(3, matches.size());

		Match match1 = matches.get(0);
		assertEquals(Country.GERMANY, match1.getCountryOne());
		assertEquals(Country.BELGIUM, match1.getCountryTwo());
		assertEquals(Group.GROUP_B, match1.getGroup());
		assertEquals(DateUtils.parseDate("01.06.2017 18:00", "dd.MM.yyyy HH:mm"), match1.getKickOffDate());
		assertEquals("Westfalenstadion, Dortmund", match1.getStadium());

		Match match2 = matches.get(1);
		assertEquals(Country.ANDORRA, match2.getCountryOne());
		assertEquals(Country.ISRAEL, match2.getCountryTwo());
		assertEquals(Group.GROUP_E, match2.getGroup());
		assertEquals(DateUtils.parseDate("05.02.2018 20:00", "dd.MM.yyyy HH:mm"), match2.getKickOffDate());
		assertEquals("Allianz Arena, München", match2.getStadium());
		
		Match match3 = matches.get(2);
		assertEquals("Sieger Gruppe A", match3.getTeamNameOne());
		assertEquals("Zweiter Gruppe B", match3.getTeamNameTwo());
		assertEquals(Group.SEMI_FINAL, match3.getGroup());
		assertEquals(DateUtils.parseDate("28.06.2017 20:00", "dd.MM.yyyy HH:mm"), match3.getKickOffDate());
		assertEquals("Kasan", match3.getStadium());
	}

}