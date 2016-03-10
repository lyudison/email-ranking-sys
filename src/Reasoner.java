import java.io.IOException;

import com.cyc.kb.BinaryPredicate;
import com.cyc.kb.BinaryPredicateFactory;
import com.cyc.kb.Context;
import com.cyc.kb.ContextFactory;
import com.cyc.kb.Fact;
import com.cyc.kb.FactFactory;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbCollectionFactory;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbIndividualFactory;
import com.cyc.kb.KbFactory;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.session.CycServerInfo;
import com.cyc.session.CycSession;
import com.cyc.session.CycSessionManager;
import com.cyc.session.SessionOptions;
import com.cyc.session.exception.SessionCommandException;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.exception.SessionInitializationException;
import com.cyc.session.spi.SessionManager;

public class Reasoner {

	private static Context universalVocabularyMt;
	private static Context universityDataMt;

	private static KbCollection assignmentObligation;
	private static KbCollection homework;
	private static KbCollection project;

	private static KbCollection event;
	private static KbCollection careerEvent;
	private static KbCollection careerFair;
	private static KbIndividual techExpo;

	private static KbCollection breakfast;
	private static KbCollection lunch;
	private static KbCollection meal;
	private static KbCollection foodComposite;
	private static KbCollection food;

	private static BinaryPredicate genls;
	
	public static String getEventType(String word) {
		
		// TODO: retrieve the type of event from OpenCyc
		// ...
		
		return "unknown";
	}

	public static void SessionManager() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException, SessionCommandException, KbTypeException, CreateException {

		try (SessionManager sessionMgr = CycSessionManager.getInstance()) {

			CreateEventIntoKB();

		} catch (IOException ioe) {

			ioe.printStackTrace(System.err);
			System.exit(1);

		} finally {

			System.exit(0);
		}
	}

	private static void CreateEventIntoKB() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException, SessionCommandException, KbTypeException, CreateException {

		try (CycSession session = CycSessionManager.getCurrentSession()) {

			ConfigureCurrentSession();

			SetupContexts();

			CreateEvent();

		} finally {

		}
	}

	private static void ConfigureCurrentSession()

			throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException,
			SessionCommandException, KbTypeException, CreateException {

		CycSession session = CycSessionManager.getCurrentSession();

		CycServerInfo serverInfo = session.getServerInfo();
		System.out.println("Current Cyc server: " + serverInfo.getCycServer());
		System.out.println("Cyc server release type: " + serverInfo.getSystemReleaseType());
		System.out.println("Cyc server revision number:" + serverInfo.getCycRevisionString());

		SessionOptions options = session.getOptions();

		options.setShouldTranscriptOperations(true);

		options.setCyclistName("CycAdministrator");
	}

	private static void SetupContexts()

			throws KbTypeException, CreateException, SessionConfigurationException, SessionCommunicationException,
			SessionInitializationException {

		universalVocabularyMt = ContextFactory.get("UniversalVocabularyMt");

		universityDataMt = ContextFactory.get("UniversityDataMt");

		CycSessionManager.getCurrentSession().getOptions()
				.setDefaultContext(ContextFactory.getDefaultContext(universityDataMt, ContextFactory.INFERENCE_PSC));
	}

	private static void CreateEvent() throws CreateException, KbTypeException {

		CreateAssignmentRelatedEvent();

		CreateCareerRelatedEvent();

		CreateFoodRelatedEvent();

	}

	private static void CreateAssignmentRelatedEvent()

			throws CreateException, KbTypeException {

		assignmentObligation = KbCollectionFactory.get("Assignment-Obligation");
		project = KbCollectionFactory.get("Factory");
		homework = KbCollectionFactory.findOrCreate("Homework");

		genls = BinaryPredicateFactory.get("genls");
		Fact projectIsObliged = FactFactory.findOrCreate(KbFactory.getSentence(genls, project, assignmentObligation),
				universityDataMt);
		Fact homeworkIsObliged = FactFactory.findOrCreate(KbFactory.getSentence(genls, homework, assignmentObligation),
				universityDataMt);

		// Add individual into homework or project collection
		// eecs340-project1=KbIndividualFactory.findOrCreate("EECS340-Project1");
		// eecs340-project1.instantiate(project,universityDataMt);
	}

	private static void CreateCareerRelatedEvent()

			throws CreateException, KbTypeException {

		event = KbCollectionFactory.get("Event");

		careerEvent = KbCollectionFactory.findOrCreate("CareerEvent");
		Fact careerEventisAnEvent = FactFactory.findOrCreate(KbFactory.getSentence(genls, careerEvent, event), universityDataMt);

		careerFair = KbCollectionFactory.findOrCreate("CareerFair");
		Fact careerFairisACareerEvent = FactFactory.findOrCreate(KbFactory.getSentence(genls, careerFair, careerEvent),
				universityDataMt);

		// Add individual into career fair collection
		techExpo = KbIndividualFactory.findOrCreate("TechExpo");
		techExpo.instantiates(careerFair, universityDataMt);
	}

	private static void CreateFoodRelatedEvent()

			throws CreateException, KbTypeException {

		breakfast = KbCollectionFactory.get("BreakFast");
		lunch = KbCollectionFactory.get("Lunch");
		meal = KbCollectionFactory.get("Meal");
		foodComposite = KbCollectionFactory.get("FoodComposite");
		food = KbCollectionFactory.get("Food");

	}

}
