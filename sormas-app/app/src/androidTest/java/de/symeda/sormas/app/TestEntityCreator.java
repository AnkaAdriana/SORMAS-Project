package de.symeda.sormas.app;

import android.provider.ContactsContract;

import java.util.Date;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.caze.InvestigationStatus;
import de.symeda.sormas.api.event.EventType;
import de.symeda.sormas.api.event.TypeOfPlace;
import de.symeda.sormas.api.sample.SampleMaterial;
import de.symeda.sormas.api.sample.SampleTestResultType;
import de.symeda.sormas.api.sample.SampleTestType;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.app.backend.caze.Case;
import de.symeda.sormas.app.backend.common.DaoException;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.backend.contact.Contact;
import de.symeda.sormas.app.backend.epidata.EpiDataBurial;
import de.symeda.sormas.app.backend.epidata.EpiDataGathering;
import de.symeda.sormas.app.backend.epidata.EpiDataTravel;
import de.symeda.sormas.app.backend.event.Event;
import de.symeda.sormas.app.backend.event.EventParticipant;
import de.symeda.sormas.app.backend.facility.Facility;
import de.symeda.sormas.app.backend.hospitalization.PreviousHospitalization;
import de.symeda.sormas.app.backend.location.Location;
import de.symeda.sormas.app.backend.person.Person;
import de.symeda.sormas.app.backend.region.Community;
import de.symeda.sormas.app.backend.region.District;
import de.symeda.sormas.app.backend.region.Region;
import de.symeda.sormas.app.backend.sample.Sample;
import de.symeda.sormas.app.backend.sample.SampleTest;
import de.symeda.sormas.app.backend.symptoms.Symptoms;
import de.symeda.sormas.app.backend.visit.Visit;

/**
 * Created by Mate Strysewske on 14.06.2017.
 */

public class TestEntityCreator {

