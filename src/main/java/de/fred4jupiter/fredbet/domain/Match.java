package de.fred4jupiter.fredbet.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import de.fred4jupiter.fredbet.props.FredbetConstants;

@Entity
@Table(name = "MATCHES")
public class Match {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MATCH_ID")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "COUNTRY_ONE")
	private Country countryOne;

	@Column(name = "TEAM_NAME_ONE")
	private String teamNameOne;

	@Enumerated(EnumType.STRING)
	@Column(name = "COUNTRY_TWO")
	private Country countryTwo;

	@Column(name = "TEAM_NAME_TWO")
	private String teamNameTwo;

	@Enumerated(EnumType.STRING)
	@Column(name = "MATCH_GROUP")
	private Group group;

	@Column(name = "GOALS_TEAM_ONE")
	private Integer goalsTeamOne;

	@Column(name = "GOALS_TEAM_TWO")
	private Integer goalsTeamTwo;

	@Column(name = "PENALTY_WINNER_ONE")
	private boolean penaltyWinnerOne;

	@Transient
	private boolean goalsChanged;

	@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
	@Column(name = "KICK_OFF_DATE")
	private LocalDateTime kickOffDate;

	@Column(name = "STADIUM")
	private String stadium;

	public boolean hasGoalsChanged() {
		boolean hasChanged = goalsChanged;
		// reset flag
		this.goalsChanged = false;
		return hasChanged;
	}

	public boolean hasContriesSet() {
		return hasContrySet(countryOne) && hasContrySet(countryTwo);
	}

	private boolean hasContrySet(Country country) {
		return country != null && !Country.NONE.equals(country);
	}

	public boolean hasResultSet() {
		return goalsTeamOne != null && goalsTeamTwo != null;
	}

	public boolean hasStarted() {
		return LocalDateTime.now().isAfter(kickOffDate);
	}

	public void enterResult(Integer goalsTeamOne, Integer goalsTeamTwo) {
		this.goalsTeamOne = goalsTeamOne;
		this.goalsTeamTwo = goalsTeamTwo;
		this.goalsChanged = true;
	}

	public Integer getGoalDifference() {
		if (goalsTeamOne == null || goalsTeamTwo == null) {
			throw new IllegalStateException("Match has not finished! No goal results set!");
		}
		return Math.abs(goalsTeamOne - goalsTeamTwo);
	}

	/**
	 * Goal difference is 0, e.g. 1:1 or 3:3
	 * 
	 * @return
	 */
	public boolean isUndecidedResult() {
		if (goalsTeamOne == null || goalsTeamTwo == null) {
			return false;
		}
		return getGoalDifference() == 0;
	}

	public boolean isTeamOneWinner() {
		if (goalsTeamOne == null || goalsTeamTwo == null) {
			throw new IllegalStateException("Match has not finished! No goal results set!");
		}

		return goalsTeamOne > goalsTeamTwo;
	}

	public boolean isTeamTwoWinner() {
		if (goalsTeamOne == null || goalsTeamTwo == null) {
			throw new IllegalStateException("Match has not finished! No goal results set!");
		}

		return goalsTeamTwo > goalsTeamOne;
	}

	public Country getWinner() {
		if (!hasResultSet()) {
			return null;
		}

		if (isTeamOneWinner()) {
			return countryOne;
		}

		if (isTeamTwoWinner()) {
			return countryTwo;
		}

		return null;
	}

	public Country getLooser() {
		if (!hasResultSet()) {
			return null;
		}

		if (isTeamOneWinner()) {
			return countryTwo;
		}

		if (isTeamTwoWinner()) {
			return countryOne;
		}

		return null;
	}

	public Integer getGoalsTeamOne() {
		return goalsTeamOne;
	}

	public void setGoalsTeamOne(Integer goalsTeamOne) {
		this.goalsTeamOne = goalsTeamOne;
		this.goalsChanged = true;
	}

