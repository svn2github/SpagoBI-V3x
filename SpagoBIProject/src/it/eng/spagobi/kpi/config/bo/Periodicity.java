/*
 * SpagoBI, the Open Source Business Intelligence suite
 * � 2005-2015 Engineering Group
 *
 * This file is part of SpagoBI. SpagoBI is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 2.1 of the License, or any later version. 
 * SpagoBI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have received
 * a copy of the GNU Lesser General Public License along with SpagoBI. If not, see: http://www.gnu.org/licenses/.
 * The complete text of SpagoBI license is included in the COPYING.LESSER file. 
 */
package it.eng.spagobi.kpi.config.bo;

import java.io.Serializable;

public class Periodicity implements Serializable{
	
    private Integer idKpiPeriodicity;
    private String name;
    private Integer months;
    private Integer days;
    private Integer hours;
    private Integer minutes;
    private String cronString; 
   
	public Periodicity(Integer idKpiPeriodicity, String name, Integer months,
			Integer days, Integer hours, Integer minutes, String period) {
		super();
		this.idKpiPeriodicity = idKpiPeriodicity;
		this.name = name;
		this.months = months;
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
		this.cronString = period;
	}
	
	public Periodicity(){
		
	}

	public Integer getIdKpiPeriodicity() {
		return idKpiPeriodicity;
	}

	public void setIdKpiPeriodicity(Integer idKpiPeriodicity) {
		this.idKpiPeriodicity = idKpiPeriodicity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMonths() {
		return months;
	}

	public void setMonths(Integer months) {
		this.months = months;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Integer getHours() {
		return hours;
	}

	public void setHours(Integer hours) {
		this.hours = hours;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	public String getCronString() {
		return cronString;
	}

	public void setCronString(String cronString) {
		this.cronString = cronString;
	}
   
}