    public static Person createPerson(String firstName, String lastName) {
        Person person = DatabaseHelper.getPersonDao().create();
        person.setFirstName(firstName);
        person.setLastName(lastName);

        try {
            DatabaseHelper.getPersonDao().saveAndSnapshot(person);
            DatabaseHelper.getPersonDao().accept(person);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        return DatabaseHelper.getPersonDao().queryForId(person.getId());
    }

    public static Case createCase() {
        Disease disease = Disease.EVD;
        Region region = DatabaseHelper.getRegionDao().queryUuid("UTJSQN-36GGNN-OBFACR-57LYSH7I");
        District district = DatabaseHelper.getDistrictDao().queryUuid("QL2H5V-M4SB23-VXBJ6L-GV6RKN4I");
        Community community = DatabaseHelper.getCommunityDao().queryUuid("XWOJNR-FLCW6Q-DHYWEL-XN6BCFMI");
        Facility facility = DatabaseHelper.getFacilityDao().queryUuid("XXYZW2-3PODTL-LMWOB6-ITYGSCCI");
        CaseClassification caseClassification = CaseClassification.SUSPECT;
        InvestigationStatus investigationStatus = InvestigationStatus.PENDING;

        Case caze = DatabaseHelper.getCaseDao().create(createPerson("Salomon", "Kalou"));
        caze.setDisease(disease);
        caze.setRegion(region);
        caze.setDistrict(district);
        caze.setCommunity(community);
        caze.setHealthFacility(facility);
        caze.setCaseClassification(caseClassification);
        caze.setInvestigationStatus(investigationStatus);

        try {
            DatabaseHelper.getCaseDao().saveAndSnapshot(caze);
            DatabaseHelper.getCaseDao().accept(caze);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        return DatabaseHelper.getCaseDao().queryForId(caze.getId());
    }

    public static Contact createContact() {
        Person person = createPerson("Thierry", "Henry");
        Case caze = createCase();

        Contact contact = DatabaseHelper.getContactDao().create();
        contact.setPerson(person);
        contact.setCaze(caze);

        try {
            DatabaseHelper.getContactDao().saveAndSnapshot(contact);
            DatabaseHelper.getContactDao().accept(contact);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        return DatabaseHelper.getContactDao().queryForId(contact.getId());
    }

    public static Event createEvent() {
        EventType eventType = EventType.RUMOR;
        String eventDesc = "FirstEventDescription";
        Date eventDate = DateHelper.subtractDays(new Date(), 2);
        TypeOfPlace typeOfPlace = TypeOfPlace.PUBLIC_PLACE;
        String srcFirstName = "Emil";
        String srcLastName = "Mpenza";
        String srcTelNo = "0150123123123";

        Event event = DatabaseHelper.getEventDao().create();
        event.setEventType(eventType);
        event.setEventDesc(eventDesc);
        event.setEventDate(eventDate);
        event.setTypeOfPlace(typeOfPlace);
        event.setSrcFirstName(srcFirstName);
        event.setSrcLastName(srcLastName);
        event.setSrcTelNo(srcTelNo);

        try {
            DatabaseHelper.getEventDao().saveAndSnapshot(event);
            DatabaseHelper.getEventDao().accept(event);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        return DatabaseHelper.getEventDao().queryForId(event.getId());
    }

    public static Sample createSample() {
        Case caze = createCase();
        Date sampleDateTime = DateHelper.subtractDays(new Date(), 1);
        Facility lab = DatabaseHelper.getFacilityDao().queryForAll().get(0);
        SampleMaterial material = SampleMaterial.BLOOD;

        Sample sample = DatabaseHelper.getSampleDao().create(caze);
        sample.setSampleDateTime(sampleDateTime);
        sample.setLab(lab);
        sample.setSampleMaterial(material);

        try {
            DatabaseHelper.getSampleDao().saveAndSnapshot(sample);
            DatabaseHelper.getSampleDao().accept(sample);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        return DatabaseHelper.getSampleDao().queryForId(sample.getId());
    }

    public static PreviousHospitalization createPreviousHospitalization(Case caze) {
        PreviousHospitalization prevHosp = DatabaseHelper.getPreviousHospitalizationDao().create();
        prevHosp.setHospitalization(caze.getHospitalization());

        try {
            DatabaseHelper.getPreviousHospitalizationDao().saveAndSnapshot(prevHosp);
            DatabaseHelper.getPreviousHospitalizationDao().accept(prevHosp);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        return DatabaseHelper.getPreviousHospitalizationDao().queryForId(prevHosp.getId());
    }

    public static EpiDataBurial createEpiDataBurial(Case caze) {
        EpiDataBurial burial = DatabaseHelper.getEpiDataBurialDao().create();
        burial.setEpiData(caze.getEpiData());

        try {
            DatabaseHelper.getEpiDataBurialDao().saveAndSnapshot(burial);
            DatabaseHelper.getEpiDataBurialDao().accept(burial);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        return DatabaseHelper.getEpiDataBurialDao().queryForId(burial.getId());
    }

    public static EpiDataGathering createEpiDataGathering(Case caze) {
        EpiDataGathering gathering = DatabaseHelper.getEpiDataGatheringDao().create();
        gathering.setEpiData(caze.getEpiData());

        try {
            DatabaseHelper.getEpiDataGatheringDao().saveAndSnapshot(gathering);
            DatabaseHelper.getEpiDataGatheringDao().accept(gathering);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        return DatabaseHelper.getEpiDataGatheringDao().queryForId(gathering.getId());
    }

    public static EpiDataTravel createEpiDataTravel(Case caze) {
        EpiDataTravel travel = DatabaseHelper.getEpiDataTravelDao().create();
        travel.setEpiData(caze.getEpiData());

        try {
            DatabaseHelper.getEpiDataTravelDao().saveAndSnapshot(travel);
            DatabaseHelper.getEpiDataTravelDao().accept(travel);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        return DatabaseHelper.getEpiDataTravelDao().queryForId(travel.getId());
    }

    public static Visit createVisit(Contact contact) {
        Visit visit = DatabaseHelper.getVisitDao().create(contact.getUuid());
        Symptoms symptoms = DatabaseHelper.getSymptomsDao().create();
        Location illLocation = DatabaseHelper.getLocationDao().create();
        symptoms.setIllLocation(illLocation);
        visit.setSymptoms(symptoms);
        visit.setVisitUser(ConfigProvider.getUser());

        try {
            DatabaseHelper.getVisitDao().saveAndSnapshot(visit);
            DatabaseHelper.getVisitDao().accept(visit);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        return DatabaseHelper.getVisitDao().queryForId(visit.getId());
    }

    public static EventParticipant createEventParticipant(Event event) {
        Person person = createPerson("Demba", "Ba");

        EventParticipant eventParticipant = DatabaseHelper.getEventParticipantDao().create();
        eventParticipant.setEvent(event);
        eventParticipant.setPerson(person);

        try {
            DatabaseHelper.getEventParticipantDao().saveAndSnapshot(eventParticipant);
            DatabaseHelper.getEventParticipantDao().accept(eventParticipant);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        return DatabaseHelper.getEventParticipantDao().queryForId(eventParticipant.getId());
    }

    public static SampleTest createSampleTest(Sample sample) {
        SampleTestType sampleTestType = SampleTestType.RAPID_TEST;
        SampleTestResultType sampleTestResultType = SampleTestResultType.NEGATIVE;
        Date sampleTestDateTime = new Date();

        SampleTest sampleTest = DatabaseHelper.getSampleTestDao().create();
        sampleTest.setSample(sample);
        sampleTest.setTestType(sampleTestType);
        sampleTest.setTestResult(sampleTestResultType);
        sampleTest.setTestDateTime(sampleTestDateTime);

        try {
            DatabaseHelper.getSampleTestDao().saveAndSnapshot(sampleTest);
            DatabaseHelper.getSampleTestDao().accept(sampleTest);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        return DatabaseHelper.getSampleTestDao().queryForId(sampleTest.getId());
    }

}