	public Integer getGoalsTeamTwo() {
		return goalsTeamTwo;
	}

	public void setGoalsTeamTwo(Integer goalsTeamTwo) {
		this.goalsTeamTwo = goalsTeamTwo;
		this.goalsChanged = true;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Match match = (Match) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(id, match.id);
		builder.append(countryOne, match.countryOne);
		builder.append(countryTwo, match.countryTwo);
		builder.append(teamNameOne, match.teamNameOne);
		builder.append(teamNameTwo, match.teamNameTwo);
		builder.append(group, match.group);
		builder.append(goalsTeamOne, match.goalsTeamOne);
		builder.append(goalsTeamTwo, match.goalsTeamTwo);
		builder.append(kickOffDate, match.kickOffDate);
		builder.append(stadium, match.stadium);

		return builder.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(id);
		builder.append(countryOne);
		builder.append(countryTwo);
		builder.append(teamNameOne);
		builder.append(teamNameTwo);
		builder.append(group);
		builder.append(goalsTeamOne);
		builder.append(goalsTeamTwo);
		builder.append(kickOffDate);
		builder.append(stadium);
		return builder.toHashCode();
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
		builder.append("id", id);
		builder.append("countryOne", countryOne);
		builder.append("countryTwo", countryTwo);
		builder.append("teamNameOne", teamNameOne);
		builder.append("teamNameTwo", teamNameTwo);
		builder.append("group", group);
		builder.append("goalsTeamOne", goalsTeamOne);
		builder.append("goalsTeamTwo", goalsTeamTwo);
		builder.append("kickOffDate", kickOffDate);
		builder.append("stadium", stadium);
		return builder.toString();
	}

	public Long getId() {
		return id;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getStadium() {
		return stadium;
	}

	public void setStadium(String stadium) {
		this.stadium = stadium;
	}

	public LocalDateTime getKickOffDate() {
		return kickOffDate;
	}

	public void setKickOffDate(LocalDateTime kickOffDate) {
		this.kickOffDate = kickOffDate;
	}

	public boolean isBettable() {
		return !hasStarted() && !hasResultSet();
	}

	public String getTeamNameOne() {
		return teamNameOne;
	}

	public void setTeamNameOne(String teamNameOne) {
		this.teamNameOne = teamNameOne;
	}

	public String getTeamNameTwo() {
		return teamNameTwo;
	}

	public void setTeamNameTwo(String teamNameTwo) {
		this.teamNameTwo = teamNameTwo;
	}

	public Country getCountryOne() {
		return countryOne;
	}

	public void setCountryOne(Country countryOne) {
		this.countryOne = countryOne;
	}

	public Country getCountryTwo() {
		return countryTwo;
	}

	public void setCountryTwo(Country countryTwo) {
		this.countryTwo = countryTwo;
	}

	public boolean isFinal() {
		return isGroup(Group.FINAL);
	}

	public boolean isPenaltyWinnerOne() {
		return penaltyWinnerOne;
	}

	public void setPenaltyWinnerOne(boolean penaltyWinnerOne) {
		this.penaltyWinnerOne = penaltyWinnerOne;
	}

	public boolean isGroupMatch() {
		return this.group.name().startsWith("GROUP");
	}

	public String getCssClassPenaltyWinnerOne() {
		if (this.isGroupMatch() || !this.isUndecidedResult()) {
			return "";
		}
		return this.isPenaltyWinnerOne() ? FredbetConstants.BADGE_PENALTY_WINNER_MATCH_CSS_CLASS : "";
	}

	public String getCssClassPenaltyWinnerTwo() {
		if (this.isGroupMatch() || !this.isUndecidedResult()) {
			return "";
		}
		return !this.isPenaltyWinnerOne() ? FredbetConstants.BADGE_PENALTY_WINNER_MATCH_CSS_CLASS : "";
	}

	public boolean isGroup(Group group) {
		return this.group.equals(group);
	}
}
