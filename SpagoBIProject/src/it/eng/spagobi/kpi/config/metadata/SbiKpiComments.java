package it.eng.spagobi.kpi.config.metadata;

// Generated 5-dic-2011 15.03.38 by Hibernate Tools 3.2.4.GA

import java.util.Date;
import it.eng.spagobi.commons.metadata.SbiBinContents;
import it.eng.spagobi.commons.metadata.SbiHibernateModel;

/**
 * SbiKpiComments generated by hbm2java
 */
public class SbiKpiComments  extends SbiHibernateModel {

	private Integer kpiCommentId;
	private SbiBinContents sbiBinContents;
	private SbiKpiInstance sbiKpiInstance;
	private String execReq;
	private String owner;
	private Boolean ispublic;
	private Date creationDate;
	private Date lastChangeDate;
	private String userIn;
	private String userUp;
	private String userDe;
	private Date timeIn;
	private Date timeUp;
	private Date timeDe;
	private String sbiVersionIn;
	private String sbiVersionUp;
	private String sbiVersionDe;
	private String metaVersion;
	private String organization;

	public SbiKpiComments() {
	}

	public SbiKpiComments(Date creationDate, Date lastChangeDate,
			String userIn, Date timeIn) {
		this.creationDate = creationDate;
		this.lastChangeDate = lastChangeDate;
		this.userIn = userIn;
		this.timeIn = timeIn;
	}

	public SbiKpiComments(SbiBinContents sbiBinContents,
			SbiKpiInstance sbiKpiInstance, String execReq, String owner,
			Boolean ispublic, Date creationDate, Date lastChangeDate,
			String userIn, String userUp, String userDe, Date timeIn,
			Date timeUp, Date timeDe, String sbiVersionIn, String sbiVersionUp,
			String sbiVersionDe, String metaVersion, String organization) {
		this.sbiBinContents = sbiBinContents;
		this.sbiKpiInstance = sbiKpiInstance;
		this.execReq = execReq;
		this.owner = owner;
		this.ispublic = ispublic;
		this.creationDate = creationDate;
		this.lastChangeDate = lastChangeDate;
		this.userIn = userIn;
		this.userUp = userUp;
		this.userDe = userDe;
		this.timeIn = timeIn;
		this.timeUp = timeUp;
		this.timeDe = timeDe;
		this.sbiVersionIn = sbiVersionIn;
		this.sbiVersionUp = sbiVersionUp;
		this.sbiVersionDe = sbiVersionDe;
		this.metaVersion = metaVersion;
		this.organization = organization;
	}

	public SbiBinContents getSbiBinContents() {
		return sbiBinContents;
	}

	public void setSbiBinContents(SbiBinContents sbiBinContents) {
		this.sbiBinContents = sbiBinContents;
	}

	public Integer getKpiCommentId() {
		return this.kpiCommentId;
	}

	public void setKpiCommentId(Integer kpiCommentId) {
		this.kpiCommentId = kpiCommentId;
	}



	public SbiKpiInstance getSbiKpiInstance() {
		return this.sbiKpiInstance;
	}

	public void setSbiKpiInstance(SbiKpiInstance sbiKpiInstance) {
		this.sbiKpiInstance = sbiKpiInstance;
	}

	public String getExecReq() {
		return this.execReq;
	}

	public void setExecReq(String execReq) {
		this.execReq = execReq;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Boolean getIspublic() {
		return this.ispublic;
	}

	public void setIspublic(Boolean ispublic) {
		this.ispublic = ispublic;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastChangeDate() {
		return this.lastChangeDate;
	}

	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}

	public String getUserIn() {
		return this.userIn;
	}

	public void setUserIn(String userIn) {
		this.userIn = userIn;
	}

	public String getUserUp() {
		return this.userUp;
	}

	public void setUserUp(String userUp) {
		this.userUp = userUp;
	}

	public String getUserDe() {
		return this.userDe;
	}

	public void setUserDe(String userDe) {
		this.userDe = userDe;
	}

	public Date getTimeIn() {
		return this.timeIn;
	}

	public void setTimeIn(Date timeIn) {
		this.timeIn = timeIn;
	}

	public Date getTimeUp() {
		return this.timeUp;
	}

	public void setTimeUp(Date timeUp) {
		this.timeUp = timeUp;
	}

	public Date getTimeDe() {
		return this.timeDe;
	}

	public void setTimeDe(Date timeDe) {
		this.timeDe = timeDe;
	}

	public String getSbiVersionIn() {
		return this.sbiVersionIn;
	}

	public void setSbiVersionIn(String sbiVersionIn) {
		this.sbiVersionIn = sbiVersionIn;
	}

	public String getSbiVersionUp() {
		return this.sbiVersionUp;
	}

	public void setSbiVersionUp(String sbiVersionUp) {
		this.sbiVersionUp = sbiVersionUp;
	}

	public String getSbiVersionDe() {
		return this.sbiVersionDe;
	}

	public void setSbiVersionDe(String sbiVersionDe) {
		this.sbiVersionDe = sbiVersionDe;
	}

	public String getMetaVersion() {
		return this.metaVersion;
	}

	public void setMetaVersion(String metaVersion) {
		this.metaVersion = metaVersion;
	}

	public String getOrganization() {
		return this.organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

}
